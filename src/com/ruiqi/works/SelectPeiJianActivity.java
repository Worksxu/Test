package com.ruiqi.works;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.utils.CurrencyUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 选择配件界面
 * @author Administrator
 *
 */
public abstract class SelectPeiJianActivity extends BaseActivity{
	
	private ListView lv_big;
	
	public List<PeiJian> mData;
	public List<PeiJianTypeMoney> mList;
	
	public MyAdapter adapter;
	
	public TextView tv_next;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_peijian);
		
		setTitle("选择配件");
		initDatas();
		init();
		
	}

	public void initDatas() {
		mData = new ArrayList<PeiJian>();
		mList = initData(mList);
		for(int i=0;i<3;i++){
			mData.add(new PeiJian("胶管", mList));
		}
		
		
	}
	public List<PeiJianTypeMoney> initData(List<PeiJianTypeMoney> mList){
		mList = new ArrayList<PeiJianTypeMoney>();
		for(int i=0;i<3;i++){
			//mList.add(new PeiJianTypeMoney("1M", "10", "0"));
		}
		return mList;
	}

	private void init() {
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_next.setOnClickListener(this);
		lv_big = (ListView) findViewById(R.id.lv_big);
		
		adapter = new MyAdapter();
		
		lv_big.setAdapter(adapter);
	}
	
	public abstract boolean setIsVisbily();

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}
	//大的listview的适配器
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public Object getItem(int position) {
			return mData.get(position);
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
				convertView=LayoutInflater.from(SelectPeiJianActivity.this).inflate(R.layout.select_peijian_list_item, null);
				//初始化组件
				viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.lv_item = (ListView) convertView.findViewById(R.id.lv_item);
				//设置标签
				convertView.setTag(viewHolder);
			}else{
				//取出标签
				viewHolder = (ViewHolder) convertView.getTag();
			}
			PeiJian pj = mData.get(position);
			viewHolder.tv_name.setText(pj.getName());
			
			List<PeiJianTypeMoney> list = pj.getmList();
			
			ItemAdapter itemAdapter = new ItemAdapter(list);
			
			viewHolder.lv_item.setAdapter(itemAdapter);
			
			CurrencyUtils.setListViewHeightBasedOnChildren(viewHolder.lv_item);
			
			return convertView;
		}
		
		class ViewHolder{
			TextView tv_name;
			ListView lv_item;
		}
		
	}
	
	class ItemAdapter extends BaseAdapter{
		List<PeiJianTypeMoney> Itemlist;
		
		
		public ItemAdapter(List<PeiJianTypeMoney> list) {
			super();
			this.Itemlist = list;
		}

		@Override
		public int getCount() {
			return Itemlist.size();
		}

		@Override
		public Object getItem(int position) {
			return Itemlist.get(position);
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
				convertView=LayoutInflater.from(SelectPeiJianActivity.this).inflate(R.layout.peijian_list_item, null);
				//初始化组件
				viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				viewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
				viewHolder.iv_peijian_add = (ImageView) convertView.findViewById(R.id.iv_peijian_add);
				viewHolder.iv_peijian_sup = (ImageView) convertView.findViewById(R.id.iv_peijian_sup);
				viewHolder.iv_peijian_num = (EditText) convertView.findViewById(R.id.iv_peijian_num);
				//设置标签
				convertView.setTag(viewHolder);
			}else{
				//取出标签
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if(setIsVisbily()){
				
				viewHolder.tv_money.setVisibility(View.GONE);
			}
			PeiJianTypeMoney pj = Itemlist.get(position);
			viewHolder.tv_name.setText(pj.getType());
			//viewHolder.tv_money.setText(pj.getMoney());
			viewHolder.iv_peijian_num.setText(pj.getNum());
			//增加添加点击事件
			viewHolder.iv_peijian_add.setTag(position);
			viewHolder.iv_peijian_add.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					int pos = (Integer) arg0.getTag();
					PeiJianTypeMoney dp = Itemlist.get(pos);
					dp.setNum(Integer.parseInt(dp.getNum())+1+"");
				//	PeiJianActivity.setChange(pos);
					notifyDataSetChanged();
				}
			});
			//减少添加点击事件
			viewHolder.iv_peijian_sup.setTag(position);
			viewHolder.iv_peijian_sup.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					int pos = (Integer) arg0.getTag();
					PeiJianTypeMoney dp = Itemlist.get(pos);
					if(Integer.parseInt(dp.getNum())==0){
						Toast.makeText(SelectPeiJianActivity.this, "已达最小数目，不能再减", 1).show();
					}else{
						dp.setNum(Integer.parseInt(dp.getNum())-1+"");
						//PeiJianActivity.setChangeSup(pos);
						notifyDataSetChanged();
					}
				}
			});
			
			return convertView;
		}
		class ViewHolder{
			TextView tv_name;
			TextView tv_money;
			ImageView iv_peijian_add;
			ImageView iv_peijian_sup;
			EditText iv_peijian_num;
		}

	}
	
	

}
