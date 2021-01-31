package lendingplace.library.service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lendingplace.library.model.User;

public class LibrarianDetails implements UserDetails {

	private static final long serialVersionUID = 7269626009319888431L;
	
	private int id;
	
	private String username;
	
	@JsonIgnore
	private String password;
	
	private String email;
	
	private Collection<? extends GrantedAuthority> authorities;

	public LibrarianDetails(int id, String username, String password, String email,
			Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.authorities = authorities;
	}
	
	public static LibrarianDetails build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().parallelStream()
				.flatMap(role -> role.getNameSet().parallelStream())
				.map(name -> new SimpleGrantedAuthority(name))
				.collect(Collectors.toList());
		return new LibrarianDetails(user.getId(), user.getUsername(),
				user.getPassword(), user.getEmail(), authorities);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof LibrarianDetails))
			return false;
		LibrarianDetails other = (LibrarianDetails) obj;
		return id == other.id;
	}
	
	

}
