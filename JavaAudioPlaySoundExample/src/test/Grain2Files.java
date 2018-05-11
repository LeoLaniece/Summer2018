package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
	 * will take the duration of the stroke in milliseconds as a parameter
	 * will take the velocity of the stroke as a parameter.
	 * should not 'loop' the sound, this needs to be determined by the required interaction
	 * @throws LineUnavailableException 
	 */
	//how to make this function sound smooth
	public void play(double duration, double velocity) throws LineUnavailableException {		
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
	
	/**
	 * will play a sound based on the duration of the stroke
	 * find a way to drop the pitch when the loopCount is higher than 4?
	 * @param strokeTime
	 */
	public void play2(double strokeTime) {
		SourceDataLine soundLine = null;      
	      //this is the size of the grains!!!
	      int BUFFER_SIZE = 400;
	      double totalCount =0;
	      double file1Count =1;
	      double percent = 0.5;
	      
	      //calculate the time of the sound file you will be using
	      File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
	      AudioInputStream audioInputStream4;
		try {
			audioInputStream4 = AudioSystem.getAudioInputStream(soundFile);
		      

		AudioFormat format = audioInputStream4.getFormat();
		      long audioFileLength = soundFile.length();
		      int frameSize = format.getFrameSize();
		      float frameRate = format.getFrameRate();
		      float durationInSeconds = (audioFileLength / (frameSize * frameRate));
		      System.out.println("duration in seconds = " +durationInSeconds);
	      //calculate how many files i need to generate to match the strokeLength
		  int loopCount = Math.round(((int) (strokeTime/durationInSeconds)));
		  
		  if (loopCount == 0) {
			  loopCount = 1;
		  }
		  System.out.println("loopCount = "+loopCount);
	      	
	      //array of sound files
	      File[] soundFiles = new File[loopCount];
	      //array of InputStreams
	      InputStream[] inputStreams = new InputStream[loopCount];
	      //array of BufferedInputStreams
	      InputStream[] bufferedIns = new BufferedInputStream[loopCount];
	      //array of audioInputStreams
	      AudioInputStream[] audioInputStreams = new AudioInputStream[loopCount];
	      
	      //initialize all the arrays
	      for (int i = 0;i<loopCount;i++) {
	    	  soundFiles[i] = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
	    	  
	    	  try {    	  inputStreams[i] = new FileInputStream(soundFiles[i]);
	    	  }catch (FileNotFoundException e){}
	    	  
	    	  bufferedIns[i] = new BufferedInputStream(inputStreams[i]);
	    	  
	    	  try {			audioInputStreams[i] = AudioSystem.getAudioInputStream(bufferedIns[i]);
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();}		    	      	  
	      }
	      
	      //initialize the source data line
	      AudioFormat audioFormat = audioInputStreams[0].getFormat();          
	      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);           
	      try {
			soundLine = (SourceDataLine) AudioSystem.getLine(info);
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      try {
			soundLine.open(audioFormat);
		} catch (LineUnavailableException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	      soundLine.start();        	      
	      
	         int nBytesRead = 0;
	        //Handles section by section of an audio file
	         byte[] sampledData = new byte[BUFFER_SIZE];         
	         int gate = 0; 

	         //want to elongate the sound of a single file. Need to load the same sound bytes into the buffer twice.
	         //can load a duplicate of the same file and have them load 50% each. Works :)
	         while (nBytesRead >-1) {
	        	 //number for loop is determined by strokeTime/durationInSeconds
	        	 for (int i = 0;i<loopCount;i++) {
	        	 try {
					nBytesRead = audioInputStreams[i].read(sampledData, 0, sampledData.length);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	 
	        	 
	        	 System.out.println("nBytesRead = "+nBytesRead);
	        	 // Writes audio data to the mixer via this source data line.
	        	 if (nBytesRead >= 0) {                           	            	
	                 soundLine.write(sampledData, 0, nBytesRead);
	        	 }
	         }
	         }
	         soundLine.drain();
	         soundLine.close();
		} catch (UnsupportedAudioFileException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
	}
	
	
	
	
}
