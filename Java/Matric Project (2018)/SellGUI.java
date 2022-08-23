package ransom;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *GUI where the you sell pokemon(no slave trade)
 * @author craig
 */
public class SellGUI extends javax.swing.JFrame implements Runnable
{
    
    private Player player;
    private JButton btnSprite[];
    private JTextArea txaStats[];
    private JLabel lblBackroubd;
    private JScrollPane scrollPane[];
    private JButton btnExit;
    private JLabel lblTitle, lblGold;
    
    /**
     *
     * @param player
     */
    public SellGUI(Player player)
    {
        this.player=player;
    }
                     
    private void initComponents()
    {
        try
        {
            int pokes=player.getNumPokes();

            
            setDefaultCloseOperation( javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Stats");
            setMinimumSize(new java.awt.Dimension(650, 400));
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            setSize(new java.awt.Dimension(800, 500));
            setLocationRelativeTo(null);
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(true);
            
            lblBackroubd = new JLabel();
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\storeBackround.png")));
            BufferedImage resizedImg = new BufferedImage(800, 500, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,800, 500, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblBackroubd.setIcon(image);
            
            btnSprite= new JButton[pokes];
            txaStats= new JTextArea[pokes];
            scrollPane= new JScrollPane[pokes];
            btnExit = new JButton();
            lblTitle = new JLabel();
            lblGold = new JLabel();
            
            lblTitle.setFont(new java.awt.Font("SansSerif", 1, 22));
            lblTitle.setForeground(new java.awt.Color(255, 255, 255));//Colour of text
            lblTitle.setText("Pokemon Selling Corporation");
            
            lblGold.setFont(new java.awt.Font("SansSerif", 1, 22));
            lblGold.setForeground(new java.awt.Color(255, 255, 255));//Colour of text
            lblGold.setText("Gold: "+player.getGold());
            
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
                txaStats[i] = new JTextArea();
                txaStats[i].setEditable(false);
                scrollPane[i] = new JScrollPane();
                scrollPane[i].setViewportView(txaStats[i]);
                scrollPane[i].setWheelScrollingEnabled(true);
                scrollPane[i].setVisible(true);
                scrollPane[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollPane[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                
                btnSprite[i] = new JButton();
                btnSprite[i].setActionCommand(i+"");
                btnSprite[i].addActionListener(new ActionListener()
                {
                    public void actionPerformed(ActionEvent evt)
                    {
                        btnSpriteActionPerformed(evt);
                    }
                });
                int x=30, y=i;
                if(i%2!=0)
                {
                    x=430;
                    y=i-1;
                }

                add(btnSprite[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x, 35 +y*80, 135, 135));
                add(scrollPane[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x+140, 35 +y*80, 200, 135));
            }
            stats();
            add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 5, 400, 30));
            add(lblGold, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 5, 170, 30));
            add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 30, 30));
            
            getContentPane().add(lblBackroubd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 800, 500));
            
            
            
            
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
        player.save();
        player.game.setScreen(ID.Game);
        player.addMessage("Game saved");
        this.dispose();
    }
    
    private void btnSpriteActionPerformed(ActionEvent evt)
    {
        try
        {
            int pos=Integer.parseInt(evt.getActionCommand());
            if(player.getNumPokes()<2)
                return;
            int i=JOptionPane.showConfirmDialog(rootPane,"Are you sure you want to sell "+player.getPokemon(pos).getName()+" for "+player.getPokemon(pos).getPrice()+" Gold?", "Confirm Sale", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(i!=0)
                return;
            player.setGold(player.getGold()+player.getPokemon(pos).getPrice());
            String query="DELETE FROM POKEMONMOVES WHERE PLAYERID = "+player.getPlayerID()+"AND PLAYERPOKEMONID = "+player.getPokemon(pos).getPlayerPokeId();
            player.getDb().update(query);
            int high=player.getPokemon(0).getPlayerPokeId();
            boolean party=true;
            for (int j = 1; j < player.getNumPokes(); j++)
            {
               if(player.getPokemon(j).getPlayerPokeId()>high)
                   high=player.getPokemon(j).getPlayerPokeId();
            }
            ResultSet rs=player.getDb().query("SELECT PLAYERPOKEID FROM PLAYERPOKEMON ORDER BY PLAYERPOKEID DESC");
            if(rs.next())
                if(rs.getInt(1)>high)
                {
                    high=rs.getInt(1);
                    party=false;
                }
            if(party)
            {
                if(high!=player.getPokemon(pos).getPlayerPokeId())
                {
                    int j = -1;
                    boolean found=false;
                    while(!found&& j < player.getNumPokes())
                    {
                        j++;
                        if(player.getPokemon(j).getPlayerPokeId()==high)
                            found=true;
                    }
                    player.getDb().update("DELETE FROM POKEMONMOVES WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEMONID = "+player.getPokemon(pos).getPlayerPokeId());
                    player.getDb().update("UPDATE POKEMONMOVES SET \"PLAYERPOKEMONID\" = "+player.getPokemon(pos).getPlayerPokeId()+" WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEMONID = "+ player.getPokemon(j).getPlayerPokeId());
                    int temp=player.getPokemon(j).getPlayerPokeId();
                    player.getPokemon(j).setPlayerPokeId(player.getPokemon(pos).getPlayerPokeId());
                    player.getPokemon(pos).setPlayerPokeId(temp); 
                }
            }
            else
            {
                player.getDb().update("DELETE FROM POKEMONMOVES WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEMONID = "+player.getPokemon(pos).getPlayerPokeId());
                player.getDb().update("UPDATE POKEMONMOVES SET \"PLAYERPOKEMONID\" = "+player.getPokemon(pos).getPlayerPokeId()+" WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEMONID = "+ high);
                player.getDb().update("UPDATE PLAYERPOKEMON SET \"PLAYERPOKEID\" = "+player.getPokemon(pos).getPlayerPokeId()+" WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEID = "+ high);
            }
            
            player.getPokes()[pos]=null;
            player.save();
            lblGold.setText("Gold: "+player.getGold());
            stats();
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }
    
    
    
    private void stats()
    {
        try
        {
            Pokemon[] pokemon=player.getPokes();
            for (int i = 0; i < player.getNumPokes(); i++)
            {
                ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pokemon[i].getPokeID()+".png")));
                BufferedImage resizedImg = new BufferedImage(135, 135, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,135, 135, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnSprite[i].setIcon(image);
                txaStats[i].setText(pokemon[i].getName()+"\nLevel:"+pokemon[i].getLvl()+"\nStage: "+pokemon[i].getStage()+"\nSells for " + pokemon[i].getPrice()+" Gold\nType");
                if(pokemon[i].getType1()==null)
                    txaStats[i].append(": "+pokemon[i].getType0());
                else
                    txaStats[i].append("s: "+pokemon[i].getType0()+"\n             "+pokemon[i].getType1());
            }
            for(int i=player.getNumPokes();i<btnSprite.length;i++)
            {
                 btnSprite[i].setVisible(false);
                 txaStats[i].setVisible(false);
                 scrollPane[i].setVisible(false);
            }
        }catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
}
