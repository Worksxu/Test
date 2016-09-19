package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;

import android.content.Intent;
import android.view.View;

public class ApplyPjDeailActivity extends ApplyActivity{
	
	@Override
	public void jumpPage() {
		super.jumpPage();
		Intent intent = new Intent(ApplyPjDeailActivity.this, GoodsReceiActivity.class);
		startActivity(intent);
		finish();
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_next:
			finalData = new ArrayList<PeiJian>();
			Intent intent = new Intent(ApplyPjDeailActivity.this, ApplyPjCommitActivity.class);//跳转到申请配件的提交界面界面
			System.out.println("mData="+mData);
			for(int i=0;i<mData.size();i++){
				List<PeiJianTypeMoney> list = new ArrayList<PeiJianTypeMoney>();
				list = mData.get(i).getmList();
				for(int j=0;j<list.size();j++){
					if(Integer.parseInt(list.get(j).getNum())>0){
						PeiJianTypeMoney p = list.get(j);
						finalList = new ArrayList<PeiJianTypeMoney>();
						finalList.add(new PeiJianTypeMoney(p.getId(), p.getName(), p.getType(), p.getNorm_id(), p.getTypename(), p.getPrice(),p.getNum()));
						finalData.add(new PeiJian(mData.get(i).getName(), finalList));
					}
				}
			}
			intent.putExtra("mData", (Serializable)finalData);
			startActivity(intent);
			
			break;

		default:
			break;
		}
	}
}
