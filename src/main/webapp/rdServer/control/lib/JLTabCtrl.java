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

<!-- JLTabCtrl start -->

<script>

</script>



<!-- JLTabCtrl.java start -->
<%!
	// btnface : #d4d0c8
	public class JLTabCtrl extends JLHttp
	{

		JLTabCtrl(javax.servlet.jsp.JspWriter out)
		{
			super(out);
			m_nIdx = 0;
			m_vPage = new Vector();
		}

		String m_sFaceBright = "#000080";
		String m_sFaceDark = "#000080";
		// String m_sBtnFace = "#d4d0c8";
		String m_sBtnFace = "#ffffff";
		String m_sInitialPage = "";
		int m_nIdx;
		Vector m_vPage;

		public void init(String sName,int nWidth,int nHeight)
		{
			m_nWidth = nWidth;
			m_nHeight = nHeight;
			setName(sName);
		}

		public void init(String sName,int nLeft,int nTop,int nWidth,int nHeight)
		{
			m_nLeft = nLeft;
			m_nTop = nTop;
			m_nWidth = nWidth;
			m_nHeight = nHeight;
			setName(sName);
		}

		public void addTab(String sName,int nWidth,int nHeight,int nImgTopMargin,String sText,String sAction,String sIcon,String sPage)
		{
			JLRcd rcd = new JLRcd();
			rcd.SetValue("name",sName);
			rcd.SetValue("width",nWidth);
			rcd.SetValue("height",nHeight);
			rcd.SetValue("imgtopmargin",nImgTopMargin);
			rcd.SetValue("text",sText);
			rcd.SetValue("action",sAction);
			rcd.SetValue("icon",sIcon);
			rcd.SetValue("page",sPage);
			m_vPage.addElement(rcd);
		}

		public void setInitialPage(String sName)
		{
			m_sInitialPage = sName;
		}

		public void printObj()
		{
			printTabs();
		}

		public int getPageIdx(String sName)
		{
			if (JLString.IsEmpty(sName))
				return -1;

			int nCount = m_vPage.size();
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vPage.elementAt(i);
				if (rcd == null)
					continue;
				String sCmp = (String)rcd.GetStrValue("name");
				if (JLString.Compare(sCmp,sName) == 0)
					return i;
			}
			return -1;
		}

		public void printTabs()
		{
			int nIdx = getPageIdx(m_sInitialPage);
			if (nIdx < 0)
				nIdx = 0;

			if(m_vPage.size() > 0)
         {
				// load default 0 page
				JLRcd rcd = (JLRcd)m_vPage.elementAt(nIdx);
				if (rcd != null)
					rcd.SetValue("load",1);

				print("<div id=elTab_"+m_sName+" style=\"POSITION: absolute;\">");
				printScript();
				printTabButtons();
				printTabBoxBegin();
				printTabPages();
				printTabBoxEnd();
				print("</div>");

				if (!JLString.IsEmpty(m_sInitialPage))
					print("<script>JLTabCtrl_focusPage('"+m_sName+"','"+m_sInitialPage+"'); window.status = ' ';</script>");
			}
			else
			   print("Not found service name");
		}

		public void printTabButtons()
		{
			print("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\""+(m_nWidth+2)+"\">");
			print("<tr> ");
			print("<script> ");
			print("JLTabCtrl_addTab(\""+m_sName+"\"); ");
			print("</script> ");

			int nRight = m_nWidth+2;
			int nCount = m_vPage.size();
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vPage.elementAt(i);
				if (rcd == null)
					continue;
				print("<td>");
				int nWidth = rcd.GetIntValue("width");
				int nImgTopMargin = rcd.GetIntValue("imgtopmargin");
				String sName = rcd.GetStrValue("name");
				String sText = rcd.GetStrValue("text");
				String sAction = rcd.GetStrValue("action");
				String sIcon = rcd.GetStrValue("icon");
				String sSrc = rcd.GetStrValue("page");
				int nHeight = rcd.GetIntValue("height");
				int bLoad = rcd.GetIntValue("load");

				print("<script> ");
				print("JLTabCtrl_addTabPage(\""+m_sName+"\",\""+sName+"\","+nWidth+","+nImgTopMargin+",\""+sText+"\",\""+sAction+"\",\""+sIcon+"\","+nHeight+",\""+sSrc+"\","+bLoad+"); ");
				print("</script> ");

				printButton(i,sName,nWidth,nImgTopMargin,sText,sAction,sIcon,nHeight,sSrc,bLoad);
				print("</td>");
				nRight -= rcd.GetIntValue("width");

				if (i < nCount-1)
				{
					print("<td>");
					printButtonGap(5);
					print("</td>");
					nRight -= 5;
				}
			}
			if (nRight > 0)
			{
				print("<td> <!-- right edge start -->");
				printTopRight(nRight);
				print("</td>");
			}

			print("</tr>");
			print("</table>");
		}

		public void printScript()
		{
			print("<script>");
			print("	var m_sFaceDark = \""+m_sFaceDark+"\";");
			print("	var m_sFaceBright = \""+m_sFaceBright+"\";");
			print("	var m_sBtnFace = \""+m_sBtnFace+"\";");

			print("	var m_sPages_"+m_sName+" = new Array()");
			print("	var m_sPagesLoad_"+m_sName+" = new Array()");
			print("	var m_sPagesSrc_"+m_sName+" = new Array()");
			int nCount = m_vPage.size();
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vPage.elementAt(i);
				if (rcd == null)
					continue;
				print("	m_sPages_"+m_sName+"["+i+"]=\""+rcd.GetStrValue("name")+"\";");
				print("	m_sPagesSrc_"+m_sName+"["+i+"]=\""+rcd.GetStrValue("action")+"\";");
				if (i == 0)
					print("	m_sPagesLoad_"+m_sName+"["+i+"] = true;");
				else
					print("	m_sPagesLoad_"+m_sName+"["+i+"] = false;");
			}
			print("</script>");
		}

		public void printTopRight(int nWidth)
		{
			String sHtml = "";
			sHtml = getTopRightHtml(nWidth);
			print(sHtml);
		}

		public void printButton(int nIdx,String sName,int nWidth,int nImgTopMargin,String sButtonText,String sAction,String sIcon,int nHeight,String sSrc,int bLoad)
		{
			String sHtml = getButtonHtml(nIdx,sName,nWidth,nImgTopMargin,sButtonText,sAction,sIcon,nHeight,sSrc,bLoad);
			print(sHtml);
		}

		public void printTabBoxBegin()
		{
			int nWidth = m_nWidth + 3;
			String sHtml = getTabBoxBeginHtml();
			print(sHtml);
		}

		public void printButtonGap(int nWidth)
		{
			String sHtml = getButtonGapHtml(nWidth);
			print(sHtml);
		}

		public void printTabPages()
		{
			int nWidth = m_nWidth + 3;
			int nHeight = m_nHeight;
			int nTopMargin = 22;
			int nIdx = 0;
			String sName = "";
			String sPage = "";
			int nCount = m_vPage.size();
			int bLoad = 0;
			for (int i=0;i<nCount;i++)
			{
				JLRcd rcd = (JLRcd)m_vPage.elementAt(i);
				if (rcd == null)
					continue;
				sName = rcd.GetStrValue("name");
				sPage = rcd.GetStrValue("page");
				bLoad = rcd.GetIntValue("load");
				// 처음에 0번 Page는 default 로 로드합니다.
				if (bLoad <= 0)
					sPage = "";
				if (i == 0)
					print("<div id=elPage"+i+"_"+m_sName+" style=\"POSITION: absolute; top:"+nTopMargin+"px;left:0px;\"><iframe id=\""+sName+"\" NAME=\""+sName+"\" height="+nHeight+" frameborder=\"0\" src=\""+sPage+"\" scrolling=auto STYLE=\"HEIGHT: "+nHeight+"px; POSITION:absolute; MARGIN-TOP: 0px; left:1px; top:0px; VISIBILITY: visible; WIDTH: "+m_nWidth+"px; \"></IFRAME></div>");
				else
					print("<div id=elPage"+i+"_"+m_sName+" style=\"POSITION: absolute; top:"+nTopMargin+"px;left:0px;\"><iframe id=\""+sName+"\" NAME=\""+sName+"\" height="+nHeight+" frameborder=\"0\" src=\""+sPage+"\" scrolling=auto STYLE=\"HEIGHT: "+nHeight+"px; POSITION:absolute; MARGIN-TOP: 0px; left:1px; top:0px; VISIBILITY: hidden; WIDTH: "+m_nWidth+"px; \"></IFRAME></div>");
				// print("<iframe id=\""+sName+"\" NAME=\""+sName+"\" height="+nHeight+" frameborder=\"0\" src=\""+sPage+"\" scrolling=no STYLE=\"HEIGHT: "+nHeight+"px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: hidden; WIDTH: "+m_nWidth+"px; \"></IFRAME>");
			}
			print("<table bgcolor=\""+m_sFaceBright+"\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\""+m_nWidth+"\">");
			print("<tr> ");
			print("<td><img alt border=\"0\" height=\""+nHeight+"\" src=\"images/trans.gif\"></td>");
			print("</tr>");
			print("</table>");
		}

		public void printTabBoxEnd()
		{
			String sHtml = "";
			sHtml = getTabBoxEndHtml();
			print(sHtml);
		}












































		public String getTabBoxBeginHtml()
		{
			String sHtml = "";
			// sHtml += "<div id=\""+m_sName+"\">";
			sHtml += "<!-- tab box begin -->\n";
			sHtml += "		<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" width=\""+(m_nWidth+2)+"\">\n";
			sHtml += "			<tr>\n";
			/*
			sHtml += "				<td width=1 bgcolor=\"#cccccc\"></td>\n";
			*/
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\"></td>\n";
			// sHtml += "				<td valign=top width="+(nWidth-4)+" bgcolor=\""+m_sBtnFace+"\">\n";
			// sHtml += "				<td valign=top width="+(nWidth-3)+" bgcolor=\""+m_sBtnFace+"\">\n";
			// sHtml += "				<td valign=top width="+(nWidth-2)+" bgcolor=\""+m_sFaceBright+"\">\n";
			// sHtml += "				<td valign=top width="+(nWidth-2)+" bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				<td valign=top width="+(m_nWidth-2)+" bgcolor=\""+m_sFaceBright+"\">\n";
			return sHtml;
		}

		public String getFocusButtonHtml(int nIdx,String sName,int nWidth,int nImgTopMargin,String sButtonText,String sAction,String sIcon,int nHeight,String sSrc,int bLoad)
		{
			String sHtml = "\n";

			sHtml += "		<table width=\""+nWidth+"\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" onmouseup=\"javascript:JLTabCtrl_ontabmouseup('"+m_sName+"','"+sName+"'); "+sAction+"\" style=\"cursor: hand;\">\n";
			sHtml += "			<tr height=1>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=25 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "			</tr>\n";
			sHtml += "			<tr height=20>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\"></td>\n";
			sHtml += "				<td align=center valign=top width=25 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height="+nImgTopMargin+" src=images/trans.gif width=10><br><img alt border=\"0\" src=\""+sIcon+"\"></td>\n";
			sHtml += "				<td align=left width="+(nWidth-25-4)+" bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=2 src=images/trans.gif width=10><br><font id=fnt_"+sName+" color=\"#aaaaaa\" style=\"FONT: 9pt 굴림\">"+sButtonText+"</font></td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\"></td>\n";
			sHtml += "			</tr>\n";
			sHtml += "			<tr height=1>\n";
			sHtml += "				<td id=td_btm2_2_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td id=td_btm2_3_"+sName+" width=25 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td id=td_btm2_4_"+sName+" bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td id=td_btm2_5_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "			</tr>\n";
			sHtml += "		</table>\n";
			return sHtml;
		}

		public String getButtonHtml(int nIdx,String sName,int nWidth,int nImgTopMargin,String sButtonText,String sAction,String sIcon,int nHeight,String sSrc,int bLoad)
		{
			String sHtml = "\n";
			if (nIdx == 0)
			{
				sHtml += "		<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" onmouseup=\"javascript:JLTabCtrl_ontabmouseup('"+m_sName+"','"+sName+"'); "+sAction+";\" style=\"cursor: hand;\">\n";
				sHtml += "			<tr height=1>\n";
				sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td width=25 bgcolor=\""+m_sFaceBright+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td bgcolor=\""+m_sFaceBright+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "			</tr>\n";
				sHtml += "			<tr height=20>\n";
				sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\"></td>\n";
				sHtml += "				<td align=center valign=top width=25 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height="+nImgTopMargin+" src=images/trans.gif width=10><br><img alt border=\"0\" src=\""+sIcon+"\"></td>\n";
				sHtml += "				<td align=left width="+(nWidth-25-2)+" bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=2 src=images/trans.gif width=10><br><font id=fnt_"+sName+" color=\"#000000\" style=\"FONT: 9pt 굴림\">"+sButtonText+"</font></td>\n";
				sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\"></td>\n";
				sHtml += "			</tr>\n";
				sHtml += "			<tr height=1>\n";
				sHtml += "				<td id=td_btm2_2_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td id=td_btm2_3_"+sName+" width=25 bgcolor=\""+m_sBtnFace+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td id=td_btm2_4_"+sName+" bgcolor=\""+m_sBtnFace+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td id=td_btm2_5_"+sName+" width=1 bgcolor=\""+m_sFaceDark+"\">\n";
				sHtml += "				</td>\n";
				sHtml += "			</tr>\n";
				sHtml += "		</table>\n";

				return sHtml;
			}
			sHtml = "";

			sHtml += "		<table width=\""+nWidth+"\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" onmouseup=\"javascript:JLTabCtrl_ontabmouseup('"+m_sName+"','"+sName+"'); "+sAction+"\" style=\"cursor: hand;\">\n";
			/*
			sHtml += "			<tr height=1>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=1 bgcolor=\"#cccccc\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=25 bgcolor=\"#cccccc\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td bgcolor=\"#cccccc\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=1 bgcolor=\"#cccccc\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "			</tr>\n";
			*/
			sHtml += "			<tr height=1>\n";
			/*
			sHtml += "				<td width=1 bgcolor=\"#cccccc\">\n";
			sHtml += "				</td>\n";
			*/
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=25 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\">\n";
			sHtml += "				</td>\n";
			/*
			sHtml += "				<td width=1 bgcolor=\"#cccccc\">\n";
			sHtml += "				</td>\n";
			*/
			sHtml += "			</tr>\n";
			sHtml += "			<tr height=20>\n";
			// sHtml += "				<td width=1 bgcolor=\"#cccccc\"></td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceBright+"\"></td>\n";
			sHtml += "				<td align=center valign=top width=25 bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height="+nImgTopMargin+" src=images/trans.gif width=10><br><img alt border=\"0\" src=\""+sIcon+"\"></td>\n";
			sHtml += "				<td align=left width="+(nWidth-25-2)+" bgcolor=\""+m_sBtnFace+"\"><img alt border=0 height=2 src=images/trans.gif width=10><br><font id=fnt_"+sName+" color=\"#aaaaaa\" style=\"FONT: 9pt 굴림\">"+sButtonText+"</font></td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\"></td>\n";
			// sHtml += "				<td width=1 bgcolor=\"#cccccc\"></td>\n";
			sHtml += "			</tr>\n";
			if (false)
			{
				sHtml += "			<tr height=1>\n";
				/*
				sHtml += "				<td id=td_btm1_1_"+sName+" width=1 bgcolor=\"#cccccc\">\n";
				sHtml += "				</td>\n";
				*/
				sHtml += "				<td id=td_btm1_2_"+sName+" width=1 bgcolor=\"#cccccc\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td id=td_btm1_3_"+sName+" width=25 bgcolor=\"#cccccc\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td id=td_btm1_4_"+sName+" bgcolor=\"#cccccc\">\n";
				sHtml += "				</td>\n";
				sHtml += "				<td id=td_btm1_5_"+sName+" width=1 bgcolor=\"#cccccc\">\n";
				sHtml += "				</td>\n";
				/*
				sHtml += "				<td id=td_btm1_6_"+sName+" width=1 bgcolor=\"#cccccc\">\n";
				sHtml += "				</td>\n";
				*/
				sHtml += "			</tr>\n";
			}
			sHtml += "			<tr height=1>\n";
			/*
			sHtml += "				<td id=td_btm2_1_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			*/
			sHtml += "				<td id=td_btm2_2_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td id=td_btm2_3_"+sName+" width=25 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td id=td_btm2_4_"+sName+" bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			sHtml += "				<td id=td_btm2_5_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			/*
			sHtml += "				<td id=td_btm2_6_"+sName+" width=1 bgcolor=\""+m_sFaceBright+"\">\n";
			sHtml += "				</td>\n";
			*/
			sHtml += "			</tr>\n";
			sHtml += "		</table>\n";

			return sHtml;
		}

		public String getButtonGapHtml(int nWidth)
		{
			String sHtml = "";
			sHtml += "		<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">\n\n";
			sHtml += "			<tr height=21>\n";
			sHtml += "				<td></td>\n";
			sHtml += "			</tr>\n";
			/*
			sHtml += "			<tr height=1>\n";
			sHtml += "				<td width=\""+nWidth+"\" bgcolor=\"#cccccc\"></td>\n";
			sHtml += "			</tr>\n";
			*/
			sHtml += "			<tr height=1>\n";
			sHtml += "				<td width=\""+nWidth+"\" bgcolor=\""+m_sFaceBright+"\"></td>\n";
			sHtml += "			</tr>\n";
			sHtml += "		</table>\n";
			return sHtml;
		}

		public String getTabBoxEndHtml()
		{
			String sHtml = "\n";
			sHtml += "				</td>\n";
			sHtml += "				<td width=1 bgcolor=\""+m_sFaceDark+"\"></td>\n";
			sHtml += "			</tr>\n";
			sHtml += "			<tr height=1>\n";
			// sHtml += "		    <td width=1 height=1 bgcolor=\"#cccccc\"></td>\n";
			sHtml += "				<td colspan=3 bgcolor=\""+m_sFaceDark+"\"></td>\n";
			sHtml += "			</tr>\n";
			sHtml += "		</table>\n";
			// print("</div>\n";
			return sHtml;
		}

		public String getTopRightHtml(int nWidth)
		{
			String sHtml = "\n";
			sHtml += "		<table border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">\n";
			sHtml += "			<tr height=21>\n";
			sHtml += "				<td width=\""+(nWidth-1)+"\"></td>\n";
			sHtml += "				<td width=\"1\"></td>\n";
			sHtml += "			</tr>\n";
			sHtml += "			<tr height=1>\n";
			sHtml += "				<td width=\""+(nWidth-1)+"\" bgcolor=\""+m_sFaceBright+"\"></td>\n";
			sHtml += "				<td width=\"1\" bgcolor=\""+m_sFaceDark+"\"></td>\n";
			sHtml += "			</tr>\n";
			sHtml += "		</table>\n";
			return sHtml;
		}
	}
%>

<!-- JLTabCtrl.java end -->

