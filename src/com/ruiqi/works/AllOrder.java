package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.db.OrderDao;
import com.ruiqi.fragment.AllOrderBottleFragment;
import com.ruiqi.fragment.AllOrderBottleFragment.BottleCallBack;
import com.ruiqi.fragment.AllOrderFragment;
import com.ruiqi.fragment.AllOrderFragment.CallBack;
import com.ruiqi.fragment.OrderFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class AllOrder extends BaseActivity{
	private AllOrderFragment order;
	private List<Order> mData;
	private Bundle bundle;
	private OrderDao od;
	
	
	private List<BackBottle>  list;
	private AllOrderBottleFragment abf;
	private BackBottleDao bbd;
	private Bundle bundle1;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			praseData(result);
		}
	};
	
	private Handler mhandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_allorder);
		setTitle("全部订单");
		od = OrderDao.getInstances(this);
		bbd = BackBottleDao.getInstances(this);
		String result = getIntent().getStringExtra(SPutilsKey.KEY);
		order = new AllOrderFragment();
		order.setCallBack(new CallBack() {
			
			@Override
			public void callBack(int a) {
				System.out.println("a="+a);
				if(a==1){
					//重新请求网络,刷新数据
					mData = new ArrayList<Order>();
					GrassOrder.mData = new ArrayList<Order>();
					RequestParams params = new RequestParams(RequestUrl.ORDET_LIST);
					String mobile = (String) SPUtils.get(AllOrder.this, SPutilsKey.MOBILLE, "error");
					params.addBodyParameter(SPutilsKey.MOBILLE, mobile);
					params.addBodyParameter("page", "1");
					HttpUtil.PostHttp(params, handler, new ProgressDialog(AllOrder.this));
				}
			}
		});
		abf = new AllOrderBottleFragment(); 
		abf.setBottleCallback(new BottleCallBack() {
			@Override
			public void callBack(int a) {
				if(a==1){
					list = new ArrayList<BackBottle>();
					BackBottleOrder.mData = new ArrayList<BackBottle>();
					String shop_id = (String) SPUtils.get(AllOrder.this, SPutilsKey.SHOP_ID, "error");
					String ship_id = (String) SPUtils.get(AllOrder.this, SPutilsKey.SHIP_ID, "error");
				
					RequestParams params = new RequestParams(RequestUrl.DESPOIT);
					params.addBodyParameter("shipper_id", ship_id);
					params.addBodyParameter("shop_id", shop_id);
					params.addBodyParameter("page", "1");
					System.out.println("shop_id="+shop_id);
					
					HttpUtil.PostHttp(params, mhandler, new ProgressDialog(AllOrder.this));
				}
			}
		});
		//根据不同界面穿过来的值替换碎片
		if("GrassOrder".equals(result)){
			mData = new ArrayList<Order>();
			GrassOrder.mData = new ArrayList<Order>();
			RequestParams params = new RequestParams(RequestUrl.ORDET_LIST);
			String mobile = (String) SPUtils.get(AllOrder.this, SPutilsKey.MOBILLE, "error");
			params.addBodyParameter(SPutilsKey.MOBILLE, mobile);
			params.addBodyParameter("page", "1");
			HttpUtil.PostHttp(params, handler, new ProgressDialog(AllOrder.this));
		}else if("BackBottleOrder".equals(result)){
			list = new ArrayList<BackBottle>();
			BackBottleOrder.mData = new ArrayList<BackBottle>();
			String shop_id = (String) SPUtils.get(AllOrder.this, SPutilsKey.SHOP_ID, "error");
			String ship_id = (String) SPUtils.get(AllOrder.this, SPutilsKey.SHIP_ID, "error");
		
			RequestParams params = new RequestParams(RequestUrl.DESPOIT);
			params.addBodyParameter("shipper_id", ship_id);
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("page", "1");
			System.out.println("shop_id="+shop_id);
			
			HttpUtil.PostHttp(params, mhandler, new ProgressDialog(AllOrder.this));
			System.out.println("list="+list);
			
		}
	}
	@Override
	public Activity getActivity() {
		return this;
	}
	@Override
	public Handler[] initHandler() {
		return null;
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
						Log.e("全部订单", obj1.toString());
						String ordersn = obj1.getString("ordersn");
						String time = obj1.getString("ctime");
						String delivery = obj1.getString("delivery"); //完成时间
						String total = obj1.getString("total");//
						String pay_money = obj1.getString("pay_money");//实付款项
						String status = obj1.getString("status");
						String kid = obj1.getString("kid");
						String mobile = obj1.getString("mobile");
						String username = obj1.getString("username");
						String address = obj1.getString("address");
						String shop_name = obj1.getString("shop_name");
						String worksname = obj1.getString("workersname");
						String worksmobile = obj1.getString("workersmobile");
						String deposit = obj1.getString("deposit");
						
						String depreciation = obj1.getString("depreciation");
						String raffinat = obj1.getString("raffinat");
						String ispayment = obj1.getString("ispayment");
						
						String sheng=obj1.getString("sheng_name");
						String shi=obj1.getString("shi_name");
						String qu=obj1.getString("qu_name");
						String cun=obj1.getString("cun_name");
						String detail = obj1.getString("address");
						sb.append(sheng);
						sb.append(shi);
						sb.append(qu);
						sb.append(cun);
						sb.append(detail);
						
						String product_pice=obj1.getString("product_pice");//配件价格
						String comment = obj1.getString("comment");//备注
						String order_tc_type=obj1.getString("order_tc_type");
						String yh_money=obj1.getString("yh_money");
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
							//list.add(new Orderdeail(title, num, goods_kind, goods_price));
							
							
							String good_zhekou = obj2.getString("good_zhekou");//是否是折扣
							String pay_moneys = obj2.getString("pay_money");//折扣价
							String norm_id = obj2.getString("norm_id");
							String goods_id = obj2.getString("goods_id");
							String goods_type = obj2.getString("goods_type");
							
							list.add(new Orderdeail(title, num, goods_kind, goods_price,norm_id,goods_id,good_zhekou,pay_moneys,goods_type));
							
						}
						
						mData.add(new Order(ordersn, time, delivery, total, pay_money, status, kid, mobile,
								username, address, shop_name, worksname, worksmobile, is_settlement, list,deposit,depreciation,raffinat,ispayment));
						
//						GrassOrder.mData.add(new Order(ordersn, time, delivery, total, pay_money, status, kid, mobile,
//								username, address, shop_name, worksname, worksmobile, is_settlement, list,deposit,depreciation,raffinat,ispayment));
						GrassOrder.mData.add(new Order(ordersn, time, delivery, total, pay_money, status, kid, mobile,username, sb.toString(), shop_name, worksname, worksmobile, is_settlement, list,deposit,depreciation,raffinat,ispayment,order_tc_type,comment,product_pice,yh_money));
						Cursor cursor = od.getFromOrderSn(ordersn);
						if(cursor.getCount()==0){//没找到，插入到数据库
							od.saveOrder(ordersn, time, delivery, total, pay_money, status, kid, is_settlement, sb.toString(),
									username,mobile,address,shop_name,worksname,worksmobile,deposit,depreciation,raffinat,ispayment);
						}else{
							od.upDataIsPayMent(ispayment, ordersn);
						}
						
					}
					order = new AllOrderFragment();
					order.setCallBack(new CallBack() {
						
						@Override
						public void callBack(int a) {
							if(a==1){
								//重新请求网络,刷新数据
								mData = new ArrayList<Order>();
								GrassOrder.mData = new ArrayList<Order>();
								RequestParams params = new RequestParams(RequestUrl.ORDET_LIST);
								String mobile = (String) SPUtils.get(AllOrder.this, SPutilsKey.MOBILLE, "error");
								params.addBodyParameter(SPutilsKey.MOBILLE, mobile);
								params.addBodyParameter("page", "1");
								HttpUtil.PostHttp(params, handler, new ProgressDialog(AllOrder.this));
							}
						}
					});
					bundle = new Bundle();
					bundle.putSerializable("mData", (Serializable) mData);
					order.setArguments(bundle);
					getSupportFragmentManager().beginTransaction().replace(R.id.rl_allorder_fragment, order).commit();
			
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void setRefulsh(){
		
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
						
						String boottle = object.getString("datainfo");
						if("null".equals(boottle)){
							
							list.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,new ArrayList<Bottle>()));
							
							BackBottleOrder.mData.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,new ArrayList<Bottle>()));
						}else{
							
							JSONArray array1 = new JSONArray(boottle);
							List<Bottle> mlist = new ArrayList<Bottle>();
							for(int j = 0;j<array1.length();j++){
								JSONObject json = array1.getJSONObject(j);
								String goods_num = json.getString("number");
								String goods_price = json.getString("typename");
								String title = json.getString("xinpian");
								mlist.add(new Bottle(title, goods_price,goods_num));
							}
							list.add(new BackBottle(depositsn, money, doormoney, productmoney, shouldmoney, username, 
									time, mobile, id, address,status,kid,status_name,mlist));
							
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
				}
				
				abf = new AllOrderBottleFragment();
				abf.setBottleCallback(new BottleCallBack() {
					
					@Override
					public void callBack(int a) {
						if(a==1){
							list = new ArrayList<BackBottle>();
							BackBottleOrder.mData = new ArrayList<BackBottle>();
							String shop_id = (String) SPUtils.get(AllOrder.this, SPutilsKey.SHOP_ID, "error");
							String ship_id = (String) SPUtils.get(AllOrder.this, SPutilsKey.SHIP_ID, "error");
							
							RequestParams params = new RequestParams(RequestUrl.DESPOIT);
							params.addBodyParameter("shipper_id", ship_id);
							params.addBodyParameter("shop_id", shop_id);
							params.addBodyParameter("page", "1");
							System.out.println("shop_id="+shop_id);
							
							HttpUtil.PostHttp(params, mhandler, new ProgressDialog(AllOrder.this));
						}
					}
				});
				bundle1 = new Bundle();
				bundle1.putSerializable("mData", (Serializable) list);
				abf.setArguments(bundle1);
				getSupportFragmentManager().beginTransaction().replace(R.id.rl_allorder_fragment, abf).commit();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	};
}
