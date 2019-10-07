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

<style type=text/css>
input.gridcell{
BACKGROUND-COLOR: #ffffff; BORDER-BOTTOM: #000000 0px solid; BORDER-LEFT: #000000 0px solid; BORDER-RIGHT: #000000 0px solid; BORDER-TOP: #000000 0px solid;FONT-FAMILY: 굴림; FONT-SIZE: 9pt; COLOR: #000000}
</style>

<%!
public class JLGridCtrl extends JLListCtrl
{
	public JLGridCtrl(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		super.init();
		m_sCtrl = "gridctrl.jsp";
		// default left header width
		m_nLeftHeaderWidth = 50;
		m_nLineHeight = 20;
		setClassName("JLGridCtrl");
		// m_bScript = true;
	}
	
	public String getCode()
	{
		String sClassNameExt = m_sClassName+"."+m_sClassExt;
		String sHtml = "";
		String sPath = m_sPath;
		if (!JLString.IsEmpty(sPath))
		{
			sPath += "/";
		}
		String sClassPath = m_sClassPath;
		if (!JLString.IsEmpty(sClassPath))
		{
			sClassPath += "/";
		}
      String m_sCharSet = m2soft.rdsystem.server.core.install.Message.getcontentType();
		String m_sContentType = m_sCharSet;
		sHtml += "<"+"%@ page language=\"java\" %"+">\n";
		sHtml += "<"+"%@ page import=\"java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File \" %"+">\n";
		sHtml += "<"+"%@ page contentType=\""+m_sContentType+"\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLJsp.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLObj.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLRuntimeClass.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLHttp.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLListCtrl.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLGridCtrl.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JSContextMenu.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLContextMenu.java\" %"+">\n";
		if (!JLString.IsEmpty(m_sClassName))
		{
			if (!m_sClassName.equalsIgnoreCase("JLGridCtrl"))
				sHtml += "<"+"%@ include file=\""+sPath+sClassNameExt+"\" %"+">\n";
		}
		sHtml += "<body bgcolor=\"#ffffff\" text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n";
		sHtml += "<"+"%\n";
		sHtml += "initArg(request,out);\n";
		sHtml += m_sClassName+" ctrl = new "+m_sClassName+"(out);\n";
		sHtml += "ctrl.initByParam(m_param);\n";
		sHtml += "int nCmd = getIntValue(m_param,\"_cmd\");\n";
		sHtml += "if (nCmd == 0) // getSegment\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.printGrid();\n";
		sHtml += "	ctrl.printFinishScript();\n";
		sHtml += "} \n";
		sHtml += "if (nCmd == 1) // insertRow\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.insertRow();\n";
		sHtml += "} \n";
		sHtml += "if (nCmd == 2) // updateRow\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.updateRow();\n";
		sHtml += "} \n";
		sHtml += "if (nCmd == 3) // deleteRow\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.deleteRow();\n";
		sHtml += "} \n";
		sHtml += "%"+">\n";
		sHtml += "</body>";
		return sHtml;
	}
	
	void printCell(String sVal,int nRow,int nCol,int nWidth)
	{
		int nCellWidth = nWidth-1;
		int nRow2 = nRow;
		String sEditable = "";
		
		JLRcd rHeader = getHeaderAt(nCol);
		if (rHeader != null)
		{
			if (rHeader.GetIntValue("edit") == 0)
				sEditable = "readonly";
		}		

		String sHtml = "";
		sHtml = "<td id=td_"+nRow2+"_"+nCol+" align=left valign=top width=\""+nCellWidth+"\"><img alt border=\"0\" height=\"1\" src=\"\" width="+nCellWidth+"><br><input "+sEditable+" class=\"gridcell\" type=text id=\""+nRow2+"_"+nCol+"\" value=\"\" maxlength=512 style=\"width:"+(nWidth-4)+"px;\"  onchange=\"parent.parent.JLGridCtrl_onchangeEx(m_obj)\" onblur=\"javascript:if (parent.parent.JLGridCtrl_onBlurEx != null) parent.parent.JLGridCtrl_onBlurEx(m_obj)\"></td>";
		if (JLString.IsEmpty(sVal))
		{
			printStream(sHtml);
			return;
		}

		int idx = sVal.indexOf("\"");
		if (idx < 0)
		{
			sHtml = "<td id=td_"+nRow2+"_"+nCol+" align=left valign=top width=\""+nCellWidth+"\"><img alt border=\"0\" height=\"1\" src=\"\" width="+nCellWidth+"><br><input "+sEditable+" type=text class=\"gridcell\" id=\""+nRow2+"_"+nCol+"\" value=\""+sVal+"\" maxlength=512 style=\"width:"+(nWidth-4)+"px;\"  onchange=\"parent.parent.JLGridCtrl_onchangeEx(m_obj)\" onblur=\"javascript:if (parent.parent.JLGridCtrl_onBlurEx != null) parent.parent.JLGridCtrl_onBlurEx(m_obj)\"></td>";
			printStream(sHtml);
		}
		else
		{
			printStream(sHtml);
			sHtml = "";
			sHtml += "<script>var elmt = document.getElementById(\""+nRow2+"_"+nCol+"\"); ";
			sHtml += "if (elmt != null) elmt.value = \""+this.insertDQuoteEsc(sVal)+"\"; </script>";
			printStream(sHtml);
		}
	}
	
	void printGridRow(Object rcd,int nIdx)
	{
		if (rcd == null)
			return;
		Vector vRcd = (Vector)rcd;
		// debugPrintV(vRcd);
		printStream("<tr height=19>");
		int nFld = vRcd.size();
		int nCol = getHeaderSize();
		for (int i=0;i<nCol;i++)
		{
			String sVal = "";
			if (i < nFld)
			{
				sVal = (String)vRcd.elementAt(i);
				if (sVal == null)
					sVal = "";
				// sVal = this.insertDQuoteEsc(sVal);
			}
			int nWidth = m_pnWidth[i];				
			/*
			// debug
			if (i == 0 && nFld > 0)
				sVal += "_"+(nIdx+1);
			*/
			printCell(sVal,nIdx,i,m_pnWidth[i]);
		}
		printStream("</tr>");
	}

	void printGridNo(int nPageStart,int nPageCount,int nCurSegSize)
	{
		if (m_bScript)
		{
			String sHtml = "";
			sHtml += "<script language=\"javascript\">";
			sHtml += "if (parent.parent.JLGridCtrl_printGridNo != null) parent.parent.JLGridCtrl_printGridNo(m_obj,"+m_nPageStart+","+nPageCount+","+m_nPageSize+","+m_nCurSegSize+",\""+m_sMainFrame+"\");";
			sHtml += "</script>";
			printStream(sHtml);
		}
		else
		{
			int nPageSize = m_nPageSize;

			int nTop = 0;
			String sHtml = "";
			String sName = "";
			sHtml += "<div id=\"divGridNo\" style=\"POSITION:absolute; left:0px; top:"+nTop+"px\" ";
			sHtml += " oncontextmenu=\"javascript:if (parent.parent.JLGridCtrl_onClickEx != null) parent.parent.JLGridCtrl_onClickEx(m_obj); if (parent.JLContextMenu_onContextMenuEx != null) { return parent.JLContextMenu_onContextMenuEx('a','"+m_sMainFrame+"',window); } \" id=\"divGridNo\" style=\"POSITION:absolute; left:0px; top:0px\"> ";
			sHtml += "<table id=leftheader border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\""+m_nLeftHeaderWidth+"\" bgcolor=\"#ffffff\" onclick=\"javascript:if (parent.parent.JLGridCtrl_onClickEx != null) parent.parent.JLGridCtrl_onClickEx(m_obj);\">";
			int nRow = (nPageSize*nPageCount);
			if (nRow > nCurSegSize)
				nRow = nCurSegSize;
			int nStart = nPageStart*nPageSize;
			for (int j=0;j<nRow;j++)
			{
				int nIdx = j + nStart;
				sHtml += "<tr height=20><td><table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\"><tr height=1><td width=1></td><td></td><td width=1 bgcolor=\"#666666\"></td></tr><tr height='18'><td width=1></td><td id=no_"+nIdx+" align=left width="+(m_nLeftHeaderWidth-2)+" bgcolor=\""+this.m_sBtnFace+"\"><img alt border=0 src=\"\" width="+(m_nLeftHeaderWidth-2)+" height=1><br><img alt border=0 height=1 src=\"\" width=3 height=2><font color=\"#000000\" style=\"FONT: 9pt 굴림\">";
				sHtml += "" + (nIdx+1);
				sHtml += "</font></td><td width=1 bgcolor=\"#666666\"></td></tr><tr height=1><td width=1 bgcolor=\"#666666\"></td><td bgcolor=\"#666666\"></td><td width=1 bgcolor=\"#666666\"></td></tr></table></td></tr>";
				sHtml += "\n";
			}
			sHtml += "</table>";
			sHtml += "</div>";
			printStream(sHtml);
		}
	}
	
	public int getWidth()
	{
		if (m_nWidth > 0)
			return m_nWidth;
		
		m_nWidth = m_nLeftHeaderWidth;	
		for (int i=0;i<getHeaderSize();i++)
		{
			JLRcd rcd = getHeaderAt(i);
			if (rcd == null)
				continue;
			int nCellWidth = rcd.GetIntValue("width");
			m_nWidth += nCellWidth;
		}
		return m_nWidth;
	}
	
	void printGrid()
	{
		Vector vSeg = getSegment();
		// debugPrintVV(vSeg);
		printGrid(vSeg);
	}

	void printListStart()
	{
		int k = 0;
		int nDataTop = -1 + m_nPageSize*20*k;
		printStream("<div id=\"divFocusRow\"></div>");
		printStream("<div id=\"gridPage"+(m_nPageStart+k)+"\">");
		printStream("<div id=\"gridData\" style=\"POSITION:absolute; left:"+(m_nLeftHeaderWidth-1)+"px; top:"+nDataTop+"px;\">");
		printStream("<table id=tbl border=\"1\" cellpadding=\"0\" style=\"border-collapse:collapse;\" bordercolor=\"#666666\" cellspacing=\"0\" width=\""+getHeaderWidth()+"\"  bgcolor=\"#ffffff\" ");
		/*
		onfocus=\"javascript:if (parent.parent.JLGridCtrl_onFocus != null) parent.parent.JLGridCtrl_onFocus(m_obj,'"+nRow2+"','"+nCol+"')\" onchange="parent.parent.JLGridCtrl_onchangeEx(m_obj)" onblur=\"javascript:if (parent.parent.JLGridCtrl_onBlurEx != null) parent.parent.JLGridCtrl_onBlurEx(m_obj)\"
		onclick=\"javascript:if (parent.parent.JLGridCtrl_onClick != null) parent.parent.JLGridCtrl_onClick(m_obj,"+nRow2+","+nCol+");\" onmouseover=\"javascript:if (parent.parent.JLGridCtrl_onmouseover != null) parent.parent.JLGridCtrl_onmouseover(m_obj,"+nRow2+","+nCol+");\" onmouseout=\"javascript:if (window.parent.parent.JLGridCtrl_onmouseout != null) parent.parent.JLGridCtrl_onmouseout(m_obj,"+nRow2+","+nCol+");\"		
		*/
		String sHtml = "";
		sHtml += " onfocus=\"javascript:if (parent.parent.JLGridCtrl_onFocusEx != null) parent.parent.JLGridCtrl_onFocusEx(m_obj)\" ";
		sHtml += " onclick=\"javascript:if (parent.parent.JLGridCtrl_onClickEx != null) parent.parent.JLGridCtrl_onClickEx(m_obj);\" onmouseover=\"javascript:if (parent.parent.JLGridCtrl_onmouseoverEx != null) parent.parent.JLGridCtrl_onmouseoverEx(m_obj);\" onmouseout=\"javascript:if (window.parent.parent.JLGridCtrl_onmouseoutEx != null) parent.parent.JLGridCtrl_onmouseoutEx(m_obj);\" ";
		printStream(sHtml);
		printStream(">");
	}

	void printListEnd()
	{
		printStream("</table>");
		printStream("</div>");
	}

	void printGrid(Vector vec)
	{
		// vec 이 null이어도 이것은 해야 합니다.
		printListStart();
		m_nCurSegSize = 0;
		if (vec != null)
			m_nCurSegSize = vec.size();
		int nPage = 0;
		if (m_nPageSize > 0)
			nPage = (m_nCurSegSize+m_nPageSize-1)/m_nPageSize;
		if (m_bScript == false)
		{
			// debugPrintStream("size " + m_nCurSegSize);
			for (int k=0;k<nPage;k++)
			{
				// debugPrintStream(vec);
				Object rcd = null;
				for (int j=0;j<m_nPageSize;j++)
				{
					rcd = null;
					int nIdx = j + k*m_nPageSize;
					if (nIdx >= m_nCurSegSize)
						break;
					rcd = (Object)vec.elementAt(nIdx);
					if (rcd == null)
						break;
					printGridRow(rcd,((k+m_nPageStart)*m_nPageSize+j));
				}
			}
		}
		else
		{
			/*
			<script> 
			var m_vList = new Array(
			"1","2","3","4","5","6","7","8",
			"1-2","2","3","4","5","6","7","8",
			"1-3","2","3","4","5","6","7","8",
			"1_1","1_2","3","4","5","6","7","8"
			);
			_printList(window);
			</script>
			*/
			String sHtml = "";
			sHtml += "<script language=\"javascript\">\n";
			sHtml += "m_obj.m_nPageStart = "+m_nPageStart+";\n";
			sHtml += "m_obj.m_nPageSize = "+m_nPageSize+";\n";
			sHtml += "m_obj.m_nPageCount = "+m_nPageCount+";\n";
			sHtml += "m_obj.m_vList = new Array(";
			printStream(sHtml);
			sHtml = "";

			printGridEx(vec);
			int nCol = getHeaderSize();
			sHtml = "";
			sHtml += ");\n";
			sHtml += "m_obj.m_nColSize = "+nCol+";\n";
			sHtml += "if (parent.parent.JLGridCtrl_printList != null) parent.parent.JLGridCtrl_printList(m_obj);\n";
			sHtml += "else\n";
			sHtml += "{ if (opener.parent.JLGridCtrl_printList != null) opener.parent.JLGridCtrl_printList(m_obj); }\n";
			sHtml += "</script>";
			printStream(sHtml);
		}
		printListEnd();
		printGridNo(m_nPageStart,nPage,m_nCurSegSize);
	}

	void printGridEx(Vector vSeg)
	{
		if (m_bScript)
		{
			if (vSeg == null)
				return;
			int nSize = vSeg.size();
			int nCol = getHeaderSize();
			Vector rcd = null;
			for (int j=0;j<nSize;j++)
			{
				rcd = (Vector)vSeg.elementAt(j);
				if (rcd == null)
					continue;
				if (j != 0 && m_bScript)
					printStream(",");
				printRcd(0,0,(Object)rcd);
			}
		}
	}
	
	void printGridV()
	{
		Vector vSeg = getSegment(m_nPageCount,m_nPageStart,m_nPageSize);
		printGridV(vSeg);
	}

	void printGridV(Vector vec)
	{
		if (vec == null)
			return;
		m_nCurSegSize = vec.size();
		if (m_nPageSize == 0)
			return;
		int nPage = (m_nCurSegSize+m_nPageSize-1)/m_nPageSize;
		printListStart();
		for (int k=0;k<nPage;k++)
		{
			// debugPrintStream(vec);
			Vector rcd = null;
			for (int j=0;j<m_nPageSize;j++)
			{
				rcd = null;
				int nIdx = j + k*m_nPageSize;
				if (nIdx >= m_nCurSegSize)
					break;
				rcd = (Vector)vec.elementAt(nIdx);
				if (rcd == null)
					break;
					
				printGridRow(rcd,((k+m_nPageStart)*m_nPageSize+j));
			}
		}
		printListEnd();
		printGridNo(0,nPage,m_nCurSegSize);
	}

	void printObj()
	{
		super.printBody();
		printStream("<script>");
		printStream("	if (window.JLGridCtrl_initBody != null) JLGridCtrl_initBody(m_obj);");
		printStream("	if (window.JLGridCtrl_initHeader != null) JLGridCtrl_initHeader(m_obj);");
		printStream("</script>");
	}
}
%>



