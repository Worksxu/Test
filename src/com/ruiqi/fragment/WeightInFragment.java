package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;

/**
 * 钢瓶重瓶入库记录
 * @author Administrator
 *
 */
public class WeightInFragment extends OutInForFragment{
	private List<TableInfo> list;
	@Override
	public boolean setIsOrNotVisbily() {
		return true;
	}

	@Override
	public List<TableInfo> initDataTable() {
		List<TableInfo> mDatas = new ArrayList<TableInfo>();
		/*for(int i = 0;i<3;i++){
			mDatas.add(new TableInfo(list.get(i).getType(), list.get(i).getStatus()));		
		}*/
		
		return mDatas;
	}

	@Override
	public List<OutIn> initDataListView() {
		Bundle bundle = getArguments();
		list = (List<TableInfo>) bundle.getSerializable("mData");
		List<OutIn> mList = new ArrayList<OutIn>();
		for(int i=0;i<list.size();i++){
			List<TableInfo> data = new ArrayList<TableInfo>();
			data.add(new TableInfo(list.get(i).getOrderNum(),list.get(i).getOrderMoney(), list.get(i).getOrderStatus(),list.get(i).getOrderTime()));
			mList.add(new OutIn(list.get(i).getOrderNum(), list.get(i).getOrderTime(), data));
		}
		return mList;
	}

	@Override
	public boolean setIsOrNotVisbilyOne() {
		// TODO Auto-generated method stub
		return false;
	}

}
