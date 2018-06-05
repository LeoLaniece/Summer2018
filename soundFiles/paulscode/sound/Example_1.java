package paulscode.sound;

import java.io.File;


import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException; 
import paulscode.sound.libraries.LibraryJavaSound; 
import paulscode.sound.codecs.CodecWav; 

/**  *  Creates the SoundSystem, plays some music, then shuts down.  */
public class Example_1 {     
	public static void main( String[] args )     { 
		new Example_1();     
		

		//progress report
		//got different error msg's, still no sound
		
		
		}          
	public Example_1()   {
		try {     SoundSystemConfig.addLibrary( LibraryJavaSound.class );
		} catch( SoundSystemException e ) { 
			System.err.println( "error linking with the LibraryJavaSound plug-in" ); 
		}
		
		try {     SoundSystemConfig.setCodec( "wav", CodecWav.class );
		} catch( SoundSystemException e ) {
			System.err.println("error linking with the CodecWav plug-in" );
			}
		
		// Instantiate the SoundSystem:
		SoundSystem mySoundSystem = new SoundSystem();
		//SoundSystemConfig.setSoundFilesPackage("C:\\Users\\HCI Lab\\eclipse-workspace\\soundFiles\\SoundFiles\\");
		// Create a new ambient, looping source called "Cool Music":
		
		//keep trying to get this to work, should allow you to use 3D sounds.
		//File f2 = new File ("C:\\Users\\HCI Lab\\eclipse-workspace\\soundFiles\\SoundFiles\\1234.wav");		
		//mySoundSystem.backgroundMusic( "Cool Music", "beethoven.mid", true ); 
		
		boolean priority = false; 
		String sourcename = "Source 1"; 
		String filename = "beethoven.mid"; 
		boolean loop = true; 
		float x = 40; 
		float y = 0; 
		float z = 0; 
		int aModel = SoundSystemConfig.ATTENUATION_LINEAR;
		float rFactor = 0;//SoundSystemConfig.getDefaultRolloff(); 		 
		mySoundSystem.quickPlay( priority, filename, loop, x, y, z, aModel, rFactor );
		
		
		//mySoundSystem.loadSound(f2.toString());
		
		// Wait for 10 seconds:
		sleep( 10 );                 
		// Shut down:       
		//System.out.println("made it past initializing the codec");
		mySoundSystem.cleanup(); 
		
		} 
	 
    public void sleep( long seconds )     { 
    	try         {  
    		Thread.sleep( 1000 * seconds ); 
    		}        
    	catch( InterruptedException e )  
    	{}     
    	}
    }
