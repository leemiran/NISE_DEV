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
	public class JLPopupMenu extends JLHttp
	{
		int m_nMenuWidth = 110;
		int m_nMenuLeft = 0;
		Vector m_vMenuItem;
		
		JLPopupMenu(javax.servlet.jsp.JspWriter out)
		{
			super(out);
			m_nMenuWidth = 110;
			m_nHeight = 20;
			this.m_nWidth = 200;
			m_vMenuItem = new Vector();
		}
		
		void init(String sName,int nWidth,int nMenuWidth)
		{
			setName(sName);
			this.m_nWidth = nWidth;
			m_nMenuWidth = nMenuWidth + 37;
		}
		
		void printStart()
		{
			// print Script
			String sHtml = "";
			sHtml += "<div id=\""+m_sName+"\" style=\"position:absolute; left:0px; top:0px; width:0px; height:0px; z-index:2; visibility: hidden\" > \n"; //
			sHtml += "<table border=0 cellpadding=\"0\" width=\""+this.m_nWidth+"\" cellspacing=\"0\" onMouseOver=\"_setPopupMenuVisible('"+m_sName+"','show');\" onMouseOut=\"_setPopupMenuVisible('"+m_sName+"','hide');\">\n"; //
			sHtml += "<tr height=1>\n"; //
			sHtml += "<td colspan=\"2\" rowspan=\"2\">\n"; //
			sHtml += "<table border=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse;\" cellspacing=\"0\" width=\""+(this.m_nWidth-1)+"\"  bgcolor=\"#ffffff\" >\n"; //
			sHtml += "<tr>\n"; //
			sHtml += "<td width=1 bgcolor=\"#666666\"></td>\n"; //
			if (m_nMenuWidth > 0)
			{
				sHtml += "<td width="+m_nMenuWidth+" bgcolor=\"#B0C4DE\"></td>\n"; //
				sHtml += "<td width=\""+(this.m_nWidth-m_nMenuWidth-1-1-1)+"\" bgcolor=\"#666666\"></td>\n"; //
			}
			else
			{
				sHtml += "<td width=1 bgcolor=\"#666666\"></td>\n"; //
				sHtml += "<td width=\""+(this.m_nWidth-1-1-1-1)+"\" bgcolor=\"#666666\"></td>\n"; //
			}
			sHtml += "<td width=1 bgcolor=\"#666666\"></td>\n"; //
			sHtml += "</tr>\n"; //
			sHtml += "<tr>\n"; //
			sHtml += "<td width=1 bgcolor=\"#666666\"></td>\n"; //
			sHtml += "<td colspan=2>\n"; //
			sHtml += "<table width=\""+(this.m_nWidth-3)+"\" bgcolor=\"#ffffff\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"; //
			printStream(sHtml);
		}
		
		void printEnd()
		{
			int nCount = m_vMenuItem.size();
			String sHtml = "";
			sHtml += "</table>\n"; //
			sHtml += "</td>	\n"; //			
			sHtml += "<td width=1 bgcolor=\"#666666\"></td>\n"; //
			sHtml += "</tr>\n"; //
			sHtml += "<tr>\n"; //
			sHtml += "<td colspan=4 width=\""+(this.m_nWidth-1-1-1)+"\" bgcolor=\"#666666\"></td>\n"; //
			sHtml += "</tr>\n"; //
			sHtml += "</table>\n"; //
			sHtml += "</td>\n"; //
			// sHtml += "<td width=\"1\" bgcolor=\"#ffffff\" height=\"1\"><img alt border=0 height=1 src=\"images/trans.gif\" width=\"1\"></td>\n"; //
			sHtml += "<td width=\"1\" height=\"1\"><img alt border=0 height=1 src=\"images/trans.gif\" width=\"1\"></td>\n"; //

			sHtml += "</tr>\n"; //
			sHtml += "<tr>\n"; //
			sHtml += "<td width=1 bgcolor=\"#999999\" height=\""+(20*nCount+3-2)+"\"></td>\n"; //
			sHtml += "</tr>\n"; //
			sHtml += "<tr>\n"; //
			// sHtml += "<td width=1 bgcolor=\"#ffffff\"></td>\n"; //
			sHtml += "<td width=1></td>\n"; //
			sHtml += "<td width=\""+(this.m_nWidth-2)+"\" bgcolor=\"#999999\"></td>\n"; //
			sHtml += "<td width=\"1\" bgcolor=\"#999999\"></td>\n"; //
			sHtml += "</tr>\n"; //
			sHtml += "</table>\n"; //
			sHtml += "</div>\n"; //
			printStream(sHtml);
		}

		void addMenuItem(String sButtonAction,String sCaption,String sIcon)
		{
			JLRcd rcd = new JLRcd();
			rcd.SetValue("action",sButtonAction);
			rcd.SetValue("caption",sCaption);
			rcd.SetValue("icon",sIcon);
			m_vMenuItem.addElement(rcd);
		}

		public void printObj(int nLeft,int nTop)
		{
			print("<!-- JLPopupMenu start : "+m_sName+" -->");
			printStart();
			String sButtonAction = "";
			String sCaption = "";
			String sIcon = "";
			int nIdx = 0;
			int nCount = m_vMenuItem.size();
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vMenuItem.elementAt(i);
				if (rcd == null)
					continue;
				sButtonAction = rcd.GetStrValue("action");
				sCaption = rcd.GetStrValue("caption");
				sIcon = rcd.GetStrValue("icon");

				nIdx = i;
				printStream("<tr style=\"cursor: hand;\">");
				printStream("<td onmouseup=\"javascript:"+sButtonAction+"\" onmouseover=\"javascript:JLPopupMenu_onMouseOver('"+m_sName+"','"+nIdx+"')\" onmouseout=\"javascript:JLPopupMenu_onMouseOut('"+m_sName+"','"+nIdx+"')\" id='"+m_sName+"_td_bg_"+nIdx+"' height=\"20\" width=\"107\" valign=\"top\"><table border=0 cellpadding=0 cellspacing=0><tr><td width=25 align=center><img alt border=0 height=1 src=images/trans.gif width=25><br><img alt border=0 height=4 src=images/trans.gif width=4><img alt border=0 src="+sIcon+"></td><td width="+(this.m_nWidth-25-3)+"><img alt border=0 height=4 src=images/trans.gif width="+(this.m_nWidth-25-3)+"><br><font color='#000000' style=\"FONT: 9pt ±¼¸²\" id='"+m_sName+"_fnt_"+nIdx+"'>"+sCaption+"</font></td></tr></table></td>");
				printStream("</tr>");
			}
			printEnd();
			print("<!-- JLPopupMenu end : "+m_sName+" -->");
		}
	}
%>


