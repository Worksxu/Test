package com.ruiqi.works;

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
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ruiqi.adapter.ChaiTypePopupAdapter1;
import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.OldUserInfo;
import com.ruiqi.bean.Type;
import com.ruiqi.db.GpDao;
import com.ruiqi.fragment.TypeFragment;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.SelectAddress;
import com.ruiqi.utils.SelectAddress.AddressConfirm;
import com.ruiqi.utils.T;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;

/**
 * 创建订单界面
 * 
 * @author Administrator
 *
 */
public class CreateOrderActivity extends BaseActivity implements
		OnCheckedChangeListener, AddressConfirm, PopDismiss,android.widget.RadioGroup.OnCheckedChangeListener {

	private ToggleButton tb;
	private RelativeLayout rl_user;// 用来存放动态增加的布局
	// 父布局
	private RelativeLayout rl_selectType;
	private RelativeLayout rl_selectTime, rl_newUserAddress;

	private TextView tv_time_result;

	private RelativeLayout rl_time;  
	private View timeView;
	private RelativeLayout rl_type;
	// 新客户
	private View newUserView;

	// 老用户
	private View oldUserView;
	private EditText et_input;
	private ImageView select;
	private String oldUserMobile;
	private List<OldUserInfo> mOldDatas;
	private ListView lv_select_address;
	private RadioGroup rg_user_type;

	private RelativeLayout rl_old_content, rl_selectTiYanTaoCan,rl_selectYouHuiTaoCan,rl_selectParts;
	private ProgressDialog pd;

	public static int OrderKind = 1;// 1正常订单，2体验套餐，3优惠套餐，4混合套餐
	public static int GasListKind=0;//商品选择类型

	private TextView tv_start, tv_select;// 开始和保存

	private EditText et_new_input, et_new_inputMobile, et_new_customer_card;
	private TextView et_new_inputAddress;

	private Handler commitHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paraseData(result);
		}
	};
	private GpDao gd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_order);
		setTitle("创建订单");
		gd = GpDao.getInstances(this);
		mTypeList = new ArrayList<Type>();
		mConeTypeList = new ArrayList<Type>();
		this.shengcode = "";
		this.shicode = "";
		this.qucode = "";
		this.cuncode = "";
		this.detail = "";
		init();
		initOldUser();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private SelectAddress selectAddress;

	// 组件初始化
	private void init() {
		selectAddress = new SelectAddress(this);
		selectAddress.setOnAddressConfirm(this);
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载");
		SPutilsKey.neworold=2;
		SPutilsKey.type = 2;

		view = LayoutInflater.from(CreateOrderActivity.this).inflate(R.layout.olduserinfo_content, null);

		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_mobile = (TextView) view.findViewById(R.id.tv_mobile);
		tv_address = (TextView) view.findViewById(R.id.tv_address);
		tv_yuping = (TextView) view.findViewById(R.id.tv_yuping);
		
		rl_selectType = (RelativeLayout) findViewById(R.id.rl_selectType);
		rl_selectType.setOnClickListener(this);

		rl_selectTiYanTaoCan = (RelativeLayout) findViewById(R.id.rl_selectTiYanTaoCan);
		rl_selectTiYanTaoCan.setOnClickListener(this);

		rl_selectYouHuiTaoCan = (RelativeLayout) findViewById(R.id.rl_selectYouHuiTaoCan);
		rl_selectYouHuiTaoCan.setOnClickListener(this);

		// rl_selectYouHuiTaoCan.setVisibility(View.GONE);
		// rl_selectTiYanTaoCan.setVisibility(View.GONE);

		rl_selectTime = (RelativeLayout) findViewById(R.id.rl_selectTime);
		rl_selectTime.setOnClickListener(this);
		//rl_selectTime.setVisibility(View.GONE);

		rl_time = (RelativeLayout) findViewById(R.id.rl_time);
		rl_selectParts = (RelativeLayout) findViewById(R.id.rl_selectParts);
		rl_selectParts.setOnClickListener(this);
		
		rl_type = (RelativeLayout) findViewById(R.id.rl_type);
		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_select = (TextView) findViewById(R.id.tv_select);
		tv_start.setOnClickListener(this);
		tv_select.setOnClickListener(this);
		tb = (ToggleButton) findViewById(R.id.tb_user);
		
		tb.setOnCheckedChangeListener(this);
		newUserView = LayoutInflater.from(CreateOrderActivity.this).inflate(R.layout.new_user, null);
		oldUserView = LayoutInflater.from(CreateOrderActivity.this).inflate(R.layout.old_user, null);
		timeView = LayoutInflater.from(CreateOrderActivity.this).inflate(R.layout.time, null);
		tv_time_result = (TextView) timeView.findViewById(R.id.tv_time_result);

		et_new_input = (EditText) newUserView.findViewById(R.id.et_new_input);
		et_new_inputMobile = (EditText) newUserView
				.findViewById(R.id.et_new_inputMobile);
		et_new_inputAddress = (TextView) newUserView
				.findViewById(R.id.et_new_inputAddress);
		rl_newUserAddress = (RelativeLayout) newUserView
				.findViewById(R.id.rl_newUserAddress);
		rl_newUserAddress.setOnClickListener(this);

		et_new_customer_card = (EditText) newUserView
				.findViewById(R.id.et_new_customer_card);
		rg_user_type = (RadioGroup) newUserView.findViewById(R.id.rg_user_type);
		rg_user_type.setOnCheckedChangeListener(this);

		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		// 设置默认的布局,为老用户布局
		rl_user.addView(oldUserView);
	}

	/**
	 * 老用户初始化
	 */
	private void initOldUser() {
		et_input = (EditText) oldUserView.findViewById(R.id.et_input);
		select = (ImageView) oldUserView.findViewById(R.id.select);
		lv_select_address = (ListView) oldUserView.findViewById(R.id.lv_select_address);
		rl_old_content = (RelativeLayout) findViewById(R.id.rl_old_content);
		select.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.select:// 搜索
			// 请求网络，加载数据
			initOldData(v);
			break;
		case R.id.rl_selectType:// 选择气罐规格
			// 请求网络，加载数据,得到气罐规格列表
			if(userFlag){
				if (tv_mobile.getText().toString() == null|| tv_mobile.getText().toString().equals("")) {
					Toast.makeText(CreateOrderActivity.this, "请选择客户",Toast.LENGTH_SHORT).show();
					return;
				}
			}
			OrderKind = 1;
			setEnableSelect(false, false, false,false);
			GasListKind=0;
			initType(v, OrderKind,GasListKind);
			break;
		case R.id.rl_selectTiYanTaoCan:// 选择体验套餐
			if(userFlag){
				if (tv_mobile.getText().toString() == null|| tv_mobile.getText().toString().equals("")) {
					Toast.makeText(CreateOrderActivity.this, "请选择客户",Toast.LENGTH_SHORT).show();
					return;
				}
			}
			OrderKind = 2;
			setEnableSelect(false, false, false,false);
			GasListKind=0;
			initType(v, OrderKind,GasListKind);
			break;
		case R.id.rl_selectYouHuiTaoCan:// 选择优惠套餐
			if(userFlag){
				if (tv_mobile.getText().toString() == null|| tv_mobile.getText().toString().equals("")) {
					Toast.makeText(CreateOrderActivity.this, "请选择客户",Toast.LENGTH_SHORT).show();
					return;
				}
			}else{
				
			}
			if(SPutilsKey.kehuyou==2  || !userFlag){//判断客户是否有余瓶
				OrderKind = 3;
				setEnableSelect(false, false, false,false);
				GasListKind=0;
				initType(v, OrderKind,GasListKind);//客户没有余瓶选择优惠套餐
			}else if(SPutilsKey.kehuyou==1){
				OrderKind = 3;
				setEnableSelect(false, false, false,false);
				chaiInitPopView(dataString);
			}
			break;
		case R.id.rl_selectTime:// 选择混合套餐
			if(userFlag){
				if (tv_mobile.getText().toString() == null|| tv_mobile.getText().toString().equals("")) {
					Toast.makeText(CreateOrderActivity.this, "请选择客户",Toast.LENGTH_SHORT).show();
					return;
				}
			}else{
				
			}
			if(SPutilsKey.kehuyou==2  || !userFlag){//判断客户是否有余瓶
				OrderKind = 4;
				setEnableSelect(false, false, false,false);
				GasListKind=1;
				initType(v, OrderKind,GasListKind);//客户没有余瓶选择正常
			}else if(SPutilsKey.kehuyou==1){
				OrderKind = 4;
				setEnableSelect(false, false, false,false);
				chaiInitPopView(dataString);
			}
			//rl_time.removeAllViews();
			//rl_time.addView(timeView);
			// 弹出选择时间对话框
			//CurrencyUtils.showDataSelector(CreateOrderActivity.this,tv_time_result);
			break;
		case R.id.rl_selectParts://选择配件
			Toast.makeText(this, "选择配件", Toast.LENGTH_SHORT).show();
			break;
		case R.id.tv_start:// 开始订单
			// 保存数据
			isStart=1;
			saveData();
			// 跳转到扫瓶界面
			break;
		case R.id.tv_select:// 保存订单
			isStart=2;
			saveOrder();
			break;
		case R.id.rl_newUserAddress:// 创建地址
			selectAddress.showSelectAddress();
			break;
		default:
			break;
		}
	}

	private int isStart;//1 开始订单 2保存订单
	/**
	 * 订单选择类型是否可选
	 */
	public void setEnableSelect(boolean flag1, boolean flag2, boolean flag3,boolean flag4) {
		rl_selectType.setEnabled(flag1);// 正常订单
		rl_selectTiYanTaoCan.setEnabled(flag2);// 体验套餐
		rl_selectYouHuiTaoCan.setEnabled(flag3);// 优惠套餐
		rl_selectTime.setEnabled(flag4);// 优惠套餐
	}

	private String customercard;
	private void saveData() {
		System.out.println("开始订单");
		SPUtils.put(CreateOrderActivity.this, "createOrderMoney", money);
		SPUtils.put(CreateOrderActivity.this, "createorder","CreateOrderActivity");
		if (userFlag == true) {// 老用户
			SPutilsKey.neworold=2;
			SPutilsKey.type = 2;
			/*
			 * if(tv_name.getText().toString()==null||tv_name.getText().toString(
			 * ).equals("")){ Toast.makeText(CreateOrderActivity.this,
			 * "客户姓名不能为空", Toast.LENGTH_SHORT).show(); }
			 */
			if (tv_mobile.getText().toString() == null
					|| tv_mobile.getText().toString().equals("")) {
				Toast.makeText(CreateOrderActivity.this, "客户电话不能为空",Toast.LENGTH_SHORT).show();
				return;
			}
			/*
			 * if(tv_address.getText().toString()==null||tv_address.getText().
			 * toString().equals("")){ Toast.makeText(CreateOrderActivity.this,
			 * "客户地址不能为空", Toast.LENGTH_SHORT).show(); } if(money==0){
			 * Toast.makeText(CreateOrderActivity.this, "必须选择气罐类型",
			 * Toast.LENGTH_SHORT).show(); }
			 */
			SPUtils.put(CreateOrderActivity.this, "oldUserName", tv_name.getText().toString());
			SPUtils.put(CreateOrderActivity.this, "oldUserMobile", tv_mobile.getText().toString());
			SPUtils.put(CreateOrderActivity.this, "oldUserAddress", tv_address.getText().toString());
			int num = 0;
			if (CreateOrderActivity.list != null) {
				for (int i = 0; i < CreateOrderActivity.list.size(); i++) {
					Type t = CreateOrderActivity.list.get(i);
					num += Integer.parseInt(t.getNum());
				}
				//SPutilsKey.GANGPINGZONGSHU = num;
				// System.out.println("ORDER_num"+num);
			}
			System.out.println("CreateOrderActivity.list"+ CreateOrderActivity.list == null);
			System.out.println("num======" + num);
			if (num == 0) {
				Toast.makeText(CreateOrderActivity.this, "必须选择商品",Toast.LENGTH_SHORT).show();
				return;
			}

		} else if (userFlag == false) {// 新用户
			SPutilsKey.neworold=1;//新客户
			SPutilsKey.type = 2;
			SPutilsKey.kehuyou=2;
			// 先判断是否是正确的手机号码
			String newUserMobile = et_new_inputMobile.getText().toString().trim();
			customercard = et_new_customer_card.getText().toString().trim();
//			if (!IsPhone.isOrNotPhone(newUserMobile)) {// 判断是不是手机号
//				T.showShort(CreateOrderActivity.this, "电话号码有误，请重新输入");
//				return;
//			}
			if (!panDuanShiYiWei(newUserMobile)) {// 判断是不是手机号
				T.showShort(CreateOrderActivity.this, "电话号码有误，请重新输入");
				return;
			}

			if (TextUtils.isEmpty(customercard)) {
				T.showShort(CreateOrderActivity.this, "客户充值卡不能为空");
				return;
			}
			if (customercard.length() != 6) {
				
				T.showShort(CreateOrderActivity.this, "请输入正确的用户卡号");
				return;
			}
			if ((et_new_inputAddress.getText().toString().trim())
					.equals("请输入客户地址")) {
				T.showShort(CreateOrderActivity.this, "请输入正确的地址");
				return;
			}
			SPUtils.put(CreateOrderActivity.this, "newUserName", et_new_input.getText().toString());
			SPUtils.put(CreateOrderActivity.this, "newUserMobile",newUserMobile);
			SPUtils.put(CreateOrderActivity.this, "newUserAddress",et_new_inputAddress.getText().toString());
			SPUtils.put(CreateOrderActivity.this, "customercard", customercard);
			SPUtils.put(CreateOrderActivity.this, "", customercard);
		}

		if(SPutilsKey.kehuyou==1){//客户有瓶
			
		}else{
			int num = 0;
			if (CreateOrderActivity.list != null) {
				for (int i = 0; i < CreateOrderActivity.list.size(); i++) {
					Type t = CreateOrderActivity.list.get(i);
					num += Integer.parseInt(t.getNum());
				}
				SPutilsKey.GANGPINGZONGSHU = num;
				System.out.println("ORDER_num" + num);
			}
			if (money == 0) {
				Toast.makeText(CreateOrderActivity.this, "必须选择商品",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}
		Log.e("lll开始客户是否有余瓶", SPutilsKey.kehuyou+"");
		saveOrder();
		
//		Intent intent = new Intent(CreateOrderActivity.this,WeightActivity.class);
//		intent.putExtra("show", "CreateOrderActivity");
//		startActivity(intent);
	}
	/**
	 * 保存订单
	 */
	private String username, usermobile, useraddress, ship_id, shipper_name,
			mobile, status, shop_id;
	private String user_type;//客户类型 1居民 2 商业

	private void saveOrder() {
		System.out.println("保存订单");
		if (userFlag) {// 老用户
			System.out.println("老用户执行了=============");
			username = tv_name.getText().toString();
			usermobile = tv_mobile.getText().toString();
			useraddress = tv_address.getText().toString();
			status = "1";
			user_type="";
		} else {// 新用户
			System.out.println("新用户执行了=============");
			username = et_new_input.getText().toString();
			usermobile = et_new_inputMobile.getText().toString();
			useraddress = et_new_inputAddress.getText().toString();
			status = "0";
		}
		ship_id = (String) SPUtils.get(CreateOrderActivity.this,SPutilsKey.SHIP_ID, "error");
		shipper_name = (String) SPUtils.get(CreateOrderActivity.this,"shipper_name", "error");
		mobile = (String) SPUtils.get(CreateOrderActivity.this,SPutilsKey.MOBILLE, "error");
		shop_id = (String) SPUtils.get(CreateOrderActivity.this,SPutilsKey.SHOP_ID, "error");
		
		if(SPutilsKey.kehuyou==1){//客户有余瓶
			
		}else{//客户没有余瓶
			if (money == 0) {
				Toast.makeText(CreateOrderActivity.this, "必须选择气罐类型",Toast.LENGTH_SHORT).show();
				return;
			}
		}
		System.out.println("userMobile=" + usermobile);
		if (usermobile == null || "".equals(usermobile)) {
			T.showShort(CreateOrderActivity.this, "请输入客户电话");
			return;
		}
		if(!panDuanShiYiWei(usermobile)){
			T.showShort(CreateOrderActivity.this, "电话号码不正确");
			return;
		}
		pd.show();
		RequestParams params = new RequestParams(RequestUrl.CREATE_ORDER);
		params.addBodyParameter("mobile", usermobile);
		params.addBodyParameter("username", username);
		params.addBodyParameter("sheng", CreateOrderActivity.shengcode);// 市
		params.addBodyParameter("shi", CreateOrderActivity.shicode);// 县
		params.addBodyParameter("qu", CreateOrderActivity.qucode);// 镇
		params.addBodyParameter("cun", CreateOrderActivity.cuncode);// 村
		params.addBodyParameter("address",detail);// 详情
		
		params.addBodyParameter("shipper_name", shipper_name);
		params.addBodyParameter("shipper_mobile", mobile);
		params.addBodyParameter("status", status);// 判断用户身份?
		
		
		params.addBodyParameter("shop_id", shop_id);
		params.addBodyParameter("urgent", 0 + "");
		params.addBodyParameter("goodtime", CurrencyUtils.getNowTime());
		params.addBodyParameter("isold", "0");// 是否有折旧
		params.addBodyParameter("ismore", "0");// 是否有残页
		params.addBodyParameter("data", getJsonData(list));
		params.addBodyParameter("bottle_data", dataString);//客户剩余钢瓶数
		if (CreateOrderActivity.OrderKind == 1) {// 订单类型
			params.addBodyParameter("tc_type", "0");
			Log.e("lll客户卡", "0");
		} else if (CreateOrderActivity.OrderKind == 2) {
			params.addBodyParameter("tc_type", "4");
			Log.e("lll客户卡", "4");
		} else if (CreateOrderActivity.OrderKind == 3) {
			Log.e("lll客户卡", "5");
			params.addBodyParameter("tc_type", "5");
		}else if (CreateOrderActivity.OrderKind == 4) {
			Log.e("lll客户卡", "5");
			params.addBodyParameter("tc_type", "6");//混合套餐
		}
		
		if(SPutilsKey.kehuyou==1 &&(CreateOrderActivity.OrderKind == 3||CreateOrderActivity.OrderKind == 4)){//有瓶且优惠或混合
			params.addBodyParameter("money", "0");
		}else{
			params.addBodyParameter("money", money + "");
		}
		
		if(status.equals("0")){
			customercard = et_new_customer_card.getText().toString().trim();
			if(TextUtils.isEmpty(customercard)){
				T.showShort(CreateOrderActivity.this, "客户充值卡不能为空");
				return ;
			}
			params.addBodyParameter("user_type",user_type);// 用户类型 1居民 2商业
			params.addBodyParameter("card_sn",customercard);
		}
		
		Log.e("lll", "上传参数"+params.getStringParams().toString());
		
		tv_select.setEnabled(false);// 防重复保存订单
		tv_start.setEnabled(false);// 防重复保存订单
		HttpUtil.PostHttp(params, commitHandler, pd);
	}

	private void paraseData(String result) {
		tv_select.setEnabled(true);
		tv_start.setEnabled(true);
		Log.e("lll保存订单", result);
		try {
			JSONObject obj = new JSONObject(result);
			int resultCode = obj.getInt("resultCode");
			if (resultCode == 1) {
				// 空瓶
				JSONObject object = obj.getJSONObject("resultInfo");
				// 将数据存入share中
				// 地址
				// 总金额
				// 折旧和残液价钱
				if(isStart==1){//开始订单
					String kid=object.getString("kid");
					String ordersn=object.getString("ordersn");
					SPUtils.put(this, "ordersn", ordersn);
					SPUtils.put(this, "kid", kid);
					
					Intent intent = new Intent(CreateOrderActivity.this,WeightActivity.class);
					intent.putExtra("show", "CreateOrderActivity");
					startActivity(intent);
				}else if(isStart==2){//保存订单
					userFlag = true;
					Toast.makeText(CreateOrderActivity.this, "创建成功",Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(CreateOrderActivity.this,MainActivity.class);
					startActivity(intent);
				}
			} else {
				String str = obj.getString("resultInfo");
				Toast.makeText(CreateOrderActivity.this, str,Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
/**
 * 显示客户的余瓶
 * @param str
 */
	public static ArrayList<Type>  arrayListyuping;//保存客户余瓶
	public void chaiInitPopView(String str){
		if(arrayListyuping==null){
			arrayListyuping=new ArrayList<Type>();
		}
		arrayListyuping.clear();
		
		int max=0;
		int[] maxs = null;
		JSONArray jas;
				try {
					Log.e("lll", "执行到这了");
					mTypeList.clear();   
					jas = new JSONArray(str);
					maxs=new int[jas.length()];
					for (int j = 0; j < jas.length(); j++) {
						Log.e("lll", "执行到这了"+j);
						JSONObject object = jas.getJSONObject(j);
						String name = object.getString("good_name");
						String norm_id = object.getString("good_kind");
						String num = object.getString("good_num");
						maxs[j]=Integer.parseInt(num);
						max+=Integer.parseInt(num);
						SPutilsKey.GANGPINGZONGSHU=max;
						String type = "1";
						String price = object.getString("good_price");
						// String price = object.getString("price");
						String yj_price="0";
						if(object.has("good_deposit")){
							yj_price = object.getString("good_deposit");
						}
						String bottle_name = object.getString("good_name");
						String id = object.getString("good_id");
						if(!num.equals("0")){
							mTypeList.add(new Type(price, name, "0", id,norm_id, type, bottle_name, name, yj_price,num));
							arrayListyuping.add(new Type(price, name, num, id,norm_id, type, bottle_name, name, yj_price,num));
						}
					}
					int[] maxss=new int[jas.length()];
					for(int k=0,p=0;k<maxs.length;k++){
						if(maxs[k]!=0){
							maxss[p]=maxs[k];
							p++;
						}
					}
					typeAdapter1 = new ChaiTypePopupAdapter1(mTypeList,CreateOrderActivity.this,max,maxss,"");
					// 填充适配器
					Log.e("lll", "执行到这了11");
					old = new SelectOrderInfoPopupWindow(CreateOrderActivity.this, itemsOnClickType,lv_select_address, typeAdapter1, onclickType);
					old.setPopDismiss(this);
					old.showAtLocation(CreateOrderActivity.this.findViewById(R.id.ll_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
					Log.e("lll", "执行到这了12");
				} catch (JSONException e) {}
	}
	/**
	 * 气罐规格列表数据
	 */
	private List<Type> mTypeList;
	private List<Type> mConeTypeList;// 用于控制取消按钮的动作
	
/**
 * 选择套餐
 * @param v
 * @param OrderKind
 * @param kind
 */
	private void initType(View v, int OrderKind,int kind) {
		CurrencyUtils.dissMiss(CreateOrderActivity.this, v);
		String shop_id = (String) SPUtils.get(CreateOrderActivity.this,SPutilsKey.SHOP_ID, "");
		Integer token= (Integer) SPUtils.get(CreateOrderActivity.this,SPutilsKey.TOKEN, 0);
		if (OrderKind == 1||OrderKind==4) {
			RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
			params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
			params.addBodyParameter("user_type", user_type);
			params.addBodyParameter("token", token+"");
			HttpUtil.PostHttp(params, typeHandler, pd);
		} else if (OrderKind == 2 || OrderKind == 3) {
				RequestParams params = new RequestParams(RequestUrl.TAOCAN_TYPE);
				params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
				HttpUtil.PostHttp(params, typeHandler, pd);
		}
		// if(mTypeList.size()==0){
		// pd.show();
		// //请求网络
		// RequestParams params = new RequestParams(RequestUrl.BOTTLE_TYPE);
		// //套餐类型
		// RequestParams params = new RequestParams(RequestUrl.TAOCAN_TYPE);
		// params.addBodyParameter(SPutilsKey.SHOP_ID, shop_id);
		// HttpUtil.PostHttp(params, typeHandler, pd);
		// }else{
		// System.out.println("mTypeList="+mTypeList);
		// //创建适配器
		// typeAdapter = new TypePopupAdapter(mTypeList,
		// CreateOrderActivity.this);
		// //填充适配器
		// old = new SelectOrderInfoPopupWindow(CreateOrderActivity.this,
		// itemsOnClickType, lv_select_address, typeAdapter,onclickType);
		// old.showAtLocation(CreateOrderActivity.this.findViewById(R.id.ll_main),
		// Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		// }
	}

	/**
	 * 老用户请求数据
	 */
	private void initOldData(View v) {
		// 消失软键盘
		CurrencyUtils.dissMiss(CreateOrderActivity.this, v);
		oldUserMobile = et_input.getText().toString().trim();
		// 判断手机号是否输入正确
//		if (!IsPhone.isOrNotPhone(oldUserMobile)) {
//			T.showShort(CreateOrderActivity.this, "电话号码有误，请重新输入");
//		} else {
//			requestHttp();
//		}
		if (!panDuanShiYiWei(oldUserMobile)) {
			T.showShort(CreateOrderActivity.this, "电话号码有误，请重新输入");
		} else {
			requestHttp();
		}

	}

	// 请求网络
	private void requestHttp() {
		pd.show();
		mOldDatas = new ArrayList<OldUserInfo>();
		int token = (Integer) SPUtils.get(CreateOrderActivity.this,SPutilsKey.TOKEN, 0);
		// 请求网络
		RequestParams params = new RequestParams(RequestUrl.USER_INFO);
		params.addBodyParameter(SPutilsKey.MOBILLE, oldUserMobile);
		params.addBodyParameter("kid", "");
		params.addBodyParameter(SPutilsKey.TOKEN, token + "");
		HttpUtil.PostHttp(params, oldHandler, pd);
	}
	// 老用户数据
	private Handler oldHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				parseData(result, 1);
			}
		}

	};
	// 选择气罐数据
	// 老用户数据
	private Handler typeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			if (result != null) {
				parseData(result, 2);
			}
		}
	};

	private SelectOrderInfoPopupWindow old;
	
	private String dataString;
	// 解析数据
	private TypePopupAdapter typeAdapter;
	private ChaiTypePopupAdapter1 typeAdapter1;
	

	private void parseData(String result, int i) {
		Log.e("lll", result);
		if (i == 1) {
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if (resultCode == 1) {
					// 继续解析
					JSONObject obj1 = obj.getJSONObject("resultInfo");
					String name = obj1.getString("user_name");
					String userPhone = obj1.getString("mobile_phone");
					//拿到客户的kid和余瓶data串
					if((obj1.get("bottle_data") instanceof JSONArray)){
						JSONArray jsa=obj1.getJSONArray("bottle_data");
						for (int j = 0; j < jsa.length(); j++) {
							JSONObject jso=jsa.getJSONObject(j);
							if(!jso.getString("good_num").equals("0")){
								SPutilsKey.kehuyou=1;//客户有瓶
								dataString=obj1.getJSONArray("bottle_data").toString();
								break;
							}else{
								dataString="";
								SPutilsKey.kehuyou=2;//客户没有瓶
							}
						}
					}else{
						SPutilsKey.GANGPINGZONGSHU=0;
						dataString="";
						SPutilsKey.kehuyou=2;//客户没有瓶
					}
					String address = "";
					Cursor cursor1 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { obj1.getString("sheng") }, null,null, null, null);
					cursor1.moveToFirst();
					if (cursor1.getCount() != 0) {
						address = address+ cursor1.getString(cursor1.getColumnIndex("name"));
						CreateOrderActivity.shengcode=obj1.getString("sheng");
					}
					Cursor cursor2 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { obj1.getString("shi") }, null, null,null, null);
					cursor2.moveToFirst();
					if (cursor2.getCount() != 0) {
						address = address+ cursor2.getString(cursor2.getColumnIndex("name"));
						CreateOrderActivity.shicode=obj1.getString("shi");
						
					}
					Cursor cursor3 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { obj1.getString("qu") }, null, null,null, null);
					cursor3.moveToFirst();
					if (cursor3.getCount() != 0) {
						address = address+ cursor3.getString(cursor3.getColumnIndex("name"));
						CreateOrderActivity.qucode=obj1.getString("qu");
					}
					Cursor cursor4 = gd.readDb.query("citys",new String[] { "name" }, "code=?",new String[] { obj1.getString("cun") }, null, null,null, null);
					cursor4.moveToFirst();
					if (cursor4.getCount() != 0) {
						address = address+ cursor4.getString(cursor4.getColumnIndex("name"));
						CreateOrderActivity.cuncode=obj1.getString("cun");
					}
					detail=obj1.getString("address");
					address = address + obj1.getString("address");
					mOldDatas.add(new OldUserInfo(name, userPhone, address));
					adapter = new MyOldUserAdapter();
					old = new SelectOrderInfoPopupWindow(CreateOrderActivity.this, itemsOnClick,lv_select_address, adapter, onclick);
					old.setPopDismiss(this);
					old.showAtLocation(CreateOrderActivity.this.findViewById(R.id.ll_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				} else {
					T.showShort(CreateOrderActivity.this, "客户不存在！");
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else if (i == 2) {// 选择气罐的数据
			try {
				JSONObject obj = new JSONObject(result);
				int resultCode = obj.getInt("resultCode");
				if (resultCode == 1) {
					mTypeList.clear();
					if (OrderKind == 1||OrderKind == 4) {
						JSONArray array = obj.getJSONArray("resultInfo");
						for (int j = 0; j < array.length(); j++) {
							JSONObject object = array.getJSONObject(j);
							String bottle_name = object.getString("typename");
							String price = object.getString("price");
							String id = object.getString("id");
							String norm_id = object.getString("norm_id");
							String type = object.getString("type");
							String name = object.getString("name");
							String yj_price = object.getString("yj_price");
							if(OrderKind == 4){
								mTypeList.add(new Type(price, name + ""+ bottle_name, "0", id, norm_id, type,bottle_name, name+bottle_name, yj_price));
							}else{
								mTypeList.add(new Type(price, name + ""+ bottle_name, "0", id, norm_id, type,bottle_name, name, yj_price));
							}
							// // //将数据保存至数据库中
							// // //1,查询数据库，看看是否有该类型的钢瓶
						}
					} else if (OrderKind == 2) {
						JSONObject jsob = obj.getJSONObject("resultInfo");
						JSONArray jas = jsob.getJSONArray("ty");
						for (int j = 0; j < jas.length(); j++) {
							JSONObject object = jas.getJSONObject(j);
							String name = object.getString("name");
							String norm_id = object.getString("norm_id");
							String num = object.getString("num");
							String type = "1";
							String price = object.getString("money");
							// String price = object.getString("price");
							String yj_price = object.getString("deposit");
							String bottle_name = object.getString("name");
							String id = object.getString("id");
							mTypeList.add(new Type(price, name, "0", id,norm_id, type, bottle_name, name, yj_price,num));
						}
					} else if (OrderKind == 3) {
							JSONObject jsob = obj.getJSONObject("resultInfo");
							JSONArray jas = jsob.getJSONArray("yh");
							for (int j = 0; j < jas.length(); j++) {
								JSONObject object = jas.getJSONObject(j);
								String name = object.getString("name");
								String norm_id = object.getString("norm_id");
								String num = object.getString("num");
								String type = "1";
								String price = object.getString("money");
								// String price = object.getString("price");
								String yj_price = object.getString("deposit");
								String bottle_name = object.getString("name");
								String id = object.getString("id");
								mTypeList.add(new Type(price, name, "0", id,norm_id, type, bottle_name, name, yj_price,num));
						}
					}
					typeAdapter = new TypePopupAdapter(mTypeList,CreateOrderActivity.this);
					// 填充适配器
					old = new SelectOrderInfoPopupWindow(CreateOrderActivity.this, itemsOnClickType,lv_select_address, typeAdapter, onclickType);
					old.setPopDismiss(this);
					old.showAtLocation(CreateOrderActivity.this.findViewById(R.id.ll_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// listview子项的点击
	private OnItemClickListener itemsOnClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

		}
	};

	// listview子项的点击
	private OnItemClickListener itemsOnClickType = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
		}
	};
	private TextView tv_name, tv_mobile, tv_address,tv_yuping;
	// 确定取消按钮点击
	private OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_sure:// 确定
				sure();
				break;
			case R.id.tv_quxiao:// 取消
				old.dismiss();
				break;
			default:
				break;
			}
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
				// 将选择的气罐类型添加到下面
				// 不保留此次的操作
				if (mTypeList.size() != 0) {
					mTypeList.clear();
				}
				for (int i = 0; i < mConeTypeList.size(); i++) {
					mTypeList.add(new Type(mConeTypeList.get(i).getPrice(),mConeTypeList.get(i).getWeight(), mConeTypeList.get(i).getNum(), mConeTypeList.get(i).getId(),
							mConeTypeList.get(i).getNorm_id(), mConeTypeList.get(i).getType(), mConeTypeList.get(i).getBottle_name(), mConeTypeList.get(i).getName(), mConeTypeList.get(i).getYj_price()));
				}
				old.dismiss();
				break;

			default:
				break;
			}
		}
	};

	private View view;

	// popupwindow的确定
	private void sure() {
		rl_old_content.removeAllViews();
		rl_old_content.addView(view);
		tv_name.setText(name);
		tv_mobile.setText(phone);
		tv_address.setText(address);
		if(SPutilsKey.kehuyou==1){
			tv_yuping.setText("有余瓶");
		}else if(SPutilsKey.kehuyou==2){
			tv_yuping.setText("无余瓶");
		}
		old.dismiss();
	}

	@Override
	public void jumpPage() {
		super.jumpPage();
		userFlag = true;
		finish();
	}

	public static List<Type> list;
	private double money = 0;

	private void typeSure() {
		list = new ArrayList<Type>();
		if (list.size() != 0) {
			list.clear();
		}
		if (mConeTypeList.size() != 0) {
			mConeTypeList.clear();
		}
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());
			if (CreateOrderActivity.OrderKind == 2) {//
				int num1 = Integer.parseInt(mTypeList.get(i).getNum());
				if (num1 > 0) {
					list.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * num1 + "", mTypeList.get(i).getWeight(), ""+ Integer.parseInt(mTypeList.get(i).getNum())* Integer.parseInt(mTypeList.get(i).getGpnum()),mTypeList.get(i).getId(), mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(),mTypeList.get(i).getBottle_name(), mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
				}
				mConeTypeList.add(new Type(mTypeList.get(i).getPrice(), mTypeList.get(i).getWeight(),""+ Integer.parseInt(mTypeList.get(i).getNum())* Integer.parseInt(mTypeList.get(i).getGpnum()), mTypeList.get(i).getId(),mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(), mTypeList.get(i).getBottle_name(), mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
			} else if (CreateOrderActivity.OrderKind == 3) {
				if(SPutilsKey.kehuyou==1&&userFlag){//客户有余瓶
					int num2 = Integer.parseInt(mTypeList.get(i).getNum());
					if (num2 > 0) {
						list.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * (num2) + "", mTypeList.get(i).getWeight(), ""+ Integer.parseInt(mTypeList.get(i).getNum()),mTypeList.get(i).getId(), mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(),mTypeList.get(i).getBottle_name(), mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
					}
					mConeTypeList.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * (num2) + "", mTypeList.get(i).getWeight(), ""+ Integer.parseInt(mTypeList.get(i).getNum())* Integer.parseInt(mTypeList.get(i).getGpnum()),mTypeList.get(i).getId(),mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(), mTypeList.get(i).getBottle_name(),mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
				}else if(SPutilsKey.kehuyou==2 || !userFlag){//老客户没有余瓶  或新客户
						int num2 = Integer.parseInt(mTypeList.get(i).getNum());
						if (num2 > 0) {
							list.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * (num2) + "", mTypeList.get(i).getWeight(), ""+ Integer.parseInt(mTypeList.get(i).getNum())* Integer.parseInt(mTypeList.get(i).getGpnum()),mTypeList.get(i).getId(), mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(),mTypeList.get(i).getBottle_name(), mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
						}
						mConeTypeList.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * (num2) + "", mTypeList.get(i).getWeight(), ""+ Integer.parseInt(mTypeList.get(i).getNum())* Integer.parseInt(mTypeList.get(i).getGpnum()),mTypeList.get(i).getId(),mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(), mTypeList.get(i).getBottle_name(),mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
				}
			} else {//正常商品列表
				if (num > 0) {
					list.add(new Type(Double.parseDouble(mTypeList.get(i).getPrice()) * num + "", mTypeList.get(i).getWeight(), mTypeList.get(i).getNum(), mTypeList.get(i).getId(), mTypeList.get(i).getNorm_id(),mTypeList.get(i).getType(), mTypeList.get(i).getBottle_name(), mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
				}
				mConeTypeList.add(new Type(mTypeList.get(i).getPrice(),mTypeList.get(i).getWeight(),mTypeList.get(i).getNum(), mTypeList.get(i).getId(),mTypeList.get(i).getNorm_id(), mTypeList.get(i).getType(), mTypeList.get(i).getBottle_name(),mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
			}
		}
		Log.e("lll", CreateOrderActivity.OrderKind + "");
		
		if (list.size() > 0) {
			money = 0;
			for (int i = 0; i < list.size(); i++) {
				money += Double.parseDouble(list.get(i).getPrice());
			}
			System.out.println("list=" + list);
			TypeFragment tf = new TypeFragment();
			// 将选中的值传递给fragment
			Bundle bundle = new Bundle();
			bundle.putSerializable("mDatas", (Serializable) list);
			tf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_type, tf).commit();
		} else {
			rl_type.removeAllViews();
		}
		old.dismiss();
	}

	private MyOldUserAdapter adapter;
	private String name, phone, address, addressid;// 得到选中的姓名，电话，地址
	private int a = 0;// 用来判定是否是第一次点击，0代表是，1代表不是
	private int cliPos;// 记录第一次点击时的位置
	private Boolean flag = true;

	/**
	 * 自定义适配器
	 */
	public class MyOldUserAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mOldDatas.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mOldDatas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			if (arg1 == null) {
				viewHolder = new ViewHolder();
				arg1 = LayoutInflater.from(getActivity()).inflate(
						R.layout.old_user_list_item, null);
				// 初始化组件
				viewHolder.tv_old_name = (TextView) arg1
						.findViewById(R.id.tv_old_name);
				viewHolder.tv_old_phone = (TextView) arg1
						.findViewById(R.id.tv_old_phone);
				viewHolder.tv_old_address = (TextView) arg1
						.findViewById(R.id.tv_old_address);
				viewHolder.iv_old_ischeck = (ImageView) arg1
						.findViewById(R.id.iv_old_ischeck);
				// 设置标签
				arg1.setTag(viewHolder);
			} else {
				// 取出标签
				viewHolder = (ViewHolder) arg1.getTag();
			}
			OldUserInfo user = mOldDatas.get(arg0);
			viewHolder.tv_old_name.setText(user.getOldName());
			viewHolder.tv_old_phone.setText(user.getOldPhone());
			viewHolder.tv_old_address.setText(user.getOldAddress());

			viewHolder.iv_old_ischeck.setImageResource(user.getIvPic());
			// 给imageview设置点击事件
			viewHolder.iv_old_ischeck.setTag(arg0);
			viewHolder.iv_old_ischeck.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					boolean bl = flag;
					int pos = (Integer) arg0.getTag();
					OldUserInfo user = mOldDatas.get(pos);
					name = user.getOldName();
					
					
					phone = user.getOldPhone();
					address = user.getOldAddress();
					if (a == 0) {
						// 代表是第一次点击
						cliPos = pos;
						a = 1;
						user.setIvPic(R.drawable.checked);
						flag = false;
					} else if (a == 1) {
						// 不是第一次点击
						if (cliPos == pos) {// 点击相同的位置
							if (user.getIvPic() == R.drawable.checked) {
								user.setIvPic(R.drawable.unchecked);
								flag = true;
							} else {
								user.setIvPic(R.drawable.checked);
								flag = false;
							}
						} else {// 不是相同的位置
								// 将前面已经点击了的设置成未被点击
							OldUserInfo user1 = mOldDatas.get(cliPos);
							user1.setIvPic(R.drawable.unchecked);
							// 将自己设置成点击
							user.setIvPic(R.drawable.checked);
							flag = false;
							cliPos = pos;
						}

					}
					notifyDataSetChanged();
				}
			});
			return arg1;
		}

		class ViewHolder {
			TextView tv_old_name;
			TextView tv_old_phone;
			TextView tv_old_address;
			ImageView iv_old_ischeck;
		}

	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		Handler[] handlers = { typeHandler, oldHandler };
		return handlers;
	}

	public static boolean userFlag = true;

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {// 1 新客户 2老用户
			userFlag = false;
			// SPutilsKey.type=1;
			rl_user.removeAllViews();
			rl_user.addView(newUserView);
		} else {// 老用户
			userFlag = true;
			// SPutilsKey.type=2;
			rl_user.removeAllViews();
			rl_user.addView(oldUserView);
		}
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
					if(SPutilsKey.kehuyou==1){//客户有瓶
						obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice())/ (Double.parseDouble(popupList.get(j).getNum())));
					}else{//客户没有瓶
							obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice())/ (Double.parseDouble(popupList.get(j).getNum())/3));
					}
				} else {
					obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice())/ (Double.parseDouble(popupList.get(j).getNum())));
				}
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

	public static String shengcode, shicode, qucode, cuncode, detail;

	@Override
	public  void getAddress(String sheng, String shi, String qu, String cun,String detail, String shengcode, String shicode, String qucode,String cuncode) {
		String str = "";
		this.detail = detail;
		if (TextUtils.isEmpty(shengcode)) {
			this.shengcode = "0";
			
		} else {
			this.shengcode = shengcode;
			str = str + sheng;
		}
		if (TextUtils.isEmpty(shicode)) {
			this.shicode = "0";
		} else {
			this.shicode = shicode;
			str = str + shi;
		}
		if (TextUtils.isEmpty(qucode)) {
			this.qucode = "0";
		} else {
			this.qucode = qucode;
			str = str + qu;
		}
		if (TextUtils.isEmpty(cuncode) || cuncode.equals("null")) {
			this.cuncode = "0";
		} else {
			this.cuncode = cuncode;
			str = str + cun;
		}
		str = str + detail;
		et_new_inputAddress.setText(str);
	}

	@Override
	public void popDismissCalBack() {
		setEnableSelect(true, true, true,true);
	}

	/**
	 * 判断是否为十一位数
	 */
	public boolean panDuanShiYiWei(String str) {

		if(str.length()==11){
			return true;
		}
		return false;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(checkedId==R.id.rb_business_user){//商业用户
			user_type="2";
		}else if(checkedId==R.id.rb_normal_user){//居民用户
			user_type="1";
		}
	}
}
