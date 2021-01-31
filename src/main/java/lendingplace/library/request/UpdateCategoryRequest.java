package lendingplace.library.request;

import java.io.Serializable;

public class UpdateCategoryRequest extends CategoryDetailsRequest implements Serializable {

	private static final long serialVersionUID = -2293817191895498707L;
	private Integer id;
	
	public UpdateCategoryRequest() {}
	
	public UpdateCategoryRequest(Integer id, String english, String hindi, String swahili, 
			String arabic, String mandarin, String spanish, String french, String imagePath) {
		super(english, hindi, swahili, arabic, mandarin, spanish, french, imagePath);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
