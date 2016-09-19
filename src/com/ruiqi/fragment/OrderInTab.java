package com.ruiqi.fragment;

import java.util.ArrayList;

import com.ruiqi.bean.TableInfo;

import android.view.View;
import android.widget.AdapterView;

/**
 * 订单收入表格Fragment
 * @author Administrator
 *
 */
public class OrderInTab extends OrderFragment{

	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		for(int i = 0;i<5;i++){
			mDatas.add(new TableInfo("李四毛", "400",  "2016-3-30"));		
		}
	}

	@Override
	public String[] initTitles() {
		String [] titles = {"名称","金额","日期"};
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	@Override
	public int isOrNoSet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return false;
	}

}
