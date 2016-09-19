package com.ruiqi.fragment;

import java.util.ArrayList;

import com.ruiqi.bean.Nopay;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.works.ArrearsDeails;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 * 欠款账户的表格fragment
 * @author Administrator
 *
 */
public class ArreasFragment extends OrderFragment{
	private ArrayList<Nopay> arrayList;
	@Override
	public void initData() {
		arrayList=(ArrayList<Nopay>) getArguments().getSerializable("data");
		mDatas = new ArrayList<TableInfo>();
		for(int i = 0;i<arrayList.size();i++){
			mDatas.add(new TableInfo(arrayList.get(i).getUsername(), arrayList.get(i).getTotal(), arrayList.get(i).getKehu_type()));		
		}
	}

	@Override
	public String[] initTitles() {
		String [] titles = {"姓名","金额  ","类型   "};
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//跳转到欠款详情界面
				if(position>0){
					Intent intent = new Intent(getContext(), ArrearsDeails.class);
					intent.putExtra("username", arrayList.get(position-1).getUsername());
					intent.putExtra("kid", arrayList.get(position-1).getKid());
					intent.putExtra("total", arrayList.get(position-1).getTotal());
					startActivity(intent);
				}
	}

	@Override
	public int isOrNoSet() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return false;
	}

}
