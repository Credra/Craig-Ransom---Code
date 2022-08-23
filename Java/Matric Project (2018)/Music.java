/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *In Progress
 * @author craig
 */
public class Music implements Runnable
{
    private AudioPlayer au;
    private AudioStream as;

    /**
     *
     */
    public Music()
    {
        try
        {
            
            au = AudioPlayer.player;
            as = new AudioStream(new FileInputStream("Music\\open.wav"));
        } catch (FileNotFoundException ex)
        {
            System.out.println(ex);
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    @Override
    public void run()
    {
        au.start(as);
    }
    
    /**
     *
     */
    public void stop()
    {
        au.stop(as);
    }

    /**
     *
     */
    public void shop()
    {
        try
        {
            au.stop(as);
            as = new AudioStream(new FileInputStream("Music\\shop.wav")); 
            au.start(as);
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    /**
     *
     */
    public void cave()
    {
         try
        {
            au.stop(as);
            as = new AudioStream(new FileInputStream("Music\\cave.wav")); 
            au.start(as);
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    /**
     *
     */
    public void battle()
    {
        try
        {
            System.out.println("Battle");
            au.stop(as);
            as = new AudioStream(new FileInputStream("Music\\battle.wav")); 
            System.out.println(as.getLength());
            au.start(as);
            System.out.println("battle");
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    /**
     *
     */
    public void pokeCenter()
    {
         try
        {
            au.stop(as);
            as = new AudioStream(new FileInputStream("Music\\pokemon center.wav")); 
            au.start(as);
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    /**
     *
     */
    public void main()
    {
        try
        {
            au.stop(as);
            as = new AudioStream(new FileInputStream("Music\\main.wav")); 
            au.start(as);
        }catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
}
