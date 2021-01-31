package lendingplace.library.request;

import java.io.Serializable;
import java.util.Set;

public class UpdateLendableRequest extends LendableDetailsRequest implements Serializable  {
	private static final long serialVersionUID = 4098775242331137285L;
	private Integer id;
	
	public UpdateLendableRequest() {}

	public UpdateLendableRequest(Integer id, String english, String hindi, String swahili, String arabic,
			String mandarin, String spanish, String french, String imagePath, String creator, 
			int numberAvailable, Set<String> categories) {
		super(english, hindi, swahili, arabic, mandarin, spanish, french, 
				imagePath, creator, numberAvailable, categories);
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
