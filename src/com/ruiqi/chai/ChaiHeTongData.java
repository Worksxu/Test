package com.ruiqi.chai;

import java.util.ArrayList;

public class ChaiHeTongData {
	
	private int resultCode;
	private ArrayList<QianKuanDetail> resultInfo;
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	
	
	
	public ArrayList<QianKuanDetail> getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(ArrayList<QianKuanDetail> resultInfo) {
		this.resultInfo = resultInfo;
	}



	public class QianKuanDetail{
		
		private String money;
		private String type;
		private String arrears_type;
		private String contractno;
		private String order_sn;
		private String status;
		private String time;
		private String time_list;
		private String typename;
		
		
		public String getArrears_type() {
			return arrears_type;
		}
		public void setArrears_type(String arrears_type) {
			this.arrears_type = arrears_type;
		}
		public String getContractno() {
			return contractno;
		}
		public void setContractno(String contractno) {
			this.contractno = contractno;
		}
		public String getOrder_sn() {
			return order_sn;
		}
		public void setOrder_sn(String order_sn) {
			this.order_sn = order_sn;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getTime_list() {
			return time_list;
		}
		public void setTime_list(String time_list) {
			this.time_list = time_list;
		}
		public String getTypename() {
			return typename;
		}
		public void setTypename(String typename) {
			this.typename = typename;
		}
		public String getMoney() {
			return money;
		}
		public void setMoney(String money) {
			this.money = money;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
	
		
		
	}

}
