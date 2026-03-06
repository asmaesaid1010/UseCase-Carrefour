package com.cabinetmedical.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetmedical.model.Specialite;

public interface SpecialiteRepository extends JpaRepository<Specialite, Long> {
	Optional<Specialite> findByCodeIgnoreCase(String code);

	boolean existsByCode(String code);
}
