package com.example.debtcontrolbe.service;

import com.example.debtcontrolbe.model.Company;
import com.example.debtcontrolbe.model.Debt;
import com.example.debtcontrolbe.model.reponse.HistoryDebtResponse;
import com.example.debtcontrolbe.model.request.AddDebtRequest;
import com.example.debtcontrolbe.repository.CompanyRepository;
import com.example.debtcontrolbe.repository.DebtRepository;
import com.example.debtcontrolbe.repository.RepresentativeRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RepresentativeRepository representativeRepository;

    DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
    DateTimeFormatter formatterMonth = DateTimeFormatter.ofPattern("MM/yyyy");
    DateTimeFormatter formatterDate_Time = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<HistoryDebtResponse> getDebtHistory(String companyCode){

        Company company = companyRepository.findByCompanyCode(companyCode).get();
        List<HistoryDebtResponse> debts = debtRepository.findTheLatest12RecordByCompanyCodeOrderByDate(company.getId())
                .stream()
                .map(i -> new HistoryDebtResponse(i.getTotalMoney(),i.getAmountPaid(),i.getAmountOwed(),i.getDate().format(formatterMonth),i.getDate().format(formatterDate_Time)))
                .collect(Collectors.toList());
        return debts;
    }

    public String addDebt(AddDebtRequest addDebtRequest) {
        Optional<Company> companyCheck = companyRepository.findByCompanyCode(addDebtRequest.getCompanyCode());
        if (companyCheck.isEmpty()){
            return "company does not exits";
        }
        Company company = companyCheck.get();
        LocalDateTime now = LocalDateTime.now();
        Optional<Debt> checkHistoryDebt1 = debtRepository.getLatestDebt(company.getId());
        if (checkHistoryDebt1.isPresent()){
            Debt debtCheck = checkHistoryDebt1.get();
            if (debtCheck.getDate().getMonthValue()==now.getMonthValue()){
                Debt debt = new Debt();
                debt.setId(debtCheck.getId());
                debt.setDate(now);
                debt.setCompany(company);
                debt.setTotalMoney(debtCheck.getTotalMoney()+addDebtRequest.getTotalMoney());
                debt.setAmountPaid(debtCheck.getAmountPaid()+addDebtRequest.getAmountPaid());
                debt.setAmountOwed(debtCheck.getAmountOwed()+addDebtRequest.getAmountOwed());
                debtRepository.save(debt);
                return "success";
            }else {
                Debt debt = new Debt();
                debt.setDate(now);
                debt.setCompany(company);
                debt.setTotalMoney(debtCheck.getTotalMoney()+addDebtRequest.getTotalMoney());
                debt.setAmountPaid(debtCheck.getAmountPaid()+addDebtRequest.getAmountPaid());
                debt.setAmountOwed(debtCheck.getAmountOwed()+addDebtRequest.getAmountOwed());
                debtRepository.save(debt);
                return "success";
            }
        }else {
            Debt debt = new Debt();
            debt.setDate(now);
            debt.setCompany(company);
            debt.setTotalMoney(addDebtRequest.getTotalMoney());
            debt.setAmountPaid(addDebtRequest.getAmountPaid());
            debt.setAmountOwed(addDebtRequest.getAmountOwed());
            debtRepository.save(debt);
            return "success";
        }
    }

    public String exportDebt(String companyCode) {
        Optional<Company> optionalCompany = companyRepository.findByCompanyCode(companyCode);
        DecimalFormat decimalFormat = new DecimalFormat("#");
        if (optionalCompany.isPresent()){
            Company company = optionalCompany.get();
            Optional<Debt> optionalDebt = debtRepository.getLatestDebt(company.getId());
            if (optionalDebt.isPresent()){

                Debt debt = optionalDebt.get();
                String path = "C:\\debt\\"+companyCode+"_"+LocalDateTime.now().format(formatterDateTime)+".pdf";
                File fontFile = new File("fonts/vuTimes.ttf");


                try {
                    Document document = new Document();
                    BaseFont bf = BaseFont.createFont(fontFile.getAbsolutePath(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font fontHeader = new Font(bf,13);
                    Font fontBody = new Font(bf,11);
                    // khởi tạo một PdfWriter truyền vào document và FileOutputStream
                    PdfWriter.getInstance(document, new FileOutputStream(path));

                    // mở file để thực hiện viết
                    document.open();
                    // thêm nội dung sử dụng add function
                    //Khai báo 2 paragraph
                    Paragraph paragraph1 = new Paragraph("CÔNG TY TNHH GIẢI QUYẾT CÔNG NỢ                   PHIẾU ĐỀ NGHỊ THANH TOÁN",fontHeader);
                    paragraph1.setIndentationLeft(10);
                    paragraph1.setAlignment(Element.ALIGN_LEFT);
                    paragraph1.setSpacingBefore(15);

                    Paragraph paragraph2 = new Paragraph("GIAI ĐOẠN: "+debt.getDate().format(formatterMonth),fontBody);
                    paragraph2.setSpacingBefore(15);
                    paragraph2.setIndentationRight(70);
                    paragraph2.setAlignment(Element.ALIGN_RIGHT);

                    Paragraph paragraphWhiteSpace = new Paragraph(" ",fontBody);

                    Paragraph paragraph3 = new Paragraph("KÍNH GỬI: "+company.getNameCompany().toUpperCase(),fontHeader);
                    paragraph3.setAlignment(Element.ALIGN_CENTER);

                    Paragraph paragraph4 = new Paragraph("Xin Chào: "+company.getRepresentative().getRepresentativeName(),fontBody);
                    paragraph4.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph5 = new Paragraph("Em xin phép gửi thông tin chi tiết tổng hợp về và phí dịch vụ kế toán Tháng "+debt.getDate().format(formatterMonth)+" như sau:",fontBody);
                    paragraph5.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph6 = new Paragraph("Tổng nợ :               "+decimalFormat.format(debt.getTotalMoney())+" VND",fontBody);
                    paragraph6.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph7 = new Paragraph("Số tiền đã trả :       "+decimalFormat.format(debt.getAmountPaid())+" VND",fontBody);
                    paragraph7.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph8 = new Paragraph("Số tiền còn nợ :     "+decimalFormat.format(debt.getAmountOwed()) +" VND",fontBody);
                    paragraph8.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph9 = new Paragraph("Ghi chú : phí dịch vụ tháng "+debt.getDate().format(formatterMonth),fontBody);
                    paragraph9.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph10 = new Paragraph("Thông tin thanh toán: ",fontBody);
                    paragraph10.setAlignment(Element.ALIGN_LEFT);
                    paragraph10.setIndentationLeft(15);

                    Paragraph paragraph11 = new Paragraph("Số tài khoản: 123456789",fontBody);
                    paragraph11.setAlignment(Element.ALIGN_LEFT);
                    paragraph11.setIndentationLeft(15);

                    Paragraph paragraph12 = new Paragraph("Tên thụ hưởng: Dang Duc Minh",fontBody);
                    paragraph12.setAlignment(Element.ALIGN_LEFT);
                    paragraph12.setIndentationLeft(15);

                    Paragraph paragraph13 = new Paragraph("Nội dung: "+company.getNameCompany().toUpperCase()+" thanh toán dịch vụ kế toán tháng "+debt.getDate().format(formatterMonth),fontBody);
                    paragraph13.setAlignment(Element.ALIGN_LEFT);
                    paragraph13.setIndentationLeft(15);

                    LocalDateTime deadline = LocalDateTime.now().plusDays(3);
                    Paragraph paragraph14 = new Paragraph("Nhờ anh/chị thanh toán giúp em trước ngày: "+deadline.format(formatterDate) ,fontBody);
                    paragraph14.setAlignment(Element.ALIGN_LEFT);

                    Paragraph paragraph15 = new Paragraph("Sau khi chuyển khoản anh chị chụp lại UNC để bên em xuất hóa đơn VAT" ,fontBody);
                    paragraph15.setAlignment(Element.ALIGN_LEFT);
                    Paragraph paragraph16 = new Paragraph("Cảm ơn quý công ty" ,fontBody);
                    paragraph16.setAlignment(Element.ALIGN_LEFT);

                    //Thêm 2 đoạn văn bản vào document
                    document.add(paragraphWhiteSpace);
                    document.add(paragraph1);
                    document.add(paragraph2);
                    document.add(paragraphWhiteSpace);
                    document.add(paragraph3);
                    document.add(paragraphWhiteSpace);
                    document.add(paragraph4);
                    document.add(paragraph5);
                    document.add(paragraphWhiteSpace);
                    document.add(paragraph6);
                    document.add(paragraph7);
                    document.add(paragraph8);
                    document.add(paragraphWhiteSpace);
                    document.add(paragraph9);
                    document.add(paragraph10);
                    document.add(paragraph11);
                    document.add(paragraph12);
                    document.add(paragraph13);
                    document.add(paragraphWhiteSpace);
                    document.add(paragraph14);
                    document.add(paragraph15);
                    document.add(paragraph16);
                    // đóng file
                    document.close();

                    sendEmail(company.getRepresentative().getGmail(),path,company.getNameCompany().toUpperCase(),company.getRepresentative().getRepresentativeName(),debt.getDate().format(formatterMonth));

                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else {
                return String.format("Company with code %s or debt not present",companyCode);
            }
        }
        return "success";
    }

    public void sendEmail(String toEmail ,String pathToFile, String companyName , String nameRepresentative, String date){

        //smtp properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.host", "smtp.gmail.com");

        String username = "loimuonnoi150601";
        String password = "zvnkbuktfcmrgnya";
        String from = "loimuonnoi150601@gmail.com";
        File file = new File(pathToFile);


        //session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setFrom(new InternetAddress(from));
            message.setSubject("Hoá đơn công nợ tháng: "+date);

            MimeBodyPart part1 = new MimeBodyPart();
            part1.setText("Kính gửi anh/chị: "+nameRepresentative+", bên em gửi hóa đơn công nợ tháng: "+date+" của công ty: "+companyName);

            MimeBodyPart part2 = new MimeBodyPart();
            part2.attachFile(file);

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(part1);
            mimeMultipart.addBodyPart(part2);

            message.setContent(mimeMultipart);

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
