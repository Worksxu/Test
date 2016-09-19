package com.ruiqi.chai;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;

import android.util.Log;

public class ChaiHttputils {
	
	public  ChaiHttputils(){}
	
	public void getDataFromServer(RequestParams params){
		
		x.http().post(params, new CommonCallback<String>() {
			
			@Override
			public void onCancelled(CancelledException arg0) {
				Log.v("lll", "请求取消："+arg0);
			}
			
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.v("lll", "请求错误："+arg0);
				success.getSuccessData(false,arg0+"");
			}
			
			@Override
			public void onFinished() {
				
			}
			
			@Override
			public void onSuccess(String arg0) {
				Log.e("lll", arg0);
				success.getSuccessData(true,arg0);
			}
		});
	}
	
	private Success success;
	public interface Success{
		public abstract void getSuccessData(boolean bool,String result);
	}
	public void setOnSuccess(Success success){
		this.success=success;
	}
	
}
