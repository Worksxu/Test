package com.ruiqi.fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.ruiqi.Table.OrderTable;
import com.ruiqi.Table.TabAdapter;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.TableInfo;
import com.ruiqi.chai.ArrearsList;
import com.ruiqi.chai.ChaiOrderInfoActivity;
import com.ruiqi.db.OrderDao;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.AutoListView;
import com.ruiqi.view.AutoListView.OnLoadListener;
import com.ruiqi.view.AutoListView.OnRefreshListener;
import com.ruiqi.works.GrassOrder;
import com.ruiqi.works.R;



public class AllOrderFragment extends OrderFragment{
	
	private OrderDao od;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			praseData(result);
		}

		
	};
	
	public interface CallBack{
		public void callBack(int a);
	}
	
	public CallBack callBack;
	
	public void setCallBack(CallBack callBack){
		this.callBack = callBack;
	}	
	
	private int page = 1;
	private int position;
	private ArrayList<ArrearsList >  arrayListArrears;
	@Override
	public View initView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.unfinsh_fragment, null);
		od = OrderDao.getInstances(getContext());
		lv_unfinsh_order = (AutoListView) view.findViewById(R.id.lv_unfinsh_order);
		initData();
		table = new OrderTable().addData( mDatas, initTitles());
		adapter = new TabAdapter(getContext(), table);
		lv_unfinsh_order.setAdapter(adapter);
		if(mDatas.size()<15){
			
			lv_unfinsh_order.setResultSize(mDatas.size());
		}
		lv_unfinsh_order.setOnItemClickListener(this);
		//下拉刷新
		lv_unfinsh_order.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				page = 1;
				System.out.println(callBack==null);
				callBack.callBack(1);
			}
		});
		
		//上拉加载
		lv_unfinsh_order.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				//请求网络
				position = lv_unfinsh_order.getFirstVisiableItem();
				page++;
				RequestParams params = new RequestParams(RequestUrl.ORDET_LIST);
				String mobile = (String) SPUtils.get(getContext(), SPutilsKey.MOBILLE, "error");
				params.addBodyParameter(SPutilsKey.MOBILLE, mobile);
				params.addBodyParameter("page", page+"");
				HttpUtil.PostHttp(params, handler, new ProgressDialog(getContext()));
			}
		});
		
		return view;
	}
	
	/**
	 * 解析数据
	 * @param result
	 */
	private void praseData(String result) {
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONArray array = obj.getJSONArray("resultInfo");
				for(int i =0;i<array.length();i++){
					StringBuffer sb = new StringBuffer();
					JSONObject obj1 = array.getJSONObject(i);
					String ordersn = obj1.getString("ordersn");
					String time = obj1.getString("ctime");
					String comment = obj1.getString("comment");//备注
					String delivery = obj1.getString("delivery"); //完成时间
					String total = obj1.getString("total");//
					String pay_money = obj1.getString("pay_money");//实付款项
					String status = obj1.getString("status");
					String kid = obj1.getString("kid");
					String mobile = obj1.getString("mobile");
					String product_pice=obj1.getString("product_pice");//配件价格
					String username = obj1.getString("username");
					String sheng=obj1.getString("sheng_name");
					String shi=obj1.getString("shi_name");
					String qu=obj1.getString("qu_name");
					String cun=obj1.getString("cun_name");
					
					String address = obj1.getString("address");
					address=sheng+shi+qu+cun+address;
					String shop_name = obj1.getString("shop_name");
					String worksname = obj1.getString("workersname");
					String worksmobile = obj1.getString("workersmobile");
					String deposit = obj1.getString("deposit");
					
					String depreciation = obj1.getString("depreciation");
					String raffinat = obj1.getString("raffinat");
					String ispayment = obj1.getString("ispayment");
					String order_tc_type=obj1.getString("order_tc_type");

					if(obj1.has("arrears")){
						Gson gson=new Gson();
						arrayListArrears=new ArrayList<ArrearsList>();
						JSONArray jsoa=obj1.getJSONArray("arrears");
						for (int j = 0; j < jsoa.length(); j++) {
							JSONObject jsob=jsoa.getJSONObject(j);
							ArrearsList arrearsList=gson.fromJson(jsob.toString(), ArrearsList.class);
							arrayListArrears.add(arrearsList);
						}
					}
					if("null".equals(raffinat)){
						raffinat = "0";
					}
					//是否当场需要收款字段，2表示不收款，其他则是当场收款
					String is_settlement = obj1.getString("is_settlement");
					JSONArray array1 = obj1.getJSONArray("type");
					List<Orderdeail> list =new ArrayList<Orderdeail>();
					for(int j=0;j<array1.length();j++){
						JSONObject obj2 = array1.getJSONObject(j);   
						String title = obj2.getString("title");
						String num = obj2.getString("num");
						String goods_kind = obj2.getString("goods_kind");
						String goods_price = obj2.getString("goods_price");
						
						String good_zhekou = obj2.getString("good_zhekou");//是否是折扣
						String pay_moneys = obj2.getString("pay_money");//折扣价
						String norm_id = obj2.getString("norm_id");
						String goods_id = obj2.getString("goods_id");
						String goods_type = obj2.getString("goods_type");
						
						list.add(new Orderdeail(title, num, goods_kind, goods_price,norm_id,goods_id,good_zhekou,pay_moneys,goods_type));
					}
					if(status.equals("4")){
						if("1".equals(ispayment)){
							mDatas.add(new TableInfo(ordersn, pay_money, "已完成", time));
						}else if("0".equals(ispayment)){
							mDatas.add(new TableInfo(ordersn, pay_money, "未支付", time));
						}
					}else if(status.equals("2")){
						mDatas.add(new TableInfo(ordersn, total, "待配送", time));
					}else if(status.equals("5")){
						mDatas.add(new TableInfo(ordersn, total, "问题订单", time));
					}else{
						mDatas.add(new TableInfo(ordersn, total, status, time));
					}
					GrassOrder.mData.add(new Order(ordersn, time, delivery, total, pay_money, status, kid, mobile,username, address, shop_name, worksname, worksmobile, is_settlement, list,deposit,depreciation,raffinat,ispayment,order_tc_type,comment,product_pice,"",arrayListArrears));
					
					Cursor cursor = od.getFromOrderSn(ordersn);
					if(cursor.getCount()==0){//没找到，插入到数据库
						od.saveOrder(ordersn, time, delivery, total, pay_money, status, kid, is_settlement, sb.toString(),
								username,mobile,address,shop_name,worksname,worksmobile,deposit,depreciation,raffinat,ispayment);
					}else{
						od.upDataIsPayMent(ispayment, ordersn);
					}
				}
				table = new OrderTable().addData( mDatas, initTitles());
				
				adapter = new TabAdapter(getContext(), table);
				
				lv_unfinsh_order.setAdapter(adapter);	
				lv_unfinsh_order.onLoadComplete();
				lv_unfinsh_order.setSelection(position);
				lv_unfinsh_order.setResultSize(array.length());
			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void initData() {
		mDatas = new ArrayList<TableInfo>();
		Bundle bundle = getArguments();
		List<Order> list = (List<Order>) bundle.getSerializable("mData");
		for(int i =0;i<list.size();i++){
			String ordersn = list.get(i).getOrdersn();
			String money = list.get(i).getTotal();
			String inMoney = list.get(i).getPay_money();
			String status = list.get(i).getStatus();
			String time = list.get(i).getTime();
			String ispayment = list.get(i).getIspayment();
			if(status.equals("4")){
				if("1".equals(ispayment)){
					mDatas.add(new TableInfo(ordersn, inMoney, "已完成", time));
				}else if("0".equals(ispayment)){
					mDatas.add(new TableInfo(ordersn, inMoney, "未支付", time));
				}
			}else if(status.equals("2")){
				
				mDatas.add(new TableInfo(ordersn, money, "待配送", time));
			}else if(status.equals("5")){
				mDatas.add(new TableInfo(ordersn, money, "问题订单", time));
			}else{
				mDatas.add(new TableInfo(ordersn, money, status, time));
			}
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//跳转到订单详情界面
		if(position>1&&position<mDatas.size()+2){
			TableInfo  tableInfo = mDatas.get(position-2);
			String ordersn = tableInfo.getOrderNum();
			Intent	intent = new Intent(getContext(), ChaiOrderInfoActivity.class);
			intent.putExtra("ordersn", ordersn);
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
		return 1;
	}
	@Override
	public boolean isRefush() {
		// TODO Auto-generated method stub
		return false;
	}



}
