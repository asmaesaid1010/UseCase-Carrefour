package com.cabinetmedical.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinetmedical.dto.CreneauDTO;
import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.exception.RessourceNotFoundException;
import com.cabinetmedical.mapper.CreneauMapper;
import com.cabinetmedical.model.Creneau;
import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.repository.CreneauRepository;
import com.cabinetmedical.repository.MedecinRepository;
import com.cabinetmedical.service.ICreneauService;

import lombok.extern.slf4j.Slf4j;
import com.cabinetmedical.utils.Constantes;

@Service
@Transactional
@Slf4j
public class CreneauServiceImpl implements ICreneauService {

	private final CreneauRepository creneauRepository;
	private final MedecinRepository medecinRepository;
	private final CreneauMapper creneauMapper;

	public CreneauServiceImpl(CreneauRepository creneauRepository, MedecinRepository medecinRepository,
			CreneauMapper creneauMapper) {
		this.creneauRepository = creneauRepository;
		this.medecinRepository = medecinRepository;
		this.creneauMapper = creneauMapper;
	}

	@Override
	public CreneauDTO addCreneauWithMedecin(Long medecinId, CreneauDTO creneauDTO) {

		log.info("[CRENEAU] Création demandée — medecinId={}, début={}, fin={}", medecinId, creneauDTO.debut(),
				creneauDTO.fin());

		Medecin medecin = medecinRepository.findById(medecinId)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.MEDECIN_NON_TROUVE_PAR_ID + medecinId));

		Creneau creneau = creneauMapper.toEntity(creneauDTO);
		creneau.setMedecin(medecin);

		Creneau saved = creneauRepository.save(creneau);

		log.info("[CRENEAU] Créneau créé — id={}, medecinId={}", saved.getId(), medecinId);

		return creneauMapper.toDTO(saved);
	}

	@Override
	@Transactional(readOnly = true)
	public CreneauDTO getCreneauById(Long id) {

		log.info("[CRENEAU] Consultation — creneauId={}", id);

		Creneau creneau = creneauRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.CRENEAU_NON_TROUVE_PAR_ID + id));

		return creneauMapper.toDTO(creneau);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<CreneauDTO> getCreneauxDisponiblByMedecin(Long medecinId, Pageable pageable) {

		log.debug("[CRENEAU] Listing par médecin — medecinId={}, page={}, size={}, sort={}", medecinId,
				pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

		medecinRepository.findById(medecinId).orElseThrow(
				() -> new RessourceNotFoundException(String.format(Constantes.MEDECIN_NON_TROUVE_PAR_ID, medecinId)));

		Page<Creneau> page = creneauRepository.findByMedecinIdAndActifTrueAndRendezVousIsNull(medecinId, pageable);

		log.info("[CRENEAU] Listing OK — medecinId={}, totalElements={}, pageIndex={}, pageSize={}, totalPages={}",
				medecinId, page.getTotalElements(), page.getNumber(), page.getSize(), page.getTotalPages());

		return createPagedResponse(page);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<CreneauDTO> getAllCreneaux(Pageable pageable) {

		log.info("[CRENEAU] Listing paginé demandé.");

		Page<Creneau> page = creneauRepository.findAll(pageable);

		return createPagedResponse(page);
	}

	@Override
	public CreneauDTO updateCreneau(Long id, CreneauDTO creneauDTO) {

		log.info("[CRENEAU] Mise à jour — id={}", id);

		Creneau existing = creneauRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.CRENEAU_NON_TROUVE_PAR_ID + id));

		existing.setDebut(creneauDTO.debut());
		existing.setFin(creneauDTO.fin());
		existing.setActif(creneauDTO.actif());

		Creneau updated = creneauRepository.save(existing);

		log.info("[CRENEAU] Mise à jour OK — id={}", id);

		return creneauMapper.toDTO(updated);
	}

	@Override
	public void deleteCreneau(Long id) {

		log.warn("[CRENEAU] Suppression demandée — creneauId={}", id);

		Creneau creneau = creneauRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.CRENEAU_NON_TROUVE_PAR_ID + id));

		creneauRepository.delete(creneau);

		log.info("[CRENEAU] Suppression OK — id={}", id);
	}

	private PagedResponseDTO<CreneauDTO> createPagedResponse(Page<Creneau> page) {
		return new PagedResponseDTO<>(creneauMapper.toDTOList(page.getContent()), page.getNumber(), page.getSize(),
				page.getTotalElements(), page.getTotalPages());
	}

	@Override
	public CreneauDTO desactiverCreneau(Long id) {
		log.info("[CRENEAU] Désactivation demandée — creneauId={}", id);

		Creneau creneau = creneauRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.CRENEAU_NON_TROUVE_PAR_ID + id));

		if (creneau.getRendezVous() != null) {
			log.warn("[CRENEAU] Désactivation refusée (réservé) — creneauId={}, rendezVousId={}", id,
					creneau.getRendezVous().getId());
			throw new IllegalStateException(Constantes.IMPOSSIBLE_DESACTIVER_CRENEAU_RESERVE);
		}

		if (Boolean.FALSE.equals(creneau.isActif())) {
			log.info("[CRENEAU] Déjà inactif — creneauId={}", id);
			return creneauMapper.toDTO(creneau);
		}

		creneau.setActif(false);
		Creneau saved = creneauRepository.save(creneau);

		log.info("[CRENEAU] Désactivation OK — creneauId={}", id);
		return creneauMapper.toDTO(saved);
	}

	@Override
	public CreneauDTO activerCreneau(Long id) {
		log.info("[CRENEAU] Activation demandée — creneauId={}", id);

		Creneau creneau = creneauRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.CRENEAU_NON_TROUVE_PAR_ID + id));

		if (Boolean.TRUE.equals(creneau.isActif())) {
			log.info("[CRENEAU] Déjà actif — creneauId={}", id);
			return creneauMapper.toDTO(creneau);
		}

		creneau.setActif(true);
		Creneau saved = creneauRepository.save(creneau);

		log.info("[CRENEAU] Activation OK — creneauId={}", id);
		return creneauMapper.toDTO(saved);
	}

}