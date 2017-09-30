package org.idreamlands.dt.repository;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class TableMetadata {

	@Getter
	@Setter
	private String columnName;

	@Getter
	@Setter
	private String tableName;
	
	@Getter
	@Setter
	private String dataType;

	@SuppressWarnings("serial")
	public static class TableMetadataKey implements Serializable {
		String columnName;
		String tableName;
	}
	
	public TableMetadata() {
		
	}
	
	public TableMetadata(String tableName, String columnName, String dataType) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.dataType = dataType;
	}
}
