package controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
import model.Enseignant;
import model.Etudiant;

public class EnseignantController implements Initializable{
	
	@FXML
    private TextField txt_search;

    @FXML
    private TableView<Enseignant> table_enseignant;

    @FXML
    private TableColumn<Enseignant, String> col_cni;

    @FXML
    private TableColumn<Enseignant, String> col_nom;

    @FXML
    private TableColumn<Enseignant, String> col_prenom;

    @FXML
    private TableColumn<Enseignant, String> col_sexe;

    @FXML
    private TableColumn<Enseignant, String> col_adresse;

    @FXML
    private TableColumn<Enseignant, String> col_contrat;

    @FXML
    private TableColumn<Enseignant, String> col_email;

    @FXML
    private TableColumn<Enseignant, String> col_telephone;

    @FXML
    private TableColumn<Enseignant, Integer> col_salaire;

    @FXML
    private ImageView img_enseignant;

    @FXML
    private Label lbl_cni;

    @FXML
    private Label lbl_nom;

    @FXML
    private Label lbl_prenom;

    @FXML
    private Label lbl_sexe;

    @FXML
    private Label lbl_adresse;

    @FXML
    private Label lbl_contrat;

    @FXML
    private Label lbl_email;

    @FXML
    private Label lbl_telephone;

    @FXML
    private Label lbl_salaire;

    @FXML
    private JFXButton btn_modifier;

    @FXML
    private JFXButton btn_supprimer;

    @FXML
    private JFXButton btn_ajouter;
    
    Connection cnx;
    PreparedStatement st;
	ResultSet result;
	NewEnseignantController newEnseignantController = new NewEnseignantController();
	Enseignant enseignant;
	double x;
	double y;

    @FXML
    void ajouterEnseignant() {
    	this.newEnseignantController.setModif(false);
    	formNewEnseignant();
    	System.out.println(this.newEnseignantController.isModif());
    }

    @FXML
    void chercherEnseignant(KeyEvent event) {
    	 ObservableList<Enseignant> listEnseignant = FXCollections.observableArrayList();
    	table_enseignant.getItems().clear();
    	String sql = "select cni,nom,prenom,sexe,adresse,contrat,email,telephone,salaire from enseignant"
    			+ " where (cni like '%"+txt_search.getText()+"%' or nom like '%"+txt_search.getText()+"%' "
    					+ "or prenom like '%"+txt_search.getText()+"%' or sexe like '%"+txt_search.getText()+"%' "
    							+ "or adresse like '%"+txt_search.getText()+"%' or contrat like '%"+txt_search.getText()+"%' "
    									+ "or email like '%"+txt_search.getText()+"%') order by nom";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				listEnseignant.add(new Enseignant(result.getString("cni"), result.getString("nom"), 
						result.getString("prenom"), result.getString("sexe"), result.getString("adresse"), 
						result.getString("contrat"), result.getString("email"), result.getString("telephone"), 
						result.getInt("salaire")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	col_cni.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("cni"));
    	col_nom.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("nom"));
    	col_prenom.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("prenom"));
    	col_sexe.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("sexe"));
    	col_adresse.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("adresse"));
    	col_contrat.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("contrat"));
    	col_email.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("email"));
    	col_telephone.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("telephone"));
    	col_salaire.setCellValueFactory(new PropertyValueFactory<Enseignant, Integer>("salaire"));
    	table_enseignant.setItems(listEnseignant);

    }

    @FXML
    void modifierEnseignant() {
    	enseignant = table_enseignant.getSelectionModel().getSelectedItem();
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("/interfaces/NewEnseignant.fxml"));
    	try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	NewEnseignantController newEnseignantController = loader.getController();
    	
    	newEnseignantController.setModif(true);
    	newEnseignantController.remplirForm(enseignant.getCni(), enseignant.getNom(), enseignant.getPrenom(), enseignant.getSexe(), 
    			enseignant.getAdresse(), enseignant.getContrat(), enseignant.getEmail(), enseignant.getTelephone(), enseignant.getSalaire(), img_enseignant.getImage());
    	
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
    	System.out.println(newEnseignantController.isModif());
    }

    @FXML
    void refreshTable() {
    	showTable();
    }

    @FXML
    void supprimerEnseignant() {
    	String sql = "delete from enseignant where cni= '"+lbl_cni.getText()+"'";
    	
    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		 alert.setHeaderText("CWS Application");
		 alert.setContentText("Voulez-vous vraiment supprimer cet enseignant ?");
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
						 alert2.setContentText("Enseignant supprimé avec succès !");
						 alert2.showAndWait();
					} catch (SQLException e) {
						e.printStackTrace();
					}
			 }
		 });
    }

    @FXML
    void voireCours() {
    	formVoireCours();
    }
    
    public ObservableList<Enseignant> listEnseignant = FXCollections.observableArrayList();
    public void showTable() {
    	table_enseignant.getItems().clear();
    	String sql = "select cni,nom,prenom,sexe,adresse,contrat,email,telephone,salaire from enseignant order by nom";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				listEnseignant.add(new Enseignant(result.getString("cni"), result.getString("nom"), 
						result.getString("prenom"), result.getString("sexe"), result.getString("adresse"), 
						result.getString("contrat"), result.getString("email"), result.getString("telephone"), 
						result.getInt("salaire")));
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	col_cni.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("cni"));
    	col_nom.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("nom"));
    	col_prenom.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("prenom"));
    	col_sexe.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("sexe"));
    	col_adresse.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("adresse"));
    	col_contrat.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("contrat"));
    	col_email.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("email"));
    	col_telephone.setCellValueFactory(new PropertyValueFactory<Enseignant, String>("telephone"));
    	col_salaire.setCellValueFactory(new PropertyValueFactory<Enseignant, Integer>("salaire"));
    	table_enseignant.setItems(listEnseignant);
    	clearDetails();
    	btn_modifier.setDisable(true);
    	btn_supprimer.setDisable(true);
    	System.out.println("Mise à jour de la table");
    }
    
    @FXML
    void tableEnseignantClick() {
    	Enseignant enseignant = table_enseignant.getSelectionModel().getSelectedItem();
    	String sql = "select cni,nom,prenom,sexe,adresse,contrat,email,telephone,salaire,image from enseignant"
    			+ " where cni= '"+enseignant.getCni() +"'";
    	
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			byte byteImage[];
			Blob blob;
			while(result.next()) {
				lbl_cni.setText(result.getString("cni"));
				lbl_nom.setText(result.getString("nom"));
				lbl_prenom.setText(result.getString("prenom"));
				lbl_sexe.setText(result.getString("sexe"));
				lbl_adresse.setText(result.getString("adresse"));
				lbl_contrat.setText(result.getString("contrat"));
				lbl_email.setText(result.getString("email"));
				lbl_telephone.setText(result.getString("telephone"));
				lbl_salaire.setText(String.valueOf(result.getInt("salaire")));
				blob = result.getBlob("image");
				byteImage = blob.getBytes(1, (int)blob.length());
				Image img = new Image(new ByteArrayInputStream(byteImage), img_enseignant.getFitWidth(), img_enseignant.getFitHeight(), true, true);
				img_enseignant.setImage(img);
				btn_modifier.setDisable(false);
		    	btn_supprimer.setDisable(false);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    
    public void clearDetails() {
    	lbl_cni.setText("...");
    	lbl_nom.setText("...");
    	lbl_prenom.setText("...");
    	lbl_sexe.setText("...");
    	lbl_adresse.setText("...");
    	lbl_contrat.setText("...");
    	lbl_email.setText("...");
    	lbl_telephone.setText("...");
    	lbl_salaire.setText("...");
    	img_enseignant.setImage(null);
    }
    
    public void formNewEnseignant() {
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/NewEnseignant.fxml"));
			
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
    
    public void formVoireCours() {
    	enseignant = table_enseignant.getSelectionModel().getSelectedItem();
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(getClass().getResource("/interfaces/VoireCours.fxml"));
    	try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	VoireCoursController voireCoursController = loader.getController();
    	voireCoursController.showTable(enseignant.getCni());
    	    	
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
