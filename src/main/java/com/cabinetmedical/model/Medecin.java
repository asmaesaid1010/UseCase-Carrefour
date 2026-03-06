package com.cabinetmedical.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "medecins", indexes = { @Index(name = "ux_medecin_email", columnList = "email", unique = true),
		@Index(name = "ix_medecin_specialite", columnList = "specialite_id") })
public class Medecin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String nom;

	@Column(nullable = false, length = 100)
	private String prenom;

	@Column(nullable = false, length = 120, unique = true)
	private String email;

	@Column(length = 40)
	private String telephone;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "specialite_id")
	private Specialite specialite;

}
