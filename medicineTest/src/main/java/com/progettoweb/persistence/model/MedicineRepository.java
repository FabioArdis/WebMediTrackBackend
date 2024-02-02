package com.progettoweb.persistence.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    @Query("SELECT m FROM Medicine m WHERE m.name = :name")
    List<Medicine> findByName(@Param("name") String name);

    @Query("SELECT m FROM Medicine m WHERE m.manufacturer = :manufacturer")
    List<Medicine> findByManufacture(@Param("manufacture") String manufacturer);

}
