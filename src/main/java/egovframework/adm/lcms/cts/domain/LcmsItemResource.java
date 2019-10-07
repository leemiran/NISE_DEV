/* 
 * LcmsItemResource.java		1.00	2011-09-05 
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
 * source      : LcmsItemResource.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsItemResource {
    private long rsrcSeq = 0;
    private String rsrcType = "";
    private String rsrcHref = "";
    private String rsrcBaseDir = "";
    private String rsrcScormType = "";
    private String rsrcPstState = "";
    private String userId = "";
    private String updateDt = "";
    private String rsrcTitle = "";
    private long rsrcScoSize = 0;
    private long groupSeq = 0;
    private String rsrcScoVersion = "";
    private String rsrcId = "";

    public long getRsrcSeq() {
        return rsrcSeq;
    }

    public String getRsrcType() {
        return rsrcType;
    }

    public String getRsrcHref() {
        return rsrcHref;
    }

    public String getRsrcBaseDir() {
        return rsrcBaseDir;
    }

    public String getRsrcScormType() {
        return rsrcScormType;
    }

    public String getRsrcPstState() {
        return rsrcPstState;
    }

    public String getUserId() {
        return userId;
    }

    public String getUpdateDt() {
        return updateDt;
    }

    public String getRsrcTitle() {
        return rsrcTitle;
    }

    public long getRsrcScoSize() {
        return rsrcScoSize;
    }

    public long getGroupSeq() {
        return groupSeq;
    }

    public String getRsrcScoVersion() {
        return rsrcScoVersion;
    }

    public String getRsrcId() {
        return rsrcId;
    }

    public void setRsrcSeq( long rsrcSeq) {
        this.rsrcSeq = rsrcSeq;
    }

    public void setRsrcType( String rsrcType) {
        this.rsrcType = rsrcType;
    }

    public void setRsrcHref( String rsrcHref) {
        this.rsrcHref = rsrcHref;
    }

    public void setRsrcBaseDir( String rsrcBaseDir) {
        this.rsrcBaseDir = rsrcBaseDir;
    }

    public void setRsrcScormType( String rsrcScormType) {
        this.rsrcScormType = rsrcScormType;
    }

    public void setRsrcPstState( String rsrcPstState) {
        this.rsrcPstState = rsrcPstState;
    }

    public void setUserId( String userId) {
        this.userId = userId;
    }

    public void setUpdateDt( String updateDt) {
        this.updateDt = updateDt;
    }

    public void setRsrcTitle( String rsrcTitle) {
        this.rsrcTitle = rsrcTitle;
    }

    public void setRsrcScoSize( long rsrcScoSize) {
        this.rsrcScoSize = rsrcScoSize;
    }

    public void setGroupSeq( long groupSeq) {
        this.groupSeq = groupSeq;
    }

    public void setRsrcScoVersion( String rsrcScoVersion) {
        this.rsrcScoVersion = rsrcScoVersion;
    }

    public void setRsrcId( String rsrcId) {
        this.rsrcId = rsrcId;
    }

}
