package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import at.favre.lib.crypto.bcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable{
	
	@FXML
    private AnchorPane main_form;

    @FXML
    private TextField txt_username;

    @FXML
    private PasswordField txt_password;

    @FXML
    private JFXButton btn_login;

    @FXML
    private Button btn_close;
    
    @FXML
    private Label lbl_forgotPwd;
    
    @FXML
    private Label lbl_inscrire;
    
    Connection cnx;
    public PreparedStatement st;
	public ResultSet result;
	private double x;
	private double y;
    
    public void close() {
    	System.exit(0);
    }
    
    @FXML
    void signIn_form() {
    	lbl_inscrire.getScene().getWindow().hide();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/SignIn.fxml"));
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
    
    @FXML
    void forgotPwd_form() {
    	lbl_forgotPwd.getScene().getWindow().hide();
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/interfaces/ForgotPassword.fxml"));
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
    
    @FXML
    void login() {
    	String username = txt_username.getText();
    	String password = txt_password.getText();
    	//String bdusername = null;
    	String bdpassword = "";
    	
    	String sql = "select * from admin where username= '"+username+"'";
    	try {
			st = cnx.prepareStatement(sql);
			result = st.executeQuery();
			while (result.next()) {
				//bdusername = result.getString("username");
				bdpassword = result.getString("password");
			}
			
			if (!bdpassword.isBlank()) {
				BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), bdpassword);
				boolean verification = result.verified;
				
				if (verification) {
					Alert alert = new Alert(Alert.AlertType.INFORMATION);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Bienvenue !");
					 alert.showAndWait().ifPresent(response -> {
						 
						 if (response == ButtonType.OK) {
							 btn_login.getScene().getWindow().hide();
							 Parent root;
								try {
									root = FXMLLoader.load(getClass().getResource("/interfaces/Dashboard.fxml"));
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
					 
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Mot de passe incorrect !!");
					 alert.showAndWait();
				}
			}else {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Nom d'utilisateur incorrect !!");
				 alert.showAndWait();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
	}

}
