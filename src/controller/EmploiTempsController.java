package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
	    void remplirDate2() {
	    	LocalDate date1 = dp_date1.getValue();
	    	LocalDate date2 = date1.plusDays(5);
	    	dp_date2.setValue(date2);
	    }
	    
	    
	    @FXML
	    void imprimer() {
	    	Document document = new Document();
	    	
	    	if (dp_date1.getValue() == null || dp_date2.getValue() == null || cbx_specialite.getSelectionModel().getSelectedItem() == null) {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Veuillez remplir tous les champs de l'entete");
				 alert.showAndWait();
				 
	    	}else {
	    		try {
					PdfWriter.getInstance(document, new FileOutputStream("Emploi_du_temps.pdf"));
					document.open();
					
					Paragraph titre = new Paragraph("Emploi Du Temps"
							+ "\n  ", FontFactory.getFont(FontFactory.TIMES_ROMAN,16,Font.BOLD,BaseColor.BLACK));
					titre.setAlignment(Element.ALIGN_CENTER);
					document.add(titre);
					
					Paragraph semaine = new Paragraph("Semaine Du "+String.valueOf(dp_date1.getValue())+"  Au  "+String.valueOf(dp_date1.getValue())
							+ "\n  ", FontFactory.getFont(FontFactory.TIMES_ROMAN,14,Font.NORMAL,BaseColor.BLACK));
					semaine.setAlignment(Element.ALIGN_CENTER);
					document.add(semaine);
					
					Paragraph specialite = new Paragraph("Spécialité: "+cbx_specialite.getValue().toUpperCase()
							+ "\n  ", FontFactory.getFont(FontFactory.TIMES_ROMAN,14,Font.NORMAL,BaseColor.BLACK));
					specialite.setAlignment(Element.ALIGN_CENTER);
					document.add(specialite);
					
					PdfPTable table = new PdfPTable(3);
					table.addCell("Jour");
					table.addCell("08H-12H");
					table.addCell("13H-17H");
					
					//String[] jours = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
					//String[] matieres = {"Matiere", "Matiere"};
					
					// Ligne du lundi
					table.addCell("Lundi");
					table.addCell(cbx_matiere1.getValue());
					table.addCell(cbx_matiere2.getValue());
					
					// Ligne du mardi
					table.addCell("Mardi");
					table.addCell(cbx_matiere3.getValue());
					table.addCell(cbx_matiere4.getValue());
					
					// Ligne du mercredi
					table.addCell("Mercredi");
					table.addCell(cbx_matiere5.getValue());
					table.addCell(cbx_matiere6.getValue());
					
					// Ligne du jeudi
					table.addCell("Jeudi");
					table.addCell(cbx_matiere7.getValue());
					table.addCell(cbx_matiere8.getValue());
					
					// Ligne du vendredi
					table.addCell("Vendredi");
					table.addCell(cbx_matiere9.getValue());
					table.addCell(cbx_matiere10.getValue());
					
					// Ligne du samedi
					table.addCell("Samedi");
					table.addCell(cbx_matiere11.getValue());
					table.addCell(cbx_matiere12.getValue());
					
					document.add(table);
					document.close();
					Desktop.getDesktop().open(new File("Emploi_du_temps.pdf"));
				} catch (IOException | DocumentException e) {
					e.printStackTrace();
				}
	    	}
	    	
	    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		remplirComboSpecialite();
	}

}
