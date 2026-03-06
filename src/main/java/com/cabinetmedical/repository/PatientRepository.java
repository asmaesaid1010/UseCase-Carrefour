package com.cabinetmedical.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetmedical.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

	Optional<Patient> findByEmailIgnoreCase(String code);

	boolean existsByEmail(String email);

}
