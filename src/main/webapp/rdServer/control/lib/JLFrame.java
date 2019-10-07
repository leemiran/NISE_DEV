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

<!-- JLFrame.java : start -->

<script language="javascript">

	function JLFrame_setSize(id,nLeft,nTop,nWidth,nHeight)
	{
		// alert("JLFrame_setSize : " + id+":"+nLeft+":"+nTop+":"+nWidth+":"+nHeight);
		_setStyle(id,"div","left",nLeft);
		_setStyle(id,"div","top",nTop);
		// _setStyle(id,"div","width",nWidth);
		// _setStyle(id,"div","height",nHeight);
		_setStyle(id,"","width",nWidth);
		_setStyle(id,"","height",nHeight);

		_setValue(id,"left",nLeft);
		_setValue(id,"top",nTop);
		_setValue(id,"width",nWidth);
		_setValue(id,"height",nHeight);

		if (eval("window."+id) != null)
		{
			// alert("window."+id);
			if (eval("window."+id+"._setSize") != null)
			{
				eval("window."+id+"._setSize()");
			}
		}
		var nToolBarHeight = 0;
		var sToolBar = _getValue(id,"toolbar");
		if (sToolBar != null)
		{
			// alert("toolbar : " + sToolBar);
			if (window.JLToolBar_setSize != null)
			{
				JLToolBar_setSize(sToolBar,2,2,nWidth,0);
				nToolBarHeight = Number(_getValue(sToolBar,"height"));
			}
		}
		var nPosLeft = 0;
		var nPosTop = 0;
		var nCount = Number(_getValue(id,"childcount"));
		for (var i=0;i<nCount;i++)
		{
			var sChild = eval(id+"_child["+i+"]");
			if (sChild == "undefined" || sChild == null || sChild == "")
				continue;
				
			var nItemWidth = Number(nWidth) - Number(nPosLeft);
			var nItemHeight = Number(nHeight) - Number(nPosTop) - Number(nToolBarHeight+2);
			nPosTop += Number(nToolBarHeight+2);

			var sClass = _getValue(sChild,"class");
			var sExp = "window."+sClass+"_setSize";
			if (eval(sExp) != null)
			{
				sExp = "window."+sClass+"_setSize(sChild,nPosLeft,nPosTop,nItemWidth,nItemHeight);";
				eval(sExp);
			}
		}
	}

</script>

<%!
public class JLFrame extends JLHttp
{
	String m_sSrc;
	String m_sHtml;
	String m_sToolBar;

	public JLFrame(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		setClassName("JLFrame");
		m_sSrc = null;
		m_sHtml = null;
		m_sToolBar = null;
	}

	void setBorderStyle(int nStyle)
	{
		m_rStyle.SetValue("borderstyle",nStyle);
	}

	void setSrc(String sSrc)
	{
		m_sSrc = sSrc;
	}

	public void init(JLHttp oParent,String sName,int nLeft,int nTop,int nWidth,int nHeight,int nRight,int nBottom)
	{
		setParent(oParent);
		setName(sName);
		setPosition(nLeft,nTop,nWidth,nHeight,nRight,nBottom);
	}
	
	public void printChildScript()
	{
		super.printChildScript();
		print("<script language=\"javascript\">");
		declVar(m_sName,"toolbar",m_sToolBar);
		print("</script>");			
	}

	public void addToolBar(JLHttp toolbar)
	{
		if (toolbar == null)
			return;
		m_sToolBar = toolbar.getName();
	}

	public void setHtml(String sHtml)
	{
		m_sHtml = sHtml;
	}

	public void printObj(int nLeft,int nTop)
	{
		printStream("<!-- JLFrame start : "+m_sName+" -->");
		m_nLeft = nLeft;
		m_nTop = nTop;
		if (m_sSrc != null)
		{
			String sHtml = "<div id=\""+m_sName+"_div\" style=\"POSITION: absolute; top:"+m_nTop+"px; left:"+m_nLeft+"px; width:"+m_nWidth+"px; height:"+m_nHeight+"px; \"><IFRAME id=\""+m_sName+"\" NAME=\""+m_sName+"\" frameborder=\"0\" src=\""+m_sSrc+"\" scrolling=no STYLE=\"LEFT: 0px; top: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH:"+m_nWidth+"px; height:"+m_nHeight+"px\"></IFRAME>";
			printStream(sHtml);
			printChildScript();
			printStream("</div>");
		}
		else
		{
			String sHtml = "<div id=\""+m_sName+"_div\" style=\"POSITION: absolute; top:"+m_nTop+"px; left:"+m_nLeft+"px; width:"+m_nWidth+"px; height:"+m_nHeight+"px; \">";
			printStream(sHtml);
			printStream("<!-- "+m_sName+" html -->");
			if (!JLString.IsEmpty(m_sHtml))
				printStream(m_sHtml);
			printChildHtml(0,0);
			printStream("</div>");
		}
		printStream("<!-- JLFrame end : "+m_sName+" -->");
	}
}
%>


<!-- JLFrame.java : end -->

