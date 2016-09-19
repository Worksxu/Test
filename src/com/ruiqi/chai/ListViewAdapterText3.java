package com.ruiqi.chai;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ruiqi.works.R;

public class ListViewAdapterText3 extends BaseAdapter {

	private ViewHolder holder;
	private List<HashMap<String, String>> list;
	private Context context;

	public ListViewAdapterText3(Context context,List<HashMap<String, String>> list) {
		this.list = list;
		this.context = context;
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
		if (position == 0) {
			convertView.setBackgroundResource(R.drawable.rectangle_edf4ff);
			holder.text0.setTextColor(Color.parseColor("#666666"));
			holder.text1.setTextColor(Color.parseColor("#666666"));
			holder.text2.setTextColor(Color.parseColor("#666666"));
		} else {
			convertView.setBackgroundResource(R.drawable.rectangle_white);
			holder.text0.setTextColor(Color.parseColor("#333333"));
			holder.text1.setTextColor(Color.parseColor("#333333"));
			holder.text2.setTextColor(Color.parseColor("#333333"));
		}
		holder.text0.setPadding(5, 0, 5, 0);
		holder.text0.setText((String) list.get(position).get("title1"));
		holder.text1.setText((String) list.get(position).get("title2"));
		holder.text2.setText((String) list.get(position).get("title3"));
		return convertView;
	}
	private static class ViewHolder {
		TextView text0, text1, text2;
	}

}
