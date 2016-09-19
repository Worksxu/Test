package com.ruiqi.chai;


public class ChaiCreateOrderData {

	
	private int resultCode;
	private ChaiCreateOrderDataInfo  resultInfo;
	public int getResultCode() {
		return resultCode;
	}
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}
	public ChaiCreateOrderDataInfo getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(ChaiCreateOrderDataInfo resultInfo) {
		this.resultInfo = resultInfo;
	}
	
	public class ChaiCreateOrderDataInfo{
		private String ordersn;
		private String kid;
		public String getOrdersn() {
			return ordersn;
		}
		public void setOrdersn(String ordersn) {
			this.ordersn = ordersn;
		}
		public String getKid() {
			return kid;
		}
		public void setKid(String kid) {
			this.kid = kid;
		}
		
	}
	
}
