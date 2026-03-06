package com.cabinetmedical.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cabinetmedical.dto.SpecialiteDTO;
import com.cabinetmedical.model.Specialite;

@Component
public class SpecialiteMapper {

	public SpecialiteDTO toDTO(Specialite specialite) {
		if (specialite == null)
			return null;

		return new SpecialiteDTO(specialite.getId(), specialite.getCode(), specialite.getLibelle());
	}

	public Specialite toEntity(SpecialiteDTO specialiteDTO) {
		if (specialiteDTO == null)
			return null;

		return new Specialite(specialiteDTO.id(), specialiteDTO.code(), specialiteDTO.libelle());
	}

	public List<SpecialiteDTO> toDTOList(List<Specialite> specialites) {
		return specialites.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<Specialite> toList(List<SpecialiteDTO> specialitesDTO) {
		return specialitesDTO.stream().map(this::toEntity).collect(Collectors.toList());
	}
}