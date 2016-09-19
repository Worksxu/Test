package com.ruiqi.bean;
/**
 * 老用户个人信息
 * @author Administrator
 *
 */
public class OldUserInfo {
private String oldName;
	
	private String oldPhone;
	
	private String OldAddress;
	
	private int ivPic;

	public int getIvPic() {
		return ivPic;
	}

	public void setIvPic(int ivPic) {
		this.ivPic = ivPic;
	}

	public OldUserInfo(String oldName, String oldPhone, String oldAddress,
			int ivPic) {
		super();
		this.oldName = oldName;
		this.oldPhone = oldPhone;
		OldAddress = oldAddress;
		this.ivPic = ivPic;
	}

	public OldUserInfo() {
		super();
	}

	public OldUserInfo(String oldName, String oldPhone, String oldAddress) {
		super();
		this.oldName = oldName;
		this.oldPhone = oldPhone;
		OldAddress = oldAddress;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getOldPhone() {
		return oldPhone;
	}

	public void setOldPhone(String oldPhone) {
		this.oldPhone = oldPhone;
	}

	public String getOldAddress() {
		return OldAddress;
	}

	public void setOldAddress(String oldAddress) {
		OldAddress = oldAddress;
	}

	@Override
	public String toString() {
		return "OldUserInfo [oldName=" + oldName + ", oldPhone=" + oldPhone
				+ ", OldAddress=" + OldAddress + ", ivPic=" + ivPic + "]";
	}


	
}
