package product;

import entities.Invoice;
import reader.DatabaseReader;

public class EquipmentRental extends Service{ //Subclass of Product

	private String name;	//Variables declared
	private double cost;
	private int amount;

	public EquipmentRental(String productID, String name, double cost) {	//Constructor
		super(productID, "");
		this.name = name;
		this.cost = cost;
	}
	
	public EquipmentRental(String productID, String name, double cost, int amount) {	//Constructor
		super(productID, "");
		this.name = name;
		this.cost = cost;
		this.amount = amount;
	}
	
	public EquipmentRental(String productID, String name, double cost, int amount, String extraInfo) {	//Constructor
		super(productID, extraInfo);
		this.name = name;
		this.cost = cost;
		this.amount = amount;
	}

	public EquipmentRental(EquipmentRental pro, int quantity, String extraInfo) {
		// TODO Auto-generated constructor stub
		super(pro.getProductID(), extraInfo);
		this.name = pro.getName();
		this.cost = pro.getCost();
		this.amount = quantity;
	}

	//Getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String productType() {	//Override the producttype
		// TODO Auto-generated method stub
		return "Equipment Rental";
	}

	@Override
	public double getTax() {	//Get tax
		return (0.04 * getSubTotal());	//4% tax for services
	}

	@Override
	public double grandTotal() {	//Get total
		return (this.getSubTotal() + this.getTax());	//Tax + subtotal
	}

	@Override
	public double getSubTotal() {	//Get subtotal
		return (this.amount * this.cost) - getDiscount();	//Return total cost - discount
	}
	
	@Override
	public int getAmount() {
		return this.amount;		//Returns the amount of items
	}
	
	@Override
	public double getDiscount() {	//Gets the discount
		for(Invoice inv : DatabaseReader.invoices) {	//Loop through all the invoices
			if(inv.getProducts().contains(this) && this.hasExtraInfo()){	//If the product of the invoice has EquipmentRental and has extra info..
				Product pro = inv.getProducts().parallelStream().filter(p -> p.getProductID().equals(this.getExtraInfo().trim())).findFirst().orElse(null);	//Get the product from the invoice with the extra info code (java 8 lambda expression)
				if(pro instanceof YearLongMember) {	//If the gotten product is YearLong..
					return (this.amount * this.cost) * 0.05;	//The discount is 5%
				}
			}
		}
		return 0;	//If not, return 0
	}
}
