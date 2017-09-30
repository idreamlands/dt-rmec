package org.idreamlands.dt.repository.sqlhandle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class InsertSqlHandle extends AbstractSqlHandle {

	private List<Map<String, Object>> values = new ArrayList<Map<String, Object>>();

	public InsertSqlHandle(String table) {
		super(table);
	}

	public InsertSqlHandle(String table, Map<String, Object> map) {
		this(table);
		values.add(map);
	}

	public InsertSqlHandle addValue(Map<String, Object> map) {

		values.add(map);
		return this;
	}

	public InsertSqlHandle(String table, List<Map<String, Object>> list) {
		this(table);
		values.addAll(list);
	}

	public InsertSqlHandle addValue(List<Map<String, Object>> list) {

		values.addAll(list);
		return this;
	}

	public static InsertSqlHandle I(String table, Map<String, Object> map) {
		return new InsertSqlHandle(table, map);
	}

	public static InsertSqlHandle I(String table) {
		return new InsertSqlHandle(table);
	}

	@Override
	public String getSql() {

		if (StringUtils.isBlank(table) || values.size() == 0) {
			return "";
		}

		List<String> cols = new ArrayList<String>();
		cols.addAll(values.get(0).keySet());
		Collections.sort(cols); 

		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(table + " ");

		sb.append("(");

		cols.forEach(p -> sb.append(p + ", "));
		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}
		sb.append(") ");

		sb.append("values ");

		for (int i = 0; i < values.size(); i++) {
			sb.append("( ");
			for (int j = 0; j < cols.size(); j++) {
				sb.append("?, ");
			}
			if (sb.toString().endsWith(", ")) {
				sb.delete(sb.length() - 2, sb.length());
			}
			sb.append("), ");
		}

		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}

		return sb.toString();
	}

	@Override
	public Object[] getValues() {
		
		if(values.size() == 0) {
			return null;
		}

		List<String> cols = new ArrayList<String>();
		cols.addAll(values.get(0).keySet());
		Collections.sort(cols); 
		Object[] obj = new Object[cols.size() * values.size()];
		
		int i = 0;
		for (Map<String, Object> m : values) {
			for (String col : cols) {
				if (m.containsKey(col)) {
					obj[i] = m.get(col);
				} else {
					obj[i] = null;
				}
				i++;
			}
		}

		return obj;
	}

	@Override
	public void clear() {
		super.clear();
		values.clear();
	}

	@Override
	public String getNamedSql() {
		return null;
	}

	@Override
	public String getStaticSql() {
		if (StringUtils.isBlank(table) || values.size() == 0) {
			return "";
		}

		List<String> cols = new ArrayList<String>();
		cols.addAll(values.get(0).keySet());
		Collections.sort(cols); 

		StringBuffer sb = new StringBuffer();
		sb.append("insert into ");
		sb.append(table + " ");

		sb.append("(");

		cols.forEach(p -> sb.append(p + ", "));
		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}
		sb.append(") ");

		sb.append("values ");

		int vIndex = 0;
		Object[] vs = getValues();
		for (int i = 0; i < values.size(); i++) {
			sb.append("( ");
			for (int j = 0; j < cols.size(); j++) {
				Object v = vs[vIndex];
				if(v instanceof String) {
					sb.append("'" + v + "', ");
				} else {
					sb.append("'" + v + "', ");
				}
				vIndex ++;
			}
			if (sb.toString().endsWith(", ")) {
				sb.delete(sb.length() - 2, sb.length());
			}
			sb.append("), ");
		}

		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}

		return sb.toString();
	}
	
	public List<Map<String, Object>> getValuesList() {
		return this.values;
	}
}