package com.cabinetmedical.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.AssertTrue;

import com.cabinetmedical.model.enums.RdvStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

public record CreneauDTO(

		Long id,

		@NotNull(message = "La date/heure de début est obligatoire.") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime debut,

		@NotNull(message = "La date/heure de fin est obligatoire.") @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime fin,

		@NotNull(message = "Le statut 'actif' est obligatoire.") Boolean actif,

		@NotNull(message = "L'identifiant du médecin est obligatoire.") Long medecinId,

		String medecinNom, String medecinPrenom, Long rendezVousId, RdvStatus statut

) {

	@AssertTrue(message = "La fin du créneau doit être strictement après le début.")
	public boolean isFinAfterDebut() {
		return debut == null || fin == null || fin.isAfter(debut);
	}
}