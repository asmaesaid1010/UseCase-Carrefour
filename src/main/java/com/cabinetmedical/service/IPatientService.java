package com.cabinetmedical.service;

import org.springframework.data.domain.Pageable;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.PatientDTO;

public interface IPatientService {

	PatientDTO addPatient(PatientDTO patientDTO);

	PatientDTO getPatientById(Long id);

	PagedResponseDTO<PatientDTO> getAllPatients(Pageable pageable);

	PatientDTO updatePatient(Long id, PatientDTO patientDTO);

	void deletePatient(Long id);

	PatientDTO getPatientsByEmail(String email);

}
