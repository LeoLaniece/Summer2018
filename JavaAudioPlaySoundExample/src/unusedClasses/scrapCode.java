package unusedClasses;

import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/////////////////
/*
 * from Grain2Files
 * 	/**
	 * plays the mixed grains of both sound files
	 * will take the duration of the stroke in milliseconds as a parameter
	 * will take the velocity of the stroke as a parameter.
	 * should not 'loop' the sound, this needs to be determined by the required interaction
	 * @throws LineUnavailableException 
	 
	//change velocity to double[]
	
	public void play(double duration, double velocity) throws LineUnavailableException {		
		try {		     	        	 
			//start a new soundLine every time?
	         soundLine = (SourceDataLine) AudioSystem.getLine(info);
	         soundLine.open(audioFormat);
	         soundLine.start();
	         //Why 20?
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
 */
