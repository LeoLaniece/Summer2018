package test;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;

import javafx.scene.shape.LineTo;

/**
 * will run the path production process and update the model as it goes along
 * @author HCI Lab
 *
 */
public class drawPath extends Thread{
	Path path;
	double x;
	double y;
	
	public drawPath(Path paths, double X, double Y) {
		path = paths;
		x = X;
		y = Y;
		run();
	}

	@Override
	public void run() {
		path.getElements().add(new LineTo(x,y));		
	}
	
	

}
