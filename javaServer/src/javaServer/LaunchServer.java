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

public class LaunchServer extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing Tool Server");
        //set the size of the window here
        //make sure the height is 200 + what you want to accommodate the color picker and sample line
        int SceneWidth = 800;
        int SceneHeight = 600;
        StackPane root = new StackPane();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        Draw2Model model = new Draw2Model();
        Draw2View view = new Draw2View(SceneWidth,SceneHeight-200,model);       
        //can change the size of the minimap here, minimap will be scaled to /7 of its size
        Draw2miniMap radarView = new Draw2miniMap(1000,1000,model);
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
