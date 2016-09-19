package com.ruiqi.works;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.bean.YouHuiInfo;
import com.ruiqi.bean.YouHuiZheKouInfo;
import com.ruiqi.bean.ZheKouInfo;
import com.ruiqi.bean.ZheiJiu;
import com.ruiqi.chai.ChaiQianKuan;
import com.ruiqi.db.GpDao;
import com.ruiqi.db.OrderDao;
import com.ruiqi.fragment.OrderConfrimFragment;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.ChaiMyDialog.ChaiCallBack;
import com.ruiqi.utils.ChaiMyDialog.ChaiSelectCallBack;
import com.ruiqi.utils.ChaiMyDialog.ChaiYaJinTiaoCallBack;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.SerializableMap;
import com.ruiqi.utils.T;
import com.ruiqi.view.MyListView;

/**
 * 订单确定页
 * 
 * @author Administrator
 *
 */
public class OrderConfirmActivity extends BaseActivity implements
		android.widget.RadioGroup.OnCheckedChangeListener, ChaiCallBack,
		ChaiSelectCallBack, ParserData, ChaiYaJinTiaoCallBack {

	private TextView tv_yunfei_title, tv_money, tv_songqi_title, tv_pay_title,
			tv_back_modify, tv_order_commit;

	private String ordersn;

	private TextView tv_name, tv_phone, tv_address, tv_time, tv_yunfei_money,
			tv_songqi_money, tv_pay, tv_yajin;
	// 修改商品金额
	private String username, usermobile, useraddress, money, yunfei, total,
			kid;

	private String shop_id, shiper_id, mobile, name;

	private int isyouhui = 0, iszhekou = 0;// 标记是否有优惠 ,折扣

	private List<Orderdeail> list;

	private ProgressDialog pd;

	private String yajin, ping_total, canye, canye_weight, deposit_num;

	private OrderDao od;

	private String from;

	private String data;
	private String nodata;

	private OrderConfrimFragment ocf;// 盛放订单内容的碎片

	private TextView tv_zheijiu_money, tv_canye_money, tv_pj_money, tv_content,
			tv_youhui_select, tv_order_money; // 押金，残液，折旧,备注,优惠

	private GpDao gd;

	private RadioGroup rg_type;
	private String pay;// 支付方式

	private RelativeLayout rl_youhui;
	private LinearLayout ll_content;

	private double peijian_money = 0;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result, 1);
		}
	};
	private Handler commitHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result, 2);
		};
	};

	private ArrayList<SerializableMap> arrayList;
	private SerializableMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_backsure);
		setTitle("订单");
		arrayList = (ArrayList<SerializableMap>) getIntent().getExtras().get(
				"arrayList");
   
		map = (SerializableMap) getIntent().getExtras().get("map");

		pd = new ProgressDialog(OrderConfirmActivity.this);
		gd = GpDao.getInstances(this);
		pd.setMessage("正在确认......");
		od = OrderDao.getInstances(OrderConfirmActivity.this);
		from = (String) SPUtils.get(OrderConfirmActivity.this, "createorder","error");
		ocf = new OrderConfrimFragment();
		System.out.println("null=" + NfcActivity.mDataNull);
		System.out.println("weight=" + NfcActivity.mDataWeight);
		initData();
		init();

		System.out.println("data-=" + data);
		System.out.println("nodata=" + nodata);
		System.out.println("zheijiu_ping="
				+ getJsonDataZheiJiu(SubsidiaryActivity.mList));
		System.out.println("canye_weight=" + canye_weight);
	}

	private HttpUtil httpUtil;

	private void initData() {
		httpUtil = new HttpUtil();
		httpUtil.setParserData(this);
		RequestParams params = new RequestParams(RequestUrl.YOUHUI);
		params.addBodyParameter(SPutilsKey.TOKEN,
				(Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0) + "");
		Log.e("lll", "请求优惠" + params.getStringParams().toString());
		httpUtil.PostHttp(params, 0);

		pay = "0";// 默认现金
		list = new ArrayList<Orderdeail>();
		ordersn = (String) SPUtils.get(OrderConfirmActivity.this, "ordersn",
				"error");

		username = (String) SPUtils.get(OrderConfirmActivity.this, "username",
				"error");
		usermobile = (String) SPUtils.get(OrderConfirmActivity.this,
				"usermobile", "error");
		useraddress = (String) SPUtils.get(OrderConfirmActivity.this,
				"useraddress", "error");
		money = (String) SPUtils.get(OrderConfirmActivity.this, "money",
				"error");
		yunfei = (String) SPUtils.get(OrderConfirmActivity.this, "yunfei",
				"error");
		total = (String) SPUtils.get(OrderConfirmActivity.this, "total",
				"error");
		kid = (String) SPUtils.get(OrderConfirmActivity.this, "kid", "error");

		shop_id = (String) SPUtils.get(OrderConfirmActivity.this,
				SPutilsKey.SHOP_ID, "error");
		shiper_id = (String) SPUtils.get(OrderConfirmActivity.this,
				SPutilsKey.SHIP_ID, "error");
		mobile = (String) SPUtils.get(OrderConfirmActivity.this,
				SPutilsKey.MOBILLE, "error");
		name = (String) SPUtils.get(OrderConfirmActivity.this, "shipper_name",
				"error");

		yajin = (String) SPUtils.get(OrderConfirmActivity.this, "deposit", "0");
		ping_total = (String) SPUtils.get(OrderConfirmActivity.this,
				"ping_total", "0");
		deposit_num = (String) SPUtils.get(OrderConfirmActivity.this,
				"deposit_num", "0");
		canye = (String) SPUtils.get(OrderConfirmActivity.this, "canye", "0");
		canye_weight = (String) SPUtils.get(OrderConfirmActivity.this,
				"canye_weight", "0");

		if (NfcActivity.mDataWeight != null) {
			if (NfcActivity.mDataWeight.size() > 0) {
				data = getJsonStr(NfcActivity.mDataWeight);
				numberWeight = NfcActivity.mDataWeight.size();
			}
		}
		if (NfcActivity.mDataNull != null) {
			if (NfcActivity.mDataNull.size() > 0) {
				nodata = getJsonStr(NfcActivity.mDataNull);
			}
		}
		if (UpdatePeiJianActivity.finalData != null) {
			peijian_money = getPjMoney(UpdatePeiJianActivity.finalData);
		}
	}

	private int numberWeight = 0;

	private LinearLayout rl_ranqi_money_qiankaun, rl_peijian_money_qiankaun,
			rl_yajin_money_qiankaun, ll_qiankuan, rl_yajintiao_qiankaun,
			rl_hetongbianhao_qiankaun;
	private TextView tv_yajin_qiankuan, tv_ranqi_qiankuan, tv_yajintiao,
			tv_hetongbianhao, tv_peijian_qiankuan;

	private void init() {
		rl_hetongbianhao_qiankaun = (LinearLayout) findViewById(R.id.rl_hetongbianhao_qiankaun);
		tv_hetongbianhao = (TextView) findViewById(R.id.tv_hetongbianhao);
		ll_qiankuan = (LinearLayout) findViewById(R.id.ll_qiankuan);
		rl_ranqi_money_qiankaun = (LinearLayout) findViewById(R.id.rl_ranqi_money_qiankaun);
		rl_peijian_money_qiankaun = (LinearLayout) findViewById(R.id.rl_peijian_money_qiankaun);
		rl_yajin_money_qiankaun = (LinearLayout) findViewById(R.id.rl_yajin_money_qiankaun);
		tv_yajin_qiankuan = (TextView) findViewById(R.id.tv_yajin_qiankuan);

		tv_peijian_qiankuan = (TextView) findViewById(R.id.tv_peijian_qiankuan);
		tv_yajin_qiankuan = (TextView) findViewById(R.id.tv_yajin_qiankuan);
		tv_ranqi_qiankuan = (TextView) findViewById(R.id.tv_ranqi_qiankuan);

		rl_yajintiao_qiankaun.setOnClickListener(this);

		rl_youhui = (RelativeLayout) findViewById(R.id.rl_youhui);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_order_money = (TextView) findViewById(R.id.tv_order_money);// 订单应付款

		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_time.setText(getNowTime());

		tv_youhui_select = (TextView) findViewById(R.id.tv_youhui_select);// 优惠
		tv_yunfei_money = (TextView) findViewById(R.id.tv_yunfei_money);// 押金应付金额
		tv_songqi_money = (TextView) findViewById(R.id.tv_songqi_money);// 优惠内容
		tv_content = (TextView) findViewById(R.id.tv_content);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);// 备注
		ll_content.setOnClickListener(this);

		tv_money = (TextView) findViewById(R.id.tv_money);// 订单实付款
		tv_money.setOnClickListener(this);
		tv_pay = (TextView) findViewById(R.id.tv_pay);
		tv_yunfei_title = (TextView) findViewById(R.id.tv_yunfei_title);
		tv_yunfei_title.setText("押金应收金额");

		tv_songqi_title = (TextView) findViewById(R.id.tv_songqi_title);
		tv_songqi_title.setText("优惠");

		tv_pay_title = (TextView) findViewById(R.id.tv_pay_title);
		tv_pay_title.setText("应收款");

		tv_back_modify = (TextView) findViewById(R.id.tv_back_modify);
		tv_back_modify.setText("欠款");
		tv_back_modify.setOnClickListener(this);

		tv_order_commit = (TextView) findViewById(R.id.tv_order_commit);
		tv_order_commit.setText("确定收款");
		tv_order_commit.setOnClickListener(this);

		tv_yajin = (TextView) findViewById(R.id.tv_yajin);// 押金实收金额
		tv_yajin.setOnClickListener(this);

		tv_zheijiu_money = (TextView) findViewById(R.id.tv_zheijiu_money);
		tv_canye_money = (TextView) findViewById(R.id.tv_canye_money);
		tv_pj_money = (TextView) findViewById(R.id.tv_pj_money);

		rg_type = (RadioGroup) findViewById(R.id.rg_type);
		rg_type.setOnCheckedChangeListener(this);
		String yajinmoeny = "";
		if (from.equals("CreateOrderActivity")) {
			String name = null;
			String mobile = null;
			String address = null;
			String money = null;
			money = (String) SPUtils.get(OrderConfirmActivity.this,"createOrderMoney", "error");
			if (SPutilsKey.neworold == 2) {
				name = (String) SPUtils.get(OrderConfirmActivity.this,"oldUserName", "error");
				mobile = (String) SPUtils.get(OrderConfirmActivity.this,"oldUserMobile", "error");
				address = (String) SPUtils.get(OrderConfirmActivity.this,"oldUserAddress", "error");
			} else if (SPutilsKey.neworold == 1) {
				name = (String) SPUtils.get(OrderConfirmActivity.this,
						"newUserName", "error");
				mobile = (String) SPUtils.get(OrderConfirmActivity.this,
						"newUserMobile", "error");
				address = (String) SPUtils.get(OrderConfirmActivity.this,
						"newUserAddress", "error");
			}
			tv_name.setText(name);
			tv_phone.setText(mobile);
			tv_address.setText(address);
			if (CreateOrderActivity.OrderKind == 3 && SPutilsKey.kehuyou == 1) {
				tv_money.setText(0 + "");
				tv_order_money.setText(0 + "");
			} else {
				Log.e("llll", "是走的这");
				tv_money.setText(Double.parseDouble(money) + "");
				tv_order_money.setText(Double.parseDouble(money) + "");
			}
			// 替换fragment
			Bundle bundle = new Bundle();
			bundle.putSerializable("mData",(Serializable) CreateOrderActivity.list);
			ocf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rl_content, ocf).commit();
			// 创建订单
		} else {
			tv_name.setText(username);
			tv_phone.setText(usermobile);
			tv_address.setText(useraddress);
			tv_yunfei_money.setText(Double.parseDouble(yunfei) + "");
			if (CreateOrderActivity.OrderKind == 3 && SPutilsKey.kehuyou == 1) {
				tv_money.setText(0 + "");
				tv_order_money.setText(0 + "");
			} else {
				Log.e("llll", "从订单来是走的这");
				tv_money.setText(Double.parseDouble(money) + "");
				tv_order_money.setText(Double.parseDouble(money) + "");
			}
			if (OrderInfoActivity.list != null) {
				List<Type> list = new ArrayList<Type>();
				for (int i = 0; i < OrderInfoActivity.list.size(); i++) {
					Orderdeail o = OrderInfoActivity.list.get(i);
					if (CreateOrderActivity.OrderKind == 3) {
						list.add(new Type(
								Double.parseDouble(o.getGoods_price())
										* (Double.parseDouble(o.getNum())) + "",
								null, o.getNum(), null, null, null, o
										.getGoods_kind(), o.getTitle()));
					} else {
						list.add(new Type(
								Double.parseDouble(o.getGoods_price())
										* Double.parseDouble(o.getNum()) + "",
								null, o.getNum(), null, null, null, o
										.getGoods_kind(), o.getTitle()));
					}
				}
				Bundle bundle = new Bundle();
				bundle.putSerializable("mData", (Serializable) list);
				ocf.setArguments(bundle);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.rl_content, ocf).commit();
			}
		}
		float yajinmoney = 0f;
		if (CreateOrderActivity.OrderKind == 1) {// 正常订单
			tv_pay.setText(Double.parseDouble(tv_money.getText().toString())
					+ Double.parseDouble(tv_yunfei_money.getText().toString())
					+ Double.parseDouble(yajin) - Double.parseDouble(canye)
					- Double.parseDouble(ping_total) + peijian_money + "");
		} else if (CreateOrderActivity.OrderKind == 2) {// 体验套餐
			// for (int i = 0; i < CreateOrderActivity.list.size(); i++) {
			// yajinmoney+=Double.parseDouble(CreateOrderActivity.list.get(i).getNum())*Double.parseDouble(CreateOrderActivity.list.get(i).getYj_price());
			// }
			tv_pay.setText(Double.parseDouble(tv_money.getText().toString())
					+ Double.parseDouble(tv_yunfei_money.getText().toString())
					+ Double.parseDouble(yajin) + yajinmoney
					- Double.parseDouble(canye)
					- Double.parseDouble(ping_total) + peijian_money + "");
		} else if (CreateOrderActivity.OrderKind == 3
				|| CreateOrderActivity.OrderKind == 4) {// 优惠套餐
			// for (int i = 0; i < CreateOrderActivity.list.size(); i++) {
			// yajinmoney+=Double.parseDouble(CreateOrderActivity.list.get(i).getNum())*Double.parseDouble(CreateOrderActivity.list.get(i).getYj_price());
			// }
			tv_pay.setText(Double.parseDouble(tv_money.getText().toString())
					+ Double.parseDouble(tv_yunfei_money.getText().toString())
					+ Double.parseDouble(yajin) - Double.parseDouble(canye)
					- Double.parseDouble(ping_total) + peijian_money + "");
		}
		// 押金，折旧，残液,配件
		tv_yajin.setText(Double.parseDouble(yajin) + "");
		tv_yunfei_money.setText(Double.parseDouble(yajin) + "");
		tv_zheijiu_money.setText("-" + Double.parseDouble(ping_total));
		tv_canye_money.setText("-" + Double.parseDouble(canye));
		tv_pj_money.setText(peijian_money + "");

		// tv_pj_money.setOnClickListener(this);
		rl_youhui.setOnClickListener(this);
	}

	private MyListView myListView;

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_order_commit:
			// if (pay.equals("1")) {
			// Toast.makeText(this, "暂不支持网上支付", Toast.LENGTH_SHORT).show();
			// } else if (pay.equals("0")) {
			sureRecei("0");
			// }
			break;
		case R.id.tv_back_modify:

			Intent it = new Intent(this, ChaiQianKuan.class);
			startActivityForResult(it, 1);

			break;
		case R.id.tv_yajin:// 修改押金
			// ChaiMyDialog.getInstance().setCallBack(this);
			// ChaiMyDialog.getInstance().showYaJin(this, "请输入押金",1);
			break;
		case R.id.tv_money:// 修改订单实收金额
			// ChaiMyDialog.getInstance().setCallBack(this);
			// ChaiMyDialog.getInstance().showYaJin(this, "请输入订单金额",2);
			break;
		case R.id.rl_youhui:// 修改配件总价格
			if (myListView == null) {
				if (youhHuiZheKou.size() == 0) {
					Toast.makeText(this, "当前没有优惠", Toast.LENGTH_SHORT).show();
					return;
				}
				myListView = new MyListView(youhHuiZheKou);
			}
			ChaiMyDialog.getInstance().setSelectCallBack(this);
			ChaiMyDialog.getInstance().showListViewSelect(this, "选择优惠类型",
					myListView.getView(this, 0));
			break;
		case R.id.ll_content:// 修改商品价格
			ChaiMyDialog.getInstance().setCallBack(this);
			ChaiMyDialog.getInstance().show(this, "请输入备注内容");
			break;
		
		default:
			break;
		}
	}

	private String hetong, yajinqiankuan, peijian, ranqi;// 合同编号，欠款的押金，配件，燃气
	private int sumoney;// 欠款总钱数

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg1 == 0) {
		} else if (arg1 == 1) {
			yajinqiankuan = "";
			peijian = "";
			ranqi = "";
			sumoney=0;
			hetong = arg2.getStringExtra("hetong");
			yajinqiankuan = arg2.getStringExtra("yajin");
			peijian = arg2.getStringExtra("peijian");
			ranqi = arg2.getStringExtra("ranqi");
			sumoney = arg2.getIntExtra("summoney", 0);
			setQianKuanDetail();
			tv_pay.setText(getPayMoney()+"");
		}
	}

	/**
	 * 设置欠款详情显示
	 */
	public void setQianKuanDetail() {
		if (sumoney == 0) {
			ll_qiankuan.setVisibility(View.GONE);
		} else {
			tv_hetongbianhao.setText(hetong);
			ll_qiankuan.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(yajinqiankuan)) {// 欠押金
				rl_yajin_money_qiankaun.setVisibility(View.VISIBLE);
				tv_yajin_qiankuan.setText(yajinqiankuan);
			} else {
				rl_yajin_money_qiankaun.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(peijian)) {// 欠配件
				rl_peijian_money_qiankaun.setVisibility(View.VISIBLE);
				tv_peijian_qiankuan.setText(peijian);
			} else {
				rl_peijian_money_qiankaun.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(ranqi)) {// 欠燃气
				rl_ranqi_money_qiankaun.setVisibility(View.VISIBLE);
				tv_ranqi_qiankuan.setText(ranqi);
			} else {
				rl_ranqi_money_qiankaun.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * 计算实际的押金
	 */
	public float getShiJiYaJin() {
		float yajins = 0f;
		if (!TextUtils.isEmpty(yajinqiankuan)) {
			yajins = Float.parseFloat(yajin) - Integer.parseInt(yajinqiankuan);
		} else {
			yajins = Float.parseFloat(yajin);
		}
		return yajins;
	}

	/**
	 * 设置按钮是否可点击
	 */
	public void setEnableClick(boolean flag1, boolean flag2) {
		tv_back_modify.setEnabled(flag1);
		tv_order_commit.setEnabled(flag2);
	}

	/**
	 * 确认收款
	 */
	private void sureRecei(String str) {
		data(str, ordersn, kid);
	}

	private void data(String str, String ordersn, String kid) {
		if (data == null || data.equals("")) {
			T.showShort(OrderConfirmActivity.this, "还没扫描瓶");
		} else {
			String isSelf = null;
			if (SelfActivity.selfList != null) {
				if (SelfActivity.selfList.size() > 0) {
					isSelf = "1";
				} else {
					isSelf = "0";
				}
			}
			RequestParams params = new RequestParams(RequestUrl.CONFIRMORDER);
			params.addBodyParameter("ordersn", ordersn);
			params.addBodyParameter("kid", kid);
			params.addBodyParameter("pay_money", tv_pay.getText().toString());// 实收款
			params.addBodyParameter("data", data);// 重瓶
			params.addBodyParameter("nodata", nodata);// 空瓶
			params.addBodyParameter("shop_id", shop_id);
			params.addBodyParameter("shipper_id", shiper_id);
			params.addBodyParameter("shiper_name", name);
			params.addBodyParameter("shiper_mobile", mobile);
			// params.addBodyParameter("deposit", yajin);//
			params.addBodyParameter("deposit", getShiJiYaJin()+"");// 实际收的押金
			params.addBodyParameter("deposit_num", deposit_num);// 押金钢瓶数量
			params.addBodyParameter("depreciation", ping_total);// 折旧钱
			if (!TextUtils.isEmpty(ping_total) ||! ping_total.equals("0")) {
				params.addBodyParameter("zheijiu_ping",
						getJsonDataZheiJiu(SubsidiaryActivity.mList));// 折旧Data串
			}
			params.addBodyParameter("raffinat", canye);// 残液
			params.addBodyParameter("raffinat_weight", canye_weight);// 残液的重量
			params.addBodyParameter("is_safe", isSelf);// 是否有安全报告
			if (isSelf.equals("1")) {
				params.addBodyParameter("selfjson",
						getJson(SelfActivity.selfList));
			}
			params.addBodyParameter("isyouhui", isyouhui + "");// 是否优惠 0 否 1是
			params.addBodyParameter("yh_money", youhuijine + "");// 优惠金额

			if (tv_content.getText().toString().trim().equals("请输入备注内容")) {
				// params.addBodyParameter("comment", "");// 备注
			} else {
				params.addBodyParameter("comment", tv_content.getText().toString().trim());// 备注呢
			}
			Log.e("lll客户钢瓶的总数:", (SPutilsKey.GANGPINGZONGSHU) + "");
			Log.e("lll扫描钢瓶的总数:", "" + numberWeight);
			Log.e("lll计算剩余钢瓶的总数:", (SPutilsKey.GANGPINGZONGSHU - numberWeight)
					+ "");
			// 配件
			if(sumoney==0){
				params.addBodyParameter("is_pay", "0");// 用于判定是否为欠款 str为0代表不是 为1代表是
			}else{
				params.addBodyParameter("is_pay", "1");// 用于判定是否为欠款 str为0代表不是 为1代表是
			}
			
			params.addBodyParameter("is_cash", pay);// 0 代表现金支付，1代表网上支付
			params.addBodyParameter("peijian",getJsonDataPj(UpdatePeiJianActivity.finalData));// 配件
			params.addBodyParameter("peijian_money", tv_pj_money.getText()
					.toString().trim());// 配件的钱
			// 折旧的瓶
			if (CreateOrderActivity.OrderKind == 1) {
				params.addBodyParameter("tc_type", "0");
			} else if (CreateOrderActivity.OrderKind == 2) {
				params.addBodyParameter("tc_type", "4");// 体验套餐
			} else if (CreateOrderActivity.OrderKind == 3
					|| CreateOrderActivity.OrderKind == 4) {
				params.addBodyParameter("tc_type", "5");// 优惠套餐
				if (from.equals("CreateOrderActivity")) {// 创建订单
					Log.e("lll客户有瓶状态", SPutilsKey.kehuyou + "");
					if (SPutilsKey.kehuyou == 1) {
						params.addBodyParameter(
								"bottle_data",
								getJsonData(
										CreateOrderActivity.arrayListyuping,
										SPutilsKey.GANGPINGZONGSHU
												- numberWeight));// 客户剩余的钢瓶Data
						Log.e("lll走着112",
								getJsonData(
										CreateOrderActivity.arrayListyuping,
										SPutilsKey.GANGPINGZONGSHU
												- numberWeight));
					} else if (SPutilsKey.kehuyou == 2) {
						params.addBodyParameter(
								"bottle_data",
								getJsonData(CreateOrderActivity.list,
										SPutilsKey.GANGPINGZONGSHU
												- numberWeight));// 客户剩余的钢瓶Data
						Log.e("lll走着11",
								getJsonData(CreateOrderActivity.list,
										SPutilsKey.GANGPINGZONGSHU
												- numberWeight));
					}
				} else {// 订单列表
					params.addBodyParameter(
							"bottle_data",
							getJsonData(CreateOrderActivity.list,
									SPutilsKey.GANGPINGZONGSHU - numberWeight));// 客户剩余的钢瓶Data
					Log.e("lll走着",
							getJsonData(CreateOrderActivity.list,
									SPutilsKey.GANGPINGZONGSHU - numberWeight));
				}
			}

			if (!TextUtils.isEmpty(hetong)) {// 判断合同编号是否为空
				params.addBodyParameter("contractno", hetong);
				if (!TextUtils.isEmpty(yajinqiankuan)) {
					params.addBodyParameter("deposit_money", yajinqiankuan);
				}
				if (!TextUtils.isEmpty(ranqi)) {
					params.addBodyParameter("bottle_money", ranqi);
				}
				if (!TextUtils.isEmpty(peijian)) {
					params.addBodyParameter("product_money", peijian);
				}
			}
			if (getShiJiYaJin()!=0) {// 押金是否存在
				if (TextUtils.isEmpty(tv_yajintiao.getText().toString().trim())) {// 判断押金条是否输入
					// ChaiMyDialog.getInstance().showHint(this, "请输入押金条编号");
					ChaiMyDialog.getInstance().setYaJinTiaoCallBack(this);
					ChaiMyDialog.getInstance().showYaJinTiao(this, "请输入押金条编号");
					return;
				} else {
					params.addBodyParameter("receiptno", tv_yajintiao.getText()
							.toString().trim());
				}
			}
			pd.show();
			Log.e("lll", "确认收款" + params.getStringParams().toString());

			 setEnableClick(false, false);// 设置提交欠款不可用
			 HttpUtil.PostHttp(params, handler, pd);
		}
	}

	private String createordersn;

	private void paraseData(String result, int a) {
		System.out.println("result=" + result);
		if (a == 1) {
			setEnableClick(true, true);// 设置提交欠款可用
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				String resultInfo = obj.getString("resultInfo");
				if (resultCode == 1) {
					Toast.makeText(OrderConfirmActivity.this, resultInfo,
							Toast.LENGTH_SHORT).show();
					System.out.println("ordersn=" + ordersn);
					System.out.println("ordersn=" + createordersn);
					// 改变状态
					if (createordersn == null) {
						od.updateStatus("4", ordersn);
						od.updatePayMoney(tv_pay.getText().toString(), ordersn);
						od.updateTime(System.currentTimeMillis() + "", ordersn);
						shangChuanTuPian(ordersn);
					} else {
						od.updateStatus("4", createordersn);
						od.updatePayMoney(tv_pay.getText().toString(),
								createordersn);
						od.updateTime(System.currentTimeMillis() + "",
								createordersn);
						shangChuanTuPian(createordersn);
					}
					// 修改完成的价格
					// 删除该芯片在送气工的库存
					// for(int i=0;i<NfcActivity.mDataWeight.size();i++){
					// String xinpian =
					// NfcActivity.mDataWeight.get(i).getXinpian();
					// gd.deleteFromXinpianAndShip(xinpian, shiper_id);
					// }
					clear();
					CreateOrderActivity.userFlag = true;// 方便下次进入的时候，用户身份默认为老用户
					Intent intent = new Intent(OrderConfirmActivity.this,
							MainActivity.class);
					startActivity(intent);
					// 测试问题
				} else {
					Toast.makeText(OrderConfirmActivity.this, resultInfo,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (a == 2) {
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				String resultInfo = obj.getString("resultInfo");
				if (resultCode == 1) {
					SPUtils.put(OrderConfirmActivity.this, "customercard", "");// 清楚用户卡
					JSONObject object = obj.getJSONObject("resultInfo");
					createordersn = object.getString("ordersn");
					String kid = object.getString("kid");
					// if (flag) {
					// data("0", createordersn, kid);
					// } else {
					// data("1", createordersn, kid);
					// }
				} else {
					Toast.makeText(OrderConfirmActivity.this, resultInfo,
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void clear() {
		if (WeightActivity.list != null) {
			WeightActivity.list = null;
		}
		if (NullActivity.list != null) {
			NullActivity.list = null;
		}
		if (NfcActivity.mDataWeight != null) {
			NfcActivity.mDataWeight = null;
		}
		if (NfcActivity.mDataNull != null) {
			NfcActivity.mDataNull = null;
		}
		if (UpdatePeiJianActivity.finalData != null) {
			UpdatePeiJianActivity.finalData = null;
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

	private String getNowTime() {
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(nowDate);
		return now;
	}

	/**
	 * 瓶和安全报告重点标注id的字符串的拼接
	 */
	private String getJsonStr(List<Weight> sList) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < sList.size(); i++) {
			array.put(sList.get(i).getXinpian());
		}
		return array.toString();
	}

	private String getJson(List<String> sList) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < sList.size(); i++) {
			array.put(sList.get(i));
		}
		return array.toString();
	}

	/**
	 * 拼接json字符串
	 * 
	 * @return
	 */
	private String getJsonData(List<Type> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("good_id", popupList.get(j).getId());
				obj1.put("good_num", popupList.get(j).getNum());
				obj1.put("good_name", popupList.get(j).getName());
				if (CreateOrderActivity.OrderKind == 3) {
					obj1.put(
							"good_price",
							Double.parseDouble(popupList.get(j).getPrice())
									/ (Double.parseDouble(popupList.get(j)
											.getNum()) / 3));
				} else {
					obj1.put(
							"good_price",
							Double.parseDouble(popupList.get(j).getPrice())
									/ (Double.parseDouble(popupList.get(j)
											.getNum())));
				}
				// obj1.put("good_price",
				// Double.parseDouble(popupList.get(j).getPrice())/Double.parseDouble(popupList.get(j).getNum()));
				obj1.put("good_type", popupList.get(j).getType());
				obj1.put("good_kind", popupList.get(j).getNorm_id());
				// obj1.put("wb", weightBottle);
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return arr.toString();
	}

	/**
	 * 客户剩余的钢瓶Data
	 * 
	 * @param popupList
	 * @param number
	 * @return
	 */
	private String getJsonData(List<Type> popupList, int number) {
		JSONArray arr = new JSONArray();
		// 订单
		Log.e("lllpopupList", popupList.toString());
		Log.e("lllmDataWeight", NfcActivity.mDataWeight.toString());
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			try {
				obj1.put("good_id", popupList.get(j).getId());
				obj1.put("good_name", popupList.get(j).getName());
				String good_kind = popupList.get(j).getNorm_id();
				int num = Integer.parseInt(popupList.get(j).getNum());
				for (int k = 0; k < NfcActivity.mDataWeight.size(); k++) {
					Log.e("lllgood_kind", good_kind);
					Log.e("lll", NfcActivity.mDataWeight.get(k).getType());
					if (good_kind.equals(NfcActivity.mDataWeight.get(k)
							.getType())) {
						if (num != 0) {
							num--;
						}
					}
				}
				obj1.put("good_kind", good_kind);
				obj1.put("good_num", num + "");
				obj1.put("good_price", "0");
				obj1.put("good_type", popupList.get(j).getType());
				obj1.put("good_deposit", popupList.get(j).getYj_price());
				// obj1.put("wb", weightBottle);
				arr.put(obj1);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		return arr.toString();
	}

	private String getJsonDataZheiJiu(List<ZheiJiu> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		if (popupList != null) {
			for (int j = 0; j < popupList.size(); j++) {
				JSONObject obj1 = new JSONObject();
				try {
					obj1.put("good_num", popupList.get(j).getNum());
					obj1.put("good_name", popupList.get(j).getWeight());
					obj1.put("good_id", popupList.get(j).getId());
					// obj1.put("wb", weightBottle);
					arr.put(obj1);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}
		return arr.toString();
	}

	private String getJsonDataPj(List<PeiJian> popupList) {
		JSONArray arr = new JSONArray();
		// 订单
		for (int j = 0; j < popupList.size(); j++) {
			JSONObject obj1 = new JSONObject();
			PeiJian pj = popupList.get(j);
			String title = pj.getName();
			List<PeiJianTypeMoney> list = pj.getmList();
			for (int i = 0; i < list.size(); i++) {
				try {
					obj1.put("good_id", list.get(i).getId());
					obj1.put("good_num", list.get(i).getNum());
					obj1.put("good_name", list.get(i).getName());
					obj1.put("good_price", list.get(i).getPrice());
					obj1.put("good_type", list.get(i).getType());
					obj1.put("good_kind", list.get(i).getNorm_id());
					obj1.put("iszhekou", "0");
					// obj1.put("wb", weightBottle);
					arr.put(obj1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		if (iszhekou == 1) {// 折扣配件
			for (int i = 0; i < myListView.arrList.size(); i++) {
				if (youhHuiZheKou
						.get(Integer.parseInt(myListView.arrList.get(i)))
						.getType().equals("2")) {// 折扣
					JSONObject obj1 = new JSONObject();
					try {
						obj1.put("good_id",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getData().getGood_id());
						obj1.put("good_num",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getData().getGood_num());
						obj1.put("good_name",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getData().getGood_name());
						obj1.put("good_price",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getData().getGood_price());
						obj1.put("good_type",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getData().getGood_type());
						obj1.put("good_kind",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getData().getGood_kind());
						obj1.put("zk_money",
								((ZheKouInfo) youhHuiZheKou.get(Integer
										.parseInt(myListView.arrList.get(i))))
										.getMoney());
						obj1.put("iszhekou", "1");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					arr.put(obj1);
				}
			}
		}
		return arr.toString();
	}

	// 计算配件的价钱
	private double getPjMoney(List<PeiJian> popupList) {
		double pjMoney = 0;
		for (int j = 0; j < popupList.size(); j++) {
			PeiJian pj = popupList.get(j);
			List<PeiJianTypeMoney> list = pj.getmList();
			for (int i = 0; i < list.size(); i++) {
				String price = list.get(i).getPrice();
				String num = list.get(i).getNum();
				pjMoney += (Double.parseDouble(price) * Double.parseDouble(num));
			}
		}
		return pjMoney;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_xianjin:// 现金支付
			pay = "0";
			break;
		case R.id.rb_intent:// 网上支付
			pay = "1";
			break;
		default:
			break;
		}
	}

	private void shangChuanTuPian(String orderSN) {
		if (arrayList != null && arrayList.size() != 0) {
			RequestParams params = new RequestParams(RequestUrl.UP_LOAD_DATA);
			params.addBodyParameter("order_sn", orderSN);
			params.addBodyParameter("num", arrayList.size() + "");
			System.out.println("图片的数量：" + arrayList.size());
			for (int i = 0, j = 1; i < arrayList.size(); i++, j++) {
				params.addBodyParameter("file" + j, new File(arrayList.get(i)
						.getMap().get("path")));
				System.out.println("file" + j);
				System.out.println(new File(arrayList.get(i).getMap()
						.get("path")));
			}
			params.setMultipart(true);
			HttpUtil.upLoadData(params, 3);
		}
	}

	@Override
	public void chaiStringCallBack(String str) {
		if (TextUtils.isEmpty(str)) {
			tv_content.setText("请输入备注内容");
		} else {
			tv_content.setText(str);
		}
	}

	@Override
	public void chaiSelectCallBack(boolean isSelect) {
		if (isSelect) {
			myListView.arrList.size();
//			tv_pay.setText(Double.parseDouble(tv_money.getText().toString())
//					+ Double.parseDouble(yajin)
//					- Double.parseDouble(canye)
//					- Double.parseDouble(ping_total)
//					+ Double.parseDouble(tv_pj_money.getText().toString()
//							.trim()) + sumYouHui() + "");
			tv_pay.setText(getPayMoney()+"");
			if (myListView.arrList.size() != 0) {
				tv_youhui_select.setText(sumYouHui() + "");
				tv_songqi_money.setText(sumYouHuiContent());
			} else {
				tv_youhui_select.setText("");
				tv_songqi_money.setText("未添加优惠");
			}
		}
	}

	/**
	 * 计算实际付款
	 */
	public float getPayMoney() {
		float payMoney=0f;
		payMoney=Float.parseFloat(tv_money.getText().toString())+ getShiJiYaJin()- Float.parseFloat(ping_total)
		+ Float.parseFloat(tv_pj_money.getText().toString().trim()) + sumYouHui();
		if(!TextUtils.isEmpty(peijian)){
			payMoney-=Float.parseFloat(peijian);
		}
		if(!TextUtils.isEmpty(ranqi)){
			payMoney-=Float.parseFloat(ranqi);
		}
		return payMoney;
	}

	/**
	 * 计算优惠价格
	 * 
	 * @return
	 */
	private float youhuijine;// 优惠金额

	public int sumYouHui() {
		int youhuimoney = 0;
		isyouhui = 0;
		iszhekou = 0;
		youhuijine = 0f;
		if (myListView != null && myListView.arrList != null) {
			for (int i = 0; i < myListView.arrList.size(); i++) {
				if (youhHuiZheKou
						.get(Integer.parseInt(myListView.arrList.get(i)))
						.getType().equals("1")) {// 优惠券
					isyouhui = 1;
					youhuijine += youhHuiZheKou.get(
							Integer.parseInt(myListView.arrList.get(i)))
							.getMoney();
					youhuimoney -= youhHuiZheKou.get(
							Integer.parseInt(myListView.arrList.get(i)))
							.getMoney();
				} else if (youhHuiZheKou
						.get(Integer.parseInt(myListView.arrList.get(i)))
						.getType().equals("2")) {// 折扣的商品
					iszhekou = 1;
					youhuimoney += youhHuiZheKou.get(
							Integer.parseInt(myListView.arrList.get(i)))
							.getMoney();
				}
			}
		} else {
			return youhuimoney;
		}
		return youhuimoney;
	}

	/**
	 * 汇总优惠内容
	 * 
	 * @return
	 */
	public String sumYouHuiContent() {
		String youhuiContent = "";
		if (myListView != null && myListView.arrList != null) {
			for (int i = 0; i < myListView.arrList.size(); i++) {
				youhuiContent += youhHuiZheKou.get(
						Integer.parseInt(myListView.arrList.get(i))).getTitle()
						+ youhHuiZheKou.get(
								Integer.parseInt(myListView.arrList.get(i)))
								.getComment()
						+ "￥"
						+ youhHuiZheKou.get(
								Integer.parseInt(myListView.arrList.get(i)))
								.getMoney() + "\u3000";
			}
		} else {
			return youhuiContent;
		}
		return youhuiContent;
	}

	@Override
	public void chaiIntCallBack(int in, int where) {

		if (where == 1) {// 押金
			tv_pay.setText(Double.parseDouble(tv_money.getText().toString())
					+ in
					- Double.parseDouble(canye)
					- Double.parseDouble(ping_total)
					+ Double.parseDouble(tv_pj_money.getText().toString()
							.trim()) + sumYouHui() + "");
			tv_yajin.setText(in + "");
			yajin = "" + in;
		} else if (where == 2) {// 订单
			tv_pay.setText(in
					+ Double.parseDouble(tv_yajin.getText().toString())
					- Double.parseDouble(canye)
					- Double.parseDouble(ping_total)
					+ Double.parseDouble(tv_pj_money.getText().toString()
							.trim()) + sumYouHui() + "");
			tv_money.setText(in + "");
		}
	}

	private ArrayList<YouHuiZheKouInfo> youhHuiZheKou = new ArrayList<YouHuiZheKouInfo>();

	@Override
	public void sendResult(String result, int what) {
		Gson gson = new Gson();
		try {
			JSONObject jsob = new JSONObject(result);
			if (jsob.getInt("resultCode") == 1) {
				youhHuiZheKou.clear();
				JSONArray jsoa = jsob.getJSONArray("resultInfo");
				for (int i = 0; i < jsoa.length(); i++) {
					JSONObject jsb = jsoa.getJSONObject(i);
					String str = jsoa.getJSONObject(i).toString();
					if (jsb.getString("type").equals("1")) {// 优惠券
						youhHuiZheKou.add(gson.fromJson(str, YouHuiInfo.class));
					} else if (jsb.getString("type").equals("2")) {// 折扣商品
						youhHuiZheKou.add(gson.fromJson(str, ZheKouInfo.class));
					}
				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void chaiYaJinTiaoStringCallBack(String str) {
		if (!TextUtils.isEmpty(str)) {
			tv_yajintiao.setText(str);
		}
	}

}
