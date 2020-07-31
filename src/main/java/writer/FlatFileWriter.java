package writer;

import org.joda.time.DateTime;

import entities.Address;
import entities.Invoice;
import entities.List;
import member.Member;
import product.DayMember;
import product.EquipmentRental;
import product.ParkingPass;
import product.Product;
import product.YearLongMember;

public class FlatFileWriter {
	public FlatFileWriter() {		//Instantiates a new object

	}

	public void outputFormatted(List<Invoice> invoices) {	//Formatted Output for the report..
		StringBuilder str = new StringBuilder();	//Create a new stringbuilder object for executive report
		str.append("Executive Summary Report\n"        //Append the titles
				+ "=============================\n").append(String.format("%-10s %-50s %-25s %-13s %-13s %-13s %-13s %-13s\n", "Invoice", "Member", "Personal Trainer", "Subtotal", "Fees", "Taxes", "Discount", "Total"));
		for(Invoice inv : invoices) {				//Loop through each invoice
			double subtotal = inv.getSubTotal();	//Get the subtotal of each invoice
			double taxes = inv.getTaxes();			//Get the taxes of each invoice
			double fees = inv.getMemberCode().getFees();	//Get the extra fees of each invoice
			double discount = inv.getDiscounts();		//Get the discounts of each invoice
			double total = (subtotal + taxes + fees) - discount;	//Total is everything added - discount
			//Append each invoice information in a proper format
			str.append(String.format("%-10s %-50s %-25s $ %-11.2f $ %-11.2f $ %-11.2f -$ %-10.2f $ %-11.2f\n", inv.getInvoiceCode(), inv.getMemberCode().getName() + " ["+inv.getMemberCode().getType()+"]", inv.getPersonCode().getFullName(), subtotal, fees, taxes, discount, total));
		}
		//Line break
		str.append("============================================================================================================================================================\n");
		double subtotal = 0, taxes = 0, fees = 0, discount = 0;    //Initialize all the variables
		for(Invoice inv : invoices) {				//Loop through each invoice
			subtotal += inv.getSubTotal();			//Add up the subtotals of all the invoices
			taxes += inv.getTaxes();				//Add up the taxes of all the invoices
			fees += inv.getMemberCode().getFees();	//Add up the fees of all the invoices
			discount += inv.getDiscounts();			//Add up the discount of all the invoices
		}
		double total = (subtotal + taxes + fees) - discount;    //Add subtotal, taxes, fees and subtract total discount
		//Append the totals of each column
		str.append(String.format("%-87s $ %-11.2f $ %-11.2f $ %-11.2f -$ %-10.2f $ %-11.2f\n", "TOTALS", subtotal, fees, taxes, discount, total));
		System.out.println(str.toString());		//Print out the executive report

		//Each Invoice
		StringBuilder ind = new StringBuilder();	//Create a new stringbuilder for individual reports
		ind.append("Individual Invoice Detail Reports\n")	//Append the titles
		.append("===================================================\n");
		for(Invoice inv : invoices) {						//For each invoices
			ind.append("Invoice ").append(inv.getInvoiceCode()).append("\n");	//Append the invoice code
			ind.append("=======================================\n");	//Line breaker
			ind.append("Personal Trainer: ").append(inv.getPersonCode().getFullName()).append("\n");	//Append the personal trainer name
			Member mem = inv.getMemberCode();	//Get the Member
			Address add = mem.getAddress();		//Get the address of the member
			//Append the memberinfo
			//Appends member id and code
			//Append type of member
			//Append the full name of the member
			//Append the street of the member
			//Append the memberinfo
			//Appends member id and code
			//Append the memberinfo
			ind.append("Member Info: \n")        //Append the memberinfo
					.append("  ").append(mem.getName()).append(" ").append("(").append(mem.getMemcode()).append(")").append("\n")    //Appends member id and code
					.append("  ").append("[").append(mem.getType()).append("]").append("\n")            //Append type of member
					.append("  ").append(mem.getPerson().getFullName()).append("\n")    //Append the full name of the member
					.append("  ").append(add.getStreet()).append("\n")                    //Append the street of the member
					.append("  ").append(add.getCity()).append(" ").append(add.getState()).append(" ").append(add.getZip()).append(" ").append(add.getCountry()).append("\n");	//Append the city, state, zip, county of the member

			ind.append("--------------------------------------------------\n");	//Line breaker
			ind.append(String.format("%-10s %-60s %-15s %-10s %-10s\n", "Code", "Item", "SubTotal", "Tax", "Total"));	//Add the title
			
			for(Product pro : inv.getProducts()) {		//For each product in each invoice..
				if(pro instanceof YearLongMember) {		//If the product is an instance of YearLongMember..
					YearLongMember temp = (YearLongMember) pro; 	//Downcast product
					//Append first line and price infos
					ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Year-long membership '"+temp.getName()+"' @ " + temp.getAddress().getStreet(), pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
					if(new DateTime(temp.getStartdate().replaceAll(" ", "T")).getMonthOfYear() == 1) {
						ind.append(String.format("%-10s %s\n", " ", temp.getStartdate().replaceAll("-", "/")+" - "+temp.getEnddate().replaceAll("-", "/")+ " ("+temp.getAmount()+" units @ "+temp.getPPU()+" with 15% off)"));
					}
					else {
						ind.append(String.format("%-10s %s\n", " ", temp.getStartdate().replaceAll("-", "/")+" - "+temp.getEnddate().replaceAll("-", "/")+ " ("+temp.getAmount()+" units @ "+temp.getPPU()+")"));
					}
				}
				else if(pro instanceof DayMember) {		//If the product is an instance of DayMember..
					DayMember temp = (DayMember) pro; 	//Downcast product
					//Append first line and price infos
					ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Day-long membership @ " + temp.getAddress().getStreet(), pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
					if(new DateTime(temp.getDatetime().replaceAll(" ", "T")).getMonthOfYear() == 1) {
						ind.append(String.format("%-10s %s\n", " ", temp.getDatetime().replaceAll("-", "/") + " ("+temp.getAmount()+" units @ "+temp.getCost()+" with 50% off)"));
					}
					else {
						ind.append(String.format("%-10s %s\n", " ", temp.getDatetime().replaceAll("-", "/") + " ("+temp.getAmount()+" units @ "+temp.getCost()+")"));
					}
				}
				else if(pro instanceof EquipmentRental) {		//If the product is an instance of EquipmentRental..
					EquipmentRental temp = (EquipmentRental) pro; 	//Downcast product
					if(pro.hasExtraInfo()) {
						Product prod = inv.getProducts().parallelStream().filter(p -> p.getProductID().equals(pro.getExtraInfo().trim())).findFirst().orElse(null);
						if(prod instanceof YearLongMember) {
							ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Rental Equipment - " + pro.getExtraInfo().trim() + " - " + temp.getName() , pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
							ind.append(String.format("%-10s %s\n", " ", "("+temp.getAmount()+" units @ "+temp.getCost()+"/unit @ 5% off)"));
						}
						else if(prod != null) {
							ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Rental Equipment - " + pro.getExtraInfo().trim() + " - " + temp.getName() , pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
							ind.append(String.format("%-10s %s\n", " ", "("+temp.getAmount()+" units @ "+temp.getCost()+"/unit)"));
						}
					}
					else {
						ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Rental Equipment - " + temp.getName() , pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
						ind.append(String.format("%-10s %s\n", " ", "("+temp.getAmount()+" units @ "+temp.getCost()+"/unit)"));
					}
				}
				else if(pro instanceof ParkingPass) {			//If the product is an instance of ParkingPass..
					ParkingPass temp = (ParkingPass) pro; 		//Downcast product
					if(pro.hasExtraInfo()) {
						Product prod = inv.getProducts().parallelStream().filter(p -> p.getProductID().equals(pro.getExtraInfo().trim())).findFirst().orElse(null);
						if(prod instanceof YearLongMember) {
							ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Parking Pass " + pro.getExtraInfo().trim() + " ("+temp.getAmount()+" units @ $"+temp.getParkingfee()+" with "+temp.getAmount()+" free)", pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
						}
						else if(prod != null) {
							ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Parking Pass " + pro.getExtraInfo().trim() + " ("+temp.getAmount()+" units @ $"+temp.getParkingfee()+" with 1 free)", pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
						}
					}
					else {
						ind.append(String.format("%-10s %-60s $ %-13.2f $ %-8.2f $ %-8.2f\n", pro.getProductID(), "Parking Pass ("+temp.getAmount()+" units @ $"+temp.getParkingfee()+")", pro.getSubTotal(), pro.getTax(), pro.grandTotal()));
					}
				}
			}
			//Line breaker
			ind.append(String.format("%110s", "=====================================\n"));
			ind.append(String.format("%-71s $ %-13.2f $ %-8.2f $ %-8.2f\n", "SUB-TOTALS", inv.getSubTotal(), inv.getTaxes(), inv.getTotal()));	//Append all the subtotals
			if(inv.getDiscounts() != 0) {	//If discount is not 0..
				ind.append(String.format("%-97s -$ %-9.2f\n", "DISCOUNT", inv.getDiscounts()));	//Append the discounts 
			}
			if(inv.getMemberCode().getFees() != 0) {	//If fee is not 0..
				ind.append(String.format("%-98s $ %-9.2f\n", "FEES", inv.getMemberCode().getFees()));	//Append the fees
			}
			ind.append(String.format("%-98s $ %-9.2f\n", "TOTAL", ((inv.getTotal() + inv.getMemberCode().getFees())-inv.getDiscounts())));	//Append the total

			ind.append("\n\n\t\tThank you for your purchase!\n\n\n");		//Thank them for purchase
		}
		System.out.println(ind.toString());		//Print out the whole report of individual invoices
	}
}
