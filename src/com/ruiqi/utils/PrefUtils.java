package com.ruiqi.utils;

import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
	public static String PARTS_NUMBER = "bottlexinxi";
	
	private static PrefUtils single = null;
	

	private PrefUtils() {
	}

	public static PrefUtils getInstance() {
		// 旨在避免实例化后其它线程在进入这个方法后进入同步代码块，提高效率
		if (single != null) {
			return single;
		}
		ReentrantLock lock = new ReentrantLock(false);
		try {
			lock.lock();
			if (single != null) {
				return single;
			}
			single = new PrefUtils();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			lock.unlock();
		}
		return single;
	}

	// 保存Boolean值

	// 保存字符串
	public String getString(Context ctx, String key, String defaultValue) {
		SharedPreferences sp = ctx.getSharedPreferences(PARTS_NUMBER,Context.MODE_PRIVATE);
		return sp.getString(key, defaultValue);
	}

	public void setString(Context ctx, String key, String value) {
		SharedPreferences sp = ctx.getSharedPreferences(PARTS_NUMBER,Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}

	
	
}
