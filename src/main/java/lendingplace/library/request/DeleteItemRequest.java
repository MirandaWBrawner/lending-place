package lendingplace.library.request;

import java.io.Serializable;

public class DeleteItemRequest implements Serializable {
	private static final long serialVersionUID = 8606402571197455438L;
	private Integer id;
	public DeleteItemRequest() {}
	public DeleteItemRequest(Integer id) {
		this.id = id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
