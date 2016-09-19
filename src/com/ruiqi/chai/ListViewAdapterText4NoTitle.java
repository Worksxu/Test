package com.ruiqi.chai;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiqi.bean.TableInfo;
import com.ruiqi.works.R;

public class ListViewAdapterText4NoTitle extends BaseAdapter {

	private ViewHolder holder;
	private List<TableInfo> list;
	private Context context;

	public ListViewAdapterText4NoTitle(Context context,List<TableInfo> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		if(list==null){
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.chai_listview_item_text4, null);
			holder.text0 = (TextView) convertView.findViewById(R.id.text0);
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);
			holder.text3 = (TextView) convertView.findViewById(R.id.text3);
			convertView.setTag(holder);  
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(positionArrayList.contains(position+"")){
				convertView.setBackgroundResource(R.drawable.rectangle_2269d4);
		}else{
			convertView.setBackgroundResource(R.drawable.rectangle_white);
		}
		holder.text0.setText((String) list.get(position).getOrderNum());
		holder.text1.setText((String) list.get(position).getOrderMoney());
		holder.text2.setText((String) list.get(position).getOrderStatus());
		holder.text3.setText((String) list.get(position).getOrderTime());
		return convertView;
	}
	private static class ViewHolder {
		TextView text0, text1, text2,text3;
	}

	private ArrayList<String> positionArrayList=new ArrayList<String>();
	public void setSelectItemIdArrayList(ArrayList< String> positionArrayList){
		this.positionArrayList=positionArrayList;
	}
	
}
