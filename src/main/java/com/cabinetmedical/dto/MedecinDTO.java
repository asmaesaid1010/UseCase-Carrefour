package com.cabinetmedical.dto;

import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.dto.validation.OnUpdate;
import jakarta.validation.constraints.*;

public record MedecinDTO(

		@Null(groups = OnCreate.class, message = "L'id ne doit pas être fourni en création") @NotNull(groups = OnUpdate.class, message = "L'id est obligatoire en mise à jour") Long id,

		@NotBlank(message = "Le nom est obligatoire") @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères") String nom,

		@NotBlank(message = "Le prénom est obligatoire") @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères") String prenom,

		@NotBlank(message = "L'email est obligatoire") @Email(message = "L'email doit être valide") @Size(max = 120, message = "L'email ne doit pas dépasser 120 caractères") String email,

		@Pattern(regexp = "^[0-9+\\- ()]{6,40}$", message = "Téléphone invalide") @Size(max = 40, message = "Le téléphone ne doit pas dépasser 40 caractères") String telephone,

		Long specialiteId, String specialiteCode, String libelle

) {
}