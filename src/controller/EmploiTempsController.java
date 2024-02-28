package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;

public class EmploiTempsController implements Initializable{
	
	 @FXML
	    private DatePicker dp_date1;

	    @FXML
	    private DatePicker dp_date2;

	    @FXML
	    private ComboBox<String> cbx_matiere1;

	    @FXML
	    private ComboBox<String> cbx_matiere2;

	    @FXML
	    private ComboBox<String> cbx_matiere3;

	    @FXML
	    private ComboBox<String> cbx_matiere4;

	    @FXML
	    private ComboBox<String> cbx_matiere5;

	    @FXML
	    private ComboBox<String> cbx_matiere6;

	    @FXML
	    private ComboBox<String> cbx_matiere7;

	    @FXML
	    private ComboBox<String> cbx_matiere8;

	    @FXML
	    private ComboBox<String> cbx_matiere9;

	    @FXML
	    private ComboBox<String> cbx_matiere10;

	    @FXML
	    private ComboBox<String> cbx_matiere11;

	    @FXML
	    private ComboBox<String> cbx_matiere12;

	    @FXML
	    private ComboBox<String> cbx_specialite;

	    @FXML
	    private JFXButton btn_imprimer;
	    
	    Connection cnx;
	    PreparedStatement st;
		ResultSet result;
	    
	    
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
				cbx_matiere1.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere2.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere3.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere4.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere5.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere6.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere7.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere8.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere9.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere10.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere11.setItems(FXCollections.observableArrayList(listMatiere));
				cbx_matiere12.setItems(FXCollections.observableArrayList(listMatiere));
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    
	    @FXML
	    void imprimer() {

	    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		remplirComboSpecialite();
	}

}
