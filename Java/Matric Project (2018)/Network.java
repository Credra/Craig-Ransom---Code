/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import javax.swing.JTextArea;

/**
 *In Progress
 * @author craig
 */
public class Network implements Runnable
{
    private PrintWriter out;
    private Scanner in;
    private Socket sock;
    private Battle battle;
    private boolean running,message;
    private String echoLine;

    /**
     *
     * @param sock
     * @param battle
     */
    public Network(Socket sock, Battle battle)
    {
        try
        {
            message=false;
            running = true;
            this.battle=battle;
            in = new Scanner(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()), true);
        } catch (IOException ex)
        {
            System.out.println(ex);
            battle.txaMessages.append("Error in connecting");
        }
    }
    
    /**
     *
     * @param msg
     */
    public void sendMessage(String msg)
    {
        out.println(msg);
        out.flush();
        System.out.println("sent : "+msg);
    }

    /**
     *
     * @return
     */
    public String getMessage()
    {
        message=false;
        System.out.println("Recieved: "+echoLine);
        return echoLine;
    }

    @Override
    public void run()
    {
        do
        {
            echoLine=in.nextLine();
            message=true;
            System.out.println("REICEVED NOT PROCESSED");
        }while(running);
        try
        {
            in.close();
            out.println("End");
            out.close();
            sock.close();
        } catch (IOException ex)
        {
            battle.txaMessages.setText("Error in disconecting");
        }
    }

    /**
     *
     * @return
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     *
     * @return
     */
    public boolean isMessage()
    {
        return message;
    }
    
    /**
     *
     */
    public void stop()
    {
        running = false;
    }
   //int pokeID, int level, int exp, int stage,String name, double [] levelStats,String type0,String type1,Move[] moves,int numMoves

    
}
