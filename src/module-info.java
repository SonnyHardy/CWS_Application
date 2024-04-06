module CWS_Application {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires javafx.swing;
	requires javafx.web;
	requires java.sql;
	requires mysql.connector.java;
	requires java.desktop;
	requires com.jfoenix;
	requires itextpdf;
	requires fontawesomefx;
	requires bcrypt;
	requires javax.mail;
	requires activation;
	requires layout; 
	requires kernel;
	
	exports controller to javafx.fxml;
	opens model to javafx.base;
	opens controller to javafx.fxml;
	opens application to javafx.graphics, javafx.fxml;
}
