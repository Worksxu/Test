package com.ruiqi.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapterNoTitle;
import com.ruiqi.Table.TabRow;
import com.ruiqi.bean.OutIn;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.works.R;

/**
 * ，左右各两个textview的fragment的基类,在子类中进行数据的初始化
 * @author Administrator
 *
 */
public abstract class OutInForFragment extends BaseFragment{
	
	
	
	private List<TableInfo> mDatas;//子项的表格类数据
	public List<OutIn> mList; //listview的数据
	
	public ProgressDialog pd;
	public MyAdapter mAdapter; //listview的适配器
	
	public ListView lv_all_content;

	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.out_in_four_fragment, null);
		pd = new ProgressDialog(getContext());
		pd.setMessage("正在加载");
		init(view);
		mList = initDataListView();
		mAdapter = new MyAdapter();
		lv_all_content.setAdapter(mAdapter);
		
		
		return view;
	}

	private void init(View view) {
		lv_all_content = (ListView) view.findViewById(R.id.lv_all_content);
	}
	
	/**
	 * 设置最后一个textview的显示和隐藏
	 */
	public abstract boolean setIsOrNotVisbily();
	/**
	 * 设置第一个textview是否显示和影藏
	 */
	public abstract boolean setIsOrNotVisbilyOne();
	/**
	 * 子类表格数据的初始化
	 */
	public abstract List<TableInfo> initDataTable();
	/**
	 * listview 数据的初始化
	 */
	public abstract List<OutIn> initDataListView();
	/**
	 * 自定义适配器
	 * @author Administrator
	 *
	 */
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView==null){
				viewHolder = new ViewHolder();
				convertView=LayoutInflater.from(getContext()).inflate(R.layout.lv_all_content_item, null);
				//初始化组件
				viewHolder.lv_type = (ListView) convertView.findViewById(R.id.lv_type);
				viewHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
				viewHolder.tv_sum = (TextView) convertView.findViewById(R.id.tv_sum);
				if(setIsOrNotVisbily()){
					viewHolder.tv_sum.setVisibility(View.GONE);
					viewHolder.tv_num.setVisibility(View.GONE);
				}
				else if(setIsOrNotVisbilyOne()){
					viewHolder.tv_num.setVisibility(View.GONE);
				}
				else{
					viewHolder.tv_sum.setVisibility(View.VISIBLE);
					viewHolder.tv_num.setVisibility(View.VISIBLE);
				}
				//设置标签
				convertView.setTag(viewHolder);
			}else{
				//取出标签
				viewHolder = (ViewHolder) convertView.getTag();
			}
			OutIn allOrder = mList.get(position);
			//给子项表格listview设置数据
			if(!setIsOrNotVisbilyOne()){
				
				viewHolder.tv_num.setText(allOrder.getOrderNum());
			}
			if(!setIsOrNotVisbily()){
				viewHolder.tv_sum.setText(allOrder.getTime());
			}
			List<TableInfo> mData = allOrder.getmTableInfo();//得到瓶的类型集合
			
			
			List<TabRow> table = new OrderTable().addDataNoTitle( mData);
			TabAdapterNoTitle adapter = null;
			
			if(setIsOrNotVisbily()){
				adapter = new TabAdapterNoTitle(getContext(), table,viewHolder.tv_num,null);
			}else if(setIsOrNotVisbilyOne()){
				adapter = new TabAdapterNoTitle(getContext(), table,null,viewHolder.tv_sum);
			}else{
				adapter = new TabAdapterNoTitle(getContext(), table,viewHolder.tv_num,viewHolder.tv_sum);
			}
			
			viewHolder.lv_type.setAdapter(adapter);
			CurrencyUtils.setListViewHeightBasedOnChildren(viewHolder.lv_type);
			
			
			return convertView;
		}
		class ViewHolder{
			ListView lv_type;
			TextView tv_num;
			TextView tv_sum;
			}
		}
		
}


