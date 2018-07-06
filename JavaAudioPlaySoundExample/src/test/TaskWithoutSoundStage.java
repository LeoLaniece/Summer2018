package test;

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


public class TaskWithoutSoundStage extends Stage{
	
	private Draw2Controller controller;
	private int task;
	private Stage me = this;
	
	public TaskWithoutSoundStage(Draw2Controller c, int task) {
		controller = c;
		this.task = task;
	      setTitle("Task without sound");
	      //set the size of the window here
	      //make sure the height is 200 + what you want to accommodate the color picker and sample line
	      HBox root5 = new HBox();
	      VBox root = new VBox();	        	     
	        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
	        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	      Scene scene = new Scene(root,SceneWidth,SceneHeight);
	      
	      Text instructions = new Text("Please remove your headphones."+"\n"+
	      "When you are ready, "+"\n"+  	      	    		  
	    		  "Please press the Okay button.");
	      instructions.setFont(Font.font ("Verdana", 40));
	      instructions.setFill(Color.BLACK); 
	      root.getChildren().add(instructions);
	      root.getChildren().add(root5);
	      	           	      
	      Button okay = new Button("Okay");
	      okay.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {	        	           	   	        	   	        	   
	        	   
	        	   if (task == controller.FREEZE_TEST_TASK) {
		        	   controller.drawViewPort =true;
		        	   controller.startTask1();
		               me.close();	
	        	   }
	        	   
	        	   if (task == controller.READ_AND_OBSERVE) {
		        	   controller.drawViewPort =true;
		        	   controller.startTask2();
		               me.close();
	        	   }	        	   
	        	   
	           }
	      });
	      root5.getChildren().add(okay);
	      //root5.getChildren().add(timer);
	      
	      root5.requestFocus();
	      setScene(scene);
	      setX(0);
	      setY(0);
	      show();  
	      
	}
}
