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

<!-- JLSplitter.jsp start -->

<script>
	function _printSkin()
	{
		var sHtml = "";
		sHtml += "<div id=_Skin style=\"z-index:200; POSITION: absolute; top:0px; left:0px; width:0px; height:0px; cursor: E-resize;\">";
		sHtml += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		sHtml += "<tr id=_SkinHeight height=\"0\">";
		sHtml += "<td id=_SkinWidth></td>";
		sHtml += "</tr>";
		sHtml += "</table>";
		sHtml += "</div>";

		var elmt = document.getElementById("_Skin");
		if (elmt == null)
		{
			// var sHtml = insertDQuoteEsc(sHtml);
			document.write(sHtml);
		}
	}
	_printSkin();

	function JLSplitter_setDragPosition(obj,id,nPosLeft,nPosTop)
	{
		if (obj == null)
			return;
		// alert(id);
		var bHor = Number(_getValue(id,"hor"));
		if (bHor == 0)
			obj.style.pixelLeft = nPosLeft;
		else
			obj.style.pixelTop = nPosTop;
	}

	function JLSplitter_setDropPosition(obj,id,nPosLeft,nPosTop)
	{
		if (obj == null)
			return;
		var bHor = Number(_getValue(id,"hor"));
		if (bHor == 0)
		{
			// vert
			_setValue(id,"left",nPosLeft);
		}
		else
		{
			// hor
			_setValue(id,"top",nPosTop);
		}

		var sSplit = _getValue(id,"parent");
		var nLeft = Number(_getValue(sSplit,"left"));
		var nTop = Number(_getValue(sSplit,"top"));
		var nWidth = Number(_getValue(sSplit,"width"));
		var nHeight = Number(_getValue(sSplit,"height"));

		// alert("split width : " + nWidth);
		// alert("setDropPosition  "+ sSplit+"    _:"+nLeft+":"+nTop+":"+nWidth+":"+nHeight);
		JLSplitter_setSize(sSplit,nLeft,nTop,nWidth,nHeight);
	}

	function JLSplitter_setSize(id,nLeft,nTop,nWidth,nHeight)
	{
		// alert(nLeft +":"+ nTop +":"+ nWidth +":"+ nHeight);
		// alert(id+"    _:"+nLeft+":"+nTop+":"+nWidth+":"+nHeight);

		_setStyle(id,"div","left",nLeft);
		_setStyle(id,"div","top",nTop);
		_setStyle(id,"","width",nWidth);
		_setStyle(id,"","height",nHeight);

		_setValue(id,"left",nLeft);
		_setValue(id,"top",nTop);
		_setValue(id,"width",nWidth);
		_setValue(id,"height",nHeight);

		var nChildLeft = 0;
		var nChildTop = 0;
		var nSplitLeft = 0;
		var nSplitTop = 0;
		var nChildWidth = 0;
		var nChildHeight = 0;
		var nCount = Number(_getValue(id,"childcount"));
		for (var i=0;i<nCount;i++)
		{
			if (i < nCount-1)
			{
				nSplitLeft = _getValue(id+i,"left");
				nSplitTop = _getValue(id+i,"top");
				var bHor = _getValue(id+i,"hor");
				if (i <= nCount-1)
				{
					if (bHor == 0)
						// vertical
						nSplitTop = 0;
					else
						// hor
						nSplitLeft = 0;
					JLSplitter_setSplitPosition(id+i,nSplitLeft,nSplitTop,nWidth,nHeight);
				}
			}
		}
		for (var i=0;i<nCount;i++)
		{
			var sChild = _getArrayValue(id,"child",i);
			// alert("splitter child : "+sChild);
			if (sChild == "undefined" || sChild == null || sChild == "")
				continue;

			if (bHor == 0)
			{
				nChildLeft += nChildWidth;
				if (i > 0)
					nChildLeft += 5;
				var nSplitLeft = Number(_getValue(id+0,"left"));
				if (i == 0)
					nChildWidth = nSplitLeft;
				else
					nChildWidth = nWidth - (nSplitLeft+5);
				nChildHeight = nHeight;
			}
			else
			{
				nChildTop += nChildHeight;
				if (i > 0)
					nChildTop += 5;
				nChildWidth = nWidth;
				var nSplitTop = Number(_getValue(id+0,"top"));
				if (i == 0)
					nChildHeight = nSplitTop;
				else
					nChildHeight = nHeight - (nSplitTop+5);
			}
			nPrevHeight = nChildHeight;

			var sClass = _getValue(sChild,"class");
			var sExp = "window."+sClass+"_setSize";
			// alert(sExp);
			if (eval(sExp) != null)
			{
				sExp = "window."+sClass+"_setSize(sChild,nChildLeft,nChildTop,nChildWidth,nChildHeight);";
				eval(sExp);
			}
		}
	}
</script>


<SCRIPT language=javascript>
// drag-drop code
// This code allows any absolutely positioned element to be dragged.
var elDragged = null  // Track current item.
function onMouseMoveEx() 
{
	// Check if mouse button is down and if an element is being dragged
	if ((1 == event.button) && (elDragged != null)) 
	{
		// Move the element
		// Where the mouse is in the document
		var intTop = event.clientY+document.body.scrollTop; 
		var intLeft = event.clientX + document.body.scrollLeft;
		// Calculate what element the mouse is really in
		var intLessTop  = 0;
		var intLessLeft = 0;
		var elCurrent = elDragged.offsetParent;
		while (elCurrent.offsetParent!=null) 
		{
			intLessTop+=elCurrent.offsetTop;
			intLessLeft+=elCurrent.offsetLeft;
			elCurrent = elCurrent.offsetParent;
		}
		// Set new position

		var sClass = _getValue(elDragged.id,"class");
		if (sClass == "undefined")
		{
			// alert("the variable is undefined : "+elDragged.id+" - " + sClass);
			return;
		}
		// elDragged.style.pixelTop = intTop  - intLessTop - elDragged.y;
		if (eval("window."+sClass+"_setDragPosition") != null)
		{
			var sExp = "window."+sClass+"_setDragPosition(elDragged,elDragged.id,intLeft - intLessLeft  - elDragged.x,intTop  - intLessTop - elDragged.y);";
			// alert(sExp);
			eval(sExp);
		}
		event.returnValue = false;
	};
};
function checkDrag(elCheck) 
{
	// Check if the clicked inside an element that supports dragging
	while (elCheck!=null) 
	{
		if (null!=elCheck.getAttribute("dragEnabled")) 
		{
			// by hank
			onDragStartEx();
			return elCheck
		}
		elCheck = elCheck.parentElement
	}      
	return null
}
function doMouseDown() 
{
	// Store element on mousedown.  Called from click handler in code below .
	// All elements that have a "dragEnabled" attribute and are positioned
	// can be dragged.
	var elCurrent = checkDrag(event.srcElement)
	if (null!=elCurrent) 
	{
		elDragged = elCurrent;
		if (elDragged != null)
		{
			var sCursor = _getStyle(elDragged.id,"","cursor");
			// alert("elDragged : "+elDragged.id+" - "+sCursor);
			if (sCursor != "" && sCursor != "undefined")
			{
				_setStyle("_Skin","","cursor",sCursor);
			}
		}
		
		// Determine where the mouse is in the element
		elDragged.x = event.offsetX
		elDragged.y = event.offsetY
		var op = event.srcElement
		// Find real location in respect to element being dragged.
		if ((elDragged!=op.offsetParent) && (elDragged!=event.srcElement)) 
		{
			while (op!=elDragged) 
			{
				elDragged.x+=op.offsetLeft
				elDragged.y+=op.offsetTop
				op=op.offsetParent
			}
		}
	}
}
function doSelectStart() 
{
	// Don't start text selections in dragged elements.
	return (null==checkDrag(event.srcElement) && (elDragged!=null))
}
function onMouseUpEx()
{
	if (elDragged != null)
		onDragEndEx();
	// alert(elDragged.style.pixelLeft);
	if (elDragged != null)
	{
		var sClass = _getValue(elDragged.id,"class");
		if (eval("window."+sClass+"_setDropPosition") != null)
		{
			var sExp = "window."+sClass+"_setDropPosition(elDragged,elDragged.id,elDragged.style.pixelLeft,elDragged.style.pixelTop);";
			eval(sExp);
		}
	}
	elDragged=null;
}
// Process mousemove.
document.onmousedown = doMouseDown;
document.onmousemove = onMouseMoveEx;
// Reset element on mouseup.
document.onmouseup = onMouseUpEx;
document.onselectstart = doSelectStart;

function onDragStartEx()
{
	var nWidth = document.body.clientWidth;
	var nHeight = document.body.clientHeight;
	_setSkinSize("",nWidth,nHeight);
}

function onDragEndEx()
{
	_setSkinSize("",0,0);
}

function _setSkinSize(id,nWidth,nHeight)
{
	var elmt = null;
	elmt = document.getElementById(id+"_Skin");
	if (elmt != null)
	{
		elmt.style.width = nWidth;
		elmt.style.height = nHeight;
	}
	elmt = document.getElementById(id+"_SkinHeight");
	if (elmt != null)
		elmt.style.height = nHeight;
	// alert(elmt.style.height);
	elmt = document.getElementById(id+"_SkinWidth");
	if (elmt != null)
		elmt.style.width = nWidth;
}

// id : split0
function JLSplitter_setSplitPosition(id,nLeft,nTop,nWidth,nHeight)
{
	// alert("setSplitPosition "+id +"_" + nLeft+":"+nTop+":"+nWidth+":"+nHeight);

	var nHor = Number(_getValue(id,"hor"));
	if (nHor == 0)
	{
	}
	else
	{
		// attach right : ¾à½Ä
		nWidth += 1;
	}

	var obj = null;
	var sParent = _getValue(id,"parent");	// split is parent of split0
	obj = document.getElementById(sParent+"_div");
	if (obj != null)
	{
		var sExp = "";
		sExp = "obj.style.posLeft = "+sParent+"_left";
		sExp = "obj.style.posTop = "+sParent+"_top";
	}
	obj = document.getElementById(id+"");
	if (obj != null)
	{
		if (nHor != 0)
		{
			_setValue(id,"top",nTop);
			eval("obj.style.posTop = "+id+"_top");
		}
		else
		{
			// vertical
			_setValue(id,"left",nLeft);
			eval("obj.style.posLeft = "+id+"_left");
		}
	}
	obj = document.getElementById(id+"_size");
	if (obj != null)
	{
		if (nHor != 0)
		{
			// hor
			obj.style.width = nWidth-2;
		}
		else
			// vert
			obj.style.height = nHeight;
	}
	obj = document.getElementById(id+"_pos");
	if (obj != null)
	{
		if (nHor != 0)	// hor
		{
			eval("obj.style.posTop = "+id+"_top");
		}
		else
		{
			eval("obj.style.posLeft = "+id+"_left");
		}
	}
	obj = document.getElementById(id+"_posSize");
	if (obj != null)
	{
		if (nHor != 0)
			// hor
			obj.style.width = nWidth-2;
		else
			// vert
			obj.style.height = nHeight;
	}
}
</script>

<%!
public class JLSplitter extends JLHttp
{	
	int m_nHor;	// 0 : Vertical, 1 : Horizontal
	int m_nOverlap;	// 1 : left, 2 : right, 3 : top, 4 : bottom
	int m_nLeft;
	int m_nTop;
	Vector m_vSplit;
	JLHttp m_aChild[];
	int m_nCol;
	int m_nRow;
	
	JLSplitter(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		setClassName("JLSplitter");
		m_nHor = 0;
		m_vSplit = new Vector();

		JLRcd rcd = new JLRcd();
		m_vSplit.addElement(rcd);
		m_nCol = 0;
		m_nRow = 0;
	}

	void init(JLHttp oParent, String sName,int nLeft,int nTop,int nRight,int nBottom,int nHor,int nOverlap,int nCol,int nRow)
	{
		m_nCol = nCol;
		m_nRow = nRow;
		setParent(oParent);
		setName(sName);
		m_nHor = nHor;
		m_nOverlap = nOverlap;
		setPosition(nLeft,nTop,-1,-1,nRight,nBottom);

		int nSize = nCol*nRow;
		if (nSize < 0)
			nSize = 3;
		m_aChild = new JLHttp[nSize];
	}

	void setChild(int nCol,int nRow,JLHttp oChild)
	{
		int nSize = m_nCol*m_nRow;
		if (nSize < 0)
			nSize = 3;
		int nIdx = m_nCol*nRow + nCol;
		if (nIdx > nSize || nIdx < 0)
			return;
		m_aChild[nIdx] = oChild;
		addChild(oChild);
	}

	void printMoveSplitter()
	{
		if (m_nHor == 0)
		{
			// vertical
			print("<div id="+m_sName+"0 style=\"POSITION: absolute; top:0px; left:0px; width:5px; cursor: E-resize;\" dragenabled>");
			print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			print("<tr id="+m_sName+"0_size>");
			// print("<td bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\""+m_sBtnFace+"\"><img alt border=0 src=images/trans.gif width=3 height=1></td>");
			print("<td bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("</table>");
			print("</div>");
		}
		else
		{
			print("<div id="+m_sName+"0 style=\"POSITION: absolute; top:0px; left:0px; height:5px; cursor: N-resize;\" dragenabled>");
			print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			print("<tr height=1>");
			print("<td width=1 bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif id="+m_sName+"0_size style=\"width:1px; height:1px;\"></td>");
			print("<td width=1 bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("<tr height=3>");
			print("<td width=1 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td width=1 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("<tr height=1>");
			print("<td width=1 bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			if (m_nOverlap == 2)
				print("<td width=1><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			else
				print("<td width=1 bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("</table>");
			print("</div>");
		}
	}
	
	void printPosSplitter()
	{
		if (m_nHor == 0)
		{
			// vertical
			print("<div id="+m_sName+"0_pos style=\"POSITION: absolute; top:0px; left:0px; width:5px; cursor: E-resize;\" dragenabled>");
			print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			print("<tr id="+m_sName+"0_posSize>");
			// print("<td bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=3 height=1></td>");
			print("<td bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("</table>");
			print("</div>");
		}
		else
		{
			print("<div id="+m_sName+"0_pos style=\"POSITION: absolute; top:0px; left:0px; height:5px; cursor: N-resize;\" dragenabled>");
			print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">");
			print("<tr height=1>");
			print("<td width=1 bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif id="+m_sName+"0_posSize style=\"width:1px; height:1px;\"></td>");
			print("<td width=1 bgcolor=\"#ffffff\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("<tr height=3>");
			print("<td width=1 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td width=1 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("<tr height=1>");
			print("<td width=1 bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("<td bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			if (m_nOverlap == 2)
				print("<td width=1><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			else
				print("<td width=1 bgcolor=\"#777777\"><img alt border=0 height=1 src=images/trans.gif width=1 height=1></td>");
			print("</tr>");
			print("</table>");
			print("</div>");
		}
	}
	
	public void printObj(int nLeft,int nTop)
	{
		m_nLeft = nLeft;
		m_nTop = nTop;
		printStream("<!-- "+m_sName+" start -->");
		printStream("<div id=\""+m_sName+"_div\" style=\"POSITION: absolute; top:"+m_nTop+"px; left:"+m_nLeft+"px;\">");

		JLRcd rcd = (JLRcd)m_vSplit.elementAt(0);
		if (rcd == null)
			return;
		int nSplitLeft = rcd.GetIntValue("left");
		int nSplitTop = rcd.GetIntValue("top");

		print("<script language=\"javascript\">");
		declVar(m_sName,"left",m_nLeft);
		declVar(m_sName,"top",m_nTop);
		declVar(m_sName+"0","hor",m_nHor);
		declVar(m_sName+"0","left",nSplitLeft);
		declVar(m_sName+"0","top",nSplitTop);
		print("</script>");

		int nCount = m_vChild.size();
		print("<script language=\"javascript\">");
		declVar(m_sName+"0","parent",m_sName);
		print("</script>");
		printChildHtml(0,0);
		print("<script language=\"javascript\">");
		declVar(m_sName+"0","class",m_sClassName);
		print("</script>");

		printPosSplitter();
		printMoveSplitter();

		printStream("</div>");
		printStream("<!-- "+m_sName+" end -->");
	}

	// nType : 0 - absolute value
	// nType : 1 - percent value , 30 means 30 %
	// nLeft : -1 : means ,, attach left
	public void setSplitPosition(int nCol,int nRow,int nLeft,int nTop,int nPosType)
	{
		if (nCol < 0 || nCol > 1 || nRow < 0 || nRow > 1)
			return;
		JLRcd rcd = (JLRcd)m_vSplit.elementAt(0);
		if (rcd == null)
			return;
		rcd.SetValue("left",nLeft);
		rcd.SetValue("top",nTop);
		rcd.SetValue("postype",nPosType);
	}
}
%>

<!-- JLSplitter.jsp end -->



