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
	var m_obj = new Object();
	m_obj.m_oContainer = null;
	m_obj.m_wnd = window;

	function _getContainer()
	{
		var oWnd = window;
		while (true)
		{
			if (oWnd == null)
				break;
			if (oWnd == oWnd.parent)
				break;
			if (oWnd.m_obj != null)
			{
				if (oWnd.m_obj.m_oContainer != null)
					return oWnd;
			}
			oWnd = oWnd.parent;
		}
		return null;
	}
</script>

<%!
public class JLHttp extends JLRuntimeClass
{
	String m_sCodeHeader;
	public JLRcd m_rStyle;
	public JLHttp m_oParent;
	public Vector m_vChild;
	public javax.servlet.jsp.JspWriter m_out;
	public boolean m_bStream = false;
	public String m_sStream = "";
	public int m_nLeft;
	public int m_nTop;
	public int m_nWidth;
	public int m_nHeight;
	public int m_nRight;
	public int m_nBottom;
	public String m_sMainFrame = "";
	public String m_sImgPath = "";
	public JLRcd m_rRequest;
	
	public JLHttp(javax.servlet.jsp.JspWriter out)
	{
		m_out = out;
		m_sStream = "";
		m_bStream = false;
		m_vChild = new Vector();
		m_oParent = null;
		m_rStyle = new JLRcd();

		m_nLeft = 0;
		m_nTop = 0;
		m_nWidth = 0;
		m_nHeight = 0;
		m_nRight = 0;
		m_nBottom = 0;
		m_sImgPath = "";
		m_sMainFrame = "_mainFrame";
		m_sCodeHeader = "";
		m_rRequest = new JLRcd();

		JLJsp jsp = new JLJsp();
		setImgPath(jsp.getValue("classimgpath"));
	}

	void setCodeHeader(String sHeader)
	{
		m_sCodeHeader = sHeader;
	}

	public void setParam(JLRcd src)
	{
		if (src == null)
			return;
		int nCount = src.GetCount();
		for (int i=0;i<nCount;i++)
		{
			String sProp = (String)src.GetProp(i);
			if (JLString.IsEmpty(sProp))
				continue;
			String sVal = (String)src.GetAt(i);
			m_rRequest.SetValue(sProp,sVal);
		}
	}
	
	public void setImgPath(String sPath)
	{
		m_sImgPath = sPath;
	}
	
	public void setMainFrame(String sName)
	{
		m_sMainFrame = sName;
	}
	
	public String getMainFrameName()
	{
		return m_sMainFrame;
	}

	public void setParent(JLHttp oParent)
	{
		if (oParent == null)
			return;
		m_oParent = oParent;
		m_oParent.addChild(this);
	}

	// nLeft : x position, -1 : attach left
	// nTop : y position , -1 : attach top
	// nBottom : y position, -1 : attach bottom;
	// nRight : x position, -1 : attach right
	// nHeight , -1 : sum of the height of bars
	// nWidth, -1 : sum of the width of bars
	public void setPosition(int nLeft,int nTop,int nWidth,int nHeight,int nRight,int nBottom)
	{
		setLeft(nLeft);
		setTop(nTop);
		setWidth(nWidth);
		setHeight(nHeight);
		setRight(nRight);
		setBottom(nBottom);
	}

	public void setLeft(int nArg)
	{
		m_nLeft = nArg;
	}

	public void setTop(int nArg)
	{
		m_nTop = nArg;
	}

	public void setWidth(int nArg)
	{
		m_nWidth = nArg;
	}

	public void setHeight(int nArg)
	{
		m_nHeight = nArg;
	}

	public void setRight(int nArg)
	{
		m_nRight = nArg;
	}

	public void setBottom(int nArg)
	{
		m_nBottom = nArg;
	}
	
	public int getLeft()
	{
		return m_nLeft;
	}

	public int getTop()
	{
		return m_nTop;
	}

	public int getWidth()
	{
		return m_nWidth;
	}

	public int getHeight()
	{
		return m_nHeight;
	}

	public int getRight()
	{
		return m_nRight;
	}

	public int getBottom()
	{
		return m_nBottom;
	}
	
	public void setStream(boolean bSet)
	{
		m_bStream = bSet;
		emptyStream();
	}

	public String convert(String str)
	{
		String strRet = null;
		try
		{
			if (str == null)
				strRet = null;
			else
			{
				strRet = new String(str.getBytes("8859_1"),"KSC5601");
				// strRet = str;
			}
		}
		catch (Exception e)
		{
		}
		return strRet;
	}

	public void convert(String sItem[])
	{
		if (sItem == null)
			return;
		int nCnt = sItem.length;
		for (int i = 0; i < nCnt; i++)
		{
			sItem[i] = convert(sItem[i]);
		}
	}
	
	public void debug(String str)
	{
		if (false)
		{
			if (str == null)
				return;
			try
			{
				m_out.println("<font style=\"FONT: 9pt 굴림\"><br>");
				m_out.println(str);
				m_out.println("</font>");
			}
			catch (IOException ioe)
			{
			}
		}
	}

	public void debug(JLRcd rcd)
	{
		if (rcd == null)
			return;
	
		String sAr;
		sAr = rcd.SerializeOut();
		debug(sAr);
	}

	public void debugPrintStream(String sArray[])
	{
		if (sArray == null)
			return;
		int nCh = sArray.length;
		for (int i = 0; i < nCh; i++)
		{
			debugPrintStream(sArray[i]);
		}
	}

	public void debugAlert(String str)
	{
		String sMsg = new String();
		if (str == null)
			return;
		sMsg += "<font style=\"FONT: 9pt 굴림\"><br>";
		sMsg += str;
		sMsg += "</font>";
		this.printStream("<script>"+sMsg+"</script>");
	}

	public void debugPrintStream(String str)
	{
		if (m_out == null)
			return;
		// if (false)
		{
			if (str == null)
				return;
			if (m_bStream)
			{
				m_sStream += str;
			}
			else
			{
				try
				{
					m_out.println("<font style=\"FONT: 9pt 굴림\"><br>");
					m_out.println(str);
					m_out.println("</font>");
				}
				catch (IOException ioe)
				{
				}
			}
		}
	}

	public void debugPrintStream(JLRcd rcd)
	{
		if (rcd == null)
			return;
	
		String sAr;
		sAr = rcd.SerializeOut();
		debugPrintStream(sAr);
	}

	public void debugPrintStream(Vector vec)
	{
		if (vec == null)
			return;
		int nCount = vec.size();
		if (vec.size() <= 0)
			return;
		int nTot = 0;
		for (int i=0;i<nCount;i++)
		{
			JLRcd rcd = (JLRcd)vec.elementAt(i);
			if (rcd == null)
				continue;
			debugPrintStream(rcd);
		}
	}
	
	public void emptyStream()
	{
		m_sStream = "";
	}
	
	public void flushStream()
	{
		if (m_out == null)
			return;
		try
		{
			m_out.println(m_sStream);
		}
		catch (java.io.IOException ioe)
		{
			return;
		}
		emptyStream();
	}
	
	public void printStream(String str)
	{
		if (m_out == null)
			return;
		if (str == null)
			return;
		if (m_bStream)
		{
			m_sStream += str;
		}
		else
		{
			try
			{
				m_out.println(str);
			}
			catch (java.io.IOException ioe)
			{
				return;
			}
		}
	}

	public void setValue(JLRcd rcd,String sProp,int nValue)
	{
		if (rcd == null)
			return;
		if (JLString.IsEmpty(sProp))
			return;
		rcd.SetValue(sProp,nValue);
	}

	public void setValue(JLRcd rcd,String sProp,String sValue)
	{
		if (rcd == null)
			return;
		if (JLString.IsEmpty(sProp) || JLString.IsEmpty(sValue))
			return;
		rcd.SetValue(sProp,sValue);
	}

	public void copyRcd(JLRcd dest,JLRcd src)
	{
		if (dest == null || src == null)
			return;
		int nCount = src.GetCount();
		for (int i=0;i<nCount;i++)
		{
			String sProp = (String)src.GetProp(i);
			if (JLString.IsEmpty(sProp))
				continue;
			String sVal = dest.GetStrValue(sProp);
			if (!JLString.IsEmpty(sVal))
				continue;
			sVal = (String)src.GetAt(i);
			dest.SetValue(sProp,sVal);
		}
	}

	public void setRcd(JLRcd dest,JLRcd src)
	{
		if (dest == null || src == null)
			return;
		int nCount = src.GetCount();
		for (int i=0;i<nCount;i++)
		{
			String sProp = (String)src.GetProp(i);
			if (JLString.IsEmpty(sProp))
				continue;
			String sVal = (String)src.GetAt(i);
			dest.SetValue(sProp,sVal);
		}
	}

	public String getResStr(String sSection,String sName)
	{
		/*
		if (JLString.IsEmpty(sSection) || JLString.IsEmpty(sName))
			return "";

		JLConfig cfg = JLDBConnectionManager.getLang();
		if (cfg != null)
		{
			String sRes = cfg.getStrValue(sSection,sName);
			if (JLString.IsEmpty(sRes))
				return "";
			try
			{
				JLDBConnectionManager conn = JLDBConnectionManager.getInstance();
				if (conn == null)
					return "";
				String sCode = conn.getEnv("langcode");
				if (JLString.IsEmpty(sCode))
					return "";
				sRes = new String(sRes.getBytes("8859_1"),sCode);
				return sRes;
				// return "";
			}
			catch (Exception e)
			{
			}
		}
		*/
		return "";
	}

	// cEsc : $,  $B1$E8 -> 김
	// %20 -> ' ', $20 -> ' '
	String removeEsc(String sArg,char cEsc)
	{
		if (JLString.IsEmpty(sArg))
			return "";
		JLString str = new JLString();
		String sRet = new String();
		char c;
		byte doublebyte[] = new byte[2];
		int nDoubleByte = 0;
		int nLen = sArg.length();
		for (int i=0;i<nLen;i++)
		{
			c = sArg.charAt(i);
			if (c == cEsc)	// '%')
			{
				int nVal = 0;
				i++;
				if (i >= nLen)
					break;
				c = sArg.charAt(i);
				nVal += str.parseHexa(c);
				nVal *= 16;
				i++;
				if (i >= nLen)
					break;
				c = sArg.charAt(i);
				nVal += str.parseHexa(c);

				if (nDoubleByte > 0)
				{
					/*
					nVal = nDoubleByte*256+nVal;
					debugPrintStream(""+nVal);
					c = (char)nVal;
					sRet += c;
					*/
					doublebyte[1] = (byte)nVal;
					String doubleString = new String(doublebyte);
					sRet += doubleString;
					nDoubleByte = 0;
					continue;
				}

				if (nVal >= 128)	// hanguel code : double byte code
				{
					nDoubleByte = nVal;
					doublebyte[0] = (byte)nVal;
					// sRet += (byte)nVal;
					continue;
				}
				c = (char)nVal;
				sRet += c;
				continue;
			}
			sRet += c;
		}
		return sRet;
	}

	String getHexa(int nVal)
	{
		if (nVal >= 0 && nVal <= 9)
		{
			String sHexa;
			sHexa = ""+nVal;
			return sHexa;
		}
		if (nVal == 10)
			return "A";
		if (nVal == 11)
			return "B";
		if (nVal == 12)
			return "C";
		if (nVal == 13)
			return "D";
		if (nVal == 14)
			return "E";
		if (nVal == 15)
			return "F";
		return "0";
	}

	String getHexa(byte c)
	{
		int nVal = c;
		if (c < 0)
		{
			nVal = 128+c+128;
		}
		int nResult = nVal / 16;

		// debugPrintStream("" + nResult);

		String sHexa = "";
		sHexa += getHexa(nResult);
		int nRemain = nVal % 16;

		// debugPrintStream("" + nRemain);

		sHexa += getHexa(nRemain);
		return sHexa;
	}

	String getHexaA(int c)
	{
		int nVal = c;
		if (c < 0)
		{
			nVal = 128+c+128;
		}
		int nResult = nVal / 16;

		// debugPrintStream("" + nResult);

		String sHexa = "";
		sHexa += getHexa(nResult);
		int nRemain = nVal % 16;

		// debugPrintStream("" + nRemain);

		sHexa += getHexa(nRemain);
		return sHexa;
	}

	String insertUrlEsc(String sArg,char cEsc)
	{
		if (sArg == null)
			return "";
		String sRet = new String();
		byte byteSrc[] = sArg.getBytes();

		int nCount = byteSrc.length;
		for (int i=0;i<nCount;i++)
		{
			byte c = byteSrc[i];

			int aa = c;
			// debugPrintStream("" + aa);

			// debugPrintStream("" + aa.longValue());
			// if (c < 'A')
			if (c == '\n' || c == '\r' || c == '\'' || c == '\"' || c == '\\' || c == ';' || c == '&' || c == '?' || c == '%')
			{
				sRet += cEsc + getHexa(c);
				continue;
			}
			if (cEsc == c)
			{
				sRet += cEsc + getHexa(c);
				continue;
			}

			// 한글 -> %C7%D1%B1%DB
			// double byte code : like hanguel code
			if (aa >= 128 || aa < 0)
			{
				sRet += cEsc + getHexa(c);
				continue;
			}
			char cc = (char)aa;
			sRet += cc;
		}
		return sRet;
	}

	// string안에 single quotation이 있으면 ''로 변환하는 기능
	// database sql에 서 필요한 기능이다.
	public String insertDQuoteEsc(String sSrc)
	{
		if (sSrc == null)
			return "";
		if (sSrc.length() <= 0)
			return "";
		String sTok = new String();
		int nLen = sSrc.length();
		for (int i=0;i<nLen;i++)
		{
			// " -> \"
			char b = sSrc.charAt(i);
			if (b == '\"')
				sTok += "\\";
			sTok += b;
		}
		String sEsc = insertLineEsc(sTok);
		return sEsc;
	}

	// CR -> "\n"
	public String insertLineEsc(String sSrc)
	{
		String sDest = replaceString(sSrc,'\r',"");
		sDest = replaceString(sDest,'\n',"\\n");
		return sDest;
	}

	// CR -> "\n"
	public String replaceString(String sSrc,char c,String sRep)
	{
		if (sSrc == null)
			return null;
		if (sSrc.length() <= 0)
			return null;
		String sRet = new String();
		int nLen = sSrc.length();
		for (int i=0;i<nLen;i++)
		{
			char b = sSrc.charAt(i);
			if (b == c)
			{
				sRet += sRep;
				continue;
			}
			sRet += b;
		}
		return sRet;
	}

	// string안에 single quotation이 있으면 ''로 변환하는 기능
	// database sql에 서 필요한 기능이다.
	public String insertDbQuoteEsc(String sSrc)
	{
		if (sSrc == null)
			return null;
		if (sSrc.length() <= 0)
			return null;
		String sTok = new String();
		int nLen = sSrc.length();
		for (int i=0;i<nLen;i++)
		{
			// " -> \"
			// ' -> ''
			char b = sSrc.charAt(i);
			if (b == '\'')
				sTok += "'";
			if (b == '\"')
				sTok += "\\";
			sTok += b;
		}
		return sTok;
	}

	// 한글 코드 (double byte는 처리하지 않는다.)
	String insertUrlEscB(String sArg,char cEsc)
	{
		if (sArg == null)
			return "";
		String sRet = new String();

		int nCount = sArg.length();
		for (int i=0;i<nCount;i++)
		{
			char c = sArg.charAt(i);
			int nChar = c;
			// debugPrintStream(""+nChar);
			if (c == '\n' || c == '\r' || c == '\'' || c == '\"' || c == '\\' || c == ';' || c == '&' || c == '?')
			{
				sRet += cEsc + getHexaA(nChar);
				continue;
			}
			if (cEsc == c)
			{
				sRet += cEsc + getHexaA(nChar);
				continue;
			}
			sRet += c;
		}
		return sRet;
	}

	public String insertEscA(String sSrc)
	{
		if (sSrc == null)
			return "";
		if (sSrc.length() <= 0)
			return "";
		String sTok = new String();
		int nLen = sSrc.length();
		for (int i=0;i<nLen;i++)
		{
			char b = sSrc.charAt(i);
			if (b == '>')
			{
				sTok += "&#62;";
				continue;
			}
			if (b == '<')
			{
				sTok += "&#60;";
				continue;
			}
			if (b == '\"')
			{
				sTok += "&#34;";
				continue;
			}
			if (b == '\'')
			{
				sTok += "&#39;";
				continue;
			}
			sTok += b;
		}
		return sTok;
	}


	/*
	String strSql = "insert into tcamp (id,text,tpltid,email,ctime,trash,guid,logtype,logsch1,logsch2,logsch3,logcnt) ";
	strSql += "values (?,'$cmname',$tpltid,'$email',sysdate,0,'" + strGUID + "',";
	strSql += "$logtype,$logsch1,$logsch2,$logsch3,$logcnt)";
	JLStmt sql = new JLStmt();
	strSql = sql.MakeQuery(strSql,m_param);
	*/
	public String printFormat(String sSql,JLRcd rcdParam)
	{
		if (sSql == null)
			return null;
	
		String sTail = "";
		String sHead = "";
		String sMid = "";

		// $$$_ui$$$의 처리를 위해서
		String sRet = new String();
		String sSrc = sSql;
		do
		{
			int nIndex = sSrc.indexOf("$");
			int nSqlLen = sSrc.length();
			if (nIndex < 0)
				break;
			sMid = sSrc.substring(nIndex+1,nSqlLen);
			String sTok = getTok(sMid);
			int nTok = sTok.length();
			sTail = sSrc.substring(nIndex + nTok+1,nSqlLen);
			sHead = sSrc.substring(0,nIndex);
			// sSrc = sHead;
			String sVal = rcdParam.GetStrValue(sTok);
			if (sVal == null)
			{
				sVal = "0";
				if (nIndex > 0)
				{
					char c = sSrc.charAt(nIndex-1);
					if (c == '\'' || c == '\"')
						sVal = "";
				}
			}
			else
			{
				if (sVal.length() <= 0)
				{
					sVal = "0";
					if (nIndex > 0)
					{
						char c = sSrc.charAt(nIndex-1);
						if (c == '\'' || c == '\"')
							sVal = "";
					}
				}
			}
			// sVal = JLString.NullCheck(JLString.insertQuoteEsc(sVal));
			sRet += sHead;
			sRet += sVal;
			
			sSrc = sTail;
		} while (true);
		sRet += sTail;
		return sRet;
	}

	public String getTok(String sAr)
	{
		if (sAr == null)
			return null;

		String sTok = new String();
		int nIdx = 0;
		// identifier;
		int nLen = sAr.length();
		for (int i=0;i<nLen;i++)
		{
			char c = sAr.charAt(nIdx);
			if (c == (char)0)
				break;
			nIdx ++;
			if (!((c >= '0' && c <= '9') || (c >= 'A' && c <= 'z')))
			    break;
			sTok += c;
		}
		return sTok;
	}
	
	public void printObj(int nLeft,int nTop)
	{
		printChildHtml(0,0);
	}

	public void declVar(String sName,String sProp,int nVal)
	{
		print("var "+sName+"_"+sProp+" = "+nVal+"; function _set_"+sName+"_"+sProp+"(nVal) { "+sName+"_"+sProp+" = nVal; } function _get_"+sName+"_"+sProp+"() { return "+sName+"_"+sProp+"; }");
	}

	public void declVar(String sName,String sProp,String sVal)
	{
		if (sProp == null || sName == null)
			return;
		if (sVal != null)
			print("var "+sName+"_"+sProp+" = \""+sVal+"\"; function _set_"+sName+"_"+sProp+"(sVal) { "+sName+"_"+sProp+" = sVal; } function _get_"+sName+"_"+sProp+"() { return "+sName+"_"+sProp+"; }");
		else
			print("var "+sName+"_"+sProp+" = null; function _set_"+sName+"_"+sProp+"(sVal) { "+sName+"_"+sProp+" = sVal; } function _get_"+sName+"_"+sProp+"() { return "+sName+"_"+sProp+"; }");
	}

	public void declArrayVar(String sName,String sProp,int nCount)
	{
		print(sName+"_"+sProp+" = new Array("+nCount+"); function _set_"+sName+"_"+sProp+"(sVal,idx) { "+sName+"_"+sProp+"[idx] = sVal; } function _get_"+sName+"_"+sProp+"(idx) { return "+sName+"_"+sProp+"[idx]; }");
	}

	public void declArrayValue(String sName,String sProp,int nIdx,String sVal)
	{
		print(sName+"_"+sProp+"["+nIdx+"] = \""+sVal+"\";");
	}

	public void setStyle(String sProp,String sVal)
	{
		if (m_rStyle == null)
			return;
		m_rStyle.SetValue(sProp,sVal);
	}

	public String getStyle(String sProp)
	{
		if (m_rStyle == null)
			return null;
		return m_rStyle.GetStrValue(sProp);
	}

	public void printChildScript()
	{
		int nCount = m_vChild.size();
		print("<script language=\"javascript\">");

		int nStyle = m_rStyle.GetCount();
		for (int i=0;i<nStyle;i++)
		{
			String sProp = m_rStyle.GetProp(i);
			int nVal = m_rStyle.GetIntValue(i);
			if (!JLString.IsEmpty(sProp))
				declVar(m_sName,sProp,nVal);
		}
		declVar(m_sName,"draw",0);
		declVar(m_sName,"class",m_sClassName);
		declVar(m_sName,"left",m_nLeft);
		declVar(m_sName,"top",m_nTop);
		declVar(m_sName,"width",m_nWidth);
		declVar(m_sName,"height",m_nHeight);
		declVar(m_sName,"childcount",nCount);
		declArrayVar(m_sName,"child",nCount);
		String sPopup = "";
		// if (false)
		for (int i=0;i<nCount;i++)
		{
			JLHttp oObj = (JLHttp)m_vChild.elementAt(i);
			if (oObj == null)
				continue;
			String sChild = oObj.getName();
			if (JLString.IsEmpty(sChild))
				sChild = "";
			declArrayValue(m_sName,"child",i,sChild);
		}
		print("</script>");			
	}

	public void printChildHtml(int nLeft,int nTop)
	{
		printChildScript();

		int nCount = m_vChild.size();
		for (int i=0;i<nCount;i++)
		{
			JLHttp oChild = (JLHttp)m_vChild.elementAt(i);
			if (oChild == null)
				continue;
			int nPosTop = oChild.getTop();
			int nPosLeft = oChild.getLeft();
			int nPosWidth = oChild.getWidth();
			int nPosHeight = oChild.getHeight();

			int nDivTop = nTop;
			int nDivLeft = nLeft;
			if (nPosTop > 0)
				nDivTop = nPosTop;
			if (nPosLeft > 0)
				nDivLeft = nPosLeft;
				
			String sChild = oChild.getName();
			JLHttp oCtrl = (JLHttp)m_vChild.elementAt(i);
			oCtrl.printObj(nDivLeft,nDivTop);

			nTop += 2;
			if (nPosHeight > 0)
				nTop += nPosHeight;
		}
	}

	public void addChild(JLHttp oArg)
	{
		if (oArg == null)
			return;

		int nCount = m_vChild.size();
		for (int i=0;i<nCount;i++)
		{
			JLHttp oChild = (JLHttp)m_vChild.elementAt(i);
			if (oChild == oArg)
				return;
		}
		m_vChild.addElement(oArg);
	}
} // JLHttp
%>
