package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;

/**
 * 重瓶出库
 * @author Administrator
 *
 */
public class WeightOutFragment extends OutInForFragment{

	private List< TableInfo> list;
	@Override
	public boolean setIsOrNotVisbily() {
		return false;
	}

	@Override
	public List<TableInfo> initDataTable() {
		List<TableInfo> mDatas = new ArrayList<TableInfo>();
		for(int i = 0;i<3;i++){
			mDatas.add(new TableInfo("15kg", "5"));		
		}
		
		return mDatas;
	}

	@Override
	public List<OutIn> initDataListView() {
		Bundle bundle = getArguments();
		list = (List<TableInfo>) bundle.getSerializable("mData");
		System.out.println("list="+list);
		List<OutIn> mList = new ArrayList<OutIn>();
		for(int i=0;i<list.size();i++){
			List<TableInfo> mDatas = new ArrayList<TableInfo>();
			mDatas.add(new TableInfo(list.get(i).getOrderMoney(), list.get(i).getOrderStatus()));
			mList.add(new OutIn(list.get(i).getOrderNum(), list.get(i).getOrderTime(), mDatas));
		}
		return mList;
	}

	@Override
	public boolean setIsOrNotVisbilyOne() {
		// TODO Auto-generated method stub
		return false;
	}

}
