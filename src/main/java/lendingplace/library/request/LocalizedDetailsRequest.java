package lendingplace.library.request;

public class LocalizedDetailsRequest {
	private String english;
	private String hindi;
	private String swahili;
	private String arabic;
	private String mandarin;
	private String spanish;
	private String french;
	
	public LocalizedDetailsRequest() {}

	public LocalizedDetailsRequest(String english, String hindi, String swahili, String arabic, String mandarin,
			String spanish, String french) {
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getHindi() {
		return hindi;
	}

	public void setHindi(String hindi) {
		this.hindi = hindi;
	}

	public String getSwahili() {
		return swahili;
	}

	public void setSwahili(String swahili) {
		this.swahili = swahili;
	}

	public String getArabic() {
		return arabic;
	}

	public void setArabic(String arabic) {
		this.arabic = arabic;
	}

	public String getMandarin() {
		return mandarin;
	}

	public void setMandarin(String mandarin) {
		this.mandarin = mandarin;
	}

	public String getSpanish() {
		return spanish;
	}

	public void setSpanish(String spanish) {
		this.spanish = spanish;
	}

	public String getFrench() {
		return french;
	}

	public void setFrench(String french) {
		this.french = french;
	}
	
	
}
