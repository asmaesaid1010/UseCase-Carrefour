package com.cabinetmedical.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.SpecialiteDTO;
import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.dto.validation.OnUpdate;
import com.cabinetmedical.service.ISpecialiteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/specialites")
@Tag(name = "Spécialités", description = "API de gestion des spécialités médicales.")
@Validated
@Slf4j
public class SpecialiteController {

	private final ISpecialiteService specialiteService;

	public SpecialiteController(ISpecialiteService specialiteService) {
		this.specialiteService = specialiteService;
	}

	@Operation(summary = "Créer une spécialité", description = "Ajoute une nouvelle spécialité médicale dans le système.")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Spécialité créée avec succès."),
			@ApiResponse(responseCode = "400", description = "Les données fournies sont invalides.") })
	@PostMapping
	public ResponseEntity<SpecialiteDTO> addSpecialite(
			@Validated(OnCreate.class) @RequestBody SpecialiteDTO specialiteDTO) {

		log.info("[SPECIALITE] Création demandée — code='{}'", specialiteDTO.code());

		SpecialiteDTO created = specialiteService.addSpecialite(specialiteDTO);

		log.info("[SPECIALITE] Créée avec succès — id={}, code={}", created.id(), created.code());

		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@Operation(summary = "Obtenir une spécialité par ID", description = "Retourne les informations d’une spécialité existante.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Spécialité trouvée."),
			@ApiResponse(responseCode = "404", description = "Aucune spécialité trouvée avec cet ID.") })
	@GetMapping("/{id}")
	public ResponseEntity<SpecialiteDTO> getSpecialiteById(@PathVariable Long id) {

		log.info("[SPECIALITE] Lecture — id={}", id);

		SpecialiteDTO specialite = specialiteService.getSpecialiteById(id);

		return ResponseEntity.ok(specialite);
	}

	@Operation(summary = "Liste paginée des spécialités", description = "Retourne une page de spécialités avec options de tri.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Liste obtenue avec succès."),
			@ApiResponse(responseCode = "400", description = "Paramètres de pagination invalides.") })
	@GetMapping
	public ResponseEntity<PagedResponseDTO<SpecialiteDTO>> getAllSpecialites(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "code") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		log.info("[SPECIALITE] Liste — page={}, size={}, tri={}-{}", page, size, sortBy, sortDirection);

		PagedResponseDTO<SpecialiteDTO> specialites = specialiteService.getAllSpecialites(pageable);

		return ResponseEntity.ok(specialites);
	}

	@Operation(summary = "Modifier une spécialité", description = "Met à jour les informations d’une spécialité existante.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Spécialité mise à jour."),
			@ApiResponse(responseCode = "404", description = "Spécialité introuvable.") })
	@PutMapping("/{id}")
	public ResponseEntity<SpecialiteDTO> updateSpecialite(@PathVariable Long id,
			@Validated(OnUpdate.class) @RequestBody SpecialiteDTO specialiteDTO) {

		log.info("[SPECIALITE] Mise à jour — id={}", id);

		SpecialiteDTO updated = specialiteService.updateSpecialite(id, specialiteDTO);

		return ResponseEntity.ok(updated);
	}

	@Operation(summary = "Supprimer une spécialité", description = "Supprime une spécialité médicale existante.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Spécialité supprimée."),
			@ApiResponse(responseCode = "404", description = "Spécialité introuvable.") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSpecialite(@PathVariable Long id) {

		log.warn("[SPECIALITE] Suppression — id={}", id);

		specialiteService.deleteSpecialite(id);

		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Rechercher une spécialité par code", description = "Renvoie une spécialité correspondant au code donné.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Spécialité trouvée."),
			@ApiResponse(responseCode = "404", description = "Aucune spécialité trouvée pour ce code.") })
	@GetMapping("/code/{codeName}")
	public ResponseEntity<SpecialiteDTO> getSpecialitesByCode(@PathVariable String codeName) {

		log.info("[SPECIALITE] Recherche par code — '{}'", codeName);

		SpecialiteDTO specialite = specialiteService.getSpecialitesByCode(codeName);

		return ResponseEntity.ok(specialite);
	}
}