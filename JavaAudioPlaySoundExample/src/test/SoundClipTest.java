package test;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import javax.sound.sampled.*;
import javax.swing.*;
   
// To play sound using Clip, the process need to be alive.
// Hence, we use a Swing application.
public class SoundClipTest extends JFrame {
   
   // Constructor
   public SoundClipTest() {
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setTitle("Test Sound Clip");
      this.setSize(300, 200);
      this.setVisible(true);
     this.getContentPane().setLayout(new FlowLayout());

      JButton btn = new JButton("sound");
      btn.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          System.out.println("button pressed");
        }
      });
      add(btn);
      
   
      try {
         // Open an audio input stream.
         File url = new File("C:\\Users\\HCI Lab\\Desktop\\Leo Laniece summer 2018\\sound recordings\\scratch-loud.WAV");
         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
         // Get a sound clip resource.
         Clip clip = AudioSystem.getClip();
         // Open audio clip and load samples from the audio input stream.
         clip.open(audioIn);
         clip.loop(2);
      } catch (UnsupportedAudioFileException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (LineUnavailableException e) {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) {
      new SoundClipTest();
   }
}
