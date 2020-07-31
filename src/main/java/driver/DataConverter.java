/*
 * Name: Nischal Neupane
 * Date: 15-03-2018
 * Description: A program that reads data files, creates objects for each one and then outputs the invoices that it reads as a formatted output from the database
 */

package driver;

import entities.Invoice;
import entities.List;
import reader.DatabaseReader;
import writer.FlatFileWriter;

public class DataConverter {	
	public static void main(String[] args) {
		DatabaseReader.loadPersonDB();		//Load the persons
		DatabaseReader.loadMemberDB();		//Load the members
		DatabaseReader.loadProductDB();		//Load the products
		DatabaseReader.loadInvoiceDB();		//Load the invoices

		List<Invoice> invoices = new List<>(Invoice.getComparator());	//Created an invoice custom sorting list
		for(Invoice inv : DatabaseReader.invoices) {			//Adding each item from the invoice arraylist to the custom sorted arraylist
			invoices.add(inv);
		}
		
		FlatFileWriter fw = new FlatFileWriter();	//Create a new flatfilewriter object
		fw.outputFormatted(invoices);		//Output the formatted output
	}

}
