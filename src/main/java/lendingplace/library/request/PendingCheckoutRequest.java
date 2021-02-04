package lendingplace.library.request;

import java.io.Serializable;
import java.util.List;

import lendingplace.library.model.MultipleLendables;

public class PendingCheckoutRequest implements Serializable {
	
	private static final long serialVersionUID = 2912986333303262425L;
	private String name;
	private List<MultipleLendables> lendables;
	
	public PendingCheckoutRequest() {}

	public PendingCheckoutRequest(String name, List<MultipleLendables> lendables) {
		this.name = name;
		this.lendables = lendables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MultipleLendables> getLendables() {
		return lendables;
	}

	public void setLendables(List<MultipleLendables> lendables) {
		this.lendables = lendables;
	}
	
	
}
