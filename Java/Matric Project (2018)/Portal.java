package ransom;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *This GameObject allows the player to move from the Main Area to another area
 * @author craig
 */
public class Portal extends GameObject
{
   
    /**
     *Constructor for Portal Class
     * @param x The x location of the object
     * @param y The y location of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param id The identifying enumeration of the object
     */
    public Portal(int x, int y,int width, int height,ID id)
    {
        super(x, y,width,height, id);
    }

    /**
     *Determines if the player and portal object are colliding, if so moves the player to a new area
     * @param temp Player object
     */
    public void tick(Player temp)
    {
        if(new Rectangle( x, y,width,height).intersects(temp.getBounds()))
            temp.moveTo(id);
    }
    
    /**
     *Portal is invisible thus no need to render, only here do to inheritance of an abstract class
     * @param g The Graphics of the JFrame which this object will be rendered to 
     */
    public void render(Graphics g)
    {
    }
    
}