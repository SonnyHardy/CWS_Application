package model;

import java.sql.Date;

public class Etudiant {
	String matricule;
	String nom;
	String prenom;
	String specialite;
	String adresse;
	Date dateNais;
	String sexe;
	String email;
	String telephone;
	
	
	public Etudiant() {
		super();
	}


	public Etudiant(String matricule, String nom, String prenom, String specialite, String adresse, Date dateNais,
			String sexe, String email, String telephone) {

		this.matricule = matricule;
		this.nom = nom;
		this.prenom = prenom;
		this.specialite = specialite;
		this.adresse = adresse;
		this.dateNais = dateNais;
		this.sexe = sexe;
		this.email = email;
		this.telephone = telephone;
	}


	public String getMatricule() {
		return matricule;
	}


	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public String getSpecialite() {
		return specialite;
	}


	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}


	public String getAdresse() {
		return adresse;
	}


	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}


	public Date getDateNais() {
		return dateNais;
	}


	public void setDateNais(Date dateNais) {
		this.dateNais = dateNais;
	}


	public String getSexe() {
		return sexe;
	}


	public void setSexe(String sexe) {
		this.sexe = sexe;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getTelephone() {
		return telephone;
	}


	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	

}

