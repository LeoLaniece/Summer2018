package paulscode.sound;

import paulscode.sound.SoundSystem; 
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
//import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import paulscode.sound.libraries.LibraryJavaSound;
import paulscode.sound.codecs.CodecWav; 
//import paulscode.sound.codecs.CodecJOgg; 
 
/**  *  Plays background music and explosions in 3D.  **/
public class Example_2 {     
	public static void main( String[] args )     {
		new Example_2();    
		}          public Example_2() 
		{         // Load some library and codec plug-ins: 
			try         {    
				//SoundSystemConfig.addLibrary( LibraryLWJGLOpenAL.class );  
				SoundSystemConfig.addLibrary( LibraryJavaSound.class ); 
				SoundSystemConfig.setCodec( "wav", CodecWav.class ); 
				//SoundSystemConfig.setCodec( "ogg", CodecJOgg.class ); 
				}         catch( SoundSystemException e )         { 
					System.err.println("error linking with the plug-ins" ); 
					}         // Instantiate the SoundSystem:  
			SoundSystem mySoundSystem = new SoundSystem();  
			// play some background music:     
			mySoundSystem.backgroundMusic( "Source 1", "1234.wav", true ); 
			// wait a bit before playing the explosions:  
			//sleep( 2000 );         // play 15 explosions, right and left: 
			mySoundSystem.setVolume( "Source 1", 0.5f ); 
			//mySoundSystem.setMasterVolume( 0f ); 
			for( int x = 0; x < 15; x++ )         {     
				// If x is divisible by 2, play to the right:     
				if( x % 2 == 0 ) {  
					System.out.println("right side");
					mySoundSystem.setPosition( "Source 1", 0.1f, 0, 0 );
					//mySoundSystem.setVolume( "Source 1", 0f ); 
				//	mySoundSystem.quickPlay( false, "explosionShort.wav", false, 
				//			0.5f, 0, 0, 
				//			SoundSystemConfig.ATTENUATION_ROLLOFF,  
				//			SoundSystemConfig.getDefaultRolloff()   
				//			);   
				}
				// Otherwise play to the left:    
				else {             
					System.out.println("left side");
					mySoundSystem.setPosition( "Source 1", 1f, 0, 0 );
					//mySoundSystem.setVolume( "Source 1", 0.5f ); 
				//	mySoundSystem.quickPlay( false, "explosionShort.wav", false,
				//			5, 0, 0, 
				//			SoundSystemConfig.ATTENUATION_ROLLOFF,     
				//			SoundSystemConfig.getDefaultRolloff()); 
				//			System.out.println(SoundSystemConfig.getDefaultRolloff());
							
				}
				// wait a bit so the explosions don't all start at once
				sleep( 2000 );
			}
	}
		
	    public void sleep( long seconds )     { 
	    	try         {  
	    		Thread.sleep( 1 * seconds ); 
	    		}        
	    	catch( InterruptedException e )  
	    	{}     
	    	}
	    }
		

