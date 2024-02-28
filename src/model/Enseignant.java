package model;

public class Enseignant {

	String cni;
	String nom;
	String prenom;
	String sexe;
	String adresse;
	String contrat;
	String email;
	String telephone;
	int salaire;
	
	public Enseignant() {
		
	}

	public Enseignant(String cni, String nom, String prenom, String sexe, String adresse, String contrat, String email,
			String telephone, int salaire) {
		super();
		this.cni = cni;
		this.nom = nom;
		this.prenom = prenom;
		this.sexe = sexe;
		this.adresse = adresse;
		this.contrat = contrat;
		this.email = email;
		this.telephone = telephone;
		this.salaire = salaire;
	}

	public String getCni() {
		return cni;
	}

	public void setCni(String cni) {
		this.cni = cni;
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

	public String getSexe() {
		return sexe;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getContrat() {
		return contrat;
	}

	public void setContrat(String contrat) {
		this.contrat = contrat;
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

	public int getSalaire() {
		return salaire;
	}

	public void setSalaire(int salaire) {
		this.salaire = salaire;
	}
	
	
	
}
