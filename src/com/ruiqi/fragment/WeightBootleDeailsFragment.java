package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;

/**
 * 重瓶领取详情的内容fragment
 * @author Administrator
 *
 */
public class WeightBootleDeailsFragment extends OutInForFragment{

	@Override
	public boolean setIsOrNotVisbily() {
		return false;
	}

	@Override
	public List<TableInfo> initDataTable() {
		List<TableInfo> mDatas = new ArrayList<TableInfo>();
		for(int i = 0;i<3;i++){
			mDatas.add(new TableInfo("50kg", "10"));		
		}
		
		return mDatas;
	}

	@Override
	public List<OutIn> initDataListView() {
		List<OutIn> mList = new ArrayList<OutIn>();
		List<TableInfo> mDatas = initDataTable();
		for(int i=0;i<5;i++){
			mList.add(new OutIn("Dd845511", "10", mDatas));
		}
		return mList;
	}

	@Override
	public boolean setIsOrNotVisbilyOne() {
		return true;
	}

}
