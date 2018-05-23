package javaServer;

import java.io.Serializable;
import javafx.scene.canvas.Canvas;
public class CanvasAddress implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public transient Canvas c;
	public CanvasAddress(Canvas c) {
		this.c = c;
	}

}
