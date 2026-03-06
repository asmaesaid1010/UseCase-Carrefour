package com.cabinetmedical.service;

import org.springframework.data.domain.Pageable;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.SpecialiteDTO;

public interface ISpecialiteService {

	SpecialiteDTO addSpecialite(SpecialiteDTO specialiteDTO);

	SpecialiteDTO getSpecialiteById(Long id);

	PagedResponseDTO<SpecialiteDTO> getAllSpecialites(Pageable pageable);

	SpecialiteDTO updateSpecialite(Long id, SpecialiteDTO specialiteDTO);

	void deleteSpecialite(Long id);

	SpecialiteDTO getSpecialitesByCode(String codeName);

}
