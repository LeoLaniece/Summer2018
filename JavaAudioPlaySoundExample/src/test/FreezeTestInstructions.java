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


public class FreezeTestInstructions extends Stage{
	
	private Draw2Controller controller;
	FreezeTestInstructions me = this;

	public FreezeTestInstructions(Draw2Controller c) {
		controller = c;
	      setTitle("Freeze test instructions");
	      //set the size of the window here
	      //make sure the height is 200 + what you want to accommodate the color picker and sample line
	      HBox root5 = new HBox();
	      VBox root = new VBox();	        	     
	        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
	        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	      Scene scene = new Scene(root,SceneWidth,SceneHeight);
	      
	      //set text for regular freeze test
	      Text instructions = new Text("FreezeTest is about to start"+"\n"+
	      "Until promted to stop, please trace the all the shapes"+"\n"+"you can find in the workspace"+"\n"+
	      "Only trace each shape once!"+"\n"+
	      "Work with the other user to achieve this goal"+"\n"+"\n"+
		  "Trace the triangles with the pencil"+"\n"+
		  "Trace the squares with the nail"+"\n"+
		  "Trace the squiggles with the chalk"+"\n"+
		  "Erase the circles with the eraser"+"\n"+		  
		  "Do not worry if you end up " +"\n"+
		  "tracing the wrong shape with the wrong tool"+"\n"+
		  "\n"+		  	    		  
	    		  "When prompted to stop, please answer all questions in the pop up window"+"\n"+
	    		  "please press the ready button when you are ready to begin");
	      //change text for location task
	      if (controller.iModel.task == controller.iModel.LOCATION_IDENTIFICATION_TASK) {
	    	  instructions = new Text("Location identification task is about to begin"+"\n"+
	    		      "Please observe the location of the other user's drawing and navigation activity"+"\n"+
	    		    "When prompted, please identify in which area of the minimap"+"\n"+
	    		    "the other user was previously active"+"\n"+"\n"+
	    		    "The other user will be erasing all the circles in the workspace");
	      }
	      //change text for tool task
	      if (controller.iModel.task == controller.iModel.TOOL_IDENTIFICATION_TASK) {
	    	  instructions = new Text("Tool identification task is about to begin"+"\n"+
	    		      "Please observe with which tool the other user is drawing"+"\n"+
	    		    "When prompted, please identify with which tool"+"\n"+
	    		    "the other user was previously using"+"\n"+"\n"+
	    		    "The other user will be tracing all the shapes in the workspace"+"\n"+
	    		    "They will be tracing the triangles with the pencil"+"\n"+
	    		    "The squares with the nail"+"\n"+
	    		    "The squiggles with the chalk"+"\n"+
	    		    "And erasing the circles with the eraser"+"\n"+"\n"+	  		    
	    		    "To familiarize yourself with the tool sounds, please press the Go to training button"+"\n"+	   		    	    		    
		    		  "When you are ready, please press the ready button ");	    	  
	      }
	      //change text for shape identification task
	      if (controller.iModel.task == controller.iModel.SHAPE_DETECTION_TASK) {
	    	  instructions = new Text("Shape identification task is about to begin"+"\n"+
	    		      "Please observe which shapes the other user is drawing"+"\n"+
	    		    "When prompted, please identify which shape"+"\n"+
	    		    "the other user was previously tracing"+"\n"+"\n"+
	    		    "The other user will be tracing all the shapes in the workspace"+"\n"+
	    		    "They will be tracing the triangles with the pencil"+"\n"+
	    		    "The squares with the nail"+"\n"+
	    		    "The squiggles with the chalk"+"\n"+
	    		    "And erasing the circles with the eraser"+"\n"+	    		    
		    		  "please press the ready button when you are ready to begin");
	      }
	      
	      instructions.setFont(Font.font ("Verdana", 20));
	      instructions.setFill(Color.BLACK); 
	      root.getChildren().add(instructions);
	      root.getChildren().add(root5);
	      
	      Text timer = new Text("0");
	      timer.setFont(Font.font ("Verdana", 20));
	      timer.setFill(Color.GREEN); 	     	      
	      Button ready = new Button("ready");
	      ready.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {	        	   
	        	  // long startTime = System.currentTimeMillis();	 
	        	   //problem here, the timer launches pause stages when i do not want to see them
	        	   FreezeTestTimer x = new FreezeTestTimer(controller, me);
	        	   x.start();
	        	   
	        	   //send message over network to hide user1's instruction stage
	        	   controller.state = controller.READY_TO_BEGIN_TASK;	        	   
	        	   controller.model.notifySubscribers();
	        	   close();
	        	   controller.view.drawBorder();	        	   
	           }
	      });
	      if (controller.iModel.task == controller.iModel.TOOL_IDENTIFICATION_TASK
	    		  || controller.iModel.task == controller.iModel.REAL_FREEZE_TEST) {
	    	  Button goToTraining = new Button("Go to training");
		      goToTraining.setOnAction(new EventHandler<ActionEvent>() {
		           public void handle(ActionEvent event) {
		        	   //change super state 
		        	   controller.superState = controller.SOUNDS_LOCAL;
		        	   System.out.println("super state = sounds local");
		        	   //add a window with a done training button
		        	   new DoneTrainingStage(controller, me);
		        	   //close this window
		        	   hide();
		           }
		      });
		      root5.getChildren().add(goToTraining);
		           }
	      
	      root5.getChildren().add(ready);	     
	      
	      root5.requestFocus();
	      setScene(scene);
	      setX(0);
	      setY(0);
	      show();  
	      
	}
	
	public void closeStage() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		close();		
		controller.state = controller.CLOSE_INSTRUCTIONS;
		controller.model.notifySubscribers();
		//controller.model.showTaskStage();
		new TaskCompleteStage(controller);
		    }
		});
	}
	

	
}
