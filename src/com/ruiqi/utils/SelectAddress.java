package com.ruiqi.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.ruiqi.addreselector.MyAdapter;
import com.ruiqi.addreselector.MyListItem;
import com.ruiqi.db.GpDao;
import com.ruiqi.works.R;

public class SelectAddress {
	
	
	private Context ctx;
	
	public SelectAddress(Context ctx){
		this.ctx=ctx;
	}
	
	public void showSelectAddress(){
		AlertDialog.Builder builder = new Builder(ctx);
		
		builder.setTitle("填写客户地址");
		View view=View.inflate(ctx,R.layout.select_address, null);
		initView(view);
		builder.setView(view);
		
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				addressConfirm.getAddress(province, city, district, cun, et_address_detail.getText().toString().trim(),shengcode,shicode,qucode,cuncode);
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		builder.create().show();
		
	}
	
	
	private GpDao gd;
	private Spinner spinner1 = null;
	private Spinner spinner2 = null;
	private Spinner spinner3 = null;
	private Spinner spinner4 = null;
	public String province = null;
	public String city = null;
	public  String district = null;
	public String cun=null;
	public String shengcode, shicode, qucode,cuncode;
	
	private EditText et_address_detail;
	 
	private void initView(View view) {
		spinner1 = (Spinner) view.findViewById(R.id.spinner1);
		spinner2 = (Spinner) view.findViewById(R.id.spinner2);
		spinner3 = (Spinner) view.findViewById(R.id.spinner3);
		spinner4 = (Spinner) view.findViewById(R.id.spinner4);
		et_address_detail = (EditText) view.findViewById(R.id.et_address_detail);
		
		spinner1.setPrompt("市");
		spinner2.setPrompt("县");
		spinner3.setPrompt("镇");
		spinner4.setPrompt("村");
		spinner1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		spinner2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		spinner3.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		spinner4.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				return false;
			}
		});
		initSpinner1();
	}

	public void initSpinner1() {
		
		gd=GpDao.getInstances(ctx);
		List<MyListItem> list = new ArrayList<MyListItem>();
		try {
			Cursor cursor=gd.readDb.query("citys", null, "pcode=10", null, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode = cursor.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code,pcode,name);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code,pcode,name);
			list.add(myListItem);
			
		} catch (Exception e) {
		}
		
		MyAdapter myAdapter = new MyAdapter(ctx, list);
		spinner1.setAdapter(myAdapter);
		spinner1.setOnItemSelectedListener(new SpinnerOnSelectedListener1());
	}

	public void initSpinner2(String pcode) {
		List<MyListItem> list = new ArrayList<MyListItem>();
		try {
			Cursor cursor=gd.readDb.query("citys", null, "pcode=?", new String[] {pcode}, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code,pcode1,name);
				myListItem.setPcode(code);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code,pcode1,name);
			list.add(myListItem);
			
		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ctx, list);
		spinner2.setAdapter(myAdapter);
		spinner2.setOnItemSelectedListener(new SpinnerOnSelectedListener2());
	}

	public void initSpinner3(String pcode) {
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {
			Cursor cursor=gd.readDb.query("citys", null, "pcode=?", new String[] {pcode}, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code,pcode1,name);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code,pcode1,name);
			list.add(myListItem);

		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ctx, list);
		spinner3.setAdapter(myAdapter);
		spinner3.setOnItemSelectedListener(new SpinnerOnSelectedListener3());
	}
	
	public void initSpinner4(String pcode) {
		List<MyListItem> list = new ArrayList<MyListItem>();

		try {
			Cursor cursor=gd.readDb.query("citys", null, "pcode=?", new String[] {pcode}, null, null, null, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				String code = cursor.getString(cursor.getColumnIndex("code"));
				String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				MyListItem myListItem = new MyListItem(code,pcode1,name);
				list.add(myListItem);
				cursor.moveToNext();
			}
			String code = cursor.getString(cursor.getColumnIndex("code"));
			String pcode1 = cursor.getString(cursor.getColumnIndex("pcode"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			MyListItem myListItem = new MyListItem(code,pcode1,name);
			list.add(myListItem);

		} catch (Exception e) {
		}

		MyAdapter myAdapter = new MyAdapter(ctx, list);
		spinner4.setAdapter(myAdapter);
		spinner4.setOnItemSelectedListener(new SpinnerOnSelectedListener4());
	}

	class SpinnerOnSelectedListener1 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> adapterView, View view,int position, long id) {
			province = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
			shengcode = ((MyListItem) adapterView.getItemAtPosition(position)).getCode();

			initSpinner2(shengcode);
		//	initSpinner3(pcode);
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}
	}

	class SpinnerOnSelectedListener2 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			city = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
			shicode = ((MyListItem) adapterView.getItemAtPosition(position)).getCode();

			initSpinner3(shicode);
		}
		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}
	}

	class SpinnerOnSelectedListener3 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			district = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
			qucode = ((MyListItem) adapterView.getItemAtPosition(position)).getCode();
			initSpinner4(qucode);
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}
	}
	
	class SpinnerOnSelectedListener4 implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> adapterView, View view,
				int position, long id) {
			cun = ((MyListItem) adapterView.getItemAtPosition(position)).getName();
			cuncode = ((MyListItem) adapterView.getItemAtPosition(position)).getCode();
		}

		public void onNothingSelected(AdapterView<?> adapterView) {
			// TODO Auto-generated method stub
		}
	}

	private AddressConfirm  addressConfirm;
	public interface AddressConfirm{
		
		public void getAddress(String sheng,String shi,String qu,String cun,String detail,String shengcode,String shicode,String qucode,String cuncode);
	}
	
	public void setOnAddressConfirm(AddressConfirm  addressConfirm){
		this.addressConfirm=addressConfirm;
	}

}
