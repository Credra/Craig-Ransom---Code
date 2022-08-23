package ransom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Stores all data relating to a specific pokemon
 * @author craig
 */
public class Pokemon
{ 
    private int pokeId, lvl, exp, expToLevel, stage, playerPokeId,numMoves;
    private Move moves[]=new Move[4];
    private String name;
    private String evolve, type0,type1;
    private int [] current= new int [6];// HP , Speed , Attack , Special Attack, Defense, Special Defense
    private int [] total= new int [6];// HP , Speed , Attack , Special Attack, Defense, Special Defense
    private DB db;
    private int levelStats;
    private static final String[] STATLEVEL = new String[] {"0,1,2,3,4,5","0,1,2,4,3,5","0,1,2,5,3,4","0,2,1,3,4,5","0,2,1,4,5,3","0,2,1,4,5,3","0,3,1,2,4,5","0,3,1,4,5,2","0,3,1,5,4,2","0,4,1,2,3,5","0,4,1,3,2,5","0,4,1,5,2,3","0,5,1,2,3,4","0,5,1,3,4,2","0,5,1,4,3,2"};
    private boolean status[] = new boolean[7];//Poison, Sleep, Paralyzed, Burn, Confused, Ice, Flinch
// <editor-fold defaultstate="collapsed" desc="Hidden"> 
    /**
     *Constructor for pokemon from storage
     * @param db Connecting class to database
     * @param playerPokeId Player unique ID to identify each pokemon
     * @param pokeID Pokemon unique ID to identify each pokemon
     * @param level Level of pokemon
     * @param exp Experience the pokemon has gained
     * @param stage The evolution stage of the pokemon 
     * @param name The name of the pokemon
     * @param levelStats Determines the distribution of stats 
     * @param type0 1st type of the pokemon
     * @param type1 2nd type of the pokemon
     * @param moves Moves known by the pokemon
     * @param numMoves Number of moves known by the pokemon
     * @param evolve The level the pokemon evolves and what it evolves into in the formate: "Level,ID"
     */
    public Pokemon(DB db,int playerPokeId,int pokeID, int level, int exp, int stage,String name, int levelStats,String type0,String type1,Move[] moves,int numMoves, String evolve)//storage
    {
        this.evolve=evolve;
        this.db=db;
        this.type0=type0;      
        this.type1=type1;
        this.stage=stage;
        this.playerPokeId=playerPokeId;
        this.pokeId = pokeID;
        this.lvl = level;
        this.exp = exp;
        expToLevel= level*5;
        this.moves = moves;
        this.name = name;
        this.levelStats=levelStats;
        this.numMoves=numMoves;
        calculateTotal();
        current= total;
        removeStatus();
        
    }
    
     /**
     *Constructor for pokemon in the player's party
     * @param db Connecting class to database
     * @param playerPokeId Player unique ID to identify each pokemon
     * @param pokeID Pokemon unique ID to identify each pokemon
     * @param level Level of pokemon
     * @param exp Experience the pokemon has gained
     * @param stage The evolution stage of the pokemon 
     * @param name The name of the pokemon
     * @param levelStats Determines the distribution of stats 
     * @param type0 1st type of the pokemon
     * @param type1 2nd type of the pokemon
     * @param moves Moves known by the pokemon
     * @param numMoves Number of moves known by the pokemon
     * @param current The current stats of the pokemon
     * @param evolve The level the pokemon evolves and what it evolves into in the formate: "Level,ID"
     */
   
    public Pokemon(DB db,int playerPokeId,int pokeID, int level, int exp, int stage, String name, int levelStats, String type0,String type1,Move[] moves, int numMoves, int current[], String evolve)//party
    {
        this.evolve=evolve;
        this.db=db;
        this.type0=type0;      
        this.type1=type1;
        this.stage=stage;
        this.playerPokeId=playerPokeId;
        this.pokeId = pokeID;
        this.lvl = level;
        this.exp = exp;
        expToLevel= level*5;
        this.moves = moves;
        this.name = name;
        this.current=current;
        this.numMoves=numMoves;
        this.levelStats=levelStats;
        calculateTotal();
        heal();
        
    }
    
   /**
     *Constructor for wild pokemon
     * @param db Connecting class to database
     * @param pokeId Pokemon unique ID to identify each pokemon
     * @param level Level of pokemon
     * @param stage The evolution stage of the pokemon 
     * @param name The name of the pokemon
     * @param type0 1st type of the pokemon
     * @param type1 2nd type of the pokemon
     * @param evolve The level the pokemon evolves and what it evolves into in the formate: "Level,ID"
     */
    public Pokemon(int pokeId, String name,int level, int stage, String type0,String type1, DB db, String evolve)//WILD
    {
        this.evolve=evolve;
        this.type0=type0;      
        this.type1=type1;   
        this.db=db;
        this.stage=stage;
        this.pokeId = pokeId;
        this.evolve=evolve;
        double random=Math.random();
        random=(5*random*random);
        if(Math.random()>.5)
            random*=-1;
        if(name.isEmpty())
            lvl=level;
        else
        {
            lvl= (int)(level+random);
            if(lvl<1)
                lvl=1;
        }
        
        int numMoves= (int)Math.round((2*Math.ceil(lvl/10)+stage/2)/2);
        if(numMoves<1)
            numMoves=1;
        else if(numMoves>4)
            numMoves=4;
        System.out.println(numMoves);
        for (int i = 0; i < numMoves; i++)
            getMove();
        this.name = name;
        levelStats=(int)(Math.random()*45)+1;
        calculateTotal();
        heal();
    }

    /**
     *Constructor for networked pokemon
     * @param pokeId Pokemon unique ID to identify each pokemon
     * @param lvl Level of pokemon
     * @param playerPokeId Player unique ID to identify each pokemon
     * @param db Connecting class to database
     * @param levelStats Determines the distribution of stats 
     * @param num Number of moves known by the pokemon
     * @param movesID Array of integers of the id of the moves known by the pokemon
     */// </editor-fold>
    public Pokemon(int pokeId, int lvl, int playerPokeId, DB db, int levelStats,int num, int [] movesID)//networked
    {
        try
        {
            this.pokeId = pokeId;
            this.lvl = lvl;
            this.playerPokeId = playerPokeId;
            this.db = db;
            this.levelStats = levelStats;
            ResultSet rs=db.query("SELECT \"NAME\",STAGE,TYPE0,TYPE1 FROM POKEMON WHERE ID ="+pokeId);
            rs.next();
            name=rs.getString(1);
            stage=rs.getInt(2);
            type0=rs.getString(3);
            type1=rs.getString(4);
            calculateTotal();
            numMoves=num;
            this.moves= new Move[4];
            for (int i = 0; i < num; i++)
            {
                rs=db.query("SELECT * FROM MOVES WHERE ID  = "+movesID[i]);
                ResultSet rsEffect = db.query("SELECT * FROM MOVESEFFECTS WHERE ID = "+movesID[i]);
                LinkedList<String> temp = new LinkedList<String>();
                while(rsEffect.next())
                {
                    temp.add(rsEffect.getString(2));
                }
                String [] effects = new String[temp.size()];
                for (int j = 0; j < effects.length; j++)
                {
                    effects[j]= temp.get(j);
                }
                rs.next();
                this.moves[i]=new Move(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getBoolean(5),rs.getInt(6),rs.getInt(7), effects);
                System.out.println(i+" "+this.moves[i].getName());
            }
        } catch (SQLException ex)
        {
            System.out.println(ex);
            System.out.println("ERROR IN USES DB");
        }
    }
    

    private void calculateTotal()
    {
        int level=lvl;
        lvl=0;
        for (int i = 0; i < level; i++)
            levelUp();
    }

    /**
     *Increase the pokemon's level and stats
     */
    public void levelUp()
    {
        lvl++;
        int inc;
        if(lvl<25)
            inc=2;
        else if(lvl<50)
            inc=3;
        else if(lvl<=75)
            inc=4;
        else if(lvl<100)
            inc=5;
        else
            inc=6;  
        if(lvl%10==0||lvl==1)
            for (int j = 0; j < total.length; j++)
            {
                total[j]+=inc*(stage+8)/10;
                current[j]+=inc*(stage+8)/10;
            }
        else
        {
            inc=2*(inc+1);
            String t[]=STATLEVEL[levelStats/6].split(",");
            int [] temp = new int [6];
            switch(levelStats%6)
            {
                case 0:
                    temp[0]=Integer.parseInt(t[0]);
                    temp[1]=Integer.parseInt(t[1]);
                    
                    temp[2]=Integer.parseInt(t[2]);
                    temp[3]=Integer.parseInt(t[3]);
                    
                    temp[4]=Integer.parseInt(t[4]);
                    temp[5]=Integer.parseInt(t[5]);
                    break;
                case 1:
                    temp[0]=Integer.parseInt(t[0]);
                    temp[1]=Integer.parseInt(t[1]);
                    
                    temp[4]=Integer.parseInt(t[4]);
                    temp[5]=Integer.parseInt(t[5]);
                    
                    temp[2]=Integer.parseInt(t[2]);
                    temp[3]=Integer.parseInt(t[3]);
                    break;
                case 2:
                    temp[2]=Integer.parseInt(t[2]);
                    temp[3]=Integer.parseInt(t[3]);
                    
                    temp[0]=Integer.parseInt(t[0]);
                    temp[1]=Integer.parseInt(t[1]);
                    
                    temp[4]=Integer.parseInt(t[4]);
                    temp[5]=Integer.parseInt(t[5]);
                    break;
                case 3:
                    temp[2]=Integer.parseInt(t[2]);
                    temp[3]=Integer.parseInt(t[3]);
                    
                    temp[4]=Integer.parseInt(t[4]);
                    temp[5]=Integer.parseInt(t[5]);
                    
                    temp[0]=Integer.parseInt(t[0]);
                    temp[1]=Integer.parseInt(t[1]);
                    break;
                case 4:
                    temp[4]=Integer.parseInt(t[4]);
                    temp[5]=Integer.parseInt(t[5]);
                    
                    temp[0]=Integer.parseInt(t[0]);
                    temp[1]=Integer.parseInt(t[1]);
                    
                    temp[2]=Integer.parseInt(t[2]);
                    temp[3]=Integer.parseInt(t[3]);
                    break;
                case 5:
                    temp[4]=Integer.parseInt(t[4]);
                    temp[5]=Integer.parseInt(t[5]);
                    
                    temp[2]=Integer.parseInt(t[2]);
                    temp[3]=Integer.parseInt(t[3]);
                    
                    temp[0]=Integer.parseInt(t[0]);
                    temp[1]=Integer.parseInt(t[1]);
                    break;
            }
            if(lvl%2==0)
            {
                current[temp[0]]++;
                total[temp[0]]++;
                current[temp[1]]++;
                total[temp[1]]++;
                inc-=2;
            }
            int i=0;
            while(inc>0)
            {
                if(i>4)
                    i=0;
                current[temp[i]]++;
                total[temp[i]]++;
                current[temp[i+1]]++;
                total[temp[i+1]]++;
                i+=2;
                inc-=2;
            }
            
        }
    }

    /**
     *teaches the pokemon a random move of their types
     */
    public void getMove()
    {
        try
        {
            int arr[]=new int[]{30,25,20,15,10,5};
            String query="SELECT * FROM MOVES WHERE (TYPE = 'Normal' OR TYPE = '"+type0;
            if(type1!=null)
                query+="' OR TYPE = '"+type1;
            
            query+="')";
            for (int i = 0; i < numMoves; i++)
            {
                query+= " AND ID !=" + moves[i].getID();
            }
            
            if(lvl<=95)
            {
                int temp[]={0,0};
                int storer[]=new int[2];
                int level=lvl/5;
                for (int i = 0; i < arr.length; i++)
                {
                    int chance=-Math.abs(level -(int)(-0.8*arr[i]+25))*25 +100;
                    if(chance>100 || chance<0)
                        chance=0;
                    
                    if(chance!=0)
                    {
                        if(temp[0]==0)
                        {
                            temp[0]=chance;
                            storer[0]=arr[i];
                        }
                        else
                        {
                            temp[1]=chance;
                            storer[1]=arr[i];
                        }
                    }
                }
                int pp=0;
                int i=((int)(Math.random()*100));
                if(i<=temp[0])
                    pp=storer[0];
                else
                    pp=storer[1];
                
                query+=" AND PP = " +pp;
            }
            
            query+=" ORDER BY RANDOM()";
            ResultSet rs=db.query(query);
            if(rs.next())
            {
                ResultSet rsEffect = db.query("SELECT EFFECT FROM MOVESEFFECTS WHERE ID = "+rs.getInt("ID"));
                LinkedList<String> temp = new LinkedList<String>();
                while(rsEffect.next())
                    temp.add(rsEffect.getString(1));
                
                String [] effects = new String[temp.size()];
                for (int i = 0; i < effects.length; i++)
                    effects[i]= temp.get(i);
                
                if(numMoves==4)
                    new MovesGUI(this,new Move(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getBoolean(5),rs.getInt(6),rs.getInt(7), effects)).setVisible(true);
                else
                {
                    moves[numMoves]=new Move(rs.getString(1),rs.getString(2),rs.getInt(3),rs.getInt(4),rs.getBoolean(5),rs.getInt(6),rs.getInt(7), effects);
                    System.out.println( moves[numMoves].getName());
                    numMoves++;
                }
            }
            else
            {
                System.out.println(query + "\nempty\n");
            }
            
        } catch (SQLException ex)
        {
            System.out.println(ex);        }
    }
    
    /**
     *Evolve a pokemon into a new one
     * @param p The Player that owns the pokemon
     * @return 
     */
    public boolean evolve(Player p)
    {
        if(evolve==null)
            return false;
        int level=Integer.parseInt(evolve.substring(0, 2));
        int id=Integer.parseInt(evolve.substring(3));
        if(lvl>=level)
        {
            try {
                boolean found=false;
                int pos=0;
                p.compact();
                while(!found&&pos<6)
                    if(p.getPokemon(pos).equals(this))
                        found=true;//(DB db,int playerPokeId,int pokeID, int level, int exp, int stage, String name, double [] levelStats, String type0,String type1,Move[] moves, int numMoves, int current[])
                //stage name tyoe0 type1
                ResultSet rs = db.query("SELECT STAGE, \"NAME\", TYPE0, TYPE1, EVOLVE FROM POKEMON WHERE ID = "+id);
                rs.next();
                Pokemon temp= new Pokemon(db,playerPokeId,id,lvl,exp,rs.getInt(1),rs.getString(2),levelStats,rs.getString(3),rs.getString(4),moves,numMoves,current,rs.getString(5));
                p.getPokes()[pos]=temp;
                return true;
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
        return false;
    }

    /**
     *Determines the experience from kills
     * @param level Level of killed pokemon
     * @param stage Stage of killed pokemon
     * @return Amount of experience gained
     */
    public boolean expFromKO(int level, int stage)
    {
        int temp=((level*(this.lvl+level))/(2*lvl)) * ((stage - this.stage)/10 +1);
        exp+=temp;
        if(exp>=expToLevel)
        {
            exp-=expToLevel;
            levelUp();
            expToLevel= lvl*5;
            if(lvl%5==0)
                getMove();
            return true;
        }
        return false;
    }
            
    /**
     *Restores all of the pokemon's stats to their maximum
     */
    public void heal()
    {
        for (int i = 0; i < current.length; i++)
        {
            current[i]=total[i];
        }
        removeStatus();
        restorePP();
    }
    
    /**
     *Removes all status effects affecting the pokemon
     */
    public void removeStatus()
    {
        for (int i = 0; i < status.length; i++)
        {
            status[i]=false;
            
        }
    }
    
    /**
     *Restores the pp of each move to their maximum
     */
    public void restorePP()
    {
        for (int i = 0; i < numMoves; i++)
            moves[i].setPpCurrent(moves[i].getPpMax());
    }

    /**
     *Mutator method for Status
     * @param pos At position pos of the array
     * @param stats Set value to stats
     */
    public void setStatus(int pos, boolean stats)
    {
        status[pos]=stats;
    }

    /**
     *Accessor for Pokemon ID
     * @return Pokemon's ID
     */
    public int getPokeID()
    {
        return pokeId;
    }

    /**
     *Accessor for Level
     * @return Pokemon's Level
     */
    public int getLvl()
    {
        return lvl;
    }

    /**
     *Accesor for Experience
     * @return Experience
     */
    public int getExp()
    {
        return exp;
    }

    /**
     *Accesor for number of moves
     * @return Number of moves
     */
    public int getNumMoves()
    {
        return numMoves;
    }
    
    /**
     *Accesor for expToLevel
     * @return Experience required to level
     */
    public int getExpToLevel()
    {
        return expToLevel;
    }

    /**
     *Accessor for Stage
     * @return Pokemon's Stage
     */
    public int getStage()
    {
        return stage;
    }
    
    /**
     *Accessor for pokemon's moves
     * @param i Position i
     * @return The move in position i
     */
    public Move getMove(int i)
    {
        if(moves[i].getPpCurrent()>0)
            return moves[i];
        return new Move("Struggle","Normal",0,100,false,176,0,new String[]{"1H-40","0H-50"});
    }

    /**
     *Accessor for Pokemon's name
     * @return Pokemon's name
     */
    public String getName()
    {
        return name;
    }

    /**
     *Accessor for price of pokemon
     * @return The amount of gold gained by selling this pokemon
     */
    public int getPrice()
    {
        int level=lvl;
        if(lvl>100)
            level=100;
        int gold = (level+1)*(stage+4)/16;
        if(gold<1)
            return 1;
        return gold;
    }

    /**
     *Accessor for status
     * @return Status array
     */
    public boolean[] getStatus()
    {
        return status;
    }



    /**
     *Accessor for Type0
     * @return Type0
     */
    public String getType0()
    {
        return type0;
    }

    /**
     *Accessor for Type1
     * @return Type1
     */
    public String getType1()
    {
        return type1;
    }

    /**
     *Accessor for DB
     * @return DB
     */
    public DB getDb()
    {
        return db;
    }

    /**
     *Accessor for Current
     * @return Current stats
     */
    public int[] getCurrent()
    {
        return current;
    }

    /**
     *Accessor for Total
     * @return Total stats
     */
    public int[] getTotal()
    {
        return total;
    }

    /**
     *Accessor for current
     * @param pos Position
     * @return Current stats at position pos of the array
     */
    public int getCurrent(int pos)
    {
        return current[pos];
    }

    /**
     *Accessor for total
     * @param pos Position
     * @return Total stats at position pos of the array
     */
    public int getTotal(int pos)
    {
        return total[pos];
    }
    
    /**
     *Accessor for offensive stats
     * @param ranged If the attack is ranged
     * @return The offensive stats
     */
    public int getAttackValue(boolean ranged)// HP , Speed , Attack , Special Attack, Defense, Special Defense
    {
        if(ranged)
            return current[3];
        return current[2];
    }
    
    /**
     *Accessor for defensive stats
     * @param ranged If the attack is ranged
     * @return The defensive stats
     */
    public int getDefenceValue(boolean ranged)// HP , Speed , Attack , Special Attack, Defense, Special Defense
    {
        if(ranged)
            return current[5];
        return current[4];
    }
    
    /**
     *Damages the pokemon
     * @param damage The damage to be delt
     */
    public void damage(int damage)
    {
        current[0]-=damage;        
    }

    /**
     *Accessor for Player Pokemon ID
     * @return The Player Pokemon ID
     */
    public int getPlayerPokeId()
    {
        return playerPokeId;
    }

    /**
     *Accessor for LevelStats
     * @return The pokemon's levels stats
     */
    public int getLevelStats()
    {
        return levelStats;
    }

    /**
     *Accessor for Formatted Current Stats
     * @return Stats seperated by a ","
     */
    public String getFormattedCurrent()
    {
        String temp=""+current[0];
        for (int i = 1; i < current.length; i++)
            temp+=","+current[i];
        return temp;
    }

    /**
     *Mutator for playerPokeId
     * @param playerPokeId The new playerPokeId value
     */
    public void setPlayerPokeId(int playerPokeId)
    {
        this.playerPokeId = playerPokeId;
    }

    /**
     *Mutator for status
     * @param status The value status will be set to
     */
    public void setStatus(boolean[] status)
    {
        this.status = status;
    }
    
    /**
     *Accessor for numStatus
     * @return Number of Status effects affecting the pokemon
     */
    public int getNumStatus()
    {
        int num=0;
        for (int i = 0; i < status.length; i++)
            if(status[i])
                num++;
        return num;
    }
    
    /**
     *Accessor fo the array of moves
     * @return Array of moves the pokemon knows
     */
    public Move[] getMoves()
    {
        return moves;
    }
}
