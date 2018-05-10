package test;

import javax.sound.sampled.LineUnavailableException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

public class Draw2Controller {
	
	
	
	/*	
	btnClear.setOnAction(new EventHandler<ActionEvent>() {

        public void handle(ActionEvent event) {
            lineGroup.getChildren().removeAll(lineGroup.getChildren());                
            //play eraser sound
           clip2.start();
            clip2.setMicrosecondPosition(0);                              
        }
    });
*/
	/*
	 * canvas.setOnMousePressed(new EventHandler<MouseEvent>()  {

            @Override

            public void handle(MouseEvent me) {

            	//will be useful in mouseDragged for velocity
            	x = me.getX();
            	y = me.getY();
            	time = System.currentTimeMillis();

                path = new Path();

                path.setMouseTransparent(true);

                path.setStrokeWidth(sampleLine.getStrokeWidth());

                path.setStroke(sampleLine.getStroke());

                lineGroup.getChildren().add(path);

                path.getElements().add(new MoveTo(me.getSceneX(), me.getSceneY()));
                
                //play drawing sound
            	//clip.loop(Clip.LOOP_CONTINUOUSLY);
                
			
                
                

            }

        });


        //in the controller
        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override

            public void handle(MouseEvent me) {
            	
            	//stop the sound
            	 clip.stop();
            	 clip2.stop();
			

                path = null;



            }

        });


        //int the controller
        canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

            @Override

            public void handle(MouseEvent me) {
            	
            	//CALCULATE DRAG VELOCITY
            	//every 100 ms calculate a curent mouse x mouse y
            	//also figure out distance from previous x and y
            	//if the distance is bigger than 300pixels, play a 'fast' sound
            	//else play slow sound
            	double distance = 0;
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
            	

                // keep lines within rectangle

                if (canvas.getBoundsInLocal().contains(me.getX(), me.getY())) {

                    path.getElements().add(new LineTo(me.getSceneX(), me.getSceneY()));

                }



            }

        });
	 */
}
