package com.cabinetmedical.web;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.RendezVousDTO;
import com.cabinetmedical.service.IRendezVousService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/rendezvous")
@Tag(name = "Rendez-vous Management", description = "API de gestion des rendez-vous")
@Validated
@Slf4j
public class RendezVousController {

	private final IRendezVousService rendezVousService;

	public RendezVousController(IRendezVousService rendezVousService) {
		this.rendezVousService = rendezVousService;
	}

	@Operation(summary = "Réserver un rendez-vous", description = "Crée un rendez-vous CONFIRME pour un patient sur un créneau actif et non réservé.")
	@ApiResponse(responseCode = "201", description = "Rendez-vous créé avec succès.")
	@PostMapping("/reserver/patients/{patientId}/creneaux/{creneauId}")
	public ResponseEntity<RendezVousDTO> reserver(@PathVariable Long patientId, @PathVariable Long creneauId) {

		log.info("[RDV] Réservation demandée — patientId={}, creneauId={}", patientId, creneauId);

		RendezVousDTO created = rendezVousService.reserverCreneau(patientId, creneauId);

		log.info("[RDV] Réservation OK — rendezVousId={}, patientId={}, creneauId={}", created.id(), patientId,
				creneauId);

		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@Operation(summary = "Obtenir un rendez-vous", description = "Retourne les informations détaillées d’un rendez-vous existant.")
	@ApiResponse(responseCode = "200", description = "Rendez-vous trouvé.")
	@GetMapping("/{id}")
	public ResponseEntity<RendezVousDTO> getRendezVousById(@PathVariable Long id) {

		log.info("[RENDEZVOUS] Consultation — id={}", id);

		RendezVousDTO dto = rendezVousService.getRendezVousById(id);

		log.debug("[RENDEZVOUS] Consultation OK — id={}, patient='{} {}'", id, dto.patientNom(), dto.patientPrenom());

		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Lister les rendez-vous", description = "Retourne une liste paginée de rendez-vous avec tri configurable.")
	@ApiResponse(responseCode = "200", description = "Liste paginée de rendez-vous retournée.")
	@GetMapping
	public ResponseEntity<PagedResponseDTO<RendezVousDTO>> getAllRendezVous(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "id") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		log.debug("[RENDEZVOUS] Listing — page={}, size={}, tri={} {}", page, size, sortBy, sortDirection);

		PagedResponseDTO<RendezVousDTO> pageDTO = rendezVousService.getAllRendezVous(pageable);

		log.info("[RENDEZVOUS] Listing OK — éléments={}, page={}/{}", pageDTO.getContent().size(), pageDTO.getPage(),
				pageDTO.getTotalPages());

		return ResponseEntity.ok(pageDTO);
	}

	@Operation(summary = "Mettre à jour un rendez-vous", description = "Modifie les informations d’un rendez-vous existant.")
	@ApiResponse(responseCode = "200", description = "Rendez-vous mis à jour avec succès.")
	@PutMapping("/{id}")
	public ResponseEntity<RendezVousDTO> updateRendezVous(@PathVariable Long id,
			@Valid @RequestBody RendezVousDTO rendezVousDTO) {

		log.info("[RENDEZVOUS] Mise à jour — id={}, nouveauStatus={}", id, rendezVousDTO.status());

		RendezVousDTO updated = rendezVousService.updateRendezVous(id, rendezVousDTO);

		log.info("[RENDEZVOUS] Mise à jour OK — id={}", id);

		return ResponseEntity.ok(updated);
	}

	@Operation(summary = "Annuler un rendez-vous", description = "Annuler le rendez-vous (status=ANNULE).")
	@ApiResponse(responseCode = "200", description = "Rendez-vous annulé.")
	@PatchMapping("/{id}/annuler")
	public ResponseEntity<RendezVousDTO> annuler(@PathVariable Long id) {
		log.warn("[RDV] Annulation demandée — rdvId={}", id);
		RendezVousDTO dto = rendezVousService.annuler(id);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Historiser en HONORE", description = "Marque le rendez-vous comme HONORE après passage du patient.")
	@ApiResponse(responseCode = "200", description = "Rendez-vous honoré.")
	@PatchMapping("/{id}/honorer")
	public ResponseEntity<RendezVousDTO> honorer(@PathVariable Long id) {
		log.info("[RDV] Historisation HONORE — rdvId={}", id);
		RendezVousDTO dto = rendezVousService.marquerHonore(id);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Historiser en NON_PRESENT", description = "Marque le rendez-vous comme NON_PRESENT.")
	@ApiResponse(responseCode = "200", description = "Rendez-vous marqué NON_PRESENT.")
	@PatchMapping("/{id}/non-present")
	public ResponseEntity<RendezVousDTO> nonPresent(@PathVariable Long id) {
		log.info("[RDV] Historisation NON_PRESENT — rdvId={}", id);
		RendezVousDTO dto = rendezVousService.marquerNonPresent(id);
		return ResponseEntity.ok(dto);
	}

	@Operation(summary = "Supprimer un rendez-vous", description = "Supprime un rendez-vous existant définitivement.")
	@ApiResponse(responseCode = "204", description = "Rendez-vous supprimé.")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteRendezVous(@PathVariable Long id) {

		log.warn("[RENDEZVOUS] Suppression demandée — id={}", id);

		rendezVousService.deleteRendezVous(id);

		log.info("[RENDEZVOUS] Suppression OK — id={}", id);

		return ResponseEntity.noContent().build();
	}
}