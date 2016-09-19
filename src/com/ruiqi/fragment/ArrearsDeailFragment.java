package com.ruiqi.fragment;

import java.util.ArrayList;

import com.ruiqi.bean.NopayDetail;
import com.ruiqi.bean.TableInfo;

import android.view.View;
import android.widget.AdapterView;

/**
 * 欠款详情表格fragment
 * @author Administrator
 *
 */
public class ArrearsDeailFragment extends OrderFragment{

	private ArrayList<NopayDetail> arrayList;
	@Override
	public void initData() {
		arrayList=(ArrayList<NopayDetail>) getArguments().getSerializable("strArrears");
		mDatas = new ArrayList<TableInfo>();
		for (int i = 0; i < arrayList.size(); i++) {
			mDatas.add(new TableInfo(arrayList.get(i).getOrder_sn(), arrayList.get(i).getMoney(), arrayList.get(i).getTime_list()));
		}
		System.out.println("集合数据是="+mDatas);
	}

	@Override
	public String[] initTitles() {
		String titles[] = { "单号", "金额", "日期" };
		return titles;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
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
