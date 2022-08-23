package ransom;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Used to determine what key is pressed by the User
 * @author craig
 */
public class KeyInput extends KeyAdapter
{
    private Game game;
    private int velocity;
    
    /**
     *Constructor for KeyInput
     * @param game Instance of Game Object
     */
    public KeyInput(Game game)
    {
        this.game=game;
        velocity=game.getWidth()/35;// /35 not /25
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        
        int key = e.getKeyCode();
        if(game.getScreen().equals(ID.Game))
        {
            switch (key)
            {
                case KeyEvent.VK_W:
                    game.getPlayer().setVelY(-velocity);
                    game.getPlayer().setVelX(0);
                    break;
                case KeyEvent.VK_S:
                    game.getPlayer().setVelY(velocity);
                    game.getPlayer().setVelX(0);
                    break;
                case KeyEvent.VK_A:
                    game.getPlayer().setVelX(-velocity);
                    game.getPlayer().setVelY(0);
                    break;
                case KeyEvent.VK_D:
                    game.getPlayer().setVelX(velocity);
                    game.getPlayer().setVelY(0);
                    break;
                case KeyEvent.VK_ADD:
                    game.getPlayer().save();
                    game.getPlayer().addMessage("Game saved");
                    break;
            }
        }
    }
           
    public void keyReleased(KeyEvent e)
    {
        int key = e.getKeyCode();
        if(key==KeyEvent.VK_ESCAPE)
         {
            game.stop();
         }    

        else if(game.getScreen().equals(ID.Game))
        {
            if(key==KeyEvent.VK_E)
               game.setScreenStats();
            else if(key==KeyEvent.VK_H)
               game.setScreenHelp();

           else if(key==KeyEvent.VK_W || key==KeyEvent.VK_S)
               game.getPlayer().setVelY(0);
           else if (key==KeyEvent.VK_A || key==KeyEvent.VK_D)
               game.getPlayer().setVelX(0);
        }
        
    }

}
