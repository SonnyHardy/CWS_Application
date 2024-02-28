package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class CoursController implements Initializable{
	
	@FXML
    private TextField txt_idcours;

    @FXML
    private JFXButton btn_idcours;

    @FXML
    private ComboBox<String> cbx_specialite;

    @FXML
    private ComboBox<String> cbx_matiere;

    @FXML
    private ComboBox<String> cbx_enseignant;

    @FXML
    private TextField txt_volume;

    @FXML
    private TextField txt_search;

    @FXML
    private TableView<Cours> table_cours;

    @FXML
    private TableColumn<Cours, String> col_idcours;

    @FXML
    private TableColumn<Cours, String> col_specialite;

    @FXML
    private TableColumn<Cours, String> col_matiere;

    @FXML
    private TableColumn<Cours, String> col_enseignant;

    @FXML
    private TableColumn<Cours, String> col_volume;
    
    @FXML
    private JFXButton btn_ajouter;

    @FXML
    private JFXButton btn_nouveau;

    @FXML
    private JFXButton btn_supprimer;

    @FXML
    private JFXButton btn_modifier;
    
    Connection cnx;
    PreparedStatement st;
	ResultSet result;

    @FXML
    void ajouterCours() {
    	if (!txt_idcours.getText().isBlank() && !txt_volume.getText().isBlank() && !cbx_enseignant.getValue().isBlank()
    			 && !cbx_matiere.getValue().isBlank() && !cbx_specialite.getValue().isBlank()) {
    		
    		int specialite = 0;
    		int matiere = 0;
    		String enseignant = "";
    		String nom = "";
    		String prenom = "";
    		String[] nameParts = cbx_enseignant.getValue().split(" ");
    		if (nameParts.length == 2) {
    			nom = nameParts[0];
    			prenom = nameParts[1];
    		}
    		
    		String sql = "select idSpecialite from specialite where nomSpecialite= '"+ cbx_specialite.getValue() +"'";
    		String sql2 = "select idMatiere from matiere where nomMatiere= '"+ cbx_matiere.getValue() +"'";
    		String sql3 = "select cni from enseignant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
    		try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					specialite = result.getInt("idSpecialite");
				}
				
				st = cnx.prepareStatement(sql2);
				result = st.executeQuery();
				if (result.next()) {
					matiere = result.getInt("idMatiere");
				}
				
				st = cnx.prepareStatement(sql3);
				result = st.executeQuery();
				if (result.next()) {
					enseignant = result.getString("cni");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
    		String sql4 = "insert into cours(idCours,enseignant,specialite,matiere,volume) values(?,?,?,?,?)";
        	try {
				st = cnx.prepareStatement(sql4);
				st.setString(1, txt_idcours.getText());
				st.setString(2, enseignant);
				st.setInt(3, specialite);
				st.setInt(4, matiere);
				st.setString(5, txt_volume.getText());
				st.executeUpdate();
				clearAll();
				showTable();
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Cours ajouté avec succès !");
				 alert.showAndWait();
				 
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
    void chercherCours(KeyEvent event) {
    	table_cours.getItems().clear();
    	String sql = "select idCours,nom,prenom,nomSpecialite,nomMatiere,volume from cours,enseignant,matiere,specialite"
    			+ " where cours.enseignant=enseignant.cni and cours.specialite=specialite.idSpecialite"
    			+ " and cours.matiere=matiere.idMatiere and (idCours like '%"+txt_search.getText()+"%' or "
    					+ "nom like '%"+txt_search.getText()+"%' or nomSpecialite like '%"+txt_search.getText()+"%' or "
    							+ "nomMatiere like '%"+txt_search.getText()+"%') order by nom";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				String nom = result.getString("nom");
				String prenom = result.getString("prenom");
				String enseignant = nom + " " + prenom;
				listCours.add(new Cours(result.getString("idCours"), enseignant, 
						result.getString("nomSpecialite"), result.getString("nomMatiere"), result.getString("volume")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	col_idcours.setCellValueFactory(new PropertyValueFactory<Cours, String>("idCours"));
    	col_enseignant.setCellValueFactory(new PropertyValueFactory<Cours, String>("enseignant"));
    	col_specialite.setCellValueFactory(new PropertyValueFactory<Cours, String>("specialite"));
    	col_matiere.setCellValueFactory(new PropertyValueFactory<Cours, String>("matiere"));
    	col_volume.setCellValueFactory(new PropertyValueFactory<Cours, String>("volume"));
    	table_cours.setItems(listCours);
    }

    @FXML
    void generateIdCours() {
    	String id = generateRandomId();
    	txt_idcours.setText(id);
    }

    @FXML
    void modifierCours() {
    	if (!txt_idcours.getText().isBlank() && !txt_volume.getText().isBlank() && !cbx_enseignant.getValue().isBlank()
   			 && !cbx_matiere.getValue().isBlank() && !cbx_specialite.getValue().isBlank()) {
   		
   		int specialite = 0;
   		int matiere = 0;
   		String enseignant = "";
   		String nom = "";
   		String prenom = "";
   		String[] nameParts = cbx_enseignant.getValue().split(" ");
   		if (nameParts.length == 2) {
   			nom = nameParts[0];
   			prenom = nameParts[1];
   		}
   		
   		String sql = "select idSpecialite from specialite where nomSpecialite= '"+ cbx_specialite.getValue() +"'";
   		String sql2 = "select idMatiere from matiere where nomMatiere= '"+ cbx_matiere.getValue() +"'";
   		String sql3 = "select cni from enseignant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
   		try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					specialite = result.getInt("idSpecialite");
				}
				
				st = cnx.prepareStatement(sql2);
				result = st.executeQuery();
				if (result.next()) {
					matiere = result.getInt("idMatiere");
				}
				
				st = cnx.prepareStatement(sql3);
				result = st.executeQuery();
				if (result.next()) {
					enseignant = result.getString("cni");
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
   		
   		String sql4 = "update cours set enseignant=?, specialite=?, matiere=?, volume=? where idCours= '"+ txt_idcours.getText() +"'";
       	try {
				st = cnx.prepareStatement(sql4);
				st.setString(1, enseignant);
				st.setInt(2, specialite);
				st.setInt(3, matiere);
				st.setString(4, txt_volume.getText());
				st.executeUpdate();
				clearAll();
				showTable();
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Cours modifié avec succès !");
				 alert.showAndWait();
				 
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
    void nouveau() {
    	clearAll();
    }

    @FXML
    void supprimerCours() {
String sql = "delete from cours where idCours= '"+txt_idcours.getText()+"'";
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		 alert.setHeaderText("CWS Application");
		 alert.setContentText("Voulez-vous vraiment supprimer ce cours ?");
		 alert.showAndWait().ifPresent(response ->{
			 if (response == ButtonType.OK) {
				 try {
						st = cnx.prepareStatement(sql);
						st.executeUpdate();
						
						clearAll();
						showTable();
						btn_modifier.setDisable(true);
				    	btn_supprimer.setDisable(true);
				    	Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
						 alert2.setHeaderText("CWS Application");
						 alert2.setContentText("Cours supprimé avec succès !");
						 alert2.showAndWait();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			 }
		 });
    }

    
    public void remplirComboSpecialite() {
    	List<String> listSpecialite = new ArrayList<String>();
    	String sql = "select nomSpecialite from specialite";
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
    void remplirComboMatiere(MouseEvent event) {
    	String specialite = cbx_specialite.getValue();
    	int idSpecialite =0;
    	String sql = "select idSpecialite from specialite where nomSpecialite= '"+ specialite +"'";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if (result.next()) {
				idSpecialite = result.getInt("idSpecialite");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
    	
    	List<String> listMatiere = new ArrayList<String>();
    	String sql2 = "select nomMatiere from matiere where specialite="+idSpecialite;
    	try {
			st = cnx.prepareStatement(sql2);
			result = st.executeQuery();
			while(result.next()) {
				listMatiere.add(result.getString("nomMatiere"));
			}
			cbx_matiere.setItems(FXCollections.observableArrayList(listMatiere));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void remplirComboEnseignant() {
    	List<String> listEnseignant = new ArrayList<String>();
    	String sql = "select nom,prenom from enseignant";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while(result.next()) {
				String nom = result.getString("nom");
				String prenom = result.getString("prenom");
				String enseignant = nom + " " + prenom;
				listEnseignant.add(enseignant);
			}
			cbx_enseignant.setItems(FXCollections.observableArrayList(listEnseignant));
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public ObservableList<Cours> listCours = FXCollections.observableArrayList();
    public void showTable() {
    	table_cours.getItems().clear();
    	String sql = "select idCours,nom,prenom,nomSpecialite,nomMatiere,volume from cours,enseignant,matiere,specialite"
    			+ " where cours.enseignant=enseignant.cni and cours.specialite=specialite.idSpecialite"
    			+ " and cours.matiere=matiere.idMatiere order by nom";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				String nom = result.getString("nom");
				String prenom = result.getString("prenom");
				String enseignant = nom + " " + prenom;
				listCours.add(new Cours(result.getString("idCours"), enseignant, 
						result.getString("nomSpecialite"), result.getString("nomMatiere"), result.getString("volume")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	col_idcours.setCellValueFactory(new PropertyValueFactory<Cours, String>("idCours"));
    	col_enseignant.setCellValueFactory(new PropertyValueFactory<Cours, String>("enseignant"));
    	col_specialite.setCellValueFactory(new PropertyValueFactory<Cours, String>("specialite"));
    	col_matiere.setCellValueFactory(new PropertyValueFactory<Cours, String>("matiere"));
    	col_volume.setCellValueFactory(new PropertyValueFactory<Cours, String>("volume"));
    	table_cours.setItems(listCours);
    	btn_modifier.setDisable(true);
    	btn_supprimer.setDisable(true);
    	System.out.println("Mise à jour de la table");
    }
    
    @FXML
    void tableCoursClick() {
    	Cours cours = table_cours.getSelectionModel().getSelectedItem();
    	String sql = "select idCours,nom,prenom,nomSpecialite,nomMatiere,volume from cours,enseignant,matiere,specialite"
    			+ " where cours.enseignant=enseignant.cni and cours.specialite=specialite.idSpecialite"
    			+ " and cours.matiere=matiere.idMatiere and idCours= '"+ cours.getIdCours() +"'";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			if (result.next()) {
				String nom = result.getString("nom");
				String prenom = result.getString("prenom");
				String enseignant = nom + " " + prenom;
				txt_idcours.setText(result.getString("idCours"));
				cbx_specialite.setValue(result.getString("nomSpecialite"));
				cbx_matiere.setValue(result.getString("nomMatiere"));
				cbx_enseignant.setValue(enseignant);
				txt_volume.setText(result.getString("volume"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	btn_ajouter.setDisable(true);
    	btn_modifier.setDisable(false);
    	btn_supprimer.setDisable(false);
    }
    
    public void clearAll() {
    	txt_idcours.setText("");
    	txt_volume.setText("");
    	cbx_enseignant.getSelectionModel().clearSelection();
    	cbx_matiere.getSelectionModel().clearSelection();
    	cbx_matiere.setValue("");
    	cbx_specialite.getSelectionModel().clearSelection();
    	btn_ajouter.setDisable(false);
    	btn_modifier.setDisable(true);
    	btn_supprimer.setDisable(true);
    }
    
    public String generateRandomId() {
    	String characters = "1234567890";
    	StringBuilder matricule = new StringBuilder();
    	Random rand = new Random();
    	for (int i=0; i<5; i++) {
    		matricule.append(characters.charAt(rand.nextInt(characters.length())));
    	}
    	return "C" + matricule.toString();
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTable();
		remplirComboSpecialite();
		remplirComboEnseignant();
	}

}
