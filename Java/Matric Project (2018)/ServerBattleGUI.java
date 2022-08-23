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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 *In Progress
 * @author craig
 */
public class ServerBattleGUI extends Battle implements Runnable
{
    private Pokemon active;
    private String enemyName;
    private int numEnemy, current, change;
    private Pokemon [] enemy;
    private Network network;
    private Thread thread;
    private Music music;
    
    /**
     *
     * @param player
     * @param area
     * @param sock
     */
    public ServerBattleGUI(Player player, ID area, Socket sock)
    {
        super(player,1200,750,area);
        player.getMusic().battle();
        this.music=new Music();
        this.music.battle();
        Thread m = new Thread(music);
        m.start();
        active=player.getActivePokemon();
        txaMessages = new javax.swing.JTextArea();
        network=new Network(sock,this);
        thread = new Thread(network);
        thread.start();
        while(!network.isMessage())
        {
            System.out.print(".");
        }
        String temp[]=network.getMessage().split("#");
        enemyName=temp[0];
        enemy= new Pokemon[6];
        int i = 1;
        for (i = 0; i < temp.length-1; i++)
        {
            int pokeId,lvl,playerPokeId,levelStats;
            String t[] = temp[i+1].split(",");
            playerPokeId= Integer.parseInt(t[0]);
            pokeId= Integer.parseInt(t[1]);
            lvl= Integer.parseInt(t[2]);
            levelStats= Integer.parseInt(t[3]);
            int [] moves=new int[4];
            int num=0;
            for (int j = 4; j < t.length; j++)
            {
                moves[j-4]=Integer.parseInt(t[j]);
                num++;
            }
            enemy[i]=new Pokemon(pokeId,lvl,playerPokeId,player.getDb(),levelStats,num, moves);
            
        }
        numEnemy=i;
        current=0;
        System.out.println("SENDINFS");
        network.sendMessage(player.getName()+"#"+player.getActivePokemon().getName()+"#"+player.getActivePokemon().getPokeID()+"#"+player.getActivePokemon().getTotal(0)+"#"+player.getActivePokemon().getLvl());
        /*name=t[0];
        id= Integer.parseInt(t[2]);
        pokeName=t[1];
        serverHP= Integer.parseInt(t[3]);
        serverHPMax= serverHP;
        level=Integer.parseInt(t[4]);*/
    }
    
    private void initComponents()
    {
        try
        {
            this.setUndecorated(true);
            setTitle("Server");
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
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+enemy[current].getPokeID()+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblEnemy.setIcon(image);
            
            lblEnemyInfo.setFont(new java.awt.Font("SansSerif", 0, 14));
            lblEnemyInfo.setText(enemy[current].getName()+" Level: "+enemy[current].getLvl());
            lblEnemyInfo.setForeground(Color.BLACK);
            
            lblInfo.setFont( new java.awt.Font("SansSerif", 0, 14));
            lblInfo.setText(active.getName()+" Level: "+active.getLvl());
            lblInfo.setForeground(Color.BLACK);
            
            pbHP.setValue(active.getCurrent(0));
            pbHP.setMaximum(active.getTotal(0));
            pbHPEnemy.setMaximum(enemy[current].getTotal(0));
            pbHPEnemy.setValue(enemy[current].getCurrent(0));
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
            
            image = new ImageIcon(ImageIO.read(new File("Sprites\\Pokemon\\"+active.getPokeID()+".png")));
            resizedImg = new BufferedImage(150, 150, BufferedImage.TYPE_INT_ARGB);
            g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,150, 150, null);
            g2.dispose();
            image.setImage(resizedImg);   
            lblPoke.setIcon(image);
                       
            this.setResizable(false);
            pack();
            this.setLocationRelativeTo(null);
            this.setVisible(true);
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
            pbHPEnemy.setMaximum(enemy[current].getTotal(0));
            pbHPEnemy.setValue(enemy[current].getCurrent(0));
            pbHP.setString(pbHP.getValue()+"/"+pbHP.getMaximum());
            pbHPEnemy.setString(pbHPEnemy.getValue()+"/"+pbHPEnemy.getMaximum());
            lblInfo.setText(active.getName()+" Level: "+active.getLvl());
            
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    @Override
    public void run()
    {
        System.out.println("server");
        initComponents();
        txaMessages.setText(enemyName+" accepted the challenge");
    }

    /**
     *
     */
    public void disableButtons()
    {
        for (int i = 0; i < btnMoves.length; i++)
            btnMoves[i].setEnabled(false);
        
        btnLeave.setEnabled(false);
    }

    /**
     *
     */
    public void reEnbableButtons()
    {
        updateButtons();
        btnLeave.setEnabled(true);
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
        while(!network.isMessage())
        {
            System.out.print(network.isMessage());
        }
        System.out.println("\nMove recievd");
        int pos=Integer.parseInt(network.getMessage());
        fight(active.getMove(i),pos);
        updateButtons();
    }

    private void fight(Move move, int pos)
    {
        change=0;
        int position=txaMessages.getCaretPosition();
        Move enemyMove=enemy[current].getMove(pos);
        boolean first= active.getCurrent()[1]>=enemy[current].getCurrent()[1];//current[1] = speed
        if(first)//true mine, false yours
            useMove(move,first);
        else
            useMove(enemyMove,first);
        if(checkFainted())
        {
            if(!first)//true mine, false yours
                useMove(move,!first);
            else
                useMove(enemyMove,!first);
            if(checkFainted())
                txaMessages.append("\n");
        }
        if(current<numEnemy)
        {   
            updateScreen();
            updateScreen();
        }
        String t[]=txaMessages.getText().substring(position).split("Your");
        String temp=t[0];
        for (int j = 1; j < t.length; j++)
        {
            temp+=("enemy"+t[j]);
        }
        
        t=temp.split("Enemy");
        temp=t[0];
        for (int j = 1; j < t.length; j++)
        {
            temp+=("Your"+t[j]);
        }
        t=temp.split("\n");
        temp=t[0];
        for (int j = 1; j < t.length; j++)
        {
            temp+=("~"+t[j]);
        }
        String message=change+"#";
        switch(change)
        {
            case 0:
                message+=active.getCurrent(0)+"#"+enemy[current].getCurrent(0)+"#"+temp;
                break;
            case 1:
                message+=active.getName()+","+active.getPokeID()+","+active.getTotal(0)+","+active.getLvl()+"#"+enemy[current].getCurrent(0)+"#"+temp;
                break;
            case 2:
                message+=active.getCurrent(0)+"#"+enemy[current].getName()+"#"+temp;
                break;
            case 3:
                message+=active.getName()+","+active.getPokeID()+","+active.getTotal(0)+","+active.getLvl()+"#"+enemy[current].getName()+","+enemy[current].getTotal(0)+","+enemy[current].getLvl()+"#"+temp;
                break;
        }
       network.sendMessage(message);
   }

    private boolean checkFainted()//false fainted
    {
        if(enemy[current].getCurrent()[0]<1)
        {
            current++;
            if(change==0)
                change=1;
            else
                change=3;
            if(current>=numEnemy)
            {
                player.addMessage("You beat " + enemyName);
                network.sendMessage("4#You lost to " + player.getName());
                btnLeave.doClick();
            }
            return false;
        }
       
        if(active.getCurrent()[0]<1)
        {
            if(change==0)
                change=2;
            else
                change=3;

            if(player.getActivePokemon()==null)
            {
                player.addMessage("You lost to " + enemyName);
                network.sendMessage("4#You beat " + player.getName());
                System.out.println("LOST");
                btnLeave.doClick();
            }
            else
            {
                txaMessages.append("\n"+active.getName() + " fainted");
                txaMessages.setCaretPosition(txaMessages.getText().length());
                active=player.getActivePokemon();
            }
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
                if(enemy[current].getStatus()[6])//flinched
                {
                    txaMessages.append("\nEnemy"+STATUS[0]);
                    enemy[current].getStatus()[6]=false;
                    return;
                }
                if(enemy[current].getStatus()[5])//frozen
                {
                    if(Math.random()<0.4)
                    {   
                        txaMessages.append("\nEnemy"+STATUS[1]);
                        return;
                    }
                    else
                    {
                        txaMessages.append("\nEnemy"+STATUS[2]);
                        enemy[current].getStatus()[5]=false;
                    }   
                }
                if(enemy[current].getStatus()[4])//confused
                {
                    if(Math.random()<0.3)
                    {
                        txaMessages.append("\nEnemy"+STATUS[3]);
                        enemy[current].getCurrent()[0]-=enemy[current].getTotal(0)*0.05;
                        return;
                    }
                    else
                    {
                        txaMessages.append("\nEnemy"+STATUS[4]);
                        enemy[current].getStatus()[4]=false;
                    }
                }
                if(enemy[current].getStatus()[2])//paralyzed
                {
                    if(Math.random()<0.2)
                    {
                        txaMessages.append("\nEnemy"+STATUS[5]);
                        return;
                    }
                }
                if(enemy[current].getStatus()[1])//sleep
                {
                    if(Math.random()<0.4)
                    {
                        txaMessages.append("\nEnemy"+STATUS[6]);
                        return;
                    }
                    else
                    {
                        enemy[current].getStatus()[1]=false;
                        txaMessages.append("\nEnemy"+STATUS[7]);
                    }
                }
            }
            if(move.getName().equals("Metronome"))
            {
                System.out.println("Metronome");
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
                    txaMessages.append("\nWild "+enemy[current].getName());
                txaMessages.append(" missed the move");
            }
            else
            {
                double damage = move.getDamage();
                String query = "SELECT IMMUNE, RESISTANT, WEAK FROM TYPES WHERE \"TYPE\" = '";
                if(mine)//used on enemy[current]
                {
                    txaMessages.append("\n"+active.getName()+" used " + move.getName());
                    query+=active.getType0();
                    if(enemy[current].getType1()!=null)
                        query+="' OR \"TYPE\" = '"+enemy[current].getType1();
                }
                else
                {
                    txaMessages.append("\n"+enemy[current].getName()+" used " + move.getName());
                    query+=enemy[current].getType0();
                    if(enemy[current].getType1()!=null)
                        query+="' OR \"TYPE\" = '"+enemy[current].getType1();
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
                        name="wild "+enemy[current].getName();
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
                    double temp=1.0*active.getAttackValue(move.isSpecial())/1.0*enemy[current].getDefenceValue(move.isSpecial());
                    if(temp>4)
                        temp=4;
                    else if(temp<0.25)
                        temp=0.25;
                   damage*=temp;
                    if(active.getLvl()<100)
                        damage*=(0.0001*active.getLvl()*active.getLvl()+0.01);
                    else
                        damage*=0.01*active.getLvl();
                            
                   enemy[current].damage((int)damage); 
                }
                else
                {
                    double temp=1.0*enemy[current].getAttackValue(move.isSpecial())/1.0*active.getDefenceValue(move.isSpecial());
                    if(temp>4)
                       temp=4;
                    else if(temp<0.25)
                       temp=0.25;
                    damage*=temp;
                    if(enemy[current].getLvl()<100)
                        damage*=(0.0001*enemy[current].getLvl()*enemy[current].getLvl()+0.01);
                    else
                        damage*=0.01*enemy[current].getLvl();
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
                        enemy[current].getCurrent()[0]=0;
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
            enemy[current].setStatus(pos, true);
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
            enemy[current].getCurrent()[pos]*=amount/100;
            temp+="Enemy";
        }
        temp+=STATS[6]+STATS[pos]+STATS[7]+amount+"%";
        txaMessages.append(temp);
    }

    private void btnLeaveActionPerformed(ActionEvent evt)
    {
        network.stop();
        music.stop();
        this.dispose();
        player.heal();
        player.game=new Game(player,area);
        player.game.start();
        player.game.setVisible(true);
    }
    
    private int clamp(int i)
    {
        if(i<0)
            return 1;
        return i;
    }
    
}
