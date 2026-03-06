package com.cabinetmedical.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.cabinetmedical.dto.MedecinDTO;
import com.cabinetmedical.dto.PagedResponseDTO;

public interface IMedecinService {

	List<MedecinDTO> getMedecinsBySpecialiteCode(String specialiteCode);

	MedecinDTO addMedecinWithSpecialite(Long specialiteId, MedecinDTO medecinDTO);

	MedecinDTO getMedecinById(Long id);

	PagedResponseDTO<MedecinDTO> getAllMedecins(Pageable pageable);

	MedecinDTO updateMedecin(Long id, MedecinDTO medecinDTO);

	void deleteMedecin(Long id);

}
