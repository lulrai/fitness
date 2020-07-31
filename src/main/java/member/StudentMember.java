package member;

import entities.Address;
import entities.Person;

public class StudentMember extends Member{		//Subclass of Member, Student Members

	public StudentMember(String memcode, String mtype, Person person, String name, Address address) {		//Constructor for student members
		super(memcode, mtype, person, name, address);
	}

	@Override
	public double getTaxes() {		//Taxes for student member is determined in the invoice and for each product but no tax for students
		return 0;
	}

	@Override
	public double getDiscount() {	//8% discount for student members
		return 0.08;
	}

	@Override
	public double getFees() {		//Extra 10.50$ fee for verification of students 
		return 10.50;
	}

	@Override
	public String getType() {		//Set type as Student
		return "Student";
	}

}
