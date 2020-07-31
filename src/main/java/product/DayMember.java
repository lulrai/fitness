package product;

import org.joda.time.DateTime;

import entities.Address;

public class DayMember extends Membership {	//Subclass of producttype

	private String datetime;	//Variable declared
	private Address address;
	private double cost;
	private int amount;
	
	public DayMember(String productID, String datetime, Address address, double cost) {	//Constructor
		super(productID, "");
		this.datetime = datetime;
		this.address = address;
		this.cost = cost;
	}
	
	public DayMember(String productID, String datetime, Address address, double cost, int amount) {	//Constructor
		super(productID, "");
		this.datetime = datetime;
		this.address = address;
		this.cost = cost;
		this.amount = amount;
	}
	
	public DayMember(String productID, String datetime, Address address, double cost, int amount, String extraInfo) {	//Constructor
		super(productID, extraInfo);
		this.datetime = datetime;
		this.address = address;
		this.cost = cost;
		this.amount = amount;
	}
	
	public DayMember(DayMember pro, int quantity, String extraInfo) {	//Copy constructor with extra information
		super(pro.getProductID(), extraInfo);
		this.datetime = pro.getDatetime();
		this.address = pro.getAddress();
		this.cost = pro.getCost();
		this.amount = quantity;
	}

	//Setters and Getters
	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}


	@Override
	public String productType() {	//Override the producttype
		return "Day membership";	//Return day membership
	}

	@Override
	public double getTax() {		//Gets the tax
		return (0.06 * this.getSubTotal());	//Since day membership, tax is 6% of the subtotal
	}

	@Override
	public double grandTotal() {	//Gets the grandtotal
		return (this.getSubTotal() + this.getTax());	//Grandtotal is subtotal + tax
	}

	@Override
	public double getSubTotal() {	//Gets subtotal
		DateTime date = new DateTime(this.getDatetime().replaceAll(" ", "T"));	//Create a new datetime object
		if(date.getMonthOfYear() == 1) {	//If the month is January.. 
			return ((this.amount * this.cost) - this.getDiscount());	//Give the discount
		}
		return (this.amount * this.cost);	//If not, then no discount
	}
	
	@Override
	public double getDiscount() {	//Gets the discount
		return ((this.amount * this.cost) * 0.5);	//50% discount
	}
	
	@Override
	public int getAmount() {	//Get the amount of items
		return this.amount;	//Return amount
	}
	
}
