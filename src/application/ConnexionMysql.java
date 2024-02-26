package application;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class ConnexionMysql {
	
	public Connection cn = null;
	public static Connection connexionDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/cws_application", "root", "");
			System.out.println("Connexion à la base de données réussie !");
			return cn;
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Connexion échouée !");
			e.printStackTrace();
			return null;
		}
	}
}
