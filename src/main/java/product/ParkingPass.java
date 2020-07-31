package product;

import entities.Invoice;
import reader.DatabaseReader;

public class ParkingPass extends Service{	//Subclass of Product

	private double Parkingfee;	//Variables declared
	private int amount;

	public ParkingPass(String productID, double parkingfee) {
		super(productID, "");
		this.Parkingfee = parkingfee;
	}

	public ParkingPass(String productID, double parkingfee, int amount) {
		super(productID, "");
		this.Parkingfee = parkingfee;
		this.amount = amount;
	}

	public ParkingPass(String productID, double parkingfee, int amount, String extraInfo) {
		super(productID, extraInfo);
		this.Parkingfee = parkingfee;
		this.amount = amount;
	}

	public ParkingPass(ParkingPass pro, int quantity, String extraInfo) {
		// TODO Auto-generated constructor stub
		super(pro.getProductID(), extraInfo);
		this.amount = quantity;
		this.Parkingfee = pro.getParkingfee();
	}

	//Setters and getters methods
	public double getParkingfee() {
		return this.Parkingfee;
	}

	public void setParkingfee(double parkingfee) {
		this.Parkingfee = parkingfee;
	}

	@Override
	public String productType() {	//Override the producttype
		return "Parking Pass";
	}

	@Override
	public double getTax() {	//Get tax
		return (0.04 * this.getSubTotal());	//Return the tax amount which is 4%
	}

	@Override
	public double grandTotal() {	//Get total
		return (this.getSubTotal() + this.getTax());	//Return the subtotal + tax
	}

	@Override
	public double getSubTotal() {	//Get subtotal
		for(Invoice inv : DatabaseReader.invoices) {	//Loop through the invoices
			if(inv.getProducts().contains(this) && this.hasExtraInfo()){	//If the products in the invoice has ParkingPass and has extra info..
				Product pro = inv.getProducts().parallelStream().filter(p -> p.getProductID().equals(this.getExtraInfo().trim())).findFirst().orElse(null);	//Get the product using the extrainfo (lambda expression)
				if(pro instanceof YearLongMember) {	//If the product is YearLongMember..
					return Math.max(0, ((this.amount-365)*this.Parkingfee));		//The total is 365 free parking passes
				}
				else if(pro instanceof DayMember){
					return ((this.amount-1)*this.Parkingfee);	//The total is 1 free parking pass 
				}
			}
		}
		return (this.amount * this.Parkingfee);		//If neither, full price
	}

	@Override
	public int getAmount() {	//Get amount
		return this.amount;	//Return the number bought
	}

	@Override
	public double getDiscount() {	//Get discount
		return 0;		//No discount
	}
}
