package com.ruiqi.utils;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MyDialog {
	public   EditText et;
	public   void show(final Context ctx,String str,final int a){
			
			et = new EditText(ctx);
			et.setText(str);
			et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
			
		new AlertDialog.Builder(ctx)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
				if(str==null||"".equals(str)){
					T.showShort(ctx, "请输入需要修改的内容");
				}else{
					if(a==1){//先判断是否为电话号码
						if(!IsPhone.isOrNotPhone(str)){
							T.showShort(ctx, "请输入正确的电话号码");
						}else{
							callBack.callBack(str);
						}
					}else{
						callBack.callBack(str);
					}
				}
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.create().show();
	}
	
	public interface StrCallBack{
		public void callBack(String str);
	}
	public  StrCallBack callBack;
	
	public void setCallBack(StrCallBack c){
		this.callBack = c;
	}
	public void showRadio(final Context ctx,final String str){
		System.out.println("str="+str);
		final RadioGroup rg = new RadioGroup(ctx);
		final RadioButton rb_1 = new RadioButton(ctx);
		rb_1.setText("问题订单");
		final RadioButton rb_2 = new RadioButton(ctx);
		rb_2.setText("正常订单");
		
		rg.addView(rb_1);
		rg.addView(rb_2);
		rg.setOrientation(LinearLayout.VERTICAL);
		
		if("2".equals(str)){
			rb_2.setChecked(true);
		}else{
			rb_1.setChecked(true);
		}
		new AlertDialog.Builder(ctx)
		.setView(rg)
		.setPositiveButton("确认", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(rb_1.isChecked()){
					rb_2.setChecked(false);
					callBack.callBack(rb_1.getText().toString());
				}else if(rb_2.isChecked()){
					rb_1.setChecked(false);
					callBack.callBack(rb_2.getText().  toString());
				}else{ 
					callBack.callBack(str);
				}
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.create().show();
		
	}
	
	public void showMaxNumber(Context ctx,String title){
		AlertDialog.Builder builder=new Builder(ctx);
		
		TextView tv = new TextView(ctx);
		tv.setText(title);
		tv.setTextSize(20);
		tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER);
		tv.setPadding(0, 20, 0, 0);
		builder.setView(tv);
		builder.setPositiveButton("确定", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		builder.show();
	}
	
}
