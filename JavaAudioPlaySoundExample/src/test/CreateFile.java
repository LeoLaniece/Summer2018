package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFile {
	
	public int fileCount;
	public String fileInfo;
	
	/**
	 * creates a rtf file.
	 * takes 2 string, the first as the file info,
	 * the second as the file title.
	 * @param fileInfo
	 * @param fileTitle
	 */
	public CreateFile(String fileInfo, String fileTitle) {
		this.fileInfo =fileInfo;
		fileCount = 0;		
		File parentDir = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\logFiles");
		parentDir.mkdir();
		String hash = fileTitle;		
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
	

	
	public File createNewFile(File parentDir, String hash) {
		String fileName = hash+fileCount+".rtf";
		File file = new File(parentDir, fileName);	 
		return file;
	}

}
