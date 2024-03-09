package controller;

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

import com.jfoenix.controls.JFXButton;

import application.ConnexionMysql;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.Cours;
import model.Transaction;

public class TransactionController implements Initializable{
	
	 @FXML
	    private TextField txt_numRecu;

	    @FXML
	    private JFXButton btn_ajouter;

	    @FXML
	    private JFXButton btn_nouveau;

	    @FXML
	    private JFXButton btn_supprimer;

	    @FXML
	    private JFXButton btn_modifier;

	    @FXML
	    private ComboBox<String> cbx_specialite;

	    @FXML
	    private ComboBox<String> cbx_etudiant;

	    @FXML
	    private ComboBox<String> cbx_banque;

	    @FXML
	    private TextField txt_montant;

	    @FXML
	    private TextField txt_search;

	    @FXML
	    private ComboBox<String> cbx_motif;

	    @FXML
	    private DatePicker dp_date;

	    @FXML
	    private TableView<Transaction> table_transaction;

	    @FXML
	    private TableColumn<Transaction, String> col_numRecu;

	    @FXML
	    private TableColumn<Transaction, String> col_etudiant;

	    @FXML
	    private TableColumn<Transaction, String> col_specialite;

	    @FXML
	    private TableColumn<Transaction, String> col_banque;

	    @FXML
	    private TableColumn<Transaction, String> col_motif;

	    @FXML
	    private TableColumn<Transaction, Integer> col_montant;

	    @FXML
	    private TableColumn<Transaction, Date> col_date;
	    
	    Connection cnx;
	    PreparedStatement st, st2;
		ResultSet result, result2;
		
		
		public void ajouter() {
			String nom = "";
    		String prenom = "";
    		String[] nameParts = cbx_etudiant.getValue().split(" ");
    		if (nameParts.length == 2) {
    			nom = nameParts[0];
    			prenom = nameParts[1];
    		}
    		String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
    		String matricule = "";
    		try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					matricule = result.getString("matricule");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
    		int banque = 0;
    		String sql2 = "select idBanque from banque where nomBanque= '"+ cbx_banque.getValue() +"'";
    		try {
				st = cnx.prepareStatement(sql2);
				result = st.executeQuery();
				if (result.next()) {
					banque = result.getInt("idBanque");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
    		String sql3 = "select numRecu from transaction where numRecu= '"+ txt_numRecu.getText() +"'";
    		try {
				st = cnx.prepareStatement(sql3);
				result = st.executeQuery();
				if (result.next()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Deux transactions ne peuvent pas avoir le meme Numéro de Reçu");
					 alert.showAndWait();
					
				}else {
					String sql4 = "insert into transaction(numRecu,etudiant,banque,motif,montant,date) values(?,?,?,?,?,?)";
					try {
						st = cnx.prepareStatement(sql4);
						st.setString(1, txt_numRecu.getText());
						st.setString(2, matricule);
						st.setInt(3, banque);
						st.setString(4, cbx_motif.getValue());
						st.setInt(5, Integer.parseInt(txt_montant.getText()));
						java.util.Date dateValue = java.util.Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		    			Date sqlDate = new Date(dateValue.getTime());
		    			st.setDate(6, sqlDate);
		    			st.executeUpdate();
		    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
						 alert.setHeaderText("CWS Application");
						 alert.setContentText("Transaction ajoutée avec succès !");
						 alert.showAndWait();
						 showTable();
						
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
    		
			
		}

	    @FXML
	    void ajouterTransaction() {
	    	if (!txt_numRecu.getText().isBlank() && !cbx_specialite.getValue().isBlank()
	    			&& !cbx_banque.getValue().isBlank() && !cbx_etudiant.getValue().isBlank()
	    			&& !cbx_motif.getValue().isBlank() && !txt_montant.getText().isBlank() && dp_date.getValue() != null) {
	    		
	    		String nom = "";
	    		String prenom = "";
	    		String[] nameParts = cbx_etudiant.getValue().split(" ");
	    		if (nameParts.length == 2) {
	    			nom = nameParts[0];
	    			prenom = nameParts[1];
	    		}
	    		String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
	    		String matricule = "";
	    		try {
					st = cnx.prepareStatement(sql);
					result = st.executeQuery();
					if (result.next()) {
						matricule = result.getString("matricule");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    		
	    		String sql2 = "select nom,prenom,motif from transaction,etudiant where transaction.etudiant=etudiant.matricule "
	    				+ "and nom= '"+nom+"' and prenom= '"+prenom+"' order by motif desc";
	    		ObservableList<String> listMotif = FXCollections.observableArrayList();
	    		try {
					st = cnx.prepareStatement(sql2);
					result = st.executeQuery();
					while (result.next()) {
						listMotif.add(result.getString("motif"));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
	    		
	    		if (listMotif.isEmpty() && !cbx_motif.getValue().equalsIgnoreCase("Inscription")) {
	    			Alert alert = new Alert(Alert.AlertType.ERROR);
					 alert.setHeaderText("CWS Application");
					 alert.setContentText("Il faut d'abord payer l'inscription");
					 alert.showAndWait();
	    		}
	    		
	    		if (cbx_motif.getValue().equalsIgnoreCase("Inscription")) {
	    			String nom2 = "";
	        		String prenom2 = "";
	        		String[] nameParts2 = cbx_etudiant.getValue().split(" ");
	        		if (nameParts2.length == 2) {
	        			nom2 = nameParts2[0];
	        			prenom2 = nameParts2[1];
	        		}
	    			String sql3 = "select nom,prenom,motif from transaction,etudiant where transaction.etudiant=etudiant.matricule "
	    					+ "and nom= '"+nom2+"' and prenom= '"+prenom2+"' and motif= '"+cbx_motif.getValue()+"'";
	    			try {
						st = cnx.prepareStatement(sql3);
						result = st.executeQuery();
						if (result.next()) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							 alert.setHeaderText("CWS Application");
							 alert.setContentText("L'inscription a déjà été payée");
							 alert.showAndWait();
						}else {
							ajouter();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
	        		
	    		}
	    				
	    		
	    		if (cbx_motif.getValue().equalsIgnoreCase("Tranche 1")) {
	    			for (String motif: listMotif) {
	    				if (motif.equalsIgnoreCase("Inscription")) {
	    					ajouter();
	    				}else if(motif.equalsIgnoreCase("Tranche 1")) {
	    					ajouter();
	    				}
	    				else {
	    					Alert alert = new Alert(Alert.AlertType.ERROR);
	    					 alert.setHeaderText("CWS Application");
	    					 alert.setContentText("Il faut d'abord payer l'inscription");
	    					 alert.showAndWait();
	    					 //break;
	    				}
	    				break;
	    			}
	    		}
	    		
	    		if (cbx_motif.getValue().equalsIgnoreCase("Tranche 2")) {
	    			for (String motif: listMotif) {
	    				if (motif.equalsIgnoreCase("Tranche 1")) {
	    					ajouter();
	    				}else if (motif.equalsIgnoreCase("Tranche 2")) {
	    					ajouter();
	    				}
	    				else {
	    					Alert alert = new Alert(Alert.AlertType.ERROR);
	    					alert.setHeaderText("CWS Application");
	    					alert.setContentText("Il faut d'abord payer la Tranche 1");
	    					alert.showAndWait();
	    					//break;
	    				}
	    				break;
	    			}
	    		}
	    		
	    		if (cbx_motif.getValue().equalsIgnoreCase("Tranche 3")) {
	    			for (String motif: listMotif) {
	    				if (motif.equalsIgnoreCase("Tranche 2")) {
	    					ajouter();
	    				}else if (motif.equalsIgnoreCase("Tranche 3")) {
	    					ajouter();
	    				}
	    				else {
	    					Alert alert = new Alert(Alert.AlertType.ERROR);
	    					alert.setHeaderText("CWS Application");
	    					alert.setContentText("Il faut d'abord payer la Tranche 2");
	    					alert.showAndWait();
	    					//break;
	    				}
	    				break;
	    			}
	    		}
	    		
	    		
	    	}else {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Veuillez remplir correctement tous les champs");
				 alert.showAndWait();
	    	}
	    	
	    }

	    @FXML
	    void chercherTransaction() throws SQLException {
	    	table_transaction.getItems().clear();
			 ObservableList<Transaction> listTransaction = FXCollections.observableArrayList();
    				
    				String sql3 = "select numRecu,nom,prenom,nomSpecialite,nomBanque,motif,montant,date from transaction,etudiant,banque,specialite "
        	    			+ "where transaction.etudiant=etudiant.matricule and transaction.banque=banque.idBanque and etudiant.specialite=specialite.idSpecialite and "
        	    			+ "(numRecu like '%"+txt_search.getText()+"%' or nom like '%"+txt_search.getText()+"%' or prenom like '%"+txt_search.getText()+"%' "
        	    					+ "or nomBanque like '%"+txt_search.getText()+"%' or motif like '%"+txt_search.getText()+"%' or date like '%"+txt_search.getText()+"%') order by nom";
        			
        			st = cnx.prepareStatement(sql3);
        			result = st.executeQuery();
        			while (result.next()) {
        				String nom = result.getString("nom");
    					String prenom = result.getString("prenom");
    					String etudiant = nom + " " + prenom;
    					
    					listTransaction.add(new Transaction(result.getString("numRecu"),
    							etudiant, result.getString("nomSpecialite"), result.getString("nomBanque"),
    							result.getString("motif"), result.getInt("montant"), result.getDate("date")));
        			}
    			
    			col_numRecu.setCellValueFactory(new PropertyValueFactory<Transaction, String>("numRecu"));
    	    	col_etudiant.setCellValueFactory(new PropertyValueFactory<Transaction, String>("etudiant"));
    	    	col_specialite.setCellValueFactory(new PropertyValueFactory<Transaction, String>("specialite"));
    	    	col_banque.setCellValueFactory(new PropertyValueFactory<Transaction, String>("banque"));
    	    	col_motif.setCellValueFactory(new PropertyValueFactory<Transaction, String>("motif"));
    	    	col_montant.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("montant"));
    	    	col_date.setCellValueFactory(new PropertyValueFactory<Transaction, Date>("date"));
    	    	
    	    	table_transaction.setItems(listTransaction);
    	    	//btn_modifier.setDisable(true);
    	    	//btn_supprimer.setDisable(true);
    	    	System.out.println("Mise à jour de la table");
	    
	    }

	    @FXML
	    void modifierTransaction() throws SQLException {
	    	if (!txt_numRecu.getText().isBlank() && !cbx_specialite.getValue().isBlank()
	    			&& !cbx_banque.getValue().isBlank() && !cbx_etudiant.getValue().isBlank()
	    			&& !cbx_motif.getValue().isBlank() && !txt_montant.getText().isBlank() && dp_date.getValue() != null) {
	    		
	    		String nom = "";
	    		String prenom = "";
	    		String[] nameParts = cbx_etudiant.getValue().split(" ");
	    		if (nameParts.length == 2) {
	    			nom = nameParts[0];
	    			prenom = nameParts[1];
	    		}
	    		String sql = "select matricule from etudiant where nom= '"+ nom +"' and prenom= '"+ prenom +"'";
	    		String matricule = "";
	    		st = cnx.prepareStatement(sql);
	    		result = st.executeQuery();
	    		if (result.next()) {
	    			matricule = result.getString("matricule");
	    		}
	    		
	    		int banque = 0;
	    		String sql2 = "select idBanque from banque where nomBanque= '"+ cbx_banque.getValue() +"'";
	    		st = cnx.prepareStatement(sql2);
	    		result = st.executeQuery();
	    		if (result.next()) {
	    			banque = result.getInt("idBanque");
	    		}
	    		
	    		String sql3 = "update transaction set etudiant=?, banque=?, motif=?, montant=?, date=? where numRecu= '"+txt_numRecu.getText()+"'";
	    		st = cnx.prepareStatement(sql3);
	    		st.setString(1, matricule);
	    		st.setInt(2, banque);
	    		st.setString(3, cbx_motif.getValue());
	    		st.setInt(4, Integer.parseInt(txt_montant.getText()));
	    		java.util.Date dateValue = java.util.Date.from(dp_date.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
    			Date sqlDate = new Date(dateValue.getTime());
    			st.setDate(5, sqlDate);
    			st.executeUpdate();
				clearAll();
				showTable();
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Transaction modifiée avec succès !");
				 alert.showAndWait();
	    	}else {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Veuillez remplir correctement tous les champs");
				 alert.showAndWait();
	    	}
	    		
	    }

	    @FXML
	    void nouveau() {
	    	clearAll();
	    }
	    
	    public void remplirComboMotif() {
	    	List<String> listMotif = new ArrayList<String>();
	    	listMotif.add("Inscription");
	    	listMotif.add("Tranche 1");
	    	listMotif.add("Tranche 2");
	    	listMotif.add("Tranche 3");
	    	cbx_motif.setItems(FXCollections.observableArrayList(listMotif));
	    }
	    
	    public void remplirComboBanque() {
	    	String sql = "select nomBanque from banque order by nomBanque";
	    	List<String> listBanque= new ArrayList<String>();
	    	try {
				st2 = cnx.prepareStatement(sql);
				result2 = st2.executeQuery();
				while (result2.next()) {
					listBanque.add(result2.getString("nomBanque"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	cbx_banque.setItems(FXCollections.observableArrayList(listBanque));
	    }
	    
	    public void remplirComboSpecialite() {
	    	String sql = "select nomSpecialite from specialite";
	    	List<String> listSpecialite= new ArrayList<String>();
	    	try {
				st2 = cnx.prepareStatement(sql);
				result2 = st2.executeQuery();
				while (result2.next()) {
					listSpecialite.add(result2.getString("nomSpecialite"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	cbx_specialite.setItems(FXCollections.observableArrayList(listSpecialite));
	    }

	    @FXML
	    void remplirComboEtudiant(MouseEvent event) {
	    	int specialite = 0;
	    	String sql = "select idSpecialite from specialite where nomSpecialite= '"+ cbx_specialite.getValue() +"'";
	    	try {
				st2 = cnx.prepareStatement(sql);
				result2 = st2.executeQuery();
				if (result2.next()) {
					specialite = result2.getInt("idSpecialite");
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
	    	
	    	String sql2 = "select nom,prenom from etudiant where specialite= "+specialite;
	    	String nom = "";
	    	String prenom = "";
	    	String etudiant = "";
	    	List<String> listEtudiant= new ArrayList<String>();
	    	try {
				st2 = cnx.prepareStatement(sql2);
				result2 = st2.executeQuery();
				while (result2.next()) {
					nom = result2.getString("nom");
					prenom = result2.getString("prenom");
					etudiant = nom + " " + prenom;
					listEtudiant.add(etudiant);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	cbx_etudiant.setItems(FXCollections.observableArrayList(listEtudiant));
	    }

	    @FXML
	    void supprimerTransaction() {
	    	if (!txt_numRecu.getText().isBlank() && !cbx_specialite.getValue().isBlank()
	    			&& !cbx_banque.getValue().isBlank() && !cbx_etudiant.getValue().isBlank()
	    			&& !cbx_motif.getValue().isBlank() && !txt_montant.getText().isBlank() && dp_date.getValue() != null) {
	    		
	    		String sql = "delete from transaction where numRecu= '"+txt_numRecu.getText()+"'";
		    	
		    	Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Voulez-vous vraiment supprimer cette transaction ?");
				 alert.showAndWait().ifPresent(response ->{
					 if (response == ButtonType.OK) {
						 try {
								st = cnx.prepareStatement(sql);
								st.executeUpdate();
								
								clearAll();
								showTable();
								btn_modifier.setDisable(true);
						    	btn_supprimer.setDisable(true);
						    	Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
								 alert2.setHeaderText("CWS Application");
								 alert2.setContentText("Transaction supprimée avec succès !");
								 alert2.showAndWait();
							} catch (SQLException e) {
								e.printStackTrace();
							}
					 }
				 });
				 
	    	}else {
	    		Alert alert = new Alert(Alert.AlertType.ERROR);
				 alert.setHeaderText("CWS Application");
				 alert.setContentText("Veuillez remplir correctement tous les champs");
				 alert.showAndWait();
	    	}
	    	
	    }
	    
	    public String getNomSpecialite(int idSpecialite) {
	    	String specialite = "";
	    	String sql = "select nomSpecialite from specialite where idSpecialite= "+idSpecialite;
	    	try {
				st2 = cnx.prepareStatement(sql);
				result2 = st2.executeQuery();
				if (result2.next()) {
					specialite = result2.getString("nomSpecialite");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return specialite;
	    }
	    
	    public ObservableList<Transaction> listTransaction = FXCollections.observableArrayList();
	    public void showTable() {
	    	table_transaction.getItems().clear();
	    	
	    	String sql = "select numRecu,nom,prenom,specialite,nomBanque,motif,montant,date from transaction,etudiant,banque "
	    			+ "where transaction.etudiant=etudiant.matricule and transaction.banque=banque.idBanque "
	    			+ "order by nom";
	    	
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				while (result.next()) {
					String nom = result.getString("nom");
					String prenom = result.getString("prenom");
					String etudiant = nom + " " + prenom;
					String specialite = "";
					int specialit = result.getInt("specialite");
					specialite = getNomSpecialite(specialit);
					
					listTransaction.add(new Transaction(result.getString("numRecu"),
							etudiant, specialite, result.getString("nomBanque"),
							result.getString("motif"), result.getInt("montant"), result.getDate("date")));
					
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    	col_numRecu.setCellValueFactory(new PropertyValueFactory<Transaction, String>("numRecu"));
	    	col_etudiant.setCellValueFactory(new PropertyValueFactory<Transaction, String>("etudiant"));
	    	col_specialite.setCellValueFactory(new PropertyValueFactory<Transaction, String>("specialite"));
	    	col_banque.setCellValueFactory(new PropertyValueFactory<Transaction, String>("banque"));
	    	col_motif.setCellValueFactory(new PropertyValueFactory<Transaction, String>("motif"));
	    	col_montant.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("montant"));
	    	col_date.setCellValueFactory(new PropertyValueFactory<Transaction, Date>("date"));
	    	
	    	table_transaction.setItems(listTransaction);
	    	btn_modifier.setDisable(true);
	    	btn_supprimer.setDisable(true);
	    	System.out.println("Mise à jour de la table");
	    }

	    @FXML
	    void tableTransactionClick(MouseEvent event) {
	    	Transaction transaction = table_transaction.getSelectionModel().getSelectedItem();
	    	String sql = "select numRecu,nom,prenom,specialite,nomBanque,motif,montant,date from transaction,etudiant,banque "
	    			+ "where transaction.etudiant=etudiant.matricule and transaction.banque=banque.idBanque and numRecu= '"+ transaction.getNumRecu() +"'";
	    	
	    	try {
				st = cnx.prepareStatement(sql);
				result = st.executeQuery();
				if (result.next()) {
					txt_numRecu.setText(result.getString("numRecu"));
					int specialit = result.getInt("specialite");
					cbx_specialite.setValue(getNomSpecialite(specialit));
					String nom = result.getString("nom");
					String prenom = result.getString("prenom");
					String etudiant = nom + " " + prenom;
					cbx_etudiant.setValue(etudiant);
					cbx_banque.setValue(result.getString("nomBanque"));
					cbx_motif.setValue(result.getString("motif"));
					txt_montant.setText(String.valueOf(result.getInt("montant")));
					Date dat = result.getDate("date");
					String datestr = String.valueOf(dat);
					LocalDate date = LocalDate.parse(datestr);
					dp_date.setValue(date);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
	    	
	    	txt_numRecu.setDisable(true);
	    	btn_modifier.setDisable(false);
	    	btn_supprimer.setDisable(false);
	    }
	    
	    public void clearAll() {
	    	cbx_etudiant.getSelectionModel().clearSelection();
	    	cbx_etudiant.setValue("");
	    	cbx_banque.getSelectionModel().clearSelection();
	    	cbx_specialite.getSelectionModel().clearSelection();
	    	cbx_motif.getSelectionModel().clearSelection();
	    	txt_numRecu.setText("");
	    	txt_montant.setText("");
	    	dp_date.setValue(null);
	    	btn_ajouter.setDisable(false);
	    	btn_modifier.setDisable(true);
	    	btn_supprimer.setDisable(true);
	    	txt_numRecu.setDisable(false);
	    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		cnx = ConnexionMysql.connexionDB();
		showTable();
		remplirComboBanque();
		remplirComboMotif();
		remplirComboSpecialite();
		
	}

}
