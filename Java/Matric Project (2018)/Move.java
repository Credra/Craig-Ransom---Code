package ransom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *The move that a pokemon can learn and use
 * @author craig
 */
public class Move
{
    private String name, type;
    private int accuracy, damage,id, ppMax, ppCurrent;
    private String[]effect;
    private boolean special;

    /**
     *Constructor for new move or from a pokemon stored in storage
     * @param name The name of the Move
     * @param type The type the move is
     * @param damage The amount of base damage the move does 
     * @param accuracy Measured in %
     * @param special Whether the move is ranged(special) or not
     * @param id The unique integer identifying the move
     * @param ppMax The maximum amount of times a move can be used before being healed
     * @param effect The bonus effects the move has
     */
    public Move(String name, String type, int damage,int accuracy, boolean special,  int id, int ppMax, String[] effect)//From DB
    {
        this.name = name;
        this.accuracy = accuracy;
        this.damage = damage;
        this.type = type;
        this.id=id;
        this.ppMax=ppMax;
        ppCurrent=ppMax;
        this.special=special;
        this.effect=effect;
    }
    
    /**
     *Constructor for a move from a pokemon in the player's party
     * @param name The name of the Move
     * @param type The type the move is
     * @param damage The amount of base damage the move does 
     * @param accuracy Measured in %
     * @param special Whether the move is ranged(special) or not
     * @param id The unique integer identifying the move
     * @param ppMax The maximum amount of times a move can be used before being healed
     * @param ppCurrent The amount of times the move has been used
     * @param effect The bonus effects the move has
     */
    public Move(String name, String type, int damage,int accuracy, boolean special,  int id, int ppMax, int ppCurrent, String[] effect)
    {
        this.name = name;
        this.accuracy = accuracy;
        this.damage = damage;
        this.type = type;
        this.id=id;
        this.ppMax=ppMax;
        this.ppCurrent=ppCurrent;
        this.special=special;
        this.effect=effect;
    }
   
    /**
     *Accessor for name
     * @return Name of move
     */
    public String getName()
    {
        return name;
    }

    /**
     *Accessor for accuracy
     * @return Accuracy of move
     */
    public int getAccuracy()
    {
        return accuracy;
    }
    
    /**
     *Accessor for id
     * @return ID of move
     */
    public int getID()
    {
        return id;
    }

    /**
     *Accessor for damage
     * @return Damage of move
     */
    public int getDamage()
    {
        return damage;
    }

    /**
     *Type
     * @return Type of move
     */
    public String getType()
    {
        return type;
    }

    /**
     * Accessor for id
     * @param id ID of move
     */
    public void setID(int id)
    {
        this.id=id;
    }

    public String toString()
    {
        String temp= name;
        
        temp+=":\nType: "+type+"\nPP("+ppCurrent+"/"+ppMax+")\nAccuracy:"+accuracy+"%\n";
        if(special)
            temp+="Ranged Attack (Special)";
        else
            temp+="Melee Attack (Physical)";
        
        temp+="\nDamage: "+damage+"\nEffect: ";
 
        for (int j = 0; j < effect.length; j++)
        {
            if(effect[0].equals("Metronome"))
                temp+="Pokemon uses a random move";
            else
            {
                if(effect[j].charAt(1)=='+')
                    temp+="in";

                switch (effect[j].charAt(1))
                {
                    case '-':
                        temp+="de";

                    case '+':
                        temp+="creases ";
                        switch(effect[j].substring(5, 7))
                        {
                            case "AT":
                                temp+="Attack";
                                break;
                                
                            case "HE":
                                temp+="HP";
                                break;

                            case "SA":
                                temp+="Special Attack";
                                break;

                            case "DE":
                                temp+="Defence";
                                break;

                            case "SD":
                                temp+="Special Defence";
                                break;

                            case "SP":
                                temp+="Speed";
                                break;

                            case "AL":
                                temp+="All stats";
                                break;      
                        }
                        temp+=" by "+effect[j].substring(7)+" on";
                        break;

                    case 'K':
                        temp+="kills";
                        break;

                    case 'T':
                        temp+="poisons";
                        break;

                    case 'F':
                        temp+="flinches";
                        break;

                    case 'H':
                        if(effect[j].charAt(2)=='+')
                            temp+="heals ";
                        else
                            temp+="deals ";
                        temp+=effect[j].substring(3) + "HP to";
                        break;

                    case 'S':
                        temp+="comatoses";
                        break;

                    case 'P':
                        temp+="paralyzes";
                        break;

                    case 'B':
                        temp+="burns";
                        break;

                    case 'C':
                        temp+="confuses";
                        break;

                    case 'I':
                        temp+="freezes";
                        break;
                }

                if(effect[j].charAt(0)=='0')
                {
                    temp+=" this pokemon";
                } 
                else if(effect[j].charAt(0)=='1')
                {
                    temp+=" opponent";
                }
            }
            temp+="\t\n          ";
        }
        return temp;
    }
    
    /**
     *Determines a score for a move
     * @param p Pokemon the move is being used on
     * @return Score of move
     */
    public int moveScore(Pokemon p)
    {
        try 
        {
            if(ppCurrent==0)
                return 0;
            int score=(50-ppMax)*(ppCurrent/ppMax);
            String query="SELECT weak,resistant,immune FROM TYPES WHERE \"TYPE\" = '"+p.getType0()+"'";
            if(p.getType1()!=null)
                query+= " OR \"TYPE\" = '"+p.getType1()+"'";
            
            ResultSet rs = p.getDb().query(query);
            int typeEffect=0;
            while(rs.next())
            {
                if(rs.getString(1).indexOf(this.getType())>0)//immune
                {
                    typeEffect+=0;
                }
                else if(rs.getString(2).indexOf(this.getType())>0)//resistant
                {
                    typeEffect+=1;
                }
                else if(rs.getString(3).indexOf(this.getType())>0)//weak
                {
                    typeEffect+=3;
                }
            }
            double typeEff=0;
            switch(typeEffect)
            {
                case 0://immune or immune, immune
                     typeEff=0.1;
                    break;
                case 1://resistant or immune,resistant
                     typeEff=0.5;
                    break;
                case 2://resistant,resistant
                    typeEff=0.25;
                    break;
                case 3://weak or weak,immune
                    typeEff=2;
                    break;
                // case 4 would be weak and resistance which will times by 1 thus redundent
                case 6://weak, weak
                     typeEff=4;
                    break;
            }
            typeEff/=4;
            typeEff++;
            score*=typeEff;
            if(score<1)
                score=1;
            return score;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return 0;
    }

    /**
     *Mutator for ppCurrent
     * @param ppCurrent The value ppCurrent will be set to
     */
    public void setPpCurrent(int ppCurrent)
    {
        this.ppCurrent = ppCurrent;
    }

    /**
     *Accessor for ppMax
     * @return Total uses of the move
     */
    public int getPpMax()
    {
        return ppMax;
    }

    /**
     *Accessor for ppCurrent
     * @return Uses remaing of the move
     */
    public int getPpCurrent()
    {
        return ppCurrent;
    }

    /**
     *Accessor for effects
     * @return Effects of the move
     */
    public String[] getEffect()
    {
        return effect;
    }

    /**
     *Accessor for special(ranged)
     * @return Whether the move is special(ranged)
     */
    public boolean isSpecial()
    {
        return special;
    }
    
    /**
     *Returns the number of effects the move has
     * @return The number of effects the move has
     */
    public int numEffects()
    {
        return effect.length;
    }
}
