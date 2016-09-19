package com.ruiqi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 对重瓶领取芯片信息数据库进行操作的类
 * @author Administrator
 *
 */
public class GpDao {
	/**
	  * 单例的三步
	  * 1)private static 类名  变量；
	  * 2)private 构造方法（参数列表）{...}
	  * 3)public static 类名 getInstances(参数列表){ return  ?;}
	  */
		DbHelper dbHelper;
		public SQLiteDatabase readDb,writeDb;
		private static GpDao orderDao;
		private GpDao(Context context){
			//1)创建dbHelper对象
			dbHelper = new DbHelper(context);
			//2)得到readDb,writeDb的对象 
			readDb =dbHelper.getReadableDatabase();
			writeDb=dbHelper.getWritableDatabase();
		}
		public static GpDao getInstances(Context context){
			if(orderDao ==null)
				orderDao = new GpDao(context);
			return orderDao;
		}
		/**
		 * 保存
		 */
		public void saveOrder(String xinpian,String shipper_id){
			//1)封装参数
			ContentValues values = new ContentValues();
			values.put("xinpian", xinpian); //参数1：表中的列名，该列对应的值
			values.put("shipper_id", shipper_id);
			//2)保存
			writeDb.insert("xin_pian", null, values);
		}
		/**
		 * 查询所有
		 * select * from userinfo;
		 */
		public Cursor getUserList(){
			Cursor c =readDb.query("xin_pian", null,null,null,null,null,null);
			return c;
		}
		/**
		 * 根据芯片号查询,第二个参数为查询你想要的列,根据时间排序
		 */
		public Cursor getFromXinpianAndShip(String xinpian,String shipid){
			Cursor c = readDb.query("xin_pian", null, "xinpian=? And shipper_id=?", new String [] {xinpian,shipid}, null, null, null, null);
			return c;
		}
		
		/**
		 * 更新用户的status(订单状态)
		 */
		public int updateStatus(String status,String ordersn){
			ContentValues values = new ContentValues();
			values.put("status", status);
			int count = writeDb.update("xin_pian", values, "ordersn=?", new String[]{ordersn});
			return count;
		}
		
		/**
		 * 根据芯片号删除
		 */
		public int deleteFromXinpianAndShip(String xinpian,String ship_id){
			return writeDb.delete("xin_pian", "xinpian=? And shipper_id=?", new String[]{xinpian,ship_id});
		}
		/**
		 * 删除所有
		 */
		public int deleteAll(){
			return writeDb.delete("xin_pian", null, null);
		}
		/**
		 * 插入地址
		 * @param code
		 * @param pcode
		 * @param name
		 */
		public void insertAddress(String code,String pcode,String name){
			ContentValues values = new ContentValues();
			values.put("code", code); //参数1：表中的列名，该列对应的值
			values.put("pcode", pcode);
			values.put("name", name);
			writeDb.insert("citys", null, values);
		}
		/**
		 * 钢瓶信息
		 */
		public void insertGangPingXinXi(String xinpian,String number,String type,String is_open,String type_name){
			ContentValues values = new ContentValues();
			values.put("xinpin", xinpian); //参数1：表中的列名，该列对应的值
			values.put("number", number);
			values.put("type", type);
			values.put("is_open", is_open);
			values.put("type_name", type_name);
			writeDb.insert("gangpingxinxi", null, values);
		}
		
}
