package org.idreamlands.dt.repository.sqlhandle;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class Condition {
	
	@Getter
	@Setter
	private String id;

	@Setter
	@Getter
	private String name;

	@Setter
	@Getter
	private Opt opertion;

	@Getter
	private Object value;

	public Condition() {
		super();
	}

	public Condition(Opt opertion, String name, Object value) {
		this.id= UUID.randomUUID().toString().replaceAll("-", "");
		this.opertion = opertion;
		this.name = name;
		this.setValue(value);
	}

	public static Condition eq(String propertyName, Object value) {
		return new Condition(Opt.Equal, propertyName, value);
	}

	public static Condition like(String propertyName, Object value) {
		return new Condition(Opt.Like, propertyName, value);
	}
	
	public static Condition UnEqual(String propertyName, Object value) {
		return new Condition(Opt.UnEqual, propertyName, value);
	}
	
	public static Condition GreatThan(String propertyName, Object value) {
		return new Condition(Opt.GreatThan, propertyName, value);
	}
	
	public static Condition LessThan(String propertyName, Object value) {
		return new Condition(Opt.LessThan, propertyName, value);
	}
	
	public static Condition in(String propertyName, Object value) {
		return new Condition(Opt.In, propertyName, value);
	}
	
	public static Condition notin(String propertyName, Object value) {
		return new Condition(Opt.NotIn, propertyName, value);
	}
	
	public static Condition customer(String propertyName, Object value) {
		return new Condition(Opt.Customer, propertyName, value);
	}
	
	public static Condition gte(String propertyName, Object value) {
		return new Condition(Opt.GreatEqual, propertyName, value);
	}
	
	public static Condition lte(String propertyName, Object value) {
		return new Condition(Opt.LessEqual, propertyName, value);
	}

	public void setValue(Object value) {
		if (this.opertion != null && this.opertion.equals(Opt.Like)) {
			this.value = "%" + value + "%";
		} else {
			this.value = value;
		}
	}
}
