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
			 
			
			//prgress report
			//fixed the mono to stereo issue
			//SoundSystemConfig.setDefaultRolloff(1f); changes the perceived distance between you and the sound
			//0 is beside you, 10 is roughly 10 meters away(just guessing)
			//this + panControl will work!
			//need to get the z or y value to do something.
			//if you set the z to +-50 and defaultRolloff to 10 and 1, it kinda sounds like front and back?
			
			//it works now? 
			//x coordinate seems to change the panning, but it is unflexible. 
			//z coordinate only works when paired with the x coordinate
			//eithier the panning is all the way to the right or all the way to the left when the other values are at 0.
						
			SoundSystem mySoundSystem = new SoundSystem(); 
			//mySoundSystem.setListenerPosition( 0, 0, 0 );
			mySoundSystem.getListenerData().setAngle((float) Math.toRadians(90));
			
			SoundSystemConfig.setDefaultRolloff(1f);
			mySoundSystem.newSource(false, "sound 1", "dooo2cha.wav", false,
							0, 0,10, SoundSystemConfig.ATTENUATION_ROLLOFF,  
							SoundSystemConfig.getDefaultRolloff()   
							);
			//SoundSystemConfig.setDefaultRolloff(10f);
			mySoundSystem.newSource(false, "sound 2", "dooo2cha.wav", false,
					0, 0, -10, SoundSystemConfig.ATTENUATION_ROLLOFF,  
					SoundSystemConfig.getDefaultRolloff()   
					);
			for( int x = 0; x < 15; x++ )         {     				
				// If x is divisible by 2, play to the right:     				if( x % 2 == 0 ) {  																								
					System.out.println("right side");			
					
					mySoundSystem.play("sound 1");
					mySoundSystem.rewind("sound 1");
				}
				// Otherwise play to the left:    
				else {             					
					System.out.println("left side"); 					
					mySoundSystem.play("sound 2");
					mySoundSystem.rewind("sound 2");					
				}
				//System.out.println("listener position! "+mySoundSystem.getListenerData().lookAt);
				 //mySoundSystem.moveListener( 0, 0, 0 );
				// wait a bit so the explosions don't all start at once
				sleep( 1000 );
				
				
				
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
		

