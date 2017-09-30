package org.idreamlands.dt.repository.sqlhandle;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class DeleteSqlHandle extends AbstractSqlHandle {

	public DeleteSqlHandle(String table) {
		super(table);
	}

	public static DeleteSqlHandle D(String table) {
		return new DeleteSqlHandle(table);
	}

	public String getSql() {

		if (StringUtils.isBlank(table)) {
			return "";
		}
		StringBuffer sb = new StringBuffer("delete from ");

		sb.append(table + " ");
		sb.append(getwhereString());
		return sb.toString();
	}

	@Override
	public AbstractSqlHandle addValue(Map<String, Object> map) {
		return this;
	}

	@Override
	public String getNamedSql() {
		if (StringUtils.isBlank(table)) {
			return "";
		}
		StringBuffer sb = new StringBuffer("delete from ");

		sb.append(table + " ");
		sb.append(getwhereNamedString());
		return sb.toString();
	}

	@Override
	public String getStaticSql() {
		if (StringUtils.isBlank(table)) {
			return "";
		}
		StringBuffer sb = new StringBuffer("delete from ");

		sb.append(table + " ");
		sb.append(getStaticWhereString());
		return sb.toString();
	}

}