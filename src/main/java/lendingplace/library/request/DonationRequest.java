package lendingplace.library.request;

import java.io.Serializable;

public class DonationRequest implements Serializable {

	private static final long serialVersionUID = -7777763877754138218L;
	private String donorName;
	private long amount;
	private String cardNumber;
	private String email;
	private String phone;
	private String mailingAddress;
	private String currency;
	
	public DonationRequest() {}

	public DonationRequest(String donorName, long amount, String cardNumber, String email, String phone,
			String mailingAddress, String currency) {
		this.donorName = donorName;
		this.amount = amount;
		this.cardNumber = cardNumber;
		this.email = email;
		this.phone = phone;
		this.mailingAddress = mailingAddress;
		this.currency = currency;
	}

	public String getDonorName() {
		return donorName;
	}

	public void setDonorName(String donorName) {
		this.donorName = donorName;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
