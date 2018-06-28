package unusedClasses;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import test.Grain2Files;
import test.drawPath;

public class PlayPathSound extends Thread {
	
	Grain2Files player;
	ArrayList<Double> velocities;
	double duration;
	drawPath drawPathThread;
	
	public PlayPathSound(Grain2Files p, ArrayList<Double> v, double d, drawPath dP) {
		//also needs the draw thread?
		player=p;
		velocities=v;
		duration=d/1000;
		drawPathThread = dP;
		run();
	}

	@Override
	public void run() {				
		File f = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");						
		//want to play file while drawing
		//so play method is 'interruptable' 
		//if it becomes interrupted, share the processor with the drawing action. 
		
		/**
		 * plays a file for the given time in MS
		 * @param soundFile
		 * @param strokeDuration in seconds
		 */		
			SourceDataLine soundLine = null;      
		      //this is the size of the grains!!!
		      int BUFFER_SIZE = 4;
		      
		      double totalCount =0;
		      double file1Count =1;
		      double percent = 0.5;
		      
		     //calculate the time of the sustained sound file you will be using	    	      
			 float durationInSeconds = player.fileLength(f);
			 System.out.println("durationInSeconds "+durationInSeconds); 
			 
		      //calculate how many files i need to generate to match the strokeLength
			  double loopCount1 = (((duration/durationInSeconds)));
			  int loopCount = (int)loopCount1;
			  if (loopCount == 0) {
				  loopCount = 1;
			  }
			  
			//set up files in order to sustain a sound given a loopCount, and a sound file
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
		    	  soundFiles[i] = f;
		    	  
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
		         long time = System.currentTimeMillis();
		         while (nBytesRead >-1) {
		        	 //if user is drawing a path, sleep
		        	 //ask Carl about this? maybe he knows?

					 
					 
		        	 for (int i = 0;i<loopCount;i++) {
		        	 try {
						nBytesRead = audioInputStreams[i].read(sampledData, 0, sampledData.length);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}        	 	        	 	        	 
		        	 // Writes audio data to the mixer via this source data line.
		        	 if (nBytesRead >= 0) {                           	            	
		                 soundLine.write(sampledData, 0, nBytesRead);
		        	 }
		        	 
		        	 
		}}

		        	 soundLine.drain();
			         soundLine.close();			
		}
	}


