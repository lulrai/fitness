package entities;

import java.util.ArrayList;

public class Person {	//Person class 
	private String PID;	//All the variables
	private String fname;
	private String lname;
	private Address address;
	private ArrayList<String> email;
	
	public Person(String pID, String lname, String fname, Address address, ArrayList<String> email) {	//Constructor for Person class
		super();
		PID = pID;
		this.fname = fname;
		this.lname = lname;
		this.address = address;
		this.email = email;
	}
	
	//Setters and getters method
	public String getPID() {
		return PID;
	}
	public void setPID(String pID) {
		PID = pID;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getFullName() {
		return (lname+", "+fname).trim();
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public ArrayList<String> getEmail() {
		return email;
	}
	public void setEmail(ArrayList<String> email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((PID == null) ? 0 : PID.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fname == null) ? 0 : fname.hashCode());
		result = prime * result + ((lname == null) ? 0 : lname.hashCode());
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
		Person other = (Person) obj;
		if (PID == null) {
			if (other.PID != null)
				return false;
		} else if (!PID.equals(other.PID))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fname == null) {
			if (other.fname != null)
				return false;
		} else if (!fname.equals(other.fname))
			return false;
		if (lname == null) {
			return other.lname == null;
		} else return lname.equals(other.lname);
	}
}
