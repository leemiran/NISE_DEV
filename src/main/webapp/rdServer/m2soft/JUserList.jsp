<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="JUserListCtrl.java" %>

<%@ include file="../control/lib/JLJsp.jsp"%>


<%
	initArg(request,out);
%>

<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java" %>
<%@ include file="../control/lib/JSListCtrl.jsp" %>
<%@ include file="../control/lib/JLListCtrl.java" %>


<body bgcolor="#FFFFFF" SCROLL ="no" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
	{	
		JUserListCtrl listCtrl = new JUserListCtrl(out);
		
		listCtrl.init("userlist","","","","",0);
		/*
		listCtrl.addHeader("��ȣ",50,"","");
		listCtrl.addHeader("MRD �������",316,"","");
		listCtrl.addHeader("���� ������",150,"","");
		listCtrl.addHeader("ũ��",70,"","");
		listCtrl.addHeader("����",33,"","");
		*/
		
		// addHeader(String sText,int nWidth,String sIcon,String sAction,int bCheckBox,int bEdit,String id,String sCellIcon,int bNo,int nIdx)
		// 5��° arg 1 �� checkbox��� �ǹ�
		//listCtrl.addHeader("",23,"","",1,0,"","",0,0);
		// bNo �� 1�̶�� ���� Number field��� �ǹ�
		listCtrl.addHeader(Message.get("useradd_07"),50,"","",0,0,"","",1,0);
		//�����ٸ� ���� vector�� 0��°��� �ǹ� (nIdx = 0)
		listCtrl.addHeader(Message.get("useradd_02"),150,"","",0,0,"","",0,0);
		listCtrl.addHeader(Message.get("useradd_03"),150,"","",0,0,"","",0,1);
		listCtrl.addHeader(Message.get("useradd_06"),150,"","",0,0,"","",0,2);
		
		listCtrl.setPageSize(20);
		listCtrl.setStripe(2);
		listCtrl.setStripeBgColor(0,"#F0F0D9");
		listCtrl.setStripeBgColor(1,"#ffffff");
		
		// default : #cccccc
		// listCtrl.setFocusColor("#ff0000");
		// listCtrl.setDebug(1);
		
		// default color : "#d4d0c8";
		listCtrl.setBtnFace("#B0C4DE");
		//listCtrl.setBtnFace("#E3F1FF");
		
		listCtrl.printObj();
	}
%>

