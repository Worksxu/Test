package com.ruiqi.chai;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 产生编号
 * @author Administrator
 *
 */
public class ChaiGetBianHao {
	
	
	private static ChaiGetBianHao chaiGetBianHao;
	
	public static ChaiGetBianHao getInstance(){
		if(chaiGetBianHao==null){
			chaiGetBianHao=new ChaiGetBianHao();
		}
	return 	chaiGetBianHao;
	}

	/**
	 * 
	 * @param title 
	 * @param kid
	 * @return
	 */
	public String  getBianHaoString(String title,String kid){
		StringBuffer sb=new StringBuffer();
		sb.append(title.trim());
		sb.append(getNowTime());
		sb.append(kid.trim());
		sb.append(((int)Math.random()*100)+"");
		while(sb.length()<15){
			sb.append("0");
		}
		return sb.toString();
		
	}
	/**
	 * 获得当前时间
	 * @return
	 */
	public  String getNowTime() {
		Date nowDate = new Date();
		DateFormat df = new SimpleDateFormat("yyMMdd");
		String now = df.format(nowDate);
		return now;
	}
}
