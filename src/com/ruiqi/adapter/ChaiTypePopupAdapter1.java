package com.ruiqi.adapter;

import java.util.List;

import com.ruiqi.bean.Type;
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
 * 淡出匡，选择气罐类型的适配器
 * @author Administrator
 *
 */
public class ChaiTypePopupAdapter1 extends BaseAdapter{
	private List<Type> list;
	private Context context;
	private double price;
	private int num;
	
	private int[] nums;

	public ChaiTypePopupAdapter1(List<Type> list, Context context,int num,int[] nums,String str) {
		super();
		this.list = list;
		this.context = context;
		this.num=num;
		this.nums=nums;
	}

	public ChaiTypePopupAdapter1() {
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
	public View getView(final int position, View arg1, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if(arg1==null){
			viewHolder = new ViewHolder();
			arg1=LayoutInflater.from(context).inflate(R.layout.older_list_item, null);
			//初始化组件
			viewHolder.tv_price = (TextView) arg1.findViewById(R.id.tv_price);
			viewHolder.tv_weight1 = (TextView) arg1.findViewById(R.id.tv_weight1);
			viewHolder.iv_olderAdd = (ImageView) arg1.findViewById(R.id.iv_olderAdd);
			viewHolder.iv_olderSup = (ImageView) arg1.findViewById(R.id.iv_olderSup);
			viewHolder.et_input = (EditText) arg1.findViewById(R.id.et_input);
			//设置标签
			arg1.setTag(viewHolder);
		}else{
			//取出标签
			viewHolder = (ViewHolder) arg1.getTag();
		}
		Type pw = list.get(position);
		if(!TextUtils.isEmpty(pw.getPrice())){
			
			price = Double.parseDouble(pw.getPrice());
		}else{
			price = 0;
		}
		viewHolder.tv_price.setText(price+"");
		viewHolder.tv_weight1.setText(pw.getWeight());
		viewHolder.et_input.setText(pw.getNum());
		viewHolder.iv_olderAdd.setTag(position);
		
		
		viewHolder.iv_olderAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int pos = (Integer) arg0.getTag();
				//Toast.makeText(context, pos+"", 1).show();
				Type p = list.get(pos);
				if(Integer.parseInt(p.getNum())==nums[position]){
					Toast.makeText(context, "已达最大数目，不能再加", 1).show();
				}else{
					p.setNum(Integer.parseInt(p.getNum())+1+"");
					notifyDataSetChanged();
				}
			//	p.setPrice(Double.parseDouble(p.getPrice())+dp+"");
				
			}
		});
		viewHolder.iv_olderSup.setTag(position);
		viewHolder.iv_olderSup.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int pos = (Integer) arg0.getTag();
				//Toast.makeText(context, pos+"", 1).show();
				Type p = list.get(pos);
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
		TextView tv_price;
		TextView tv_weight1;
		ImageView iv_olderAdd;
		ImageView iv_olderSup;
		EditText et_input;
	}
}
