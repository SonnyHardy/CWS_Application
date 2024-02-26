package controller;

import java.io.File;
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
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SignInController implements Initializable{
	
	 @FXML
	    private AnchorPane main_form;

	    @FXML
	    private TextField txt_username;

	    @FXML
	    private PasswordField txt_password;
	    
	    @FXML
	    private JFXButton btn_inscrire;

	    @FXML
	    private Button btn_close;

	    @FXML
	    private Label lbl_connecter;
	    
	    @FXML
	    private Label lbl_usernameError;

	    @FXML
	    private Label lbl_pwdError;
	    
	    @FXML
	    private ImageView img_usernameError;

	    @FXML
	    private ImageView img_pwdError;
	    
	    @FXML
	    private TextField txt_code;
	    
	    @FXML
	    private JFXButton btn_valider;

	    
	    Connection cnx;
	    public PreparedStatement st;
		public ResultSet result;
		private double x;
		private double y;

	    @FXML
	    void close() {
	    	System.exit(0);
	    }
	    
	    @FXML
	    void connexion_form() {
	    	lbl_connecter.getScene().getWindow().hide();
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
	    
	   
	    @FXML
	    void inscrire() {
	    	String hashedPassword;
	    	String hashPassword;
	    	String username = txt_username.getText();
	    	String password = txt_password.getText();
	    	
	    	final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    	Pattern pattern = Pattern.compile(EMAIL_REGEX);
	    	boolean matcher = Pattern.matches(EMAIL_REGEX, username);
	    	if (!matcher) {
	    		lbl_usernameError.setText("Adresse mail invalide !");
	    		img_usernameError.setVisible(true);
	    		img_usernameError.setOpacity(1);
	    	}else {
	    		lbl_usernameError.setText("");
	    		img_usernameError.setVisible(false);
	    		img_usernameError.setOpacity(0);
	    	}
	    	
	    	
	    	
	    	final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[@#$%&-_~|^/*+.,?;!§µ<>:£])(?!.*\\s).{8,}$";
	    	Pattern pattern2 = Pattern.compile(PASSWORD_REGEX);
	    	boolean matcher2 = Pattern.matches(PASSWORD_REGEX, password);
	    	if (!matcher2) {
	    		lbl_pwdError.setText("Mot de passe faible !");
	    		img_pwdError.setVisible(true);
	    		img_pwdError.setOpacity(1);
	    	}else {
	    		lbl_pwdError.setText("");
	    		img_pwdError.setVisible(false);
	    		img_pwdError.setOpacity(0);
	    	}
	    	
	    	if (matcher && matcher2) {
	    		hashPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
		    	hashedPassword = hashPassword.toString();
		    	
		    	String sql = "insert into admin(username,password) values(?,?)";
		    	try {
					st = cnx.prepareStatement(sql);
					st.setString(1, username);
					st.setString(2, hashedPassword);
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		    	
		    	btn_inscrire.setDisable(true);
		    	btn_inscrire.setOpacity(0);
		    	btn_valider.setDisable(false);
		    	btn_valider.setOpacity(1);
		    	txt_code.setDisable(false);
		    	txt_code.setOpacity(1);
		    	
		    	String code = generateRandomCode();
		    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Nous vous avons envoyé un code d'activation par mail");
				 alert.showAndWait().ifPresent(response -> {
					 
					 if (response == ButtonType.OK) {
						 SendEmail("", "", username, "Code d'activation", "Votre code d'activation est: "+ code);
					 }
				 });
				 java.util.Date dat = new java.util.Date();
				 java.sql.Date date = new java.sql.Date(dat.getTime());
				 //System.out.println(date.toString());
				 String sql2 = "insert into activation(code,username,date) values(?,?,?)";
				 try {
					st = cnx.prepareStatement(sql2);
					st.setString(1, code);
					st.setString(2, username);
					st.setDate(3, date);
					st.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    	}
	    }
	    
	    @FXML
	    void validerCode() {
	    	String sql = "select code from activation where username= '"+txt_username.getText()+"'";
	    	String code = null;
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				while (result.next()) {
					code = result.getString("code");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	String txtCode = txt_code.getText();
	    	
	    	if (txtCode.equals(code)) {
	    		Alert alert = new Alert(Alert.AlertType.INFORMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Activation du compte réussie");
				 alert.showAndWait().ifPresent(response -> {
					 
					 if (response == ButtonType.OK) {
						 btn_valider.getScene().getWindow().hide();
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
	    	}else {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Code incorrect!! Veuillez reessayer");
				 alert.showAndWait();
	    	}
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
	    	
	    	public String generateRandomCode() {
		    	String characters = "1234567890";
		    	StringBuilder matricule = new StringBuilder();
		    	Random rand = new Random();
		    	for (int i=0; i<6; i++) {
		    		matricule.append(characters.charAt(rand.nextInt(characters.length())));
		    	}
		    	return matricule.toString();
		    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		
	}

}
