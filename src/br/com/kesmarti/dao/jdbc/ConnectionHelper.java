/**
 * 
 */
package br.com.kesmarti.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * @author Kelson Martins
 *
 */

public class ConnectionHelper {

	/**
	 * @param args
	 */
		// TODO Auto-generated method stub
		public static Connection getConnection() throws SQLException {
			String url = "jdbc:mysql://localhost:3300/TaskManager";
			return DriverManager.getConnection(url,"admin","admin");
		}
	}
