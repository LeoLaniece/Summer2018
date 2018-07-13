package unusedClasses;

import javafx.application.Application;


//import javafx.beans.value.ChangeListener;
import javafx.scene.control.ToggleButton ;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton ;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font ;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import test.Draw2Controller;
import test.FreezeTestInstructions;
import javafx.geometry.Pos ;
import java.lang.reflect.Field;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.KeyCode ;
import javafx.scene.shape.LineTo; 
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;
import javafx.scene.control.TextField ;

public class DoneTrainingStage extends Stage{
	
	private Draw2Controller controller;
	private FreezeTestInstructions prepStage;	
	
	public DoneTrainingStage(Draw2Controller c, FreezeTestInstructions lastStage) {
		controller = c;
		prepStage = lastStage;
		
		setTitle("Training Terminal");	      
	      VBox root = new VBox();	        	     
	      
	        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
	        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	      Scene scene = new Scene(root,SceneWidth,SceneHeight);
	      
	      Button done = new Button("Completed Training");
	      done.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {
	        	   //change super state 
	        	   controller.superState = controller.SOUNDS_OVER_NETWORK;
	        	   //open instructions window
	        	   prepStage.show();
	        	   //close this window
	        	   close();
	           }
	      });
	      done.setPrefHeight(SceneHeight);
	      done.setPrefWidth(SceneWidth);
	      root.getChildren().add(done);
	      setScene(scene);
	      setX(800);
	      setY(0);
	      show(); 
	}
	
	
}
