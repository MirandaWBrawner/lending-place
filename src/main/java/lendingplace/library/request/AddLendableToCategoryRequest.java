package lendingplace.library.request;

import java.io.Serializable;

public class AddLendableToCategoryRequest implements Serializable {

	private static final long serialVersionUID = 4359350296988870417L;

	private Integer lendableId;
	private Integer categoryId;
	
	public AddLendableToCategoryRequest() {}
	
	public AddLendableToCategoryRequest(Integer lendableId, Integer categoryId) {
		this.lendableId = lendableId;
		this.categoryId = categoryId;
	}

	public Integer getLendableId() {
		return lendableId;
	}

	public void setLendableId(Integer lendableId) {
		this.lendableId = lendableId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	
}
