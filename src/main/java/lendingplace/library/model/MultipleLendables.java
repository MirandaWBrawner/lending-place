package lendingplace.library.model;

import java.io.Serializable;

public class MultipleLendables implements Serializable {
	private static final long serialVersionUID = -5951370817215919049L;
	private int id;
	private int count;
	
	public MultipleLendables() {}
	
	public MultipleLendables(int id, int count) {
		super();
		this.id = id;
		this.count = count;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
