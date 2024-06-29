package com.service;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.dao.OrderDAO;
import com.dao.RouteDAO;
import com.model.Journey;
import com.model.Order;
import com.model.Route;

public class JourneyService {
	private RouteDAO routeDAO;
	private OrderDAO orderDAO;

	public JourneyService(RouteDAO routeDAO, OrderDAO orderDAO) {
		this.routeDAO = routeDAO;
		this.orderDAO = orderDAO;
	}

	public JourneyService() {
		// TODO Auto-generated constructor stub
	}

//	public void setRoutes(List<Route> routes) {
//		this.routes = routes;
//	}

	public void planJourney() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nPlan Journey");

		System.out.print("Enter source: ");
		String source = scanner.nextLine();

		System.out.print("Enter destination: ");
		String destination = scanner.nextLine();

		System.out.print("Enter journey date (YYYY-MM-DD): ");
		String journeyDateString = scanner.nextLine();
		LocalDate journeyDate = LocalDate.parse(journeyDateString, DateTimeFormatter.ISO_LOCAL_DATE);

		System.out.print("Enter number of passengers: ");
		int noOfPassengers = scanner.nextInt();
		scanner.nextLine(); // Consume the leftover newline
		scanner.close();
		try {
			List<Route> matchingRoutes = getRoutes(source, destination, journeyDate, noOfPassengers);
			if (!matchingRoutes.isEmpty()) {
				System.out.println("Available Routes: ");
				for (int i = 0; i < matchingRoutes.size(); i++) {
					System.out.println((i + 1) + ": " + matchingRoutes.get(i));
				}
				System.out.print("Select a route (number): ");
				int routeNumber = scanner.nextInt();
				Route selectedRoute = matchingRoutes.get(routeNumber - 1);

				Order newOrder = createOrder(journeyDate, noOfPassengers, selectedRoute);
				orderDAO.saveOrder(newOrder);
				System.out.println("Journey planned successfully. Order details: " + newOrder);
			} else {
				System.out.println("No available routes found for the given details.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void reScheduleJourney() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nRe-Schedule Journey");

		System.out.print("Enter your Order ID: ");
		int orderId = scanner.nextInt();
		scanner.nextLine(); // Consume the leftover newline
		scanner.close();
		try {
			Order orderToReschedule = orderDAO.getOrderById(orderId);
			if (orderToReschedule != null) {
				System.out.print("Enter new journey date (YYYY-MM-DD): ");
				String newDateStr = scanner.nextLine();
				LocalDate newDate = LocalDate.parse(newDateStr, DateTimeFormatter.ISO_LOCAL_DATE);

				List<Route> availableRoutes = getRoutes(orderToReschedule.getRoute().getSource(),
						orderToReschedule.getRoute().getDestination(), newDate,
						orderToReschedule.getRequestedJourneyPlan().getNumberOfPassengers());

				if (!availableRoutes.isEmpty()) {
					orderToReschedule.getRequestedJourneyPlan().setJourneyDate(newDate);
					orderDAO.updateOrder(orderToReschedule);
					System.out.println("Journey rescheduled successfully. Updated order details: " + orderToReschedule);
				} else {
					System.out.println("No available routes for the new journey date.");
				}
			} else {
				System.out.println("Order not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private List<Route> getRoutes(String source, String destination, LocalDate date, int numberOfPassengers)
			throws SQLException {
		List<Route> allRoutes = routeDAO.getAllRoutes();
		List<Route> matchingRoutes = new ArrayList<>();
		for (Route route : allRoutes) {
			if (route.getSource().equals(source) && route.getDestination().equals(destination)
					&& route.getJourneyDate().isEqual(date) && route.getNoOfSeatsAvailable() >= numberOfPassengers) {
				matchingRoutes.add(route);
			}
		}
		return matchingRoutes;
	}

	private Order createOrder(LocalDate date, int passengers, Route selectedRoute) throws SQLException {
		Order newOrder = new Order();
		double bookingCost = selectedRoute.getTicketPricePerPassenger() * passengers;

		if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
			bookingCost += 200; // Weekend surge
			bookingCost += bookingCost * 0.1; // Adding 10% GST
		}

		newOrder.setOrderAmount(bookingCost);
		newOrder.setRoute(selectedRoute);
		newOrder.setRequestedJourneyPlan(new Journey(date, passengers));
		newOrder.setOrderStatus("created");
		newOrder.setOrderId(orderDAO.getAllOrders().size() + 1); // Generate a unique order ID

		return newOrder;
	}
}
