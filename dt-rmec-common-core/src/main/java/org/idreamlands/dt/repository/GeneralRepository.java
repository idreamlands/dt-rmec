package org.idreamlands.dt.repository;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.SQLQuery;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.transform.Transformers;
import org.idreamlands.dt.repository.sqlhandle.AbstractSqlHandle;
import org.idreamlands.dt.repository.sqlhandle.Condition;
import org.idreamlands.dt.repository.sqlhandle.CustomerValue;
import org.idreamlands.dt.repository.sqlhandle.InsertSqlHandle;
import org.idreamlands.dt.repository.sqlhandle.Opt;
import org.springframework.stereotype.Component;


/**
 * 
 * @author idreamlands
 *
 */
@Component
public class GeneralRepository {

	@PersistenceContext
	private EntityManager entityManager;

	public List<?> query(String sql) {
		return this.query(sql, new Object[] {});
	}

	public List<?> query(String sql, Object[] where) {

		Query query = entityManager.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (where != null) {
			for (int i = 0; i < where.length; i++) {
				query.setParameter(i + 1, where[i]);
			}
		}
		return query.getResultList();
	}

	public List<?> query(AbstractSqlHandle sqlHandle) {
		return this.query(sqlHandle.getNamedSql(), sqlHandle.getConditions());
	}

	

	public List<?> queryUsePosition(AbstractSqlHandle sqlHandle) {
		return this.query(sqlHandle.getSql(), sqlHandle.getValues());
	}

	public Map<String, Object> queryFirstOne(String sql) {
		return this.queryFirstOne(sql, new Object[] {});
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryFirstOne(String sql, Object[] where) {
		List<?> list = queryPage(sql, where, 1, 1);
		if (list.size() == 1) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	public Map<String, Object> queryFirstOne(AbstractSqlHandle sqlHandle) {
		return this.queryFirstOne(sqlHandle.getNamedSql(), sqlHandle.getConditions());
	}

	public List<?> queryPage(String sql, int size, int page) {
		return this.queryPage(sql, new Object[] {}, size, page);
	}

	public List<?> queryPage(String sql, Object[] where, int size, int page) {

		List<Object> vList = new ArrayList<Object>();
		vList.addAll(Arrays.asList(where));

		if (sql.endsWith("for update")) {
			sql = sql.substring(0, sql.length() - 10);
			sql = sql + " limit ?, ? for update";

			vList.add((page - 1) * size);
			vList.add(size);
		}

		Query query = entityManager.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		if (sql.indexOf("limit") < 0) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}

		if (vList.size() > 0) {
			for (int i = 0; i < vList.size(); i++) {
				query.setParameter(i + 1, vList.get(i));
			}
		}

		return query.getResultList();
	}

	public List<?> queryPage(AbstractSqlHandle sqlHandle, int size, int page) {
		return this.queryPage(sqlHandle.getNamedSql(), sqlHandle.getConditions(), size, page);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryOne(String sql) {
		List<?> list = this.query(sql, new Object[] {});
		if (list.size() == 1) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryOne(AbstractSqlHandle sqlHandle) {
		List<?> list = this.query(sqlHandle.getSql(), sqlHandle.getValues());
		if (list.size() == 1) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryOne(String sql, Object[] values) {
		List<?> list = this.query(sql, values);
		if (list.size() == 1) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	public Object querySingleResult(AbstractSqlHandle sqlHandle) {
		return this.querySingleResult(sqlHandle.getSql(), sqlHandle.getValues());
	}

	public Object querySingleResult(String sql, Object[] where) {

		Query query = entityManager.createNativeQuery(sql);
		if (where != null) {
			for (int i = 0; i < where.length; i++) {
				query.setParameter(i + 1, where[i]);
			}
		}
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> queryFirstOne(String sql, List<Condition> conditions) {
		List<?> list = queryPage(sql, conditions, 1, 1);
		if (list.size() == 1) {
			return (Map<String, Object>) list.get(0);
		}
		return null;
	}

	public List<?> queryPage(String sql, final List<Condition> conditions, int size, int page) {

		List<Condition> conditionsN = new ArrayList<Condition>();
		conditionsN.addAll(conditions);

		if (sql.endsWith("for update")) {

			Condition start = Condition.eq("start", (page - 1) * size);
			Condition end = Condition.eq("end", size);
			conditionsN.add(start);
			conditionsN.add(end);

			sql = sql.substring(0, sql.length() - 10);
			sql = sql + " limit :" + start.getId() + ", :" + end.getId() + " for update";
		}

		Query query = entityManager.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		if (sql.indexOf("limit") < 0) {
			query.setFirstResult((page - 1) * size);
			query.setMaxResults(size);
		}

		for (Condition c : conditionsN) {
			if (c.getOpertion() == Opt.Customer) {
				continue;
			}
			Object v = c.getValue();
			if (v.getClass().isArray()) {
				query.setParameter(c.getId(), Arrays.asList((Object[]) v));
			} else if (v instanceof CustomerValue) {
				continue;
			} else {
				query.setParameter(c.getId(), c.getValue());
			}
		}

		return query.getResultList();
	}

	public List<?> query(String sql, List<Condition> conditions) {

		Query query = entityManager.createNativeQuery(sql);
		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

		for (Condition c : conditions) {
			if (c.getOpertion() == Opt.Customer) {
				continue;
			}
			Object v = c.getValue();
			if (v.getClass().isArray()) {
				query.setParameter(c.getId(), Arrays.asList((Object[]) v));
			} else if (v instanceof CustomerValue) {
				continue;
			} else {
				query.setParameter(c.getId(), c.getValue());
			}
		}

		return query.getResultList();
	}

	public int add(String sql) {
		return this.executeUpdate(sql, null, null);
	}

	public int add(String sql, Object[] values) {
		return this.executeUpdate(sql, values, null);
	}

	// 插入之前删除表中不存在的数据
	public int add(AbstractSqlHandle sqlHandle) {

		InsertSqlHandle iSql = sqlHandle instanceof InsertSqlHandle ? (InsertSqlHandle) sqlHandle : null;
		if (iSql == null) {
			return 0;
		}

		if (iSql.getValuesList().size() == 0) {
			return 0;
		}

		List<TableMetadata> tableMetadatas = getTableMetadata(iSql.getTable());
		Set<String> cols = new HashSet<String>();
		for (TableMetadata tm : tableMetadatas) {
			cols.add(tm.getColumnName());
		}
		Set<String> noins = new HashSet<String>();
		for (String key : iSql.getValuesList().get(0).keySet()) {
			if (!cols.contains(key)) {
				noins.add(key);
			}
		}

		for (Map<String, Object> m : iSql.getValuesList()) {
			for (String noin : noins) {
				m.remove(noin);
			}
		}

		return this.executeUpdate(sqlHandle.getSql(), sqlHandle.getValues());
	}

	public int update(String sql) {
		return this.executeUpdate(sql, null, null);
	}

	public int update(String sql, Object[] values) {
		return this.executeUpdate(sql, values, null);
	}

	public int update(String sql, Object[] values, Object[] where) {
		return this.executeUpdate(sql, values, where);
	}

	public int updateUsePosition(AbstractSqlHandle sqlHandle) {
		return this.executeUpdate(sqlHandle.getSql(), sqlHandle.getValues());
	}

	public int update(AbstractSqlHandle sqlHandle) {
		return this.executeUpdate(sqlHandle.getNamedSql(), sqlHandle.getConditions());
	}

	public int bupdate(AbstractSqlHandle sqlHandle) {
		return this.executeBUpdate(sqlHandle.getNamedSql(), sqlHandle.getConditions());
	}

	public int del(String sql) {
		return this.executeUpdate(sql, null, null);
	}

	public int del(String sql, Object[] values) {
		return this.executeUpdate(sql, values, null);
	}

	public int del(AbstractSqlHandle sqlHandle) {
		return this.executeUpdate(sqlHandle.getNamedSql(), sqlHandle.getConditions());
	}

	public int delUsePosition(AbstractSqlHandle sqlHandle) {
		return this.executeUpdate(sqlHandle.getSql(), sqlHandle.getValues());
	}

	public int executeUpdate(String sql) {
		return this.executeUpdate(sql, null, null);
	}

	public int executeUpdate(String sql, Object[] values) {
		return this.executeUpdate(sql, values, null);
	}

	public int executeUpdate(String sql, Object[] values, Object[] where) {

		Query query = entityManager.createNativeQuery(sql);
		int index = 1;

		if (values != null) {
			for (Object obj : values) {
				query.setParameter(index, obj);
				index++;
			}
		}

		if (where != null) {
			for (Object obj : where) {
				query.setParameter(index, obj);
				index++;
			}
		}
		return query.executeUpdate();
	}

	public int executeUpdate(String sql, List<Condition> conditions) {

		Query query = entityManager.createNativeQuery(sql);

		for (Condition c : conditions) {
			if (c.getOpertion() == Opt.Customer) {
				continue;
			}
			Object v = c.getValue();
			if (v.getClass().isArray()) {
				query.setParameter(c.getId(), Arrays.asList((Object[]) v));
			} else if (v instanceof CustomerValue) {
				continue;
			} else {
				query.setParameter(c.getId(), c.getValue());
			}
		}

		return query.executeUpdate();
	}

	public int executeBUpdate(String sql, List<Condition> conditions) {

		int numbers = 0;
		String[] sqls = sql.split(";");

		for (String s : sqls) {
			Query query = entityManager.createNativeQuery(s);

			for (Condition c : conditions) {
				if (c.getOpertion() == Opt.Customer) {
					continue;
				}
				if (s.indexOf(c.getId()) < 0) {
					continue;
				}
				Object v = c.getValue();
				if (v.getClass().isArray()) {
					query.setParameter(c.getId(), Arrays.asList((Object[]) v));
				} else if (v instanceof CustomerValue) {
					continue;
				} else {
					query.setParameter(c.getId(), c.getValue());
				}
			}

			numbers += query.executeUpdate();
		}

		return numbers;
	}

	public int executeUpdate(AbstractSqlHandle sqlHandle) {
		return this.executeUpdate(sqlHandle.getSql(), sqlHandle.getValues());
	}

	public void add(Object entity) {
		entityManager.persist(entity);
	}

	public int getLastInsID() {
		Query query = entityManager.createNativeQuery("select last_insert_id()");

		return Integer.valueOf(query.getSingleResult().toString());
	}

	public void batch(List<String> sqls) {
		if (sqls.size() == 0) {
			return;
		}
		try {
			SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
			PreparedStatement ps = session.connection().prepareStatement(sqls.get(0));
			for (int i = 1; i < sqls.size(); i++) {
				ps.addBatch(sqls.get(i));
			}
			ps.executeBatch();
		} catch (java.sql.SQLException e) {
			query("BatchException");
		}
	}

	public void batch2(List<AbstractSqlHandle> sqlHandles) {
		if (sqlHandles.size() == 0) {
			return;
		}
		try {
			SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
			PreparedStatement ps = session.connection().prepareStatement("select 1 from dual");
			for (int i = 0; i < sqlHandles.size(); i++) {
				System.out.println("batch2:" + sqlHandles.get(i).getStaticSql());
				ps.addBatch(sqlHandles.get(i).getStaticSql());
			}
			ps.executeBatch();

		} catch (java.sql.SQLException e) {
			query("BatchException");
		}

	}

	public List<TableMetadata> getTableMetadata(String tableName) {

		List<TableMetadata> metadatas = new ArrayList<TableMetadata>();
		try {
			SessionImplementor session = entityManager.unwrap(SessionImplementor.class);
			DatabaseMetaData dm = session.connection().getMetaData();
			ResultSet colRet = dm.getColumns(null, "%", tableName, "%");
			while (colRet.next()) {
				metadatas.add(new TableMetadata(tableName, colRet.getString("COLUMN_NAME"), colRet.getString("TYPE_NAME")));
			}
		} catch (java.sql.SQLException e) {
			query("BatchException");
		}

		return metadatas;
	}

}
