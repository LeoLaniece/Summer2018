package javaServer;

import javafx.scene.Group;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import test.Draw2Controller;
import test.Draw2Model;
import test.Draw2View;
import test.Draw2miniMap;
import test.InteractionModel;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * Will launch the client version of the drawing application
 * will only connect if the Server is launched first
 * @author HCI Lab
 *
 */
public class LaunchClient extends Application{
	@Override
	public void start(Stage primaryStage)throws Exception{
		   String[] arr = new String[2];
		   //name of the Server computer
		   arr[0] = "DESKTOP-3QFK6AS";
		   //id of the port to connect to on the Server computer
		   arr[1] = "9080";
		   //launch the drawing client
		   DrawingClient GC = new DrawingClient(arr);
		   GC.start();		
	}	
	
	 public static void main(String[] args) {
	        Application.launch(args);
	    }

}
