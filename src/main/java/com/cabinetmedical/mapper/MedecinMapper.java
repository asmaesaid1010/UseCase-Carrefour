package com.cabinetmedical.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cabinetmedical.dto.MedecinDTO;
import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.model.Specialite;

@Component
public class MedecinMapper {

	public MedecinDTO toDTO(Medecin medecin) {
		if (medecin == null)
			return null;

		Long specialiteId = null;
		String specialiteCode = null;
		String specialiteLibelle = null;

		Specialite spec = medecin.getSpecialite();
		if (spec != null) {
			specialiteId = spec.getId();
			specialiteCode = spec.getCode();
			specialiteLibelle = spec.getLibelle();
		}

		return new MedecinDTO(medecin.getId(), medecin.getNom(), medecin.getPrenom(), medecin.getEmail(),
				medecin.getTelephone(), specialiteId, specialiteCode, specialiteLibelle);
	}

	public Medecin toEntity(MedecinDTO dto) {
		if (dto == null)
			return null;

		return Medecin.builder().id(dto.id()).nom(dto.nom()).prenom(dto.prenom()).email(dto.email())
				.telephone(dto.telephone()).build();
	}

	public List<MedecinDTO> toDTOList(List<Medecin> medecins) {
		if (medecins == null)
			return List.of();

		return medecins.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<Medecin> toList(List<MedecinDTO> medecinsDTO) {
		if (medecinsDTO == null)
			return List.of();

		return medecinsDTO.stream().map(this::toEntity).collect(Collectors.toList());
	}
}