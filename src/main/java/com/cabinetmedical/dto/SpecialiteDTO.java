package com.cabinetmedical.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.dto.validation.OnUpdate;
import jakarta.validation.constraints.*;

public record SpecialiteDTO(

		@Null(groups = OnCreate.class, message = "L'id ne doit pas être fourni en création") @NotNull(groups = OnUpdate.class, message = "L'id est obligatoire en mise à jour") Long id,
		@NotBlank(message = "Le code est obligatoire") @Size(max = 50, message = "Le code ne doit pas dépasser 50 caractères") String code,

		@NotBlank(message = "Le libellé est obligatoire") @Size(max = 300, message = "Le libellé ne doit pas dépasser 150 caractères") String libelle

) {
}