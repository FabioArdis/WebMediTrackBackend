package com.progettoweb;

import com.progettoweb.persistence.model.Medicine;
import com.progettoweb.persistence.model.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.Date;

@SpringBootApplication
@ServletComponentScan
public class WebMediTrackMedicine {
    @Autowired
    private MedicineRepository medicineRepository;

    public static void main(String[] args) {
        SpringApplication.run(WebMediTrackMedicine.class, args);
    }

}
