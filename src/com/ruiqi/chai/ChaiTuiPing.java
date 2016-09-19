package com.ruiqi.chai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.chai.ChaiHttpCustomerData.ParserCustomerDefaultData;
import com.ruiqi.chai.ChaiHttpTuiPingData.ParserTuiPingData;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

public class ChaiTuiPing extends Activity implements OnClickListener,
		ParserCustomerDefaultData, ParserTuiPingData {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chai_tui_ping);
		initView();
	}

	private TextView tv_back, tvTitle, tv_button_start, tv_tuiping_name,
			tv_tuiping_have;
	private ImageView ivBack, select;
	private EditText et_input;
	private String kid, mobile, username, address;

	private void initView() {
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tv_button_start = (TextView) findViewById(R.id.tv_button_start);
		tv_tuiping_name = (TextView) findViewById(R.id.tv_tuiping_name);
		tv_tuiping_have = (TextView) findViewById(R.id.tv_tuiping_have);
		et_input = (EditText) findViewById(R.id.et_input);
		select = (ImageView) findViewById(R.id.select);
		tvTitle.setText("下退瓶订单");
		ivBack.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		select.setOnClickListener(this);
		tv_button_start.setOnClickListener(this);
		ChaiHttpCustomerData.getInstance().setOnParserCustomerDefaultData(this);
		ChaiHttpTuiPingData.getInstance().setOnParserTuiPingData(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBack:
		case R.id.tv_back:
			finish();
			break;
		case R.id.select:
			getData("", et_input.getText().toString().trim());
			break;
		case R.id.tv_button_start:// 创建退瓶订单
			ChaiHttpTuiPingData.getInstance().app2server(kid, mobile, username,
					address,
					(String) SPUtils.get(this, SPutilsKey.SHOP_ID, "0"),
					(String) SPUtils.get(this, SPutilsKey.SHIP_ID, "0"),
					(String) SPUtils.get(this, "shipper_name", ""), 
					(String) SPUtils.get(this, SPutilsKey.MOBILLE, "0"),
					 "",
					(Integer) SPUtils.get(this, SPutilsKey.TOKEN, 0));
			break;
		default:
			break;
		}
	}

	@Override
	public void parserCustomerDefaultResult(boolean webErrer,
			boolean loginErrer, ChaiCustomerTuiPingData universalData,UniversalData universalDatas) {
		if (webErrer) {
			if (loginErrer) {
				tv_tuiping_name.setText(universalData.getResultInfo().getUser_name());
				tv_tuiping_have.setText(universalData.getResultInfo().getKtype().equals("2")? "商业用户":"居民用户" );
				kid = universalData.getResultInfo().getKid();
				mobile = universalData.getResultInfo().getMobile_phone();
				username = universalData.getResultInfo().getUser_name();
				address = universalData.getResultInfo().getSheng_name()
						+ universalData.getResultInfo().getShi_name()
						+ universalData.getResultInfo().getQu_name()
						+ universalData.getResultInfo().getCun_name()
						+ universalData.getResultInfo().getAddress();
			} else {
				Toast.makeText(this,universalDatas.getResultInfo(), Toast.LENGTH_SHORT).show();
			}
		} else {
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
				IsPhone.isOrNotPhone(mobile);
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
	public void parserTuiPingResult(boolean webErrer, boolean loginErrer,UniversalData universalData) {

		if (webErrer) {
			if (loginErrer) {
				String ordersn=universalData.getResultInfo();
				if(TextUtils.isEmpty(ordersn)){
					Toast.makeText(this,"创建失败！", Toast.LENGTH_SHORT).show();
				}else{
					SPutilsKey.tuipingordersn=ordersn;
					SPutilsKey.tuipingkid=kid;
					Toast.makeText(this,"创建成功，跳转扫瓶界面！！", Toast.LENGTH_SHORT).show();
					Intent it = new Intent(this, ChaiBackSaoMiaoActivity.class);
					startActivity(it);
				}
			} else {

			}

		} else {

		}

	}

}
