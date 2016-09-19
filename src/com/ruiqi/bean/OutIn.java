package com.ruiqi.bean;

import java.io.Serializable;
import java.util.List;


/**
 * 出入库记录的信息类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class OutIn implements Serializable{
	private String orderNum;
	
	private String time;
	
	private List<TableInfo> mTableInfo;

	public OutIn(String orderNum, String time, List<TableInfo> mTableInfo) {
		super();
		this.orderNum = orderNum;
		this.time = time;
		this.mTableInfo = mTableInfo;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<TableInfo> getmTableInfo() {
		return mTableInfo;
	}

	public void setmTableInfo(List<TableInfo> mTableInfo) {
		this.mTableInfo = mTableInfo;
	}

	@Override
	public String toString() {
		return "OutIn [orderNum=" + orderNum + ", time=" + time
				+ ", mTableInfo=" + mTableInfo + "]";
	}
	
	
}
