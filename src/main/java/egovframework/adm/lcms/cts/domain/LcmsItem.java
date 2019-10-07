/* 
 * LcmsItem.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.domain;


/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsItem.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsItem {
	
	//db
    private long   itemSeq = 0;
    private long   rsrcSeq = 0;
    private long   orgSeq = 0;
    private long   highItemSeq = 0;
    private long   orgItemNo = 0;
    private String itemId = "";
    private String itemIdRef = "";
    private String itemTitle = "";
    private String itemType = "";
    private String itemOpen = "";
    private String itemTlAction = "";
    private long   itemMaxTime = 0;
    private String itemStartFile = "";
    private String itemLomFile = "";
    private String itemParameters = "";
    private String dataFromLms = "";
    private String itemThreshold = "";
    private String metaLocation = "";
    private String userId = "";
    private String updateDt = "";
    private String courseCd = "";
    private long   itemProgressTime = 0;
    private long   beforeItemSeq = 0;
    
    /** 
	private String rsrcId      = null;
	private String rsrcBaseDir = null;
	private String progId      = null;
	private String publicYN    = null;
	private int    courseSeq   = 0;
    **/
    
    
    
    
    
	private String launchPath		= null;
	private String next 			= null;
	private String Previous			= null;
	private String exit 			= null;
	private String abandon 			= null;

    public long getItemSeq() {
        return itemSeq;
    }

    public long getRsrcSeq() {
        return rsrcSeq;
    }

    public long getOrgSeq() {
        return orgSeq;
    }

    public long getHighItemSeq() {
        return highItemSeq;
    }

    public long getOrgItemNo() {
        return orgItemNo;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemIdRef() {
        return itemIdRef;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public String getItemType() {
        return itemType;
    }

    public String getItemOpen() {
        return itemOpen;
    }

    public String getItemTlAction() {
        return itemTlAction;
    }

    public long getItemMaxTime() {
        return itemMaxTime;
    }

    public String getItemStartFile() {
        return itemStartFile;
    }

    public String getItemLomFile() {
        return itemLomFile;
    }

    public String getItemParameters() {
        return itemParameters;
    }

    public String getDataFromLms() {
        return dataFromLms;
    }

    public String getItemThreshold() {
        return itemThreshold;
    }

    public String getMetaLocation() {
        return metaLocation;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public String getCourseCd() {
        return courseCd;
    }

    public long getItemProgressTime() {
        return itemProgressTime;
    }

    public long getBeforeItemSeq() {
        return beforeItemSeq;
    }

    public void setItemSeq( long itemSeq) {
        this.itemSeq = itemSeq;
    }

    public void setRsrcSeq( long rsrcSeq) {
        this.rsrcSeq = rsrcSeq;
    }

    public void setOrgSeq( long orgSeq) {
        this.orgSeq = orgSeq;
    }

    public void setHighItemSeq( long highItemSeq) {
        this.highItemSeq = highItemSeq;
    }

    public void setOrgItemNo( long orgItemNo) {
        this.orgItemNo = orgItemNo;
    }

    public void setItemId( String itemId) {
        this.itemId = itemId;
    }

    public void setItemIdRef( String itemIdRef) {
        this.itemIdRef = itemIdRef;
    }

    public void setItemTitle( String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public void setItemType( String itemType) {
        this.itemType = itemType;
    }

    public void setItemOpen( String itemOpen) {
        this.itemOpen = itemOpen;
    }

    public void setItemTlAction( String itemTlAction) {
        this.itemTlAction = itemTlAction;
    }

    public void setItemMaxTime( long itemMaxTime) {
        this.itemMaxTime = itemMaxTime;
    }

    public void setItemStartFile( String itemStartFile) {
        this.itemStartFile = itemStartFile;
    }

    public void setItemLomFile( String itemLomFile) {
        this.itemLomFile = itemLomFile;
    }

    public void setItemParameters( String itemParameters) {
        this.itemParameters = itemParameters;
    }

    public void setDataFromLms( String dataFromLms) {
        this.dataFromLms = dataFromLms;
    }

    public void setItemThreshold( String itemThreshold) {
        this.itemThreshold = itemThreshold;
    }

    public void setMetaLocation( String metaLocation) {
        this.metaLocation = metaLocation;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setCourseCd( String courseCd) {
        this.courseCd = courseCd;
    }

    public void setItemProgressTime( long itemProgressTime) {
        this.itemProgressTime = itemProgressTime;
    }

    public void setBeforeItemSeq( long beforeItemSeq) {
        this.beforeItemSeq = beforeItemSeq;
    }

	public String getLaunchPath() {
		return launchPath;
	}

	public void setLaunchPath(String launchPath) {
		this.launchPath = launchPath;
	}

	public String getNext() {
		return next;
	}

	public void setNext(String next) {
		this.next = next;
	}

	public String getPrevious() {
		return Previous;
	}

	public void setPrevious(String previous) {
		Previous = previous;
	}

	public String getExit() {
		return exit;
	}

	public void setExit(String exit) {
		this.exit = exit;
	}

	public String getAbandon() {
		return abandon;
	}

	public void setAbandon(String abandon) {
		this.abandon = abandon;
	}

    
}
