package javaServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.shape.Path;
import test.Draw2Controller;
import test.Draw2Model;

public class ServerListener extends ClientListener {

	public ServerListener(Draw2Model m, Draw2Controller c, DataOutputStream o) {
		super(m, c, o);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void modelChanged() {
		//send the serialized modelPathsArray to the server
		if (controller.state ==controller.READY) {								
			try {
				//out.writeObject("Passed at least a string!!!");
				out.writeUTF("Server model changed!");// + client.getLocalSocketAddress());
				//is the path Alive
				if (model.path == null) {
					out.writeUTF("false");
				}else {
					out.writeUTF("true");
				}
				
				//sending the line over			
				out.writeUTF(Double.toString(model.currentPathCoordinate.x+(model.iModel.viewPortX*7)));
				out.writeUTF(Double.toString(model.currentPathCoordinate.y+(model.iModel.viewPortY*7)));	
				
				System.out.println("x = "+model.currentPathCoordinate.x+(model.iModel.viewPortX*7)+" y = "+model.currentPathCoordinate.y+(model.iModel.viewPortY*7));
				
				//sending the colour over
				out.writeUTF(model.sampleLine.getStroke().toString());
				//send the strokeWidth over
				out.writeUTF(Double.toString(model.sampleLine.getStrokeWidth()));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
}
}
