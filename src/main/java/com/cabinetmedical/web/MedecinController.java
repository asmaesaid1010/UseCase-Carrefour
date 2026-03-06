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

import com.cabinetmedical.dto.MedecinDTO;
import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.service.IMedecinService;

@RestController
@RequestMapping("/api/v1/medecins")
@Tag(name = "Medecin Management", description = "API de gestion des medecins.")
@Validated
@Slf4j
public class MedecinController {

	private IMedecinService medecinService;

	public MedecinController(IMedecinService medecinService) {
		this.medecinService = medecinService;
	}

	@Operation(summary = "Ajouter un médecin", description = "Crée un nouveau médecin et l'associe à une spécialité existante.")
	@ApiResponse(responseCode = "201", description = "Médecin créé avec succès et associé à la spécialité.")
	@PostMapping("/specialites/{specialiteId}")
	public ResponseEntity<MedecinDTO> addMedecinWithSpecialite(@PathVariable Long specialiteId,
			@Validated(OnCreate.class) @RequestBody MedecinDTO medecinDTO) {

		log.info("[MEDECIN] Ajout demandé — specialiteId={}, nom='{} {}'", specialiteId, medecinDTO.prenom(),
				medecinDTO.nom());

		MedecinDTO created = medecinService.addMedecinWithSpecialite(specialiteId, medecinDTO);

		log.info("[MEDECIN] Ajout réussi — medecinId={}, specialiteId={}", created.id(), specialiteId);

		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@Operation(summary = "Obtenir un medecin", description = "Retourne les informations détaillées d’un medecin existant.")
	@ApiResponse(responseCode = "200", description = "Medecin trouvé.")
	@GetMapping("/{id}")
	public ResponseEntity<MedecinDTO> getMedecinById(@PathVariable Long id) {

		log.info("[MEDECIN] Consultation — medecinId={}", id);

		MedecinDTO dto = medecinService.getMedecinById(id);

		log.debug("[MEDECIN] Consultation OK — id={}, nom='{}'", id, dto.nom());

		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Lister les medecins", description = "Retourne une liste paginée d’medecins avec tri configurable.")
	@ApiResponse(responseCode = "200", description = "Liste paginée d’medecins retournée.")
	@GetMapping
	public ResponseEntity<PagedResponseDTO<MedecinDTO>> getAllAccessories(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "nom") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		log.debug("[MEDECIN] Listing — page={}, size={}, tri={} {}", page, size, sortBy, sortDirection);

		PagedResponseDTO<MedecinDTO> accessories = medecinService.getAllMedecins(pageable);

		log.info("[MEDECIN] Listing OK — éléments={}, page={}/{}", accessories.getContent().size(),
				accessories.getPage(), accessories.getTotalPages());

		return ResponseEntity.ok(accessories);
	}

	@Operation(summary = "Mettre à jour un medecin", description = "Modifie les informations d’un medecin existant.")
	@ApiResponse(responseCode = "200", description = "medecin mis à jour avec succès.")
	@PutMapping("/{id}")
	public ResponseEntity<MedecinDTO> updateMedecin(@PathVariable Long id, @Valid @RequestBody MedecinDTO medecinDTO) {

		log.info("[MEDECIN] Mise à jour — medecinId={}, nouveauNom='{}'", id, medecinDTO.nom());

		MedecinDTO updated = medecinService.updateMedecin(id, medecinDTO);

		log.info("[MEDECIN] Mise à jour OK — medecinId={}", id);

		return ResponseEntity.ok(updated);
	}

	@Operation(summary = "Supprimer un medecin", description = "Supprime un medecin existant définitivement.")
	@ApiResponse(responseCode = "204", description = "Medecin supprimé.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMedecin(@PathVariable Long id) {

		log.warn("[MEDECIN] Suppression demandée — medecinId={}", id);

		medecinService.deleteMedecin(id);

		log.info("[MEDECIN] Suppression OK — medecinId={}", id);

		return ResponseEntity.noContent().build();
	}
}