package com.ruiqi.chai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruiqi.utils.ChaiMyDialog;
import com.ruiqi.works.R;

public class ChaiQianKuan extends Activity implements OnClickListener{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chai_qiankuan);
		initView();
		maxMoney=getIntent().getFloatExtra("maxMoney", 0f);
		maxYaJinMoney=getIntent().getFloatExtra("maxYaJinMoney", 0f);
	}

	private TextView tvTitle,tv_back,tv_sum_money,tv_button_start;
	private EditText et_hetong,et_hetong_yajin,et_hetong_peijian,et_ranqi,et_yajin,et_peijian;
	private ImageView ivBack;
	private int sumMoney;
	private Float maxMoney,maxYaJinMoney;
	private void initView() {

		tvTitle=(TextView) findViewById(R.id.tvTitle);
		tvTitle.setText("欠款");
		tv_back=(TextView) findViewById(R.id.tv_back);
		ivBack=(ImageView) findViewById(R.id.ivBack);
		
		et_hetong_peijian=(EditText) findViewById(R.id.et_hetong_peijian);
		et_hetong_yajin=(EditText) findViewById(R.id.et_hetong_yajin);
		et_hetong=(EditText) findViewById(R.id.et_hetong);
		et_ranqi=(EditText) findViewById(R.id.et_ranqi);
		et_yajin=(EditText) findViewById(R.id.et_yajin);
		et_peijian=(EditText) findViewById(R.id.et_peijian);
		
		tv_sum_money=(TextView) findViewById(R.id.tv_sum_money);
		tv_button_start=(TextView) findViewById(R.id.tv_button_start);
		
		tvTitle.setOnClickListener(this);
		tv_back.setOnClickListener(this);
		tv_sum_money.setOnClickListener(this);
		tv_button_start.setOnClickListener(this);
		ivBack.setOnClickListener(this);
		sumMoney();
		
		
	}

	@Override
	public void onClick(View v) {
		Intent it=new Intent();
		switch (v.getId()) {
		case R.id.ivBack:
		case R.id.tv_back:
			setResult(0, it);
			finish();
			break;
		case R.id.tv_button_start:
			checkEditContent(it);
			break;
		default:
			break;
		}
	}
	/**
	 * 确认欠款详情
	 * @param it
	 */
	public void checkEditContent(Intent it){
		String hetong=et_hetong.getText().toString().trim();
		String hetongpeijian=et_hetong_peijian.getText().toString().trim();
		String hetongyajin=et_hetong_yajin.getText().toString().trim();
		
		String yajin= et_yajin.getText().toString().trim();
		String peijian=et_peijian.getText().toString().trim();
		String ranqi=et_ranqi.getText().toString().trim();
		
		if(maxYaJinMoney<(TextUtils.isEmpty(yajin)==true? 0f:Float.parseFloat(yajin))){
			ChaiMyDialog.getInstance().showHint(this, "当前订单押金最大欠款额度为"+maxYaJinMoney+"！！！");
			return;
		}
		if(sumMoney>maxMoney){
			ChaiMyDialog.getInstance().showHint(this, "欠款金额大于客户实际付款金额，请重新输入！");
			return;
		}else{
			it.putExtra("hetongpeijian", hetongpeijian);
			it.putExtra("hetongyajin", hetongyajin);
			it.putExtra("hetong", hetong);
			it.putExtra("yajin", yajin);
			it.putExtra("peijian", peijian);
			it.putExtra("ranqi", ranqi);
			it.putExtra("summoney",sumMoney);
			setResult(1, it);
			finish();
		}
	}
	
	/**
	 * 自动汇总欠款金额
	 */
	
	public void sumMoney(){
		
		et_ranqi.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s)){
					sumMoney=0;
					String str=et_peijian.getText().toString().trim();
					String str1=et_yajin.getText().toString().trim();
					sumMoney=Integer.parseInt(s.toString());
					if(!TextUtils.isEmpty(str)){
						sumMoney+=Integer.parseInt(str);
					}
					if(!TextUtils.isEmpty(str1)){
						sumMoney+=Integer.parseInt(str1);
					}
					tv_sum_money.setText("欠款金额￥"+sumMoney);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		et_peijian.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s)){
					sumMoney=0;
					String str=et_ranqi.getText().toString().trim();
					String str1=et_yajin.getText().toString().trim();
					sumMoney=Integer.parseInt(s.toString());
					if(!TextUtils.isEmpty(str)){
						sumMoney+=Integer.parseInt(str);
					}
					if(!TextUtils.isEmpty(str1)){
						sumMoney+=Integer.parseInt(str1);
					}
					tv_sum_money.setText("欠款金额￥"+sumMoney);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		et_yajin.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s)){
					sumMoney=0;
					String str=et_peijian.getText().toString().trim();
					String str1=et_ranqi.getText().toString().trim();
					sumMoney=Integer.parseInt(s.toString());
					if(!TextUtils.isEmpty(str)){
						sumMoney+=Integer.parseInt(str);
					}
					if(!TextUtils.isEmpty(str1)){
						sumMoney+=Integer.parseInt(str1);
					}
					tv_sum_money.setText("欠款金额￥"+sumMoney);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

}
