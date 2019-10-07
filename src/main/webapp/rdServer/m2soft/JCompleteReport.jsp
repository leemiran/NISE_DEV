<%@ page import="java.util.*,java.lang.*, java.io.*,java.io.File,java.text.*,m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message " %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JSContextMenu.jsp"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
%>
<html>
<head>
<style><%@ include file="addschedule.css" %></style>
<script language="JavaScript">
function Folder(folderDescription, hreference) //constructor
{
     //constant data
      this.desc = folderDescription
      this.hreference = hreference
      this.id = -1
      this.navObj = 0
      this.iconImg = 0
      this.nodeImg = 0
      this.isLastNode = 0

      //dynamic data
      this.isOpen = true
      this.iconSrc = "images/ftv2folderopen.gif"
      this.children = new Array
      this.nChildren = 0

     //methods
     this.initialize = initializeFolder
     this.setState = setStateFolder
     this.addChild = addChild
     this.createIndex = createEntryIndex
     this.hide = hideFolder
     this.display = display
     this.renderOb = drawFolder
     this.totalHeight = totalHeight
     this.subEntries = folderSubEntries
     this.outputLink = outputFolderLink
}

function setStateFolder(isOpen)
{
      var subEntries
      var totalHeight
      var fIt = 0
      var i=0

      if (isOpen == this.isOpen)
               return

      if (browserVersion == 2)  {
               totalHeight = 0
               for (i=0; i < this.nChildren; i++)
        totalHeight = totalHeight + this.children[i].navObj.clip.height
               subEntries = this.subEntries()
               if (this.isOpen)
         totalHeight = 0 - totalHeight
               for (fIt = this.id + subEntries + 1; fIt < nEntries; fIt++)
         indexOfEntries[fIt].navObj.moveBy(0, totalHeight)
      }
      this.isOpen = isOpen
      propagateChangesInState(this)
}

function propagateChangesInState(folder)
{
  var i=0

  if (folder.isOpen)
  {
      if (folder.nodeImg)
          if (folder.isLastNode)
                 folder.nodeImg.src = "images/ftv2mlastnode.gif"
          else
                 folder.nodeImg.src = "images/ftv2mnode.gif"
    folder.iconImg.src = "images/ftv2folderopen.gif"
    for (i=0; i<folder.nChildren; i++)
      folder.children[i].display()
  }
  else
  {
    if (folder.nodeImg)
      if (folder.isLastNode)
        folder.nodeImg.src = "images/ftv2plastnode.gif"
      else
     folder.nodeImg.src = "images/ftv2pnode.gif"
    folder.iconImg.src = "images/ftv2folderclosed.gif"
    for (i=0; i<folder.nChildren; i++)
      folder.children[i].hide()
  }
}
function hideFolder()
{
  if (browserVersion == 1) {
    if (this.navObj.style.display == "none")
      return
    this.navObj.style.display = "none"
  } else {
    if (this.navObj.visibility == "hiden")
      return
    this.navObj.visibility = "hiden"
  }

  this.setState(0)
}

function initializeFolder(level, lastNode, leftSide)
{
var j=0
var i=0
var numberOfFolders
var numberOfDocs
var nc

  nc = this.nChildren

  this.createIndex()

  var auxEv = ""

  if (browserVersion > 0)
    auxEv = "<a href='javascript:clickOnNode("+this.id+")'>"
  else
    auxEv = "<a>"

  if (level>0)
    if (lastNode) //the last 'brother' in the children array
    {
      this.renderOb(leftSide + auxEv + "<img name='nodeIcon" + this.id + "' src='images/ftv2mlastnode.gif' border=0></a>")
      leftSide = leftSide + "<img src='images/ftv2blank.gif'>"
      this.isLastNode = 1
    }
    else
    {
      this.renderOb(leftSide + auxEv + "<img name='nodeIcon" + this.id + "' src='images/ftv2mnode.gif' border=0></a>")
      leftSide = leftSide + "<img src='images/ftv2vertline.gif'>"
      this.isLastNode = 0
    }
  else
    this.renderOb("")

  if (nc > 0)
  {
    level = level + 1
    for (i=0 ; i < this.nChildren; i++)
    {
      if (i == this.nChildren-1)
        this.children[i].initialize(level, 1, leftSide)
      else
        this.children[i].initialize(level, 0, leftSide)
      }
  }
}


function drawFolder(leftSide)
{
  if (browserVersion == 2) {
    if (!document.yPos)
      document.yPos=8
    document.write("<layer id='folder" + this.id + "' top=" + document.yPos + " visibility=hiden>")
  }

  document.write("<table ")
  if (browserVersion == 1)
    document.write(" id='folder" + this.id + "' style='position:block;' ")
  document.write(" border=0 cellspacing=0 cellpadding=0>")
  document.write("<tr><td width=5%>")
  document.write(leftSide)
  this.outputLink()
  document.write("<img name='folderIcon" + this.id + "' ")
  document.write("src='" + this.iconSrc+"' border=0></a>")
  document.write("</td><td valign=middle nowrap width=60%>")
  if (USETEXTLINKS)
  {
    this.outputLink()
    document.write(this.desc + "</a>")
  }
  else
    document.write(this.desc)
  document.write("</td>")

  document.write("<td valign=middle nowrap align=center>"+document.setmessage.completefilelist_01.value+"</td>")
  document.write("</table>")

  if (browserVersion == 2) {
    document.write("</layer>")
  }

  if (browserVersion == 1) {
    this.navObj = document.all["folder"+this.id]
    this.iconImg = document.all["folderIcon"+this.id]
    this.nodeImg = document.all["nodeIcon"+this.id]
  } else if (browserVersion == 2) {
    this.navObj = document.layers["folder"+this.id]
    this.iconImg = this.navObj.document.images["folderIcon"+this.id]
    this.nodeImg = this.navObj.document.images["nodeIcon"+this.id]
    document.yPos=document.yPos+this.navObj.clip.height
  }
}
function outputFolderLink()
{
  if (this.hreference)
  {
    document.write("<a href='" + this.hreference + "' TARGET=\"_new\" ")
    if (browserVersion > 0)
      document.write("onClick='javascript:clickOnFolder("+this.id+")'")
    document.write(">")
  }
  else
    document.write("<a>")
//  document.write("<a href='javascript:clickOnFolder("+this.id+")'>")
}

function addChild(childNode)
{
  this.children[this.nChildren] = childNode
  this.nChildren++
  return childNode
}

function folderSubEntries()
{
  var i = 0
  var se = this.nChildren

  for (i=0; i < this.nChildren; i++){
    if (this.children[i].children) //is a folder
      se = se + this.children[i].subEntries()
  }

  return se
}

function Item(itemDescription, itemLink, createDate) // Constructor
{
  // constant data
  this.desc = itemDescription
  this.link = itemLink
  this.cdate = createDate
  this.id = -1 //initialized in initalize()
  this.navObj = 0 //initialized in render()
  this.iconImg = 0 //initialized in render()
  this.iconSrc = "images/rd.gif"

  // methods
  this.initialize = initializeItem
  this.createIndex = createEntryIndex
  this.hide = hideItem
  this.display = display
  this.renderOb = drawItem
  this.totalHeight = totalHeight
}

function hideItem()
{
  if (browserVersion == 1) {
    if (this.navObj.style.display == "none")
      return
    this.navObj.style.display = "none"
  } else {
    if (this.navObj.visibility == "hiden")
      return
    this.navObj.visibility = "hiden"
  }
}

function initializeItem(level, lastNode, leftSide)
{
  this.createIndex()

  if (level>0)
    if (lastNode) //the last 'brother' in the children array
    {
      this.renderOb(leftSide + "<img src='images/ftv2lastnode.gif'>")
      leftSide = leftSide + "<img src='images/ftv2blank.gif'>"
    }
    else
    {
      this.renderOb(leftSide + "<img src='images/ftv2node.gif'")
      leftSide = leftSide + "<img src='images/ftv2vertline.gif'>"
    }
  else
    this.renderOb("")
}

function drawItem(leftSide)
{
  if (browserVersion == 2)
    document.write("<layer id='item" + this.id + "' top=" + document.yPos + " visibility=hiden>")

  document.write("<table ")
  if (browserVersion == 1)
    document.write(" id='item" + this.id + "' style='position:block;' ")
  document.write(" border=0 cellspacing=0 cellpadding=0>")
  document.write("<tr onMouseOver=this.style.backgroundColor='lightyellow';status='';return true; onMouseOut=this.style.backgroundColor='';status='';return true; height=20>")
  document.write("<td>")
  document.write(leftSide)
  document.write("<a href=" + this.link + ">")
  document.write("<img id='itemIcon"+this.id+"' ")
  document.write("src='"+this.iconSrc+"' border=0 hspace=2 vspace=1>")
  document.write("</a>")
  document.write("</td><td valign=middle nowrap width=570>")
  if (USETEXTLINKS)
    document.write("<a href=" + this.link + ">" + this.desc + "</a>")
  else
    document.write(this.desc)
  document.write("</td><td valign=middle nowrap align=center>"+this.cdate+"</td>")
  document.write("</table>")

  if (browserVersion == 2)
    document.write("</layer>")

  if (browserVersion == 1) {
    this.navObj = document.all["item"+this.id]
    this.iconImg = document.all["itemIcon"+this.id]
  } else if (browserVersion == 2) {
    this.navObj = document.layers["item"+this.id]
    this.iconImg = this.navObj.document.images["itemIcon"+this.id]
    document.yPos=document.yPos+this.navObj.clip.height
  }
}


function display()
{
  if (browserVersion == 1)
    this.navObj.style.display = "block"
  else
    this.navObj.visibility = "show"
}

function createEntryIndex()
{
  this.id = nEntries
  indexOfEntries[nEntries] = this
  nEntries++
}

// total height of subEntries open
function totalHeight() //used with browserVersion == 2
{
  var h = this.navObj.clip.height
  var i = 0

  if (this.isOpen) //is a folder and _is_ open
    for (i=0 ; i < this.nChildren; i++)
      h = h + this.children[i].totalHeight()

  return h
}

function clickOnFolder(folderId)
{
  var clicked = indexOfEntries[folderId]

  if (!clicked.isOpen)
    clickOnNode(folderId)

  return

  if (clicked.isSelected)
    return
}

function clickOnNode(folderId)
{
  var clickedFolder = 0
  var state = 0

  clickedFolder = indexOfEntries[folderId]
  state = clickedFolder.isOpen

  clickedFolder.setState(!state) //open<->close
}

function initializeDocument()
{
  if (document.all)
    browserVersion = 1 //IE4
  else
    if (document.layers)
      browserVersion = 2 //NS4
    else
      browserVersion = 0 //other

  aux0.initialize(0, 1, "")
  aux0.display()

  if (browserVersion > 0)
  {
    document.write("<layer top="+indexOfEntries[nEntries-1].navObj.top+"> </layer>")

    // close the whole tree
    clickOnNode(0)
    // open the root folder
    clickOnNode(0)
  }
}

// Auxiliary Functions for Folder-Treee backward compatibility
// *********************************************************

function gFld(description, hreference)
{
  folder = new Folder(description, hreference)
  return folder
}

function gLnk(target, description, linkData,createDate)
{
  fullLink = ""

  if (target==0)
  {
    fullLink = "'"+linkData+"' target=\"_self\""
  }
  else
  {
    if (target==1)
       fullLink = "'../"+linkData+"' target=_new"
    else
       fullLink = "'../"+linkData+"' target=\"_new\""
  }

  linkItem = new Item(description, fullLink, createDate)
  return linkItem
}

function insFld(parentFolder, childFolder)
{
  return parentFolder.addChild(childFolder)
}

function insDoc(parentFolder, document)
{
  parentFolder.addChild(document)
}

 // Global variables
// ****************

USETEXTLINKS = 1
indexOfEntries = new Array
nEntries = 0
doc = document
browserVersion = 0
selectedFolder=0

<%!

	int count = 1;

   void makeDirTree(String completedir,int rootinx,JspWriter out)
   {
	   try
	   {
	      File f = null;
	      File rootdir = new File(completedir);
	      if(!rootdir.exists())
	      	rootdir.mkdir();
	      String[] filelist = rootdir.list();
	      Vector dirnameList = new Vector();
	      Vector filenameList = new Vector();

	      for(int j=0; j<filelist.length; j++){
	         f = new File(completedir+"/"+filelist[j]);
	         if(f.isDirectory())
	            dirnameList.addElement(filelist[j]);
	         else{
	            filenameList.addElement(filelist[j]);
	            count = count + 1;
	         }
	      }

			for (Enumeration e = filenameList.elements() ; e.hasMoreElements() ;) {
				dirnameList.addElement((String)e.nextElement());
			}

	      Calendar date = Calendar.getInstance();
	      SimpleDateFormat formatter   = new SimpleDateFormat("yyyy'-'MM'-'dd a hh:mm");

	      try{

	         for(int i=0; i<dirnameList.size(); i++) {

	            f = new File(completedir+"/"+dirnameList.elementAt(i));
	            date.setTime(new Date(f.lastModified()));
	            if(f.isDirectory()) {
	               out.println("aux"+(rootinx+i+1)+" = insFld(aux"+rootinx+",gFld(\""+dirnameList.elementAt(i)+"\"))");
	               makeDirTree(completedir+"/"+dirnameList.elementAt(i),(rootinx+i+1),out);
	            }
	            else{
	               if(dirnameList.elementAt(i).toString().indexOf(".htm") < 0)
	                  continue;
	               int inx = completedir.indexOf(ocxhtmpath);

	               if(inx != -1)
	                  out.println("insDoc(aux"+rootinx+",gLnk(2,\""+dirnameList.elementAt(i)+"\",\"download.jsp?filename="+ocxhtmpath+completedir.substring(inx+ocxhtmpath.length())+"/"+dirnameList.elementAt(i)+"\",\""+formatter.format(date.getTime())+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type=checkbox name=delocxhtm value="+ocxhtmpath+completedir.substring(inx+ocxhtmpath.length())+"/"+dirnameList.elementAt(i)+"\"))");
	               else
	                  out.println("insDoc(aux"+rootinx+",gLnk(2,\""+dirnameList.elementAt(i)+"\",\"download.jsp?filename="+ocxhtmpath+"/"+dirnameList.elementAt(i)+"\",\""+formatter.format(date.getTime())+"&nbsp;<input type=checkbox name=delocxhtm value="+ocxhtmpath+completedir.substring(inx+ocxhtmpath.length())+"/"+dirnameList.elementAt(i)+"\"))");
	            }
	         }

	      }catch (Exception e){
	         System.out.println(e);
	      }
	   }
	   catch (Exception e)
	   {
	   	System.out.println(e);
	   }
   }
%>
 aux0 = gFld("<%=ocxhtmpath+"/"+sessionUser%>") //The top layer lable
<%
 String completedir = webpath +"/"+ ocxhtmpath+"/"+sessionUser;
 makeDirTree(completedir,0,out); //the beginning directory, the beginning root index,outstream
%>
</script>

</head>
<body leftmargin=10 topmargin="10" marginwidth="0" marginheight="0" ondragstart='return false' onselectstart='return false'>

<p>

<table border="0" cellspacing="0" cellpadding="0" width="770">
<tr height=20>
<td>
		<table border="0" cellPadding="0" cellSpacing="0" style="cursor: hand;" width="50">
			<tr height=1>
				<td id=td_lefttop_4 width=1 bgcolor="#ffffff">
				</td>
				<td id=td_top_4 bgcolor="#ffffff">
				</td>
				<td id=td_righttop_4 width=1 bgcolor="#666666"></td>
			</tr>
			<tr height='18'>
            <td id=td_left_4 width=1 bgcolor="#ffffff"></td>
            <td id=td_text_4 align=left width=48 bgcolor="#B0C4DE"><table border="0" cellPadding="0" cellSpacing="0"><tr>
            <td width=2 bgcolor="#B0C4DE"></td>
            <td width=2 bgcolor="#B0C4DE"></td>
            <td><img alt border=0 height=2 src=images/trans.gif width="28"><br><font color="#000000" style="FONT: 9pt"><%= Message.get("completefilelist_08") %></font></td></tr>
      </table>
</td>
<td id=td_right_4 width=1 bgcolor="#666666"></td>
</tr>
			<tr height=1>
				<td id=td_leftbottom_4 width=1 bgcolor="#666666">
				</td>
				<td id=td_bottom_4 bgcolor="#666666">
				</td>
				<td id=td_rightbottom_4 width=1 bgcolor="#666666">
				</td>
			</tr>
		</table>
</td>

<td>
		<table border="0" cellPadding="0" cellSpacing="0" style="cursor: hand;" width="557">
			<tr height=1>
				<td id=td_lefttop_4 width=1 bgcolor="#ffffff">
				</td>
				<td id=td_top_4 bgcolor="#ffffff">
				</td>
				<td id=td_righttop_4 width=1 bgcolor="#666666"></td>
			</tr>
			<tr height='18'>
         <td id=td_left_4 width=1 bgcolor="#ffffff"></td>
         <td id=td_text_4 align=left width=555 bgcolor="#B0C4DE"><table border="0" cellPadding="0" cellSpacing="0"><tr>
         <td width=2 bgcolor="#B0C4DE"></td>
         <td width=2 bgcolor="#B0C4DE"></td>
         <td><img alt border=0 height=2 src=images/trans.gif width="535"><br><font color="#000000" style="FONT: 9pt"><%= Message.get("completefilelist_02") %></font></td><td width=16 bgcolor="#B0C4DE"></td></tr></table>
</td>
<td id=td_right_4 width=1 bgcolor="#666666"></td>


			</tr>
			<tr height=1>
				<td id=td_leftbottom_4 width=1 bgcolor="#666666">
				</td>
				<td id=td_bottom_4 bgcolor="#666666">
				</td>
				<td id=td_rightbottom_4 width=1 bgcolor="#666666">
				</td>
			</tr>
		</table>
</td>

<td>
		<table border="0" cellPadding="0" cellSpacing="0" style="cursor: hand;"  width="130">
			<tr height=1>
				<td id=td_lefttop_4 width=1 bgcolor="#ffffff">
				</td>
				<td id=td_top_4 bgcolor="#ffffff">
				</td>
				<td id=td_righttop_4 width=1 bgcolor="#666666"></td>
			</tr>
			<tr height='18'>


<td id=td_left_4 width=1 bgcolor="#ffffff"></td>
<td id=td_text_4 align=left width=128 bgcolor="#B0C4DE"><table border="0" cellPadding="0" cellSpacing="0"><tr>
<td width=2 bgcolor="#B0C4DE"></td>

<td width=2 bgcolor="#B0C4DE"></td>
<td><img
alt border=0 height=2 src=images/trans.gif width="108"><br><font color="#000000" style="FONT: 9pt"><%= Message.get("completefilelist_03") %></font></td><td
width=16 bgcolor="#B0C4DE"></td></tr></table></td>
<td id=td_right_4 width=1 bgcolor="#666666"></td>


			</tr>
			<tr height=1>
				<td id=td_leftbottom_4 width=1 bgcolor="#666666">
				</td>
				<td id=td_bottom_4 bgcolor="#666666">
				</td>
				<td id=td_rightbottom_4 width=1 bgcolor="#666666">
				</td>
			</tr>
		</table>
</td>

<td>
		<table border="0" cellPadding="0" cellSpacing="0" style="cursor: hand;" onclick="javascript:delfileform.submit();" width=38>
			<tr height=1>
				<td id=td_lefttop_4 width=1 bgcolor="#ffffff">
				</td>
				<td id=td_top_4 bgcolor="#ffffff">
				</td>
				<td id=td_righttop_4 width=1 bgcolor="#666666"></td>
			</tr>
			<tr height='18'>

<td id=td_left_4 width=1 bgcolor="#ffffff"></td>
<td id=td_text_4 align=left width=30 bgcolor="#B0C4DE"><table border="0" cellPadding="0" cellSpacing="0"><tr>
<td width=2 bgcolor="#B0C4DE"></td>

<td width=2 bgcolor="#B0C4DE"></td>
<td width=120 ><%= Message.get("completefilelist_09") %></font></td></tr></table></td>
<td id=td_right_4 width=1 bgcolor="#666666"></td>

			</tr>
			<tr height=1>
				<td id=td_leftbottom_4 width=1 bgcolor="#666666">
				</td>
				<td id=td_bottom_4 bgcolor="#666666">
				</td>
				<td id=td_rightbottom_4 width=1 bgcolor="#666666">
				</td>
			</tr>
		</table>
</td>

</tr>
</table>

<form name="setmessage">
	<input type="hidden" name="completefilelist_01" value="<%= Message.get("completefilelist_01") %>" >
</form>

<form name="delfileform" method="post" action="JDocDelete.jsp">
<input type="hidden" name="delocx" value="ok">
<script language="javascript">
initializeDocument()
</script>

<br>

<%
      int btnX = 705;
      int btnY = 50;
      int gap = count * 25;
      JLButton btnadd = new JLButton();
      btnadd.printButton(btnX,100+gap,"fdel",75,3,Message.get("completefilelist_09"),"javascript:document.delfileform.delocx.value='ok';document.delfileform.submit();","images/button_icon_del.gif",20);
%>

</form>
<br> </br>
<%= Message.get("completefilelist_04") %><br>
<%= Message.get("completefilelist_05") %>
<font color=red><%= Message.get("completefilelist_06") %></font><%= Message.get("completefilelist_07") %>

</body>
</html>