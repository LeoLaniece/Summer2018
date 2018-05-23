package javaServer;

import javafx.scene.Group;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class CanvasPass extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		HBox root = new HBox();
		Scene scene = new Scene(root, 400,400);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		
		CanvasAddress cd = new CanvasAddress(new Canvas(400,400));
		//Canvas c = cd.c;
		root.getChildren().add(cd.c);
		
		String[] args = new String[1];
		args[0] = "HEELOWORLD";
		GreetingServer.main(args,cd);
	}
	
	
	 public static void main(String[] args) {
	        Application.launch(args);
	    }

}
