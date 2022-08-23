package ransom;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *GameObject used for collision detection 
 * @author craig
 */
public class Wall extends GameObject
{
    
    private  BufferedImage image;
    
    /**
     *Constructor for a wall
     * @param x x location
     * @param y y location
     * @param id Identifying enumeration
     */
    public Wall(int x, int y, ID id)
    {
        super(x, y, 25,40,id);

        try
        {
            String path="Sprites\\wall.png";
            image = ImageIO.read(new File (path));

            switch (this.id)
            {
                case WallS:
                    rotate();
                case WallN:
                    width=40;
                    height=25;
                    break;
                case WallW:
                    rotate();
                case WallE:
                    rotate(1);
            }
            
        
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
    
    /**
     *Constructor for a building or structure
     * @param x x location
     * @param y y location
     * @param width width of object
     * @param height height of object
     * @param id Identifying enumeration
     */
    public Wall(int x, int y, int width, int height, ID id)
    {
        
        super(x, y, width, height, id);
        try
        {
            String path="Sprites//";
            switch (this.id)
            {
                case PokeCenter:
                    path+="pokecenter";
                    break;
                case Shop:
                    path+="shop";
                    break;
                case Cave:
                    path+="cave";
                    break;
                case Door:
                    path+="door";
                    break;
                case Buy:
                    path+="buyDoor";
                    break;
                case Sell:
                    path+="sellDoor";
                    break;
                case Computer:
                    path+="storageDoor";
                    break;
                case Heal:
                path+="healDoor";
                break;
                case Connect:
                path+="connectDoor";
                break;
                default:
                    image=null;
                    return;
            }
            path+=".png";
                    
            image = ImageIO.read(new File (path));
        } catch (IOException ex)
        {
            System.out.println(ex);
        }
    }

    /**
     *Determines if the player is colliding this object
     * @param temp The GamrObject that with collision is determined with 
     */
    public void tick(Player temp)
    {
        Rectangle rec = new Rectangle( x, y,height,width);
        
        if(rec.intersects(temp.getBounds()))
        {
            switch (id)
            {
                case WallW:
                    temp.setX((int)Math.floor(rec.getMaxX()));
                    break;
                case WallE:
                    temp.setX((int) Math.ceil(rec.getMinX()-temp.width));
                    break;
                case WallN:
                    temp.setY((int)Math.ceil(rec.getMaxY()));
                    break;
                case WallS:
                    temp.setY((int)Math.floor(rec.getMinY()-temp.height));
                    break;
                case Buy:
                    temp.buy();
                    break;
                case Sell:
                    temp.sell();
                    break;
                case Computer:
                    temp.storage();
                    break;
                case Heal:
                    temp.heal();
                    break;
                case Connect:
                    temp.connect();
                    break;
                default:
                    break;
            }
        }
    }
    
    /**
     *Renders the GameObject
     * @param g The Graphic of the JFrame the GameObject is rendered to
     */
    public void render(Graphics g)
    {
        g.drawImage(image, x, y,width,height, null);           
    }

    /**
     *Rotates the BufferedImage by i radians
     * @param i Rotates image by i radians
     */
    public void rotate(int i)
    {
        AffineTransform tx = new AffineTransform();
        tx.translate(image.getHeight() / 2,image.getWidth() / 2);
        tx.rotate(Math.PI*i/2.0);

        tx.translate(-image.getWidth() / 2,-image.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        BufferedImage newImage =new BufferedImage(image.getHeight(), image.getWidth(), image.getType());
        image=op.filter(image, newImage);
        
    }
   
    /**
     *Rotates the image by 180°(Will distort image if not exactly 180° thus two rotate methods)
     */
    public void rotate()
    {
        AffineTransform tx = new AffineTransform();
        tx.rotate(Math.toRadians(180), image.getWidth() / 2, image.getHeight() / 2);

        AffineTransformOp op = new AffineTransformOp(tx,AffineTransformOp.TYPE_BILINEAR);
        image = op.filter(image, null);
    }
    
}
