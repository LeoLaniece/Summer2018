package test;

import javafx.scene.Group;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

//dabord, desine quelquechose
//essaye dintaller tout les boutons et menu's qui existent deja
public class Draw3 extends Application{
	
	 @Override
	    public void start(Stage primaryStage) throws InterruptedException {
	        primaryStage.setTitle("Drawing Tool");
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
	        model.setModelRadarView(radarView);
	        view.setModelRadarView(radarView);

	        primaryStage.setScene(scene);
	        primaryStage.setX(0);
	        primaryStage.setY(0);
	        primaryStage.show();
	        
	

}
	 public static void main(String[] args) {

	        Application.launch(args);

	    }
}
