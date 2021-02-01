package lendingplace.library.request;

import java.io.Serializable;
import java.util.List;

import lendingplace.library.model.MultipleLendables;

public class CheckoutRequest implements Serializable {
	private static final long serialVersionUID = -8586122264193135398L;
	private Integer memberId;
	private List<MultipleLendables> lendables;
	public CheckoutRequest() {}
	public CheckoutRequest(Integer memberId, List<MultipleLendables> lendables) {
		this.memberId = memberId;
		this.lendables = lendables;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public List<MultipleLendables> getLendables() {
		return lendables;
	}
	public void setLendables(List<MultipleLendables> lendables) {
		this.lendables = lendables;
	}
	
	
}
