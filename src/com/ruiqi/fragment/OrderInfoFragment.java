package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.TableInfo;

public class OrderInfoFragment extends OrderFragment{

	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		Bundle bundle = getArguments();
		List<Orderdeail> list = (List<Orderdeail>) bundle.getSerializable("mData");
		for(int i =0;i<list.size();i++){
			String title = list.get(i).getTitle();
			String num =list.get(i).getNum();
			String money = list.get(i).getGoods_price();
			String goods_kind = list.get(i).getGoods_kind();
			String pay_money = list.get(i).getPay_money();
			mDatas.add(new TableInfo(title, goods_kind, num, money,pay_money));
		}
	
	}

	@Override
	public String[] initTitles() {
		String titles [] = {"商品名称","规格","数量","单价","折扣价"};
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
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
