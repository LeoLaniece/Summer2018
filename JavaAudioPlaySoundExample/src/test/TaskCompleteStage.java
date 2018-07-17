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


public class TaskCompleteStage extends Stage{
	
	private Draw2Controller controller;
	
	public TaskCompleteStage(Draw2Controller c) {
		controller = c;
	      setTitle("Task Complete");
	      //set the size of the window here
	      //make sure the height is 200 + what you want to accommodate the color picker and sample line
	      HBox root5 = new HBox();
	      VBox root = new VBox();	        	     
	        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
	        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	      Scene scene = new Scene(root,SceneWidth,SceneHeight);
	      
	      Text instructions = new Text("Congratulations!!"+"\n"+
	      "The task was successfully"+"\n"+  
	      	    		  "completed!"+"\n"+"\n"+
	      "Please put the speakers back on your "+"\n"+"head if you had previously taken them off"+"\n"+"\n"+
	    		  "Please press the Okay button to continue");
	      instructions.setFont(Font.font ("Verdana", 40));
	      instructions.setFill(Color.BLACK); 
	      root.getChildren().add(instructions);
	      root.getChildren().add(root5);
	      
	      Text timer = new Text("0");
	      timer.setFont(Font.font ("Verdana", 20));
	      timer.setFill(Color.GREEN); 	     	      
	      Button okay = new Button("Okay");
	      okay.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {	        	      	   	        	   
	        	   //send message over network to hide user1's instruction stage
	        	   controller.state = controller.NOTREADY;
	        	   controller.taskRunning = false;
	        	   controller.drawViewPort =true;
	        	   controller.iModel.task = -1;
	        	   controller.iModel.SoundsTrue(); 
	        	   controller.view.setImageForFreezeTest();
	        	   controller.model.resetModel();
	        	   controller.view.resetView();
	        	   controller.radarView.resetView();
	        	   controller.model.showTaskStage();
	        	   controller.model.notifySubscribers();
	        	   close();
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
