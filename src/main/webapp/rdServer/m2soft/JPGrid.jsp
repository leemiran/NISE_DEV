<%
/*
*  JDC (Java DHTML Control) by Hank Moon (Han-Goo Moon in Korean)
*  Copyright (C) 2003 Hank Moon
*
*  This program is distributed in the hope that it will be useful,
*  You can use it and/or modify it, If you do not use it for commercial purpose
*  If you want it to use for commercial purpose, 
*  You have to buy or receive a copy of the License to use this software from 
*  Hank Moon or m2soft Inc., Ltd. (www.m2soft.org)
*
*  Hank Moon , m2soft Inc., Ltd. <hankmoon@m2soft.org>
*
*/
%>

<%@ page language="java" import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File " %>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp" %>
<%@ include file="../control/lib/JLObj.java" %>
<%@ include file="../control/lib/JLRuntimeClass.java" %>
<%@ include file="../control/lib/JSHttp.jsp" %>
<%@ include file="../control/lib/JLHttp.java" %>
<%@ include file="../control/lib/JLListCtrl.java" %>
<%@ include file="../control/lib/JSContextMenu.jsp" %>
<%@ include file="../control/lib/JLContextMenu.java" %>
<%@ include file="../control/lib/JLGridCtrl.java" %>

<%
	initArg(request,out);

	JLGridCtrl grid = new JLGridCtrl(out);
	grid.initByParam(m_param);
	int nWidth = grid.getHeaderWidth();
%>

<body bgColor=#ffffff leftMargin=0 topMargin=0 MARGINWIDTH="0" MARGINHEIGHT="0">

<div id=gridAll>
<div id=gridBody></div>
</div>



<script>
	function submitForm(form)
	{
		if (form == null)	
			return;
		form.submit();
	}

	function onScrollEx()
	{
		var scrollLeft = document.body.scrollLeft;
		var nPage = parseInt(document.body.scrollTop/(m_obj.m_nLineHeight*m_obj.m_nPageSize));
		var nCurScrollLeft = parent.JLListCtrl_getScrollLeft(m_obj,nPage);
		if (scrollLeft > 0 || nCurScrollLeft > 0)
		{
			if (parent != this)
			{
				if (parent.onScrollTitle != null)
				{
					parent.onScrollTitle(document.body.scrollLeft);
				}
			}
			// document.all['gridno'].style.left = scrollLeft;
			if (nPage > 0)
				m_obj.m_nPage = nPage;
			else
				m_obj.m_nPage = 0;
	
			if (parent.JLListCtrl_scrollPage != null)
			{
				parent.JLListCtrl_scrollPage(m_obj,m_obj.m_nPage-3);
				parent.JLListCtrl_scrollPage(m_obj,m_obj.m_nPage-2);
				parent.JLListCtrl_scrollPage(m_obj,m_obj.m_nPage-1);
				parent.JLListCtrl_scrollPage(m_obj,m_obj.m_nPage);
				parent.JLListCtrl_scrollPage(m_obj,m_obj.m_nPage+1);
				parent.JLListCtrl_scrollPage(m_obj,m_obj.m_nPage+2);
			}
		}
		if (parent.JLListCtrl_onPageEx != null)
		{
			parent.JLListCtrl_onPageEx(m_obj);
		}
	}

	function _printScrollArea()
	{
		var sHtml = "";
		sHtml += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		sHtml += "<tr id=trScroll height=\"0\">";
		sHtml += "<td><img alt border=0 height=1 src=images/trans.gif width=1 height=0></td>";
		sHtml += "</tr>";
		sHtml += "</table>";
		document.write(sHtml);
	}
	
	m_obj.m_bGridReady = true;

	if (parent.JLListCtrl_onPage != null)
	{
		form = parent.document.fgridparam;
		parent.CS_Producer(m_obj,0,1,form.pagesize.value);
	}
	if (parent.JLGridCtrl_focusRow != null)
	{
		parent.JLGridCtrl_focusRow(m_obj,-1);
	}
	_printScrollArea();
	window.onscroll = onScrollEx;
	// document.onkeydown = onKeyDownEx;
	m_obj.m_nHeaderWidth = <%=nWidth%>;
</script>


