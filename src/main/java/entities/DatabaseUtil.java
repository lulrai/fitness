package entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import member.Member;
import product.DayMember;
import product.EquipmentRental;
import product.ParkingPass;
import product.Product;
import product.YearLongMember;
import reader.DatabaseReader;

public class DatabaseUtil {
	public static Address findAddress(int addressID) {	//Finds an address from the database, creates a new address object and returns it
		Address address = null;		//Create a new address, initialize to null
		try {
			Connection c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s2 = c.prepareStatement("SELECT * FROM Address WHERE AddressID = ?;");	//Create a prepared statement for address
			s2.setInt(1, addressID);		//Set the given info, addressid
			ResultSet rs2 = s2.executeQuery();	//Execute and store the resultset
			if(rs2.next()) {		//if the resultset has next..
				address = new Address(rs2.getString("Street"), rs2.getString("City"), rs2.getString("State"), rs2.getString("Country"), rs2.getString("ZIP"));	//create a new address object with all the info from resultset
			}
			else {
				throw new SQLException("Failed to find Address.");	//If nothing found.. throw sql exception
			}
			rs2.close();	//Close resultset
			s2.close();		//Close statement
			c.close();		//Close connection
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return address;	//Return address
	}
	
	public static Person findPerson(int personID) {		//Find a person with the given id
		Person person = null;		//Create a new person, initialize to null
		try {
			String personCode;		//Create a personcode string
			Connection c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s3 = c.prepareStatement("SELECT PersonCode FROM Person WHERE PersonID = ?;");		//Create a prepared statement for person
			s3.setInt(1, personID);		//Set the given info, personid
			ResultSet rs3 = s3.executeQuery();		//Execute and store the resultset
			if(rs3.next()) {		//If the resultset has next..
				personCode = rs3.getString("PersonCode");	//Get personcode 
			}
			else {
                throw new SQLException("Failed to find the Person, no PersonCode obtained.");	//If nothing found.. throw sql exception
			}
			rs3.close();	//Close resultset
			s3.close();		//Close statement
			c.close();		//Close connection
			
			for(Person per : DatabaseReader.persons) {	//Loop through each person arraylist
				if(per.getPID().equals(personCode)) {			//Check if the personcode matches
					person = per;		//Set the person to person object
					break;
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return person;	//Return person
	}
	
	public static Member findMember(int memberID) {		//Find a member with the given id
		Member member = null;		//Create a new member, initialize to null
		try {
			String memberCode;	//Create a membercode string
			Connection c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s3 = c.prepareStatement("SELECT MemberCode FROM Member WHERE MemberID = ?;");		//Create a prepared statement for member
			s3.setInt(1, memberID);		//Set the given info, memberid
			ResultSet rs2 = s3.executeQuery();	//Execute and store the resultset
			if(rs2.next()) {		//If the resultset has next..
				memberCode = rs2.getString("MemberCode");	//Get membercode
			}
			else {
                throw new SQLException("Failed to find the Member, no MemberCode obtained.");		//If nothing found.. throw sql exception
			}
			rs2.close();	//Close resultset
			s3.close();		//Close statement
			c.close();		//Close connection
			
			for(Member mem : DatabaseReader.members) {	//Loop through each member arraylist
				if(mem.getMemcode().equals(memberCode)) {			//Check if the membercode matches
					member = mem;		//Set the member to member object
					break;
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return member;		//Return member
	}
	
	public static ArrayList<Product> findInvoiceProducts(int invoiceID) {	//Returns all the products of the given invoiceid
		ArrayList<Product> products = new ArrayList<>();	//Create a new arraylist of products
		try {
			Connection c = DatabaseInfo.getConnection();	//Create a new connection
			PreparedStatement s3 = c.prepareStatement("SELECT * FROM Purchased WHERE InvoiceID = ?;");		//Create a prepared statement for purchased
			s3.setInt(1, invoiceID);		//Set the given info, invoiceid
			ResultSet rs2 = s3.executeQuery();	//Execute and store the resultset
			while(rs2.next()) {		//If the resultset has next..
				Product prod = null;	//Create a new product object
				String productCode = rs2.getString("ProductCode");		//Get the productcode
				int quantity = rs2.getInt("Quantity");					//Get the quantity
				String extraInfo = rs2.getString("ExtraInfo");			//Get the extra info
				
				for(Product pro : DatabaseReader.products) {		//For each product from db
					if(pro.getProductID().equals(productCode)) {	//If the productcodes are equal
						if(pro instanceof ParkingPass) {	//If the type of Product is ParkingPass, then..
							//Create a new parkingpass object and store it in prod
							prod = new ParkingPass((ParkingPass)pro, quantity, extraInfo);
						}
						if(pro instanceof EquipmentRental) {	//If the type of Product is EquipmentRental, then..
							//Create a new EquipmentRental object and store it in prod
							prod = new EquipmentRental((EquipmentRental)pro, quantity, extraInfo);
						}						
						if(pro instanceof YearLongMember) {		//If the type of Product is YearLongMember, then..
							//Create a new YearLongMember object and store it in prod
							prod = new YearLongMember((YearLongMember)pro, quantity, extraInfo);
						}
						if(pro instanceof DayMember) {		//If the type of Product is DayMember, then..
							//Create a new DayMember object and store it in prod
							prod = new DayMember((DayMember)pro, quantity, extraInfo);
						}
					}
				}
				products.add(prod);		//Add the new created product object to the arraylist
			}
			rs2.close();		//Close resultset
			s3.close();			//Close statement
			c.close();			//Close connection
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return products;		//Return the arraylist of products
	}
	
	public static ArrayList<String> findEmails(int personID) {		//Returns all the emails of the given personID
		ArrayList<String> emails = new ArrayList<>();		//Create a new arraylist of emails
		try {
			Connection c = DatabaseInfo.getConnection();		//Create a new connection to the db
			PreparedStatement s3 = c.prepareStatement("SELECT Email FROM Email WHERE PersonID = ?;");	//Prepare a statement for Emails
			s3.setInt(1, personID);		//Set the given info
			ResultSet rs3 = s3.executeQuery();	//Execute and store the resultset
			while(rs3.next()) {		//If resultset has next..
				emails.add(rs3.getString("Email"));	//Add the email from the email column to the arraylist
			}
			rs3.close();	//Close the resultset
			s3.close();		//Close the statement
			c.close();		//Close the connection
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return emails;		//Return the arraylist of emails
	}
}
