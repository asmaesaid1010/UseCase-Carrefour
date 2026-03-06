package com.cabinetmedical.model;

import java.time.LocalDateTime;

import com.cabinetmedical.model.enums.RdvStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "rendez_vous", uniqueConstraints = {
		// Garantit qu'un créneau ne peut être réservé que par un seul patient à la fois
		@UniqueConstraint(name = "ux_rdv_creneau_unique", columnNames = { "creneau_id" }) })

public class RendezVous {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private RdvStatus status = RdvStatus.CONFIRME;

	@Column(nullable = false)
	private LocalDateTime creeLe = LocalDateTime.now();

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id")
	private Patient patient;

	@OneToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "creneau_id", unique = true)
	private Creneau creneau;

	public RendezVous(Patient patient, Creneau creneau) {
		this.patient = patient;
		this.creneau = creneau;
		this.status = RdvStatus.CONFIRME;
		this.creeLe = LocalDateTime.now();
	}

}
