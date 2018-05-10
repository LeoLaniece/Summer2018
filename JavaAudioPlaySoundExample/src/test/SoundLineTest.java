package test;

import java.io.*;
import javax.sound.sampled.*;
/**
 * Use SourceDataLine to read line-by-line from the external sound file.
 * For computer game, use Clip to pre-load short-duration sound files.
 * 
 * make this into a workable class, integrate it into the Draw2 program.
 */
public class SoundLineTest {
   public static void main(String[] args) {
      SourceDataLine soundLine = null;      
      //this is the size of the grains!!!
      int BUFFER_SIZE = 400;
      double totalCount =0;
      double file1Count =1;
      double percent = 0.5;
   
      // Set up an audio input stream piped from the sound file.
      try {
         File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
         File soundFile2 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
         //add a buffer to both sound files so they can be marked!
         InputStream inputStream = new FileInputStream(soundFile);
         InputStream inputStream2 = new FileInputStream(soundFile2);
         InputStream bufferedIn = new BufferedInputStream(inputStream);
         InputStream bufferedIn2 = new BufferedInputStream(inputStream2);
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
         AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(bufferedIn2);

         //mark the beggining of each sound file
         audioInputStream.mark(Integer.MAX_VALUE);
         audioInputStream2.mark(Integer.MAX_VALUE);    	 

         AudioFormat audioFormat = audioInputStream.getFormat();
         AudioFormat audioFormat2 = audioInputStream2.getFormat();         
         DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
         DataLine.Info info2 = new DataLine.Info(SourceDataLine.class, audioFormat2);
         soundLine = (SourceDataLine) AudioSystem.getLine(info);

         soundLine.open(audioFormat);

         soundLine.start();

         
         int nBytesRead = 0;
        //Handles section by section of an audio file
         byte[] sampledData = new byte[BUFFER_SIZE];         
         int gate = 0; 

         //want to elongate the sound of a single file. Need to load the same sound bytes into the buffer twice.
         //can load a duplicate of the same file and have them load 50% each. Works :)
         while (nBytesRead >-1) {        	                 	        	       	 
        	 
        	 if (gate == 0) {        		 
        	nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
        	
        	if (nBytesRead ==-1) {
        	//resets the sound files to the beggining
               audioInputStream.reset();
               nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
        	}
                        
        	 }
        	 else {
        		 nBytesRead = audioInputStream2.read(sampledData, 0, sampledData.length); 
        		 
        		 if (nBytesRead ==-1) {
        	        	//resets the sound files to the beggining
        	               audioInputStream2.reset();
        	               nBytesRead = audioInputStream2.read(sampledData, 0, sampledData.length);
        	        	}
        		 
        	 }        	           
            
            if (nBytesRead >= 0) {
               // Writes audio data to the mixer via this source data line.          	            	
               soundLine.write(sampledData, 0, nBytesRead);               

               totalCount += 1;  
               //System.out.println("totalCount = " +totalCount);
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
      } catch (UnsupportedAudioFileException ex) {
         ex.printStackTrace();
      } catch (IOException ex) {
         ex.printStackTrace();
      } catch (LineUnavailableException ex) {
         ex.printStackTrace();
      } finally {
         soundLine.drain();
         soundLine.close();
      }
   }
}
