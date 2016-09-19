package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;

public class ApplyPjConfrimFragment extends OutInForFragment{

	public List<TableInfo> mDatas;
	public List<OutIn> mList;
	@Override
	public boolean setIsOrNotVisbily() {
		return true;
	}

	@Override
	public boolean setIsOrNotVisbilyOne() {
		return true;
	}

	@Override
	public List<TableInfo> initDataTable() {
		if(mDatas!=null){
			return mDatas;
		}
		List<TableInfo> mDatas = new ArrayList<TableInfo>();
//		for(int i = 0;i<3;i++){
//		//	mDatas.add(new TableInfo("中号", "10"));		
//		}
		return mDatas;
	}

	@Override
	public List<OutIn> initDataListView() {
		if(mList!=null){
			return mList;
		}
		List<OutIn> mList = new ArrayList<OutIn>();
		List<TableInfo> mDatas = initDataTable();
		for(int i=0;i<1;i++){
			mList.add(new OutIn("垫圈", "1", mDatas));
		}
		return mList;
	}
	public void initData(){
		
	}

}
