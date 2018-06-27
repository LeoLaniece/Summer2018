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

public class LaunchClient extends Application{
	@Override
	public void start(Stage primaryStage)throws Exception{
		   String[] arr = new String[2];
		   arr[0] = "DESKTOP-3QFK6AS";
		   arr[1] = "9080";
		   DrawingClient GC = new DrawingClient(arr);
		   GC.start();		
	}
	
	
	 public static void main(String[] args) {
	        Application.launch(args);
	    }

}
