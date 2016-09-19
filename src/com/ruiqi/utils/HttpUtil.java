package com.ruiqi.utils;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 网络请求的类 使用的是xutils 3.0 框架
 * 
 * @author Administrator
 *
 */
public class HttpUtil {
	/**
	 * 基本的post 请求
	 */
	public static void PostHttp(RequestParams params, final Handler handler,final ProgressDialog pd) {
		x.http().post(params, new CommonCallback<String>() {
			@Override
			public void onCancelled(CancelledException arg0) {
				pd.dismiss();
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("lll", "网络错误="+arg0);
				System.out.println("网络连接失败");
				Toast.makeText(x.app(), "网络连接失败", Toast.LENGTH_SHORT).show();
				pd.dismiss();
			}

			@Override
			public void onFinished() {
				pd.dismiss();
			}

			@Override
			public void onSuccess(String arg0) {
				pd.dismiss();
				Message msg = Message.obtain();
				msg.what=1;
				msg.obj = arg0;
				handler.sendMessage(msg);
			}
		});

	}
	/**
	 * 基本的post 请求
	 */
	public  void PostHttp(RequestParams params,final int what) {
		x.http().post(params, new CommonCallback<String>() {
			@Override
			public void onCancelled(CancelledException arg0) {
			}
			
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Log.e("lll", "网络连接="+arg0.toString());
				Toast.makeText(x.app(), "网络连接失败", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinished() {
			}
			
			@Override
			public void onSuccess(String arg0) {
				parserData.sendResult(arg0,what);
			}
		});
		
	}

	/**
	 * 基本的post 请求
	 */
	public static void PostHttp(RequestParams params, final Handler handler,final int what) {
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
			}
			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Toast.makeText(x.app(), "网络连接失败", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(String arg0) {
				Message msg = Message.obtain();
				msg.what = what;
				msg.obj = arg0;
				handler.sendMessage(msg);
			}
		});

	}

	/**
	 * 上传文件
	 * 
	 * @param <T>
	 */

	public static void upLoadData(RequestParams params, final int what) {
		
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				Toast.makeText(x.app(), "网络连接失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinished() {
			}

			@Override
			public void onSuccess(String arg0) {
//				System.out.println("上传结果："+arg0);
//				Message msg = Message.obtain();
//				msg.what = what;
//				msg.obj = arg0;
//				handler.sendMessage(msg);
			}
		});
	}
	
	private ParserData parserData;
	public interface ParserData{
		public void sendResult(String result,int what);
	}
	
	public void setParserData(ParserData parserData){
		this.parserData=parserData;
	}
}
