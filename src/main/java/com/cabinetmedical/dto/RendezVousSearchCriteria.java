package com.cabinetmedical.dto;

import org.springframework.format.annotation.DateTimeFormat;

import com.cabinetmedical.model.enums.RdvStatus;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RendezVousSearchCriteria {
	private Long patientId;

	private Long medecinId;

	private Long specialiteId;

	private Enum<RdvStatus> status;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateDebut;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime dateFin;

	@Override
	public String toString() {
		return "RendezVousSearchCriteria{" + "patientId=" + patientId + ", medecinId=" + medecinId + ", specialiteId="
				+ specialiteId + ", statut='" + RdvStatus.values() + '\'' + ", dateDebut=" + dateDebut + ", dateFin="
				+ dateFin + '}';
	}
}