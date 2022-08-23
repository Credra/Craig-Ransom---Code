/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ransom;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


//This class is used to connect to the java derby database

/**
 *Used to connect to, query and update the database
 * @author craig
 */
public class DB
{

    private static final String driver = "org.apache.derby.jdbc.ClientDriver";
    private static final String user = null;
    private static final String password = null;
    private static final String url = "jdbc:derby://localhost:1527/Pokemon";
    private Connection connection;
    private PreparedStatement statement;
    private ResultSet resultSet;

    /**
     *Constructor for DB Class
     */
    public DB()
    {
        try
        {
            Class.forName(driver);//Loads the driver to connect to the database
            connection = DriverManager.getConnection(url, user, password);//a connection is made with the database
        } catch (SQLException s)
        {
            System.out.println("Cannot connect to database" + s);
        } catch (ClassNotFoundException c)
        {
           System.out.println("Cannot load driver " + c);

        }


    }

    /**
     *Queries the database and returns the data in a ResultSet
     * @param query The String that will be executed as a command
     * @return ResultSet of executed command
     * @throws SQLException Thrown in order to be handler in a higher level
     */
    public ResultSet query(String query) throws SQLException
    {
        statement = connection.prepareStatement(query);
        resultSet = statement.executeQuery();
        return resultSet;
    }

    /**
     *Updates infomation in the database
     * @param query The String that will be executed as a command
     * @throws SQLException Thrown in order to be handler in a higher level
     */
    public void update(String query) throws SQLException
    {
        statement = connection.prepareStatement(query);
        statement.executeUpdate();
        statement.close();
    }
}

