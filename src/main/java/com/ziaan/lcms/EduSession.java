/*
 * @(#)EduSession.java	1.0 2008. 12.08
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */

package com.ziaan.lcms;

import org.apache.log4j.Logger;

import com.ziaan.library.RequestBox;

/**
 * 학습창 세션 관리 클래스
 * 
 * @version 1.0, 2008/12/08
 * @author Chung Jin-pil
 */
public class EduSession {

	private static Logger logger = Logger.getLogger("EduSession");
	
	public static void setEduInfoAndAuthSession(RequestBox box) throws Exception {
		setEduInfoSession(box);
		setEduAuthSession(box);
	}

	/**
	 * 학습권한(s_eduauth) Session 저장
	 * 
	 * @param box
	 */
	public static void setEduAuthSession(RequestBox box) throws Exception {

		String s_eduAuth = EduAuthBean.EDU_AUTH_UNAUTHORIZED;
		
		String s_gadmin = box.getSession("gadmin");
		String v_studytype = box.getString("p_studytype");
		String v_year = box.getString("p_year");

		EduAuthBean eduAuthBean = EduAuthBean.getInstance();
		
		if ( v_studytype.equals("openedu") ) {
			s_eduAuth = eduAuthBean.getEduAuthOpenedu(box);
		} else if ( v_studytype.equals("customedu") ) {
			s_eduAuth = eduAuthBean.getEduAuthCustomedu(box);
		} else if ( v_studytype.equals("review") ) {
			s_eduAuth = EduAuthBean.EDU_AUTH_PREVIEW;	// eduAuthBean.getEduAuthReview(box);
		} else if ( v_studytype.equals("betatest") ) {
			s_eduAuth = eduAuthBean.getEduAuthBetatest(box);
		} else if ( v_year.equals("PREV") ) {
			s_eduAuth = EduAuthBean.EDU_AUTH_PREVIEW;
		} else if ( v_year.equals("2000") && eduAuthBean.isAdminAuth(s_gadmin) ) {
			s_eduAuth = EduAuthBean.EDU_AUTH_PREVIEW;
		} else {
			s_eduAuth = eduAuthBean.getEduAuthStudent(box);
		}

		logger.debug( "@ 학습권한 : " + s_eduAuth );
		box.setSession("s_eduauth", s_eduAuth);
	}

	/**
	 * 키값 Session 저장
	 * 
	 * @param box
	 */
	public static void setEduInfoSession(RequestBox box) {
		String v_subj = box.getString("p_subj");
		String v_year = box.getString("p_year");
		String v_subjseq = box.getString("p_subjseq");
		
		String s_userid = box.getSession("userid");

		if (!v_subj.equals("")) {
			box.setSession("s_subj", v_subj);
		}
		
		if (!v_year.equals("")) {
			box.setSession("s_year", v_year);
		}
		
		if (!v_subjseq.equals("")) {
			box.setSession("s_subjseq", v_subjseq);
		}

		// 과정 맛보기 - Session이 없을 경우, 임시 Session 생성
		if ( (v_year.equals("2000") || v_year.equals("PREV"))
				&& s_userid.equals("") ) {
			box.setSession("userid", "guest1");
		}
	}
	
	/**
	 * 진도체크 권한여부를 반환한다.
	 * 
	 * @param reqBox
	 * @return
	 */
    public static String getEduCheckAuth(RequestBox reqBox) {

    	String eduCheckAuth = reqBox.getSession("s_eduauth");
    	logger.debug( "@ is Edu Check Auth : " + eduCheckAuth );

    	if ( eduCheckAuth == EduAuthBean.EDU_AUTH_AUTHORIZED
    			|| eduCheckAuth == EduAuthBean.EDU_AUTH_BETATEST
    			|| eduCheckAuth == EduAuthBean.EDU_AUTH_PREVIEW )
    		return eduCheckAuth;
    	else
    		return EduAuthBean.EDU_AUTH_UNAUTHORIZED;
	}	
}
