package org.idreamlands.dt.repository.sqlhandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

public class QuerySqlHandle extends AbstractSqlHandle {

	private List<String> fields = new ArrayList<String>();
	
	private boolean lock = false;
	
	private List<String> orders = new ArrayList<String>();

	public QuerySqlHandle(String table) {
		super(table);
	}
	
	public static QuerySqlHandle Q(String table) {
		return new QuerySqlHandle(table);
	}
	
	public QuerySqlHandle addField(String... field) {
		Stream.of(field).forEach(p -> fields.add(p));
		return this;
	}
	
	public QuerySqlHandle lock() {
		lock = true;
		return this;
	}
	
	public QuerySqlHandle sort(String... order) {
		orders.addAll(Arrays.asList(order));
		return this;
	}
	
	

	public String getSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		if (fields.size() > 0) {
			fields.forEach(p -> sb.append(p + ","));
			sb.delete(sb.length() - 1, sb.length());
		} else {
			sb.append("* ");
		}

		sb.append(" from ");

		if (StringUtils.isBlank(table)) {
			return "";
		}

		sb.append(table + " ");
		sb.append(getwhereString());
		
		if(orders.size() > 0) {
			sb.append(" order by ");
			for(String order : orders) {
				sb.append(order + ", ");
			}
			sb.delete(sb.length() - 2, sb.length());
		}
		
		if(lock) {
			sb.append(" for update");
		}
		return sb.toString();
	}

	@Override
	public AbstractSqlHandle addValue(Map<String, Object> map) {
		return this;
	}
	
	@Override 
	public void clear() {
		super.clear();
		fields.clear();
		lock = false;
		orders.clear();
	}

	@Override
	public String getNamedSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		if (fields.size() > 0) {
			fields.forEach(p -> sb.append(p + ","));
			sb.delete(sb.length() - 1, sb.length());
		} else {
			sb.append("* ");
		}

		sb.append(" from ");

		if (StringUtils.isBlank(table)) {
			return "";
		}

		sb.append(table + " ");
		sb.append(getwhereNamedString());
		
		if(orders.size() > 0) {
			sb.append(" order by ");
			for(String order : orders) {
				sb.append(order + ", ");
			}
			sb.delete(sb.length() - 2, sb.length());
		}
		
		if(lock) {
			sb.append(" for update");
		}
		return sb.toString();
	}

	@Override
	public String getStaticSql() {
		return "";
	}

	public String getFirstField() {
		if(fields.size() > 0) {
			return fields.get(0);
		}
		return "";
	}
	
	public String getSecondField() {
		if(fields.size() > 1) {
			return fields.get(1);
		}
		return "";
	}
	
	public int geFieldSize() {
		return fields.size();
	}

}