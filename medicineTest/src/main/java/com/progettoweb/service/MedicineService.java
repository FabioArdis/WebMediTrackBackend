package com.progettoweb.service;

import com.progettoweb.persistence.model.Medicine;
import com.progettoweb.persistence.model.MedicineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicineService {
    private final MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine getMedicineById(int id) {
        return medicineRepository.findById(id).orElse(null);
    }

    public void saveMedicine(Medicine medicine) {
        medicineRepository.save(medicine);
    }

    public void deleteMedicineById(int id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> findMedicineByName(String name) {
        return medicineRepository.findByName(name);
    }

    public List<Medicine> findMedicineByManufacture(String manufacture) {
        return medicineRepository.findByManufacture(manufacture);
    }
}
