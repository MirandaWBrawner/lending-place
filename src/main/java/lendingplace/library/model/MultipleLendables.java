package lendingplace.library.model;

import java.io.Serializable;

public class MultipleLendables implements Serializable {
	private static final long serialVersionUID = -5951370817215919049L;
	private Integer id;
	private Integer count;
	
	public MultipleLendables() {}
	
	public MultipleLendables(Integer id, Integer count) {
		this.id = id;
		this.count = count;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
