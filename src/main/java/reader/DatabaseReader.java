package reader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entities.Address;
import entities.DatabaseInfo;
import entities.DatabaseUtil;
import entities.Invoice;
import entities.Person;
import member.GeneralMember;
import member.Member;
import member.StudentMember;
import product.DayMember;
import product.EquipmentRental;
import product.ParkingPass;
import product.Product;
import product.YearLongMember;

public class DatabaseReader {
	public static ArrayList<Person> persons = new ArrayList<Person>();		//Created a person arraylist
	public static ArrayList<Member> members = new ArrayList<Member>();		//Created a member arraylist
	public static ArrayList<Product> products = new ArrayList<Product>();	//Created a product arraylist
	public static ArrayList<Invoice> invoices = new ArrayList<Invoice>();	//Created an invoice arraylist
	
	public static void loadPersonDB(){		//Loads all the people from the DB
		Connection c = null;				//Create connection and statement and initialize them 
		Statement s1 = null;
		try {		
			c = DatabaseInfo.getConnection();		//Create a new connection
			s1 = c.createStatement();				//Create a new statement
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Person;");	//Execute and store resultset for each person

			while(rs1.next()) {			//Loop until there are more lines from Person table
				String personCode = rs1.getString("PersonCode");	//Get the PersonCode
				String firstName = rs1.getString("FirstName");		//Get the FirstName
				String lastName = rs1.getString("LastName");		//Get the LastName
				Address address = DatabaseUtil.findAddress(rs1.getInt("AddressID"));	//Find and get the address using AddressID
				ArrayList<String> emails = DatabaseUtil.findEmails(rs1.getInt("PersonID"));	//Find and get the emails using PersonID
				Person person = new Person(personCode, lastName, firstName, address, emails);	//Create a new person object using all the info
				persons.add(person);		//Add the person to the persons arraylist
			}

			rs1.close();	//Close the resultset
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close the connection
		}
	}

	public static void loadMemberDB(){		//Loads all the members from the DB
		Connection c = null;				//Create connection and statement and initialize them 
		Statement s1 = null;
		try {
			c = DatabaseInfo.getConnection();		//Create a new connection
			s1 = c.createStatement();				//Create a new statement
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Member;");	//Execute and store resultset for each member

			while(rs1.next()) {			//Loop until there are more lines from Member table
				String memberCode = rs1.getString("MemberCode");	//Get the MemberCode
				String memberType = rs1.getString("MemberType");	//Get the MemberType
				String name = rs1.getString("CompanyName");			//Get the Company Name
				Address address = DatabaseUtil.findAddress(rs1.getInt("AddressID"));	//Find and get the address using AddressID

				Member member = null;		//Create a new member object
				Person person = DatabaseUtil.findPerson(rs1.getInt("PersonID"));	//Find and get the person using PersonID
				if(memberType.equalsIgnoreCase("G") || memberType.equalsIgnoreCase("General")) {	//If the type is general..
					member = new GeneralMember(memberCode, "G", person, name, address);		//Create a generalmember object
				}
				else if(memberType.equalsIgnoreCase("S") || memberType.equalsIgnoreCase("Student")) {	//If student..
					member = new StudentMember(memberCode, "S", person, name, address);		//Create a studentmember object
				}

				members.add(member);	//Add the member to the member arraylist
			}
			
			rs1.close();	//Close the resultset
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close the connection
		}
	}

	public static void loadProductDB(){		//Loads all the products from the DB
		Connection c = null;				//Create connection and statement and initialize them 
		Statement s1 = null;
		try {
			c = DatabaseInfo.getConnection();		//Create a new connection
			s1 = c.createStatement();				//Create a new statement
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Product;");	//Execute and store resultset for each product

			while(rs1.next()) {		//Loop until there are more lines from Product tables
				String productCode = rs1.getString("ProductCode");	//Get the ProductCode
				String productType = rs1.getString("ProductType");	//Get the ProductType


				Product product = null;			//Initialize a product variable
				switch(productType.toLowerCase()) {		//Use the split char to check what type
				case "y":{			//Year long product
					Statement s2 = c.createStatement();		//Create a new statement for YearlyProduct
					ResultSet rs2 = s2.executeQuery("SELECT * FROM YearlyProduct WHERE ProductCode = '"+rs1.getString("ProductCode")+"'");	//Execute and store the resultset
					
					if(rs2.next()) {	//If the product with productcode was found..
						String startDate = rs2.getString("StartDate");		//Get the startDate
						String endDate = rs2.getString("EndDate");			//Get the endDate
						Address address = DatabaseUtil.findAddress(rs2.getInt("AddressID"));	//Find and get the address using AddressID
						String name = rs2.getString("Name");		//Get the Name
						double cost = rs2.getDouble("Cost");		//Get the Cost
						product = new YearLongMember(productCode, startDate, endDate, address, name, cost);	//Create a new YearLongMember object with all the info
					}
					
					rs2.close();		//Close the resultset
					s2.close();			//Close the statement
					break;
				}
				case "d":{			//Day product
					Statement s2 = c.createStatement();		//Create a new statement for DailyProduct
					ResultSet rs2 = s2.executeQuery("SELECT * FROM DailyProduct WHERE ProductCode = '"+rs1.getString("ProductCode")+"'");	//Execute and store the resultset
					
					if(rs2.next()) {	//If the product with productcode was found..
						String date = rs2.getString("Date");	//Get the Date
						Address address = DatabaseUtil.findAddress(rs2.getInt("AddressID"));	//Find and get the address using AddressID
						double cost = rs2.getDouble("Cost");	//Get the Cost
						product = new DayMember(productCode, date, address, cost);	//Create a new DayMember object with all the info
					}
					
					rs2.close();		//Close the resultset
					s2.close();			//Close the statement
					break;
				}
				case "p":{			//Parking Pass
					Statement s2 = c.createStatement();		//Create a new statement for ParkingPass
					ResultSet rs2 = s2.executeQuery("SELECT * FROM ParkingPass WHERE ProductCode = '"+rs1.getString("ProductCode")+"'");	//Execute and store the resultset
					
					if(rs2.next()) {	//If the product with productcode was found..
						double cost = rs2.getDouble("Cost");	//Get the Cost
						product = new ParkingPass(productCode, cost);	//Create a new ParkingPass object with all the info
					}
					
					rs2.close();		//Close the resultset
					s2.close();			//Close the statement
					break;
				}
				case "r":{			//Rental Equipment
					Statement s2 = c.createStatement();		//Create a new statement for RentalEquipment
					ResultSet rs2 = s2.executeQuery("SELECT * FROM RentalEquipment WHERE ProductCode = '"+rs1.getString("ProductCode")+"'");	//Execute and store the resultset
					
					if(rs2.next()) {	//If the product with productcode was found..
						String productName = rs2.getString("ProductName");	//Get the name
						double cost = rs2.getDouble("Cost");	//Get the Cost
						product = new EquipmentRental(productCode, productName, cost);	//Create a new RentalEquipment object with all the info
					}
					
					rs2.close();		//Close the resultset
					s2.close();			//Close the statement
					break;
				}
				}

				products.add(product);	//Add the product to the products arraylist
			}
			
			rs1.close();	//Close the resultset
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close the connection
		}
	}

	public static void loadInvoiceDB(){		//Loads all the invoices from the DB
		Connection c = null;				//Create connection and statement and initialize them 
		Statement s1 = null;
		try {
			c = DatabaseInfo.getConnection();		//Create a new connection
			s1 = c.createStatement();				//Create a new statement
			ResultSet rs1 = s1.executeQuery("SELECT * FROM Invoice;");		//Execute and store resultset for each invoice

			while(rs1.next()) {			//Loop until there are more lines from Invoice table
				String invoiceCode = rs1.getString("InvoiceCode");					//Get the InvoiceCode
				String invoiceDate = rs1.getString("InvoiceDate");					//Get the InvoiceDate
				Person trainer = DatabaseUtil.findPerson(rs1.getInt("PersonID"));	//Find and get the Person using PersonID
				Member member = DatabaseUtil.findMember(rs1.getInt("MemberID"));	//Find and get the Member using MemberID
				ArrayList<Product> product = DatabaseUtil.findInvoiceProducts(rs1.getInt("InvoiceID"));	//Find and get the Products using InvoiceID
				
				Invoice inv = new Invoice(invoiceCode, member, trainer, invoiceDate, product);	//Create a new Invoice object with the given info
				invoices.add(inv);	//Add invoice to invoices arraylist
			}
			
			rs1.close();		//Close the resultset
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DatabaseInfo.closeConnection(c, s1);	//Close the connection
		}
	}
}
