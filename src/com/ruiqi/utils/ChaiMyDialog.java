package com.ruiqi.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChaiMyDialog {
	
	
	private static ChaiMyDialog chaiMyDialog;
	
	private ChaiMyDialog(){}
	
	public static ChaiMyDialog getInstance(){
		
		if(chaiMyDialog==null){
			chaiMyDialog=new ChaiMyDialog();
		}
		return chaiMyDialog;
	}
	/**
	 * 
	 * @param ctx
	 * @param str提示信息
	 */
	public void show(final Context ctx,String str){
			
		final EditText et = new EditText(ctx);
		et.setHint(str);
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
						callBack.chaiStringCallBack(str);
			}
		})
		.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.create().show();
	}
	
	/**
	 * 简单提示
	 * @param ctx
	 * @param str
	 */
	
	public void showHint(final Context ctx,String str){
		final TextView et = new TextView(ctx);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		et.setText(str);
		et.setGravity(Gravity.CENTER);
		et.setPadding(5, 5, 5, 5);
		et.setTextSize(18f);
		new AlertDialog.Builder(ctx)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		})
		.create().show();
	}
	/**
	 * @param ctx
	 * @param str提示信息
	 * @param length输入长度
	 */
	public  void show(final Context ctx,String str,int length){
		
		final EditText et = new EditText(ctx);
		et.setHint(str);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
				if(str.length()==6){
					callBack.chaiStringCallBack(str);
				}else{
					Toast.makeText(ctx, "请输入正确的内容", Toast.LENGTH_SHORT).show();
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
	/**
	 * 修改押金
	 * @param ctx
	 * @param str提示内容
	 * @param money押金
	 */
	public  void showYaJin(final Context ctx,String str,final int where){
		
		final EditText et = new EditText(ctx);
		et.setTextSize(20);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		et.setGravity(Gravity.CENTER);
		et.setPadding(0, 20, 0, 0);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		.setTitle(str)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String str = et.getText().toString().trim();
				if(!TextUtils.isEmpty(str)&&Integer.parseInt(str)>=0){
					callBack.chaiIntCallBack(Integer.parseInt(str),where);
				}else{
					callBack.chaiIntCallBack(0,where);
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
	/**
	 * 输入押金条编号
	 * @param ctx
	 * @param str
	 * @param where
	 */
	public  void showYaJinTiao(final Context ctx,String str){
		
		final EditText et = new EditText(ctx);
		et.setTextSize(20);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		et.setGravity(Gravity.CENTER);
		et.setPadding(0, 20, 0, 0);
		//et.setKeyListener(new DigitsKeyListener(false, true));
		et.setKeyListener(new NumberKeyListener() {
			
			@Override
			public int getInputType() {
				return android.text.InputType.TYPE_CLASS_TEXT;
			}
			
			@Override
			protected char[] getAcceptedChars() {
				return new char[] { '0','1', '2', '3', '4', '5', '6', '7', '8','9','a','b','c','d','e','f','g','h','i','g','k','l','j','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','G','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
			}
		});
		et.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
		new AlertDialog.Builder(ctx)
		.setTitle(str)
		.setView(et)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String strs = et.getText().toString().trim();
				if(!TextUtils.isEmpty(strs)){
					callBackYaJinTiao.chaiYaJinTiaoStringCallBack(strs);
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
	/**
	 * 
	 * @param ctx
	 * @param str 提示语
	 * @param view 显示布局
	 */
	
	public  void showListViewSelect(final Context ctx,String str,View view){
		
		new AlertDialog.Builder(ctx)
		.setTitle(str)
		.setView(view)
		.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cs.chaiSelectCallBack(true);
			}
		}).create().show();
	}
	
	
	/**
	 * 简单提示确认或取消
	 * @param ctx
	 * @param str
	 */
	
	public void showHintConfirm(final Context ctx,String str,String confrim,String cancel){
		final TextView et = new TextView(ctx);
		et.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		et.setText(str);
		et.setGravity(Gravity.CENTER);
		et.setPadding(5, 5, 5, 5);
		et.setTextSize(18f);
		new AlertDialog.Builder(ctx)
		.setView(et)
		.setPositiveButton(confrim, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cs.chaiSelectCallBack(true);
			}
		}).setNegativeButton(cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				cs.chaiSelectCallBack(false);
			}
		})
		.create().show();
	}
	
	private  ChaiCallBack callBack;
	public interface ChaiCallBack{
		/**
		 * 回调字符串
		 * @param str
		 */
		public void chaiStringCallBack(String str);
		/**
		 * 回调int
		 * @param in
		 */
		public void chaiIntCallBack(int in,int where);
	}
	public void setCallBack(ChaiCallBack c){
		this.callBack = c;
	}
	
	/**
	 * View确认回调
	 * @author Administrator
	 *
	 */
	public interface ChaiSelectCallBack{
		public void chaiSelectCallBack(boolean isSelect);
	}
	private ChaiSelectCallBack cs;
	public void setSelectCallBack(ChaiSelectCallBack cs){
		this.cs = cs;
	}
	
	
	private  ChaiYaJinTiaoCallBack callBackYaJinTiao;
	public interface ChaiYaJinTiaoCallBack{
		/**
		 * 回调字符串
		 * @param str
		 */
		public void chaiYaJinTiaoStringCallBack(String str);
		
	}
	public void setYaJinTiaoCallBack(ChaiYaJinTiaoCallBack c){
		this.callBackYaJinTiao = c;
	}
	
	
	
}
