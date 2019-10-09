package DrinkRewards;

public class Customer {
	private String firstName;
	private String lastName;
	private String guestID;
	private float amountSpent;

	public Customer() {
		firstName = "";
		lastName = "";
		guestID = "";
		amountSpent = 0;
	}

	public Customer(String first, String last, String guest, float amount) {
		firstName = first;
		lastName = last;
		guestID = guest;
		amountSpent = amount;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getGuestID() {
		return guestID;
	}

	public float getAmountSpent() {
		return amountSpent;
	}

	public void setFirstName(String s) {
		firstName = s;
	}

	public void setLastName(String s) {
		lastName = s;
	}

	public void setGuestID(String s) {
		guestID = s;
	}

	public void setAmountSpent(float f) {
		amountSpent = f;
	}
}
