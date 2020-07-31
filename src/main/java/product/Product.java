package product;

public abstract class Product {	//abstract SuperClass for 3 other subclasses

	private String ProductID;	//variable
	private boolean hasExtraInfo;
	private String extraInfo;

	public Product(String productID) {	//Create a constructor
		super();
		ProductID = productID;
	}
	
	public Product(String productID, String extraInfo) {
		super();
		ProductID = productID;
		this.extraInfo = extraInfo;
		this.hasExtraInfo = !extraInfo.isEmpty();
	}

	//Setter and getter methods
	public String getProductID() {
		return ProductID;
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}
	
	public boolean hasExtraInfo() {
		return this.hasExtraInfo;
	}
	
	public String getExtraInfo() {
		return this.extraInfo;
	}
	
	//Abstract method for subclass
	public abstract String productType();
	public abstract double getSubTotal();	
	public abstract double getTax();
	public abstract double grandTotal();
	public abstract int getAmount();
	public abstract double getDiscount();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ProductID == null) ? 0 : ProductID.hashCode());
		result = prime * result + ((extraInfo == null) ? 0 : extraInfo.hashCode());
		result = prime * result + (hasExtraInfo ? 1231 : 1237);
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
		Product other = (Product) obj;
		if (ProductID == null) {
			if (other.ProductID != null)
				return false;
		} else if (!ProductID.equals(other.ProductID))
			return false;
		if (extraInfo == null) {
			if (other.extraInfo != null)
				return false;
		} else if (!extraInfo.equals(other.extraInfo))
			return false;
		return hasExtraInfo == other.hasExtraInfo;
	}
}