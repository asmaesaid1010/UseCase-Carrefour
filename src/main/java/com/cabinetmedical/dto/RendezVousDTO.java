package com.cabinetmedical.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.dto.validation.OnUpdate;
import com.cabinetmedical.model.enums.RdvStatus;

public record RendezVousDTO(

		@Null(groups = OnCreate.class, message = "L'id est généré automatiquement.") @NotNull(groups = OnUpdate.class, message = "L'id doit être fourni en mise à jour.") Long id,

		@NotNull(message = "Le status du rendez-vous est obligatoire.") RdvStatus status,

		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime creeLe,

		@NotNull(message = "Le patient est obligatoire.") Long patientId, String patientNom, String patientPrenom,
		String patientEmail,

		@NotNull(message = "Le créneau est obligatoire.") Long creneauId,

		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime creneauDebut,

		@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime creneauFin,

		Boolean creneauActif

) {
}