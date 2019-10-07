/* 
 * LcmsModule.java		1.00	2011-10-11 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.nct.domain;


/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsModule.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-10-11 created by ?
 * 1.1	
 * </pre>
 */


public class LcmsModule {
    private String crsCode = "";
    private String module = "";
    private String moduleName = "";
    private String inuserid = "";
    private String indate = "";
    private String luserid = "";
    private String ldate = "";

    public String getCrsCode() {
        return crsCode;
    }

    public String getModule() {
        return module;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getInuserid() {
        return inuserid;
    }

    public String getIndate() {
        return indate;
    }

    public String getLuserid() {
        return luserid;
    }

    public String getLdate() {
        return ldate;
    }

    public void setCrsCode( String crsCode) {
        this.crsCode = crsCode;
    }

    public void setModule( String module) {
        this.module = module;
    }

    public void setModuleName( String moduleName) {
        this.moduleName = moduleName;
    }

    public void setInuserid( String inuserid) {
        this.inuserid = inuserid;
    }

    public void setIndate( String indate) {
        this.indate = indate;
    }

    public void setLuserid( String luserid) {
        this.luserid = luserid;
    }

    public void setLdate( String ldate) {
        this.ldate = ldate;
    }

}
