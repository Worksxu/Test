package com.ruiqi.chai;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.works.R;

public class ListViewSelectAdapter extends BaseAdapter {

	private ViewHolder holder;
	private ArrayList<YouHuiZheKouInfo> list;
	private Context context;
	private ArrayList<String> arrList;

	public ListViewSelectAdapter(Context context,
			ArrayList<YouHuiZheKouInfo> list) {
		this.list = list;
		this.context = context;
	}

	public ListViewSelectAdapter(Context context,ArrayList<YouHuiZheKouInfo> list, int position,ArrayList<String> arrList) {
		this.list = list;
		this.context = context;
		this.arrList=arrList;
	}

	@Override
	public int getCount() {
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.chai_listview_item_text3, null);
			holder.text0 = (TextView) convertView.findViewById(R.id.text0);
			holder.text1 = (TextView) convertView.findViewById(R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(arrList.contains(position+"")){
			convertView.setBackgroundResource(R.drawable.rectangle_2269d4);
		}else{
			convertView.setBackgroundResource(R.drawable.rectangle_white);
		}
		holder.text0.setTextColor(Color.parseColor("#333333"));
		holder.text1.setTextColor(Color.parseColor("#333333"));
		holder.text2.setTextColor(Color.parseColor("#333333"));
		holder.text0.setText(list.get(position).getTitle()+","+list.get(position).getComment()+",￥"+list.get(position).getMoney());
		holder.text1.setVisibility(View.GONE);
		holder.text2.setVisibility(View.GONE);
		return convertView;
	}

	private static class ViewHolder {
		TextView text0, text1, text2;
	}

}
