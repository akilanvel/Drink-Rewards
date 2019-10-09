package DrinkRewards;

public class PlatinumCustomer extends Customer {
	private float bonusBucks;

	public PlatinumCustomer() {
		super();
		bonusBucks = 0;
	}

	public PlatinumCustomer(String f, String l, String i, float a, float b) {
		super(f, l, i, a);
		bonusBucks = b;
	}

	public float getBonusBucks() {
		return bonusBucks;
	}

	public void setBonusBucks(float b) {
		bonusBucks = b;
	}
}
