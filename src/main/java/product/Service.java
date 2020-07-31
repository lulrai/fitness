package product;

abstract class Service extends Product{

	Service(String productID, String extraInfo) {		//Subclass to Product and Superclass of ParkingPass and Equipment Rental
		super(productID, extraInfo);
	}

}
