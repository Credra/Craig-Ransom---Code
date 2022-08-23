/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *Stores the unique Player-Pokemon ID and the pokemon ID of pokemon in storage in the format: "POKEMONID,PLAYERPOKEID"
 * @author craig
 */
public class PokeViewer
{
    private int pages;
    private String pokemon [];

    /**
     *Constructor for PokeViewer
     * @param rs ResultSet containing the columns POKEMONID and PLAYERPOKEID
     */
    public PokeViewer(ResultSet rs)
    {
        try
        {
            LinkedList<String> poke = new LinkedList<String>();//POKEMONID, PLAYERPOKEID
            pages=1;
            while(rs.next())            
            {
                poke.add(rs.getInt(1)+","+rs.getInt(2));
                if(poke.size()-40*pages>0)
                {
                    pages++;
                }
            }
            pages++;
            pokemon= new String[pages*40];
            for (int i = 0; i < poke.size(); i++)
                pokemon[i]=poke.get(i);
        } catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }
    
    /**
     *Accessor for total number of pages
     * @return Total number of pages
     */
    public int getPages()
    {
        return pages;
    }

    /**
     *Mutator method for pokemon array
     * @param pos Position in the array
     * @param val Value to be set
     */
    public void setPokemon(int pos, String val)
    {
        pokemon[pos]=val;
    }

    /**
     *Accessor for pokemon array
     * @param i Position in array
     * @return The String stored in position i of the array
     */
    public String getCurrentPokemon(int i)
    {
        if(i>pokemon.length)
            return null;
        return pokemon[i];
    }
    
    /**
     *Switches the values of i and j in the array
     * @param i Position of value to be swapped
     * @param j Position of value to be swapped
     */
    public void swop(int i, int j)
    {
        String temp=pokemon[i];
        pokemon[i]=pokemon[j];
        pokemon[j]=temp;
    }
    
    /**
     *Accessor for Pokemon ID
     * @param pos Position in array
     * @return The pokemon ID of the pokemon in position pos in the array
     */
    public int getPokeID(int pos)
    {
        if(pokemon[pos]==null)
            return 0;
        return Integer.parseInt(pokemon[pos].substring(0, pokemon[pos].indexOf(",")));
    }
    
     /**
     *Accessor for Player-Pokemon ID
     * @param pos Position in array
     * @return The Player-Pokemon ID of the pokemon in position pos in the array
     */
    public int getPlayerPokeID(int pos)
    {
         return Integer.parseInt(pokemon[pos].substring(1+pokemon[pos].indexOf(",")));
    }
            
}
