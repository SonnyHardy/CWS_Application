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
import model.Etudiant;

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
							
							lbl_matricule.setText("...");
							lbl_nom.setText("...");
							lbl_prenom.setText("...");
							lbl_specialite.setText("...");
							lbl_adresse.setText("...");
							lbl_dateNais.setText("...");
							lbl_sexe.setText("...");
							lbl_email.setText("...");
							lbl_telephone.setText("...");
							img_etudiant.setImage(null);
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTable();
	}


}
