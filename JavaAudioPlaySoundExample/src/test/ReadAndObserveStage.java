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

public class ReadAndObserveStage extends Stage{

	public long startTime =0;
	public long totalTime =0;
	public Button userDrawing;
	
	public ReadAndObserveStage() {
      setTitle("read and observe stage!");
      //set the size of the window here
      //make sure the height is 200 + what you want to accommodate the color picker and sample line
      int SceneWidth = 800;
      int SceneHeight = 600;
      VBox root = new VBox();	        
      Scene scene = new Scene(root, SceneWidth,SceneHeight);
      
      //progress
      //still need the window to close on its own automatically after a certain time
      //need to check the timer is recording the times accurately
      
      Text t = new Text("Please read the article provided on your desk."+"\n"+
      " While reading, whenever you notice that the other user is drawing, please press and hold the big button."+"\n"+
    		  " When the other user ceases to draw, release the big button."+"\n"+
      "After one minute we will ask you to answer a question pertaining to the information in the article"+"\n"+
    		  "Press the ready button when you are prepared to start.");

      root.getChildren().add(t);
      root.setAlignment(Pos.TOP_CENTER);        
      t.setFont(Font.font ("Verdana", 14));
      t.setFill(Color.BLACK);           
      
      userDrawing = new Button("USER IS DRAWING");
      userDrawing.setPrefHeight(400);
      userDrawing.setPrefWidth(400);
      userDrawing.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent event) {        	   
        		   startTime = System.currentTimeMillis();       		         		         	  
           }
      });
      userDrawing.setOnMouseReleased(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	totalTime += System.currentTimeMillis()-startTime;        	
        }
      });            
      
     Button ready = new Button("ready");
     ready.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {           	              				
        	  root.getChildren().add(userDrawing);
        	  long timer = System.currentTimeMillis();
        	  root.getChildren().remove(1);
        	  //when 5 seconds have gone by, remove button, prompt for answering questions
        	  
        	  
          }
     });
     
     
     root.getChildren().add(ready);                                          
      
      root.requestFocus();
      setScene(scene);
      setX(0);
      setY(0);
      show();  
      
      root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {		
				System.out.println(key.getText());
				if (key.getText().equals("f")) {					
						close();
					}
				}							    		
   	});                               
	}
	


}
