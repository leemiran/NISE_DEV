<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message" %>

<%@ include file="JSchListCtrl.java" %>
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
      JSchListCtrl listCtrl = new JSchListCtrl(out, session);

		listCtrl.init("schdulelist","","","","",0);

		// addHeader(String sText,int nWidth,String sIcon,String sAction,int bCheckBox,int bEdit,String id,String sCellIcon,int bNo,int nIdx)
		// the fifth arg 1 is means of checkbox
		//listCtrl.addHeader("",23,"","",1,0,"","",0,0);				// it means Number field when bNo is 1

		listCtrl.addHeader( "NO",32 , "", "",0,0,"","",1,0);				//The vector about schedule name means 0th (nIdx = 0)
		listCtrl.addHeader( "NAME" ,80, "", "",0,0,"","",0,0);
		listCtrl.addHeader( "PATH" ,300, "", "",0,0,"","",0,1);
		listCtrl.addHeader( "OUT FILE" ,90, "", "",0,0,"","",0,2);
		listCtrl.addHeader( "TYPE" ,40 , "", "",0,0,"","",0,3);
		listCtrl.addHeader( "MAIL" ,40 , "", "",0,0,"","",0,4);
		listCtrl.addHeader( "DATE",70, "", "",0,0,"","",0,5);
		listCtrl.addHeader( ".." ,40 , "", "",0,0,"","",0,6);
		listCtrl.addHeader( ".." ,40 , "", "",0,0,"","",0,7);

		listCtrl.setPageSize(40);
		listCtrl.setStripe(2);
		listCtrl.setStripeBgColor(0,"#F0F0D9");
		listCtrl.setStripeBgColor(1,"#ffffff");

		// default : #cccccc
		// listCtrl.setFocusColor("#ff0000");
		// listCtrl.setDebug(1);

		// default color : "#d4d0c8";
		listCtrl.setBtnFace("#B0C4DE");
		//listCtrl.setBtnFace("#E3F1FF");

		//listCtrl.setDebug(1);
		listCtrl.printObj();
	}
%>

</body>