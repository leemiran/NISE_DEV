<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=EDGE" />
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/userCommonHead.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>

<!-- nonActive -->
<!--로컬 -->
<%-- 
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/crownix-viewer.min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/crownix-viewer.min.css"  />
--%>


<!--운영-->

<script type="text/javascript" src="http://27.101.217.158:8080/ReportingServer/html5/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="http://27.101.217.158:8080/ReportingServer/html5/js/crownix-viewer.min.js"></script>
<link rel="stylesheet" type="text/css" href="http://27.101.217.158:8080/ReportingServer/html5/css/crownix-viewer.min.css"  />

</head> 

<%
	String p_zz = (String)session.getAttribute("gadmin"); 
	System.out.println("############### " + p_zz);
	String zicIn = zicIn = "http://10.175.134.37/images/user/award1-2-1.gif";
	String fileName = fileName = "eduCerti2017.mrd";
	
	String p_scoreYn = (String)request.getAttribute("p_scoreYn");
	String p_emp_gubun = (String)request.getAttribute("p_emp_gubun");
	String p_upperclass = (String)request.getAttribute("p_upperclass");
	
	int i_p_schoolparent = request.getAttribute("p_schoolparent") !=null ? Integer.parseInt(request.getAttribute("p_schoolparent").toString()) : 0;

	//i_p_schoolparent 1:학부모가 알아야 할 특수교육개론, 2:가정에서의 장애학생 지원 방안, 3:학부모가 알아야할 치료지원 서비스
	//int i_p_schoolparent = Integer.parseInt(p_schoolparent);
	
	System.out.println("############### " + p_scoreYn);
	System.out.println("############### " + p_emp_gubun);
	System.out.println("############### i_p_schoolparent ---> " + i_p_schoolparent);
	
	
	zicIn = "http://10.175.134.37/images/user/award1-2-1.gif";
	
	
	/*
	//회원
	if(p_zz.equals("ZZ")){		
		//T:교원, R:교육 전문직(사립유치원 교원)
		if(p_emp_gubun.equals("T") || p_emp_gubun.equals("R")){
			fileName = "eduCerti1.mrd";
		}else{
			//E:보조인력,P:일반회원(학부모 등) 
			fileName = "eduCerti2.mrd";
		}
	}else{
		zicIn = "";
	//	p_scoreYn = "N";
		fileName = "eduCerti1.mrd";
	}
	*/

	
	/* //PAR:기타과정 , P:일반회원(학부모 등)
	if(("PAR".equals(p_upperclass)) && "P".equals(p_emp_gubun)){
		fileName = "eduCerti3.mrd";
	}
	//PAR:기타과정 , i_p_schoolparent : 과정명에 학부모가 있으면 
	if(("PAR".equals(p_upperclass)) && i_p_schoolparent > 0){
		fileName = "eduCerti4.mrd";
	} */
	
	//20160330
	//PAR:기타과정, SCP:학부모
	if("PAR".equals(p_upperclass) || "SCP".equals(p_upperclass)){
		fileName = "eduCerti2017.mrd";
	}
	
	System.out.println("############### p_upperclass " + p_upperclass);
	System.out.println("############### p_emp_gubun " + p_emp_gubun);
	
	System.out.println("############### " + fileName);
%>

<c:set var="rf" value="<%=fileName %>"/>
<c:set var="rv"></c:set>
<c:set var="rp">
	[<c:out value="${p_subj}"  escapeXml="false"/>] 
	[<c:out value="${p_year}"  escapeXml="false"/>] 
	[<c:out value="${p_subjseq}"  escapeXml="false"/>] 
	[<c:out value="${p_userid}"  escapeXml="false"/>] 
	[<%=zicIn %>]
	[<%=p_scoreYn %>]
	[<c:out value="${p_emp_gubun}"  escapeXml="false"/>]
</c:set>



<BODY leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#FFFFFF" style="background:none">
	<p id="report_programes"></p>
	<%-- <ui:report title="RD" file="${rf}" rv="${rv}" rp="${rp}" /> --%>
	
	
	<div id="crownix-viewer" style="position:absolute;width:100%;height:100%"></div>
	<script>

		window.onload = function(){
			//var viewer = new m2soft.crownix.Viewer('http://localhost:8283/ReportingServer/service', 'crownix-viewer');
			var viewer = new m2soft.crownix.Viewer('http://27.101.217.158:8080/ReportingServer/service.jsp', 'crownix-viewer');
			//viewer.openFile('http://localhost/mrd/${rf}', '/rp [<c:out value="${p_subj}"  escapeXml="false"/>] [<c:out value="${p_year}"  escapeXml="false"/>] [<c:out value="${p_subjseq}"  escapeXml="false"/>] [<c:out value="${p_userid}"  escapeXml="false"/>] [<%=zicIn %>] [<%=p_scoreYn %>]	[<c:out value="${p_emp_gubun}"  escapeXml="false"/>]');
			viewer.openFile('http://10.175.134.40:8080/DataServer/mrd/${rf}', '/rf [http://10.175.134.40:8080/DataServer/rdagent.jsp] /rsn [newknise] /rp [<c:out value="${p_subj}"  escapeXml="false"/>] [<c:out value="${p_year}"  escapeXml="false"/>] [<c:out value="${p_subjseq}"  escapeXml="false"/>] [<c:out value="${p_userid}"  escapeXml="false"/>] [<%=zicIn %>] [<%=p_scoreYn %>]	[<c:out value="${p_emp_gubun}"  escapeXml="false"/>]');
			//viewer.openFile('http://27.101.217.158:8080/DataServer/mrd/${rf}', '/rf [http://27.101.217.158:8080/DataServer/rdagent.jsp] /rsn [newknise] /rp [<c:out value="${p_subj}"  escapeXml="false"/>] [<c:out value="${p_year}"  escapeXml="false"/>] [<c:out value="${p_subjseq}"  escapeXml="false"/>] [<c:out value="${p_userid}"  escapeXml="false"/>] [<%=zicIn %>] [<%=p_scoreYn %>]	[<c:out value="${p_emp_gubun}"  escapeXml="false"/>]');
			

		};
	</script>
	
</BODY>


</html>
