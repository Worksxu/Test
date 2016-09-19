package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;

/**
 * 废瓶出库
 * @author Administrator
 *
 */
public class NotOutFragment extends OutInForFragment{

	@Override
	public boolean setIsOrNotVisbily() {
		return true;
	}

	@Override
	public List<TableInfo> initDataTable() {
		List<TableInfo> mDatas = new ArrayList<TableInfo>();
		for(int i = 0;i<3;i++){
			mDatas.add(new TableInfo("20kg", "5"));		
		}
		
		return mDatas;
	}

	@Override
	public List<OutIn> initDataListView() {
		List<OutIn> mList = new ArrayList<OutIn>();
		List<TableInfo> mDatas = initDataTable();
		for(int i=0;i<5;i++){
			mList.add(new OutIn("Dd66255", "2016-4-6", mDatas));
		}
		return mList;
	}

	@Override
	public boolean setIsOrNotVisbilyOne() {
		// TODO Auto-generated method stub
		return false;
	}

}
