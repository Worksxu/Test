package com.ruiqi.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.chai.ArrearsList;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.db.OrderDao;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.AllOrder;
import com.ruiqi.works.BackBottleActivity;
import com.ruiqi.works.BackBottleOrder;
import com.ruiqi.works.R;

public class AllOrderBottleFragment extends OrderFragment{
	
	private BackBottleDao bbd;
	
	public interface BottleCallBack{
		public void callBack(int a);
	}
	public BottleCallBack bcb;
	
	public void setBottleCallback(BottleCallBack bcb){
		this.bcb = bcb;
	}
	private int page=1;
	private int position;
	
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.unfinsh_fragment, null);
		bbd = BackBottleDao.getInstances(getContext());
		lv_unfinsh_order = (AutoListView) view.findViewById(R.id.lv_unfinsh_order);
		initData();
		table = new OrderTable().addData( mDatas, initTitles());
		
		adapter = new TabAdapter(getContext(), table);
		
		lv_unfinsh_order.setAdapter(adapter);
		if(mDatas.size()<14){
			
			lv_unfinsh_order.setResultSize(mDatas.size());
		}
		
		lv_unfinsh_order.setOnItemClickListener(this);
		
		lv_unfinsh_order.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				page = 1;
				bcb.callBack(1);
			}
		});
		
		lv_unfinsh_order.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				page++;
				position = lv_unfinsh_order.getFirstVisiableItem();
				String shop_id = (String) SPUtils.get(getContext(), SPutilsKey.SHOP_ID, "error");
				String ship_id = (String) SPUtils.get(getContext(), SPutilsKey.SHIP_ID, "error");
			
				RequestParams params = new RequestParams(RequestUrl.DESPOIT);
				params.addBodyParameter("shipper_id", ship_id);
				params.addBodyParameter("shop_id", shop_id);
				params.addBodyParameter("page", page+"");
				System.out.println("shop_id="+shop_id);
				
				HttpUtil.PostHttp(params, mhandler, new ProgressDialog(getContext()));
			}
		});
		
		return view;
	}
	
	private void paraseData(String result) {
		System.out.println("result="+result);
		//json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				//继续解析
				if(!obj.isNull("resultInfo")){
					JSONArray array = obj.getJSONArray("resultInfo");
					for(int i=0;i<array.length();i++){
						JSONObject object = array.getJSONObject(i);
						String depositsn = object.getString("depositsn");
						String money = object.getString("money");//
						String doormoney = object.getString("doormoney");//上门费
						String productmoney = object.getString("productmoney");
						String shouldmoney = object.getString("shouldmoney");
						String status = object.getString("status");
						String username = object.getString("username");
						String time = object.getString("time");
						String mobile = object.getString("mobile");
						String id = object.getString("id");
						String address = object.getString("address");
						String kid = object.getString("kid");
						String status_name = object.getString("status_name");
						String boottle = object.getString("bottle");
						if("null".equals(boottle)){
							
							if("2".equals(status)){
								
								mDatas.add(new TableInfo(depositsn, money, status_name, time));
							}else {
								mDatas.add(new TableInfo(depositsn, productmoney, status_name, time));
							}
							
							BackBottleOrder.mData.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,new ArrayList<Bottle>()));
						}else{
							
							JSONArray array1 = new JSONArray(boottle);
							List<Bottle> mlist = new ArrayList<Bottle>();
							for(int j = 0;j<array1.length();j++){
								JSONObject json = array1.getJSONObject(j);
								String goods_num = json.getString("goods_num");
								String goods_price = json.getString("goods_price");
								if(json.has("goods_title")){
									String title = json.getString("goods_title");
									if(!"null".equals(title)){
										mlist.add(new Bottle(goods_num, goods_price, title));
									}else{
										mlist.add(new Bottle(goods_num, goods_price, ""));
									}
								}else{
									mlist.add(new Bottle(goods_num, goods_price, ""));
								}
							}
							
							if("2".equals(status)){
								
								mDatas.add(new TableInfo(depositsn, money, status_name, time));
							}else {
								mDatas.add(new TableInfo(depositsn, productmoney, status_name, time));
							}
							BackBottleOrder.mData.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,mlist));
						}
							
						
						//保存到数据库
						Cursor cursor = bbd.getFromOrderSn(depositsn);
						if(cursor.getCount()==0){//没找到，插入到数据库
							bbd.saveOrder(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name);
						}
						
					}
					lv_unfinsh_order.setResultSize(array.length());
				}else{
					lv_unfinsh_order.setResultSize(mDatas.size());
					
				}
				table = new OrderTable().addData( mDatas, initTitles());
				
				adapter = new TabAdapter(getContext(), table);
				
				lv_unfinsh_order.setAdapter(adapter);
				
				lv_unfinsh_order.onLoadComplete();
				lv_unfinsh_order.setSelection(position);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};
	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		
		Bundle bundle = getArguments();
		List<BackBottle> list = (List<BackBottle>) bundle.getSerializable("mData");
		for(int i =0;i<list.size();i++){
			String ordersn = list.get(i).getDepositsn();
			String money = list.get(i).getMoney();
			String status = list.get(i).getStatus();
			String time = list.get(i).getTime();
			String status_name = list.get(i).getStatus_name();
			String product_money= list.get(i).getProductmoney();
			if("2".equals(status)){
				
				mDatas.add(new TableInfo(ordersn, money, status_name, time));
			}else {
				mDatas.add(new TableInfo(ordersn, product_money, status_name, time));
			}
		}
		System.out.println("mDatas="+mDatas);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(position>1&&position<mDatas.size()+2){
			String depositsn = mDatas.get(position-2).getOrderNum();
			SPUtils.put(getContext(), "depositsn", depositsn);
			//跳转到订单详情界面
			Intent intent = new Intent(getContext(), BackBottleActivity.class);
			intent.putExtra("depositsn", depositsn);
			startActivity(intent);
		}
	}

	@Override
	public String[] initTitles() {
		String titles [] ={"订单号","订单金额","订单状态","订单时间"};
		return titles;
	}

	@Override
	public int isOrNoSet() {
		// TODO Auto-generated method stub
		return 1;
	}
	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return false;
	}
}
