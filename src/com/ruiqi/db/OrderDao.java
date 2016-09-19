package com.ruiqi.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 对数据库进行操作的类
 * @author Administrator
 *
 */
public class OrderDao {
	/**
	  * 单例的三步
	  * 1)private static 类名  变量；
	  * 2)private 构造方法（参数列表）{...}
	  * 3)public static 类名 getInstances(参数列表){ return  ?;}
	  */
		DbHelper dbHelper;
		SQLiteDatabase readDb,writeDb;
		private static OrderDao orderDao;
		private OrderDao(Context context){
			//1)创建dbHelper对象
			dbHelper = new DbHelper(context);
			//2)得到readDb,writeDb的对象 
			readDb =dbHelper.getReadableDatabase();
			writeDb=dbHelper.getWritableDatabase();
		}
		public static OrderDao getInstances(Context context){
			if(orderDao ==null)
				orderDao = new OrderDao(context);
			return orderDao;
		}
		/**
		 * 保存
		 */
		public void saveOrder(String orderSn,String time,String delivery,String total,String pay_money,
				String status,String kid,String is_settlement,String type,String userName,String mobile,
				String address,String shopName,String workersname,String workersmobile,String deposit,
				String depreciation,String raffinat,String ispayment){
			//1)封装参数
			ContentValues values = new ContentValues();
			values.put("ordersn", orderSn); //参数1：表中的列名，该列对应的值
			values.put("time", time);
			values.put("delivery", delivery);
			values.put("total", total);
			values.put("money", pay_money);
			values.put("status", status);
			values.put("kid", kid);
			values.put("is_settlement", is_settlement);
			values.put("type", type);
			values.put("userName", userName);
			values.put("mobile", mobile);
			values.put("address", address);
			values.put("shopName", shopName);
			values.put("workersname", workersname);
			values.put("workersmobile", workersmobile);
			values.put("deposit", deposit);
			values.put("depreciation", depreciation);
			values.put("raffinat", raffinat);
			values.put("ispayment", ispayment);
			
			//2)保存
			writeDb.insert("orderinfo", null, values);
		}
		/**
		 * 查询所有
		 * select * from userinfo;
		 */
		public Cursor getUserList(){
			Cursor c =readDb.query("orderinfo", null,null,null,null,null,"time desc,_id desc");
			return c;
		}
		/**
		 * 根据flag查询,第二个参数为查询你想要的列,根据时间排序
		 */
		public Cursor getFromFlag(String str){
			Cursor c = readDb.query("orderinfo", null, "flag=?", new String [] {str}, null, null, "time desc,_id desc", null);
			return c;
		}
		/**
		 * 根据status状态查找
		 */
		
		public Cursor getFromStatus(String str){
			Cursor c = readDb.query("orderinfo", null, "status=?", new String [] {str}, null, null, "delivery desc", null);
			return c;
		}
		/**
		 * 查询status不为4的订单信息
		 */
		
		public Cursor getFromStatusNot(String str){
			Cursor c = readDb.query("orderinfo", null, "status<>?", new String [] {str}, null, null, "time desc,_id desc", null);
			return c;
		}
		/**
		 * 根据订单号，修改客户的姓名，电话，地址，和订单的状态
		 */
		
		public int updateFromOrdersn(String name,String mobile,String address,String status,String ordersn){
			ContentValues values = new ContentValues();
			values.put("status", status);
			values.put("userName", name);
			values.put("mobile", mobile);
			values.put("address", address);
			
			int count = writeDb.update("orderinfo", values, "ordersn=?", new String[]{ordersn});
			return count;
		}
		/**
		 * 根据ordersn查询
		 */
		public Cursor getFromOrderSn(String ordersn){
			Cursor c = readDb.query("orderinfo", null, "ordersn=?", new String [] {ordersn}, null, null, null, null);
			return c;
		}
		/**
		 * 根据时间区间查询
		 */
		public Cursor getFromTime(String startTime,String endTime){
			Cursor c = readDb.query("orderinfo", null, "time >=? and time <= ?", new String [] {startTime,endTime}, null, null, null);
			return c;
		}
		
		/**
		 * 更新用户的flag(根据订单号)
		 */
		public int updateUser(String flag,String ordersn){
			//writeDb.execSQL("update userinfo set username="+username+",address='天津' where _id=1 and age=21")
			/**
			 * 参数一:table-->表名
			 * 参数二:values-->即将要更新的内容 （等同于set <username='张三'>）
			 * 参数三:whereClause-->筛选的语句 (_id=? and age=?)  "?"称为占位符号
			 * 参数四:whereArgs--->new String[]{"1","20"}
			 */
			//要更新的内容
			ContentValues  values = new ContentValues();
			values.put("flag", flag);
			//values.put("address", address);
			int count =writeDb.update("orderinfo", values, "ordersn=?", new String[]{ordersn});
			/*筛选的语句
			String whereClause="_id=?";
			String [] whereArgs=new String[]{id};
			int count =writeDb.update("userinfo", values, whereClause, whereArgs);*/
			return count;
			
		}
	
		/**
		 * 更新用户的status(订单状态)
		 */
		public int updateStatus(String status,String ordersn){
			ContentValues values = new ContentValues();
			values.put("status", status);
			int count = writeDb.update("orderinfo", values, "ordersn=?", new String[]{ordersn});
			return count;
		}
		/**
		 * 更新用户的实付款
		 */
		public int updatePayMoney(String money,String ordersn){
			ContentValues values = new ContentValues();
			values.put("money", money);
			int count = writeDb.update("orderinfo", values, "ordersn=?", new String []{ordersn});
			return count;
		}
		/**
		 * 更新ispayment
		 */
		public int upDataIsPayMent(String isPayMent,String ordersn){
			ContentValues values = new ContentValues();
			values.put("ispayment", isPayMent);
			int count = writeDb.update("orderinfo", values, "ordersn=?", new String []{ordersn});
			return count;
		}
		/**
		 * 更新订单的完成时间
		 */
		public int updateTime(String time,String ordersn){
			ContentValues values = new ContentValues();
			values.put("delivery", time);
			int count = writeDb.update("orderinfo", values, "ordersn=?", new String []{ordersn});
			return count;
		}
		
		
		/**
		 * 删除所有
		 */
		public int deleteAll(){
			return writeDb.delete("orderinfo", null, null);
		}
		
}
