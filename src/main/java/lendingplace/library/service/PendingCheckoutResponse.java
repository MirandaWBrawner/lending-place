package lendingplace.library.service;

import java.io.Serializable;
import java.util.List;

public class PendingCheckoutResponse implements Serializable {

	private static final long serialVersionUID = 3047273636995676751L;
	private List<Integer> acceptedList;
	private List<Integer> notFoundList;
	
	public PendingCheckoutResponse() {}

	public PendingCheckoutResponse(List<Integer> acceptedList, List<Integer> notFoundList) {
		this.acceptedList = acceptedList;
		this.notFoundList = notFoundList;
	}

	public List<Integer> getAcceptedList() {
		return acceptedList;
	}

	public void setAcceptedList(List<Integer> acceptedList) {
		this.acceptedList = acceptedList;
	}

	public List<Integer> getNotFoundList() {
		return notFoundList;
	}

	public void setNotFoundList(List<Integer> notFoundList) {
		this.notFoundList = notFoundList;
	}
	
	
}
