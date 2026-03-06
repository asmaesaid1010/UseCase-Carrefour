package com.cabinetmedical.model;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
@Table(name = "creneaux", uniqueConstraints = @UniqueConstraint(columnNames = { "medecin_id", "debut", "fin" }))

public class Creneau {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime debut;

	@Column(nullable = false)
	private LocalDateTime fin;

	@Column(nullable = false)
	private boolean actif = true;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "medecin_id")
	private Medecin medecin;

	@OneToOne(mappedBy = "creneau", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private RendezVous rendezVous;

	@PrePersist
	@PreUpdate
	private void validate() {
		if (fin == null || debut == null || !fin.isAfter(debut) || fin.equals(debut)) {
			throw new IllegalStateException("Creneau.fin doit être après Creneau.debut");
		}
	}

}
