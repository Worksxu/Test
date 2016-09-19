package com.ruiqi.bean;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class GangPingList implements Serializable {
	
	private int resultCode;
	private ArrayList<Info> resultInfo;
	
	
	public int getResultCode() {
		return resultCode;
	}


	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}


	public ArrayList<Info> getResultInfo() {
		return resultInfo;
	}


	public void setResultInfo(ArrayList<Info> resultInfo) {
		this.resultInfo = resultInfo;
	}


	public  class Info{
		private String id;
		private String number;
		private String xinpian;
		private String type;
		private String is_open;
		private String type_name;
		
		
		public String getType_name() {
			return type_name;
		}
		public void setType_name(String type_name) {
			this.type_name = type_name;
		}
		public String getIs_open() {
			return is_open;
		}
		public void setIs_open(String is_open) {
			this.is_open = is_open;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getNumber() {
			return number;
		}
		public void setNumber(String number) {
			this.number = number;
		}
		public String getXinpian() {
			return xinpian;
		}
		public void setXinpian(String xinpian) {
			this.xinpian = xinpian;
		}
		
	}
	
	
	

}
