package com.cabinetmedical.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cabinetmedical.model.RendezVous;
import com.cabinetmedical.model.enums.RdvStatus;

public interface RendezVousRepository extends JpaRepository<RendezVous, Long> {
	Page<RendezVous> findByPatientId(Long patientId, Pageable pageable);

	Optional<RendezVous> findByCreneauId(Long creneauId);

	boolean existsByCreneau_Id(Long creneauId);

	Page<RendezVous> findByPatient_IdOrderByCreeLeDesc(Long patientId, Pageable pageable);

	Page<RendezVous> findByPatient_IdAndStatus(Long patientId, RdvStatus status, Pageable pageable);

	@Query("SELECT COUNT(r) FROM RendezVous r WHERE r.patient.id = :patientId")
	long countByPatientId(Long patientId);

	long countByPatient_Id(Long patientId);

}