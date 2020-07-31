package member;

import entities.Address;
import entities.Person;

public class GeneralMember extends Member {		//Subclass of Member, General Members

	public GeneralMember(String memcode, String mtype, Person person, String name, Address address) {		//Constructor for general members
		super(memcode, mtype, person, name, address);
	}

	@Override
	public double getTaxes() {		//Taxes for general member is determined in the invoice and for each product
		return 0;
	}

	@Override
	public double getDiscount() {	//No discounts for general member
		return 0;
	}

	@Override
	public double getFees() {		//No extra fees for general member
		return 0;
	}

	@Override
	public String getType() {		//Set type to general
		return "General";
	}

}
