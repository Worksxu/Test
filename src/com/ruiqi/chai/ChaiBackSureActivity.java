package com.ruiqi.chai;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.bean.Weight;
import com.ruiqi.chai.ChaiHttpSearchContractmoData.ParserSearchContractmoData;
import com.ruiqi.chai.ChaiHttpTuiYaJinToCustomer.ParserTuiYaJinToCustomerData;
import com.ruiqi.db.BackBottleDao;
import com.ruiqi.fragment.BackSureFragment;
import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.utils.ChaiMyDialog.ChaiCallBack;
import com.ruiqi.utils.ChaiMyDialog.ChaiYaJinTiaoCallBack;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.works.MainActivity;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;

/**
 * 退瓶的确定界面
 * 
 * @author Administrator
 *
 */
public class ChaiBackSureActivity extends FragmentActivity implements
		android.view.View.OnClickListener, ParserTuiYaJinToCustomerData ,ChaiYaJinTiaoCallBack,ChaiCallBack{

	private String doormoney, money, id,canye_weight,shurumoney;

	private TextView tv_money, tv_pay, tv_yuqi_weight, tv_yuqi_money,tv_shuru_yajin_money,
			tv_ordersn;

	private List<Weight> list;

	private BackBottleDao bbd;
	private RelativeLayout rl_pay;

	private TextView tv_order_commit;
	private ProgressDialog pd;
	private String yajinmoney;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chai_activity_backsure);

		bbd = BackBottleDao.getInstances(this);
		pd = new ProgressDialog(this);
		pd.setMessage("正在提交......");
		init();
		initData();
		BackSureFragment bsf = new BackSureFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("mData", (Serializable) NfcActivity.mDataBottle);
		bsf.setArguments(bundle);
		getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, bsf).commit();
	}

	private void initData() {
		money = getIntent().getStringExtra("money");
		canye_weight = getIntent().getStringExtra("canye_weight");
		tv_ordersn.setText(SPutilsKey.tuipingordersn);
		if (!TextUtils.isEmpty(canye_weight)) {
			tv_yuqi_money.setText("￥" + money);
			tv_yuqi_weight.setText(canye_weight + "kg");
		}
		tv_pay.setText(sumMoney()+"");
		list = new ArrayList<Weight>();

		tv_order_commit.setText("确定付款");
		tv_order_commit.setOnClickListener(this);
	}

	private TextView tv_back, tv_title,tv_yajin;
	private ImageView ivBack;
	private TextView et_input;
	private ProgressDialog pds;
	private LinearLayout ll_shuru_yajin;

	private void init() {
		ChaiHttpTuiYaJinToCustomer.getInstance().setOnParserTuiYaJinToCustomerData(this);
		pds=new ProgressDialog(this);
		pds.setMessage("搜索中...");
		tv_back = (TextView) findViewById(R.id.tv_back);
		ivBack = (ImageView) findViewById(R.id.ivBack);
		tv_title = (TextView) findViewById(R.id.tvTitle);
		rl_pay = (RelativeLayout) findViewById(R.id.rl_pay);
		tv_pay = (TextView) findViewById(R.id.tv_pay);
		tv_ordersn = (TextView) findViewById(R.id.tv_ordersn);
		tv_yuqi_weight = (TextView) findViewById(R.id.tv_yuqi_weight);
		tv_yuqi_money = (TextView) findViewById(R.id.tv_yuqi_money);
		et_input = (TextView) findViewById(R.id.et_input);
		tv_yajin = (TextView) findViewById(R.id.tv_yajin);
		ll_shuru_yajin = (LinearLayout) findViewById(R.id.ll_shuru_yajin);
		tv_shuru_yajin_money = (TextView) findViewById(R.id.tv_shuru_yajin_money);
		tv_title.setText("退瓶确认");

		tv_order_commit = (TextView) findViewById(R.id.tv_order_commit);

		tv_back.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		tv_order_commit.setOnClickListener(this);
		rl_pay.setOnClickListener(this);
		et_input.setOnClickListener(this);
		ll_shuru_yajin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_shuru_yajin:// 输入押金的金额
			ChaiMyDialog.getInstance().setCallBack(this);
			ChaiMyDialog.getInstance().showYaJin(this, "请输入退押金金额！！", 0);;
			break;
		case R.id.tv_order_commit:// 确定付款
			surePay();
			break;
		case R.id.ivBack: 
		case R.id.tv_back:
			finish();
			break;
		case R.id.et_input:
			ChaiMyDialog.getInstance().setYaJinTiaoCallBack(this);
			ChaiMyDialog.getInstance().showYaJinTiao(this, "请输入押金条编码：");
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ChaiBackSureActivity.this,ChaiBackSaoMiaoActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);   
	}

	/**
	 * 确定付款
	 */   
	private void surePay() {
		String shop_id = (String) SPUtils.get(ChaiBackSureActivity.this,SPutilsKey.SHOP_ID, "error");
		String shipper_id = (String) SPUtils.get(ChaiBackSureActivity.this,SPutilsKey.SHIP_ID, "error");
		String str=tv_shuru_yajin_money.getText().toString().trim();
		if(TextUtils.isEmpty(str)||str.equals("请输入退押金金额")){
			ChaiMyDialog.getInstance().setCallBack(this);
			ChaiMyDialog.getInstance().showYaJin(this, "请输入退押金金额！！", 0);
			return;
		}
		pd.show();
		ChaiHttpTuiYaJinToCustomer.getInstance().app2server(SPutilsKey.tuipingordersn,tv_pay.getText().toString().trim(), SPutilsKey.tuipingkid,getJsonStr(NfcActivity.mDataBottle), money, canye_weight,shop_id, shipper_id);
	}

	/**
	 * 拼接json串
	 */
	private String getJsonStr(List<Weight> sList) {
		JSONArray array = new JSONArray();
		for (int i = 0; i < sList.size(); i++) {
			array.put(sList.get(i).getXinpian());
		}
		return array.toString();
	}

	@Override
	public void parserTuiYaJinToCustomerResult(boolean webErrer,boolean loginErrer, UniversalData universalData) {
		if(webErrer){
			if(loginErrer){
				Toast.makeText(this, "提交成功！", Toast.LENGTH_SHORT).show();
				Intent it=new Intent(this,MainActivity.class);
				it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(it);
				finish();
			}else{}
		}else{
			
		}

	}
	@Override
	public void chaiYaJinTiaoStringCallBack(String str) {
		if(!TextUtils.isEmpty(str)){
			et_input.setText(str);   
		}
	}
/**
 * 计算实际退款
 * @return
 */
	public float sumMoney(){
		float moneys=0f;
		if(!TextUtils.isEmpty(shurumoney)){
			moneys+=Float.parseFloat(shurumoney)+Float.parseFloat(money);
		}else{
			moneys+=Float.parseFloat(money);
		}
		return moneys;
	}

@Override
public void chaiStringCallBack(String str) {
	
}

@Override
public void chaiIntCallBack(int in, int where) {
	shurumoney=in+"";
	tv_shuru_yajin_money.setText("￥"+shurumoney);
	tv_pay.setText(sumMoney()+"");	
}
}
