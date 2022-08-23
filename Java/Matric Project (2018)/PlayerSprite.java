package ransom;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *GameObject controlled by the player
 * @author craig
 */
public class PlayerSprite extends GameObject
{

    /**
     *Path to the images used in in this class
     */
    protected static final String PATH = "Sprites\\";

    /**
     *image currently being rendered
     */
    protected BufferedImage image;

    /**
     *boolean used to alternate between images to give the appearance of walking
     */
    protected boolean step=false;

    /**
     *The direction the player is currently facing
     */
    protected String currentDirection;

    /**
     *An instance of the game object
     */
    protected Game game;

    /**
     *The area the player is currently in
     */
    protected ID currentArea;

    /**
     *The messages that must be rendered to the screens
     */
    protected Messages messages;

    /**
     *Constructor for PlayerSprite when the game is started
     * @param x x location
     * @param y y location
     * @param id identifying enumeration
     * @param game instance of game object
     * @param name name of the player
     */
    public PlayerSprite(int x, int y, ID id, Game game, String name)
    {
        super(x, y, 64, 64, id);
        this.game=game;
        currentDirection="South";
        currentArea=ID.MainArea;
        messages=new Messages(name);

    }
    
    /**
     *Constructor for PlayerSprite after a battle
     * @param ps instance of the PlayerSprite
     * @param name name of the player
     */
    public PlayerSprite(PlayerSprite ps, String name)
    {
        super(ps.getX(),ps.getY(),64,64,ps.getId());
        this.game=ps.getGame();
        currentDirection="South";
        currentArea=ID.MainArea;
        messages=new Messages(name);
    }

    /**
     *Calls the moveTo method in the game object
     * @param id Area the PlayerSprite will move to
     */
    public void moveTo(ID id)
    {
        game.moveTo(id);
    }
   
    /**
     *Used in collision detection
     * @return A rectangle of the area the PlayerSprite is occupying
     */
    public Rectangle getBounds()
    {
        return new Rectangle(x,y-10,width,height);
    }
    
    /**
     *Sets the velocities to 0
     */
    public void stop()
    {
        velX=0;
        velY=0;
    }
    public void tick(Player temp)
    {
        if(temp.game.getScreen().equals(ID.Game))
        {
            step=!step;
            x+=velX;
            y+=velY;
        }
        else
        {
            velX=0;
            velY=0;
        }
    
    }
    
    public void render(Graphics g)
    {
        try
        {
            
            String temp="Male";
            if(velX!=0 || velY!=0)
            {   
                if(velX<0)
                {
                    temp+="West";
                    currentDirection="West";

                }
                else if(velX>0)
                {
                    temp+="East";
                    currentDirection="East";
                }
                else if(velY>0)
                {
                    temp+="South";
                    currentDirection="South";
                }
                else 
                {
                    temp+="North";
                    currentDirection="North";
                }
                
                if(step)
                    temp+="0";
                else
                    temp+="1";
            }
            else
                temp+=currentDirection+"Still";
            temp+=".png";
            image = ImageIO.read(new File (PATH + temp));
            g.drawImage(image, x, y, null);
            
            messages.render(g, game.getWidth(), game.getHeight());
            
        } catch (IOException ex)
        {
            System.out.println(ex);    
        }
    }



    /**
     *Accessor for Current Dircetion
     * @return The direction the player is facing
     */
    public String getCurrentDirection()
    {
        return currentDirection;
    }

    /**
     *Accessor for the game object
     * @return The game object
     */
    public Game getGame()
    {
        return game;
    }

    /**
     *Accessor for current area
     * @return The area the PlayerSprite is currently in
     */
    public ID getCurrentArea()
    {
        return currentArea;
    }


    /**
     *Mutator for current area
     * @param currentArea The value currentArea will be set to
     */
    public void setCurrentArea(ID currentArea)
    {
        this.currentArea = currentArea;
    }

    /**
     *Adds a new message to the Messages object
     * @param temp The message being added
     */
    public void addMessage(String temp)
    {
        messages.addMessage(temp);
    }
    
    /**
     *Mutator method for step
     * @param step The boolean value of which foot the player sprite has forward
     */
    public void setStep(boolean step)
    {
        this.step=step;
    }
    
    /**
     *Accessor method for step
     * @return The boolean value of which foot the player sprite has forward
     */
    public boolean getStep()
    {
        return step;
    }
    
}