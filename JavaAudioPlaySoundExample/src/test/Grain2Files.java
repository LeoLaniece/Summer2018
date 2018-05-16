package test;

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
import javax.sound.sampled.Clip;
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
public class Grain2Files implements Runnable {
	
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
    double strokeTime;
    File soundFile;

	
    //take an arraylist of velocities 
    //for each velocity, play the appropriate sound sample
    //so, if the velocity is <5 playSlow ect..
    
    //functions
    //playStroke(ArrayList<Double> velocity)
    //sampleSoundFile(File slowOrFast)??
    //
    
    
    
    
    
	/**
	 * will take 2 WAV files as parameters and will initialize the files to be ready for sampling or playing
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
	public void play2(double sT, double StrokeVelocity) {
		 strokeTime = sT/1000;
		 //slow stroke=slowPencil
		 if (StrokeVelocity < 1) {
			 soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilSlow2.WAV");
		 }else {
			 soundFile = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\pencilFast.WAV");
		 }
		 run();
	}
	public float fileLength(File soundFile) {
	     //calculate the time of the sustained sound file you will be using	      
	      AudioInputStream audioInputStream4;		
			try {
				audioInputStream4 = AudioSystem.getAudioInputStream(soundFile);
						AudioFormat format = audioInputStream4.getFormat();
		      long audioFileLength = soundFile.length();
		      int frameSize = format.getFrameSize();
		      float frameRate = format.getFrameRate();
		      return (audioFileLength / (frameSize * frameRate));
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			return 0;		
	}
	//run() is my attempt at integrating a threading system, does not work yet
			@Override
			public void run() {										 
		SourceDataLine soundLine = null;      
	      //this is the size of the grains!!!
	      int BUFFER_SIZE = 4;
	      
	      double totalCount =0;
	      double file1Count =1;
	      double percent = 0.5;
	      
	     //calculate the time of the sustained sound file you will be using	    	      
		 float durationInSeconds = fileLength(soundFile);
		 
	      //calculate how many files i need to generate to match the strokeLength
		  double loopCount1 = (((strokeTime/durationInSeconds)));
		  int loopCount = (int)loopCount1;
		  if (loopCount == 0) {
			  loopCount = 1;
		  }
		  
	      //set up strokeChange clip
	      File strokeChange = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\strokeChange.WAV");
	      AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(strokeChange);
		
	      // Get a sound clip resource.
	      Clip clip = AudioSystem.getClip();
	      // Open audio clip and load samples from the audio input stream.
	      clip.open(audioIn);
	    //calculate the time of the sound file you will be using     	     	            
		  durationInSeconds = fileLength(strokeChange);
	      
		  
		  //idea:
		  //given an array of velocities, knowing that velocities are calculated every 40ms
		  //figure out when the velocity changes from <5 to >5
		  //sustain one type of stroke for each interval between the velocity changes
		  //functions
		  //play(file, timeInMS), plays a file for the given time in MS
		  
		  //return List<file+timeInMS> determineStrokesAndSustain(List<double> velocities)
		  //will return the type of file and the length for which the file should be played
		  //based on the given list of velocities
		  
		  //playStroke(strokeDuration, List<double> velocities), will play the appropriate sounds
		  //given the stroke duration and the stroke velocities list 
		  
		  //how to make this work using Carl's idea
		  //need one more function which is 
		  //mix(file1,file2,duration,file1%), will mix grains from file1 and file2,
		  //will play the mixed sound for the given duration and will increment the mixing base on the 
		  //% wanted of file1 given.
		  //use mix instead of play once it works.
		  
		  
		  
		  
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
	    	  soundFiles[i] = soundFile;
	    	  
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
	        	 //number for loop is determined by strokeTime/durationInSeconds
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
	        	 
	        	 //ONLY PLAY AFTER 10SECONDs
	        	 //make it so that the clip plays every stroke change occurrence in the model
	        	 if (System.currentTimeMillis()-time > 10000) {
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
	        	 
	         }
	        	//create the action as an object
	        	 //refer to the action as a thread
	        	 //use t.join method so that drawCommands(also reworked to become action as objects)
	        	 //can execute while the sound is playing back.
	        	 
	        	 //at that point, remove the sleep command because it won't do anything
	        	 //maybe ask Carl if this is a good idea before you implement it?  
                 try {
					Thread.sleep(0);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	         soundLine.drain();
	         soundLine.close();
		}  catch (UnsupportedAudioFileException | IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (LineUnavailableException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
	}
			
 //return List<file+timeInMS> determineStrokesAndSustain(List<double> velocities)
  //will return the type of file and the length for which the file should be played
  //based on the given list of velocities
	public ArrayList<FileAndDuration> determineStrokesAndSustain(File file1, File file2, long duration, ArrayList<Double> velocities){
		ArrayList<FileAndDuration> filesAndDurations = new ArrayList<>();
		double increment = duration/velocities.size();		
		int incrementCount =0;
		//0 = <5
		//1 = >5
		int currentV = 0;
		//for all v,
		//while v >5 and has been >5 increment count
		//while v< 5 and has been <5 increment count
		//when v changes from <5 to >5, add file1 for incrementCount to the list
		//when v changes from >5 to <5, add file2 and incrementCount to the list
		for (int i = 0; i<velocities.size();i++) {
			
			if (velocities.get(i) < 5 && currentV == 0) {
				incrementCount++;				
			}					
			if (velocities.get(i) > 5 && currentV == 1) {
				incrementCount++;				
			}
			if (currentV == 0 && velocities.get(i) > 5) {
				//not <5 so change 
				filesAndDurations.add(new FileAndDuration(file1, (incrementCount*increment)));
				incrementCount = 0;	
				currentV =1;
			}
			if (currentV ==1 && velocities.get(i) < 5) {
				//not >5 so change 
				filesAndDurations.add(new FileAndDuration(file2, (incrementCount*increment)));
				incrementCount = 0;	
				currentV =0;
			}																	
		}								
		return filesAndDurations;
	}
			
	/**
	 * plays a file for the given time in MS
	 * @param soundFile
	 * @param strokeDuration in seconds
	 */
	public void playFor(File soundFile, double strokeDuration) {
		SourceDataLine soundLine = null;      
	      //this is the size of the grains!!!
	      int BUFFER_SIZE = 4;
	      
	      double totalCount =0;
	      double file1Count =1;
	      double percent = 0.5;
	      
	     //calculate the time of the sustained sound file you will be using	    	      
		 float durationInSeconds = fileLength(soundFile);
		 System.out.println("durationInSeconds "+durationInSeconds); 
		 
	      //calculate how many files i need to generate to match the strokeLength
		  double loopCount1 = (((strokeDuration/durationInSeconds)));
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
	    	  soundFiles[i] = soundFile;
	    	  
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
	        	 //number for loop is determined by strokeTime/durationInSeconds
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
	