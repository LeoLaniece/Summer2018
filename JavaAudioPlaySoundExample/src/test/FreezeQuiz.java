package test;

import javafx.application.Application;
import javafx.scene.control.ToggleButton ;
import javafx.scene.control.RadioButton ;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Font ;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.geometry.Pos ;
import java.lang.reflect.Field;
import javafx.application.Platform;
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
	
	public FreezeQuiz(Draw2Controller con, Coordinate viewPortCenter) {
        setTitle("Freeze Quiz");
        //set the size of the window here
        //make sure the height is 200 + what you want to accommodate the color picker and sample line
        int SceneWidth = 800;
        int SceneHeight = 600;
        VBox root = new VBox();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        
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
        Canvas c = new Canvas(1000/7,1000/7);
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
        HBox q1AndBullets = new HBox();
        VBox bullets = new VBox();
        VBox question1 = new VBox();
        question1.setAlignment(Pos.CENTER_LEFT);
        q1AndBullets.getChildren().add(question1);
        q1AndBullets.getChildren().add(bullets);
        root.getChildren().add(q1AndBullets);
          
        final ToggleGroup group = new ToggleGroup();

        RadioButton rb1 = new RadioButton("Were they drawing?");
        rb1.setToggleGroup(group);
        rb1.setSelected(true);

        RadioButton rb2 = new RadioButton("Were they panning the mini-map?");
        rb2.setToggleGroup(group);
         
        RadioButton rb3 = new RadioButton("Were they sitting still?");
        rb3.setToggleGroup(group);
        question1.getChildren().addAll(rb1,rb2,rb3);
        
        TextField t4 = new TextField();
        root.getChildren().add(t4);
        t4.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent key) {
				if (key.getCode().equals(KeyCode.ENTER)) {
	                userInput = t4.getText();	                
	                System.out.println(userInput);	                
	                t4.clear();
	                con.state = con.READY;
	                con.iModel.freezeTestOff();
					con.view.modelChanged();					
					con.radarView.modelChanged();
					
					close();	                
	            }

			}
        });
        

               
        c.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) { 
            	if (state == PICKING) { 
            	
            	gc.setFill(Color.WHITE);
            	gc.fillRect(0, 0, c.getWidth(), c.getHeight());
            	gc.setFill(Color.YELLOW);            	            	
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
            	gc.setFill(Color.RED);            	            	
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
                    t2.setFont(Font.font ("Verdana", 80));
                    t2.setFill(Color.GREEN);
                    t2.setText("CORRECT!");
                }else{             
                	t2.setFont(Font.font ("Verdana", 20));
                    t2.setFill(Color.RED);
                    t2.setText("INCORRECT!");
                }
            	
            }
        });        
        
        
        
        root.requestFocus();
        setScene(scene);
        setX(800);
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
