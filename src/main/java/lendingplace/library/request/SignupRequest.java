package lendingplace.library.request;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest implements Serializable {
	private static final long serialVersionUID = 4719579055761270014L;
	
	@Size(max = 250)
	private String language;
	
	@NotBlank
	@Size(max = 250)
	private String username;
	
	@NotBlank
	@Size(min = 9, max = 1000)
	private String password;
	
	@NotBlank
	@Size(max = 1000)
	private String email;
	
	private Set<String> roleNames;
	
	public SignupRequest() {}

	public SignupRequest(String language, String username, String password, String email,
			Set<String> roleNames) {
		this.language = language;
		this.username = username;
		this.password = password;
		this.email = email;
		this.roleNames = roleNames;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
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

	public Set<String> getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(Set<String> roleNames) {
		this.roleNames = roleNames;
	}
	
	
}
