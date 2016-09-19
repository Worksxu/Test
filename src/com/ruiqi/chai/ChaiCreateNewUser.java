package com.ruiqi.chai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.chai.ChaiHttpCreateNewUser.ParserCreateNewUserData;
import com.ruiqi.chai.ChaiHttpCustomerData.ParserCustomerDefaultData;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.SelectAddress;
import com.ruiqi.utils.SelectAddress.AddressConfirm;
import com.ruiqi.works.R;

public class ChaiCreateNewUser extends Activity implements ParserCustomerDefaultData,OnClickListener,ParserCreateNewUserData,AddressConfirm,android.widget.RadioGroup.OnCheckedChangeListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.chai_new_user);
		initView();
	}

	private TextView tv_name,tv_button_start,et_new_inputAddress,tv_back,tvTitle;
	private EditText et_new_input,et_new_inputMobile,et_new_customer_card,et_input;
	private ImageView select,ivBack;
	private RadioGroup rg_user_type;
	private String user_type="1";
	
	private SelectAddress selectAddress;
	private ProgressDialog pd;
	
	private void initView() {
		pd=new ProgressDialog(this);
		pd.setMessage("正在提交...");
		selectAddress = new SelectAddress(this);
		selectAddress.setOnAddressConfirm(this);
		tv_back=(TextView) findViewById(R.id.tv_back);
		ivBack=(ImageView) findViewById(R.id.ivBack);
		tvTitle=(TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("创建新客户");
		
		et_new_input=(EditText) findViewById(R.id.et_new_input);
		et_new_inputMobile=(EditText) findViewById(R.id.et_new_inputMobile);
		et_new_inputAddress=(TextView) findViewById(R.id.et_new_inputAddress);
		et_new_customer_card=(EditText) findViewById(R.id.et_new_customer_card);
		rg_user_type=(RadioGroup) findViewById(R.id.rg_user_type);
		et_input=(EditText) findViewById(R.id.et_input);
		select=(ImageView) findViewById(R.id.select);
		tv_name=(TextView) findViewById(R.id.tv_name);
		tv_button_start=(TextView) findViewById(R.id.tv_button_start);
		initHttp();
		
		tv_back.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		et_new_inputAddress.setOnClickListener(this);
		tv_button_start.setOnClickListener(this);
		select.setOnClickListener(this);
		rg_user_type.setOnCheckedChangeListener(this);
		
	}
	private void initHttp() {
		ChaiHttpCustomerData.getInstance().setOnParserCustomerDefaultData(this);
		ChaiHttpCreateNewUser.getInstance().setOnParserCreateNewUserData(this);
	}
	@Override
	public void parserCustomerDefaultResult(boolean webErrer,
			boolean loginErrer, ChaiCustomerTuiPingData universalData,
			UniversalData universalDatas) {
		if(webErrer){
			if(loginErrer){
				tv_name.setText(universalData.getResultInfo().getUser_name());
			}else{
				Toast.makeText(this, universalDatas.getResultInfo(), Toast.LENGTH_SHORT).show();
			}
		}else{
			
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
		case R.id.ivBack:
			finish();
			break;
		case R.id.et_new_inputAddress://选择地址
			selectAddress.showSelectAddress();
			break;
		case R.id.tv_button_start://创建新客户
			createNewUser();
			break;
		case R.id.select://搜索推荐人
			getData("", et_input.getText().toString().trim());
			break;
		default:
			break;
		}
		
	}
	private void createNewUser() {
		String username=et_new_input.getText().toString().trim();
		String card_sn=et_new_customer_card.getText().toString().trim();
		String address=et_new_inputAddress.getText().toString().trim();
		String mobile=et_new_inputMobile.getText().toString().trim();
		if(TextUtils.isEmpty(username)){
			ChaiMyDialog.getInstance().showHint(this, "客户姓名不能为空！");
			return;
		}
		if (!TextUtils.isEmpty(mobile)) {
				if (!IsPhone.panDuanShiYiWei(mobile)) {
					ChaiMyDialog.getInstance().showHint(this, "请输入正确的手机号！");
					return;
				}
		} else {
				ChaiMyDialog.getInstance().showHint(this, "请输入手机号!");
			return;
		}
		if(TextUtils.isEmpty(card_sn)){
			ChaiMyDialog.getInstance().showHint(this, "客户卡号不能为空！！");
			return;
		}
		if (card_sn.length() != 6) {
			ChaiMyDialog.getInstance().showHint(this, "请输入正确的用户卡号！");
			return;
		}
		if(TextUtils.isEmpty(address)||address.equals("请输入客户地址")){
			ChaiMyDialog.getInstance().showHint(this, "请输入客户地址！");
			return;
		}
		pd.show();
		ChaiHttpCreateNewUser.getInstance().app2server(mobile, username, shengcode, shicode, qucode, cuncode, detail, card_sn,(String) SPUtils.get(this, SPutilsKey.SHOP_ID, "") , user_type, et_input.getText().toString().trim());
		
	}
	/**
	 * 搜索客户
	 * 
	 * @param kid
	 * @param mobile
	 */
	public void getData(String kid, String mobile) {
		if (!TextUtils.isEmpty(mobile)) {
			if (IsPhone.isOrNotPhone(mobile)) {
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
	@Override
	public void parserCreateNewUserResult(boolean webErrer, boolean loginErrer, ChaiCreateOrderData chaiCreateOrderData,UniversalData universalData) {
		pd.dismiss();
		if(webErrer){
			if(loginErrer){
				Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(this, universalData.getResultInfo(), Toast.LENGTH_SHORT).show();
			}
		}else{}
		
	}
	public  String shengcode, shicode, qucode, cuncode, detail;
	@Override
	public void getAddress(String sheng, String shi, String qu, String cun,
			String detail, String shengcode, String shicode, String qucode,
			String cuncode) {
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
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(checkedId==R.id.rb_business_user){//商业用户
			user_type="2";
		}else if(checkedId==R.id.rb_normal_user){//居民用户
			user_type="1";
		}
	}
}
