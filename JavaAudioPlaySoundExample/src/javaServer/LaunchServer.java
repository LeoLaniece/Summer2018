package javaServer;

import javafx.scene.Group;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import test.Draw2Controller;
import test.Draw2Model;
import test.Draw2View;
import test.Draw2miniMap;
import test.InteractionModel;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

/**
 * Will launch the server version of the drawing application
 * Will connect to a single client version of the drawing application
 * @author HCI Lab
 *
 */
public class LaunchServer extends Application{
	
	//sets the size of all windows in the aplication
   public static int SceneWidth = 800;
   public static int SceneHeight = 600;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing Tool Server");
        StackPane root = new StackPane();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        Draw2Model model = new Draw2Model();
        Draw2View view = new Draw2View(SceneWidth,SceneHeight-200,model);       
        //can change the size of the minimap here, minimap will be scaled to 1/7 of the submitted values size
        //the submitted values sets the size of the virtual workspace.
        Draw2miniMap radarView = new Draw2miniMap(2000,2000,model);
        InteractionModel iModel = new InteractionModel(model,view);
        model.setIModel(iModel);
        view.setIModel(iModel);
        radarView.setIModel(iModel);
        radarView.drawViewPort();

        StackPane.setAlignment(view, Pos.TOP_LEFT);
        StackPane.setAlignment(radarView, Pos.TOP_LEFT);
        root.getChildren().add(view);
        root.getChildren().add(radarView);
        radarView.setPickOnBounds(false);
        
        Draw2Controller controller = new Draw2Controller(view, model,radarView);
        model.addSubscriber(view);
        model.addSubscriber(radarView);
        controller.setIModel(iModel);
        model.setModelView(view);
        view.setModelRadarView(radarView);
        model.setModelRadarView(radarView);
        view.setController(controller);
        radarView.setController(controller);
        model.setController(controller);
        
        primaryStage.setScene(scene);
        primaryStage.setX(0);
        primaryStage.setY(0);
        primaryStage.show();      
        
        //launch the server
		String[] args = new String[1];
		args[0] = "HEELOWORLD";
		DrawingServer.main(args, model, controller);		
	}	
	
	 public static void main(String[] args) {
	        Application.launch(args);
	    }

}
