package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Type;
import com.ruiqi.works.CreateOrderActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

/**
 * 订单确定的fragment
 * @author Administrator
 *
 */
public class OrderConfrimFragment extends OrderFragment{

	private List<Type> list;
	@Override
	public void initData() {
		Bundle bundle = getArguments();
		list = (List<Type>) bundle.getSerializable("mData");
		mDatas = new ArrayList<TableInfo>();
		for(int i = 0;i<list.size();i++){
			Type type = list.get(i);
			if(CreateOrderActivity.OrderKind==3){
				mDatas.add(new TableInfo(type.getName(), type.getBottle_name(), type.getNum(),Float.parseFloat(type.getPrice())/3+""));
			}else{
				mDatas.add(new TableInfo(type.getName(), type.getBottle_name(), type.getNum(), type.getPrice()));		
			}
		}
	}

	@Override
	public String[] initTitles() {
		String titles [] ={"商品名称","规格","数量","合计"};
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
	}

	@Override
	public int isOrNoSet() {
		return 0;
	}

	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return false;
	}

}
