package com.cabinetmedical.config;

import java.time.Instant;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.cabinetmedical.dto.ApiErreur;
import com.cabinetmedical.exception.OperationFailedException;
import com.cabinetmedical.exception.RessourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	private ApiErreur build(HttpServletRequest req, HttpStatus status, String message, String code) {
		return new ApiErreur(Instant.now(), status.value(), status.getReasonPhrase(), message, req.getRequestURI(),
				code);
	}

	@ExceptionHandler(RessourceNotFoundException.class)
	public ResponseEntity<ApiErreur> handleNotFound(RessourceNotFoundException ex, HttpServletRequest req) {
		log.warn("404 Not Found: {}", ex.getMessage());
		ApiErreur body = build(req, HttpStatus.NOT_FOUND, ex.getMessage(), "NOT_FOUND");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiErreur> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
		log.error("409 Conflict (integrity): {}", ex.getMessage(), ex);
		ApiErreur body = build(req, HttpStatus.CONFLICT, "Opération impossible : contrainte d'intégrité violée.",
				"DATA_INTEGRITY");
		return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
	}

	@ExceptionHandler(EmptyResultDataAccessException.class)
	public ResponseEntity<ApiErreur> handleEmptyResult(EmptyResultDataAccessException ex, HttpServletRequest req) {
		log.warn("404 Not Found (deleteById): {}", ex.getMessage());
		ApiErreur body = build(req, HttpStatus.NOT_FOUND, "Ressource non trouvée.", "NOT_FOUND");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErreur> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> err.getField() + " " + err.getDefaultMessage()).findFirst().orElse("Requête invalide");
		log.warn("400 Bad Request (validation): {}", message);
		ApiErreur body = build(req, HttpStatus.BAD_REQUEST, message, "VALIDATION_ERROR");
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiErreur> handleResponseStatus(ResponseStatusException ex, HttpServletRequest req) {
		HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
		if (status == null)
			status = HttpStatus.BAD_REQUEST;
		log.warn("{} ResponseStatusException: {}", status.value(), ex.getReason());
		ApiErreur body = build(req, status, ex.getReason(), "ERROR");
		return ResponseEntity.status(status).body(body);
	}

	@ExceptionHandler(OperationFailedException.class)
	public ResponseEntity<ApiErreur> handleOperationFailed(OperationFailedException ex, HttpServletRequest req) {
		log.error("OperationFailed: {}", ex.getMessage(), ex);
		ApiErreur body = build(req, HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), "OPERATION_FAILED");
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(body);
	}

	@ExceptionHandler({ ErrorResponseException.class, Exception.class })
	public ResponseEntity<ApiErreur> handleGeneric(Exception ex, HttpServletRequest req) {
		log.error("500 Internal Server Error: {}", ex.getMessage(), ex);
		ApiErreur body = build(req, HttpStatus.INTERNAL_SERVER_ERROR,
				"Erreur interne, veuillez contacter l’administrateur", "INTERNAL_ERROR");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}
	
	

}