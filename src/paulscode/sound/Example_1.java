package paulscode.sound;

import java.io.File;

/**  *  Creates the SoundSystem, plays some music, then shuts down.  */
public class Example_1 {     
	public static void main( String[] args )     { 
		new Example_1();     
		}          
	public Example_1()   {
		// Instantiate the SoundSystem:
		SoundSystem mySoundSystem = new SoundSystem();
		// Create a new ambient, looping source called "Cool Music":
		
		//keep trying to get this to work, should allow you to use 3D sounds.
		File f2 = new File ("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\1234.WAV");
		
		//mySoundSystem.backgroundMusic( "Cool Music", f2.toString(), true ); 
		mySoundSystem.loadSound(f2.toString());
		
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
