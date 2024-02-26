package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable{
	
	@FXML
    private StackPane main_form;
	
	 @FXML
	 private Button btn_logout;
	 
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

}
