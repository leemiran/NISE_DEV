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

<%@ page import="m2soft.base.*,m2soft.rdsystem.server.core.install.Message" %>

<%!
	// @ include file="JLJsp.jsp"

	HttpSession m_sessn;
	JLEnv m_env;
	JLRcd m_param;
	javax.servlet.jsp.JspWriter m_out;
	HttpServletRequest m_req;
	int m_nWspid;
	String m_sGuidEx = "";
	String m_sNewGuid = "";
	// String m_sBtnFace = "#dfdfdf";
	// String m_sBtnFace = "#8686c1";
	// String m_sBtnFace = "#AFAFDB";
	String m_sBtnFace = "";
   String m_sCharSet = Message.get("charset");


	// stream 기능
	// stream 이 true이면,
	// print() 함수에서 프린트를 하는 방식이 아니라, print에서는 m_sStream에 append한다.
	boolean m_bStream = false;
	String m_sStream = "";

	void setStream(boolean bSet)
	{
		m_bStream = bSet;
		emptyStream();
	}

	String getNewGuidEx()
	{
		if (!JLString.IsEmpty(m_sNewGuid))
			return m_sNewGuid;
		m_sNewGuid = JLSystem.GetGUID();
		return m_sNewGuid;
	}

	void initBtnFace()
	{
		/*
		String sBtnFace = getEnv("btnface");
		if (JLString.IsEmpty(sBtnFace))
			sBtnFace = "#dfdfdf";
		m_sBtnFace = sBtnFace;
		*/
		m_sBtnFace = "#d4d0c8";
	}

	void init(HttpServletRequest request,javax.servlet.jsp.JspWriter out)
	{
		m_sNewGuid = "";
		m_out = out;
		m_req = request;
		m_sessn = m_req.getSession(false);
		if (m_sessn == null)
			m_sessn = m_req.getSession(true);

		JLEnv env = (JLEnv)m_sessn.getValue("env");
		if (env != null)
			m_env = env;
		else
		{
			m_env = new JLEnv();
			// m_env.init();
		}
		m_param = new JLRcd();

		initBtnFace();
	}

	void initArgSimple(HttpServletRequest request,javax.servlet.jsp.JspWriter out)
	{
		m_out = out;
		m_req = request;
		m_param = new JLRcd();
		JLJsp jsp = new JLJsp();
		String sCharSet = jsp.getValue("charset");
		Enumeration enu = request.getParameterNames();
      if (enu == null)
         return;

		for (;enu.hasMoreElements();)
		{
			String sProp = (String)enu.nextElement();
			String strVal = request.getParameter(sProp);
			if (!JLString.IsEmpty(sCharSet))
				strVal = JLString.NullCheck(convert(strVal));
			m_param.SetValue(sProp,strVal);
			debug(sProp + ":" + strVal);
		}

		initBtnFace();
	}

	void initArg(HttpServletRequest request,javax.servlet.jsp.JspWriter out)
	{
		init(request,out);
		JLJsp jsp = new JLJsp();
		String sCharSet = jsp.getValue("charset");
		Enumeration enu = request.getParameterNames();
      if (enu == null)
         return;

		for (;enu.hasMoreElements();)
		{
			String sProp = (String)enu.nextElement();
			String strVal = request.getParameter(sProp);
			if (!JLString.IsEmpty(sCharSet))
				strVal = JLString.NullCheck(convert(strVal));
			m_param.SetValue(sProp,strVal);
			//debug(sProp + ":" + strVal);
		}
	}

	String getEnv(String sProp)
	{
		if (m_env == null)
			return "";
		String sVal = m_env.GetEnv(sProp);
		return sVal;
	}

	boolean isEmpty(Vector vec)
	{
		if (vec == null)
			return true;
		int nCount = vec.size();
		if (vec.size() <= 0)
			return true;
		return false;
	}

	String convert(String str)
	{
		String strRet = null;
		try
		{
			if (str == null)
				strRet = null;
			else
			{
				strRet = new String(str.getBytes("8859_1"),"KSC5601");
			}
		}
		catch (Exception e)
		{
		}
		return strRet;
	}

	void convert(String sItem[])
	{
		if (sItem == null)
			return;
		int nCnt = sItem.length;
		for (int i = 0; i < nCnt; i++)
		{
			sItem[i] = convert(sItem[i]);
		}
	}

	String GetParam(String sProp)
	{
		if (sProp == null)
			return "";
		String sRet = JLString.NullCheck(m_param.GetStrValue(sProp));
		return sRet;
	}

	void debug(String str)
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

	void debug(JLRcd rcd)
	{
		if (rcd == null)
			return;

		String sAr;
		sAr = rcd.SerializeOut();
		debug(sAr);
	}

	void debugPrint(String sArray[])
	{
		if (sArray == null)
			return;
		int nCh = sArray.length;
		for (int i = 0; i < nCh; i++)
		{
			debugPrint(sArray[i]);
		}
	}

	void debugAlert(String str)
	{
		String sMsg = new String();
		if (str == null)
			return;
		sMsg += "<font style=\"FONT: 9pt 굴림\"><br>";
		sMsg += str;
		sMsg += "</font>";
		print("<script>"+sMsg+"</script>");
	}

	void debugPrint(String str)
	{
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

	void debugPrint(JLRcd rcd)
	{
		if (rcd == null)
			return;

		String sAr;
		sAr = rcd.SerializeOut();
		debugPrint(sAr);
	}

	void debugPrint(Vector vec)
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
			debugPrint(rcd);
		}
	}

	void debugPrintVV(Vector vec)
	{
		if (vec == null)
			return;
		int nCount = vec.size();
		if (vec.size() <= 0)
			return;
		int nTot = 0;
		for (int i=0;i<nCount;i++)
		{
			Vector rcd = (Vector)vec.elementAt(i);
			if (rcd == null)
				continue;
			debugPrintV(rcd);
		}
	}

	void debugPrintV(Vector vec)
	{
		if (vec == null)
			return;
		int nCount = vec.size();
		if (vec.size() <= 0)
			return;
		int nTot = 0;
		for (int i=0;i<nCount;i++)
		{
			String sData = (String)vec.elementAt(i);
			debugPrint(sData);
		}
	}

	void emptyStream()
	{
		m_sStream = "";
	}

	void flushStream()
	{
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

	void print(String str)
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
				m_out.println(str);
			}
			catch (java.io.IOException ioe)
			{
				return;
			}
		}
	}

	// sitename_
	String getPrefix()
	{
		String sPrefix = m_param.GetStrValue("prefix");
		if (!JLString.IsEmpty(sPrefix))
			return sPrefix+"_";
		sPrefix = getCfgValue("default","prefix");
		if (!JLString.IsEmpty(sPrefix))
			return sPrefix+"_";
		return "";
	}

	// _sitename
	String getPostfix()
	{
		String sPrefix = m_param.GetStrValue("prefix");
		if (!JLString.IsEmpty(sPrefix))
			return "_"+sPrefix;
		sPrefix = getCfgValue("default","prefix");
		if (!JLString.IsEmpty(sPrefix))
			return "_"+sPrefix;
		return "";
	}

	String getUserName()
	{
		if (m_sessn == null)
			return "";
		JLRcd rcd = (JLRcd)m_sessn.getValue("user");
		if (rcd == null)
			return "";
		String sName = rcd.GetStrValue("_un");		// user name
		return JLString.NullCheck(sName);
	}

	String getUserID()
	{
		if (m_sessn == null)
			return "";
		JLRcd rcd = (JLRcd)m_sessn.getValue("user");
		if (rcd == null)
			return "";
		String sName = rcd.GetStrValue("_ui");
		if (!JLString.IsEmpty(sName))
			return sName;
		sName = rcd.GetStrValue("text");		// user id
		return JLString.NullCheck(sName);
	}

	String getUserEmail()
	{
		if (m_sessn == null)
			return "";
		JLRcd rcd = (JLRcd)m_sessn.getValue("user");
		if (rcd == null)
			return "";
		String sName = rcd.GetStrValue("_em");		// user email
		return JLString.NullCheck(sName);
	}

	int getWspID()
	{
		JLRcd wsp = (JLRcd)m_sessn.getValue("wsp");
		if (wsp == null)
			return 0;
		int nID = wsp.GetIntValue("id");
		return nID;
	}

	// // workspace id : database에는 wspid로 저장되어 있다.
	// 여기에는 LOGIN아이디가 들어가 있다. EX: hankmoon
	//
	String getWspUser(int nWid)
	{
		return "";
	}

	String getWsp()
	{
		JLRcd sessn = (JLRcd)m_sessn.getValue("user");
		String sWsp = getStrValue(sessn,"_ui");
		return sWsp;
	}

	String getWspUser(String sWid)
	{
		/*
		String sSql = "select * from twsp where id="+sWid;
		JLStmt stmt = new JLStmt();
		Vector vec = stmt.SelectQueryN(sSql,null,"cm",1);
		if (vec == null)
			return null;
		if (vec.size() <= 0)
			return null;
		JLRcd rcd = (JLRcd)vec.elementAt(0);
		String sUid = rcd.GetStrValue("uid");
		return sUid;
		*/
		return "";
	}

	int validateLoginSub()
	{
		if (m_sessn == null)
			return -1;
		JLRcd rcd = (JLRcd)m_sessn.getValue("user");
		if (rcd != null)
		{
			int nCount = rcd.GetCount();
			if (nCount > 0)
				return 0;
		}
		return -1;
	}

	int validateLogin()
	{
		JLRcd rcd = (JLRcd)m_sessn.getValue("user");
		if (rcd != null)
		{
			int nCount = rcd.GetCount();
			if (nCount > 0)
				return 0;
		}

		try
		{
			m_out.println("<table bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"460\">");
			m_out.println("<tbody> ");
			m_out.println("<tr> ");
			m_out.println("<td align=center>");
			m_out.println("<font color=red style=\"FONT: 9pt 굴림\"><br>");
			m_out.println("<a href=\"javascript:onlogin()\">시간이 경과하여 다시 로그인을 해야합니다. 다시 로그인하려면 여기를 클릭하세요.</a>");
			m_out.println("</font>");
			m_out.println("</td>");
			m_out.println("</tr>");
			m_out.println("</tbody>");
			m_out.println("</table>");
		}
		catch (java.io.IOException ioe)
		{
			return -1;
		}
		return -1;
	}

	String getPrivName()
	{
		int nPriv = getPriv();
		if (nPriv <= 1)
			return "관리자";
		if (nPriv <= 2)
			return "일반";
		if (nPriv <= 100)
			return "검색";
		return "";
	}

	int getPriv()
	{
		String sPriv = getUserValue("priv");
		int nPriv = JLString.parseInt(sPriv);
		return nPriv;
	}

	int validatePermission(int nWspid)
	{
		if (getWspID() == nWspid || getPriv() <= 1)
			return 0;
		printPermissionMsg();
		return -1;
	}

	// 0 : super
	// 1 : super
	// 2 : 일반
	// .. : 일반 or 검색
	// nRequired PermLevel : 1인경우 관리자만이 이기능을 사용할 수 있는데.. 지금 로그인한 사용자가 그 레벨 이상인지를
	// 검사합낟.
	boolean hasPerm(int nRequiedPermLevel)
	{
		int nPriv = getPriv();
		// required perm level보다 숫자적으로 낮아야,, 높은 perm level이다.
		if (nPriv <= nRequiedPermLevel)
			return true;
		return false;
	}

	void printPermViolationMsg()
	{
		print("<script>onalert('"+getUserName()+"님은 "+getPrivName()+"권한[Level : "+getPriv()+"]입니다. <br>권한이 없습니다. 관리자에게 문의하세요')</script>");
	}


	// priviledge checking
	int validatePriv(int nPriv)
	{
		if (getPriv() <= 1)	// superuser
			return 0;
		if (getPriv() <= nPriv)
			return 0;
		printPermissionMsg();
		return -1;
	}

	int printPermissionMsg()
	{
		try
		{
			m_out.println("<table bgcolor=\"#ffffff\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"460\">");
			m_out.println("<tbody> ");
			m_out.println("<tr> ");
			m_out.println("<td align=center>");
			m_out.println("<font color=red style=\"FONT: 9pt 굴림\"><br>");
			m_out.println("권한이 없어 사용할 수 없습니다.");
			m_out.println("</font>");
			m_out.println("</td>");
			m_out.println("</tr>");
			m_out.println("</tbody>");
			m_out.println("</table>");
			return -1;
		}
		catch (java.io.IOException ioe)
		{
		}
		return -1;
	}

	String getWspCond()
	{
		int nPriv = getPriv();
		if (nPriv <= 1)	// super user
			return null;
		int nWsp = getWspID();
		if (nWsp == 0)
			return "";
		String sCond = " wspid="+getWspID();
		return sCond;
	}

	String getUserValue(String sProp)
	{
		if (JLString.IsEmpty(sProp))
			return "";
		JLRcd rcd = (JLRcd)m_sessn.getValue("user");
		if (rcd == null)
			return "";
		String sVal = rcd.GetStrValue(sProp);
		if (!JLString.IsEmpty(sVal))
			return sVal;
		String sEmpty = new String();
		if (sProp == null)
			return sEmpty;
		sVal = m_env.GetEnv(sProp);
		if (sVal == null)
			return sEmpty;
		return sVal;
	}

	public String getWebServerEx()
	{
		String sWeb = getEnv("webserver");
		String sPort = getEnv("webserverport");
		int nPort = JLString.parseInt(sPort);
		if (nPort <= 0)
			return sWeb;
		sWeb += ":"+nPort;
		return sWeb;
	}

	public String getLogServerEx()
	{
		String sWeb = getEnv("logsvr");
		if (JLString.IsEmpty(sWeb))
			sWeb = getEnv("webserver");
		String sPort = getEnv("logsvrport");
		if (JLString.IsEmpty(sPort))
			sPort = getEnv("webserverport");
		int nPort = JLString.parseInt(sPort);
		if (nPort <= 0)
			return sWeb;
		sWeb += ":"+nPort;
		return sWeb;
	}

	public void initEnv()
	{
		JLEnv env = new JLEnv();
		env.init();
		m_sessn.putValue("env",env);
	}

	public String getCfgValue(String sSection,String sProp)
	{
		if (JLString.IsEmpty(sSection) || JLString.IsEmpty(sProp))
			return null;

		JLConfig cfg = JLConfig.getInstance();
		if (cfg != null)
		{
			JLRcd rcd = cfg.getSection(sSection);
			if (rcd != null)
			{
				String sVal = rcd.GetStrValue(sProp);
				if (!JLString.IsEmpty(sVal))
					return sVal;
			}
		}

		JLDBConnectionManager connMgr = JLDBConnectionManager.getInstance();
		if (connMgr != null)
		{
			String sRet = connMgr.getEnv(sProp);
			if (!JLString.IsEmpty(sRet))
				return sRet;
		}
		return null;
	}

	Vector copy(Vector src)
	{
		if (src == null)
			return null;
		Vector dest = new Vector();
		int nCnt = src.size();
		for (int i=0;i<nCnt;i++)
		{
			JLRcd rcd = (JLRcd)src.elementAt(i);
			if (rcd == null)
				continue;
			dest.addElement(rcd);
		}
		return dest;
	}

	String getSenderName()
	{
		String sName = m_param.GetStrValue("salias");
		if (!JLString.IsEmpty(sName))
			return sName;
		sName = getUserName();
		if (!JLString.IsEmpty(sName))
			return sName;
		sName = getEnv("salias");
		return sName;
	}

	String getSenderEmail()
	{
		String sName = m_param.GetStrValue("semail");
		if (!JLString.IsEmpty(sName))
			return sName;
		sName = getUserEmail();
		if (!JLString.IsEmpty(sName))
			return sName;
		sName = getEnv("semail");
		return sName;
	}

	String getStrValue(JLRcd rcd,String sProp)
	{
		if (rcd == null)
			return "";
		String sValue = JLString.NullCheck(rcd.GetStrValue(sProp));
		return sValue;
	}

	int getIntValue(JLRcd rcd,String sProp)
	{
		if (rcd == null)
			return 0;
		int nValue = rcd.GetIntValue(sProp);
		return nValue;
	}

	void setValue(JLRcd rcd,String sProp,int nValue)
	{
		if (rcd == null)
			return;
		if (JLString.IsEmpty(sProp))
			return;
		rcd.SetValue(sProp,nValue);
	}

	void setValue(JLRcd rcd,String sProp,String sValue)
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

%>

<%!
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
					debugPrint(""+nVal);
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

		// debugPrint("" + nResult);

		String sHexa = "";
		sHexa += getHexa(nResult);
		int nRemain = nVal % 16;

		// debugPrint("" + nRemain);

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

		// debugPrint("" + nResult);

		String sHexa = "";
		sHexa += getHexa(nResult);
		int nRemain = nVal % 16;

		// debugPrint("" + nRemain);

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
			// debugPrint("" + aa);

			// debugPrint("" + aa.longValue());
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
	public static String insertDQuoteEsc(String sSrc)
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
	public static String insertLineEsc(String sSrc)
	{
		String sDest = replaceString(sSrc,'\r',"");
		sDest = replaceString(sDest,'\n',"\\n");
		return sDest;
	}

	// CR -> "\n"
	public static String replaceString(String sSrc,char c,String sRep)
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
	public static String insertDbQuoteEsc(String sSrc)
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
			// debugPrint(""+nChar);
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

	void setGuidEx(String sGuid)
	{
		m_sGuidEx = sGuid;
		// debugPrint(sGuid);
	}

	boolean setProgress(int prog,int total,String msg)
	{
		return setProgress(prog,total,msg,m_sGuidEx);
	}

	boolean setProgress(int prog,int total,String msg,String sGuid)
	{
		if (!isValidTransaction(sGuid))
			return false;

		if (!JLString.IsEmpty(msg))
		{
			m_sessn.putValue(sGuid+"progmsg",msg);
			// debugPrint(sGuid+"progmsg : "+msg);
		}

		Integer nTotal = new Integer(total);
		m_sessn.putValue(sGuid+"total",nTotal);
		//debugPrint(sGuid+"total : "+nTotal);

		Integer nProg = new Integer(prog);
		m_sessn.putValue(sGuid+"progress",nProg);
		//debugPrint(sGuid+"progress : "+nProg);
		return true;
	}

	boolean isValidTransaction(String sGuid)
	{
		if (JLString.IsEmpty(sGuid))
			return false;

		// sync 는 현재 valid한 action을 뜻한다.
		// guid 는 나의 activation 이름이다.
		// sync = guid이면, 내가 valid한거고
		// sync != guid 이면, 내가 invalid하니간  kill transaction해야 한다.
		// count값은 값을  sessn에 저장하는 것이기 때문에 아무거나 되도 된다.
		// 즉 2개 이상의 activation이 동이에 도는 것을 방지하기 위한 것은 sync, guid만 관여하고, count값은 상관없다.

		// 나의 activation말고 다른 activation이 돌고 있으면서, 나의activation이 valid하지 않으면, 즉시 중단하고, 나간다.
		// 이렇게 하는 이유는 다중의 activation이 동시에 transaction을 처리하는 것을 막기 위함이다.
		// guid와 sSync가 동일하면, 현재의 activation만 남고, 다른 action은 중지한다.

		String sSync = (String)m_sessn.getValue("sync");
		// debugPrint("guid : " + sGuid);
		// debugPrint("sync : " + sSync);

		if (JLString.IsEmpty(sSync))
		{
			// 현재 돌고 있는 activation이 없기 때문에 내가 valid가 된다.
			m_sessn.putValue("sync",sGuid);
			// // debugPrint("kill transaction 2");
			setGuidEx(sGuid);
			return true;
		}
		if (!sSync.equalsIgnoreCase(sGuid))
		{
			// 현재 나 말고, 다른게 돌고 있다.
			// // debugPrint("kill transaction 3");
			return false;
		}
		// 내가 현재 valid한 transaction이다.
		setGuidEx(sGuid);
		return true;
	}

	public double getDoublePcnt(int iCount, int iTotal)
	{
		if (iCount == 0)
		{
			return 0;
		}

		if (iTotal == 0)
		{
			return 0;
		}

		double dCount = 0.00;
		double dTotal = 0.00;

		dCount = iCount;
		dTotal = iTotal;

		double dPcnt = 0.00;
		int iPcnt = 0;

		dPcnt = (dCount/dTotal)*100;
		iPcnt = (int)(dPcnt * 100);

		dPcnt = iPcnt/100.00;

		return dPcnt;

	}

	void moveFolder(String sTbl,int folderid)
	{
		if (JLString.IsEmpty(sTbl))
			return;
		String[] strArray = m_req.getParameterValues("cb");
		if (strArray == null)
		{
			return;
		}
		int nCh = strArray.length;
		for (int i = 0; i < nCh; i++)
		{
			int nRet;
			String strSql = "update "+sTbl+" set folderid = "+folderid+" where id="+strArray[i];
			JLStmt sql = new JLStmt();
			// debugPrint(strSql);
			nRet = sql.QueryExecute(strSql,null,"cm");
		}
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

	void decodeSqlParam(JLRcd param)
	{
		String sEsc = getStrValue(param,"encode");
		if (!JLString.IsEmpty(sEsc))
		{
			char cEsc = sEsc.charAt(0);
			if (cEsc == '$')
			{
				// debugPrint(param);
				int nCount = param.GetCount();
				String strProp;
				String strValue = "";
				for (int i=0;i<nCount;i++)
				{
					strProp = param.GetProp(i);
					if (strProp == null)
						continue;
					strValue = param.GetStrValue(strProp);
					if (strValue == null)
						strValue = "";
					param.SetValue(strProp,removeEsc(strValue,'$'));
				}
			}
		}
		setValue(param,"encode","");
	}



public class JLJsp extends JLObj
{
	JLJsp()
	{
	}

	void setValue(String sProp,String sVal)
	{
		if (m_sessn == null)
			return;
		JLRcd rcd = (JLRcd)m_sessn.getValue("control");
		if (rcd == null)
		{
			rcd = new JLRcd();
			m_sessn.putValue("control",rcd);
		}
		rcd.SetValue(sProp,sVal);
	}

	void setClassHome(String sVal)
	{
		setValue("classhome",sVal);
	}

	void setClassPath(String sVal)
	{
		setValue("classpath",sVal);
	}

	void setHome(String sVal)
	{
		setValue("home",sVal);
	}

	void setPath(String sVal)
	{
		setValue("path",sVal);
	}

	void setClassImgPath(String sVal)
	{
		setValue("classimgpath",sVal);
	}

	String getValue(String sProp)
	{
		if (m_sessn == null)
			return "";
		JLRcd rcd = (JLRcd)m_sessn.getValue("control");
		if (rcd == null)
		{
			rcd = new JLRcd();
			m_sessn.putValue("control",rcd);
		}
		String sVal = rcd.GetStrValue(sProp);
		return sVal;
	}

	void setCharSet(String sCharSet)
	{
		setValue("charset",sCharSet);
	}
}
%>

<script language="javascript">
	function onlogin()
	{
		if (parent != this)
		{
			if (parent.onlogin != null)
				parent.onlogin();
		}
		else
		{
			window.navigate("login.jsp");
		}
	}
</script>
