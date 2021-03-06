package com.bridgelabz.fellowship.operation;

import java.util.Date;
import java.util.List;

import com.bridgelabz.fellowship.control.Stockcontrol;
import com.bridgelabz.fellowship.model.Stockuser;
import com.bridgelabz.fellowship.model.Stocks;
import com.bridgelabz.fellowship.model.Transactionlog;

public class Dataprocessing {

	public static void usermenu() {
		int key;
		do {
			System.out.println("**********************Menu*************************");
			System.out.println("1-Add user\n" + "2-remove user\n" + "3-Transaction\n" + "4-check share stock mareket");
			System.out.print("select choice:-> ");
			key = Inputvalidation.isValidInteger();
			switch (key) {
			case 1:
				addUsers(); // Add new user entry
				break;
			case 2:
				displayAllUsers(); // show users
				removeUser(); // Remove User
				break;
			case 3:
				transactionsMenu(); // Users transaction (buy and sales).
				break;
			case 4:
				Stockoperations.displayStock(); // show all stock report
				break;
			default:
				break;
			}
		} while (key != 5); // exit loop when input key is 5.
	}

	public static void displayAllUsers() {
		// Retrieve all users list from file.
		List<Stockuser> list = Stockcontrol.readUsers();
		System.out.println("***********************************");
		for (Stockuser stockUser : list) {
			System.out.println(stockUser.toString());
			System.out.println("***********************************");
		}
	}

	// users transactions buy and sales shares.
	public static void transactionsMenu() {
		int key;
		do {
			System.out.println();
			System.out.println("**************Transaction**************");
			System.out.println("1-Buy Shares\n" + "2-Sale Shares\n" + "3-user Log\n" + "4-Stock Menu");
			System.out.print("enter choice: ");
			key = Inputvalidation.isValidInteger();
			// select choice for operation
			switch (key) {
			case 1:
				buy(); // get buys shares
				break;
			case 2:
				sell(); // sale shares
				break;
			case 3:
				printReport(); // print log report of transaction.
				break;
			case 4:
				Stockoperations.displayStock(); // display all stock report.
				break;
			default:
				System.out.println("invaalid choice"); // input is invalid or out of range
				break;
			}
		} while (key != 5); // exit if input is 5.
	}

	// add new users entry
	private static void addUsers() {
		// Retrieve all users list from file.
		List<Stockuser> newuser = Stockcontrol.readUsers();

		Stockuser newentry = new Stockuser(); // new class initialized

		System.out.print("enter user name: "); // take new user name
		newentry.setUsername(Inputvalidation.isString());
		System.out.println();
		newentry.setShare(0); // default new user having 0 shares.

		newuser.add(newentry); // add new user in list.

		Stockcontrol.writeUsers(newuser); // write new added file.
	}

	// remove existing users.
	private static void removeUser() {
		// read all users.
		List<Stockuser> user = Stockcontrol.readUsers();

		System.out.print("\nenter user name: \n"); // user name for search
		String inputusername = Inputvalidation.isString();

		boolean find = false; // for check user in list or not
		for (Stockuser stockUser : user) {
			if (stockUser.getUsername().equalsIgnoreCase(inputusername)) {
				find = true;
				if (stockUser.getShare() == 0) {
					user.remove(stockUser); // user found with having 0 shares then delete that users
					System.out.println("removed successfully....");
				} else {
					System.out.println("sale all shares then remove");
				}
			}
		}
		if (!find) { // check user is not in list
			System.out.println("not in the list\n");
		}

		Stockcontrol.writeUsers(user); // write new updated list.
	}

	// buy the shares from the stock.
	private static void buy() {
		Stockoperations.displayStock(); // show all stock info.
		System.out.println("user name"); // input for search
		String username = Inputvalidation.isString();
		List<Stockuser> user = Stockcontrol.readUsers(); // get all users from file
		for (Stockuser stockUser : user) { // search all user from list.

			if (stockUser.getUsername().equalsIgnoreCase(username)) { // if user found
				System.out.println("enter company symbol"); // company symbols
				String inputsymbol = Inputvalidation.scanner.next();
				Dataprocessing.displayAllUsers(); // show all users having shares.
				String spath = Stockoperations.spath;
				List<Stocks> list = Stockcontrol.readStock(spath); // read all stock info
				for (Stocks stockmodel : list) {
					// search company name using symbol.
					if (stockmodel.companysymbol.equals(inputsymbol)) { // check company using user input symbol
						System.out.println("welcome to " + stockmodel.companyname); // company name
						System.out.println("availabe Share is: " + stockmodel.companyavailableshare); // available
																										// shares
						System.out.println("price per share is: " + stockmodel.shareprice); // share price per share
						System.out.println("no. of share you want?");// user input for buys number of shares.
						int getshare = Inputvalidation.isValidInteger();
						// display no. of shares want * actual share price
						System.out.println("your buy" + getshare + " share at " + getshare * stockmodel.shareprice);

						stockmodel.setCompanyavailableshare(stockmodel.companyavailableshare - getshare);// update
																											// stock(remains-buy
																											// shares.
						System.out.println();
						stockUser.setShare(getshare + stockUser.getShare()); // add shares in user file
						transaction(stockUser, stockmodel, "buy", getshare); // write log entry of transaction
					}
				}
				// update existing users shares and transaction
				Stockcontrol.writeUsers(user);
				Stockcontrol.writeStock(list);
			}
		}
	}

	/**
	 * @param stockUser  user
	 * @param stockmodel user selected stock
	 * @param status     buy/sell share
	 * @param amount     buy/sell amount of shares
	 */
	// Transaction having sale or buys shares option
	public static void transaction(Stockuser stockUser, Stocks stockmodel, String status, int amount) {
		// DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		Date date = new Date(); // current date
		List<Transactionlog> log = Stockcontrol.readLog(); // read transaction log entry
		// set transaction log entry
		Transactionlog transLog = new Transactionlog();
		transLog.setUsername(stockUser.getUsername()); // set user name
		transLog.setCompanysymbol(stockmodel.getCompanysymbol()); // company symbol
		transLog.setCompanyname(stockmodel.getCompanyname()); // company name
		transLog.setCompanyshares(amount); // shares amount
		transLog.setShareprice(stockUser.getShare() * stockmodel.getShareprice()); // shares price per share
		transLog.setDate(date); // set current date
		switch (status) {
		case "sale":
			transLog.setTransction("sale"); // sale shares
			break;
		case "buy":
			transLog.setTransction("buy"); // buy shares.
			break;
		default:
			break;
		}
		// add log in list and write list
		log.add(transLog);
		Stockcontrol.writeLog(log);
	}

	// display transaction log of user
	public static void printReport() {
		System.out.println("\n*******************User Log**********************");
		System.out.println("1-all transaction of users");
		boolean find = false;
		System.out.print("enter the name : "); // transaction of users in list
		String username = Inputvalidation.isString();
		System.out.println();
		List<Transactionlog> log = Stockcontrol.readLog(); // read all transaction from file
		for (Transactionlog transactionLog : log) {
			if (transactionLog.getUsername().equalsIgnoreCase(username)) {
				// display report if find user
				find = true; // transaction found and show
				System.out.println(transactionLog.getUsername() + " " + transactionLog.getCompanyname() + " "
						+ transactionLog.getDate().toString() + " " + transactionLog.getTransction());
			}
		}
		if (!find) { // transaction not found
			System.out.println("no log found....");
		}
	}

	// sales the existing share to company.
	private static void sell() {
		Stockoperations.displayStock(); // all stock display
		System.out.println("username");
		String username = Inputvalidation.isString();
		List<Stockuser> user = Stockcontrol.readUsers();
		for (Stockuser stockUser : user) { // if user found then
			if (stockUser.getUsername().equals(username)) {
				System.out.println("enter company symbol"); // enter company symbol
				String inputsymbol = Inputvalidation.scanner.next();
				String spath = Stockoperations.spath;
				List<Stocks> list = Stockcontrol.readStock(spath); // get all stock info
				for (Stocks stockmodel : list) {
					if (stockmodel.companysymbol.equals(inputsymbol)) { // check stock info and input-symbol
						System.out.println("enter share amount");
						int shares = Inputvalidation.isValidInteger();
						System.out.println("your shares is : " + shares + "\nper share price: " + stockmodel.shareprice
								+ "\ntotal price is: " + shares * stockmodel.shareprice); // show shares and available
																							// price
						System.out.println("sale successfully.....");
						stockmodel.setCompanyavailableshare(stockmodel.companyavailableshare + shares);

						stockUser.setShare(stockUser.getShare() - shares); // substract and update user data

						transaction(stockUser, stockmodel, "sale", shares);// set log entry
					}
				}
				// write new user data and stock list
				Stockcontrol.writeUsers(user);
				Stockcontrol.writeStock(list);
			}
		}
	}
}
