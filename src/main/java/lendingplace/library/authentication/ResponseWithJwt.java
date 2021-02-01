package lendingplace.library.authentication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResponseWithJwt implements Serializable {

	private static final long serialVersionUID = 817764208723849897L;
	private String jwt;
	private String username;
	private String email;
	private String language;
	private List<String> roles;
	private String tokenType = "Bearer";
	
	public ResponseWithJwt(String jwt) {
		this.jwt = jwt;
		roles = new ArrayList<>();
	}

	public ResponseWithJwt(String jwt, String username, String email, String language, List<String> roles) {
		this.jwt = jwt;
		this.username = username;
		this.email = email;
		this.language = language;
		this.roles = roles;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	
}
