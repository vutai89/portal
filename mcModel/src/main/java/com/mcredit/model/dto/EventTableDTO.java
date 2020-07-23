package com.mcredit.model.dto;

import java.util.ArrayList;

public class EventTableDTO {

	private String table;
	private ArrayList<String> rowIds;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public ArrayList<String> getRowIds() {
		return rowIds;
	}

	public void setRowIds(ArrayList<String> rowIds) {
		this.rowIds = rowIds;
	}
}
