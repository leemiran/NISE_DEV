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

<!-- JLMultiFrame.java : start -->
<script>
	var m_sFrameName = new Array();
	var m_sFrameSrc = new Array();
	var m_sFrameArgName = new Array();
	var m_sFrameArgValue = new Array();
	var m_nFrame = 0;
	var m_bleftmenu = true;
	var m_sFocusFrame = "";

	function addFrame(sName,sSrc)
	{
		var nIdx = m_nFrame;
		m_sFrameName[nIdx] = sName;
		m_sFrameSrc[nIdx] = sSrc;
		m_nFrame ++;
		return nIdx;
	}

	function createFrame(id,sName,sSrc)
	{
		var nIdx = addFrame(sName,sSrc);
		obj = document.getElementById(sName);
		if (obj == null)
		{
			var oPos = document.getElementById("elFramePos");
			if (oPos != null)
			{
				var nLeft = 0;
				var nTop = 0;
				var nWidth = Number(_getValue(id,"width"));
				var nHeight = Number(_getValue(id,"height"));
				var sHtml = "<IFRAME id=\""+sName+"\" NAME=\""+sName+"\" frameborder=\"0\" src=\""+sSrc+"\" scrolling=yes style=\"LEFT: "+nLeft+"px; top: "+nTop+"px; POSITION: absolute; VISIBILITY: hidden; WIDTH: "+nWidth+"px; height: "+nHeight+"px; \"></IFRAME>";
				sHtml += "<div id=elFramePos></div>";
				oPos.outerHTML = sHtml;
			}
		}
		return nIdx;
	}

	function printFrames()
	{
		var obj = null;
		for (i=0;i<m_nFrame;i++)
		{
			var sName = m_sFrameName[i];
			var sSrc = m_sFrameSrc[i];
			obj = document.getElementById(sName);
			if (obj == null)
			{
				var oPos = document.getElementById("elFramePos");
				if (oPos != null)
				{
					var sHtml = "<IFRAME id=\""+sName+"\" NAME=\""+sName+"\" frameborder=\"0\" src=\""+sSrc+"\" scrolling=yes style=\"LEFT: 205px; top: 56px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: hidden; WIDTH: 500px; height: 500px; \"></IFRAME>";
					sHtml += "<div id=elFramePos></div>";
					oPos.outerHTML = sHtml;
				}
			}
		}
	}

	function getFrameIdx(sFrame)
	{
		var obj = null;
		for (i=0;i<m_nFrame;i++)
		{
			if (m_sFrameName[i] == sFrame)
			{
				return i;
			}
		}
		return -1;
	}
	
	function setFrameSrc(id,sFrame,sSrc)
	{
		// alert(sFrame + ":"+ sSrc);
		
		if (sFrame == null || sFrame == "undefined" || sFrame == "")
			return;
		var nIdx = getFrameIdx(sFrame);
		if (nIdx < 0)
			nIdx = createFrame(id,sFrame,"");
		m_sFrameSrc[nIdx] = sSrc;
		
		var form = document.fthis;
		form.target = sFrame;
		form.action = sSrc;
		form.method = "post";
		form.submit();
	}

	function findFrameBySrc(sSrc)
	{
		var obj = null;
		for (i=0;i<m_nFrame;i++)
		{
			if (m_sFrameSrc[i] == sSrc)
			{
				obj = document.getElementById(m_sFrameName[i]);
				return obj;
			}
		}
		return null;
	}
	
	function findFrameNameBySrc(sSrc)
	{
		var obj = null;
		for (i=0;i<m_nFrame;i++)
		{
			if (m_sFrameSrc[i] == sSrc)
			{
				return m_sFrameName[i];
			}
		}
		return null;
	}
	
	function setVisible(id,sName)
	{
		var obj = null;
		for (i=0;i<m_nFrame;i++)
		{
			obj = document.getElementById(m_sFrameName[i]);
			if (obj != null && m_sFrameName[i] != sName)
			{
				obj.style.visibility = "hidden";
				obj.style.width = 0;
				obj.style.height = 0;
			}
		}
		m_sFocusFrame = sName;
		JLMultiFrame_setFrameSize(id);
	}

	function getUrlFileName(sArg)
	{
		if (sArg == null || sArg == "undefined" || sArg == "")
			return "";
		var sURL = new String(sArg);
		var nIdx = sURL.lastIndexOf("/");
		if (nIdx >= 0)
		{
			var nLen = sURL.length;
			sRet = sURL.substring(nIdx + 1,nLen);
			return sRet;
		}
		return sURL;
	}
	
	function JLFrame_getFrame(sFrame)
	{
		var nIdx = getFrameIdx(sFrame);
		if (nIdx < 0)
			return null;
		return eval("window."+sFrame);
	}

	function _openFrame(id,sFrame,sSrc)
	{
		var nIdx = getFrameIdx(sFrame);
		if (nIdx < 0)
			setFrameSrc(id,sFrame,sSrc);
		else
		{
			var obj = document.frames(sFrame);
			if (obj != null)
			{
				var sUrlFile = getUrlFileName(obj.document.URL);
				if (sSrc != sUrlFile)
					setFrameSrc(id,sFrame,sSrc);
			}
		}
		setVisible(id,sFrame);
	}

	function JLMultiFrame_setFrameSize(id)
	{
		if (m_sFocusFrame == null || m_sFocusFrame == "")
			return;
		var elmt = document.getElementById(m_sFocusFrame);
		if (elmt != null)
		{
			elmt.style.visibility = "visible";
			var nLeft = Number(_getValue(id,"left"));
			var nTop = Number(_getValue(id,"top"));
			var nWidth = Number(_getValue(id,"width"));
			var nHeight = Number(_getValue(id,"height"));

			// alert(nWidth + " - "+ nHeight);
			if (nWidth != NaN && nWidth > 0)
				elmt.style.width = nWidth;
			if (nHeight != NaN && nHeight > 0)
				elmt.style.height = nHeight;
		}
	}

	function JLMultiFrame_setSize(id,nLeft,nTop,nWidth,nHeight)
	{
		JLFrame_setSize(id,nLeft,nTop,nWidth,nHeight);
		JLMultiFrame_setFrameSize(id);
	}


</script>

<%!
public class JLMultiFrame extends JLFrame
{
	public JLMultiFrame(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		setClassName("JLMultiFrame");
	}
}
%>


<!-- JLMultiFrame.java : end -->

