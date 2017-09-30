
package org.idreamlands.dt.repository.sqlhandle;

import lombok.Getter;
import lombok.Setter;

public class CustomerValue {
	
	@Getter
	@Setter
	private Object value;
	
	public CustomerValue() {
		
	}
	
	public CustomerValue(Object value) {
		this.value = value;
	}
}
