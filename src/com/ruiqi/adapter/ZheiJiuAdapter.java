package com.ruiqi.adapter;

import java.util.List;

import com.ruiqi.adapter.TypePopupAdapter.ViewHolder;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.ZheiJiu;
import com.ruiqi.works.R;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 折旧瓶的adapter
 * @author Administrator
 *
 */
public class ZheiJiuAdapter extends BaseAdapter{
	private List<ZheiJiu> list;
	private Context context;
	private double price;
	
	public ZheiJiuAdapter(List<ZheiJiu> list, Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	public ZheiJiuAdapter() {
		super();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(arg1==null){
			viewHolder = new ViewHolder();
			arg1=LayoutInflater.from(context).inflate(R.layout.zhei_jiu_item, null);
			//初始化组件
			viewHolder.tv_typ = (TextView) arg1.findViewById(R.id.tv_typ);
			viewHolder.iv_peijian_sup = (ImageView) arg1.findViewById(R.id.iv_peijian_sup);
			viewHolder.iv_peijian_add = (ImageView) arg1.findViewById(R.id.iv_peijian_add);
			viewHolder.et_peijian_num_1 = (EditText) arg1.findViewById(R.id.et_peijian_num_1);
			//设置标签
			arg1.setTag(viewHolder);
		}else{
			//取出标签
			viewHolder = (ViewHolder) arg1.getTag();
		}
		ZheiJiu pw = list.get(arg0);
	
		viewHolder.tv_typ.setText(pw.getWeight());
		viewHolder.et_peijian_num_1.setText(pw.getNum());
		viewHolder.iv_peijian_add.setTag(arg0);
		viewHolder.iv_peijian_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int pos = (Integer) arg0.getTag();
				//Toast.makeText(context, pos+"", 1).show();
				ZheiJiu p = list.get(pos);
			
				p.setNum(Integer.parseInt(p.getNum())+1+"");
			//	p.setPrice(Double.parseDouble(p.getPrice())+dp+"");
				notifyDataSetChanged();
				
			}
		});
		viewHolder.iv_peijian_sup.setTag(arg0);
		viewHolder.iv_peijian_sup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int pos = (Integer) arg0.getTag();
				//Toast.makeText(context, pos+"", 1).show();
				ZheiJiu p = list.get(pos);
				if(Integer.parseInt(p.getNum())==0){
					Toast.makeText(context, "已达最小数目，不能再减", 1).show();
				}else{
					p.setNum(Integer.parseInt(p.getNum())-1+"");
					notifyDataSetChanged();
				}
				
			}
		});
		
		
		return arg1;
	}
	class ViewHolder{
		TextView tv_typ;
		ImageView iv_peijian_sup;
		ImageView iv_peijian_add;
		EditText et_peijian_num_1;
	}
}
