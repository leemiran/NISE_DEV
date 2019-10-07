<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="JRptListCtrl.java" %>

<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java" %>
<%@ include file="../control/lib/JSListCtrl.jsp" %>
<%@ include file="../control/lib/JLListCtrl.java"%>


<%
	initArg(request,out);
%>




<body bgcolor="#FFFFFF" SCROLL ="no" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<%
	{	
		JRptListCtrl listCtrl = new JRptListCtrl(out);
		
		listCtrl.init("schdulelist","","","","",0);
		/*
		listCtrl.addHeader("",23,"","",1);
		listCtrl.addHeader("Number",50,"","");
		listCtrl.addHeader("MRD document name",460,"","");
		listCtrl.addHeader("The modified day of document",130,"","");
		listCtrl.addHeader("Size",97,"","");
		*/
		
		// addHeader(String sText,int nWidth,String sIcon,String sAction,int bCheckBox,int bEdit,String id,String sCellIcon,int bNo,int nIdx)
		// the fifth arg 1 is means of checkbox
		listCtrl.addHeader("",23,"","",1,0,"","",0,0);
		// it means Number field when bNo is 1
		listCtrl.addHeader(Message.get("mrdfilelist_04"),50,"","",0,0,"","",1,0);
		//The vector corresponded to schedlue name means 0th(nIdx = 0)
		listCtrl.addHeader(Message.get("mrdfilelist_05_1"),460,"","",0,0,"","",0,1);
		listCtrl.addHeader(Message.get("mrdfilelist_06"),130,"","",0,0,"","",0,2);
		listCtrl.addHeader(Message.get("mrdfilelist_07"),97,"","",0,0,"","",0,3);
		
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

