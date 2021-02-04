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
@Table(name = "pending_loan")
public class PendingLoan {
	
	@Id
	@Column(name = "pending_id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "lendable_id")
	private Lendable lendable;
	
	@Column(name = "count")
	private int count;
	
	@Column(name = "requester_name")
	private String name;
	
	@Column(name = "date_posted")
	private Timestamp datePosted;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "member_id")
	private CommunityMember member;
	
	// private transient long milliseconds;
	
	public PendingLoan() {
		// milliseconds = System.currentTimeMillis();
	}
	
	public PendingLoan(Lendable lendable, int count, String name, Timestamp datePosted) {
		this.lendable = lendable;
		this.count = count;
		this.name = name;
		this.datePosted = datePosted;
		// milliseconds = datePosted.getTime();
	}

	public PendingLoan(long id, Lendable lendable, int count, String name, Timestamp datePosted) {
		this.id = id;
		this.lendable = lendable;
		this.count = count;
		this.name = name;
		this.datePosted = datePosted;
		// milliseconds = datePosted.getTime();
	}

	public PendingLoan(Lendable lendable, int count, String name, Timestamp datePosted, CommunityMember member) {
		this.lendable = lendable;
		this.count = count;
		this.name = name;
		this.datePosted = datePosted;
		this.member = member;
		// milliseconds = datePosted.getTime();
	}

	public PendingLoan(long id, Lendable lendable, int count, String name, Timestamp datePosted,
			CommunityMember member) {
		this.id = id;
		this.lendable = lendable;
		this.count = count;
		this.name = name;
		this.datePosted = datePosted;
		this.member = member;
		// milliseconds = datePosted.getTime();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Lendable getLendable() {
		return lendable;
	}

	public void setLendable(Lendable lendable) {
		this.lendable = lendable;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getDatePosted() {
		return datePosted;
	}

	public void setDatePosted(Timestamp datePosted) {
		this.datePosted = datePosted;
	}

	public CommunityMember getMember() {
		return member;
	}

	public void setMember(CommunityMember member) {
		this.member = member;
	}

	/*
	public long getMilliseconds() {
		milliseconds = datePosted.getTime();
		return milliseconds;
	}

	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	*/

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PendingLoan))
			return false;
		PendingLoan other = (PendingLoan) obj;
		return id == other.id;
	}
}
