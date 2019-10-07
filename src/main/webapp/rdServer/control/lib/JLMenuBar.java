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


<%!
	public class JLMenuBar extends JLHttp
	{
		int m_nMenuLeft = 0;
		Vector m_vMenu;
		
		JLMenuBar(javax.servlet.jsp.JspWriter out)
		{
			super(out);
			m_nMenuLeft = 8;	// left gripper width (15) + left Edge (1)
			m_nWidth = 0;
			m_nHeight = 25;	// 21 pixel + 2 + 2
			m_vMenu = new Vector();
			m_nLeft = 0;
			
			setClassName("JLMenuBar");
		}
		
		void init(String sName,int nWidth,int nHeight)
		{
			setName(sName);
			m_nHeight = nHeight;
			m_nWidth = nWidth;
		}
		
		void addMenu(String sName,int nWidth,String sText,String sAction,String sIcon,String sPopup,int nPopupItem,boolean bSplit)
		{
			JLRcd rcd = new JLRcd();
			
			rcd.SetValue("name",sName);
			rcd.SetValue("width",nWidth);
			rcd.SetValue("text",sText);
			rcd.SetValue("action",sAction);
			rcd.SetValue("icon",sIcon);
			rcd.SetValue("popup",sPopup);
			rcd.SetValue("popupitem",nPopupItem);
			if (bSplit)
				rcd.SetValue("split",1);
			else
				rcd.SetValue("split",0);
			
			m_vMenu.addElement(rcd);
		}
	
		public void printObj(int nLeft,int nTop)
		{
			printStream("<!-- JLMenuBar start : "+m_sName+" -->");

			m_nLeft = nLeft;
			m_nTop = nTop;
			printStream("<div id="+m_sName+"_div style=\"POSITION: absolute; top:"+nTop+"px; left:"+nLeft+"px;\">");
			int nCount = m_vMenu.size();
			print("<script language=\"javascript\">");
			declVar(m_sName,"class",m_sClassName);
			declVar(m_sName,"left",m_nLeft);
			declVar(m_sName,"top",m_nTop);
			declVar(m_sName,"height",m_nHeight);
			declVar(m_sName,"childcount",nCount);
			declArrayVar(m_sName,"child",nCount);
			String sPopup = "";
			// if (false)
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vMenu.elementAt(i);
				if (rcd == null)
					continue;
				sPopup = rcd.GetStrValue("popup");
				if (JLString.IsEmpty(sPopup))
					sPopup = "";
				declArrayValue(m_sName,"child",i,sPopup);
			}
			print("</script>");
			
			print("<!-- menu bar start -->");
			print("<table id=\""+m_sName+"\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">");
			print("<tr height=\""+m_nHeight+"\">");
			print("<td id=\""+m_sName+"_height\" width=2 bgcolor=\""+m_sBtnFace+"\" height="+m_nHeight+"></td>");
			print("<td id=\""+m_sName+"_width\" valign=top bgcolor=\"#d4d0c8\"><img alt border=0 height=1 src=images/trans.gif width=1><br>");
			print("<!-- menu items gripper start -->");
			print("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">");
			print("<tr>");
			printMenuItems();
			print("</tr>");
			print("</table>");
			print("<!-- menu items gripper end -->");

			print("</td>");
			print("</tr>");
			print("</table>");
			print("<!-- menu bar end -->");

			printStream("</div>");
			printStream("<!-- JLMenuBar end : "+m_sName+" -->");
		}
		
		void printMenuItems()
		{
			print("<!-- menu items start -->");
			print("<td valign=top>");
			printGripper();
			print("</td>");
			
			int nCount = m_vMenu.size();
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vMenu.elementAt(i);
				if (rcd == null)
					continue;
				
				String sName = rcd.GetStrValue("name");
				int nWidth = rcd.GetIntValue("width");
				String sText = rcd.GetStrValue("text");
				String sAction = rcd.GetStrValue("action");
				String sIcon = rcd.GetStrValue("icon");
				String sPopup = rcd.GetStrValue("popup");
				int nPopupItem = rcd.GetIntValue("popupitem");
				print("<td valign=top>");
				printMenuItem(sName,nWidth,sText,sAction,sIcon,24,sPopup,nPopupItem);
				print("</td>");
				print("<td valign=top>");
				int nSplit = rcd.GetIntValue("split");
				if (nSplit > 0)
				{
					printSplit(28);
					m_nMenuLeft += nWidth+40+6;	// 6 : splitter
				}
				else
					m_nMenuLeft += nWidth+40;	// 6 : splitter
				print("</td>");
			}
			print("<!-- menu items end -->");
		}
		
		void printMenuItem(String sName,int nWidth,String sText,String sAction,String sIcon,int nHeight,String sPopup,int nPopupItem)
		{
			print("<script language=\"javascript\"> <!-- "+sName+" -->");
			if (!JLString.IsEmpty(sPopup))
			{
				declVar(sName,"child",sPopup);
				declVar(sPopup,"parent",sName);
				declVar(sPopup,"left",(m_nMenuLeft+m_nLeft+1));
				declVar(sPopup,"top",0);
				declVar(sPopup,"width",200);
				declVar(sPopup,"height",20*nPopupItem+3);

				print("function onmouseover_"+sName+"_ex()");
				print("{");
				print("	if (window.onMenu != null) {");
				print("		window.onMenu(Number("+sPopup+"_left),"+sPopup+"_top,"+sPopup+"_width,"+sPopup+"_height,\""+sPopup+"\"); if (window._onMenuEx != null) window._onMenuEx('"+sName+"'); }");
				print("}");
			}
			else
			{
				declVar(sName,"child","");
			}
			print("</script>");

			// 1 + 26 + (nWidth+11) + 1 + 1
			// 29 + 14 + nWidth;
			int nTblWidth = 29+11+nWidth;
			if (JLString.IsEmpty(sPopup))
				print("<table border=\"0\" width="+nTblWidth+" cellPadding=\"0\" cellSpacing=\"0\" onmouseover=\"javascript:onmouseover_menubarbtn('"+sName+"'); if (window.onmouseover_"+sName+"_ex != null) window.onmouseover_"+sName+"_ex();\" onmousedown=\"javascript:onmousedown_menubarbtn('"+sName+"')\" onmouseup=\"javascript:onmouseup_menubarbtn('"+sName+"'); "+sAction+"\" onmouseout=\"javascript:if (onmouseout_menubarbtn != null) onmouseout_menubarbtn('"+sName+"');\" style=\"cursor: hand;\">");
			else
				print("<table border=\"0\" width="+nTblWidth+" cellPadding=\"0\" cellSpacing=\"0\" onmouseover=\"javascript:onmouseover_menubarbtn('"+sName+"'); if (window.onmouseover_"+sName+"_ex != null) window.onmouseover_"+sName+"_ex();\" onmousedown=\"javascript:onmousedown_menubarbtn('"+sName+"')\" onmouseup=\"javascript:onmouseup_menubarbtn('"+sName+"'); "+sAction+"\" onmouseout=\"javascript:if (onmouseout_menubarbtn != null) onmouseout_menubarbtn('"+sName+"'); if (window._setPopupMenuVisible != null) window._setPopupMenuVisible('"+sPopup+"','hide');\" style=\"cursor: hand;\">");
			print("<tr height=1>");
			print("<td id=td_lefttop_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_top0_"+sName+" bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_top_"+sName+" bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_righttop_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_righttopedge_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("</tr>");
			print("<tr height="+(nHeight-4)+">");
			print("<td id=td_left_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\"></td>");
			// print("<td id=td_icon_"+sName+" align=left width=26 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=10><br><img alt border=0 height=1 src=images/trans.gif width=2><img alt border=\"0\" src=\""+sIcon+"\"></td>");
			print("<td height=24 id=td_icon_"+sName+" align=left width=26 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=1 src=images/trans.gif width=2><img alt border=\"0\" src=\""+sIcon+"\"></td>");
			print("<td id=td_text_"+sName+" width="+(nWidth+11)+" bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=2 src=images/trans.gif width="+(nWidth+11)+"><br><font color=\"#000000\" style=\"FONT: 9pt ±¼¸²\">"+sText+"</font></td>");
			print("<td id=td_right_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\"></td>");
			print("<td id=td_rightedge_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\"></td>");
			print("</tr>");
			print("<tr height=1>");
			print("<td id=td_leftbottom_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_bottom0_"+sName+" bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_bottom_"+sName+" bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_rightbottom_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("<td id=td_rightbottomedge_"+sName+" width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("</tr>");
			print("</table>");
		}
	
		// width : 10
		void printGripper()
		{
			print("<!-- gripper start -->");
			print("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">");
			print("<tr>");
			print("<td align=left width=7 bgcolor=\""+m_sBtnFace+"\">");
			print("<img alt border=0 height=1 src=images/trans.gif width=7><br>");
			print("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" >");
			print("<tr height=1>");
			print("<td width=1 bgcolor=\"#ffffff\">");
			print("</td>");
			print("<td width=1 bgcolor=\"#ffffff\">");
			print("</td>");
			print("<td width=1 bgcolor=\"#777777\">");
			print("</td>");
			print("</tr>");
			print("<tr height=\""+(m_nHeight-6)+"\">");
			print("<td  width=1 bgcolor=\"#ffffff\"></td>");
			print("<td width=1 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td  width=1 bgcolor=\"#777777\">");
			print("</td>");
			print("</tr>");
			print("<tr height=1>");
			print("<td  width=1 bgcolor=\"#777777\">");
			print("</td>");
			print("<td  bgcolor=\"#777777\">");
			print("</td>");
			print("<td  width=1 bgcolor=\"#777777\">");
			print("</td>");
			print("</tr>");
			print("</table>");
			print("</td></tr>");
			print("</table>");
			print("<!-- gripper end -->");
		}
		
		// width : 6
		void printSplit(int nHeight)
		{
			print("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">");
			print("<tr>");
			print("<td bgcolor=\""+m_sBtnFace+"\">");
			print("<img alt border=0 height=1 src=images/trans.gif width=6><br>");
			print("<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">");
			print("<tr height=\""+(nHeight-4)+"\">");
			print("<td id=td_left width=2 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("<td id=td_top0 width=1 bgcolor=\"#777777\">");
			print("</td>");
			print("<td id=td_top width=1 bgcolor=\"#ffffff\">");
			print("</td>");
			print("<td id=td_right width=2 bgcolor=\""+m_sBtnFace+"\">");
			print("</td>");
			print("</tr>");
			print("</table>");
			print("</td></tr>");
			print("</table>");
		}
	
	}
%>
<!-- JLMenuBar.jsp end -->
