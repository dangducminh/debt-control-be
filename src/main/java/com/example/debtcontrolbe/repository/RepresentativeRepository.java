package com.example.debtcontrolbe.repository;

import com.example.debtcontrolbe.model.Representative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepresentativeRepository extends JpaRepository<Representative,Long> {
}
