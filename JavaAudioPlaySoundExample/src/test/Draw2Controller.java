package test;

import javax.sound.sampled.LineUnavailableException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Draw2Controller {
	public Draw2View view;
	public Draw2Model model;
	double distanceTraveled = 0;
	double x;
	double y;            	
	double dx = 0;
	double dy = 0;
	
	long time;
	
	public Draw2Controller(Draw2View v, Draw2Model m) {
		view = v;
		model = m;
	
	
	view.c.setOnMousePressed(new EventHandler<MouseEvent>()  {        	
        @Override
        public void handle(MouseEvent me) {
        	//will be useful in mouseDragged for velocity
        	x = me.getX();
        	y = me.getY();
        	time = System.currentTimeMillis();        	 
        	view.startPath(me.getX(), me.getY()); 
                        		                        
        }
    });
	
        //in the controller
	view.c.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {            	
            	 //calculate speed and distance of the stroke(in the model)
            	model.calculateStroke(distanceTraveled,time);
            	distanceTraveled = 0;
            	
                
            }
        });
      
	view.c.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {  
            	
            	//distance travelled = distance += |distance|
        		dx = me.getX();
        		dy = me.getY();
            	distanceTraveled += Math.abs(Math.sqrt(Math.pow((dx-x), 2)+Math.pow((dy-y), 2)));
            	x = dx;
            	y = dy;
            	
            	
            	//CALCULATE DRAG VELOCITY
            	//every 100 ms calculate a curent mouse x mouse y
            	//also figure out distance from previous x and y
            	//if the distance is bigger than 300pixels, play a 'fast' sound
            	//else play slow sound
            /*	
            	double dx = 0;
            	double dy = 0;
            	
            	//Works! doesn't quite sound realistic, need to implement the granular synthesis probably
            	if (System.currentTimeMillis() -time >= 100) {
            		time = System.currentTimeMillis();
            		dx = me.getX();
            		dy = me.getY();
            		distance = Math.sqrt(Math.pow((dx-x), 2)+Math.pow((dy-y), 2));
            		x = dx;
            		y = dy;
            		//System.out.println("distance = " +distance);
            		if (distance >= 100) {
            			//clip.start();
                    //clip.setMicrosecondPosition(0);
            		}else {
            			//clip2.start();
                        //clip2.setMicrosecondPosition(0);
            		}
                    
            		
            	}
            	//play sound synthesis
            	try {
					player.play();					
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	*/

                // keep lines within rectangle

                if (view.c.getBoundsInLocal().contains(me.getX(), me.getY())) {
                	view.strokePath(me.getX(), me.getY()); 
                	view.startPath(me.getX(), me.getY());
                }
                }
        });	
}}
