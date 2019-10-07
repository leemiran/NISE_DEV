package egovframework.com.utl.fcc.service;

import egovframework.com.utl.cas.service.EgovMessageUtil;

public class EgovUtilTestService {

    /**
     * 
     * @param source
     * @param subject
     * @param object
     * @return
     */
    public static String replaceString(String source, String subject, String object) {
	return EgovStringUtil.replace(source, subject, object);
    }

    /**
     * 
     * @param srcString
     * @param charsetNm
     * @return
     */
    public static String getEncodedString(String srcString, String charsetNm, String cnvrCharsetNm) {
	return EgovStringUtil.getEncdDcd(srcString, charsetNm, cnvrCharsetNm);
    }

    /**
     * 
     * @param sourceStr
     * @param compareStr
     * @param returnStr
     * @param defaultStr
     * @return
     */
    public static String decodeString(String sourceStr, String compareStr, String returnStr, String defaultStr) {
	return EgovStringUtil.decode(sourceStr, compareStr, returnStr, defaultStr);
    }

    /**
     * 
     * @param str
     * @param searchStr
     * @return
     */
    public static int getIndexOfSrtring(String str, String searchStr) {
	return EgovStringUtil.indexOf(str, searchStr);
    }

    /**
     * 
     * @param strCode
     * @param arrParam
     * @return
     */
    public static String getInfoMsg(String strCode, String[] arrParam) {
	return EgovMessageUtil.getInfoMsg(strCode, arrParam);
    }

    /**
     * 
     * @param strCode
     * @param arrParam
     * @return
     */
    public static String getWarnMsg(String strCode, String[] arrParam) {
	return EgovMessageUtil.getWarnMsg(strCode, arrParam);
    }

    /**
     * 
     * @param strCode
     * @param arrParam
     * @return
     */
    public static String getErrorMsg(String strCode, String[] arrParam) {
	return EgovMessageUtil.getErrorMsg(strCode, arrParam);
    }

    /**
     * 
     * @param strCode
     * @param arrParam
     * @return
     */
    public static String getConfirmMsg(String strCode, String[] arrParam) {
	return EgovMessageUtil.getConfirmMsg(strCode, arrParam);
    }		
	
}
