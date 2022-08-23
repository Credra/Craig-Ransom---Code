package ransom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *In Progress
 * @author craig
 */
public class ConnectGUI extends javax.swing.JFrame implements Runnable
{
    private static final int PORT = 8080;
    private Game game;
    private Socket sock;
    private ServerSocket servSock;
    private Thread t;
    
    /**
     *
     * @param game
     */
    public ConnectGUI(Game game)
    {
        this.game=game;
        initComponents();
        this.setVisible(true);
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents()
    {

        try
        {
            btnJoin = new javax.swing.JButton();
            btnHost = new javax.swing.JButton();
            lblError = new javax.swing.JLabel();
            lblTitle = new javax.swing.JLabel();
            lblBackround = new javax.swing.JLabel();
            btnExit = new JButton();
            
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            
            btnExit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnExitActionPerformed(evt);
                }
                
                
            });
            btnExit.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\exitButton.png"))));
            btnExit.setBorder(null);
            add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(815-60, 15, 30, 30));
            
            btnJoin.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
            btnJoin.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
            btnJoin.setBackground(new Color(200,50,150));
            btnJoin.setForeground(Color.white);
            btnJoin.setText("Join");
            btnJoin.setBorder(null);
            btnJoin.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnJoinActionPerformed(evt);
                }
            });
            getContentPane().add(btnJoin, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 370, 100, 50));
            
            btnHost.setFont(new java.awt.Font("SansSerif", 1, 24)); // NOI18N
            btnHost.setText("Host");
            btnHost.setBackground(new Color(200,50,150));
            btnHost.setForeground(Color.white);
            btnHost.setBorder(null);
            btnHost.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnHostActionPerformed(evt);
                }
            });
            getContentPane().add(btnHost, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 370, 100, 50));
            
            lblTitle.setFont(new java.awt.Font("SansSerif", 1, 20)); // NOI18N
            lblTitle.setForeground(Color.WHITE);
            lblTitle.setText("Connect to other player on the same network");
            getContentPane().add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 20, 500, 100));
            
            
            lblError.setFont(new java.awt.Font("SansSerif", 0, 16)); // NOI18N
            lblError.setForeground(Color.WHITE);
            getContentPane().add(lblError, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 230, 370, 130));
            
            lblBackround.setOpaque(true);
            ImageIcon image=new ImageIcon(ImageIO.read(new File("Sprites\\connectBackround.png")));
            BufferedImage resizedImg = new BufferedImage(800, 500, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,800, 500, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblBackround.setIcon(image);
            getContentPane().add(lblBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 500));
            
            this.setResizable(false);
            this.setUndecorated(true);
            pack();
            this.setLocationRelativeTo(null);
        } // </editor-fold>
        catch (IOException ex)
        {
            lblError.setText("File not found");
        }
    }

    
    @Override
    public void run()
    {
        try
        {
            sock = servSock.accept();
            ServerBattleGUI s =new ServerBattleGUI(game.getPlayer(),game.getArea(),sock);
            System.out.println("started");
            game.getPlayer().getMusic().battle();
            new Thread(s).start();
            game.stop();
            this.dispose();
        } catch (IOException ex)
        {
            lblError.setText("Game already hosted");
        }
    }
    private void btnHostActionPerformed(java.awt.event.ActionEvent evt)                                        
    {                                            
        
        try
        {
            servSock = new ServerSocket(PORT);
            lblError.setText("Waiting for another player");
            btnHost.setEnabled(false);
            btnJoin.setEnabled(false);
            t = new Thread(this);
            t.start();
        }

        catch(IOException ex)
        {
            lblError.setText("Game already hosted");
        }
    }                                       

    private void btnJoinActionPerformed(java.awt.event.ActionEvent evt)                                        
    {                
        try
        {
            sock = new Socket(InetAddress.getLocalHost().getHostAddress(), PORT);
            ClientBattleGUI s =new ClientBattleGUI(game.getPlayer(),game.getArea(),sock);
            new Thread(s).start();
            game.stop();
            s.setVisible(true);
            this.dispose();
            //Client- get here found host and ready!
        } catch (IOException ex)
        {
            lblError.setText("No host found");
        }
    }                                       

    
    private void btnExitActionPerformed(ActionEvent evt)
    {
        if(t!=null)
        {
            try
            {
                servSock.close();
            } catch (IOException ex)
            {
                lblError.setText("An error occur while exiting");
            }
        }
        game.setScreen(ID.Game);
        this.dispose();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnHost;
    private javax.swing.JButton btnJoin;
    private javax.swing.JLabel lblBackround;
    private javax.swing.JLabel lblError, lblTitle;
    private JButton btnExit;
    // End of variables declaration                   

}
