<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File,m2soft.rdsystem.server.core.install.*,m2soft.rdsystem.server.core.rddbagent.beans.* " %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include	file="../control/lib/JLButton.java"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSTreeCtrl.jsp"%>
<%@ include file="../control/lib/JLTreeCtrl.java"%>

<%@ include file="../control/lib/JSContextMenu.jsp"%>
<%@ include file="../control/lib/JLContextMenu.java"%>

<%@ include file="JLDocTree.java"%>
<%!
   static String NODEINITPARAM="@#*";
%>

<%
	initArg(request,out);
   String loginuser = sessionUser;
   String JMainTree_07 = Message.get("JMainTree_07");
	String opcode = request.getParameter("opcode");
	String filepath = request.getParameter("filepath");	

	if(filepath != null && opcode != null && opcode.equals("delete"))
	{
		// in the case of management from file
		if(docinfo.equals("no"))
		{
			BeansDirAuthentication DirAuth = new BeansDirAuthentication();
			DirAuth.delete(filepath);
		}
		// in the case of management from DB
		else
		{
			Directoryauthorization DirAuth = new Directoryauthorization();
			DirAuth.delete(filepath, servicename);
		}
	}
%>

<script>

   var lastEditNodePath = null;
   var pnLeft = null;
   var pnTop = null;
	function _setContextMenu(id,wnd,nLeft,nTop)
	{		
		if (wnd == null)
			return;
		var node = JLTreeCtrl_getFocusNode();
      if (node == null)
         return;
			
     var path = getPath3(node);         
     
	   pnLeft = nLeft;
	   pnTop = nTop;
		JLContextMenu_init(wnd,window,id,150);

		if(path.substring(path.length-4) == '.mrd')
		{
	      JLContextMenu_addItem(150,"images/icon_img_del.gif","<%=Message.get("JMainTree_02")%>","ondelete()");
	      JLContextMenu_addItem(150,"images/icon_img_mail.gif","<%=Message.get("JMainTree_05")%>","onsendmail()");
	      JLContextMenu_addItem(150,"images/icon_img_big.gif","<%=Message.get("JMainTree_11")%>","onfiledesc()");
	      JLContextMenu_addItem(150,"images/icon_img_reflesh.gif","<%=Message.get("JMainTree_06")%>","onrefresh()");
		}
		else
		{
	      JLContextMenu_addItem(150,"images/icon_img_folder_close.gif","<%=Message.get("JMainTree_01")%>","oninsert()");
	      JLContextMenu_addItem(150,"images/icon_img_del.gif","<%=Message.get("JMainTree_02")%>","ondelete()");
	      JLContextMenu_addItem(150,"images/icon_img_new.gif","<%=Message.get("JMainTree_03")%>","onrename()");
	      JLContextMenu_addItem(150,"images/icon_img_up.gif","<%=Message.get("JMainTree_04")%>","onuploadfile()");
	      JLContextMenu_addItem(150,"images/icon_img_reflesh.gif","<%=Message.get("JMainTree_06")%>","onrefresh()");
	   <% if(sessionGroup.equals(ADMINGROUP)) {%>
	   	if(getPath2(node) != '/mrd')
	      	JLContextMenu_addItem(150,"images/icon_img_customer.gif","<%=Message.get("JMainTree_09")%>","onauthorization()");
	   <% } %>
		}	   
      
		JLContextMenu_printObj();
		//JLContextMenu_setDebug(12);
	}
	
	function onauthorization()
	{
		var node = JLTreeCtrl_getFocusNode();
		if (node == null)
			return;

		//alert('<%=webpath%>' + '/' + '<%=mrdpath%>' + getPath2(node));
		
		var sAction = 'JDirAuthorization.jsp?dir=' + '<%=webpath%>' + '/' + '<%=mrdpath%>' + getPath2(node);
		
		var oContainer = JLContextMenu_getContextMenuContainer();
		var nLeft = 200 + Number(oContainer.screenLeft);
		var nTop =  50 + Number(oContainer.screenTop);
		
		newTargetwindow(sAction,nLeft,nTop,600,400);
	}
	
	function oninsert()
	{
	   //alert('insertnode');
      var icon = "images/ftv2folderclose.gif";
		var expand = 1;
		var action = "";
		var childcount = -1;
		// JLTreeCtrl_setDebug(2);
      //parentNode,id,text,icon,iconex,expand,action,childcount
      
      var  parentNode = JLTreeCtrl_getFocusNode();
      if (parentNode == null)
         return;
         
      var path = getPath(parentNode);         
     
      if(path.substring(path.length-4) == '.mrd'){
        alert(document.setmessage.JMainTree_08.value);
        return;
      }
        
      JLTreeCtrl_insertNodeSvr(parentNode,"New","New",icon,icon,expand,action,childcount);
      lastEditNodePath ='<%=NODEINITPARAM%>'; 
	}
	
	function getPath(id){
		var sPath ="";
		var node = id;

		while (node != null)
		{
			sPath = node.name + sPath;
			node = node.parent;
			sPath = "/"+sPath;
		}
		return sPath;
	}
	
	// kokim 2003.10.10
	function getPath2(id){
	  var sPath ="";
	  var node = id;

     while (node != null && node.name != "")
     {
         sPath = node.name + sPath;
         node = node.parent;
         sPath = "/"+sPath;
     }
      return sPath;
	}

	// kokim 2003.10.17
	function getPath3(id){
	  var sPath ="";
	  var node = id;

     while (node != null && node.name != "")
     {
         sPath = node.id + sPath;
         node = node.parent;
         sPath = "/"+sPath;
     }
      return sPath;
	}

	function JLTreeCtrl_onUpdateEx(node)
	{
		// alert(node.id);
		var arg = new Array();
		
		arg[0] = node.id;
		arg[1] = node.name;
		if (node.parent != null){
			arg[2] = node.parent.id;
         arg[3] = node.parent.treeidx;
         arg[4] = node.parent.nodeidx;
         arg[5] = getPath(node);
         
         if(lastEditNodePath != null)
            arg[6] = lastEditNodePath;
         else
            arg[6] = '<%=NODEINITPARAM%>'; //init
	   }
	   
      JLTreeCtrl_updateNodeSvr(node,arg);

	}

   
	function ondelete()
	{
      var node = JLTreeCtrl_getFocusNode();
      
      if (node == null)
         return;
      var nodev = JLTreeCtrl_getNode(node.treeidx,node.idx);
      
      var username = '<%=loginuser%>';
      if(username == node.name)
         return;
      
      var nCount = Number(nodev.childcount);
      if(nCount > 0 )
      {
         alert('<%=JMainTree_07%>');
         return;
      }

      var ret = confirm(node.id + ' ' + document.setmessage.scheduleList_info_javascript_15.value);
      
      if(!ret)
         return;
      
      //node.name = node.id;
      
      var arg = new Array();
      arg[0] = node.id;
      arg[1] = node.treeidx; 
      arg[2] = node.idx;
      arg[3] = getPath3(node);
         
      JLTreeCtrl_deleteNodeSvr(node,arg);

      ext = node.id.substr(node.id.length-3, 3);
      if(ext == 'mrd'){
      	document.filedelete.filename.value=node.id;
      	document.filedelete.submit();
         onrefresh();
      }

		var fullpath = '<%=webpath%>' + '/' + '<%=mrdpath%>' + getPath2(node);

      //sjs 04.22 reload the page again, to show the contents correclty
      window.location='JMainTree.jsp?opcode=delete&filepath=' + fullpath;
      window.location.reload();
	}
	
	function onrename()
	{
		var node = JLTreeCtrl_getFocusNode();
      if (node == null)
         return;

      lastEditNodePath = getPath(node);
		JLTreeCtrl_editNodeEx(node);
	}

	function onrefresh()
	{
      window.location.reload();
	}
	
   function onuploadfile(){
      var node = JLTreeCtrl_getFocusNode();
      
      if (node == null)
         return;

     var path = getPath(node);         
     
     if(path.substring(path.length-4) == '.mrd')
        return;
     
     var sAction = 'JUploadSelect.jsp?targetpath=' + getPath(node);
     
     var oContainer = JLContextMenu_getContextMenuContainer();
     var nLeft = pnLeft + Number(oContainer.screenLeft);
     var nTop =  pnTop + Number(oContainer.screenTop);
     newTargetwindow(sAction,nLeft,nTop,600,200);
     
   }
   
   function newTargetwindow(url,l, t, w, h) {
      var windowprops = "location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no" +",left=" + l + ",top=" + t + ",width=" + w + ",height=" + h;
      popup = window.open(url,"MenuPopup",windowprops);
   }

   
  function onsendmail()
  {
     var node = JLTreeCtrl_getFocusNode();

     if (node == null)
         return;

      var nCount = Number(node.childcount);
      if(nCount > 0 )
         return;
     //alert(getPath(node)); 
      
     var sAction = 'JDocSendMail.jsp?targetpath=' + getPath3(node);
     
     var oContainer = JLContextMenu_getContextMenuContainer();
     var nLeft = pnLeft + Number(oContainer.screenLeft);
     var nTop =  pnTop + Number(oContainer.screenTop);
     newTargetwindow(sAction,nLeft,nTop,600,200);
     
  }
  
	function onfiledesc()
	{
		var node = JLTreeCtrl_getFocusNode();
		
		if (node == null)
		   return;
		
		var sAction = 'JFileDesc.jsp?targetpath=' + getPath3(node);
		
		var oContainer = JLContextMenu_getContextMenuContainer();
		var nLeft = pnLeft + Number(oContainer.screenLeft);
		var nTop =  pnTop + Number(oContainer.screenTop);
		newTargetwindow(sAction,nLeft,nTop,600,200);
	}
</script>

<script>
	function myContextMenu()
	{
		if (JLContextMenu_onContextMenuEx != null) 
		{ 
			return JLContextMenu_onContextMenuEx('folderPopup','_mainFrame',window); 
		}
	}
</script>
		 

<body bgcolor="#ffffff" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0"  oncontextmenu="javascript:if (myContextMenu != null) { return myContextMenu(); } ">

<form name="setmessage">
   <input type="hidden" name="scheduleList_info_javascript_15" value="<%= Message.get("scheduleList_info_javascript_15")%>">
   <input type="hidden" name="JMainTree_08" value="<%= Message.get("JMainTree_08")%>">
   <input type="hidden" name="JMainTree_10" value="<%= Message.get("JMainTree_10")%>">
</form>

<form name="fileupload" action="JDocAdd.jsp">
  <input type="hidden" name="uploadtarget">
</form>

<form name="filedelete" action="JDelFileDesc.jsp">
  <input type="hidden" name="filename">
</form>

</form>
<%

   String sInclude = "<"+"%@ include file=\"properties.h\"%"+"><"+"%@ page import=\"java.util.*,java.io.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message\" %"+"> ";
	initArg(request,out);
	
   JLDocTree treeDoc = new JLDocTree(out);   
	treeDoc.setCodeHeader(sInclude);
	treeDoc.init("MrdDoc");
%>

<script>
	// (id,oParent,name,icon,iconex,expand,treeidx,action,childcount)
   var root0 = JLTreeCtrl_addNode(0,null,"<%=""%>","./images/ftv2folderclosed.gif","./images/ftv2folderclosed.gif",1,0,"onclickDoc('<%=loginuser%>')",1);
   JLTreeCtrl_setName(0,"MrdDoc");
</script>

<%
   treeDoc.printTree("root0","0");

%>


<script>
   function ondblclicknode(){
   	var node = JLTreeCtrl_getFocusNode();
		var nodePath = getPath(node);      
		var nodePathCheck = nodePath.indexOf(".mrd");
		
		/// bgso 2003/10/06 .mrd If it is not file, return 
		if (nodePathCheck==-1){
			alert(document.setmessage.JMainTree_10.value);
			return;
		}

     if (node == null)
         return;

     var sAction = 'JDocView.jsp?targetpath=/' + getPath3(node);
     
     var wMainFrame = getAncestor(window,"_mainFrame");
     if (wMainFrame != null)
     {
        var sFrame = "JDocView";
        var sSrc = "JDocView.jsp?targetpath=/" + getPath3(node);
        wMainFrame._openFrame("rightframe",sFrame,sSrc);
     }
      
     
	}
	
	function onclickSch(id)
	{
	   /* sjs 0717
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JMainHome";
			var sSrc = "JMainHome.jsp";
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}
		*/	
	}
	
</script>

</body>
</html>


