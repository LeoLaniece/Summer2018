package threadDemo;

import java.io.File;
import java.lang.reflect.Field;
import javafx.application.Platform;

import javafx.scene.shape.LineTo; 

import java.lang.reflect.Modifier;
import javafx.scene.layout.BackgroundImage ;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BackgroundSize ;
import javafx.scene.layout.Background; 
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;

public class LoadImage extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Load Image");

        StackPane sp = new StackPane();
     // new Image(url)
        File pencil = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\images\\pencil.png");        
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
        gc.drawImage(image, 0, 0);
        
        
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
