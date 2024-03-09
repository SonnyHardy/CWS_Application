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

public class VoireCoursController implements Initializable{
	
	@FXML
    private TableView<Cours> table_cours;

    @FXML
    private TableColumn<Cours, String> col_specialite;

    @FXML
    private TableColumn<Cours, String> col_matiere;

    @FXML
    private TableColumn<Cours, String> col_volume;

    @FXML
    private Button btn_close;
    
    Connection cnx;
    PreparedStatement st;
	ResultSet result;
	String cni = "";

    @FXML
    void close() {
    	btn_close.getScene().getWindow().hide();
    }
    
    public ObservableList<Cours> listCours = FXCollections.observableArrayList();
    public void showTable(String cni) {
    	this.setCni(cni);
    	
    	table_cours.getItems().clear();
    	String sql = "select nomSpecialite,nomMatiere,volume from cours,matiere,specialite"
    			+ " where cours.specialite=specialite.idSpecialite"
    			+ " and cours.matiere=matiere.idMatiere and enseignant = '"+ cni +"' order by nomSpecialite";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				listCours.add(new Cours("",result.getString("nomSpecialite"), result.getString("nomMatiere"), 
						result.getString("volume")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	col_specialite.setCellValueFactory(new PropertyValueFactory<Cours, String>("specialite"));
    	col_matiere.setCellValueFactory(new PropertyValueFactory<Cours, String>("matiere"));
    	col_volume.setCellValueFactory(new PropertyValueFactory<Cours, String>("volume"));
    	table_cours.setItems(listCours);
    	
    	System.out.println("Mise à jour de la table");
    }
    
    @FXML
    void tableCoursClick() {

    }
    
    
    
	public String getCni() {
		return cni;
	}

	public void setCni(String cni) {
		this.cni = cni;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTable(this.getCni());
	}

}
