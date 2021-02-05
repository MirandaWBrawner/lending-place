package lendingplace.library.request;

import java.io.Serializable;

public class RecommendationRequest implements Serializable {

	private static final long serialVersionUID = -4138782949619167505L;
	private String subject;
	private String fullText;
	
	public RecommendationRequest() {}

	public RecommendationRequest(String subject, String fullText) {
		this.subject = subject;
		this.fullText = fullText;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFullText() {
		return fullText;
	}

	public void setFullText(String fullText) {
		this.fullText = fullText;
	}
	
	
	
}
