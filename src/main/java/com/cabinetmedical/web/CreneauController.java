package com.cabinetmedical.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import com.cabinetmedical.dto.CreneauDTO;
import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.service.ICreneauService;

@RestController
@RequestMapping("/api/v1/creneaux")
@Tag(name = "Creneau Management", description = "API de gestion des créneaux.")
@Validated
@Slf4j
public class CreneauController {

	private final ICreneauService creneauService;

	public CreneauController(ICreneauService creneauService) {
		this.creneauService = creneauService;
	}

	@Operation(summary = "Créer un créneau", description = "Ajoute un nouveau créneau et l’associe à un médecin existant.")
	@ApiResponse(responseCode = "201", description = "Créneau créé avec succès.")
	@PostMapping("/medecins/{medecinId}")
	public ResponseEntity<CreneauDTO> addCreneauWithMedecin(@PathVariable Long medecinId,
			@Validated(OnCreate.class) @RequestBody CreneauDTO creneauDTO) {

		log.info("[CRENEAU] Création demandée — medecinId={}, début={}, fin={}", medecinId, creneauDTO.debut(),
				creneauDTO.fin());

		CreneauDTO created = creneauService.addCreneauWithMedecin(medecinId, creneauDTO);

		log.info("[CRENEAU] Création réussie — creneauId={}, medecinId={}", created.id(), medecinId);

		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@Operation(summary = "Lister les créneaux disponible d’un médecin", description = "Retourne une liste paginée et triée des créneaux disponible pour un médecin donné.")
	@ApiResponse(responseCode = "200", description = "Liste paginée de créneaux retournée avec succès.")
	@GetMapping("/by-medecin/{medecinId}")
	public ResponseEntity<PagedResponseDTO<CreneauDTO>> getCreneauByMedecinId(@PathVariable Long medecinId,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "debut") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		log.debug("[CRENEAU] Listing médecin — medecinId={}, page={}, size={}, sortBy={}, direction={}", medecinId,
				page, size, sortBy, sortDirection);

		PagedResponseDTO<CreneauDTO> creneaux = creneauService.getCreneauxDisponiblByMedecin(medecinId, pageable);

		log.info("[CRENEAU] Listing OK — totalElements={}, page={}/{}", creneaux.getTotalElements(), creneaux.getPage(),
				creneaux.getTotalPages());

		return ResponseEntity.ok(creneaux);
	}

	@Operation(summary = "Obtenir un créneau", description = "Retourne les informations détaillées d’un créneau existant.")
	@ApiResponse(responseCode = "200", description = "Créneau trouvé.")
	@GetMapping("/{id}")
	public ResponseEntity<CreneauDTO> getCreneauById(@PathVariable Long id) {

		log.info("[CRENEAU] Consultation demandée — creneauId={}", id);

		CreneauDTO dto = creneauService.getCreneauById(id);

		log.debug("[CRENEAU] Consultation réussie — id={}, actif={}, début={}, fin={}", id, dto.actif(), dto.debut(),
				dto.fin());

		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Lister les créneaux", description = "Retourne une liste paginée et triable de tous les créneaux.")
	@ApiResponse(responseCode = "200", description = "Liste paginée de créneaux retournée avec succès.")
	@GetMapping
	public ResponseEntity<PagedResponseDTO<CreneauDTO>> getAllCreneaux(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "debut") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		log.debug("[CRENEAU] Listing — page={}, size={}, sortBy={}, direction={}", page, size, sortBy, sortDirection);

		PagedResponseDTO<CreneauDTO> creneaux = creneauService.getAllCreneaux(pageable);

		log.info("[CRENEAU] Listing OK — totalElements={}, page={}/{}", creneaux.getTotalElements(), creneaux.getPage(),
				creneaux.getTotalPages());

		return ResponseEntity.ok(creneaux);
	}

	@Operation(summary = "Mettre à jour un créneau", description = "Modifie les informations d’un créneau existant.")
	@ApiResponse(responseCode = "200", description = "Créneau mis à jour avec succès.")
	@PutMapping("/{id}")
	public ResponseEntity<CreneauDTO> updateCreneau(@PathVariable Long id, @Valid @RequestBody CreneauDTO creneauDTO) {

		log.info("[CRENEAU] Mise à jour demandée — creneauId={}, nouveauDébut={}, nouvelleFin={}", id,
				creneauDTO.debut(), creneauDTO.fin());

		CreneauDTO updated = creneauService.updateCreneau(id, creneauDTO);

		log.info("[CRENEAU] Mise à jour OK — creneauId={}", id);

		return ResponseEntity.ok(updated);
	}

	@Operation(summary = "Supprimer un créneau", description = "Supprime définitivement un créneau existant.")
	@ApiResponse(responseCode = "204", description = "Créneau supprimé avec succès.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCreneau(@PathVariable Long id) {

		log.warn("[CRENEAU] Suppression demandée — creneauId={}", id);

		creneauService.deleteCreneau(id);

		log.info("[CRENEAU] Suppression réussie — creneauId={}", id);

		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Désactiver un créneau", description = "Met le champ actif à false.")
	@ApiResponse(responseCode = "200", description = "Créneau désactivé ou déjà inactif.")
	@PatchMapping("/{id}/desactiver")
	public ResponseEntity<CreneauDTO> desactiver(@PathVariable Long id) {
		log.warn("[CRENEAU] Désactivation demandée — creneauId={}", id);
		CreneauDTO dto = creneauService.desactiverCreneau(id);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Activer un créneau", description = "Passe le champ 'actif' à true (idempotent).")
	@ApiResponse(responseCode = "200", description = "Créneau activé ou déjà actif.")
	@PatchMapping("/{id}/activer")
	public ResponseEntity<CreneauDTO> activer(@PathVariable Long id) {
		log.info("[CRENEAU] Activation demandée — creneauId={}", id);
		CreneauDTO dto = creneauService.activerCreneau(id);
		return ResponseEntity.ok(dto);
	}
}