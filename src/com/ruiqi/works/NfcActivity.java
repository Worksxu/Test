package com.ruiqi.works;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.bean.Weight;
import com.ruiqi.chai.ChaiBackSaoMiaoActivity;
import com.ruiqi.chai.ChaiNullActivity;
import com.ruiqi.chai.ChaiWeightActivity;
import com.ruiqi.db.GpDao;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;

public class NfcActivity extends BaseActivity {
	
	private TextView tv_result_show;
	
	private NfcA mTag;
	private String result;
	
	private Button btn_sure;
	private String ui;
	private String ship_id;
	private String shop_id;
	
	private   List<String> mList;
	
	public static List<Weight> mData;
	
	public static List<Weight> mDataWeight;//重瓶
	public static List<Weight> mDataNull;//空瓶
	public static List<Weight> mDataBottle;//退瓶
	public static List<Weight> mDataRecei;
	
	private GpDao gd;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String result = (String) msg.obj;
			paseData(result);
		}

		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		setTitle("扫描结果");
		gd = GpDao.getInstances(this);
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
		tv_result_show = (TextView) findViewById(R.id.tv_result_show);
		mList = new ArrayList<String>();
		
		ui = (String) SPUtils.get(NfcActivity.this, "UI", "error");
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_sure:
			
			if(ui.equals("chaiweight")){
				mDataWeight = new ArrayList<Weight>();
				List<String> list = new ArrayList<String>();
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					//先判断是否在送气工库存里
					String xinpian = mData.get(i).getXinpian();
					list.add(xinpian);
					System.out.println("mData="+list);
					System.out.println("xinpian="+xinpian);
				}
//			
				for(int j = 0;j<list.size();j++){
					Cursor c=gd.readDb.query("gangpingxinxi", null,"xinpin=?" , new String[] {list.get(j)}, null, null, null);
							if(c.getCount()!=0&&c.getString(c.getColumnIndex("is_open")).equals("1")){
								c.moveToFirst();
						mDataWeight.add(new Weight(list.get(j),c.getString(c.getColumnIndex("type")), "1"));
					}else{
						T.showShort(NfcActivity.this, list.get(j)+"不在库中");
						//删除mdata对应的值
						mData.remove(j);
					}
				}
				Intent intent = new Intent(NfcActivity.this, ChaiWeightActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				String from = (String) SPUtils.get(NfcActivity.this, "NFC", "error");
				if("chaicreateOrder".equals(from)){
					intent.putExtra("show", "ChaiCreateOrderActivity");
				}else if("orderInfo".equals(from)){
					intent.putExtra("show", "OrderInfoActivity");
				}
				startActivity(intent);
				finish();
				
			}else if(ui.equals("weight")){
				mDataWeight = new ArrayList<Weight>();
				List<String> list = new ArrayList<String>();
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					//先判断是否在送气工库存里
					String xinpian = mData.get(i).getXinpian();
					list.add(xinpian);
					System.out.println("mData="+list);
					System.out.println("xinpian="+xinpian);
				}
//			
				for(int j = 0;j<list.size();j++){
					Cursor c=gd.readDb.query("gangpingxinxi", null,"xinpin=?" , new String[] {list.get(j)}, null, null, null);
							if(c.getCount()!=0&&c.getString(c.getColumnIndex("is_open")).equals("1")){
								c.moveToFirst();
						mDataWeight.add(new Weight(list.get(j),c.getString(c.getColumnIndex("type")), "1"));
					}else{
						T.showShort(NfcActivity.this, list.get(j)+"不在库中");
						//删除mdata对应的值
						mData.remove(j);
					}
				}
				Intent intent = new Intent(NfcActivity.this, WeightActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				String from = (String) SPUtils.get(NfcActivity.this, "NFC", "error");
				if("createOrder".equals(from)){
					intent.putExtra("show", "CreateOrderActivity");
				}else if("orderInfo".equals(from)){
					intent.putExtra("show", "OrderInfoActivity");
				}
				startActivity(intent);
				finish();
			}else if(ui.equals("chainull")){
				mDataNull = new ArrayList<Weight>();
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					mDataNull.add(new Weight(mData.get(i).getXinpian(), mData.get(i).getType(), mData.get(i).getStatus()));
				}
				Intent intent = new Intent(NfcActivity.this, ChaiNullActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				String from = (String) SPUtils.get(NfcActivity.this, "NFC", "error");
				if("chaicreateOrder".equals(from)){
					intent.putExtra("show", "ChaiCreateOrderActivity");
				}else if("orderInfo".equals(from)){
					intent.putExtra("show", "OrderInfoActivity");
				}
				startActivity(intent);
				finish();
				
			}else if(ui.equals("null")){
				mDataNull = new ArrayList<Weight>();
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					mDataNull.add(new Weight(mData.get(i).getXinpian(), mData.get(i).getType(), mData.get(i).getStatus()));
				}
				Intent intent = new Intent(NfcActivity.this, NullActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				String from = (String) SPUtils.get(NfcActivity.this, "NFC", "error");
				if("createOrder".equals(from)){
					intent.putExtra("show", "CreateOrderActivity");
				}else if("orderInfo".equals(from)){
					intent.putExtra("show", "OrderInfoActivity");
				}
				startActivity(intent);
				finish();
			}else if(ui.equals("recei")){//领瓶
				mDataRecei = new ArrayList<Weight>();
				List<String> list = new ArrayList<String>();
				//先判断在不在库存中，在则不能继续领取
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					String xinpian = mData.get(i).getXinpian();
					list.add(xinpian);
				}
				for(int j=0;j<list.size();j++){
					//根据芯片号查询
					Cursor c=gd.readDb.query("gangpingxinxi", null,"xinpin=?" , new String[] {list.get(j)}, null, null, null);
					if(c.getCount()==0){//没查到,插入
						mDataRecei.add(new Weight(list.get(j), "1", "1"));
					}else{
						T.showShort(NfcActivity.this, "该瓶已在库存");
						mData.remove(j);
					}
				}
				Intent intent = new Intent(NfcActivity.this, GoodsReceiActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				startActivity(intent);
				finish();
			}else if(ui.equals("ChaiBackSaoMiaoActivity")){
				mDataBottle = new ArrayList<Weight>();
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					Cursor c=gd.readDb.query("gangpingxinxi", null,"xinpin=?" , new String[] {mData.get(i).getXinpian()}, null, null, null);
					if(c.getCount()==0){//没查到,插入
						mDataBottle.add(new Weight(mData.get(i).getXinpian(), mData.get(i).getType(), mData.get(i).getStatus(),mData.get(i).getNumber(),mData.get(i).getType_name()));
					}else{
						T.showShort(NfcActivity.this, "该瓶已在库存");
						mData.remove(i);
					}
				}
				Intent intent = new Intent(NfcActivity.this, ChaiBackSaoMiaoActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				startActivity(intent);
				finish();
			}else if(ui.equals("BackSaoMiaoActivity")){
				mDataBottle = new ArrayList<Weight>();
				//吧mData 复制给mdataweight
				for(int i=0;i<mData.size();i++){
					Cursor c=gd.readDb.query("gangpingxinxi", null,"xinpin=?" , new String[] {mData.get(i).getXinpian()}, null, null, null);
					if(c.getCount()==0){//没查到,插入
						mDataBottle.add(new Weight(mData.get(i).getXinpian(), mData.get(i).getType(), mData.get(i).getStatus()));
					}else{
						T.showShort(NfcActivity.this, "该瓶已在库存");
							mData.remove(i);
					}
				}
				Intent intent = new Intent(NfcActivity.this, BackSaoMiaoActivity.class);
				intent.putExtra("mData", (Serializable)mList);
				startActivity(intent);
				finish();
			}else{
				T.showShort(NfcActivity.this, "请前往相应的界面进行扫瓶操作");
				mData.clear();
				finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}
	
	
	private void paseData(String resul) {
		Log.e("lll", resul);
		try {
			JSONObject obj = new JSONObject(resul);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				JSONObject jsob=obj.getJSONObject("resultInfo");
				String type=jsob.getString("type");
				String number=jsob.getString("number");
				String xinpian=jsob.getString("xinpian");
				String type_name="";
				if(jsob.has("type_name")){
					 type_name=jsob.getString("type_name");
				}
				mData.add(new Weight(xinpian,type, "1",number,type_name));
			}else{
				T.showShort(NfcActivity.this, "此钢瓶不属于本门店");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(mData==null){
			mData = new ArrayList<Weight>();
		}
		ship_id = (String) SPUtils.get(NfcActivity.this, SPutilsKey.SHIP_ID, "error");
		shop_id = (String) SPUtils.get(NfcActivity.this, SPutilsKey.SHOP_ID, "");
		 if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())
					|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent().getAction())
					|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(getIntent().getAction())) {
	            //处理该intent   
	            processIntent(getIntent()); 
	            if(!CurrencyUtils.getStrFlag(result, mData)){
	            	RequestParams params = new RequestParams(RequestUrl.IS_BOTTLE);
	            	params.addBodyParameter("xinpian", result);
	            	if(ui.equals("recei")||ui.equals("weight")){
	            		Log.e("lll", "在这里"+shop_id);
	            		params.addBodyParameter("shop_id", shop_id);
	            		params.addBodyParameter("is_open", "1");//
	            	}  
	            	if(ui.equals("null")|| ui.equals("BackSaoMiaoActivity")){//空瓶
	            		params.addBodyParameter("is_open", "0");//
	            	}
	            	Log.e("llll", "请求刚瓶信息"+params.getStringParams().toString());
	            	HttpUtil.PostHttp(params, handler,new ProgressDialog(NfcActivity.this));
				}else{
					Toast.makeText(NfcActivity.this, "该瓶已经扫过", Toast.LENGTH_SHORT).show();
				}
		 }
	}
		 
		 /**
			 * 对intent进行处理
			 * @param intent
			 */
	private void processIntent(Intent intent) {
	 //取出封装在intent中的TAG   
		Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);  
		        
		        
		   for (String tech : tagFromIntent.getTechList()) {   
		        System.out.println("tech="+tech);   
		     } 
		        mTag =NfcA.get(tagFromIntent);
		        if(mTag!=null){
		        	try {
						mTag.connect();//连接卡
						if(mTag.isConnected()){
							System.out.println("已连接");
						}else{
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
		        }
		        //读取数据
		        readTag(mTag);
		      
			}
			
			/**
			 * 读取数据
			 * @param mfc 
			 */
			private String readTag(NfcA mfc) {
				// 读取TAG
				byte [] bt = mfc.getTag().getId();
				String id="";
				for(int i=0;i<bt.length;i++){
					System.out.println("扫描码"+bt[i]);
					id+=bt[i];
				}
				result = bytesToHexString(bt);
				mList.add(result);
				tv_result_show.setText(result);
				return null;
			}
			
			/**
			 * 字符序列转换为16进制字符串  
			 * @param data
			 * @return
			 */
			private String bytesToHexString(byte[] src) {
				 StringBuilder stringBuilder = new StringBuilder();   
			        if (src == null || src.length <= 0) {   
			            return null;   
			        }   
			        char[] buffer = new char[2];   
			        for (int i = 0; i < src.length; i++) {   
			            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);   
			            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);   
			            System.out.println(buffer);   
			            stringBuilder.append(buffer);   
			        }   
			        return stringBuilder.toString().toUpperCase(); 
			}

			
		
			
			
			@Override
			public Handler[] initHandler() {
				return null;
			}

			
			

}
