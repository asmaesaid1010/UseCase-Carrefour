package com.cabinetmedical.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cabinetmedical.model.Medecin;

public interface MedecinRepository extends JpaRepository<Medecin, Long> {
	Optional<Medecin> findByEmail(String email);

	List<Medecin> findBySpecialite_Code(String code);

	List<Medecin> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

}
