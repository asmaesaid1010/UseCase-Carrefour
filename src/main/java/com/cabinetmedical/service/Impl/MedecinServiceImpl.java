package com.cabinetmedical.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cabinetmedical.dto.MedecinDTO;
import com.cabinetmedical.dto.PagedResponseDTO;
import com.cabinetmedical.exception.RessourceNotFoundException;
import com.cabinetmedical.mapper.MedecinMapper;
import com.cabinetmedical.model.Medecin;
import com.cabinetmedical.model.Specialite;
import com.cabinetmedical.repository.MedecinRepository;
import com.cabinetmedical.repository.SpecialiteRepository;
import com.cabinetmedical.service.IMedecinService;

import lombok.extern.slf4j.Slf4j;
import com.cabinetmedical.utils.Constantes;

@Service
@Transactional
@Slf4j
public class MedecinServiceImpl implements IMedecinService {

	private final MedecinRepository medecinRepository;
	private final SpecialiteRepository specialiteRepository;
	private final MedecinMapper medecinMapper;

	public MedecinServiceImpl(MedecinRepository medecinRepository, SpecialiteRepository specialiteRepository,
			MedecinMapper medecinMapper) {

		this.medecinRepository = medecinRepository;
		this.specialiteRepository = specialiteRepository;
		this.medecinMapper = medecinMapper;
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedecinDTO> getMedecinsBySpecialiteCode(String specialiteCode) {
		log.info("Recherche des médecins de la spécialité — code={}", specialiteCode);

		List<Medecin> medecins = medecinRepository.findBySpecialite_Code(specialiteCode);

		return medecinMapper.toDTOList(medecins);
	}

	@Override
	public MedecinDTO addMedecinWithSpecialite(Long specialiteId, MedecinDTO medecinDTO) {
		log.info("Ajout d'un médecin à la spécialité — ID={}", specialiteId);

		Specialite specialite = specialiteRepository.findById(specialiteId).orElseThrow(
				() -> new RessourceNotFoundException(Constantes.SPECIALITE_NON_TROUVEE_PAR_ID + specialiteId));

		Medecin medecin = medecinMapper.toEntity(medecinDTO);
		medecin.setSpecialite(specialite);
		Medecin savedMedecin = medecinRepository.save(medecin);

		log.info("Médecin ajouté avec succès — ID={}", savedMedecin.getId());

		return medecinMapper.toDTO(savedMedecin);
	}

	@Override
	@Transactional(readOnly = true)
	public MedecinDTO getMedecinById(Long id) {
		log.info("Récupération du médecin — ID={}", id);

		Medecin medecin = medecinRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.MEDECIN_NON_TROUVE_PAR_ID + id));

		return medecinMapper.toDTO(medecin);
	}

	@Override
	@Transactional(readOnly = true)
	public PagedResponseDTO<MedecinDTO> getAllMedecins(Pageable pageable) {
		log.info("Récupération paginée des médecins");

		Page<Medecin> medecinPage = medecinRepository.findAll(pageable);

		return createPagedResponse(medecinPage);
	}

	@Override
	public MedecinDTO updateMedecin(Long id, MedecinDTO medecinDTO) {
		log.info("Mise à jour du médecin — ID={}", id);

		Medecin existingMedecin = medecinRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.MEDECIN_NON_TROUVE_PAR_ID + id));

		existingMedecin.setNom(medecinDTO.nom());
		existingMedecin.setPrenom(medecinDTO.prenom());
		existingMedecin.setEmail(medecinDTO.email());
		existingMedecin.setTelephone(medecinDTO.telephone());

		Medecin updatedMedecin = medecinRepository.save(existingMedecin);

		log.info("Médecin mis à jour avec succès — ID={}", updatedMedecin.getId());

		return medecinMapper.toDTO(updatedMedecin);
	}

	@Override
	public void deleteMedecin(Long id) {
		log.info("Suppression du médecin — ID={}", id);

		Medecin medecin = medecinRepository.findById(id)
				.orElseThrow(() -> new RessourceNotFoundException(Constantes.MEDECIN_NON_TROUVE_PAR_ID + id));

		medecinRepository.delete(medecin);

		log.info("Médecin supprimé avec succès — ID={}", id);
	}

	private PagedResponseDTO<MedecinDTO> createPagedResponse(Page<Medecin> medecinPage) {
		return new PagedResponseDTO<>(medecinMapper.toDTOList(medecinPage.getContent()), medecinPage.getNumber(),
				medecinPage.getSize(), medecinPage.getTotalElements(), medecinPage.getTotalPages());
	}
}