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
	private ToggleGroup shapeGroup;

	public ReadAndObserveInstructionStage() {
	      setTitle("read and observe stage!");
	      //set the size of the window here
	      //make sure the height is 200 + what you want to accommodate the color picker and sample line
	      root = new VBox();	        
	      Scene scene = new Scene(root);
	      
	      Text instructions = new Text("A task is about to start"+"\n"+
	      "Until this pop-up window closes, please trace the all the shapes you can find in the workspace"+"\n"+
	    		  "please begin as soon as possible");
	      instructions.setFont(Font.font ("Verdana", 40));
	      instructions.setFill(Color.BLACK); 
	      root.getChildren().add(instructions);
	      root.setAlignment(Pos.CENTER);
	      
	      root.requestFocus();
	      setScene(scene);
	      setX(800);
	      setY(600);
	      show();  	      
	}
	
	public void showShapeQuestions(Draw2Model model) {		
		instructions.setText("Please select which was the last shape that you finished completely tracing");
        //shape toggle group
		  
  		  shapeGroup = new ToggleGroup();
          RadioButton sgrb1 = new RadioButton("Tracing the shape of a triangle");
          sgrb1.setToggleGroup(shapeGroup);        

          RadioButton sgrb2 = new RadioButton("Tracing the shape of a square");
          sgrb2.setToggleGroup(shapeGroup);
           
          RadioButton sgrb3 = new RadioButton("Tracing the shape of a squiggle");
          sgrb3.setToggleGroup(shapeGroup);
          
          RadioButton sgrb4 = new RadioButton("Filling in a circle");
          sgrb4.setToggleGroup(shapeGroup);
          
          root.getChildren().addAll(sgrb1,sgrb2,sgrb3,sgrb4);        
          
          Button submit = new Button("SUBMIT");
          submit.setOnAction(new EventHandler<ActionEvent>() {
               public void handle(ActionEvent event) { 
            	 //needs to be the results of the radio button questions
               	RadioButton groupResult = (RadioButton) shapeGroup.getSelectedToggle();
   				String groupResultString = groupResult.getText();
   				System.out.println("user 1 result "+groupResultString);
   				model.user1FreezeQuestionResult += groupResultString+"\n";   				
   				//need to send this to user2 
   				//will not nessesairly be at the right moment for saving into a file
   				//could save it onto this computer?
   				//CreateFile x = new CreateFile("User1 selected "+groupResultString);
               }
          });          
		}
	
	}
	
	
	
	

