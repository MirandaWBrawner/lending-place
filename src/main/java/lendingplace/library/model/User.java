package lendingplace.library.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "library_user", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class User {
	
	@Id
	@Column(name = "user_id")
	private int id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "email")
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role_join_table", joinColumns = @JoinColumn(name = "user_id"),
	inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<UserRole> roles;
	
	public User() {}

	public User(int id, String username, String password, String email, Set<UserRole> roles) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
	}

	public User(String username, String password, String email, Set<UserRole> roles) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.roles = roles;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		return id == other.id;
	}

	
	
}
