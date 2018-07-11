package test;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class CreateFile {
	
	public int fileCount;
	public String fileInfo;
	public int userID =0;
	
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
		File parentDir =  new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\logFiles");
		parentDir.mkdir();
		String hash = "User "+userID+" "+fileTitle;		
		File file = createNewFile(parentDir, hash);		
		if (file.exists()) {
		//while (file.exists()){
		//	  fileCount+=1;		  			  
			  //file = createNewFile(parentDir, hash);			  
		 // }
			try {
			FileReader reader = new FileReader(file);
			try(BufferedReader br = new BufferedReader(reader)) {
			    StringBuilder sb = new StringBuilder();
			    String line = br.readLine();
			    while (line != null) {
			        sb.append(line);
			        sb.append(System.lineSeparator());
			        line = br.readLine();
			    }
			    String everything = sb.toString();			    
			    //System.out.println("already in file :\n"+everything);
				//file.createNewFile(); 
			    Date date = new Date();
		        // display time and date using toString()
		        fileInfo = date.toString()+"\n"+fileInfo;
			    fileInfo = everything+fileInfo;
				FileWriter writer = new FileWriter(file);
				writer.write(fileInfo);				
				writer.close();
			}						

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
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
	}
	

	
	public File createNewFile(File parentDir, String hash) {
		String fileName = hash+".rtf";
		File file = new File(parentDir, fileName);	 
		return file;
	}

}
