package com.cabinetmedical.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.cabinetmedical.dto.CreneauDTO;
import com.cabinetmedical.model.Creneau;
import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.enums.RdvStatus;

@Component
public class CreneauMapper {

	public CreneauDTO toDTO(Creneau creneau) {
		if (creneau == null)
			return null;

		Long medecinId = null;
		String medecinNom = null;
		String medecinPrenom = null;

		Long rendezVousId = null;
		RdvStatus rendezVousstatut = null;

		Medecin medecin = creneau.getMedecin();
		if (medecin != null) {
			medecinId = medecin.getId();
			medecinNom = medecin.getNom();
			medecinPrenom = medecin.getPrenom();
		}

		RendezVous rendezVous = creneau.getRendezVous();
		if (rendezVous != null) {
			rendezVousId = rendezVous.getId();
			rendezVousstatut = rendezVous.getStatus();
		}

		return new CreneauDTO(creneau.getId(), creneau.getDebut(), creneau.getFin(), creneau.isActif(), medecinId,
				medecinNom, medecinPrenom, rendezVousId, rendezVousstatut);
	}

	public Creneau toEntity(CreneauDTO dto) {
		if (dto == null)
			return null;

		return Creneau.builder().id(dto.id()).debut(dto.debut()).fin(dto.fin()).actif(dto.actif()).build();
	}

	public List<CreneauDTO> toDTOList(List<Creneau> creneaux) {
		if (creneaux == null)
			return List.of();
		return creneaux.stream().map(this::toDTO).collect(Collectors.toList());
	}

	public List<Creneau> toList(List<CreneauDTO> creneauxDTO) {
		return creneauxDTO.stream().map(this::toEntity).collect(Collectors.toList());
	}
}