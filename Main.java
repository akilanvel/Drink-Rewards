package DrinkRewards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws FileNotFoundException {

		// Check if preferred.dat exists in the directory
		boolean preferredExists = false;
		boolean preferredCreated = false;
		File folder = new File(System.getProperty("user.dir"));
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			File file = listOfFiles[i];
			if (file.isFile() && file.getName().equals("preferred.dat")) {
				preferredExists = true;
			}
		}

		// Create and initialize preferred array if there is a preferred file
		Customer[] preferred = null;
		if (preferredExists) {
			File file = new File("preferred.dat");
			Scanner s = new Scanner(file);
			Scanner s1 = new Scanner(file);
			// initialize preferred customer array by calling a method to read it using
			// scanners
			preferred = readPreferred(s, s1);
		}

		// Check if customer.dat exists by looping through the directory
		boolean customerExists = false;
		File folder1 = new File(System.getProperty("user.dir"));
		File[] listOfFiles1 = folder1.listFiles();
		for (int i = 0; i < listOfFiles1.length; i++) {
			File file = listOfFiles1[i];
			if (file.isFile() && file.getName().equals("customer.dat")) {
				customerExists = true;
			}
		}

		// Check if orders.dat exists by looping through the directory
		boolean ordersExists = false;
		File folder2 = new File(System.getProperty("user.dir"));
		File[] listOfFiles2 = folder2.listFiles();
		for (int i = 0; i < listOfFiles2.length; i++) {
			File file = listOfFiles2[i];
			if (file.isFile() && file.getName().equals("orders.dat")) {
				ordersExists = true;
			}
		}

		// Execute the program only if there is a customer.dat and if there is an
		// orders.dat
		if (!customerExists) {
			System.out.println("The customer.dat file does not exist. The program cannot execute.");
		} else if (!ordersExists) {
			System.out.println("The orders.dat file does not exist. The program cannot execute.");
		} else {
			// Create a scanner to read the customer.dat file
			File file = new File("customer.dat");
			Scanner scanner = new Scanner(file);

			// Initialize regular customer array
			Customer[] customers = readCustomers(scanner, scanner);

			// Loop through orders using a scanner
			File orderFile = new File("orders.dat");
			Scanner oScanner = new Scanner(orderFile);
			while (oScanner.hasNextLine()) { // Check if scanner has an order
				// Read in the entire order and split it up
				String input = oScanner.nextLine();
				String[] inputs = input.split(" ");

				// Determine if the order is valid (by calling the skip() method)
				boolean skip = skip(inputs, customers, preferred);

				if (!skip) { // If the order is valid
					char regularOrPreferred = 'l'; // holds whether the order is for a regular customer, gold customer,
													// or
													// platinum customer
					// Split up each part of the order and put it in its corresponding variable
					String ID = inputs[0];
					char drinkSize = inputs[1].charAt(0);
					String drinkType = inputs[2];
					float squareInchPrice = Float.parseFloat(inputs[3]);
					int quantity = Integer.parseInt(inputs[4]);
					int index = findCustomer(customers, ID); // the index of the customer we are handling either in the
																// preferred array or customers array
					// if we found the customer ID in the customers array, denote the customer to be
					// 'r' or regular
					if (index != -1) {
						regularOrPreferred = 'r';
					}
					// if we don't find the customer ID in the customers array, check if it's in the
					// preferred array
					if (index == -1) {
						index = findCustomer(preferred, ID);
					}
					// if we find the customer ID in the preferred array, denote the customer to be
					// either 'g' (gold) or 'p' platinum
					if (index != -1 && regularOrPreferred != 'r') {
						if (preferred[index] instanceof GoldCustomer) {
							regularOrPreferred = 'g';
						}
						if (preferred[index] instanceof PlatinumCustomer) {
							regularOrPreferred = 'p';
						}
					}
					float price = 0f; // the price of the order

					// Adjust the price of the order depending on the type of drink and its size
					if (drinkType.equals("tea")) {
						if (drinkSize == 'S') {
							price += (0.12f * 12);
						} else if (drinkSize == 'M') {
							price += (0.12f * 20);
						} else if (drinkSize == 'L') {
							price += (0.12f * 32);
						}
					} else if (drinkType.equals("soda")) {
						if (drinkSize == 'S') {
							price += (0.20f * 12);
						} else if (drinkSize == 'M') {
							price += (0.20f * 20);
						} else if (drinkSize == 'L') {
							price += (0.20f * 32);
						}
					} else if (drinkType.equals("punch")) {
						if (drinkSize == 'S') {
							price += (0.15f * 12);
						} else if (drinkSize == 'M') {
							price += (0.15f * 20);
						} else if (drinkSize == 'L') {
							price += (0.15f * 32);
						}
					}

					// Add to the price of the order by finding the cost of the design on the
					// cylinder
					if (drinkSize == 'S') {
						price += (4f * Math.PI * 4.5f) * squareInchPrice;
					} else if (drinkSize == 'M') {
						price += (4.5f * Math.PI * 5.75f) * squareInchPrice;
					} else if (drinkSize == 'L') {
						price += (5.5f * Math.PI * 7f) * squareInchPrice;
					}

					// Adjust the price for the quantity of drinks ordered
					price *= quantity;

					// Handle cases depending on whether the customer at hand is regular, platinum,
					// or gold
					if (regularOrPreferred == 'r') { // If the customer is regular
						customers[index].setAmountSpent(round(customers[index].getAmountSpent() + price)); // 1
						if (customers[index].getAmountSpent() >= 200) { // if the customer will jump directly to
																		// platinum
							// give the appropriate number of bonus bucks and update the amount they spent
							float temp = (customers[index].getAmountSpent() - 200);
							float bonus = (float) ((int) temp / 5);
							preferred = add(preferred,
									new PlatinumCustomer(customers[index].getFirstName(),
											customers[index].getLastName(), customers[index].getGuestID(),
											customers[index].getAmountSpent(), bonus));
							customers = remove(customers, index);
							preferredCreated = true;
						} else if (customers[index].getAmountSpent() >= 150) { // if the customer reaches the third part
																				// of
																				// gold status
							// apply the appropriate discount to the order and fix the amount they spent
							float discount = 15f;
							customers[index].setAmountSpent(round((customers[index].getAmountSpent() - price)
									+ (price * ((100f - discount) / 100f))));
							preferred = add(preferred,
									new GoldCustomer(customers[index].getFirstName(), customers[index].getLastName(),
											customers[index].getGuestID(), customers[index].getAmountSpent(),
											discount));
							customers = remove(customers, index);
							preferredCreated = true;
						} else if (customers[index].getAmountSpent() >= 100) { // if the customer reaches the second
																				// part of
																				// gold status
							// apply the appropriate discount to the order and fix the amount they spent
							float discount = 10f;
							customers[index].setAmountSpent(round((customers[index].getAmountSpent() - price)
									+ (price * ((100f - discount) / 100f))));
							preferred = add(preferred,
									new GoldCustomer(customers[index].getFirstName(), customers[index].getLastName(),
											customers[index].getGuestID(), customers[index].getAmountSpent(),
											discount));
							customers = remove(customers, index);
							preferredCreated = true;
						} else if (customers[index].getAmountSpent() >= 50) { // if the customer reaches the first part
																				// of
																				// gold status
							// apply the appropriate discount to the order and fix the amount they spent
							float discount = 5f;
							customers[index].setAmountSpent(round((customers[index].getAmountSpent() - price)
									+ (price * ((100f - discount) / 100f))));
							preferred = add(preferred,
									new GoldCustomer(customers[index].getFirstName(), customers[index].getLastName(),
											customers[index].getGuestID(), customers[index].getAmountSpent(),
											discount));
							customers = remove(customers, index);
							preferredCreated = true;
						}
					} else if (regularOrPreferred == 'g') { // If the customer is a gold customer
						// determine the amount they just spent (including discount)
						price *= (100 - ((GoldCustomer) preferred[index]).getDiscountPercentage()) / 100;
						preferred[index].setAmountSpent(round(preferred[index].getAmountSpent() + price)); // 2
						if (preferred[index].getAmountSpent() >= 200) { // if the customer needs to be promoted to
																		// platinum
							// add a platinum customer to the array and remove the gold one
							float temp = (preferred[index].getAmountSpent() - 200);
							float bonus = (float) ((int) temp / 5);
							preferred = promote(preferred,
									new PlatinumCustomer(preferred[index].getFirstName(),
											preferred[index].getLastName(), preferred[index].getGuestID(),
											preferred[index].getAmountSpent(), bonus),
									index);
						} else if (preferred[index].getAmountSpent() >= 150
								&& ((GoldCustomer) preferred[index]).getDiscountPercentage() <= 10f) { // if the
																										// customer
																										// needs to be
																										// promoted to
																										// 15%
																										// discount
																										// level
							// adjust the price, amount spent, and discount
							float newPrice = price
									/ ((100 - ((GoldCustomer) preferred[index]).getDiscountPercentage()) / 100);
							newPrice *= 0.85f;
							((GoldCustomer) preferred[index]).setDiscountPercentage(15f);
							preferred[index]
									.setAmountSpent(round(preferred[index].getAmountSpent() - price) + (newPrice)); // 3
						} else if (preferred[index].getAmountSpent() >= 100
								&& ((GoldCustomer) preferred[index]).getDiscountPercentage() <= 5f) { // if the customer
																										// needs to be
																										// promoted to
																										// 10%
																										// discount
																										// level
							// adjust price, amount spent, and discount appropriately
							float newPrice = price
									/ ((100 - ((GoldCustomer) preferred[index]).getDiscountPercentage()) / 100);
							newPrice *= 0.90f;
							((GoldCustomer) preferred[index]).setDiscountPercentage(10f);
							preferred[index]
									.setAmountSpent(round(preferred[index].getAmountSpent() - price) + (newPrice)); // 4
						}
					} else if (regularOrPreferred == 'p') { // If the customer we are handling is a platinum customer
						// if the bonus bucks cover the entire cost, set price to zero and reduce from
						// bonus bucks. if not, remove applicable bonus bucks from total cost
						if (price <= ((PlatinumCustomer) preferred[index]).getBonusBucks()) {
							((PlatinumCustomer) preferred[index])
									.setBonusBucks(((PlatinumCustomer) preferred[index]).getBonusBucks() - (int) price);
							price -= (int) price;
						} else if (price > ((PlatinumCustomer) preferred[index]).getBonusBucks()) {
							price -= ((PlatinumCustomer) preferred[index]).getBonusBucks();
							((PlatinumCustomer) preferred[index]).setBonusBucks(0f);
						}
						// add price (with bonus bucks used) to the amount spent
						preferred[index].setAmountSpent(round(preferred[index].getAmountSpent() + price)); // 5
						// after the transaction, if they have crossed $5 threshold(s), add bonus
						// buck(s)
						float bonus = 0f;
						for (double base = preferred[index].getAmountSpent() - price
								+ 1; base < (int) preferred[index].getAmountSpent() + 1; base++) {
							if ((int) base % 5 == 0) {
								bonus++;
							}
						}
						// add bonus bucks to customer
						((PlatinumCustomer) preferred[index])
								.setBonusBucks(((PlatinumCustomer) preferred[index]).getBonusBucks() + bonus);
					}
				}
			}

			// Close the scanner objects
			scanner.close();
			oScanner.close();

			// Write updated customer list and preferred customer list to customer.dat and
			// preferred.dat
			PrintWriter pwCustomer = new PrintWriter("customer.dat");
			for (int i = 0; i < customers.length; i++) {
				// print to the file in the appropriate format so that it can be input next time
				pwCustomer.print(customers[i].getGuestID() + " " + customers[i].getFirstName() + " "
						+ customers[i].getLastName() + " ");
				int indexOfDecimal = -1;
				String f1 = Float.toString(customers[i].getAmountSpent());
				for (int ix = 0; ix < f1.length(); ix++) {
					if (f1.substring(ix, ix+1).equals(".")) {
						indexOfDecimal = ix;
					}
				}
				if (f1.length() - indexOfDecimal < 3) {
					pwCustomer.println(customers[i].getAmountSpent() + "0");
				} else {
					pwCustomer.println(customers[i].getAmountSpent());
				}
			}
			if (preferredExists || preferredCreated) {
				PrintWriter pwPreferred = new PrintWriter("preferred.dat");
				for (int i = 0; i < preferred.length; i++) {
					// print to the file in the appropriate format so that it can be input next time
					pwPreferred.print(preferred[i].getGuestID() + " " + preferred[i].getFirstName() + " "
							+ preferred[i].getLastName() + " ");
					int indexOfDecimal = -1;
					String f1 = Float.toString(preferred[i].getAmountSpent());
					for (int ix = 0; ix < f1.length(); ix++) {
						if (f1.substring(ix, ix+1).equals(".")) {
							indexOfDecimal = ix;
						}
					}
					if (f1.length() - indexOfDecimal < 3) {
						pwPreferred.print(preferred[i].getAmountSpent() + "0");
					} else {
						pwPreferred.print(preferred[i].getAmountSpent());
					}
					if (preferred[i] instanceof GoldCustomer) {
						pwPreferred.println(" " + (int) ((GoldCustomer) preferred[i]).getDiscountPercentage() + "%");
					} else if (preferred[i] instanceof PlatinumCustomer) {
						pwPreferred.println(" " + (int) ((PlatinumCustomer) preferred[i]).getBonusBucks());
					}
				}
				// Close PrintWriter object
				pwPreferred.close();
			}
			// Close PrintWriter object
			pwCustomer.close();
		}
	}

	// This method returns a Customer array containing the customers in the
	// customer.dat file
	public static Customer[] readCustomers(Scanner scanner, Scanner scanner1) throws FileNotFoundException {
		// Create a file object for customer.dat
		File file = new File("customer.dat");

		// Initialize the scanner passed in as a parameter to read customer.dat
		scanner = new Scanner(file);

		// Determine the size of array by determining number of customers
		int numCustomers = 0;
		while (scanner.hasNextLine()) {
			String temporary = scanner.nextLine();
			String[] inputs = temporary.split(" ");
			if (inputs.length == 4) {
				numCustomers++;
			}
		}

		// Declare and initialize an array to the size that was determined above
		Customer[] customers = new Customer[numCustomers];

		// Initialize another scanner to customer.dat
		scanner1 = new Scanner(file);

		// Use the above scanner to fill up with customers array with customers from
		// customer.dat
		for (int i = 0; i < customers.length && scanner1.hasNextLine(); i++) {
			String temporary = scanner1.nextLine();
			String[] inputs = temporary.split(" ");
			customers[i] = new Customer(inputs[1], inputs[2], inputs[0], Float.parseFloat(inputs[3]));
		}

		// Close both scanners
		scanner1.close();
		scanner.close();

		return customers;
	}

	// This method returns a Customer array containing the Gold customer and
	// Platinum customers in the preferred.dat file
	public static Customer[] readPreferred(Scanner scanner, Scanner scanner1) throws FileNotFoundException {
		// Create a file object to hold preferred.dat
		File file = new File("preferred.dat");

		// Initialize a scanner parameter to preferred.dat
		scanner = new Scanner(file);

		// Determine the number of preferred customers
		int numCustomers = 0;
		while (scanner.hasNextLine()) {
			String temporary = scanner.nextLine();
			String[] inputs = temporary.split(" ");
			if (inputs.length == 5) {
				numCustomers++;
			}
		}

		// Initialize an array to hold preferred customers with the size determined
		// above
		Customer[] preferred = new Customer[numCustomers];

		// Initialize another scanner parameter to preferred.dat
		scanner1 = new Scanner(file);

		// Loop through preferred.dat and fill up the preferred array with customers
		for (int i = 0; i < preferred.length && scanner1.hasNextLine(); i++) {
			String temporary = scanner1.nextLine();
			String[] inputs = temporary.split(" ");
			boolean gold = false;
			// If the preferred customer is gold, store him/her as a gold customer.
			// otherwise, store him/her as a platinum customer
			if (inputs[4].substring(inputs[4].length() - 1, inputs[4].length()).equals("%")) {
				gold = true;
			}
			if (gold) {
				preferred[i] = new GoldCustomer(inputs[1], inputs[2], inputs[0], Float.parseFloat(inputs[3]),
						Float.parseFloat(inputs[4].substring(0, inputs[4].length() - 1)));
			} else {
				preferred[i] = new PlatinumCustomer(inputs[1], inputs[2], inputs[0], Float.parseFloat(inputs[3]),
						Float.parseFloat(inputs[4]));
			}
		}

		// Close the scanners
		scanner.close();
		scanner1.close();

		return preferred;
	}

	// This method determines whether an order in the order.dat file is valid
	public static boolean skip(String[] inputs, Customer[] customers, Customer[] preferred) {

		// The order is invalid if it doesn't have 5 components
		if (inputs.length != 5) {
			return true;
		}
		// The order is invalid if the customer ID doesn't exist
		boolean customerIdExists = false;
		for (int i = 0; i < customers.length; i++) {
			if (inputs[0].equals(customers[i].getGuestID())) {
				customerIdExists = true;
			}
		}
		if (preferred != null) {
			for (int i = 0; i < preferred.length; i++) {
				if (inputs[0].equals(preferred[i].getGuestID())) {
					customerIdExists = true;
				}
			}
		}
		if (!customerIdExists) {
			return true;
		}
		// The order is invalid if the drink size is not S, M, or L
		if (!inputs[1].equals("S") && !inputs[1].equals("M") && !inputs[1].equals("L")) {
			return true;
		}
		// The order is invalid if the drink type is not soda, tea, or punch
		if (!inputs[2].equals("soda") && !inputs[2].equals("tea") && !inputs[2].equals("punch")) {
			return true;
		}
		// The order is invalid if the square inch price is not a float and if the
		// quantity is not an integer
		if (!isFloat(inputs[3]) || !isInt(inputs[4])) {
			return true;
		}

		return false;
	}

	// This method determines if the parameter is a float
	public static boolean isFloat(String s) {
		// If converting the string to a float doens't throw an exception, it is a float
		try {
			float f = Float.parseFloat(s);
			if (f == 1) {

			}
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}

		return true;
	}

	// This method determines if the parameter is an integer
	public static boolean isInt(String s) {
		// If converting the string to an int doesn't through an exception, then it is
		// an int
		try {
			int i = Integer.parseInt(s);
			if (i == 1) {

			}
		} catch (NumberFormatException | NullPointerException e) {
			return false;
		}

		return true;
	}

	// This method returns the index of the customer in the customer array given
	// his/her ID
	public static int findCustomer(Customer[] customer, String ID) {
		// Loop through the customer array until a customer with matching ID is found
		int index = -1;
		for (int i = 0; i < customer.length; i++) {
			if (ID.equals(customer[i].getGuestID())) {
				// If a customer with matching ID is found, set the index equals to where the
				// matching customer is located in the customer array
				index = i;
			}
		}
		return index;
	}

	// This method adds a customer to the end of a customer array
	public static Customer[] add(Customer[] p, Customer c) {
		// Treat the array differently if it's null and if it is not
		if (p != null) {
			// Create a new array holding the customer array plus 1 spot
			Customer[] preferred = new Customer[p.length + 1];
			// Copy the old array to the new array
			for (int x = 0; x < p.length; x++) {
				preferred[x] = p[x];
			}

			// At the very end, assign the extra index to the customer we want added
			preferred[p.length] = c;

			return preferred;
		} else {
			Customer[] preferred = new Customer[1];

			// add the first preferred customer into the preferred array
			preferred[0] = c;

			return preferred;
		}
	}

	// This method removes a customer from the customer array
	public static Customer[] remove(Customer[] c, int i) {
		// Create a new array with one less spot than the old one
		Customer[] customers = new Customer[c.length - 1];

		// Loop through the old array and copy every element
		for (int x = 0, y = 0; x < c.length; x++) {
			// Except the one with the index that we want removed
			if (x != i) {
				customers[y] = c[x];
				y++;
			} else {

			}
		}

		return customers;
	}

	// This method replaces a customer with another customer in the customer array
	// while keeping all other elements in the same position
	public static Customer[] promote(Customer[] c, Customer p, int index) {
		// create a new array with the same length
		Customer[] preferred = new Customer[c.length];

		// Loop through the old array and copy every element
		for (int i = 0; i < c.length; i++) {
			// But put the new Customer at the index that was passed in as parameter
			if (i == index) {
				preferred[i] = p;
			} else {
				preferred[i] = c[i];
			}
		}
		return preferred;
	}

	// This method rounds a float to the nearest hundredth (2 decimal places)
	public static float round(float f) {
		f = Math.round(f * 100.0f) / 100.0f;
		
		return f;
	}
}