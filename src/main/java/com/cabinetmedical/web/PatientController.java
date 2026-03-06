package com.cabinetmedical.web;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.PatientDTO;
import com.cabinetmedical.dto.validation.OnCreate;
import com.cabinetmedical.service.IPatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/patients")
@Tag(name = "Patients", description = "API de gestion des patients.")
@Validated
@Slf4j
public class PatientController {

	private final IPatientService patientService;

	public PatientController(IPatientService patientService) {
		this.patientService = patientService;
	}

	@Operation(summary = "Créer un patient", description = "Ajoute un nouveau patient dans le système.")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Patient créé avec succès."),
			@ApiResponse(responseCode = "400", description = "Données invalides fournies.") })
	@PostMapping
	public ResponseEntity<PatientDTO> addPatient(@Validated(OnCreate.class) @RequestBody PatientDTO patientDTO) {

		log.info("[PATIENT] Création — nom={} {}", patientDTO.nom(), patientDTO.prenom());

		PatientDTO created = patientService.addPatient(patientDTO);

		log.info("[PATIENT] Créé — id={}, nom={} {}", created.id(), created.nom(), created.prenom());

		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}

	@Operation(summary = "Obtenir un patient par ID", description = "Retourne les informations d’un patient existant.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Patient trouvé."),
			@ApiResponse(responseCode = "404", description = "Patient introuvable.") })
	@GetMapping("/{id}")
	public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {

		log.info("[PATIENT] Consultation — id={}", id);

		PatientDTO patient = patientService.getPatientById(id);

		return ResponseEntity.ok(patient);
	}

	@Operation(summary = "Liste paginée des patients", description = "Retourne une liste paginée et triable de tous les patients.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès."),
			@ApiResponse(responseCode = "400", description = "Paramètres de pagination invalides.") })
	@GetMapping
	public ResponseEntity<PagedResponseDTO<PatientDTO>> getAllPatients(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "nom") String sortBy,
			@RequestParam(defaultValue = "asc") String sortDirection) {

		Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

		log.info("[PATIENT] Liste — page={}, size={}, sort={} {}", page, size, sortBy, sortDirection);

		PagedResponseDTO<PatientDTO> patients = patientService.getAllPatients(pageable);

		return ResponseEntity.ok(patients);
	}

	@Operation(summary = "Modifier un patient", description = "Met à jour les informations d’un patient existant.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Patient mis à jour."),
			@ApiResponse(responseCode = "404", description = "Patient introuvable.") })
	@PutMapping("/{id}")
	public ResponseEntity<PatientDTO> updatePatient(@PathVariable Long id,
			@Validated(OnCreate.class) @RequestBody PatientDTO patientDTO) {

		log.info("[PATIENT] Mise à jour — id={}", id);

		PatientDTO updated = patientService.updatePatient(id, patientDTO);

		return ResponseEntity.ok(updated);
	}

	@Operation(summary = "Supprimer un patient", description = "Supprime un patient existant du système.")
	@ApiResponses({ @ApiResponse(responseCode = "204", description = "Patient supprimé."),
			@ApiResponse(responseCode = "404", description = "Patient introuvable.") })
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePatient(@PathVariable Long id) {

		log.warn("[PATIENT] Suppression — id={}", id);

		patientService.deletePatient(id);

		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Rechercher un patient par email", description = "Renvoie un patient correspondant à l'email fourni.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Patient trouvé."),
			@ApiResponse(responseCode = "404", description = "Aucun patient trouvé pour cet email.") })
	@GetMapping("/email/{email}")
	public ResponseEntity<PatientDTO> getPatientByEmail(@PathVariable String email) {

		log.info("[PATIENT] Recherche par email — '{}'", email);

		PatientDTO patient = patientService.getPatientsByEmail(email);

		return ResponseEntity.ok(patient);
	}
}