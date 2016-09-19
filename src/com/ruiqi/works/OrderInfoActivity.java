package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiqi.bean.Order;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.chai.ArrearsList;
import com.ruiqi.db.OrderDao;
import com.ruiqi.fragment.OrderInfoFragment;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

public class OrderInfoActivity extends BaseActivity implements ParserData {

	private TextView tv_select, tv_start;

	private String ordersn;

	private OrderDao od;

	private TextView tv_username, tv_usermobile, tv_useraddress,
			tv_mendian_content, tv_works_name, tv_works_phone,
			tv_orderinfo_type, tv_orderinfo_content, tv_money_content,
			tv_yunfei_content, tv_total_content,tv_yunfei,tv_money_content_product_qiankaun,tv_money_content_yajin_qiankaun;
	
	private String username, mobile, address, shopName, workersname,
			workersmobile, status, kid, total, yunfei, yajin, zheijiu, canye,
			pay_money, ispayment,commtent,peijianprice,youhuimoney;

	private RelativeLayout rl_other;
	private TextView tv_yajin_content, tv_canye_content, tv_zhejiu_content,tv_beizhu_content,
			tv_total;

	public static List<Orderdeail> list;
	
	private RelativeLayout rl_button;

	private LinearLayout ll_product_qiankuan,ll_yajin_qiankuan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_info);
		setTitle("订单详情");
		init();
		initData();

	}

	private int mMoney = 0;
	private ArrayList<ArrearsList> arrayListArrears;

	private void initData() {
		list = new ArrayList<Orderdeail>();
		ordersn = getIntent().getStringExtra("ordersn");
		if (GrassOrder.mData != null) {
			for (int i = 0; i < GrassOrder.mData.size(); i++) {
				Order o = GrassOrder.mData.get(i);
				if (o.getOrdersn().equals(ordersn)) {
					username = o.getUsername();
					mobile = o.getMobile();
					address = o.getAddress();
					shopName = o.getShop_name();
					workersname = o.getWorksname();
					workersmobile = o.getWorksmobile();
					status = o.getStatus();
					kid = o.getKid();
					total = o.getTotal();
					yunfei = "0";
					yajin = o.getDeposit();
					zheijiu = o.getDepreciation();
					canye = o.getRaffinat();
					pay_money = o.getPay_money();
					ispayment = o.getIspayment();
					commtent=o.getComment();
					peijianprice=o.getPeijianprice();
					youhuimoney=o.getYh_money();
					
					arrayListArrears=o.getArrayListArrears();
					List<Orderdeail> mList = o.getOd();
					if(o.getOrder_tc_type()!=null){
						if(o.getOrder_tc_type().equals("6")){
							CreateOrderActivity.OrderKind = 4;
						}else if (o.getOrder_tc_type().equals("5")) {
							CreateOrderActivity.OrderKind = 3;
						} else if (o.getOrder_tc_type().equals("4")) {
							CreateOrderActivity.OrderKind = 2;
						} else {
							CreateOrderActivity.OrderKind = 1;
						}
					}
					if(CreateOrderActivity.list==null){
						CreateOrderActivity.list=new ArrayList<Type>();
					}
					CreateOrderActivity.list.clear();
					for (int j = 0; j < mList.size(); j++) {
						Orderdeail od = mList.get(j);
						if(od.getGood_zhekou().equals("1")){
							list.add(new Orderdeail(od.getTitle(), od.getNum(), od.getGoods_kind(), od.getGoods_price(),od.getNorm_id(),od.getGoods_id(),"1",od.getPay_money(),od.getGoods_type()));
						}else if(od.getGood_zhekou().equals("0")){
							list.add(new Orderdeail(od.getTitle(), od.getNum(), od.getGoods_kind(), od.getGoods_price(),od.getNorm_id(),od.getGoods_id(),"0","0",od.getGoods_type()));
						}
						CreateOrderActivity.list.add(new Type(od.getGoods_price(), od.getTitle(), od.getNum(), od.getGoods_id(),od.getNorm_id(), "1", od.getTitle(), od.getTitle(), "0",od.getNum()));
					}
					if (CreateOrderActivity.OrderKind == 3) {
						int num = 0;
						for (int a = 0; a < list.size(); a++) {
							if(list.get(a).getGoods_type().equals("1")){//燃气
								mMoney += (Float.parseFloat(list.get(a).getGoods_price()) * (Float.parseFloat(list.get(a).getNum()) / 3));
								num += Integer.parseInt(list.get(a).getNum());
							}else if(list.get(a).getGoods_type().equals("2")){//配件
								if(list.get(a).getGood_zhekou().equals("0")){
									mMoney += (Float.parseFloat(list.get(a).getGoods_price()) * (Float.parseFloat(list.get(a).getNum())));
								}else if(list.get(a).getGood_zhekou().equals("1")){
									mMoney += (Float.parseFloat(list.get(a).getPay_money()) * (Float.parseFloat(list.get(a).getNum())));
								}
							}
						}
						SPutilsKey.GANGPINGZONGSHU = num;
					} else {
						int num = 0;
						for (int a = 0; a < list.size(); a++) {
							if(list.get(a).getGoods_type().equals("1")){//燃气
								mMoney += (Double.parseDouble(list.get(a).getNum()) * Double.parseDouble(list.get(a).getGoods_price()));
								num += Integer.parseInt(list.get(a).getNum());
							}else if(list.get(a).getGoods_type().equals("2")){//配件
								if(list.get(a).getGood_zhekou().equals("0")){
									mMoney += (Float.parseFloat(list.get(a).getGoods_price()) * (Float.parseFloat(list.get(a).getNum())));
								}else if(list.get(a).getGood_zhekou().equals("1")){
									mMoney += (Float.parseFloat(list.get(a).getPay_money()) * (Float.parseFloat(list.get(a).getNum())));
								}
							}
						}
						SPutilsKey.GANGPINGZONGSHU = num;
					}
				}
			}
		}
		tv_username.setText(username);
		tv_usermobile.setText(mobile);
		tv_useraddress.setText(address);
		tv_mendian_content.setText(shopName);
		tv_works_name.setText(workersname);
		tv_works_phone.setText(workersmobile);
		tv_orderinfo_content.setText(ordersn);
		tv_money_content.setText(mMoney + "");//价格
		tv_yunfei_content.setText(youhuimoney);//运费改为优惠

		tv_orderinfo_type.setText(status);
		tv_yajin_content.setText(yajin);
		tv_canye_content.setText("-"+canye);
		tv_zhejiu_content.setText("-"+zheijiu);
		if(arrayListArrears!=null&&arrayListArrears.size()!=0){
			for (int i = 0; i < arrayListArrears.size(); i++) {
				if(arrayListArrears.get(i).getType().equals("1")){//1商品欠款
					ll_product_qiankuan.setVisibility(View.VISIBLE);
					tv_money_content_product_qiankaun.setText("￥"+arrayListArrears.get(i).getMoney());
				}else if(arrayListArrears.get(i).getType().equals("2")){//2押金欠款
					ll_yajin_qiankuan.setVisibility(View.VISIBLE);
					tv_money_content_yajin_qiankaun.setText("￥"+arrayListArrears.get(i).getMoney());
				}
			}
		}
		if(!TextUtils.isEmpty(commtent)){
			tv_beizhu_content.setText(commtent);
		}
		System.out.println("status=" + status);
		if ("4".equals(status)) {
			if ("1".equals(ispayment)) {
				tv_orderinfo_type.setText("已完成");
			} else if ("0".equals(ispayment)) {
				tv_orderinfo_type.setText("未支付");
			}
			tv_start.setVisibility(View.GONE);
			tv_select.setVisibility(View.GONE);
			rl_other.setVisibility(View.VISIBLE);
			tv_total_content.setText(pay_money);
			tv_total.setText("实收款");
			rl_button.setVisibility(View.GONE);
		} else if ("2".equals(status)) {
			tv_start.setVisibility(View.VISIBLE);
			tv_select.setVisibility(View.VISIBLE);
			tv_orderinfo_type.setText("待配送");
			if(CreateOrderActivity.OrderKind==3 && SPutilsKey.kehuyou==1){
				tv_total_content.setText( Double.parseDouble(yunfei) + "");
			}else{
				tv_total_content.setText(Double.parseDouble(total)+ Double.parseDouble(yunfei) + "");
			}
		} else if ("5".equals(status)) {
			tv_orderinfo_type.setText("问题订单");
			tv_start.setVisibility(View.VISIBLE);
			tv_select.setVisibility(View.VISIBLE);
			tv_total_content.setText(Double.parseDouble(total)+ Double.parseDouble(yunfei) + "");
		} else {
			tv_orderinfo_type.setText(status);
			tv_start.setVisibility(View.GONE);
			tv_select.setVisibility(View.GONE);
			tv_total_content.setText(Double.parseDouble(total)+ Double.parseDouble(yunfei) + "");
		}
		if(CreateOrderActivity.OrderKind==3||CreateOrderActivity.OrderKind==4){
			// 客户余瓶信息
			getShengYuPing();
		}
		OrderInfoFragment oif = new OrderInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) list);
		oif.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_orderinfo_fragment, oif).commit();
	}

	private void init() {
		tv_select = (TextView) findViewById(R.id.tv_select);
		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_usermobile = (TextView) findViewById(R.id.tv_usermobile);
		tv_useraddress = (TextView) findViewById(R.id.tv_useraddress);
		tv_mendian_content = (TextView) findViewById(R.id.tv_mendian_content);
		tv_works_phone = (TextView) findViewById(R.id.tv_works_phone);
		tv_works_name = (TextView) findViewById(R.id.tv_works_name);
		tv_orderinfo_content = (TextView) findViewById(R.id.tv_orderinfo_content);
		tv_money_content = (TextView) findViewById(R.id.tv_money_content);
		tv_yunfei_content = (TextView) findViewById(R.id.tv_yunfei_content);
		tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
		tv_yunfei.setText("优惠金额");
		tv_total_content = (TextView) findViewById(R.id.tv_total_content);
		tv_beizhu_content = (TextView) findViewById(R.id.tv_beizhu_content);//备注

		tv_orderinfo_type = (TextView) findViewById(R.id.tv_orderinfo_type);
		tv_select.setOnClickListener(this);
		tv_start.setOnClickListener(this);

		ll_product_qiankuan = (LinearLayout) findViewById(R.id.ll_product_qiankuan);
		tv_money_content_product_qiankaun = (TextView) findViewById(R.id.tv_money_content_product_qiankaun);
		ll_yajin_qiankuan = (LinearLayout) findViewById(R.id.ll_yajin_qiankuan);
		tv_money_content_yajin_qiankaun = (TextView) findViewById(R.id.tv_money_content_yajin_qiankaun);
		
		
		rl_other = (RelativeLayout) findViewById(R.id.rl_other);
		tv_yajin_content = (TextView) findViewById(R.id.tv_yajin_content);
		tv_canye_content = (TextView) findViewById(R.id.tv_canye_content);
		tv_zhejiu_content = (TextView) findViewById(R.id.tv_zhejiu_content);
		tv_total = (TextView) findViewById(R.id.tv_total);

		rl_button = (RelativeLayout) findViewById(R.id.rl_button);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_select:// 修改订单
			intent = new Intent(OrderInfoActivity.this,ModifiyOrderActivity.class);
			intent.putExtra("ordersn", ordersn);
			intent.putExtra("username", tv_username.getText().toString().trim());
			intent.putExtra("mobile", tv_usermobile.getText().toString().trim());
			intent.putExtra("address", tv_useraddress.getText().toString().trim());
			intent.putExtra("yunfei", yunfei);
			intent.putExtra("total", total);
			intent.putExtra("all",Double.parseDouble(total) + Double.parseDouble(yunfei) + "");
			intent.putExtra("status", status);
			intent.putExtra("kid", kid);
			startActivityForResult(intent, 1);
			break;
		case R.id.tv_start:// 开始订单
			String from = (String) SPUtils.get(OrderInfoActivity.this,"createorder", "error");
			SPUtils.put(this, "createorder", "");
			SPutilsKey.type = 2;
			if (from.equals("CreateOrderActivity")) {
				SPUtils.remove(OrderInfoActivity.this, "createorder");
			}
			SPUtils.put(OrderInfoActivity.this, "ordersn", ordersn);
			SPUtils.put(OrderInfoActivity.this, "username", tv_username.getText().toString().trim());
			SPUtils.put(OrderInfoActivity.this, "usermobile", tv_usermobile.getText().toString().trim());
			SPUtils.put(OrderInfoActivity.this, "useraddress", tv_useraddress.getText().toString().trim());
			SPUtils.put(OrderInfoActivity.this, "type", list);
			SPUtils.put(OrderInfoActivity.this, "money", tv_money_content.getText().toString().trim());
			SPUtils.put(OrderInfoActivity.this, "yunfei", tv_yunfei_content.getText().toString().trim());
			SPUtils.put(OrderInfoActivity.this, "total", tv_total_content.getText().toString().trim());
			SPUtils.put(OrderInfoActivity.this, "kid", kid);
			intent = new Intent(OrderInfoActivity.this, WeightActivity.class);
			intent.putExtra("show", "OrderInfoActivity");
			startActivity(intent);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg0 == 1 && arg1 == 2) {
			if (arg2 != null) {
				String name = arg2.getStringExtra("name");
				String mobile = arg2.getStringExtra("mobile");
				String address = arg2.getStringExtra("address");
				String status = arg2.getStringExtra("status");
				tv_username.setText(name);
				tv_usermobile.setText(mobile);
				tv_useraddress.setText(address);
				this.status = status;
				if (status.equals("2")) {
					tv_orderinfo_type.setText("待配送");
				} else if (status.equals("5")) {
					tv_orderinfo_type.setText("问题订单");
				}
			}
		}
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}

	private void getShengYuPing() {
		int token = (Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0);
		// 请求网络
		RequestParams params = new RequestParams(RequestUrl.USER_INFO);
		Log.e("llll", mobile);
		params.addBodyParameter(SPutilsKey.MOBILLE, "");
		params.addBodyParameter("kid",kid );
		params.addBodyParameter(SPutilsKey.TOKEN, token + "");

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		httpUtil.PostHttp(params, 1);
	}


	@Override
	public void sendResult(String result, int what) {

		Log.e("lll客户余瓶", result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				// 继续解析
				JSONObject obj1 = obj.getJSONObject("resultInfo");
				String name = obj1.getString("user_name");
				String userPhone = obj1.getString("mobile_phone");
				ArrayList<Type> arrayList=new ArrayList<Type>();
				// 拿到客户的kid和余瓶data串
				if ((obj1.get("bottle_data") instanceof JSONArray)) {
					JSONArray jsa = obj1.getJSONArray("bottle_data");
					
					SPutilsKey.GANGPINGZONGSHU=0;
					for (int j = 0; j < jsa.length(); j++) {
						JSONObject jso = jsa.getJSONObject(j);
						if (!jso.getString("good_num").equals("0")) {
							SPutilsKey.GANGPINGZONGSHU+=Integer.parseInt(jso.getString("good_num"));
							String name1 = jso.getString("good_name");
							String norm_id = jso.getString("good_kind");
							String num = jso.getString("good_num");
							String type = "1";
							String price = jso.getString("good_price");
							// String price = object.getString("price");
							String yj_price="0";
							if(jso.has("good_deposit")){
								yj_price = jso.getString("good_deposit");   
							}
							String bottle_name = jso.getString("good_name");
							String id = jso.getString("good_id");
							if(CreateOrderActivity.list==null){
								CreateOrderActivity.list= new ArrayList<Type>();
							}
							arrayList.add(new Type(price, name1, num, id,norm_id, type, bottle_name, name1, yj_price,num));
						} 
					}
					if(SPutilsKey.GANGPINGZONGSHU==0){
						SPutilsKey.kehuyou = 2;// 客户没有瓶  拿到订单详情的瓶
					}else{
						CreateOrderActivity.list.clear();
						CreateOrderActivity.list.addAll(arrayList);
						SPutilsKey.kehuyou = 1;// 客户没有瓶  拿到订单详情的瓶
					}
				} else {
					SPutilsKey.kehuyou = 2;// 客户没有瓶
				}

			} else {
				T.showShort(this, "网络请求错误");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

}
