package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable{
	
	@FXML
    private StackPane main_form;
	
	 @FXML
	 private Button btn_logout;
	 
	 @FXML
	    private JFXButton btn_dashboard;

	    @FXML
	    private JFXButton btn_etudiant;

	    @FXML
	    private JFXButton btn_cours;

	    @FXML
	    private JFXButton btn_enseignant;

	    @FXML
	    private JFXButton btn_note;

	    @FXML
	    private JFXButton btn_scolarite;

	    @FXML
	    private JFXButton btn_attestation;
	    
	    @FXML
	    private JFXButton btn_emploiTemps;
	 
	 @FXML
	 private AnchorPane anchor_interface;
	 private Parent fxml;
	 
	 private double x;
	 private double y;
	
	 @FXML
	    void close() {
		 Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		 alert.setHeaderText("CWS Application");
		 alert.setContentText("Etes-vous sure de vouloir continuer ?");
		 alert.showAndWait().ifPresent(response -> {
			 if (response == ButtonType.OK) {
				 System.exit(0);
			 }
		 });
		 	
	    }

	    @FXML
	    void minimize() {
	    	Stage stage = (Stage)main_form.getScene().getWindow();
	    	stage.setIconified(true);
	    }
	    
	    @FXML
	    void logOut() {
	    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			 alert.setHeaderText("CWS Application");
			 alert.setContentText("Etes-vous sure de vouloir vous déconnecter ?");
			 alert.showAndWait().ifPresent(response -> {
				 if (response == ButtonType.OK) {
					 btn_logout.getScene().getWindow().hide();
					 Parent root;
					try {
						root = FXMLLoader.load(getClass().getResource("/interfaces/Login.fxml"));
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
			 });
	    }
	    
	    @FXML
	    void attestationForm() {

	    }
	    
	    @FXML
	    void coursForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Cours.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void dashboardForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Home.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void enseignantForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Enseignant.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void etudiantForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Etudiant.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    @FXML
	    void noteForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Note.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }

	    @FXML
	    void scolariteForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Transaction.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    @FXML
	    void emploiTempsForm() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/EmploiTemps.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    
	    public void lancement() {
	    	try {
				fxml = FXMLLoader.load(getClass().getResource("/interfaces/Home.fxml"));
				anchor_interface.getChildren().removeAll();
				anchor_interface.getChildren().setAll(fxml);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lancement();
	}

}
