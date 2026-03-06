package com.cabinetmedical.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cabinetmedical.dto.RendezVousDTO;
import com.cabinetmedical.model.Creneau;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.RendezVous;

@Component
public class RendezVousMapper {

	public RendezVousDTO toDTO(RendezVous rendezVous) {
		if (rendezVous == null) {
			return null;
		}

		Long patientId = null;
		String patientNom = null;
		String patientPrenom = null;
		String patientEmail = null;

		Patient patient = rendezVous.getPatient();
		if (patient != null) {
			patientId = patient.getId();
			patientNom = patient.getNom();
			patientPrenom = patient.getPrenom();
			patientEmail = patient.getEmail();
		}

		Long creneauId = null;
		LocalDateTime creneauDebut = null;
		LocalDateTime creneauFin = null;
		Boolean creneauActif = null;

		Creneau creneau = rendezVous.getCreneau();
		if (creneau != null) {
			creneauId = creneau.getId();
			creneauDebut = creneau.getDebut();
			creneauFin = creneau.getFin();
			creneauActif = creneau.isActif();
		}

		return new RendezVousDTO(rendezVous.getId(), rendezVous.getStatus(), rendezVous.getCreeLe(), patientId,
				patientNom, patientPrenom, patientEmail, creneauId, creneauDebut, creneauFin, creneauActif);
	}

	public RendezVous toEntity(RendezVousDTO dto) {
		if (dto == null) {
			return null;
		}

		return RendezVous.builder().id(dto.id()).status(dto.status()).creeLe(dto.creeLe()).build();
	}

	public List<RendezVousDTO> toDTOList(List<RendezVous> liste) {
		if (liste == null) {
			return List.of();
		}
		return liste.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<RendezVous> toList(List<RendezVousDTO> listeDTO) {
		if (listeDTO == null) {
			return List.of();
		}
		return listeDTO.stream().map(this::toEntity).collect(Collectors.toList());
	}

}