package threadDemo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class CreateFile {
	
	public int fileCount;
	public String fileInfo;
	
	public CreateFile(String fileInfo) {
		
		//progress
		//need to figure out how to tell if we are logging from the same task!
		
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
	        Date date = new Date();
	        // display time and date using toString()
	        fileInfo = date.toString()+"\n"+fileInfo;
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
