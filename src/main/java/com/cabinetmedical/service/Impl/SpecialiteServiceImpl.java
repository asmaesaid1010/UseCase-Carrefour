package com.cabinetmedical.service.Impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.dto.SpecialiteDTO;
import com.cabinetmedical.exception.RessourceNotFoundException;
import com.cabinetmedical.mapper.SpecialiteMapper;
import com.cabinetmedical.model.Specialite;
import com.cabinetmedical.repository.SpecialiteRepository;
import com.cabinetmedical.service.ISpecialiteService;

import lombok.extern.slf4j.Slf4j;
import com.cabinetmedical.utils.Constantes;

@Service
@Transactional
@Slf4j
public class SpecialiteServiceImpl implements ISpecialiteService {

	private final SpecialiteRepository specialiteRepository;
	private final SpecialiteMapper specialiteMapper;

	public SpecialiteServiceImpl(SpecialiteRepository specialiteRepository, SpecialiteMapper specialiteMapper) {
		this.specialiteRepository = specialiteRepository;
		this.specialiteMapper = specialiteMapper;
	}

	@Override
	public SpecialiteDTO addSpecialite(SpecialiteDTO specialiteDTO) {
		log.info("Début d'ajout de la spécialité — code={} libellé={}", specialiteDTO.code(), specialiteDTO.libelle());

		Specialite specialite = specialiteMapper.toEntity(specialiteDTO);

		Specialite savedSpecialite = specialiteRepository.save(specialite);

		log.info("Spécialité ajoutée avec succès — ID={}", savedSpecialite.getId());

		return specialiteMapper.toDTO(savedSpecialite);
	}

	@Override
	@Transactional(readOnly = true)
	public SpecialiteDTO getSpecialiteById(Long id) {
		log.info("Récupération de la spécialité — ID={}", id);

		Specialite specialite = specialiteRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.SPECIALITE_NON_TROUVEE_PAR_ID + id));

		return specialiteMapper.toDTO(specialite);
	}

	@Override
	public SpecialiteDTO updateSpecialite(Long id, SpecialiteDTO specialiteDTO) {
		log.info("Début de mise à jour de la spécialité — ID={}", id);

		Specialite existingSpecialite = specialiteRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.SPECIALITE_NON_TROUVEE_PAR_ID + id));

		existingSpecialite.setCode(specialiteDTO.code());
		existingSpecialite.setLibelle(specialiteDTO.libelle());

		Specialite updated = specialiteRepository.save(existingSpecialite);

		log.info("Spécialité mise à jour avec succès — ID={}", updated.getId());

		return specialiteMapper.toDTO(updated);
	}

	@Override
	public void deleteSpecialite(Long id) {
		log.info("Début de suppression de la spécialité — ID={}", id);

		Specialite specialite = specialiteRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.SPECIALITE_NON_TROUVEE_PAR_ID + id));

		specialiteRepository.delete(specialite);
		log.info("Spécialité supprimée avec succès — ID={}", id);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<SpecialiteDTO> getAllSpecialites(Pageable pageable) {
		Page<Specialite> page = specialiteRepository.findAll(pageable);
		return createPagedResponse(page);
	}

	@Override
	@Transactional(readOnly = true)
	public SpecialiteDTO getSpecialitesByCode(String code) {
		Specialite specialite = specialiteRepository.findByCodeIgnoreCase(code).orElseThrow(
				() -> new RessourceNotFoundException(Constantes.AUCUNE_SPECIALITE_TROUVEE_PAR_CODE + code));

		return specialiteMapper.toDTO(specialite);
	}

	private PagedResponseDTO<SpecialiteDTO> createPagedResponse(Page<Specialite> specialitePage) {
		return new PagedResponseDTO<>(specialiteMapper.toDTOList(specialitePage.getContent()),
				specialitePage.getNumber(), specialitePage.getSize(), specialitePage.getTotalElements(),
				specialitePage.getTotalPages());
	}
}