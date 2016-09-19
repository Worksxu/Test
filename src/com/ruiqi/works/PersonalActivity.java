package com.ruiqi.works;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import com.ruiqi.adapter.CommonAdapter;
import com.ruiqi.adapter.ViewHolder;
import com.ruiqi.bean.Pensoral;
import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.HttpUtil;
import com.ruiqi.utils.MyActivityManager;
import com.ruiqi.utils.RequestUrl;
import com.ruiqi.utils.SPUtils;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.T;
import com.ruiqi.view.RoundImageView;
import com.ruiqi.view.popupwindow.SelectPicPopupWindow;

import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
//个人资料界面
public class PersonalActivity extends FragmentActivity implements OnClickListener{
	
	private ListView lv_top,lv_buttom;
	
	//适配器
	private CommonAdapter<Pensoral > topAdapter;
	private CommonAdapter<Pensoral> buttomAdapter;
	
	//数据源
	private List<Pensoral> mTopDatas;
	private List<Pensoral> mButtomDatas;
	
	private ImageView iv_personal_back;
	private TextView account_back;
	private RelativeLayout rl_personal_select; //修改手机号
	private TextView tv_personal_selectHend;
	private RoundImageView riv_personal_head;
	
	private int token;
	private String mobile,name;
	
	private ProgressDialog pd;
	
	private SelectPicPopupWindow mPopupWindow;
	
	private RelativeLayout rl_personal_pwd_select;
	
	private TextView tv_personal_name,tv_personal_phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		MyActivityManager.getInstance().pushOneActivity(this);
		initView();
		//数据的初始化
		initData();
		
		topAdapter = setDataToListView(topAdapter, mTopDatas);
		buttomAdapter = setDataToListView(buttomAdapter, mButtomDatas);
		lv_top.setAdapter(topAdapter);
		lv_buttom.setAdapter(buttomAdapter);
	}

	private void initData() {
		pd.setMessage("正在退出，请稍后......");
		
		mTopDatas = new ArrayList<Pensoral>();
		mTopDatas.add(new Pensoral("隶属", ""));
		mTopDatas.add(new Pensoral("岗位", ""));
		mTopDatas.add(new Pensoral("入职时间", ""));
		
		mButtomDatas = new ArrayList<Pensoral>();
		mButtomDatas.add(new Pensoral("配送总时长", ""));
		mButtomDatas.add(new Pensoral("配送订单数", ""));
		mButtomDatas.add(new Pensoral("门店派送排名", ""));
		mButtomDatas.add(new Pensoral("气站派送排名", ""));
		
		token = (Integer) SPUtils.get(PersonalActivity.this, "token", 0);
		mobile = (String) SPUtils.get(PersonalActivity.this, "mobile", "error");
		name = (String) SPUtils.get(PersonalActivity.this, "shipper_name", "error");
		
		tv_personal_name.setText(name);
		tv_personal_phone.setText(mobile);
	}

	private void initView() {
		pd = new ProgressDialog(PersonalActivity.this);
		lv_top = (ListView) findViewById(R.id.lv_top);
		lv_buttom = (ListView) findViewById(R.id.lv_buttom);
		
		iv_personal_back = (ImageView) findViewById(R.id.iv_personal_back);
		account_back = (TextView) findViewById(R.id.account_back);
		rl_personal_select = (RelativeLayout) findViewById(R.id.rl_personal_select);
		tv_personal_selectHend = (TextView) findViewById(R.id.tv_personal_selectHend);
		riv_personal_head =(RoundImageView) findViewById(R.id.riv_personal_head);
		
		rl_personal_pwd_select = (RelativeLayout) findViewById(R.id.rl_personal_pwd_select);
		rl_personal_pwd_select.setOnClickListener(this);
		iv_personal_back.setOnClickListener(this);
		account_back.setOnClickListener(this);
		rl_personal_select.setOnClickListener(this);
		tv_personal_selectHend.setOnClickListener(this);
		
		tv_personal_name = (TextView) findViewById(R.id.tv_personal_name);
		tv_personal_phone = (TextView) findViewById(R.id.tv_personal_phone);
	}
	
	/**
	 * 初始化填充数据的adapter
	 */
	private CommonAdapter<Pensoral> setDataToListView(CommonAdapter<Pensoral> adapter,List<Pensoral> mDatas){
		adapter = new CommonAdapter<Pensoral>(PersonalActivity.this, mDatas, R.layout.personal_content) {
			
			@Override
			public void convert(ViewHolder helper, Pensoral item,int position) {
				helper.setText(R.id.tv_personal_left, item.getTitle());
				helper.setText(R.id.tv_personal_right, item.getContent());
				
			}
		};
		return adapter;
	}
	/**
	 * 退出账号功能
	 */
	private void accountExit(){
		new AlertDialog.Builder(PersonalActivity.this)
		.setTitle("确定退出？")
		.setMessage("继续退出？")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				pd.show();
				//请求账号退出接口
				RequestParams params = new RequestParams(RequestUrl.PWD);
				params.addBodyParameter(SPutilsKey.TOKEN, token+"");
				params.addBodyParameter(SPutilsKey.MOBILLE, mobile);
				HttpUtil.PostHttp(params, handler, pd);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				
			}
		})
		.create().show();
	}
	/**
	 * 数据解析
	 * @param str
	 */
	private void parseData(String str) {
		System.out.println("str="+str);
		//json解析
		try {
			JSONObject obj = new JSONObject(str);
			int resultCode = obj.getInt("resultCode");
			if(resultCode==1){
				//继续解析
				//JSONObject object = obj.getJSONObject("resultInfo");
				T.showShort(PersonalActivity.this, "退出成功");
				//修改flag为false
				SPUtils.put(PersonalActivity.this, SPutilsKey.FLAG, false);
				SPUtils.put(PersonalActivity.this, SPutilsKey.LUNCHFALG, false);
				//跳转到登录界面
				Intent intent = new Intent(PersonalActivity.this, LoginActivity.class);
				startActivity(intent);
				this.finish();
			}else{
				T.showShort(PersonalActivity.this, "退出失败");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_personal_back:
			finish();
			break;
		case R.id.account_back://账号退出
			accountExit();
			break;
		case R.id.rl_personal_select://手机号修改
			break;
			
		case R.id.tv_personal_selectHend://更换头像
			mPopupWindow = new SelectPicPopupWindow(PersonalActivity.this, itemsOnClick);
			//设置显示位置
			mPopupWindow.showAtLocation(PersonalActivity.this.findViewById(R.id.main), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.rl_personal_pwd_select://修改密码
			String mobile = (String) SPUtils.get(PersonalActivity.this, SPutilsKey.MOBILLE, "");
			Intent intent = new Intent(PersonalActivity.this, FindActivity.class);
			intent.putExtra("mobile",mobile );
			intent.putExtra("flag", "personal");
			startActivity(intent);
			break;
			

		default:
			break;
		}
	}
	
	
	
	private OnClickListener itemsOnClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_take_photo://拍照
				takePhoto();
				break;
			case R.id.btn_pick_photo://从相册中选择
				getImageFromAlbum();
				break;

			default:
				break;
			}
		}
	};
	/**
	 * 从相册中获取图片
	 */
	private void getImageFromAlbum(){
		 Intent intent = new Intent(Intent.ACTION_PICK);  
	     intent.setType("image/*");//相片类型  
	     startActivityForResult(intent, 2); 
	}
	/**
	 * 拍照
	 */
	private void takePhoto(){
		//验证sd卡是否正确安装
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			//判断父目录是否存在
			//得到根目录
			File paths = new File(SPutilsKey.PATH);
			File path = new File(SPutilsKey.PATH_NAME);
			if(!paths.exists()){//不存在
				//创建
				paths.mkdirs();
			}
			//跳转到系统拍照的activity
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,null);
			//存储到path路径下
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(path));
			//跳转到拍照界面
			startActivityForResult(intent, 1);
			//拍照完成后上传
		}else{
			Toast.makeText(PersonalActivity.this, "请检查SD卡是否正确安装", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == this.RESULT_CANCELED){
		}else if(requestCode == 1&&resultCode ==this.RESULT_OK){
			mPopupWindow.dismiss();
			
			CurrencyUtils.setImageFromSdCard(SPutilsKey.PATH_NAME, riv_personal_head);
			
			//上传到服务器
			
		}else if(requestCode==2&&resultCode ==this.RESULT_OK){//从相册中返回来的
			mPopupWindow.dismiss();
			ContentResolver resolver = getContentResolver();
			Bitmap bitmap = null;
			Uri uri = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
				if(bitmap!=null){
					riv_personal_head.setImageBitmap(bitmap);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			String str = (String) msg.obj;
			parseData(str);
		}

	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		CurrencyUtils.recoveryHandler(handler);
	}
	
	
}





















