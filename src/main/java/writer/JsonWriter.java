package writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.Person;
import member.Member;
import product.Product;

public class JsonWriter {
	public JsonWriter() {	//Create a constructor for the writer 
		
	}

	public void toPersonJson(List<Person> persons) {	//Write person json to file

		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();	//Create a gson constructor
		File jsonOutput = new File("data/Persons.json");	//Load the file to output it to

		PrintWriter jsonPrintWriter = null;		//Writer class

		try {
			jsonPrintWriter = new PrintWriter(jsonOutput);	//Initiate the writer class
		} catch (FileNotFoundException e) {
			e.printStackTrace();			//If file not found
		} 
		for(Person aPerson : persons) {
			// Use toJson method to convert Person object into a String
			String personOutput = gson.toJson(aPerson);
			assert jsonPrintWriter != null;
			jsonPrintWriter.write(personOutput + "\n");
		}

		assert jsonPrintWriter != null;
		jsonPrintWriter.close();	//Close the writer
	}

	public void toMemberJson(List<Member> members) {	//Write member json to file

		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();	//Create a gson constructor
		File jsonOutput = new File("data/Members.json");	//Load the file 

		PrintWriter jsonPrintWriter = null;		//Writer class

		try {
			jsonPrintWriter = new PrintWriter(jsonOutput);	//Initiate the writer class
		} catch (FileNotFoundException e) {
			e.printStackTrace();			//If file not found
		} 

		for(Member aMember : members) {
			// Use toJson method to convert Person object into a String
			String memberOutput = gson.toJson(aMember);
			assert jsonPrintWriter != null;
			jsonPrintWriter.write(memberOutput + "\n");
		}

		assert jsonPrintWriter != null;
		jsonPrintWriter.close();	//Close the writer
	}

	public void toProductJson(List<Product> products) {		//Write product json to file

		//Gson gson = new Gson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();	//Create a gson constructor
		File jsonOutput = new File("data/Products.json");	//Load the file

		PrintWriter jsonPrintWriter = null;	//Writer class

		try {
			jsonPrintWriter = new PrintWriter(jsonOutput);	//Initiate the writer class
		} catch (FileNotFoundException e) {
			e.printStackTrace();			//If file not found
		} 

		for(Product aProduct : products) {
			// Use toJson method to convert Person object into a String
			String productOutput = gson.toJson(aProduct);
			assert jsonPrintWriter != null;
			jsonPrintWriter.write(productOutput + "\n");
		}

		assert jsonPrintWriter != null;
		jsonPrintWriter.close();	//Close the writer
	}
}
