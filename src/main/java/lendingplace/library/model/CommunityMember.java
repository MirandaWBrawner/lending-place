package lendingplace.library.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "community_member")
public class CommunityMember {
	
	@Id
	@Column(name = "member_id")
	private int id;
	
	@Column(name = "member_name")
	private String name;
	
	@Column(name = "member_email")
	private String email;
	
	@Column(name = "member_phone")
	private String phone;
	
	public CommunityMember() {}

	public CommunityMember(int id, String name, String email, String phone) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public CommunityMember(String name, String email, String phone) {
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CommunityMember))
			return false;
		CommunityMember other = (CommunityMember) obj;
		return id == other.id;
	}
	
	

}
