package entities;

import java.util.ArrayList;
import java.util.Comparator;

import member.Member;
import member.StudentMember;
import product.Product;

public class Invoice{
	private String invoiceCode;			//Variable declarations
	private Member memberCode;
	private Person personCode;
	private String invoiceDate;
	private ArrayList<Product> products;

	public Invoice(String invoiceCode, Member memberCode, Person personCode, String invoiceDate,		//Constructor for invoice class
			ArrayList<Product> products) {
		super();
		this.invoiceCode = invoiceCode;
		this.memberCode = memberCode;
		this.personCode = personCode;
		this.invoiceDate = invoiceDate;
		this.products = products;
	}
	
	//Setters and getters methods

	public String getInvoiceCode() {
		return invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public Member getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(Member memberCode) {
		this.memberCode = memberCode;
	}

	public Person getPersonCode() {
		return personCode;
	}

	public void setPersonCode(Person personCode) {
		this.personCode = personCode;
	}

	public String getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public ArrayList<Product> getProducts() {
		return products;
	}

	public void setProduct(Product product) {
		this.products.add(product);
	}

	public double getSubTotal() {				//Get the subtotal for invoice
		double total = 0;						//Initialize total as 0
		for(Product pro : this.products) {		//Loop through products in invoice
			total += pro.getSubTotal();			//Add all the subtotals for each products in invoice
		}
		return total;							//Return the total
	}

	public double getTaxes() {				//Get the total taxes for invoice
		double total = 0;					//Initialize total as 0
		for(Product pro : this.products) {	//Loop through products in invoice
			total += pro.getTax();			//Add all the taxes for each products in invoice
		}
		return total;						//Return the total tax
	}

	public double getTotal() {				//Get the total amount for invoice
		return getSubTotal() + getTaxes();	//Subtotal + tax = total	
	}
	
	public double getDiscounts() {						//Get the total discounts for invoice
		if(this.memberCode instanceof StudentMember) {	//If the member of this invoice is a studentmember..
			return getSubTotal()*this.memberCode.getDiscount()+getTaxes();	//Get the total discount which includes tax
		}			//If not..
		return getSubTotal()*this.memberCode.getDiscount();	//Get the total discount without tax 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((invoiceCode == null) ? 0 : invoiceCode.hashCode());
		result = prime * result + ((invoiceDate == null) ? 0 : invoiceDate.hashCode());
		result = prime * result + ((memberCode == null) ? 0 : memberCode.hashCode());
		result = prime * result + ((personCode == null) ? 0 : personCode.hashCode());
		result = prime * result + ((products == null) ? 0 : products.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Invoice other = (Invoice) obj;
		if (invoiceCode == null) {
			if (other.invoiceCode != null)
				return false;
		} else if (!invoiceCode.equals(other.invoiceCode))
			return false;
		if (invoiceDate == null) {
			if (other.invoiceDate != null)
				return false;
		} else if (!invoiceDate.equals(other.invoiceDate))
			return false;
		if (memberCode == null) {
			if (other.memberCode != null)
				return false;
		} else if (!memberCode.equals(other.memberCode))
			return false;
		if (personCode == null) {
			if (other.personCode != null)
				return false;
		} else if (!personCode.equals(other.personCode))
			return false;
		if (products == null) {
			return other.products == null;
		} else return products.equals(other.products);
	}
	
	public static Comparator<Invoice> getComparator() {
		return Comparator.comparingDouble(Invoice::getTotal);
	}

	@Override
	public String toString() {
		return "Invoice [invoiceCode=" + invoiceCode + ", total=" + this.getTotal() + "]";
	}
	
}
