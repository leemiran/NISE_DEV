package com.ziaan.sulmun;

import com.ziaan.library.DataBox;

public class Option {
    private int selnum;
    private String seltext;
    private int selpoint;
    
    private int count; //답변 선택한 사람 수
    
    public Option() {
    	
    }
    
    public Option(Object o) {
    	DataBox dataBox = (DataBox)o;
    	setSelnum(dataBox.getInt("d_selnum"));
    	setSeltext(dataBox.getString("d_seltext"));
    	setSelpoint(dataBox.getInt("d_selpoint"));
    }
    
	/**
	 * @return Returns the selnum.
	 */
	public int getSelnum() {
		return selnum;
	}
	/**
	 * @return Returns the count.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count The count to set.
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @param selnum The selnum to set.
	 */
	public void setSelnum(int selnum) {
		this.selnum = selnum;
	}
	/**
	 * @return Returns the selpoint.
	 */
	public int getSelpoint() {
		return selpoint;
	}
	/**
	 * @param selpoint The selpoint to set.
	 */
	public void setSelpoint(int selpoint) {
		this.selpoint = selpoint;
	}
	/**
	 * @return Returns the seltext.
	 */
	public String getSeltext() {
		return seltext;
	}
	/**
	 * @param seltext The seltext to set.
	 */
	public void setSeltext(String seltext) {
		this.seltext = seltext;
	}
    
    
    
}