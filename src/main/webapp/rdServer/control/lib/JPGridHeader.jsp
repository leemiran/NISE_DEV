<%
/*
*  JDC (Java DHTML Control) by Hank Moon (Han-Goo Moon in Korean)
*  Copyright (C) 2003 Hank Moon
*
*  This program is distributed in the hope that it will be useful,
*  You can use it and/or modify it, If you do not use it for commercial purpose
*  If you want it to use for commercial purpose, 
*  You have to buy or receive a copy of the License to use this software from 
*  Hank Moon or HMSOFT Inc., Ltd. (www.hmsoft.org)
*
*  Hank Moon , HMSOFT Inc., Ltd. <hankmoon@hmsoft.org>
*
*/
%>

<%@ page language="java" import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File " %>
<% String contentType1 = m2soft.rdsystem.server.core.install.Message.getcontentType(); response.setContentType(contentType1); %>

<%@ include file="JLJsp.jsp" %>
<%@ include file="JLObj.java" %>
<%@ include file="JLRuntimeClass.java" %>
<%@ include file="JSHttp.jsp"%>
<%@ include file="JLHttp.java" %>
<%@ include file="JLListCtrl.java" %>

<%
	initArg(request,out);

	JLListCtrl gridCtrl = new JLListCtrl(out);
	gridCtrl.initByParam(m_param);

	int nCount = gridCtrl.getCount();
	int nGridCount = nCount;
	if (nGridCount > 30)
	{
		nGridCount = 30;
	}
	String sBtnFace = gridCtrl.getBtnFace();

	// debugPrint(m_param);
%>

<script>
	function onClickEx(idx)
	{
		if (parent.JLListCtrl_onClickHeader != null)
			parent.JLListCtrl_onClickHeader(idx);
	}
</script>


<body bgColor=#ffffff leftMargin=0 topMargin=0 MARGINWIDTH="0" MARGINHEIGHT="0">

<table border="0" cellspacing="0" cellpadding="0" width="<%=gridCtrl.getHeaderWidth()%>">
<tr height=20>

<%
	{
		for (int i=0;i<gridCtrl.getHeaderSize();i++)
		{
			//  = 1 2 16 2 x 2 1
			// x = 100 - 24; // 76
			JLRcd rcd = gridCtrl.getHeaderAt(i);
			if (rcd == null)
				continue;
			String sText = rcd.GetStrValue("text");
			// sText = JLString.trimRight(sText,7);
			// print("<script>alert(\""+sText+"\");</script>");
			int nWidth = rcd.GetIntValue("width");
			
%>
<td>
		<table border="0" cellPadding="0" cellSpacing="0" style="cursor: hand;" onclick="javascript:onClickEx(<%=i%>);" width="<%=nWidth%>">
			<tr height=1>
				<td id=td_lefttop_4 width=1 bgcolor="#ffffff">
				</td>
				<td id=td_top_4 bgcolor="#ffffff">
				</td>
				<td id=td_righttop_4 width=1 bgcolor="#666666"></td>
			</tr>
			<tr height='18'>


<%

			int nCellWidth = nWidth - 2;
			if (nWidth < 20)
				nCellWidth = 20-2;
			
			print("<td id=td_left_4 width=1 bgcolor=\"#ffffff\"></td>");
			
			if (nWidth > 20)
			{
				print("<td id=td_text_4 align=left width="+nCellWidth+" bgcolor=\""+sBtnFace+"\"><table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\"><tr>");
				print("<td width=2 bgcolor=\""+sBtnFace+"\"></td>");
				print(""+gridCtrl.getIconHtml(i)+"");	// 16
				print("<td width=2 bgcolor=\""+sBtnFace+"\"></td>");
				print("<td><img ");
				if (nWidth >= 78) 		// 78 = 1+2+16+2+20+16+1 (min)
				{
					print("alt border=0 height=2 src=images/trans.gif width=\""+(nCellWidth-20)+"\"><br><font color=\"#000000\" style=\"FONT: 9pt ±¼¸²\">"+sText+"</font></td><td ");
					print("width=16 bgcolor=\""+sBtnFace+"\">"+gridCtrl.getSortOrderHtml(i)+"</td></tr></table></td>");
				}
				else
				{
					print("alt border=0 height=2 src=images/trans.gif width=\""+(nCellWidth-20)+"\"><br><font color=\"#000000\" style=\"FONT: 9pt ±¼¸²\">"+sText+"</font></td></tr></table></td>");
				}
			}
			else
			{
				print("<td id=td_text_4 align=left width="+nCellWidth+" bgcolor=\""+sBtnFace+"\"><table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\"><tr>");
				print(""+gridCtrl.getIconHtml(i)+"");
				print("</tr></table></td>");
			}
			print("<td id=td_right_4 width=1 bgcolor=\"#666666\"></td>");

%>

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
<%
		}
	}
%>

</tr>
</table>

<%
	{
		int nLeft = 0;
		for (int i=0;i<gridCtrl.getHeaderSize();i++)
		{
			//  = 1 2 16 2 x 2 1
			// x = 100 - 24; // 76
			JLRcd rcd = gridCtrl.getHeaderAt(i);
			if (rcd == null)
				continue;
			int bCheckbox = rcd.GetIntValue("checkbox");
			int nWidth = rcd.GetIntValue("width");
			if (bCheckbox > 0)
			{
				int nCheckBoxLeft = nLeft+1;
				if (nWidth < 23)
					nCheckBoxLeft -= (23-nWidth)/2;
				print("<div id=divCb style=\"VISIBILITY: visible; POSITION: absolute; top:0px; left:"+nCheckBoxLeft+"px;\"><input type=checkbox id=\"cb\" name=cb></div>");
			}
			nLeft += nWidth;
		}
	}
%>





<script>
	function onScrollTitle(nScroll)
	{
		document.body.scrollLeft = nScroll;
	}
</script>

