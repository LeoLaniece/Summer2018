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

/**
 * Will launch a second version of the drawing application
 * Stage will be the same size as the Server Stage
 * @author HCI Lab
 *
 */
public class ClientDrawingStage extends Stage {
	public Draw2Model m;
	public Draw2Controller c;

	
	ClientDrawingStage(){
		setTitle("Drawing Application client");	    
		int SceneWidth = javaServer.LaunchServer.SceneWidth;
        int SceneHeight = javaServer.LaunchServer.SceneHeight;
        StackPane root = new StackPane();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        Draw2Model model = new Draw2Model();
        Draw2View view = new Draw2View(SceneWidth,SceneHeight-200,model);
        //values passed in here will be equivalent to the total size of the virtual workspace
        Draw2miniMap radarView = new Draw2miniMap(2000,2000,model);
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

		try {
			c = new Draw2Controller(view, model,radarView);

        model.addSubscriber(view);
        model.addSubscriber(radarView);
        c.setIModel(iModel);
        view.setController(c);
        radarView.setController(c);
        model.setController(c);

        setScene(scene);
        setX(800);
        setY(0);
        show();
        //the stage stores a reference of the model to pass networking events
        m = model;
        		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
