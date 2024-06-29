package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.model.User;
import com.util.DataBaseConnection;

public class UserDAO {

	public void saveUser(User user) throws SQLException {
		String query = "INSERT INTO users (first_name, last_name, mobile_number, gender, email, password, failed_count, account_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, user.getFirstName());
			pstmt.setString(2, user.getLastName());
			pstmt.setString(3, user.getMobileNumber());
			pstmt.setString(4, user.getGender());
			pstmt.setString(5, user.getEmail());
			pstmt.setString(6, user.getPassword());
			pstmt.setInt(7, user.getFailedCount());
			pstmt.setString(8, user.getAccountStatus());
			pstmt.executeUpdate();
		}
	}

	public User getUserByEmail(String email) throws SQLException {
		String query = "SELECT * FROM users WHERE email = ?";
		User user = null;

		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				user = new User(rs.getString("first_name"), rs.getString("last_name"), rs.getString("mobile_number"),
						rs.getString("gender"), rs.getString("email"), rs.getString("password"),
						rs.getInt("failed_count"), rs.getString("account_status"));
			}
		}

		return user;
	}
}
