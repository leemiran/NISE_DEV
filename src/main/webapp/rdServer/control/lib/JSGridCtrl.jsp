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


<script>
	function JLGridCtrl_getObj()
	{
		return m_obj;
	}

	function JLGridCtrl_onmouseover(obj)
	{
	}

	function JLGridCtrl_onmouseover(obj,x,y)
	{
		var id = x+"_"+y;
		if (obj.m_focusitem == id)
			return;
		var elmt = null;
		elmt = document.getElementById(id);
		if (elmt != null)
			elmt.style.backgroundColor = "#dfdfdf"; // "#B0C4DE";
		elmt = document.getElementById("td_"+id);
		if (elmt != null)
			elmt.style.backgroundColor = "#dfdfdf"; // "#B0C4DE";
	}

	function JLGridCtrl_onmouseoutEx(obj)
	{
	}

	function JLGridCtrl_onmouseout(obj,x,y)
	{
		var id = x+"_"+y;
		if (obj.m_focusitem == id)
			return;
		var elmt = null;
		elmt = document.getElementById(id);
		if (elmt != null)
			elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
		elmt = document.getElementById("td_"+id);
		if (elmt != null)
			elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
	}

	function JLGridCtrl_initBody(obj)
	{
		var nWidth = document.body.clientWidth;
		var nHeight = document.body.clientHeight;
		document.write("<div id=divGrid style=\"POSITION: absolute; left:0px; top:0px;\">");
		document.write("<IFRAME NAME=\"grid\" frameborder=\"0\" src=\"\" scrolling=yes STYLE=\"HEIGHT: "+nHeight+"px; LEFT: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH: "+nWidth+"px; \"></IFRAME>");
		document.write("</div>");

		form = getGridParam();
		form.target = "grid";
		var sPath = form.classpath.value;
		if (form.classpath.value.length > 0)
			sPath += "/";
		form.action = sPath + "JPGrid.jsp";
		form.method = "post";
		form.submit();
	}

	function JLGridCtrl_onRefreshPage(nPage)
	{
		// alert(nPage);
	}

	// Greater Than or Equal 은 모두 삭재
	function JLGridCtrl_onDeleteAllPage()
	{
		var elmt = document.getElementById("gridAll");
		if (elmt != null)
			elmt.innerHTML = "<div id=gridBody></div>";
		m_obj.m_aSegIdx = new Array();
		m_obj.m_nSegCount = 0;
	}

	function JLGridCtrl_initHeader(obj)
	{
		var form = getGridParam();
		if (form == null)
			return;
		var sBtnFace = form._btnface.value;
		var sHtml = "";
		sHtml += "<div id=divGridHeaderleft style=\"POSITION: absolute; left: 0px; height: 20px; top: 0px; width:"+form._leftheaderwidth.value+"px;\">";
		sHtml += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width="+form._leftheaderwidth.value+">";
		sHtml += "<tr height=20>";
		sHtml += "<td>";
		sHtml += "<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" style=\"cursor: hand;\" width="+form._leftheaderwidth.value+">";
		sHtml += "<tr height=1>";
		sHtml += "<td width=1 bgcolor=\"#ffffff\"></td>";
		sHtml += "<td width="+(form._leftheaderwidth-4)+" bgcolor=\"#ffffff\"></td>";
		sHtml += "<td width=2 bgcolor=\"#ffffff\"></td>";
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>";
		sHtml += "</tr>";
		sHtml += "<tr height='18'>";
		sHtml += "<td width=1 bgcolor=\"#ffffff\"></td>";
		sHtml += "<td align=left width="+(form._leftheaderwidth-4)+" bgcolor=\""+sBtnFace+"\"><table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\"><tr><td><img ";
		sHtml += "alt border=0 height=2 src=images/trans.gif width="+(form._leftheaderwidth-4)+"><br><font color=\"#000000\"></font></td><td ";
		sHtml += "width=16 bgcolor=\""+sBtnFace+"\"></td></tr></table></td>";
		sHtml += "<td valign=top width=2 bgcolor=\""+sBtnFace+"\"></td>";
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>";
		sHtml += "</tr>";
		sHtml += "<tr height=1>";
		sHtml += "<td width=1 bgcolor=\"#666666\"></td>";
		sHtml += "<td width="+(form._leftheaderwidth-4)+" bgcolor=\"#666666\"></td>";
		sHtml += "<td width=2 bgcolor=\"#666666\"></td>";
		sHtml += "<td width=1 bgcolor=\"#666666\">";
		sHtml += "</td></tr></table></td></tr></table></div>";
		document.write(sHtml);

		var nWidth = document.body.clientWidth;
		var nHeight = document.body.clientHeight;
		document.write("<div id=divGridHeader style=\"POSITION: absolute; left:"+form._leftheaderwidth.value+"px; top:0px; \">");
		document.write("<IFRAME NAME=\"gridheader\" frameborder=\"0\" src=\"\" scrolling=no STYLE=\"HEIGHT: 20px; top:0px; LEFT: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH: "+(nWidth-66)+"px; \"></IFRAME>");
		document.write("</div>");

		form = getGridParam();
		form.target = "gridheader";
		var sPath = form.classpath.value;
		if (form.classpath.value.length > 0)
			sPath += "/";
		form.action = sPath + "JPGridHeader.jsp";
		form.method = "post";
		form.submit();
	}

	function JLGridCtrl_onClickEx(obj)
	{
		var elmt = obj.m_wnd.event.srcElement;
		if (elmt == null)
			return;
		// id : 32_13
		var id = new String(elmt.id);
		var nLen = id.length;
		var idx = id.indexOf("_");
		if (idx < 0)
			return;
		var row = id.substr(0,idx);
		if (row == "td")
		{
			var tmp = id.substr(idx+1,nLen-idx);
			id = tmp;

			idx = id.indexOf("_");
			if (idx < 0)
				return;
			row = id.substr(0,idx);
		}
		if (row == "no")
		{
			var tmp = id.substr(idx+1,nLen-idx);
			id = tmp;
			JLGridCtrl_blurFocusCell(obj);
			_focusRow(obj,id);
			return;
		}
		var col = id.substr(idx+1,nLen-idx);
		// alert(row+":"+col);
		JLGridCtrl_onClick(obj,row,col);
	}

	function JLGridCtrl_blurFocusCell(obj)
	{
		if (obj.m_focusitem != "")
		{
			var elmt = obj.m_wnd.document.getElementById(obj.m_focusitem);
			if (elmt != null)
				elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
			elmt = obj.m_wnd.document.getElementById("td_"+obj.m_focusitem);
			if (elmt != null)
				elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
		}
	}

	function JLGridCtrl_onClick(obj,row,col)
	{
		if (obj == null)
			return;
		if (!(Number(row) >= 0 && Number(col) >= 0))
			return;
		//  row : row, col : col
		var id = row+"_"+col;
		JLGridCtrl_blurFocusCell(obj);

		obj.m_nX = row;
		obj.m_nY = col;
		obj.m_focusitem = id;
		if (obj.m_focusitem != "")
		{
			elmt = obj.m_wnd.document.getElementById(obj.m_focusitem);
			if (elmt != null)
				elmt.style.backgroundColor = "#B0C4DE";
			elmt = obj.m_wnd.document.getElementById("td_"+obj.m_focusitem);
			if (elmt != null)
				elmt.style.backgroundColor = "#B0C4DE";
		}
		_focusRow(obj,row);
	}

	function updateData(obj,nPageStart)
	{
		// scroll 되면서 focus된것을 해지한다.
		{
			var id = obj.m_nX+'_'+obj.m_nY;
			var oInput = obj.m_wnd.document.getElementById(id);
			if (oInput != null)
				oInput.blur();
			JLGridCtrl_onBlur(obj.m_nX,obj.m_nY);
		}
		
		var nCurPageStart = "";
		if (window.griddata != null)
		{
			if (window.griddata.getPageStart != null)
			{
				nCurPageStart = window.griddata.getPageStart();
			}
		}
		if (nCurPageStart == nPageStart && nCurPageStart != "")
		{
			// alert("return 1");
			return;
		}
			
		obj.m_bDataReady = false;
		
		form = getGridParam();
		submitData(obj,form,nPageStart,form.pagesize.value,3,true);
	}

	function onKeyDownEx() 
	{
		var ev = window.event;
		if (ev.keyCode == 27)
		{
			if (window.event.srcElement.tagName == "INPUT")
			{
				window.event.srcElement.blur();
				m_obj.m_bFocusBox = true;
				// JLGridCtrl_onFocus(m_obj.m_nX,m_obj.m_nY);
			}
			else
			{
				if (m_obj.m_bFocusBox == true)
				{
					onBlurBorder();
				}
			}
		}
		
		// alert(ev.keyCode);	
		if (m_obj.m_bFocusBox == true)
		{
			switch (ev.keyCode) 
			{
				case 13:
					if (Number(m_obj.m_nX) >= 0 && Number(m_obj.m_nY) >= 0)
					{
						var id = m_obj.m_nX+"_"+m_obj.m_nY;
						var elmt = document.getElementById(id);
						if (elmt != null)
							elmt.focus();
					}
					break;
				case 37:
					// left
					if (Number(m_obj.m_nX) > 0 && Number(m_obj.m_nY) >= 0)
					{
						onblurBox(m_obj.m_nX,m_obj.m_nY);
						parent.parent.JLGridCtrl_onFocus(m_obj,Number(m_obj.m_nX)-1,Number(m_obj.m_nY));
						m_obj.m_bFocusBox = true;
					}
					break
				case 38:
					// up
					if (Number(m_obj.m_nX) >= 0 && Number(m_obj.m_nY) > 0)
					{
						onblurBox(m_obj.m_nX,m_obj.m_nY);
						parent.parent.JLGridCtrl_onFocus(m_obj,Number(m_obj.m_nX),Number(m_obj.m_nY)-1);
						m_obj.m_bFocusBox = true;
					}
					break
				case 39:
					// right
					if (Number(m_obj.m_nX) < Number(m_obj.m_nMaxX)-1 && Number(m_obj.m_nX) >= 0 && Number(m_obj.m_nY) >= 0)
					{
						onblurBox(m_obj.m_nX,m_obj.m_nY);
						parent.parent.JLGridCtrl_onFocus(m_obj,Number(m_obj.m_nX)+1,Number(m_obj.m_nY));
						m_obj.m_bFocusBox = true;
					}
					break
				case 40:
					// down
					if (Number(m_obj.m_nX) >= 0 && Number(m_obj.m_nY) >= 0 && Number(m_obj.m_nY) < Number(m_obj.m_nMaxY)-1)
					{
						onblurBox(m_obj.m_nX,m_obj.m_nY);
						parent.parent.JLGridCtrl_onFocus(m_obj,Number(m_obj.m_nX),Number(m_obj.m_nY)+1);
						m_obj.m_bFocusBox = true;
					}
					break
			}
			// alert(m_obj.m_nX + "_" + m_obj.m_nY);
		}
	}
	
	function onScrollA()
	{
		var scrollLeft = document.body.scrollLeft;
		{
			if (parent != this)
			{
				if (parent.onScrollTitle != null)
				{
					parent.onScrollTitle(document.body.scrollLeft);
				}
			}
			// document.all['gridno'].style.left = scrollLeft;
		}
		
		var nMod = (document.body.scrollTop)%(m_obj.m_nLineHeight*m_obj.m_nPageSize);
		var nPage = (document.body.scrollTop - nMod)/(m_obj.m_nLineHeight*m_obj.m_nPageSize);
		if (nPage > 0)
			m_obj.m_nPage = nPage;
		else
			m_obj.m_nPage = 0;

		var nScrollTop = document.body.scrollTop;
		var oGridBody = document.getElementById("gridbody");
		if (oGridBody != null)
		{
			var nPosTop = oGridBody.style.posTop;
			var nRemainCount = m_obj.m_nTotalCount - ((m_obj.m_nPage+1)*m_obj.m_nPageSize);
			var nRemainPage = ((m_obj.m_nTotalCount+m_obj.m_nPageSize) - ((m_obj.m_nPage+1)*m_obj.m_nPageSize))/m_obj.m_nPageSize;
			nRemainPage = parseInt(nRemainPage);
			// nRemainPage : 1 남아있는 1 page, 즉 posTop을
			
			m_obj.m_nMarginBottomRcd = (((nPosTop-20)+(m_obj.m_nLineHeight*m_obj.m_nCurSegSize)) - ((m_obj.m_nPage+1)*m_obj.m_nLineHeight*m_obj.m_nPageSize))/ m_obj.m_nLineHeight;
			if (m_obj.m_nMarginBottomRcd <= 0)
			{
				if (nRemainPage >= 1 && nRemainPage < 3)
				{
				}
				if (nRemainCount > 0)
				{
					// pagedown
					oGridBody.style.posTop = m_obj.m_nPage*m_obj.m_nLineHeight*m_obj.m_nPageSize + 20;
					var ogridno = document.getElementById("gridno");
					if (ogridno != null)
						ogridno.style.posTop = m_obj.m_nPage*m_obj.m_nLineHeight*m_obj.m_nPageSize + 20;

					var oNo;
					oNo = document.getElementById(""+0);
					if (oNo != null)
					{
						for (i=0;i<m_obj.m_nGridCount;i++)
						{
							oNo = document.getElementById(""+i);
							if (oNo != null)
								oNo.value = m_obj.m_nPage*m_obj.m_nPageSize+Number(i)+1;
						}
					}
					m_obj.m_nPageStart = m_obj.m_nPage;
					updateData(m_obj,m_obj.m_nPageStart);
				}
			}
			else
			{
				// pageup
				m_obj.m_nMarginTopRcd = (((m_obj.m_nPage+1)*m_obj.m_nLineHeight*m_obj.m_nPageSize) - (nPosTop-20))/ m_obj.m_nLineHeight;
				if (m_obj.m_nMarginTopRcd <= 0)
				{
					m_obj.m_nPageStart = m_obj.m_nPage-1;
					if (m_obj.m_nPageStart < 0)
						m_obj.m_nPageStart = 0;
					var ogridno = document.getElementById("gridno");
					if (ogridno != null)
						ogridno.style.posTop = m_obj.m_nPageStart*m_obj.m_nLineHeight*m_obj.m_nPageSize + 20;
					oGridBody.style.posTop = m_obj.m_nPageStart*m_obj.m_nLineHeight*m_obj.m_nPageSize + 20;
					
					var oNo;
					oNo = document.getElementById(""+0);
					if (oNo != null)
					{
						for (i=0;i<m_obj.m_nGridCount;i++)
						{
							oNo = document.getElementById(""+i);
							if (oNo != null)
								oNo.value = m_obj.m_nPageStart*m_obj.m_nPageSize+Number(i)+1;
						}
					}
					updateData(m_obj,m_obj.m_nPageStart);
				}
			}
			// debugging
			if (parent.parent.document.fthis.a != null)
			{
				parent.parent.document.fthis.a.value = m_obj.m_nPage;
				parent.parent.document.fthis.b.value = nScrollTop;
				parent.parent.document.fthis.c.value = nPosTop;
				parent.parent.document.fthis.d.value = nMod;
				parent.parent.document.fthis.e.value = m_obj.m_nMarginBottomRcd;
				parent.parent.document.fthis.f.value = oGridBody.style.posTop;
				parent.parent.document.fthis.g.value = m_obj.m_nMarginTopRcd;
				parent.parent.document.fthis.pagecount.value = m_obj.m_nCurSegSize;
				parent.parent.document.fthis.remain.value = nRemainCount;
				parent.parent.document.fthis.pagestart.value = m_obj.m_nPageStart;
			}
		}
	}

	function updateCell(nPageStart)
	{
		// alert("m_obj.m_nPageStart : " + m_obj.m_nPageStart + "nPageStart : " + nPageStart);
		// if (m_obj.m_nPageStart == nPageStart) return;
		// alert("m_obj.m_bDataReady : " + m_obj.m_bDataReady);
		// if (m_obj.m_nViewPage == m_obj.m_nPage) return;
		
		if (m_obj.m_bDataReady == false)
			return;
		if (window.griddata == null)
			return;
		if (window.griddata.getData == null)
			return;
			
		// alert("updatecell");	
		/*
		for (i=0;i<m_obj.m_nGridCount;i++)
		{
			var y = m_obj.m_nPageStart*10+i;
			for (j=0;j<m_obj.m_nCol;j++)
			{
				var x = j;
				var sVal = window.griddata.getData(x,y);
				if (sVal == null)
				{
					// alert(x+"_"+y);
					return;
				}
				setCell(j,i,sVal);
			}
		}
		*/
		m_obj.m_nViewPage = m_obj.m_nPage;
	}

	function JLGridCtrl_onBlurEx(obj)
	{
		var elmt = obj.m_wnd.event.srcElement;
		if (elmt == null)
			return;
		// id : 32_13
		var id = new String(elmt.id);
		var nLen = id.length;
		var idx = id.indexOf("_");
		if (idx < 0)
			return;
		var row = id.substr(0,idx);
		if (row == "td")
		{
			var tmp = id.substr(idx+1,nLen-idx);
			id = tmp;

			idx = id.indexOf("_");
			if (idx < 0)
				return;
			row = id.substr(0,idx);
		}
		var col = id.substr(idx+1,nLen-idx);
		JLGridCtrl_onBlur(obj,row,col);
	}

	function JLGridCtrl_onBlur(obj,x,y)
	{
		//  x : row, y : col
		var id = x+"_"+y;
		if (id != "")
		{
			elmt = obj.m_wnd.document.getElementById(id);
			if (elmt != null)
				elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
			elmt = obj.m_wnd.document.getElementById("td_"+id);
			if (elmt != null)
				elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
		}

		/*
		obj.m_oFocus = null;
		if (obj.m_bFocusBox == true)
			return;
		if (onblurBox(obj,x,y) == true)
		{
			obj.m_nX = "";
			obj.m_nY = "";
		}
		*/
	}
	
	function onblurBox(obj,x,y)
	{
		obj.m_nX = x;
		obj.m_nY = y;
		obj.m_bFocusBox = false;
		onBlurBorder();
	}
	
	function onfocusBox(x,y)
	{
		JLGridCtrl_onFocus(x,y);
		m_obj.m_bFocusBox = true;
	}

	function onFocusBorder(x,y)
	{
	}

	function onBlurBorder()
	{
		var nLeft = 0;
		var nWidth = 0;
		var nTop = 0;
		var nHeight = 0;
		var nBorder = 0;
	}

	function JLGridCtrl_onFocusEx(obj)
	{
	}

	function JLGridCtrl_onFocus(obj,x,y)
	{
		if (eval(x) != eval(obj.m_nX) || eval(y) != eval(obj.m_nY))
			onblurBox(obj.m_nX,obj.m_nY);
		var id = x+'_'+y;
		obj.m_nX = x;
		obj.m_nY = y;
		obj.m_bFocusBox = false;
		onFocusBorder(x,y);
	}

	function onInputKeydownEx()
	{
		var ev = window.event;
		if (ev.keyCode == 27)
		{
			JLGridCtrl_onBlur(m_obj.m_nX,m_obj.m_nY);
			/*
			var elmt = window.event.srcElement;
			if (elmt != null)
				elmt.onblur();
			*/
		}
		// window.event.srcElement.onkeydown();
	}
		
	function setCell(nX,nY,sVal)
	{
		var sName = nX+"_"+nY;
		var elmt = document.getElementById(sName);
		if (elmt != null)
			elmt.value = sVal;
	}


	function JLGridCtrl_focusRow(obj,row)
	{
		if (obj == null)
			return;
		var form = getGridParam();
		if (form == null)
			return;
			
		row = Number(row);
		var nTop = row*20+20;
		var nWidth = Number(obj.m_nHeaderWidth) + Number(form._leftheaderwidth.value) - 2;// 4*150+50-2;
		var id = "rowcursor";
		var elmt = null;
		elmt = obj.m_wnd.document.getElementById(id+"_t");
		if (elmt != null)
		{
			moveLine(obj.m_wnd,id+"_t",0,nTop,nWidth,1,"#000000");
			moveLine(obj.m_wnd,id+"_l",0,nTop,1,18,"#000000");
			moveLine(obj.m_wnd,id+"_b",0,nTop+18,nWidth,1,"#000000");
			moveLine(obj.m_wnd,id+"_r",nWidth,nTop,1,19,"#000000");
		}
		else
		{
			drawLine(obj.m_wnd,id+"_t",0,nTop,nWidth,1,"#000000");
			drawLine(obj.m_wnd,id+"_l",0,nTop,1,18,"#000000");
			drawLine(obj.m_wnd,id+"_b",0,nTop+18,nWidth,1,"#000000");
			drawLine(obj.m_wnd,id+"_r",nWidth,nTop,1,19,"#000000");
		}
		var oGrid = _getObj('grid');
		if (oGrid != null)
		{
			if (oGrid.m_nUpdateIdx >= 0)
			{
				if (oGrid.m_nFocusRow != row)
				{
					if (oGrid.m_wnd.parent._onUpdateRow != null)
					{
						var arg = new Array();
						for (var i=0;i<form.count.value;i++)
						{
							arg[i] = JLGridCtrl_getCellValue(obj.m_nUpdateIdx,i);
							// alert("cellvalue : " + arg[i]);
						}
						oGrid.m_wnd.parent._onUpdateRow(obj.m_nUpdateIdx,arg);
					}
					oGrid.m_nUpdateIdx = -1;
				}
			}
		}
		obj.m_nFocusRow = row;
	}

	function onmousedownLeftHead(idx)
	{
		_focusRow(m_obj,idx);
	}

	function _focusRow(obj,idx)
	{
		if (obj != null)
			obj.m_nX = idx;
		// alert(obj.m_wnd.name);
		if (parent != this)
		{
			var oGrid = _getObj('grid'); // obj.m_wnd.parent.m_obj;
			/*
			if (Number(obj.m_nX) != Number(idx))
			{
				if (obj.m_focusitem != "")
				{
					elmt = obj.m_wnd.document.getElementById(obj.m_focusitem);
					if (elmt != null)
						elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
					elmt = obj.m_wnd.document.getElementById("td_"+obj.m_focusitem);
					if (elmt != null)
						elmt.style.backgroundColor = "#ffffff"; // "#B0C4DE";
				}
			}
			*/
			obj.m_nFocusRow = idx;
			if (oGrid != null)
				JLGridCtrl_focusRow(oGrid,idx);
		}
	}

	function JLGridCtrl_printGridNo(obj,nPageStart,nPageCount,nPageSize,nCurSegSize,sMainFrame)
	{
		var nTop = 0;
		var sHtml = "";
		var sName = "";
		sHtml += "<div id=\"divGridNo\" style=\"POSITION:absolute; left:0px; top:"+nTop+"px\" ";
		sHtml += " oncontextmenu=\"javascript:if (parent.JLContextMenu_onContextMenuEx != null) { return parent.JLContextMenu_onContextMenuEx('a','"+sMainFrame+"',window); } \" id=\"divGridNo\" style=\"POSITION:absolute; left:0px; top:0px\"> ";
		sHtml += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\""+form._leftheaderwidth.value+"\" bgcolor=#ffffff>";
		var nRow = (nPageSize*nPageCount);
		var nStart = nPageStart*nPageSize;
		for (var j=0;j<nRow;j++)
		{
			var nIdx = j + nStart;
			sHtml += "<tr height=20><td><table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" ><tr height=1><td width=1></td><td></td><td width=1 bgcolor=\"#666666\"></td></tr><tr height='18'><td width=1></td><td align=left width=48 bgcolor=\"#d4d0c8\"><img alt border=0 src=\"\" width=48 height=1><br><img alt border=0 height=1 src=\"\" width=3 height=2><font color=\"#000000\" style=\"FONT: 9pt 굴림\">";
			sHtml += "" + (nIdx+1);
			sHtml += "</font></td><td width=1 bgcolor=\"#666666\"></td></tr><tr height=1><td width=1 bgcolor=\"#666666\"></td><td bgcolor=\"#666666\"></td><td width=1 bgcolor=\"#666666\"></td></tr></table></td></tr>";
			sHtml += "\n";
		}
		sHtml += "</table>";
		sHtml += "</div>";
		obj.m_wnd.document.write(sHtml);
	}

	/*
	onfocus=\"javascript:if (parent.parent.JLGridCtrl_onFocus != null) parent.parent.JLGridCtrl_onFocus(m_obj,'"+nRow2+"','"+nCol+"')\" onblur=\"javascript:if (parent.parent.JLGridCtrl_onBlurEx != null) parent.parent.JLGridCtrl_onBlurEx(m_obj)\"
	onclick=\"javascript:if (parent.parent.JLGridCtrl_onClick != null) parent.parent.JLGridCtrl_onClick(m_obj,"+nRow2+","+nCol+");\" onmouseover=\"javascript:if (parent.parent.JLGridCtrl_onmouseover != null) parent.parent.JLGridCtrl_onmouseover(m_obj,"+nRow2+","+nCol+");\" onmouseout=\"javascript:if (window.parent.parent.JLGridCtrl_onmouseout != null) parent.parent.JLGridCtrl_onmouseout(m_obj,"+nRow2+","+nCol+");\"
	*/
	function _printCell(obj,nRow,nCol,nWidth)
	{
		if (obj == null)
			return;
		var nRowStart = obj.m_nPageStart*obj.m_nPageSize;
		var nRow2 = nRow+nRowStart;
		var nIdx = Number(nRow)*Number(obj.m_nColSize);
		var sValue = obj.m_vList[nIdx+nCol];

		var sHtml = JLGridCtrl_getCellHtml(sValue,nRow,nCol,nWidth);
		document.write(sHtml);
		var sStrValue = new String(sValue);
		var idx = sStrValue.indexOf("\"");
		if (idx >= 0)
		{
			obj.m_wnd.document.write(sHtml);
			var elmt = obj.m_wnd.document.getElementById(""+nRow+"_"+nCol+""); 
			if (elmt != null)
				elmt.value = sValue;
		}
	}

	function _printRow(obj,nRow)
	{
		if (obj == null)
			return;
		var nWidth = 0;
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}
		// alert(param.pagesize.value);
		obj.m_wnd.document.write("<tr height=19>");
		for (var i=0;i<obj.m_nColSize;i++)
		{
			if (eval("param.width"+i) != null)
				nWidth = eval("param.width"+i+".value");
			_printCell(obj,nRow,i,nWidth);
		}
		obj.m_wnd.document.write("</tr>");
	}

	function JLGridCtrl_printList(obj)
	{
		if (obj == null)
			return;
		var nSize = (obj.m_vList.length)/obj.m_nColSize;
		for (var i=0;i<nSize;i++)
			_printRow(obj,i);
	}

	function JLGridCtrl_getCellHtml(sValue,nRow,nCol,nWidth)
	{
		var sHtml = "";
		var nCellWidth = Number(nWidth)-1;
		var sEditable = "";
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}
		if (eval("param._edit"+nCol+".value") == 0)
			sEditable = "readonly";
		sHtml += "<td id=td_"+nRow+"_"+nCol+" align=left valign=top width=\""+nCellWidth+"\"><img alt border=\"0\" height=\"1\" src=\"\" width="+nCellWidth+"><br><input "+sEditable+" class=\"gridcell\" type=text id=\""+nRow+"_"+nCol+"\" value=\"";
		if (!(sValue == "" || sValue == null))
		{
			var sStrValue = new String(sValue);
			var idx = sStrValue.indexOf("\"");
			if (idx < 0)
			{
				sHtml += sStrValue;
			}
		}
		sHtml += "\" maxlength=512 style=\"width:"+(nWidth-4)+"px;\" onchange=\"parent.parent.JLGridCtrl_onchangeEx(m_obj)\" onblur=\"javascript:if (parent.parent.JLGridCtrl_onBlurEx != null) parent.parent.JLGridCtrl_onBlurEx(m_obj)\"></td>";
		return sHtml;
	}

	function JLGridCtrl_getRowHtml(obj,nRow)
	{
		if (obj == null)
			return;
		var nWidth = 0;
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}
		var sHtml = "";
		for (var i=0;i<obj.m_nColSize;i++)
		{
			if (eval("param.width"+i) != null)
				nWidth = eval("param.width"+i+".value");
			sHtml += JLGridCtrl_getCellHtml("",nRow,i,nWidth);
		}
		return sHtml;
	}

	function JLGridCtrl_increaseRowIdx(obj,idx)
	{
		if (obj == null)
			return;
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}

		// idx 다음에 있는 row들의 id값들을 모두 하나 증가 시킨다.
		var nStart = Number(idx)+Number(obj.m_nPageStart*obj.m_nPageSize);
		var nEnd = obj.m_nPageSize + nStart; // obj.m_nCurSegSize;
		for (var i=Number(nEnd)-1;i>=nStart;i--)
		{
			var elmt = obj.m_wnd.document.getElementById("no_"+i);
			if (elmt != null)
			{
				elmt.id = "no_"+(i+1);
				var nCellWidth = Number(param._leftheaderwidth.value) - 2;
				elmt.innerHTML = "<img alt border=0 src=\"\" width="+nCellWidth+" height=1><br><img alt border=0 height=1 src=\"\" width=3 height=2><font color=\"#000000\" style=\"FONT: 9pt 굴림\">"+(i+2)+"</font>";
			}
		}
		// idx 다음에 있는 row들의 id값들을 모두 하나 증가 시킨다.
		for (var i=Number(nEnd)-1;i>=nStart;i--)
		{
			for (var j=0;j<obj.m_nColSize;j++)
			{
				var elmt = obj.m_wnd.document.getElementById("td_"+i+"_"+j);
				if (elmt != null)
					elmt.id = "td_"+(i+1)+"_"+j;
				elmt = obj.m_wnd.document.getElementById(""+i+"_"+j);
				if (elmt != null)
				{
					elmt.id = ""+(i+1)+"_"+j;
					elmt.name = ""+(i+1)+"_"+j;
				}
			}
		}
	}

	function JLGridCtrl_decreaseRowIdx(obj,idx)
	{
		if (obj == null)
			return;
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}

		// idx 다음에 있는 row들의 id값들을 모두 하나 -- 시킨다.
		var nStart = Number(idx);
		var nEnd = obj.m_nPageSize + nStart; // obj.m_nCurSegSize;
		// alert("end : "+nEnd + " start : " +  nStart);
		for (var i=Number(nStart);i<=nEnd;i++)
		{
			var elmt = obj.m_wnd.document.getElementById("no_"+i);
			if (elmt != null)
			{
				elmt.id = "no_"+(i-1);
				// alert("decreaseRowIdx : "+elmt.id);
				var nCellWidth = Number(param._leftheaderwidth.value) - 2;
				elmt.innerHTML = "<img alt border=0 src=\"\" width="+nCellWidth+" height=1><br><img alt border=0 height=1 src=\"\" width=3 height=2><font color=\"#000000\" style=\"FONT: 9pt 굴림\">"+i+"</font>";
			}
		}
		// idx 다음에 있는 row들의 id값들을 모두 하나 -- 시킨다.
		for (var i=Number(nStart);i<=nEnd;i++)
		{
			for (var j=0;j<obj.m_nColSize;j++)
			{
				var elmt = obj.m_wnd.document.getElementById("td_"+i+"_"+j);
				if (elmt != null)
					elmt.id = "td_"+(i-1)+"_"+j;
				elmt = obj.m_wnd.document.getElementById(""+i+"_"+j);
				if (elmt != null)
				{
					elmt.id = ""+(i-1)+"_"+j;
					elmt.name = ""+(i-1)+"_"+j;
				}
			}
		}
	}

	function JLGridCtrl_deleteRow(obj)
	{
		var oGrid = _getObj('grid');
		if (oGrid == null)
			return;

		var idx = oGrid.m_nFocusRow;
		var oFrameObj = JLListCtrl_getFrameObj(idx);
		if (oFrameObj == null)
			return;
		var id = oFrameObj.m_wnd.document.getElementById("tbl");
		if (id == null)	
			return;
		var pageidx = idx-(oFrameObj.m_nPageStart*oFrameObj.m_nPageSize);
		{
			var elmt = oFrameObj.m_wnd.document.getElementById("leftheader");
			if (elmt != null)
				elmt.deleteRow(pageidx);
		}
		{
			var tbl = oFrameObj.m_wnd.document.getElementById("tbl");
			if (tbl == null)	
				return;
			var tr = tbl.deleteRow(pageidx);
			if (tr != null)
			{
				alert("delete row ");
				for (var j=0;j<oFrameObj.m_nColSize;j++)
				{
					tr.deleteCell(0);
				}
			}
		}
		JLGridCtrl_decreaseRowIdx(oFrameObj,idx);

		// 바로 다음 page에 해당하는 segment의 위치를 이동한다.
		{
			var nPage = parseInt(Number(idx)/oFrameObj.m_nPageSize);
			// alert("cur page " + nPage);
			// current page의 height는 늘리지 않아도 자동으로 늘어난다.
			// 바로 다음에 있는 divPage의 위치를 20만큼 위로로 이동한다.
			{
				var elmt = oGrid.m_wnd.document.getElementById("divPage"+(nPage+1));
				if (elmt != null)
				{
					var elmtFrame = eval("oGrid.m_wnd.griddata"+(nPage+1));
					if (elmtFrame != null)
					{
						elmt.style.posTop = elmt.style.posTop - 20; // 위로 이동
						var oFrame = elmtFrame._getThis();
						if (oFrame != null)
							JLGridCtrl_decreaseRowIdx(oFrame,(nPage+1)*oFrame.m_nPageSize);

						// invalid 한 frame이라는 뜻으로 앞에 _를 붙인다.
						elmtFrame.name = "_"+elmtFrame.name;
						elmtFrame.id = "_"+elmtFrame.id;
						elmt.id = "_"+elmt.id;
					}
				}
			}
		}
		
		var param = getGridParam();
		if (param == null)
			return null;
		if (param._serverdata.value > 0)
		{
			// 현재 segment의 pagestart를 찾아낸다.
			// 현재 segment를 refresh합니다.
			{
				for (var i=0;i<4;i++)
				{
					var elmtFrame = eval("oGrid.m_wnd.griddata"+(nPage-i));
					if (elmtFrame != null)
					{
						var elmt = oGrid.m_wnd.document.getElementById("divPage"+(nPage-i));
						if (elmt != null)
						{
							elmtFrame.name = "_"+elmtFrame.name;
							elmtFrame.id = "_"+elmtFrame.id;
							elmt.id = "_"+elmt.id;
	
							// 현재 page를 refresh한다.
							var form = getGridParam();
							CS_Producer(oGrid,(nPage-i),i+1,form.pagesize.value);
						}
					}
				}
			}
			// 다음 다음 (+2) 이후 모든 segment를 삭제합니다.
			{
				var nPage = parseInt(idx/oFrameObj.m_nPageSize);
				var nRemainPageCount = parseInt(oFrameObj.m_nTotalCount/oFrameObj.m_nPageSize) - (oFrameObj.m_nPageStart+1);
				var nStart = nPage + 2;
				var nEnd = nStart + nRemainPageCount;
				// alert("start : " + nStart +" : " + " end : " + nEnd);
				for (var i=nStart;i<=nEnd;i++)
				{
					// alert("remove page : "+i);
					var elmt = oGrid.m_wnd.document.getElementById("divPage"+i);
					if (elmt != null)
						elmt.removeNode(true);
				}
			}
	
			// 다음 페이지가 download 않된 경우 nPage에서 delete되면, 다음페이지가 보이는 것이 좋다. 
			// 다음 페이지를 refresh합니다.
			JLListCtrl_openPage(oFrameObj,nPage+1);
		}
		param._rowcount.value --;
		if (Number(oGrid.m_nFocusRow) <= param._rowcount.value)
			_focusRow(oGrid,-1);
	}
	
	function JLListCtrl_getFrame(idx)
	{
		var oGrid = _getObj('grid');
		if (oGrid == null)
			return null;
		var form = getGridParam();
		if (form == null)
			return null;
		var nPage = parseInt(idx/form.pagesize.value);
		// 현재 segment의 start를 찾아낸다.
		for (var i=0;true;i++)
		{
			if (nPage-i < 0)
				break;
			var sFrameName = "griddata"+(nPage-i);
			var elmt = eval("oGrid.m_wnd."+sFrameName);
			if (elmt != null)
			{
				return elmt;
			}
		}
		return null;
	}
	
	function JLListCtrl_getFrameObj(idx)
	{
		var oFrame = JLListCtrl_getFrame(idx);
		if (oFrame == null)
			return null;
		return oFrame.m_obj;
	}
	
	function JLGridCtrl_getCellValue(nRow,nCol)
	{
		var oFrame = JLListCtrl_getFrame(nRow);
		if (oFrame == null)
			return null;
		var elmt = oFrame.document.getElementById(nRow+"_"+nCol);
		if (elmt == null)
			return null;
		return elmt.value;
	}
	
	function JLGridCtrl_getRowCount()
	{
		var param = getGridParam();
		if (param == null)
			return null;
		return param._rowcount.value;
	}

	function JLGridCtrl_submitCmd(obj,cmd,arg)
	{
		var param = getGridParam();
		if (param == null)
			return null;

		var oGrid = _getObj('grid');
		if (oGrid == null)
			return;

		var idx = Number(oGrid.m_nFocusRow);
		if (!(idx >= 0))
			idx = 0;

		param._cmd.value = cmd;
		param._idx.value = idx;
		for (var i=0;i<param.count.value;i++)
			eval("param._val"+i+".value = arg[i]");

		param.method = "post";
		param.target = param._name.value+"_faction";
		if (param._debug.value == 2)
			param.target = "_blank";
		param.action = param.griddata.value;
		if (oGrid.m_wnd.submitForm != null)
			oGrid.m_wnd.submitForm(param);
	}

	function JLGridCtrl_insertRowSvr(obj,arg)
	{
		JLGridCtrl_submitCmd(obj,1,arg);
	}

	function JLGridCtrl_updateRowSvr(obj,arg)
	{
		JLGridCtrl_submitCmd(obj,2,arg);
	}

	function JLGridCtrl_deleteRowSvr(obj,arg)
	{
		JLGridCtrl_submitCmd(obj,3,arg);
	}

	function JLGridCtrl_onFinishInsert(obj,arg)
	{
		// arg는 var arg = new Array()이다.
		// alert("JLGridCtrl_onFinishInsert : window name : " + obj.m_wnd.name);
		JLGridCtrl_insertRow(obj,arg);
	}

	function JLGridCtrl_onFinishUpdate(obj,arg)
	{
		// arg는 var arg = new Array()이다.
		// alert("JLGridCtrl_onFinishInsert : window name : " + obj.m_wnd.name);
		// JLGridCtrl_insertRow(obj,arg);
	}

	function JLGridCtrl_onFinishDelete(obj,arg)
	{
		// arg는 var arg = new Array()이다.
		// alert("JLGridCtrl_onFinishDelete : window name : " + obj.m_wnd.name);

		// db에서 삭제된 이후, JLGridCtrl_onFinishDelete 자동으로 호출됩니다.
		// 화면에 보이는 row를 삭제합니다.
		JLGridCtrl_deleteRow(obj);
	}

	function JLGridCtrl_getFocusRowIdx()
	{
		var oGrid = _getObj('grid');
		if (oGrid == null)
			return;

		var idx = Number(oGrid.m_nFocusRow);
		return idx;
	}

	function JLGridCtrl_getFocusRow()
	{
		var idx = JLGridCtrl_getFocusRowIdx();
		if (!(idx >= 0))
			return null;
		return JLGridCtrl_getRow(idx);
	}

	function JLGridCtrl_getRow(idx)
	{
		if (!(idx >= 0))
			return null;
		var arg = new Array();
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}
		for (var i=0;i<param.count.value;i++)
		{
			arg[i] = JLGridCtrl_getCellValue(idx,i);
		}
		return arg;
	}

	function JLGridCtrl_insertRow(obj,arg)
	{
		var oGrid = _getObj('grid');
		if (oGrid == null)
			return;

		var idx = Number(oGrid.m_nFocusRow);
		if (!(idx >= 0))
			idx = 0;
		// alert("JLGridCtrl_insertRow : " + idx);
		var oFrameObj = JLListCtrl_getFrameObj(idx);
		if (oFrameObj == null)
			return;
		// alert("frame : " + oFrameObj.m_wnd.name);
		var pageidx = idx-(oFrameObj.m_nPageStart*oFrameObj.m_nPageSize);
		JLGridCtrl_increaseRowIdx(oFrameObj,pageidx);

		// grid param		
		var param = getGridParam();
		if (param == null)
		{
			alert('grid param is null');
			return;
		}
		// insert left header 
		{
			var elmt = oFrameObj.m_wnd.document.getElementById("leftheader");
			if (elmt != null)
			{
				var tr = elmt.insertRow(pageidx);
				if (tr == null)
					return;
				var td = tr.insertCell();
				if (td == null)
					return;
				var nCellWidth = Number(param._leftheaderwidth.value) - 2;
				var sHtml = "<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\"><tr height=1><td width=1></td><td></td><td width=1 bgcolor=\"#666666\"></td></tr><tr height='18'><td width=1></td><td id=no_"+idx+" align=left width="+nCellWidth+" bgcolor=\"#d4d0c8\"><img alt border=0 src=\"\" width="+nCellWidth+" height=1><br><img alt border=0 height=1 src=\"\" width=3 height=2><font color=\"#000000\" style=\"FONT: 9pt 굴림\">"+(idx+1)+"</font></td><td width=1 bgcolor=\"#666666\"></td></tr><tr height=1><td width=1 bgcolor=\"#666666\"></td><td bgcolor=\"#666666\"></td><td width=1 bgcolor=\"#666666\"></td></tr></table>";
				// alert(sHtml);
				td.innerHTML = sHtml;
				td.width = nCellWidth+2;
			}
		}
		// insert row
		{
			var tbl = oFrameObj.m_wnd.document.getElementById("tbl");
			if (tbl == null)
			{
				alert("JSGridCtrl @ tbl is null");
				return;
			}
			var tr = tbl.insertRow(pageidx);
			if (tr == null)
				return;
			tr.height = 19;
			var td = null;
			var nWidth = 0;
			for (var i=0;i<oFrameObj.m_nColSize;i++)
			{
				if (eval("param._width"+i) != null)
					nWidth = eval("param._width"+i+".value");
				td = tr.insertCell();
				if (td != null)
				{
					var nRow = idx;
					var nCol = i;

					var sEditable = "";
					if (eval("param._edit"+nCol+".value") == 0)
						sEditable = "readonly";
						
					var nCellWidth = Number(nWidth)-1;
					var sHtml = "";
					sHtml += "<img alt border=\"0\" height=\"1\" src=\"\" width="+nCellWidth+"><br><input "+sEditable+" class=\"gridcell\" type=text id=\""+nRow+"_"+nCol+"\" value=\"\" maxlength=512 style=\"width:"+(nWidth-4)+"px;\" onchange=\"parent.parent.JLGridCtrl_onchangeEx(m_obj)\" onblur=\"javascript:if (parent.parent.JLGridCtrl_onBlurEx != null) parent.parent.JLGridCtrl_onBlurEx(m_obj)\">";
					td.innerHTML = sHtml;
					if (arg != null)
					{
						if (arg[i] != null)
						{
							var elmt = oFrameObj.m_wnd.document.getElementById(nRow+"_"+nCol);
							if (elmt != null)
								elmt.value = arg[i];
						}
					}
					// <td id=td_"+nRow+"_"+nCol+" align=left valign=top width=\""+nCellWidth+"\">
					td.id = "td_"+nRow+"_"+nCol;
					td.align = "left";
					td.valign = "top";
					td.width = nCellWidth;
					// td.onkeydown = new Function("JLGridCtrl_onKeyDownEx(eval(\"obj\")");
					// td.onblur = new Function("JLGridCtrl_onBlurEx(obj)");
				}
			}
		}

		// 현재 segment의 height를 키운다.
		{
			var nPage = parseInt(idx/oFrameObj.m_nPageSize);
			// 현재 segment의 start를 찾아낸다.
			for (var i=0;i<4;i++)
			{
				var sFrameName = "griddata"+(nPage-i);
				var elmt = null;
				var elmt = oGrid.m_wnd.document.getElementById(sFrameName);
				if (elmt != null)
				{
					// alert("frame height : " + elmt.style.pixelHeight);
					elmt.style.pixelHeight += 20;
					break;
				}
			}
		}

		// next segment의 위치를 20 높이만큼 미룬다.
		{
			var nPage = parseInt(idx/oFrameObj.m_nPageSize);
			// current page의 height는 늘리지 않아도 자동으로 늘어난다.
			// 바로 다음에 있는 divPage의 위치를 20만큼 아래로 이동한다.
			{
				var elmt = oGrid.m_wnd.document.getElementById("divPage"+(nPage+1));
				if (elmt != null)
				{
					var elmtFrame = eval("oGrid.m_wnd.griddata"+(nPage+1));
					if (elmtFrame != null)
					{
						elmt.style.posTop = elmt.style.posTop+20;
						var oFrame = elmtFrame._getThis();
						if (oFrame != null)
							JLGridCtrl_increaseRowIdx(oFrame,0);
					}
				}
			}
		}
		// 이후 오는 모든 segment를 삭제합니다.
		{
			var nPage = parseInt(idx/oFrameObj.m_nPageSize);
			var nRemainPageCount = parseInt(oFrameObj.m_nTotalCount/oFrameObj.m_nPageSize) - (oFrameObj.m_nPageStart+1);
			var nStart = nPage + 1;
			var nEnd = nStart + nRemainPageCount;
			// alert("start : " + nStart +" : " + " end : " + nEnd);
			for (var i=nStart;i<=nEnd;i++)
			{
				var elmt = oGrid.m_wnd.document.getElementById("divPage"+i);
				if (elmt != null)
					elmt.removeNode(true);
			}
		}
		oGrid.m_nUpdateIdx = idx;
		param._rowcount.value ++;
	}

	function JLGridCtrl_onchangeEx(obj)
	{
		var elmt = obj.m_wnd.event.srcElement;
		if (elmt == null)
			return;
		var id = new String(elmt.id);
		var nLen = id.length;
		var idx = id.indexOf("_");
		if (idx < 0)
			return;
		var row = id.substr(0,idx);

		var oGrid = _getObj('grid');
		if (oGrid == null)
			return;
		oGrid.m_nUpdateIdx = row;
	}

	function _onDeleteRow(idx)
	{
		alert("ondelete row : " + idx);
	}

	// invoked by framework when a cell value is update and focused row is changed
	function _onUpdateRow(idx,arg)
	{
		var oContainer = _getContainer();
		oContainer.JLGridCtrl_updateRowSvr(idx,arg);
	}
</script>

