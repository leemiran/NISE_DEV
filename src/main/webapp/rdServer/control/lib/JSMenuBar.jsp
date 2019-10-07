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

<!-- JLMenuBar.jsp start -->

<script>
function JLMenuBar_setSize(sID,nLeft,nTop,nWidth,nHeight)
{
	_setValue(sID,"left",nLeft);
	_setValue(sID,"top",nTop);
	_setObjSize(sID,nLeft,nTop,nWidth,nHeight);
	
	// 메뉴항목에 대한 Popup Menu의 위치를 지정합니다.
	var nCount = Number(_getValue(sID,"childcount"));
	for (var i=0;i<nCount;i++)
	{
		var sChild = _getArrayValue(sID,"child",i);
		if (sChild != "" && sChild != "undefined" && sChild != null)
			_setMenuItemSize(sChild,nLeft,nTop,0,0);
	}
}

function onMenu(left,top,width,height,id)
{
	var obj = document.getElementById(id);;
	if (obj != null)
	{
		obj.style.height = height;
		obj.style.width = width;
		obj.style.left = left;
		obj.style.top = top;
		// // alert("top : " + top);
		showHideLayers(id,'','show','');
		if (window._onMenuEx != null)
			window._onMenuEx(id);
	}
}
</script>

<script>
	var sBtnFaceColor = "<%=m_sBtnFace%>";
	function onmouseover_menubarbtn(id)
	{
		{
			var obj = document.getElementById(id);
			// alert(obj.outerHTML);
		}
		// alert(id);
		if (_getValue(id,"child") != "")
		{
			onmouseover_menubarbtn_popup(id);
			return;
		}

		var obj;
		obj = document.getElementById("td_lefttop_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_top0_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_top_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_righttop_"+id); if (obj != null) obj.style.background = "#ffffff"

		obj = document.getElementById("td_left_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_right_"+id); if (obj != null) obj.style.background = "#777777"

		obj = document.getElementById("td_leftbottom_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_bottom0_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_bottom_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_rightbottom_"+id); if (obj != null) obj.style.background = "#777777"

		if (window._onMenuEx != null)
			window._onMenuEx(id);
	}

	function onmouseup_menubarbtn(id)
	{
		if (_getValue(id,"child") != "")
		{
			onmouseup_menubarbtn_popup(id);
			return;
		}
		onmouseover_menubarbtn(id);
	}

	function onmousedown_menubarbtn(id)
	{
		if (_getValue(id,"child") != "")
		{
			onmousedown_menubarbtn_popup(id);
			return;
		}
		var obj;
		obj = document.getElementById("td_lefttop_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_top0_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_top_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_righttop_"+id); if (obj != null) obj.style.background = "#777777"

		obj = document.getElementById("td_left_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_right_"+id); if (obj != null) obj.style.background = "#ffffff"

		obj = document.getElementById("td_leftbottom_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_bottom0_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_bottom_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_rightbottom_"+id); if (obj != null) obj.style.background = "#ffffff"
	}

	function onmouseout_menubarbtn(id)
	{
		if (_getValue(id,"child") != "")
		{
			onmouseout_menubarbtn_popup(id);
			return;
		}
		var obj;
		obj = document.getElementById("td_lefttop_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_top0_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_top_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_righttop_"+id); if (obj != null) obj.style.background = sBtnFaceColor;

		obj = document.getElementById("td_left_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_right_"+id); if (obj != null) obj.style.background = sBtnFaceColor;

		obj = document.getElementById("td_leftbottom_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_bottom0_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_bottom_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_rightbottom_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
	}

	// popupmenu가 지원되는 경우
	function onmouseover_menubarbtn_popup(id)
	{
		var obj;
		obj = document.getElementById("td_lefttop_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_top0_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_top_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_righttop_"+id); if (obj != null) obj.style.background = "#777777"

		obj = document.getElementById("td_left_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_icon_"+id); if (obj != null) obj.style.background = "#B0C4DE"
		obj = document.getElementById("td_text_"+id); if (obj != null) obj.style.background = "#B0C4DE"
		obj = document.getElementById("td_right_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_rightedge_"+id); if (obj != null) obj.style.background = "#999999"

		obj = document.getElementById("td_leftbottom_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_bottom0_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_bottom_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_rightbottom_"+id); if (obj != null) obj.style.background = "#777777"
	}

	function onmouseup_menubarbtn_popup(id)
	{
		onmouseover_menubarbtn_popup(id);
	}

	function onmousedown_menubarbtn_popup(id)
	{
		var obj;
		obj = document.getElementById("td_lefttop_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_top0_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_top_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_righttop_"+id); if (obj != null) obj.style.background = "#777777"

		obj = document.getElementById("td_left_"+id); if (obj != null) obj.style.background = "#777777"
		// obj = document.getElementById("td_right_"+id); if (obj != null) obj.style.background = "#ffffff"

		obj = document.getElementById("td_leftbottom_"+id); if (obj != null) obj.style.background = "#777777"
		obj = document.getElementById("td_bottom0_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_bottom_"+id); if (obj != null) obj.style.background = "#ffffff"
		obj = document.getElementById("td_rightbottom_"+id); if (obj != null) obj.style.background = "#ffffff"
	}

	function onmouseout_menubarbtn_popup(id)
	{
		var obj;
		obj = document.getElementById("td_lefttop_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_top0_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_top_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_righttop_"+id); if (obj != null) obj.style.background = sBtnFaceColor;

		obj = document.getElementById("td_left_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_icon_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_text_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_right_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_rightedge_"+id); if (obj != null) obj.style.background = sBtnFaceColor;

		obj = document.getElementById("td_leftbottom_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_bottom0_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_bottom_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
		obj = document.getElementById("td_rightbottom_"+id); if (obj != null) obj.style.background = sBtnFaceColor;
	}
</script>
