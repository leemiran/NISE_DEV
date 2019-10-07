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
	m_obj.m_nX = 100;
	m_obj.m_aSegIdx = new Array();
	m_obj.m_nSegCount = 0;
	m_obj.m_nTotalCount = 0;
	m_obj.m_nGridCount = 10;
	m_obj.m_oFocus = null;
	m_obj.m_nMaxX = "";
	m_obj.m_nMaxY = "";
	m_obj.m_nX = "";
	m_obj.m_nY = "";
	m_obj.m_bFocusBox = false;
	m_obj.m_nViewPage = -1;
	m_obj.m_nPageStart = 0;
	m_obj.m_nPageSize = 10;
	m_obj.m_nPageCount = 0;
	m_obj.m_nCurSegSize = 0;
	m_obj.m_nPage = 0;
	m_obj.m_nMarginTopRcd = 0;
	m_obj.m_nMarginBottomRcd = 0;
	m_obj.m_bGridReady = false;
	m_obj.m_bDataReady = false;
	m_obj.m_nLineHeight = 20;
	m_obj.m_oSubmit = null;
	m_obj.m_nPrevScrollTop = 0;
	m_obj.m_nScrollDir = 1;	// -1 : prev, 1 : next
	m_obj.m_nHeaderWidth = 0;
	m_obj.m_nScrollPageStart = 0;
	m_obj.m_nFocusRow = -1;
	m_obj.m_sBtnFace = "#d4d0c8";
	m_obj.m_nColSize = 0;
	m_obj.m_focusitem = "";
	m_obj.m_focusitemid = "";
	m_obj.m_nLeftHeaderWidth = 0;
	m_obj.m_nUpdateIdx = -1;
	m_obj.m_startidx = 0;
	m_obj.m_focusitem = "";
	m_obj.m_focuscolor = "";
	m_obj.m_linebgcolor = new Array();

	function _getThis()
	{
		return m_obj;
	}
</script>


<%!
public class JLListCtrl extends JLHttp
{
	Vector m_vHeader;
	// 해더에 대한 정보를 저장하는 Vector 입니다.

	String m_sPKey;
	// 하나의 Row에서 Primary Key에 해당하는 필드의 이름을 저장하고 있습니다.

	String m_sCountSql;
	String m_sSelectSql;
	String m_sDsn;
	String m_sUrl;
	int m_nDebug;
	// debug 할때 debug mode를 나타냅니다.
	// 0 : no debug
	// 1 : submit data page to a blank window
	// 2 : 

	int m_nPageStart;
	// download할 page의 시작 위치를 나타냅니다.

	int m_nPageCount;
	// download할 page의 갯수

	int m_nPageSize;
	// download할 한개의 page에 들어가는 item 수

	JLRcd m_rParam;
	// get, post로 전달되는 parameter들을 저장하는 object

	int m_nTotalCount;
	// row count

	String m_sCtrl;
	// 사용 안함

	int m_nCurSegSize;
	// 현재 segment의 개수

	int m_nLineHeight;
	// 한개의 row height

	int m_nEditMode;
	// grid의 경우 편집이 가능한지, 현재 사용 않함

	int m_pnWidth[];
	// header width를 저장하고 이는 object

	int m_nLeftHeaderWidth;
	// 왼쪽 header의 width;

	boolean m_bScript;
	// 사용안함
	int m_nPaging;

	int m_nEnableContextMenu;
	// context menu를 사용할지 결정

	JLRcd m_rMeta;
	// data record에 대한 field명을 저장하고 있는 class

	int m_nAccessMode;
	// direct , sequential access mode 를 설정하는 class
	// 0 : direct (default)
	// 1 : sequential 

	String m_sLastKey;
	// sequential access mode에서 사용되는 이전 segment의 마지막, key 값

	String m_sNextKey;
	// sequential access mode에서 사용되는 현재 segment의 마지막, key 값

	int m_nKeyRcdIdx;
	// sequential access mode에서 사용되는 record에서 key 값의 idx (zero base)

	int m_nServerData;
	// 서버의

	String m_sBtnFace;
	// header background color

	String m_sLineBgColor[];
	// line bg color

	int m_nStripe = 0;
	// strip의 숫자 : default 0
	// setStripe 사용
	// setStripeBgColor사용

	String m_sFocusColor;
	// list에서 mouse가 over시에 focus된 row의 background color설정
	// setFocusColor 사용

	public JLListCtrl(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		m_nLeftHeaderWidth = 0;
		init();
		setClassName("JLListCtrl");
		m_bScript = false;
		m_nEnableContextMenu = 0;
		m_rMeta = new JLRcd();
		m_nAccessMode = 0;	// direct access
		m_sLastKey = "";
		m_sNextKey = "";
		m_nKeyRcdIdx = 0;
		
		// default server에서 데이터를 가지고 옵니다.
		m_nServerData = 1;
		m_sBtnFace = "#d4d0c8";
		m_sLineBgColor = new String[32];
		m_nStripe = 0;
		m_sFocusColor = "#cccccc";
	}

	// sColor : #cccccc
	// 마우스가 over 할때 row의 color가 변하게 되고, 이 color를 설정합니다.
	public void setFocusColor(String sColor)
	{
		m_sFocusColor = sColor;
	}

	// 줄무니가 몇 단위로 설정될 것인가.
	public void setStripe(int nStripe)
	{
		m_nStripe = nStripe;
	}

	// sColor : #dfdfdf
	// nIdx : 0 ~ 31
	public void setStripeBgColor(int nIdx,String sColor)
	{
		if (nIdx < 0 || nIdx >= 32)
			return;
		m_sLineBgColor[nIdx] = sColor;
	}
	
	// header의 background color를 설정합니다.
	// default color : #d4d0c8
	public void setBtnFace(String sColor)
	{
		// m_sBtnFace = "#d4d0c8";
		m_sBtnFace = sColor;
	}
	
	// header의 background color를 얻어냅니다.
	public String getBtnFace()
	{
		return m_sBtnFace;
	}

	// 0 : direct access
	// 1 : sequential access
	public void setAccessMode(int nMode)
	{
		m_nAccessMode = nMode;
	}

	// record의 primary key가 몇번째에 해당하는지
	// 0 base	
	void setKeyRcdIdx(int nIdx)
	{
		m_nKeyRcdIdx = nIdx;
	}

	// grid의 left header의 width 설정
	public void setLeftHeaderWidth(int nWidth)
	{
		m_nLeftHeaderWidth = nWidth;
	}
	
	// 하나의 page에 들어가는 item의 갯수
	public void setPageSize(int nSize)
	{
		m_nPageSize = nSize;
	}

	// contextmenu를 사용하겠다는뜻 : 현재 사용안함
	public void enableContextMenu(int nEnable)
	{
		m_nEnableContextMenu = nEnable;
	}

	// java -> jsp 파일을 생성할때 jsp파일에 저장되는 내용을 String으로 return 합니다.
	// 시스템에서 자동으로 호출 
	public String getCode()
	{
		String sClassNameExt = m_sClassName+"."+m_sClassExt;
		String sHtml = "";
		String sPath = m_sPath;
		if (!JLString.IsEmpty(sPath))
		{
			sPath += "/";
		}
		String sClassPath = m_sClassPath;
		if (!JLString.IsEmpty(sClassPath))
		{
			sClassPath += "/";
		}
      String m_sCharSet = m2soft.rdsystem.server.core.install.Message.getcontentType();
		String m_sContentType = m_sCharSet;
		sHtml += "<"+"%@ page language=\"java\" %"+">\n";
		sHtml += "<"+"%@ page import=\"java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File \" %"+">\n";
		sHtml += "<"+"%@ page contentType=\""+m_sContentType+"\" %"+">\n";
		
		if (!JLString.IsEmpty(m_sCodeHeader))
		{
			sHtml += m_sCodeHeader;
			sHtml += "\n";
		}
		if (!JLString.IsEmpty(m_sClassName))
		{
			if (!m_sClassName.equalsIgnoreCase("JLListCtrl"))
				sHtml += "<"+"%@ include file=\""+sClassNameExt+"\" %"+">\n";
				//sHtml += "<"+"%@ include file=\""+sPath+sClassNameExt+"\" %"+">\n";
		}
		/*
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLJsp.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLObj.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLRuntimeClass.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JSHttp.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLHttp.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLListCtrl.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JSContextMenu.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLContextMenu.java\" %"+">\n";
		*/
		sHtml += "<"+"%@ include file=\"../control/lib/JLJsp.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JLObj.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JLRuntimeClass.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JSHttp.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JLHttp.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JLListCtrl.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JSContextMenu.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\"../control/lib/JLContextMenu.java\" %"+">\n";

		sHtml += "<body bgcolor=\"#ffffff\" text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n";
		sHtml += "<"+"%\n";
		sHtml += "initArg(request,out);\n";
		sHtml += m_sClassName+" ctrl = new "+m_sClassName+"(out,session);\n";
		sHtml += "ctrl.initByParam(m_param);\n";
		sHtml += "ctrl.printList();\n";
		sHtml += "ctrl.printFinishScript();\n";
		sHtml += "%"+">\n";
		sHtml += "</body>";
		return sHtml;
	}

	// class를 초기화 합니다.
	public void init()
	{
		m_pnWidth = new int[64];
		m_vHeader = new Vector();
		m_rParam = new JLRcd();

		m_nPageStart = 0;	// default page start 0
		m_nPageCount = 3;	// default 3 pages
		m_nPageSize = 10;	// default : one page contains 10 elements
		m_nTotalCount = -1;
		m_sUrl = "";	// default
		m_sCtrl = "JLListCtrlBody.jsp";
		m_nCurSegSize = 0;
		m_nLineHeight = 22;
		m_nWidth = 0;
		m_nEditMode = 0;	// readonly
		m_nPaging = 1;
	}

	// paging 기능을 사용합니다.
	public void enablePaging(int nEnable)
	{
		// m_nPaging > 0, : enable
		m_nPaging = nEnable;
	}
	
	/*
	0 : default - no debug
	1 : segment를 표시하는 jsp를 _blank window에 띄웁니다.
	2 : action 을 처리하는 jsp를 _blank window에 띄웁니다.
	3 : top header를 표시하는 jsp를 _blank window에 띄웁니다.
	*/
	void setDebug(int nDebug)
	{
		m_nDebug = nDebug;
	}
	
	// grid의 cell을 편집 기능을 사용합니다. Optional
	void setEditMode(int nEditMode)
	{
		m_nEditMode = nEditMode;
	}
	
	// list에서 한개의 line의 높이를 설정하는 기능
	// default : 22 pixel
	void setLineHeight(int nLineHeight)
	{
		m_nLineHeight = nLineHeight;
	}
	
	// list에서 한개의 line 높이를 얻어오는 함수
	public int getLineHeight()
	{
		return m_nLineHeight;
	}
	
	// get, post로 전달된 parameter를 이용하여 class를 초기화 한다.
	void initByParam(JLRcd param)
	{
		if (param == null)
			return;
		enablePaging(param.GetIntValue("paging"));
		String sSelectSql = param.GetStrValue("selectsql");
		String sCountSql = param.GetStrValue("countsql");
		String sDsn = param.GetStrValue("dsn");
		String sPKey = param.GetStrValue("pkey");
		int nDebug = param.GetIntValue("debug");
		init("",sSelectSql,sCountSql,sDsn,sPKey,nDebug);
		initParamHeader(param);

		int nPageStart = getIntValue(param,"pagestart");
		int nPageCount = getIntValue(param,"pagecount");
		int nPageSize = getIntValue(param,"pagesize");
		initPage(nPageStart,nPageCount,nPageSize);
		m_nEditMode = getIntValue(param,"editmode");
		
		if (m_nDebug > 0)
			debugPrintStream(param);
			
		String sMainFrame = getStrValue(param,"_mainframe");
		if (!JLString.IsEmpty(sMainFrame))
			setMainFrame(sMainFrame);
		m_nLeftHeaderWidth = getIntValue(param,"_leftheaderwidth");

		m_nAccessMode = getIntValue(param,"_accessmode");
		m_sLastKey = getStrValue(param,"_lastkey");
		m_sImgPath = getStrValue(param,"_imgpath");
		m_sBtnFace = getStrValue(param,"_btnface");
		m_sFocusColor = getStrValue(param,"_focuscolor");

		m_nStripe = getIntValue(param,"_stripe");
		for (int i=0;i<m_nStripe;i++)
		{
			m_sLineBgColor[i] = getStrValue(param,"_stripebgcolor"+i);
		}

		// debugPrint("stripe count : " +m_nStripe);
		// debugPrint(m_sLineBgColor);

		setParam(param);
	}
	
	// class를 초기화 합니다.
	void init(String sSelectSql,String sCountSql,String sDsn,String sPKey,int bDebug)
	{
		init("",sSelectSql,sCountSql,sDsn,sPKey,bDebug);
	}
	
	// 클래스를 초기화 합니다.
	void init(String sName,String sSelectSql,String sCountSql,String sDsn,String sPKey,int bDebug)
	{
		setName(sName);
		m_sSelectSql = sSelectSql;
		m_sCountSql = sCountSql;
		m_sDsn = sDsn;
		m_sPKey = sPKey;
		m_nDebug = bDebug;
	
		m_nTotalCount = getCount();
	}
	
	// 사용하지 않음
	void setGridData(String sGridData)
	{
		m_sUrl = sGridData;
	}
	
	// page관련 변수를 초기화함
	void initPage(int nPageStart,int nPageCount,int nPageSize)
	{
		m_nPageStart = nPageStart;
		if (nPageCount > 0)
			m_nPageCount = nPageCount;
		if (nPageSize > 0)
			m_nPageSize = nPageSize;
			
		String sHtml = "";
		sHtml += "<script>";
		sHtml += "m_obj.m_nPageSize = "+nPageSize+";";
		sHtml += "</script>";
		printStream(sHtml);
	}
	
	// header관련 parameter를 초기화 합니다.
	void initParamHeader(JLRcd param)
	{
		if (param == null)
			return;
		int nCount = getIntValue(param,"count");
		if (nCount > 0)
		{
			String sFld = "";
			String sText = "";
			String sIcon = "";
			int nWidth = 0;
			int bCheckbox = 0;
			int bEdit = 0;
			String sID = "";
			String sCellIcon = "";
			int bNo = 0;
			int nIdx = 0;
			m_nWidth = m_nLeftHeaderWidth;
			
			// debugPrintStream(param);
			for (int i=0;i<nCount;i++)
			{
				sFld = "_text"+i;
				sText = getStrValue(param,sFld);
				sFld = "_width"+i;
				nWidth = getIntValue(param,sFld);
				sFld = "_icon"+i;
				sIcon = getStrValue(param,sFld);
				sFld = "_checkbox"+i;
				bCheckbox = getIntValue(param,sFld);

				sFld = "_edit"+i;
				bEdit = getIntValue(param,sFld);
				sFld = "_id"+i;
				sID = getStrValue(param,sFld);
				sFld = "_cellicon"+i;
				sCellIcon = getStrValue(param,sFld);
				sFld = "_no"+i;
				bNo = getIntValue(param,sFld);
				sFld = "_idx"+i;
				nIdx = getIntValue(param,sFld);

				addHeader(sText,nWidth,sIcon,"",bCheckbox,bEdit,sID,sCellIcon,bNo,nIdx);
				m_nWidth += nWidth;
				
			}
		}
	}
	
	void setNextKey(String sKey)
	{
		m_sNextKey = sKey;
	}

	void setParam(String sProp,String sValue)
	{
		if (JLString.IsEmpty(sProp))	
			return;
		m_rParam.SetValue(sProp,sValue);
	}
	
	int getHeaderWidth()
	{
		if (m_vHeader == null)
			return 0;
		int nRet = 0;
		int nCount = m_vHeader.size();
		for (int i=0;i<nCount;i++)
		{
			JLRcd rcd = (JLRcd)m_vHeader.elementAt(i);
			if (rcd == null)
				continue;
			int nWidth = rcd.GetIntValue("width");
			nRet += nWidth;
		}
		return nRet;
	}
	
	// nIdx 번째 (zero base) header의 width를 얻어냅니다. (pixel단위)
	int getHeaderWidth(int nIdx)
	{
		if (m_vHeader == null)
			return 0;
		int nRet = 0;
		int nCount = m_vHeader.size();
		if (nCount <= nIdx)
			return 0;
		JLRcd rcd = (JLRcd)m_vHeader.elementAt(nIdx);
		if (rcd == null)
			return 0;
		int nWidth = rcd.GetIntValue("width");
		return nWidth;
	}
	
	// 해더를 추가합니다.
	void addHeader(Vector vHeader)
	{
		if (vHeader == null)
			return;
		int nCount = vHeader.size();
		for (int i=0;i<nCount;i++)
		{
			JLRcd rcd = (JLRcd)vHeader.elementAt(i);
			if (rcd == null)
				continue;
				
			String sText = rcd.GetStrValue("text");
			int nWidth = rcd.GetIntValue("width");
			String sIcon = rcd.GetStrValue("icon");
			if (JLString.IsEmpty(sText))
				sText = ""+(m_vHeader.size()+1);
			if (JLString.IsEmpty(sIcon))
				sIcon = "";
			// if (nWidth <= 100)
			//	nWidth = 150;				
			addHeader(sText,nWidth,sIcon,"");
		}
	}

	// 해더 항목을 추가
	void addHeader(String sText,int nWidth,String sIcon,String sAction)
	{
		addHeader(sText,nWidth,sIcon,sAction,0);
	}

	// 해더 항목을 추가
	void addHeader(String sText,int nWidth,String sIcon,String sAction,int bCheckBox)
	{
		addHeader(sText,nWidth,sIcon,sAction,0,0,"","",0,-1);
	}

	// 해더 항목을 추가
	void addHeader(String sText,int nWidth,String sIcon,String sAction,int bCheckBox,int bEdit,String id,String sCellIcon,int bNo)
	{
		addHeader(sText,nWidth,sIcon,sAction,bCheckBox,bEdit,id,sCellIcon,bNo,-1);
	}

	// 해더 항목을 추가
	// sText : 해더에 보여지는 문자열
	// nWidth : 해더의 width
	// sIcon : 문자열 앞에 보이는 icon (16x16 pixel만 허용)
	// sAction : 헤더가 click될때 호출되는 javascript
	// bCheckBox : checkbox용 header
	// bEdit : 현재 header의 column이 editable하게
	// id : header의 식별자 : 영문으로만 기입
	// sCellIcon : list되는 항목에 표시되는 icon
	// bNo : sequence number
	// nFieldIdx : getSegment에서 return되는 Vector의 idx
	void addHeader(String sText,int nWidth,String sIcon,String sAction,int bCheckBox,int bEdit,String id,String sCellIcon,int bNo,int nFieldIdx)
	{
		JLRcd rcd = new JLRcd();

		rcd.SetValue("text",sText);
		rcd.SetValue("width",nWidth);
		rcd.SetValue("icon",sIcon);
		rcd.SetValue("action",sAction);
		rcd.SetValue("checkbox",bCheckBox);

		rcd.SetValue("edit",bEdit);
		rcd.SetValue("id",id);
		rcd.SetValue("cellicon",sCellIcon);
		rcd.SetValue("no",bNo);
		rcd.SetValue("idx",nFieldIdx);

		int nIdx = m_vHeader.size();
		m_pnWidth[nIdx] = nWidth;	// 빠른 시간을 위해서
		m_vHeader.addElement(rcd);		
	}
	
	// 모든 해더의 size , 즉 row size
	int getHeaderSize()
	{
		return m_vHeader.size();
	}
	
	// nIdx가 가리키는 해더에 대한 정보를 얻어옵니다.
	JLRcd getHeaderAt(int nIdx)
	{
		int nSize = getHeaderSize();
		if (nSize <= nIdx) 
			return null;
		JLRcd rcd = (JLRcd)m_vHeader.elementAt(nIdx);
		return rcd;
	}
	
	String getIconHtml(int nIdx)
	{
		JLRcd rcd = getHeaderAt(nIdx);
		if (rcd == null)
			return "";
		String sIcon = rcd.GetStrValue("icon");
		// sIcon = "images/button16.gif";	// sample
		if (JLString.IsEmpty(sIcon))
			return "";
		String sRet = "<td align=center valign=top width=16 bgcolor=\"#dfdfdf\"><img alt border=\"0\" src=\""+sIcon+"\"></td>";
		return sRet;
	}
	
	String getSortOrderHtml(int nIdx)
	{
		/*
		String sIcon = m_sIcon[nIdx];
		if (JLString.IsEmpty(sIcon))
			return "";
		// <img src="images/sort_asc_16.gif">
		sIcon = m_sImgPath+"sort_asc_16.gif";
		
		String sRet = "<td align=center valign=top width=16 bgcolor=\"#dfdfdf\"><img alt border=0 height=1 src=images/trans.gif width=16><br><img alt border=\"0\" src=\""+sIcon+"\">";
		return sRet;
		*/
		return "";
	}
	
	String getFldNameById(String sID)
	{
		if (JLString.IsEmpty(sID))
			return "";
		if (sID.equalsIgnoreCase("_ui"))
			return "고객아이디";
		if (sID.equalsIgnoreCase("_un"))
			return "고객이름";
		if (sID.equalsIgnoreCase("_em"))
			return "고객이메일";
		return sID;
	}
	
	void initHeaderBySql(String sSql,String sDsn)
	{
		// debugPrintStream(sSql);
		JLStmt stmt = new JLStmt();
		Vector vec = stmt.SelectQueryN(sSql,null,sDsn,1);
		if (vec == null)
			return;
		if (vec.size() <= 0)
			return;
		JLRcd rcd = (JLRcd)vec.elementAt(0);
		// debugPrintStream(rcd);

		int nCount = rcd.GetCount();
		for (int i=0;i<nCount;i++)
		{
			String sFld = (String)rcd.GetProp(i);
			String sText = getFldNameById(sFld);
			addHeader(sText,150,"images/button16.gif","");
		}
	}
	
	void setServerData(int nMode)
	{
		m_nServerData = nMode;			
	}

	// 현재 object에 대한 html을 print합니다.
	void printBody()
	{
		String sUrl = createInstance(m_sHome,m_sPath,m_sClassName);
		m_sUrl = sUrl;
		// debugPrint("data url : " + m_sUrl);

		printStream("<!-- JLListCtrl.java printBody start -->");
		
		printStream("<script>");
		printStream("	m_obj.m_nDebug = "+m_nDebug+";");
		printStream("	m_obj.m_nHeaderWidth = "+getHeaderWidth()+";");
		printStream("</script>");

		printStream("<!-- grid data param start -->");
		printStream("<form name=\"fgridparam\">");

		// grid data에 전달되는 parameter의 input값을 설정합니다.
		{
			String sProp = "";
			String sVal = "";
			int nCount = m_rParam.GetCount();
			for (int i=0;i<nCount;i++)
			{
				sProp = (String)m_rParam.GetProp(i);
				sVal = (String)m_rParam.GetAt(i);
				printStream("<input type=\"hidden\" name=\""+sProp+"\" value=\"\">");
				printStream("<script> document.fgridparam."+sProp+".value = \""+this.insertDQuoteEsc(sVal)+"\"; </script>");
			}
		}
		printStream("<input type=\"hidden\" name=\"_focuscolor\" value=\""+m_sFocusColor+"\">");
		printStream("<input type=\"hidden\" name=\"_stripe\" value=\""+m_nStripe+"\">");
		{
			for (int i=0;i<m_nStripe;i++)
			{
				printStream("<input type=\"hidden\" name=\"_stripebgcolor"+i+"\" value=\""+m_sLineBgColor[i]+"\">");
			}
		}
		printStream("<input type=\"hidden\" name=\"_classname\" value=\""+m_sClassName+"\">");

		printStream("<input type=\"hidden\" name=\"_name\" value=\""+m_sName+"\">");
		printStream("<input type=\"hidden\" name=\"_cmd\" value=\"0\">");
		printStream("<input type=\"hidden\" name=\"_idx\" value=\"0\">");
	
		// 버튼 칼라
		printStream("<input type=\"hidden\" name=\"_btnface\" value=\""+m_sBtnFace+"\">");

		// 모든 data를 client에서 처리하는 경우 setServerData(0);를 호출합니다.
		printStream("<input type=\"hidden\" name=\"_serverdata\" value=\""+m_nServerData+"\">");
		
		// sequential access mode에서 마지막 page, 마지막 element의 key값을 설정한다.
		// next page의 key 값으로 활용한다.
		printStream("<input type=\"hidden\" name=\"_lastkey\" value=\"\">");
		// sequential access mode인경우 lastpage는 last download된 page번호를 뜻한다.
		// default : direct access mode
		printStream("<input type=\"hidden\" name=\"_lastpage\" value=\"0\">");
		printStream("<input type=\"hidden\" name=\"_rowcount\" value=\"0\">");
		printStream("<input type=\"hidden\" name=\"_accessmode\" value=\""+m_nAccessMode+"\">");
		printStream("<input type=\"hidden\" name=\"_mainframe\" value=\""+m_sMainFrame+"\">");
		printStream("<input type=\"hidden\" name=\"_leftheaderwidth\" value=\""+m_nLeftHeaderWidth+"\">");
		printStream("<input type=\"hidden\" name=\"enablecontextmenu\" value=\""+m_nEnableContextMenu+"\">");
		printStream("<input type=\"hidden\" name=\"paging\" value=\""+m_nPaging+"\">");
		printStream("<input type=\"hidden\" name=\"pagestart\" value=\"0\">");
		printStream("<input type=\"hidden\" name=\"pagecount\" value=\"3\">");
		printStream("<input type=\"hidden\" name=\"pagesize\" value=\""+m_nPageSize+"\">");
		printStream("<input type=\"hidden\" name=\"pkey\" value=\""+m_sPKey+"\">");
		printStream("<input type=\"hidden\" name=\"editmode\" value=\""+m_nEditMode+"\">");
		printStream("<input type=\"hidden\" name=\"_debug\" value=\""+m_nDebug+"\">");
		printStream("<input type=\"hidden\" name=\"griddata\" value=\"\">");
		printStream("<input type=\"hidden\" name=\"_imgpath\" value=\"\">");
		printStream("<script> document.fgridparam._imgpath.value = \""+this.insertDQuoteEsc(m_sImgPath)+"\"; </script>");
		
		printStream("<script> document.fgridparam.griddata.value = \""+this.insertDQuoteEsc(m_sUrl)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"dsn\" value=\"\">");
		printStream("<script> document.fgridparam.dsn.value = \""+this.insertDQuoteEsc(m_sDsn)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"countsql\" value=\"\">");
		printStream("<script> document.fgridparam.countsql.value = \""+this.insertDQuoteEsc(m_sCountSql)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"selectsql\" value=\"\">");
		printStream("<script> document.fgridparam.selectsql.value = \""+this.insertDQuoteEsc(m_sSelectSql)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"count\" value=\""+getHeaderSize()+"\">");

		printStream("<input type=\"hidden\" name=\"classpath\" value=\"\">");
		printStream("<script> document.fgridparam.classpath.value = \""+this.insertDQuoteEsc(m_sClassPath)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"path\" value=\"\">");
		printStream("<script> document.fgridparam.path.value = \""+this.insertDQuoteEsc(m_sPath)+"\"; </script>");
		int nCount = getHeaderSize();
		for (int i=0;i<nCount;i++)
		{
			JLRcd rcd = getHeaderAt(i);
			if (rcd == null)
				continue;
			printStream("<input type=\"hidden\" name=\"_text"+i+"\" value=\""+rcd.GetStrValue("text")+"\">");
			printStream("<input type=\"hidden\" name=\"_width"+i+"\" value=\""+rcd.GetStrValue("width")+"\">");
			printStream("<input type=\"hidden\" name=\"_icon"+i+"\" value=\""+rcd.GetStrValue("icon")+"\">");
			printStream("<input type=\"hidden\" name=\"_checkbox"+i+"\" value=\""+rcd.GetStrValue("checkbox")+"\">");

			printStream("<input type=\"hidden\" name=\"_edit"+i+"\" value=\""+rcd.GetStrValue("edit")+"\">");
			printStream("<input type=\"hidden\" name=\"_id"+i+"\" value=\""+rcd.GetStrValue("id")+"\">");
			printStream("<input type=\"hidden\" name=\"_cellicon"+i+"\" value=\""+rcd.GetStrValue("cellicon")+"\">");
			printStream("<input type=\"hidden\" name=\"_no"+i+"\" value=\""+rcd.GetStrValue("no")+"\">");
			printStream("<input type=\"hidden\" name=\"_idx"+i+"\" value=\""+rcd.GetStrValue("idx")+"\">");
			printStream("<input type=\"hidden\" name=\"_val"+i+"\" value=\"\">");
		}
		printStream("</form>");
		printStream("<!-- grid header param end -->");

		// action frame
		printStream("<iframe name=\""+m_sName+"_faction\" frameborder=\"0\" src=\"\" scrolling=no STYLE=\"HEIGHT:0px; LEFT: 0px; MARGIN-TOP: 0px; POSITION: absolute; VISIBILITY: visible; WIDTH:0px; \"></IFRAME>");

		printStream("<script>");
		printStream("	m_obj.m_oContainer = window;");
		printStream("</script>");
	}
	
	// container에서 호출된다.
	void printObj()
	{
		printBody();
		printStream("<script>");
		printStream("	JLListCtrl_initBody(m_obj);");
		printStream("	JLListCtrl_initHeader(m_obj);");
		printStream("</script>");
		String sHtml = "";
		sHtml += "<div id=divHtmlSize style=\"POSITION: absolute; top:0px; left:0px; width:1px; height:10px; \">"; // <table><tr><td></td></tr></table>";
		sHtml += "</div>";
		printStream(sHtml);
	}

	// row 갯수를 리턴합니다.	
	int getCount()
	{
		if (m_nTotalCount >= 0)
			return m_nTotalCount;
		int nRet = getCountEx();
		m_nTotalCount = nRet;
		return m_nTotalCount;
	}

	// row 갯수를 리턴합니다.	
	// getCountEx는 단한번 호출됩니다.
	int getCountEx()
	{
		int nRet = 0;
		nRet = getCount(m_sCountSql,m_sDsn);
		if (m_nPaging == 0)
		{
			if (nRet > m_nPageSize)
				nRet = m_nPageSize;
		}
		return nRet;
	}

	// 사용안함
	int getCount(String sCountSql,String sDsn)
	{
		String sSql = sCountSql;
		if (JLString.IsEmpty(sSql))
			return 0;
		if (m_nDebug > 0)
			debugPrintStream("countsql : " + sCountSql);
		JLStmt stmt = new JLStmt();
		Vector vec = stmt.SelectQuery(sSql,null,sDsn);
		if (vec == null)
			return 0;
		if (vec.size() <= 0)
			return 0;
		JLRcd rcd = (JLRcd)vec.elementAt(0);
		if (rcd != null)
		{
			int nRet;
			String sVal = (String)rcd.GetAt(0);
			nRet = JLString.parseInt(sVal);
			if (m_nDebug > 0)
				debugPrintStream("totalcount : "+nRet);
			return nRet;
		}
		return 0;
	}

	// 사용안함
	String getPageSql(String sSelectSql,int nPageCount,int nPageStart,int nPageSize,String sPKey)
	{
		if (JLString.IsEmpty(sSelectSql))
			return "";
		String sSegSql = "";
		String sTmp = sSelectSql;
		int nSegmentSize = nPageCount * nPageSize;
		sTmp = sTmp.toUpperCase();
		int nIdx = sTmp.indexOf("SELECT");
		if (nIdx >= 0)
		{
			String sMid = sSelectSql.substring(nIdx+6);
			sSegSql = "select top "+(nPageStart*nPageSize + nSegmentSize)+ " " + sMid;
		}
		
		String sSkipSql = "";
		if (nIdx >= 0)
		{
			String sMid = sSelectSql.substring(nIdx+6);
			sSkipSql = "select top "+(nPageStart*nPageSize)+ " " + sMid;
		}
		
		String sSql = "";
		if (nPageStart == 0)
			sSql = "select top "+nSegmentSize+" s.* from ("+sSegSql+") s";
		else
			sSql = "select top "+nSegmentSize+" s.* from ("+sSegSql+") s where s."+sPKey+" not in (select s2."+sPKey+" from ("+sSkipSql+") s2)";
		return sSql;
	}

	/*
	srcsql : select id as _ui,text,domain,dlv from tlog order by id
	sql1 : select top 60 id as _ui,text,domain,dlv from tlog order by id
	sql2 : select top 30 id as _ui,text,domain,dlv from tlog order by id
	
	select top 30 s.* from (select top 60 id as _ui,text,domain,dlv from tlog order by id) s 
		where s._ui not in 
		(select top 30 s2._ui from (select top 30 id as _ui,text,domain,dlv from tlog order by id) s2)
	// 단점 속도가 느리다.
	select top 30 id as _ui,text,domain,dlv from tlog where _ui not in 
		(select top 30 s2._ui from (select top 30 id as _ui,text,domain,dlv from tlog order by id) s2)
	*/
	// Direct Access Mode
	// page : 하나 최소 packet 단위
	// segment : 데이터 교환 단위 : a set of pages (일반적으로 1 ~ 4개의 page -> segment)
	// 한번에 서버에서 데이터를 가지고 올때 1개의 page만 가지고 오면,
	// performance의 낭비가 심하기 때문에 , 한번에 여러개의 page를
	// 가지고 오면, performance낭비가 비교적 없습니다.
	/*
	모든 data record는 page로 나누어 집니다. (예 : 100개의 record = 10개의 page 
	about 3개 정도의 segment입니다. (about라고 한 이유는 시스템의 성능, scroll의
	취치에따라 segment내의 page 갯수는 가변적입니다.)
	한번에 가지고 오는 segment내의 page 시작 index를 pagestart라고 합니다.
	pagesize는 하나의 page내의 record갯수, (default : 10)
	pagecount 는 segment내의 page갯수
	따라서, pagestart는 뭐가 될지 모릅니다.
	*/
	// pagesize :10
	// pagestart : 
	// page access : 1,  n, 2,4
	
	/*
	아래는 MSSQL에서 direct access mode(특정 page를 바로 access하는 방식)에 사용되는 sql의 예입니다. (mssql)
	select id as _ui,text,domain,dlv from tlog order by id
	31 ~ 60 에 해당하는 record를 select하는 sql
	select top 30 s.* from (select top 60 id as _ui,text,domain,dlv from tlog order by id) s 
		where s._ui not in 
		(select top 30 s2._ui from (select top 30 id as _ui,text,domain,dlv from tlog order by id) s2)
	// 단점 속도가 느리다. (10,000,000갯수가 넘어가면, 상당히 느립니다. 		
	// 이 단점을 보완한, 방법이 sequential access mode입니다.
	*/
	
	// Sequential Access Mode
	/*
	record의 수가, 천만건 (1M 이상)인경우, 특정 page의 데이터만을 뽑아내는
	sql을 실행하면, db transaction이 상당히 느립니다. (select sql 처리속도)
	sequential access mode는 최종 select된 segment의 마지막 primary key값으로 
	다음 segment의 시작을 계산하여, db에서 select하는 방식
	page access가 순서번호를 따릅니다. 1,2,3 ... n
	*/
	Vector getSegment()
	{
		Vector vSeg = (Vector)getSegment(m_nPageCount,m_nPageStart,m_nPageSize);
		
		if (m_nAccessMode == 1)
		{
			// sequential access)
			// Next Key값 즉 다음 segment에서 사용될 lastkey 값을 설정하는 부분입니다.
			// next key값은 현재의 segment중에  맨 마지막 row의 값이되고, 이값을 setNextKey라는 함수를 사용하여,
			// 시스템에 설정하면, 다음 page호출시에 m_sLastKey로 넘어 옵니다.
			if (vSeg == null)
				return vSeg;
			int nSize = vSeg.size();
			if (nSize > 0)
			{
				Vector vRow = (Vector)vSeg.elementAt(nSize-1);
				if (vRow != null)
				{
					int nCol = m_nKeyRcdIdx;
					String sNextKey = (String)vRow.elementAt(nCol);
					setNextKey(sNextKey);
				}
			}
		}
		return vSeg;
	}
	
	// getSegment는 override된다.
	Vector getSegment(int nPageCount,int nPageStart,int nPageSize)
	{
		if (JLString.IsEmpty(m_sSelectSql))
			return null;
		String sSql = "";
		String sDsn = m_sDsn;
		if (m_nPaging > 0 && m_sDsn.equalsIgnoreCase("cm"))
			sSql = getPageSql(m_sSelectSql,nPageCount,nPageStart,nPageSize,m_sPKey);
		else
			sSql = m_sSelectSql;
		if (m_nDebug > 0)
			debugPrintStream(sSql);
		int nSegmentSize = nPageCount * nPageSize;
		JLStmt stmt = new JLStmt();
		Vector vec = stmt.SelectQueryVN(sSql,null,sDsn,nSegmentSize,m_rMeta);
		if (vec == null)
			return null;
		if (vec.size() <= 0)
			return null;
		return vec;
	}
	
	// 스크립트 방식에서 사용, 지금은 사용 않함
	String getData()
	{
		String sData = "";
		JLRcd rEmpty = new JLRcd();
		int nSegmentSize = m_nPageCount * m_nPageSize;
		Vector vec = getSegment();
		if (vec != null)
		{
			m_nCurSegSize = vec.size();
			for (int i=0;i<nSegmentSize;i++)
			{
				JLRcd rcd = null;
				if (i < m_nCurSegSize)
					rcd = (JLRcd)vec.elementAt(i);
				if (rcd == null)
					rcd = rEmpty;
				int nFld = rcd.GetCount();
				for (int j=0;j<getHeaderSize();j++)
				{
					String sVal = "";
					if (j < nFld)
					{
						sVal = (String)rcd.GetAt(j);
						sVal = this.insertDQuoteEsc(sVal);
					}
					if (j == 0 && nFld > 0)
						sVal += "_"+(m_nPageStart*10+i+1);
					if (!JLString.IsEmpty(sData))
						sData += ",";
					sData += "\""+sVal+"\"";
				}
			}
		}
		return sData;
	}
	
	// script 방식에서 사용
	void printGridData()
	{
		String sData = "";
		sData = getData();
		// debugPrintStream(sData);
		
		printStream("<script>");
		printStream("	var m_nCol = "+getHeaderSize()+";");
		printStream("	var m_nPageStart = "+m_nPageStart+";");
		printStream("	var m_nPageCount = "+m_nPageCount+";");
		printStream("	var m_nPageSize = "+m_nPageSize+";");
		printStream("	function getPageStart()");
		printStream("	{");
		printStream("		return m_nPageStart;");
		printStream("	}");
		printStream("	var m_aData = new Array("+sData+");");
		printStream("	function getData(x,y)");
		printStream("	{");
		printStream("		var nMod = y%10;");
		printStream("		var nPage = (y-nMod)/10;");
		printStream("		var idx = (nPage-m_nPageStart)*10*m_nCol + nMod*m_nCol+x;");
		printStream("		if (idx < 0 || idx >= m_nCol*30)");
		printStream("		{");
		printStream("			return null;");
		printStream("		}");
		printStream("		return m_aData[idx];");
		printStream("	}");
		printStream("</script>");
	}
	
	void printFinishScript()
	{
		int nHeight = getLineHeight()*m_nCurSegSize;
		int nWidth = this.getWidth();
		printStream("<script>");
		printStream("m_obj.m_nColSize = "+getHeaderSize()+";");
		printStream("m_obj.m_nCurSegSize = "+m_nCurSegSize+";");
		printStream("m_obj.m_nPageStart = "+m_nPageStart+";");
		printStream("m_obj.m_nPageSize = "+m_nPageSize+";");
		printStream("m_obj.m_nTotalCount = "+m_nTotalCount+";");
		printStream("m_obj.m_focuscolor = \""+m_sFocusColor+"\";");
		printStream("{ var form = parent.parent.fgridparam; if (form != null) { form._lastkey.value = \""+m_sNextKey+"\";} }");
		printStream("if (parent.parent._onFinishLoad != null) parent.parent._onFinishLoad(m_obj,"+getLineHeight()+","+nWidth+","+nHeight+","+m_nCurSegSize+","+m_nTotalCount+","+m_nPageStart+","+m_nPageCount+"); </script>");
		printCheckboxScript();
		printCellScript();
		if (m_nDebug > 0)
		{
			printStream("<table border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tr id=trScroll height=\"400\"><td><img alt border=0 height=1 src=images/trans.gif width=10 height=20></td></tr></table>");
			debugPrintStream(m_param);
		}
	}

	// 하나의 record에 대한 html을 print합니다.	
	void printRcd(int nDepth,int nIdx,Object oArg)
	{
		if (oArg == null)
			return;
		Vector vRcd = (Vector)oArg;
		if (m_bScript)
		{
			if (vRcd == null)
				return;
			String sHtml = "";
			String sVal = "";
			Object oItem = null;
			int nCol = getHeaderSize();
			int nSize = vRcd.size();
			for (int i=0;i<nCol;i++)
			{
				sVal = "";
				if (i < nSize)
				{
					oItem = vRcd.elementAt(i);
					if (oItem == null)
						sVal = "";
					else
						sVal = (String)oItem;
				}
				if (sVal == null)
					sVal = "";
				if (i != 0)
					sHtml += ",";
				sVal = this.insertDQuoteEsc(sVal);
				sHtml += "\""+sVal+"\"";
			}
			printStream(sHtml);
		}
		else
		{
			int no = nIdx+1; 
			// default color
			String sBgColor = "#ffffff";
			for (int i=0;i<m_nStripe;i++)
			{
				if (nIdx%m_nStripe == i)
				{
					sBgColor = m_sLineBgColor[i];
					break;
				}
			}
			String sHtml = "";
			int nStart = m_nPageStart * m_nPageSize;
			sHtml += "<table style=\"cursor: hand;\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\">"; //
			sHtml += "<tr id="+no+" bgcolor='"+sBgColor+"' align=left onmouseover=\"javascript:parent.parent.JLListCtrl_over(m_obj,"+no+")\" onmouseout=\"javascript:parent.parent.JLListCtrl_out(m_obj,"+no+")\">"; //
			sHtml += "\n<script>m_obj.m_linebgcolor["+(nIdx+1- nStart )+"]= \""+sBgColor+"\"</script>\n";
			String sCellHtml = "";

			if (m_vHeader == null)
				return;
			int nRet = 0;
			int nCount = m_vHeader.size();
			for (int i=0;i<nCount;i++)
			{
				JLRcd rHeader = (JLRcd)m_vHeader.elementAt(i);
				if (rHeader == null)
					continue;

				String id = rHeader.GetStrValue("id");
				int nWidth = getHeaderWidth(i);
				int nCheckbox = rHeader.GetIntValue("checkbox");
				String sCellIcon = rHeader.GetStrValue("cellicon");
				int nNoField = rHeader.GetIntValue("no");

				// debugPrint(rHeader);

				sCellHtml = "";
				if (nCheckbox > 0)
					sCellHtml = "<td width="+nWidth+" bordercolor=\"#ffffff\" align=center><img alt border=0 height=1 src=\"images/trans.gif\" width="+nWidth+"><br><input type=checkbox name=cb value=\""+id+"\" onclick=\"\"></td>"; //
				if (!JLString.IsEmpty(sCellIcon))
					sCellHtml = "<td align=center width=\""+nWidth+"\"><img alt border=0 height=0 src=\"images/trans.gif\" width="+nWidth+"><br><img src=\""+sCellIcon+"\"></td>";
				if (nNoField > 0)
					sCellHtml = "<td width="+nWidth+" bordercolor=\"#ffffff\" align=left><img alt border=0 height=3 src=\"images/trans.gif\" width="+nWidth+"><br><font color=\"#000000\" style=\"FONT: 9pt 굴림\">"+no+"</td>"; //
				if (JLString.IsEmpty(sCellHtml))
				{
					String sValue = "";
					int nRcdIdx = getRcdIdx(i,id);
					if (nRcdIdx < 0)
					{
						// default로 column순서로 설정
						nRcdIdx = i;
					}
					if (nRcdIdx >= 0 && nRcdIdx < vRcd.size())
					{
						sValue = (String)vRcd.elementAt(nRcdIdx);
						if (JLString.IsEmpty(sValue))
							sValue = "";
					}
					String sCellValue = "";
					int nEsc = sValue.indexOf("\\");
					if (nEsc < 0)
						sCellValue = sValue;
					int nRow = nIdx;
					int nCol = i;
					String sAction = onGetClickAction(nIdx,i,vRcd);
					if (JLString.IsEmpty(sAction))
						sAction = "";
					sCellHtml = "<td width="+nWidth+" onclick=\""+sAction+"\" bordercolor=\"#ffffff\" align=left><img alt border=0 height=3 src=\"images/trans.gif\" width="+nWidth+"><br><input readonly type=text name="+no+"_"+nCol+" value=\""+sCellValue+"\" maxlength=512 style=\"cursor: hand; width:"+nWidth+"px; BACKGROUND-COLOR: "+sBgColor+"; BORDER-BOTTOM: #000000 0px solid; BORDER-LEFT: #000000 0px solid; BORDER-RIGHT: #000000 0px solid; BORDER-TOP: #000000 0px solid;FONT-FAMILY: 굴림; FONT-SIZE: 9pt; COLOR: #000000; text-align=left\"></td>"; //
					if (nEsc >= 0)
					{
						sCellValue = this.insertDQuoteEsc(sValue);
						sCellHtml += "<script> { var obj = document.getElementById('"+no+"_"+nCol+"'); if (obj != null) obj.value = \""+sCellValue+"\"} </script>";
					}
				}
				sHtml += sCellHtml+"\n";
			}
			sHtml += "</tr></table>"; //
			sHtml += "<table border='0' cellspacing=\"0\"  cellpadding=\"0\" width=\""+getHeaderWidth()+"\">"; //
			sHtml += "<tr>"; //
			sHtml += "<td align=left height=\"1\" background=\""+m_sImgPath+"/dot_666666.gif\"><img src=\"images/trans.gif\" width=\"1\" height=\"1\"></td>"; //
			sHtml += "</tr></table>"; //
			printStream(sHtml);
		}
	}

	// 시스템에서 자동으로 호출되며
	int getRcdIdx(int nIdx,String id)
	{
		if (nIdx < 0 || m_vHeader == null)
			return -1;
		int nSize = m_vHeader.size();
		if (nSize <= nIdx)
			return -1;
		JLRcd rField = (JLRcd)m_vHeader.elementAt(nIdx);
		if (rField == null)
			return -1;
		int nRet = rField.GetIntValue("idx");
		return nRet;

		/*
		if (m_rMeta.GetCount() > 0)
		{
			nIdx = m_rMeta.GetIntValue(id);
			return nIdx;
		}
		return -1;
		*/
	}

	void printListStart()
	{
		printStream("<form name=\"fparam\">");
		int nPage = 0;
		if (m_bScript)
		{
			print("<script language=\"javascript\">");
			printStream("parent.parent.JLListCtrl_printListPageStart(m_obj,"+m_nPageStart+","+m_nPageSize+","+nPage+","+getLineHeight()+","+getHeaderWidth()+");");
			print("m_obj.m_vList = new Array(");
		}
		else
		{
			int nDataTop = m_nPageSize*getLineHeight()*nPage;
			printStream("<div id=\"gridPage"+(m_nPageStart+nPage)+"\">");
			printStream("<div id=\"gridData"+(m_nPageStart+nPage)+"\" style=\"POSITION:absolute; left:0px; top:"+nDataTop+"px;\">");

			String sHtml = "";

			int nStart = m_nPageStart * m_nPageSize;
			sHtml += "<script>m_obj.m_startidx = "+nStart+";</script>";
			String sContextMenuScript = "javascript:if (parent.JLContextMenu_onContextMenuEx != null) return parent.JLContextMenu_onContextMenuEx('a','"+m_sMainFrame+"',window);";
			sHtml += "<table style=\"cursor: hand;\" border=\"0\" cellPadding=\"0\" cellSpacing=\"0\" oncontextmenu=\""+sContextMenuScript+"\">"; //
			sHtml += "<tr><td>"; //
			printStream(sHtml);
		}
	}
	
	void printList()
	{
		printListStart();
		Vector vSeg = getSegment();
		printListEx(vSeg);
		printListEnd(m_nCurSegSize);
	}

	void printListEx(Vector vSeg)
	{
		if (vSeg != null)
		{
			Object rcd = null;
			m_nCurSegSize = vSeg.size();
			for (int k=0;k<m_nCurSegSize;k++)
			{
				if (m_bScript && (k != 0))
					printStream(",");
				rcd = (Object)vSeg.elementAt(k);
				if (rcd == null)
					break;
				if (m_bScript)
					printStream(" // "+k);
				printRcd(0,(m_nPageStart*m_nPageSize) + k,rcd);
			}
		}
	}

	void printListEnd(int nCount)
	{
		if (m_bScript)
		{
			int nPageStartIdx = m_nPageStart * m_nPageSize;
			print(");");
			print("parent.parent.JLListCtrl_printList(m_obj,"+nPageStartIdx+","+nCount+");");
			printStream("parent.parent.JLListCtrl_printListPageEnd(m_obj);");
			print("</script>");
		}
		else
		{
			printStream("</td></tr></table>");
			printStream("</div>");
			printStream("</div>");
		}
		printStream("</form> <!-- fparam -->");
	}

	void printCheckboxScript()
	{
		printStream("<script>");
		printStream("if (parent.parent.onCheckbox != null) parent.parent.onCheckbox(m_obj,"+m_nPageStart+");");
		printStream("</script>");
	}
	
	void printCellScript()
	{
	}

	int insertRow()
	{
		debugPrint(m_rRequest);

		int nCount = getIntValue(m_rRequest,"count");
		String sArg[] = new String[nCount];
		for (int i=0;i<nCount;i++)
		{
			sArg[i] = getStrValue(m_rRequest,"_val"+i);
		}
		int nIdx = getIntValue(m_rRequest,"_idx");
		Vector vRow = onInsertRow(nIdx,sArg);
		if (vRow != null)
		{
			printStream("<script>");
			printStream("{ var arg = new Array();");
			int nSize = vRow.size();
			for (int i=0;i<nSize;i++)
			{
				String sVal = (String)vRow.elementAt(i);
				if (JLString.IsEmpty(sVal))
					sVal = "";
				printStream("arg["+i+"] = \""+sVal+"\";");
			}
			// 이것은 container에서 submit됩니다. 따라서, parent가 하나가 붙습니다.
			printStream("if (parent.JLGridCtrl_onFinishInsert != null) {  parent.JLGridCtrl_onFinishInsert(m_obj,arg); } ");
			printStream("} ");
			printStream("</script>");
		}
		return 0;
	}

	Vector onInsertRow(int nIdx,String sArg[])
	{
		return null;
	}

	int updateRow()
	{
		debugPrint(m_rRequest);

		int nCount = getIntValue(m_rRequest,"count");
		String sArg[] = new String[nCount];
		for (int i=0;i<nCount;i++)
		{
			sArg[i] = getStrValue(m_rRequest,"_val"+i);
		}
		int nIdx = getIntValue(m_rRequest,"_idx");
		Vector vRow = onUpdateRow(nIdx,sArg);
		if (vRow != null)
		{
			printStream("<script>");
			printStream("{ var arg = new Array();");
			int nSize = vRow.size();
			for (int i=0;i<nSize;i++)
			{
				String sVal = (String)vRow.elementAt(i);
				if (JLString.IsEmpty(sVal))
					sVal = "";
				printStream("arg["+i+"] = \""+sVal+"\";");
			}
			// 이것은 container에서 submit됩니다. 따라서, parent가 하나가 붙습니다.
			printStream("if (parent.JLGridCtrl_onFinishUpdate != null) {  parent.JLGridCtrl_onFinishUpdate(m_obj,arg); } ");
			printStream("} ");
			printStream("</script>");
		}
		return 0;
	}

	Vector onUpdateRow(int nIdx,String sArg[])
	{
		return null;
	}

	int deleteRow()
	{
		debugPrint(m_rRequest);

		int nCount = getIntValue(m_rRequest,"count");
		String sArg[] = new String[nCount];
		for (int i=0;i<nCount;i++)
		{
			sArg[i] = getStrValue(m_rRequest,"_val"+i);
		}
		int nIdx = getIntValue(m_rRequest,"_idx");
		Vector vRow = onDeleteRow(nIdx,sArg);
		if (vRow != null)
		{
			printStream("<script>");
			printStream("{ var arg = new Array();");
			int nSize = vRow.size();
			for (int i=0;i<nSize;i++)
			{
				String sVal = (String)vRow.elementAt(i);
				if (JLString.IsEmpty(sVal))
					sVal = "";
				printStream("arg["+i+"] = \""+sVal+"\";");
			}
			// 이것은 container에서 submit됩니다. 따라서, parent가 하나가 붙습니다.
			printStream("if (parent.JLGridCtrl_onFinishDelete != null) {  parent.JLGridCtrl_onFinishDelete(m_obj,arg); } ");
			printStream("} ");
			printStream("</script>");
		}
		return 0;
	}

	Vector onDeleteRow(int nIdx,String sArg[])
	{
		return null;
	}

	String onGetClickAction(int nRow,int nCol,Vector vRcd)
	{
		return null;
	}
}
%>
