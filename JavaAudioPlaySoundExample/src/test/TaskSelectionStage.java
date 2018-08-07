package test;
import javafx.application.Application;
import javafx.geometry.Insets ;
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
	      /* 
	      Button task3 = new Button("FreezeTest study task, without the minimap!");
	      task3.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	               //	me.close(); 
	        	   //don't draw the viewPort
	        	   controller.drawViewPort =false;
	        	   controller.startTask1();
	               me.close();	               
	           }
	      });*/
	      /* 
	      Button task4 = new Button("Read and observe study task, without the minimap!");
	      task4.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =false;
	               	 controller.startTask2();
	               	 me.close();	                     	  	         	  
	           }
	      });*/	
	      
	      Button task5 = new Button("FreezeTest study task, without sounds!");
	      task5.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {         	  	              
	        	   controller.iModel.SoundsFalse();
	        	   controller.startTask1();
	               me.close();	               
	           }
	      });
	       
	      Button task6 = new Button("Read and observe study task, without sounds!");
	      task6.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.iModel.SoundsFalse();
	        	   controller.startTask2();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task7 = new Button("Preparation for Freeze test, location identification");
	      task7.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) { 
	        	   controller.drawViewPort =true;
	        	     controller.startTask0();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task8 = new Button("Preparation for Freeze test, tool identification");
	      task8.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {   
	        	   controller.drawViewPort =true;
	        	     controller.startTask4();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      /* 
	      Button task9 = new Button("Button holding task, for activity identification");
	      task9.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;
	        	     controller.startTask5();
	               	 me.close();	                     	  	         	  
	           }
	      });*/
	      /* 
	      Button task10 = new Button("Preparation for Freeze test, location identification, without the minimap!");
	      task10.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =false;
	        	     controller.startTask0();
	               	 me.close();	                     	  	         	  
	           }
	      });*/
	      /* 
	      Button task11 = new Button("Preparation for Freeze test, tool identification, without the minimap!");
	      task11.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) { 
	        	   controller.drawViewPort =false;
	        	     controller.startTask4();
	               	 me.close();	                     	  	         	  
	           }
	      });*/
	      /*
	      Button task12 = new Button("Button holding task, for activity identification, without the minimap!");
	      task12.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {
	        	   controller.drawViewPort =false;
	        	     controller.startTask5();
	               	 me.close();	                     	  	         	  
	           }
	      }); */
	      
	      Button task13 = new Button("Preparation for Freeze test, shape detection");
	      task13.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;
	        	     controller.startTask6();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      /*
	      Button task14 = new Button("Preparation for Freeze test, shape detection, without the minimap!");
	      task14.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =false;
	        	     controller.startTask6();
	               	 me.close();	                     	  	         	  
	           }
	      });*/
	      
	      Button task15 = new Button("Tool identification reaction task");
	      task15.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;
	        	     controller.startTask7();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      
	      
	      //sound less preparation tests	      
	      Button task16 = new Button("Preparation for Freeze test, shape detection, without sounds!");
	      task16.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;	 
	        	   controller.iModel.SoundsFalse();
	        	     controller.startTask6();
	               	 me.close();
	               	
	           }
	      });
	      
	      Button task17 = new Button("Preparation for Freeze test, location identification, without sounds!");
	      task17.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) { 
	        	   controller.drawViewPort =true;	
	        	   controller.iModel.SoundsFalse();
	        	     controller.startTask0();
	               	 me.close();	
	               	
	           }
	      });
	      
	      Button task18 = new Button("Preparation for Freeze test, tool identification, without sounds!");
	      task18.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {   
	        	   controller.drawViewPort =true;	
	        	   controller.iModel.SoundsFalse();
	        	     controller.startTask4();
	               	 me.close();	               	 
	                }
	      });
	      
	      Button task19 = new Button("Tool identification reaction task, without sounds!");
	      task19.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    
	        	   controller.drawViewPort =true;
	        	   controller.iModel.SoundsFalse();
	        	     controller.startTask7();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      
	      Button task20 = new Button("Sound calibration demo!");
	      task20.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {    	        	  
	        	     controller.startTask8();
	               	 me.close();	                     	  	         	  
	           }
	      });
	      task20.setPrefHeight(40);
	      task20.setPrefWidth(200);
	      task20.setAlignment(Pos.TOP_CENTER);
	      
	      
	      
	      VBox sound = new VBox();
	      sound.setAlignment(Pos.CENTER);
	      VBox noSound = new VBox();
	      noSound.setAlignment(Pos.CENTER);
	      sound.getChildren().addAll(task1,task2,task7,task8,task13,task15);
	      noSound.getChildren().addAll(task5,task6,task17,task18,task16, task19);
	      //task7,task8, task13, task5,task6,task1,task2,task15
	      HBox hbox = new HBox();
	      hbox.getChildren().addAll(sound,noSound);
	      hbox.setAlignment(Pos.CENTER);
	      root.getChildren().addAll(task20,hbox);
	      root.setAlignment(Pos.CENTER);	      
	      root.requestFocus();
	      setScene(scene);
	      setX(0);
	      setY(0);
	      show();  
	}

}
