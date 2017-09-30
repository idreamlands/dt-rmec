package org.idreamlands.dt.repository.sqlhandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class UpdateSqlHandle extends AbstractSqlHandle {

	private List<ColumnValue> values = new ArrayList<ColumnValue>();

	public UpdateSqlHandle() {
	}

	public UpdateSqlHandle(String table) {
		super(table);
	}

	public UpdateSqlHandle(String table, Map<String, Object> map) {
		super(table);
		map.keySet().forEach(p -> values.add(new ColumnValue(p, map.get(p))));
	}

	public static UpdateSqlHandle U(String table, Map<String, Object> map) {
		return new UpdateSqlHandle(table, map);
	}

	public static UpdateSqlHandle U(String table) {
		return new UpdateSqlHandle(table);
	}

	public UpdateSqlHandle addValue(Map<String, Object> map) {

		map.keySet().forEach(p -> values.add(new ColumnValue(p, map.get(p))));
		return this;
	}
	
	public UpdateSqlHandle addValue(String key, Object value) {
		values.add(new ColumnValue(key, value));
		return this;
	}

	public String getSql() {

		if (StringUtils.isBlank(table)) {
			return "";
		}

		StringBuffer sb = new StringBuffer("update ");
		sb.append(table + " set ");

		values.forEach(p -> {
			sb.append(p.getCol());
			if (p.getValue() instanceof CustomerValue) {
				sb.append(" = ( " + ((CustomerValue) p.getValue()).getValue() + " ), ");
			} else {
				sb.append(" = ?, ");
			}

		});

		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}

		sb.append(getwhereString());
		return sb.toString();
	}

	public Object[] getValues() {

		List<Object> valueList = new ArrayList<Object>();

		for (ColumnValue cv : values) {
			if (cv.getValue() instanceof CustomerValue) {
				continue;
			}
			valueList.add(cv.getValue());
		}

		for (Condition c : conditions) {
			if (c.getOpertion() == Opt.Customer) {
				continue;
			}
			Object v = c.getValue();
			if (v.getClass().isArray()) {
				valueList.addAll(Arrays.asList((Object[]) v));
			} else if (v instanceof List) {
				valueList.addAll((List<?>) v);
			} else if (v instanceof CustomerValue) {
				continue;
			} else {
				valueList.add(v);
			}

		}
		return valueList.toArray();
	}

	@Override
	public void clear() {
		super.clear();
		values.clear();
	}

	@Override
	public String getNamedSql() {
		if (StringUtils.isBlank(table)) {
			return "";
		}

		StringBuffer sb = new StringBuffer("update ");
		sb.append(table + " set ");

		values.forEach(p -> {
			sb.append(p.getCol());
			if (p.getValue() instanceof CustomerValue) {
				sb.append(" = ( " + ((CustomerValue) p.getValue()).getValue() + " ), ");
			} else {
				sb.append(" = :" + p.getId() + ", ");
			}

		});

		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}

		sb.append(getwhereNamedString());
		return sb.toString();
	}
	
	public List<Condition> getConditions() {
		List<Condition> list = new ArrayList<Condition>();
		
		values.forEach(p -> {
			Condition c =  Condition.eq(p.getCol(), p.getValue());
			c.setId(p.getId());
			list.add(c);
		});
		list.addAll(conditions);
		
		return list;
	}

	@Override
	public String getStaticSql() {
		if (StringUtils.isBlank(table)) {
			return "";
		}

		StringBuffer sb = new StringBuffer("update ");
		sb.append(table + " set ");

		values.forEach(p -> {
			sb.append(p.getCol());
			if (p.getValue() instanceof CustomerValue) {
				sb.append(" = ( " + ((CustomerValue) p.getValue()).getValue() + " ), ");
			} else if(p.getValue() instanceof String) {
				sb.append(" = '"   + p.getValue() + "', ");
			}
			else {
				sb.append(" = " + p.getValue() + ", ");
			}

		});

		if (sb.toString().endsWith(", ")) {
			sb.delete(sb.length() - 2, sb.length());
		}

		sb.append(getStaticWhereString());
		return sb.toString();
	}

}