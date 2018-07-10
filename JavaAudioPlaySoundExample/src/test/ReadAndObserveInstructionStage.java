package test;

import javafx.application.Application;
import javafx.scene.control.ToggleButton ;
import javafx.scene.control.Toggle;
import javafx.scene.control.RadioButton ;
import javafx.scene.control.ToggleGroup;

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


public class ReadAndObserveInstructionStage extends Stage{
	
	private VBox root;
	private Text instructions;
	private ToggleGroup shapeGroup =null;
	VBox radioButtons = null;
	Text timer;
	ReadAndObserveInstructionStage me = this;
	private Draw2Model model;

	public ReadAndObserveInstructionStage(InteractionModel iModel) {
	      setTitle("read and observe stage!");
	      //set the size of the window here
	      //make sure the height is 200 + what you want to accommodate the color picker and sample line
	      root = new VBox();	        
	        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
	        int SceneHeight = javaServer.LaunchServer.SceneHeight;
	      Scene scene = new Scene(root,SceneWidth,SceneHeight);
	      
	      instructions = new Text("A task is about to start"+"\n"+
	      "Until notified, please"+"\n"+"trace the all the shapes you can find in the workspace"+"\n"+
	    		  "Trace the triangles with the pencil"+"\n"+
	    		  "Trace the squares with the nail"+"\n"+
	    		  "Trace the squiggles with the chalk"+"\n"+
	    		  "Erase the circles with the eraser"+"\n"+
	    		  "Do not worry if you end up " +"\n"+
	    		  "tracing the wrong shape with the wrong tool"+"\n"+
	    		  "Just do your best!"+"\n"+
	    		  "please begin drawing when the workspace appears");
	      System.out.println("iModel task "+iModel.task);
	      if (iModel.task == iModel.LOCATION_IDENTIFICATION_TASK) {
	    	  instructions = new Text("A task is about to start"+"\n"+
	      "Until notified, please"+"\n"+"erase all the circles you can find in the workspace"+"\n"+	      		
	    		  "You can select the Eraser tool in the bottom left corner of the workspace"+"\n"+
	    		  "You can move around in the workspace by holding down the" +"\n"+
	    		  "SHIFT key and dragging with the mouse cursor"+"\n"+
	    		  "Good luck!"+"\n"+
	    		  "please begin drawing when the workspace appears");
	      }
	      if (iModel.task == iModel.REAL_FREEZE_TEST) {
	    	  instructions = new Text("A task is about to start"+"\n"+
	    		      "Until notified, please"+"\n"+
	    			  "trace the all the shapes you can find in the workspace"+"\n"+
	    			  "Only trace each shape once!"+"\n"+
	    		      "Work with the other user to achieve this goal!"+"\n"+"\n"+
		    		  "Trace the triangles with the pencil"+"\n"+
		    		  "Trace the squares with the nail"+"\n"+
		    		  "Trace the squiggles with the chalk"+"\n"+
		    		  "Erase the circles with the eraser"+"\n"+
		    		  "Do not worry if you end up " +"\n"+
		    		  "tracing the wrong shape with the wrong tool"+"\n"+
		    		  "Just do your best!"+"\n"+"\n"+
		    		  "please begin drawing when the workspace appears"); 
	      }
	      
	      instructions.setFont(Font.font ("Verdana", 20));
	      instructions.setFill(Color.BLACK); 
	      root.getChildren().add(instructions);	   
	      root.setAlignment(Pos.CENTER);
	      
	      root.requestFocus();
	      setScene(scene);
	      setX(800);
	      setY(0);
	      show();  	      
	}
	
	public void showShapeQuestions(Draw2Model model) {
		this.model = model;
		//everytime this is called, i want a file to be created!
		//if the user has not hit submit, create an empty file before resetting everything.
		
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		    	show();
		instructions.setText("Please select which was the "+ "\n"+"last shape that you finished completely tracing");
		instructions.setFont(Font.font ("Verdana", 20));
	      instructions.setFill(Color.GREEN); 
	      if (radioButtons !=null) {
	    	  CreateFile f = new CreateFile("","User 1 freeze test shape claim");
	    	  root.getChildren().remove(radioButtons);
	      }
		 radioButtons = new VBox();
        //shape toggle group
		  
  		  shapeGroup = new ToggleGroup();
          RadioButton sgrb1 = new RadioButton("Tracing the shape of a triangle");
          sgrb1.setToggleGroup(shapeGroup);        

          RadioButton sgrb2 = new RadioButton("Tracing the shape of a square");
          sgrb2.setToggleGroup(shapeGroup);
           
          RadioButton sgrb3 = new RadioButton("Tracing the shape of a squiggle");
          sgrb3.setToggleGroup(shapeGroup);
          
          RadioButton sgrb4 = new RadioButton("Erasing a circle");
          sgrb4.setToggleGroup(shapeGroup);
          
          RadioButton sgrb5 = new RadioButton("can't remember");
          sgrb5.setToggleGroup(shapeGroup);
          shapeGroup.selectToggle(sgrb5);
          
          radioButtons.getChildren().addAll(sgrb1,sgrb2,sgrb3,sgrb4);        
          root.getChildren().add(radioButtons);
          root.setAlignment(Pos.BASELINE_LEFT);
          Button submit = new Button("SUBMIT");
          submit.setOnAction(new EventHandler<ActionEvent>() {
               public void handle(ActionEvent event) { 
            	 //needs to be the results of the radio button questions
               	RadioButton groupResult = (RadioButton) shapeGroup.getSelectedToggle();
   				String groupResultString = groupResult.getText();
   				System.out.println("user 1 result "+groupResultString);
   				model.user1FreezeQuestionResult += groupResultString+"\n";   				
   				
   				//close and remove current bullets and submit button
   				root.getChildren().remove(radioButtons);
   				radioButtons = null;
   				//timer = null;
   		        instructions.setText("The other user is completing a short questionaire"+"\n"+
   			 	      "Please wait until this window automatically closes"+"\n"+
   			 	    		  "This window will close when the other user is ready to begin");
   		 	      instructions.setFont(Font.font ("Verdana", 20));
   		 	      instructions.setFill(Color.BLACK);   		 	      
   				CreateFile x = new CreateFile("User1 selected "+groupResultString,"User 1 freeze test shape claim");
   				//hide();   				
   				
               }
          }); 
          
          timer = new Text("30");
          long startTime = System.currentTimeMillis();
          //TimeWidget t = new TimeWidget(startTime,timer,me);
         // t.start();
          HBox submitTimer = new HBox();
          submitTimer.getChildren().addAll(submit);
          radioButtons.getChildren().add(submitTimer);
	      
          
		}
	});
	}
	
	public void restoreInstructions() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		if (radioButtons != null) {
			root.getChildren().remove(radioButtons);
			radioButtons = null;
	        instructions.setText("A task is about to start"+"\n"+
	 	      "Until this pop-up window closes, please"+"\n"+"trace the all the shapes you can find in the workspace"+"\n"+
	 	    		  "please begin as soon as possible");
	 	      instructions.setFont(Font.font ("Verdana", 20));
	 	      instructions.setFill(Color.BLACK);
		}
		    }
		});
	}
	
	public void submit() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {			
		if (radioButtons!= null) {
       	RadioButton groupResult = (RadioButton) shapeGroup.getSelectedToggle();
			String groupResultString = groupResult.getText();
			System.out.println("user 1 result "+groupResultString);
			model.user1FreezeQuestionResult += groupResultString+"\n";   				
			
			//close and remove current bullets and submit button
			root.getChildren().remove(radioButtons);
			radioButtons = null;
			CreateFile x = new CreateFile("User1 selected "+groupResultString,"User 1 freeze test shape claim");
		}
		hide();
		
		    }
		});
	}
	public void pauseForQuiz() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		    	show();
			root.getChildren().remove(radioButtons);
			radioButtons = null;
			//timer = null;
	        instructions.setText("The other user is completing a short questionaire"+"\n"+
	 	      "Please wait until this window automatically closes"+"\n"+
	 	    		  "This window will close when the other user is ready to begin");
	 	      instructions.setFont(Font.font ("Verdana", 20));
	 	      instructions.setFill(Color.BLACK);
		    }
		});
	}
	}
	
	
	
	

