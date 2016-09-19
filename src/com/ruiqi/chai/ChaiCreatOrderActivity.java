package com.ruiqi.chai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.adapter.ChaiTypePopupAdapter1;
import com.ruiqi.adapter.TypePopupAdapter;
import com.ruiqi.bean.Type;
import com.ruiqi.chai.ChaiBottles.BottlesType;
import com.ruiqi.chai.ChaiHttpCreateOrderData.ParserCreateOrderData;
import com.ruiqi.chai.ChaiHttpCustomerData.ParserCustomerDefaultData;
import com.ruiqi.chai.ChaiHttpSelectBottletype.ParserSelectBottlestypeData;
import com.ruiqi.fragment.TypeFragment;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.ChaiMyDialog.ChaiSelectCallBack;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow;
import com.ruiqi.view.popupwindow.SelectOrderInfoPopupWindow.PopDismiss;
import com.ruiqi.works.R;

public class ChaiCreatOrderActivity extends FragmentActivity implements
		OnClickListener, ParserCustomerDefaultData,
		ParserSelectBottlestypeData, PopDismiss, ParserCreateOrderData,
		ChaiSelectCallBack {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chai_create_order);
		initView();
	}

	private EditText et_input;
	private TextView tv_start, tv_select, tv_back, tvTitle, tv_name,tv_youhui,tv_price,tv_yingshuo,tv_youhui_price,tv_address, tv_type, tv_phone;
	private RelativeLayout rl_selectType, rl_user, rl_type;
	private ImageView select, ivBack;
	private List<Type> mTypeList = new ArrayList<Type>();
	private List<Type> mConeTypeList = new ArrayList<Type>();
	private ArrayList<ChaiYouHui> enableYouhui=new ArrayList<ChaiYouHui>();
	private TypePopupAdapter typeAdapter;
	private ListView lv_select_address;
	private SelectOrderInfoPopupWindow old;
	private int money;
	private int youhuimoeny;
	private int yingshou;
	private View oldUserView;
	private LinearLayout ll_youhui;
	public  List<Type> listyouhui= new ArrayList<Type>();

	private void initView() {
		if (SPutilsKey.list.size() != 0) {
			SPutilsKey.list.clear();
		}
		tv_back = (TextView) findViewById(R.id.tv_back);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("创建订单");

		tv_youhui_price = (TextView) findViewById(R.id.tv_youhui_price);
		tv_yingshuo = (TextView) findViewById(R.id.tv_yingshuo);
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_youhui = (TextView) findViewById(R.id.tv_youhui);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		et_input = (EditText) findViewById(R.id.et_input);
		et_input = (EditText) findViewById(R.id.et_input);
		select = (ImageView) findViewById(R.id.select);
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_select = (TextView) findViewById(R.id.tv_select);
		rl_selectType = (RelativeLayout) findViewById(R.id.rl_selectType);
		rl_type = (RelativeLayout) findViewById(R.id.rl_type);
		ll_youhui = (LinearLayout) findViewById(R.id.ll_youhui);
		oldUserView = LayoutInflater.from(this).inflate(R.layout.old_user, null);
		tv_back.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		select.setOnClickListener(this);
		tv_start.setOnClickListener(this);
		tv_select.setOnClickListener(this);
		rl_selectType.setOnClickListener(this);
		ll_youhui.setOnClickListener(this);
		initOldUser();
		initHttp();

	}

	private void initHttp() {
		ChaiHttpSelectBottletype.getInstance().setOnParserSelectBottlestypeData(this);
		ChaiHttpCreateOrderData.getInstance().setOnParserCreateOrderData(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:// 返回
		case R.id.ivBack:
			finish();
			break;
		case R.id.select:// 搜索客户
			ChaiHttpCustomerData.getInstance().setOnParserCustomerDefaultData(this);
			getData("", et_input.getText().toString().trim());
			break;
		case R.id.tv_start:// 保存/开始
			createOrder();
			break;
		case R.id.ll_youhui:// 优惠选择
			if(money<=0){
				ChaiMyDialog.getInstance().showHint(this, "请先选择商品！！");
				return;
			}
			if(universalData!=null&&enableYouhui!=null&&enableYouhui.size()!=0){
				ll_youhui.setEnabled(false);
				chaiInitPopView(enableYouhui);
			}else{
				ChaiMyDialog.getInstance().showHint(this, "客户没有相同规格余气券！！");
			}
			break;
		case R.id.tv_select:// 创建新客户
			Intent it = new Intent(this, ChaiCreateNewUser.class);
			startActivity(it);
			break;
		case R.id.rl_selectType:// 选择商品
			if (universalData != null) {
				rl_selectType.setEnabled(false);
				ChaiHttpSelectBottletype.getInstance().app2server(universalData.getResultInfo().getKtype(),(String) SPUtils.get(this, SPutilsKey.SHOP_ID, ""),
						(Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0));
			} else {
				ChaiMyDialog.getInstance().showHint(this, "请先搜索客户！！");
			}
			break;

		default:
			break;
		}
	}

	/**
	 * 搜索客户
	 * 
	 * @param kid
	 * @param mobile
	 */
	public void getData(String kid, String mobile) {
		if (!TextUtils.isEmpty(mobile)) {
			if (IsPhone.panDuanShiYiWei(mobile)) {
				ChaiHttpCustomerData.getInstance().app2server(kid, mobile);
			} else {
				ChaiMyDialog.getInstance().showHint(this, "请输入正确的手机号");
				return;
			}
		} else {
			ChaiMyDialog.getInstance().showHint(this, "请输入手机号");
			return;
		}
	}

	/**
	 * 创建订单
	 */
	private void createOrder() {
		if (universalData ==null ||SPutilsKey.list == null || SPutilsKey.list.size() == 0) {
			ChaiMyDialog.getInstance().showHint(this, "请选择商品！！");
		} else {
			ChaiMyDialog.getInstance().setSelectCallBack(this);
			ChaiMyDialog.getInstance().showHintConfirm(this, "保存订单或开始订单", "开始","保存");
		}
	}

	private ChaiCustomerTuiPingData universalData=null;

	@Override
	public void parserCustomerDefaultResult(boolean webErrer,
			boolean loginErrer, ChaiCustomerTuiPingData universalData,
			UniversalData universalDatas) {
		if (webErrer) {
			if (loginErrer) {
				this.universalData = universalData;
				tv_name.setText(universalData.getResultInfo().getUser_name());
				String address = universalData.getResultInfo().getSheng_name()
						+ universalData.getResultInfo().getShi_name()
						+ universalData.getResultInfo().getQu_name()
						+ universalData.getResultInfo().getCun_name()
						+ universalData.getResultInfo().getAddress();
				tv_address.setText(address);
				tv_phone.setText(universalData.getResultInfo().getMobile_phone());
				tv_type.setText(universalData.getResultInfo().getKtype().equals("2") ? "商业用户" : "居民用户");
				
				universalData.getResultInfo().getYhdata();
				if(universalData.getResultInfo().getYhdata()!=null &&universalData.getResultInfo().getYhdata().size()!=0){
					tv_youhui.setText("有余气券");
				}else{
					tv_youhui.setText("无余气券");
				}
			} else {
				Toast.makeText(this, universalDatas.getResultInfo(),Toast.LENGTH_SHORT).show();
			}
		} else {

		}
	}

	@Override
	public void parserSelectBottlestypeResult(boolean webErrer,boolean loginErrer, ChaiBottles universalData,UniversalData universalDatas) {
		
		if (webErrer) {
			if (loginErrer) {
				mTypeList.clear();
				ArrayList<BottlesType> cbtdi = universalData.getResultInfo();
				for (int i = 0; i < cbtdi.size(); i++) {
					mTypeList.add(new Type(cbtdi.get(i).getPrice(), cbtdi.get(i).getName() + cbtdi.get(i).getTypename(),
							"0", cbtdi.get(i).getId(), cbtdi.get(i).getNorm_id(), cbtdi.get(i).getType(),
							cbtdi.get(i).getTypename(), cbtdi.get(i).getName(),cbtdi.get(i).getYj_price()));
				}
				setPopup();
			} else {
				Toast.makeText(this, universalDatas.getResultInfo(),
						Toast.LENGTH_SHORT).show();
			}
		} else {
		}
	}

	@Override
	public void parserCreateOrderResult(boolean webErrer, boolean loginErrer,ChaiCreateOrderData chaiCreateOrderData,UniversalData universalData, int where) {

		if (webErrer) {
			if (loginErrer) {
				if (where == 1) {
					Toast.makeText(this, "订单创建成功！", Toast.LENGTH_SHORT).show();
					finish();
				} else {
					Toast.makeText(this, "开始订单", Toast.LENGTH_SHORT).show();
					SPUtils.put(this, "ordersn", chaiCreateOrderData.getResultInfo().getOrdersn());
					SPUtils.put(this, "username", tv_name.getText().toString().trim());
					SPUtils.put(this, "usermobile", tv_phone.getText().toString().trim());
					SPUtils.put(this, "useraddress", tv_address.getText().toString().trim());
					SPUtils.put(this, "money", yingshou+"");
					SPUtils.put(this, "total", yingshou+"");
					SPUtils.put(this, "kid", chaiCreateOrderData.getResultInfo().getKid());
					Intent it = new Intent(this, ChaiWeightActivity.class);
					it.putExtra("show", "ChaiCreateOrderActivity");
					startActivity(it);
				}
			} else {
				Toast.makeText(this, universalData.getResultInfo(),Toast.LENGTH_SHORT).show();
			}
		} else {

		}

	}
	/**
	 * 弹出popup
	 */
	public void setPopup() {
		typeAdapter = new TypePopupAdapter(mTypeList, this);
		old = new SelectOrderInfoPopupWindow(this, itemsOnClickType,lv_select_address, typeAdapter, onclickType);
		old.setPopDismiss(this);
		old.showAtLocation(this.findViewById(R.id.ll_create_order),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private OnItemClickListener itemsOnClickType = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		}
	};

	/**
	 * popup 点击监听
	 */
	private OnClickListener onclickType = new OnClickListener() {

		@Override
		public void onClick(View v) {
			rl_selectType.setEnabled(true);
			switch (v.getId()) {
			case R.id.tv_sure:// 确定
				typeSure();
				break;
			case R.id.tv_quxiao:// 取消
				if (mTypeList.size() != 0) {
					mTypeList.clear();
				}
				for (int i = 0; i < mConeTypeList.size(); i++) {
					mTypeList.add(new Type(mConeTypeList.get(i).getPrice(),
							mConeTypeList.get(i).getWeight(), mConeTypeList.get(i).getNum(), mConeTypeList.get(i).getId(),
							mConeTypeList.get(i).getNorm_id(), mConeTypeList.get(i).getType(), mConeTypeList.get(i).getBottle_name(), mConeTypeList.get(i).getName(), mConeTypeList.get(i).getYj_price()));
				}
				xuanzeyouhuihoujisuan();
				break;

			default:
				break;
			}
		}
	};
	/**
	 * 优惠确认监听
	 */
	private OnClickListener onclickTypeyouhui = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			ll_youhui.setEnabled(true);
			switch (v.getId()) {
			case R.id.tv_sure:// 确定
				typeSureYouhui();
				old.dismiss();
				break;
			case R.id.tv_quxiao:// 取消
				old.dismiss();
				break;
				
			default:
				break;
			}
		}
	};

	@Override
	public void popDismissCalBack() {
		rl_selectType.setEnabled(true);
		ll_youhui.setEnabled(true);
	}

	/**
	 * popup确认
	 */
	private void typeSure() {
		if (SPutilsKey.list.size() != 0) {
			SPutilsKey.list.clear();
		}
		if (mConeTypeList.size() != 0) {
			mConeTypeList.clear();
		}
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());

			if (num > 0) {
				SPutilsKey.list.add(new Type(Double.parseDouble(mTypeList
						.get(i).getPrice())+ "", mTypeList.get(i)
						.getWeight(), mTypeList.get(i).getNum(), mTypeList.get(
						i).getId(), mTypeList.get(i).getNorm_id(), mTypeList
						.get(i).getType(), mTypeList.get(i).getBottle_name(),
						mTypeList.get(i).getName(), mTypeList.get(i).getYj_price()));
			}
			mConeTypeList.add(new Type(mTypeList.get(i).getPrice(), mTypeList.get(i)
							.getWeight(), mTypeList.get(i).getNum(), mTypeList
							.get(i).getId(), mTypeList.get(i).getNorm_id(),
							mTypeList.get(i).getType(), mTypeList.get(i)
									.getBottle_name(), mTypeList.get(i)
									.getName(), mTypeList.get(i).getYj_price()));
		}
		money = 0;
		if (SPutilsKey.list.size() > 0) {
			for (int i = 0; i < SPutilsKey.list.size(); i++) {
				money += Double.parseDouble(SPutilsKey.list.get(i).getPrice())*Integer.parseInt(SPutilsKey.list.get(i).getNum());
			}
			TypeFragment tf = new TypeFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mDatas", (Serializable) SPutilsKey.list);
			tf.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_type, tf).commit();
		} else {
			rl_type.removeAllViews();
		}
		xuanzeyouhuihoujisuan();
		old.dismiss();
	}
	/**
	 * 
	 */     
	private void typeSureYouhui() {
		
		if (listyouhui.size() != 0) {
			listyouhui.clear();
		}
		if (mConeTypeList.size() != 0) {
			mConeTypeList.clear();
		}
		for (int i = 0; i < mTypeList.size(); i++) {
			int num = Integer.parseInt(mTypeList.get(i).getNum());
			if (num > 0) {
				listyouhui.add(new Type(Double.parseDouble(mTypeList
						.get(i).getPrice()) * num + "", mTypeList.get(i)
						.getWeight(), mTypeList.get(i).getNum(), mTypeList.get(
								i).getId(), mTypeList.get(i).getNorm_id(), mTypeList
								.get(i).getType(), mTypeList.get(i).getBottle_name(),
								mTypeList.get(i).getName(), mTypeList.get(i)
								.getYj_price()));
			}
			mConeTypeList
			.add(new Type(mTypeList.get(i).getPrice(), mTypeList.get(i)
					.getWeight(), mTypeList.get(i).getNum(), mTypeList
					.get(i).getId(), mTypeList.get(i).getNorm_id(),
					mTypeList.get(i).getType(), mTypeList.get(i)
					.getBottle_name(), mTypeList.get(i)
					.getName(), mTypeList.get(i).getYj_price()));
		}
		youhuimoeny = 0;
		if (listyouhui.size() > 0) {
			for (int i = 0; i < listyouhui.size(); i++) {
				youhuimoeny += Double.parseDouble(listyouhui.get(i).getPrice());
			}
		} else {
		}
		sumPrice();
		old.dismiss();
	}

	private void initOldUser() {
		lv_select_address = (ListView) oldUserView
				.findViewById(R.id.lv_select_address);
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
				obj1.put("good_price",Double.parseDouble(popupList.get(j).getPrice()));
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

	@Override
	public void chaiSelectCallBack(boolean isSelect) {
		int where;
		if (isSelect) {
			where = 2;
		} else {
			where = 1;
		}
		String youhuijson="";
		if(listyouhui.size()!=0){
			youhuijson=getJsonYouHui();
		}
		ChaiHttpCreateOrderData.getInstance().app2server(where,
				universalData.getResultInfo().getMobile_phone(),
				universalData.getResultInfo().getUser_name(),
				universalData.getResultInfo().getSheng(),
				universalData.getResultInfo().getShi(),
				universalData.getResultInfo().getQu(),
				universalData.getResultInfo().getCun(),
				universalData.getResultInfo().getAddress(),
				(String) SPUtils.get(this, "shipper_name", ""),
				(String) SPUtils.get(this, SPutilsKey.MOBILLE, ""),
				(String) SPUtils.get(this, SPutilsKey.SHOP_ID, ""),
				getJsonData(SPutilsKey.list), yingshou + "",
				universalData.getResultInfo().getKtype(),(String) SPUtils.get(this, SPutilsKey.SHIP_ID, ""),youhuijson);
	}
	/**
	 * 计算订单价格
	 */
	public void sumPrice(){
		tv_youhui_price.setText("￥"+youhuimoeny);
		tv_price.setText("￥"+money);
		yingshou=(money-youhuimoeny)>0?(money-youhuimoeny):0;
		tv_yingshuo.setText("￥"+yingshou);
	}
	/**
	 * 客户拥有的优惠券选择
	 */
	private ChaiTypePopupAdapter1 typeAdapter1;
	
	public void chaiInitPopView(ArrayList<ChaiYouHui> str){
		int max=0;
		int[] maxs = null;
					mTypeList.clear();   
					maxs=new int[str.size()];
					for (int i = 0; i < str.size(); i++) {
						if(Integer.parseInt(str.get(i).getNum())>0){
							mTypeList.add(new Type(str.get(i).getPrice(), str.get(i).getTitle(), "0", str.get(i).getId(),str.get(i).getYhsn(), str.get(i).getGood_type(), "", "", "",str.get(i).getNum()));
							maxs[i]=Integer.parseInt(str.get(i).getNum());
						}
					}
					int[] maxss=new int[str.size()];
					for(int k=0,p=0;k<maxs.length;k++){
						if(maxs[k]!=0){
							maxss[p]=maxs[k];
							p++;
						}
					}
					typeAdapter1 = new ChaiTypePopupAdapter1(mTypeList,this,max,maxss,"");
					// 填充适配器
					old = new SelectOrderInfoPopupWindow(this, itemsOnClickType,lv_select_address, typeAdapter1, onclickTypeyouhui);
					old.setPopDismiss(this);
					old.showAtLocation(this.findViewById(R.id.ll_create_order),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	/**
	 * 组优惠的data串
	 * @return
	 */
	public String  getJsonYouHui(){
		JSONArray jsoa=new JSONArray();
		for (int i = 0; i < listyouhui.size(); i++) {
				jsoa.put(listyouhui.get(i).getId());
		}
		return jsoa.toString(); 
	}
	/**
	 * 计算可选的优惠
	 * @return
	 */
	public ArrayList<ChaiYouHui> kexuabze(){
		
		 ArrayList<ChaiYouHui> youhui=new ArrayList<ChaiYouHui>();
		 if(universalData.getResultInfo().getYhdata()!=null){
			 for (int i = 0; i < SPutilsKey.list.size(); i++) {
				 String type=SPutilsKey.list.get(i).getNorm_id();
				 int number=Integer.parseInt(SPutilsKey.list.get(i).getNum());
				 
				 for (int j = 0 ,k=0; j < universalData.getResultInfo().getYhdata().size()&&number>k;j++) {
					 
					 if(type.equals(universalData.getResultInfo().getYhdata().get(j).getGood_type())){
						 
						 youhui.add(universalData.getResultInfo().getYhdata().get(j));
						 
						 k++;
					 }
				 }
			 }
		 }
		return youhui;
	}
	/**
	 * 选择商品之后的计算
	 */
	public void xuanzeyouhuihoujisuan(){
		
		if(money==0){
			youhuimoeny=0;
			listyouhui.clear();
		}
		enableYouhui=kexuabze();
		if(enableYouhui.size()==0){
			listyouhui.clear();
		}
		sumPrice();
	}
}
