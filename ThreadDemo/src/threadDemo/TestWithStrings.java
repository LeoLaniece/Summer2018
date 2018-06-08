package threadDemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class TestWithStrings {
	


public static void main(String[] args) {
	String msg1 = Double.toString(0.0001);
	String msg2 = "000.01 hey!";
	String fullmsg = msg1+"\n"+msg2+"\n";
	
	System.out.println(fullmsg);
	String line;
	BufferedReader reader = new BufferedReader(new StringReader(fullmsg));
			
	try {
		while ((line = reader.readLine()) != null) {
		    System.out.println(line+" xxxasd");		    
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
