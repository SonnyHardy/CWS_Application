package controller;

import java.awt.Desktop;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.layout.element.*;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.kernel.*;
import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Etudiant;
import model.Note;
import model.Releve;

public class EtudiantController implements Initializable{
	
	  @FXML
	    private TextField txt_search;

	    @FXML
	    TableView<Etudiant> table_etudiant;

	    @FXML
	    TableColumn<Etudiant, String> col_matricule;

	    @FXML
	    TableColumn<Etudiant, String> col_nom;

	    @FXML
	    TableColumn<Etudiant, String> col_prenom;

	    @FXML
	    TableColumn<Etudiant, String> col_specialite;

	    @FXML
	    TableColumn<Etudiant, String> col_adresse;

	    @FXML
	    TableColumn<Etudiant, Date> col_dateNais;

	    @FXML
	    TableColumn<Etudiant, String> col_sexe;

	    @FXML
	    TableColumn<Etudiant, String> col_email;

	    @FXML
	    TableColumn<Etudiant, String> col_telephone;

	    @FXML
	    private ImageView img_etudiant;

	    @FXML
	    private Label lbl_matricule;

	    @FXML
	     private Label lbl_nom;

	    @FXML
	    private Label lbl_prenom;

	    @FXML
	    private Label lbl_specialite;

	    @FXML
	    Label lbl_adresse;

	    @FXML
	    private Label lbl_dateNais;

	    @FXML
	    private Label lbl_sexe;

	    @FXML
	    private Label lbl_email;

	    @FXML
	    private Label lbl_telephone;

	    @FXML
	    private Label lbl_recu;

	    @FXML
	    private Label lbl_note;
	    
	    @FXML
	    private Label lbl_releveNote;
	    
	    @FXML
	    JFXButton btn_modifier;

	    @FXML
	    JFXButton btn_supprimer;

	    @FXML
	    private JFXButton btn_ajouter;
	    
	    Connection cnx;
	    PreparedStatement st;
		ResultSet result;
		double x;
		double y;
		NewEtudiantController newEtudiantController = new NewEtudiantController();
		Etudiant etudiant;

	    @FXML
	    void ajouterEtudiant() {
	    	this.newEtudiantController.setModif(false);
	    	formNewEtudiant();
	    	System.out.println(newEtudiantController.isModif());
	    }
	    

	    @FXML
	    void chercherEtudiant(KeyEvent event) {
	    	ObservableList<Etudiant> listEtudiant = FXCollections.observableArrayList();
	    	table_etudiant.getItems().clear();
	    	String sql = "select matricule,nom,prenom,nomSpecialite,adresse,dateNais,sexe,email,telephone from etudiant,specialite"
	    			+ " where etudiant.specialite=specialite.idSpecialite and "
	    			+ "(matricule like '%"+txt_search.getText()+"%' or nom like '%"+txt_search.getText()+"%' "
	    					+ "or prenom like '%"+txt_search.getText()+"%' or nomSpecialite like '%"+txt_search.getText()+"%' "
	    							+ "or adresse like '%"+txt_search.getText()+"%' or sexe like '%"+txt_search.getText()+"%' "
	    									+ "or dateNais like '%"+txt_search.getText()+"%') order by nom";
	    	
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				while (result.next()) {
					listEtudiant.add(new Etudiant(result.getString("matricule"), result.getString("nom"), 
							result.getString("prenom"), result.getString("nomSpecialite"), 
							result.getString("adresse"), result.getDate("dateNais"), 
							result.getString("sexe"), result.getString("email"), 
							result.getString("telephone")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	col_matricule.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("matricule"));
	    	col_nom.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("nom"));
	    	col_prenom.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("prenom"));
	    	col_specialite.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("specialite"));
	    	col_adresse.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("adresse"));
	    	col_dateNais.setCellValueFactory(new PropertyValueFactory<Etudiant, Date>("dateNais"));
	    	col_sexe.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("sexe"));
	    	col_email.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("email"));
	    	col_telephone.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("telephone"));
	    	table_etudiant.setItems(listEtudiant);
	    	//btn_modifier.setDisable(true);
	    	//btn_supprimer.setDisable(true);

	    }

	    @FXML
	    void modifierEtudiant() {
	    	etudiant = table_etudiant.getSelectionModel().getSelectedItem();
	    	String dateStr = String.valueOf(etudiant.getDateNais());
	    	LocalDate date = LocalDate.parse(dateStr);
	    	FXMLLoader loader = new FXMLLoader();
	    	loader.setLocation(getClass().getResource("/interfaces/NewEtudiant.fxml"));
	    	try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	NewEtudiantController newEtudiantController = loader.getController();
	    	
	    	newEtudiantController.setModif(true);
	    	newEtudiantController.remplirForm(etudiant.getMatricule(), etudiant.getNom(), etudiant.getPrenom(), etudiant.getSpecialite(), 
	    			date, etudiant.getSexe(), etudiant.getEmail(), etudiant.getTelephone(), etudiant.getAdresse(), img_etudiant.getImage());
	    	
	    	Parent parent = loader.getRoot();
	    	Stage stage = new Stage();
	    	parent.setOnMousePressed((MouseEvent event) ->{
				x = event.getSceneX();
				y = event.getSceneY();
			});
			
	    	parent.setOnMouseDragged((MouseEvent event) ->{
				stage.setX(event.getScreenX() - x);
				stage.setY(event.getScreenY() - y);
				
				stage.setOpacity(.8);
			});
			
	    	parent.setOnMouseReleased((MouseEvent event) ->{
				stage.setOpacity(1);
			});
	    	stage.setScene(new Scene(parent));
	    	stage.initStyle(StageStyle.TRANSPARENT);
	    	stage.show();
	    	System.out.println(newEtudiantController.isModif());
	    	
	    }

	    @FXML
	    void supprimerEtudiant() {
	    	String sql = "delete from etudiant where matricule= '"+lbl_matricule.getText()+"'";
	    	
	    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("Voulez-vous vraiment supprimer cet étudiant ?");
			 alert.showAndWait().ifPresent(response ->{
				 if (response == ButtonType.OK) {
					 try {
							st = cnx.prepareStatement(sql);
							st.executeUpdate();
							
							clearDetails();
							showTable();
							btn_modifier.setDisable(true);
					    	btn_supprimer.setDisable(true);
					    	Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
							 alert2.setHeaderText("CWS Application");
							 alert2.setContentText("Etudiant supprimé avec succès !");
							 alert2.showAndWait();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				 }
			 });
			 
	    	
	    }

	    @FXML
	    void voireNotes() {
	    	formVoireNote();
	    }
	    
	    public String getGrade(double note) {
	    	if(note >= 18) {
	    		return "A+";
	    	}else if(note<18 && note>=16) {
	    		return "A";
	    	}else if(note<16 && note>=14){
	    		return "A-";
	    	}else if(note<14 && note>=12){
	    		return "B+";
	    	}else if(note<12 && note>=10){
	    		return "B";
	    	}else if(note<10 && note>=8){
	    		return "B-";
	    	}else if(note<8 && note>=6){
	    		return "C+";
	    	}else if(note<6 && note>=4){
	    		return "C";
	    	}else if(note<4 && note>=2){
	    		return "C-";
	    	}else if(note<2 && note>=0){
	    		return "D";
	    	}
			return "";
	    }
	    
	    @FXML
	    void imprimerReleveNote(MouseEvent event) throws DocumentException, MalformedURLException, IOException {
	    	Document document = new Document();
	    	//FileOutputStream file = new FileOutputStream("Releve_notes.pdf");
	    	//com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(new com.itextpdf.kernel.pdf.PdfWriter(file));
	    	//com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);
	    	PdfWriter.getInstance(document, new FileOutputStream("Releve_notes.pdf"));
			document.open();
	
			com.itextpdf.text.Image img = com.itextpdf.text.Image.getInstance("C:\\Users\\Sonny\\eclipse-workspace\\CWS_Application\\src\\application\\icon.png");
	    	//com.itextpdf.layout.element.Image img = new com.itextpdf.layout.element.Image(null);
			img.setAlignment(com.itextpdf.text.Image.ALIGN_CENTER);
			//document.add(img);
			Paragraph par = new Paragraph("COMETAL WELDING SCHOOL"
					+ "\n ******************************************"
					+ "\n RELEVE DE NOTES ANNUEL",  FontFactory.getFont(FontFactory.TIMES_ROMAN,16,Font.BOLD,BaseColor.BLACK));
			par.setAlignment(Element.ALIGN_CENTER);
			//document.add(par);
			
			PdfPTable table = new PdfPTable(1);
			//Table table = new Table(1);
			table.setWidthPercentage(100);
			PdfPCell cell = new PdfPCell();
			//Cell cell = new Cell();
			img.scaleAbsolute(100, 100);
			//cell.addElement(img);
			cell.addElement(img);
			cell.addElement(par);
			table.addCell(cell);
			document.add(table);
			
			//com.itextpdf.layout.element.Paragraph pa = new com.itextpdf.layout.element.Paragraph("fbdfb");
			//document.add(pa);
			Paragraph par2 = new Paragraph("\n");
			document.add(par2);
			String matricule = lbl_matricule.getText();
			Paragraph par3 = new Paragraph("Matricule:    "+matricule+"                                        Année Académique:     2023/2024 \n" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.NORMAL,BaseColor.BLACK));
			//par3.setAlignment(Element.ALIGN_CENTER);
			document.add(par3);
			
			// On recupère les informations de l'étudiant
			String nom,prenom,sexe,dateNais,adresse,specialite;
			nom = lbl_nom.getText();
			prenom = lbl_prenom.getText();
			sexe = lbl_sexe.getText();
			dateNais = lbl_dateNais.getText();
			adresse = lbl_adresse.getText();
			specialite = lbl_specialite.getText().toUpperCase();
			
			
			Paragraph par4 = new Paragraph("Nom(s) et Prénom(s):    "+nom+" " +prenom+"                                        Sexe:     "+sexe+"" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.NORMAL,BaseColor.BLACK));
			//par4.setAlignment(Element.ALIGN_CENTER);
			document.add(par4);
			
			Paragraph par5 = new Paragraph("Date de Naissance:    "+dateNais+"                                        Adresse:     "+adresse+"" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.NORMAL,BaseColor.BLACK));
			//par4.setAlignment(Element.ALIGN_CENTER);
			document.add(par5);
			
			Paragraph par6 = new Paragraph("Nom Spécialité:       "+specialite+" \n" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.NORMAL,BaseColor.BLACK));
			//par4.setAlignment(Element.ALIGN_CENTER);
			document.add(par6);

			
			// Tableau de 8 colonnes
			PdfPTable tab1 = new PdfPTable(8);
			tab1.setWidthPercentage(100);
			
			// colonne 1
			PdfPCell cel = new PdfPCell();
			Paragraph pa = new Paragraph("Code Matiere" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel.addElement(pa);
			tab1.addCell(cel);
			
			// colonne 2
			PdfPCell cel2 = new PdfPCell();
			Paragraph pa2 = new Paragraph("Intitulé de la Matiere" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel2.addElement(pa2);
			tab1.addCell(cel2);
			
			// colonne 3
			PdfPCell cel3 = new PdfPCell();
			Paragraph pa3 = new Paragraph("Crédit Prévu" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel3.addElement(pa3);
			tab1.addCell(cel3);
			
			// colonne 4
			PdfPCell cel4 = new PdfPCell();
			Paragraph pa4 = new Paragraph("Note/20" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel4.addElement(pa4);
			tab1.addCell(cel4);
			
			// colonne 5
			PdfPCell cel5 = new PdfPCell();
			Paragraph pa5 = new Paragraph("Crédit Acquis" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel5.addElement(pa5);
			tab1.addCell(cel5);
			
			// colonne 6
			PdfPCell cel6 = new PdfPCell();
			Paragraph pa6 = new Paragraph("Grade" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel6.addElement(pa6);
			tab1.addCell(cel6);
			
			// colonne 7
			PdfPCell cel7 = new PdfPCell();
			Paragraph pa7 = new Paragraph("Décision" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel7.addElement(pa7);
			tab1.addCell(cel7);
			
			// colonne 8
			PdfPCell cel8 = new PdfPCell();
			Paragraph pa8 = new Paragraph("Session" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel8.addElement(pa8);
			tab1.addCell(cel8);
			
			document.add(tab1);
			
			
			ObservableList<Releve> semestre1 = FXCollections.observableArrayList();
			String sql = "select idMatiere,nomMatiere,credit,resultat,evaluation from note,matiere,evaluation "
					+ "where note.matiere=matiere.idMatiere and note.evaluation=evaluation.idEvaluation "
					+ "and etudiant= '"+lbl_matricule.getText()+"' and semestre= 1 order by idMatiere";
			String idMatiere,nomMatiere,session,grade,decision;
			double note;
			int creditPrev,creditAc;
			int somCredit = 0;
			double somm = 0;
			double moySem1 = 0;
			String gradeSem1, decisionSem1;
			try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				while (result.next()) {
					idMatiere = result.getString("idMatiere");
					nomMatiere = result.getString("nomMatiere");
					int sess = result.getInt("evaluation");
					note = result.getDouble("resultat");
					creditPrev = result.getInt("credit");
					
					somm = somm + (note * creditPrev);
					session = "";
					if (sess == 1) {
						session = "CC";
					}else if(sess == 2) {
						session = "SN";
					}else if(sess == 3) {
						session = "SR";
					}
					
					if (note<10) {
						creditAc = 0;
						decision = "NA";
					}else {
						creditAc = creditPrev;
						decision = "AC";
					}
					somCredit = somCredit + creditAc;
					
					grade = getGrade(note);
					semestre1.add(new Releve(idMatiere, nomMatiere, creditPrev, note, creditAc, grade, decision, session));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			moySem1 = somm / somCredit;
			if (moySem1<10) {
				decisionSem1 = "NA";
			}else {
				decisionSem1 = "AC";
			}
			gradeSem1 = getGrade(moySem1);
			
			// Tableau 1 colonne
			PdfPTable tab2 = new PdfPTable(1);
			tab2.setWidthPercentage(100);
			PdfPCell cel9 = new PdfPCell();
			Paragraph pa9 = new Paragraph("Semestre   1      CREDIT ACQUIS DANS LE SEMESTRE:  "+somCredit+"      MOYENNE:   "+String.format("%.2f", moySem1) ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel9.addElement(pa9);
			tab2.addCell(cel9);	
			document.add(tab2);
			
			
			
			
			// colonne 1
			Paragraph pa31 = new Paragraph("UNITES D'ENSEIGNEMENT" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,10,Font.BOLD,BaseColor.BLACK));
			
			// colonne 2
			Paragraph pa32 = new Paragraph("30" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			
			// colonne 3
			Paragraph pa33 = new Paragraph(String.format("%.2f", moySem1) ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 4
			Paragraph pa34 = new Paragraph(String.valueOf(somCredit) ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 5
			Paragraph pa35 = new Paragraph(gradeSem1 ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 6
			Paragraph pa36 = new Paragraph(decisionSem1 ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 7
			Paragraph pa37 = new Paragraph(" " ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			
			
			// Tableau de 8 colonnes
			PdfPTable tab4 = new PdfPTable(8);
			tab4.setWidthPercentage(100);
			PdfPCell c1 = new PdfPCell();
			c1.addElement(pa31);
			c1.setColspan(2);
			tab4.addCell(c1);
			
			PdfPCell c2 = new PdfPCell();
			c2.addElement(pa32);
			tab4.addCell(c2);
			
			PdfPCell c3 = new PdfPCell();
			c3.addElement(pa33);
			tab4.addCell(c3);
			
			PdfPCell c4 = new PdfPCell();
			c4.addElement(pa34);
			tab4.addCell(c4);
			
			PdfPCell c5 = new PdfPCell();
			c5.addElement(pa35);
			tab4.addCell(c5);
			
			PdfPCell c6 = new PdfPCell();
			c6.addElement(pa36);
			tab4.addCell(c6);
			
			PdfPCell c7 = new PdfPCell();
			c7.addElement(pa37);
			tab4.addCell(c7);
			
			document.add(tab4); 
			
			
			
			// Tableau de 8 colonnes
			PdfPTable tab5 = new PdfPTable(8);
			tab5.setWidthPercentage(100);
			for (Releve releve: semestre1) {
				tab5.addCell(releve.getIdMatiere());
				tab5.addCell(releve.getNomMatiere());
				tab5.addCell(String.valueOf(releve.getCreditPrev()));
				tab5.addCell(String.valueOf(releve.getNote()));
				tab5.addCell(String.valueOf(releve.getCreditAc()));
				tab5.addCell(releve.getGrade());
				tab5.addCell(releve.getDecision());
				tab5.addCell(releve.getSession());
			}
			
			document.add(tab5);
			
			
			ObservableList<Releve> semestre2 = FXCollections.observableArrayList();
			String sql2 = "select idMatiere,nomMatiere,credit,resultat,evaluation from note,matiere,evaluation "
					+ "where note.matiere=matiere.idMatiere and note.evaluation=evaluation.idEvaluation "
					+ "and etudiant= '"+lbl_matricule.getText()+"' and semestre= 2 order by idMatiere";
			String idMatiere2,nomMatiere2,session2,grade2,decision2;
			double note2;
			int creditPrev2,creditAc2;
			int somCredit2 = 0;
			double somm2 = 0;
			double moySem2 = 0;
			String gradeSem2, decisionSem2;
			try {
				st = cnx.prepareStatement(sql2);
				result = st.executeQuery();
				while (result.next()) {
					idMatiere2 = result.getString("idMatiere");
					nomMatiere2 = result.getString("nomMatiere");
					int sess = result.getInt("evaluation");
					note2 = result.getDouble("resultat");
					creditPrev2 = result.getInt("credit");
					
					somm2 = somm2 + (note2 * creditPrev2);
					session2 = "";
					if (sess == 1) {
						session2 = "CC";
					}else if(sess == 2) {
						session2 = "SN";
					}else if(sess == 3) {
						session2 = "SR";
					}
					
					if (note2<10) {
						creditAc2 = 0;
						decision2 = "NA";
					}else {
						creditAc2 = creditPrev2;
						decision2 = "AC";
					}
					somCredit2 = somCredit2 + creditAc2;
					
					grade2 = getGrade(note2);
					semestre2.add(new Releve(idMatiere2, nomMatiere2, creditPrev2, note2, creditAc2, grade2, decision2, session2));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			moySem2 = somm2 / somCredit2;
			if (moySem2<10) {
				decisionSem2 = "NA";
			}else {
				decisionSem2 = "AC";
			}
			gradeSem2 = getGrade(moySem2);
			// Semestre 2
			// Tableau 1 colonne
			PdfPTable tab22 = new PdfPTable(1);
			tab22.setWidthPercentage(100);
			PdfPCell cel92 = new PdfPCell();
			Paragraph pa92 = new Paragraph("Semestre  2      CREDIT ACQUIS DANS LE SEMESTRE:  "+somCredit2+"     MOYENNE:   "+String.format("%.2f", moySem2) ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			cel92.addElement(pa92);
			tab22.addCell(cel92);	
			document.add(tab22);
			
			
			
			
			// colonne 1
			Paragraph pa312 = new Paragraph("UNITES D'ENSEIGNEMENT" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,10,Font.BOLD,BaseColor.BLACK));
			
			// colonne 2
			Paragraph pa322 = new Paragraph("30" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			
			// colonne 3
			Paragraph pa332 = new Paragraph(String.format("%.2f", moySem2) ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 4
			Paragraph pa342 = new Paragraph(String.valueOf(somCredit2) ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 5
			Paragraph pa352 = new Paragraph(gradeSem2 ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 6
			Paragraph pa362 = new Paragraph(decisionSem2 ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLUE));
			
			// colonne 7
			Paragraph pa372 = new Paragraph(" " ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			
			
			// Tableau de 8 colonnes
			PdfPTable tab6 = new PdfPTable(8);
			tab6.setWidthPercentage(100);
			PdfPCell c12 = new PdfPCell();
			c12.addElement(pa312);
			c12.setColspan(2);
			tab6.addCell(c12);
			
			PdfPCell c22 = new PdfPCell();
			c22.addElement(pa322);
			tab6.addCell(c22);
			
			PdfPCell c32 = new PdfPCell();
			c32.addElement(pa332);
			tab6.addCell(c32);
			
			PdfPCell c42 = new PdfPCell();
			c42.addElement(pa342);
			tab6.addCell(c42);
			
			PdfPCell c52 = new PdfPCell();
			c52.addElement(pa352);
			tab6.addCell(c52);
			
			PdfPCell c62 = new PdfPCell();
			c62.addElement(pa362);
			tab6.addCell(c62);
			
			PdfPCell c72 = new PdfPCell();
			c72.addElement(pa372);
			tab6.addCell(c72);
			
			document.add(tab6); 
			
			
			// Tableau de 8 colonnes
			PdfPTable tab7 = new PdfPTable(8);
			tab7.setWidthPercentage(100);
			for (Releve releve: semestre2) {
				tab7.addCell(releve.getIdMatiere());
				tab7.addCell(releve.getNomMatiere());
				tab7.addCell(String.valueOf(releve.getCreditPrev()));
				tab7.addCell(String.valueOf(releve.getNote()));
				tab7.addCell(String.valueOf(releve.getCreditAc()));
				tab7.addCell(releve.getGrade());
				tab7.addCell(releve.getDecision());
				tab7.addCell(releve.getSession());
			}
			document.add(tab7);
			
			int somme = somCredit + somCredit2;
			double moyGen = ((moySem1*30)+ (moySem2*30)) / 60;
			String verdict;
			if (moyGen<10) {
				verdict = "REFUSE(E)";
			}else {
				verdict = "ADMIS(E)";
			}
			Paragraph para1 = new Paragraph("Total Crédits Capitalisés:      "+somme+"     Moyenne Générale:     "+String.format("%.2f", moyGen)+"        Décision:     "+verdict ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			document.add(para1);
			
			LocalDate date = LocalDate.now();
			Paragraph para2 = new Paragraph("Date:  "+String.valueOf(date)+" \n" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.NORMAL,BaseColor.BLACK));
			para2.setAlignment(Element.ALIGN_RIGHT);
			document.add(para2);
			
			Paragraph para3 = new Paragraph("Signature Directeur" ,
					FontFactory.getFont(FontFactory.TIMES_ROMAN,13,Font.BOLD,BaseColor.BLACK));
			para3.setAlignment(Element.ALIGN_RIGHT);
			document.add(para3);
			
			
			document.close();
			Desktop.getDesktop().open(new File("Releve_notes.pdf"));
			
	    }
	    

	    @FXML
	    void voireRecu() {
	    	
	    }
	    
	    public ObservableList<Etudiant> listEtudiant = FXCollections.observableArrayList();
	    public void showTable() {
	    	table_etudiant.getItems().clear();
	    	String sql = "select matricule,nom,prenom,nomSpecialite,adresse,dateNais,sexe,email,telephone from etudiant,specialite where etudiant.specialite=specialite.idSpecialite order by nom";
	    	
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				while (result.next()) {
					listEtudiant.add(new Etudiant(result.getString("matricule"), result.getString("nom"), 
							result.getString("prenom"), result.getString("nomSpecialite"), 
							result.getString("adresse"), result.getDate("dateNais"), 
							result.getString("sexe"), result.getString("email"), 
							result.getString("telephone")));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    	col_matricule.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("matricule"));
	    	col_nom.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("nom"));
	    	col_prenom.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("prenom"));
	    	col_specialite.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("specialite"));
	    	col_adresse.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("adresse"));
	    	col_dateNais.setCellValueFactory(new PropertyValueFactory<Etudiant, Date>("dateNais"));
	    	col_sexe.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("sexe"));
	    	col_email.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("email"));
	    	col_telephone.setCellValueFactory(new PropertyValueFactory<Etudiant, String>("telephone"));
	    	table_etudiant.setItems(listEtudiant);
	    	clearDetails();
	    	btn_modifier.setDisable(true);
	    	btn_supprimer.setDisable(true);
	    	System.out.println("Mise à jour de la table");
	    }
	    
	    @FXML
	    void refreshTable() {
	    	showTable();
	    }
	    
	    @FXML
	    void tableEtudiantClick() {
	    	Etudiant etudiant = table_etudiant.getSelectionModel().getSelectedItem();
	    	String sql = "select matricule,nom,prenom,nomSpecialite,adresse,dateNais,sexe,email,telephone,image from etudiant,specialite"
	    			+ " where etudiant.specialite = specialite.idSpecialite and etudiant.matricule= '"+etudiant.getMatricule() +"'";
	    	
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				byte byteImage[];
				Blob blob;
				while(result.next()) {
					lbl_matricule.setText(result.getString("matricule"));
					lbl_nom.setText(result.getString("nom"));
					lbl_prenom.setText(result.getString("prenom"));
					lbl_specialite.setText(result.getString("nomSpecialite"));
					lbl_adresse.setText(result.getString("adresse"));
					lbl_dateNais.setText(String.valueOf(result.getDate("dateNais")));
					lbl_sexe.setText(result.getString("sexe"));
					lbl_email.setText(result.getString("email"));
					lbl_telephone.setText(result.getString("telephone"));
					blob = result.getBlob("image");
					byteImage = blob.getBytes(1, (int)blob.length());
					Image img = new Image(new ByteArrayInputStream(byteImage), img_etudiant.getFitWidth(), img_etudiant.getFitHeight(), true, true);
					img_etudiant.setImage(img);
					btn_modifier.setDisable(false);
			    	btn_supprimer.setDisable(false);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    }
	    
	    public void clearDetails() {
	    	lbl_matricule.setText("...");
	    	lbl_nom.setText("...");
	    	lbl_prenom.setText("...");
	    	lbl_specialite.setText("...");
	    	lbl_adresse.setText("...");
	    	lbl_dateNais.setText("...");
	    	lbl_email.setText("...");
	    	lbl_sexe.setText("...");
	    	lbl_telephone.setText("...");
	    	img_etudiant.setImage(null);
	    }
	    
	    public void formNewEtudiant() {
	    	
	    	Parent root;
			try {
				//FXMLLoader loader = FXMLLoader.load(getClass().getResource("/interfaces/NewEtudiant.fxml"));
				//NewEtudiantController newEtudiantController = new NewEtudiantController(this);
				//loader.setController(newEtudiantController);
				//root = loader.load();
				root = FXMLLoader.load(getClass().getResource("/interfaces/NewEtudiant.fxml"));
				//AnchorPane anchor = (AnchorPane)root;
				
				Scene scene = new Scene(root);
				Stage stage = new Stage();
				root.setOnMousePressed((MouseEvent event) ->{
					x = event.getSceneX();
					y = event.getSceneY();
				});
				
				root.setOnMouseDragged((MouseEvent event) ->{
					stage.setX(event.getScreenX() - x);
					stage.setY(event.getScreenY() - y);
					
					stage.setOpacity(.8);
				});
				
				root.setOnMouseReleased((MouseEvent event) ->{
					stage.setOpacity(1);
				});
				stage.setScene(scene);
				stage.initStyle(StageStyle.TRANSPARENT);
				stage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
	    	
	    }
	    
	    public void formVoireNote() {
	    	Etudiant etudiant = table_etudiant.getSelectionModel().getSelectedItem();
	    	FXMLLoader loader = new FXMLLoader();
	    	loader.setLocation(getClass().getResource("/interfaces/VoireNote.fxml"));
	    	try {
				loader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    	VoireNoteController voireNoteController = loader.getController();
	    	voireNoteController.showTable(etudiant.getNom(), etudiant.getPrenom());
	    	    	
	    	Parent parent = loader.getRoot();
	    	Stage stage = new Stage();
	    	parent.setOnMousePressed((MouseEvent event) ->{
				x = event.getSceneX();
				y = event.getSceneY();
			});
			
	    	parent.setOnMouseDragged((MouseEvent event) ->{
				stage.setX(event.getScreenX() - x);
				stage.setY(event.getScreenY() - y);
				
				stage.setOpacity(.8);
			});
			
	    	parent.setOnMouseReleased((MouseEvent event) ->{
				stage.setOpacity(1);
			});
	    	stage.setScene(new Scene(parent));
	    	stage.initStyle(StageStyle.TRANSPARENT);
	    	stage.show();
	    }
	 


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTable();
	}


}
