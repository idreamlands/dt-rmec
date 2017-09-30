package org.idreamlands.dt.repository.sqlhandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import lombok.Setter;

public class BUpdateSqlHandle extends AbstractSqlHandle {

	@Setter
	private String primaryKey = "id";

	private List<ColumnValue> values = new ArrayList<ColumnValue>();

	// String1: column String2:id Object:value
	private Map<String, Map<Object, Object>> bSaveData = new HashMap<String, Map<Object, Object>>();

	public BUpdateSqlHandle() {
	}

	public BUpdateSqlHandle(String table) {
		super(table);
	}

	public BUpdateSqlHandle(String table, String primaryKey) {
		super(table);
		this.primaryKey = primaryKey;
	}

	public BUpdateSqlHandle(String table, Map<String, Object> map) {
		super(table);
		add(map);
	}

	public BUpdateSqlHandle(String table, String primaryKey, Map<String, Object> map) {
		super(table);
		this.primaryKey = primaryKey;
		add(map);
	}

	public BUpdateSqlHandle addValue(Map<String, Object> map) {
		add(map);
		return this;
	}

	private void add(Map<String, Object> map) {
		if (map.containsKey(primaryKey)) {
			Object id = map.get(primaryKey);

			for (String key : map.keySet()) {
				if (primaryKey.equals(key)) {
					continue;
				}
				if (!bSaveData.containsKey(key)) {
					bSaveData.put(key, new HashMap<Object, Object>());
				}
				bSaveData.get(key).put(id, map.get(key));
			}
		}
	}

	public BUpdateSqlHandle addValue(Object id, String key, Object value) {
		if (primaryKey.equals(key)) {
			return this;
		}

		if (!bSaveData.containsKey(key)) {
			bSaveData.put(key, new HashMap<Object, Object>());
		}
		bSaveData.get(key).put(id, value);
		return this;
	}

	public String getSql() {

		if (StringUtils.isBlank(table)) {
			return "";
		}

		values.clear();
		StringBuffer sbs = new StringBuffer("");
		for (String col : bSaveData.keySet()) {

			StringBuffer sb = new StringBuffer("update ");
			sb.append(table + " set ");

			sb.append(col);
			sb.append(" = ( ");
			sb.append("case " + primaryKey);

			for (Object id : bSaveData.get(col).keySet()) {
				ColumnValue cvid = new ColumnValue(col, id);
				ColumnValue cvValue = new ColumnValue(col, bSaveData.get(col).get(id));
				values.add(cvid);
				values.add(cvValue);

				sb.append(" when :" + cvid.getId() + " then :" + cvValue.getId());
			}

			sb.append(" end )");

			ColumnValue cvids = new ColumnValue(col, bSaveData.get(col).keySet());
			values.add(cvids);

			if (StringUtils.isNotEmpty(getwhereNamedString())) {
				sb.append(getwhereNamedString());
				sb.append(" and " + primaryKey + " in ( :" + cvids.getId() + ")");
			} else {
				sb.append(" where " + primaryKey + " in ( :" + cvids.getId() + " )");
			}

			sbs.append(sb);
			sbs.append(";");
		}

		if (sbs.toString().endsWith(";")) {
			sbs.delete(sbs.length() - 1, sbs.length());
		}

		return sbs.toString();
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
		return getSql();
	}

	public List<Condition> getConditions() {
		List<Condition> list = new ArrayList<Condition>();

		values.forEach(p -> {
			Condition c = Condition.eq(p.getCol(), p.getValue());
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

		values.clear();
		StringBuffer sbs = new StringBuffer("");
		for (String col : bSaveData.keySet()) {

			StringBuffer sb = new StringBuffer("update ");
			sb.append(table + " set ");

			sb.append(col);
			sb.append(" = ( ");
			sb.append("case " + primaryKey);

			for (Object id : bSaveData.get(col).keySet()) {
				ColumnValue cvid = new ColumnValue(col, id);
				ColumnValue cvValue = new ColumnValue(col, bSaveData.get(col).get(id));
				values.add(cvid);
				values.add(cvValue);

				String idValue = id.toString();
				if (id instanceof String) {
					idValue = "'" + idValue + "'";
				}

				String colValue = cvValue.getValue().toString();
				if (id instanceof String) {
					colValue = "'" + colValue + "'";
				}

				sb.append(" when " + idValue + " then " + colValue);
			}

			sb.append(" end )");

			ColumnValue cvids = new ColumnValue(col, bSaveData.get(col).keySet());
			values.add(cvids);

			if (StringUtils.isNotEmpty(getwhereNamedString())) {
				sb.append(getwhereNamedString());
				sb.append(" and " + primaryKey + " in ( ");
			} else {
				sb.append(" where " + primaryKey + " in ( ");
			}

			for (Object id : bSaveData.get(col).keySet()) {
				String s = id instanceof String ? "'" + id + "'" : id.toString();
				sb.append(s + ", ");
			}
			if (sb.toString().endsWith(", ")) {
				sb.delete(sb.length() - 2, sb.length());
			}
			sb.append(")");

			sbs.append(sb);
			sbs.append(";");
		}

		if (sbs.toString().endsWith(";")) {
			sbs.delete(sbs.length() - 1, sbs.length());
		}

		return sbs.toString();
	}

}