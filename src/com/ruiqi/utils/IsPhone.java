package com.ruiqi.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IsPhone {
	 /**
     * 判定是否为正确的电话号码
     */
	public static boolean isOrNotPhone(String str){
		Pattern p = Pattern.compile("^((13[0-9])|(147)|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$"); 
		Matcher m =p.matcher(str);
		return m.matches();
	}
	
	public static boolean panDuanShiYiWei(String str) {

		if(str.length()==11){
			return true;
		}
		return false;
	}
}
