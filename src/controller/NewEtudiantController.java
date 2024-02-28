package controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class NewEtudiantController implements Initializable{
	
	 	@FXML
	    TextField txt_matricule;

	    @FXML
	    private JFXButton btn_matricule;

	    @FXML
	    private TextField txt_nom;

	    @FXML
	    private TextField txt_prenom;

	    @FXML
	    private ComboBox<String> cbx_specialite;

	    @FXML
	    private TextField txt_adresse;

	    @FXML
	    private DatePicker dp_date;

	    @FXML
	    private ComboBox<String> cbx_sexe;

	    @FXML
	    private TextField txt_email;

	    @FXML
	    private TextField txt_telephone;

	    @FXML
	    private ImageView img_etudiant;

	    @FXML
	    private JFXButton btn_inserer;

	    @FXML
	    private JFXButton btn_valider;

	    @FXML
	    private JFXButton btn_nouveau;

	    @FXML
	    private JFXButton btn_annuler;

	    @FXML
	    private Button btn_close;
	    
	    @FXML
	    private Label lbl_error;

	    @FXML
	    private ImageView img_error;
	    
	    Connection cnx;
	    PreparedStatement st;
		ResultSet result;
		String path;
		FileInputStream fis;
		EtudiantController etudiantController;
		boolean modif = false;
		
		
		 @FXML
		    void verifierEmail(KeyEvent event) {
			 final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
		    	Pattern pattern = Pattern.compile(EMAIL_REGEX);
		    	boolean matcher = Pattern.matches(EMAIL_REGEX, txt_email.getText());
		    	if (!matcher) {
		    		lbl_error.setDisable(false);
		    		lbl_error.setOpacity(1);
		    		img_error.setDisable(false);
		    		img_error.setOpacity(1);
		    	}else {
		    		lbl_error.setDisable(true);
		    		lbl_error.setOpacity(0);
		    		img_error.setDisable(true);
		    		img_error.setOpacity(0);
		    	}
		    }
		 
		 @FXML
		    void verifierTelephone(KeyEvent event) {
			 	String character = event.getCharacter();
			 	if (!character.matches("[0-9]")) {
			 		txt_telephone.deletePreviousChar();
			 	}
		    }

	    @FXML
	    void annuler() {
	    	btn_annuler.getScene().getWindow().hide();
	    	this.setModif(false);
	    }

	    @FXML
	    void close() {
	    	btn_close.getScene().getWindow().hide();
	    	this.setModif(false);
	    }

	    @FXML
	    void genererMatricule() {
	    	String matricule = generateRandomMatricule();
	    	txt_matricule.setText(matricule);
	    }

	    @FXML
	    void insererImage() {
	    	FileChooser fc = new FileChooser();
	    	fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
	    	File f = fc.showOpenDialog(null);
	    	if (f != null) {
	    		path = f.getAbsolutePath();
	    		Image img = new Image(f.toURI().toString(), img_etudiant.getFitWidth(), img_etudiant.getFitHeight(), true, true);
	    		img_etudiant.setImage(img);
	    	}
	    }

	    @FXML
	    void nouveau() {
	    	clearAll();
	    }

	    @FXML
	    void valider() {
	    	if (modif == false) {
	    		String sql = "insert into etudiant(matricule,nom,prenom,specialite,adresse,dateNais,sexe,email,telephone,image)"
		    			+ " values(?,?,?,?,?,?,?,?,?,?)";
		    	
		    	if (!txt_matricule.getText().isBlank() && !txt_nom.getText().isBlank() && !txt_prenom.getText().isBlank() 
		    			&& !txt_email.getText().isBlank() && !txt_telephone.getText().isBlank() 
		    			&& dp_date.getValue() != null && !txt_adresse.getText().isBlank() && !cbx_sexe.getSelectionModel().isEmpty() 
		    			&& !cbx_specialite.getSelectionModel().isEmpty() && img_etudiant.getImage() != null 
		    			&& lbl_error.isDisable()) {
		    		
		    		String matricule = txt_matricule.getText();
		    		String nom = txt_nom.getText();
		    		String prenom = txt_prenom.getText();
		    		String email = txt_email.getText();
		    		String adresse = txt_adresse.getText();
		    		String telephone = txt_telephone.getText();
		    		String sexe = cbx_sexe.getValue();
		    		int specialite = 0;
		    		
		    		
		    		String specialit = cbx_specialite.getValue();
		    		String sql2 = "select idSpecialite from specialite where nomSpecialite= '"+specialit+"'";
		    		try {
						st = cnx.prepareStatement(sql2);
						result = st.executeQuery();
						if (result.next()) {
							specialite = result.getInt("idSpecialite");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
		    		
		    		File image = new File(path);
		    		java.util.Date dateValue = java.util.Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
	    			Date sqlDate = new Date(dateValue.getTime());
	    			
	    			try {
						st = cnx.prepareStatement(sql);
						st.setString(1, matricule);
						st.setString(2, nom);
						st.setString(3, prenom);
						st.setInt(4, specialite);
						st.setString(5, adresse);
						st.setDate(6, sqlDate);
						st.setString(7, sexe);
						st.setString(8, email);
						st.setString(9, telephone);
						fis = new FileInputStream(image);
						st.setBinaryStream(10, fis, image.length());
						st.executeUpdate();
						clearAll();
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						 alert.setHeaderText("CWS Application");
						 alert.setContentText("Etudiant ajouté avec succès !");
						 alert.showAndWait();
						
					} catch (SQLException | FileNotFoundException e) {
						e.printStackTrace();
					}
		    		
		    	}else {
		    		Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Veuiilez remplir correctement tous les champs");
					 alert.showAndWait();
		    	}
		    	
	    	}else {
	    		String sql = "update etudiant set nom=?, prenom=?, specialite=?, adresse=?, dateNais=?, "
	    				+ "sexe=?, email=?, telephone=?, image=? where matricule= '"+txt_matricule.getText()+"'";
	    		
	    		if (!txt_matricule.getText().isBlank() && !txt_nom.getText().isBlank() && !txt_prenom.getText().isBlank() 
		    			&& !txt_email.getText().isBlank() && !txt_telephone.getText().isBlank() 
		    			&& dp_date.getValue() != null && !txt_adresse.getText().isBlank() && !cbx_sexe.getSelectionModel().isEmpty() 
		    			&& !cbx_specialite.getSelectionModel().isEmpty() && img_etudiant.getImage() != null 
		    			&& lbl_error.isDisable()) {
	    			
		    		String nom = txt_nom.getText();
		    		String prenom = txt_prenom.getText();
		    		String email = txt_email.getText();
		    		String adresse = txt_adresse.getText();
		    		String telephone = txt_telephone.getText();
		    		String sexe = cbx_sexe.getValue();
		    		int specialite = 0;
		    		
		    		
		    		String specialit = cbx_specialite.getValue();
		    		String sql2 = "select idSpecialite from specialite where nomSpecialite= '"+specialit+"'";
		    		try {
						st = cnx.prepareStatement(sql2);
						result = st.executeQuery();
						if (result.next()) {
							specialite = result.getInt("idSpecialite");
						}
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
		    		
		    		//File image = new File(path);
		    		java.util.Date dateValue = java.util.Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
	    			Date sqlDate = new Date(dateValue.getTime());
	    			
	    			try {
						st = cnx.prepareStatement(sql);
						st.setString(1, nom);
						st.setString(2, prenom);
						st.setInt(3, specialite);
						st.setString(4, adresse);
						st.setDate(5, sqlDate);
						st.setString(6, sexe);
						st.setString(7, email);
						st.setString(8, telephone);
						//fis = new FileInputStream(image);
						Image img = img_etudiant.getImage();
						byte[] image = imageToByteArray(img);
						st.setBytes(9, image);
						st.executeUpdate();
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						 alert.setHeaderText("CWS Application");
						 alert.setContentText("Etudiant modifié avec succès !");
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
	    	
	    }
	    
	    public byte[] imageToByteArray(Image img) {
	    	try {
				BufferedImage bufferedImage = SwingFXUtils.fromFXImage(img, null);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
				byte[] imageBytes = byteArrayOutputStream.toByteArray();
				return imageBytes;
	    		
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
	    }
	    
	    public void remplirComboSpecialite(){
	    	String sql = "select nomSpecialite from specialite";
	    	List<String> listSpecialite= new ArrayList<String>();
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				while (result.next()) {
					listSpecialite.add(result.getString("nomSpecialite"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	cbx_specialite.setItems(FXCollections.observableArrayList(listSpecialite));
	    }
	    
	    public void remplirComboSexe() {
	    	List<String> listSexe = new ArrayList<String>();
	    	listSexe.add("Masculin");
	    	listSexe.add("Feminin");
	    	cbx_sexe.setItems(FXCollections.observableArrayList(listSexe));
	    }
	    
	    public String generateRandomMatricule() {
	    	String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	    	StringBuilder matricule = new StringBuilder();
	    	Random rand = new Random();
	    	for (int i=0; i<5; i++) {
	    		matricule.append(characters.charAt(rand.nextInt(characters.length())));
	    	}
	    	return "CWS" + matricule.toString();
	    }
	    
	    public void clearAll() {
	    	txt_matricule.setText("");
	    	txt_nom.setText("");
	    	txt_prenom.setText("");
	    	txt_email.setText("");
	    	txt_telephone.setText("");
	    	dp_date.setValue(null);
	    	cbx_sexe.getSelectionModel().clearSelection();
	    	cbx_specialite.getSelectionModel().clearSelection();
	    	img_etudiant.setImage(null);
	    }
	    
	    public void remplirForm(String matricule, String nom, String prenom, String specialite, LocalDate date, 
	    		String sexe, String email, String telephone, String adresse, Image img) {
			
	    	txt_matricule.setText(matricule);
	    	txt_nom.setText(nom);
	    	txt_prenom.setText(prenom);
	    	cbx_specialite.setValue(specialite);
	    	dp_date.setValue(date);
	    	cbx_sexe.setValue(sexe);
	    	txt_email.setText(email);
	    	txt_telephone.setText(telephone);
	    	txt_adresse.setText(adresse);
	    	img_etudiant.setImage(img);
		}
	    
	    

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		remplirComboSexe();
		remplirComboSpecialite();
		//System.out.println(etudiantController.isBol());
	}
	
	

	public boolean isModif() {
		return modif;
	}

	public void setModif(boolean modif) {
		this.modif = modif;
	}
	
	
}
