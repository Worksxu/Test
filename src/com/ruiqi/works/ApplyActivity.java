package com.ruiqi.works;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.db.Pj;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

public class ApplyActivity extends BaseActivity{
	
	private ListView lv_big;
	
	public List<PeiJian> mData;
	public List<PeiJianTypeMoney> mList;
	
	public MyAdapter adapter;
	
	public TextView tv_next,tv_price,tv_total_price;
	public EditText et_length;
	
	private TextWatcher tw=new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			System.out.println(count);
			if(TextUtils.isEmpty(s.toString())){
				tv_total_price.setText("￥"+0);
			}else{
				int length=Integer.parseInt(s.toString());
				float money=length*0.01f;
				tv_total_price.setText("￥"+money);
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			
		}
	};
	
	private List<Pj> pjlist;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
	};
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_peijian);
		setTitle("申请配件");
		init();
		initDatas();
	}
	
	private void init() {
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_next.setOnClickListener(this);
		
		tv_price = (TextView) findViewById(R.id.tv_price);
		
		tv_total_price = (TextView) findViewById(R.id.tv_total_price);
		
		et_length = (EditText) findViewById(R.id.et_length);
		
		lv_big = (ListView) findViewById(R.id.lv_big);
		
		et_length.addTextChangedListener(tw);
		
	}
	
	public void initDatas() {
		mData = new ArrayList<PeiJian>();
		String shipper_id = (String) SPUtils.get(ApplyActivity.this, SPutilsKey.SHIP_ID, "error");
		String shop_id = (String) SPUtils.get(ApplyActivity.this, SPutilsKey.SHOP_ID, "error");
		RequestParams params = new RequestParams(RequestUrl.PJ);
		params.addBodyParameter("shipper_id", shipper_id);
		params.addBodyParameter("shop_id", shop_id);
		HttpUtil.PostHttp(params, handler, new ProgressDialog(ApplyActivity.this));
		
	}
	
	private void paraseData(String result) {
		System.out.println("result="+result);
		try {
			JSONObject obj1 = new JSONObject(result);
			int resultCode = obj1.getInt("resultCode");
			if(resultCode==1){
				pjlist = new ArrayList<Pj>();
				JSONArray array = obj1.getJSONArray("resultInfo");
				for(int i=0;i<array.length();i++){
					JSONObject obj2 = array.getJSONObject(i);
					mList = new ArrayList<PeiJianTypeMoney>();
					String id = obj2.getString("id");
					String name = obj2.getString("name");
					String type = obj2.getString("type");
					String norm_id = obj2.getString("norm_id");
					String typename = obj2.getString("typename");
					String price = obj2.getString("price");
					pjlist.add(new Pj(id, name, type, norm_id, typename, price));
					mList.add(new PeiJianTypeMoney(id, name, type, norm_id, typename, price,"0"));
					for(int j=0;j<mList.size();j++){
						mData.add(new PeiJian(mList.get(j).getName(), mList));
					}
				}
				adapter = new MyAdapter();
				
				lv_big.setAdapter(adapter);
				setListViewHeight(lv_big);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	
	

	public boolean setIsVisbily() {
		return true;
	}
	public List<PeiJianTypeMoney> finalList;
	public List<PeiJian> finalData;
	@Override
	public void onClick(View v) {
		super.onClick(v);
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
					convertView=LayoutInflater.from(ApplyActivity.this).inflate(R.layout.select_peijian_list_item, null);
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
				// TODO Auto-generated method stub
				return Itemlist.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return Itemlist.get(position);
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder = null;
				if(convertView==null){
					viewHolder = new ViewHolder();
					convertView=LayoutInflater.from(ApplyActivity.this).inflate(R.layout.peijian_list_item, null);
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
				viewHolder.tv_name.setText(pj.getTypename());
				viewHolder.tv_money.setText("￥"+pj.getPrice());
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
							Toast.makeText(ApplyActivity.this, "已达最小数目，不能再减", 1).show();
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

		@Override
		public Activity getActivity() {
			// TODO Auto-generated method stub
			return this;
		}

		@Override
		public Handler[] initHandler() {
			// TODO Auto-generated method stub
			return null;
		}
		
		/**
		 * 手动设施listView的高度
		 * 
		 * @param listView
		 */
		public void setListViewHeight(ListView listView) {

			if (listView == null)
				return;
			MyAdapter listAdapter = (MyAdapter) listView.getAdapter();
			if (listAdapter == null) {
				// pre-condition
				return;
			}
			int totalHeight = 0;
			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			
			listView.setLayoutParams(params);

		}

}
