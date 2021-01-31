package lendingplace.library.request;

import java.io.Serializable;

public class CategoryDetailsRequest extends LocalizedDetailsRequest implements Serializable {
	
	private static final long serialVersionUID = 6629428480850046082L;
	
	private String imagePath;
	
	public CategoryDetailsRequest() {}

	public CategoryDetailsRequest(String english, String hindi, String swahili, String arabic, String mandarin,
			String spanish, String french, String imagePath) {
		super(english, hindi, swahili, arabic, mandarin, spanish, french);
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	
}
