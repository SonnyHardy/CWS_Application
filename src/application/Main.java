package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	
	private double x = 0;
	private double y = 0;
	
	@Override
	public void start(Stage primaryStage) {
		
		try {	
			Parent root = FXMLLoader.load(getClass().getResource("/interfaces/Etudiant.fxml"));
			Scene scene = new Scene(root);
			
			root.setOnMousePressed((MouseEvent event) ->{
				x = event.getSceneX();
				y = event.getSceneY();
			});
			
			root.setOnMouseDragged((MouseEvent event) ->{
				primaryStage.setX(event.getScreenX() - x);
				primaryStage.setY(event.getScreenY() - y);
				
				primaryStage.setOpacity(.8);
			});
			
			root.setOnMouseReleased((MouseEvent event) ->{
				primaryStage.setOpacity(1);
			});
			
			primaryStage.setScene(scene);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			primaryStage.getIcons().add(new Image(getClass().getResource("icon.png").toExternalForm()));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
