package org.idreamlands.dt.common.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Page<T> implements Serializable {

	private static final long serialVersionUID = -403835523012872934L;

	private int totalCount;

	private List<T> recordList = new ArrayList<T>(0);

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getRecordList() {
		return recordList;
	}

	public void setRecordList(List<T> recordList) {
		this.recordList = recordList;
	}

}
