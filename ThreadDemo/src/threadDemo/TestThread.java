package threadDemo;
import java.lang.reflect.Field;






import java.lang.reflect.Modifier;

import javafx.application.Application;

import javafx.event.ActionEvent;

import javafx.event.EventHandler;

import javafx.geometry.Pos;

import javafx.scene.Cursor;

import javafx.scene.Group;

import javafx.scene.Scene;

import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.Slider;

import javafx.scene.control.Tooltip;

import javafx.scene.input.MouseEvent;

import javafx.scene.layout.FlowPane;

import javafx.scene.layout.HBox;

import javafx.scene.layout.StackPane;

import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.scene.shape.Line;

import javafx.scene.shape.LineTo;

import javafx.scene.shape.MoveTo;

import javafx.scene.shape.Path;

import javafx.scene.shape.Rectangle;

import javafx.stage.Stage;
//Leo's imports
import sun.audio.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.*;

import javax.management.timer.Timer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
public class TestThread extends Application{

	   public static void main(String args[]) {
		   Application.launch(args);
	   }

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		Canvas c = new Canvas(600, 600);
		GraphicsContext gc = c.getGraphicsContext2D();
		Scene scene = new Scene(root, 600,600);
		root.getChildren().add(c);
		gc.setFill(Color.WHITE);
		gc.fillRect(0, 0, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		//this is the setup i need to run 2 threads simultaneously
		//make it so that when the mouse is clicked, play a sound. 
		
		  ThreadDemo T1 = new ThreadDemo( "Thread-1", gc, c);
	      T1.start();
	      //System.out.println("Starting ");
	    //  ThreadDemo2 T2 = new ThreadDemo2("Thread-2");
	    //  T2.start();
	}   
	}
