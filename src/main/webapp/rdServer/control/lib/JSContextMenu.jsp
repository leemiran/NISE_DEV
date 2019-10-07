
<div id=elContextMenuPos></div>	
<div id=elContextMenuCtrl style="POSITION: absolute; top:-100px; left:0px; width:0px; height:0px;"><form name=fContextMenuCtrl><input type=text value="" name="ifocus" onblur="javascript:JLContextMenu_onBlur();"></form></div>

<!-- JSContextMenu.jsp start -->

<script>
	function getMainFrame(sName)
	{
		if (this == parent)
			return null;
		var oWnd = window;
		while (true)
		{
			if (oWnd == null)
				break;
			if (oWnd.name == sName)
				return oWnd;
			if (oWnd.parent == null)
				break;
			if (oWnd == oWnd.parent)
				break;
			oWnd = oWnd.parent;
		}
		return null;
	}
	
	function getMainFrameForm()
	{
		var wMainFrame = getMainFrame();
		return wMainFrame.document.fthis;
	}
</script>

<script language="javascript">
	if (m_obj == null)
		var m_obj = new Object();

	function _isContextMenu()
	{
		return true;
	}

	var m_sContextMenuContainer = "ContextMenu";
	var m_sContextMenuName = "";
	var m_sContextMenuHtml = "";
	var m_nContextMenuItem = 0;
	var m_nContextMenuWidth = 0;
	var m_oContextMenuWnd = null;
	var m_oContextMenu_Handler = new Array(64);

	// window.abc.asdf.df
	function getWindowPath(sParent)
	{
		var sPath = "";
		var oWnd = window;
		while (true)
		{
			if (oWnd == null)
				break;
			if (oWnd.name == sParent)
				return "window."+sPath;
			if (oWnd.parent == null)
				break;
			if (sPath == "")
				sPath = oWnd.name;
			else
			{
				var sTmp = sPath;
				sPath = oWnd.name+"."+sTmp;
			}
			oWnd = oWnd.parent;
		}
		return "window."+sPath;
	}

	function getAbsLeft(sParent)
	{
		var nDelta = 0;
		var nLeft = 0;
		var oWnd = window;
		while (true)
		{
			if (oWnd == null)
				break;
			if (oWnd.name == sParent)
				return nLeft;
			if (oWnd.parent == null)
				break;
			if (oWnd.parent.name == "")
				break;
			if (oWnd.parent.name == sParent)
				return nLeft;
			// alert("oWnd.screenLeft : " + oWnd.screenLeft);
			// alert("oWnd.parent.screenLeft : " + oWnd.parent.screenLeft);

			nDelta = oWnd.screenLeft - oWnd.parent.screenLeft;
			nLeft += nDelta;
			oWnd = oWnd.parent;
		}
		return nLeft;
	}

	function getAbsTop(sParent)
	{
		var nDelta = 0;
		var nTop = 0;
		var oWnd = window;
		while (true)
		{
			if (oWnd == null)
				break;
			if (oWnd.name == sParent)
				return nTop;
			if (oWnd.parent == null)
				break;
			if (oWnd.parent.name == "")
				break;
			if (oWnd.parent.name == sParent)
				return nTop;
			nDelta = oWnd.screenTop - oWnd.parent.screenTop - oWnd.document.body.scrollTop;
			nTop += nDelta;
			oWnd = oWnd.parent;
		}
		return nTop;
	}

	function JLContextMenu_getContextMenuContainer()
	{
		var oContainer = window;
		var oWnd = window;
		while (true)
		{
			// alert("JLContextMenu_getContextMenuContainer : " + oWnd.name);
			if (oWnd == null)
				break;
			if (oWnd._isContextMenu != null)
				oContainer = oWnd;
			if (oWnd.parent == null)
				break;
			if (oWnd == oWnd.parent)
				break;
			oWnd = oWnd.parent;
		}
		return oContainer;
	}

	function JLContextMenu_onContextMenuEx(id,wndName,wndSrc)
	{
		if (wndSrc == null)
		{
			if (wndSrc.m_obj._debug == 3)
				alert("JLContextMenu_onContextMenuEx : wndSrc is null");
			return;
		}
		if (wndSrc._setContextMenu == null)
		{
			if (wndSrc.m_obj._debug == 3)
				alert("JLContextMenu_onContextMenuEx : function _setContextMenu is not declared");
			return;
		}
		var oContainer = JLContextMenu_getContextMenuContainer();
		if (oContainer == null)
		{
			if (wndSrc.m_obj._debug == 3)
				alert("JLContextMenu_onContextMenuEx : no container includes JSContextMenu.jsp , you have to include JSContextMenu.jsp file in the container");
			return;
		}

		var nLeft = wndSrc.event.screenX - Number(oContainer.screenLeft);
		var nTop = wndSrc.event.screenY - Number(oContainer.screenTop);
		wndSrc._setContextMenu(id,oContainer,nLeft,nTop);
		if (wndSrc.m_obj._debug == 12)
		{
			// debug
			nLeft += 250;
		}
		if (oContainer.JLContextMenu_setSize != null)
		{
			oContainer.JLContextMenu_setSize(id,nLeft,nTop,0,0);
			var form = oContainer.document.fContextMenuCtrl;
			if (form != null)
				form.ifocus.focus();
		}
		// debug
		if (wndSrc.m_obj._debug == 12)
			return;
		return false;
	}

	function JLContextMenu_onBlur()
	{
		JLContextMenu_setHide();
	}

	function JLContextMenu_onMouseOver(id,itemid)
	{
		var sTdName = id+"_td_bg_"+itemid;
		var sFntName = id+"_fnt_"+itemid;
		var obj = document.getElementById(sTdName); if (obj != null) obj.style.background = "#122071";
		obj = document.getElementById(sFntName); if (obj != null) obj.color = "#ffffff";
	}

	function JLContextMenu_onMouseOut(id,itemid)
	{
		var sTdName = id+"_td_bg_"+itemid;
		var sFntName = id+"_fnt_"+itemid;
		var obj = document.getElementById(sTdName); if (obj != null) obj.style.background = "#ffffff";
		obj = document.getElementById(sFntName); if (obj != null) obj.color = "#000000";
	}

	function JLContextMenu_setHide()
	{
		var id = m_sContextMenuName;
		var obj = document.getElementById(id);
		if (obj != null)
		{
			obj.style.left = -500;
			obj.style.top = 0;
			obj.style.visibility = "hidden";
		}
	}

	function JLContextMenu_setSize(id,nLeft,nTop,nWidth,nHeight)
	{
		var obj = document.getElementById(id);
		if (obj != null)
		{
			obj.style.left = nLeft;
			obj.style.top = nTop;
			obj.style.visibility = "visible";
		}
	}

	function JLContextMenu_setDebug(nDebug)
	{
		m_obj._debug = nDebug;
	}

	function JLContextMenu_init(oFrameWnd,oWnd,sName,nWidth)
	{
		var sHtml = "";
		sHtml += "<div id=\""+sName+"\" style=\"position:absolute; left:0px; top:0px; z-index:100; visibility: hidden\" > "; //
		sHtml += "<table border=0 cellpadding=\"0\" width=\""+Number(nWidth)+"\" cellspacing=\"0\">"; //
		sHtml += "<tr height=1>"; //
		sHtml += "<td colspan=\"2\" rowspan=\"2\">"; //
		sHtml += "<table border=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse;\" cellspacing=\"0\" width=\""+(Number(nWidth)-1)+"\"  bgcolor=\"#ffffff\" >"; //
		sHtml += "<tr>"; //
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>"; //
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>"; //
		sHtml += "<td width=\""+(Number(nWidth)-1-1-1-1)+"\" bgcolor=\"#666666\"></td>"; //
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>"; //
		sHtml += "</tr>"; //
		sHtml += "<tr>"; //
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>"; //
		sHtml += "<td colspan=2>"; //
		sHtml += "<table width=\""+(Number(nWidth)-3)+"\" bgcolor=\"#ffffff\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"; //
		m_sContextMenuHtml = sHtml;
		m_nContextMenuItem = 0;
		m_nContextMenuWidth = nWidth;
		m_oContextMenuFrameWnd = oFrameWnd;
		m_oContextMenuFrameWnd.m_oContextMenuWnd = oWnd;
		m_oContextMenuFrameWnd.m_sContextMenuName = sName;
		m_sContextMenuName = sName;
	}

	function JLContextMenu_printObj()
	{
		var sHtml = "";
		sHtml += "</table>"; //
		sHtml += "</td>	"; //			
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>"; //
		sHtml += "</tr>"; //
		sHtml += "<tr>"; //
		sHtml += "<td colspan=4 width=\""+(Number(m_nContextMenuWidth)-1-1-1)+"\" bgcolor=\"#666666\"></td>"; //
		sHtml += "</tr>"; //
		sHtml += "</table>"; //
		sHtml += "</td>"; //
		sHtml += "<td width=\"1\" height=\"1\"><img alt border=0 height=1 src=\"images/trans.gif\" width=\"1\"></td>"; //
		sHtml += "</tr>"; //
		sHtml += "<tr>"; //
		sHtml += "<td width=1 bgcolor=\"#999999\" height=\""+(20*Number(m_nContextMenuItem)+3-2)+"\"></td>"; //
		sHtml += "</tr>"; //
		sHtml += "<tr>"; //
		sHtml += "<td width=1></td>"; //
		sHtml += "<td width=\""+(Number(m_nContextMenuWidth)-2)+"\" bgcolor=\"#999999\"></td>"; //
		sHtml += "<td width=\"1\" bgcolor=\"#999999\"></td>"; //
		sHtml += "</tr>"; //
		sHtml += "</table>"; //
		sHtml += "</div>"; //
		m_sContextMenuHtml += sHtml;

		var oPos = m_oContextMenuFrameWnd.document.getElementById("elContextMenuPos");
		if (oPos != null)
		{
			oPos.innerHTML = m_sContextMenuHtml;
		}
	}

	function JLContextMenu_onClick(nIdx)
	{
		var sAction = m_oContextMenu_Handler[nIdx];
		var sExp = "m_oContextMenuWnd."+sAction;
		eval(sExp);
	}

	function JLContextMenu_addItem(nWidth,sIcon,sCaption,sAction)
	{
		if (m_oContextMenuFrameWnd != null)
		{
			var sName = m_sContextMenuName;
			var nIdx = m_nContextMenuItem;
			var sHtml = "";
			sHtml += "<tr style=\"cursor: hand;\">"; //
			sHtml += "<td onmousedown=\"javascript:JLContextMenu_onClick('"+nIdx+"')\" onmouseover=\"javascript:JLContextMenu_onMouseOver('"+sName+"','"+nIdx+"')\" onmouseout=\"javascript:JLContextMenu_onMouseOut('"+sName+"','"+nIdx+"')\" id='"+sName+"_td_bg_"+nIdx+"' height=\"20\" width=\"107\" valign=\"top\"><table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src="+sIcon+"></td><td align=left style=\"text-align:left;\" width="+(Number(nWidth)-25-3)+"><img alt border=0 height=4 src=images/trans.gif width="+(Number(nWidth)-25-3)+"><br><font color='#000000' style=\"FONT: 9pt\" id='"+sName+"_fnt_"+nIdx+"'>"+sCaption+"</font></td></tr></table></td>"; //
			sHtml += "</tr>"; //
			if (m_oContextMenuFrameWnd.m_oContextMenu_Handler == null)
				return;
			m_oContextMenuFrameWnd.m_oContextMenu_Handler[nIdx] = sAction;
			m_sContextMenuHtml += sHtml;
			m_nContextMenuItem ++;
		}
	}
</script>

<!-- JSContextMenu.jsp end -->

