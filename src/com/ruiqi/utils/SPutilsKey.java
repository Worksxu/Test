package com.ruiqi.utils;

import java.util.ArrayList;
import java.util.List;

import com.ruiqi.bean.PeiJian;
import com.ruiqi.bean.PeiJianTypeMoney;
import com.ruiqi.bean.Type;

import android.os.Environment;

/**
 * sputils 对应的key
 * @author Administrator
 *
 */
public class SPutilsKey {
	public static final String TOKEN = "token"; //token
	public static final String MOBILLE = "mobile"; //手机号码
	public static final String SHOP_ID = "shop_id";
	public static final String SHIP_ID = "ship_id";
	
	public static final String FLAG = "flag";//判定是否自动登录
	public static final String PWD = "pwd";//判定是否自动登录
	
	public static final String LUNCHFALG="lunchfalg";//是否登录
	
	public static int GANGPINGZONGSHU=0;//订单钢瓶总数
	
	public static List<Type> list= new ArrayList<Type>(); 
	
	public static int kehuyou;//客户是否有瓶  1有 2没有
	
	public static String  tuipingordersn;//退瓶订单号
	public static String tuipingkid;//退瓶客户的kid
	
	public static final String KEY = "activity"; //传值的key
	//判断新老用户的标记
	public static int type = 0; //2老客户  1新客户
	
	public static int neworold=0;//老客户2 新客户1
	
	//sd相关的操作
	public static final String PATH_NAME = Environment.getExternalStorageDirectory().getPath()+"/my_img/Head";
	public static final String PATH = Environment.getExternalStorageDirectory().getPath()+"/my_img/";
}
