/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.awt.Graphics;

/**
 *Abstract class used inherited by all classes with graphical components
 * @author Craig
 */
public abstract class GameObject
{
    /**
     *The location of the object on a horizontal plane  
     */
      protected int  x,

    /**
     *The location of the object on a vertical plane  
     */
    y,

    /**
     *The width of the object
     */
    width,

    /**
     *The height of the object
     */
    height,

    /**
     *The velocity of the object on a horizontal plane
     */

    /**
     *
     */
    velX,

    /**
     *The velocity of the object on a vertical plane
     */

    /**
     *
     */
    velY;

    /**
     *The enumeration used to identifies the object
     */
    protected ID id;

    /**
     *Constructor for the GamObject Class
     * @param x The x location of the object
     * @param y The y location of the object
     * @param width The width of the object
     * @param height The height of the object
     * @param id The enumeration that identifies the object
     */
    public GameObject(int x, int y, int width, int height, ID id)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    /**
     *Updates logical components of the graphics such of change in location due to a velocity or collisions
     * @param temp The player object that be check for collisions
     */
    public abstract void tick(Player temp);
    
    /**
     *Renders the images onto the screen
     * @param g The graphics of the JFrame that will be rendered on
     */
    public abstract void render(Graphics g);
    
    /**
     *Returns the x location of the object
     * @return x location of the object
     */
    public int getX()
    {
        return x;
    }
    
    /**
     *Returns the y location of the object
     * @return y location of the object
     */
    public int getY()
    {
        return y;
    }

    /**
     *Returns the width of the object
     * @return width of the object
     */
    public int getWidth()
    {
        return width;
    }

    /**
     *Returns the height of the object
     * @return height of the object
     */
    public int getHeight()
    {
        return height;
    }

    /**
     *Returns the identifying enumeration of the object
     * @return identifying enumeration of the object
     */
    public ID getId()
    {
        return id;
    }

    /**
     *Accessor for velocity on the horizontal plane
     * @return velocity on the horizontal plane
     */
    public int getVelX()
    {
        return velX;
    }

    /**
     *Accessor for velocity on the vertical plane
     * @return for velocity on the vertical plane
     */
    public int getVelY()
    {
        return velY;
    }

    /**
     *Mutator for the x location
     * @param x x location
     */
    public void setX(int x)
    {
        this.x = x;
    }

    /**
     *Mutator for the y location
     * @param y for the y location
     */
    public void setY(int y)
    {
        this.y = y;
    }

    /**
     *Mutator for the width
     * @param width width
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     *Mutator for the
     * @param height height
     */
    public void setHeight(int height)
    {
        this.height = height;
    }

    /**
     *Mutator for the id enumeration
     * @param id id enumeration
     */
    public void setId(ID id)
    {
        this.id = id;
    }

    /**
     *Mutator for the x velocity
     * @param velX x velocity
     */
    public void setVelX(int velX)
    {
        this.velX = velX;
    }

    /**
     *Mutator for the y velocity
     * @param velY y velocity
     */
    public void setVelY(int velY)
    {
        this.velY = velY;
    }
      
      
}
