package com.ruiqi.works;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.ruiqi.db.GpDao;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.HttpUtil.ParserData;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;

public class StartPager extends Activity implements ParserData{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_pager);
		initView();
	}

	private ImageView iv;
	private GpDao gd;
	private void initView() {
		gd = GpDao.getInstances(this);
		final String str=(String) SPUtils.get(StartPager.this, "AddressData","0" );
		if(str.equals("0")){
			initAddressData();
		}
		iv = (ImageView) findViewById(R.id.iv);
		 ScaleAnimation scale=new ScaleAnimation(1, 1, 1,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,0.5f);
		 scale.setDuration(1000);
		 scale.setFillAfter(true);
		 iv.setAnimation(scale);
		 scale.setAnimationListener(new AnimationListener() {
		 @Override
		 public void onAnimationStart(Animation animation) {
		 }
		 @Override
		 public void onAnimationRepeat(Animation animation) {
		 }
		 @Override
		 public void onAnimationEnd(Animation animation) {
			 if(str.equals("1")){
				 Intent it=new Intent(StartPager.this,LoginActivity.class);
				 startActivity(it);
				 finish();
			 }
		 }
		 });
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}
	/**
	 * 创建地址
	 */
	public void initAddressData(){
		
		HttpUtil httpUtil=new HttpUtil();
		RequestParams params = new RequestParams(RequestUrl.ADDRESSLIST);
		httpUtil.setParserData(this);
		httpUtil.PostHttp(params,1 );
	}
	
	@Override
	public void sendResult(String result, int what) {
		Log.e("lll地址", result);
		
		JSONObject jsb;
		try {
			jsb = new JSONObject(result);
			if(jsb.getInt("resultCode")==1){
				JSONArray jsa=jsb.getJSONArray("resultInfo");
				for (int i = 0; i < jsa.length(); i++) {
					JSONObject jsob=jsa.getJSONObject(i);
					gd.insertAddress(jsob.getString("code"), jsob.getString("pcode"), jsob.getString("name"));
				}
				SPUtils.put(StartPager.this, "AddressData","1" );
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent it=new Intent(StartPager.this,LoginActivity.class);
		 startActivity(it);
		 finish();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

}
