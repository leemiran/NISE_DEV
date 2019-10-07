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

<!-- JLPopupMenu.jsp start -->

<script language="javascript">

	function JLPopupMenu_onMouseOver(id,itemid)
	{
		var sTdName = id+"_td_bg_"+itemid;
		var sFntName = id+"_fnt_"+itemid;
		var obj = document.getElementById(sTdName); if (obj != null) obj.style.background = "#122071";
		obj = document.getElementById(sFntName); if (obj != null) obj.color = "#ffffff";
	}

	function JLPopupMenu_onMouseOut(id,itemid)
	{
		var sTdName = id+"_td_bg_"+itemid;
		var sFntName = id+"_fnt_"+itemid;
		var obj = document.getElementById(sTdName); if (obj != null) obj.style.background = "#ffffff";
		obj = document.getElementById(sFntName); if (obj != null) obj.color = "#000000";
	}

	function _setMenuItemSize(sID,nLeft,nTop,nWidth,nHeight)
	{
		if (sID != "")
			_setPopupMenuSize(sID,nLeft,Number(nTop)+25,nWidth,nHeight);
	}
	
	function _setPopupMenuSize(sID,nLeft,nTop,nWidth,nHeight)
	{
		var nItemTop = Number(nTop)-2;
		_setValue(sID,"top",nItemTop);
	}
	
	function _setObjSize(sID,nLeft,nTop,nWidth,nHeight)
	{
		var obj = null;
		obj = document.getElementById(sID);
		if (obj != null)
		{
			obj.style.left = nLeft;
			obj.style.top = nTop;
		}
		obj = document.getElementById(sID+"_width");
		if (obj != null)
			obj.style.width = nWidth;
		obj = document.getElementById(sID+"_height");
		if (obj != null)
			obj.style.height = nHeight;
	}
	
	function _setPopupMenuVisible(sID,sVisible)
	{
		var sParent = _getValue(sID,"parent");
		if (_getValue(sParent,"child") == null)
			return;

		if (sVisible == 'show')
			onmouseover_menubarbtn_popup(sParent)
		if (sVisible == 'hide')
			onmouseout_menubarbtn_popup(sParent)
		showHideLayersEx(sID,'',sVisible,sID);
		showHideLayersEx(sParent,'',sVisible,sParent);
	}
</script>
