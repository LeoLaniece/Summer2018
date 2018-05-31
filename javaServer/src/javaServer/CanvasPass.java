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

public class CanvasPass extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Drawing Tool Server");
        //a type of layout	        
        int SceneWidth = 1000;
        int SceneHeight = 600;
        StackPane root = new StackPane();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        Draw2Model model = new Draw2Model();
        Draw2View view = new Draw2View(SceneWidth,SceneHeight-200,model);
        //make the radarView a scale of the size of the logical size of the view
        
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

        primaryStage.setScene(scene);
        primaryStage.show();
		
        //progress report
        //make sure both models know the viewPortOffsets and draw the paths appropriately
        //maybe try passing the current viewport location over the network.
        
        
		String[] args = new String[1];
		args[0] = "HEELOWORLD";
		GreetingServer.main(args, model, controller);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(final WindowEvent me) {  
            	//close the port here
            }
		});
	}
	
	
	 public static void main(String[] args) {
	        Application.launch(args);
	    }

}
