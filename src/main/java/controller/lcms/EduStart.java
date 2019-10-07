/*
 * @(#)EduStart.java	1.0 2008. 11. 03
 *
 * Copyright 2008  Co,. LTD. All rights reserved.
 */
package controller.lcms;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.credu.learn.ReportProfBean;
import com.ziaan.course.SubjGongAdminBean;
import com.ziaan.exam.ExamUserBean;
import com.ziaan.lcms.EduAuthBean;
import com.ziaan.lcms.EduLogBean;
import com.ziaan.lcms.EduProgressBean;
import com.ziaan.lcms.EduScoreData;
import com.ziaan.lcms.EduSession;
import com.ziaan.lcms.EduStartBean;
import com.ziaan.lcms.MasterFormData;
import com.ziaan.lcms.MasterformConfigBean;
import com.ziaan.library.AlertManager;
import com.ziaan.library.DataBox;
import com.ziaan.library.EduEtc1Bean;
import com.ziaan.library.ErrorManager;
import com.ziaan.library.RequestBox;
import com.ziaan.library.RequestManager;
import com.ziaan.research.SulmunAllPaperBean;
import com.ziaan.research.SulmunContentsUserBean;
import com.ziaan.research.SulmunSubjUserBean;
import com.ziaan.study.ProjectAdminBean;
import com.ziaan.study.ReportBean;
import com.ziaan.system.AdminUtil;
import com.ziaan.system.StudyCountBean;

/**
 * 학습창 (ContentType : Normal, OBC) 
 * 
 * @author Lee Su-Min, 2003/08/19
 * @modifier Chung Jin-Pil, 2008/11/03
 */
public class EduStart extends HttpServlet {

	private Logger logger = Logger.getLogger(this.getClass());

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		this.doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
		response.setContentType("text/html;charset=euc-kr");
		request.setCharacterEncoding("euc-kr");

		PrintWriter out = response.getWriter();
		RequestBox box = RequestManager.getBox(request);

		try {
			String v_process = box.getStringDefault("p_process", "main");

			if (!AdminUtil.getInstance().checkLoginPopup(out, box)) {
				return; 
			}
			
			if (ErrorManager.isErrorMessageView()) {
				box.put("errorout", out);
			}

			logger.debug("# EduStart Process : " + v_process);

			if (v_process.equals("main")) {							// 학습창 메인 Frame
				this.performFrameSetPage(request, response, box, out);
			} else if (v_process.equals("fsetsub")) {				// 학습창 중앙 Frame
				this.performFrameSetSubPage(request, response, box, out);
			} else if (v_process.equals("fup")) {					// 학습창 상단 Frame
				this.performFrameUpPage(request, response, box, out);
			} else if (v_process.equals("fmenu")) {					// 학습창 메뉴 Frame
				this.performFrameMenuPage(request, response, box, out);
			} else if (v_process.equals("tree")) {					// 학습창 OBC Tree 목차
				this.performFrameTreePage(request, response, box, out);
			}  else if (v_process.equals("bott")) {					// 학습창 OBC Navigation
				this.performFrameNaviPage(request, response, box, out);
			} else if (v_process.equals("liveshare")) {				// LiveShare
				this.performLiveShare(request, response, box, out);
			} else if (v_process.equals("branchControl")) {			// 분기제어 (사용안함 - 삭제예정)
				this.performEduBranchPage(request, response, box, out);
			} else if (v_process.equals("fmain")) {					// 학습창 메인
				this.performMainPage(request, response, box, out);
			} else if (v_process.equals("firstSubj")) {				// 학습자 유의사항 동의처리
				this.performFirstSubj(request, response, box, out);
			} else if (v_process.equals("eduList")) {				// 진도/목차 현황
				this.eduListPage(request, response, box, out);
			// 20100512 강의창 수정작업 @ Parks	
			} else if (v_process.equals("newfup")) {				// 학습창 상단 Frame
				this.performFrameNewUpPage(request, response, box, out);
			} else if (v_process.equals("newfmain")) {				// 학습창 메인
				this.performNewMainPage(request, response, box, out);	
			} else if (v_process.equals("NewStudy")) {				// 콘텐츠 링크
				this.performNewStudy(request, response, box, out);					
			}
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
		}
	}

	/**
	 * 학습창 메인 Frame
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameSetPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			// 키값 및 권한정보 Session에 저장 (학습창 Main일 경우만)
			EduSession.setEduInfoAndAuthSession(box);
			
			String v_url = "";
			
			MasterformConfigBean mfBean = new MasterformConfigBean();
			MasterFormData mfData = mfBean.selectMasterform(box);
			request.setAttribute("MasterFormData", mfData);
			
			// 외부과정 처리
			if ( mfData.getIsoutsourcing().equals("Y") ) {
				String p_subj = box.getString("p_subj");
				String p_year = box.getString("p_year");
				String p_subjseq = box.getString("p_subjseq");

				String field1 = box.getString("FIELD1");
				String field2 = box.getString("FIELD2");
				String field3 = box.getString("FIELD3");
				String field4 = box.getString("FIELD4");
				String field5 = box.getString("FIELD5");
				String field99 = box.getString("FIELD99");
				String field100 = box.getString("FIELD100");
				String p_iurl = box.getString("p_iurl");

				box.put("p_iurl", p_iurl);
				box.put("p_subj", p_subj);
				box.put("p_year", p_year);
				box.put("p_subjseq", p_subjseq);
				
				box.put("isoutsourcing", mfData.getIsoutsourcing());
				box.put("eduurl", mfData.getEduurl());
				box.put("p_isinfo", "N");
				
				v_url = "/learn/user/lcms/z_EduStart_Outsourcing.jsp"
					+ "?FIELD1=" + field1 + "&FIELD2=" + field2 + "&FIELD3=" + field3 + "&FIELD4=" + field4 + "&FIELD99=" + field99 + "&FIELD100=" +field100;
			} else {
				if (mfData.getContenttype().equals("N")) {
					// 20100512 강의창 프레임 경로 수정 @ Parks
					v_url = "/learn/user/lcms/knise/z_EduStart_fset.jsp";
				} else if (mfData.getContenttype().equals("O")) {
					v_url = "/learn/user/lcms/z_EduStart_fset_OBC.jsp";
				}
			}
			// 리포트 배정 : S
			ReportBean bean = new ReportBean();
			ArrayList M_ReportList = (ArrayList)bean.MReport(box);			// 중간 리포트 배정여부
			ArrayList F_ReportList = (ArrayList)bean.FReport(box);			// 기말 리포트 배정여부
			if(M_ReportList.size() > 0 ){	
				int projMcnt = bean.isReportMAssign(box, M_ReportList);
				if(projMcnt == 0 ){
					bean.updateReportMAssign(box);        // 학습자 배정 
				}
			}
			if(F_ReportList.size() > 0 ){
				int projFcnt = bean.isReportFAssign(box, F_ReportList);
				if(projFcnt == 0 ){
					bean.updateReportFAssign(box);        // 학습자 배정 
				}
			}
			// 리포트 배정 : E
			
			// 학습창 접속 Log
			EduLogBean eduLogBean = new EduLogBean();
			if ( EduSession.getEduCheckAuth(box) == EduAuthBean.EDU_AUTH_AUTHORIZED ) {
				eduLogBean.eduStartLog(box, request.getRemoteAddr());
			}

			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performMainPage()\r\n" + ex.getMessage());
		}
	}
	
	/**
	 * 학습창 중앙(Sub) Frame
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameSetSubPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String v_url = "";
			String v_contenttype = box.getString("p_contenttype");
			
			if (v_contenttype.equals("O")) {
				v_url = "/learn/user/lcms/z_EduStart_fsetSub_OBC.jsp";
			}

			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performFrameSetSubPage()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습창 상단 Frame
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameUpPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String v_url = "";
			String v_contenttype = box.getString("p_contenttype");

			if (v_contenttype.equals("N")) {

				EduStartBean bean = EduStartBean.getInstance();
				ArrayList lessonList = bean.SelectMfLessonList(box); // Lesson List
				request.setAttribute("MfLessonList", lessonList);
				
				v_url = "/learn/user/lcms/z_EduStart_fup.jsp";
			} else if (v_contenttype.equals("O")) {
				v_url = "/learn/user/lcms/z_EduStart_fup_OBC.jsp";
			}

			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performFrameUpPage()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습창 메뉴 Frame
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameMenuPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String v_url = "";

			// p_year==PREV (맛보기) 일 경우  Menu 숨김 
			boolean isPreview = box.getSession("s_year").equals("PREV") ? true : false;

			if ( isPreview ) {
				v_url = "/learn/user/lcms/z_EduStart_fmenu_blank.jsp";
			} else {
				MasterformConfigBean mfcb = new MasterformConfigBean();
				String s_subj = box.getSession("s_subj");
				List menuList = mfcb.selectMFSubjList(s_subj);
				request.setAttribute("menuList", menuList);

				v_url = "/learn/user/lcms/z_EduStart_fmenu.jsp";
			}

			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performFrameMenuPage()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습창 OBC Navigation
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameNaviPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String v_url = "/learn/user/lcms/z_EduStart_fbott_OBC.jsp";
			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performFrameNaviPage()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습창(OBC) Tree Frame
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameTreePage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String v_url = "";
			
			String p_contenttype = box.getString("p_contenttype");
			boolean isPreview = box.getSession("s_year").equals("PREV") ? true : false;

			EduStartBean eduBean = EduStartBean.getInstance();

			if (p_contenttype.equals("O")) {

				List treeList = null;
				Map progressDataMap = null;

				if ( isPreview ) {
					treeList = eduBean.SelectTreeDataListPreview(box);
					progressDataMap = new HashMap();
				} else {
					treeList = eduBean.SelectTreeDataList(box);
					progressDataMap = (HashMap) eduBean.selectProgressData(box);	// 진도정보
				}

				String isaccesstype = eduBean.israndomaccess(box.getSession("s_subj"));
				
				System.out.println("isaccesstype =====> "+isaccesstype);
				System.out.println("isaccesstype =====> "+isaccesstype);
				System.out.println("isaccesstype =====> "+isaccesstype);
				System.out.println("isaccesstype =====> "+isaccesstype);
				System.out.println("isaccesstype =====> "+isaccesstype);
				System.out.println("isaccesstype =====> "+isaccesstype);
				System.out.println("isaccesstype =====> "+isaccesstype);
				
				if(isaccesstype.equals("N") && !box.getSession("s_year").equals("PREV")){
					String p_lesson = EduEtc1Bean.maxlesson(box.getSession("s_subj"),box.getSession("s_year"),box.getSession("s_subjseq"),box.getSession("userid"));
					if(p_lesson.equals("0")){
						boolean isok = eduBean.firstInsertProgress(box);
					}
				}
				
				request.setAttribute("TreeData", treeList);
				request.setAttribute("progressDataMap", progressDataMap);

				String treeTitle = eduBean.selectSubjnm(box);
				request.setAttribute("treeTitle", treeTitle);

				v_url = "/learn/user/lcms/z_EduStart_ftree_OBC.jsp";
			}
			
			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performFrameTreePage()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습창 메인 페이지 - 공지, Q&A 등 게시판 및 진도/목차 정보
	 *  
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performMainPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String p_contenttype = box.getString("p_contenttype");
			String v_subjgu = box.getString("p_subjgubun");	// M: 매월정기안전교육, J: JIT
			String forwardUrl = "";
			
			if ( isSkipMain(box) && !p_contenttype.equals("K") ) {	// 메인 페이지 Skip : 열린교육, 맛보기
				forwardUrl = "/learn/user/lcms/z_EduStart_Main_blank.jsp";
			} else if ( v_subjgu.equals("M") ) {	// 매월정기안전교육 메인 페이지
				setSubjGubunMInfo(request, box);	// 매월정기안전교육정보
				forwardUrl = "/learn/user/lcms/z_EduStart_Main_M.jsp";
			} else {								// 일반 메인 페이지
				forwardUrl = "/learn/user/lcms/z_EduStart_Main.jsp";
			}
			
			setSummaryInfo(request, box);					// 요약정보
			setBBSInfo(request, box);						// 게시판정보
			setSulmunAndReportAndExamInfo(request, box);	// 설문, 과제, 평가정보

			double progress = Double.parseDouble( (String) request.getAttribute("progress") );
			setAllowStudyInfo(request, box, progress);		// 제약사항
			setContentTypeInfo(request, box);				// 컨텐츠별 예외사항

			forwardUrl( request, response, forwardUrl );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performMainPage()\r\n" + ex.getMessage());
		}
	}

	private void setContentTypeInfo(HttpServletRequest request, RequestBox box) throws Exception {
		String v_contenttype = box.getString("p_contenttype");
	    EduStartBean esbean = EduStartBean.getInstance();
		if (v_contenttype.equals("N")) {
			// [하드코딩] ThinkWise 과정일 때,
			if (box.getSession("s_subj").equals("4108D")) {
				SubjGongAdminBean sgaBean = new SubjGongAdminBean();
				Map thinkWiseInfoMap = sgaBean.selectThinkWiseInfo(box);
				request.setAttribute("thinkWiseInfoMap", thinkWiseInfoMap);
			}
			
			String starting = esbean.getStartLessonString(box);
			request.setAttribute("starting", starting);
		} else if (v_contenttype.equals("O")) {
			String starting = esbean.getStartOidString(box);
			request.setAttribute("starting", starting);
		}
	}

	private void setAllowStudyInfo(HttpServletRequest request, RequestBox box, double progress) throws Exception {
		/*
		 * 제약 사항 Check 
		 */
		boolean allowStudy  = false;
		boolean isSulpaper  = false;
		boolean isInfoAgree = false;
		boolean isGoyong    = false;
		
		if ( EduSession.getEduCheckAuth(box) == EduAuthBean.EDU_AUTH_AUTHORIZED ) {			
			// 학습시간 제약
			SubjGongAdminBean sgaBean = new SubjGongAdminBean();
		    allowStudy = sgaBean.allowStudy(box);
		    logger.debug( "학습시간 제약 : " + allowStudy );
		    
		    // 설문 여부  (true시 설문 팝업창 보여줌)
		    isSulpaper = getIsSulpaper(box, progress);
			logger.debug( "설문 여부 : " + isSulpaper );

			// 정보동의 여부 (true시 정보동의 팝업창 보여줌)
			isInfoAgree = getIsInfoAgree(box);
		    logger.debug( "정보동의 여부 : " + isInfoAgree );
		    
		    // 고용보험과정 여부 (true시 고용보험과정 팝업창 보여줌)
		    isGoyong = getIsGoyong(box);
		    logger.debug( "고용보험과정 : " + isGoyong );
		}

		request.setAttribute("allowStudy", new Boolean(allowStudy));
		request.setAttribute("isSulpaper", isSulpaper );
		request.setAttribute("isInfoAgree", isInfoAgree );
		request.setAttribute("isGoyong", isGoyong );
	}

	private void setSulmunAndReportAndExamInfo(HttpServletRequest request, RequestBox box) throws Exception {
		/* s : 과목설문 응시여부 */
		SulmunSubjUserBean sulbean = new SulmunSubjUserBean();
		int suldata = sulbean.getSulData(box);
		int sulresult = sulbean.getUserData(box);
		ArrayList suldate = sulbean.getSulDate(box);
		box.put("p_suldata",String.valueOf(suldata));
		box.put("p_sulresult",String.valueOf(sulresult));
		request.setAttribute("Suldate", suldate);
		/* e : 과목설문 응시여부 */

		SulmunContentsUserBean contentsbean = new SulmunContentsUserBean();
		//먼저 컨텐츠설문지 잇는지 확인...(2005.10.13)
		int ispaper = contentsbean.getContentsSulmunPaper(box);
		box.put("p_ispaper", String.valueOf(ispaper));
		

		/* s : 과제 출제개수 */
		ProjectAdminBean report = new ProjectAdminBean();
		int reportadmin = report.getAdminData(box);
		box.put("p_report",String.valueOf(reportadmin));
		/* e : 과제 출제개수 */
		
		/* s : 과제 제출여부 */
		int reportdata = report.getUserData(box);
		box.put("p_reportdata",String.valueOf(reportdata));
		/* e : 과제 제출여부 */
		
		/* s : 평가 갯수 */
		ExamUserBean exambean = new ExamUserBean();
		ArrayList examdata = exambean.getUserData(box);
		request.setAttribute("ExamData", examdata);
		/* e : 평가 갯수 */
		
		/* s : 평가 응시여부 */
		ArrayList examresultdata = exambean.getUserResultData(box);
		request.setAttribute("ExamResultData", examresultdata);
		/* e : 평가 응시여부 */
		
		/* s : 평가 응시조건 */
		ArrayList examconditiondata = exambean.getApplyExamConditionData(box);
		request.setAttribute("ExamConditionData", examconditiondata);
		/* e : 평가 응시조건 */
		
		/* s : 평가 응시상태 */
		ArrayList examstatusdata = exambean.getUserStatusData(box); 
		request.setAttribute("ExamStatusData", examstatusdata);
		/* e : 평가 응시상태 */
	}

	private void setBBSInfo(HttpServletRequest request, RequestBox box) throws Exception {
		
		EduStartBean bean = EduStartBean.getInstance();
		
		// 공지사항
		ArrayList selectGongList = bean.selectGongList(box);
		request.setAttribute("selectGongList", selectGongList);
		
		// 자료실
		box.put("p_type", "SD");
		ArrayList sdList = bean.selectBoardList(box);
		request.setAttribute("sdList", sdList);
		// Q&A
		box.put("p_type", "SQ");
		ArrayList sqList = bean.selectQnaBoardList(box);
		request.setAttribute("sqList", sqList);
	}

	private void setSummaryInfo(HttpServletRequest request, RequestBox box) throws Exception {
		SubjGongAdminBean sgaBean = new SubjGongAdminBean();
		
		// 나의 진도율, 권장 진도율
		double progress = Double.parseDouble(sgaBean.getProgress(box));
		double promotion = Double.parseDouble(sgaBean.getPromotion(box));
		
		request.setAttribute("progress", String.valueOf(progress));
		request.setAttribute("promotion", String.valueOf(promotion));
		
		// 학습정보
		EduStartBean bean = EduStartBean.getInstance();
		ArrayList dataTime = bean.SelectEduTimeCountOBC(box);          // 학습시간,최근학습일,강의접근횟수
		request.setAttribute("EduTime", dataTime);
		
		EduScoreData data2 = bean.SelectEduScore(box);
		request.setAttribute("EduScore", data2);
		
		// 강사정보
		DataBox tutorInfo = bean.getTutorInfo(box);
		request.setAttribute("tutorInfo", tutorInfo);
		
		ReportProfBean report = new ReportProfBean();
		box.put("p_grcode","N000001");
		box.put("p_class","1");
        ArrayList list = report.selectListOrderPerson(box);
		request.setAttribute("ReportInfo", list);

		// 총차시, 학습한 차시, 진도율, 과정구분
		DataBox dbox = bean.getStudyChasi(box);
		int datecnt = Integer.parseInt(dbox.getString("datecnt").equals("") ? "0" : dbox.getString("datecnt"));				// 총차시
		int edudatecnt = Integer.parseInt(dbox.getString("edudatecnt").equals("") ? "0" : dbox.getString("edudatecnt"));	// 학습한 차시
		double wstep = Double.parseDouble(dbox.getString("wstep").equals("") ? "0" : dbox.getString("wstep"));				// 진도율
		
		request.setAttribute("datecnt", String.valueOf(datecnt));
		request.setAttribute("edudatecnt", String.valueOf(edudatecnt));
		request.setAttribute("wstep", String.valueOf(wstep));		
	}

	private void setSubjGubunMInfo(HttpServletRequest request, RequestBox box) throws Exception {
		
		// 매월정기안전 현황
		EduStartBean bean = EduStartBean.getInstance();
		DataBox dbox2 = bean.getCalcStudyMm(box);
		int totalchasi	= Integer.parseInt(dbox2.getString("totalchasi").equals("") ? "0" : dbox2.getString("totalchasi"));		// 총차시
		int studychasi	= Integer.parseInt(dbox2.getString("studychasi").equals("") ? "0" : dbox2.getString("studychasi"));		// 총차시
		int studycount 	= Integer.parseInt(dbox2.getString("studycount").equals("") ? "0" : dbox2.getString("studycount"));	// 학습한 차시
		int studytime 	= Integer.parseInt(dbox2.getString("studytime").equals("") ? "0" : dbox2.getString("studytime"));	// 학습한 차시
		
		request.setAttribute("start_date", dbox2.getString("start_date"));
		request.setAttribute("end_date"  , dbox2.getString("end_date"));
		request.setAttribute("totalchasi", String.valueOf(totalchasi));
		request.setAttribute("studychasi", String.valueOf(studychasi));
		request.setAttribute("studycount", String.valueOf(studycount));
		request.setAttribute("studytime" , String.valueOf(studytime));		
	}

	/**
	 * 메인 페이지 생략 여부 : true일 경우 메인 생략하고 바로 학습시작함
	 * 
	 * @param box
	 * @return
	 */
	private boolean isSkipMain(RequestBox box) {
		boolean isSkipMain = false;
		
		String v_studytype = box.getString("p_studytype");
		String s_year = box.getSession("s_year");
		if ( v_studytype.equals("openedu") || s_year.equals("PREV") ) {
			isSkipMain = true;
		}
		
		return isSkipMain;
	}

	private boolean getIsInfoAgree(RequestBox box) throws Exception {
		
		EduStartBean eduBean = EduStartBean.getInstance();
	    int firstCheck = eduBean.firstCheck(box);
	    
	    boolean isInfoAgree = false;
	    if ( firstCheck == 0) {
	    	isInfoAgree = true;
	    }
	    
	    return isInfoAgree;
	}
	
	/**
	 * 고용보험 환급과정 체크
	 * @param box
	 * @return
	 * @throws Exception
	 */
	private boolean getIsGoyong(RequestBox box) throws Exception {
		
		EduStartBean eduBean = EduStartBean.getInstance();
		int firstCheck = eduBean.goyongCheck(box);
		
		boolean isGoyong = false;
		if ( firstCheck == 1) {
			isGoyong = true;
		}
		
		return isGoyong;
	}

	private boolean getIsSulpaper(RequestBox box, double progress) throws Exception {
		boolean isSulpaper = false;

		String p_subj = box.getString("p_subj");
		String p_year= box.getString("p_year");
		String p_subjseq = box.getString("p_subjseq");

		SulmunAllPaperBean sulbean = new SulmunAllPaperBean();
		int sulprogress = sulbean.getSulProgress(p_subj, p_year, p_subjseq);
		int sulresult = sulbean.getSulResult(p_subj, p_year, p_subjseq);

		if ( progress >= sulprogress && sulprogress > 0 && sulresult ==0 ) {
			isSulpaper = true;
		}
		
		return isSulpaper;
	}

	/**
	 * LiveShare 팝업창
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performLiveShare(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
	
			EduStartBean esb = EduStartBean.getInstance();
			Map params = esb.getLiveShareParams(box);
	
			box.put("p_isstudycomp", params.get("isstudycomp"));
			box.put("p_session_time", params.get("session_time"));
	
			request.setAttribute("requestbox", box);
	
			String v_url = "/learn/user/lcms/z_EduLiveShare.jsp";
	
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(v_url);
			rd.forward(request, response);
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performLiveShare()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습분기 제어
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performEduBranchPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			String v_url = "";
	
			EduStartBean bean = EduStartBean.getInstance();
			ArrayList data = bean.SelectEduBranch(box);
			request.setAttribute("BranchList", data);
	
			v_url = "/learn/user/lcms/z_EduBranchCntl.jsp";
	
			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performEduBranchPage()\r\n" + ex.getMessage());
		}
	}

	/**
	 * 학습자 유의사항 동의처리
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFirstSubj(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			EduStartBean bean = EduStartBean.getInstance();

			int isOk = bean.firstSubj(box);
			String v_msg = "";
			AlertManager alert = new AlertManager();

			if (isOk > 0) {
				v_msg = "학습자 유의사항에 동의하셨습니다";
				alert.selfClose(out, v_msg);
			} else {
				v_msg = "실패했습니다.";
				alert.selfClose(out, v_msg);
			}

			// Log.info.println(this, box, v_msg + " on LoginServlet");
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("performFirstSubj()\r\n" + ex.getMessage());
		}
	}	
	
	/**
	 * 해당 forwardUrl로 Forwarding한다.
	 * 
	 * @param request HttpServletRequest 
	 * @param response HttpServletResponse 
	 * @param String forwardUrl
	 * @exception ServletException
	 * @exception IOException
	 */
	protected void forwardUrl(HttpServletRequest request, HttpServletResponse response, String forwardUrl) throws Exception
	{
		if ( forwardUrl != null ) {
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(forwardUrl);
			rd.forward(request, response);
		} else {
			throw new Exception("ForwardUrl 값이 없습니다.");
		}
	}

	/**
	 * TODO 진도/목차 현황 - 정리필요~!
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void eduListPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);

			String v_url = "";
			String v_iurl = box.getString("p_iurl"); 		 // 외주구분(1:외주)
			String v_iseduend = box.getString("p_iseduend"); // 복습여부(1:복습)

			if (box.getString("p_subj").equals(""))
				box.put("p_subj", box.getSession("s_subj"));
			
			if (box.getString("p_year").equals(""))
				box.put("p_year", box.getSession("s_year"));
			
			if (box.getString("p_subjseq").equals(""))
				box.put("p_subjseq", box.getSession("s_subjseq"));

			// if ( !box.getSession("s_subjseq").equals("0000")) {
			
			MasterformConfigBean mfBean = new MasterformConfigBean();
			MasterFormData mfData = mfBean.selectMasterform(box);
			request.setAttribute("MasterFormData", mfData);
			
			EduStartBean bean = EduStartBean.getInstance();

			// 과목별 메뉴 접속 정보 추가
			box.put("p_menu", "03");
			StudyCountBean scBean = new StudyCountBean();
			scBean.writeLog(box);

			 ArrayList data1 = null;
			 ArrayList dataTime= null;
			
			 EduScoreData data2= bean.SelectEduScore(box);
			 request.setAttribute("EduScore", data2);
			 
			 ArrayList  list33= bean.SelectEduScore2(box);
			 request.setAttribute("EduScore2", list33);
			
			int result = bean.selectUserPage(box);

			if (result == 1) {	// L : 외주 , N : Normal , O : OBC , S : SCORM2004
				if (mfData.getContenttype().equals("N")||mfData.getContenttype().equals("K")) { // Normal MasterForm, KT Type
					data1 = bean.SelectEduList(box); // 진도데이터
					dataTime= bean.SelectEduTimeCountOBC(box); // 학습시간,최근학습일,강의접근횟수
					v_url = "/learn/user/lcms/z_EduChk_List.jsp";
				} else if (mfData.getContenttype().equals("O") || mfData.getContenttype().equals("OA")) { // OBC,SCORM
					data1 = bean.SelectEduListOBC(box); // 진도데이터
					dataTime= bean.SelectEduTimeCountOBC(box); // 학습시간,최근학습일,강의접근횟수
					v_url = "/learn/user/lcms/z_EduChk_List_OBC.jsp";
				} else if (mfData.getContenttype().equals("S")) { // SCORM2004
					box.put("p_process", "eduList");
					v_url = "/servlet/controller.scorm2004.ScormStudyServlet";
				} else if (mfData.getContenttype().equals("L")) {
					data1 = bean.SelectEduList(box); // 진도데이터
					dataTime= bean.SelectEduTimeCountOBC(box); // 학습시간,최근학습일,강의접근횟수
					v_url = "/learn/user/lcms/z_EduChk_List_Link.jsp";
				}

				request.setAttribute("EduList", data1);
				request.setAttribute("EduTime", dataTime);
				request.setAttribute("requestbox", box);

			} else if (box.getString("p_subjseq").equals("0000")) {	// 베타테스트이면,

				if (mfData.getContenttype().equals("N")||mfData.getContenttype().equals("K")) { // Normal MasterForm, KT Type
					data1 = bean.SelectEduList(box); // 진도데이터
					dataTime= bean.SelectEduTimeCountOBC(box); // 학습시간,최근학습일,강의접근횟수
					v_url = "/learn/user/lcms/z_EduChk_List_Beta.jsp";
				} else if (mfData.getContenttype().equals("O") || mfData.getContenttype().equals("OA")) { // OBC,SCORM
					data1 = bean.SelectEduListOBC(box); // 진도데이터
					dataTime= bean.SelectEduTimeCountOBC(box); // 학습시간,최근학습일,강의접근횟수
					v_url = "/learn/user/lcms/z_EduChk_List_OBC_Beta.jsp";
				} else if (mfData.getContenttype().equals("S")) { // SCORM2004
					box.put("p_process", "eduList");
					v_url = "/servlet/controller.scorm2004.ScormStudyServlet";
				}

				request.setAttribute("EduList", data1);
				request.setAttribute("EduTime", dataTime);
				request.setAttribute("requestbox", box);

				/*
				if (mfData.getContenttype().equals("S")) { // SCORM2004

					data1 = bean.SelectEduListSCORM2004(box); // 진도데이터
					// dataTime= bean.SelectEduTimeCountOBC(box); // 학습시간,최근학습일,강의접근횟수

					v_url = "/learn/user/lcms/z_EduChk_List_SCORM2004_Beta.jsp"; // 임시로 대체함.
					request.setAttribute("EduList", data1);
					// request.setAttribute("EduTime", dataTime);
				}
				*/
			} else {
				String v_msg = "개인화 페이지라 입과생이 아니면 보실 수 없습니다.";
				AlertManager alert = new AlertManager();
				alert.selfClose(out, v_msg);
			}

			if (mfData.getContenttype().equals("L") || box.getString("p_subjseq").equals("0000")) {
				// System.out.println("외주");
			} else {
				
				// 리포트 배정 : S
				ReportBean rbean = new ReportBean();
				ArrayList M_ReportList = (ArrayList)rbean.MReport(box);			// 중간 리포트 배정여부
				ArrayList F_ReportList = (ArrayList)rbean.FReport(box);			// 기말 리포트 배정여부
				if(M_ReportList.size() > 0 ){	
					int projMcnt = rbean.isReportMAssign(box, M_ReportList);
					if(projMcnt == 0 ){
						rbean.updateReportMAssign(box);        // 학습자 배정 
					}
				}
				if(F_ReportList.size() > 0 ){
					int projFcnt = rbean.isReportFAssign(box, F_ReportList);
					if(projFcnt == 0 ){
						rbean.updateReportFAssign(box);        // 학습자 배정 
					}
				}
				// 리포트 배정 : E
				
				
				/* ==  ==  ==  ==  ==   권장진도율, 자기진도율 시작 ==  ==  ==  ==  == */
				if (box.getString("p_year").equals(""))
					box.put("p_year", box.getSession("s_year"));
				if (box.getString("p_subjseq").equals(""))
					box.put("p_subjseq", box.getSession("s_subjseq"));
				if (box.getString("p_subj").equals(""))
					box.put("p_subj", box.getSession("s_subj"));

				 SubjGongAdminBean sbean = new SubjGongAdminBean();
				 String promotion = sbean.getPromotion(box);
				 request.setAttribute("promotion", promotion);

				String progress = sbean.getProgress(box);
				request.setAttribute("progress", progress);
				
				/* ==  ==  ==  ==  ==   권장진도율, 자기진도율 끝  ==  ==  ==  ==  == */
			
				//s -출석부
				StudyCountBean bean2 = new StudyCountBean();
				ArrayList list6 = bean2.selectattend3(box);
	            request.setAttribute("attend2", list6);

				
				
				//e -출석부

				// 총차시, 학습한 차시, 진도율, 과정구분
				DataBox dbox = bean.getStudyChasi(box);
				int datecnt    = Integer.parseInt(dbox.getString("datecnt"));		// 총차시
				int edudatecnt = Integer.parseInt(dbox.getString("edudatecnt"));	// 학습한 차시
				double wstep = Double.parseDouble(dbox.getString("wstep"));			// 진도율
				
				String v_subjgu = dbox.getString("subjgu");							// 과정구분

				request.setAttribute("datecnt", String.valueOf(datecnt));
				request.setAttribute("edudatecnt", String.valueOf(edudatecnt));
				request.setAttribute("wstep", String.valueOf(wstep));
				request.setAttribute("subjgu", v_subjgu);
				
				if ("M".equals(v_subjgu)) {
					// 매월정기안전 현황
					DataBox dbox2 = bean.getCalcStudyMm(box);
					int totalchasi	= Integer.parseInt(dbox2.getString("totalchasi"));		// 총차시
					int studychasi	= Integer.parseInt(dbox2.getString("studychasi"));		// 총차시
					int studycount 	= Integer.parseInt(dbox2.getString("studycount"));	// 학습한 차시
					int studytime 	= Integer.parseInt(dbox2.getString("studytime"));	// 학습한 차시
					request.setAttribute("start_date", dbox2.getString("start_date"));
					request.setAttribute("end_date"  , dbox2.getString("end_date"));
					request.setAttribute("totalchasi", String.valueOf(totalchasi));
					request.setAttribute("studychasi", String.valueOf(studychasi));
					request.setAttribute("studycount", String.valueOf(studycount));
					request.setAttribute("studytime" , String.valueOf(studytime));
				}

				/* s : 과목설문 응시여부 */
				SulmunSubjUserBean sulbean = new SulmunSubjUserBean();
				int suldata = sulbean.getSulData(box);
				int sulresult = sulbean.getUserData(box);
				box.put("p_suldata",String.valueOf(suldata));
				box.put("p_sulresult",String.valueOf(sulresult));
				/* e : 과목설문 응시여부 */

				/* ==  ==  ==  ==  ==   컨텐츠평가 응시여부 ==  ==  ==  ==  == */
				// SulmunContentsUserBean contentsbean = new SulmunContentsUserBean();
				// 먼저 컨텐츠설문지 잇는지 확인...(2005.10.13)
				// int ispaper = contentsbean.getContentsSulmunPaper(box);
				// box.put("p_ispaper", String.valueOf(ispaper));
				// int contentsdata = contentsbean.getUserData(box);
				// box.put("p_contentsdata",String.valueOf(contentsdata));
				/* ==  ==  ==  ==  ==   컨텐츠평가 응시여부 ==  ==  ==  ==  == */

				/* s : 과제 출제개수 */
				ProjectAdminBean report = new ProjectAdminBean();
				int reportadmin = report.getAdminData(box);
				box.put("p_report",String.valueOf(reportadmin));
				/* e : 과제 출제개수 */

				/* s : 과제 제출여부 */
				ProjectAdminBean reportuser = new ProjectAdminBean();
				int reportdata = reportuser.getUserData(box);
				box.put("p_reportdata",String.valueOf(reportdata));
				/* e : 과제 제출여부 */
				
				/* 스콤2004에서 사용 */
				request.setAttribute("p_suldata", String.valueOf(suldata));
				request.setAttribute("p_sulresult", String.valueOf(sulresult));
				request.setAttribute("p_report", String.valueOf(reportadmin));
				request.setAttribute("p_reportdata", String.valueOf(reportdata));

				/* s : 평가 갯수 */
				ExamUserBean exambean = new ExamUserBean();
				ArrayList examdata = exambean.getUserData(box);
				request.setAttribute("ExamData", examdata);
				/* e : 평가 갯수 */

				/* s : 평가 응시여부 */
				ArrayList examresultdata = exambean.getUserResultData(box);
				request.setAttribute("ExamResultData", examresultdata);
				/* e : 평가 응시여부 */

				/* s : 평가 응시조건 */
				ArrayList examconditiondata = exambean.getApplyExamConditionData(box);
				request.setAttribute("ExamConditionData", examconditiondata);
				/* e : 평가 응시조건 */
			
			}

			/*
			} else { 
			// 베타테스트일때 과목진도보기
			    BetaMasterFormBean mfbean = new BetaMasterFormBean();
			    BetaMasterFormData  data = mfbean.SelectBetaMasterFormData(box);    // 마스터폼 정보
			    request.setAttribute("BetaMasterFormData", data);
			    BetaEduStartBean    bean = new BetaEduStartBean();
			
			    ArrayList       data1= null;
			    
			    // BetaEduScoreData  data2= bean.SelectEduScore(box);
			    // request.setAttribute("EduScore", data2);
			    
			    if ( data.getContenttype().equals("N") ) {                       // Normal MasterForm
			        data1= bean.SelectEduList(box);                           // 진도데이터
			    
			        v_url = "/beta/admin/z_BetaEduChk_List.jsp";
			    } else if ( data.getContenttype().equals("O") || data.getContenttype().equals("S") ) {          // OBC,SCORM MasterForm
			        data1= bean.SelectEduListOBC(box);                        // 진도데이터
			
			        v_url = "/beta/admin/z_BetaEduChk_List_SCORM.jsp";
			    }
			    request.setAttribute("EduList", data1);
			
			
			    /* ==  ==  ==  ==  ==   권장진도율, 자기진도율 시작 ==  ==  ==  ==  == */
			// SubjGongAdminBean sbean = new SubjGongAdminBean();
			// String promotion = sbean.getPromotion(box);
			// request.setAttribute("promotion", promotion);
			// String progress = sbean.getProgress(box);
			// request.setAttribute("progress", progress);
			/* ==  ==  ==  ==  ==   권장진도율, 자기진도율 끝  ==  ==  ==  ==  == */
			/*
			}
			*/

			System.out.println("v_url ==> "+v_url);
			System.out.println("v_url ==> "+v_url);
			System.out.println("v_url ==> "+v_url);
			System.out.println("v_url ==> "+v_url);
			System.out.println("v_url ==> "+v_url);
			System.out.println("v_url ==> "+v_url);
			
			ServletContext sc = getServletContext();
			RequestDispatcher rd = sc.getRequestDispatcher(v_url);
			rd.forward(request, response);
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("eduListPage()\r\n" + ex.getMessage());
		}
	}

	
	
	
	
	
	
	// 20100512 학습창 수정작업 @ Parks
	/**
	 * 학습창 상단 Frame
	 * 
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performFrameNewUpPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String v_url = "";
			EduStartBean bean = EduStartBean.getInstance();
			ArrayList lessonList = bean.SelectNewLessonList(box); // Lesson List
			request.setAttribute("NewLessonList", lessonList);
				
			v_url = "/learn/user/lcms/knise/z_EduStart_fup.jsp";

			forwardUrl( request, response, v_url );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("EduStart.performFrameNewUpPage()\r\n" + ex.getMessage());
		}
	}
	
	/**
	 * 학습창 메인 페이지 - 공지, Q&A 등 게시판 및 진도/목차 정보
	 *  
	 * @param request
	 * @param response
	 * @param box
	 * @param out
	 * @throws Exception
	 */
	public void performNewMainPage(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);
			
			String p_contenttype = box.getString("p_contenttype");
			String v_subjgu = box.getString("p_subjgubun");	// M: 매월정기안전교육, J: JIT
			String forwardUrl = "/learn/user/lcms/knise/z_EduStart_Main.jsp";
			
			setSummaryInfo(request, box);					// 요약정보
			setBBSInfo(request, box);						// 게시판정보
			setSulmunAndReportAndExamInfo(request, box);	// 설문, 과제, 평가정보

			double progress = Double.parseDouble( (String) request.getAttribute("progress") );
			setAllowStudyInfo(request, box, progress);		// 제약사항
			setContentTypeInfo(request, box);				// 컨텐츠별 예외사항

			forwardUrl( request, response, forwardUrl );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("EduStart.performNewMainPage()\r\n" + ex.getMessage());
		}
	}
	
		
	// 콘텐츠 가져오기
	public void performNewStudy(HttpServletRequest request, HttpServletResponse response, RequestBox box, PrintWriter out) throws Exception {
		try {
			request.setAttribute("requestbox", box);

			String forwardUrl = "";



			EduStartBean esb = EduStartBean.getInstance();
//System.out.println("@@@ box.getSession : "+box.getSession("s_subj")+"/"+box.getString("p_lesson"));			
			forwardUrl = esb.SelectLesson(box.getSession("s_subj"), box.getString("p_lesson"));
//System.out.println("@@@ forwardUrl : "+forwardUrl);
			forwardUrl( request, response, forwardUrl );
		} catch (Exception ex) {
			ErrorManager.getErrorStackTrace(ex, out);
			throw new Exception("EduStart.performNewMainPage()\r\n" + ex.getMessage());
		}
	}
}
