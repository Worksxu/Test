package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Weight;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 退订单确定的内容碎片
 * @author Administrator
 *
 */
public class BackSureFragment extends OrderFragment{

	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		Bundle bundle = getArguments();
		
		List<Weight> list = (List<Weight>) bundle.getSerializable("mData");
		for(int i = 0;i<list.size();i++){
			Weight b = list.get(i);
			mDatas.add(new TableInfo(b.getNumber(), b.getXinpian(), b.getType_name()));		
		}
	}

	@Override
	public String[] initTitles() {
		String titles [] ={"钢印号","芯片号","规格"};
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
