package com.example.hadconsentservice.repository;

import com.example.hadconsentservice.bean.ConsentItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsentItemRepository extends JpaRepository<ConsentItem, Integer> {
    List<ConsentItem> findAllById(Integer patientID);
    List<ConsentItem> findAllByDoctorID(Integer doctorID);

    List<ConsentItem> findAllByPatientID(Integer patientID);
}
