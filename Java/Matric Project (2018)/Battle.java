/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

/**
 *
 * @author craig
 */
public abstract class Battle extends javax.swing.JFrame
{

    /**
     *
     */
    protected Player player;

    /**
     *
     */
    protected javax.swing.JButton btnMoves[];

    /**
     *
     */
    protected javax.swing.JButton btnLeave;
    protected javax.swing.JLabel lblEnemy,lblEnemyInfo,

    /**
     *
     */
    lblInfo,

    /**
     *
     */
    lblBackroubd,

    /**
     *
     */
    lblMovesBackround,lblPoke;
    protected javax.swing.JProgressBar pbHP,

    /**
     *
     */
    pbHPEnemy;
    protected int width,

    /**
     *
     */
    height;

    /**
     *
     */
    protected javax.swing.JTextArea txaMessages; 

    /**
     *
     */
    protected JScrollPane jspMessages;

    /**
     *
     */
    protected ID area;

    /**
     *
     */
    protected static final String [] EFFECTS = new String[]{"poisoned","sleep","paralyzed","burned","confused","frozen","flinched"," pokemon was just "};

    /**
     *
     */
    protected static final String [] STATS = new String[]{"health","speed","attack","special attack","defence","special defence"," pokemon's "," has been changed by "};

    /**
     *
     */
    protected static final String [] STATUS = new String[]{" pokemon is flinched and can't attack"," pokemon is frozen and let it go (the move)"," pokemon was frozen but the cold never bothered it anyway so it thawed"," pokemon is confused and hurt itself instead(it's theory all over again)"," pokemon is no longer confused, did pokemon 252 explain something nicely??"," pokemon is paralyzed and can't attack"," pokemon is asleep and can't attack"," pokemon woke up, did 251 scream again?"};

    /**
     *
     */
    protected Music music;

    /**
     *
     * @param player
     * @param width
     * @param height
     * @param area
     */
    public Battle(Player player, int width, int height, ID area)
    {
        this.player = player;
        this.width = width;
        this.height = height;
        this.area = area;
    }
    
    /**
     *
     */
    public abstract void updateScreen();
    
    /**
     *
     */
    public abstract void run();
    
    /**
     *
     */
    public abstract void disableButtons();
   
    /**
     *
     */
    public abstract void reEnbableButtons();
    
    /**
     *
     */
    public abstract void updateButtons();
   
    /**
     *
     * @param evt
     */
    public abstract void moveActionPerformed(ActionEvent evt);
}
