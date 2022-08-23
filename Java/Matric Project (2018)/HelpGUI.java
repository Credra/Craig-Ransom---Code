/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *GUI which will display help
 * @author Craig
 */
public class HelpGUI extends javax.swing.JFrame implements Runnable
{
    
    private Player player;
    private JTextArea txaHelp;
    private JLabel lblBackround;
    private JScrollPane scpHelp;
    private JButton btnExit;
    private JButton btnHelp[];
    private JLabel lblTitle;

    public HelpGUI(Player player)
    {
        this.player=player;
    }

    private void initComponents()
    {
        try
        {
            setSize(new java.awt.Dimension(650, 430));
            setTitle("Help");
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            setLocationRelativeTo(null);
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(true);
            
            lblTitle = new javax.swing.JLabel();
            lblTitle.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
            lblTitle.setForeground(Color.black);
            lblTitle.setText("HELP SCREEN");
            add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 0, 130, 50));
            
            btnHelp = new JButton[6];
            for (int i = 0; i < 6; i++)
            {
                btnHelp[i]=new JButton();
                btnHelp[i].addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        btnHelpActionPerformed(evt);
                    }
                });
            }
            
            btnHelp[0].setText("Movement");
            btnHelp[1].setText("Pokemon");
            btnHelp[2].setText("Battle");
            btnHelp[3].setText("Saves & Exits");
            btnHelp[4].setText("Shop");
            btnHelp[5].setText("Connect");
            
            for (int i = 0; i < 6; i++)
            {
                add(btnHelp[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 60+i*60, 120, 50));
            }
            
            txaHelp = new JTextArea();
            txaHelp.setEditable(false);
            txaHelp.setColumns(20);
            txaHelp.setRows(5);
            scpHelp = new JScrollPane();
            scpHelp.setViewportView(txaHelp);
            scpHelp.setWheelScrollingEnabled(true);
            scpHelp.setVisible(true);
            scpHelp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scpHelp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            add(scpHelp, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 485, 350));
            
            lblBackround = new javax.swing.JLabel();
            lblBackround.setBackground(new java.awt.Color(0, 102, 102));
            lblBackround.setOpaque(true);
            add(lblBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 430));
            
            btnExit = new JButton();
            btnExit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnExitActionPerformed(evt);
                }
            });
            btnExit.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\exitButton.png"))));
            add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(650-45, 15, 30, 30));
            
            
        
           
        } catch (IOException ex)
        {
            txaHelp.setText("Exit Button image file not found");
        }
    }
    @Override
    public void run()
    {
        initComponents();
        setVisible(true);
    }
    
    private void btnHelpActionPerformed(ActionEvent evt)
    {
        try
        {
            String query= "SELECT HELP. DESCRIPTION FROM HELP WHERE HELP.TOPIC = '"+evt.getActionCommand()+"'";
            ResultSet rs = player.getDb().query(query);
            String temp="";
            if(rs.next())
            {
                temp=rs.getString(1);
                temp=escapeSequenceCheck(temp);
            }
            else
                temp="Button name and name in table don't match";
            txaHelp.setText(temp);
        } catch (SQLException ex)
        {
            txaHelp.setText(ex.toString());
        }
    }
    private String escapeSequenceCheck(String in)
    {
        int pos=in.indexOf("\\");
        if(pos==-1)
            return in;
        String temp="";
        int prev=0;
        do
        {
            temp+=in.substring(prev, pos);
            if(in.charAt(pos+1)=='n')
                temp+="\n";
            else if(in.charAt(pos+1)=='t')
                temp+="\t";
            prev=pos+2;
            pos=in.indexOf("\\",prev);
        }while(pos!=-1);
        temp+=in.substring(prev);
        return temp;
    }
    private void btnExitActionPerformed(ActionEvent evt)
    {
        player.game.setScreen(ID.Game);
        this.dispose();
    }
    
}