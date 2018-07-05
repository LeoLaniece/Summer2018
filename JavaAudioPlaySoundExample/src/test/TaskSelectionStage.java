package test;
import javafx.application.Application;
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

public class TaskSelectionStage extends Stage{
	private Draw2Controller controller;
	private Stage me;
	
	public TaskSelectionStage(Draw2Controller contro){
		controller = contro;
		me = this;
		setTitle("Task selection Stage");
	      //set the size of the window here
	      //make sure the height is 200 + what you want to accommodate the color picker and sample line
        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	      VBox root = new VBox();	        
	      Scene scene = new Scene(root, SceneWidth,SceneHeight);
	     
	      Button task1 = new Button("Maze race study task");
	      task1.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	               	me.close();        	  	         	  
	           }
	      });
	      
	      Button task2 = new Button("FreezeTest study task");
	      task2.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	               controller.startTask2();
	               me.close();	               
	           }
	      });
	      
	      Button task3 = new Button("Read and observe study task");
	      task3.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	               	 controller.startTask3();
	               	 me.close();
	           }
	      });
	      
	      root.getChildren().addAll(task2,task3);
	      root.setAlignment(Pos.CENTER);	      
	      root.requestFocus();
	      setScene(scene);
	      setX(0);
	      setY(0);
	      show();  
	}

}
