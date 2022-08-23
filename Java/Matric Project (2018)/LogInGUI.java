package ransom;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 *GUI used to log in, 1st GUI to be seen
 * @author craig
 */
public class LogInGUI extends javax.swing.JFrame implements Runnable
{
        //Game game = new Game("Craig",0,"Main Area",new DB(),100,10,5,1,0);game.setVisible(true);
    private Timer timer;
    private DB db;                               // 
    private static final int [] STARTERS=new int[]{1,4,7,71,74,77,103,106,109,147,150,153,220,223,226,239,242,245};
    private Music music;
    public LogInGUI()
    {
        db = new DB();
        initComponents();
        music = new Music();
        Thread t = new Thread(music);
        t.start();
        try
        {
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\exitButton.png")));
            btnExit.setIcon(image);
        } catch (FileNotFoundException ex)
        {
            System.out.println(ex);
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        btnExit = new javax.swing.JButton();
        lblPassword = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        pafPassword = new javax.swing.JPasswordField();
        txfUsername = new javax.swing.JTextField();
        lblPokeRight = new javax.swing.JLabel();
        lblPokeLeft = new javax.swing.JLabel();
        btnLogIn = new javax.swing.JButton();
        btnSignUp = new javax.swing.JButton();
        lblGreet = new javax.swing.JLabel();
        lblGreetShadow = new javax.swing.JLabel();
        lblError = new javax.swing.JLabel();
        lblBackrounnd = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pokemon JavaFire");
        setName(""); // NOI18N
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnExit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExitActionPerformed(evt);
            }
        });
        getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 10, 30, 30));

        lblPassword.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(255, 255, 255));
        lblPassword.setText("Password");
        getContentPane().add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 100, 30));

        lblUsername.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 18)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(255, 255, 255));
        lblUsername.setText("Username");
        getContentPane().add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 130, 100, 30));
        getContentPane().add(pafPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 180, 150, 30));
        getContentPane().add(txfUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 130, 150, 30));
        getContentPane().add(lblPokeRight, new org.netbeans.lib.awtextra.AbsoluteConstraints(475, 75, 100, 100));
        getContentPane().add(lblPokeLeft, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 75, 100, 100));

        btnLogIn.setBackground(new java.awt.Color(204, 255, 255));
        btnLogIn.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        btnLogIn.setForeground(new java.awt.Color(0, 51, 102));
        btnLogIn.setText("Log In");
        btnLogIn.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnLogInActionPerformed(evt);
            }
        });
        getContentPane().add(btnLogIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(475, 350, 100, 25));

        btnSignUp.setBackground(new java.awt.Color(204, 255, 255));
        btnSignUp.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 12)); // NOI18N
        btnSignUp.setForeground(new java.awt.Color(0, 51, 102));
        btnSignUp.setText("Sign Up");
        btnSignUp.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnSignUpActionPerformed(evt);
            }
        });
        getContentPane().add(btnSignUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 350, 100, 25));

        lblGreet.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 36)); // NOI18N
        lblGreet.setForeground(new java.awt.Color(51, 0, 102));
        lblGreet.setText("Pokemon JavaFire");
        getContentPane().add(lblGreet, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, -1, -1));

        lblGreetShadow.setFont(new java.awt.Font("MS Reference Sans Serif", 0, 36)); // NOI18N
        lblGreetShadow.setForeground(new java.awt.Color(255, 204, 204));
        lblGreetShadow.setText("Pokemon JavaFire");
        getContentPane().add(lblGreetShadow, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 21, -1, -1));

        lblError.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        getContentPane().add(lblError, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 230, 270, 90));

        lblBackrounnd.setBackground(new java.awt.Color(0, 51, 51));
        lblBackrounnd.setIcon(new javax.swing.ImageIcon("C:\\Users\\craig\\Documents\\NetBeansProjects\\JavaFire\\Sprites\\backround.png")); // NOI18N
        lblBackrounnd.setOpaque(true);
        getContentPane().add(lblBackrounnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 600, 400));

        setSize(new java.awt.Dimension(600, 400));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private boolean check()
    {
        boolean valid=true;
        if(txfUsername.getText().isEmpty()&&pafPassword.getText().isEmpty())
        {
            lblError.setText("Username and Password cannot be empty");
            valid=false;
        }
        else if(pafPassword.getText().isEmpty())
        {
            lblError.setText("Password cannot be empty");
            valid=false;    
        }
        else if(txfUsername.getText().isEmpty())
        {
            lblError.setText("Username cannot be empty");
            valid=false;  
        }
        return valid;
    }
    
    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnSignUpActionPerformed
    {//GEN-HEADEREND:event_btnSignUpActionPerformed
        if(check())
        {
            try {
                String username=txfUsername.getText(), password=pafPassword.getText();
                ResultSet rs=db.query("SELECT * FROM PLAYER WHERE username = '"+username+"'");
                
                if(rs.next())
                {
                    lblError.setText("Username already taken");
                }
                else
                {//INSERT INTO PLAYER (USERNAME, PASSWORD, PLAYERID, GOLD, POKEBALL, GREATBALL, ULTRABALL, SCREEN, CAVETICKETS) VALUES ('1', '1', 1, 25, 5, DEFAULT, DEFAULT, DEFAULT, DEFAULT)
                    rs=db.query("SELECT PLAYERID FROM PLAYER ORDER BY PLAYERID DESC");
                   int id=0;
                   if(rs.next())
                       id=1+rs.getInt(1);
                   db.update("INSERT INTO PLAYER (USERNAME, PASSWORD, PLAYERID, GOLD, POKEBALL, GREATBALL, ULTRABALL, SCREEN, CAVETICKETS) VALUES ('"+username+"', '"+password+"',"+id+", 25, 5, DEFAULT, DEFAULT, DEFAULT, DEFAULT)");
                   int pokeID=STARTERS[(int)(Math.random()*(STARTERS.length))];
                   String type="Grass";
                   if(pokeID%3==1)
                       type="Fire";
                   else if(pokeID%3==2)
                       type="Water";
                   
                   Pokemon p =new Pokemon( pokeID, "",5, 1, type,null, db, ("16,"+pokeID+1));
                   String query="INSERT INTO PARTY (PLAYERID, POKEMONID, \"LEVEL\", EXP, LEVELSTATS, PLAYERPOKEID, CURRENTSTATS) VALUES ("+id+", ";
                    query+=p.getPokeID()+", ";                                               /*                       */
                    query+=p.getLvl()+", ";
                    query+=p.getExp()+", ";
                    query+=p.getLevelStats()+" ,";
                    query+=p.getPlayerPokeId()+", '";
                    query+=p.getFormattedCurrent()+"')";
                    db.update(query);
                    
                    query="INSERT INTO POKEMONMOVES (PLAYERID, PLAYERPOKEMONID, MOVEID, PP) VALUES (";
                    query+=id+", ";
                    query+=p.getPlayerPokeId()+", ";
                    query+=p.getMove(0).getID()+", ";
                    query+=p.getMove(0).getPpCurrent()+")";
                    db.update(query);
                    Game game = new Game(username,id,"Main Area",db,25,5,0,0,0,music);
                    game.setVisible(true);
                    timer.setRun(false);
                    music.stop();
                    this.dispose();
                    //String name, int playerID, String location, DB db, int gold, int pokeball, int greatball, int ultraball,int cavePasses
                }
                
            } catch (SQLException ex) {
                lblError.setText("Could not connect to database");
            }
        }
    }//GEN-LAST:event_btnSignUpActionPerformed

    private void btnLogInActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnLogInActionPerformed
    {//GEN-HEADEREND:event_btnLogInActionPerformed
        if(check())
        {
            try {
                String username=txfUsername.getText();
                                                //1     2          3        4     5         6         7              8
                ResultSet rs=db.query("SELECT USERNAME, PLAYERID, SCREEN, GOLD, POKEBALL, GREATBALL, ULTRABALL, CAVETICKETS FROM PLAYER WHERE username = '"+username+"' AND password= '"+pafPassword.getText()+"'");

                if(rs.next())
                {
                    new Game(rs.getString(1),rs.getInt(2),rs.getString(3),db,rs.getInt(4),rs.getInt(5),rs.getInt(6),rs.getInt(7),rs.getInt(8),music).setVisible(true);
                    timer.setRun(false);
                    this.dispose();
                }
                else
                {
                    lblError.setText("Incorrect Username or Password");
                }
            } catch (SQLException ex) {
                lblError.setText("Could not connect to database");
            }
        }
    }//GEN-LAST:event_btnLogInActionPerformed

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExitActionPerformed
    {//GEN-HEADEREND:event_btnExitActionPerformed
        timer.setRun(false);
        music.stop();
        this.dispose();
    }//GEN-LAST:event_btnExitActionPerformed

    public static void main(String args[])
    {
        Thread t = new Thread(new LogInGUI());
        t.start();
    }
    
    @Override
    public void run()
    {
        timer=new Timer(this);
        Thread t = new Thread(timer);
        t.start();
        this.setVisible(true);
    }

    public void updateImage(int i, int j)
    {
        try
        {
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+i+".png")));
            BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,100, 100, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblPokeLeft.setIcon(image);
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+j+".png")));
            resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,100, 100, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblPokeRight.setIcon(image);
            
        } catch (IOException ex)
        {
            lblError.setText("Image path incorrect");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLogIn;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JLabel lblBackrounnd;
    private javax.swing.JLabel lblError;
    private javax.swing.JLabel lblGreet;
    private javax.swing.JLabel lblGreetShadow;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPokeLeft;
    private javax.swing.JLabel lblPokeRight;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField pafPassword;
    private javax.swing.JTextField txfUsername;
    // End of variables declaration//GEN-END:variables

    
}
