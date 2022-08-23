package ransom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *Allows the player to view their pokemon's stats and moves
 * @author craig
 */
public class PokemonStatsGUI extends javax.swing.JFrame implements Runnable
{
    
    private Player player;
    private JProgressBar pbExp[];
    private JProgressBar pbHP[];
    private JButton btnSprite[];
    private JTextArea txaStats[];
    private JLabel lblBackroubd;
    private JScrollPane scrollPane[];
    private JButton btnExit;
    private JSlider jsSlider;
    private JLabel lblMoves,lblStats;
    private int selectedPos;
    private BattleGUI battleGUI;
    
    public PokemonStatsGUI(Player player)
    {
        this.player=player;
    }
    
    public PokemonStatsGUI(Player player, BattleGUI battleGUI)
    {
        this.player=player;
        this.battleGUI=battleGUI;
    }
                     
    private void initComponents()
    {
        try
        {
            UIManager.put("ProgressBar.foreground",Color.cyan);
            UIManager.put("ProgressBar.selectionBackground", Color.WHITE);
            UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
            selectedPos=-1;
            int pokes=player.getNumPokes();
            lblBackroubd = new  JLabel();
            
            setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Stats");
            setMinimumSize(new java.awt.Dimension(650, 400));
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            setSize(new java.awt.Dimension(800, 500));
            setLocationRelativeTo(null);
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(true);
            
            lblBackroubd.setBackground(new java.awt.Color(102, 102, 102));
            lblBackroubd.setOpaque(true);
            pbExp=new JProgressBar[pokes];
            pbHP=new JProgressBar[pokes];
            btnSprite= new JButton[pokes];
            txaStats= new JTextArea[pokes];
            scrollPane= new JScrollPane[pokes];
            btnExit = new JButton();
            jsSlider = new JSlider();
            lblMoves = new JLabel();
            lblStats = new JLabel();
            
            lblMoves.setFont(new java.awt.Font("SansSerif", 0, 20)); 
            lblMoves.setForeground(new java.awt.Color(255, 255, 255));//Colour of text
            lblMoves.setText("Moves");
            
            lblStats.setFont(new java.awt.Font("SansSerif", 0, 20));
            lblStats.setForeground(new java.awt.Color(255, 255, 255));//Colour of text
            lblStats.setText("Stats");
            
            jsSlider.setMajorTickSpacing(1);
            jsSlider.setMaximum(1);
            jsSlider.setMinorTickSpacing(1);
            jsSlider.setValue(0);
            jsSlider.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    jsSliderStateChanged(e);
                }
            });
            
            btnExit.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnExitActionPerformed(evt);
                }
            });
            btnExit.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\exitButton.png"))));
            Pokemon[] pokemon= player.getPokes();
            for (int i = 0; i < pokes; i++)
            {
                pbExp[i] = new JProgressBar();
                pbExp[i].setMaximum(pokemon[i].getExpToLevel());
                pbExp[i].setValue(pokemon[i].getExp());
                pbExp[i].setBackground(new Color(50,120,150));
                pbExp[i].setOpaque(true);
                pbExp[i].setStringPainted(true);
                pbExp[i].setString(pbExp[i].getValue()+"/"+pbExp[i].getMaximum());
            }
            UIManager.put("ProgressBar.foreground",Color.GREEN);
            UIManager.put("ProgressBar.selectionBackground", Color.BLACK);
            for (int i = 0; i < pokes; i++)
            {
                pbHP[i] = new JProgressBar();
                pbHP[i].setMaximum(pokemon[i].getTotal(0));
                pbHP[i].setValue(pokemon[i].getCurrent(0));
                pbHP[i].setOpaque(true);
                pbHP[i].setStringPainted(true);
                pbHP[i].setString(pbHP[i].getValue()+"/"+pbHP[i].getMaximum());

                txaStats[i] = new JTextArea();
                txaStats[i].setEditable(false);
                scrollPane[i] = new JScrollPane();
                scrollPane[i].setViewportView(txaStats[i]);
                scrollPane[i].setWheelScrollingEnabled(true);
                scrollPane[i].setVisible(true);
                scrollPane[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                
                btnSprite[i] = new JButton();
                ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pokemon[i].getPokeID()+".png")));
                BufferedImage resizedImg = new BufferedImage(135, 135, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,135, 135, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnSprite[i].setIcon(image);
                btnSprite[i].setOpaque(true);
                btnSprite[i].addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        btnSpriteActionPerformed(evt);
                    }
                });
                btnSprite[i].setActionCommand(i+"");
                int x=30, y=i;
                if(i%2!=0)
                {
                    x=430;
                    y=i-1;
                }

                add(btnSprite[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x, 35 +y*80, 135, 135));
                add(pbHP[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x+140, 35 +y*80, 200, 20));
                add(pbExp[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x+140, 55 +y*80, 200, 12));
                add(scrollPane[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x+140, 70 +y*80, 200, 100));
            }
            stats(player.getPokes());
            add(jsSlider, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 5, 200, 25));
            add(lblMoves, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 5, 60, 20));
            add(lblStats, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 5, 50, 20));
            
            add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 30, 30));
            
            getContentPane().add(lblBackroubd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 510));
            
            
            
            
        } catch (IOException ex)
        {
            player.addMessage(ex.toString());
        }
    }

    @Override
    public void run()
    {
        initComponents();
        this.setVisible(true);
    }
    
    private void btnExitActionPerformed(ActionEvent evt)
    {
        if(battleGUI==null)
            player.game.setScreen(ID.Game);
        else
            battleGUI.resume();
        this.dispose();
    }
    
    private void btnSpriteActionPerformed(ActionEvent evt)
    {
         try 
         {
            int pos=Integer.parseInt(evt.getActionCommand());
            if(selectedPos>=0)
            {
                player.swop(pos, selectedPos);
                if(jsSlider.getValue()==0)
                    stats(player.getPokes());
                else
                    moves(player.getPokes());

                pbExp[pos].setMaximum(player.getPokemon(pos).getExpToLevel());
                pbExp[pos].setValue(player.getPokemon(pos).getExp());
                pbExp[pos].setString(pbExp[pos].getValue()+"/"+pbExp[pos].getMaximum());
                pbExp[selectedPos].setMaximum(player.getPokemon(selectedPos).getExpToLevel());
                pbExp[selectedPos].setValue(player.getPokemon(selectedPos).getExp());
                pbExp[selectedPos].setString(pbExp[selectedPos].getValue()+"/"+pbExp[selectedPos].getMaximum());

                pbHP[pos].setMaximum(player.getPokemon(pos).getTotal(0));
                pbHP[pos].setValue(player.getPokemon(pos).getCurrent(0));
                pbHP[pos].setString(pbHP[pos].getValue()+"/"+pbHP[pos].getMaximum());
                pbHP[selectedPos].setMaximum(player.getPokemon(selectedPos).getTotal(0));
                pbHP[selectedPos].setValue(player.getPokemon(selectedPos).getCurrent(0));
                pbHP[selectedPos].setString(pbHP[selectedPos].getValue()+"/"+pbHP[selectedPos].getMaximum());
                
                ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokemon(pos).getPokeID()+".png")));
                BufferedImage resizedImg = new BufferedImage(135, 135, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,135, 135, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnSprite[pos].setIcon(image);

                image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokemon(selectedPos).getPokeID()+".png")));
                resizedImg = new BufferedImage(135, 135, BufferedImage.TYPE_INT_ARGB);
                g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,135, 135, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnSprite[selectedPos].setIcon(image);              
                btnSprite[selectedPos].setEnabled(true);
                selectedPos=-1;
            }
            else
            {
                selectedPos=pos;
                btnSprite[selectedPos].setEnabled(false);
            }
                
        } catch (IOException ex)
        {
             System.out.println(ex);
        }
    }
    
    private void jsSliderStateChanged(ChangeEvent e)
    {
        
        if(((JSlider)e.getSource()).getValue()==0)
            stats(player.getPokes());
        else
            moves(player.getPokes());
    }
    
    private void moves(Pokemon [] pokemon)
    {
        for (int i = 0; i < player.getNumPokes(); i++)
            {
                txaStats[i].setText(pokemon[i].getName()+"\n");
                for (int j = 0; j < pokemon[i].getNumMoves(); j++)
                {
                    txaStats[i].append(pokemon[i].getMove(j).toString()+"\n\n");
                    txaStats[i].setCaretPosition(0);
                }
            }
    }
    
    private void stats(Pokemon [] pokemon)
    {
        for (int i = 0; i < player.getNumPokes(); i++)
            {
                txaStats[i].setText(pokemon[i].getName()+"\nLevel:"+pokemon[i].getLvl()+"\n");
                int cur[]=pokemon[i].getCurrent();
                String temp="HP:"+cur[0]+"/"+pokemon[i].getTotal()[0];
                temp+="\nSpeed: "+cur[1];
                temp+="\nAttack:"+cur[2];
                temp+="\nSpecial Attack: "+cur[3];
                temp+="\nDefense: "+cur[4];
                temp+="\nSpecial Defense "+cur[5];
                txaStats[i].append(temp+"\n");
                txaStats[i].setCaretPosition(0);
            }
    }
}
