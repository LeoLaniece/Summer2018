package test;

import java.io.*;
import javax.sound.sampled.*;
/**
 * Use SourceDataLine to read line-by-line from the external sound file.
 * For computer game, use Clip to pre-load short-duration sound files.
 * make this into a workable class, integrate it into the Draw2 program.
 */

//how do i make the sustained audio smoother?
//new idea, for interactive sounds, provide a lag.
//calculate length and speed of stroke, then generate the sound.



public class SoundLineTest {
   public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
      SourceDataLine soundLine = null;      
      //this is the size of the grains!!!
      //keeping the buffer size small eliminates most of the electric fry, but the pitch drop is huge.
      int BUFFER_SIZE = 4;
      double totalCount =0;
      double file1Count =1;
      double percent = 0.5;
      double strokeTime = 0.6;
      int loopCount =0;
      //long startTime = System.currentTimeMillis();
      
      //set up a clip and try to get it to play at the given times
      //can do!
      //have your times and check them in the while loop  
      //when the time matches, play the clip.
      
      // audio for the scratch
      File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\strokeChange.WAV");
      AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
      // Get a sound clip resource.
      Clip clip = AudioSystem.getClip();
      // Open audio clip and load samples from the audio input stream.
      clip.open(audioIn);
      //clip.loop(clip.LOOP_CONTINUOUSLY);
      //calculate the time of the sound file you will be using      
      AudioInputStream audioInputStream4;
      audioInputStream4 = AudioSystem.getAudioInputStream(soundFile);	      
	AudioFormat format = audioInputStream4.getFormat();
	      long audioFileLength = soundFile.length();
	      int frameSize = format.getFrameSize();
	      float frameRate = format.getFrameRate();
	      float durationInSeconds = (audioFileLength / (frameSize * frameRate));
      


      loopCount = 1;	
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
    	  soundFiles[i] = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\strokeChange.WAV");
    	  
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
          audioInputStreams[i].mark(Integer.MAX_VALUE);
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
      
   /*
      // Set up an audio input stream piped from the sound file.
      try {
         File soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
         File soundFile2 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
         File soundFile3 = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow.WAV");
         //test to find length of audio file
         AudioInputStream audioInputStream4 = AudioSystem.getAudioInputStream(soundFile);
         AudioFormat format = audioInputStream4.getFormat();
         long audioFileLength = soundFile.length();
         int frameSize = format.getFrameSize();
         float frameRate = format.getFrameRate();
         float durationInSeconds = (audioFileLength / (frameSize * frameRate));
         System.out.println("duration in seconds = " +durationInSeconds);
         
         
         //add a buffer to both sound files so they can be marked!
         InputStream inputStream = new FileInputStream(soundFile);
         InputStream inputStream2 = new FileInputStream(soundFile2);
         InputStream inputStream3 = new FileInputStream(soundFile3);
         InputStream bufferedIn = new BufferedInputStream(inputStream);
         InputStream bufferedIn2 = new BufferedInputStream(inputStream2);
         InputStream bufferedIn3 = new BufferedInputStream(inputStream3);
         //just need to create multiple audio input streams to extend the sound
         
         AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedIn);
         AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(bufferedIn2);
         AudioInputStream audioInputStream3 = AudioSystem.getAudioInputStream(bufferedIn3);

         //mark the beggining of each sound file
         audioInputStream.mark(Integer.MAX_VALUE);
         audioInputStream2.mark(Integer.MAX_VALUE);    	
         audioInputStream3.mark(Integer.MAX_VALUE);

         AudioFormat audioFormat = audioInputStream.getFormat();
         AudioFormat audioFormat2 = audioInputStream2.getFormat();         
         DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
         DataLine.Info info2 = new DataLine.Info(SourceDataLine.class, audioFormat2);
         
         soundLine = (SourceDataLine) AudioSystem.getLine(info);
         soundLine.open(audioFormat);
         soundLine.start();
*/
         
         int nBytesRead = 0;
        //Handles section by section of an audio file
         byte[] sampledData = new byte[BUFFER_SIZE];         
         int gate = 0; 

         //want to elongate the sound of a single file. Need to load the same sound bytes into the buffer twice.
         //can load a duplicate of the same file and have them load 50% each. Works :)
         long time = System.currentTimeMillis();
         while (nBytesRead >-1) {
        	 
        	 
        	 //number for loop is determined by strokeTime/durationInSeconds
        	 for (int i = 0;i<loopCount;i++) {
        	 try {
				nBytesRead = audioInputStreams[i].read(sampledData, 0, sampledData.length);
	        	 if (nBytesRead ==-1) {
	              	//resets the sound file to the beggining
	                     audioInputStreams[i].reset();
	                     nBytesRead = audioInputStreams[i].read(sampledData, 0, sampledData.length);
	              	}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	 
        	 
        	// System.out.println("nBytesRead = "+nBytesRead);
        	 // Writes audio data to the mixer via this source data line.
        	 if (nBytesRead >= 0) {                           	            	
                 soundLine.write(sampledData, 0, nBytesRead);
        	 }
        	 
        	 //ONLY PLAY AFTER SECOND
        	 if (System.currentTimeMillis()-time > 100) {
        		 System.out.println("playing clip");
   	      //PLAY LOOP FOR EXACTLY THE LENGTH OF THE RECORDING
             clip.loop(clip.LOOP_CONTINUOUSLY);
             clip.start(); 
             long startTime = System.currentTimeMillis();     	      
       	      //have the clip play for the duration in seconds of the sound file
       	      System.out.println("satrt time " +startTime);
       	      System.out.println("current time " +System.currentTimeMillis());
       	      System.out.println("recording duration " +durationInSeconds);
       	      while(System.currentTimeMillis()-startTime < durationInSeconds*1000) {
       	    	  //clip finishes
       	      }
       	      clip.stop();
       	      time = System.currentTimeMillis();
        	 }
        	 
        	 
        	 /*
        	 //space between the files
        	 nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
        	 if (nBytesRead ==-1) {
             	//resets the sound file to the beggining
                    audioInputStream.reset();
                    nBytesRead = audioInputStream.read(sampledData, 0, sampledData.length);
             	}
        	 
        	 if (nBytesRead >= 0) {                           	            	
                 soundLine.write(sampledData, 0, nBytesRead);
        	 }*/
        	 
        	 
        	 
         }
         }

	}
        	 /*
        	 if (gate == 0) {        		 
        		 //audioInputStream3.read(sampledData, 0, sampledData.length);
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
      }*/
	}


