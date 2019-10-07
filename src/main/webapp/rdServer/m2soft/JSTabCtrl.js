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


var m_aTab = new Array();
var m_nTab = 0;
var m_sBtnFace = "";
var m_sFaceDark = "";
var m_sFaceBright = "";

function JLTabCtrl_addTab(sName)
{
	var obj = new Object();
	obj.m_nCount = 0;
	obj.m_aPage = new Array();
	obj.sName = sName;
	m_aTab[m_nTab] = obj;
	m_nTab ++;
	return obj;
}

function JLTabCtrl_getTab(sName)
{
	for (var i=0;i<m_nTab;i++)
	{
		var obj = m_aTab[i];
		if (obj == null)
			continue;
		if (obj.sName == sName)
			return obj;
	}
	return null;
}

// String sName,int nWidth,int nImgTopMargin,String sButtonText,String sAction,String sIcon,int nHeight
function JLTabCtrl_addTabPage(sTabName,sName,nWidth,nImgTopMargin,sButtonText,sAction,sIcon,nHeight,sSrc,bLoad)
{
	var tab = JLTabCtrl_getTab(sTabName);
	if (tab == null)
		return;
	var oPage = new Object();
	tab.m_aPage[tab.m_nCount] = oPage;
	oPage.sName = sName;
	oPage.nWidth = nWidth;
	oPage.nImgTopMargin = nImgTopMargin;
	oPage.sButtonText = sButtonText;
	oPage.sAction = sAction;
	oPage.sIcon = sIcon;
	oPage.nHeight = nHeight;
	oPage.sSrc = sSrc;
	oPage.bLoad = bLoad;
	tab.m_nCount ++;
}

function JLTabCtrl_ontabmouseup(sTabName,id)
{
	JLTabCtrl_focusPage(sTabName,id);
	
	if (window._onFocusPage != null)
		_onFocusPage(sTabName,id);
}

function JLTabCtrl_getPageFrameName(sTabName,id)
{
	var tab = JLTabCtrl_getTab(sTabName);
	if (tab == null)
		return null;
		
	var elmt;
	for (i=0;i<tab.m_nCount;i++)
	{
		var oPage = tab.m_aPage[i];
		var objname = oPage.sName;
		if (id == objname)
		{
			return objname;
			// elmt = document.getElementById(objname);
		}
	}
	return null;
}

function JLTabCtrl_focusPage(sTabName,id)
{
	var tab = JLTabCtrl_getTab(sTabName);
	if (tab == null)
		return;
	var elmt;
	for (i=0;i<tab.m_nCount;i++)
	{
		var oPage = tab.m_aPage[i];
		var sSrc = oPage.sSrc;
		var bLoad = oPage.bLoad;
		var objname = oPage.sName;
		if (id == objname)
		{
			if (i == 0)
			{
				JLTabCtrl_onFocusL(objname);
				elmt = document.getElementById(objname); if (elmt != null) elmt.style.visibility = "visible";
			}
			else
			{
				JLTabCtrl_onFocusR(objname);
				elmt = document.getElementById(objname); if (elmt != null) elmt.style.visibility = "visible";
			}
			if (bLoad == 0)
			{
				elmt.src = sSrc;
				oPage.bLoad = true;
			}
		}
		else
		{
			if (i == 0)
			{
				JLTabCtrl_onBlurL(objname);
				elmt = document.getElementById(objname); if (elmt != null) elmt.style.visibility = "hidden";
			}
			else
			{
				JLTabCtrl_onBlurR(objname);
				//alert(objname);
				elmt = document.getElementById(objname); if (elmt != null) elmt.style.visibility = "hidden";
			}
		}
	}
}

function JLTabCtrl_onBlurR(id)
{
	var obj = null;
	elmt = document.getElementById("td_btm1_1_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_2_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_3_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_4_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_5_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_6_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	
	elmt = document.getElementById("td_btm2_1_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_2_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_3_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_4_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_5_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_6_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;

	elmt = document.getElementById("fnt_"+id); if (elmt != null) elmt.color = "#aaaaaa";							
}

function JLTabCtrl_onFocusR(id)
{
	var elmt;
	elmt = document.getElementById("td_btm1_1_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_2_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm1_3_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm1_4_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm1_5_"+id); if (elmt != null) elmt.style.background = m_sFaceDark;
	elmt = document.getElementById("td_btm1_6_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	
	elmt = document.getElementById("td_btm2_1_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_2_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_3_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm2_4_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm2_5_"+id); if (elmt != null) elmt.style.background = m_sFaceDark;
	elmt = document.getElementById("td_btm2_6_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;

	elmt = document.getElementById("fnt_"+id); if (elmt != null) elmt.color = "#000000";							
}

function JLTabCtrl_onBlurL(id)
{
	var elmt;
	elmt = document.getElementById("td_btm1_1_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_2_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_3_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_4_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_5_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_6_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	
	elmt = document.getElementById("td_btm2_1_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm2_2_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_3_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_4_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_5_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_6_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;

	elmt = document.getElementById("fnt_"+id); if (elmt != null) elmt.color = "#aaaaaa";							
}

function JLTabCtrl_onFocusL(id)
{
	var elmt;
	elmt = document.getElementById("td_btm1_1_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm1_2_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm1_3_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm1_4_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm1_5_"+id); if (elmt != null) elmt.style.background = m_sFaceDark;
	elmt = document.getElementById("td_btm1_6_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	
	elmt = document.getElementById("td_btm2_1_"+id); if (elmt != null) elmt.style.background = "#cccccc";
	elmt = document.getElementById("td_btm2_2_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;
	elmt = document.getElementById("td_btm2_3_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm2_4_"+id); if (elmt != null) elmt.style.background = m_sBtnFace;
	elmt = document.getElementById("td_btm2_5_"+id); if (elmt != null) elmt.style.background = m_sFaceDark;
	elmt = document.getElementById("td_btm2_6_"+id); if (elmt != null) elmt.style.background = m_sFaceBright;

	elmt = document.getElementById("fnt_"+id); if (elmt != null) elmt.color = "#000000";							
}
