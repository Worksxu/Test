package com.ruiqi.chai;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiqi.chai.ChaiHttpCustomerData.ParserCustomerDefaultData;
import com.ruiqi.chai.ChaiHttpCustomerReportData.ParserCustomerReportData;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.IsPhone;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.R;

public class ChaiSafeReport extends Activity implements OnClickListener,
		ParserCustomerReportData {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chai_safe_report);
		initView();
	}

	private TextView tv_back, tvTitle, rl_button_start, tv_anjian_name,
			tv_anjian, tv_anjian_time, tv_anjian_type;
	private ImageView ivBack, select;
	private EditText et_input;
	public static String kid;  
	private ProgressDialog pd;

	private void initView() {
		pd=new ProgressDialog(this);
		pd.setMessage("搜索客户中。。。");
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tv_back = (TextView) findViewById(R.id.tv_back);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tv_anjian_name = (TextView) findViewById(R.id.tv_anjian_name);
		tv_anjian = (TextView) findViewById(R.id.tv_anjian);
		tv_anjian_time = (TextView) findViewById(R.id.tv_anjian_time);
		tv_anjian_type = (TextView) findViewById(R.id.tv_anjian_type);

		et_input = (EditText) findViewById(R.id.et_input);
		select = (ImageView) findViewById(R.id.select);
		rl_button_start = (TextView) findViewById(R.id.tv_button_start);
		tvTitle.setText("安全报告");
		ivBack.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		select.setOnClickListener(this);
		rl_button_start.setOnClickListener(this);
		ChaiHttpCustomerReportData.getInstance().setOnParserCustomerReportData(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ivBack:
		case R.id.tv_back:
			finish();
			break;
		case R.id.select:// 获得客户信息
			getData( et_input.getText().toString().trim());
			break;
		case R.id.tv_button_start:// 开始安检
			if (universalData == null) {
				ChaiMyDialog.getInstance().showHint(this, "请先搜索用户");
			} else {
				Intent it = new Intent(this, ChaiSelfActivity.class);
				it.putExtra("data", universalData.getResultInfo());
				startActivity(it);
			}
			break;
		default:
			break;   
		}
	}

	// 搜索条件
	public void getData(String mobile) {
		if (!TextUtils.isEmpty(mobile)) {
			if (IsPhone.panDuanShiYiWei(mobile)) {
				pd.show();
				ChaiHttpCustomerReportData.getInstance().app2server(mobile,(Integer)SPUtils.get(this,SPutilsKey.TOKEN, 0));
			} else {
				ChaiMyDialog.getInstance().showHint(this, "请输入正确的手机号");
				return;
			}
		} else {
			ChaiMyDialog.getInstance().showHint(this, "请输入手机号");
			return;
		}
	}

	// 搜索之后显示内容
	public void setShowContent(String name, String type, String memery,String time) {
		tv_anjian_name.setText(name);
		if (type.equals("1")) {
			tv_anjian_type.setText("居民用户");
		} else if (type.equals("2")) {
			tv_anjian_type.setText("商业用户");
		}
		if(TextUtils.isEmpty(memery)){
			tv_anjian.setText("暂无");
		}else{
			tv_anjian.setText(memery);
		}
		if(TextUtils.isEmpty(memery)){
			tv_anjian_time.setText("暂无");
		}else{
			tv_anjian_time.setText(time);
		}
		
	}
	private ChaiCustomerData universalData;
	@Override
	public void parserCustomerReportResult(boolean webErrer,
			boolean loginErrer, ChaiCustomerData universalData) {
		pd.dismiss();
		if (webErrer) {
			if (loginErrer) {
				this.universalData = universalData;
				setShowContent(universalData.getResultInfo().getUser_name(),universalData.getResultInfo().getKtype(),universalData.getResultInfo().getOrderlist() , universalData.getResultInfo().getTime());
				kid=universalData.getResultInfo().getKid();
			} else {

			}
		} else {
		}		
	}

}
