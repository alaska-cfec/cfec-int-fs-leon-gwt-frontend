package com.cfecweb.leon.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PervasiveConnection {
	
	public static Connection getit(String btreiveHost) throws SQLException {		
		String url = "jdbc:pervasive://146.63.17.17:1583/Licensing"+btreiveHost+"?transport=tcp";
		Connection con = null;
		try {
			con=  DriverManager.getConnection(url);
		} catch(SQLException ex) {
            System.err.print("SQLException: ");
            System.err.println(ex.getMessage());
        }   		
		con.setAutoCommit(false);
		return con;		
	}

}
