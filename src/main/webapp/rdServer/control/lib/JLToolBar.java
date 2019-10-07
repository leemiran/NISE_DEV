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

<!-- JLToolBar.jsp start -->

<script>
	function JLToolBar_drawObj(id)
	{
		var nTop = 0;
		var nLeft = 0;
		var nCount = Number(_getValue(id,"childcount"));
		for (var i=0;i<nCount;i++)
		{
			nTop += 2;
			var sChild = _getArrayValue(id,"child",i);
			if ( sChild == "undefined" || sChild == null || sChild == "")
				continue;
				
			var nPosLeft = nLeft;
			var nPosTop = nTop;
			var nItemLeft = _getValue(sChild,"left");
			if (nItemLeft > 0)
				nPosLeft = nItemLeft;
			var nItemTop = _getValue(sChild,"top");
			if (nItemTop > 0)
				nPosTop = nItemTop;
			
			var nItemHeight = _getValue(sChild,"height");
			nTop += nItemHeight;
			if (i < nCount-1)
				drawLine(window,id+"_i"+i,2,nTop,document.body.clientWidth-3,1,"#777777");
		}
		drawLine(window,id+"_t",0,0,document.body.clientWidth-1,1,"#777777");
		drawLine(window,id+"_l",0,0,1,nTop,"#777777");
		drawLine(window,id+"_b",0,nTop,document.body.clientWidth-1,1,"#777777");
		drawLine(window,id+"_r",document.body.clientWidth-2,0,1,nTop,"#777777");
		_setValue(id,"draw",1);
	}

	function JLToolBar_setSize(id,nLeft,nTop,nWidth,nHeight)
	{
		if (id == null)
			return;
		// alert("JLToolBar_setSize : " + id+":"+nLeft+":"+nTop+":"+nWidth+":"+nHeight);
		if (_getValue(id,"draw") <= 0)
		{
			if (JLToolBar_drawObj != null)
				JLToolBar_drawObj(id);
		}
			
		nTop = 0;
		nLeft = 0;
		var nCount = Number(_getValue(id,"childcount"));
		for (var i=0;i<nCount;i++)
		{
			nTop += 2;
			var sChild = _getArrayValue(id,"child",i);
			if (sChild == "undefined" || sChild == null || sChild == "")
				continue;

			var nPosLeft = nLeft;
			var nPosTop = nTop;
			var nItemLeft = _getValue(sChild,"left");
			if (nItemLeft > 0)
				nPosLeft = nItemLeft;
			var nItemTop = _getValue(sChild,"top");
			if (nItemTop > 0)
				nPosTop = nItemTop;
			
			var nItemHeight = _getValue(sChild,"height");
			var sClass = _getValue(sChild,"class");
			if (sClass != "undefined" && sClass != "" && sClass != null)
			{
				var sExp = "window."+sClass+"_setSize";
				if (eval(sExp) != null)
				{
					sExp = "window."+sClass+"_setSize(sChild,nPosLeft,nPosTop,nWidth-nPosLeft-4,nItemHeight);";
					eval(sExp);
				}
				nTop += nItemHeight;
				if (i < nCount-1)
					moveLine(window,id+"_i"+i,2,nTop,document.body.clientWidth-3,1,"#777777");
			}
		}
		moveLine(window,id+"_t",0,0,document.body.clientWidth-1,1,"#777777");
		moveLine(window,id+"_l",0,0,1,nTop,"#777777");
		moveLine(window,id+"_b",0,nTop,document.body.clientWidth-1,1,"#777777");
		moveLine(window,id+"_r",document.body.clientWidth-2,0,1,nTop,"#777777");
		_setValue(id,"height",nTop);
	}

	function _setBarSize(id,width)
	{
		var obj = document.getElementById(id);
		if (obj != null)
			obj.style.width = width;
	}
</script>

<%!
public class JLToolBar extends JLHttp
{	
	int m_nCount;
	Vector m_vBarInfo;
	
	JLToolBar(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		m_nCount = 0;
		m_vBarInfo = new Vector();
	}

	void init(String sName)
	{
		setName(sName);
	}
	
	void addBar(String sName,JLHttp oBar,int nLeft,int nTop,int nWidth,int nHeight)
	{
		JLRcd rcd = new JLRcd();
		rcd.SetValue("name",sName);
		rcd.SetValue("top",nTop);
		rcd.SetValue("left",nLeft);
		rcd.SetValue("width",nWidth);
		rcd.SetValue("height",nHeight);
		addChild(oBar);
		m_vBarInfo.addElement(rcd);
	}
	
	void addBar(String sName)
	{
		print("<script>m_sToolBar_"+m_sName+"_"+m_nCount+" = \""+sName+"\";</script>");
		m_nCount ++;
	}

	void printBarBegin(String sName,int nHeight)
	{
		addBar(sName);
		
		print("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">");
		print("<tr height=1>");
		print("<td width=1 bgcolor=\"#ffffff\">");
		print("</td>");
		print("<td bgcolor=\"#ffffff\">");
		print("</td>");
		print("<td width=1 bgcolor=\"#777777\">");
		print("</td>");
		print("</tr>");
		print("<tr height="+nHeight+">");
		print("<td id=td_left_border width=1 bgcolor=\"#ffffff\"><img alt border=0 height="+nHeight+" src=images/trans.gif width=1></td>");
		print("<td bgcolor=\"#d4d0c8\" id=\""+sName+"\" height="+nHeight+"><img alt border=0 height=1 src=images/trans.gif width=1><br>");
	}		

	void printBarEnd(boolean bFinal)
	{
		print("</td>");
		print("<td id=td_right_border width=1 bgcolor=\"#777777\">");
		print("</td>");
		print("</tr>");
		print("<tr height=1>");
		if (bFinal)
			print("<td width=1 bgcolor=\"#777777\">");
		else
			print("<td width=1 bgcolor=\"#ffffff\">");
		print("</td>");
		print("<td bgcolor=\"#777777\">");
		print("</td>");
		print("<td width=1 bgcolor=\"#777777\">");
		print("</td>");
		print("</tr>");
		print("</table>");
	}
	
	void printToolBarStart()
	{
		print("<script>m_nToolBarCount_"+m_sName+" = \"0\";</script>");

		print("<table id="+m_sName+" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" width=\"0\">");
		print("<tr height=1>");
		print("<td width=1 bgcolor=\"#666666\">");
		print("</td>");
		print("<td id="+m_sName+"_Cell width=10 bgcolor=\"#666666\">");
		print("</td>");
		print("<td width=1 bgcolor=\"#ffffff\">");
		print("</td>");
		print("</tr>");
		print("<tr>");
		print("<td width=1 bgcolor=\"#666666\">");
		print("</td>");
		print("<td>");
	}
	
	void printToolBarEnd()
	{
		print("</td>");
		print("<td width=1 bgcolor=\"#ffffff\">");
		print("</td>");
		print("</tr>");
		print("<tr height=1>");
		print("<td colspan=3 width=1 bgcolor=\"#ffffff\"></td>");
		print("</tr>");
		print("</table>");

		print("<script>m_nToolBarCount_"+m_sName+" = \""+m_nCount+"\";</script>");
	}
	
	public void printObj(int nLeft,int nTop)
	{
		printStream("<!-- JLToolBar start : "+m_sName+" -->");
		printChildScript();
		int nCount = m_vChild.size();
		
		nTop = 2;
		nLeft = 2;
		for (int i=0;i<nCount;i++)
		{
			JLRcd rcd = (JLRcd)m_vBarInfo.elementAt(i);
			if (rcd == null)
				continue;
			int nPosTop = rcd.GetIntValue("top");
			int nPosLeft = rcd.GetIntValue("left");
			int nPosWidth = rcd.GetIntValue("width");
			int nPosHeight = rcd.GetIntValue("height");

			int nDivTop = nTop;
			int nDivLeft = nLeft;
			if (nPosTop > 0)
				nDivTop = nPosTop;
			if (nPosLeft > 0)
				nDivLeft = nPosLeft;
				
			String sChild = rcd.GetStrValue("name");
			JLHttp oCtrl = (JLHttp)m_vChild.elementAt(i);
			oCtrl.printObj(nDivLeft,nDivTop);
			nTop += 2;
			if (nPosHeight > 0)
				nTop += nPosHeight;
		}
		printStream("<!-- JLToolBar end : "+m_sName+" -->");
	}
}
%>

<!-- JLToolBar.jsp end -->
