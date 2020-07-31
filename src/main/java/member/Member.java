package member;

import entities.Address;
import entities.Person;

abstract public class Member {	//A member class
	private String memcode;
	private String mtype;
	private Person person;
	private String name;
	private Address address;
	
	public Member(String memcode, String mtype, Person person, String name, Address address) {	//Create a constructor
		super();
		this.memcode = memcode;
		this.mtype = mtype;
		this.person = person;
		this.name = name;
		this.address = address;
	}
	
	//Setters and Getters
	public String getMemcode() {
		return memcode;
	}
	public void setMemcode(String memcode) {
		this.memcode = memcode;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	//Abstract methods
	public abstract double getTaxes();	
	public abstract double getDiscount();
	public abstract double getFees();
	public abstract String getType();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((memcode == null) ? 0 : memcode.hashCode());
		result = prime * result + ((mtype == null) ? 0 : mtype.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((person == null) ? 0 : person.hashCode());
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
		Member other = (Member) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (memcode == null) {
			if (other.memcode != null)
				return false;
		} else if (!memcode.equals(other.memcode))
			return false;
		if (mtype == null) {
			if (other.mtype != null)
				return false;
		} else if (!mtype.equals(other.mtype))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (person == null) {
			return other.person == null;
		} else return person.equals(other.person);
	}
	
}
