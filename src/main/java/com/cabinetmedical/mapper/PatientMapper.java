package com.cabinetmedical.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cabinetmedical.dto.PatientDTO;
import com.cabinetmedical.model.Patient;

@Component
public class PatientMapper {

	public PatientDTO toDTO(Patient patient) {
		if (patient == null)
			return null;

		return new PatientDTO(patient.getId(), patient.getNom(), patient.getPrenom(), patient.getEmail(),
				patient.getTelephone());
	}

	public Patient toEntity(PatientDTO patientDTO) {
		if (patientDTO == null)
			return null;

		return new Patient(patientDTO.id(), patientDTO.nom(), patientDTO.prenom(), patientDTO.email(),
				patientDTO.telephone());
	}

	public List<PatientDTO> toDTOList(List<Patient> patients) {
		return patients.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<Patient> toList(List<PatientDTO> patientsDTO) {
		return patientsDTO.stream().map(this::toEntity).collect(Collectors.toList());
	}
}