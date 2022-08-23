package ransom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *Used to see pokemon in storage and move them
 * @author craig
 */
public class PokeViewerGUI extends javax.swing.JFrame implements Runnable
{
    private Player player;
    private JButton[] btnStored, btnParty;
    private JButton btnExit;
    private JButton btnPageDown;
    private JButton btnPageUp;
    private ButtonGroup btngrpOrderBy;
    private JLabel lblBackround;
    private JButton btnCancell;
    private JLabel lblMoves;
    private JLabel lblOrderBy;
    private JLabel lblPages, lblPageNumber;
    private JLabel lblParty;
    private JLabel lblSelected;
    private JLabel lblSelectedPoke;
    private JLabel lblStats;
    private JLabel lblStored;
    private JRadioButton rbtnOrderBy[];
    private JScrollPane sclStats;
    private JSlider sdrStats;
    private JTextArea txaStats;
    private PokeViewer pv;
    private boolean selected,selectedParty;
    private Pokemon selectedPokemon;
    private int selectedPos;
    private int currentPage;
    private static final String ORDERBY[] = new String[]{"Level","Name","Stage","ID","Type"};
      
    public PokeViewerGUI(Player player)
    {
        this.player=player;
        
        
    }
                       
    private void initComponents()
    { 
        try
        {
            this.setResizable(false);
            this.setUndecorated(true);
            String query ="SELECT POKEMONID, PLAYERPOKEID FROM PLAYERPOKEMON WHERE PLAYERID =" +player.getPlayerID();
            pv = new PokeViewer(player.getDb().query(query));
            currentPage=1;
            setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            
            btnStored= new JButton[40];
            btnParty = new JButton[6];
            btngrpOrderBy = new javax.swing.ButtonGroup();
            btnExit = new javax.swing.JButton();
            btnPageUp = new javax.swing.JButton();
            btnPageDown = new javax.swing.JButton();
            rbtnOrderBy = new JRadioButton[5];
            lblSelectedPoke = new javax.swing.JLabel();
            sclStats = new javax.swing.JScrollPane();
            txaStats = new javax.swing.JTextArea();
            sdrStats = new javax.swing.JSlider();
            btnCancell = new javax.swing.JButton();
            lblOrderBy = new javax.swing.JLabel();
            lblMoves = new javax.swing.JLabel();
            lblStats = new javax.swing.JLabel();
            lblParty = new javax.swing.JLabel();
            lblStored = new javax.swing.JLabel();
            lblPages = new javax.swing.JLabel();
            lblPageNumber = new javax.swing.JLabel();
            lblSelected = new javax.swing.JLabel();
            lblBackround = new javax.swing.JLabel();

            lblPageNumber.setFont(new java.awt.Font("SansSerif", 1, 20));
            lblPageNumber.setForeground(Color.WHITE);
            pageNumber();
            getContentPane().add(lblPageNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(1085, 520, 100, 25));
            
            
            for (int i = 0; i < btnStored.length; i++)
            {
                
                btnStored[i]= new JButton();
                btnStored[i].addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        btnPokemonActionPerformed(evt);
                    }
                });
                ImageIcon image;
                if(pv.getCurrentPokemon(i)==null)
                {
                    image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png")));
                }
                else
                {
                    image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getCurrentPokemon(i).substring(0, pv.getCurrentPokemon(i).indexOf(","))+".png")));
                }
                
                BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnStored[i].setIcon(image);
 
                getContentPane().add(btnStored[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(210+110*(i%10), 50+110*(i/10), 100, 100));
                
            }
            
            
            for (int i = 0; i < rbtnOrderBy.length; i++)
            {
                rbtnOrderBy[i]= new JRadioButton();
                btngrpOrderBy.add(rbtnOrderBy[i]);
                rbtnOrderBy[i].setFont(new java.awt.Font("SansSerif", 0, 12));
                rbtnOrderBy[i].addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        rbtnOrderByActionPerformed(evt);
                    }
                });
                rbtnOrderBy[i].setText(ORDERBY[i]);
                add(rbtnOrderBy[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 560+i*35, 70, 25));
            }
            
            for (int i = 0; i < btnParty.length; i++)
            {
                btnParty[i]= new JButton();
                if(i<player.getNumPokes())
                {
                    ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokes()[i].getPokeID()+".png")));
                    BufferedImage resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = resizedImg.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(image.getImage(), 0, 0,110, 110, null);
                    g2.dispose();
                    image.setImage(resizedImg);   
                    btnParty[i].setIcon(image);
                }
                else
                {
                    btnParty[i].setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png"))));
                }
                btnParty[i].addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        btnPokemonActionPerformed(evt);
                    }
                });
                
                getContentPane().add(btnParty[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(29, 29+i*120, 110, 110));
            }
            
            btnExit.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\exitButton.png"))));
            btnExit.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnExitActionPerformed(evt);
                }
            });
            getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(1330, 6, 30, 30));
                 
            btnPageUp.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
            btnPageUp.setText(">>");
            btnPageUp.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnPageUpActionPerformed(evt);
                }
            });
            getContentPane().add(btnPageUp, new org.netbeans.lib.awtextra.AbsoluteConstraints(1280, 520, 50, 25));
            
            btnPageDown.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
            btnPageDown.setText("<<");
            btnPageDown.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnPageDownActionPerformed(evt);
                }
            });
            getContentPane().add(btnPageDown, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 520, 50, 25));
                                                                                               
            selected=false;
            
            lblSelectedPoke.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png"))));
            getContentPane().add(lblSelectedPoke, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 530, 200, 200));
            
            txaStats.setColumns(20);
            txaStats.setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
            txaStats.setRows(5);
            txaStats.setEditable(false);
            sclStats.setViewportView(txaStats);
            
            getContentPane().add(sclStats, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 530, 450, 160));
            
            sdrStats.setMajorTickSpacing(1);
            sdrStats.setMaximum(1);
            sdrStats.setValue(0);
            sdrStats.addChangeListener(new ChangeListener(){
                @Override
                public void stateChanged(ChangeEvent e)
                {
                    jsSliderStateChanged(e);
                }
            });
            getContentPane().add(sdrStats, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 700, 150, -1));
            
            btnCancell.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
            btnCancell.setText("Cancel");
            btnCancell.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnCancellActionPerformed(evt);
                }
            });
            getContentPane().add(btnCancell, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 710, 90, -1));
            
            lblOrderBy.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
            lblOrderBy.setForeground(Color.WHITE);
            lblOrderBy.setText("Order By:");
            getContentPane().add(lblOrderBy, new org.netbeans.lib.awtextra.AbsoluteConstraints(1050, 560, -1, -1));
            
            lblMoves.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
            lblMoves.setForeground(Color.WHITE);
            lblMoves.setText("Moves");
            getContentPane().add(lblMoves, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 700, -1, -1));
            
            lblStats.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
            lblStats.setForeground(Color.WHITE);
            lblStats.setText("Stats");
            getContentPane().add(lblStats, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 700, -1, -1));
            
            lblParty.setBackground(new java.awt.Color(255, 102, 51));
            
            getContentPane().add(lblParty, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 150, 730));
            
            lblStored.setBackground(new java.awt.Color(153, 0, 0));
            lblStored.setForeground(new java.awt.Color(153, 0, 0));
            
            getContentPane().add(lblStored, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 1110, 450));
            
            lblPages.setBackground(new java.awt.Color(153, 0, 51));

            getContentPane().add(lblPages, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 510, 410, 240));
                                                                                                            
            lblSelected.setBackground(new java.awt.Color(204, 51, 0));
            
            getContentPane().add(lblSelected, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 510, 730, 240));
            
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\computerBackround.png")));
            BufferedImage resizedImg = new BufferedImage(1370, 770, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,1370, 770, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblBackround.setIcon(image);
            
            getContentPane().add(lblBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 770));         
            this.setVisible(true);
            pack();
            setLocationRelativeTo(null);
            this.setAlwaysOnTop(true);
            
        } // </editor-fold>
        catch (SQLException | IOException ex)
        {
            System.out.println(ex);        }
    }

    private void moves(Pokemon pokemon)
    {
        txaStats.setText(pokemon.getName()+"\n");
        for (int j = 0; j < pokemon.getNumMoves(); j++)
        {
            txaStats.append(pokemon.getMove(j).toString()+"\n\n");
            txaStats.setCaretPosition(0);
        }
    }
    
    private void stats(Pokemon pokemon)
    {
        txaStats.setText(pokemon.getName()+"\nLevel:"+pokemon.getLvl()+"\n");
        int cur[]=pokemon.getCurrent();
        String temp="HP:"+cur[0]+"/"+pokemon.getTotal()[0];
        temp+="\nSpeed: "+cur[1];
        temp+="\nAttack:"+cur[2];
        temp+="\nSpecial Attack: "+cur[3];
        temp+="\nDefense: "+cur[4];
        temp+="\nSpecial Defense "+cur[5];
        txaStats.append(temp+"\n");
        txaStats.setCaretPosition(0);
    }
    
    private void rbtnOrderByActionPerformed(java.awt.event.ActionEvent evt)                                          
    {           
        try
        {
            String query ="SELECT POKEMONID, PLAYERPOKEID FROM PLAYERPOKEMON JOIN POKEMON ON POKEMON.ID = PLAYERPOKEMON.POKEMONID WHERE PLAYERID ="+player.getPlayerID()+" ORDER BY ";
            switch(evt.getActionCommand())
            {
                case "Level":
                    query+="\"LEVEL\"";
                    break;
                case "Name":
                    query+="\"NAME\"";
                    break;
                case"Stage":
                    query+="STAGE";
                    break;
                case"ID":
                    query+="POKEMON.ID";
                    break;
                case"Type":
                    query+="POKEMON.TYPE0, POKEMON.TYPE1";
                    break;
            }
            pv = new PokeViewer(player.getDb().query(query));
            reOrder();
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }                                         

    private void btnPageDownActionPerformed(java.awt.event.ActionEvent evt)                                            
    {                                                
        if(currentPage==1)
            currentPage=pv.getPages();
        else
            currentPage--;
        reloadStorage();
    }    
    
    private void btnPageUpActionPerformed(java.awt.event.ActionEvent evt)                                            
    {                                                
        if(currentPage==pv.getPages())
            currentPage=1;
        else
            currentPage++;
        reloadStorage();
    }  

    private void reloadStorage()
    {
        try
        {
            pageNumber();
            for (int i = 0; i < btnStored.length; i++)
            {
                ImageIcon image;
                if(pv.getCurrentPokemon(i+40*(currentPage-1))==null)
                {
                    image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png")));
                }
                else
                {
                    image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getCurrentPokemon(i+40*(currentPage-1)).substring(0, pv.getCurrentPokemon(i+40*(currentPage-1)).indexOf(","))+".png")));
                }
                BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnStored[i].setIcon(image);
            }
        }catch (IOException ex)
        {
            System.out.println(ex);}
    }
    private void btnCancellActionPerformed(java.awt.event.ActionEvent evt)                                           
    {                  
        if(selected)
        {
            try {
                txaStats.setText("");
                lblSelectedPoke.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png"))));
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
        selected=false;
    }                                          

    private void btnPokemonActionPerformed(java.awt.event.ActionEvent evt)                                           
    { 
        String temp=evt.toString();
        if(temp.indexOf("100x100")!=-1)//storage
        {
            try 
            {
                temp=temp.substring(temp.indexOf("[,")+2, temp.indexOf(",100x100"));
                int x=Integer.parseInt(temp.substring(0, temp.indexOf(",")));
                x-=210;
                x/=110;
                int y=Integer.parseInt(temp.substring(temp.indexOf(",")+1));
                y-=50;
                y/=110;
                int pos=10*y+x;
                if(pv.getCurrentPokemon(pos+40*(currentPage-1))==null)
                {
                    if(selected)
                    {
                        if(selectedParty)//party to empty storage
                        {
                             if(player.getNumPokes()==1)
                             {
                                 btnCancell.doClick();
                                 return;
                             }
                             else
                             {
                                try
                                {
                                    String query="INSERT INTO PLAYERPOKEMON (PLAYERID, POKEMONID, \"LEVEL\", EXP, PLAYERPOKEID, LEVELSTATS) VALUES ("+player.getPlayerID()+","+selectedPokemon.getPokeID()+"," +selectedPokemon.getLvl()+", "+selectedPokemon.getExp()+", "+selectedPokemon.getPlayerPokeId()+", "+selectedPokemon.getLevelStats()+")";
                                    player.getDb().update(query);
                                    player.getPokes()[selectedPos]=null;
                                    pv.setPokemon(pos+40*(currentPage-1), (selectedPokemon.getPokeID()+","+selectedPokemon.getPlayerPokeId()));
                                    player.setNumPokes(player.getNumPokes()-1);
                                    player.save();

                                    ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+selectedPokemon.getPokeID()+".png")));
                                    BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                                    Graphics2D g2 = resizedImg.createGraphics();
                                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                    g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                                    g2.dispose();
                                    image.setImage(resizedImg);   
                                    btnStored[pos].setIcon(image);
                                    
                                    player.compact();
                                    for (int i = 0; i < btnParty.length; i++)
                                    {
                                        if(i<player.getNumPokes())
                                        {
                                            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokes()[i].getPokeID()+".png")));
                                            resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
                                            g2 = resizedImg.createGraphics();
                                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                            g2.drawImage(image.getImage(), 0, 0,110, 110, null);
                                            g2.dispose();
                                            image.setImage(resizedImg);   
                                            btnParty[i].setIcon(image);
                                        }
                                        else
                                            btnParty[i].setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png"))));
                                    }

                                } catch (SQLException | IOException ex)
                                {
                                    System.out.println(ex);
                                }
                             }
                         }
                         else//storage to empty storage
                         {
                            try
                            {
                                pv.swop(pos+40*(currentPage-1),selectedPos);
                                ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getPokeID(pos+40*(currentPage-1))+".png")));
                                BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                                Graphics2D g2 = resizedImg.createGraphics();
                                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                                g2.dispose();
                                image.setImage(resizedImg);   
                                btnStored[pos].setIcon(image);

                                image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getPokeID(selectedPos)+".png")));
                                resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                                g2 = resizedImg.createGraphics();
                                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                                g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                                g2.dispose();
                                image.setImage(resizedImg);   
                                btnStored[selectedPos].setIcon(image);
                            } catch (IOException ex)
                            {
                                System.out.println(ex);}
                        }
                    }
                    btnCancell.doClick();
                }
                else
                {
                    String t[]=pv.getCurrentPokemon(pos+40*(currentPage-1)).split(",");
                    ResultSet rs = player.getDb().query("SELECT PLAYERPOKEID,POKEMONID,\"LEVEL\",EXP, STAGE, \"NAME\",TYPE0,TYPE1,LEVELSTATS, EVOLVE FROM PLAYERPOKEMON JOIN POKEMON ON POKEMONID = ID WHERE PLAYERID="+player.getPlayerID()+" AND PLAYERPOKEID="+t[1]);
                    ResultSet move= player.getDb().query("SELECT \"NAME\", \"TYPE\", DAMAGE, ACCURACY, SPECIAL, ID, MOVES.PP, POKEMONMOVES.PP FROM MOVES JOIN POKEMONMOVES ON MOVES.ID = POKEMONMOVES.MOVEID WHERE POKEMONMOVES.PLAYERID ="+player.getPlayerID()+" AND POKEMONMOVES.PLAYERPOKEMONID = "+t[1]);
                    Move moves[]=new Move[4];
                    int i=0;

                    while(move.next()&&i<4)
                    {
                        ResultSet rsEffect = player.getDb().query("SELECT EFFECT FROM MOVESEFFECTS WHERE ID = "+move.getInt("ID"));
                        LinkedList<String> eff = new LinkedList<String>();
                        while(rsEffect.next())
                        {
                            eff.add(rsEffect.getString(1));
                        }
                        String [] effects = new String[eff.size()];
                        for (int j = 0; j < effects.length; j++)
                        {
                            effects[j]= eff.get(j);
                        }
                        moves[i]=new Move(move.getString(1),move.getString(2),move.getInt(3),move.getInt(4),move.getBoolean(5),move.getInt(6),move.getInt(7),move.getInt(8), effects);
                        i++;
                    }
                    rs.next();
                    selectPokemon(new Pokemon(player.getDb(),rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getInt(9),rs.getString(7),rs.getString(8),moves,i,rs.getString("EVOLVE")),false,pos+40*(currentPage-1));
                }
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        else//party
        {
            temp=temp.substring(temp.indexOf("[,")+5, temp.indexOf(",110x110"));
            int pos = Integer.parseInt(temp)-29;
            pos/=120;
            if(pos>=player.getNumPokes())
            {
                if(selected)
                {
                    if(selectedParty)//party to empty party
                    {                     
                    }
                    else//storage to empty party
                    {
                        try
                        {
                            String query="DELETE FROM PLAYERPOKEMON WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEID = "+selectedPokemon.getPlayerPokeId();
                            player.getDb().update(query);
                            player.getPokes()[pos]=selectedPokemon;
                            pv.setPokemon(selectedPos, null);
                            player.setNumPokes(player.getNumPokes()+1);
                            player.save();
                            
                            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+selectedPokemon.getPokeID()+".png")));
                            BufferedImage resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D g2 = resizedImg.createGraphics();
                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            g2.drawImage(image.getImage(), 0, 0,110, 110, null);
                            g2.dispose();
                            image.setImage(resizedImg);   
                            btnParty[player.getNumPokes()-1].setIcon(image);

                            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png")));
                            resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                            g2 = resizedImg.createGraphics();
                            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                            g2.dispose();
                            image.setImage(resizedImg);   
                            btnStored[selectedPos].setIcon(image);

                        } catch (SQLException | IOException ex)
                        {
                            System.out.println(ex);
                        }
                    }
                }
                else
                {
                    
                }
                btnCancell.doClick();
            }
            else
                selectPokemon(player.getPokemon(pos),true,pos);
        }
        
    } 
    
    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)                                           
    {                                        
        player.save();
        player.game.setScreen(ID.Game);
        player.addMessage("Game saved");
        this.dispose();
    } 

    @Override
    public void run()
    {
        initComponents();
    }
    
    private void reOrder()
    {
        btnCancell.doClick();
        try
        {
            for (int i = 0; i < btnStored.length; i++)
            {
                ImageIcon image= (ImageIcon)btnStored[i].getIcon();
                if(pv.getCurrentPokemon(i+40*(currentPage-1))==null)
                {
                    image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\0.png")));
                }
                else
                {
                    try
                    {
                        image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getCurrentPokemon(i+40*(currentPage-1)).substring(0, pv.getCurrentPokemon(i+40*(currentPage-1)).indexOf(","))+".png")));
                    } catch (IOException ex)
                    {
                        Logger.getLogger(PokeViewerGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                g2.dispose();
                image.setImage(resizedImg);
                btnStored[i].setIcon(image);
                pageNumber();
                                
            }
        }catch(IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    private void selectPokemon(Pokemon pokemon, boolean party, int pos)
    {
        try
        {
            if(sdrStats.getValue()==0)//stats
                stats(pokemon);
            else//moves
                moves(pokemon);
            lblSelectedPoke.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pokemon.getPokeID()+".png"))));
            if(selected)
            {
                if(selectedParty)
                {
                    if(party)//party to party
                    {
                        player.swop(pos,selectedPos);
                        ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokes()[pos].getPokeID()+".png")));
                        BufferedImage resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = resizedImg.createGraphics();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.drawImage(image.getImage(), 0, 0,110, 110, null);
                        g2.dispose();
                        image.setImage(resizedImg);   
                        btnParty[pos].setIcon(image);
                        
                        image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokes()[selectedPos].getPokeID()+".png")));
                        resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
                        g2 = resizedImg.createGraphics();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.drawImage(image.getImage(), 0, 0,110, 110, null);
                        g2.dispose();
                        image.setImage(resizedImg);   
                        btnParty[selectedPos].setIcon(image);
                    }
                    else//storage to party
                    {
                        swopPartyStorage(selectedPokemon,selectedPos,pokemon,pos);
                    }
                }
                else
                {
                    if(party)//party to storage
                    {
                        swopPartyStorage(pokemon,pos,selectedPokemon,selectedPos);
                    }
                    else//storage to storage
                    {
                        pv.swop(pos,selectedPos);
                        ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getPokeID(pos)+".png")));
                        BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D g2 = resizedImg.createGraphics();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                        g2.dispose();
                        image.setImage(resizedImg);   
                        btnStored[pos].setIcon(image);
                        
                        image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getPokeID(selectedPos)+".png")));
                        resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                        g2 = resizedImg.createGraphics();
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g2.drawImage(image.getImage(), 0, 0,100, 100, null);
                        g2.dispose();
                        image.setImage(resizedImg);   
                        btnStored[selectedPos].setIcon(image);
                    }
                }
                btnCancell.doClick();
            }
            else
            {
                selected=true;
                selectedParty=party;
                selectedPokemon=pokemon;
                selectedPos= pos;
            }

        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    private void swopPartyStorage(Pokemon party, int pPos, Pokemon storage, int sPos)
    {
        try
        {
            String query="DELETE FROM PLAYERPOKEMON WHERE PLAYERID = "+player.getPlayerID()+" AND PLAYERPOKEID = "+storage.getPlayerPokeId();
            player.getDb().update(query);
            query="INSERT INTO PLAYERPOKEMON (PLAYERID, POKEMONID, \"LEVEL\", EXP, PLAYERPOKEID, LEVELSTATS) VALUES ("+player.getPlayerID()+","+party.getPokeID()+"," +party.getLvl()+", "+party.getExp()+", "+party.getPlayerPokeId()+", "+party.getLevelStats()+")";
            player.getDb().update(query);
            player.getPokes()[pPos]=storage;
            pv.setPokemon(sPos, (party.getPokeID()+","+party.getPlayerPokeId()) );
            player.save();
            
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+pv.getPokeID(sPos)+".png")));
            BufferedImage resizedImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,100, 100, null);
            g2.dispose();
            image.setImage(resizedImg);   
            btnStored[sPos].setIcon(image);

            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+player.getPokemon(pPos).getPokeID()+".png")));
            resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,110, 110, null);
            g2.dispose();
            image.setImage(resizedImg);   
            btnParty[pPos].setIcon(image);
        
        } catch (SQLException | IOException ex)
        {
            System.out.println(ex);
        }
        
    }
    private void pageNumber()
    {
        lblPageNumber.setText("Page "+currentPage+"/"+pv.getPages());
    }
    
    private void jsSliderStateChanged(ChangeEvent e)
    {
        if(selected)
        {
            if(selectedPokemon==null)
                return;
            if(((JSlider)e.getSource()).getValue()==0)
                stats(selectedPokemon);
            else
                moves(selectedPokemon);
        }
    }
    
}
