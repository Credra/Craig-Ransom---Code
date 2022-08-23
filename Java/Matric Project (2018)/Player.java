package ransom;

import java.sql.SQLException;

/**
 *The player class stores all information related to the player
 * @author craig
 */
public class Player extends PlayerSprite
{
    private String name;
    private Pokemon pokes[]=new Pokemon[6];
    private int numPokes,gold,pokeball,greatball,ultraball, playerID, caveTickets;
    private DB db;
    private long time;
    private boolean cave;
    private Music music;
    /**
     *Constructor for the Player Class
     * @param name The name of the player
     * @param id The unique Player ID
     * @param db The already initialized DB Class
     * @param pokes Array of Pokemon
     * @param ps PlayerSprtie GameObject
     * @param gold Amount of gold the player has
     * @param pokeball Amount of pokeballs the player has
     * @param greatball Amount of greatballs the player has
     * @param ultraball Amount of ultraballs the player has
     * @param caveTickets Amount of caveTickets the player has
     */
    public Player(String name, int id, DB db,Pokemon pokes[], PlayerSprite ps, int gold, int pokeball, int greatball, int ultraball,int caveTickets, Music music)
    {
        super(ps, name);
        playerID=id;
        this.pokes=pokes;
        numPokes();
        this.db=db;
        this.name=name;
        this.gold=gold;
        this.pokeball=pokeball;
        this.greatball=greatball;
        this.ultraball=ultraball;
        this.caveTickets=caveTickets;
        time=0;
        cave=false;
        this.music=music;
        switch(this.getCurrentArea())
        {
            case Shop:
                music.shop();
                break;
            case Cave:
                music.cave();
                break;
            case PokeCenter:
                music.pokeCenter();
                break;
            case MainArea:
            default:
                music.main();
                break;
        }
    }
        
    private void numPokes()
    {
        int temp=0;
        for (int i = 0; i < getPokes().length; i++)
        {
            if(getPokes()[i]!=null)
                temp++;
        }
        numPokes= temp;
    }
    
    /**
     *Heals all the pokemon in the player's party
     */
    public void heal()
    {
        if(messages.getMessage(3)=="Your pokemon have been heal")
            return;
        
        messages.addMessage("Your pokemon have been heal");
        compact();
        for (int i = 0; i < getNumPokes(); i++)
            getPokes()[i].heal();
    }

    /**
     *Ensures that the array of Pokemon never has a null before a pokemon
     */
    public void compact()
    {
        for (int i = 0; i < getPokes().length; i++)
        {
            if(getPokes()[i]==null)
            {
                int j=i+1;
                boolean replaced=false;
                while(!replaced&&j<getPokes().length)
                {
                    if(getPokes()[j]!=null)
                    {
                        pokes[i]=getPokes()[j];
                        pokes[j]=null;
                        replaced=true;
                    }
                    j++;
                }
            }
        }
    }

    /**
     *Saves the game, stores the player's information in the database
     */
    public void save()
    {
        try
        {
            String query="UPDATE PLAYER SET \"GOLD\" = "+getGold()+", \"POKEBALL\" = "+getPokeball()+", \"GREATBALL\" = "+getGreatball()+", \"ULTRABALL\" = "+getUltraball()+", \"CAVETICKETS\" = "+getCaveTickets()+", \"SCREEN\" = '"+game.getArea()+"' WHERE PLAYERID = "+getPlayerID();
            getDb().update(query);
            numPokes();
            compact();
            getDb().update("DELETE FROM PARTY WHERE PLAYERID = "+getPlayerID());
            for (int i = 0; i < getNumPokes(); i++)
            {
                query="INSERT INTO PARTY (PLAYERID, POKEMONID, \"LEVEL\", EXP, LEVELSTATS, PLAYERPOKEID, CURRENTSTATS) VALUES ("+getPlayerID()+", ";
                query+=getPokes()[i].getPokeID()+", ";                                               /*                       */
                query+=getPokes()[i].getLvl()+", ";
                query+=getPokes()[i].getExp()+", ";
                query+=getPokes()[i].getLevelStats()+",";
                query+=getPokes()[i].getPlayerPokeId()+", '";
                query+=getPokes()[i].getFormattedCurrent()+"')";
                getDb().update(query);
                getDb().update("DELETE FROM POKEMONMOVES WHERE PLAYERID ="+ getPlayerID()+"AND PLAYERPOKEMONID ="+getPokes()[i].getPlayerPokeId());
                for (int j = 0; j < getPokes()[i].getNumMoves(); j++)
                {
                   query="INSERT INTO POKEMONMOVES (PLAYERID, PLAYERPOKEMONID, MOVEID, PP) VALUES (";
                   query+=getPlayerID()+", ";
                   query+=getPokes()[i].getPlayerPokeId()+", ";
                   query+=getPokes()[i].getMove(j).getID()+", ";
                   query+=getPokes()[i].getMove(j).getPpCurrent()+")";
                   getDb().update(query);
                }
            }
        } catch (SQLException ex)
        {
            addMessage(ex.toString());
        }

    }

    /**
     *Accessor for name
     * @return Player's name
     */
    public String getName()
    {
        return name;
    }

    /**
     *Accessor for the array of Pokemon
     * @return Array of Pokemon
     */
    public Pokemon[] getPokes()
    {
        return pokes;
    }
    
    /**
     *Accessor for a Pokemon in postion i of the array
     * @param i The position in the array
     * @return Pokemon at position i 
     */
    public Pokemon getPokemon(int i)
    {
        return getPokes()[i];
    }

    /**
     *Returns the number of pokemon in the player's party
     * @return Number of pokemon
     */
    public int getNumPokes()
    {
        return numPokes;
    }

    /**
     *Accessor for gold
     * @return Amount of gold
     */
    public int getGold()
    {
        return gold;
    }

    /**
     *Accessor for pokeballs
     * @return Number of pokeballs
     */
    public int getPokeball()
    {
        return pokeball;
    }

    /**
     *Accessor for greatballs
     * @return Number of greatballs
     */
    public int getGreatball()
    {
        return greatball;
    }

    /**
     *Accessor for ultraballs
     * @return Number of Ultraballs
     */
    public int getUltraball()
    {
        return ultraball;
    }

    /**
     *Accessor for playerID
     * @return Player's unique player id
     */
    public int getPlayerID()
    {
        return playerID;
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
     *Accessor for name
     * @param name Player's name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *Accessor for active pokemon
     * @return The 1st pokemon with HP is greater than 0
     */
    public Pokemon getActivePokemon()
    {
        compact();
        for (int i = 0; i < getNumPokes(); i++)
        {
            if(getPokes()[i].getCurrent()[0]>0)
                return getPokes()[i];
        }
        return null;
    }

    /**
     *Accessor for caveTickets
     * @return Number of Cave Tickets
     */
    public int getCaveTickets()
    {
        return caveTickets;
    }

    /**
     *Accessor for time
     * @return Time
     */
    public long getTime()
    {
        return time;
    }
    
    /**
     *Accessor for Formatted Time
     * @return The time the player has remaining left in the format mm:ss
     */
    public String getFormattedTime()
    {
        int t = (int)((getTime()-System.nanoTime())/1000000000+5*60);//+ amount of time in cave 60*5=300
        if(t<0)
        {
            if(game.getArea()==ID.Cave)
            {
                setY(325);
                game.moveTo(ID.MainArea);
            }
            cave=false;
            messages.addMessage("You time in the cave has expired");
            return "";
        }
        int min=t/60, sec=t%60;
        String temp="";
        if(min<10)
            temp+="0";
        temp+=min+":";
        if(sec<10)
            temp+="0";
        temp+=sec+"";
        return temp;
    }

    /**
     *Accessor for cave 
     * @return Whether the player has an active cave ticket
     */
    public boolean isCave()
    {
        return cave;
    }
    
    /**
     *Mutator method for the array of Pokemon
     * @param pokes Array of Pokemon
     */
    public void setPokes(Pokemon[] pokes)
    {
        this.pokes = pokes;
    }

    /**
     *Adds a new Pokemon to the Pokemon Array in position i
     * @param poke Pokemon
     * @param pos Position
     */
    public void setPoke(Pokemon poke, int pos)
    {
        pokes[pos] = poke;
    }
    
    /**
     *Mutator for number of Pokemon in player's party
     * @param numPokes Number of pokemon
     */
    public void setNumPokes(int numPokes)
    {
        this.numPokes = numPokes;
    }
 
    /**
     *Mutator for amount of gold
     * @param gold Amount of gold
     */
    public void setGold(int gold)
    {
        this.gold = gold;
    }

    /**
     *Mutator for amount of pokeballs
     * @param pokeball Number of pokeballs
     */
    public void setPokeball(int pokeball)
    {
        this.pokeball = pokeball;
    }

    /**
     *Mutator for amount of greatball
     * @param greatball Number of greatballs
     */
    public void setGreatball(int greatball)
    {
        this.greatball = greatball;
    }

    /**
     *Mutator for amount of ultraballs
     * @param ultraball number of ultraballs
     */
    public void setUltraball(int ultraball)
    {
        this.ultraball = ultraball;
    }


    /**
     *Calls battle method in Game Class
     * @param p The wild pokemon the player will battle
     */
    public void battle(Pokemon p)
    {
        game.battle(p);
    }
    
    /**
     *Calls connect method in Game Class
     */
    public void connect()
    {
        game.connect();
    }
    
    /**
     *Calls sell method in Game Class
     */
    public void sell()
    {
        game.sell();
    }
    
    /**
     *Calls buy method in Game Class
     */
    public void buy()
    {
        game.buy();
    }

    /**
     *Calls storage method in Game Class
     */
    public void storage()
    {
        game.storage();
    }

    /**
     *Allows the player to enter the cave for a specific time frame
     */
    public void enterCave()
    {
        caveTickets--;
        time=System.nanoTime();
        cave=true;
    }

    /**
     *Switches the pokemon in the Pokemon Array at positions i and j
     * @param i Position in array of object to be swapped
     * @param j Position in array of object to be swapped
     */
    public void swop(int i, int j)
    {
        Pokemon temp=getPokes()[i];
        pokes[i]=getPokes()[j];
        pokes[j]=temp;
        
    }
     
    /**
     *Mutator method for caveTickets
     * @param caveTickets New number of cave tickets
     */
    public void setCaveTickets(int caveTickets)
    {
        this.caveTickets = caveTickets;
    }

    /**
     *Mutator for whether the player is allowed to enter the cave
     * @param cave True or False
     */
    public void setCave(boolean cave)
    {
        this.cave = cave;
    }

    public Music getMusic()
    {
        return music;
    }
    
    
}
