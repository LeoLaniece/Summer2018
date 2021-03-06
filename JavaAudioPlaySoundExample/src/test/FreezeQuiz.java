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

public class FreezeQuiz extends Stage{

    int PICKING = 1;
    int SELECTED = 2;
    
    /**
     * 1 = top left
     * 2 = bottom left
     * 3 = top right
     * 4 = bottom right
     */
    int selectedField = 0;
    int state = PICKING;
    String userInput;
    ToggleGroup toolGroup;
    ToggleGroup shapeGroup;
    FreezeTestTimer FTT;
    long quizStartTime;
    long FTTstartTimeCopy;
    int FTTincrementCopy;
	
	public FreezeQuiz(Draw2Controller con, Coordinate viewPortCenter, FreezeTestTimer FTT) {
        setTitle("Freeze Quiz");
        //set the size of the window here
        //make sure the height is 200 + what you want to accommodate the color picker and sample line
        int SceneWidth = javaServer.LaunchServer.SceneWidth; 
        int SceneHeight = javaServer.LaunchServer.SceneHeight;
        VBox root = new VBox();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        
        this.FTT = FTT;
        //timer business
        FTTstartTimeCopy = FTT.startTime;
        quizStartTime = System.currentTimeMillis();
        System.out.println("FTT startTime :"+FTT.startTime);
        FTT.startTime = Long.MAX_VALUE;
        FTTincrementCopy = FTT.timeIncrement;
        FTT.timeIncrement = Integer.MAX_VALUE;
        //System.out.println("FTT max value :"+FTT.startTime);
        
        Text t = new Text("In which area of your minimap was your partner recently active?");
        Text t2 = new Text("Please select a region:");
        root.getChildren().add(t);
        root.getChildren().add(t2);
        root.setAlignment(Pos.TOP_CENTER);        
        t.setFont(Font.font ("Verdana", 20));
        t.setFill(Color.BLACK);
        t2.setFont(Font.font ("Verdana", 20));
        t2.setFill(Color.BLACK);
        
        VBox minimapRepresentation = new VBox();	
        Canvas c = new Canvas(con.radarView.width/7,con.radarView.height/7);
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, c.getWidth(), c.getHeight());
        minimapRepresentation.getChildren().add(c);
        root.getChildren().add(minimapRepresentation);
        minimapRepresentation.setAlignment(Pos.CENTER);
        
        Text t3 = new Text("What activity was the user accomplishing in this region?");
        t3.setFont(Font.font ("Verdana", 20));
        t3.setFill(Color.BLACK);
        root.getChildren().add(t3);
        
        //was he drawing? was he displacing the minimap? was he just sitting still?
        //want the list of questions, beside each i want a bullet button
        //was he tracing a triangle, was he tracing a square was he tracing a squiggle, was he filling in a circle
        //was he using a pencil, nail, chalk, eraser
        //was he panning the minimap
        HBox q1AndBullets = new HBox();
        VBox bullets = new VBox();
        VBox moreBullets = new VBox();
        VBox question1 = new VBox();
        question1.setAlignment(Pos.CENTER_LEFT);
        q1AndBullets.getChildren().add(question1);
        q1AndBullets.getChildren().add(bullets);
        q1AndBullets.getChildren().add(moreBullets);
        root.getChildren().add(q1AndBullets);
          
        final ToggleGroup group = new ToggleGroup();
        RadioButton rb1 = new RadioButton("they are drawing");
        rb1.setToggleGroup(group);        

        RadioButton rb2 = new RadioButton("they are panning the mini-map");
        rb2.setToggleGroup(group);
         
        RadioButton rb3 = new RadioButton("they are sitting still");
        rb3.setToggleGroup(group);
        group.selectToggle(rb3);
        question1.getChildren().addAll(rb1,rb2,rb3);
        
        toolGroup = new ToggleGroup();
        RadioButton tgrb1 = new RadioButton("Drawing with a pencil");
        tgrb1.setToggleGroup(toolGroup);        

        RadioButton tgrb2 = new RadioButton("Drawing with a piece of metal");
        tgrb2.setToggleGroup(toolGroup);
         
        RadioButton tgrb3 = new RadioButton("Drawing with a piece of chalk");
        tgrb3.setToggleGroup(toolGroup);
        
        RadioButton tgrb4 = new RadioButton("Erasing");
        tgrb4.setToggleGroup(toolGroup);
        toolGroup.selectToggle(tgrb1);
       // bullets.getChildren().addAll(tgrb1,tgrb2,tgrb3,tgrb4);
        
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
          
          RadioButton sgrb5 = new RadioButton("I do not know");
          sgrb5.setToggleGroup(shapeGroup);
          
          RadioButton sgrb6 = new RadioButton("None of the above");
          sgrb6.setToggleGroup(shapeGroup);
          shapeGroup.selectToggle(sgrb5);
        
        //if the user selects was drawing, show multiple choices for shapes and for tool used
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
			@Override
			public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {				
				RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
				String toogleGroupValue = selectedRadioButton.getText();
				if (rb1.getText().equals(toogleGroupValue)) {
					//tool toggle group
            		toolGroup = new ToggleGroup();
                    RadioButton tgrb1 = new RadioButton("Drawing with a pencil");
                    tgrb1.setToggleGroup(toolGroup);        

                    RadioButton tgrb2 = new RadioButton("Drawing with a piece of metal");
                    tgrb2.setToggleGroup(toolGroup);
                     
                    RadioButton tgrb3 = new RadioButton("Drawing with a piece of chalk");
                    tgrb3.setToggleGroup(toolGroup);
                    
                    RadioButton tgrb4 = new RadioButton("Erasing");
                    tgrb4.setToggleGroup(toolGroup);
                    toolGroup.selectToggle(tgrb1);
                    bullets.getChildren().addAll(tgrb1,tgrb2,tgrb3,tgrb4);
                    
                  //shape toggle group
            		shapeGroup = new ToggleGroup();
                    RadioButton sgrb1 = new RadioButton("Tracing the shape of a triangle");
                    sgrb1.setToggleGroup(shapeGroup);        

                    RadioButton sgrb2 = new RadioButton("Tracing the shape of a square");
                    sgrb2.setToggleGroup(shapeGroup);
                     
                    RadioButton sgrb3 = new RadioButton("Tracing the shape of a squiggle");
                    sgrb3.setToggleGroup(shapeGroup);
                    
                    RadioButton sgrb4 = new RadioButton("Erasing");
                    sgrb4.setToggleGroup(shapeGroup);
                    
                    RadioButton sgrb5 = new RadioButton("I do not know");
                    sgrb5.setToggleGroup(shapeGroup);
                    
                    RadioButton sgrb6 = new RadioButton("None of the above");
                    sgrb6.setToggleGroup(shapeGroup);
                    shapeGroup.selectToggle(sgrb5);
                    moreBullets.getChildren().addAll(sgrb1,sgrb2,sgrb3,sgrb4,sgrb5,sgrb6);        
				}else {
					bullets.getChildren().clear();
					moreBullets.getChildren().clear();
				}
			} 
        });
        
        TextField t4 = new TextField();
        
       Button submit = new Button("SUBMIT");
       submit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {             	            
            	//don't worry about any of that stuff for tool test
        		if ((con.iModel.task == con.iModel.TOOL_IDENTIFICATION_TASK)) {
        			//get result from tool toggle group
        			// post what was the current tool selection for path sounds
        			RadioButton toolGroupResult = (RadioButton) toolGroup.getSelectedToggle();
    				String toolGroupResultString = toolGroupResult.getText();
    				userInput += "User claims that their partner is "+toolGroupResultString+"\n";
    				userInput += "Current sound file for paths is "+con.model.netSelectedSoundFile.toString();
            		String fileName = "Tool identification task";
            		if (!con.iModel.Sounds) {
            			fileName += " without sounds!";
            		}
    				CreateFile log = new CreateFile(userInput, fileName);
    				con.iModel.currentLogFileName = fileName;
                    System.out.println(userInput);	                               
            		
        		}else if ((con.iModel.task == con.iModel.SHAPE_DETECTION_TASK)) {
        			//get result from shape toggle group        			
        			RadioButton toolGroupResult = (RadioButton) shapeGroup.getSelectedToggle();
    				String toolGroupResultString = toolGroupResult.getText();
    				userInput += "User claims that their partner is "+toolGroupResultString+"\n";
    				//userInput += "Current sound file for paths is "+con.model.selectedSoundFile.toString();
            		String fileName = "Shape detection task";
            		if (!con.iModel.Sounds) {
            			fileName += " without sounds!";
            		}
    				CreateFile log = new CreateFile(userInput, fileName);
    				con.iModel.currentLogFileName = fileName;
                    System.out.println(userInput);	                               
            		
        		}else {        			
            	
            	userInput = "User selected region "+selectedField+"\n"+
            "The user was "+t4.getText()+" in his selection \n";
            	
            	//don't worry about any of that stuff for location test
        		if (!(con.iModel.task == con.iModel.LOCATION_IDENTIFICATION_TASK)) {        		        		
            	
            	//needs to be the results of the radio button questions
            	RadioButton groupResult = (RadioButton) group.getSelectedToggle();
				String groupResultString = groupResult.getText();
				userInput += "User claims that "+groupResultString+"\n";
				if (groupResultString.equals(rb1.getText())) {
				RadioButton toolGroupResult = (RadioButton) toolGroup.getSelectedToggle();
				String toolGroupResultString = toolGroupResult.getText();
				RadioButton shapeGroupResult = (RadioButton) shapeGroup.getSelectedToggle();
				String shapeGroupResultString = shapeGroupResult.getText();				                                
                	userInput += "User claims that their partner is "+toolGroupResultString+"\n";
                	userInput += "User claims that their partner is "+shapeGroupResultString+"\n";
                }
        		}
        		
				//send userInput to controller
        		String fileName = "FreezeTest";
        		if (con.iModel.task == con.iModel.LOCATION_IDENTIFICATION_TASK) {
        			fileName = "Location identification task";        			        			
        		}
        		
        	//	if (con.iModel.task == con.iModel.TOOL_IDENTIFICATION_TASK) {
        			//this used to be freezeTest tool identification
        	//		fileName += "Tool identification task";
        	//	}        		
        		if (!con.iModel.Sounds) {
        			fileName += " without sounds!";
        			System.out.println("without sounds!");
        		}
				CreateFile log = new CreateFile(userInput, fileName);
				con.iModel.currentLogFileName = fileName;
                System.out.println(userInput);
        		}
                //send msg to user 1 so that they can continue their task
                con.state = con.CLOSE_PROMPT_FOR_SHAPE;
                con.iModel.freezeTestOff();
				con.model.notifySubscribers();					
				//con.model.showTaskStage();	
				FTT.startTime = FTTstartTimeCopy +(System.currentTimeMillis()-quizStartTime);
				FTT.timeIncrement = FTTincrementCopy;
				close();
            
            }
       });
       submit.prefHeight(100);
       VBox submitBox = new VBox();
       submitBox.setPrefHeight(SceneHeight);
       submitBox.setAlignment(Pos.BOTTOM_CENTER);
       submitBox.getChildren().add(submit);       
       root.getChildren().add(submitBox);
        
       //tweak appearance for location task
		if (con.iModel.task == con.iModel.LOCATION_IDENTIFICATION_TASK) {
			root.getChildren().remove(t3);
			root.getChildren().remove(q1AndBullets);
		}
	       //tweak appearance for tool task
			if (con.iModel.task == con.iModel.TOOL_IDENTIFICATION_TASK) {
				root.getChildren().clear();
				t.setText("Please select with which tool \n was the other User previously drawing\n"
						+ "Press the submit button when complete");
				root.getChildren().add(t);
				root.getChildren().addAll(tgrb1,tgrb2,tgrb3,tgrb4,submit);				
			}
		       //tweak appearance for shape task
				if (con.iModel.task == con.iModel.SHAPE_DETECTION_TASK) {
					root.getChildren().clear();
					t.setText("Please select which shape \n the other User was previously drawing"
							+ "Press the submit button when complete");
					root.getChildren().add(t);
					root.getChildren().addAll(sgrb1,sgrb2,sgrb3,sgrb4,sgrb5,sgrb6,submit);				
				}
			
               
        c.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) { 
            	if (state == PICKING) { 
            	
            	gc.setFill(Color.WHITE);
            	gc.fillRect(0, 0, c.getWidth(), c.getHeight());
            	gc.setFill(Color.LIGHTGREEN);            	            	
            	if (me.getX()> 0 && me.getX()< c.getWidth()/2) {
            		if (me.getY() >0 && me.getY() < c.getHeight()/2) {
            			//draw rectangle in region 1
            			gc.fillRect(0, 0, c.getWidth()/2, c.getHeight()/2);
            		}
            		if (me.getY() <c.getHeight() && me.getY() > c.getHeight()/2) {
            			//draw rectangle in region 2
            			gc.fillRect(0, c.getHeight()/2, c.getWidth()/2, c.getHeight()/2);
            		}
            	}            	
            	if (me.getX()< c.getWidth() && me.getX()> c.getWidth()/2) {
            		if (me.getY() >0 && me.getY() < c.getHeight()/2) {
            			//draw rectangle in region 3
            			gc.fillRect(c.getWidth()/2, 0, c.getWidth()/2, c.getHeight()/2);
            		}
            		if (me.getY() <c.getHeight() && me.getY() > c.getHeight()/2) {
            			//draw rectangle in region 4
            			gc.fillRect(c.getWidth()/2, c.getHeight()/2, c.getWidth()/2, c.getHeight()/2);
            		}
            	}
            }
            }
        });
        
        
        c.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {             	
            	state = SELECTED;
            	gc.setFill(Color.WHITE);
            	gc.fillRect(0, 0, c.getWidth(), c.getHeight());
            	gc.setFill(Color.BLACK);            	            	
            	if (me.getX()> 0 && me.getX()< c.getWidth()/2) {
            		if (me.getY() >0 && me.getY() < c.getHeight()/2) {
            			//draw rectangle in region 1
            			gc.fillRect(0, 0, c.getWidth()/2, c.getHeight()/2);
            			selectedField = 1;
            		}
            		if (me.getY() <c.getHeight() && me.getY() > c.getHeight()/2) {
            			//draw rectangle in region 2
            			gc.fillRect(0, c.getHeight()/2, c.getWidth()/2, c.getHeight()/2);
            			selectedField = 2;
            		}
            	}            	
            	if (me.getX()< c.getWidth() && me.getX()> c.getWidth()/2) {
            		if (me.getY() >0 && me.getY() < c.getHeight()/2) {
            			//draw rectangle in region 3
            			gc.fillRect(c.getWidth()/2, 0, c.getWidth()/2, c.getHeight()/2);
            			selectedField = 3;
            		}
            		if (me.getY() <c.getHeight() && me.getY() > c.getHeight()/2) {
            			//draw rectangle in region 4
            			gc.fillRect(c.getWidth()/2, c.getHeight()/2, c.getWidth()/2, c.getHeight()/2);
            			selectedField = 4;
            		}
            	}            	
            	System.out.println("view port region = "+determineViewPortRegion(viewPortCenter));
                if (selectedField == determineViewPortRegion(viewPortCenter)) {
                    //t2.setFont(Font.font ("Verdana", 80));
                    //t2.setFill(Color.GREEN);
                    t4.setText("CORRECT!");
                }else{             
                	//t2.setFont(Font.font ("Verdana", 20));
                    //t2.setFill(Color.RED);
                    t4.setText("INCORRECT!");
                }
            	
            }
        });                                
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
						System.out.println(key.getText());	
						con.state = con.READY;
						con.view.modelChanged();
						con.radarView.modelChanged();
						close();
					}
				}							    		
     	});                               
	}
	
	public int determineViewPortRegion(Coordinate p) {
		System.out.println("p.x = "+p.x);
		System.out.println("p.y = "+p.y);
		if (p.x > 0 && p.x < 0.5) {
			if (p.y >0 && p.y < 0.5) {
				return 1;
			}
			if (p.y >= 0.5&& p.y< 1.0) {
				return 2;
			}
		}		
		if (p.x < 1.0 && p.x > 0.5) {
			if (p.y >0 && p.y < 0.5) {
				return 3;
			}
			if (p.y >= 0.5&& p.y< 1.0) {
				return 4;
			}
		}		
		return 0;
	}
	


}
