package lendingplace.library.service;

import java.io.Serializable;

public class RecommendationResponse implements Serializable {

	private static final long serialVersionUID = -2621708734745925432L;
	private String message;
	
	public RecommendationResponse() {}
	public RecommendationResponse(String message) {
		this.message = message;
	}
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
