package ransom;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *GUI used to allow players to change the moves of their pokemon
 * @author craig
 */
public class MovesGUI extends javax.swing.JFrame
{

    private JButton btnMoves[];
    private JScrollPane scpMoves[];
    private JTextArea txaMoves[];
    private int pos;
    private boolean selected;
    private Pokemon pokemon;
    private Move move;

    public MovesGUI(Pokemon pokemon, Move move)
    {
        this.pokemon=pokemon;
        this.move=move;
        initComponents();
    }

                        
    private void initComponents()
    {
        selected=false;
        btnMoves= new JButton[4];
        scpMoves=new JScrollPane[4];
        txaMoves=new JTextArea[4];
        lblNewMove = new javax.swing.JLabel();
        txaNewMove = new javax.swing.JTextArea();
        btnConfirm = new javax.swing.JButton();
        btnCurrent = new javax.swing.JButton();
        lblBackround = new javax.swing.JLabel();
        scpNewMove = new JScrollPane();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setName("Moves"); // NOI18N
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblNewMove.setFont(new java.awt.Font("SansSerif", 1, 16)); // NOI18N
        lblNewMove.setText("New Move");
        lblNewMove.setForeground(Color.WHITE);
        getContentPane().add(lblNewMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 350, -1, -1));

        txaNewMove.setEditable(false);
        txaNewMove.setColumns(20);
        txaNewMove.setRows(5);
        txaNewMove.setText(move.toString());
        txaNewMove.setCaretPosition(0);
        scpNewMove.setViewportView(txaNewMove);

        getContentPane().add(scpNewMove, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 320, -1, -1));

        for (int i = 0; i < btnMoves.length; i++)
        {
           btnMoves[i]= new JButton();
           btnMoves[i].setBackground(new java.awt.Color(204, 255, 255));
           btnMoves[i].setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
           btnMoves[i].setText("Forget");
           btnMoves[i].setActionCommand(i+"");
           btnMoves[i].addActionListener(new java.awt.event.ActionListener()
           {
                public void actionPerformed(java.awt.event.ActionEvent evt)
                {
                    btnMoveActionPerformed(evt);
                }
            });
           int x=10+i%2*355;
           int y=80+i/2*140;
           getContentPane().add(btnMoves[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, 100, 20));
           scpMoves[i]=new JScrollPane();
           txaMoves[i]=new JTextArea();
           
            txaMoves[i].setEditable(false);
            txaMoves[i].setColumns(20);
            txaMoves[i].setRows(5);
            txaMoves[i].setText(pokemon.getMove(i).toString());
            txaMoves[i].setCaretPosition(0);
            scpMoves[i].setViewportView(txaMoves[i]);
             
            add(scpMoves[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(120+i%2*350, 40+i/2*140, -1, -1));
        }
        
        btnConfirm.setBackground(new java.awt.Color(204, 255, 204));
        btnConfirm.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N
        btnConfirm.setText("Confirm");
        btnConfirm.setEnabled(selected);
        btnConfirm.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnConfirmActionPerformed(evt);
            }
        });
        getContentPane().add(btnConfirm, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 380, 150, 20));

        btnCurrent.setBackground(new java.awt.Color(204, 204, 255));
        btnCurrent.setFont(new java.awt.Font("SansSerif", 0, 12)); // NOI18N
        btnCurrent.setText("Keep Current Moves");
        btnCurrent.setActionCommand((-1)+"");
        btnCurrent.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCurrentActionPerformed(evt);
            }
        });
        getContentPane().add(btnCurrent, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 330, 150, 20));

        lblBackround.setBackground(new java.awt.Color(0, 149, 146));
        lblBackround.setOpaque(true);
        getContentPane().add(lblBackround, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 762, 450));

        setSize(new java.awt.Dimension(762, 450));
        setLocationRelativeTo(null);
    }                      

    private void btnConfirmActionPerformed(java.awt.event.ActionEvent evt)                                            
    {          
        if(pos!=-1)
            pokemon.getMoves()[pos]=move;
        this.dispose();
    }                                           

    private void btnMoveActionPerformed(java.awt.event.ActionEvent evt)                                         
    {                                             
        if(selected)
        {
            if(pos==-1)
                btnCurrent.setBackground(new java.awt.Color(204, 204, 255));
            else
                btnMoves[pos].setBackground(new java.awt.Color(204, 255, 255));
        }
        else
           selected=true;
        pos=Integer.parseInt(evt.getActionCommand());
        btnConfirm.setEnabled(true);
        btnMoves[Integer.parseInt(evt.getActionCommand())].setBackground(Color.pink);
    }                                        

    private void btnCurrentActionPerformed(java.awt.event.ActionEvent evt)                                           
    {            
        if(selected)
        {
            if(pos!=-1)
                btnMoves[pos].setBackground(new java.awt.Color(204, 255, 255));
        }
        else
            selected=true;
        pos=Integer.parseInt(evt.getActionCommand());
        btnConfirm.setEnabled(true);
        btnCurrent.setBackground(Color.pink);
   }                                          
      
    private javax.swing.JButton btnCurrent;
    private javax.swing.JButton btnConfirm;
    private javax.swing.JLabel lblBackround;
    private javax.swing.JLabel lblNewMove;
    private javax.swing.JTextArea txaNewMove;
    private javax.swing.JScrollPane scpNewMove;               
}
