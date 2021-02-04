package lendingplace.library.model;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "items_on_loan")
public class ItemOnLoan {
	
	@Id
	@Column(name = "instance_id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "member_id")
	private CommunityMember member;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lendable_id")
	private Lendable lendable;
	
	@Column(name = "loan_start_date")
	private Timestamp startDate;
	
	@Column(name = "due_date")
	private Timestamp dueDate;
	
	public ItemOnLoan() {}

	public ItemOnLoan(CommunityMember member, Lendable lendable, Timestamp startDate, Timestamp dueDate) {
		this.member = member;
		this.lendable = lendable;
		this.startDate = startDate;
		this.dueDate = dueDate;
	}
	
	public ItemOnLoan(long id, CommunityMember member, Lendable lendable, Timestamp startDate, Timestamp dueDate) {
		this.id = id;
		this.member = member;
		this.lendable = lendable;
		this.startDate = startDate;
		this.dueDate = dueDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CommunityMember getMember() {
		return member;
	}

	public void setMember(CommunityMember member) {
		this.member = member;
	}

	public Lendable getLendable() {
		return lendable;
	}

	public void setLendable(Lendable lendable) {
		this.lendable = lendable;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof ItemOnLoan))
			return false;
		ItemOnLoan other = (ItemOnLoan) obj;
		return id == other.id;
	}
	
	
	
	
}
