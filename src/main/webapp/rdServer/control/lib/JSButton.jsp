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
	function onmouseover_btn(id)
	{
		document.all["table_"+id].style.border = "#000000";
		document.all["table_"+id].style.icon = "hand";
		
		document.all["td_lefttop_"+id].style.background = "#666666"
		document.all["td_top0_"+id].style.background = "#666666"
		document.all["td_top_"+id].style.background = "#666666"
		document.all["td_righttop_"+id].style.background = "#666666"

		document.all["td_left_"+id].style.background = "#666666"
		document.all["td_right_"+id].style.background = "#ffffff"

		document.all["td_leftbottom_"+id].style.background = "#666666"
		document.all["td_bottom0_"+id].style.background = "#ffffff"
		document.all["td_bottom_"+id].style.background = "#ffffff"
		document.all["td_rightbottom_"+id].style.background = "#ffffff"
		
	}

	function onmouseup_btn(id)
	{
		onmouseover_btn(id);
	}

	function onmousedown_btn(id)
	{
		// alert('hello');

		document.all["td_lefttop_"+id].style.background = "#666666"
		document.all["td_top0_"+id].style.background = "#666666"
		document.all["td_top_"+id].style.background = "#666666"
		document.all["td_righttop_"+id].style.background = "#666666"

		document.all["td_left_"+id].style.background = "#666666"
		document.all["td_right_"+id].style.background = "#ffffff"

		document.all["td_leftbottom_"+id].style.background = "#666666"
		document.all["td_bottom0_"+id].style.background = "#ffffff"
		document.all["td_bottom_"+id].style.background = "#ffffff"
		document.all["td_rightbottom_"+id].style.background = "#ffffff"
	}

	function onmouseout_btn(id)
	{
		onmouseover_btn(id)

		document.all["td_lefttop_"+id].style.background = "#ffffff"
		document.all["td_top0_"+id].style.background = "#ffffff"
		document.all["td_top_"+id].style.background = "#ffffff"
		document.all["td_righttop_"+id].style.background = "#666666"

		document.all["td_left_"+id].style.background = "#ffffff"
		document.all["td_right_"+id].style.background = "#666666"

		document.all["td_leftbottom_"+id].style.background = "#666666"
		document.all["td_bottom0_"+id].style.background = "#666666"
		document.all["td_bottom_"+id].style.background = "#666666"
		document.all["td_rightbottom_"+id].style.background = "#666666"
	}

	function JLButton_printObj(nX,nY,sName,nWidth,nImgTopMargin,sButtonText,sAction,sIcon,nHeight)
	{
		m_sBtnFace = "#d4d0c8";
		var nBorder = 1;
		if (nHeight <= 15)
			nBorder = 0;
		var sHtml = "";
		var bAbsolute = true;
		if (nX < 0 || nY < 0)
			bAbsolute = false;
		if (bAbsolute)
			sHtml += "<div id=\"div_"+sName+"\" style=\"POSITION: absolute\">"; //
		sHtml += "<table id=table_"+sName+" border=\""+nBorder+"\" cellpadding=\"0\" style=\"border-collapse:collapse;\" bordercolor=\""+m_sBtnFace+"\" cellspacing=\"0\" width=\""+nWidth+"\"  bgcolor=\""+m_sBtnFace+"\" >"; //
		sHtml += "<tr>"; //
		sHtml += "		<td>"; //
		sHtml += "        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\">"; //
		sHtml += "		<tr>"; //
		sHtml += "		<td>"; //
		sHtml += "			<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">"; //
		sHtml += "			<tr>"; //
		sHtml += "				<td width=100% bgcolor=\""+m_sBtnFace+"\">"; //
		sHtml += "<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" onmouseover=\"javascript:onmouseover_btn('"+sName+"');\" onmousedown=\"javascript:onmousedown_btn('"+sName+"')\" onmouseup=\"javascript:onmouseup_btn('"+sName+"'); "+sAction+";\" onmouseout=\"javascript:onmouseout_btn('"+sName+"')\" style=\"cursor: hand;\">"; //
		sHtml += "			<tr height=1>"; //
		sHtml += "				<td id=td_lefttop_"+sName+" width=1 bgcolor=\"#ffffff\">"; //
		sHtml += "				</td>"; //
		sHtml += "				<td id=td_top0_"+sName+" width=25 bgcolor=\"#ffffff\">"; //
		sHtml += "				</td>"; //
		sHtml += "			<td id=td_top_"+sName+" bgcolor=\"#ffffff\">"; //
		sHtml += "				</td>"; //
		sHtml += "				<td id=td_righttop_"+sName+" width=1 bgcolor=\"#666666\">"; //
		sHtml += "				</td>"; //
		sHtml += "			</tr>"; //
		sHtml += "			<tr height='"+nHeight+"'>"; //
		sHtml += "				<td id=td_left_"+sName+" width=1 bgcolor=\"#ffffff\"></td>"; //
		sHtml += "				<td align=center valign=top width=25 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height="+nImgTopMargin+" src=images/trans.gif width=10><br><img alt border=\"0\" src=\""+sIcon+"\"></td>"; //
		sHtml += "				<td align=left width="+(nWidth-1-1-25)+" bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=2 src=images/trans.gif width=10><br><font color=\"#000000\" style=\"FONT: 9pt ±¼¸²\">"+sButtonText+"</font></td>"; //
		sHtml += "				<td id=td_right_"+sName+" width=1 bgcolor=\"#666666\"></td>"; //
		sHtml += "			</tr>"; //
		sHtml += "			<tr height=1>"; //
		sHtml += "				<td id=td_leftbottom_"+sName+" width=1 bgcolor=\"#666666\">"; //
		sHtml += "				</td>"; //
		sHtml += "				<td id=td_bottom0_"+sName+" width=25 bgcolor=\"#666666\">"; //
		sHtml += "				</td>"; //
		sHtml += "				<td id=td_bottom_"+sName+" bgcolor=\"#666666\">"; //
		sHtml += "				</td>"; //
		sHtml += "				<td id=td_rightbottom_"+sName+" width=1 bgcolor=\"#666666\">"; //
		sHtml += "				</td>"; //
		sHtml += "			</tr>"; //
		sHtml += "		</table>"; //
		sHtml += "			</tr>"; //
		sHtml += "			</table>"; //
		sHtml += "		</td>"; //
		sHtml += "		</tr>"; //
		sHtml += "		</table>"; //
		sHtml += "	</td>"; //
		sHtml += "</tr>"; //
		sHtml += "</table>"; //
		if (bAbsolute)
			sHtml += "</div>";
		document.write(sHtml);
		if (bAbsolute)
		{
			var id = "div_"+sName;
			var obj = document.getElementById(id);
			if (obj != null)
			{
				obj.style.left = nX;
				obj.style.top = nY;
			}
		}
	}

</script>

