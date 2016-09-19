package com.ruiqi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.TableInfo;

public class ApplyPjCommitFragment extends ApplyPjConfrimFragment{
	
	private List<PeiJian> mData;
	@Override
	public List<TableInfo> initDataTable() {
		Bundle bundle = getArguments();
		mData =(List<PeiJian>) bundle.getSerializable("mData");
		
		
		List<TableInfo> mDatas = new ArrayList<TableInfo>();
		if(mData!=null){
			for(int i = 0;i<mData.size();i++){
				List<PeiJianTypeMoney> list = mData.get(i).getmList();
				for(int j=0;j<list.size();j++){
					mDatas.add(new TableInfo(list.get(j).getTypename(), list.get(j).getNum()));		
				}
			}
		}
		
		return mDatas;
	}
	
	
	@Override
	public List<OutIn> initDataListView() {
		Bundle bundle = getArguments();
		mData =(List<PeiJian>) bundle.getSerializable("mData");
		List<OutIn> mList = new ArrayList<OutIn>();
		//List<TableInfo> mDatas = initDataTable();
		if(mData!=null){
			
			for(int i=0;i<mData.size();i++){
				List<TableInfo> mDatas = new ArrayList<TableInfo>();
				List<PeiJianTypeMoney> list = mData.get(i).getmList();
				for(int j=0;j<list.size();j++){
					
					mDatas.add(new TableInfo(list.get(j).getTypename(), list.get(j).getNum()));		
				}
				mList.add(new OutIn(mData.get(i).getName(), "10", mDatas));
			}
		}
		System.out.println("mList="+mList);
		return mList;
	}
}






















