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
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Enseignant;

public class NewEnseignantController implements Initializable{
	
	@FXML
    private TextField txt_cni;

    @FXML
    private TextField txt_nom;

    @FXML
    private TextField txt_prenom;

    @FXML
    private ComboBox<String> cbx_sexe;

    @FXML
    private TextField txt_salaire;

    @FXML
    private ComboBox<String> cbx_contrat;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_telephone;

    @FXML
    private ImageView img_enseignant;

    @FXML
    private JFXButton btn_valider;

    @FXML
    private JFXButton btn_nouveau;

    @FXML
    private JFXButton btn_annuler;

    @FXML
    private Button btn_close;

    @FXML
    private TextField txt_adresse;
    
    Connection cnx;
    PreparedStatement st;
	ResultSet result;
	String path;
	FileInputStream fis;
	//EnseignantController enseignantController = new EnseignantController();
	boolean modif = false;

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
    void insererImage() {
    	FileChooser fc = new FileChooser();
    	fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
    	File f = fc.showOpenDialog(null);
    	if (f != null) {
    		path = f.getAbsolutePath();
    		Image img = new Image(f.toURI().toString(), img_enseignant.getFitWidth(), img_enseignant.getFitHeight(), true, true);
    		img_enseignant.setImage(img);
    	}
    }

    @FXML
    void nouveau() {
    	clearAll();
    }

    @FXML
    void valider() {
    	if (modif == false) {
    		String sql = "insert into enseignant(cni,nom,prenom,sexe,adresse,contrat,email,telephone,salaire,image)"
	    			+ " values(?,?,?,?,?,?,?,?,?,?)";
	    	
	    	if (!txt_cni.getText().isBlank() && !txt_nom.getText().isBlank() && !txt_prenom.getText().isBlank() 
	    			&& !txt_email.getText().isBlank() && !txt_telephone.getText().isBlank() && !txt_salaire.getText().isBlank()
	    			&& !txt_adresse.getText().isBlank() && !cbx_sexe.getSelectionModel().isEmpty() 
	    			&& !cbx_contrat.getSelectionModel().isEmpty() && img_enseignant.getImage() != null) {
	    		
	    		String cni = txt_cni.getText();
	    		String nom = txt_nom.getText();
	    		String prenom = txt_prenom.getText();
	    		String sexe = cbx_sexe.getValue();
	    		String adresse = txt_adresse.getText();
	    		String contrat = cbx_contrat.getValue();
	    		String email = txt_email.getText();
	    		String telephone = txt_telephone.getText();
	    		int salaire = Integer.parseInt(txt_salaire.getText());
	    		
	    		File image = new File(path);
    			
    			try {
					st = cnx.prepareStatement(sql);
					st.setString(1, cni);
					st.setString(2, nom);
					st.setString(3, prenom);
					st.setString(4, sexe);
					st.setString(5, adresse);
					st.setString(6, contrat);
					st.setString(7, email);
					st.setString(8, telephone);
					st.setInt(9, salaire);
					fis = new FileInputStream(image);
					st.setBinaryStream(10, fis, image.length());
					st.executeUpdate();
					clearAll();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Enseignant ajouté avec succès !");
					 alert.showAndWait();
					
				} catch (SQLException | FileNotFoundException e) {
					e.printStackTrace();
				}
	    		
	    	}else {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Veuillez remplir correctement tous les champs");
				 alert.showAndWait();
	    	}
	    	
    	}else {
    		String sql = "update enseignant set nom=?, prenom=?, sexe=?, adresse=?, contrat=?, "
    				+ "email=?, telephone=?, salaire=?, image=? where cni= '"+txt_cni.getText()+"'";
    		
    		if (!txt_cni.getText().isBlank() && !txt_nom.getText().isBlank() && !txt_prenom.getText().isBlank() 
	    			&& !txt_email.getText().isBlank() && !txt_telephone.getText().isBlank() && !txt_salaire.getText().isBlank()
	    			&& !txt_adresse.getText().isBlank() && !cbx_sexe.getSelectionModel().isEmpty() 
	    			&& !cbx_contrat.getSelectionModel().isEmpty() && img_enseignant.getImage() != null) {
    			
    			String nom = txt_nom.getText();
	    		String prenom = txt_prenom.getText();
	    		String sexe = cbx_sexe.getValue();
	    		String adresse = txt_adresse.getText();
	    		String contrat = cbx_contrat.getValue();
	    		String email = txt_email.getText();
	    		String telephone = txt_telephone.getText();
	    		int salaire = Integer.parseInt(txt_salaire.getText());
    			
    			try {
					st = cnx.prepareStatement(sql);
					st.setString(1, nom);
					st.setString(2, prenom);
					st.setString(3, sexe);
					st.setString(4, adresse);
					st.setString(5, contrat);
					st.setString(6, email);
					st.setString(7, telephone);
					st.setInt(8, salaire);
					//fis = new FileInputStream(image);
					Image img = img_enseignant.getImage();
					byte[] image = imageToByteArray(img);
					st.setBytes(9, image);
					st.executeUpdate();
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Enseignant modifié avec succès !");
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
    
    public void remplirComboSexe() {
    	List<String> listSexe = new ArrayList<String>();
    	listSexe.add("Masculin");
    	listSexe.add("Feminin");
    	cbx_sexe.setItems(FXCollections.observableArrayList(listSexe));
    }
    
    public void remplirComboContrat() {
    	List<String> listContrat = new ArrayList<String>();
    	listContrat.add("Vacataire");
    	listContrat.add("Permanent");
    	cbx_contrat.setItems(FXCollections.observableArrayList(listContrat));
    }
    
    public void clearAll() {
    	txt_cni.setText("");
    	txt_nom.setText("");
    	txt_prenom.setText("");
    	txt_telephone.setText("");
    	txt_adresse.setText("");
    	txt_salaire.setText("");
    	txt_email.setText("");
    	cbx_sexe.getSelectionModel().clearSelection();
    	cbx_contrat.getSelectionModel().clearSelection();
    	img_enseignant.setImage(null);
    }
    
    public void remplirForm(String cni, String nom, String prenom, String sexe, String adresse, 
    		String contrat, String email, String telephone, int salaire, Image img) {
		
    	txt_cni.setText(cni);
    	txt_nom.setText(nom);
    	txt_prenom.setText(prenom);
    	cbx_sexe.setValue(sexe);
    	txt_adresse.setText(adresse);
    	cbx_contrat.setValue(contrat);
    	txt_email.setText(email);
    	txt_telephone.setText(telephone);
    	txt_salaire.setText(String.valueOf(salaire));
    	img_enseignant.setImage(img);
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
    

	public boolean isModif() {
		return modif;
	}

	public void setModif(boolean modif) {
		this.modif = modif;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		remplirComboContrat();
		remplirComboSexe();
	}

}
