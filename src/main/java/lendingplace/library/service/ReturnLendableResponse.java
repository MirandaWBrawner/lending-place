package lendingplace.library.service;

import java.io.Serializable;
import java.util.Set;

public class ReturnLendableResponse implements Serializable {

	private static final long serialVersionUID = 7892923341253709937L;
	private int memberId;
	private Set<Integer> lendablesNotReturned;
	
	public ReturnLendableResponse() {}

	public ReturnLendableResponse(int memberId, Set<Integer> lendablesNotReturned) {
		this.memberId = memberId;
		this.lendablesNotReturned = lendablesNotReturned;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public Set<Integer> getLendablesNotReturned() {
		return lendablesNotReturned;
	}

	public void setLendablesNotReturned(Set<Integer> lendablesNotReturned) {
		this.lendablesNotReturned = lendablesNotReturned;
	}
}
