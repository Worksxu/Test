package com.ruiqi.chai;

import java.io.Serializable;
import java.util.ArrayList;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.adapter.ZheiJiuAdapter;
import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.bean.ZheiJiu;
import com.ruiqi.fragment.YajinFragment;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;
import com.ruiqi.works.BaseActivity;
import com.ruiqi.works.CreateOrderActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.NullActivity;
import com.ruiqi.works.OrderInfoActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.SelfActivity;
import com.ruiqi.works.WeightActivity;

//押金和折现界面
public class ChaiSubsidiaryActivity extends BaseActivity implements
		OnCheckedChangeListener ,PopDismiss{

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			getResult(result);
		}

	};

	private RelativeLayout rl_select;
	private ProgressDialog pd;

	private ListView lv_select_address;

	private LinearLayout rl_all;

	private ToggleButton tb_jiuping, tb_canye;

	private LinearLayout rl_canye_content;

	private LinearLayout rl_jiupin;

	private TextView tv_next;

	private TextView tv_sum_content;

	private EditText et_money, et_money_1, et_money_2,et_money_3;

	private EditText et_money_input, et_new_input;

	private ImageView iv_peijian_add, iv_ping_add, iv_ping_add_1;
	private ImageView iv_peijian_sup, iv_ping_sup, iv_ping_sup_1;
	private EditText et_peijian_num_1, et_ping_num, et_ping_num_1;

	public static List<ZheiJiu> mList;
	private List<ZheiJiu> tList;
	private ListView lv_content;

	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sub);
		setTitle("押金和折现");

		mTypeList = new ArrayList<Type>();
		mConeTypeList = new ArrayList<Type>();
		tList = new ArrayList<ZheiJiu>();
		mList = new ArrayList<ZheiJiu>();
		from = (String) SPUtils.get(ChaiSubsidiaryActivity.this, "createorder","error");
		list = new ArrayList<Type>();
		isDeposit();
		init();
		setFragmentFromUser();
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.e("lll", "返回押金自己的");
	}
	private void init() {
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		rl_select = (RelativeLayout) findViewById(R.id.rl_select);
		rl_select.setOnClickListener(this);

		lv_select_address = (ListView) findViewById(R.id.lv_select_address);

		rl_type = (RelativeLayout) findViewById(R.id.rl_content);
		rl_all = (LinearLayout) findViewById(R.id.rl_all);

		tb_jiuping = (ToggleButton) findViewById(R.id.tb_user);
		tb_jiuping.setOnCheckedChangeListener(this);

		tb_canye = (ToggleButton) findViewById(R.id.tb_canye);
		tb_canye.setOnCheckedChangeListener(this);

		rl_canye_content = (LinearLayout) findViewById(R.id.rl_canye_content);

		rl_jiupin = (LinearLayout) findViewById(R.id.rl_jiupin);
		rl_jiupin.setVisibility(View.GONE);

		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_next.setOnClickListener(this);

		tv_sum_content = (TextView) findViewById(R.id.tv_sum_content);

		et_money = (EditText) findViewById(R.id.et_money);
		et_money_1 = (EditText) findViewById(R.id.et_money_1);
		et_money_2 = (EditText) findViewById(R.id.et_money_2);
		et_money_3 = (EditText) findViewById(R.id.et_money_3);

		et_money_input = (EditText) findViewById(R.id.et_money_input);

		et_peijian_num_1 = (EditText) findViewById(R.id.et_peijian_num_1);
		et_ping_num = (EditText) findViewById(R.id.et_ping_num);
		et_ping_num_1 = (EditText) findViewById(R.id.et_ping_num_1);
		

		iv_peijian_add = (ImageView) findViewById(R.id.iv_peijian_add);
		iv_peijian_add.setOnClickListener(this);

		iv_ping_add = (ImageView) findViewById(R.id.iv_ping_add);
		iv_ping_add.setOnClickListener(this);

		iv_ping_add_1 = (ImageView) findViewById(R.id.iv_ping_add_1);
		iv_ping_add_1.setOnClickListener(this);

		iv_peijian_sup = (ImageView) findViewById(R.id.iv_peijian_sup);
		iv_peijian_sup.setOnClickListener(this);

		iv_ping_sup = (ImageView) findViewById(R.id.iv_ping_sup);
		iv_ping_sup.setOnClickListener(this);

		iv_ping_sup_1 = (ImageView) findViewById(R.id.iv_ping_sup_1);
		iv_ping_sup_1.setOnClickListener(this);

		et_new_input = (EditText) findViewById(R.id.et_new_input);

		lv_content = (ListView) findViewById(R.id.lv_content);
	}

	private int num;

	// 需收取的押金的数目
	// 判断是否需要收取押金,收几个瓶的押金
	private void isDeposit() {
		// TODO 这里条件缺失
		if (CreateOrderActivity.OrderKind == 1
				|| CreateOrderActivity.OrderKind == 2
				|| CreateOrderActivity.OrderKind == 3||CreateOrderActivity.OrderKind == 4) {
			if (NfcActivity.mDataNull == null) {
				NfcActivity.mDataNull = new ArrayList<Weight>();
			}
			if (NfcActivity.mDataWeight == null) {
				NfcActivity.mDataWeight = new ArrayList<Weight>();
			}
			num = NfcActivity.mDataWeight.size() - NfcActivity.mDataNull.size();

			System.err.println("NfcActivity.mDataWeight.size()==="+ NfcActivity.mDataWeight.size()+ "NfcActivity.mDataNull.size()"+ NfcActivity.mDataNull.size());

		}

	}

	@Override
	public void jumpPage() {
		super.jumpPage();
		if ("weightbottle".equals(getIntent().getStringExtra("flag"))) {
			Intent intent = new Intent(ChaiSubsidiaryActivity.this,ChaiWeightActivity.class);
			intent.putExtra("show", getIntent().getStringExtra("show"));
			startActivity(intent);
		} else if ("nullbottle".equals(getIntent().getStringExtra("flag"))) {
			Intent intent = new Intent(ChaiSubsidiaryActivity.this,ChaiNullActivity.class);
			intent.putExtra("show", getIntent().getStringExtra("show"));
			startActivity(intent);
		}
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("weightbottle".equals(getIntent().getStringExtra("flag"))) {
				Intent intent = new Intent(ChaiSubsidiaryActivity.this,ChaiWeightActivity.class);
				intent.putExtra("show", getIntent().getStringExtra("show"));
				startActivity(intent);
			} else if ("nullbottle".equals(getIntent().getStringExtra("flag"))) {
				Intent intent = new Intent(ChaiSubsidiaryActivity.this,ChaiNullActivity.class);
				intent.putExtra("show", getIntent().getStringExtra("show"));
				startActivity(intent);
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_select:
			rl_select.setEnabled(false);
			initType(v);
			break;
		case R.id.iv_peijian_add:
			add(et_peijian_num_1);
			break;
		case R.id.iv_ping_add:
			add(et_ping_num);
			break;
		case R.id.iv_ping_add_1:
			add(et_ping_num_1);
			break;

		case R.id.iv_peijian_sup:
			sup(et_peijian_num_1);
			break;
		case R.id.iv_ping_sup:
			sup(et_ping_num);
			break;
		case R.id.iv_ping_sup_1:
			sup(et_ping_num_1);
			break;
		case R.id.tv_next:
			if ("nullbottle".equals(getIntent().getStringExtra("flag"))) {
				{
					int depositNum = 0;
					for (int i = 0; i < list.size(); i++) {
						depositNum += Integer.parseInt(list.get(i).getNum());
					}
					// 判断选择押金的数目
					if (depositNum < num) {
						T.showShort(ChaiSubsidiaryActivity.this, "选择押金的数量少了");
					} else if (depositNum > num) {
						T.showShort(ChaiSubsidiaryActivity.this, "选择押金的数量多了");
					} else if (depositNum == num) {
						// 跳转
						saveData();
						if (setJumpPage() && setCanYeJump()) {
							Intent intent = new Intent(ChaiSubsidiaryActivity.this,ChaiOrderConfirmActivity.class);
							startActivity(intent);
						}
					}
				}
			} else {
				System.out.println("走下面");
				saveData();
				if (setJumpPage() && setCanYeJump()) {
					Intent intent = new Intent(ChaiSubsidiaryActivity.this,SelfActivity.class);
					startActivity(intent);
				}
			}
			break;
		default:
			break;
		}
	}

	private String money_1, money_2, money_3,money_4, canye, canye_weight, num_1,num_2, num_3, yajin;
	private double ping_total;
	private void saveData() {
		// 获取
		yajin = tv_sum_content.getText().toString().trim();
		money_1 = et_money.getText().toString().trim();
		money_2 = et_money_1.getText().toString().trim();
		money_3 = et_money_2.getText().toString().trim();
		money_4 = et_money_3.getText().toString().trim();
		canye = et_money_input.getText().toString().trim();
		canye_weight = et_new_input.getText().toString().trim();
		
		/*
		 * num_1 = et_peijian_num_1.getText().toString().trim(); num_2 =
		 * et_ping_num.getText().toString().trim(); num_3 =
		 * et_ping_num_1.getText().toString().trim();
		 * if(num_1==null||num_1.equals("")){ num_1="0"; }
		 * 
		 * if(Integer.parseInt(num_1)>0){ mList.add(new ZheiJiu("5KG", num_1));
		 * }
		 * 
		 * if(num_2==null||num_2.equals("")){ num_2="0"; }
		 * if(Integer.parseInt(num_2)>0){ mList.add(new ZheiJiu("15KG", num_2));
		 * }
		 * 
		 * if(num_3==null||num_3.equals("")){ num_3="0"; }
		 * if(Integer.parseInt(num_3)>0){ mList.add(new ZheiJiu("50KG", num_3));
		 * }
		 */
		
		mList = new ArrayList<ZheiJiu>();
		for (int i = 0; i < tList.size(); i++) {
			if (Integer.parseInt(tList.get(i).getNum()) > 0) {
				mList.add(new ZheiJiu(tList.get(i).getWeight(), tList.get(i).getNum(), tList.get(i).getId(), "0"));
			}
		}
		if (yajin == null || yajin.equals("")) {
			yajin = "0";
		}
		if (money_1 == null || money_1.equals("")) {
			money_1 = "0";
		}
		if (money_2 == null || money_2.equals("")) {
			money_2 = "0";
		}
		if (money_3 == null || money_3.equals("")) {
			money_3 = "0";
		}
		if (money_4 == null || money_4.equals("")) {
			money_4 = "0";
		}
		if (canye == null || canye.equals("")) {
			canye = "0";
		}
		if (canye_weight == null || canye_weight.equals("")) {
			canye_weight = "0";
		}
		System.out.println("yajin=" + yajin);
		ping_total = Double.parseDouble(money_1) + Double.parseDouble(money_2)+ Double.parseDouble(money_3)+Double.parseDouble(money_4);
		SPUtils.put(ChaiSubsidiaryActivity.this, "deposit", yajin);
		Log.e("lll", "num"+num);
		SPUtils.put(ChaiSubsidiaryActivity.this, "deposit_num", num+"");
		SPUtils.put(ChaiSubsidiaryActivity.this, "ping_total", ping_total + "");//折旧的钱
		SPUtils.put(ChaiSubsidiaryActivity.this, "canye", canye);
		SPUtils.put(ChaiSubsidiaryActivity.this, "canye_weight", canye_weight);
	}

	// 折旧瓶的判断跳转
	private boolean setJumpPage() {
		if (tList.size() > 0) {

			if (Integer.parseInt(tList.get(0).getNum()) > 0) {
				if (Double.parseDouble(money_1) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请输入5KG折旧瓶的金钱");
					return false;
				}
			}
			if (Double.parseDouble(money_1) > 0) {
				if (Integer.parseInt(tList.get(0).getNum()) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请选择5KG折旧瓶的数目");
					return false;
				}
			}
			if (Integer.parseInt(tList.get(1).getNum()) > 0) {
				if (Double.parseDouble(money_2) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请输入15KG折旧瓶的金钱");
					return false;
				}
			}
			if (Double.parseDouble(money_2) > 0) {
				if (Integer.parseInt(tList.get(1).getNum()) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请选择15KG折旧瓶的数目");
					return false;
				}
			}
			if (Integer.parseInt(tList.get(2).getNum()) > 0) {
				if (Double.parseDouble(money_3) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请输入50KG折旧瓶的金钱");
					return false;
				}
			}
			if (Double.parseDouble(money_3) > 0) {
				if (Integer.parseInt(tList.get(2).getNum()) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请选择50KG折旧瓶的数目");
					return false;
				}
			}
			if (Integer.parseInt(tList.get(3).getNum()) > 0) {
				if (Double.parseDouble(money_4) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请输入50KG(Ⅱ)折旧瓶的金钱");
					return false;
				}
			}
			if (Double.parseDouble(money_4) > 0) {
				if (Integer.parseInt(tList.get(3).getNum()) == 0) {
					T.showShort(ChaiSubsidiaryActivity.this, "请选择50KG(Ⅱ)折旧瓶的数目");
					return false;
				}
			}
		}
		return true;
	}

	// 残液的跳转判断
	private boolean setCanYeJump() {
		if (Double.parseDouble(canye_weight) > 0) {
			if (Double.parseDouble(canye) == 0) {
				T.showShort(ChaiSubsidiaryActivity.this, "请输入折旧金额");
				return false;
			}
		}
		if (Double.parseDouble(canye) > 0) {
			if (Double.parseDouble(canye_weight) == 0) {
				T.showShort(ChaiSubsidiaryActivity.this, "请输入残液重量");
				return false;
			}
		}
		return true;
	}

	private List<Type> mTypeList;
	private List<Type> mConeTypeList;

	private void initType(View v) {
		CurrencyUtils.dissMiss(ChaiSubsidiaryActivity.this, v);

		if (mTypeList.size() == 0) {
			pd.show();
			String shop_id = (String) SPUtils.get(ChaiSubsidiaryActivity.this,
					SPutilsKey.SHOP_ID, "");
			int token = (Integer) SPUtils.get(ChaiSubsidiaryActivity.this,
					SPutilsKey.TOKEN, 0);
			// 请求网络
			RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
			params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
			params.addBodyParameter(SPutilsKey.TOKEN, token + "");
			HttpUtil.PostHttp(params, typeHandler, pd);

		} else {
			// 创建适配器
			typeAdapter = new TypePopupAdapter(mTypeList,ChaiSubsidiaryActivity.this);

			// 填充适配器
			old = new SelectOrderInfoPopupWindow(ChaiSubsidiaryActivity.this,
					itemsOnClickType, lv_select_address, typeAdapter,onclickType);
			old.setPopDismiss(this);
			old.showAtLocation(ChaiSubsidiaryActivity.this.findViewById(R.id.ll_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}

	private Handler typeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				parseData(result, 2);
			}
		}

	};
	private SelectOrderInfoPopupWindow old;
	private TypePopupAdapter typeAdapter;

	private void parseData(String result, int i) {
		if (i == 2) {
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if (resultCode == 1) {
					// 继续解析
					JSONArray array = obj.getJSONArray("resultInfo");
					for (int j = 0; j < array.length(); j++) {
						JSONObject object = array.getJSONObject(j);
						String bottle_name = object.getString("typename");
						String price = object.getString("yj_price");
						if(object.getString("type").equals("1")){
							mTypeList.add(new Type(price, bottle_name, "0"));
						}
						// 将数据保存至数据库中
						// 1,查询数据库，看看是否有该类型的钢瓶
					}
					// 创建适配器
					typeAdapter = new TypePopupAdapter(mTypeList,
							ChaiSubsidiaryActivity.this);

					// 填充适配器
					old = new SelectOrderInfoPopupWindow(
							ChaiSubsidiaryActivity.this, itemsOnClickType,
							lv_select_address, typeAdapter, onclickType);
					old.setPopDismiss(this);
					old.showAtLocation(
							ChaiSubsidiaryActivity.this.findViewById(R.id.ll_main),
							Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// listview子项的点击
	private OnItemClickListener itemsOnClickType = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

		}
	};

	// 确定取消按钮点击
	private OnClickListener onclickType = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_sure:// 确定
				// 将选择的气罐类型添加到下面
				typeSure();
				break;
			case R.id.tv_quxiao:// 取消
				if (mTypeList.size() != 0) {
					mTypeList.clear();
				}
				for (int i = 0; i < mConeTypeList.size(); i++) {
					mTypeList.add(new Type(mConeTypeList.get(i).getPrice(),
							mConeTypeList.get(i).getWeight(), mConeTypeList.get(i).getNum()));
				}
				old.dismiss();
				break;

			default:
				break;
			}
		}
	};
	private List<Type> list;
	private RelativeLayout rl_type;

	private void typeSure() {
		if (list.size() != 0) {
			list.clear();
		}
		if (mConeTypeList.size() != 0) {
			mConeTypeList.clear();
		}
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());
			if (num > 0) {
				list.add(new Type(Double.parseDouble(mTypeList.get(i)
						.getPrice()) * num + "", mTypeList.get(i).getWeight(),mTypeList.get(i).getNum()));
			}
			mConeTypeList.add(new Type(mTypeList.get(i).getPrice(), mTypeList
					.get(i).getWeight(), mTypeList.get(i).getNum()));
		}
		if (list.size() > 0) {
			double sum = 0;
			rl_all.setVisibility(View.VISIBLE);
			for (int i = 0; i < list.size(); i++) {
				sum += Double.parseDouble(list.get(i).getPrice());
			}
			tv_sum_content.setText(sum + "");
			YajinFragment tf = new YajinFragment();
			// 将选中的值传递给fragment
			Bundle bundle = new Bundle();
			bundle.putSerializable("mDatas", (Serializable) list);
			tf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rl_content, tf).commit();
		} else {
			tv_sum_content.setText("0.0");
			rl_type.removeAllViews();
			rl_all.setVisibility(View.GONE);
		}
		old.dismiss();
	}

	// 根据用户的身份设置押金的选择方式
	private void setFragmentFromUser() {
		if (SPutilsKey.type == 1) {// 新用户
			rl_select.setVisibility(View.GONE);
			double sum = 0;
			YajinFragment tf = new YajinFragment();
			Bundle bundle = new Bundle();
			rl_all.setVisibility(View.VISIBLE);
			if ("ChaiCreateOrderActivity".equals(from)) {
				List<Type> list = new ArrayList<Type>();
				for (int i = 0; i < CreateOrderActivity.list.size(); i++) {
					Type t = CreateOrderActivity.list.get(i);
					sum += Double.parseDouble(SPutilsKey.list.get(i).getYj_price());
					list.add(new Type(Double.parseDouble(t.getYj_price())* Double.parseDouble(t.getNum()) + "", t.getWeight(), t.getNum()));
				}
				bundle.putSerializable("mDatas", (Serializable) list);
			} else {
				List<Type> list = new ArrayList<Type>();
				for (int i = 0; i < OrderInfoActivity.list.size(); i++) {
					Orderdeail o = OrderInfoActivity.list.get(i);
					sum += Double.parseDouble(o.getGoods_price())* Double.parseDouble(o.getNum());
					list.add(new Type(Double.parseDouble(o.getGoods_price())* Double.parseDouble(o.getNum()) + "", o.getGoods_kind(), o.getNum()));
				}
				bundle.putSerializable("mDatas", (Serializable) list);
			}
			tv_sum_content.setText(sum + "");
			// 将选中的值传递给fragment
			tf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.rl_content, tf).commit();
		}
	}

	/**
	 * 增加的方法
	 */
	private void add(EditText et) {
		String result = et.getText().toString();
		et.setText(Integer.parseInt(result) + 1 + "");
	}

	/**
	 * 减少的方法
	 */
	private void sup(EditText et) {
		String result = et.getText().toString();
		if (Integer.parseInt(result) < 1) {
			T.showShort(ChaiSubsidiaryActivity.this, "数目已达最小");
		} else {
			et.setText(Integer.parseInt(result) - 1 + "");
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.tb_user:
			if (isChecked) {// 有旧瓶折现
				tList = new ArrayList<ZheiJiu>();
				rl_jiupin.setVisibility(View.VISIBLE);
				// 请求网络
				pd.show();
				RequestParams params = new RequestParams(RequestUrl.BOOTLE_LIST);
				HttpUtil.PostHttp(params, handler, pd);
			} else {// 没有旧瓶折现
				rl_jiupin.setVisibility(View.GONE);
			}

			break;
		case R.id.tb_canye:
			if (isChecked) {// 有残液折现
				rl_canye_content.setVisibility(View.VISIBLE);
			} else {// 没有残液折现
				rl_canye_content.setVisibility(View.GONE);
			}
			break;

		default:
			break;
		}
	}

	private ZheiJiuAdapter adapter;

	private void getResult(String result) {
		System.out.println("result=" + result);
		// json解析
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				JSONArray array = obj.getJSONArray("resultInfo");
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj1 = array.getJSONObject(i);
					String id = obj1.getString("id");
					String bottle_name = obj1.getString("bottle_name");
					tList.add(new ZheiJiu(bottle_name, "0", id, "0"));
				}
				// 放到list上
				adapter = new ZheiJiuAdapter(tList, ChaiSubsidiaryActivity.this);
				lv_content.setAdapter(adapter);
				CurrencyUtils.setListViewHeightBasedOnChildren(lv_content);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void popDismissCalBack() {
		rl_select.setEnabled(true);
	}

}
