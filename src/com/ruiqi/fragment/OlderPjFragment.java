package com.ruiqi.fragment;

import java.util.List;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;

public class OlderPjFragment extends ApplyPjConfrimFragment{
	
	public OlderPjFragment(){
		
	}
	
	public OlderPjFragment(List<TableInfo> mDatas,List<OutIn> mList){
		this.mDatas=mDatas;
		this.mList=mList;
	}

}
