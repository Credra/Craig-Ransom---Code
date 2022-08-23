package ransom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *GUI in which players can buy items
 * @author craig
 */
public class BuyGUI extends javax.swing.JFrame implements Runnable
{
    private Player player;
    private JLabel lblImages[];           
    private JButton btnAdd[];
    private JButton btnBuy[];
    private JButton btnExit;
    private JButton btnMinus[];
    private JScrollPane scpInfo[];
    private JTextArea txaInfo[];
    private JLabel lblAmount[];
    private JLabel lblPrice[];
    private JLabel lblBackround;       
    private JLabel lblGold;  
    private static final String [] PATHS=new String[]{"Pokeball","Greatball","Ultraball","caveTicket","Sprites\\",".png"};
    private static final String [] DESCRIPTION=new String[]{"Pokeball\t1 Gold\n\nEffect on Pokemon Level 30 or less.","Greatball\t10 Gold\n\nEffect on Pokemon Level 50 or less.","Ultraball\t25 Gold\n\nStrongest of the three devices.\n\nEffecive on Pokemon of all levels","CaveTicket\t50 Gold\n\nAllows player entry into the Cave\nfor 5 minutes."};
    private static final int[] PRICES= new int[]{1,10,25,50};
    private int[] amounts;

    public BuyGUI(Player player)
    {
        this.player=player;
    }
                       
    private void initComponents()
    {
        try
        {
            setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
            getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
            this.setUndecorated(true);
            this.setResizable(false);
            amounts=new int[]{0,0,0,0};
            lblImages=new JLabel[4];
            scpInfo=new  JScrollPane[4];
            txaInfo=new  JTextArea[4];
            btnBuy = new JButton[4];
            btnAdd = new JButton[4];
            btnMinus = new JButton[4];
            lblAmount = new JLabel[4];
            lblPrice = new JLabel[4];
            for (int i = 0; i < lblImages.length; i++)
            {
                lblImages[i]=new JLabel();
                ImageIcon image = new ImageIcon(ImageIO.read(new File(PATHS[4]+PATHS[i]+PATHS[5])));
                BufferedImage resizedImg = new BufferedImage(110, 110, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image.getImage(), 0, 0,110, 110, null);
                g2.dispose();
                image.setImage(resizedImg);
                lblImages[i].setIcon(image);
                getContentPane().add(lblImages[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50+i*130, 110, 110));
 
                scpInfo[i]=new JScrollPane();
                txaInfo[i]= new JTextArea();
                txaInfo[i].setEditable(false);
                txaInfo[i].setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
                txaInfo[i].setText(DESCRIPTION[i]);
                scpInfo[i].setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scpInfo[i].setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
                scpInfo[i].setViewportView(txaInfo[i]); 
                getContentPane().add(scpInfo[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 50+i*130, 220, 110));

                btnBuy[i]=new JButton();
                btnBuy[i].setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
                btnBuy[i].setText("Buy");
                 btnBuy[i].setActionCommand(i+"");
                btnBuy[i].addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        btnBuyActionPerformed(evt);
                    }
                });
                getContentPane().add(btnBuy[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 50+i*130, 110, 60));
                
                btnAdd[i]= new JButton();
                btnAdd[i].setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
                btnAdd[i].setText("+");
                btnAdd[i].setActionCommand(""+i);
                btnAdd[i].addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        btnAddActionPerformed(evt);
                    }
                });
                getContentPane().add(btnAdd[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 120+i*130, 50, 40));

                btnMinus[i]= new JButton();
                btnMinus[i].setFont(new java.awt.Font("SansSerif", 0, 13)); // NOI18N
                btnMinus[i].setText("-");
                btnMinus[i].setActionCommand(""+i);
                btnMinus[i].addActionListener(new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        btnMinusActionPerformed(evt);
                    }
                });
                getContentPane().add( btnMinus[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 120+i*130, 50, 40));

                lblAmount[i]= new JLabel();
                lblAmount[i].setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
                int num=0;
                switch(i)
                {
                    case 0:
                        num= player.getPokeball();
                        break;
                    case 1:
                        num= player.getGreatball();
                        break;
                    case 2:
                        num= player.getUltraball();
                        break;
                    case 3:
                        num= player.getCaveTickets();
                        break;
                }
                lblAmount[i].setText("You currently have: "+num);
                lblAmount[i].setForeground(Color.white);
                getContentPane().add(lblAmount[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 50+i*130, 180, 60));
                
                lblPrice[i]= new JLabel();
                lblPrice[i].setFont(new java.awt.Font("SansSerif", 0, 15)); // NOI18N
                lblPrice[i].setText("Buy "+amounts[i]+" for " + (amounts[i]*PRICES[i])+" Gold");
                lblPrice[i].setForeground(Color.white);
                getContentPane().add(lblPrice[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 110+i*130, 180, 60));
                
            }
            
            btnExit = new JButton();
            btnExit.setIcon(new ImageIcon(ImageIO.read(new File("Sprites\\exitButton.png"))));
            btnExit.addActionListener(new java.awt.event.ActionListener()
            {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnExitActionPerformed(evt);
                }
            });
            getContentPane().add(btnExit, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 10, 30, 30));

            lblGold = new JLabel();
            lblGold.setFont(new java.awt.Font("SansSerif", 0, 18));
            lblGold.setForeground(Color.white);
            lblGold.setText("Gold: "+player.getGold());
            getContentPane().add(lblGold, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 560, 140, 30));
            lblBackround = new JLabel();
            ImageIcon image = new ImageIcon(ImageIO.read(new File("Sprites\\storeBackround.png")));
            BufferedImage resizedImg = new BufferedImage(750, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = resizedImg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(image.getImage(), 0, 0,750, 600, null);
            g2.dispose();
            image.setImage(resizedImg);
            lblBackround.setIcon(image);
            getContentPane().add(lblBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 750, 600));
            

            pack();
            setLocationRelativeTo(null);
        }catch (IOException ex)
        {
            System.out.println(ex);        
        }
    }                   

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt)                                        
    {                                            
        player.game.setScreen(ID.Game);
        this.dispose();
    }    
    
    private void btnBuyActionPerformed(java.awt.event.ActionEvent evt)                                        
    {                                            
        int i = Integer.parseInt(evt.getActionCommand());
        if(amounts[i]<1)
            return;
        int num=0;
        switch(i)
        {
            case 0:
                player.setPokeball(player.getPokeball()+amounts[i]);
                num= player.getPokeball();
                break;
            case 1:
                player.setGreatball(player.getGreatball()+amounts[i]);
                 num= player.getGreatball();
                break;
            case 2:
                player.setUltraball(player.getUltraball()+amounts[i]);
                num= player.getUltraball();
                break;
            case 3:
                player.setCaveTickets(player.getCaveTickets()+amounts[i]);
                num= player.getCaveTickets();
                break;
        }
        player.setGold(player.getGold()-amounts[i]*PRICES[i]);
        amounts[i]=0;
        lblAmount[i].setText("You currently have: "+num);
        for (int j = 0; j < 4; j++)
        {
            while(amounts[j]*PRICES[j]>player.getGold())
                amounts[j]--;
            
            lblPrice[j].setText("Buy "+amounts[j]+" for " + (amounts[j]*PRICES[j])+" Gold");
        }
        lblGold.setText("Gold: "+player.getGold());
        player.save();
    }
    
    private void btnAddActionPerformed(java.awt.event.ActionEvent evt)                                        
    {                                            
        int i = Integer.parseInt(evt.getActionCommand());
        if(amounts[i]==99)
            amounts[i]=0;
        else
        {
            int max=player.getGold()/PRICES[i];
            if(amounts[i]<max)
                amounts[i]++;
            else
                amounts[i]=0;
        }
        lblPrice[i].setText("Buy "+amounts[i]+" for " + (amounts[i]*PRICES[i])+" Gold");
    }
    
    private void btnMinusActionPerformed(java.awt.event.ActionEvent evt)                                        
    {               
        int i = Integer.parseInt(evt.getActionCommand());
        if(amounts[i]==0)
        {
            int num=player.getGold()/PRICES[i];
            if(num>99)
                num=99;
            amounts[i]=num;
        }
        else
            amounts[i]--;
        lblPrice[i].setText("Buy "+amounts[i]+" for " + (amounts[i]*PRICES[i])+" Gold");
    }
    
    @Override
    public void run()
    {
        initComponents();
        this.setVisible(true);
    }
}
