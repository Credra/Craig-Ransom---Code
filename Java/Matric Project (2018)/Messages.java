/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.awt.Graphics;

/**
 *Stores the messages for the player that need to be rendered onto the game screen
 * @author Craig
 */
public class Messages
{
    private String message [];
    
    /**
     *Constructor for the Message Class 
     * @param name The player's name
     */
    public Messages(String name)
    {
        message=new String[]{"","Welcome "+name,"Press the 'H' key for help","You can move with the W,A,S and D keys"};
            
    }
    
    /**
     *Adds a new message to be rendered and overwrites the oldest one
     * @param temp The message to be added
     */
    public void addMessage(String temp)
    {
        int i;
        for (i = 0; i < message.length-1; i++)
        {
            message[i]=message[i+1];
        }
        message[i]=temp;
    }
    
    /**
     *Renders the messages
     * @param g the Graphics of the JFrame the messages will be rendered to
     * @param width Width of message
     * @param height Height of message
     */
    public void render(Graphics g, int width, int height)
    {
        for (int i = 0; i < message.length; i++)
        {
            g.drawString(message[i], width/2-125, height-100+i*20);//SansSerif
        }
    }

    /**
     *Returns message at position i
     * @param i The position of the String that will be returned
     * @return Returns String at position i of th array
     */
    public String getMessage(int i)
    {
        return message[i];
    }

    @Override
    public String toString()
    {
        String temp="";
        for (int i = 0; i < message.length; i++)
        {
            temp+=message[i]+"\n";
            
        }
        return temp;
    }
    
}
