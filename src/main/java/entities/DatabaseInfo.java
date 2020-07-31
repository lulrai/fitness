package entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInfo {

	private static final String url = "";		//Database url
	private static final String username = "";							//Username
	private static final String password = "";							//Password

	static public Connection getConnection()						//Creates and returns a new connection object
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();	//Initialize the mysql driver
		} catch (InstantiationException e) {
			System.out.println("InstantiationException: ");	//Catch all the exceptions
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException: ");
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		Connection conn;		//Create a new connection variable and initialize it

		try {
			conn = DriverManager.getConnection(DatabaseInfo.url, DatabaseInfo.username, DatabaseInfo.password);	//Set the connection information and create a new object
		} catch (SQLException e) {
			System.out.println("SQLException: ");		//Exception handling
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		return conn;		//Return connection
	}

	static public void closeConnection(Connection c, Statement s) {	//Closes the provided connection and statement
		try {
			if(s != null && !s.isClosed()) {	//If the statement isn't closed or isn't null..
				s.close();		//Close the statement
			}
			if(c != null && !c.isClosed()) {	//If the connection isn't closed or isn't null..
				c.close();		//Close the connection
			}
		}catch(SQLException e) {
			e.printStackTrace();	//Exception
		}
	}
}
