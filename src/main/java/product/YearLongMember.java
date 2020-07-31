package product;

import org.joda.time.DateTime;

import entities.Address;

public class YearLongMember extends Membership{	//Sub class of Product
	private String startdate, enddate;	//All the variables
	private Address address;
	private String name;
	private double PPU;
	private int amount;

	public YearLongMember(String productID, String startdate, String enddate, Address address, String name, double pPU) {	//Constructor with all the variables
		super(productID, "");
		this.startdate = startdate;
		this.enddate = enddate;
		this.address = address;
		this.name = name;
		PPU = pPU;
	}
	
	public YearLongMember(String productID, String startdate, String enddate, Address address, String name, double pPU, int amount) {	//Constructor with all the variables
		super(productID, "");
		this.startdate = startdate;
		this.enddate = enddate;
		this.address = address;
		this.name = name;
		PPU = pPU;
		this.amount = amount;
	}
	
	public YearLongMember(String productID, String startdate, String enddate, Address address, String name, double pPU, int amount, String extraInfo) {	//Constructor with all the variables
		super(productID, extraInfo);
		this.startdate = startdate;
		this.enddate = enddate;
		this.address = address;
		this.name = name;
		PPU = pPU;
		this.amount = amount;
	}

	public YearLongMember(YearLongMember pro, int quantity, String extraInfo) {
		// TODO Auto-generated constructor stub
		super(pro.getProductID(), extraInfo);
		this.startdate = pro.getStartdate();
		this.enddate = pro.getEnddate();
		this.address = pro.getAddress();
		this.name = pro.getName();
		PPU = pro.getPPU();
		this.amount = quantity;
	}

	//Setters and getters method
	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPPU() {
		return PPU;
	}

	public void setPPU(double pPU) {
		PPU = pPU;
	}

	@Override
	public String productType() {	//Override the producttype
		return "Year long membership";
	}

	@Override
	public double getTax() {
		return (0.06 * this.getSubTotal());
	}

	@Override
	public double grandTotal() {
		return (this.getSubTotal() + this.getTax());
	}

	@Override
	public double getSubTotal() {
		// TODO Auto-generated method stub
		DateTime date = new DateTime(this.startdate);
		if(date.getMonthOfYear() == 1) {
			return ((this.amount * this.PPU) - this.getDiscount());
		}
		return (this.amount * this.PPU);
	}
	
	@Override
	public double getDiscount() {
		return ((this.amount * this.PPU) * (0.15));
	}
	
	@Override
	public int getAmount() {
		return this.amount;
	}
}
