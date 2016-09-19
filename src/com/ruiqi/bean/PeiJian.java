package com.ruiqi.bean;

import java.io.Serializable;
import java.util.List;

public class PeiJian implements Serializable{
	private String Name; 
	
	private List<PeiJianTypeMoney> mList;

	public PeiJian() {
		super();
	}

	public PeiJian(String name, List<PeiJianTypeMoney> mList) {
		super();
		Name = name;
		this.mList = mList;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<PeiJianTypeMoney> getmList() {
		return mList;
	}

	public void setmList(List<PeiJianTypeMoney> mList) {
		this.mList = mList;
	}

	@Override
	public String toString() {
		return "PeiJian [Name=" + Name + ", mList=" + mList + "]";
	}
	
	
}
