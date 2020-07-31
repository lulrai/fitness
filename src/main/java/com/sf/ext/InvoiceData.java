package com.sf.ext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import entities.DatabaseInfo;
/*
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 * 16 methods in total, add more if required.
 * Do not change any method signatures or the package name.
 * 
 */

public class InvoiceData {

	/**
	 * 1. Method that removes every person record from the database
	 */
	public static void removeAllPersons() {
		/** TODO*/
		Connection c = null;			//Created Connection and Statement object, initialized it
		Statement s = null;
		try {
			c = DatabaseInfo.getConnection();		//Created and set the connection
			s = c.createStatement();				//Created a statement
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");	//Disabled all the checks
			s.executeUpdate("SET UNIQUE_CHECKS=0;");

			s.executeUpdate("TRUNCATE Email;");				//Cleared Email table
			s.executeUpdate("UPDATE Member SET PersonID = 0;");	//Updated all Members with person as 0
			s.executeUpdate("UPDATE Invoice SET PersonID = 0;");	//Updated all Invoice with person as 0

			s.executeUpdate("TRUNCATE Person;");	//Cleared Person table
			
			s.executeUpdate("SET UNIQUE_CHECKS=1;");	//Enabled all the checks
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);		//Closed connection
		}
	}

	/**
	 * 2. Method to add a person record to the database with the provided data.
	 * 
	 * @param personCode
	 * @param firstName
	 * @param lastName
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addPerson(String personCode, String firstName, String lastName, String street, String city, String state, String zip, String country) {
		Connection c = null;			//Created Connection and statement objects, initialized it
		PreparedStatement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Created and set the connection
			s = c.prepareStatement("INSERT INTO Address (Street, City, State, Country, ZIP) VALUES (?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);	//Prepared a statement with the key as return value
			s.setString(1, street);
			s.setString(2, city);
			s.setString(3, state);		//Set all the values
			s.setString(4, country);
			s.setString(5, zip);
			s.executeUpdate();	//Execute the sql 
			try (ResultSet generatedKeys = s.getGeneratedKeys()) {	//Get the returned key and set it as a resultset
				if (generatedKeys.next()) {					//If it returned a key..
					int addID = generatedKeys.getInt(1);	//Store the key
					generatedKeys.close();					//Close the resultset for the key
					s.close();								//Close statement
					s = c.prepareStatement("INSERT INTO Person (PersonCode, FirstName, LastName, AddressID) VALUES (?,?,?,?);");	//Prepare a new statement for person
					s.setString(1, personCode);		
					s.setString(2, firstName);		//Set all the values
					s.setString(3, lastName);
					s.setInt(4, addID);
					s.executeUpdate();		//Execute the statement
				}
				else {
					generatedKeys.close();	//Close the key even if it fails or didn't generate anything
					throw new SQLException("Creating person failed, no ID obtained.");	//If not successful, throw exception
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();		//Print exception
		} finally {
			DatabaseInfo.closeConnection(c, s);		//Close connection
		}
	}

	/**
	 * 3. Adds an email record corresponding person record corresponding to the
	 * provided <code>personCode</code>
	 * 
	 * @param personCode
	 * @param email
	 */
	public static void addEmail(String personCode, String email) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.prepareStatement("INSERT INTO Email VALUES (?,?);");	//Prepare an sql statement
			s.setString(2, email);	//Set the value

			PreparedStatement st = c.prepareStatement("SELECT PersonID FROM Person WHERE PersonCode = ?;");	//Prepare another sql statement to get PersonID
			st.setString(1, personCode);		//Set the personcode
			ResultSet rs = st.executeQuery();	//Executre and store in resultset
			if(rs.next()) {						//If resultset has a value..
				s.setInt(1, rs.getInt("PersonID"));	//Set the first value as personID
			}
			else {
				throw new SQLException("Creating email failed, no ID obtained.");		//If nothing returned, throw new exception
			}
			s.executeUpdate();	//Execute the statement

			rs.close();	//Close the connections
			st.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);	//Close the connections
		}
	}

	/**
	 * 4. Method that removes every member record from the database
	 */
	public static void removeAllMembers() {
		Connection c = null;	//Create connection and statement objects, initialize it
		Statement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.createStatement();			//Prepare a statement
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");	//Disable key checks
			s.executeUpdate("SET UNIQUE_CHECKS=0;");

			s.executeUpdate("UPDATE Invoice SET MemberID = 0;");	//Set all the member id to 0

			s.executeUpdate("TRUNCATE Member;");		//Clear the Member table
			
			s.executeUpdate("SET UNIQUE_CHECKS=1;");	//Enable key checks
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		} catch (SQLException e) {
			e.printStackTrace();	
		} finally {
			DatabaseInfo.closeConnection(c, s);	//Close the connection
		}
	}
	/**
	 * 5. Method to add a member record to the database with the provided data
	 * @param memberCode
	 * @param memberType
	 * @param primaryContactPersonCode
	 * @param name
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 * @param country
	 */
	public static void addMember(String memberCode, String memberType, String primaryContactPersonCode,String name, String street, String city, String state, String zip, String country) {
		Connection c = null;	//Create connection and statement objects, initialize it
		PreparedStatement s1 = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s1 = c.prepareStatement("INSERT INTO Member (MemberCode, MemberType, CompanyName, AddressID, PersonID) VALUES (?,?,?,?,?);");	//Prepare a statement
			s1.setString(1, memberCode);
			s1.setString(2, memberType);	//Set the given values
			s1.setString(3, name);

			PreparedStatement s2 = c.prepareStatement("INSERT INTO Address (Street, City, State, Country, ZIP) VALUES (?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);	//Create another statement for address
			s2.setString(1, street);
			s2.setString(2, city);
			s2.setString(3, state);		//Set the address values
			s2.setString(4, country);
			s2.setString(5, zip);
			s2.executeUpdate();		//Execute the statement for address
			try (ResultSet generatedKeys = s2.getGeneratedKeys()) {	//Store the returned key in resultset
				if (generatedKeys.next()) {		//If there is a key..
					s1.setInt(4, generatedKeys.getInt(1));	//Set the returned key as a value
				}	
				else {
					throw new SQLException("Creating member failed, no ID obtained.");	//If no key obtained.. 
				}
				generatedKeys.close();	//Close the resultset
			}
			s2.close();	//Close address statement

			PreparedStatement s3 = c.prepareStatement("SELECT PersonID FROM Person WHERE PersonCode = ?;");	//Create a new statement for personid
			s3.setString(1, primaryContactPersonCode);	//Set the personcode
			ResultSet rs = s3.executeQuery();	//Execute the statement
			if(rs.next()) {						//If it returns something..
				s1.setInt(5, rs.getInt("PersonID"));	//Get the value in personid column
			}
			else {
				throw new SQLException("Creating member failed, no ID obtained.");	//If nothing returned..
			}
			rs.close();		//Close the personid statement
			s3.close();

			s1.executeUpdate();		//Execture the member statement
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close the connection
		}
	}

	/**
	 * 6. Removes all product records from the database
	 */
	public static void removeAllProducts() {
		Connection c = null;	//Create connection and statement objects, initialize it
		Statement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create and store a new connection
			s = c.createStatement();	//Create a new statement
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");	//Disable key checks
			s.executeUpdate("SET UNIQUE_CHECKS=0;");	

			s.executeUpdate("TRUNCATE YearlyProduct;");		//Clear YearlyProduct table
			s.executeUpdate("TRUNCATE DailyProduct;");		//Clear DailyProduct table
			s.executeUpdate("TRUNCATE ParkingPass;");		//Clear ParkingPass table
			s.executeUpdate("TRUNCATE RentalEquipment;");	//Clear RentalEquipment table
			s.executeUpdate("TRUNCATE Purchased;");			//Clear Purchased table

			s.executeUpdate("TRUNCATE Product;");			//Clear Product table
			
			s.executeUpdate("SET UNIQUE_CHECKS=1;");		//Enable key checks
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);		//Close connection
		}
	}

	/**
	 * 7. Adds a day-pass record to the database with the provided data.
	 */
	public static void addDayPass(String productCode, String dateTime, String street, String city, String state, String zip, String country, double pricePerUnit) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s1 = null;
		try {
			c = DatabaseInfo.getConnection();		//Create a new connection
			PreparedStatement s3 = c.prepareStatement("INSERT INTO Product (ProductCode, ProductType) VALUES (?,?);");	//Prepare a new statement for product
			s3.setString(1, productCode);		//Set the given informations
			s3.setString(2, "D");
			s3.executeUpdate();		//Execute and add product
			s3.close();		//Close product statement
			
			s1 = c.prepareStatement("INSERT INTO DailyProduct VALUES (?,?,?,?);");	//Prepare a new statement for Daily product
			s1.setString(1, productCode);	//Set the given information
			s1.setString(2, dateTime);

			PreparedStatement s2 = c.prepareStatement("INSERT INTO Address (Street, City, State, Country, ZIP) VALUES (?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS);	//Prepare a new statement for address
			s2.setString(1, street);
			s2.setString(2, city);
			s2.setString(3, state);		//Use the given info and add address
			s2.setString(4, country);
			s2.setString(5, zip);
			s2.executeUpdate();		//Execute address statement
			try (ResultSet generatedKeys = s2.getGeneratedKeys()) {	//Get and store the keys in resultset
				if (generatedKeys.next()) {		//If it has key..
					s1.setInt(3, generatedKeys.getInt(1));	//Set the key for address
				}
				else {
					throw new SQLException("Creating DayPass failed, no ID obtained."); //If it doesn't have key..
				}
				generatedKeys.close();	//Close the resultset
			}
			s2.close();

			s1.setDouble(4, pricePerUnit);	//Set the price
			s1.executeUpdate();	//Execute the daily product statement
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close the connection
		}
	}

	/**
	 * 8. Adds a year-long-pass record to the database with the provided data.
	 */
	public static void addYearPass(String productCode, String StartDate, String EndDate,String street, String city, String state, String zip, String country, String name, double pricePerUnit) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s1 = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s3 = c.prepareStatement("INSERT INTO Product (ProductCode, ProductType) VALUES (?,?);");		//Prepare a new statement for product
			s3.setString(1, productCode);
			s3.setString(2, "Y");			//Set all the information
			s3.executeUpdate();		//Add the product
			s3.close();		//Close the product statement
			
			s1 = c.prepareStatement("INSERT INTO YearlyProduct VALUES (?,?,?,?,?,?);");		//Prepare a new statement for Yearly Product
			s1.setString(1, productCode);
			s1.setString(2, StartDate);
			s1.setString(3, EndDate);			//Store all the given information first
			s1.setString(5, name);
			s1.setDouble(6, pricePerUnit);

			PreparedStatement s2 = c.prepareStatement("INSERT INTO Address (Street, City, State, Country, ZIP) VALUES (?,?,?,?,?);", PreparedStatement.RETURN_GENERATED_KEYS); //Prepare a new statement for address
			s2.setString(1, street);
			s2.setString(2, city);
			s2.setString(3, state);			//Set all the given address info
			s2.setString(4, country);
			s2.setString(5, zip);
			s2.executeUpdate();			//Add the address
			try (ResultSet generatedKeys = s2.getGeneratedKeys()) {	//If the key is returned..
				if (generatedKeys.next()) {
					s1.setInt(4, generatedKeys.getInt(1));		//Store the addressid
				}
				else {
					throw new SQLException("Creating YearPass failed, no ID obtained.");	//If nothing returned..
				}
				generatedKeys.close();		//Close the resultset
			}
			s2.close();		//Close the address statement

			s1.executeUpdate();		//Execute the yearly product statement
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close connection
		}
	}

	/**
	 * 9. Adds a ParkingPass record to the database with the provided data.
	 */
	public static void addParkingPass(String productCode, double parkingFee) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s1 = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s3 = c.prepareStatement("INSERT INTO Product (ProductCode, ProductType) VALUES (?,?);");		//Prepare a new statement for product
			s3.setString(1, productCode);
			s3.setString(2, "P");		//Set the given information
			s3.executeUpdate();		//Add the product
			s3.close();		//Close the product statement
			
			s1 = c.prepareStatement("INSERT INTO ParkingPass VALUES (?,?);");		//Prepare a new statement for parking pass
			s1.setString(1, productCode);
			s1.setDouble(2, parkingFee);		//Set the given information
			s1.executeUpdate();		//Execute parking pass
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);		//Close connection
		}
	}

	/**
	 * 10. Adds an equipment rental record to the database with the provided data.
	 */
	public static void addRental(String productCode, String name, double cost) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s1 = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s3 = c.prepareStatement("INSERT INTO Product (ProductCode, ProductType) VALUES (?,?);");		//Prepare a new statement for product
			s3.setString(1, productCode);
			s3.setString(2, "R");	//Set the given information
			s3.executeUpdate();	//Add the product
			s3.close();		//Close the product statement
			
			s1 = c.prepareStatement("INSERT INTO RentalEquipment VALUES (?,?,?);");		//Prepare a new statement for rental equipment
			s1.setString(1, productCode);
			s1.setString(2, name);		//Set the given information
			s1.setDouble(3, cost);
			s1.executeUpdate();		//Execute rental equipment statement
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1); //Close connection
		}
	}

	/**
	 * 11. Removes all invoice records from the database
	 */
	public static void removeAllInvoices() {
		Connection c = null;			//Create connection and statement objects, initialize it
		Statement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.createStatement();
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=0;");	//Disable key checks
			s.executeUpdate("SET UNIQUE_CHECKS=0;");

			s.executeUpdate("TRUNCATE Purchased;");		//Clear Purchased table

			s.executeUpdate("TRUNCATE Invoice;");		//Clear Invoice table
			
			s.executeUpdate("SET UNIQUE_CHECKS=1;");	//Enable key checks
			s.executeUpdate("SET FOREIGN_KEY_CHECKS=1;");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);		//Close connection
		}
	}

	/**
	 * 12. Adds an invoice record to the database with the given data.
	 */
	public static void addInvoice(String invoiceCode, String memberCode, String personalTrainerCode, String invoiceDate) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s1 = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s1 = c.prepareStatement("INSERT INTO Invoice (InvoiceCode, InvoiceDate, PersonID, MemberID) VALUES (?,?,?,?);");		//Prepare a new statement for invoice
			s1.setString(1, invoiceCode);
			s1.setString(2, invoiceDate);		//Set the given information

			PreparedStatement s2 = c.prepareStatement("SELECT PersonID FROM Person WHERE PersonCode = ?;"); //Prepare a new statement for personid
			s2.setString(1, personalTrainerCode);	//Set the personcode
			ResultSet rs1 = s2.executeQuery();		//Store personid
			if(rs1.next()) {
				s1.setInt(3, rs1.getInt("PersonID"));	//Set the personid to invoice statement
			}
			else {
				throw new SQLException("Creating Invoice failed, no ID obtained.");	//If no personid found..
			}
			rs1.close();	//Close the personid statement
			s2.close();

			PreparedStatement s3 = c.prepareStatement("SELECT MemberID FROM Member WHERE MemberCode = ?;");	//Prepare a new statement for memberid
			s3.setString(1, memberCode);		//Set the membercode
			ResultSet rs2 = s3.executeQuery();	//Store memberid
			if(rs2.next()) {
				s1.setInt(4, rs2.getInt("MemberID"));	//Set the memberid to invoice statement
			}
			else {
				throw new SQLException("Creating Invoice failed, no ID obtained.");	//If no memberid found..
			}
			rs2.close();	//Close the memberid statement
			s3.close();

			s1.executeUpdate();		//Execute the invoice statement
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close connection
		}
	}

	/**
	 * 13. Adds a particular day-pass (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given number of units
	 */

	public static void addDayPassToInvoice(String invoiceCode, String productCode, int quantity) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.prepareStatement("INSERT INTO Purchased VALUES (?,?,?,?);");		//Prepare a new statement for day long
			s.setString(2, productCode);
			s.setInt(3, quantity);		//Set the given info
			s.setString(4, "");

			PreparedStatement st = c.prepareStatement("SELECT InvoiceID FROM Invoice WHERE InvoiceCode = ?;");	//Prepare a new statement for invoiceid
			st.setString(1, invoiceCode);	//Set the invoicecode
			ResultSet rs = st.executeQuery();	//Store the invoice id
			if(rs.next()) {							
				s.setInt(1, rs.getInt("InvoiceID"));	//Set the invoiceid to daylong statement
			}
			else {
				throw new SQLException("Adding daypass to invoice failed, no ID obtained.");	//If no invoiceid found..
			}
			rs.close();		//Close the invoiceid statement
			st.close();
			
			s.executeUpdate();	//Execute the day long statement
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);	//Close connection
		}
	}

	/**
	 * 14. Adds a particular year-long-pass (corresponding to <code>productCode</code>
	 * to an invoice corresponding to the provided <code>invoiceCode</code> with
	 * the given begin/end dates
	 */
	public static void addYearPassToInvoice(String invoiceCode, String productCode, int quantity) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.prepareStatement("INSERT INTO Purchased VALUES (?,?,?,?);");		//Prepare a new statement for year long
			s.setString(2, productCode);
			s.setInt(3, quantity);		//Set the given info
			s.setString(4, "");

			PreparedStatement st = c.prepareStatement("SELECT InvoiceID FROM Invoice WHERE InvoiceCode = ?;");	//Prepare a new statement for invoiceid
			st.setString(1, invoiceCode);		//Set the invoicecode
			ResultSet rs = st.executeQuery();	//Store the invoice id
			if(rs.next()) {
				s.setInt(1, rs.getInt("InvoiceID"));	//Set the invoiceid to yearlong statement
			}
			else {
				throw new SQLException("Adding yearpass to invoice failed, no ID obtained.");	//If no invoiceid found..
			}
			rs.close();		//Close the invoiceid statement
			st.close();
			
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);	//Close connection
		}
	}

	/**
	 * 15. Adds a particular ParkingPass (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of quantity.
	 * NOTE: membershipCode may be null
	 */
	public static void addParkingPassToInvoice(String invoiceCode, String productCode, int quantity, String membershipCode) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.prepareStatement("INSERT INTO Purchased VALUES (?,?,?,?);");		//Prepare a new statement for parking pass
			s.setString(2, productCode);		//Set the given info
			s.setInt(3, quantity);
			if(membershipCode == null || membershipCode.isEmpty()) {
				s.setString(4, "");
			}
			else {
				s.setString(4, membershipCode);
			}

			PreparedStatement st = c.prepareStatement("SELECT InvoiceID FROM Invoice WHERE InvoiceCode = ?;");	//Prepare a new statement for invoiceid
			st.setString(1, invoiceCode);		//Set the invoicecode
			ResultSet rs = st.executeQuery();	//Store the invoice id
			if(rs.next()) {
				s.setInt(1, rs.getInt("InvoiceID"));	//Set the invoiceid to parkingpass statement
			}
			else {
				throw new SQLException("Adding parkingpass to invoice failed, no ID obtained.");	//If no invoiceid found..
			}
			rs.close();		//Close the invoiceid statement
			st.close();
			
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);	//Close connection
		}
	}

	/**
	 * 16. Adds a particular equipment rental (corresponding to <code>productCode</code> to an 
	 * invoice corresponding to the provided <code>invoiceCode</code> with the given
	 * number of quantity. 
	 * NOTE: membershipCode may be null
	 */
	public static void addRentalToInvoice(String invoiceCode, String productCode, int quantity, String membershipCode) {
		Connection c = null;			//Create connection and statement objects, initialize it
		PreparedStatement s = null;
		try {
			c = DatabaseInfo.getConnection();	//Create a new connection
			s = c.prepareStatement("INSERT INTO Purchased VALUES (?,?,?,?);");		//Prepare a new statement for rental equipment
			s.setString(2, productCode);		//Set the given info
			s.setInt(3, quantity);
			if(membershipCode == null || membershipCode.isEmpty()) {
				s.setString(4, "");
			}
			else {
				s.setString(4, membershipCode);
			}

			PreparedStatement st = c.prepareStatement("SELECT InvoiceID FROM Invoice WHERE InvoiceCode = ?;");	//Prepare a new statement for invoiceid
			st.setString(1, invoiceCode);		//Set the invoicecode
			ResultSet rs = st.executeQuery();	//Store the invoice id
			if(rs.next()) {
				s.setInt(1, rs.getInt("InvoiceID"));	//Set the invoiceid to rental equipment statement
			}
			else {
				throw new SQLException("Adding rentalequipment to invoice failed, no ID obtained.");	//If no invoiceid found..
			}
			rs.close();		//Close the invoiceid statement
			st.close();

			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s);	//Close connection
		}
	}


}
