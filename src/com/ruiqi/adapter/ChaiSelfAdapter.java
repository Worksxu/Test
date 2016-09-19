package com.ruiqi.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiqi.bean.SelfContent;
import com.ruiqi.works.R;

public class ChaiSelfAdapter extends BaseAdapter {

	
	private ArrayList<SelfContent> arrayList;
	private Context context;
	
	public ChaiSelfAdapter(Context context,ArrayList<SelfContent> arrayList){
		this.arrayList=arrayList;
		this.context=context;
		
	}
	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=View.inflate(context, R.layout.self_list_item, null);
			vh.tv=(TextView) convertView.findViewById(R.id.tv_myself_content);
			vh.iv=(ImageView) convertView.findViewById(R.id.iv_myself_pic);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		vh.tv.setText(arrayList.get(position).getContent());
		vh.iv.setImageResource(arrayList.get(position).getIcon());
		return convertView;
	}
	
	class ViewHolder{
		
		public TextView tv;
		public ImageView iv;
	}
}
