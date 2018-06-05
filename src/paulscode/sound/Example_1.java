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
		
		try {     SoundSystemConfig.addLibrary( LibraryJavaSound.class );
		} catch( SoundSystemException e ) { 
			System.err.println( "error linking with the LibraryJavaSound plug-in" ); 
		}
		
		
		
		}          
	public Example_1()   {
		// Instantiate the SoundSystem:
		SoundSystem mySoundSystem = new SoundSystem();
		SoundSystemConfig.setSoundFilesPackage("C:\\Users\\HCI Lab\\eclipse-workspace\\soundFiles\\");
		// Create a new ambient, looping source called "Cool Music":
		
		//keep trying to get this to work, should allow you to use 3D sounds.
		File f2 = new File ("SoundFiles\\1234.wav");
		
		mySoundSystem.backgroundMusic( "Cool Music", f2.toString(), true ); 
		//mySoundSystem.loadSound(f2.toString());
		
		// Wait for 10 seconds:
		sleep( 10 );                 
		// Shut down:       
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
