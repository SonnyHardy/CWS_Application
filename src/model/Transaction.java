package model;

import java.sql.Date;

public class Transaction {
	private String numRecu;
	private String etudiant;
	private String specialite;
	private String banque;
	private String motif;
	private int montant;
	private Date date;
	
	public Transaction() {
		
	}

	public Transaction(String numRecu, String etudiant, String specialite, String banque, String motif, int montant,
			Date date) {
		super();
		this.numRecu = numRecu;
		this.etudiant = etudiant;
		this.specialite = specialite;
		this.banque = banque;
		this.motif = motif;
		this.montant = montant;
		this.date = date;
	}

	public String getNumRecu() {
		return numRecu;
	}

	public void setNumRecu(String numRecu) {
		this.numRecu = numRecu;
	}

	public String getEtudiant() {
		return etudiant;
	}

	public void setEtudiant(String etudiant) {
		this.etudiant = etudiant;
	}

	public String getSpecialite() {
		return specialite;
	}

	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}

	public String getBanque() {
		return banque;
	}

	public void setBanque(String banque) {
		this.banque = banque;
	}

	public String getMotif() {
		return motif;
	}

	public void setMotif(String motif) {
		this.motif = motif;
	}

	public int getMontant() {
		return montant;
	}

	public void setMontant(int montant) {
		this.montant = montant;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
