package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Cours;
import model.Note;

public class VoireNoteController implements Initializable{
	
	  @FXML
	    private TableView<Note> table_note;

	    @FXML
	    private TableColumn<Note, String> col_specialite;

	    @FXML
	    private TableColumn<Note, String> col_evaluation;
	    
	    @FXML
	    private TableColumn<Note, String> col_semestre;

	    @FXML
	    private TableColumn<Note, String> col_matiere;

	    @FXML
	    private TableColumn<Note, Double> col_note;

	    @FXML
	    private Button btn_close;
	    
	    Connection cnx;
	    PreparedStatement st;
		ResultSet result;
		
		String nom;
		String prenom;

	    @FXML
	    void close() {
	    	btn_close.getScene().getWindow().hide();
	    }
	    
	    public void showTable(String nom, String prenom) {
	    	ObservableList<Note> listNote = FXCollections.observableArrayList();
	    	this.setNom(nom);
	    	this.setPrenom(prenom);
	    	
	    	// On récupère le matricule de l'etudiant
			String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
			String matricule = "";
		
				try {
					st = cnx.prepareStatement(sql);
					result = st.executeQuery();
					if (result.next()) {
						matricule = result.getString("matricule");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
	    	
	    	table_note.getItems().clear();
	    	String sql2 ="select nomSpecialite,nomEvaluation,semestre,nomMatiere,resultat from note,etudiant,matiere,evaluation,specialite "
	    			+ "where note.etudiant=etudiant.matricule and note.matiere=matiere.idMatiere and note.evaluation=evaluation.idEvaluation "
	    			+ "and etudiant.specialite=specialite.idSpecialite and matricule= '"+matricule+"' order by semestre";
	    	
	    	try {
				st = cnx.prepareStatement(sql2);
				result = st.executeQuery();
				while (result.next()) {
					listNote.add(new Note("", result.getString("nomSpecialite"), result.getString("nomEvaluation"), 
							"Semestre "+result.getInt("semestre"), result.getString("nomMatiere"),
							result.getDouble("resultat")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    	col_specialite.setCellValueFactory(new PropertyValueFactory<Note, String>("specialite"));
	    	col_evaluation.setCellValueFactory(new PropertyValueFactory<Note, String>("evaluation"));
	    	col_semestre.setCellValueFactory(new PropertyValueFactory<Note, String>("semestre"));
	    	col_matiere.setCellValueFactory(new PropertyValueFactory<Note, String>("matiere"));
	    	col_note.setCellValueFactory(new PropertyValueFactory<Note, Double>("note"));
	    	table_note.setItems(listNote);
	    	
	    	System.out.println("Mise à jour de la table");
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



	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTable(getNom(), getPrenom());
	}

}
