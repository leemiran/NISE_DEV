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

<!-- JSHttp.jsp - start -->
<script>
	function getAncestor(wnd,sName)
	{
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

	function JLHttp_dumpHtml(sArg)
	{
		document.write("<font style=\"FONT: 9pt\">");
		var sHtml = new String(sArg);
		var size = sHtml.length;
		for (var i=0;i<size;i++)
		{
			if (sHtml.charAt(i) == '<')
				document.write("&lt;");
			else
			{
				if (sHtml.charAt(i) == '>')
					document.write("&gt;<br>");
				else
					document.write(sHtml.charAt(i));
			}
		}
		document.write("</font>");
	}

	function JLCaptionFrame_printFrame(wnd,id,sSrc,nLeft,nTop,nWidth,nHeight)
	{
		if (wnd == null)
			return;
		var sHtml = "";
		sHtml += "<IFRAME NAME=client frameborder=\"0\" src=\""+sSrc+"\" scrolling=auto height=0 STYLE=\"z-index:1; LEFT: "+nLeft+"px; top:"+nTop+"px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH:0px; \"></IFRAME>";
		wnd.document.write(sHtml);
	}

	function drawCaption(wnd,id,sCaption,nLeft,nTop,nWidth,nHeight)
	{
		var sHtml = "";
		sHtml += "<div id=\""+id+"\" STYLE=\"z-index:1; LEFT: "+nLeft+"px; top:"+nTop+"px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH:"+nWidth+"px; \">";
		sHtml += "<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
		sHtml += "<tr height=\"1\">";
		sHtml += "<td bgcolor=\"#ffffff\"></td>";
		sHtml += "<td bgcolor=\"#777777\" width=1></td>";
		sHtml += "</tr>";
		sHtml += "<tr height=\"+(nHeight-2)+\">";
		sHtml += "<td id="+id+"_width bgcolor=\""+m_sBtnFace+"\" width="+nWidth+"><img alt border=0 src=images/trans.gif width=10 height=2><br><img alt border=0 height=1 src=images/trans.gif width=3 height=2><font color=\"#000000\" style=\"FONT: 9pt\">"+sCaption+"</font></td>";
		sHtml += "<td bgcolor=\"#777777\" width=1></td>";
		sHtml += "</tr>";
		sHtml += "<tr height=\"1\">";
		sHtml += "<td bgcolor=\"#777777\" colspan=2></td>";
		sHtml += "</tr>";
		sHtml += "</table>";
		sHtml += "</div>";
		wnd.document.write(sHtml);
	}

	function JLCaptionFrame_printObj(wnd,id,sCaption,sSrc)
	{
		if (wnd == null)
			return;
		window.name = id;
		drawCaption(wnd,"caption",sCaption,2,1,197,21);
		JLCaptionFrame_printFrame(wnd,"client",sSrc,1,20,0,0);
		drawLine(wnd,id+"_topline",0,0,document.body.clientWidth,1,"#777777");
		drawLine(wnd,id+"_leftline",0,0,1,document.body.clientHeight,"#777777");
	}

	function JLCaptionFrame_setSize() 
	{
		var id = window.name;
		var nHeight = document.body.clientHeight;
		var nWidth = document.body.clientWidth;

		document.all.caption.style.width = nWidth - 2;
		document.all.caption_width.style.width = nWidth - 2;
		document.all.client.style.width = nWidth - 1;
		document.all.client.style.height = nHeight - 20;
		moveLine(window,id+"_topline",0,0,document.body.clientWidth,1,"#777777");
		moveLine(window,id+"_leftline",0,0,1,document.body.clientHeight,"#777777");
	}
</script>

<script>
	var m_sFocusRow = "";
	var m_sBtnFace = "#d4d0c8";

	function onitem_setBackgroundColor(obj,id,color)
	{
		if (obj == null)
			return;
		obj.style.backgroundColor=color;
	}

	function onitemover(id)
	{
		var obj = document.getElementById(id);
		if (obj == null)
			return;
		if (m_sFocusRow == id)
			return;
		onitem_setBackgroundColor(obj,id,'#cccccc');
	}
	
	function onitemclick(id)
	{
		var obj = document.getElementById(id);
		if (obj == null)
			return;
		if (m_sFocusRow != '')
		{
			var fobj = document.getElementById(m_sFocusRow);
			if (fobj != null)
				onitem_setBackgroundColor(fobj,m_sFocusRow,'#ffffff');
		}
		m_sFocusRow = id;
		onitem_setBackgroundColor(obj,id,'#B0C4DE');
	}

	function onitemout(id)
	{
		if (m_sFocusRow == id)
			return;
		var obj = document.getElementById(id);
		if (obj != null)
			onitem_setBackgroundColor(obj,id,'#ffffff');
	}

	function _printSizeHtml()
	{
		var sHtml = "";
		sHtml += "<div style=\"POSITION: absolute; top:-1000px; left:-1000px; width:1000px;\">";
		sHtml += "-<div id=divHtmlSize style=\"POSITION: absolute; top:100px; left:0px;\">";
		sHtml += "</div>";
		sHtml += "</div>";
		document.write(sHtml);
	}
	_printSizeHtml();

	var m_oSize = document.getElementById("divHtmlSize");

	function _getHtmlWidth(sHtml)
	{
		if (m_oSize != null)
		{
			m_oSize.innerHTML = sHtml;
			var nSize = m_oSize.clientWidth;
			m_oSize.innerHTML = "";
			return nSize;
		}
		return 0;
	}

	function onJumpMenu(targ,selObj,restore)
	{
		eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
		if (restore)
			selObj.selectedIndex=0;
	}
	function MM_findObj(n, d)
	{
		var p,i,x;
		if(!d) 
			d=document; 
		if((p=n.indexOf("?"))>0&&parent.frames.length) 
		{
			d=parent.frames[n.substring(p+1)].document; 
			n=n.substring(0,p);
		}
		if(!(x=d[n])&&d.all) 
			x=d.all[n]; 
		for (i=0;!x&&i<d.forms.length;i++) 
			x=d.forms[i][n];
		for(i=0;!x&&d.layers&&i<d.layers.length;i++) 
			x=MM_findObj(n,d.layers[i].document);
		if(!x && document.getElementById) 
			x=document.getElementById(n); 
		return x;
	}
	var m_objid = "";
	var m_v;
	var m_objLayer;
	var m_interval;
	var m_check=0;
	function showHideLayers() 
	{
		//alert("showhidelayers");
		var i,p,v,obj,args=showHideLayers.arguments;
		// for (i=0; i<(args.length-2); i+=3) 
		{
			// obj=MM_findObj(args[i]);
			obj=MM_findObj(args[0]);
			// alert(obj.id);
			if (obj != null) 
			{ 
				// v=args[i+2];
				v=args[2];
				if (obj.style) 
				{ 
					obj=obj.style; 
					// by hank
					m_objid = args[3];
					layercontrol(v,obj);
					//v=(v=='show')?'visible':(v='hide')?'hidden':v; 
				}
				//obj.visibility=v; 
			}
		}
	}
	function layercontrol(v,obj)
	{
		m_v = v;
		m_objLayer = obj;
		if (m_v == 'show')
		{			
			//cleartime();
			showlayer()
		}
		else
		{ 
			if (m_v == 'hide')
			{
				// m_interval = window.setTimeout('hidelayer()',500);
				hidelayer();
			}
		}
	}
	function showlayer()
	{
		if (window.onmouseover_menubarbtn_popup != null && m_objid != "")
			window.onmouseover_menubarbtn_popup(m_objid);
				
		//alert("showlayer");
		m_check = 0;
		m_objLayer.visibility = 'visible';
		// window.clearTimeout(m_interval);		
	}
	function hidelayer()
	{
		//alert("hidelayer");
		m_check ++;
		//if(m_check == 10)
		//{
			//alert(m_check);
			window.clearTimeout(m_interval);
			m_objLayer.visibility = 'hidden';
			m_check = 0;
		//}
		if (window.onmouseout_menubarbtn_popup != null && m_objid != "")
			window.onmouseout_menubarbtn_popup(m_objid);
		m_objid = "";
	}
	var m_layer = '';
	function showHideLayersEx(a,b,c,id) 
	{		
		showHideLayers(a,b,c,id);
	}

	function drawLine(wnd,sID,nLeft,nTop,nWidth,nHeight,sColor)
	{
		var sHtml = "";
		sHtml += "<div id="+sID+" style=\"POSITION: absolute; top:"+nTop+"px; left:"+nLeft+"px;\">";
		sHtml += "<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" style=\"POSITION: absolute; top:0px; left:0px;\">";
		sHtml += "<tr id=\""+sID+"_height\" height="+nHeight+">";
		sHtml += "<td id=\""+sID+"_width\" width="+nWidth+" bgcolor=\""+sColor+"\"></td>";
		sHtml += "</tr>";
		sHtml += "</table>";
		sHtml += "</div>";
		wnd.document.write(sHtml);
	} 

	function moveLine(wnd,sID,nLeft,nTop,nWidth,nHeight,sColor)
	{
		if (wnd == null)
			return;

		var elmt = null;
		elmt = wnd.document.getElementById(sID);
		if (elmt != null)
		{
			elmt.style.top = nTop;
			elmt.style.left = nLeft;
			elmt.style.width = nWidth;
			elmt.style.height = nHeight;
		}
		elmt = wnd.document.getElementById(sID+"_height");
		if (elmt != null)
			elmt.height = nHeight;
		elmt = wnd.document.getElementById(sID+"_width");
		if (elmt != null)
		{
			elmt.width = nWidth;
			elmt.bgcolor = sColor;
		}
	}
	function _getArrayValue(sObj,sProp,idx)
	{
		var sExp = "window._get_"+sObj+"_"+sProp;
		if (eval(sExp) == null)
		{
			// alert(sExp + " is null");
			return null;
		}
		return sRet = eval("window._get_"+sObj+"_"+sProp+"(idx)");
	}
	function _getValue(sObj,sProp)
	{
		var sExp = "window._get_"+sObj+"_"+sProp;
		if (eval(sExp) == null)
		{
			// alert(sExp + " is null");
			return null;
		}
		return sRet = eval("window._get_"+sObj+"_"+sProp+"()");
	}
	function _setValue(sObj,sProp,sValue)
	{
		var sExp = "window._set_"+sObj+"_"+sProp;
		if (eval(sExp) == null)
		{
			// alert(sExp + " is null");
			return null;
		}
		sRet = eval("window._set_"+sObj+"_"+sProp+"(\""+sValue+"\")");
	}
	function _getStyle(id,prop,style)
	{
		var sProp = id;
		if (prop != "")
			sProp = id+"_"+prop;
		var obj = document.getElementById(sProp);
		if (obj != null)
		{
			var sVal = eval("obj.style."+style);
			return sVal;
		}
	}
	function _setStyle(id,prop,style,sValue)
	{
		var sProp = id;
		if (prop != "")
			sProp = id+"_"+prop;
		var obj = document.getElementById(sProp);
		if (obj != null)
		{
			eval("obj.style."+style+"=\""+sValue+"\"");
		}
	}
</script>

<!-- JSHttp.jsp - end -->
