<%
/**
 *  1. ��      ��: �н�â ��� Frame (Normal)
 *  2. ���α׷��� : z_EduStart_fset.jsp
 *  3. ��      ��: �н�â ��� Frame (Normal)
 *  4. ȯ      ��: JDK 1.5
 *  5. ��      ��: 1.0
 *  6. ��      ��: 
 *  7. ��      ��: 
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

	// ���� ���� ���� LESSON �߿� ���� ���� LESSON �� �����´�.
	String v_lesson = EduEtc1Bean.getStudyLesson(s_subj,s_year,s_subjseq,s_userid);
	
	String v_base_url = "/servlet/controller.lcms.EduStart";
	String v_eduChkURL = "/servlet/controller.lcms.EduProgress";	// ���� üũ URL

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
	
	var startval = "<%=v_lesson%>";	<%// ���� �н�â���� ���� �ϴ� ���� %>
	var studysec = 0;  				<%// ����â�� ���� �־��� �ʴ��� �ѽð�%>
	var progsec  = 0;  				<%// �������� ���� �־��� �ʴ��� �ѽð�%> 
	var savesec  = 0;  				<%// �������� ���� �н�â���� ������ �ѽð�%>

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
	
	<%// �귯���� �ð� ó�� %>
	function staytime() {
		studysec++;
		progsec++;

		<% // ����ó�� (�� 1�ʸ��� �����Ƿ� ���⼭ 60�� ���� �ð��� ���� ����ó���� �����Ѵ�)%>
		if ((progsec % 60) == 0) {
			setProgress('0');
		}
		
		setTimeout("staytime()", 1000);
	} 

	<%// ����Ʈ �ڽ� �ٲ������ ���� %>
	function goContents() {
		eduChk();
		// ���� ������ ������ �ش� ó���ϱ�
		setProgress();
		startval = document.getElementById("i_lesson").options[document.getElementById("i_lesson").options.selectedIndex].value;
		top.ebody.location = document.getElementById("i_lesson").options[document.getElementById("i_lesson").options.selectedIndex].url;
		<%//"<//v_base_url//>?p_process=NewStudy&p_lesson="+startval;%>
		setTimeout("setProgress()", 500);
	}


	//���ϼ�������üũ
	function eduChk(){
		//alert("eduChk = <%=v_eduChkURL%>");
		document.getElementById("i_lesson").options[document.getElementById("i_lesson").options.selectedIndex].value
		var  url = "<%=v_eduChkURL%>?p_process=eduCheck&p_lesson=" + startval;
		parent.echk.location = url;
		return;
	}
	
	
	<%// ���� ó�� %>
	function setProgress() {
		var frm = document.form1;	 	
	 	var url = "<%=v_eduChkURL%>";
		var sec = progsec;
		savesec += progsec;
		progsec = 0;

	 	var param = "p_process=eduNewCheck&p_lesson="+startval+"&p_sec="+sec;
		var ret = doRequest(url, param);

		if (ret == "true") {
			//alert("����ó�� ����");
		} else {
			progsec = progsec + sec;	<%// ����ó���� ���� �߻��ϸ� �ð��� ȯ���Ͽ� ���� ó���� ����ϵ��� �Ѵ�. %>
			//alert("���� ó���߿� ������ �߻��Ͽ����ϴ�.");
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
