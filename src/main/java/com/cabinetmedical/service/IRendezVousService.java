
package com.cabinetmedical.service;

import org.springframework.data.domain.Pageable;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.RendezVousDTO;

import jakarta.validation.Valid;

public interface IRendezVousService {

	RendezVousDTO reserverCreneau(Long patientId, Long creneauId);

	RendezVousDTO annuler(Long rendezVousId);

	RendezVousDTO marquerHonore(Long rendezVousId);

	RendezVousDTO marquerNonPresent(Long rendezVousId);

	RendezVousDTO getRendezVousById(Long id);

	PagedResponseDTO<RendezVousDTO> getAllRendezVous(Pageable pageable);

	RendezVousDTO updateRendezVous(Long id, @Valid RendezVousDTO rendezVousDTO);

	void deleteRendezVous(Long id);

}