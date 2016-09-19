package com.ruiqi.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ZheKouInfo extends YouHuiZheKouInfo implements Serializable {

	private ZheKouContent data;

	public ZheKouContent getData() {
		return data;
	}

	public void setData(ZheKouContent data) {
		this.data = data;
	}

}
