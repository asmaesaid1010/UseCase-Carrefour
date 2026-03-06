package com.cabinetmedical.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetmedical.model.Creneau;

public interface CreneauRepository extends JpaRepository<Creneau, Long> {
	Page<Creneau> findByMedecinId(Long medecinId, Pageable pageable);

	Page<Creneau> findByMedecinIdAndActifTrueAndRendezVousIsNull(Long medecinId, Pageable pageable);

	Page<Creneau> findByMedecin_IdAndDebutGreaterThanEqual(Long medecinId, LocalDateTime from, Pageable pageable);

	Page<Creneau> findByMedecinIdAndActifTrueAndRendezVousIsEmpty(Long medecinId, Pageable pageable);

}
