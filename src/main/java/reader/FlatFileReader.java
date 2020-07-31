/*
 * Name: Nirmitee Gite, Nischal Neupane
 * Date: 18-02-2018
 * Description: A program that reads data files, creates objects for each one and then outputs the invoices that it reads as a formatted output
 */

package reader;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;

import entities.Address;
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

public class FlatFileReader {
	private ArrayList<Person> persons;		//Declared all the arraylists of people, member, product, invoice
	private ArrayList<Member> members;
	private ArrayList<Product> products;
	private ArrayList<Invoice> invoicer;
	
	public ArrayList<Person> getPersons() {	//Getter for person arraylist
		return persons;
	}

	public ArrayList<Member> getMembers() {	//Getter for member arraylist
		return members;
	}

	public ArrayList<Product> getProducts() {	//Getter for product arraylist
		return products;
	}
	
	public ArrayList<Invoice> getInvoices()	{	//Getter for invoice arraylist
		return invoicer;
	}

	public FlatFileReader() {		//Constructor to load all the files
		persons = loadPersonFile();
		members = loadMemberFile();
		products = loadProductFile();
		invoicer = loadInvoiceFile();
	}
	
	private ArrayList<Person> loadPersonFile() {			//Load the persons.dat file and create objects for each
		ArrayList<Person> persons = new ArrayList<>();	//Temp arraylist of persons
		Scanner s = null;								//Scanner object
		try {
			s = new Scanner(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("Persons.dat")).toURI()));	//Initiates scanner object with the file as argument
		}catch(Exception ex) {
			ex.printStackTrace();						//If file not found
		}

		assert s != null;
		while(s.hasNextLine()) {							//Until there is next line
			String[] split = s.nextLine().split(";");		//Split each line with ; delimiter
			ArrayList<String> emails = new ArrayList<>();	//ArrayList of emails
			if(split.length > 3) {							//If the length is more than 3
				Collections.addAll(emails, split[3].split(","));
			}
			Person person = new Person(split[0], split[1].split(",")[0], split[1].split(",")[1], new Address(split[2].split(",")), emails);	//Create a person object
			persons.add(person);		//Add the person object to the arraylist of person
		}
		return persons;		//Return the arraylist of person
	}
	
	private ArrayList<Member> loadMemberFile() {			//Load the members.dat file and create objects for each
		ArrayList<Member> members = new ArrayList<>();	//Temp arraylist of members
		Scanner s = null;								//Scanner object
		try {
			s = new Scanner(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("Members.dat")).toURI()));	//Initiates scanner object with the file as argument
		}catch(Exception ex) {
			ex.printStackTrace();						//If file not found
		}

		assert s != null;
		while(s.hasNextLine()) {							//Until there is next line
			String[] split = s.nextLine().split(";");		//Split each line with ; delimiter
			for(Person per : persons) {						//Loop through each person arraylist
				if(per.getPID().equals(split[2])) {			//Check if the personcode matches
					Member member;
					if(split[1].equalsIgnoreCase("G")) {
						member = new GeneralMember(split[0], split[1], per, split[3], new Address(split[4].split(",")));	//If it matches, pass the person object and create a member object
					}
					else {
						member = new StudentMember(split[0], split[1], per, split[3], new Address(split[4].split(",")));
					}
					
					members.add(member);	//Add the member object to the member arraylist
				}
			}
		}
		return members;		//Return the arraylist of members
	}
	
	private ArrayList<Product> loadProductFile() {	//Load the products.dat file and create objects for each
		ArrayList<Product> products = new ArrayList<>();	//Temp arraylist of products
		Scanner s = null;							//Scanner object
		try {
			s = new Scanner(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("Products.dat")).toURI()));	//Initiates scanner object with the file as argument
		}catch(Exception ex) {
			ex.printStackTrace();					//If file not found
		}

		assert s != null;
		while(s.hasNext()) {								//Until there is next line
			String[] split = s.nextLine().split(";");		//Split each line with ; delimiter
			Product product = null;
			switch(split[1].toLowerCase()) {		//Use the split char to check what type
			case "y":{			//Year long product
				product = new YearLongMember(split[0], split[2] , split[3], new Address(split[4].split(",")), split[5], Double.parseDouble(split[6]));
				break;
			}
			case "d":{			//Day product
				product = new DayMember(split[0], split[2], new Address(split[3].split(",")), Double.parseDouble(split[4]));
				break;
			}
			case "p":{			//Parking
				product = new ParkingPass(split[0], Double.parseDouble(split[2]));
				break;
			}
			case "r":{			//Rental
				product = new EquipmentRental(split[0], split[2], Double.parseDouble(split[3]));
				break;
			}
			}
			products.add(product);	//Add the created product object
		}
		return products;	//Return the product arraylist
	}
	
	private ArrayList<Invoice> loadInvoiceFile() {	//Load the invoices.dat file and create objects for each
		ArrayList<Invoice> invoicer = new ArrayList<>();	//Temp arraylist of invoices
		Scanner s = null;							//Scanner object
		try {
			s = new Scanner(new File(Objects.requireNonNull(getClass().getClassLoader().getResource("Invoices.dat")).toURI()));	//Initiates scanner object with the file as argument
		}catch(Exception ex) {
			ex.printStackTrace();					//If file not found
		}

		assert s != null;
		while(s.hasNext()) {								//Until there is next line
			String[] split = s.nextLine().split(";");		//Split each line with ; delimiter
			Member member = null;							//Create a member object
			for(Member mem : members) {						//Loop through each members in the members array
				if(mem.getMemcode().equals(split[1]))		//Check if the membercode matches
					member = mem;							//If it matches, set the member equal to the member from the arraylist
			}
			Person person = null;							//Create a person object
			for(Person per : persons) {						//Loop through each person in the persons array
				if(per.getPID().equals(split[2])) 			//Check if the personid matches
					person = per;							//If it matches, set the person equal to the person from the arraylist
			}
			ArrayList<Product> product = new ArrayList<>();	//Create a new arraylist of product
			String[] allProducts = split[4].split(",");		//Split the products by the comma delimiter
			for(String str : allProducts) {					//Loop through the array of string of products in the invoice
				String[] splt = str.split(":");				//Split each product into different parts for it's info
				for(Product pro : products) {				//Loop through the arraylist of products
					if(pro.getProductID().equals(splt[0].trim())) {		//If the product id matches..
						Product prod = null;				//Initialize Product object
						String extraInfo = "";
						if(splt.length > 2 && !splt[2].isEmpty()) {
							extraInfo = splt[2];
						}
						
						if(pro instanceof ParkingPass) {	//If the type of Product is ParkingPass, then..
							//Create a new parkingpass object and store it in prod
							prod = new ParkingPass(pro.getProductID(), ((ParkingPass) pro).getParkingfee(), Integer.parseInt(splt[1].trim()), extraInfo);
						}
						if(pro instanceof EquipmentRental) {	//If the type of Product is EquipmentRental, then..
							//Create a new EquipmentRental object and store it in prod
							prod = new EquipmentRental(pro.getProductID(), ((EquipmentRental) pro).getName(), ((EquipmentRental) pro).getCost(), Integer.parseInt(splt[1].trim()), extraInfo);
						}						
						if(pro instanceof YearLongMember) {		//If the type of Product is YearLongMember, then..
							//Create a new YearLongMember object and store it in prod
							prod = new YearLongMember(pro.getProductID(), ((YearLongMember) pro).getStartdate(), ((YearLongMember) pro).getEnddate(), ((YearLongMember) pro).getAddress(), ((YearLongMember) pro).getName(), ((YearLongMember) pro).getPPU(), Integer.parseInt(splt[1].trim()), extraInfo);
						}
						if(pro instanceof DayMember) {		//If the type of Product is DayMember, then..
							//Create a new DayMember object and store it in prod
							prod = new DayMember(pro.getProductID(), ((DayMember) pro).getDatetime(), ((DayMember) pro).getAddress(), ((DayMember) pro).getCost(), Integer.parseInt(splt[1].trim()), extraInfo);
						}
						product.add(prod);				//Add the new prod object to the product arraylist
					}
				}
			}
			
			Invoice inv = new Invoice(split[0], member, person, split[3], product);	//Create a new invoice object with invoiceid, member, person, date and product arraylist
			invoicer.add(inv);							//Add the invoice to the arraylist of invoices
		}
		return invoicer;	//Return the invoice arraylist
	}
}
