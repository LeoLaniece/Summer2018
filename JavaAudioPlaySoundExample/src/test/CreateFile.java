package test;

import java.io.BufferedReader;
import java.io.File;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

/**
 * Will create a .rtf file given a file name and the file information
 * @author HCI Lab
 *
 */
public class CreateFile {
	
	//Change the User id here when logging the study tasks of different users
	public int userID =0;
	public String fileInfo;
	
	
	/**
	 * Creates a .rtf file.
	 * takes 2 string, the first as the file info,
	 * the second as the file title.
	 * @param fileInfo
	 * @param fileTitle
	 */
	public CreateFile(String fileInfo, String fileTitle) {
		this.fileInfo =fileInfo;		
		//storage location, need to be changed if i am using my program on another computer
		File parentDir =  new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\logFiles");
		parentDir.mkdir();
		String hash = "User "+userID+" "+fileTitle;		
		File file = createNewFile(parentDir, hash);	
		//if the file already exists, add information to the file
		//else, create new file with the information in it!
		if (file.exists()) {
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
			    Date date = new Date();
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
	

	/**
	 * Will create a file at the given location
	 * 
	 * @param parentDir = location
	 * @param hash = file name
	 * @return
	 */
	public File createNewFile(File parentDir, String hash) {
		String fileName = hash+".rtf";
		File file = new File(parentDir, fileName);	 
		return file;
	}

}
