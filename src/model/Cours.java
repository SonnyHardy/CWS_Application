package model;

public class Cours {

	String enseignant;
	String specialite;
	String matiere;
	String volume;
	
	public Cours() {
		super();
	}
	
	public Cours(String enseignant, String specialite, String matiere, String volume) {
		super();
		this.enseignant = enseignant;
		this.specialite = specialite;
		this.matiere = matiere;
		this.volume = volume;
	}
	
	
	public String getEnseignant() {
		return enseignant;
	}
	public void setEnseignant(String enseignant) {
		this.enseignant = enseignant;
	}
	public String getSpecialite() {
		return specialite;
	}
	public void setSpecialite(String specialite) {
		this.specialite = specialite;
	}
	public String getMatiere() {
		return matiere;
	}
	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	
	
	
}
