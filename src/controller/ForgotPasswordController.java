package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ForgotPasswordController implements Initializable{
	
	@FXML
    private StackPane stackPane;
	
	 @FXML
	    private AnchorPane main_form;

	    @FXML
	    private TextField txt_username;

	    @FXML
	    private Button btn_close;

	    @FXML
	    private PasswordField txt_newPwd;
	    
	    @FXML
	    private JFXButton btn_reinitialiser;

	    @FXML
	    private JFXButton btn_valider;

	    @FXML
	    private JFXButton btn_confirmer;

	    @FXML
	    private PasswordField txt_confirmPwd;

	    @FXML
	    private Label lbl_errorPwd1;

	    @FXML
	    private ImageView img_error1;

	    @FXML
	    private ImageView img_error2;

	    @FXML
	    private Label lbl_errorPwd2;

	    @FXML
	    private TextField txt_code;
	    
	    Connection cnx;
	    public PreparedStatement st;
		public ResultSet result;
		String code = generateRandomCode();
		double x;
		double y;

	    @FXML
	    void close() {
	    	System.exit(0);
	    }

	    @FXML
	    void confirmer() {
	    	String username = txt_username.getText();
	    	String newPwd = txt_newPwd.getText();
	    	String confirmPwd = txt_confirmPwd.getText();
	    	if (newPwd.isBlank() || confirmPwd.isBlank()) {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Veuillez remplir tous les champs");
				 alert.showAndWait();
	    	}else {
	    		if (confirmPwd.equals(newPwd)) {
	    			lbl_errorPwd2.setDisable(true);
		    		lbl_errorPwd2.setOpacity(0);
		    		img_error2.setDisable(true);
		    		img_error2.setOpacity(0);
	    			
	    			String hashPassword = BCrypt.withDefaults().hashToString(12, newPwd.toCharArray());
			    	String hashedPassword = hashPassword.toString();
	    			String sql = "update admin set username=?, password=? where username= '"+username+"'";
	    			try {
						st = cnx.prepareStatement(sql);
						st.setString(1, username);
						st.setString(2, hashedPassword);
						st.executeUpdate();
					} catch (SQLException e) {
						System.out.println("Erreur de mise à jour du mot de passe");
						e.printStackTrace();
					}
	    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Mot de passe réinitialisé avec succès!");
					 alert.showAndWait();
					 login_form();
	    		}else {
		    		lbl_errorPwd2.setDisable(false);
		    		lbl_errorPwd2.setOpacity(1);
		    		img_error2.setDisable(false);
		    		img_error2.setOpacity(1);
	    		}
	    	}
	    }

	    @FXML
	    void reinitialiser() {
	    	String username = txt_username.getText();
	    	String sql = "select username from admin where username= '"+username+"'";
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					 SendEmail("", "", username, "Code de réinitialisation", "Votre code de réinitialisation est: "+ code);
					 validerEnable();
					 Alert alert = new Alert(Alert.AlertType.INFORMATION);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Nous vous avons envoyé un code de réinitialisation par mail");
					 alert.showAndWait();
					 
				}else {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Nom d'utilisateur introuvable!! Réessayer");
					 alert.showAndWait();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    }

	    @FXML
	    void validerCode() {
	    	String txtCode = txt_code.getText();
	    	if (txtCode.equals(code)) {
	    		confirmerEnable();
	    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Entrer votre nouveau mot de passe");
				 alert.showAndWait();
	    	}else {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Code incorrect!! Réessayer");
				 alert.showAndWait();
	    	}
	    }

	    @FXML
	    void verifierPassword(KeyEvent event) {
	    	String newPwd = txt_newPwd.getText();
	    	final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[@#$%&-_~|^/*+.,?;!§µ<>:£])(?!.*\\s).{8,}$";
	    	Pattern pattern = Pattern.compile(PASSWORD_REGEX);
	    	boolean matcher = Pattern.matches(PASSWORD_REGEX, newPwd);
	    	
	    	if (!matcher) {
	    		lbl_errorPwd1.setText("Mot de passe faible");
	    		lbl_errorPwd1.setDisable(false);
	    		lbl_errorPwd1.setOpacity(1);
	    		img_error1.setDisable(false);
	    		img_error1.setOpacity(1);
	    	}else {
	    		lbl_errorPwd1.setDisable(true);
	    		lbl_errorPwd1.setOpacity(0);
	    		img_error1.setDisable(true);
	    		img_error1.setOpacity(0);
	    	}
	    }
	    
	    @FXML
	    void verifierUsername(KeyEvent event) {
	    	String username = txt_username.getText();
	    	final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    	Pattern pattern = Pattern.compile(EMAIL_REGEX);
	    	boolean matcher = Pattern.matches(EMAIL_REGEX, username);
	    	
	    	if (!matcher) {
	    		lbl_errorPwd1.setText("Adresse mail invalide");
	    		lbl_errorPwd1.setDisable(false);
	    		lbl_errorPwd1.setOpacity(1);
	    		img_error1.setDisable(false);
	    		img_error1.setOpacity(1);
	    	}else {
	    		lbl_errorPwd1.setDisable(true);
	    		lbl_errorPwd1.setOpacity(0);
	    		img_error1.setDisable(true);
	    		img_error1.setOpacity(0);
	    	}
	    }
	    
	    public void allDisable() {
	    	txt_username.setDisable(true);
	    	txt_username.setOpacity(0);
	    	txt_newPwd.setDisable(true);
	    	txt_newPwd.setOpacity(0);
	    	btn_confirmer.setDisable(true);
	    	btn_confirmer.setOpacity(0);
	    	btn_reinitialiser.setDisable(true);
	    	btn_reinitialiser.setOpacity(0);
	    	btn_valider.setDisable(true);
	    	btn_valider.setOpacity(0);
	    	txt_confirmPwd.setDisable(true);
	    	txt_confirmPwd.setOpacity(0);
	    	lbl_errorPwd1.setDisable(true);
	    	lbl_errorPwd1.setOpacity(0);
	    	lbl_errorPwd2.setDisable(true);
	    	lbl_errorPwd2.setOpacity(0);
	    	img_error1.setDisable(true);
	    	img_error1.setOpacity(0);
	    	img_error2.setDisable(true);
	    	img_error2.setOpacity(0);
	    	txt_code.setDisable(true);
	    	txt_code.setOpacity(0);
	    }
	    
	    public void reinitialiserEnable() {
	    	allDisable();
	    	txt_username.setDisable(false);
	    	txt_username.setOpacity(1);
	    	btn_reinitialiser.setDisable(false);
	    	btn_reinitialiser.setOpacity(1);
	    }
	    
	    public void validerEnable() {
	    	allDisable();
	    	txt_code.setDisable(false);
	    	txt_code.setOpacity(1);
	    	btn_valider.setDisable(false);
	    	btn_valider.setOpacity(1);
	    }
	    
	    public void confirmerEnable() {
	    	allDisable();
	    	txt_newPwd.setDisable(false);
	    	txt_newPwd.setOpacity(1);
	    	txt_confirmPwd.setDisable(false);
	    	txt_confirmPwd.setOpacity(1);
	    	btn_confirmer.setDisable(false);
	    	btn_confirmer.setOpacity(1);
	    }
	    
	    public String generateRandomCode() {
	    	String characters = "1234567890";
	    	StringBuilder matricule = new StringBuilder();
	    	Random rand = new Random();
	    	for (int i=0; i<6; i++) {
	    		matricule.append(characters.charAt(rand.nextInt(characters.length())));
	    	}
	    	return matricule.toString();
	    }
	    
	    private void   SendEmail(String user, String pass, String to, String sub, String msg)
        {
            Properties prop= new Properties();
            
             prop.put("mail.smtp.ssl.trust","smtp.gmail.com");
             prop.put("mail.smtp.auth",true);
             prop.put("mail.smtp.starttls.enable",true);
             prop.put("mail.smtp.host","localhost");
             prop.put("mail.smtp.port",9025);
             
             
             Session session= Session.getInstance(prop, new javax.mail.Authenticator()
             {
                 @Override
                 protected javax.mail.PasswordAuthentication getPasswordAuthentication()
                 {
                 return new javax.mail.PasswordAuthentication(user, pass);
           
                 }
                 
             });
             
             try
             {
                 Message message= new MimeMessage(session);
                 
                 message.setFrom( new InternetAddress("no-reply@gmail.com"));
                 message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
                 message.setSubject(sub);
                 message.setText(msg);
                 
                 Transport.send(message);
             }   
             
             catch(Exception e)
             {
              JOptionPane.showMessageDialog(null,e);
             }
             
    }
	    
	public void login_form() {
		btn_confirmer.getScene().getWindow().hide();
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
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		reinitialiserEnable();
		
	}
	
	
}
