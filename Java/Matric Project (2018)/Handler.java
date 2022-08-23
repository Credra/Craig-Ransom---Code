package ransom;

import java.awt.Graphics;
import java.util.LinkedList;

/**
 *Stores a LinkedList of GameObjects
 * A LinkedList is use as the list will always be going through sequentially and the number of GameObjects in stores is not static
 * @author craig
 */
public class Handler
{
     private LinkedList<GameObject> objectArr;
     
    /**
     *Constructor for Handler Class
     */
    public Handler()
     {
         objectArr = new  LinkedList<>();
     }
     
    /**
     *Calls the tick method for all GameObjects stored in the LinkedList
     */
    public void tick()
    {
        for (int i = 0; i < objectArr.size(); i++)
        {
            objectArr.get(i).tick((Player)objectArr.getLast());
        }
    }

    /**
     *Calls the render method for all GameObjects stored in the LinkedList
     * @param g The Graphics of the JFrame the GameObject will be rendered to
     */
    public void render(Graphics g)
    {
         for (int i = 0; i < objectArr.size(); i++)
        {
            objectArr.get(i).render(g);
        }
    }
    
    /**
     *Adds a GameObject to the end of the LinkedList
     * @param temp The GameObject being added to the LinkedList
     */
    public void addObj(GameObject temp)
    {
        objectArr.add(temp);
    }
    
    /**
     *Removes a GameObject to the end of the LinkedList
     * @param temp The GameObject being removed to the LinkedList
     */
    public void removeObj(GameObject temp)
    {
        objectArr.remove(temp);
    }   
    
}
