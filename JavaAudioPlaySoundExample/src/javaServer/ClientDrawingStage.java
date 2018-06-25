package javaServer;
import javafx.scene.Group;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import test.Draw2Controller;
import test.Draw2Model;
import test.Draw2View;
import test.Draw2miniMap;
import test.InteractionModel;
import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
public class ClientDrawingStage extends Stage {
	public Draw2Model m;
	public Draw2Controller c;
	
	ClientDrawingStage(){
		setTitle("Drawing Tool client");
        //a type of layout	        
		int SceneWidth = 800;
        int SceneHeight = 600;
        StackPane root = new StackPane();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        Draw2Model model = new Draw2Model();
        Draw2View view = new Draw2View(SceneWidth,SceneHeight-200,model);
        //make the radarView a scale of the size of the logical size of the view
        
        Draw2miniMap radarView = new Draw2miniMap(1000,1000,model);
        InteractionModel iModel = new InteractionModel(model,view);
        model.setIModel(iModel);
        view.setIModel(iModel);
        radarView.setIModel(iModel);
        radarView.drawViewPort();
        model.setModelView(view);
        view.setModelRadarView(radarView);
        model.setModelRadarView(radarView);

        StackPane.setAlignment(view, Pos.TOP_LEFT);
        StackPane.setAlignment(radarView, Pos.TOP_LEFT);
        root.getChildren().add(view);
        root.getChildren().add(radarView);
        radarView.setPickOnBounds(false);
        
       // Draw2Controller controller;
		try {
			c = new Draw2Controller(view, model,radarView);

        model.addSubscriber(view);
        model.addSubscriber(radarView);
        c.setIModel(iModel);

        setScene(scene);
        setX(800);
        setY(0);
        show();
        m = model;
        		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
