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

<div id="divCheckbox" style="POSITION:absolute; left:0px; top:-1000px; VISIBILITY: hidden;">
<div id="divCheckboxPage" style="POSITION:absolute; left:0px; top:-1000px; VISIBILITY: hidden;"></div>
</div>

<script>
	function setCheckboxHtml(nPage,sHtml)
	{
		var sDiv = "divCheckbox"+nPage;
		var obj = null;
		obj = document.getElementById(sDiv);
		if (obj != null)
		{
			obj.outerHTML = sHtml;
		}
		else
		{
			obj = document.getElementById("divCheckboxPage");
			if (obj != null)
			{
				var sHtmlEx = sHtml;
				sHtmlEx += "<div id=\"divCheckboxPage\" style=\"POSITION:absolute; left:0px; top:-1000px; VISIBILITY: hidden;\"></div>";
				obj.outerHTML = sHtmlEx;
			}
		}
	}
</script>

<script>
	function JLListCtrl_setBackgroundColor(elmt,color)
	{
		if (elmt == null)
			return;
		elmt.style.backgroundColor=color;
	}

	function JLListCtrl_over(obj,id)
	{
		if (obj == null)
			return;
		if (m_obj.m_focusitem == id)
			return;

		var elmt = obj.m_wnd.document.getElementById(id);
		if (elmt == null)
			return;
		JLListCtrl_setBackgroundColor(elmt,obj.m_focuscolor);
		for (var i=0;i<obj.m_nColSize;i++)
		{
			// row_col
			elmt = obj.m_wnd.document.getElementById(id+"_"+i);
			if (elmt != null)
				JLListCtrl_setBackgroundColor(elmt,obj.m_focuscolor);
		}
	}
	
	function JLListCtrl_click(obj,id)
	{
		if (obj == null)
			return;
		var elmt = obj.m_wnd.document.getElementById(id);
		if (elmt == null)
			return;
		for (var i=0;i<obj.m_nColSize;i++)
		{
			var fobj = obj.m_wnd.document.getElementById(m_obj.m_focusitem);
			if (fobj != null)
				JLListCtrl_setBackgroundColor(fobj,'#ffffff');
		}
		m_obj.m_focusitem = id;
		JLListCtrl_setBackgroundColor(elmt,'#B0C4DE');
	}

	function JLListCtrl_out(obj,id)
	{
		if (obj == null)
			return;
		if (m_obj.m_focusitem == id)
			return;
		var idx = Number(id)-obj.m_startidx;
		var elmt = obj.m_wnd.document.getElementById(id);
		if (elmt != null)
			JLListCtrl_setBackgroundColor(elmt,obj.m_linebgcolor[idx]);
		for (var i=0;i<obj.m_nColSize;i++)
		{
			// row_col
			elmt = obj.m_wnd.document.getElementById(id+"_"+i);
			if (elmt != null)
				JLListCtrl_setBackgroundColor(elmt,obj.m_linebgcolor[idx]);
		}
	}

	var m_nSubmitPageStart = 0;
	var m_nSubmitPageCount = 0;
	var m_nSubmitPageSize = 0;

	var m_csFlag = new Array(); 
	var m_csTurn = 1; 

	// 0, Dekker's Algorithm , Operation System, William Stallings, Maxwell Macmillan, International Editions
	function CS_Consumer(obj,nSubmitPageStart)
	{
		m_csFlag[0] = true;
		while (m_csFlag[1])
		{
			if (m_csTurn == 1)
			{
				m_csFlag[0] = false;
				while (m_csTurn == 1)
				{
				}
				m_csFlag[0] = true;
			}
		}

		// critical section
		// critical section start
		// 방금 submit한 page와 다른경우 또 submit해야 한다.
		if (m_nSubmitPageStart != nSubmitPageStart)
		{
			// submit할 page가 있는경우
			// alert(nSubmitPageStart);
			submitPage(obj,m_nSubmitPageStart,m_nSubmitPageCount,m_nSubmitPageSize);
		}
		else
		{
			// 바로 이 위치에서 race condition이 발생할 수 있다. consumer , m_nsubmitpagestart == submitpagestart인경우
			// 0으로 setting하기 전에 Producer가 page를 설정하는 경우 그 page는 보이지 않게 된다.
			// 해당 page를 submit 완료한 경우
			m_nSubmitPageStart = 0;
			m_nSubmitPageCount = 0;
			m_nSubmitPageSize = 0;
		}
		// critical section end

		m_csTurn = 1;
		m_csFlag[0] = false;
		// remainder
	}

	// 1
	function CS_Producer(obj,pagestart,pagecount,pagesize)
	{
		m_csFlag[1] = true;
		while (m_csFlag[0])
		{
			if (m_csTurn == 0)
			{
				m_csFlag[1] = false;
				while (m_csTurn == 0)
				{
				}
				m_csFlag[1] = true;
			}
		}

		// alert("submitpagestart : " + m_nSubmitPageStart);		
		// critical section start
		if (m_nSubmitPageStart == 0)
		{
			m_nSubmitPageStart = pagestart;
			m_nSubmitPageSize = pagesize;
			m_nSubmitPageCount = pagecount;
			submitPage(obj,pagestart,pagecount,pagesize);
		}
		else
		{
			// 바로 이위치에서 race condition이 발생
			// submit page start가 != 0 이 아니라고 해서, pagestart를 설정할려고 하는 순간
			// consumer가 0으로 설정하고 나가면, 현재의 page는 submit할 consumer가 없어 진다.
			// 따라서, page가 보이지 않게 된다.
			// 기존에 다른 page를 submit할라고 했는데, 또 다른 page submit요청이 온경우
			m_nSubmitPageStart = pagestart;
			m_nSubmitPageSize = pagesize;
			m_nSubmitPageCount = pagecount;
		}
		// critical section end
		
		m_csTurn = 0;
		m_csFlag[1] = false;
		// remainder
	}
	
	function getAncestor(wnd,sName)
	{
		if (this == parent)
			return null;
		var oWnd = wnd;
		while (true)
		{
			// alert(oWnd.name);
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

	function _getObj(sName)
	{
		if (sName == null)
			return null;
		var wnd = window;
		if (eval("window."+sName) != null)
		{
			wnd = eval("window."+sName);
		}
		else
		{
			wnd = getAncestor(wnd,sName);
			if (wnd == null)
				return null;
		}
		if (wnd == null)
			return null;
		var gridObj = null;
		if (wnd.m_obj != null)	
			gridObj = wnd.m_obj;
		return gridObj;
	}

	function submitPage(obj,pagestart,pagecount,pagesize)
	{
		if (obj == null)
			return;
		var gridObj = _getObj('grid');
		if (gridObj == null)
			return;
		form = getGridParam();
		if (form != null)
		{
			JLListCtrl_onPage(gridObj,pagestart,pagecount);
			// alert(" summitPage pagestart : "+pagestart);
			
			form.pagestart.value = pagestart;
			// form.pagesize.value = pagesize;
			form.pagecount.value = pagecount;
			form.target = "griddata"+form.pagestart.value;
			if (form._debug.value == 1)
				form.target = "_blank";
			form.method = "post";
			form.action = form.griddata.value;
			if (gridObj.m_wnd.submitForm != null)
				gridObj.m_wnd.submitForm(form);
		}
	}
</script>

<script>
	function getScrollPageStart(obj)
	{
		if (obj == null)
			return -1;
		// scroll 방향을 결정
		if (obj.m_nPrevScrollTop - obj.m_wnd.document.body.scrollTop < 0)
			obj.m_nScrollDir = 1;	// next
		if (obj.m_nPrevScrollTop - obj.m_wnd.document.body.scrollTop > 0)
			obj.m_nScrollDir = -1;	// prev
		obj.m_nPrevScrollTop = obj.m_wnd.document.body.scrollTop;
		// alert("scrolldir " + obj.m_nScrollDir);

		var form = getGridParam();
		if (form == null)
		{
			alert("form is null @ JLListCtrl_onPage");
			return;
		}
		var gridObj = _getObj('grid');
		if (gridObj == null)
			return -1;
		var nPageSize = form.pagesize.value;
		var nPageCount = 1;
		// default dir 이 next이므로 prev로 약간 이동할 경우 대비 -1을 한다.
		var nCur = parseInt((gridObj.m_nPrevScrollTop/gridObj.m_nLineHeight)/nPageSize);
		var nStart = nCur-1;
		if (nStart < 0)
			nStart = 0;
		var nPageStart = nStart;
		while (true)
		{
			if (obj.m_nScrollDir > 0)
			{
				// 8 : 4, 4
				if (nStart + 4 <= nPageStart)
					break;
			}
			if (obj.m_nScrollDir < 0)
			{
				if (nStart - 2 >= nPageStart)
					break;
			}
			var elmt = obj.m_wnd.document.getElementById("divPage"+nPageStart);
			if (elmt == null)
			{
				if (obj.m_nScrollDir < 0)	// prev
				{
					// 아래의 2개
					// nCur에서 -1 한거 1개, cur 1개, 즉 4개
					// for (var i=0;i<2;i++)
					{
						if (nPageStart > 0 && (nStart+1 - nPageStart) < 3)
						{
							var sExp = "divPage"+(nPageStart-1);
							elmt = obj.m_wnd.document.getElementById(sExp);
							if (elmt == null)
								nPageStart --;
							else
								break;
						}
					}
					// alert("pagestart : " +(nStart+1)+ ":"+ nPageStart);
				}
				// onPage(nPageStart,nPageCount);
				return nPageStart;
			}
			nPageStart += obj.m_nScrollDir;	// -1 : prev, 1 : next
			if (nPageStart > obj.m_nTotalCount/nPageSize || nPageStart < 0)
				break;
			// if (nPageStart - obj.m_nPage > 3 || nPageStart - obj.m_nPage < -3)
			// 	break;
		}
		return -1;
	}

	// JPGrid.jsp에서 호출한다.
	// timer를 이용하는 이유는 scroll을 마구하는경우 0.5 second를 기다린다는 의미이다.
	function JLListCtrl_onPageEx(obj)
	{
		if (obj == null)
			return;
		var gridObj = _getObj('grid');
		if (gridObj == null)
			return -1;
		var form = getGridParam();
		if (form == null)
			return;
		// 0 : direct access mode
		if (Number(form._accessmode.value) != 0)
		{
			// sequential access mode
			var nPage = parseInt(Number(gridObj.m_wnd.document.body.scrollTop)/Number(form.pagesize.value*20));
			if (nPage >= Number(form._lastpage.value))
			{
				// sequential access, increased last page
				nPage = Number(form._lastpage.value)+1;
			}
			JLListCtrl_openPage(obj,nPage);
			return;
		}

		// direct access mode
		var nScrollPageStart = getScrollPageStart(gridObj);
		// alert("scroll page start : " + nScrollPageStart);
		if (nScrollPageStart < 0)
			return;
		if (gridObj.m_nPrevScrollPageStart == nScrollPageStart)
			return;
		gridObj.m_nPrevScrollPageStart = nScrollPageStart;
		JLListCtrl_openPage(obj,nScrollPageStart);
	}

	function JLListCtrl_openPage(obj,nPage)
	{
		var gridObj = _getObj('grid');
		if (gridObj == null)
			return -1;

		var nPageCount = 0;
		var nIdx = nPage;
		for (var i=0;i<4;i++)
		{
			elmt = gridObj.m_wnd.document.getElementById("divPage"+(nIdx+i));
			if (elmt == null)
				nPageCount ++;
			else
				break;
		}
		if (nPageCount <= 0)
			return;

		// alert("page start, page count : " + nPage + " : " + nPageCount);
		form = getGridParam();
		CS_Producer(gridObj,nPage,nPageCount,form.pagesize.value);

		// default direction = 1;
		gridObj.m_nScrollDir = 1;	// next
	}
	
	function JLListCtrl_scrollPage(obj,nPage)
	{
		if (obj == null)
			return;
		if (nPage < 0)
			return;
		var oGrid = _getObj('grid');
		if (oGrid == null)
			return -1;

		var sName = "griddata"+nPage;
		var elmt = null;
		elmt = oGrid.m_wnd.document.getElementById(sName);
		if (elmt != null)
		{
			// grid에서 호출합니다. oGrid = grid
			if (eval("oGrid.m_wnd."+sName+".m_obj") != null)
			{
				var sExp = "if (_scrollLeft != null) _scrollLeft(oGrid.m_wnd."+sName+".m_obj,oGrid.m_wnd.document.body.scrollLeft)";
				// alert(sExp);
				eval(sExp);
			}
		}
	}

</script>

<script>
	function getHtml(nPage)
	{
		var obj = document.getElementById("gridPage"+nPage);
		if (obj != null)
			return obj.innerHTML;
		return null;
	}

	// obj은 griddata0의 m_obj이 넘어온다.
	function onGridData(obj,width,height,nCurSegSize,nLineHeight,nTotalCount,nPageStart,nPageCount)
	{
		var oGrid = _getObj('grid');
		if (oGrid == null)
		{
			alert('grid is null @ onGridData');
			return;
		}
		var form = getGridParam();
		if (form != null)
		{
			if (form._accessmode.value == 1)
			{
				// pagestart + count -1 (-1은 보이는 부분에 대한 index이다.)
				form._lastpage.value = nPageStart+nPageCount-1;
				var nScrollBottom = (nPageStart+nPageCount)*form.pagesize.value*20;
				if (oGrid.m_wnd.document.body.scrollTop > nScrollBottom)
				{
					oGrid.m_wnd.document.body.scrollTop = nPageStart*form.pagesize.value*20
				}
			}
		}
		// invalid한 page가 있으면, 이것을 지운다.
		elmt = oGrid.m_wnd.document.getElementById("_divPage"+nPageStart);
		if (elmt != null)
		{
			elmt.removeNode(true);
		}

		// alert('ongriddata' + nPageStart);
		if (obj == null)
		{
			alert("obj is null @ onGridData");
			return;
		}
		// iframe 에 submit을 하구, iframe의 size를 조정하는 경우이다.
		{
			nPageStart = Number(nPageStart);
			var sFrameName = "griddata"+nPageStart;
			var elmt = null;
			var elmt = oGrid.m_wnd.document.getElementById(sFrameName);
			if (elmt != null)
			{
				elmt.style.width = width;
				elmt.style.height = height;
				// alert("line height : " + height + " pagecount : " + nPageCount);
			}
			else
			{
				alert(sFrameName + " is null");
			}
		}
		oGrid.m_nLineHeight = nLineHeight;
		oGrid.m_wnd.document.all.trScroll.style.height = nTotalCount*nLineHeight;
		oGrid.m_nCurSegSize = nCurSegSize;
		oGrid.m_nTotalCount = nTotalCount;
		oGrid.m_bDataReady = true;
	}

	function _onFinishLoad(obj,nLineHeight,nWidth,nHeight,nCurSegSize,nTotal,nPageStart,nPageCount)
	{
		// var sExp = nLineHeight+":"+nWidth+":"+nHeight+":"+nCurSegSize+":"+nTotal+":"+nPageStart+":"+nPageCount+":";
		// alert(sExp);
		
		// alert();
		var oGrid = _getObj('grid');
		if (oGrid != null)
		{
			oGrid.m_wnd.parent.onGridData(obj,nWidth,Number(nHeight),nCurSegSize,nLineHeight,nTotal,nPageStart,nPageCount);
		}
		CS_Consumer(obj,nPageStart);
	}

	function _scrollLeft(obj,nScrollLeft)
	{
		if (obj == null)
			return;
		if (obj.m_wnd != null)
		{
			var obj = obj.m_wnd.document.getElementById("divGridNo");
			if (obj != null)
				obj.style.posLeft = nScrollLeft;
		}
	}

	function JLListCtrl_getScrollLeft(obj,nPage)
	{
		if (obj == null)
			return;
		if (nPage < 0)
			return;
		var elmt = null;
		var oGrid = _getObj('grid');
		if (oGrid == null)
			return;
		// grid에서 호출합니다. obj = grid
		var sName = "griddata"+nPage;
		if (eval("oGrid.m_wnd."+sName) != null)
		{
			if (eval("oGrid.m_wnd."+sName+".m_obj") != null)
			{
				var obj = eval("oGrid.m_wnd."+sName+".m_obj");
				if (obj != null)
				{
					var elmt = obj.m_wnd.document.getElementById("divGridNo");
					if (elmt != null)
						return elmt.style.posLeft;
				}
			}
		}
		return 0;
	}

	function JLListCtrl_onPage(obj,pagestart,nPageCount)
	{
		if (obj == null)
			return;
		var nPageStart = Number(pagestart);
		if (nPageStart < 0)
			nPageStart = 0;

		var form = getGridParam();
		if (form == null)
		{
			alert("form is null @ JLListCtrl_onPage");
			return;
		}
		var nPageSize = form.pagesize.value;
		var nRemainPage = ((obj.m_nTotalCount+nPageSize-1) - (nPageStart*nPageSize))/nPageSize;
		nRemainPage = parseInt(nRemainPage);
		nMax = Number(nPageCount) + nPageStart;
		if (obj.m_nTotalCount > 0 && nRemainPage > 0 && nMax > nRemainPage+nPageStart)
		{
			nMax = nRemainPage+nPageStart;
		}

		var sHtml = "";
		for (var i=nPageStart;i<nMax;i++)
		{
			sHtml += "<div id=\"divPage"+i+"\" style=\"POSITION: absolute; left:0px; top:"+(20+nPageStart*nPageSize*obj.m_nLineHeight)+"px;\">";
			if (i == nPageStart)
			{
				sHtml += "<iframe name=\"griddata"+i+"\" frameborder=\"0\" src=\"\" scrolling=no STYLE=\"HEIGHT:0px; LEFT: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH:0px; \"></IFRAME>";
			}
			sHtml += "</div>";
		}			
		sHtml += "<div id=gridBody></div>";

		var gridObj = _getObj('grid');
		if (gridObj == null)
		{
			alert("gridObj is null JLListCtrl_onPage");
			return;
		}
		var oGridBody = gridObj.m_wnd.document.getElementById("gridBody");
		if (oGridBody != null)
		{
			var elmt = gridObj.m_wnd.document.getElementById("divPage"+nPageStart);
			if (elmt == null)
			{
				oGridBody.outerHTML = sHtml;
			}
		}
		else
		{
			alert("gridBody tag is null");
		}
	}

	function onCheckbox(obj,nPageStart)
	{
		var sHtml = getCheckboxHtml();
		// if (m_nDebug > 0)
		//	alert(sHtml);
		if (sHtml == null)
			return;
		if (parent != this)
		{
			if (parent.parent.setCheckboxHtml != null)
				parent.parent.setCheckboxHtml(nPageStart,sHtml);
		}
	}
	function getCheckboxHtml()
	{
		form = document.fparam;
		if (form == null)
			return null;
		var sHtml = "<div id=\"divCheckbox0\" style=\"POSITION:absolute; left:0px; top:-1000px; VISIBILITY: hidden;\">";
		for (i = 0;i<form.elements.length; i++)
		{
			if (form.elements[i].name == 'cb')
			{
				if (form.elements[i].checked)
				{
					sHtml += "<input type=\"checkbox\" name=\"cb\" value=\""+form.elements[i].value+"\" checked>";
				}
			}
		}
		sHtml += "</div>";
		return sHtml;
	}
	function getCellValue(x,y)
	{
		var id = x+"_"+y;
		var obj = null;
		var obj = document.getElementById(id);
		if (obj != null)
			return obj.value;
		return null;
	}
	function getColCount(nRow)
	{
		var nCol = 0;
		while (true)
		{
			var id = nCol+"_"+nRow;
			var obj = null;
			var obj = document.getElementById(id);
			if (obj == null)
				break;
			nCol ++;
		}
		return nCol;
	}
	function getRowInfo(nRow,oRcd)
	{
		var nCol = 0;
		var nCount = getColCount(nRow);
		var sElements = new Array();
		for (i=0;i<nCount;i++)
		{
			sVal = getCellValue(nCol,nRow);
			if (sVal == null)
				break;
			sElements[i] = sVal;
			nCol ++;
		}
		oRcd.row = nRow; 
		oRcd.count = nCol; 
		oRcd.elements = sElements;
	}
	function ondblclickEx(x,y)
	{
		var oRcd = new Object();
		getRowInfo(y,oRcd);
		if (parent.parent.parent.ondblclickEx != null)
			parent.parent.parent.ondblclickEx(y,oRcd)
	}
	function onmouseoutLeftHead(y)
	{
	}
	function onmouseoverLeftHead(y)
	{
	}
	function getGridParam()
	{
		var ret = null;
		if (document.fgridparam != null)
			return document.fgridparam;
		if (parent != this)
		{
			if (parent.getGridParam != null)
			{
				ret = parent.getGridParam();
			}
		}
		return ret;
	}

	function JLListCtrl_initBody(obj)
	{
		if (obj == null)
			return;
		var nWidth = obj.m_wnd.document.body.clientWidth;
		var nHeight = obj.m_wnd.document.body.clientHeight;
		obj.m_wnd.document.write("<div id=divGrid style=\"POSITION: absolute; left:0px; top:0px;\">");
		obj.m_wnd.document.write("<IFRAME NAME=\"grid\" frameborder=\"0\" src=\"\" scrolling=yes STYLE=\"HEIGHT: "+nHeight+"px; LEFT: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH: "+nWidth+"px; \"></IFRAME>");
		obj.m_wnd.document.write("</div>");

		form = getGridParam();
		form.target = "grid";
		var sPath = form.classpath.value;
		if (form.classpath.value.length > 0)
			sPath += "/";
		form.action = sPath + "JPGrid.jsp";
		form.method = "post";
		form.submit();
	}

	function JLListCtrl_initHeader(obj)
	{
		if (obj == null)
			return;
		var nWidth = obj.m_wnd.document.body.clientWidth;
		var nHeight = obj.m_wnd.document.body.clientHeight;
		obj.m_wnd.document.write("<div id=divGridHeader style=\"POSITION: absolute; left:0px; top:0px;\">");
		obj.m_wnd.document.write("<IFRAME NAME=\"gridheader\" frameborder=\"0\" src=\"\" scrolling=no STYLE=\"top:0px; HEIGHT: 20px; LEFT: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH: "+(nWidth-16)+"px; \"></IFRAME>");
		obj.m_wnd.document.write("</div>");

		form = getGridParam();
		form.target = "gridheader";
		var sPath = form.classpath.value;
		if (form.classpath.value.length > 0)
			sPath += "/";
		form.action = sPath + "JPGridHeader.jsp";
		if (form._debug.value == 3)
			form.target.value = "_blank";
		form.method = "post";
		form.submit();
	}

	function onScrollTitle(nScroll)
	{
		window.gridheader.onScrollTitle(nScroll);
	}

	function JLListCtrl_printList(obj,nPageStartIdx,nCount)
	{
		if (obj.m_vList == null)
			return;
		var idx = 0;
		for (var i=0;i<nCount;i++)
		{
			_printItem(obj,nPageStartIdx,i);
		}
	}

	function JLListCtrl_printListPageStart(obj,nPageStart,nPageSize,nPage,nLineHeight,nHeaderWidth)
	{
		if (obj == null)
			return;
		var nDataTop = nPageSize*nLineHeight*nPage;
		var sHtml = "";
		sHtml += "<div id=\"gridPage"+(nPageStart+nPage)+"\">"; //
		sHtml += "<div id=\"gridData"+(nPageStart+nPage)+"\" style=\"POSITION:absolute; left:0px; top:"+nDataTop+"px;\">"; //
		// sHtml += "<table border=\"1\" cellpadding=\"0\" style=\"border-collapse:collapse;\" bordercolor=\"#666666\" cellspacing=\"0\" width=\""+nHeaderWidth+"\"  bgcolor=\"#ffffff\" >"; //
		obj.m_wnd.document.write(sHtml);
	}

	function JLListCtrl_printListPageEnd(obj)
	{
		var sHtml = "";
		sHtml += "</table>"; //
		sHtml += "</div>"; //
		sHtml += "</div>"; //
		obj.m_wnd.document.write(sHtml);
	}
</script>
