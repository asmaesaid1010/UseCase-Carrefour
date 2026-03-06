package com.cabinetmedical.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.PatientDTO;
import com.cabinetmedical.exception.RessourceNotFoundException;
import com.cabinetmedical.mapper.PatientMapper;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.repository.PatientRepository;
import com.cabinetmedical.service.IPatientService;

import lombok.extern.slf4j.Slf4j;
import com.cabinetmedical.utils.Constantes;

@Service
@Transactional
@Slf4j
public class PatientServiceImpl implements IPatientService {

	private final PatientRepository patientRepository;
	private final PatientMapper patientMapper;

	public PatientServiceImpl(PatientRepository patientRepository, PatientMapper patientMapper) {
		this.patientRepository = patientRepository;
		this.patientMapper = patientMapper;
	}

	@Override
	public PatientDTO addPatient(PatientDTO patientDTO) {
		log.info("Début d'ajout du patient — nom={} {}", patientDTO.nom(), patientDTO.prenom());

		Patient patient = patientMapper.toEntity(patientDTO);

		Patient savedPatient = patientRepository.save(patient);

		log.info("Patient ajouté avec succès — ID={}", savedPatient.getId());

		return patientMapper.toDTO(savedPatient);
	}

	@Override
	@Transactional(readOnly = true)
	public PatientDTO getPatientById(Long id) {
		log.info("Récupération du patient — ID={}", id);

		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.PATIENT_NON_TROUVE_PAR_ID + id));

		return patientMapper.toDTO(patient);
	}

	@Override
	public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
		log.info("Début de mise à jour du patient — ID={}", id);

		Patient existingPatient = patientRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.PATIENT_NON_TROUVE_PAR_ID + id));

		existingPatient.setNom(patientDTO.nom());
		existingPatient.setPrenom(patientDTO.prenom());
		existingPatient.setEmail(patientDTO.email());
		existingPatient.setTelephone(patientDTO.telephone());

		Patient updatedPatient = patientRepository.save(existingPatient);

		log.info("Patient mis à jour avec succès — ID={}", updatedPatient.getId());

		return patientMapper.toDTO(updatedPatient);

	}

	@Override
	public void deletePatient(Long id) {
		log.info("Début de suppression du patient — ID={}", id);

		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.PATIENT_NON_TROUVE_PAR_ID + id));

		patientRepository.delete(patient);
		log.info("Patient supprimé avec succès — ID={}", id);

	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<PatientDTO> getAllPatients(Pageable pageable) {
		Page<Patient> patientPage = patientRepository.findAll(pageable);
		return createPagedResponse(patientPage);
	}

	@Override
	@Transactional(readOnly = true)
	public PatientDTO getPatientsByEmail(String email) {
		Patient patient = patientRepository.findByEmailIgnoreCase(email)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.AUCUN_PATIENT_TROUVE_PAR_EMAIL + email));

		return patientMapper.toDTO(patient);
	}

	private PagedResponseDTO<PatientDTO> createPagedResponse(Page<Patient> patientPage) {
		return new PagedResponseDTO<>(patientMapper.toDTOList(patientPage.getContent()), patientPage.getNumber(),
				patientPage.getSize(), patientPage.getTotalElements(), patientPage.getTotalPages());
	}
}