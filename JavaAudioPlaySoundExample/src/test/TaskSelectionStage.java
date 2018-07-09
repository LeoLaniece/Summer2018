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
	      
	      Button task1 = new Button("FreezeTest study task");
	      task1.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	        	   controller.drawViewPort =true;
	               controller.startTask1();
	               me.close();	               
	           }
	      });
	      
	      Button task2 = new Button("Read and observe study task");
	      task2.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	        	   controller.drawViewPort =true;
	               	 controller.startTask2();
	               	 me.close();
	           }
	      });
	     
	      Button task3 = new Button("FreezeTest study task, without the minimap!");
	      task3.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	               //	me.close(); 
	        	   //don't draw the viewPort
	        	   controller.drawViewPort =false;
	        	   controller.startTask1();
	               me.close();	               
	           }
	      });
	      
	      Button task4 = new Button("Read and observe study task, without the minimap!");
	      task4.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =false;
	               	 controller.startTask2();
	               	 me.close();	                     	  	         	  
	           }
	      });	
	      
	      Button task5 = new Button("FreezeTest study task, without sounds!");
	      task5.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	        	   new TaskWithoutSoundStage(controller,controller.FREEZE_TEST_TASK);
	               me.close();	               
	           }
	      });
	      
	      Button task6 = new Button("Read and observe study task, without sounds!");
	      task6.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   new TaskWithoutSoundStage(controller,controller.READ_AND_OBSERVE);
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task7 = new Button("Freeze test, for location identification");
	      task7.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) { 
	        	   controller.drawViewPort =true;
	        	     controller.startTask0();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task8 = new Button("Freeze test, for tool identification");
	      task8.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {   
	        	   controller.drawViewPort =true;
	        	     controller.startTask4();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task9 = new Button("Button holding task, for activity identification");
	      task9.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;
	        	     controller.startTask5();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task10 = new Button("Freeze test, for location identification, without the minimap!");
	      task10.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =false;
	        	     controller.startTask0();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task11 = new Button("Freeze test, for tool identification, without the minimap!");
	      task11.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) { 
	        	   controller.drawViewPort =false;
	        	     controller.startTask4();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task12 = new Button("Button holding task, for activity identification, without the minimap!");
	      task12.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {
	        	   controller.drawViewPort =false;
	        	     controller.startTask5();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task13 = new Button("Shape detection task, preparation for freeze test");
	      task13.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;
	        	     controller.startTask6();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      root.getChildren().addAll(task7,task10,task13, task11, task9,task12,task5,task6,task3,task4,task1,task2);
	      root.setAlignment(Pos.CENTER);	      
	      root.requestFocus();
	      setScene(scene);
	      setX(0);
	      setY(0);
	      show();  
	}

}
