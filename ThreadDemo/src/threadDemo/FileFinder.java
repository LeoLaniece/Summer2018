package threadDemo;

import java.io.File;

public class FileFinder {

	
	public static void main(String[] args) {
		File file = new File("C:\\soundAndImageFiles\\chalk.png");
		
		System.out.println("Path : " + file.getAbsolutePath());

	}
}
