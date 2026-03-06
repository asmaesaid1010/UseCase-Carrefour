package com.cabinetmedical.service.Impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.RendezVousDTO;
import com.cabinetmedical.exception.OperationFailedException;
import com.cabinetmedical.exception.RessourceNotFoundException;
import com.cabinetmedical.mapper.RendezVousMapper;
import com.cabinetmedical.model.Creneau;
import com.cabinetmedical.model.Patient;
import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.enums.RdvStatus;
import com.cabinetmedical.repository.CreneauRepository;
import com.cabinetmedical.repository.PatientRepository;
import com.cabinetmedical.repository.RendezVousRepository;
import com.cabinetmedical.service.IRendezVousService;
import com.cabinetmedical.utils.Constantes;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RendezVousServiceImpl implements IRendezVousService {

	private final RendezVousRepository rendezVousRepository;
	private final PatientRepository patientRepository;
	private final CreneauRepository creneauRepository;
	private final RendezVousMapper rendezVousMapper;

	public RendezVousServiceImpl(RendezVousRepository rendezVousRepository, PatientRepository patientRepository,
			CreneauRepository creneauRepository, RendezVousMapper rendezVousMapper) {
		this.rendezVousRepository = rendezVousRepository;
		this.patientRepository = patientRepository;
		this.creneauRepository = creneauRepository;
		this.rendezVousMapper = rendezVousMapper;
	}

	@Override
	@Transactional
	public RendezVousDTO reserverCreneau(Long patientId, Long creneauId) {
		log.info("[RDV] Réservation demandée — patientId={}, creneauId={}", patientId, creneauId);

		Patient patient = patientRepository.findById(patientId).orElseThrow(
				() -> new RessourceNotFoundException(String.format(Constantes.PATIENT_NON_TROUVE_PAR_ID, patientId)));

		Creneau creneau = creneauRepository.findById(creneauId).orElseThrow(
				() -> new RessourceNotFoundException(String.format(Constantes.CRENEAU_NON_TROUVE_PAR_ID, creneauId)));

		if (Boolean.FALSE.equals(creneau.isActif())) {
			throw new OperationFailedException(Constantes.CRENEAU_DESACTIVE_RESERVATION_IMPOSSIBLE);
		}
		if (creneau.getRendezVous() != null) {
			throw new OperationFailedException(Constantes.CRENEAU_DEJA_RESERVE);
		}
		if (creneau.getDebut().isBefore(LocalDateTime.now())) {
			throw new OperationFailedException(Constantes.CRENEAU_DEJA_PASSE);
		}

		RendezVous rdv = RendezVous.builder().patient(patient).creneau(creneau).creeLe(LocalDateTime.now())
				.status(RdvStatus.CONFIRME).build();

		try {
			RendezVous saved = rendezVousRepository.save(rdv);
			log.info("[RDV] Réservation OK — rdvId={}, patientId={}, creneauId={}", saved.getId(), patientId,
					creneauId);
			return rendezVousMapper.toDTO(saved);

		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			// Course critique attrapée par la contrainte unique (creneau_id unique)
			log.error("[RDV] Contrainte d'unicité — créneau déjà réservé — creneauId={}", creneauId, e);
			throw new OperationFailedException(Constantes.CRENEAU_DEJA_RESERVE);
		}
	}

	@Override
	public RendezVousDTO annuler(Long rendezVousId) {
		log.info("[RDV] Annulation demandée — rdvId={}", rendezVousId);

		RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).orElseThrow(
				() -> new RessourceNotFoundException(Constantes.RENDEZVOUS_NON_TROUVE_PAR_ID + rendezVousId));

		if (rendezVous.getStatus() == RdvStatus.ANNULE) {
			log.info("[RDV] Déjà annulé — rdvId={}", rendezVousId);
			return rendezVousMapper.toDTO(rendezVous);
		}

		rendezVous.setStatus(RdvStatus.ANNULE);
		RendezVous saved = rendezVousRepository.save(rendezVous);

		log.info("[RDV] Annulation OK — rdvId={}", rendezVousId);
		return rendezVousMapper.toDTO(saved);
	}

	@Override
	public RendezVousDTO marquerHonore(Long rendezVousId) {
		log.info("[RDV] Marquage HONORE — rdvId={}", rendezVousId);

		RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).orElseThrow(
				() -> new RessourceNotFoundException(Constantes.RENDEZVOUS_NON_TROUVE_PAR_ID + rendezVousId));

		if (rendezVous.getCreneau().getFin().isAfter(LocalDateTime.now())) {
			throw new IllegalStateException(Constantes.CRENEAU_NON_TERMINE_HISTORISATION_IMPOSSIBLE_HONORE);
		}

		rendezVous.setStatus(RdvStatus.HONORE);
		RendezVous saved = rendezVousRepository.save(rendezVous);

		log.info("[RDV] Marqué HONORE — rdvId={}", rendezVousId);
		return rendezVousMapper.toDTO(saved);
	}

	@Override
	public RendezVousDTO marquerNonPresent(Long rendezVousId) {
		log.info("[RDV] Marquage NON_PRESENT — rdvId={}", rendezVousId);

		RendezVous rendezVous = rendezVousRepository.findById(rendezVousId).orElseThrow(
				() -> new RessourceNotFoundException(Constantes.RENDEZVOUS_NON_TROUVE_PAR_ID + rendezVousId));

		if (rendezVous.getCreneau().getFin().isAfter(LocalDateTime.now())) {
			throw new IllegalStateException(Constantes.CRENEAU_NON_TERMINE_HISTORISATION_IMPOSSIBLE_NON_PRESENT);
		}

		rendezVous.setStatus(RdvStatus.NON_PRESENT);
		RendezVous saved = rendezVousRepository.save(rendezVous);

		log.info("[RDV] Marqué NON_PRESENT — rdvId={}", rendezVousId);
		return rendezVousMapper.toDTO(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public RendezVousDTO getRendezVousById(Long id) {
		log.info("[RENDEZVOUS] GetById — id={}", id);
		RendezVous rv = rendezVousRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.RENDEZVOUS_NON_TROUVE_PAR_ID + id));
		return rendezVousMapper.toDTO(rv);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<RendezVousDTO> getAllRendezVous(Pageable pageable) {
		log.info("[RENDEZVOUS] GetAll — pageable={}", pageable);
		Page<RendezVous> page = rendezVousRepository.findAll(pageable);
		return createPagedResponse(page);
	}

	@Override
	public RendezVousDTO updateRendezVous(Long id, RendezVousDTO rendezVousDTO) {
		log.info("[RENDEZVOUS] Update — id={}", id);

		RendezVous existingRendezVous = rendezVousRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.RENDEZVOUS_NON_TROUVE_PAR_ID + id));

		existingRendezVous.setCreeLe(rendezVousDTO.creeLe());
		existingRendezVous.setStatus(rendezVousDTO.status());

		RendezVous updated = rendezVousRepository.save(existingRendezVous);
		log.info("[RENDEZVOUS] Update OK — id={}", id);
		return rendezVousMapper.toDTO(updated);
	}

	@Override
	public void deleteRendezVous(Long id) {
		log.warn("[RENDEZVOUS] Delete — id={}", id);
		RendezVous existing = rendezVousRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.RENDEZVOUS_NON_TROUVE_PAR_ID + id));
		rendezVousRepository.deleteById(existing.getId());
		log.info("[RENDEZVOUS] Delete OK — id={}", id);
	}

	private PagedResponseDTO<RendezVousDTO> createPagedResponse(Page<RendezVous> page) {
		return new PagedResponseDTO<>(rendezVousMapper.toDTOList(page.getContent()), page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages());
	}

}