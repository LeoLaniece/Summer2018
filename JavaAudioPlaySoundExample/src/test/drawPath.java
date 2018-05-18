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
	ArrayList<Path> paths;
	double x;
	double y;
	
	public drawPath(ArrayList<Path> modelPaths, double X, double Y) {
		paths = modelPaths;
		x = X;
		y = Y;
		run();
	}

	@Override
	public void run() {
		paths.get(paths.size()-1).getElements().add(new LineTo(x,y));		
	}

}
