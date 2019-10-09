package DrinkRewards;

public class GoldCustomer extends Customer {
	private float discountPercentage;

	public GoldCustomer() {
		super();
		discountPercentage = 0;
	}

	public GoldCustomer(String f, String l, String i, float a, float d) {
		super(f, l, i, a);
		discountPercentage = d;
	}

	public void setDiscountPercentage(float d) {
		discountPercentage = d;
	}

	public float getDiscountPercentage() {
		return discountPercentage;
	}
}
