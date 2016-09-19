package com.ruiqi.chai;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.ruiqi.bean.Weight;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.works.NfcActivity;
import com.ruiqi.works.R;
import com.ruiqi.works.SaoMiaoActivity;
import com.ruiqi.works.SubsidiaryActivity;
import com.ruiqi.works.WeightActivity;

//扫空瓶界面
public class ChaiNullActivity extends SaoMiaoActivity{
	
	public static List<Weight> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("扫描空瓶");
	}

	@Override
	public int initFlag() {
		return 2;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.e("lll", "返回空瓶自己的");
		SPUtils.put(ChaiNullActivity.this, "UI", "chainull");
	}
	

	@Override
	public Activity getActivity() {
		return this;
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		
		case R.id.next: //跳转到安全报告界面或者残气，折旧
			SPUtils.put(ChaiNullActivity.this, "deposit", "0");
			SPUtils.put(ChaiNullActivity.this, "ping_total", "0");
			SPUtils.put(ChaiNullActivity.this, "canye", "0");
			if(NfcActivity.mData!=null){
				NfcActivity.mData=null;
			}
			SPUtils.remove(ChaiNullActivity.this, "UI");
			if(NfcActivity.mDataNull==null){
				NfcActivity.mDataNull = new ArrayList<Weight>();
			}
		/*	if(SPutilsKey.type==1){
				intent = new Intent(NullActivity.this, SelfActivity.class);
			}else if(SPutilsKey.type==2){*/
				intent = new Intent(ChaiNullActivity.this, ChaiSubsidiaryActivity.class);
				intent.putExtra("flag", "nullbottle");
				intent.putExtra("show", getIntent().getStringExtra("show"));
			//}
			break;
			
		case R.id.tv_input:
			if(NfcActivity.mDataNull==null){
				NfcActivity.mDataNull = new ArrayList<Weight>();
			}
			str = et_input.getText().toString();
			new InPutIsBottle(str, ChaiNullActivity.this, mData, adapter, pd, 
					NfcActivity.mDataNull).addDataToList();
			break;

		default:
			break;
		}
		if(intent!=null){
			startActivity(intent);
		}
	}
	
    
	@Override
	public Handler[] initHandler() {
		return null;
	}
	
	@Override
	public void jumpPage() {
		super.jumpPage();
		if(NfcActivity.mData!=null){
			NfcActivity.mData=null;
		}
		if(NfcActivity.mDataNull!=null){
			NfcActivity.mDataNull=null;
		}
		SPUtils.remove(ChaiNullActivity.this, "UI");
		Intent intent = new Intent(ChaiNullActivity.this, ChaiWeightActivity.class);
		intent.putExtra("show", getIntent().getStringExtra("show"));
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(NfcActivity.mData!=null){
				NfcActivity.mData=null;
			}
			if(NfcActivity.mDataNull!=null){
				NfcActivity.mDataNull=null;
			}
			SPUtils.remove(ChaiNullActivity.this, "UI");
			Intent intent = new Intent(ChaiNullActivity.this, WeightActivity.class);
			intent.putExtra("show", getIntent().getStringExtra("show"));
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}
	
	private List<Weight> mData;

	@Override
	public List<Weight> initList() {
		mData = new ArrayList<Weight>();
		if(NfcActivity.mDataNull!=null){
			for(int i=0;i<NfcActivity.mDataNull.size();i++){
				Weight w = NfcActivity.mDataNull.get(i);
				
				mData.add(new Weight(w.getXinpian(), w.getType(), w.getStatus()));
			}
		}
		return mData;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		CurrencyUtils.onLongClickDelete(position, ChaiNullActivity.this,adapter,mData,NfcActivity.mDataNull,NfcActivity.mData);
		return false;
	}

}
