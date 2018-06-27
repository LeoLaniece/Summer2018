package test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.application.Application;



import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import test.Grain2Files;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author gutwin
 */
public class MouseTest extends Thread {

    ArrayList<MouseRecord> points;
    ArrayList<Dot> dots;
    int windowSize;
    double maxVelocity;
    MouseView mv;
    Timer timer1, timer2;
    public long time =System.currentTimeMillis();
    public Grain2Files player;
    public double clipStaggerIncrement;
    public double clipDuration;
    
    
    
    
    public void preStop() {
        timer1.cancel();
        timer1.purge();
        timer2.cancel();
        timer2.purge();
    }
    
    public double getVelocity() {
    	return calculateVelocity();
    }

    @Override
  //  public void start(Stage primaryStage) {
  //  }
    
    public void run() {
        mv = new MouseView(1400, 600);
        points = new ArrayList<>();
        windowSize = 8;
        dots = new ArrayList<>();
        maxVelocity = 30.0;

        //StackPane root = new StackPane();
        //root.getChildren().add(mv);

       // Scene scene = new Scene(root);

        //primaryStage.setTitle("Hello World!");
       // primaryStage.setScene(scene);
       // primaryStage.show();
        //leo's addition
		 File f1 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");
		 player = new Grain2Files(f1,f1);
		 clipStaggerIncrement = calculateStaggerIncrement(f1); 
		 clipDuration= player.fileLength(f1)*1000;
        time =System.currentTimeMillis();
        //System.out.println("runin in the velocitycalculator");
        
        
        while (true) {
        	if (System.currentTimeMillis()-time >5) {
        points.add(new MouseRecord(MouseInfo.getPointerInfo().getLocation(), System.currentTimeMillis()));
        dots.add(new Dot(mv.currentX++, calculateVelocity()));
        time = System.currentTimeMillis();
        	}

        }
        

        
        
        /*timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {                    	 
                        points.add(new MouseRecord(MouseInfo.getPointerInfo().getLocation(), System.currentTimeMillis()));
                        dots.add(new Dot(mv.currentX++, calculateVelocity()));
                        if (System.currentTimeMillis()-time > clipStaggerIncrement) {
                        	    AnInteractiveStaggeredThread t = new AnInteractiveStaggeredThread("sound", calculateVelocity(),
                        		0, 180, clipDuration);
                        t.start();
                        time = System.currentTimeMillis();
                        }

                    }
                });
            }
        }, 1000, 10);
        timer2 = new java.util.Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                       // mv.draw();
                    }
                });
            }
        }, 1000, 100);*/
    }

    private double calculateVelocity() {
        int i;
        double distance;
        double total = 0;
        double elapsed = 0;
        MouseRecord previous, current;
        if (points.size() > windowSize + 1) {
            previous = points.get(points.size() - windowSize - 1);
            for (i = points.size() - windowSize; i < points.size(); i++) {
                current = points.get(i);
                distance = dist(previous.x, previous.y, current.x, current.y);
                total += distance;
                elapsed += current.time - previous.time;
                previous = current;
            }
        }
        return total / elapsed;
    }

    private double dist(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    class MouseRecord {

        int x;
        int y;
        long time;

        public MouseRecord(Point p, long now) {
            x = p.x;
            y = p.y;
            time = now;
        }
    }

    class Dot {

        int x;
        int y;
        double velocity;

        public Dot(int newX, double newV) {
            x = newX;
            velocity = newV;
            y = (int) (mv.getHeight() - velocity / maxVelocity * mv.getHeight() - 2);
        }
    }

    class MouseView extends Pane {

        Canvas mCanvas;
        GraphicsContext gc;
        int currentX;
        int prevX, prevY;

        public MouseView(double width, double height) {
            mCanvas = new Canvas(width, height);
            gc = mCanvas.getGraphicsContext2D();
            getChildren().add(mCanvas);
            currentX = 0;
            prevX = 0;
            prevY = (int) height;
        }

        public void draw() {
            // draw current velocities since last draw 
            gc.setStroke(Color.BLACK);
            //dots.forEach(d -> gc.fillRect(d.x, d.y, 2, 2));
            dots.forEach(d -> {
                gc.strokeLine(prevX, prevY, d.x, d.y);
                prevX = d.x;
                prevY = d.y;
            });
            //if (dots.size() > 0) {
            //System.out.println(dots.get(0).x + "," + dots.get(0).y + "         " + dots.get(0).velocity);
            //}
            dots.clear();

            // wrap around
            if (currentX > this.getWidth()) {
                currentX = 0;
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, mCanvas.getWidth(), mCanvas.getHeight());
                prevX = 0;
                prevY = (int) mCanvas.getHeight();
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      //  launch(args);
    }
	   /**
	    * will calculate the stagger increment for a submitted file 
	    */
	   public double calculateStaggerIncrement(File f) {
		   double clipDuration = player.fileLength(f)*1000;
			double clipOverlapDuration = 0.8;//0.865;
			return (clipDuration)-(clipDuration*clipOverlapDuration);			
	   }

}
