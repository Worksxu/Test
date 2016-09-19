package com.ruiqi.view;



import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.chai.ListViewSelectAdapter;
import com.ruiqi.works.R;



public class MyListView  implements OnItemClickListener{
	
	private ListView listView;
	private ListViewSelectAdapter listViewSelectAdapter;
	public ArrayList<String> arrList;
	private ArrayList<YouHuiZheKouInfo> arrayList;
	/**
	 * 优惠选择ListView
	 * @param arrayList 优惠选择项数据
	 */
	public MyListView( ArrayList<YouHuiZheKouInfo> arrayList){
		this.arrayList=arrayList;
	}
	public View getView(Context ctx,int position) {
		if(arrList==null){
			arrList=new ArrayList<String>();
		}
		arrList.clear();
		listView=new ListView(ctx);
		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		listView.setLayoutParams(params);
		listViewSelectAdapter=new ListViewSelectAdapter(ctx,arrayList,position,arrList);
		listView.setAdapter(listViewSelectAdapter);
		listView.setOnItemClickListener(this);
		return listView;
	}   
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		if(arrList.contains(position+"")){
			view.setBackgroundResource(R.drawable.rectangle_white);
			arrList.remove(position+"");
		}else if(!arrList.contains(position+"")){
			view.setBackgroundResource(R.drawable.rectangle_2269d4);
			arrList.add(position+"");
		}
	}

}
