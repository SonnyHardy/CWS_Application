package model;

public class Note {
	
	private String etudiant;
	private String specialite;
	private String evaluation;
	private String semestre;
	private String matiere;
	private double note;
	
	public Note() {
		
	}

	public Note(String etudiant, String specialite, String evaluation, String semestre, String matiere, double note) {
		super();
		this.etudiant = etudiant;
		this.specialite = specialite;
		this.evaluation = evaluation;
		this.semestre = semestre;
		this.matiere = matiere;
		this.note = note;
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

	public String getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	
	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getMatiere() {
		return matiere;
	}

	public void setMatiere(String matiere) {
		this.matiere = matiere;
	}

	public double getNote() {
		return note;
	}

	public void setNote(double note) {
		this.note = note;
	}
	
	
}
