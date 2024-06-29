package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.model.Route;
import com.util.DataBaseConnection;

public class RouteDAO {

	public List<Route> getAllRoutes() throws SQLException {
		List<Route> routes = new ArrayList<>();
		String query = "SELECT * FROM routes";

		try (Connection conn = DataBaseConnection.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {

			while (rs.next()) {
				Route route = new Route(rs.getInt("route_id"), rs.getString("source"), rs.getString("destination"),
						rs.getDate("journey_date").toLocalDate(), rs.getDouble("ticket_price_per_passenger"),
						rs.getInt("no_of_seats_available"));
				routes.add(route);
			}
		}

		return routes;
	}

	public void updateRouteSeats(int routeId, int newSeats) throws SQLException {
		String query = "UPDATE routes SET no_of_seats_available = ? WHERE route_id = ?";

		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setInt(1, newSeats);
			pstmt.setInt(2, routeId);
			pstmt.executeUpdate();
		}
	}
}
