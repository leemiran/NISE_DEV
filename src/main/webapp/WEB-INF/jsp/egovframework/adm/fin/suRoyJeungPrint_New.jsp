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
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/userCommonHead.jsp" %>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
</head> 

<%
	String p_zz = (String)session.getAttribute("gadmin"); 
	System.out.println("############### " + p_zz);
	String zicIn = "";
	String fileName = "";
	
	String p_scoreYn = (String)request.getAttribute("p_scoreYn");
	String p_emp_gubun = (String)request.getAttribute("p_emp_gubun");
	String p_upperclass = (String)request.getAttribute("p_upperclass");
	
	int i_p_schoolparent = request.getAttribute("p_schoolparent") !=null ? Integer.parseInt(request.getAttribute("p_schoolparent").toString()) : 0;

	//i_p_schoolparent 1:학부모가 알아야 할 특수교육개론, 2:가정에서의 장애학생 지원 방안, 3:학부모가 알아야할 치료지원 서비스
	//int i_p_schoolparent = Integer.parseInt(p_schoolparent);
	
	System.out.println("############### " + p_scoreYn);
	System.out.println("############### " + p_emp_gubun);
	System.out.println("############### i_p_schoolparent ---> " + i_p_schoolparent);
	
	
	//회원
	if(p_zz.equals("ZZ")){
		zicIn = "http://iedu.nise.go.kr/images/user/award1-2-1.gif";
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
		fileName = "eduCerti3.mrd";
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
	<ui:report title="RD" file="${rf}" rv="${rv}" rp="${rp}" />
</BODY>


<script language="javascript">
$(window).load(function(){
	if(Rdviewer.ReportDone != true)
	{
		alert("수료증 출력 프로그램이 설치되어 있지 않습니다.\n수료증 출력  프로그램을 다운로드 하신 후 설치 해 주세요!");
		var tmp = "<br>";
		tmp += '<table summary="수료증 출력 프로그램 다운로드" border="1" width="100%">';
		tmp += '	<caption>수료증 출력 프로그램</caption>';
		tmp += '	<colgroup>';
		tmp += '		<col width="50%"/>';
		tmp += '		<col width="50%" />';
		tmp += '	</colgroup>';
		tmp += '	<tbody>';
		tmp += '	<tr>';
		tmp += '		<th><img src="/images/m2soft.gif" alt="M2Soft" /></br><strong>Report Designer View 5.0</strong></th>';
		tmp += '		<td align="center">수료증 출력 프로그램&nbsp;&nbsp;<a href="/rdocx50_pro/rdocx50_pro.exe" target="_blank"><img src="/images/btn_tdown.gif" alt="다운로드" align="absmiddle"/></a></td>';
		tmp += '	</tr>';
		tmp += '	</tbody>';
		tmp += '</table>';

		document.getElementById("report_programes").innerHTML = tmp;
				
	}
});

</script>
</html>