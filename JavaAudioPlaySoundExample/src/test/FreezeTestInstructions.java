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
	      Scene scene = new Scene(root);
	      
	      Text instructions = new Text("FreezeTest is about to start"+"\n"+
	      "Until promted to stop, please trace the all the shapes you can find in the workspace"+"\n"+
	    		  "When prompted to stop, please answer all questions in the pop up window"+"\n"+
	    		  "please press the ready button when you are ready to begin");
	      instructions.setFont(Font.font ("Verdana", 30));
	      instructions.setFill(Color.BLACK); 
	      root.getChildren().add(instructions);
	      root.getChildren().add(root5);
	      
	      Text timer = new Text("0");
	      timer.setFont(Font.font ("Verdana", 20));
	      timer.setFill(Color.GREEN); 	     	      
	      Button ready = new Button("ready");
	      ready.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {
	        	   System.out.println("ready!!");
	        	   long startTime = System.currentTimeMillis();	        	   
	        	   FreezeTestTimer x = new FreezeTestTimer(startTime,timer,controller, me);
	        	   x.start();
	           }
	      });
	      root5.getChildren().add(ready);
	      root5.getChildren().add(timer);
	      
	      root5.requestFocus();
	      setScene(scene);
	      setX(0);
	      setY(600);
	      show();  
	      
	}
	
	public void closeStage() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		close();		
		controller.model.showTaskStage();
		    }
		});
	}
	
}
