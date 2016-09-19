package com.ruiqi.chai;

import java.util.ArrayList;

public class ChaiBottles {
	
	private int resultCode;
	private ArrayList<BottlesType> resultInfo;
	
	
	
	public int getResultCode() {
		return resultCode;
	}



	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}



	public ArrayList<BottlesType> getResultInfo() {
		return resultInfo;
	}



	public void setResultInfo(ArrayList<BottlesType> resultInfo) {
		this.resultInfo = resultInfo;
	}



	public class BottlesType{
		private String id;
		private String name;
		private String norm_id;
		private String price;
		private String type;
		private String typename;
		private String yj_price;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNorm_id() {
			return norm_id;
		}
		public void setNorm_id(String norm_id) {
			this.norm_id = norm_id;
		}
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getTypename() {
			return typename;
		}
		public void setTypename(String typename) {
			this.typename = typename;
		}
		public String getYj_price() {
			return yj_price;
		}
		public void setYj_price(String yj_price) {
			this.yj_price = yj_price;
		}
		
		
	}

}
