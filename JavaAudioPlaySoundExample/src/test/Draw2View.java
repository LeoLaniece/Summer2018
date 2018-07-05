package test;

import java.io.File;
import javafx.scene.shape.Path;
import java.lang.reflect.Field;
import javafx.application.Platform;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.LineTo; 

import java.lang.reflect.Modifier;

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
    File pencil = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\randomScatteredShapes.JPG");        
    Image image = new Image(pencil.toURI().toString());
    double imageWidth;
    double imageHeight;
    double imageX;
    double imageY;
	
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
		
		setCanvas(logicalHeight,logicalWidth);
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
		VBox UCRight = new VBox();
		UCRight.setPrefWidth(height);
		UCRight.setAlignment(Pos.BOTTOM_RIGHT);
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

	
	public void setCanvas(double h, double w) {
		c = new Canvas(width,height);
		gc = c.getGraphicsContext2D();
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width,height);
	}
	
	public void setSampleStroke(VBox UCLeft, VBox UCRight) {
		UCLeft.getChildren().add(model.labelStroke);		
		//sizing for sampleLine
		StackPane stackpane = new StackPane();
        stackpane.setPrefHeight(model.MAXSTROKE);
        stackpane.setPrefWidth(model.sampleLine.getEndX() + model.MAXSTROKE);
        stackpane.setAlignment(Pos.CENTER);
        stackpane.getChildren().add(model.sampleLine);
		
		UCLeft.getChildren().add(stackpane);		
		UCLeft.getChildren().add(model.strokeSlider);
		UCLeft.getChildren().add(model.btnClear);
		UCLeft.getChildren().add(model.btnPencil);
		UCLeft.getChildren().add(model.btnMetal);
		UCLeft.getChildren().add(model.btnChalk);
		UCLeft.getChildren().add(model.btnEraser);		
		UCRight.getChildren().add(model.colorLabel);
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
        UCRight.getChildren().add(flow);
	}
	
	public void drawModelPaths() {	
		    	//DRAW MODELpATHS
		//darw the relativized modelpath coordinates
		//do everything the same, but! multiply by the canvas height and width where appropriate
				for (int i=0; i<model.getModelPaths().size();i++) {	 
					gc.beginPath();
					gc.moveTo(model.modelPathsCoordinates.get(i).x*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(i).x),
							model.modelPathsCoordinates.get(i).y*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(i).y));											
					gc.setStroke(model.getModelPaths().get(i).getStroke());
					gc.setLineWidth(model.getModelPaths().get(i).getStrokeWidth());
					if (model.getModelPaths().get(i).getElements().size()>0) {
					final double translateX = iModel.modelPathsTranslateByCoordinates.get(i).x;
					final double translateY = iModel.modelPathsTranslateByCoordinates.get(i).y;											
					synchronized (model.getModelPaths()) {						
						for (int a = 0; a<model.getModelPaths().get(i).getElements().size(); a++) {
							if (model.getModelPaths().get(i).getElements().get(a) instanceof LineTo) {
							gc.lineTo(((LineTo) model.getModelPaths().get(i).getElements().get(a)).getX()*radarView.width
									+translateX, 
									((LineTo) model.getModelPaths().get(i).getElements().get(a)).getY()*radarView.height
									+translateY);	
							}
						}																	
					}					
					gc.stroke();
				}					
				}
	}
	
	public void paintOverPaths() {
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, width, height);
	}		
	
	@Override
	public void modelChanged() {
		Platform.runLater(new Runnable() {
		    @Override
		        public void run() {		
		if (controller.state == controller.PAN_READY) {
			drawImage();			
			//if we are running the read and observe task, don't draw the paths			
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
		gc.setStroke(Color.GREEN);
		gc.setLineWidth(5);
		gc.strokeRect(0, 0, width, height);
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
        gc.setLineWidth(model.sampleLine.getStrokeWidth());
        gc.setStroke(model.sampleLine.getStroke());
		gc.strokeLine(pathStartx, pathStarty, x*width, y*height);
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
		gc.drawImage(getImage(), -iModel.viewPortX*7 +imageX, -iModel.viewPortY*7+imageY, 
				imageWidth, imageHeight);
		}
		
	}
	
	public void addToPath() {
		//try 				
		gc.beginPath();
		synchronized (model.getModelPaths()) {						
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {					
		//move to the second last coordinate on the elements list
		gc.moveTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
				*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
				*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));	
		gc.setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
		gc.setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth());
		
		//draw a LineTo to the last element in the path
		gc.lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
				*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
				(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
				*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));		
		//stroke the bit
		gc.stroke();
		}
		}
	}
	
	public void drawPath() {
		synchronized (model.getModelPaths()) {
		if (model.getModelPaths().size()>0) {		
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() > 2) {
			addToPath();
		}else if (model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size() ==2){
			gc.beginPath();
			gc.moveTo((((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getX())
					*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
					(((MoveTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-2)).getY())
					*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));	
			gc.setStroke(model.getModelPaths().get(model.getModelPaths().size()-1).getStroke());
			gc.setLineWidth(model.getModelPaths().get(model.getModelPaths().size()-1).getStrokeWidth());			
			//draw a LineTo to the last element in the path
			gc.lineTo((((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getX())
					*radarView.width +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).x), 
					(((LineTo) model.getModelPaths().get(model.getModelPaths().size()-1).getElements().get(model.getModelPaths().get(model.getModelPaths().size()-1).getElements().size()-1)).getY())
					*radarView.height +(iModel.modelPathsTranslateByCoordinates.get(iModel.modelPathsTranslateByCoordinates.size()-1).y));		
			//stroke the bit
			gc.stroke();
		}
		}	
		}
	}
	
	//all for net path
	public void addToNetPath() {
		//try 				
		gc.beginPath();
		synchronized (model.getNetWorkPath()) {						
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getNetWorkPath().getElements().size() > 2) {					
		//move to the second last coordinate on the elements list
		gc.moveTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
				*radarView.width +(model.netPathTranslateByCoordinate.x), 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
				*radarView.height +(model.netPathTranslateByCoordinate.y));	
		gc.setStroke(model.getNetWorkPath().getStroke());
		gc.setLineWidth(model.getNetWorkPath().getStrokeWidth());
		
		//draw a LineTo to the last element in the path
		gc.lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
				*radarView.width +(model.netPathTranslateByCoordinate.x), 
				(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
				*radarView.height +(model.netPathTranslateByCoordinate.y));		
		//stroke the bit
		gc.stroke();
		}
		}
	}
	
	public void drawNetPath() {
		if (controller.state != controller.READ_AND_OBSERVE) {
		synchronized (model.getNetWorkPath()) {
		if (model.getNetWorkPath()!=null) {		
		//Path currentPath = model.getModelPaths().get(model.getModelPaths().size()-1);
		if (model.getNetWorkPath().getElements().size() > 2) {
			addToNetPath();
		}else if (model.getNetWorkPath().getElements().size() ==2){
			gc.beginPath();
			gc.moveTo((((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getX())
					*radarView.width +(model.netPathTranslateByCoordinate.x), 
					(((MoveTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-2)).getY())
					*radarView.height +(model.netPathTranslateByCoordinate.y));	
			gc.setStroke(model.getNetWorkPath().getStroke());
			gc.setLineWidth(model.getNetWorkPath().getStrokeWidth());			
			//draw a LineTo to the last element in the path
			gc.lineTo((((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getX())
					*radarView.width +(model.netPathTranslateByCoordinate.x), 
					(((LineTo) model.getNetWorkPath().getElements().get(model.getNetWorkPath().getElements().size()-1)).getY())
					*radarView.height +(model.netPathTranslateByCoordinate.y));		
			//stroke the bit
			gc.stroke();
		}
		}	
		}
		}
	}
	
	
	
	
	public void setController(Draw2Controller c) {
		controller = c;
	}
	
	public File mouseCursor = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\mouseCursor.png");        
    public Image mouseImage = new Image(mouseCursor.toURI().toString());
    
	public void drawMouseCursor(double x, double y) {
		drawImage();
    	gc.drawImage(mouseImage, x-10,
    			y-7,30,30);
	}
	
	public void setImageForFreezeTest() {
		File randomShapes = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\randomScatteredShapes.JPG");        
	    image = new Image(randomShapes.toURI().toString());
		imageWidth = radarView.width;
		imageHeight = radarView.height;
		imageX = 0;
		imageY = 0;
		drawImage();					
		drawModelPaths();
	}
	
	public void setImageForReadAndObserve() {
		File camelFacts = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\camelFacts.JPG");        
	    image = new Image(camelFacts.toURI().toString());
		imageWidth = width/2;
		imageHeight = height/6*7;
		imageX = radarView.width/7;
		//imageY = radarView.height/3;
		drawImage();					
		drawModelPaths();
	}
}
