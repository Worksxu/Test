package com.ruiqi.works;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruiqi.bean.Weight;
import com.ruiqi.fragment.ApplyPeiJianFragment;
import com.ruiqi.fragment.BootleWeightFragment;
import com.ruiqi.utils.MyActivityManager;
import com.ruiqi.utils.SPUtils;
/**
 * 商品领取界面
 * @author Administrator
 *
 */
public class GoodsReceiActivity extends FragmentActivity implements OnCheckedChangeListener,OnClickListener{
	
	private RadioGroup rg_select;
	
	private RelativeLayout rl_back;
	
	private TextView apply;
	private boolean flag = true;
	
	private List<Weight> list;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_recei);
		MyActivityManager.getInstance().pushOneActivity(this);
		init();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	private BootleWeightFragment boot;
	private void initData() {
		if(flag){
			list = new ArrayList<Weight>();
			List<String> mList = (List<String>) getIntent().getSerializableExtra("mData");
			if(NfcActivity.mDataRecei!=null){
				for(int i =0;i<NfcActivity.mDataRecei.size();i++){
					list.add(new Weight(NfcActivity.mDataRecei.get(i).getXinpian(), "1", "1"));
				}
			}
			boot = new BootleWeightFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mData", (Serializable)list);
			boot.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, boot).commitAllowingStateLoss();
		}else{
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new ApplyPeiJianFragment()).commit();
			apply.setVisibility(View.GONE);
			apply.setText("申请配件");
		}
		
		
	}
	
	

	private void init() {
		rg_select = (RadioGroup) findViewById(R.id.rg_select);
		rg_select.setOnCheckedChangeListener(this);
		
		rl_back = (RelativeLayout) findViewById(R.id.rl_back);
		rl_back.setOnClickListener(this);
		
		apply = (TextView) findViewById(R.id.apply);
		apply.setOnClickListener(this);
		
		apply.setText("下一步");
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		SPUtils.put(GoodsReceiActivity.this, "UI", "recei");
		initData();
	}
	

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_weight://重瓶领取
			//切换到收入fragment
			BootleWeightFragment boot = new BootleWeightFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("mData", (Serializable)list);
			boot.setArguments(bundle);
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, boot).commit();
			flag = true;
			apply.setVisibility(View.VISIBLE);
			apply.setText("下一步");
			break;
		case R.id.rb_peijian://配件领取
			//切换到支出界面（先申请，再领取）
			getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, new ApplyPeiJianFragment()).commit();
			flag = false;
			apply.setVisibility(View.GONE);
			apply.setText("申请配件");
			//直接领取
//			if(NfcActivity.mData!=null){
//				NfcActivity.mData=null;
//			}
//			Intent intent = new Intent(GoodsReceiActivity.this, ApplyPjDeailActivity.class);
//			startActivity(intent);
//			finish();
//			SPUtils.remove(GoodsReceiActivity.this, "UI");
//			break;

		default:
			break;
		}
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_back:
			if(NfcActivity.mData!=null){
				NfcActivity.mData=null;
			}
			SPUtils.remove(GoodsReceiActivity.this, "UI");
			finish();
			break;
		case R.id.apply:
			if(NfcActivity.mData!=null){
				NfcActivity.mData=null;
			}
			SPUtils.remove(GoodsReceiActivity.this, "UI");
			if(flag==true){
				Intent intent = new Intent(GoodsReceiActivity.this, BottleWeightDeails.class);
				intent.putExtra("mData", (Serializable)list);
				startActivity(intent);
				
			}else{
				Intent intent = new Intent(GoodsReceiActivity.this, ApplyPjDeailActivity.class);
				startActivity(intent);
			}
			break;

		default:
			break;
		}
	}




}
