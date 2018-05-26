package javaServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.shape.Path;
import test.Draw2Model;

public class ServerListener extends ClientListener {

	public ServerListener(Draw2Model m, DataOutputStream o) {
		super(m, o);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void modelChanged() {
		//send the serialized modelPathsArray to the server
		ArrayList<Path> modelPaths = model.modelPaths;
		try {
			//out.writeObject("Passed at least a string!!!");
			out.writeUTF("Server model changed!");
			out.writeUTF(Integer.toString(30));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
