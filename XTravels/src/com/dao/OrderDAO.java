package com.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.model.Order;
import com.util.DataBaseConnection;

public class OrderDAO {
	private Connection connection;

	public OrderDAO() throws SQLException {
		this.connection = DataBaseConnection.getConnection();
	}

	public void saveOrder(Order order) throws SQLException {
		String query = "INSERT INTO orders (order_id, order_amount, order_status, journey_date, passengers, route_id) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, order.getOrderId());
			stmt.setDouble(2, order.getOrderAmount());
			stmt.setString(3, order.getOrderStatus());
			stmt.setDate(4, Date.valueOf(order.getRequestedJourneyPlan().getJourneyDate()));
			stmt.setInt(5, order.getRequestedJourneyPlan().getNumberOfPassengers());
			stmt.setInt(6, order.getRoute().getRouteId()); // Assuming routeId is set properly

			stmt.executeUpdate();
		}
	}

	public Order getOrderById(int orderId) throws SQLException {
		String query = "SELECT * FROM orders WHERE order_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setInt(1, orderId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				Order order = new Order();
				order.setOrderId(rs.getInt("order_id"));
				order.setOrderAmount(rs.getDouble("order_amount"));
				order.setOrderStatus(rs.getString("order_status"));

				// Assuming the Journey and Route objects are properly set in the Order
				// This might depend on how you fetch and set Route in the Order object

				return order;
			}
		}
		return null;
	}

	public void updateOrder(Order order) throws SQLException {
		String query = "UPDATE orders SET order_amount = ?, order_status = ?, journey_date = ?, passengers = ?, route_id = ? WHERE order_id = ?";
		try (PreparedStatement stmt = connection.prepareStatement(query)) {
			stmt.setDouble(1, order.getOrderAmount());
			stmt.setString(2, order.getOrderStatus());
			stmt.setDate(3, Date.valueOf(order.getRequestedJourneyPlan().getJourneyDate()));
			stmt.setInt(4, order.getRequestedJourneyPlan().getNumberOfPassengers());
			stmt.setInt(5, order.getRoute().getRouteId()); // Assuming routeId is set properly
			stmt.setInt(6, order.getOrderId());

			stmt.executeUpdate();
		}
	}

	public List<Order> getAllOrders() throws SQLException {
		List<Order> orders = new ArrayList<>();
		String query = "SELECT * FROM orders";
		try (Statement stmt = connection.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Order order = new Order();
				order.setOrderId(rs.getInt("order_id"));
				order.setOrderAmount(rs.getDouble("order_amount"));
				order.setOrderStatus(rs.getString("order_status"));

				// Assuming the Journey and Route objects are properly set in the Order
				// This might depend on how you fetch and set Route in the Order object

				orders.add(order);
			}
		}
		return orders;
	}
}
