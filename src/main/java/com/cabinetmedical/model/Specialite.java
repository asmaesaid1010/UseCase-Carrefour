package com.cabinetmedical.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "specialites", indexes = { @Index(name = "ux_specialite_code", columnList = "code", unique = true) })
public class Specialite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	@NotBlank
	@Column(nullable = false, length = 50, unique = true)
	private String code;

	@Column(nullable = false, length = 150)
	private String libelle;

	public Specialite(String code, String libelle) {
		this.code = code;
		this.libelle = libelle;
	}

}
