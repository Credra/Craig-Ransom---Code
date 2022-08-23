package ransom;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

/**
 *Central class on which most of the game is played through
 * @author craig
 */
public class Game extends Canvas implements Runnable//overflow when pokemon reaches level 100 000
{
    private int width, height; 
    private Thread thread;
    private boolean running = false;
    private Graphics g;
    private BufferedImage image;
    private Handler handler;
    private Player player;
    private DB db;
    private JFrame frame;
    private KeyInput keyInput;
    private ID screen;
    private ID area;

    /**
     *Constructor for Game after the LogInGUI
     * @param name Name of the player
     * @param playerID Player unique ID
     * @param location The area the player is in
     * @param db Connection to the database
     * @param gold Amount of gold the player has
     * @param pokeball The number of Pokeballs the player has
     * @param greatball The number of Greatballs the player has
     * @param ultraball The number of Ulraballs the player has
     * @param caveTickets The number of cave tickets the player has
     */
    public Game(String name, int playerID, String location, DB db, int gold, int pokeball, int greatball, int ultraball,int caveTickets, Music music)
    {
        try
        {
            screen=ID.Game;            
            width=1200;height=750;
            this.db=db;
            Pokemon pokes[]=new Pokemon[6];
            int l=0;
            ResultSet rs = db.query("SELECT PLAYERPOKEID,POKEMONID,\"LEVEL\",EXP, STAGE, \"NAME\",TYPE0,TYPE1,LEVELSTATS,CURRENTSTATS, EVOLVE FROM PARTY JOIN POKEMON ON POKEMONID = ID WHERE PLAYERID="+playerID);
            while(rs.next()&&l<6)
            {
                ResultSet move= db.query("SELECT \"NAME\", \"TYPE\", DAMAGE, ACCURACY, SPECIAL, ID, MOVES.PP, POKEMONMOVES.PP FROM MOVES JOIN POKEMONMOVES ON MOVES.ID = POKEMONMOVES.MOVEID WHERE POKEMONMOVES.PLAYERID ="+playerID+" AND POKEMONMOVES.PLAYERPOKEMONID = "+rs.getInt(1));
                Move moves[]=new Move[4];
                int i=0;
                while(move.next()&&i<4)
                {
                    ResultSet rsEffect = db.query("SELECT EFFECT FROM MOVESEFFECTS WHERE ID = "+move.getInt("ID"));
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
                
                int [] current=new int[6];
                String curr[]=rs.getString(10).split(",");
                for (int j = 0; j < 6; j++)
                {
                    current[j]=Integer.parseInt(curr[j]);
                } 
                
                System.out.println(l);
                pokes[l]=new Pokemon(db,rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getInt(9),rs.getString(7),rs.getString(8),moves,i,current,rs.getString("EVOLVE"));
                l++;
            }
            player=new Player(name, playerID,db,pokes, new PlayerSprite(width/2,height/2,ID.Player,this,name), gold, pokeball, greatball, ultraball,caveTickets,music);
            player.setX(width/2);
            player.setY(height/2);
            
            frame = new JFrame("JavaFire");
 
            frame.setSize(new Dimension(width,height));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setUndecorated(true);
            frame.add(this);
            frame.setVisible(true);
            switch(location)
            {
                
                case "Shop":
                    shop();
                    break;
                case "Cave":
                    cave();
                    break;
                case "PokeCenter":
                    pokecentre();
                    break;
                case "Main Area":
                default:
                    mainArea();
                    break;
            }
            keyInput=new KeyInput(this);
            this.addKeyListener(keyInput);
            
            
            start();
        } catch (SQLException ex)
        {
            player.addMessage(ex.toString());
        }
        
    }
    
    /**
     *Constructor for Game after a battle
     * @param player The player that is playing the game
     * @param area The area of the player
     */
    public Game(Player player, ID area)
    {
        running=true;
        this.player=player;
        this.area=area;
        width=1200;height=750;
        db=player.getDb();
        frame = new JFrame("JavaFire");
        keyInput=new KeyInput(this);
        this.addKeyListener(keyInput);
        frame.setSize(new Dimension(width,height));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setUndecorated(true);
        frame.add(this);
        frame.setFocusableWindowState(true);
        frame.setVisible(true);
        switch(area)                    
        {

            case Shop:
                shop();
                break;
            case Cave:
                cave();
                break;
            case PokeCenter:
                pokecentre();
                break;
            case MainArea:
            default:
                mainArea();
                break;
        }            
        player.setVelX(0);
        player.setVelY(0);
        screen=ID.Game;
    }
    
    private void mainArea()
    {
        try
        {
            player.getMusic().main();
            area=ID.MainArea;
            image = ImageIO.read(new File("Sprites\\backround.png"));
            handler = new Handler(); 
            
            addWalls();
            
            for (int j = 6; j > 0; j--)
            {
                for (int i = 0; i < j; i++)
                {
                    handler.addObj(new Grass(width-33*(i+1)-24,(6-j)*33+25,ID.GrassN));
                    
                    handler.addObj(new Grass(33*i+25,height-(6-j+2)*33+9,ID.GrassD));
                   
                    handler.addObj(new Grass(33*i+25,(6-j)*33+25,ID.GrassA));
                    
                    handler.addObj(new Grass(width-33*(i+1)-24,height-(6-j+2)*33+9,ID.GrassI));
                }
            }
            
            handler.addObj(new Portal((width-124)/2+45,95,1,1,ID.PokeCenter));
            handler.addObj(new Wall((width-124)/2+5,110,1,80,ID.WallN));
            handler.addObj(new Wall((width-124)/2+5,0,100,1,ID.WallE));
            handler.addObj(new Wall((width-124)/2+125,0,100,1,ID.WallW));
            handler.addObj(new Wall((width-124)/2,0,124,124,ID.PokeCenter));

            
            handler.addObj(new Portal(70,(height-125)/2+55,1,1,ID.Cave));
            handler.addObj(new Wall(80,(height-125)/2+5,110,1,ID.WallW));            
            handler.addObj(new Wall(18,(height-125)/2-5,1,50,ID.WallS));
            handler.addObj(new Wall(18,(height-125)/2+105,1,50,ID.WallN));
            handler.addObj(new Wall(18,(height-125)/2,60,125,ID.Cave));

            
            handler.addObj(new Portal(width-128+41,(height-100)/2+85,1,1,ID.Shop)); 
            handler.addObj(new Wall(width-128+6,(height-100)/2,70,1,ID.WallE));            
            handler.addObj(new Wall(width-128+33,(height-100)/2-5,1,55,ID.WallS));
            handler.addObj(new Wall(width-128+33,(height-100)/2+105,1,55,ID.WallN));
            handler.addObj(new Wall(width-128,(height-100)/2,100,100,ID.Shop));

            
            player.setCurrentArea(ID.MainArea);
            handler.addObj(player);
            //temp.addMessage("You have entered the Main Area");
          
        } catch (IOException ex)
        {
            player.addMessage(ex.toString());
        }
        
    }
    
    private void cave()
    {
        if(player.getCaveTickets()<1&&!player.isCave())
        {
            player.setX(50);
            if(player.messages.getMessage(3)!="You need to pass to enter the cave")
                player.addMessage("You need to pass to enter the cave");
            return;
        }
        try
        {
            player.getMusic().cave();
            if(!player.isCave())
                player.enterCave();
            area=ID.Cave;
            image = ImageIO.read(new File("Sprites\\caveBackground.png"));
            handler = new Handler(); 
            addWalls();
            int collums=(width-175)/64;
            int rows=(height-2*35)/64;
            int i=0;
            boolean pos[][] = new boolean[collums][rows];
            
            while(i<(collums*rows)/3)
            {
                int row=(int)(Math.random()*rows);
                int collum=(int)(Math.random()*collums);
                if(pos[collum][row]!=true)
                {
                    pos[collum][row]=true;
                    collum=75+collum*64;
                    row=35+row*64;
                    handler.addObj(new Grass(collum,row,ID.GrassC));
                    i++;
                }
            }
                   
            handler.addObj(new Portal(width-78+8,(height-125)/2+55,10,10,ID.MainArea));
            handler.addObj(new Wall(width-78+8,(height-125)/2+5,110,1,ID.WallE));
            handler.addObj(new Wall(width-78+18,(height-125)/2-5,1,10,ID.WallS));
            handler.addObj(new Wall(width-78+18,(height-125)/2+105,1,10,ID.WallN));
             
            Wall cave = new Wall(width-78,(height-125)/2,60,125,ID.Cave);
            //x=872

            
            cave.rotate();
            handler.addObj(cave);
            
            player.setCurrentArea(ID.Cave);
            handler.addObj(player);
            if(player.messages.getMessage(3)!="You have entered the Cave")
                player.addMessage("You have entered the Cave");
        } catch (IOException ex)
        {
            player.addMessage(ex.toString());    
        }
    }

    private void shop()
    {
        try
        {
            player.getMusic().shop();
            area=ID.Shop;
            image = ImageIO.read(new File("Sprites\\shopBackground.png"));
            handler = new Handler();
            addWalls();

            handler.addObj(new Wall(40,40,150,120,ID.Buy));
            handler.addObj(new Wall(width-190,height/2-60,150,120,ID.Sell));
            handler.addObj(new Portal(width/2-250,height-70,10,10,ID.MainArea));
            handler.addObj(new Wall(width/2-290,height-62-25,80,60,ID.Door));
            
            player.setCurrentArea(ID.Shop);
            handler.addObj(player);
            player.addMessage("You have entered the Shop");
            
        } catch (IOException ex)
        {
            player.addMessage(ex.toString());  
        }        
    }
    private void pokecentre()
    {
        
        try
        {
            player.getMusic().pokeCenter();
            area=ID.PokeCenter;
            image = ImageIO.read(new File("Sprites\\pokecenterBackground.png"));
            handler=null;
            handler = new Handler();

            addWalls();

            handler.addObj(new Wall(width-224,25,200,150,ID.Computer));
            handler.addObj(new Wall(25,25,150,100,ID.Heal));
            handler.addObj(new Wall(width-174,height-125,150,100,ID.Connect));
            handler.addObj(new Wall(width/2-290,height-62-25,80,60,ID.Door));
            handler.addObj(new Portal(width/2-250,height-70,10,10,ID.MainArea));
            
            
            player.setCurrentArea(ID.PokeCenter);
            handler.addObj(player);
            player.addMessage("You have entered the PokeCentre");
            
        } catch (IOException ex)
        {
            player.addMessage(ex.toString());    
        }        
        
        
        
    }
    private void addWalls()
    {
        int i=0;
        while(i*40<height)
        {
            handler.addObj(new Wall(0,i*40+18,ID.WallW));
            handler.addObj(new Wall(width-24,i*40+18,ID.WallE));
            i++;
        }
        i=0;
        while(i*40<width)
        {
            handler.addObj(new Wall(i*40,0,ID.WallN));
            handler.addObj(new Wall(i*40,height-25,ID.WallS));
            i++;
        }
        
    }

    /**
     *Starts the thread
     */
    public synchronized void start()
    {
        thread = new Thread(this);
        running=true;
        thread.start();
    }
    
    /**
     *Stops the thread
     */
    public synchronized void stop()
    {
        player.getMusic().stop();
        running=false;
    }

    /**
     *Starts the game and calls the tick and render method every 0.125 seconds
     */

     
    public void run()
    {
        long prevTime = System.nanoTime();
        double amountOfTicks=8.0, ns= 1000000000/amountOfTicks, delta=0;
        
        while(running)
        {
            
            long now= System.nanoTime();
            delta+=(now-prevTime)/ns;
            prevTime=now;
            while(running&&delta>=1)
            {
                tick();
                delta--;
            }
            if(running&&screen.equals(ID.Game))
            {
                render();
            }
           
        }
        prevTime = System.nanoTime();
        while((System.nanoTime()-prevTime)/ns<=2)
        {
        }
        frame.dispose();
    }
    
    /**
     *Calls the tick method in the handler
     */
    public void tick()
    {
        handler.tick();
    }
    
    /**
     *Calls the render method in the handler and renders it to the JFrame
     */
    public void render()
    {
            BufferStrategy bs = this.getBufferStrategy();
            if(bs==null)
            {
                this.createBufferStrategy(3);
                return;
            }
        
        
            g = bs.getDrawGraphics();
            g.drawImage(image, 0,0,width,height, null);
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif",1,16));
            handler.render(g);
            if(player.isCave())
                g.drawString(player.getFormattedTime(), 1120, 50);

            g.dispose();
            bs.show();
    }
    
    /**
     *Changes the player current area
     * @param id Sets the current area to this value
     */
    public void moveTo(ID id)
    {
        
        if(id.equals(ID.PokeCenter)&&player.getCurrentArea().equals(ID.MainArea))
        {
            player.setY(height-150);
            player.setX(width/2-250);
            pokecentre();
        }
        else if(id.equals(ID.Shop)&&player.getCurrentArea().equals(ID.MainArea))
        {
            player.setY(height-150);
            player.setX(width/2-250);
            shop();
        }
        else if(id.equals(ID.Cave)&&player.getCurrentArea().equals(ID.MainArea))
        {   
            player.setX(width-150);
            cave();
        }
        else if(id.equals(ID.MainArea))
        {
            if(player.getCurrentArea().equals(ID.PokeCenter))
            {
                player.setX(width/2-35);
                player.setY(111);
            }
            else if(player.getCurrentArea().equals(ID.Shop))
            {
                player.setX(width-66);
                player.setY(height/2+66);
            }
            else if(player.getCurrentArea().equals(ID.Cave))
            {
                player.setX(140);
                player.setY(height/2);
            }
            mainArea();
        }
    }


    
    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    /**
     *Getter for player
     * @return An instance of the player object
     */
    public Player getPlayer()
    {
        return player;
    }


    /**
     *Getter for screen
     * @return The screen that is currently being displayed
     */
    public ID getScreen()
    {
        return screen;
    }

    /**
     *Getter for area
     * @return The area that is currently being displayed
     */
    public ID getArea()
    {
        return area;
    }

    /**
     *Mutator for screen
     * @param id The enumeration that screen will be to set to
     */
    public void setScreen(ID id)
    {
        screen=id;
    }

    /**
     *Starts a battle
     * @param p The wild pokemon being battled
     */
    public void battle(Pokemon p)
    {
            player.getMusic().battle();
            Thread t = new Thread(new BattleGUI(player,area,p));
            t.start();
            player.setVelX(0);
            player.setVelY(0);
            this.stop();
            
    }

    /**
     *Opens the PokeViewerGUI
     */
    public void storage()
    {
        Thread t = new Thread(new PokeViewerGUI(player));
        player.setVelX(0);
        player.setVelY(0);
        moveOff();
        screen=ID.Computer;
        t.start();
    }
    
    private void moveOff()
    {
        int i=-64;
        switch(player.getCurrentDirection())
        {
            case "North":
                i*=-1;
            case "South":
                player.setY(player.getY()+i);
                break;
            case "West":
                i*=-1;
            case "East":
                player.setX(player.getX()+i);
                break;
        }
    }

    /**
     *Opens the BuyGUI
     */
    public void buy()
    {
        Thread t = new Thread(new BuyGUI(player));
        player.setVelX(0);
        player.setVelY(0);
        moveOff();
        screen=ID.Buy;
        t.start();
        
    }

    /**
     *Opens the SellGUI
     */
    public void sell()
    {
        Thread t = new Thread(new SellGUI(player));
        player.setVelX(0);
        player.setVelY(0);
        moveOff();
        screen=ID.Sell;
        t.start();
        
    }

    /**
     *Opens the ConnectGUI
     */
    public void connect()
    {
        player.setVelX(0);
        player.setVelY(0);
        screen=ID.Connect;
        moveOff();
        new ConnectGUI(this);
    }

    /**
     *Opens the PokemonStatsGUI
     */
    public void setScreenStats()
    {
        Thread t = new Thread(new PokemonStatsGUI(player));
        player.setVelX(0);
        player.setVelY(0);
        t.start();
        screen=ID.Stats;
    }

    /**
     *Opens the HelpGUI
     */
    public void setScreenHelp()
    {
        Thread t = new Thread(new HelpGUI(player));
        player.setVelX(0);
        player.setVelY(0);
        t.start();
        screen=ID.Help;
    }

    /**
     *
     * @return
     */
    public JFrame getFrame()
    {
        return frame;
    }

}
