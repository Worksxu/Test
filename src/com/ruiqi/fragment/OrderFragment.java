package com.ruiqi.fragment;

import java.util.List;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
/**
 * 订单表格碎片的基类
 * @author Administrator
 *
 */
public abstract class OrderFragment extends BaseFragment implements OnItemClickListener{
	public AutoListView lv_unfinsh_order;
	public ListView lv;
	
	public List<TabRow> table;
	
	public List<TableInfo> mDatas;
	
	public TabAdapter adapter;
	
	public View view;
	@Override
	public View initView() {
		if(isRefush()){
			view = LayoutInflater.from(getContext()).inflate(R.layout.unfinsh_fragment, null);
			lv_unfinsh_order = (AutoListView) view.findViewById(R.id.lv_unfinsh_order);
			initData();
			table = new OrderTable().addData( mDatas, initTitles());
			
			adapter = new TabAdapter(getContext(), table);
			
			lv_unfinsh_order.setAdapter(adapter);
			
			lv_unfinsh_order.setOnItemClickListener(this);
			
			if(isOrNoSet()==1){
			}else{
				setListViewHeightBasedOnChildren(lv_unfinsh_order);
			}
		}else {
			view = LayoutInflater.from(getContext()).inflate(R.layout.fragmnet, null);
			lv = (ListView) view.findViewById(R.id.lv_unfinsh_order);
			
			initData();
			table = new OrderTable().addData( mDatas, initTitles());
			
			adapter = new TabAdapter(getContext(), table);
			
			lv.setAdapter(adapter);
			
			lv.setOnItemClickListener(this);
			
			if(isOrNoSet()==1){
			}else{
				setListViewHeightBasedOnChildren(lv);
			}
		}
		
		
		//解解决scroview和listview的冲突问题
	
		
		return view;
	}
	

	public abstract int isOrNoSet();
	
	public abstract boolean isRefush();
	
	public abstract void initData();
	
	public abstract String [] initTitles();
	@Override
	public abstract void onItemClick(AdapterView<?> parent, View view, int position,
			long id) ;
	private void setListViewHeightBasedOnChildren(ListView listView) {   
		// 获取ListView对应的Adapter   
		ListAdapter listAdapter = listView.getAdapter();   
		if (listAdapter == null) {   
			return;   
		}   
		
		int totalHeight = 0;   
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {   
			// listAdapter.getCount()返回数据项的数目   
			View listItem = listAdapter.getView(i, null, listView);   
			// 计算子项View 的宽高   
			listItem.measure(0, 0);    
			// 统计所有子项的总高度   
			totalHeight += listItem.getMeasuredHeight();    
		}   
		
		ViewGroup.LayoutParams params = listView.getLayoutParams();   
		params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));   
		// listView.getDividerHeight()获取子项间分隔符占用的高度   
		// params.height最后得到整个ListView完整显示需要的高度   
		listView.setLayoutParams(params);   
	}   
		

}
