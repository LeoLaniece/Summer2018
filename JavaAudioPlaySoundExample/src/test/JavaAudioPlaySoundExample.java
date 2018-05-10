package test;
import sun.audio.*;

import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;



/**
 * A simple Java sound file example (i.e., Java code to play a sound file).
 * AudioStream and AudioPlayer code comes from a javaworld.com example.
 * @author alvin alexander, devdaily.com.
 */
public class JavaAudioPlaySoundExample
{
	
	
	
  public static void main(String[] args) 
  throws Exception
  {
    // open the sound file as a Java input stream
    String gongFile = "C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\scratch-loud.WAV";
   // InputStream in = new FileInputStream(gongFile);

    // create an audiostream from the inputstream
   // AudioStream audioStream = new AudioStream(in);

    // play the audio clip with the audioplayer class
   // AudioPlayer.player.start(audioStream);
   // AudioInputStream audioInputStream =  new AudioInputStream((TargetDataLine) in);
    
    Sound scratch = new Sound(gongFile);
    scratch.play();

    
    //extractFloatDataFromAudioInputStream(audioInputStream);
 /*   AudioFormat format = audioInputStream.getFormat();
	    //calculate size
	    byte[] audioBytes = new byte[(int) (audioInputStream.getFrameLength() * format.getFrameSize())];
	    // calculate durationSec
	    float milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / audioInputStream.getFormat().getFrameRate());
	    double durationSec = milliseconds / 1000.0;
	     System.out.println("The current signal has duration "+durationSec+" Sec");
	    try {
	        audioInputStream.read(audioBytes);
	    } catch (IOException e) {
	        System.out.println("IOException during reading audioBytes");
	        e.printStackTrace();
	    }*/
    int totalFramesRead = 0;
    File fileIn = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.aif");
    // somePathName is a pre-existing string whose value was
    // based on a user selection.
    try {
      AudioInputStream audioInputStream = 
        AudioSystem.getAudioInputStream(fileIn);
      int bytesPerFrame = 
        audioInputStream.getFormat().getFrameSize();
      System.out.println("file frame size ="+audioInputStream.getFormat().getFrameSize());
        if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
        // some audio formats may have unspecified frame size
        // in that case we may read any amount of bytes
        bytesPerFrame = 1;
      } 
      // Set an arbitrary buffer size of 1024 frames.
      int numBytes = 1024 * bytesPerFrame; 
      byte[] audioBytes = new byte[numBytes];
      try {
        int numBytesRead = 0;
        int numFramesRead = 0;
        // Try to read numBytes bytes from the file.
        while ((numBytesRead = 
          audioInputStream.read(audioBytes)) != -1) {
          // Calculate the number of frames actually read.
          numFramesRead = numBytesRead / bytesPerFrame;
          totalFramesRead += numFramesRead;
          // Here, do something useful with the audio data that's 
          // now in the audioBytes array...
          System.out.println("numFramesRead = "+numFramesRead);	   
          System.out.println("audioByte "+numBytesRead+" = "+audioBytes[numBytesRead]);
          
        }
      } catch (Exception ex) { 
        // Handle the error...
      }
    } catch (Exception e) {
      // Handle the error...
    }
    
    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    
    
  }
  
  public static void extractFloatDataFromAudioInputStream(AudioInputStream audioInputStream) {
	    
	    //extractFloatDataFromAmplitudeByteArray(format, audioBytes);
	}
  
  
}