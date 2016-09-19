package com.ruiqi.bean;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class YouHuiInfo extends YouHuiZheKouInfo implements Serializable {

	private ArrayList<String> data;

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}

}
