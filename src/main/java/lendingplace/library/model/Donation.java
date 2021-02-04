package lendingplace.library.model;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "donation")
public class Donation {
	
	@Id
	@Column(name = "donation_id")
	private int id;
	
	@Column(name = "donor_name")
	private String donorName;
	
	@Column(name = "amount")
	private long amount;
	
	@Column(name = "currency")
	private String currencyCode;
	
	@Column(name = "donation_date")
	private Timestamp date;
	
	public Donation() {}

	public Donation(int id, String donorName, long amount, 
			String currencyCode, Timestamp date) {
		this.id = id;
		this.donorName = donorName;
		this.amount = amount;
		this.currencyCode = currencyCode;
		this.date = date;
	}

	public Donation(String donorName, long amount, String currencyCode, Timestamp date) {
		this.donorName = donorName;
		this.amount = amount;
		this.currencyCode = currencyCode;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Donation))
			return false;
		Donation other = (Donation) obj;
		return id == other.id;
	}
}
