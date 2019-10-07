<%
/**
 *  1. 제      목: 학습창 상단 Frame (Normal)
 *  2. 프로그램명 : z_EduStart_fset.jsp
 *  3. 개      요: 학습창 상단 Frame (Normal)
 *  4. 환      경: JDK 1.5
 *  5. 버      젼: 1.0
 *  6. 작      성: 
 *  7. 수      정: 
 **/
%>
<%@ page contentType="text/html;charset=euc-kr"%>

<%@ page import="java.util.*"%>
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
	
	String s_userid = box.getSession("userid");
	String s_name = box.getSession("name");
	String s_gadmin = box.getSession("gadmin");
	String s_resno = box.getSession("resno");

	// 아직 듣지 않은 LESSON 중에 가장 작은 LESSON 을 가져온다.
	String v_lesson = EduEtc1Bean.getStudyLesson(s_subj,s_year,s_subjseq,s_userid);
	
	String v_base_url = "/servlet/controller.lcms.EduStart";
	String v_eduChkURL = "/servlet/controller.lcms.EduProgress";	// 진도 체크 URL

	List list = (ArrayList) request.getAttribute("NewLessonList");
	MfLessonData x = null;
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<!-- CopyRights. ziaan.Corp.  written by LeeSuMin -->
<script src="/script/prototype.js"></script>
<script language=javascript>
<!--
	
	var startval = "<%=v_lesson%>";	<%// 현재 학습창에서 들어야 하는 차시 %>
	var studysec = 0;  				<%// 강의창이 열려 있었던 초단위 총시간%>
	var progsec  = 0;  				<%// 현재차시 열려 있었던 초단위 총시간%> 
	var savesec  = 0;  				<%// 현재차시 현재 학습창에서 저장한 총시간%>

	function init() {	
		staytime();
		var obj = document.getElementById("i_lesson");
		for (var i=0;i<obj.length;i++) {
			if (obj.options[i].value == startval) {
				obj.options[i].selected = true;
				break;
			}
		}
	}
	
	<%// 흘러가는 시간 처리 %>
	function staytime() {
		studysec++;
		progsec++;

		<% // 진도처리 (매 1초마다 들어오므로 여기서 60초 마다 시간에 대한 진도처리를 수행한다)%>
		if ((progsec % 60) == 0) {
			setProgress('0');
		}
		
		setTimeout("staytime()", 1000);
	} 

	<%// 셀렉트 박스 바뀌었을때 동작 %>
	function goContents() {
		eduChk();
		// 보던 콘텐츠 있으면 해당 처리하기
		setProgress();
		startval = document.getElementById("i_lesson").options[document.getElementById("i_lesson").options.selectedIndex].value;
		top.ebody.location = document.getElementById("i_lesson").options[document.getElementById("i_lesson").options.selectedIndex].url;
		<%//"<//v_base_url//>?p_process=NewStudy&p_lesson="+startval;%>
		setTimeout("setProgress()", 500);
	}


	//일일수강진도체크
	function eduChk(){
		//alert("eduChk = <%=v_eduChkURL%>");
		document.getElementById("i_lesson").options[document.getElementById("i_lesson").options.selectedIndex].value
		var  url = "<%=v_eduChkURL%>?p_process=eduCheck&p_lesson=" + startval;
		parent.echk.location = url;
		return;
	}
	
	
	<%// 진도 처리 %>
	function setProgress() {
		var frm = document.form1;	 	
	 	var url = "<%=v_eduChkURL%>";
		var sec = progsec;
		savesec += progsec;
		progsec = 0;

	 	var param = "p_process=eduNewCheck&p_lesson="+startval+"&p_sec="+sec;
		var ret = doRequest(url, param);

		if (ret == "true") {
			//alert("진도처리 성공");
		} else {
			progsec = progsec + sec;	<%// 진도처리중 오류 발생하면 시간을 환원하여 다음 처리시 사용하도록 한다. %>
			//alert("진도 처리중에 오류가 발생하였습니다.");
		} 
	}

	function doRequest(u, a) {
		var xmlDoc;
		if (window.ActiveXObject) {
			xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
			xmlHttp = new ActiveXObject("MSXML2.XMLHTTP");
		}	else if (window.XMLHttpRequest) {
			xmlHttp = new XMLHttpRequest();
		}

		var url = u;
		xmlHttp.open("POST", url, false);
		xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=euc-kr");
		xmlHttp.send(a);
		return xmlHttp.responseText;
	}	
	
-->   
</script>
</head>

<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="javascript:init()">
<FORM name="sel">
<table border="0" cellpadding="0" cellspacing="0" width="1010" align="LEFT" valign="TOP" marginwidth="0" marginheight="0" leftmargin="0" topmargin="0">
	<TR>
		<TD height="30" align="CENTER" valign=middle>&#160; 
		<SELECT	name="p_lesson"	id="i_lesson" onChange="javascript:goContents();">
			<%
				for (int i = 0; i < list.size(); i++) {
					x = (MfLessonData) list.get(i);
			%>
			<option	value="<%=x.getLesson()%>" url="<%=x.getStarting()%>"><%=x.getLesson()%>-<%=x.getSdesc()%></option>
			<%
				}
			%>
		</SELECT>
		</TD>
	</TR>
</TABLE>
</FORM>
</body>
</html>
