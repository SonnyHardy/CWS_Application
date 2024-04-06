package model;

public class Releve {
	
	private String idMatiere;
	private String nomMatiere;
	private int creditPrev;
	private double note;
	private int creditAc;
	private String grade;
	private String decision;
	private String session;
	
	public Releve() {
		
	}

	public Releve(String idMatiere, String nomMatiere, int creditPrev, double note, int creditAc, String grade,
			String decision, String session) {
		super();
		this.idMatiere = idMatiere;
		this.nomMatiere = nomMatiere;
		this.creditPrev = creditPrev;
		this.note = note;
		this.creditAc = creditAc;
		this.grade = grade;
		this.decision = decision;
		this.session = session;
	}

	
	public String getIdMatiere() {
		return idMatiere;
	}

	public void setIdMatiere(String idMatiere) {
		this.idMatiere = idMatiere;
	}

	public String getNomMatiere() {
		return nomMatiere;
	}

	public void setNomMatiere(String nomMatiere) {
		this.nomMatiere = nomMatiere;
	}

	public int getCreditPrev() {
		return creditPrev;
	}

	public void setCreditPrev(int creditPrev) {
		this.creditPrev = creditPrev;
	}

	public double getNote() {
		return note;
	}

	public void setNote(double note) {
		this.note = note;
	}

	public int getCreditAc() {
		return creditAc;
	}

	public void setCreditAc(int creditAc) {
		this.creditAc = creditAc;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}
	
}
