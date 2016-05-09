package javaBean;

import java.io.Serializable;

public class Data implements Serializable{

	private static final long serialVersionUID = 1L;
	String table;
	public Data(String table){
		setTable(table);
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
}