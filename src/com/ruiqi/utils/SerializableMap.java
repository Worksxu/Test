package com.ruiqi.utils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 封装map 实现序列化
 * 
 * @author Administrator
 * 
 */

@SuppressWarnings("serial")
public class SerializableMap implements Serializable {
	private HashMap<String, String> map;

	public HashMap<String, String> getMap() {
		return map;
	}

	public void setMap(HashMap<String, String> map) {
		this.map = map;
	}

}
