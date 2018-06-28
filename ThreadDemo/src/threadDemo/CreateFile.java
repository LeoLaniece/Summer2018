package threadDemo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFile {
	
	public int fileCount;
	public String fileInfo;
	
	public CreateFile(String fileInfo) {
		this.fileInfo =fileInfo;
		fileCount = 0;		
		File parentDir = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\logFiles");
		parentDir.mkdir();
		String hash = "AudioAwarenessLog";		
		File file = createNewFile(parentDir, hash);		
		if (file.exists()) {
		while (file.exists()){
			  fileCount+=1;		  			  
			  file = createNewFile(parentDir, hash);			  
		  }
		}
		try {
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			writer.write(fileInfo);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public static void main(String[] args) {
	//	CreateFile x = new CreateFile("LOG DATA TEST");
	//}
	
	public File createNewFile(File parentDir, String hash) {
		String fileName = hash+fileCount+".txt";
		File file = new File(parentDir, fileName);	 
		return file;
	}

}
