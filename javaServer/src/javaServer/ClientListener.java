package javaServer;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.scene.shape.Path;
import test.*;
public class ClientListener implements modelListener{

	public Draw2Model model;
	public DataOutputStream out;
	
	public ClientListener(Draw2Model m, DataOutputStream o) {
		model = m;
		out =o;
		model.addSubscriber(this);
	}
	
	@Override
	public void modelChanged() {
		//send the serialized modelPathsArray to the server
		ArrayList<Path> modelPaths = model.modelPaths;
		try {
			//out.writeObject("Passed at least a string!!!");
			out.writeUTF("Hello from the model1!");// + client.getLocalSocketAddress());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
