package com.ruiqi.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 创建数据库的类
 * @author Administrator
 *
 */

public class DbHelper extends SQLiteOpenHelper{
	public static final String DB_NAME = "works.db";
	public static final int VERSION = 1;
	public DbHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}
	/**
	 * 自动创建表,如果不存在，才创建
	 */

 	 @Override
	public void onCreate(SQLiteDatabase db) {
		//订单详情表
		db.execSQL("create table if not exists orderinfo(_id integer primary key autoincrement,ordersn text,time text,delivery text,total text,type text,money text,status text,kid text,flag text,is_settlement text,userName text,mobile text,address text,shopName text,workersname text,workersmobile text,deposit text,depreciation text,raffinat text,ispayment text)");
		//钢瓶信息表
		//db.execSQL("create table if not exists gpinfo(_id integer primary key autoincrement,price text,weight text)");
		//配件信息表
		//db.execSQL("create table if not exists pjinfo(_id integer primary key autoincrement,products_price text,products_name text,products_no text)");
		//最后的订单提交信息表
		//db.execSQL("create table if not exists ordercommit(_id integer primary key autoincrement,shop_id text,shiper_id text,shiper_name text,shiper_mobile text,token integer,mobile text,username text,address text,status text,urgent text,goodtime text,isold text,ismore text,data text,money text,json text,nojson text,yajin text,zhejiu text,canye text)");
		//确定收款信息表
		//db.execSQL("create table if not exists confirminfo(_id integer primary key autoincrement,ordersn text,kid text,money text,data text,nodata text,shop_id text,shiper_id text,shiper_name text,shiper_mobile text,token integer,yajin text,zhejiu text,canye text)");
		//赊欠账户的未收款订单表
		//db.execSQL("create table if not exists sheqian(_id integer primary key autoincrement,ordersn text,mobile text,pay_money integer,ctime text)");
		//安全报告列表表,将安全报告的标题和对应的id存入
		//db.execSQL("create table if not exists self(_id integer primary key autoincrement,title text,id text)");
		//退瓶订单信息库
		db.execSQL("create table if not exists back_bottle(_id integer primary key autoincrement,depositsn text,money text,doormoney text,productmoney text,shouldmoney text,username text,time text,mobile text,id text,address text,status text,kid text,status_name text)");
		 //领取重瓶的芯片库
		db.execSQL("create table if not exists xin_pian(_id integer primary key autoincrement,xinpian text,shipper_id text)");
		/**
		 * 地址联动表
		 * code
		 * pcode
		 * text 
		 */
		db.execSQL("create table if not exists citys(_id integer primary key autoincrement,code text,pcode text,name text)");
		/**
		 * 钢瓶库存表
		 * xinpin 芯片号
		 * number 钢印号
		 * type 钢瓶类型
		 * is_open 空重瓶
		 * type_name 类型名称
		 */
		db.execSQL("create table if not exists gangpingxinxi(_id integer primary key autoincrement,xinpin text,number text,type text,is_open text,type_name text)");
		/**
		 * 客户余瓶
		 * type 钢瓶类型
		 * number 钢瓶数量
		 */
		db.execSQL("create table if not exists kehugangpingxinxi(_id integer primary key autoincrement,number text,type text)");
		/**
		 * 扫描重瓶
		 * type 钢瓶类型
		 * number 钢瓶数量
		 */
		db.execSQL("create table if not exists saomiaogangpingxinxi(_id integer primary key autoincrement,number text,type text)");
 	 
 	 }
	/**
	 * 版本更新的时候自动执行
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
