package test;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FreezeQuiz extends Stage{

	
	public FreezeQuiz(Draw2Controller c) {
        setTitle("Freeze Quiz");
        //set the size of the window here
        //make sure the height is 200 + what you want to accommodate the color picker and sample line
        int SceneWidth = 800;
        int SceneHeight = 600;
        StackPane root = new StackPane();	        
        Scene scene = new Scene(root, SceneWidth,SceneHeight);
        
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
						c.state = c.READY;
						c.view.modelChanged();
						c.radarView.modelChanged();
						close();
					}
				}							    		
     	});
	}
	


}
