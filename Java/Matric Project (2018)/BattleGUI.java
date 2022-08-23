/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

/**
 *GUI which battle between wild pokemon takes place
 * @author craig
 */
public class BattleGUI extends Battle implements Runnable
{
    private Pokemon enemy, active;
    private javax.swing.JButton btnSwitch, btnPoke, btnGreat, btnUltra;
    private javax.swing.JProgressBar pbExp;
    
    /**
     *
     * @param player
     * @param area
     * @param enemy
     */
    public BattleGUI(Player player, ID area, Pokemon enemy)
    {
        super(player,1200,750,area);
        active=player.getActivePokemon();
        this.enemy=enemy;
        music=player.getMusic();
        Thread t = new Thread(music);
        music.battle();
        t.start();
    }
    
    private void initComponents()
    {
        try
        {
            setTitle("Battle");
            setMinimumSize(new java.awt.Dimension(width, height));
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            
            UIManager.put("ProgressBar.foreground",Color.cyan);
            UIManager.put("ProgressBar.selectionBackground", Color.WHITE);
            UIManager.put("ProgressBar.selectionForeground", Color.BLACK);
            pbExp = new javax.swing.JProgressBar();
            pbExp.setBackground(new Color(50,120,150));
            pbExp.setOpaque(true);
            pbExp.setStringPainted(true);
            pbExp.setMaximum(active.getExpToLevel());
            UIManager.put("ProgressBar.foreground",Color.green);
            lblPoke = new javax.swing.JLabel();
            lblEnemy = new javax.swing.JLabel();
            lblEnemyInfo= new javax.swing.JLabel();
            lblInfo= new javax.swing.JLabel();
            btnMoves= new JButton[4];
            pbHP = new javax.swing.JProgressBar();
            pbHPEnemy = new javax.swing.JProgressBar();
            lblBackroubd = new javax.swing.JLabel();
            btnSwitch=new JButton();
            btnLeave=new JButton();
            lblMovesBackround= new javax.swing.JLabel();
            txaMessages = new javax.swing.JTextArea();
            jspMessages = new javax.swing.JScrollPane();
            btnPoke=new JButton();
            btnGreat=new JButton();
            btnUltra=new JButton();
            
            btnPoke.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnBallActionPerformed(evt);
                }
            });
            btnPoke.setFont(new java.awt.Font("SansSerif", 0, 12));
            btnPoke.setText("Pokeball");
            btnPoke.setEnabled(player.getPokeball()>0);
            
            btnGreat.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnBallActionPerformed(evt);
                }
            });
            btnGreat.setFont(new java.awt.Font("SansSerif", 0, 12));
            btnGreat.setText("GreatBall");
            btnGreat.setEnabled(player.getGreatball()>0);
            
            btnUltra.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnBallActionPerformed(evt);
                }
            });
            btnUltra.setFont(new java.awt.Font("SansSerif", 0, 12));
            btnUltra.setText("Ultraball");
            btnUltra.setEnabled(player.getUltraball()>0);
            
            txaMessages.setEditable(false);
            txaMessages.setColumns(20);
            txaMessages.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
            txaMessages.setRows(5);
            jspMessages.setViewportView(txaMessages);
            
            
            btnSwitch.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent evt)
                {
                    btnSwitchActionPerformed(evt);
                }
            });
            btnSwitch.setFont(new java.awt.Font("SansSerif", 0, 12));
            btnSwitch.setText("Switch Pokemon");
            
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
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+enemy.getPokeID()+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblEnemy.setIcon(image);
            
            lblEnemyInfo.setFont(new java.awt.Font("SansSerif", 0, 14));
            lblEnemyInfo.setText(enemy.getName()+" Level: "+enemy.getLvl());
            lblEnemyInfo.setForeground(Color.BLACK);
            
            lblInfo.setFont(new java.awt.Font("SansSerif", 0, 14));
            lblInfo.setText(active.getName()+" Level: "+active.getLvl());
            lblInfo.setForeground(Color.BLACK);
            
            pbHP.setValue(active.getCurrent(0));
            pbHP.setMaximum(active.getTotal(0));
            pbExp.setValue(active.getExp());
            pbExp.setMaximum(active.getExpToLevel());
            pbHPEnemy.setMaximum(enemy.getTotal(0));
            pbHPEnemy.setValue(enemy.getCurrent(0));
            pbHP.setString(pbHP.getValue()+"/"+pbHP.getMaximum());
            pbHPEnemy.setString(pbHPEnemy.getValue()+"/"+pbHPEnemy.getMaximum());
            pbExp.setString(pbExp.getValue()+"/"+pbExp.getMaximum());
            pbHP.setBackground(Color.darkGray);
            pbHPEnemy.setBackground(Color.darkGray);
            pbHP.setOpaque(true);
            pbHPEnemy.setOpaque(true);
            pbHP.setStringPainted(true);
            pbHPEnemy.setStringPainted(true);
            
            
            updateScreen();
                    
            add(btnUltra, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 650, 130, 25));
            add(btnGreat, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 650, 130, 25));
            add(btnPoke, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 650, 130, 25));
            add(btnSwitch, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 685, 130, 25));
            add(btnLeave, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 650, 130, 25));
            
            add(btnMoves[0], new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 650, 165, 25));
            add(btnMoves[1], new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 650, 165, 25));
            add(btnMoves[2], new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 685, 165, 25));
            add(btnMoves[3], new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 685, 165, 25));
            add(lblMovesBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(45, 635, 380, 90));
            
            
            add(pbHPEnemy,new org.netbeans.lib.awtextra.AbsoluteConstraints(990,305, 150, 20));
            add(pbHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 530, 150, 20));
            add(pbExp , new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 550, 150, 12));
            
            add(lblPoke, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 375, 150, 150));
            add(lblEnemy, new org.netbeans.lib.awtextra.AbsoluteConstraints(990,150, 150, 150));
            add(lblEnemyInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(990,325, 150, 20));
            add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(60,565, 150, 20));
            add(jspMessages, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 430, 430, 200));
            add(lblBackroubd, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 750));
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+active.getPokeID()+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblPoke.setIcon(image);
                       
            setResizable(false);
            setUndecorated(true);
            setAlwaysOnTop(false);
            setLocationRelativeTo(null);
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    /**
     *
     */
    public void updateScreen()
    {
        try
        {
            
            updateButtons();
            
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+active.getPokeID()+".png")));
            BufferedImage resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblPoke.setIcon(image);
            pbHP.setValue(active.getCurrent(0));
            pbHP.setMaximum(active.getTotal(0));
            pbExp.setValue(active.getExp());
            pbExp.setMaximum(active.getExpToLevel());
            pbHPEnemy.setMaximum(enemy.getTotal(0));
            pbHPEnemy.setValue(enemy.getCurrent(0));
            pbHP.setString(pbHP.getValue()+"/"+pbHP.getMaximum());
            pbHPEnemy.setString(pbHPEnemy.getValue()+"/"+pbHPEnemy.getMaximum());
            pbExp.setString(pbExp.getValue()+"/"+pbExp.getMaximum());
            lblInfo.setText(active.getName()+" Level: "+active.getLvl());
            
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    @Override
    public void run()
    {
        initComponents();
        this.setVisible(true);
        txaMessages.setText("A wild "+enemy.getName()+" appeared\n");
    }

    /**
     *
     */
    public void disableButtons()
    {
        for (int i = 0; i < btnMoves.length; i++)
            btnMoves[i].setEnabled(false);
        
        btnSwitch.setEnabled(false);
        btnLeave.setEnabled(false);
        btnPoke.setEnabled(false);
        btnGreat.setEnabled(false);
        btnUltra.setEnabled(false);
    }

    /**
     *
     */
    public void reEnbableButtons()
    {
        updateButtons();
        btnSwitch.setEnabled(true);
        btnLeave.setEnabled(true);
        btnPoke.setEnabled(true);
        btnGreat.setEnabled(true);
        btnUltra.setEnabled(true);
    }

    /**
     *
     */
    public void updateButtons()
    {
        boolean move=false;
        for (int i = 0; i <4; i++)
        {
            btnMoves[i].setText("");
            btnMoves[i].setEnabled(false);
        }
        if(active==null)
            return;
        for (int i = 0; i < active.getNumMoves(); i++)
        { 
            btnMoves[i].setText(active.getMove(i).getName()+" "+active.getMove(i).getPpCurrent()+"/"+active.getMove(i).getPpMax());
            if(active.getMove(i).getPpCurrent()>0)
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
        if(player.getPokeball()<1)
            btnPoke.setEnabled(false);
        if(player.getGreatball()<1)
            btnGreat.setEnabled(false);
        if(player.getUltraball()<1)
            btnUltra.setEnabled(false);
    }

    /**
     *
     * @param evt
     */
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
        fight(active.getMove(i));
        updateButtons();
    }

    private void fight(Move move)
    {
        Move enemyMove=getBestMove();
        boolean first= active.getCurrent()[1]>=enemy.getCurrent()[1];//current[1] = speed
        if(first)//true mine, false yours
            useMove(move,first);
        else
            useMove(enemyMove,first);
        updateScreen();
        if(checkFainted())
        {
            if(!first)//true mine, false yours
                useMove(move,!first);
            else
                useMove(enemyMove,!first);
            if(checkFainted())
            {
                updateScreen();
                txaMessages.append("\n");
                return;
            }
        }
        txaMessages.append("\n"+active.getName() + " fainted");
        txaMessages.setCaretPosition(txaMessages.getText().length());
        
        active=player.getActivePokemon();
        if(active==null)
        {
            int gold=player.getGold()/10;
            player.setGold(player.getGold()-gold);
            player.addMessage("All you pokemon fainted, you lost "+gold+" gold");
            player.game=new Game(player,ID.PokeCenter);
            player.setX(80);
            player.setY(50);
            this.dispose();
            return;
        }    
        updateScreen();
        updateScreen();
   }

    private boolean checkFainted()//false fainted
    {
        if(enemy.getCurrent()[0]<1)
        {
            String temp=active.getName() + " killed "+ enemy.getName();
            if(active.expFromKO(enemy.getLvl(),enemy.getStage()))
            {
                
                if(active.evolve(player))
                    temp+=" and evolved";
                else
                    temp+=" and leveled up";
            }
            player.addMessage(temp);
            btnLeave.doClick();
            return false;
        }
        if(active.getCurrent()[0]<1)
        {
            return false;
        }
        return true;
    }

    private void useMove(Move move, boolean mine)
    {
        try
        {
            if(mine)
            {
                if(active.getStatus()[6])//flinched
                {
                    txaMessages.append("\nYour"+STATUS[0]);
                    active.getStatus()[6]=false;
                    return;
                }
                if(active.getStatus()[5])//frozen^tm
                {
                    if(Math.random()<0.4)
                    {   
                        txaMessages.append("\nYour"+STATUS[1]);
                        return;
                    }
                    else
                    {
                        txaMessages.append("\nYour"+STATUS[2]);
                        active.getStatus()[5]=false;
                    }   
                }
                if(active.getStatus()[4])//confused
                {
                    if(Math.random()<0.3)
                    {
                        txaMessages.append("\nYour"+STATUS[3]);
                        active.getCurrent()[0]-=active.getTotal(0)*0.05;
                        return;
                    }
                    else
                    {
                        txaMessages.append("\nYour"+STATUS[4]);
                        active.getStatus()[4]=false;
                    }
                }
                if(active.getStatus()[2])//paralyzed
                {
                    if(Math.random()<0.2)
                    {
                        txaMessages.append("\nYour"+STATUS[5]);
                        return;
                    }
                }
                if(active.getStatus()[1])//sleep
                {
                    if(Math.random()<0.4)
                    {
                        txaMessages.append("Your"+STATUS[6]);
                        return;
                    }
                    else
                    {
                        active.getStatus()[1]=false;
                        txaMessages.append("\nYour"+STATUS[7]);
                    }
                }
            }
            else
            {                
                if(enemy.getStatus()[6])//flinched
                {
                    txaMessages.append("\nEnemy"+STATUS[0]);
                    enemy.getStatus()[6]=false;
                    return;
                }
                if(enemy.getStatus()[5])//frozen
                {
                    if(Math.random()<0.4)
                    {   
                        txaMessages.append("\nEnemy"+STATUS[1]);
                        return;
                    }
                    else
                    {
                        txaMessages.append("\nEnemy"+STATUS[2]);
                        enemy.getStatus()[5]=false;
                    }   
                }
                if(enemy.getStatus()[4])//confused
                {
                    if(Math.random()<0.3)
                    {
                        txaMessages.append("\nEnemy"+STATUS[3]);
                        enemy.getCurrent()[0]-=enemy.getTotal(0)*0.05;
                        return;
                    }
                    else
                    {
                        txaMessages.append("\nEnemy"+STATUS[4]);
                        enemy.getStatus()[4]=false;
                    }
                }
                if(enemy.getStatus()[2])//paralyzed
                {
                    if(Math.random()<0.2)
                    {
                        txaMessages.append("\nEnemy"+STATUS[5]);
                        return;
                    }
                }
                if(enemy.getStatus()[1])//sleep
                {
                    if(Math.random()<0.4)
                    {
                        txaMessages.append("\nEnemy"+STATUS[6]);
                        return;
                    }
                    else
                    {
                        enemy.getStatus()[1]=false;
                        txaMessages.append("\nEnemy"+STATUS[7]);
                    }
                }
            }
            if(move.getName().equals("Metronome"))
            {
                ResultSet rs=player.getDb().query("SELECT * FROM MOVES ORDER BY RANDOM()");
                rs.next();
                ResultSet rsEffect = player.getDb().query("SELECT EFFECT FROM MOVESEFFECTS WHERE ID = "+rs.getInt("ID"));
                LinkedList<String> temp = new LinkedList<String>();
                while(rsEffect.next())
                {
                    temp.add(rsEffect.getString(1));
                }
                String [] effects = new String[temp.size()];
                for (int i = 0; i < effects.length; i++)
                {
                    effects[i]= temp.get(i);
                }
                move.setPpCurrent(move.getPpCurrent()-1);
                move=new Move(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getBoolean(5),rs.getInt(6),rs.getInt(7), effects);  
            }
            
            
            if(Math.random()*100>move.getAccuracy())
            {
                if(mine)
                    txaMessages.append("\n"+active.getName());
                else
                    txaMessages.append("\nWild "+enemy.getName());
                txaMessages.append(" missed the move");
            }
            else
            {
                double damage = move.getDamage();
                String query = "SELECT IMMUNE, RESISTANT, WEAK FROM TYPES WHERE \"TYPE\" = '";
                if(mine)//used on enemy
                {
                    txaMessages.append("\n"+active.getName()+" used " + move.getName());
                    query+=active.getType0();
                    if(enemy.getType1()!=null)
                        query+="' OR \"TYPE\" = '"+enemy.getType1();
                }
                else
                {
                    txaMessages.append("\n"+enemy.getName()+" used " + move.getName());
                    query+=enemy.getType0();
                    if(enemy.getType1()!=null)
                        query+="' OR \"TYPE\" = '"+enemy.getType1();
                }
                query+="'";
                ResultSet rs = player.getDb().query(query);
                int typeEffect=0;
                boolean effect=false;
                while(rs.next())
                {
                    if(rs.getString(1).indexOf(move.getType())>0)//immune
                    {
                        typeEffect+=0;
                        effect=true;
                    }
                    else if(rs.getString(2).indexOf(move.getType())>0)//resistant
                    {
                        typeEffect+=1;
                        effect=true;
                    }
                    else if(rs.getString(3).indexOf(move.getType())>0)//weak
                    {
                        typeEffect+=3;
                        effect=true;
                    }
                }
                if(effect)
                {
                    String name="";
                    if(mine)
                        name="wild "+enemy.getName();
                    else
                        name=active.getName();
                        
                    switch(typeEffect)
                    {
                        case 0://immune or immune, immune
                             txaMessages.append("\n"+name+" is immune to the move");
                             damage*=0;
                            break;
                        case 1://resistant or immune,resistant
                            txaMessages.append("\n"+name+" is resistant to the move");
                             damage*=0.5;
                            break;
                        case 2://resistant,resistant
                            txaMessages.append("\n"+name+" is very resistant to the move"); 
                            damage*=0.25;
                            break;
                        case 3://weak or weak,immune
                            txaMessages.append("\n"+name+" is weak to the move");
                            damage*=2;
                            break;
                        // case 4 would be weak and resistance which will times by 1 thus redundent
                        case 6://weak, weak
                            txaMessages.append("\n"+name+" is very weak to the move");
                             damage*=4;
                            break;
                    }
                }
                if(mine)
                {
                    double temp=1.0*active.getAttackValue(move.isSpecial())/1.0*enemy.getDefenceValue(move.isSpecial());
                    if(temp>4)
                        temp=4;
                    else if(temp<0.25)
                        temp=0.25;
                   damage*=temp;
                    if(active.getLvl()<100)
                        damage*=(0.0001*active.getLvl()*active.getLvl()+0.01);
                    else
                        damage*=0.01*active.getLvl();
                            
                   enemy.damage((int)damage); 
                }
                else
                {
                    double temp=1.0*enemy.getAttackValue(move.isSpecial())/1.0*active.getDefenceValue(move.isSpecial());
                    if(temp>4)
                       temp=4;
                    else if(temp<0.25)
                       temp=0.25;
                    damage*=temp;
                    if(enemy.getLvl()<100)
                        damage*=(0.0001*enemy.getLvl()*enemy.getLvl()+0.01);
                    else
                        damage*=0.01*enemy.getLvl();
                    active.damage((int)damage);
                }
                addStatus(move.getEffect(),mine);

            }
            
            move.setPpCurrent(move.getPpCurrent()-1);
        
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
        txaMessages.setCaretPosition(txaMessages.getText().length());
    }  
    
    private void addStatus(String [] effect, boolean mine)
    {
        for (int j = 0; j < effect.length; j++)
        {
            int amount=-1;
            boolean me = (effect[j].charAt(0)=='0')==mine;//false= affects enemy; true= affects player's poke
            String chance=effect[j].substring(2);
            switch (effect[j].charAt(1))
            {
                case '-':
                    amount*=-1;

                case '+':
                    amount*=-1;
                    amount*=Integer.parseInt(effect[j].substring(7));
                    switch(effect[j].substring(5, 7))// HP , Speed , Attack , Special Attack, Defense, Special Defense
                    {
                        case "HE":
                            statChange(me,0,amount);
                            break;
                            
                        case "AT":
                            statChange(me,2,amount);
                            break;
                            
                        case "SP":
                            statChange(me,1,amount);
                            break;
                            
                        case "SA":
                            statChange(me,3,amount);
                            break;

                        case "DE":
                            statChange(me,4,amount);
                            break;

                        case "SD":
                            statChange(me,5,amount);
                            break;
                            
                        case "AL":
                            for (int i = 1; i < 6; i++)
                            {
                                statChange(me,1,amount);
                            }
                            break;      
                    } 
                    break;

                case 'K':
                    if(me)
                    { 
                        active.getCurrent()[0]=0;
                        txaMessages.append("\n"+"Your");
                    }
                    else
                    {
                        enemy.getCurrent()[0]=0;
                        txaMessages.append("\n"+"Enemy");
                    }
                    txaMessages.append(" pokemon was just one-shotted");
                    break; 
                case 'T'://poison
                    stat(me,0,chance);
                    break;
                case 'S'://sleep
                    stat(me,1,chance);
                    break;
                case 'P'://paralyzed
                    stat(me,2,chance);
                    break;
                case 'B'://burn
                    stat(me,3,chance);
                        break;
                case 'C'://confused
                    stat(me,4,chance);
                    break;
                case 'I'://iced
                    stat(me,5,chance);
                    break;
                case 'F'://flinch
                    stat(me,6,chance);
                    break;
            }
        }
        txaMessages.setCaretPosition(txaMessages.getText().length());
    }   
    
    private void stat(boolean mine, int pos, String chance)
    {
        if(Math.random()*100>Integer.parseInt(chance))
            return;
        String temp="\n";
        if(mine)
        {
            temp+="Your";
            active.setStatus(pos, true);
        }
        else
        {
            temp+="Enemy";
            enemy.setStatus(pos, true);
        }
        temp+=EFFECTS[7]+EFFECTS[pos];
        txaMessages.append(temp);
    } 
    
    private void statChange(boolean mine, int pos, int amount)
    {
        String temp="\n";
        if(mine)
        {
            active.getCurrent()[pos]*=amount/100;
            temp+="Your";
        }
        else
        {
            enemy.getCurrent()[pos]*=amount/100;
            temp+="Enemy";
        }
        temp+=STATS[6]+STATS[pos]+STATS[7]+amount+"%";
        txaMessages.append(temp);
    }
    
    private Move getBestMove()
    {
       int scores[]=new int[enemy.getNumMoves()];
       int sum=0;
       for (int i = 0; i < scores.length; i++)
       {
            scores[i]=enemy.getMove(i).moveScore(active);
            sum+=scores[i];
       }
       if(sum==0)
       {
           return new Move("Struggle","Normal",0,100,false,176,0,new String[]{"1H-40","0H-50"});
       }

       int random = (int)  (Math.random()*sum);
       for (int i = 0; i < scores.length; i++)
       {
            if(sum-scores[i]<random)    
                return enemy.getMove(i);
       }

       return new Move("Struggle","Normal",0,100,false,176,0,new String[]{"1H-40","0H-50"});
    }

    private void btnLeaveActionPerformed(ActionEvent evt)
    {
        music.stop();
        player.game=new Game(player,area);
        new Thread(player.game).start();
        this.dispose();
    }

    private void btnSwitchActionPerformed(ActionEvent evt)
    {
        active=active;
        Thread t = new Thread(new PokemonStatsGUI(player,this));
        t.start();
        disableButtons();
    }

    /**
     *
     */
    public void resume()
    {
        reEnbableButtons();
        if(!active.equals(player.getActivePokemon()))
        {
            
            txaMessages.append("\n"+active.getName()+" retreated and "+player.getActivePokemon().getName()+" took their place\n");
            useMove(getBestMove(),false);
            if(checkFainted())
            {
                updateScreen();
            }
            else
            {
                txaMessages.append("\n"+active.getName() + " fainted");
                txaMessages.setCaretPosition(txaMessages.getText().length());
            }
            active=player.getActivePokemon();
        }
        updateScreen();
        updateScreen();
    }
    
    private void btnBallActionPerformed(ActionEvent evt)
    {
        double chance=1;
        switch(evt.getActionCommand())
        {
            case "Pokeball":
                player.setPokeball(player.getPokeball()-1);
                if(enemy.getLvl()>30)
                    chance/=2.0;
                if(enemy.getLvl()>50)
                    chance/=2.0;
                break;
            case "GreatBall":
                player.setGreatball(player.getGreatball()-1);
                if(enemy.getLvl()>50)
                    chance/=2.0;
                break;
            case "Ultraball":
                player.setUltraball(player.getUltraball()-1);
                break;
        }
        useBall(chance);
        updateScreen();
    }
    
    private void useBall(double chance)
    {
        chance*=1.0/64.0*(1.0+enemy.getNumStatus()/7.0)*(3.0+(4.0-enemy.getStage())/3.0)*(8.0-1.0*enemy.getCurrent(0)/enemy.getTotal(0));
        if(Math.random()<=chance)
        {
            try 
            {
                String temp="You have captured "+enemy.getName();
                int id=player.getPokemon(0).getPlayerPokeId();
                for (int j = 1; j < player.getNumPokes(); j++)
                   if(player.getPokemon(j).getPlayerPokeId()>id)
                       id=player.getPokemon(j).getPlayerPokeId();
                ResultSet rs=player.getDb().query("SELECT PLAYERPOKEID FROM PLAYERPOKEMON WHERE PLAYERPOKEID > "+id+" ORDER BY PLAYERPOKEID DESC");
                if(rs.next())
                        id=rs.getInt(1);
                id++;
                if(player.getNumPokes()==6)
                {

                        String query="INSERT INTO PLAYERPOKEMON (PLAYERID, POKEMONID, \"LEVEL\", EXP, PLAYERPOKEID, LEVELSTATS) VALUES ("+player.getPlayerID()+","+enemy.getPokeID()+"," +enemy.getLvl()+", "+enemy.getExp()+", "+id+", "+enemy.getLevelStats()+")";
                        player.getDb().update(query);
                        for (int i = 0; i < enemy.getNumMoves(); i++)
                        {
                            query="INSERT INTO APP.POKEMONMOVES (PLAYERID, PLAYERPOKEMONID, MOVEID, PP) VALUES ("+player.getPlayerID()+", "+id+", "+enemy.getMove(i).getID()+", "+enemy.getMove(i).getPpCurrent()+")";
                            player.getDb().update(query);
                        }
                        temp+=" and was place into storage";
                }
                else
                {
                    enemy.setPlayerPokeId(id);
                    player.getPokes()[player.getNumPokes()]=enemy;
                }
                
            player.addMessage(temp);
            player.save();
            player.addMessage("Game saved");
            btnLeave.doClick();
        } catch (SQLException ex) {
                    System.out.println(ex);
                }
        }
        else
        {
            txaMessages.append("\n"+enemy.getName()+" broke free from the ball");
            useMove(getBestMove(),false);
        }
    }
}