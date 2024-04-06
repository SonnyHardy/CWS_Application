package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import model.Cours;
import model.Note;
import model.Transaction;

public class NoteController implements Initializable{
	

    @FXML
    private JFXButton btn_ajouter;

    @FXML
    private JFXButton btn_nouveau;

    @FXML
    private JFXButton btn_supprimer;

    @FXML
    private JFXButton btn_modifier;

    @FXML
    private ComboBox<String> cbx_etudiant;
    
    @FXML
    private ComboBox<String> cbx_specialite;

    @FXML
    private ComboBox<String> cbx_evaluation;

    @FXML
    private ComboBox<String> cbx_matiere;

    @FXML
    private TextField txt_note;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<Note> table_note;

    @FXML
    private TableColumn<Note, String> col_etudiant;

    @FXML
    private TableColumn<Note, String> col_specialite;

    @FXML
    private TableColumn<Note, String> col_evaluation;

    @FXML
    private TableColumn<Note, String> col_matiere;

    @FXML
    private TableColumn<Note, Double> col_note;
    
    Connection cnx;
    PreparedStatement st, st2;
	ResultSet result, result2;
	
	public void ajouter() throws SQLException {
		// On recupere le matricule de l'etudiant
		String nom = "";
		String prenom = "";
		String[] nameParts = cbx_etudiant.getValue().split(" ");
		if (nameParts.length == 2) {
			nom = nameParts[0];
			prenom = nameParts[1];
		}
		String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
		String matricule = "";
	
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if (result.next()) {
				matricule = result.getString("matricule");
			}
		
		// On recupere l'id de l'evaluation
		String sql2 = "select idEvaluation from evaluation where nomEvaluation= '"+cbx_evaluation.getValue()+"'";
		int evaluation = 0;
		
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
			if (result.next()) {
				evaluation = result.getInt("idEvaluation");
			}
		
		// On recupere l'id de la matiere
		String sql3 = "select idMatiere from matiere,specialite where matiere.specialite=specialite.idSpecialite and "
				+ "nomMatiere= '"+cbx_matiere.getValue()+"' and nomSpecialite= '"+cbx_specialite.getValue()+"'";
		String matiere = "";
	
			st = cnx.prepareStatement(sql3);
			result = st.executeQuery();
			if (result.next()) {
				matiere = result.getString("idMatiere");
			}
		
		String sql4 = "insert into note(etudiant,evaluation,matiere,resultat) values (?,?,?,?)";
		
			st = cnx.prepareStatement(sql4);
			st.setString(1, matricule);
			st.setInt(2, evaluation);
			st.setString(3, matiere);
			st.setDouble(4, Double.parseDouble(txt_note.getText()));
			st.executeUpdate();
			
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("La note a été ajoutée avec succès !");
			 alert.showAndWait();
			 showTable();
	
	}
	
	public void modifier() throws SQLException {
		Note note = table_note.getSelectionModel().getSelectedItem();
		
		// On recupère le matricule de l'etudiant
		String nom = "";
		String prenom = "";
		String[] nameParts = cbx_etudiant.getValue().split(" ");
		if (nameParts.length == 2) {
			nom = nameParts[0]; 
			prenom = nameParts[1];
		}
		String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
		String matricule = "";
		st = cnx.prepareStatement(sql);
		result = st.executeQuery();
		if (result.next()) {
			matricule = result.getString("matricule");
		}
		
		// On recupère le matricule de l'etudiant à modifier
		String nom2 = "";
		String prenom2 = "";
		String[] nameParts2 = note.getEtudiant().split(" ");
		if (nameParts2.length == 2) {
			nom2 = nameParts2[0]; 
			prenom2 = nameParts2[1];
		}
		String sql1 = "select matricule from etudiant where nom= '"+ nom2 +"' and prenom= '"+ prenom2 +"'";
		String matricule2 = "";
		st = cnx.prepareStatement(sql1);
		result = st.executeQuery();
		if (result.next()) {
			matricule2 = result.getString("matricule");
		}
		
		// On recupere l'id de l'evaluation
		String sql2 = "select idEvaluation from evaluation where nomEvaluation= '"+cbx_evaluation.getValue()+"'";
		int evaluation = 0;
		
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
			if (result.next()) {
				evaluation = result.getInt("idEvaluation");
			}
			
			// On recupere l'id de l'evaluation à modifier
			String sql22 = "select idEvaluation from evaluation where nomEvaluation= '"+note.getEvaluation()+"'";
			int evaluation2 = 0;
			
			st = cnx.prepareStatement(sql22);
			result = st.executeQuery();
			if (result.next()) {
				evaluation2 = result.getInt("idEvaluation");
			}
		
		// On recupere l'id de la matiere
		String sql3 = "select idMatiere from matiere where nomMatiere= '"+cbx_matiere.getValue()+"'";
		String matiere = "";
	
			st = cnx.prepareStatement(sql3);
			result = st.executeQuery();
			if (result.next()) {
				matiere = result.getString("idMatiere");
			}
			
			// On recupere l'id de la matiere à modifier
			String sql33 = "select idMatiere from matiere where nomMatiere= '"+note.getMatiere()+"'";
			String matiere2 = "";
			
			st = cnx.prepareStatement(sql33);
			result = st.executeQuery();
			if (result.next()) {
				matiere2 = result.getString("idMatiere");
			}
		
		String sql4 = "update note set etudiant=?, evaluation=?, matiere=?, resultat=? where etudiant= '"+matricule2+"' "
				+ "and evaluation= "+evaluation2+" and matiere= '"+matiere2+"'";
		st = cnx.prepareStatement(sql4);
		st.setString(1, matricule);
		st.setInt(2, evaluation);
		st.setString(3, matiere);
		st.setDouble(4, Double.parseDouble(txt_note.getText()));
		st.executeUpdate();
		clearAll();
		showTable();
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		 alert.setHeaderText("CWS Application");
		 alert.setContentText("Note modifiée avec succès !");
		 alert.showAndWait();
	}

    @FXML
    void ajouterNote() {
    	if (!txt_note.getText().isBlank() && cbx_etudiant.getValue() != null && cbx_evaluation.getValue() != null 
    			&& cbx_matiere.getValue() != null && cbx_specialite.getValue() != null) {
    		
    		
        	String nom = "";
    		String prenom = "";
    		String[] nameParts = cbx_etudiant.getValue().split(" ");
    		if (nameParts.length == 2) {
    			nom = nameParts[0];
    			prenom = nameParts[1];
    		}
        	String sql = "select nom,prenom,nomEvaluation,nomMatiere from note,etudiant,evaluation,matiere where "
        			+ "note.etudiant=etudiant.matricule and note.evaluation=evaluation.idEvaluation "
        			+ "and note.matiere=matiere.idMatiere and nom= '"+nom+"' and prenom= '"+prenom+"' "
        					+ "and nomEvaluation= '"+cbx_evaluation.getValue()+"' and nomMatiere= '"+cbx_matiere.getValue()+"'";
        	
        	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Un étudiant ne peut pas avoir deux notes sur une meme matière");
					 alert.showAndWait();
				}else {
					ajouter();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} 
        	
    	}else {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("Veuillez remplir correctement tous les champs");
			 alert.showAndWait();
    	} 
        	
    	
    }

    @FXML
    void chercherNote(KeyEvent event) throws SQLException {
    	table_note.getItems().clear();
		 ObservableList<Note> listNote = FXCollections.observableArrayList();

				String sql3 = "select nom,prenom,nomSpecialite,nomEvaluation,nomMatiere,resultat from note,specialite,etudiant,matiere,evaluation "
   	    			+ "where note.etudiant=etudiant.matricule and note.matiere=matiere.idMatiere and etudiant.specialite=specialite.idSpecialite and "
   	    			+ "note.evaluation=evaluation.idEvaluation and (nom like '%"+txt_search.getText()+"%' or prenom like '%"+txt_search.getText()+"%' "
   	    					+ "or nomMatiere like '%"+txt_search.getText()+"%' or nomEvaluation like '%"+txt_search.getText()+"%' or nomSpecialite like '%"+txt_search.getText()+"%') order by nom";
   			
   			st = cnx.prepareStatement(sql3);
   			result = st.executeQuery();
   			while (result.next()) {
   				String nom = result.getString("nom");
					String prenom = result.getString("prenom");
					String etudiant = nom + " " + prenom;
					
					listNote.add(new Note(etudiant, result.getString("nomSpecialite"), result.getString("nomEvaluation"), "",
							result.getString("nomMatiere"), result.getDouble("resultat")));
			}
			
	    	col_etudiant.setCellValueFactory(new PropertyValueFactory<Note, String>("etudiant"));
	    	col_specialite.setCellValueFactory(new PropertyValueFactory<Note, String>("specialite"));
	    	col_evaluation.setCellValueFactory(new PropertyValueFactory<Note, String>("evaluation"));
	    	col_matiere.setCellValueFactory(new PropertyValueFactory<Note, String>("matiere"));
	    	col_note.setCellValueFactory(new PropertyValueFactory<Note, Double>("note"));
	    	
	    	table_note.setItems(listNote);
	    	//btn_modifier.setDisable(true);
	    	//btn_supprimer.setDisable(true);
	    	System.out.println("Mise à jour de la table");
    }

    @FXML
    void modifierNote() throws SQLException {
    	if (!txt_note.getText().isBlank() && cbx_etudiant.getValue() != null && cbx_evaluation.getValue() != null 
    			&& cbx_matiere.getValue() != null && cbx_specialite.getValue() != null) {
    		
    		modifier();
    		/*String nom = "";
    		String prenom = "";
    		String[] nameParts = cbx_etudiant.getValue().split(" ");
    		if (nameParts.length == 2) {
    			nom = nameParts[0];
    			prenom = nameParts[1];
    		}
        	String sql = "select nom,prenom,nomEvaluation,nomMatiere from note,etudiant,evaluation,matiere where "
        			+ "note.etudiant=etudiant.matricule and note.evaluation=evaluation.idEvaluation "
        			+ "and note.matiere=matiere.idMatiere and nom= '"+nom+"' and prenom= '"+prenom+"' "
        					+ "and nomEvaluation= '"+cbx_evaluation.getValue()+"' and nomMatiere= '"+cbx_matiere.getValue()+"'";
        	
        	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Un étudiant ne peut pas avoir deux notes sur une meme matière");
					 alert.showAndWait();
				}else {
					modifier();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} */
    	}else {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("Veuillez remplir correctement tous les champs");
			 alert.showAndWait();
    	}
    }

    @FXML
    void nouveau() {
    	clearAll();
    }

    @FXML
    void supprimerNote() throws SQLException {
    	if (!txt_note.getText().isBlank() && cbx_etudiant.getValue() != null && cbx_evaluation.getValue() != null 
    			&& cbx_matiere.getValue() != null && cbx_specialite.getValue() != null) {
    		
    		// On recupere le matricule de l'etudiant
    		String nom = "";
    		String prenom = "";
    		String[] nameParts = cbx_etudiant.getValue().split(" ");
    		if (nameParts.length == 2) {
    			nom = nameParts[0];
    			prenom = nameParts[1];
    		}
    		String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
    		String matricule = "";
    	
    			st = cnx.prepareStatement(sql);
    			result = st.executeQuery();
    			if (result.next()) {
    				matricule = result.getString("matricule");
    			}
    			
    			// On recupere l'id de l'evaluation
    			String sql2 = "select idEvaluation from evaluation where nomEvaluation= '"+cbx_evaluation.getValue()+"'";
    			int evaluation = 0;
    			
    				st = cnx.prepareStatement(sql2);
    				result = st.executeQuery();
    				if (result.next()) {
    					evaluation = result.getInt("idEvaluation");
    				}
    				
    				// On recupere l'id de la matiere
    				String sql3 = "select idMatiere from matiere where nomMatiere= '"+cbx_matiere.getValue()+"'";
    				String matiere = "";
    			
    					st = cnx.prepareStatement(sql3);
    					result = st.executeQuery();
    					if (result.next()) {
    						matiere = result.getString("idMatiere");
    					}
    		
    		String sql4 = "delete from note where etudiant= '"+matricule+"' and evaluation= "+evaluation+" and "
    				+ "matiere = '"+matiere+"'";
	    	
	    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("Voulez-vous vraiment supprimer cette note ?");
			 alert.showAndWait().ifPresent(response ->{
				 if (response == ButtonType.OK) {
					 try {
							st = cnx.prepareStatement(sql4);
							st.executeUpdate();
							
							clearAll();
							showTable();
							btn_modifier.setDisable(true);
					    	btn_supprimer.setDisable(true);
					    	Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
							 alert2.setHeaderText("CWS Application");
							 alert2.setContentText("Note supprimée avec succès !");
							 alert2.showAndWait();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				 }
			 });
			 
    	}else {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("Veuillez remplir correctement tous les champs");
			 alert.showAndWait();
    	}
    }
    
    /*public String getNomSpecialite(int idSpecialite) {
    	String specialite = "";
    	String sql = "select nomSpecialite from specialite where idSpecialite= "+idSpecialite;
    	try {
			st2 = cnx.prepareStatement(sql);
			result2 = st2.executeQuery();
			if (result2.next()) {
				specialite = result2.getString("nomSpecialite");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return specialite;
    } */
    
    
    public void showTable() {
    	table_note.getItems().clear();
    	ObservableList<Note> listNote = FXCollections.observableArrayList();
    	String sql = "select nom,prenom,nomSpecialite,nomEvaluation,nomMatiere,resultat from note,etudiant,matiere,evaluation,specialite "
    			+ "where note.etudiant=etudiant.matricule and note.matiere=matiere.idMatiere and note.evaluation=evaluation.idEvaluation "
    			+ "and etudiant.specialite=specialite.idSpecialite order by nomSpecialite";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				String nom = result.getString("nom");
				String prenom = result.getString("prenom");
				String etudiant = nom + " " + prenom;
				
				listNote.add(new Note(etudiant, result.getString("nomSpecialite"), result.getString("nomEvaluation"), "", 
						result.getString("nomMatiere"), result.getDouble("resultat")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	col_etudiant.setCellValueFactory(new PropertyValueFactory<Note, String>("etudiant"));
    	col_specialite.setCellValueFactory(new PropertyValueFactory<Note, String>("specialite"));
    	col_evaluation.setCellValueFactory(new PropertyValueFactory<Note, String>("evaluation"));
    	col_matiere.setCellValueFactory(new PropertyValueFactory<Note, String>("matiere"));
    	col_note.setCellValueFactory(new PropertyValueFactory<Note, Double>("note"));
    	
    	
    	table_note.setItems(listNote);
    	btn_modifier.setDisable(true);
    	btn_supprimer.setDisable(true);
    	System.out.println("Mise à jour de la table");
    }

    @FXML
    void tableNoteClick(MouseEvent event) {
    	Note note = table_note.getSelectionModel().getSelectedItem();
    	
    	cbx_specialite.setValue(note.getSpecialite());
    	cbx_etudiant.setValue(note.getEtudiant());
    	cbx_evaluation.setValue(note.getEvaluation());
    	cbx_matiere.setValue(note.getMatiere());
    	txt_note.setText(String.valueOf(note.getNote()));
    	
    	btn_modifier.setDisable(false);
    	btn_supprimer.setDisable(false);
    }
    
    public void clearAll() {
    	cbx_etudiant.setValue("");
    	cbx_evaluation.setValue("");
    	cbx_matiere.setValue("");
    	cbx_specialite.setValue("");
    	txt_note.setText("");
    	btn_ajouter.setDisable(false);
    	btn_modifier.setDisable(true);
    	btn_supprimer.setDisable(true);
    }
    
    public void remplirComboEvaluation() {
    	ObservableList<String> listEvaluation = FXCollections.observableArrayList();
    	String sql = "select * from evaluation order by nomEvaluation";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
	    	while(result.next()) {
	    		listEvaluation.add(result.getString("nomEvaluation"));
	    	}
	    	cbx_evaluation.setItems(FXCollections.observableArrayList(listEvaluation));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    
    public void remplirComboSpecialite() {
    	ObservableList<String> listSpecialite = FXCollections.observableArrayList();
    	String sql = "select * from specialite order by nomSpecialite";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
	    	while(result.next()) {
	    		listSpecialite.add(result.getString("nomSpecialite"));
	    	}
	    	cbx_specialite.setItems(FXCollections.observableArrayList(listSpecialite));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void remplirComboEtudiantMatiere(ActionEvent event) {
    	String sql = "select idSpecialite from specialite where nomSpecialite= '"+cbx_specialite.getValue()+"'";
    	int specialite = 0;
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
	    	if(result.next()) {
	    		specialite = result.getInt("idSpecialite");
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	ObservableList<String> listEtudiant = FXCollections.observableArrayList();
    	String nom = "";
    	String prenom = "";
    	String etudiant = "";
    	String sql2 = "select nom,prenom from etudiant where specialite= "+specialite+" order by nom";
    	try {
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
	    	while(result.next()) {
	    		nom = result.getString("nom");
	    		prenom = result.getString("prenom");
	    		etudiant = nom + " " + prenom;
	    		listEtudiant.add(etudiant);
	    	}
	    	cbx_etudiant.setItems(FXCollections.observableArrayList(listEtudiant));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	ObservableList<String> listMatiere = FXCollections.observableArrayList();
    	String sql3 = "select nomMatiere from matiere where specialite= "+specialite+" order by nomMatiere";
    	try {
			st = cnx.prepareStatement(sql3);
			result = st.executeQuery();
			while(result.next()) {
				listMatiere.add(result.getString("nomMatiere"));
			}
			cbx_matiere.setItems(FXCollections.observableArrayList(listMatiere));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		remplirComboEvaluation();
		remplirComboSpecialite();
		showTable();
	}

}
