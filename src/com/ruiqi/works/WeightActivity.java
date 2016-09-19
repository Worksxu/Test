package com.ruiqi.works;

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

import com.ruiqi.bean.Orderdeail;
import com.ruiqi.bean.Type;
import com.ruiqi.bean.Weight;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.InPutIsBottle;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

//扫描重瓶界面
public class WeightActivity extends SaoMiaoActivity{
	
	private String show;
	
	private List<String > mList;
	
	public static List<Weight> list;
	
	private WeightActivity wa;
	
	private List<Weight> mData;
	
	private int num;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("扫描重瓶");
		Log.e("lll", "返回重瓶");
		System.out.println("+++"+SPutilsKey.type);
	}
	@Override
	protected void onResume() {
		super.onResume();
		SPUtils.put(WeightActivity.this, "UI", "weight");
		show = getIntent().getStringExtra("show");
		num=0;
		//num = setNum();
	}
	

	@Override
	public Activity getActivity() {
		return this;
	}
	//根据不同的界面，设置需要扫描的个数
	private int  setNum(){
		int num =0;
		if("CreateOrderActivity".equals(show)){//创建订单
			for(int i=0;i<CreateOrderActivity.list.size();i++){
				Type t = CreateOrderActivity.list.get(i);
				num+=Integer.parseInt(t.getNum());
			}
		}else if("OrderInfoActivity".equals(show)){
			for(int i=0;i<OrderInfoActivity.list.size();i++){
				Orderdeail o = OrderInfoActivity.list.get(i);
				num+=Integer.parseInt(o.getNum());
			}
		}
		return num;
	}

	@Override
	public int initFlag() {
		show = getIntent().getStringExtra("show");
		if(show.equals("CreateOrderActivity")){
			SPUtils.put(WeightActivity.this, "NFC", "createOrder");
			return 2;//隐藏
		}else if(show.equals("OrderInfoActivity")){
			SPUtils.put(WeightActivity.this, "NFC", "orderInfo");
			return 2;
		}else{
			return 1;//显示
		}
	}
	
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = null;
		switch (v.getId()) {
		case R.id.next://下一步
			intent = initIntent(intent);
			break;
		case R.id.tv_input://手动输入
			if(NfcActivity.mDataWeight==null){
				NfcActivity.mDataWeight = new ArrayList<Weight>();
			}
			str = et_input.getText().toString().trim();
			//柴钢瓶判断
			new InPutIsBottle(str, WeightActivity.this, mData, adapter, pd, GpDao.getInstances(WeightActivity.this),NfcActivity.mDataWeight,1).addDataToList();
			break;
		
		default:
			break;
		}
		if(CreateOrderActivity.OrderKind==1){
			if(intent!=null){
				if(mData.size()==num){
					if(NfcActivity.mData!=null){
						NfcActivity.mData=null;
					}
					SPUtils.remove(WeightActivity.this, "UI");
					startActivity(intent);
					finish();
				}else if(mData.size()<num){
					T.showShort(WeightActivity.this, "瓶的数量小于用户实际的需求");
				}else if(mData.size()>num){
					T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
				}
			}
		}else if(CreateOrderActivity.OrderKind==2||CreateOrderActivity.OrderKind==3||CreateOrderActivity.OrderKind==4){
			if(intent!=null){
				if(mData.size()>0){
					if(NfcActivity.mData!=null){
						NfcActivity.mData=null;
					}
					SPUtils.remove(WeightActivity.this, "UI");
					startActivity(intent);
					finish();
				}else if(mData.size()==0){
					T.showShort(WeightActivity.this, "请扫描钢瓶");
				}else if(mData.size()>num){
					T.showShort(WeightActivity.this, "瓶的数量大于用户实际的需求");
				}
			}
		}
	}
	@Override
	public void jumpPage() {
		super.jumpPage();
		
		SPutilsKey.type=0;
		if(NfcActivity.mData!=null){
			NfcActivity.mData=null;
		}
		if(NfcActivity.mDataWeight!=null){
			NfcActivity.mDataWeight=null;
		}
		if(list!=null){
			list=null;
		}
		SPUtils.remove(WeightActivity.this, "UI");
		
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==event.KEYCODE_BACK){
			SPutilsKey.type=0;
			if(NfcActivity.mData!=null){
				NfcActivity.mData=null;
			}
			if(NfcActivity.mDataWeight!=null){
				NfcActivity.mDataWeight=null;
			}
			if(list!=null){
				list=null;
			}
			SPUtils.remove(WeightActivity.this, "UI");
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private Intent initIntent(Intent intent) {
		switch (SPutilsKey.type) {
		//创建订单
		case 1://新用户
			intent = new Intent(WeightActivity.this, SubsidiaryActivity.class);//押金和折现
			intent.putExtra("flag", "weightbottle");
			intent.putExtra("show", show);
			break;
		case 2://老用户
			intent = new Intent(WeightActivity.this, NullActivity.class);//空瓶
			intent.putExtra("show", show);
			break;
		default:
			T.showShort(WeightActivity.this, "请选择用户身份");
			break;
		}
		return intent;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}
	
	@Override
	
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	@Override
	public List<Weight> initList() {
		mData = new ArrayList<Weight>();
		if(NfcActivity.mDataWeight!=null){
			for(int i=0;i<NfcActivity.mDataWeight.size();i++){
				Weight w = NfcActivity.mDataWeight.get(i);
				mData.add(new Weight(w.getXinpian(), w.getType(), w.getStatus()));
			}
		}
		System.out.println("mDataWeight===="+ NfcActivity.mDataWeight);
		return  mData;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		System.out.println("NfcActivity.mData="+NfcActivity.mData);
		CurrencyUtils.onLongClickDelete(position, WeightActivity.this,adapter,mData,NfcActivity.mDataWeight,NfcActivity.mData);
		return false;
	}

	public void add(){
		int a = 1;
		a++;
	}

}
