package ransom;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.imageio.ImageIO;

/**
 *GameObject used to find wild pokemon
 * @author craig
 */
public class Grass extends GameObject
{
    private BufferedImage image;
    
    /**
     *Constructor for the Grass Class
     * @param x the x location of the grass
     * @param y the x location of the grass
     * @param id the enumeration identifying the type of grass
     */
    public Grass(int x, int y, ID id)
    {
        super(x, y, 32,32,id);
        
        try
        {
            String path="Sprites\\grass";
            switch (this.id)
            {
                case GrassA:
                    path+="Autum";
                    break;
                case GrassI:
                    path+="Ice";
                    break;
                case GrassD:
                    path+="Dark";
                    break;
                case GrassN:
                    path+="Plain";
                    break;
                case GrassC:
                    path+="Cave";
                    break;
            }
            path+=".png";
                    
            image = ImageIO.read(new File (path));
            
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    /**
     *Determines if the player and grass are colliding and if so generates a wild pokemon
     * @param temp Player object to determine if there is a collision
     */
    public void tick(Player temp)
    {
        Rectangle rec = new Rectangle( x, y,32,32);
        if(rec.intersects(temp.getBounds()) && (temp.velX!=0||temp.velY!=0))
        {
            int i = (int)(Math.random()*75);
            if(i==1)
            {
                try {
                    DB db =temp.getDb();
                    String query="SELECT * FROM POKEMON\nWHERE (TYPE0 = ";
                    
                    if(this.id.equals(ID.GrassC))
                    {
                        query+="'Dragon' OR TYPE0 = 'Fairy' OR TYPE1 = 'Dragon' OR TYPE1 = 'Fairy') OR STAGE>=3";
                    }
                    else
                    {
                        if(this.id.equals(ID.GrassA))
                            query+="'Fire' OR TYPE0 = 'Fighting' OR TYPE0 = 'Rock' OR TYPE0 = 'Ground' OR TYPE1 = 'Fire' OR TYPE1 = 'Fighting' OR TYPE1 = 'Rock' OR TYPE1 = 'Ground'";
                        else if(this.id.equals(ID.GrassD))
                            query+="'Dark' OR TYPE0 = 'Poison' OR TYPE0 = 'Psychic' OR TYPE0 = 'Ghost' OR TYPE1 = 'Dark' OR TYPE1 = 'Psychic' OR TYPE1 = 'Poison' OR TYPE1 = 'Ghost'";
                        else if(this.id.equals(ID.GrassI))
                            query+="'Ice' OR TYPE0 = 'Water' OR TYPE0 = 'Steel' OR TYPE0 = 'Flying' OR TYPE1 = 'Ice' OR TYPE1 = 'Water' OR TYPE1 = 'Steel' OR TYPE1 = 'Flying'";
                        else if(this.id.equals(ID.GrassN))
                            query+="'Normal' OR TYPE0 = 'Grass' OR TYPE0 = 'Bug' OR TYPE0 = 'Electric' OR TYPE1 = 'Normal' OR TYPE1 = 'Grass' OR TYPE1 = 'Bug' OR TYPE1 = 'Electric'";
                        query+=") AND STAGE<3";
                    }
                    query+="\nORDER BY RANDOM()\n";

                    ResultSet rs=db.query(query);
                    if(rs.next())
                    {
                        String name=rs.getString(1);
                        Pokemon p=new Pokemon(rs.getInt("ID"), name, (temp).getActivePokemon().getLvl(), rs.getInt("Stage"), rs.getString("Type0"),rs.getString("Type1"), ((Player)temp).getDb(), rs.getString("EVOLVE"));
                        temp.battle(p);
                    }
                    else
                        System.out.println("problem");
                    rs.close();
                
                           
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
                
            }
   
        }
    }

    /**
     *Renders the grass object
     * @param g The Graphics of the JFrame which this object will be rendered to 
     */
    public void render(Graphics g)
    {       
        g.drawImage(image, x, y,width,height, null);
    }
    
}
