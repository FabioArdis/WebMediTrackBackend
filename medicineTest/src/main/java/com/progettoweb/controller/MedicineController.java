package com.progettoweb.controller;

import com.progettoweb.service.MedicineService;
import com.progettoweb.persistence.model.Medicine;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmaci")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @GetMapping("/lista")
    public List<Medicine> getListaFarmaci() {
        return medicineService.getAllMedicines();
    }

    @GetMapping("/{id}")
    public Medicine getFarmacoById(@PathVariable int id) {
        return medicineService.getMedicineById(id);
    }

    @PostMapping("/salva")
    public void salvaFarmaco(@RequestBody Medicine medicine) {
        medicineService.saveMedicine(medicine);
    }

    @DeleteMapping("/elimina/{id}")
    public void eliminaFarmaco(@PathVariable int id) {
        medicineService.deleteMedicineById(id);
    }
    @GetMapping("/searchByName")
    public List<Medicine> searchMedicineByName(@RequestParam String name) {
        return medicineService.findMedicineByName(name);
    }
    @GetMapping("/searchByManufacture")
    public List<Medicine> searchMedicineByManufature(@RequestParam String manufacture) {
        return medicineService.findMedicineByManufacture(manufacture);
    }
}

