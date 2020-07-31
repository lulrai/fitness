package product;

abstract class Membership extends Product{
	Membership(String productID, String extraInfo) {		//Subclass of Product and superclass of YearLongMember and DayMember
		super(productID, extraInfo);
	}
}
