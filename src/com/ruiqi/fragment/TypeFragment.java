package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.TableInfo;
import com.ruiqi.bean.Type;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

/**
 * 气罐类型的表格类碎片
 * @author Administrator
 *
 */
public class TypeFragment extends OrderFragment{
	private List<Type> mList;

	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		Bundle bundle = getArguments();
		mList = (List<Type>) bundle.getSerializable("mDatas");
		for(int i = 0;i<mList.size();i++){
			mDatas.add(new TableInfo(mList.get(i).getWeight(), mList.get(i).getNum(), mList.get(i).getPrice()));		
		}
	}

	@Override
	public String[] initTitles() {
		String [] titles = {"类型","数量","价格"}; 
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
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
