package com.ruiqi.works;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.BackBottle;
import com.ruiqi.bean.Bottle;
import com.ruiqi.bean.Weight;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.T;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 退瓶中扫描空瓶的界面
 * @author Administrator
 *
 */
public class BackSaoMiaoActivity extends BaseActivity implements OnCheckedChangeListener,OnItemLongClickListener{
	
	private RelativeLayout rl_input_canye;//需要设置显示和隐藏
	
	private TextView tv_next;
	
	private ToggleButton tb;
	
	private EditText et_input_zhexian,et_input_total;
	
	private String money,canye_weight;
	
	private List<String> mList;
	private List<Weight> list;
	
	private ListView lv_content;
	private CommonAdapter<Weight> adapter;
	
	private EditText et_input;
	private TextView tv_input;
	private ProgressDialog pd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_saomiao);
		setTitle("退瓶扫描空瓶");
		
		
	}
	
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	private void initData() {
		list = new ArrayList<Weight>();
		mList = (List<String>) getIntent().getSerializableExtra("mData");
		
		if(NfcActivity.mDataBottle!=null){
			for(int i =0;i<NfcActivity.mDataBottle.size();i++){
				list.add(new Weight(NfcActivity.mDataBottle.get(i).getXinpian(), 
						NfcActivity.mDataBottle.get(i).getType(), 
						NfcActivity.mDataBottle.get(i).getStatus()));
			}
		}
		
	}

	private void init() {
		rl_input_canye = (RelativeLayout) findViewById(R.id.rl_input_canye);
		rl_input_canye.setVisibility(View.GONE);
		
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_next.setOnClickListener(this);
		
		tb= (ToggleButton) findViewById(R.id.iv_toff);
		tb.setOnCheckedChangeListener(this);
		
		et_input_zhexian = (EditText) findViewById(R.id.et_input_zhexian);
		et_input_total = (EditText) findViewById(R.id.et_input_total);
		
		lv_content = (ListView) findViewById(R.id.lv_content);
		
		et_input = (EditText) findViewById(R.id.et_input);
		tv_input = (TextView) findViewById(R.id.tv_input);
		tv_input.setOnClickListener(this);
		
		pd = new ProgressDialog(this);
		pd.setMessage("正在加载......");
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SPUtils.put(BackSaoMiaoActivity.this, "UI", "BackSaoMiaoActivity");
		init();
		initData();
		
		System.out.println("list="+list);
		if(list!=null){
			
			adapter = new CommonAdapter<Weight>(BackSaoMiaoActivity.this,list,R.layout.ping_list_item) {
				@Override
				public void convert(ViewHolder helper, Weight item,int position) {
					helper.setText(R.id.tv_xinpian, item.getXinpian());
//					if(position==0){
//						helper.setText(R.id.tv_xuliehao, "序列号");
//					}else{
//						helper.setText(R.id.tv_xuliehao, position+"");
//					}
					helper.setText(R.id.tv_type, "1");
					//helper.setText(R.id.tv_status, item.getStatus());
					helper.setText(R.id.tv_status, "1");
				}
			};
			lv_content.setAdapter(adapter);
			lv_content.setOnItemLongClickListener(this);
		}
	}
	
	//将扫描的瓶和用户需要退的瓶作比较,数目相等才能跳转到下一步
	private boolean setBottle(){
		String depositsn = (String) SPUtils.get(BackSaoMiaoActivity.this, "depositsn", "error");
		int num = 0;
		if(BackBottleOrder.mData!=null){
			for(int i=0;i<BackBottleOrder.mData.size();i++){
				BackBottle b = BackBottleOrder.mData.get(i);
				if(b.getDepositsn().equals(depositsn)){
					List<Bottle> mlist = b.getList();
					for(int j=0;j<mlist.size();j++){
						num+=Integer.parseInt(mlist.get(j).getXinpian());
					}
					System.out.println("num="+num);
					if(NfcActivity.mDataBottle.size()!=num){
						T.showShort(BackSaoMiaoActivity.this, "扫描的瓶与退的瓶数目不一致");
						return false;
					}
				}
			}
		}
		return true;
	}
	//根据残液判断页面的跳转
	private boolean setCanYeJump(){
		if(Double.parseDouble(canye_weight)>0){
			if(Double.parseDouble(money)==0){
				T.showShort(BackSaoMiaoActivity.this, "请输入残液金额");
				return false;
			}
		}
		
		if(Double.parseDouble(money)>0){
			if(Double.parseDouble(canye_weight)==0){
				T.showShort(BackSaoMiaoActivity.this, "请输入残液重量");
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.tv_next:
			//如果没有扫描瓶，也没有输入瓶
			if(NfcActivity.mDataBottle==null){					
				T.showShort(BackSaoMiaoActivity.this, "没有扫描任何瓶");
			}else{
				if(NfcActivity.mDataBottle.size()==0){
					T.showShort(BackSaoMiaoActivity.this, "没有扫描任何瓶");
				}else{
						money = et_input_zhexian.getText().toString().trim();
						canye_weight = et_input_total.getText().toString().trim();
						if(money==null||money.equals("")){
							money="0";
						}
						if(canye_weight==null||canye_weight.equals("")){
							canye_weight="0";
						}
						if(setCanYeJump()){
							SPUtils.remove(BackSaoMiaoActivity.this, "UI");
							if(NfcActivity.mData!=null){
								NfcActivity.mData=null;
							}
							intent = new Intent(BackSaoMiaoActivity.this,BackSureActivity.class);
							intent.putExtra("mData", (Serializable)list);
							intent.putExtra("money", money);
							intent.putExtra("canye_weight", canye_weight);
						}
				}
			}
			break;
		case R.id.tv_input://手动输入
			if(NfcActivity.mDataBottle==null){
				NfcActivity.mDataBottle = new ArrayList<Weight>();
			}
			String str = et_input.getText().toString();
			new InPutIsBottle(str, BackSaoMiaoActivity.this, list, adapter, pd, 
					NfcActivity.mDataBottle).addDataToList();
			break;

		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
			finish();
		}
	}
	
	@Override
	public void jumpPage() {
		super.jumpPage();
		if(NfcActivity.mData!=null){
			NfcActivity.mData=null;
		}
		if(NfcActivity.mDataBottle!=null){
			NfcActivity.mDataBottle=null;
		}
		SPUtils.remove(BackSaoMiaoActivity.this, "UI");
		finish();
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
		if(isChecked){
			rl_input_canye.setVisibility(View.VISIBLE);
		}else{
			rl_input_canye.setVisibility(View.GONE);
		}
		
	}



	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		CurrencyUtils.onLongClickDelete(position, BackSaoMiaoActivity.this, adapter, list, NfcActivity.mDataBottle,NfcActivity.mData);
		return false;
	}

}
