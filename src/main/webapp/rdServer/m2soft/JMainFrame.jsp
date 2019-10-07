<%@page language="java" import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File ,m2soft.rdsystem.server.core.install.Message, m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.rddbagent.*" %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>

<%@ include file="../control/lib/JSMenuBar.jsp"%>
<%@ include file="../control/lib/JLMenuBar.java"%>

<%@ include file="../control/lib/JSToolBar.jsp"%>
<%@ include file="../control/lib/JLToolBar.java"%>

<%@ include file="../control/lib/JSSplitter.jsp"%>
<%@ include file="../control/lib/JLSplitter.java"%>

<%@ include file="../control/lib/JSFrame.jsp"%>
<%@ include file="../control/lib/JLFrame.java"%>

<%@ include file="../control/lib/JSMultiFrame.jsp"%>
<%@ include file="../control/lib/JLMultiFrame.java"%>

<%@ include file="../control/lib/JSContextMenu.jsp"%>
<%@ include file="../control/lib/JLContextMenu.java"%>

<%@ include file="../control/lib/JSPopupMenu.jsp"%>
<%@ include file="../control/lib/JLPopupMenu.java"%>
<%
response.setHeader("Pragma","No-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0);
response.setHeader ("Cache-Control", "no-cache");
	initArg(request,out);

	JLJsp jsp = new JLJsp();

   String rdserver_dir = RDUtil.getRDSERVER_DIR();
   String context = request.getRequestURI();
   context = context.substring(0, context.indexOf("/m2soft/JMainFrame.jsp"));

   if(!context.startsWith("/"))
      context = "/" + context;

   jsp.setClassImgPath(context + "/control/img");
   jsp.setPath(context + "/m2soft");
   jsp.setClassPath(context + "/control/lib");

   jsp.setClassHome(rdserver_dir + "/control/lib");
   jsp.setHome(rdserver_dir + "/m2soft");

   jsp.setCharSet(Message.get("charset"));

%>
<html>
<head>
<style>
<%@ include file="icis.css" %>
</style>

<script language="JavaScript">

	function initInstance()
	{
		window.name = "_mainFrame";
	}
	initInstance();
</script>
</head>
<title><%=Message.get("main_01")%></title>
<STYLE> BODY {border: 0px black solid;border-top: none;} </STYLE>
<form name=fthis>
<input name=id value="" type=hidden>
</form>
<body bgcolor="#FFFFFF" SCROLL ="no" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" >


<script>
	function onaddjob()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JScheduleAdd";
			var sSrc = "JScheduleAdd.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function onregisterdoc()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JDocAdd";
			var sSrc = "JDocAdd.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function onviewcompletedoc()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JCompleteReport";
			var sSrc = "JCompleteReport.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function onviewstatus()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JSchStatus";
			var sSrc = "JSchStatus.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function oncontrolservice()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JServiceControl";
			var sSrc = "JServiceControl.jsp?cmd=start";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function oncontroladdress()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JManageAddress";
			var sSrc = "JManageAddress.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function oncontrolsctable(opcode)
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JSchTable";
			if(opcode == 1){
			var sSrc = "JSchTable.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			}else{
			   delTable();
			}
		}
	}

	function delTable(){
      var ret = confirm(<%=Message.get("menu_02")%>);
      if(ret) {
         var wMainFrame = getAncestor(window,"_mainFrame");
         if(wMainFrame != null)
         {
            var sFrame = "JSchTable";
            var sSrc = "JSchTable.jsp?delete=yes";
            wMainFrame._openFrame("rightframe",sFrame,sSrc);
         }
      }
  }


	function oninitjob()
	{
	}

	function onconfig()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JEnvSetup";
         var sSrc = "JEnvSetup.jsp?rdEncStatus=true";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function onupdate()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JUpdate";
			var sSrc = "JUpdateMain.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}


	function onrptengine()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var sSrc = "JRptEngine.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}



	function oncontrolmoniter()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(0);
				}
			}
		}
	}


	function oncontroljdbc()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(1);
				}
			}
		}
	}


	function oncontrolxml()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(2);
				}
			}
		}
	}

	function oncontrolcache()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(2);
				}
			}
		}
	}

   function oncontrolsecurity()
   {
      var wMainFrame = getAncestor(window,"_mainFrame");
      if (wMainFrame != null)
      {
         var sFrame = "JRptEngine";
         var oFrame = JLFrame_getFrame(sFrame);

         var sSrc = "JRptEngine.jsp?id=100";
         wMainFrame._openFrame("rightframe",sFrame,sSrc);
         if(oFrame != null)
         {
            if (oFrame.focusPage != null)
            {
               oFrame.focusPage(3);
            }
         }
      }
   }


	function onconfigWASpool()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(3);
				}
			}
		}
   }


	function onconfigcharacter()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(4);
				}
			}
		}
	}

	function onconfigsecurity()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JRptEngine";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JRptEngine.jsp?id=100";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(5);
				}
			}
		}
	}

	function onmanagerpt()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");

		if (wMainFrame != null)
		{
			var sFrame = "JManageReport";
			var sSrc = "JManageReport.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			setVisible("rightframe","JManageReport");
		}
	}
	function onadddocbox()
	{
	}
	function onviewworkingdoc()
	{
	}
	function oncontrolhistory()
	{
	}
	function onconfigerrlog()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JLogConfig";
			var oFrame = JLFrame_getFrame(sFrame);

         var sSrc = "JLogConfig.jsp?id=100&logEncStatus=true";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(0);
				}
			}
		}
	}

	function onconfigrptlog()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JLogConfig";
			var oFrame = JLFrame_getFrame(sFrame);

			var sSrc = "JLogConfig.jsp?id=200";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			if(oFrame != null)
			{
				if (oFrame.focusPage != null)
				{
				   oFrame.focusPage(1);
				}
			}

		}
	}
	function onviewerrlog()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JManageLog";
			var sSrc = "JLogErrview.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}

	function onviewrptlog()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JManageLog";
			var sSrc = "JLogview.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}

	}

	function onviewsclog()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JManageLog";
			var sSrc = "JLogScheduleview.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}

	}

	function onadduser()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JUserAdd";
			var sSrc = "JUserAdd.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}
	function onallowauthority()
	{
	}

	function onviewuser()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JUserListHome";
			var sSrc = "JUserListHome.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
	}
	function onlogout()
	{
		location.href="JLogout.jsp";
	}
	function onviewsample()
	{
	}




</script>

		<script language="JavaScript">

		<!--
		function MM_reloadPage(init) {  //reloads the window if Nav4 resized
		if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
			document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
		else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
		}
		MM_reloadPage(true);
		// -->

		function MM_findObj(n, d) { //v4.0
		var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
			d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
		if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
		for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
		if(!x && document.getElementById) x=document.getElementById(n); return x;
		}

		function MM_showHideLayers() { //v3.0
		var i,p,v,obj,args=MM_showHideLayers.arguments;
		for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
			if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
			obj.visibility=v; }
		}

		//-->
		</script>


<%
	JLFrame frame = new JLFrame(out);
	frame.init(null,"_mainFrame",-1,-1,-1,-1,-1,-1);
	{
		JLSplitter split = new JLSplitter(out);
		JLMultiFrame rightFrame = new JLMultiFrame(out);
		JLFrame leftTop = new JLFrame(out);
		leftTop.init(split,"leftTop",-1,-1,-1,-1,-1,-1);
		leftTop.setSrc("JMainFolder.jsp");

		split.init(frame,"split",0,0,-1,-1,0,0,2,1);
      split.setSplitPosition(0,0,220,-1,0);
		split.setChild(0,0,leftTop);
		split.setChild(1,0,rightFrame);
		rightFrame.init(split,"rightframe",-1,-1,-1,-1,-1,-1);
		rightFrame.setHtml("<div id=elFramePos></div>");
	}
	{

		JLToolBar toolBar = new JLToolBar(out);
		toolBar.init("toolbar");
		toolBar.setPosition(0,0,-1,-1,-1,-1);
		{
			JLMenuBar menuBar = new JLMenuBar(out);
			int nHeight=20;
			menuBar.init("mbMain",-1,25+25);

			/*
			menuBar.addMenu("miHome",80,"Scheduling Management","window.openMainHome()","images/button_icon_watch.gif","schMngr",0,false);
			menuBar.addMenu("miJPCmCampPopup",90,"Reporting Engine Management","window.onrptengine();","images/beide.gif","JPCmCampPopup",2,false);
			menuBar.addMenu("miJPCmTpltPopup",90,"Reporting Document Management","window.onmanagerpt();","images/report.gif","JPCmTpltPopup",2,false);
			menuBar.addMenu("miJPCmTargetPopup",50,"Log Management","window.onconfigerrlog()","images/log.gif","JPCmTargetPopup",1,false);
			menuBar.addMenu("miJPCmSvyPopup",90,"View Reporting Sample","window.onviewsample();","images/svy20.gif","",2,true);
			menuBar.addMenu("miJPCmAdmin",60,"User Management","","images/itarget.gif","adminMenu",2,true);
			menuBar.addMenu("miLogout",50,"Log Out","window.onlogout();","images/logout.gif","",0,true);
			*/

			//Reporting Engine Management

         if(sessionGroup.equals(ADMINGROUP)){
   			menuBar.addMenu("miJPCmCampPopup",90,Message.get("jmainframe_02"),"window.onrptengine();","images/beide.gif","",2,true);
   						//Scheduling
   			if(AgentProcess.getScheduleServer() != null)
   				menuBar.addMenu("miHome",80,Message.get("jmainframe_01"),"window.openMainHome()","images/button_icon_watch.gif","",0,true);

   			//Reporting Document Management
   			//menuBar.addMenu("miJPCmTpltPopup",90,Message.get("jmainframe_03"),"window.onmanagerpt();","images/report.gif","",2,true);
   			//Log Management
   			if(language.equals("english"))
   				menuBar.addMenu("miJPCmTargetPopup",90,Message.get("jmainframe_04"),"window.onconfigerrlog()","images/log.gif","",1,true);
   			else
   				menuBar.addMenu("miJPCmTargetPopup",50,Message.get("jmainframe_04"),"window.onconfigerrlog()","images/log.gif","",1,true);
   			//Envrionment Configuration
   		   	menuBar.addMenu("miJPScSetupPopup",70,Message.get("jmainframe_05"),"window.onconfig()","images/admin16.gif","",0,true);
   		   
				// Finish update kokim 2005.09.08 				//menuBar.addMenu("miJPScUpdatePopup",50,Message.get("jmainframe_05_1"),"window.onupdate()","images/button_icon_arrowup.gif","",0,true);

		   }
			//View Reporting Sample
			//menuBar.addMenu("miJPCmSvyPopup",90,Message.get("jmainframe_06"),"window.onviewsample();","images/svy20.gif","",2,true);
			//User Management
			//menuBar.addMenu("miJPCmAdmin",60,Message.get("jmainframe_07"),"","images/itarget.gif","",2,true); 
			//Log Out
		        menuBar.addMenu("miLogout",90,Message.get("jmainframe_08")+" ("+sessionUser+")","window.onlogout();","images/logout.gif","",0,true); 

			toolBar.addBar("mbMain",menuBar,-1,-1,-1,25);
		}
		toolBar.printObj(0,0);
		frame.addToolBar(toolBar);

		/*
		JLToolBar toolBar2 = new JLToolBar(out);
		toolBar2.init("toolbar2");
		toolBar2.printObj(0,0);
		frame.addToolBar(toolBar2);
		*/
	}
	frame.printObj(0,0);

	if (false)
	{
		{
			JLPopupMenu popupMenu = new JLPopupMenu(out);
			popupMenu.init("schMngr",200,80);
			popupMenu.addMenuItem("onshowlist()","Schedule List","images/button_icon_new.gif");
			popupMenu.addMenuItem("onaddjob()","Add Work","images/scheduler.gif");
			popupMenu.addMenuItem("onregisterdoc()","Register Document","images/scheduler.gif");
			popupMenu.addMenuItem("onviewcompletedoc()","View document after finishing work","images/scheduler.gif");
			popupMenu.addMenuItem("oncontrolservice()","Service Control","images/scheduler.gif");
			popupMenu.addMenuItem("oncontroladdress()","Manage according to address","images/scheduler.gif");
			popupMenu.addMenuItem("oninitjob()","Work Initialized","images/scheduler.gif");
			popupMenu.addMenuItem("onconfig()","Envrionment Configuration","images/scheduler.gif");
			popupMenu.printObj(0,0);
		}

		{
			JLPopupMenu popupMenu = new JLPopupMenu(out);
			popupMenu.init("JPCmCampPopup",200,90);
			popupMenu.addMenuItem("oncontrolmoniter()","Mornitoring","images/ok.gif");
			popupMenu.addMenuItem("oncontroljdbc()","JDBC(Service)","images/db_folder1.gif");
			//popupMenu.addMenuItem("oncontrolcache()","Cache function setting","images/beide.gif"); 
			//popupMenu.addMenuItem("onconfigWASpool()","WAS Pool Usage Setting","images/beide.gif");
			//popupMenu.addMenuItem("onconfigcharacter()","Basic Character Set Setting","images/beide.gif");
			//popupMenu.addMenuItem("onconfigsecurity()","Security Setting","images/beide.gif");
			popupMenu.printObj(0,0);
		}

		{
			JLPopupMenu popupMenu = new JLPopupMenu(out);
			popupMenu.init("JPCmTpltPopup",200,90);
			popupMenu.addMenuItem("onadddocbox()","Add Documents","images/report.gif");
			popupMenu.addMenuItem("onviewworkingdoc()","View the working document","images/report.gif");
			popupMenu.addMenuItem("oncontrolhistory()","History Management","images/report.gif");
			popupMenu.printObj(0,0);
		}

		{
			JLPopupMenu popupMenu = new JLPopupMenu(out);
			popupMenu.init("JPCmTargetPopup",200,50);
         popupMenu.addMenuItem("onconfigerrlog()","Error Log Setting","images/button_icon_search.gif.gif");
			popupMenu.addMenuItem("onconfigrptlog()","Reporting Log Setting ","images/log.gif");
         popupMenu.addMenuItem("onviewrptlog()","View Report Log","images/button_icon_search.gif.gif");
			popupMenu.printObj(0,0);
		}

		{
			JLPopupMenu popupMenu = new JLPopupMenu(out);
			popupMenu.init("adminMenu",200,60);
			popupMenu.addMenuItem("onadduser()","Add User","images/grp.gif");
			popupMenu.addMenuItem("onallowauthority()","grant user authority","images/grp.gif");
			popupMenu.addMenuItem("onviewuser()","View registered user","images/grp.gif");
			popupMenu.printObj(0,0);
		}
	}
%>

<script language="javascript">

	function _onMenuEx(id)
	{
		if (id == "miHome")
		{
			MM_showHideLayers('Layer1','','show',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}
		if (id == "miJPScSetupPopup")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','hide',
				'Layer6','','hide',
				'Layer7','','hide',
            'Layer8','','show',
				'menu01_1','','show',
				'menu01_2','','hide',
				'menu02_1','','hide',
				'menu02_2','','show',
				'menu03_1','','show',
				'menu03_2','','hide',
				'menu04_1','','show',
				'menu04_2','','hide',
				'menu05_1','','show',
				'menu05_2','','hide',
				'menu06_1','','show',
				'menu06_2','','hide',
				'menu07_1','','show',
				'menu07_2','','hide',
				'menu08_1','','show',
				'menu08_2','','hide');
		}
		if (id == "miJPCmCampPopup")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','show',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}
		if (id == "miJPCmTpltPopup")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','show',
				'Layer4','','hide',
				'Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}

		if (id == "miJPCmTargetPopup")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','show',
				'Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}

		if (id == "miJPCmSvyPopup")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','show','Layer6','','hide','Layer7','','hide','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}

		if (id == "miJPCmAdmin")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','hide','Layer6','','show','Layer7','','hide','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}

		if (id == "miJPScUpdatePopup")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','hide','Layer6','','hide','Layer7','','show','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}

		if (id == "miLogout")
		{
			MM_showHideLayers('Layer1','','hide',
				'Layer2','','hide',
				'Layer3','','hide',
				'Layer4','','hide',
				'Layer5','','hide','Layer6','','hide','Layer7','','show','Layer8','','hide','menu01_1','','show','menu01_2','','hide','menu02_1','','hide','menu02_2','','show','menu03_1','','show','menu03_2','','hide','menu04_1','','show','menu04_2','','hide','menu05_1','','show','menu05_2','','hide','menu06_1','','show','menu06_2','','hide','menu07_1','','show','menu07_2','','hide','menu08_1','','show','menu08_2','','hide');
		}
	}


	function _drawEdge()
	{
		var nTop = 29;
		drawLine(window,"rightEdge",document.body.clientWidth-1,nTop,1,document.body.clientHeight-nTop,"#ffffff");
		var nLeft = Number(_getValue("split0","left"))+5;
		// drawLine(window,"topEdge",nLeft,nTop,document.body.clientWidth-nLeft-1,1,"#777777");
		drawLine(window,"bottomEdge",nLeft,document.body.clientHeight-1,document.body.clientWidth-nLeft-1,1,"#ffffff");
	}

	function _setSize()
	{
		var nTop = 0;
		var nLeft = 0;
		var nWidth = document.body.clientWidth;
		var nHeight = document.body.clientHeight;
		JLFrame_setSize("_mainFrame",nLeft,nTop,nWidth,nHeight);
	}

	function _initInstance()
	{
		window.onresize = _setSize;
		_setSize();
	}
	_drawEdge();
	_initInstance();

	function openMainHome()
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		// alert(wMainFrame);
		if (wMainFrame != null)
		{
			var sFrame = "JMainHome";
			var sSrc = "JMainHome.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
			setVisible("rightframe","JMainHome");
		}
	}

	onrptengine(); //default rightframe
</script>

</body>




<div id="elMenu2" style="POSITION: absolute; top:28px; left:10px;">

	<!--  ############## The begining of Scheduling Management ##############  -->
	<div id="Layer1" style="Z-INDEX:1; LEFT:126px; VISIBILITY:hidden; WIDTH:827px; POSITION:absolute; TOP:2px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','show','Layer2','','hide','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')" onMouseOut="MM_showHideLayers('Layer1','','show','Layer2','','hide','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')" class="sub">
      <table border="0" cellspacing="0" cellpadding="0" height="20" bgcolor="#122071">
			<tr>

				<td>
					<table width="197" border="0" cellspacing="1" cellpadding="0">

						<tr style="cursor: hand;">
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="window.openMainHome()" height="20" width="110" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_new.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_09")%></font></td></tr></table>
						</td>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onaddjob()" height="20" width="110" valign="top" bgcolor="#ffffff">
							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_add.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_10")%></font></td></tr></table>
						</td>
						<% if(language.equals("english")) {%>
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onregisterdoc()" height="20" width="150" valign="top" bgcolor="#ffffff">
								<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/report.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_11")%></font></td></tr></table>
							</td>
						<% } else { %>
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onregisterdoc()" height="20" width="120" valign="top" bgcolor="#ffffff">
                        <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/report.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("mrdfilelist_05")%></font></td></tr></table>
							</td>
						<% } %>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewcompletedoc()" height="20" width="150" valign="top" bgcolor="#ffffff">
							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_bigwindow.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_12")%></font></td></tr></table>
						</td>
						<% if(language.equals("english")) {%>
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewstatus()" height="20" width="150" valign="top" bgcolor="#ffffff">
								<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/ok.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_14")%></font></td></tr></table>
							</td>
						<% } else { %>
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewstatus()" height="20" width="110" valign="top" bgcolor="#ffffff">
								<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/ok.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_14")%></font></td></tr></table>
							</td>
						<% } %>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolservice()" height="20" width="110" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_com.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_13")%></font></td></tr></table>
						</td>


						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	<!--  @@@@@@@@@@@@@@ The End of Scheduling Management  @@@@@@@@@@@@@@  -->

	<!--  ############## The begining of Report Engine Management ##############  -->
<div id="Layer2" style="Z-INDEX:2; LEFT:0px; VISIBILITY:hidden; WIDTH:827px; POSITION:absolute; TOP:2px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','hide','Layer2','','show','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')" onMouseOut="MM_showHideLayers('Layer1','','hide','Layer2','','show','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')">
      <table border="0" cellspacing="0" cellpadding="0" height="20" bgcolor="#122071">
         <tr>
            <td>
               <table width="197" border="0" cellspacing="1" cellpadding="0">
                  <tr style="cursor: hand;">
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolmoniter()" height="20" width="120" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/ok.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt" id='JPCmCampPopup_fnt_16'><%=Message.get("jmainframe_16")%></font></td></tr></table>
                  </td>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontroljdbc()" height="20" width="120" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/db_folder1.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt " id='JPCmCampPopup_fnt_17'><%=Message.get("jmainframe_17")%></font></td></tr></table>
                  </td>
						<% if(isNongHyup) { %>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolxml()" height="20" width="120" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/icon_img_del.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt " id='JPCmCampPopup_fnt_17'>XML</font></td></tr></table>
                  </td>
						<% } %>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolsecurity()" height="20" width="120" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/targetfolder.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt " id='JPCmCampPopup_fnt_015'><%=Message.get("Security15")%></font></td></tr></table>
                  </td>
                  </tr>
               </table>

            </td>
         </tr>
      </table>
   </div>

	<!--  @@@@@@@@@@@@@@ The End of Report Engine Management @@@@@@@@@@@@@@  -->

	<!--  ############## The begining of Report Document Management ##############  
	<div id="Layer3" style="Z-INDEX:3; LEFT:30px; VISIBILITY:hidden; WIDTH:827px; POSITION:absolute; TOP:0px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','show','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')" onMouseOut="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','show','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')">
      <table border="0" cellspacing="0" cellpadding="0" height="20" bgcolor="#122071">
			<tr>
				<td>
					<table width="197" border="0" cellspacing="1" cellpadding="0">
						<tr style="cursor: hand;">
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onadddocbox()" height="20" width="150" valign="top" bgcolor="#ffffff">
							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/report.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_18")%></font></td></tr></table>
						</td>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewworkingdoc()" height="20" width="150" valign="top" bgcolor="#ffffff">
							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/report.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_19")%></font></td></tr></table>
						</td>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolhistory()" height="20" width="150" valign="top" bgcolor="#ffffff">
							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/report.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_20")%></font></td></tr></table>
						</td>
						</tr>
					</table>

				</td>
			</tr>
		</table>
	</div> -->
	<!--  @@@@@@@@@@@@@@ The End of Report Document Management @@@@@@@@@@@@@@  -->

	<!--  ############## The begining of Log Management ##############  -->
	<% if(AgentProcess.getScheduleServer() != null) {%>
		<div id="Layer4" style="Z-INDEX:4; LEFT:262px; VISIBILITY:hidden; WIDTH:827px; POSITION:absolute; TOP:2px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','show','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')" onMouseOut="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','show','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')">
	<% } else { %>
   	<div id="Layer4" style="Z-INDEX:4; LEFT:132px; VISIBILITY:hidden; WIDTH:827px; POSITION:absolute; TOP:2px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','show','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')" onMouseOut="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','show','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','hide')">
   <% } %>

      <table border="0" cellspacing="0" cellpadding="0" height="20" bgcolor="#122071">
			<tr>
				<td>
					<table  border="0" cellspacing="1" cellpadding="0">
						<tr style="cursor: hand;">
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onconfigerrlog()" height="20" width="120" valign="top" bgcolor="#ffffff">
							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/log.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_21")%></font></td></tr></table>
						</td>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewerrlog()" height="20" width="120" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_search.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_22")%></font></td></tr></table>
						</td>
						<% if(language.equals("english")) {%>
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onconfigrptlog()" height="20" width="130" valign="top" bgcolor="#ffffff">
								<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/log.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_23")%></font></td></tr></table>
							</td>
						<% } else { %>
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onconfigrptlog()" height="20" width="120" valign="top" bgcolor="#ffffff">
								<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/log.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_23")%></font></td></tr></table>
							</td>
						<% } %>
                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewrptlog()" height="20" width="120" valign="top" bgcolor="#ffffff">
                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_search.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_24")%></font></td></tr></table>
						</td>
						<%
						if(AgentProcess.getScheduleServer() != null)
						{
						%>
	                  <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onviewsclog()" height="20" width="120" valign="top" bgcolor="#ffffff">
	                     <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/button_icon_search.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_24_1")%></font></td></tr></table>
							</td>
						<%
						}
						%>
						</tr>
					</table>

				</td>
			</tr>
		</table>
	</div>


	<!--  ############## The begining of Logout ##############  
	<div id="Layer7" style="Z-INDEX:7; LEFT:30px; VISIBILITY:hidden; WIDTH:827px; POSITION:absolute; TOP:0px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','show','Layer8','','hide')" onMouseOut="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','show','Layer8','','hide')">
		<table border="0" cellspacing="0" cellpadding="0" height="20">
			<tr>
				<td>
					<table width="197" border="0" cellspacing="0" cellpadding="0">
					</table>

				</td>
			</tr>
		</table>
	</div>	-->
	<!--  @@@@@@@@@@@@@@ The End of Logout @@@@@@@@@@@@@@  -->


   <!--  ############## The begining of Scheduling Envrionment Setting ##############  -->
   <%
      int leftsize = 359;
      if(AgentProcess.getScheduleServer() == null)
		   leftsize -= 130;
		if(language.equals("english"))
			leftsize -= 59;
   %>
		<div id="Layer8" style="Z-INDEX:8; LEFT:<%=leftsize%>px; VISIBILITY:hidden; WIDTH:800px; POSITION:absolute; TOP:2px; HEIGHT:19px" onMouseOver="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','show')" onMouseOut="MM_showHideLayers('Layer1','','hide','Layer2','','hide','Layer3','','hide','Layer4','','hide','Layer5','','hide','Layer6','','hide','Layer7','','hide','Layer8','','show')">

      <table border="0" cellspacing="0" cellpadding="0" height="20" bgcolor="#122071">
   		<tr>
   			<td>
   				<table width="500" border="0" cellspacing="1" cellpadding="0">
   					<tr style="cursor: hand;">
                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:onconfig()" height="20" width="85" valign="top" bgcolor="#ffffff">
   							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/menu_icon_admin.gif'></td><td width=110><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_28")%></font></td></tr></table>
   						</td>
<!--                     <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontroladdress()" height="20" width="70" valign="top" bgcolor="#ffffff">
                        <table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/grp.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_29")%></font></td></tr></table>
   						</td>
-->
							<% if(language.equals("english")) {%>
                        <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolsctable(1)" height="20" width="180" valign="top" bgcolor="#ffffff">
	   							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/icon_db.gif'></td><td width=180><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_30")%></font></td></tr></table>
	   						</td>
                        <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolsctable(2)" height="20" width="220" valign="top" bgcolor="#ffffff">
	   							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/icon_db.gif'></td><td width=220><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_31")%></font></td></tr></table>
	   						</td>
	   					<% } else { %>
                        <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolsctable(1)" height="20" width="110" valign="top" bgcolor="#ffffff">
	   							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/icon_db.gif'></td><td width=150><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_30")%></font></td></tr></table>
	   						</td>
                        <td onMouseOver="this.style.backgroundColor='B0C4DE'" onMouseOut="this.style.backgroundColor='ffffff'" onmouseup="javascript:oncontrolsctable(2)" height="20" width="150" valign="top" bgcolor="#ffffff">
	   							<table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=4><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src='images/icon_db.gif'></td><td width=180><img alt border=0 height=4 src=images/trans.gif width=50><br><font color='#000000' style="FONT: 9pt ±¼¸²" id='JPCmCampPopup_fnt_0'><%=Message.get("jmainframe_31")%></font></td></tr></table>
	   						</td>
	   					<% } %>

   					</tr>
   				</table>

   			</td>
   		</tr>
   	</table>
   </div>

</div>
<!--  @@@@@@@@@@@@@@ The End of Scheduling Envrionment Setting @@@@@@@@@@@@@@  -->
