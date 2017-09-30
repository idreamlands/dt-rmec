package org.idreamlands.dt.repository.sqlhandle;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class ColumnValue {
	
	@Getter
	private String id;
	
	@Setter
	@Getter
	private String col;

	@Getter
	@Setter
	private Object value;
	
	public ColumnValue(String col, Object value) {
		this.id = UUID.randomUUID().toString().replaceAll("-", "");
		this.col = col;
		this.value = value;
	}
}
