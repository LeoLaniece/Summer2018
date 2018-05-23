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
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
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
	int BUFFER_SIZE = 100;
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
		  //if the loop count is 0, don't play anything
		  if (loopCount > 0) {
			  
		  
		  
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
		  
		  //current sound 'sounds like' too many strokes
		  //fix this with Carl's mixing idea
		  //given the total time of the stroke, two sound files and the velocities 
		  //mix more or less of the files if they are close to certain velocities
		  //probably want a 'silence' factor mixed into this.
		  
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
	}
			

	/**
	 *  will return a list of files and the length for which the file should be played
 	 *  based on the given list of velocities
	 * @param file1 slower sound
	 * @param file2 faster sound
	 * @param duration
	 * @param velocities
	 * @return
	 */
	public ArrayList<FileAndDuration> determineStrokesAndSustain(File file1, File file2, long duration, ArrayList<Double> velocities){
		ArrayList<FileAndDuration> filesAndDurations = new ArrayList<>();
		//increment is the amount of time for which each velocity is responsible
		double increment = duration/velocities.size();		
		System.out.println("duration "+duration);
		System.out.println("velocities.size() "+velocities.size());
		System.out.println("increment "+increment);
		int incrementCount =1;
		int velocityChange = 5;
		//0 = <5
		//1 = >5
		int currentV = 0;
		//initialize currentV
		if (velocities.get(0) < 5) {
			currentV = 0;				
		}else {
			currentV = 1;
		}
		//for all v,
		//while v >5 and has been >5 increment count
		//while v< 5 and has been <5 increment count
		//when v changes from <5 to >5, add file1 for incrementCount to the list
		//when v changes from >5 to <5, add file2 and incrementCount to the list
		for (int i = 0; i<velocities.size();i++) {			
			if (velocities.get(i) < velocityChange && currentV == 0) {
				incrementCount++;				
			}					
			if (velocities.get(i) > velocityChange && currentV == 1) {
				incrementCount++;				
			}
			if (currentV == 0 && velocities.get(i) > velocityChange) {
				//not <5 so change 
				filesAndDurations.add(new FileAndDuration(file1, (incrementCount*increment)));
				incrementCount = 0;	
				currentV =1;
			}
			if (currentV ==1 && velocities.get(i) < velocityChange) {
				//not >5 so change 
				filesAndDurations.add(new FileAndDuration(file2, (incrementCount*increment)));
				incrementCount = 0;	
				currentV =0;
			}
			//when the last velocity is reached, make last segment
			if (i == velocities.size()-1) {
				
				if (currentV ==0) {
					filesAndDurations.add(new FileAndDuration(file1, (incrementCount*increment)));
				}else {
					filesAndDurations.add(new FileAndDuration(file2, (incrementCount*increment)));
				}
			}
			
		}								
		return filesAndDurations;
	}
			
	/**
	 * plays a file for the given time in MS
	 * will also pan the sound based on the given pan-value array.
	 * @param soundFile
	 * @param strokeDuration in seconds
	 * @param panValues array of floats between -1 and 1
	 */
	public void playFor(File soundFile, double strokeDuration, ArrayList<Float> panValues) {
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
	      
	    //prepare the percentage increment
		     long totalFramesRead = 0;
		     for (int i =0;i<loopCount;i++) {
		    	 totalFramesRead += audioInputStreams[i].getFrameLength();
		     }	      
	      //calculate when to change the pan value
	      //change at regular intervals in time, so time/panValues.size()
	      //better to use the total frames method
		    // System.out.println("total frame length = "+totalFramesRead);
		     int predictedBytesRead = (int)totalFramesRead*4;
		     if (panValues.size() == 0) {
		    	 panValues.add((float) 0.0);
		     }
		     int bytesPerPanValue = predictedBytesRead/panValues.size();
		     int panValuesIncrement =0;

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
	      //controls for the sourceDataLine	 
	      //FIGURED OUT HOW TO PAN THE SOUND!!!!
	      FloatControl volCtrl;
	      Mixer mixer = AudioSystem.getMixer(null);
	      
	      volCtrl = (FloatControl) soundLine.getControl(FloatControl.Type.PAN);	    	      	      	      	      	            	      
	      volCtrl.setValue(panValues.get(0));
	      soundLine.start();
	      
	      int bytesCount = 0;
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
	        		 bytesCount += nBytesRead;
	        		 volCtrl.setValue(panValues.get(panValuesIncrement));
	                 soundLine.write(sampledData, 0, nBytesRead);
	        	 }
	        	 
	         	  //increment the percentage if increment is reached
	         	  //use the frame size to know where we are in the recording.
	         	  //would be totalBytesRead or something
	         	  if (bytesCount > bytesPerPanValue) {
	         		  bytesCount = 0;
	         		  panValuesIncrement ++;
	         	  }
	        	 
	        	 
	        	 
	}}
	     soundLine.drain();
		 soundLine.close();
	}

	/**
	 * will mix grains from file1 and file2,
	   will play the mixed sounds and will increment the mixing base on the 
	  % wanted of file1 given.
	  will sustain the mixed sound for the value of duration(in milliseconds)
	 * @param file1
	 * @param file2
	 * @param file1Percent
	 * @param duration - in milliseconds
	 */
	public void playMixFor(File file1,File file2, double file1Percent, double duration) {
		//trying to stretch the strokes based on the percentages !!!
		//this would be a different kind of mixing
		//not by grain, instead stretch the files so that they match the duration
		//then mix by grain (no file is played in full more than once)
		//functions:  given 2 files a duration and a percentage play without repeating a file more than once
		//need to 'stretch' 2 files and mix them smoothly
		//functions:
		
		//stretch the given file, return the audioInputStreams[] and loopCount(can do this separate?)
		//mix the given stretched files, ->edit mixFor to take in audioInputStream[]? 
		
		//given the audioInputStreams[], and the loop count mix the files so that they play for the given duration
		//start just mixing the stretched files 50% each.
		
		//play 
		
		
		//prepare the buffers for the 2 sound files
		try {
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
		
		//now mix the soundFiles
		try {
	      // this determines how long the sound is sustained for. make this last for the stroke duration.
			long startTime = System.currentTimeMillis();
			//duration/4 because it takes time for the system to load the buffer and then to play the buffer
		    while (System.currentTimeMillis()-startTime < duration/4) {
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
	              
	              //changes the file which is read into the inputStream
	              totalCount += 1;               
	         	  if (gate ==0) {
	        		  gate = 1;
	        	  }else {
	        		  gate = 0;
	        	  }             	
	              if (file1Count/totalCount <= file1Percent) {
	            	  gate = 0;
	              }else {
	           	   gate = 1;
	              }
	              
	              if (gate == 0) {
	            	  file1Count += 1;            	   
	              }
	              
	           }
		         }
	           
		}catch (IOException ex) {
	        ex.printStackTrace();
	     }//catch (LineUnavailableException e) {     }
			finally {
	    	 soundLine.drain();    	     	 
	         soundLine.close();
	    	 }				
	}
	/**
	 * will take an array of velocities and return an array of the % of the file1 needed to play appropriate mix
	 * mix more or less of the files if they are close to certain velocities
	//if the file is at 1.0 velocity play slow sound
	//if the file is at 0 velocity play silence
	//if the file is at 5.0 velocity play fast sound
	//anything in between should be a mix of 2 sounds	  
	 * @param velocities
	 * @return
	 */
	//how to use this function to accurately build the sound?
	//with 3 sounds
	//ideally, v>5 = 0% of slow and silence sounds
	//v>1 v<5 = just mix fast and slow sound by the percent by which is closer 
	//v <1, if v = 0, play silence otherwise play a mix of silence and the slow sound 	
	
	public ArrayList<Double> determineMixPercentage(ArrayList<Double> velocities){
		ArrayList<Double> mixPercentages = new ArrayList<>();
		velocities.forEach(v ->{
			if (v <= 1) {
				//mix of slow and silence
				mixPercentages.add(v);
			}
			if (v > 1 && v < 5) {
				//mix of slow and fast
				mixPercentages.add(1-(v-1)/4);
			}
			if (v >5) {
				//all fast
				mixPercentages.add(0.0);
			}
		});
		return mixPercentages;				
	}
	
	/**
	 * will take an array of velocities and return an array of the % of the file1 needed to play appropriate mix
	 * mix more or less of the files if they are close to certain velocities
	//if the file is at 1.0 velocity play slow sound
	//if the file is at 0 velocity play silence
	//if the file is at 5.0 velocity play fast sound
	//anything in between should be a mix of 2 sounds	  
	 * @param velocities
	 * @return
	 */
	//how to use this function to accurately build the sound?
	//with 3 sounds
	//ideally, v>5 = 0% of slow and silence sounds
	//v>1 v<5 = just mix fast and slow sound by the percent by which is closer 
	//v <1, if v = 0, play silence otherwise play a mix of silence and the slow sound 
	
	
	/**
	 * will 
	 * @param velocities
	 * @return
	 */
	public ArrayList<Coordinate> determineMixPercentageFor3Files(ArrayList<Coordinate> velocities){
		ArrayList<Coordinate> mixPercentages = new ArrayList<>();
		velocities.forEach(v ->{
			if (v.x <= 1) {
				//mix of slow and silence
				mixPercentages.add(new Coordinate(v.x,3));
			}
			if (v.x > 1 && v.x < 5) {
				//mix of slow and fast
				mixPercentages.add(new Coordinate((1-(v.x-1)/4),2));
			}
			if (v.x >5) {
				//all fast
				mixPercentages.add(new Coordinate(0.0, 2));
			}
		});
		return mixPercentages;				
	}
	/**
	 * will calculate how many times the given file needs to be looped to be played for the given duration
	 * @param soundFile
	 * @param duration
	 * @return
	 */
	public int calculateLoopCount(File soundFile, double duration) {
		float durationInSeconds = fileLength(soundFile);			
	      //calculate how many files i need to generate to match the strokeLength
		  float loopCount1 = (float)(duration/durationInSeconds);	  		  
		  int loopCount = Math.round(loopCount1);
		  //if (loopCount == 0) {
		//	  loopCount = 1;
		 // }		  
		  return loopCount;
	}
	/**
	 * stretch the given file for the given duration(in seconds), return the audioInputStreams[] and will set 
	 * the submitted loopCount?
	 * @param soundFile
	 * @param duration
	 * @return
	 */
	public AudioInputStream[] stretchFileFor(File soundFile, double duration){
		//calculate the time of the sustained sound file you will be using	    	      
		  int loopCount = calculateLoopCount(soundFile,duration);
		  
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
	      
	      return audioInputStreams;
	}
	/**
	 * will play the sound file associated with the given AudioInputStream
	 * @param audioInputStreams
	 */
	public void playStretchedFor(AudioInputStream[] audioInputStreams, int loopCount) {
		//initialize the source data line
		System.out.println("audioInputStreamSize = "+audioInputStreams.length);
		System.out.println("loopCount = "+loopCount);
		
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
	        

	         //want to elongate the sound of a single file. Need to load the same sound bytes into the buffer twice.
	         //can load a duplicate of the same file and have them load 100% each. Works :)
	         
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
	        	 //System.out.println("increment i "+i);
	        	 
	}}
	        	 soundLine.drain();
		         soundLine.close();			
	}
	
	/**
	 * will mix grains from audioInputStreams and audioInputStreams2,
	   will play the mixed sounds 
	   
	   will increment the mixing base on the % wanted of file1 given.
	   will play silence when the % 
	   will sustain the mixed sound for the value of duration(in milliseconds)
	   
	  AudioInputStream[] audioInputStreams
	  int loopCount	 
	  AudioInputStream[] audioInputStreams2
	  int loopCount2
	  ArrayList<Double> mixPercentages 
	  double duration
	 */
	public void mixStreamsFor(AudioInputStream[] audioInputStreams, int loopCount,
			AudioInputStream[] audioInputStreams2, int loopCount2, 
			ArrayList<Double> mixPercentages, double duration) {
		//always use the smallest loop count so that none of the audioStream arrays reaches its end
		
		double percentageIncrementInTime = duration/mixPercentages.size();		
		
		int newloopCount;
		if (loopCount < loopCount2) {
			newloopCount = loopCount;
		}else {
			newloopCount = loopCount2;
		}
		
		//prepare the percentage increment
	     long totalFramesRead = 0;
	     for (int i =0;i<loopCount;i++) {
	    	 totalFramesRead += audioInputStreams[i].getFrameLength();
	     }
	     for (int i =0;i<loopCount2;i++) {
	    	 totalFramesRead += audioInputStreams2[i].getFrameLength();
	     }
	     System.out.println("total frame length = "+totalFramesRead);
	     int predictedBytesRead = (int)totalFramesRead*4;
	     if (mixPercentages.size() != 0) {
	     int bytesPerPercent = predictedBytesRead/mixPercentages.size();
	     int mixPercentagesIncrement =0;
	     
		
		//trying to stretch the strokes based on the percentages !!!
		//this would be a different kind of mixing
		//not by grain, instead stretch the files so that they match the duration
		//then mix by grain (no file is played in full more than once)
		//functions:  given 2 files a duration and a percentage play without repeating a file more than once
		//need to 'stretch' 2 files and mix them smoothly
		//functions:
		
		//stretch the given file, return the audioInputStreams[] and loopCount(can do this separate?)
		//mix the given stretched files, 
		
		//given the audioInputStreams[], and the loop count mix the files so that they play for the given duration
		//need to adjust the loop count to fit duration?
		//need to use separate for loops to achieve this
		//start just mixing the stretched files 50% each.
		
		//STROKE-velocities, duration
		//calculate percentages, how? by total average velocity? by segments?
		//start with by total average velocity
		//get percentage of fast to slow sound you should play
		//calculate how long each file should be played for
		//duration = totalTime*percentage
		//mix each of these elongated files together by the same amounts as the percentages
		//play result 
		
		//play 
		
		
		//prepare the buffers for the 2 sound files
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
		
	     nBytesRead = 0;
	     int loop1Count = 0;
	     int loop2Count = 0;
	     int bytesCount = 0;

	     
		//now mix the soundFiles
		try {
	      //once the audioStreams are empty stop		
			while (nBytesRead >-1) {
	        //number for loop is determined by strokeTime/durationInSeconds
			//don't want to reach the end of the array
			//what do you do with 2 to 1. 
				
			loop1Count = 0;
			loop2Count = 0;
	       // for (int i = 0;i<loopCount;i++) {
			while (loop1Count != loopCount && loop2Count != loopCount2) {
				
	       	if (gate == 0) {   
	       		if (loop1Count < loopCount) {
	       		nBytesRead = audioInputStreams[loop1Count].read(sampledData, 0, sampledData.length); 	       		 
	       		loop1Count++;
	       	}
	       	} 	else {
	       		 if (loop2Count < loopCount2) {
	       		 nBytesRead = audioInputStreams2[loop2Count].read(sampledData, 0, sampledData.length); 		       		 
	       		loop2Count++;
	       		 }
	       		 
	       	 }        	           	           
	           if (nBytesRead >= 0) {
	        	   bytesCount +=nBytesRead;
	              // Writes audio data to the mixer via this source data line.          	            	
	              soundLine.write(sampledData, 0, nBytesRead);	  
	              
	              //changes the file which is read into the inputStream
	              totalCount += 1;               
	         	  if (gate ==0) {
	        		  gate = 1;
	        	  }else {
	        		  gate = 0;
	        	  }
	         	  //increment the percentage if increment is reached
	         	  //use the frame size to know where we are in the recording.
	         	  //would be totalBytesRead or something
	         	  if (bytesCount > bytesPerPercent) {
	         		  bytesCount = 0;
	         		  mixPercentagesIncrement ++;
	         	  }
	         	  
	         	  
	              if (file1Count/totalCount <= mixPercentages.get(mixPercentagesIncrement)) {
	            	  gate = 0;
	              }else {
	           	   gate = 1;
	              }	              
	              if (gate == 0) {
	            	  file1Count += 1;            	   
	              }	              
	           }
	           
	        	 }
	        }
			}
		catch (IOException ex) {
	        ex.printStackTrace();
	     }//catch (LineUnavailableException e) {     }
			finally {
	    	 soundLine.drain();    	     	 
	         soundLine.close();
	         System.out.println("total bytes Read = "+bytesCount);
	    	 }
	     }
		}
	/**
	 * will mix grains from audioInputStreams and audioInputStreams2 and audioInputStreams3
	   will play the mixed sounds 
	   
	   will increment the mixing based on the mixPercentages 
	   will sustain the mixed sound for the value of duration(in milliseconds)
	   
	  AudioInputStream[] audioInputStreams
	  int loopCount	 
	  AudioInputStream[] audioInputStreams2
	  int loopCount2
	  AudioInputStream[] audioInputStreams3
	   int loopCount3
	  ArrayList<Double> mixPercentages 
	  double duration
	 */
	public void mixStreamsFor(AudioInputStream[] audioInputStreams, int loopCount,
			AudioInputStream[] audioInputStreams2, int loopCount2,
			AudioInputStream[] audioInputStreams3, int loopCount3,
			ArrayList<Coordinate> mixPercentages, double duration, ArrayList<Float> panValues) {
		//always use the smallest loop count so that none of the audioStream arrays reaches its end
		
		double percentageIncrementInTime = duration/mixPercentages.size();		
		
		int newloopCount;
		if (loopCount < loopCount2) {
			newloopCount = loopCount;
		}else {
			newloopCount = loopCount2;
		}
		
		//prepare the percentage increment
	     long totalFramesRead = 0;
	     for (int i =0;i<loopCount;i++) {
	    	 totalFramesRead += audioInputStreams[i].getFrameLength();
	     }
	     for (int i =0;i<loopCount2;i++) {
	    	 totalFramesRead += audioInputStreams2[i].getFrameLength();
	     }
	     for (int i =0;i<loopCount3;i++) {
	    	 totalFramesRead += audioInputStreams3[i].getFrameLength();
	     }
	     
	     System.out.println("predictedBytesRead = "+totalFramesRead*4);
	     int predictedBytesRead = (int)totalFramesRead*4;
	     if (mixPercentages.size() != 0) {
	     int bytesPerPercent = predictedBytesRead/mixPercentages.size();
	     int mixPercentagesIncrement =0;
	     //
	     if (panValues.size() == 0) {
	    	 panValues.add((float) 0.0);
	     }
	     int bytesPerPanValue = predictedBytesRead/panValues.size();
	     int panValuesIncrement =0;
		//prepare the buffers for the 2 sound files
		//initialize the source data line
		
	      AudioFormat audioFormat = audioInputStreams3[0].getFormat();          
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
	      
	      //set up the panner
	      FloatControl volCtrl;
	      Mixer mixer = AudioSystem.getMixer(null);	      
	      volCtrl = (FloatControl) soundLine.getControl(FloatControl.Type.PAN);	    	      	      	      	      	            	      
	      volCtrl.setValue(panValues.get(0));
	      
	      soundLine.start();
		
	     nBytesRead = 0;
	     int loop1Count = 0;
	     int loop2Count = 0;
	     int loop3Count = 0;
	     int bytesCount = 0;
	     int panBytesCount =0;
	     
	     //testing counts
	     double slowCountF = 0.1;
	     double  slowCountS = 0.1;
	     double  fastCount =0.1;
	     double  silenceCount =0.1;
	     int totalCountF = 1;
	     int totalCountS =1;
	     long sTime = System.currentTimeMillis();
	     int totalBytesCount =0;
	     int mixIncrementYCount =0;
	     
		//now mix the soundFiles
		try {
	      //once the audioStreams are empty stop		
			while (nBytesRead >-1) {
	        //number for loop is determined by strokeTime/durationInSeconds
			//don't want to reach the end of the array
			//what do you do with 2 to 1. 
				
			loop1Count = 0;
			loop2Count = 0;
			loop3Count = 0;
	       //until all the loopCounts are reached, keep repeating!
			//problem is that once a single file == its loopCount, the counters are reset
			
			while (loop1Count != loopCount &&loop2Count != loopCount2 && loop3Count != loopCount3) {				
	       	if (gate == 0) {  
	       		//check to see if there is buffer left in this audioStream
	       		if (loop1Count < loopCount) {
	       		nBytesRead = audioInputStreams[loop1Count].read(sampledData, 0, sampledData.length); 	       		 
	       		loop1Count++;
	       		//if empty, check the other audioStreams for more
	       		if (nBytesRead <0) {
	       			//pencilFast
		       		 if (loop2Count < loopCount2) {
			       		 nBytesRead = audioInputStreams2[loop2Count].read(sampledData, 0, sampledData.length); 
			       		//System.out.println("hit loop2");
			       		loop2Count++;
			       		 }
		       		 //silence
		       		 if (loop3Count < loopCount3 && nBytesRead <0) {
			       		 nBytesRead = audioInputStreams3[loop3Count].read(sampledData, 0, sampledData.length); 	
			       		 //System.out.println("hit loop3");
			       		loop3Count++;
	       		 }
	       		}
	       		
	       	}
	       		if(nBytesRead <0) {
	       			System.out.println("nBytes are 0 in loop 1");
	       		}
	       		
	       	} 	else if (gate==1){
	       		 if (loop2Count < loopCount2) {
	       		 nBytesRead = audioInputStreams2[loop2Count].read(sampledData, 0, sampledData.length); 
	       		loop2Count++;
	       	//if empty, check the other audioStreams for more
	       		if (nBytesRead <0) {
	       			//pencilSlow
		       		 if (loop1Count < loopCount) {
			       		 nBytesRead = audioInputStreams[loop1Count].read(sampledData, 0, sampledData.length); 
			       		loop1Count++;
			       		 }
		       		 //silence
		       		 if (loop3Count < loopCount3 &&nBytesRead <0) {
			       		 nBytesRead = audioInputStreams3[loop3Count].read(sampledData, 0, sampledData.length); 	
			       		loop3Count++;
	       		 }
	       		}
	       		
	       		 }
		       		if(nBytesRead <0) {
		       			System.out.println("nBytes are 0 in loop 2");
		       		}
	       		 
	       	}else if(gate==2){
		       		 if (loop3Count < loopCount3) {
			       		 nBytesRead = audioInputStreams3[loop3Count].read(sampledData, 0, sampledData.length); 	
			       		loop3Count++;
				       	//if empty, check the other audioStreams for more
			       		if (nBytesRead <0) {
			       			//pencilSlow
				       		 if (loop1Count < loopCount) {
					       		 nBytesRead = audioInputStreams[loop1Count].read(sampledData, 0, sampledData.length); 
					       		loop1Count++;
					       		 }else
				       		 //pencilFast
				       		 if (loop2Count < loopCount2 && nBytesRead <0) {
					       		 nBytesRead = audioInputStreams2[loop2Count].read(sampledData, 0, sampledData.length); 	
					       		loop2Count++;
			       		 }
			       		}
			       		if(nBytesRead <0) {
			       			System.out.println("nBytes are 0 in loop 3");
			       		}
			       		}		       		 
	       	 }
       		if(nBytesRead <0) {
       			System.out.println("nBytes are 0");
       		}
	           if (nBytesRead >= 0) {
	        	   bytesCount +=nBytesRead;
	        	   panBytesCount += nBytesRead;
	        	   totalBytesCount += nBytesRead;
	              // Writes audio data to the mixer via this source data line.        
	        	   volCtrl.setValue(panValues.get(panValuesIncrement));
	              soundLine.write(sampledData, 0, nBytesRead);	  
	              
	            //increment the percentage if increment is reached
	         	  //use the frame size to know where we are in the recording.
	         	  //would be totalBytesRead or something
	         	  if (panBytesCount > bytesPerPanValue) {
	         		 panBytesCount = 0;
	         		  panValuesIncrement ++;
	         	  }
	             
	              
	         	  //increment the percentage if increment is reached
	         	  //use the frame size to know where we are in the recording.
	         	  //would be totalBytesRead or something
	         	  if (bytesCount > bytesPerPercent) {
	         		  bytesCount = 0;
	         		  mixPercentagesIncrement ++;
	         	  }
	         	  
	         	  
	         	  
	         	  //figure out which file to mix
	         	  if (mixPercentages.get(mixPercentagesIncrement).y == 2) {
	         		  //for pencilFast
	         		 if (slowCountF/totalCountF < mixPercentages.get(mixPercentagesIncrement).x) {
		            	  gate = 0;
		            	  slowCountF += 1;
		              }else {
		            	  gate = 1;
		            	  fastCount+=1;
		              }
	         		 totalCountF+=1;
	         	  }
	         	  
	         	  if (mixPercentages.get(mixPercentagesIncrement).y == 3) {
	         		 mixIncrementYCount++;
	         		  //for silence	         		 
	         		  //add a silenceTotalCount?
	         		 if (slowCountS/totalCountS < mixPercentages.get(mixPercentagesIncrement).x) {
		            	  gate = 0;
		            	  slowCountS+=1;
		              }else {
		            	  gate = 2;
		            	  silenceCount+=1;			            	  
		              }
	         		 totalCountS +=1;
	         	  }	         	                     
	           }
	           
	        	 }

			
			//figure out how to make the loopCount even
	         System.out.println("loop1Count = "+loop1Count);
	         System.out.println("loop2Count = "+loop2Count);
	         System.out.println("loop3Count = "+loop3Count);
	        }
			//System.out.println("last mix increment "+mixPercentagesIncrement);
			//System.out.println(" mixPercentagesSize "+mixPercentages.size());
			}
		
		catch (IOException ex) {
	        ex.printStackTrace();
	     }//catch (LineUnavailableException e) {     }
			finally {
	    	 soundLine.drain();    	     	 
	         soundLine.close();
	         System.out.println("loopCount1 = "+loopCount);
	         System.out.println("loopCount2 = "+loopCount2);
	         System.out.println("loopCount3 = "+loopCount3);
	     //    System.out.println("total bytes count = "+totalBytesCount);
	     //    System.out.println("slowCountF = "+slowCountF);
	     //    System.out.println("slowCountS = "+slowCountS);
	     //    System.out.println("fastCount = "+fastCount);
	     //    System.out.println("silenceCount = "+silenceCount);
	     //    System.out.println("mixIncrementYCount = "+mixIncrementYCount);
	    	 }
	     }
		}
	

	/**
	 * will take the mix percentages and total stroke duration in seconds
	 * will return an array with the duration in seconds for 3 sound files
	 * @param mixPercentages
	 * @param duration
	 * @return
	 */
	public double[] calculateDurationsFor3Files(ArrayList<Coordinate> mixPercentages, ArrayList<Coordinate> velocities, double duration) {
		//for each mixPercentage some length of each file will be played
		//double durationPerPercent = duration/mixPercentages.size();
		double file1D =0;
		double file2D =0;
		double file3D =0;
		for (int i = 0; i< mixPercentages.size();i++) {
			if (mixPercentages.get(i).y == 2) {
				//for pencilFast
				double addToFile1 = velocities.get(i).y*mixPercentages.get(i).x;
				file2D += velocities.get(i).y -addToFile1;
				file1D += addToFile1;				
			}else {
				//for silence
				double addToFile1 = velocities.get(i).y*mixPercentages.get(i).x;
				file3D += velocities.get(i).y -addToFile1;
				file1D += addToFile1;	
			}
		}
		System.out.println("file 1 duration "+file1D);
		System.out.println("file 2 duration "+file2D);
		System.out.println("file 3 duration "+file3D);
		double[] durations = {file1D,file2D,file3D};
		return durations;											
	}
	
	
	
	}
	

	