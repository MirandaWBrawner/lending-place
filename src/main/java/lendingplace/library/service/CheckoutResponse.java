package lendingplace.library.service;

import java.io.Serializable;
import java.util.List;

public class CheckoutResponse implements Serializable {
	
	private static final long serialVersionUID = 5057042963242235602L;
	private List<Integer> checkedOutList;
	private List<Integer> notFoundList;
	
	public CheckoutResponse() {}

	public CheckoutResponse(List<Integer> checkedOutList, List<Integer> notFoundList) {
		this.checkedOutList = checkedOutList;
		this.notFoundList = notFoundList;
	}

	public List<Integer> getCheckedOutList() {
		return checkedOutList;
	}

	public void setCheckedOutList(List<Integer> checkedOutList) {
		this.checkedOutList = checkedOutList;
	}

	public List<Integer> getNotFoundList() {
		return notFoundList;
	}

	public void setNotFoundList(List<Integer> notFoundList) {
		this.notFoundList = notFoundList;
	}
	
	
}
