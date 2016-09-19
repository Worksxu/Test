package com.ruiqi.works;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruiqi.utils.CurrencyUtils;
import com.ruiqi.utils.MyDialog;
import com.ruiqi.utils.SPutilsKey;
import com.ruiqi.utils.SerializableMap;

/**
 * 拍照留底界面
 * @author Administrator
 *
 */
public class TakePhotoActivity extends BaseActivity implements OnItemClickListener,OnItemLongClickListener{
	
	private GridView gv_image;
	
	private ArrayList<HashMap<String, String>> mList;
	
	private Gadapter adapter;
	
	private TextView tv_next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_take_photo);
		setTitle("拍照留底");
		init();
		mList = new ArrayList<HashMap<String, String>>();
		
		adapter = new Gadapter();
		
		gv_image.setAdapter(adapter);
		gv_image.setOnItemLongClickListener(this);
	}
	
	private void init() {
		gv_image = (GridView) findViewById(R.id.gv_image);
		gv_image.setOnItemClickListener(this);
		
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_next.setOnClickListener(this);
	}

	
	
	/**
	 * 拍照
	 */
	private void takePhoto(){
		//验证sd卡是否正确安装
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			//判断父目录是否存在
			//得到根目录
//			File paths = new File(SPutilsKey.PATH);
//			String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";   
//			File path = new File(SPutilsKey.PATH+"take"+name);
//			if(!paths.exists()){//不存在
//				//创建
//				paths.mkdirs();
//			}
			//跳转到系统拍照的activity
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,null);
			//存储到path路径下
			//intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(path));
			//跳转到拍照界面
			startActivityForResult(intent, 3);
			//拍照完成后上传
		}else{
			Toast.makeText(TakePhotoActivity.this, "请检查SD卡是否正确安装", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 从相册中获取图片
	 */
	private void getImageFromAlbum(){
		 Intent intent = new Intent(Intent.ACTION_PICK);  
	     intent.setType("image/*");//相片类型  
	     startActivityForResult(intent, 4); 
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == this.RESULT_CANCELED){
		}else if(requestCode == 3&&resultCode ==this.RESULT_OK){
			
			if(data!=null){
				new DateFormat();  
	            String name = DateFormat.format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";   
	            Bundle bundle = data.getExtras();  
	            Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式  
	          
	            FileOutputStream b = null;           
	            File file = new File(SPutilsKey.PATH);  
	            file.mkdirs();// 创建文件夹  
	            String fileName =SPutilsKey.PATH+name;  
	  
	            try {  
	                b = new FileOutputStream(fileName);  
	                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件  
	            } catch (FileNotFoundException e) {  
	                e.printStackTrace();  
	            } finally {  
	                try {  
	                    b.flush();  
	                    b.close();  
	                } catch (IOException e) {  
	                    e.printStackTrace();  
	                }  
	            }  
	           // CurrencyUtils.setImageFromSdCard(SPutilsKey.PATH+"take", iv_photo);
	            HashMap<String, String> hm=new HashMap<String, String>();
	            hm.put("path", fileName);
	            hm.put("name", name);
	            mList.add(hm);
	            try {
	                MediaStore.Images.Media.insertImage(this.getContentResolver(),fileName, name, null);
	            } catch (FileNotFoundException e) {
	                e.printStackTrace();
	            }
	            // 最后通知图库更新
	           this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
			}
			adapter.notifyDataSetChanged();
			//上传到服务器
			
		}else if(requestCode==4&&resultCode ==this.RESULT_OK){//从相册中返回来的
			ContentResolver resolver = getContentResolver();
			Bitmap bitmap = null;
			Uri uri = data.getData();
			try {
				bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
			    String[] proj = {MediaStore.Images.Media.DATA};
			    Cursor cursor = managedQuery(uri, proj, null, null, null);
			    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			    cursor.moveToFirst();
			    String path = cursor.getString(column_index);
			    HashMap<String, String> hm=new HashMap<String, String>();
			    System.out.println("图片路径："+path);
				hm.put("path", path);
				String name="";
				int start=path.lastIndexOf("/");  
		        if(start!=-1 ){  
		            name= path.substring(start+1);    
		        }  
		        System.out.println("文件名称："+name);
				hm.put("name", name);
				mList.add(hm);
			    //mList.add(path);
			    adapter.notifyDataSetChanged();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_next:
			System.out.println("集合的内容个数："+mList.size());
			ArrayList<SerializableMap> arrayList=new ArrayList<SerializableMap>();
			for (int i = 0; i < mList.size(); i++) {
				SerializableMap sMap = new SerializableMap();
				HashMap<String, String> map = new HashMap<String, String>();
				map=mList.get(i);
				sMap.setMap(map);
				arrayList.add(sMap);
			}
			Intent intent = new Intent(TakePhotoActivity.this, OrderConfirmActivity.class);
			Bundle bundle=new Bundle();
			bundle.putSerializable("arrayList", arrayList);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public Handler[] initHandler() {
		return null;
	}
	
	class Gadapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mList.size()+2;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView==null){
				convertView = LayoutInflater.from(TakePhotoActivity.this).inflate(R.layout.gv_item, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_photo);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if(position==0){
				viewHolder.iv_photo.setImageResource(R.drawable.take);
			}else if(position==1){
				viewHolder.iv_photo.setImageResource(R.drawable.photo);
			}else{
				CurrencyUtils.setImageFromSdCard(mList.get(position-2).get("path"), viewHolder.iv_photo);
			}
			return convertView;
		}
		
		class ViewHolder{
			ImageView iv_photo;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			if(mList.size()<4){
				takePhoto();
			}else{
				new MyDialog().showMaxNumber(this, "最多上传三张照片!");
			}
			break;
		case 1:
			if(mList.size()<4){
				getImageFromAlbum();
			}else{
				new MyDialog().showMaxNumber(this, "最多上传三张照片!");
			}
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if(position!=0&& position!=1){
			deletePicture(position);
		}
		return true;
	}
	
	public void deletePicture(final int position){
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		
		TextView tv = new TextView(this);
		tv.setText("是否删除此照片？");
		tv.setTextSize(20);
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, 20, 0, 0);
		builder.setView(tv);
		builder.setPositiveButton("是", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mList.remove(position-2);
				adapter.notifyDataSetChanged();
			}
		});
		builder.setNegativeButton("否", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}

}
