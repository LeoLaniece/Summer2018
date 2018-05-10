package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Takes two files as parameters
 * has a grain frequency count represented as a %% for each file
 * has a granularity value, default = 64*1024, 64MB
 * has a play command 
 * has a change % command 
 * has a change granularity command
 * @author HCI Lab
 * Purpose is to perform interactive drawing sounds with the drawing tool
 *
 */
public class Grain2Files {
	
	File file1;
	File file2;
	double file1Frequency;
	double file2Frequency;

	SourceDataLine soundLine = null; 
	//the audiInputStreams
    AudioInputStream audioInputStream;
    AudioInputStream audioInputStream2;
    //Buffer util
    //this is the size of the grains!!!
	int BUFFER_SIZE = 1000;
    int nBytesRead = 0;
   //Handles section by section of an audio file
    byte[] sampledData = new byte[BUFFER_SIZE];         
    int gate = 0; 
    double file1Count = 1;
    double totalCount = 0;
    double percent =0.5;
    DataLine.Info info;
    AudioFormat audioFormat;

	
	/**
	 * will take 2 WAV files as parameters and will mix the grains of each into a single sound file
	 * @param f1
	 * @param f2
	 */
	public Grain2Files(File f1, File f2) {
		file1 = f1;
		file2 = f2;
		file1Frequency = 0.5;
		file2Frequency = 0.5;	      	      	      	   
	      // Set up an audio input stream piped from the sound file.
	      try {
	        // File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\metalOnWoodSlow.WAV");
	        // File soundFile2 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.WAV");
	         //add a buffer to both sound files so they can be marked!
	         InputStream inputStream = new FileInputStream(file1);
	         InputStream inputStream2 = new FileInputStream(file2);
	         InputStream bufferedIn = new BufferedInputStream(inputStream);
	         InputStream bufferedIn2 = new BufferedInputStream(inputStream2);
	         audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
	         audioInputStream2 = AudioSystem.getAudioInputStream(bufferedIn2);

	         //mark the beggining of each sound file
	         audioInputStream.mark(Integer.MAX_VALUE);
	         audioInputStream2.mark(Integer.MAX_VALUE);    	 

	         audioFormat = audioInputStream.getFormat();
	         AudioFormat audioFormat2 = audioInputStream2.getFormat();         
	         info = new DataLine.Info(SourceDataLine.class, audioFormat);
	         DataLine.Info info2 = new DataLine.Info(SourceDataLine.class, audioFormat2);
	         soundLine = (SourceDataLine) AudioSystem.getLine(info);
	         soundLine.open(audioFormat);
	         soundLine.start();	         				
	}catch (UnsupportedAudioFileException ex) {
        ex.printStackTrace();
     } catch (IOException ex) {
        ex.printStackTrace();
     } catch (LineUnavailableException ex) {
        ex.printStackTrace();
     } finally {
    	 //soundLine.drain();
         //soundLine.close();
    	 }		
	}
	
	/**
	 * plays the mixed grains of both sound files
	 * 
	 * should not 'loop' the sound, this needs to be determined by the required interaction
	 * @throws LineUnavailableException 
	 */
	//how to make this function sound smooth
	public void play() throws LineUnavailableException {		
		try {		     	        	 
			//start a new soundLine every time?
	         soundLine = (SourceDataLine) AudioSystem.getLine(info);
	         soundLine.open(audioFormat);
	         soundLine.start();
	         for (int i = 0; i < 20;i++) {
       	 if (gate == 0) {        		 
       	nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
       	if (nBytesRead ==-1) {
       	//resets the sound files to the beggining
              audioInputStream.reset();
              nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
       	}
           //System.out.println(nBytesRead);            
       	 }
       	 else {
       		 nBytesRead = audioInputStream2.read(sampledData, 0, sampledData.length); 
       		 if (nBytesRead ==-1) {
       	        	//resets the sound files to the beggining
       	               audioInputStream2.reset();
       	               nBytesRead = audioInputStream2.read(sampledData, 0, sampledData.length);
       	        	}
       		 //System.out.println(nBytesRead);
       	 }        	           
           
           if (nBytesRead >= 0) {
              // Writes audio data to the mixer via this source data line.          	            	
              soundLine.write(sampledData, 0, nBytesRead);
              
              //changes the file which is read into the inputStream
              //based on the value of gate
              //if percent = 0.5, there should be 50% of each file in the byte array
              //if percent = 0.1, there should be 10% of the first file and 90% of the second file.
              
              //if percent = 1.0, there should be 100% of the first file in the byte array
              
              //0 = file 1
              //1 = file 2
              //need to work this out.
              //percent == how often should file 1 be buffered into the byte array?
              //find total amount of stuff in the byte array
              //divide this by the number of grains of file 1
              //if this amount is less than the percentage, keep adding file 1
              //if the amount is more than the percentage, add file 2.
              totalCount += 1;               
         	  if (gate ==0) {
        		  gate = 1;
        	  }else {
        		  gate = 0;
        	  }             	
              if (file1Count/totalCount <= percent) {
            	  gate = 0;
              }else {
           	   gate = 1;
              }
              
              if (gate == 0) {
            	  file1Count += 1;            	   
              }
              
           }
	         }
      //  }        
	}catch (IOException ex) {
        ex.printStackTrace();
     }//catch (LineUnavailableException e) {     }
		finally {
    	 soundLine.drain();    	     	 
         soundLine.close();
    	 }
		
	}
	/**
	 * will change the frequency of occurrence of file1.
	 * @param percentage
	 */
	public void changeFrequency(double percentage) {
		percent = percentage;
	}
	
	
	
	
}
