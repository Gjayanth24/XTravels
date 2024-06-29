package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import com.dao.OrderDAO;
import com.dao.RouteDAO;
import com.dao.UserDAO;
import com.model.User;
import com.service.JourneyService;
import com.service.UserService;

public class TravelApp {

	private static final String LOGO_FILE_PATH = "company_logo.txt";

	public static void main(String[] args) throws SQLException {
		displayCompanyLogo();
		runTravelApp();
	}

	private static void displayCompanyLogo() {
		try (BufferedReader reader = new BufferedReader(new FileReader(LOGO_FILE_PATH))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			System.err.println("Error reading company logo file: " + e.getMessage());
		}
	}

	private static void runTravelApp() throws SQLException {
		Scanner scanner = new Scanner(System.in);
		UserDAO userDAO = new UserDAO();
		UserService userService = new UserService(userDAO);
		RouteDAO routeDAO = new RouteDAO();
		OrderDAO orderDAO = new OrderDAO();
		JourneyService journeyService = new JourneyService(routeDAO, orderDAO);

		boolean running = true;

		while (running) {
			System.out.println("\nWelcome to the Travel App");
			System.out.println("1. Register");
			System.out.println("2. Login");
			System.out.println("3. Plan Journey");
			System.out.println("4. Reschedule Journey");
			System.out.println("5. Forgot Password");
			System.out.println("6. Exit");
			System.out.print("Select an option: ");

			try {
				int choice = scanner.nextInt();
				scanner.nextLine(); // Consume newline character after nextInt()

				switch (choice) {
				case 1:
					userService.registerNewAdmin();
					break;
				case 2:
					User loggedInUser = userService.login();
					if (loggedInUser != null) {
						// Optionally handle logged-in user actions here
						System.out.println(
								"Logged in as: " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
					}
					break;
				case 3:
					journeyService.planJourney();
					break;
				case 4:
					journeyService.reScheduleJourney();
					break;
				case 5:
					userService.forgotPassword();
					break;
				case 6:
					System.out.println("Exiting the application...");
					running = false; // Set running to false to exit the loop
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
				}
			} catch (InputMismatchException e) {
				System.out.println("Invalid input. Please enter a number.");
				scanner.nextLine(); // Consume invalid input to prevent infinite loop
			} catch (NoSuchElementException e) {
				System.out.println("No input found. Exiting...");
				break;
			}
		}

		scanner.close(); // Close the scanner when done
	}

}
