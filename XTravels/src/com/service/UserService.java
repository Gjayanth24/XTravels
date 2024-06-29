package com.service;

import java.sql.SQLException;
import java.util.Scanner;

import com.dao.UserDAO;
import com.model.User;

public class UserService {
	private UserDAO userDAO;

	public UserService(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public UserService() {
		// TODO Auto-generated constructor stub
	}

	public void registerNewAdmin() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nNew Admin User Registration");

		System.out.print("Enter first name: ");
		String firstName = scanner.nextLine();

		System.out.print("Enter last name: ");
		String lastName = scanner.nextLine();

		System.out.print("Enter mobile number: ");
		String mobileNumber = scanner.nextLine();

		System.out.print("Enter gender: ");
		String gender = scanner.nextLine();

		System.out.print("Enter email: ");
		String email = scanner.nextLine();

		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		scanner.close();
		try {
			if (userDAO.getUserByEmail(email) != null) {
				System.out.println("User with this email: " + email + " already exists");
				return;
			}

			User newUser = new User(firstName, lastName, mobileNumber, gender, email, password, 0, "Active");
			userDAO.saveUser(newUser);
			System.out.println("Registration successful!");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public User login() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nUser Login");

		System.out.print("Enter email: ");
		String email = scanner.nextLine();

		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		scanner.close();
		try {
			User user = userDAO.getUserByEmail(email);
			if (user != null) {
				if (user.getPassword().equals(password)) {
					System.out.println("Login successful!");
					return user;
				} else {
					System.out.println("Invalid password. Please try again.");
				}
			} else {
				System.out.println("User not found. Please register.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void forgotPassword() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nForgot Password");

		System.out.print("Enter your email: ");
		String email = scanner.nextLine();
		scanner.close();
		try {
			User user = userDAO.getUserByEmail(email);
			if (user != null) {
				System.out.println("Password recovery successful. Your password is: " + user.getPassword());
			} else {
				System.out.println("User not found.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
