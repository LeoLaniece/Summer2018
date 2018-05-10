package test;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.layout.VBox;


//dabord, desine quelquechose
//essaye dintaller tout les boutons et menu's qui existent deja
public class Draw3 extends Application{
	
	 @Override
	    public void start(Stage primaryStage) {
	        primaryStage.setTitle("Drawing Tool");
	        //a type of layout
	        VBox root = new VBox();
	        Scene scene = new Scene(root, 700, 500);
	        Draw2Model model = new Draw2Model();
	        Draw2View view = new Draw2View(model);
	        model.addSubscriber(view);
	        
	       
	        root.getChildren().add(view);
	        primaryStage.setScene(scene);
	        primaryStage.show();
	

}
	 public static void main(String[] args) {

	        Application.launch(args);

	    }
}