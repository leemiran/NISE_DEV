package com.ziaan.library.smsLog.model;

public class ArreoSmsLogBean {
	private String idx; // �ε���
	private String group_idx;// �׷��ε���(�׷����۽� ���� �ε����� ����Ͽ� group By�� ��� ����ϱ� ���ؼ�
	private String SND_MSG;// ���۸޽���
	private String RHanName;// ������ �̸�
	private String RCellPhoneNumber;// ������ �޴���ȭ��ȣ
	private String RSLT_VAL;// �߼۰��
	private String sendDate;//�߼���
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
