package test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.event.*;
import javafx.scene.input.KeyEvent; 
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

public class ReadAndObserveStage extends Stage{

	public long startTime =0;
	public long totalTime =0;
	public Button userDrawing;
	public Button userPanning;
	public ArrayList<Long> User1ActiveTimes;
	public ArrayList<Long> User2ActiveTimes;
	private Draw2Controller controller;
	
	private ReadAndObserveStage me;
	private VBox root = new VBox();	
	public Button exit;
	private Draw2Model model;
	private InteractionModel iModel;
	private Draw2miniMap radarView;
    private double dx = 0;
    private double dy = 0;
	
	public ReadAndObserveStage(Draw2Controller controller, Draw2Model model,
	InteractionModel iModel, Draw2miniMap radarView) {
      setTitle("read and observe stage!");
      //set the size of the window here
      //make sure the height is 200 + what you want to accommodate the color picker and sample line
      int SceneWidth = javaServer.LaunchServer.SceneWidth; 
      int SceneHeight = javaServer.LaunchServer.SceneHeight;
      //VBox root = new VBox();	        
      Scene scene = new Scene(root, SceneWidth,SceneHeight);
      User1ActiveTimes = new ArrayList<>();
      User2ActiveTimes = new ArrayList<>();
      me = this;
      this.controller = controller;
      
      //progress
      //still need the window to close on its own automatically after a certain time
      //need to store user1's activity!
      
      Text t = new Text("Please read the article provided in the workspace."+"\n"+
      " While reading, whenever you notice that the other user is drawing, please press and hold the big button."+"\n"+
    		  " When the other user ceases to draw, release the big button."+"\n"+
      "After one minute we will ask you to answer a question pertaining to the information in the article"+"\n"+    		  
    		  "Press the ready button when you are prepared to start.");
      
      if (iModel.task == iModel.ACTIVITY_IDENTIFICATION_TASK) {
    	  t.setText("Until the task is complete please:"+"\n"+
    			  "Observe the activity of the other user"+"\n"+
    			  "If the other user is not doing anything, you do not do anything"+"\n"+"\n"+
    			  "If the other user is drawing, press and hold the DRAWING button until the other user stops drawing"+"\n"+"\n"+
    			  "If the other user is panning the minimap, press and hold the"+"\n"+
    			  "PANNING button until the other user stops panning the minimap"+"\n"+"\n"+    			     			  
    			  "Press the ready button when you are ready to begin");
      }

      root.getChildren().add(t);
      root.setAlignment(Pos.TOP_CENTER);        
      t.setFont(Font.font ("Verdana", 14));
      t.setFill(Color.BLACK);           
      
      Text timer = new Text("60");
      timer.setFont(Font.font ("Verdana", 20));
      timer.setFill(Color.GREEN); 

      //remove this big button, add 2 buttons, 
      //get them to show when the user presses ready
      //log the 2 different time stamps 
      
      userDrawing = new Button("DRAWING");
      userDrawing.setPrefHeight(200);
      userDrawing.setPrefWidth(200);
      userDrawing.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
          @Override
          public void handle(MouseEvent me) {
          	//totalTime += System.currentTimeMillis()-startTime;        	
          	User2ActiveTimes.add(System.currentTimeMillis()-startTime);
          }
        });            
      userDrawing.setOnMouseReleased(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	//totalTime += System.currentTimeMillis()-startTime;        	
        	User2ActiveTimes.add(System.currentTimeMillis()-startTime);
        }
      });       
      
      //second button for activity identification task
      userPanning = new Button("PANNING");
      userPanning.setPrefHeight(200);
      userPanning.setPrefWidth(200);
      userPanning.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
          @Override
          public void handle(MouseEvent me) {
          	//totalTime += System.currentTimeMillis()-startTime;        	
          	User2ActiveTimes.add(System.currentTimeMillis()-startTime);
          }
        });            
      userPanning.setOnMouseReleased(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	//totalTime += System.currentTimeMillis()-startTime;        	
        	User2ActiveTimes.add(System.currentTimeMillis()-startTime);
        }
      }); 

      
     Button ready = new Button("ready");
     ready.setOnAction(new EventHandler<ActionEvent>() {
          public void handle(ActionEvent event) {
        	  startTime = System.currentTimeMillis();
        	  root.getChildren().add(userDrawing);       	         	  
        	  root.getChildren().remove(1);
        	  root.getChildren().add(timer);
              UpdateTimer z = new UpdateTimer(startTime, timer,me);
              z.start();
              setX(800);
              controller.state = controller.READY_TO_BEGIN_TASK;
              model.notifySubscribers();
              exit = new Button("exit");
              exit.setOnAction(new EventHandler<ActionEvent>() {
                   public void handle(ActionEvent event) {
                 	  if (!z.isAlive()) {
                 		  close();
                 	  }                 	  
                   }
              });
        	  if ((iModel.task == iModel.ACTIVITY_IDENTIFICATION_TASK)) {
        		  root.getChildren().clear();
        		  root.getChildren().add(t);
        		  root.getChildren().add(userDrawing); 
        		  root.getChildren().add(userPanning);
        		  root.getChildren().add(timer);        		  
        	  }
           //  root.getChildren().add(exit);
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
				if (key.getText().equals("r")) {					
						close();
					}
				}							    		
   	});
      
      //settings to pan the minimap when nessesary
      
      root.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
      if (key.getText().equals("s")) {
			System.out.println("key s pressed!");
  		dy = 10;           		            		           		            		        		            		
  		//move the viewPort within its bounds
  		//but only if it is a drag allowed in the view port 
  		if (iModel.viewPortY +dy >=0 &&            				
  				(iModel.viewPortY +iModel.viewPortHeight +dy) <=radarView.height/7) {            		
  		iModel.viewPortY += dy/7;
  		//drag the paths around to make it seem like we are panning the background
  		for (int a = 0; a < model.getModelPaths().size(); a++) {            			
  			iModel.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
  			//model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);
  			model.getModelPaths().get(a).setTranslateY(iModel.modelPathsTranslateByCoordinates.get(a).y);            			
  		}
  		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
  		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
  		  model.notifySubscribers();    		
  		}  
		}
		if (key.getText().equals("w")) {
			System.out.println("key w pressed!");
  		dy = -10;           		            		           		            		        		            		
  		//move the viewPort within its bounds
  		//but only if it is a drag allowed in the view port 
  		if (iModel.viewPortY +dy >=0 &&            				
  				(iModel.viewPortY +iModel.viewPortHeight +dy) <=radarView.height/7) {            		
  		iModel.viewPortY += dy/7;
  		//drag the paths around to make it seem like we are panning the background
  		for (int a = 0; a < model.getModelPaths().size(); a++) {            			
  			iModel.modelPathsTranslateByCoordinates.get(a).y-=dy;            			
  			//model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);
  			model.getModelPaths().get(a).setTranslateY(iModel.modelPathsTranslateByCoordinates.get(a).y);            			
  		}
  		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
  		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
  		  model.notifySubscribers();    		
  		} 
		}
		if (key.getText().equals("d")) {
			System.out.println("key d pressed!");
			dx = 10;            		            		           		            		        		            		
  		//move the viewPort within its bounds
  		//but only if it is a drag allowed in the view port 
  		if (iModel.viewPortX +dx >=0 &&            				
  				(iModel.viewPortX +iModel.viewPortWidth + dx) <=radarView.width/7) 
  				 {
  		iModel.viewPortX += dx/7;            		
  		//drag the paths around to make it seem like we are panning the background
  		for (int a = 0; a < model.getModelPaths().size(); a++) {
  			iModel.modelPathsTranslateByCoordinates.get(a).x -=dx;            			           			
  			model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);            			           			
  		}
  		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
  		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
  		  model.notifySubscribers();    		
  		}            		
		}
		if (key.getText().equals("a")) {
			System.out.println("key a pressed!");
			dx = -10;            		            		           		            		        		            		
  		//move the viewPort within its bounds
  		//but only if it is a drag allowed in the view port 
  		if (iModel.viewPortX +dx >=0 &&            				
  				(iModel.viewPortX +iModel.viewPortWidth + dx) <=radarView.width/7) 
  				 {
  		iModel.viewPortX += dx/7;            		
  		//drag the paths around to make it seem like we are panning the background
  		for (int a = 0; a < model.getModelPaths().size(); a++) {
  			iModel.modelPathsTranslateByCoordinates.get(a).x -=dx;            			           			
  			model.getModelPaths().get(a).setTranslateX(iModel.modelPathsTranslateByCoordinates.get(a).x);            			           			
  		}
  		 // model.updateVPDSGeneratorVelocity(soundVelocityThread.getVelocity());
  		  //model.updateVPDSGeneratorLocation(iModel.calculateViewPortCenter());
  		  model.notifySubscribers();    		
  		}            		
		}
			}
      });
      
	}
	
	public void calculateAwareness() {
		
		System.out.println("User 2 activity");
		for (int i = 0; i < User2ActiveTimes.size();i++) {
			if (i%2 == 0) {
				System.out.println("activity start"+User2ActiveTimes.get(i));
			}else {
				System.out.println("activity end"+User2ActiveTimes.get(i));
			}			
		}
		
		System.out.println("User 1 activity");
		for (int i = 0; i < User1ActiveTimes.size();i++) {
			if (i%2 == 0) {
				System.out.println("activity start"+User1ActiveTimes.get(i));
			}else {
				System.out.println("activity end"+User1ActiveTimes.get(i));
			}			
		} 
		String fileName = "ReadAndObserve";
		if (controller.iModel.task == controller.iModel.ACTIVITY_IDENTIFICATION_TASK) {
			fileName += " activity identification";
		}
		
		System.out.println("user2 awareness was only off by "+calculateUser2Awareness(User1ActiveTimes, User2ActiveTimes)+" milliseconds");		
		CreateFile x = new CreateFile("user2 awareness was only off by "
		+calculateUser2Awareness(User1ActiveTimes, User2ActiveTimes)+" milliseconds", fileName);
	}
	
	public double calculateUser2Awareness(ArrayList<Long> User1, ArrayList<Long> User2) {
		ArrayList<Long> u1distance = new ArrayList<>();
		long temp = 0;
		for (int i=0;i<User1.size();i++) {
			if(i%2 ==0) {
				temp = User1.get(i);
			}
			else {
				u1distance.add(User1.get(i) - temp);
			}
		}
		
		ArrayList<Long> u2distance = new ArrayList<>();
		long temp2 = 0;
		for (int i=0;i<User2.size();i++) {
			if(i%2 ==0) {
				temp2 = User2.get(i);
			}
			else {
				u2distance.add(User2.get(i) - temp2);
			}
		}
		ArrayList<Double> difference = new ArrayList<>();
		double totalActiveTimeU1 =0;
		for (int i = 0; i< u1distance.size();i++) {
			totalActiveTimeU1 += u1distance.get(i);
		}
		double totalActiveTimeU2 =0;
		for (int i = 0; i< u2distance.size();i++) {
			totalActiveTimeU2 += u2distance.get(i);
		}
		
		
		
		//if user2 clicked as many times as user 1
		/*if (u1distance.size() == u2distance.size()) {			
			for (int i = 0; i< u1distance.size();i++) {
				if (u1distance.get(i) > u2distance.get(i)) {
					difference.add((double) (u1distance.get(i) - u2distance.get(i)));					
				}else {
					difference.add((double) (u2distance.get(i) - u1distance.get(i)));	
				}
			}
		}
		
		double totalDifference = 0;
		for (int i = 0; i< difference.size();i++) {
			totalDifference += difference.get(i);
		}*/		
			return totalActiveTimeU1-totalActiveTimeU2;
		
		
		
	}
	
	public void closeStage() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {	
		    	controller.state = controller.CLOSE_INSTRUCTIONS;
		    	//controller.model.showTaskStage();
		    	controller.model.notifySubscribers();
		    	TaskCompleteStage x = new TaskCompleteStage(controller);
		close();
		    }
		});
	}


}
