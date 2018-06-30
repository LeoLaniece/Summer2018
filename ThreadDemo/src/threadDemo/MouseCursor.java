package threadDemo;

import java.io.File;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MouseCursor  extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Load Image");

        StackPane sp = new StackPane();
     // new Image(url)
        File pencil = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\mouseCursor.png");        
        Image image = new Image(pencil.toURI().toString());
        
        // new BackgroundSize(width, height, widthAsPercentage, heightAsPercentage, contain, cover)
     //   BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        // new BackgroundImage(image, repeatX, repeatY, position, size)
       // BackgroundImage backgroundImage = new BackgroundImage(image, 
       // 		BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        // new Background(images...)
      //  Background background = new Background(backgroundImage);
        
        Canvas c = new Canvas(400,400);
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 300, 300);        
        
        c.setOnMouseMoved(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, 400, 400);  
        	gc.drawImage(image, me.getX()-10, me.getY()-7,30,30);
        }
        });
        
        
        
       // File pencil = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\pencil.png");        
      //  Image img = new Image(pencil.toURI().toString());
      //  ImageView imgView = new ImageView(img);
       // sp.setBackground(background);
        sp.getChildren().add(c);
        
        //Adding HBox to the scene
        Scene scene = new Scene(sp);
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }    
}



