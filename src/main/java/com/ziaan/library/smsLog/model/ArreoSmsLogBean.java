package com.ziaan.library.smsLog.model;

public class ArreoSmsLogBean {
	private String idx; // 인덱스
	private String group_idx;// 그룹인덱스(그룹전송시 같은 인덱스를 사용하여 group By로 묶어서 출력하기 위해서
	private String SND_MSG;// 전송메시지
	private String RHanName;// 받은이 이름
	private String RCellPhoneNumber;// 받은이 휴대전화번호
	private String RSLT_VAL;// 발송결과
	private String sendDate;//발송일
	private String SCellphoneNumber;
	
	public String getSCellphoneNumber() {
		return SCellphoneNumber;
	}

	public void setSCellphoneNumber(String cellphoneNumber) {
		SCellphoneNumber = cellphoneNumber;
	}

	public ArreoSmsLogBean() {}
	
	public ArreoSmsLogBean(String idx, 
						String group_idx, 
						String SND_MSG,
						String RHanName,
						String RCellPhoneNumber,
						String RSLT_VAL,
						String sendDate, 
						String SCellphoneNumber) {
		this.idx=idx;
		this.group_idx=group_idx;
		this.SND_MSG=SND_MSG;
		this.RHanName=RHanName;
		this.RCellPhoneNumber=RCellPhoneNumber;
		this.RSLT_VAL=RSLT_VAL;
		this.sendDate=sendDate;
		this.SCellphoneNumber=SCellphoneNumber;
	}
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getGroup_idx() {
		return group_idx;
	}
	public void setGroup_idx(String group_idx) {
		this.group_idx = group_idx;
	}
	public String getSND_MSG() {
		return SND_MSG;
	}
	public void setSND_MSG(String snd_msg) {
		SND_MSG = snd_msg;
	}
	public String getRHanName() {
		return RHanName;
	}
	public void setRHanName(String hanName) {
		RHanName = hanName;
	}
	public String getRCellPhoneNumber() {
		return RCellPhoneNumber;
	}
	public void setRCellPhoneNumber(String cellPhoneNumber) {
		RCellPhoneNumber = cellPhoneNumber;
	}
	public String getRSLT_VAL() {
		return RSLT_VAL;
	}
	public void setRSLT_VAL(String rslt_val) {
		RSLT_VAL = rslt_val;
	}
	public String getSandDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	
	
	
}
