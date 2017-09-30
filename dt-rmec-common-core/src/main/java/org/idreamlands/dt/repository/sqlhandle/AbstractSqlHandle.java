package org.idreamlands.dt.repository.sqlhandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractSqlHandle {

	@Setter 
	@Getter
	protected String table = "";

	@Setter
	protected StringBuffer sql = new StringBuffer();

	protected List<Condition> conditions = new ArrayList<Condition>();

	@SuppressWarnings("serial")
	private static Map<Opt, String> opts = new HashMap<Opt, String>() {
		{
			put(Opt.Equal, " = ");
			put(Opt.UnEqual, " <> ");
			put(Opt.GreatThan, " > ");
			put(Opt.LessThan, " < ");
			put(Opt.Like, " like ");
			put(Opt.In, " in ");
			put(Opt.GreatEqual, " >= ");
			put(Opt.LessEqual, " <= ");
			put(Opt.NotIn, " not in ");
		}
	};

	public AbstractSqlHandle() {
	}

	public AbstractSqlHandle(String table) {
		this.table = table;
	}

	public AbstractSqlHandle addConditon(Condition condition) {
		conditions.add(condition);
		return this;
	}

	public AbstractSqlHandle addConditon(Map<String, Object> condition) {
		condition.keySet().forEach(p -> conditions.add(Condition.eq(p, condition.get(p))));
		return this;
	}

	public abstract String getSql();

	public abstract String getNamedSql();
	
	public abstract String getStaticSql();

	public abstract AbstractSqlHandle addValue(Map<String, Object> map);

	public String getwhereString() {
		StringBuffer sb = new StringBuffer();
		if (conditions.size() > 0) {
			sb.append(" where 1=1 and");
			for (Condition item : conditions) {
				Object v = item.getValue();
				if (item.getOpertion() == Opt.Customer) {
					sb.append(" " + v + " and");
				} else {
					sb.append(" " + item.getName());
					sb.append(opts.get(item.getOpertion()));

					if (v instanceof CustomerValue) {
						sb.append("( " + ((CustomerValue)v).getValue() + " ) and");
						continue;
					}

					if (item.getOpertion() == Opt.In || item.getOpertion() == Opt.NotIn) {
						getWhereInString(item, sb);
					} else {
						sb.append("? and");
					}
				}
			}
			if (sb.toString().endsWith("and")) {
				sb.delete(sb.length() - 3, sb.length());
			}
		}
		return sb.toString();
	}
	
	public String getStaticWhereString() {
		StringBuffer sb = new StringBuffer();
		if (conditions.size() > 0) {
			sb.append(" where 1=1 and");
			for (Condition item : conditions) {
				Object v = item.getValue();
				if (item.getOpertion() == Opt.Customer) {
					sb.append(" " + v + " and");
				} else {
					sb.append(" " + item.getName());
					sb.append(opts.get(item.getOpertion()));

					if (v instanceof CustomerValue) {
						sb.append("( " + ((CustomerValue)v).getValue() + " ) and");
						continue;
					}

					if (item.getOpertion() == Opt.In || item.getOpertion() == Opt.NotIn) {
						getStaticWhereInString(item, sb);
					} else {
						if(v instanceof String) {
							sb.append("'" + v + "' and");
						} else {
							sb.append("" + v + " and");
						}
					}
				}
			}
			if (sb.toString().endsWith("and")) {
				sb.delete(sb.length() - 3, sb.length());
			}
		}
		return sb.toString();
	}
	
	private void getStaticWhereInString(Condition item, StringBuffer sb) {
		sb.append("( ");
		
		List<Object> vl = new ArrayList<Object>();
		
		if (item.getValue().getClass().isArray()) {
			vl.addAll(Arrays.asList((Object[])item.getValue()));
		} else if (item.getValue() instanceof List) {
			vl.addAll((List<?>) item.getValue());
		}
		
		for(Object obj : vl) {
			if(obj instanceof String) {
				sb.append("'" + obj + "', ");
			} else {
				sb.append("" + obj + ", ");
			}
		}
		
		sb.delete(sb.length() - 1, sb.length());

		sb.append(") and");
	}

	private void getWhereInString(Condition item, StringBuffer sb) {
		sb.append("( ");
		int size = 1;
		if (item.getValue().getClass().isArray()) {
			size = ((Object[]) item.getValue()).length;
		} else if (item.getValue() instanceof List) {
			size = ((List<?>) item.getValue()).size();
		}

		for (int i = 0; i < size; i++) {
			sb.append("?,");
		}
		sb.delete(sb.length() - 1, sb.length());

		sb.append(") and");
	}

	public String getwhereNamedString() {
		StringBuffer sb = new StringBuffer();
		if (conditions.size() > 0) {
			sb.append(" where 1=1 and");
			for (Condition item : conditions) {

				Object v = item.getValue();
				if (item.getOpertion() == Opt.Customer) {
					sb.append(" " + v + " and");
				} else {
					sb.append(" " + item.getName());
					sb.append(opts.get(item.getOpertion()));

					if (v instanceof CustomerValue) {
						sb.append("( " + ((CustomerValue)v).getValue() + " ) and");
						continue;
					}

					if (item.getOpertion() == Opt.In || item.getOpertion() == Opt.NotIn) {
						sb.append("(:" + item.getId() + ") and");
					} else {
						sb.append(":" + item.getId() + " and");
					}
				}
			}
			if (sb.toString().endsWith("and")) {
				sb.delete(sb.length() - 3, sb.length());
			}
		}
		return sb.toString();
	}

	public String getwhereOrString() {
		String result = this.getwhereString();
		return result.replace("and", "or");
	}

	public Object[] getValues() {

		List<Object> valueList = new ArrayList<Object>();

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

	public void clear() {
		conditions.clear();
	}

	public List<Condition> getConditions() {
		return conditions;
	}

}