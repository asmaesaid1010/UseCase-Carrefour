package com.cabinetmedical.service;

import org.springframework.data.domain.Pageable;

import com.cabinetmedical.dto.CreneauDTO;
import com.cabinetmedical.dto.PagedResponseDTO;

import jakarta.validation.Valid;

public interface ICreneauService {

	CreneauDTO addCreneauWithMedecin(Long medecinId, CreneauDTO creneauDTO);

	PagedResponseDTO<CreneauDTO> getCreneauxDisponiblByMedecin(Long medecinId, Pageable pageable);

	CreneauDTO getCreneauById(Long id);

	PagedResponseDTO<CreneauDTO> getAllCreneaux(Pageable pageable);

	CreneauDTO updateCreneau(Long id, @Valid CreneauDTO creneauDTO);

	void deleteCreneau(Long id);

	CreneauDTO desactiverCreneau(Long id);

	CreneauDTO activerCreneau(Long id);

}
