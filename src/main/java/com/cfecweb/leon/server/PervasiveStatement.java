package com.cfecweb.leon.server;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PervasiveStatement {
	
	public static Statement getit(Connection con) {
		Statement stmt = null;
		try {
			/*
			 * Sensitive RS's will reflect inserts, updates and deletes immediately in the scroll, but
			 * you do not get RS methods getRow, isLast or isFirst.
			 */
			stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}

}
