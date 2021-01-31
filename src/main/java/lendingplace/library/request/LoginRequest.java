package lendingplace.library.request;

import java.io.Serializable;

public class LoginRequest implements Serializable {
	private static final long serialVersionUID = -7061041779300815444L;
	private String language;
	private String username;
	private String password;
	
	public LoginRequest() {}

	public LoginRequest(String language, String username, String password) {
		this.language = language;
		this.username = username;
		this.password = password;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
