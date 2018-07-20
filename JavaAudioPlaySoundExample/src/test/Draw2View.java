package test;

import java.io.File;
import javafx.scene.shape.Path;
import java.lang.reflect.Field;
import javafx.application.Platform;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo; 

import java.lang.reflect.Modifier;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
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


/**
 * should show a canvas with a minimap
 * a sample stroke controller 
 * and a color picker 
 * @author HCI Lab
 *
 */


//take out the sample line and colour picker while you implement the pan-able minimap
public class Draw2View extends Pane implements modelListener{
	public Draw2Controller controller;
	public float zoomScale =1;
	Pane topPane;
	Draw2Model model;
	public Canvas c;
	public GraphicsContext gc;
	double pathStartx=0;
	double pathStarty=0;
	public double height;
	public double width;
	double logicalHeight;
	double logicalWidth;
	public Group lineGroup;
	InteractionModel iModel;
	public Draw2miniMap radarView;
    File pencil = new File(new File("src\\soundAndImageFiles\\randomScatteredShapes.JPG").getAbsolutePath());        
    Image image = new Image(pencil.toURI().toString());
    double imageWidth;
    double imageHeight;
    double imageX;
    double imageY;
    public VBox UCRight;
    
	//put in a logical size?
	public Draw2View(double w, double h, Draw2Model m) {
		model = m;
		//canvas height and width
		height = h;
		width = w;
		logicalHeight = 1000;
		logicalWidth =1000;
		//lineGroup = model.lineGroup;
		
		topPane = new Pane();
		
		setCanvas();
		setLineGroup();     
		mainViewOnly();
	}
	
	public synchronized Image getImage() {
		return image;
	}
	
	public void mainViewOnly() {
		VBox root = new VBox();
		HBox underCanvas = new HBox();
		this.getChildren().add(root);				
		root.getChildren().add(c);		
		root.getChildren().add(underCanvas);
		VBox UCLeft = new VBox();
		UCLeft.setPrefWidth(height);
		UCLeft.setAlignment(Pos.BOTTOM_LEFT);
		UCRight = new VBox();
		UCRight.setPrefWidth(height);
		UCRight.setAlignment(Pos.BOTTOM_CENTER);
		underCanvas.getChildren().add(UCLeft);
		underCanvas.getChildren().add(UCRight);
		
		setSampleStroke(UCLeft, UCRight);						
		setFlowPane(UCRight);
	}
	public void setLineGroup() {
		StackPane group = new StackPane();
		//group.getChildren().add(lineGroup);
		group.setAlignment(Pos.TOP_LEFT);
		group.setPrefHeight(height);
		group.setPrefWidth(width);
		topPane.getChildren().add(group);
		
	}
	
	protected synchronized GraphicsContext getGC() {
		return gc;
	}
	
	public void setCanvas() {
		c = new Canvas(width,height);
		gc = c.getGraphicsContext2D();
		getGC().setFill(Color.WHITE);
		getGC().fillRect(0, 0, width,height);
	}
	
	public void setSampleStroke(VBox UCLeft, VBox UCRight) {
		//UCLeft.getChildren().add(model.labelStroke);		
		//sizing for sampleLine
		StackPane stackpane = new StackPane();
        stackpane.setPrefHeight(model.MAXSTROKE);
        stackpane.setPrefWidth(model.sampleLine.getEndX() + model.MAXSTROKE);
        stackpane.setAlignment(Pos.CENTER);
        stackpane.getChildren().add(model.sampleLine);
		
		//UCLeft.getChildren().add(stackpane);		
		//UCLeft.getChildren().add(model.strokeSlider);
		UCRight.getChildren().add(model.btnClear);
		
		HBox h1 = new HBox();		
		h1.getChildren().addAll(model.btnPencil, model.triangle);		
		UCLeft.getChildren().add(h1);
		HBox h2 = new HBox();		
		h2.getChildren().addAll(model.btnMetal, model.square);		
		UCLeft.getChildren().add(h2);
		HBox h3 = new HBox();		
		h3.getChildren().addAll(model.btnChalk, model.squiggle);		
		UCLeft.getChildren().add(h3);
		HBox h4 = new HBox();		
		h4.getChildren().addAll(model.btnEraser, model.circle);		
		UCLeft.getChildren().add(h4);				
		//UCRight.getChildren().add(model.colorLabel);
	}
	
	public void setFlowPane(VBox UCRight) {
		//set up the color picker grid
		FlowPane flow = new FlowPane();
        flow.setVgap(2);
        flow.setHgap(2);
        flow.setPrefWrapLength(400);
        // Get the declared fields for the Color class
        Field[] colorFields = Color.class.getDeclaredFields();
        for (Field fieldname : colorFields) {
            int mods = fieldname.getModifiers();
            // Only look at the field if it's public, static, and NOT 'TRANSPARENT'
            if (Modifier.isPublic(mods) && Modifier.isStatic(mods)
                    && !(fieldname.getName().equals("TRANSPARENT"))) {
                try {
                    Color c = Color.web(fieldname.getName());
                    // Make a rectangle with that field name's color
                    final Rectangle r = new Rectangle(15, 15, c);
                    // Configure the rectangle
                    r.setCursor(Cursor.HAND);
                    Tooltip t = new Tooltip(fieldname.getName().toLowerCase());
                    Tooltip.install(r, t);
                    r.setUserData(t.getText());
                    r.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent me) {
                            model.sampleLine.setStroke(r.getFill());
                            model.colorLabel.setText("color: " + ((String) r.getUserData()));
                        }
                    });
                    flow.getChildren().add(r);
                }catch (IllegalArgumentException e) {
                    // just ignore it if for some reason we can't make
                    // a color
                }
            }
        }
       // UCRight.getChildren().add(flow);
	}
	
	public void drawModelPaths() {	
		    	//DRAW MODELpATHS
		//darw the relativized modelpath coordinates
		//do everything the same, but! multiply by the canvas height and width where appropriate
		synchronized (getGC()) {
				for (int i=0; i<model.getModelPaths().size();i++) {	 
					getGC().beginPath();
					getGC().moveTo(model.modelPathsCoordinates.get(i).x*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(i).x),
							model.modelPathsCoordinates.get(i).y*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(i).y));											
					getGC().setStroke(model.getModelPaths().get(i).getStroke());
					getGC().setLineWidth(model.getModelPaths().get(i).getStrokeWidth());
					if (model.getModelPaths().get(i).getElements().size()>0) {
					final double translateX = iModel.modelPathsTranslateByCoordinates.get(i).x;
					final double translateY = iModel.modelPathsTranslateByCoordinates.get(i).y;											
					synchronized (model.getModelPaths()) {						
						for (int a = 0; a<model.getModelPaths().get(i).getElements().size(); a++) {
							if (model.getModelPaths().get(i).getElements().get(a) instanceof LineTo) {
								getGC().lineTo(((LineTo) model.getModelPaths().get(i).getElements().get(a)).getX()*radarView.width
									+translateX, 
									((LineTo) model.getModelPaths().get(i).getElements().get(a)).getY()*radarView.height
									+translateY);	
							}
						}																	
					}					
					getGC().stroke();
				}					
				}
		}
	}
	
	public void paintOverPaths() {
		synchronized (getGC()) {
		getGC().setFill(Color.WHITE);
		getGC().fillRect(0, 0, width, height);
		}
	}		
	
	@Override
	public void modelChanged() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {		
		if (controller.state == controller.PAN_READY) {
			drawImage();								
			drawModelPaths();			
		}else {
			if (controller.taskRunning) {
				drawBorder();
			}			
			drawPath();
		}					
		if (iModel.freezeTest) {
			paintOverPaths();
		}
		if (model.getModelPaths().size()==0) {
			//probably means that i have hit the clear button
			drawImage();					
			drawModelPaths();
			if (controller.taskRunning) {
				drawBorder();
			}
		}
		if (controller.state ==controller.READ_AND_OBSERVE) {
			setImageForReadAndObserve();			
			if (iModel.task == iModel.ACTIVITY_IDENTIFICATION_TASK) {
				setImageForFreezeTest();
			}
			drawImage();					
			drawBorder();
			//drawModelPaths();
		}
		if (controller.state ==controller.FREEZE_TEST_TASK) {
			setImageForFreezeTest();
			drawImage();					
			drawBorder();
			//drawModelPaths();
		}
		
		    }
		});
	}	
	
	public void resetView() {
		drawImage();
		drawModelPaths();		
	}
	
	public void drawBorder() {
		synchronized (getGC()) {
			getGC().setStroke(Color.GREEN);
			getGC().setLineWidth(5);
			getGC().strokeRect(0, 0, width, height);
		}
	}
	
	public void startPath(double x, double y) {
		//should take into account the size of the view
		//need to relativize the coodinates
		//0 = left
		//1.0 = right
		pathStartx = x*width;
		pathStarty = y*height;
	}
	public void strokePath(double x, double y) {
		synchronized (getGC()) {
		getGC().setLineWidth(model.sampleLine.getStrokeWidth());
		getGC().setStroke(model.sampleLine.getStroke());
		getGC().strokeLine(pathStartx, pathStarty, x*width, y*height);
		}
	}
	

	public void setModel(Draw2Model m) {
		model = m;
	}
	public void setIModel(InteractionModel iM) {
		iModel = iM;
	}
	public void setModelRadarView(Draw2miniMap v) {
		radarView = v;		
		imageWidth = radarView.width;
		imageHeight = radarView.height;
		imageX = 0;
		imageY = 0;
		//gc.drawImage(image, 0, 0, radarView.width, radarView.height);
		drawImage();
	}
	public void zoomIn() {
		zoomScale += 0.2;
		modelChanged();
		//zoom the paths		
	}

	public void drawImage() {			
		synchronized (getImage()) {
			synchronized (getGC()) {
				getGC().drawImage(getImage(), -iModel.viewPortX*7 +imageX, -iModel.viewPortY*7+imageY, 			
				imageWidth, imageHeight);
		}		
	}
	}
	
	public void addToPath() {
		//try 				
		
		synchronized (model.getModelPaths()) {	
			synchronized (getGC()) {
				getGC().beginPath();
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {					
		//move to the second last coordinate on the elements list
			getGC().moveTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
				*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
				*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));	
			getGC().setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
			getGC().setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth());
		
		//draw a LineTo to the last element in the path
			getGC().lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
				*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
				*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));		
		//stroke the bit
			getGC().stroke();
		}
		}
		}
	}
	
	public void drawPath() {		
		synchronized (model.getModelPaths()) {			
		if (model.getModelPaths().size()>0) {		
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
			
			//modelPaths size changes when you begin drawing the network path! how to adjust/change this?
			//could change from size -1 to size -2 if netPath is alive?(2 cases)
			//or put the net path as the 2nd last element in model paths
			
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {			
			addToPath();
		}else if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() ==2){
			synchronized (getGC()) {
				getGC().beginPath();
				getGC().moveTo((((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
					*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
					(((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
					*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));	
				getGC().setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
				getGC().setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth());			
			//draw a LineTo to the last element in the path
				getGC().lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
					*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
					(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
					*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));		
			//stroke the bit
				getGC().stroke();
		}
		}	
		}
		}
	}
	
	//PROGRESS 
	//finish synchornising all access to the graphics context of the view and the radarView
	
	
	//all for net path
	public void addToNetPath() {
		//try 						
		synchronized (model.getNetWorkPath()) {	
		synchronized (getGC()) {
		getGC().beginPath();
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getNetWorkPath().getElements().size() > 2) {					
		//move to the second last coordinate on the elements list
			getGC().moveTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
				*radarView.width +(model.netPathTranslateByCoordinate.x), 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
				*radarView.height +(model.netPathTranslateByCoordinate.y));	
			getGC().setStroke(model.getNetWorkPath().getStroke());
			getGC().setLineWidth(model.getNetWorkPath().getStrokeWidth());
		
		//draw a LineTo to the last element in the path
			getGC().lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
				*radarView.width +(model.netPathTranslateByCoordinate.x), 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
				*radarView.height +(model.netPathTranslateByCoordinate.y));		
		//stroke the bit
			getGC().stroke();
		}
		}
		}
	}
	
	public void drawNetPath() {
		if (iModel.task != iModel.READ_AND_OBSERVE) {
		if (controller.state != controller.READ_AND_OBSERVE) {
		synchronized (model.getNetWorkPath()) {
		if (model.getNetWorkPath()!=null) {		
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getNetWorkPath().getElements().size() > 2) {
			addToNetPath();
		}else if (model.getNetWorkPath().getElements().size() ==2){
			synchronized (getGC()) {
				getGC().beginPath();
				getGC().moveTo((((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
					*radarView.width +(model.netPathTranslateByCoordinate.x), 
					(((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
					*radarView.height +(model.netPathTranslateByCoordinate.y));	
				getGC().setStroke(model.getNetWorkPath().getStroke());
				getGC().setLineWidth(model.getNetWorkPath().getStrokeWidth());			
			//draw a LineTo to the last element in the path
				getGC().lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
					*radarView.width +(model.netPathTranslateByCoordinate.x), 
					(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
					*radarView.height +(model.netPathTranslateByCoordinate.y));		
			//stroke the bit
				getGC().stroke();
		}
		}
		}	
		}
		}
		}
	}
	
	public void setController(Draw2Controller c) {
		controller = c;
	}
	
	public File mouseCursor =new File(new File("src\\soundAndImageFiles\\mouseCursor.png").getAbsolutePath());       
    public Image mouseImage = new Image(mouseCursor.toURI().toString());
    
	public void drawMouseCursor(double x, double y) {
		drawImage();
    	gc.drawImage(mouseImage, x-10,
    			y-7,30,30);
	}
	
	public void setImageForFreezeTest() {
		File randomShapes = new File(new File("src\\soundAndImageFiles\\randomScatteredShapes.JPG").getAbsolutePath());       
	    image = new Image(randomShapes.toURI().toString());
		imageWidth = radarView.width;
		imageHeight = radarView.height;
		imageX = 0;
		imageY = 0;
		drawImage();					
		drawModelPaths();
	}
	
	public void setImageForReadAndObserve() {
		File camelFacts = new File(new File("src\\soundAndImageFiles\\camelFacts.JPG").getAbsolutePath());        
	    image = new Image(camelFacts.toURI().toString());
		imageWidth = width/2;
		imageHeight = height/6*7;
		imageX = radarView.width/7;
		//imageY = radarView.height/3;
		drawImage();					
		drawModelPaths();
	}
	
	public void addDoneTrainingButton(FreezeTestInstructions lastStage) {
	      Button done = new Button("Completed Training");
	      done.setOnAction(new EventHandler<ActionEvent>() {
	           public void handle(ActionEvent event) {
	        	   controller.state = controller.CLEAR_WORKSPACE;
	        	   model.resetModel();
	        	   model.notifySubscribers();	        	   
	        	   //change super state 
	        	   controller.superState = controller.SOUNDS_OVER_NETWORK;
	        	   //restore current log file
	        	   iModel.currentLogFileName = lastStage.logFile;
	        	   //open instructions window
	        	   lastStage.show();
	        	   //close this window
	        	   UCRight.getChildren().remove(done);
	           }
	      });
	      done.setPrefHeight(100);
	     // done.setPrefWidth(100);
	      UCRight.getChildren().add(done);
	}
}
