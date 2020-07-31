package writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import com.thoughtworks.xstream.XStream;

import entities.Person;
import member.Member;
import product.Product;

public class XMLFileWriter {
	public XMLFileWriter() {	//Create a constructor for the writer
		
	}

	public void toPersonXML(List<Person> persons) {	//Write person xml to file
		XStream  xstream = new XStream();		//Create a xstream object
		
        File xmlOutput = new File("data/Persons.xml");	//File object with the output file
		
		PrintWriter xmlPrintWriter = null;		//Writer object
		try {
			xmlPrintWriter = new PrintWriter(xmlOutput);	//Initiate writer object with file
		} catch (FileNotFoundException e) {
			e.printStackTrace();		//If file not found
		}

		assert xmlPrintWriter != null;
		xmlPrintWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");	//Encoding
		
		xstream.alias("person", Person.class); 	//Person class to encapsulate
		for(Person aPerson : persons) {			//For each person
			// Use toXML method to convert Person object into a String
			String personOutput = xstream.toXML(aPerson);
			xmlPrintWriter.write(personOutput);
		}
		xmlPrintWriter.close();	//close the writer
	}
	
	public void toMemberXML(List<Member> members) {	//Write member xml to file
		XStream  xstream = new XStream();	//Create a xstream object
		
        File xmlOutput = new File("data/Members.xml");	//File object with the output file
		
		PrintWriter xmlPrintWriter = null;		//Writer object
		try {
			xmlPrintWriter = new PrintWriter(xmlOutput);	//Initiate writer object with file
		} catch (FileNotFoundException e) {
			e.printStackTrace();		//If file not found
		}

		assert xmlPrintWriter != null;
		xmlPrintWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");	//Encoding
		
		xstream.alias("member", Member.class); //Member class to encapsulate
		for(Member aMember : members) {			//For each member
			// Use toXML method to convert Person object into a String
			String memberOutput = xstream.toXML(aMember);
			xmlPrintWriter.write(memberOutput);
		}
		xmlPrintWriter.close();	//close the writer
	}
	
	public void toProductXML(List<Product> products) {	//Write member xml to file
		XStream  xstream = new XStream();	//Create a xstream object
		
        File xmlOutput = new File("data/Products.xml");	//File object with the output file
		
		PrintWriter xmlPrintWriter = null;		//Writer object
		try {
			xmlPrintWriter = new PrintWriter(xmlOutput);	//Initiate writer object with file
		} catch (FileNotFoundException e) {
			e.printStackTrace();		//If file not found
		}

		assert xmlPrintWriter != null;
		xmlPrintWriter.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");	//Encoding
		
		xstream.alias("person", Product.class); //Product class to encapsulate
		for(Product aProduct : products) {
			// Use toXML method to convert Person object into a String
			String productOutput = xstream.toXML(aProduct);
			xmlPrintWriter.write(productOutput);
		}
		xmlPrintWriter.close();		//close the writer
	}
}
