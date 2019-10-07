<%
/**
 *  1. 제      목: 학습창 메인 Frame (Normal)
 *  2. 프로그램명 : z_EduStart_fset.jsp
 *  3. 개      요: 학습창 메인 Frame (Normal)
 *  4. 환      경: JDK 1.5
 *  5. 버      젼: 1.0
 *  6. 작      성: 박정수
 *  7. 수      정: 
 **/
%>
<%@ page contentType="text/html;charset=euc-kr"%>
<%@ page errorPage="/learn/library/error.jsp"%>
<%@ page import="com.ziaan.library.*"%>
<%@ page import="com.ziaan.lcms.*"%>
<jsp:useBean id="conf" class="com.ziaan.library.ConfigSet" scope="page" />
<%
	RequestBox box = null;
	box = (RequestBox) request.getAttribute("requestbox");
	if (box == null) {
		box = RequestManager.getBox(request);
	}

	String s_subj = box.getSession("s_subj");
	String s_year = box.getSession("s_year");
	String s_subjseq = box.getSession("s_subjseq");
	String s_eduauth= box.getSession("s_eduauth");

	String c_gadmin = box.getSession("gadmin");
	String v_sitegubun = box.getString("p_sitegubun");
	
	String v_review = box.getString("p_review"); 	   // 복습
	String v_studytypeb = box.getString("p_studytype");
	
	if (s_eduauth.equals("N") && !v_studytypeb.equals("betatest") ) {
%>
	<html>
	<head>
		<script type="text/javascript">
		function init() {
			alert("현재는 수료처리 중이라 강의실 입장이 불가합니다.");
			window.close();
		}
		</script>
	</head>
	<body onload="init();">
	</body>
	</html>
<%
	} else {

		MasterFormData da = (MasterFormData) request.getAttribute("MasterFormData");
	
		String v_subjnm = da.getSubjnm();
		String v_iscentered = da.getIscentered();
		int v_width = da.getWidth();
		int v_height = da.getHeight();
	
		String v_mftype = StringManager.shieldXSS( da.getMftype() );
		String v_contenttype = StringManager.shieldXSS(da.getContenttype() );
		String v_isMFMenuImg = StringManager.shieldXSS(da.getIsMFMenuImg() );
		String v_isMFBranch = StringManager.shieldXSS(da.getIsmfbranch() );
		String v_mfChat= StringManager.shieldXSS(da.getMFChat());
		String v_studytype = StringManager.shieldXSS(box.getString("p_studytype") );
	
		String v_base_url = "/servlet/controller.lcms.EduStart?";
	
		String v_url_up = v_base_url + "p_process=newfup";
		String v_url_gong = v_base_url + "p_process=newfmain&p_subj=" + s_subj + "&p_year=" + s_year + "&p_subjseq=" + s_subjseq;
	
		String v_spaceUrl = "";
		if (v_iscentered.equals("Y")) {
			String contentPath = conf.getProperty("dir.content.path");
			v_spaceUrl = contentPath + s_subj + "/docs/menuimg/space.html";
		}
%>
<html>
<head>
<title><%= v_subjnm %></title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<!-- CopyRights. ziaan.Corp.  written by LeeSuMin -->
</head>
<script type="text/javascript" src="/script/control.js"></script>
<script language="javascript">
	function resizeWindow() {
		w = <%=v_width%>;
		h = <%=v_height%>;
		var v_w = '', v_h='';
		
		if (w==99999)	v_w = screen.availWidth;
		else			v_w = w;
		if (h==99999)	v_h = screen.availHeight;
		else			v_h = h;
		
		if (parseInt(navigator.appVersion)>3) {
		   if(w==99999 && h==99999){
		   }   
		   else{
		      top.moveTo(0,0);
		   	  top.resizeTo(v_w, v_h);
		   }
		}
	}
	resizeWindow();
	
	// 강의창이 닫힐때 종료 처리
	window.onbeforeunload = function() {top.etop.setProgress();};
	
	
</script>


<%
		if (v_iscentered.equals("Y")) {
%>
<frameset rows="*,768,*" cols="*,1024,*" scrolling="NO" frameborder="NO" border="0" framespacing="0">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
<%
		}
%>
	<frameset rows="0,1,28,1*" cols="*" frameborder="NO" border="0" framespacing="0">
		<frame src="" name="ememo" scrolling="NO" frameborder="NO">
		<frame src="" name="echk" scrolling="NO" frameborder="NO">
		<frame src="<%=v_url_up%>" name="etop" scrolling="NO" frameborder="NO">
		<frame src="<%=v_url_gong%>" name="ebody" scrolling="AUTO" frameborder="NO">
	</frameset>
<%
		if (v_iscentered.equals("Y")) {
%>
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
	<frame src="<%=v_spaceUrl%>" scrolling="NO">
</frameset>
<%
		}
%>

<noframes>
<body bgcolor="#FFFFFF"></body>
</noframes>
</html>
<%
	}
%>