package com.cabinetmedical.utils;

public class Constantes {

	private Constantes() {
		throw new UnsupportedOperationException("Classe utilitaire");
	}

	public static final String MEDECIN_NON_TROUVE_PAR_ID = "Médecin non trouvé avec l'ID : ";

	public static final String PATIENT_NON_TROUVE_PAR_ID = "Patient non trouvé avec l'ID : ";

	public static final String CRENEAU_NON_TROUVE_PAR_ID = "Créneau non trouvé avec l'ID : ";

	public static final String SPECIALITE_NON_TROUVEE_PAR_ID = "Spécialité non trouvée avec l'ID :  ";

	public static final String AUCUN_PATIENT_TROUVE_PAR_EMAIL = "Aucun patient trouvé pour l'email :  ";

	public static final String AUCUNE_SPECIALITE_TROUVEE_PAR_CODE = "Aucune spécialité trouvée avec le code :  ";

	public static final String RENDEZVOUS_NON_TROUVE_PAR_ID = "Rendez-vous non trouvé avec l'ID :  ";

	public static final String CRENEAU_DESACTIVE_RESERVATION_IMPOSSIBLE = "Le créneau est désactivé, réservation impossible.";

	public static final String CRENEAU_DEJA_COMMENCE_OU_PASSE = "Impossible de réserver un créneau déjà commencé ou passé.";

	public static final String CRENEAU_NON_TERMINE_HISTORISATION_IMPOSSIBLE_HONORE = "Le créneau n'est pas encore terminé, impossible d'historiser en HONORE.";

	public static final String CRENEAU_NON_TERMINE_HISTORISATION_IMPOSSIBLE_NON_PRESENT = "Le créneau n'est pas encore terminé, impossible d'historiser en NON_PRESENT.";

	public static final String CRITERES_RECHERCHE_OBLIGATOIRES = "Au moins un critère de recherche doit être fourni.";

	public static final String IMPOSSIBLE_MAJ_PATIENT = "Impossible de mettre à jour le patient :  ";

	public static final String IMPOSSIBLE_SUPPRIMER_PATIENT = "Impossible de supprimer le patient :  ";

	public static final String IMPOSSIBLE_MAJ_SPECIALITE = "Impossible de mettre à jour la spécialité :  ";

	public static final String IMPOSSIBLE_SUPPRIMER_SPECIALITE = "Impossible de supprimer la spécialité :  ";

	public static final String IMPOSSIBLE_DESACTIVER_CRENEAU_RESERVE = "Impossible de désactiver un créneau déjà réservé.";

	public static final String IMPOSSIBLE_ANNULER_RDV_CRENEAU_PASSE = "Impossible d'annuler un rendez-vous dont le créneau est déjà passé.";

	public static final String ERREUR_INTERNE = "Erreur interne, veuillez contacter l’administrateur";

	public static final String CRENEAU_DEJA_RESERVE = "Créneau déjà réservé";

	public static final String CRENEAU_DEJA_PASSE = "Créneau déjà passé";

	public static final String CRENEAU_NON_ASSOCIE_AU_MEDECIN = "Le créneau n'appartient pas au médecin";

	public static final String LOG_PREFIX_CRENEAU = "[CRENEAU]";
	public static final String LOG_PREFIX_MEDECIN = "[MEDECIN]";
	public static final String LOG_PREFIX_PATIENT = "[PATIENT]";
	public static final String LOG_PREFIX_RENDEZVOUS = "[RENDEZVOUS]";

	public static final String LOG_CREATION_DEMANDEE = "Création demandée —  ";
	public static final String LOG_CREATION_OK = "Création OK — id= ";
	public static final String LOG_CONSULTATION = "Consultation — id= ";
	public static final String LOG_LISTING_PAGINE_DEMANDE = "Listing paginé demandé";
	public static final String LOG_MISE_A_JOUR = "Mise à jour — id= ";
	public static final String LOG_MISE_A_JOUR_OK = "Mise à jour OK — id= ";
	public static final String LOG_SUPPRESSION_DEMANDEE = "Suppression demandée — id= ";
	public static final String LOG_SUPPRESSION_OK = "Suppression OK — id= ";
	public static final String LOG_RECHERCHE = "Recherche —  ";

}
