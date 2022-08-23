package ransom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *Battle GUI for server in networked battled
 * @author craig
 */
public class ClientBattleGUI extends Battle implements Runnable
{

    private int id, serverHPMax, serverHP, level;
    private String name, pokeName;
    private javax.swing.JButton btnMoves[];
    private javax.swing.JButton btnLeave;
    private javax.swing.JLabel lblEnemy;
    private javax.swing.JLabel lblEnemyInfo, lblInfo;
    private javax.swing.JLabel lblPoke;
    private javax.swing.JProgressBar pbHP;
    private javax.swing.JProgressBar pbHPEnemy;
    private int width,height;
    private javax.swing.JTextArea txaMessages; 
    private JScrollPane jspMessages;
    private ID area;
    private Network network;
    private Thread thread;
    
    public ClientBattleGUI(Player player, ID area, Socket sock)
    {
        super(player,1200,750,area);
        txaMessages = new javax.swing.JTextArea();
        network=new Network(sock,this);
        thread = new Thread(network);
        thread.start();
        String temp=player.getName();
        for (int i = 0; i < player.getNumPokes(); i++)
        {
            temp+="#";
            temp+=player.getPokemon(i).getPlayerPokeId()+","+player.getPokemon(i).getPokeID()+","+player.getPokemon(i).getLvl()+","+player.getPokemon(i).getLevelStats();
            for (int j = 0; j < player.getPokemon(i).getNumMoves(); j++)
                temp+=","+player.getPokemon(i).getMove(j).getID();
        }
        network.sendMessage(temp);
    }
    
    private void initComponents()
    {
        try
        {
            setTitle("Client");
            setMinimumSize(new java.awt.Dimension(width, height));
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            
            UIManager.put("ProgressBar.foreground",Color.cyan);
            UIManager.put("ProgressBar.selectionBackground", Color.WHITE);
            UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
            UIManager.put("ProgressBar.foreground",Color.green);
            lblPoke = new javax.swing.JLabel();
            lblEnemy = new javax.swing.JLabel();
            lblEnemyInfo= new javax.swing.JLabel();
            lblInfo= new javax.swing.JLabel();
            btnMoves= new JButton[4];
            pbHP = new javax.swing.JProgressBar();
            pbHPEnemy = new javax.swing.JProgressBar();
            lblBackroubd = new javax.swing.JLabel();
            btnLeave=new JButton();
            lblMovesBackround= new javax.swing.JLabel();
            jspMessages = new javax.swing.JScrollPane();
            
            txaMessages.setEditable(false);
            txaMessages.setColumns(20);
            txaMessages.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
            txaMessages.setRows(5);
            jspMessages.setViewportView(txaMessages);
            
            
            btnLeave.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnLeaveActionPerformed(evt);
                }
            });
            btnLeave.setFont(new java.awt.Font("SansSerif", 0, 12));
            btnLeave.setText("Leave Battle");
            
            for (int i = 0; i < btnMoves.length; i++)
            {
                btnMoves[i] = new JButton();
                btnMoves[i].setFont(new java.awt.Font("SansSerif", 0, 12));
                btnMoves[i].addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        moveActionPerformed(evt);
                    }
                });
            }
            
            lblMovesBackround.setOpaque(true);
            lblMovesBackround.setBackground(new Color(98, 103, 132));
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\battleBackround.png")));
            BufferedImage resizedImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,width, height, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblBackroubd.setIcon(image);
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+id+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblEnemy.setIcon(image);
            
            lblEnemyInfo.setFont(new java.awt.Font("SansSerif", 0, 14));
            lblEnemyInfo.setText(pokeName+" Level: "+level);
            lblEnemyInfo.setForeground(Color.BLACK);
            
            lblInfo.setFont( new java.awt.Font("SansSerif", 0, 14));
            lblInfo.setText(player.getActivePokemon().getName()+" Level: "+player.getActivePokemon().getLvl());
            lblInfo.setForeground(Color.BLACK);
            
            pbHP.setValue(player.getActivePokemon().getCurrent(0));
            pbHP.setMaximum(player.getActivePokemon().getTotal(0));
            pbHPEnemy.setMaximum(serverHPMax);
            pbHPEnemy.setValue(serverHP);
            pbHP.setString(pbHP.getValue()+"/"+pbHP.getMaximum());
            pbHPEnemy.setString(pbHPEnemy.getValue()+"/"+pbHPEnemy.getMaximum());
            pbHP.setBackground(Color.darkGray);
            pbHPEnemy.setBackground(Color.darkGray);
            pbHP.setOpaque(true);
            pbHPEnemy.setOpaque(true);
            pbHP.setStringPainted(true);
            pbHPEnemy.setStringPainted(true);
            
            
            updateScreen();
                    
            add(btnLeave, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 650, 130, 25));
            
            add(btnMoves[0], new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 650, 165, 25));
            add(btnMoves[1], new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 650, 165, 25));
            add(btnMoves[2], new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 685, 165, 25));
            add(btnMoves[3], new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 685, 165, 25));
            add(lblMovesBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 635, 380, 90));
            
            
            add(pbHPEnemy,new org.netbeans.lib.awtextra.AbsoluteConstraints(990,305, 150, 20));
            add(pbHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 530, 150, 20));
            add(lblPoke, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 375, 150, 150));
            add(lblEnemy, new org.netbeans.lib.awtextra.AbsoluteConstraints(990,150, 150, 150));
            add(lblEnemyInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(990,325, 150, 20));
            add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60,565, 150, 20));
            add(jspMessages, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 430, 430, 200));
            add(lblBackroubd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 750));
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getActivePokemon().getPokeID()+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblPoke.setIcon(image);
                       
            /*setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(false);*/
            setLocationRelativeTo(null);
            pack();
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    @Override
    public void updateScreen()
    {
        try
        {
            
            updateButtons();
            
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getActivePokemon().getPokeID()+".png")));
            BufferedImage resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblPoke.setIcon(image);
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+id+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblEnemy.setIcon(image);
            
            pbHP.setValue(player.getActivePokemon().getCurrent(0));
            pbHP.setMaximum(player.getActivePokemon().getTotal(0));
            pbHPEnemy.setMaximum(serverHPMax);
            pbHPEnemy.setValue(serverHP);
            pbHP.setString(pbHP.getValue()+"/"+pbHP.getMaximum());
            pbHPEnemy.setString(pbHPEnemy.getValue()+"/"+pbHPEnemy.getMaximum());
            lblInfo.setText(player.getActivePokemon().getName()+" Level: "+player.getActivePokemon().getLvl());
            lblEnemyInfo.setText(pokeName+" Level: "+level);
            
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    @Override
    public void run()
    {
        while(!network.isMessage())
        {
            System.out.print("1");
        }
        System.out.println("");
        System.out.println("recieved");
        String [] t=network.getMessage().split("#");
        name=t[0];
        id= Integer.parseInt(t[2]);
        pokeName=t[1];
        serverHP= Integer.parseInt(t[3]);
        serverHPMax= serverHP;
        level=Integer.parseInt(t[4]);
        initComponents();
        txaMessages.setText("A wild "+name+" appeared\n");
        this.setVisible(true);
    }
    public void disableButtons()
    {
        for (int i = 0; i < btnMoves.length; i++)
            btnMoves[i].setEnabled(false);
        
        btnLeave.setEnabled(false);
    }
    public void reEnbableButtons()
    {
        updateButtons();
        btnLeave.setEnabled(true);
    }
    public void updateButtons()
    {
        boolean move=false;
        for (int i = 0; i <4; i++)
        {
            btnMoves[i].setText("");
            btnMoves[i].setEnabled(false);
        }
        
        for (int i = 0; i < player.getActivePokemon().getNumMoves(); i++)
        { 
            btnMoves[i].setText(player.getActivePokemon().getMove(i).getName()+" "+player.getActivePokemon().getMove(i).getPpCurrent()+"/"+player.getActivePokemon().getMove(i).getPpMax());
            if(player.getActivePokemon().getMove(i).getPpCurrent()>0)
            {
                btnMoves[i].setEnabled(true);
                move=true;
            }
        }
        if(!move)
        {
            btnMoves[0].setText("Struggle");
            btnMoves[0].setEnabled(true);
        }
    }

    public void moveActionPerformed(ActionEvent evt)
    {
        int i=-1;
        while(i<0&&i>-5)//loop finding which move was used
        {
            if(evt.getSource().equals(btnMoves[4+i]))
                i+=4;
            else
                i--;
        }
        network.sendMessage(i+"");
        while(!network.isMessage()){}
        
        String temp[]=network.getMessage().split("#");
        if(temp.length==2)
        {
            serverHP-=Integer.parseInt(temp[0]);
            player.getActivePokemon().damage(Integer.parseInt(temp[1]));
        }
        else
        {
            serverHP-=Integer.parseInt(temp[0]);
            id=Integer.parseInt(temp[1]);
            player.getActivePokemon().damage(Integer.parseInt(temp[2]));
            level=Integer.parseInt(temp[3]);
            name=temp[4];
        }
        
        network.sendMessage("Recieved");
        while(network.isMessage()){}
        
        txaMessages.append(network.getMessage());
        updateScreen();
    }

    private void btnLeaveActionPerformed(ActionEvent evt)
    {
        player.game=new Game(player,area);
        network.stop();
        this.dispose();
    }
    
}
