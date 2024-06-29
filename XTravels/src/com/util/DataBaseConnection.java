package com.util;

//This class is for DB Connections

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/xtravels";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
	}

}
