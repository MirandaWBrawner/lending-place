package lendingplace.library.request;

import java.io.Serializable;
import java.util.Set;

public class LendableDetailsRequest extends LocalizedDetailsRequest implements Serializable {

	private static final long serialVersionUID = -3757079420694346461L;
	
	private String imagePath;
	private String creator;
	private int numberAvailable;
	private Set<String> categories;
	
	public LendableDetailsRequest() {}

	public LendableDetailsRequest(String english, String hindi, String swahili, String arabic, String mandarin,
			String spanish, String french, String imagePath, String creator, int numberAvailable,
			Set<String> categories) {
		super(english, hindi, swahili, arabic, mandarin, spanish, french);
		this.imagePath = imagePath;
		this.creator = creator;
		this.numberAvailable = numberAvailable;
		this.categories = categories;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getNumberAvailable() {
		return numberAvailable;
	}

	public void setNumberAvailable(int numberAvailable) {
		this.numberAvailable = numberAvailable;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	
	

}
