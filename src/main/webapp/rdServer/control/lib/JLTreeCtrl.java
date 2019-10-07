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

<!-- JLTreeCtrl.java : start -->

<%!
public class JLTreeCtrl extends JLHttp
{
	int m_nTreeIdx = 0;
	int m_nSpan = 0;
	int m_nButtonTextCount = 0;
	JLRcd m_rMeta;
	String m_sCodeHeader;
	int m_nDebug;

	JLTreeCtrl(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		m_nTreeIdx = 0;
		m_nSpan = 0;
		m_nButtonTextCount = 0;
		m_rMeta = new JLRcd();
		m_sCodeHeader = "";
		m_nDebug = 0;
	}
	
	void setCodeHeader(String sHeader)
	{
		m_sCodeHeader = sHeader;
	}

	void setDebug(int nDebug)
	{
		m_nDebug = nDebug;
	}

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
		//String m_sCharSet = "EUC-KR";
		//String m_sContentType = "text/html; charset="+m_sCharSet;
      String m_sContentType = m2soft.rdsystem.server.core.install.Message.getcontentType();
      //System.out.println(m_sContentType);
		sHtml += "<"+"%@ page language=\"java\" import=\"java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File \" %"+">\n";
		sHtml += "<"+"%@ page contentType=\""+m_sContentType+"\" %"+">\n";
		if (!JLString.IsEmpty(m_sCodeHeader))
		{
			sHtml += m_sCodeHeader;
			sHtml += "\n";
		}
		if (!JLString.IsEmpty(m_sClassName))
		{
			if (!m_sClassName.equalsIgnoreCase("JLGridCtrl"))
				sHtml += "<"+"%@ include file=\""+sPath+sClassNameExt+"\" %"+">\n";
		}

		sHtml += "<"+"%@ include file=\""+sClassPath+"JLJsp.jsp\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLObj.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLRuntimeClass.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLHttp.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLTreeCtrl.java\" %"+">\n";
		sHtml += "<"+"%@ include file=\""+sClassPath+"JLContextMenu.java\" %"+">\n";

		sHtml += "<body bgcolor=\"#ffffff\" text=\"#000000\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n";
		sHtml += "<"+"%\n";
		sHtml += "initArg(request,out);\n";
		sHtml += m_sClassName+" ctrl = new "+m_sClassName+"(out);\n";
		sHtml += "ctrl.setParam(m_param);\n";
		sHtml += "int nCmd = getIntValue(m_param,\"_cmd\");\n";
		sHtml += "if (nCmd == 0) // getSegment\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.printData(m_param);\n";
		sHtml += "} \n";
		sHtml += "if (nCmd == 1) // insertRow\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.insertNode();\n";
		sHtml += "} \n";
		sHtml += "if (nCmd == 2) // updateRow\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.updateNode();\n";
		sHtml += "} \n";
		sHtml += "if (nCmd == 3) // deleteRow\n";
		sHtml += "{ \n";
		sHtml += "	ctrl.deleteNode();\n";
		sHtml += "} \n";
		sHtml += "%"+">\n";
		sHtml += "</body>";
		return sHtml;
	}
	
	void init(String sName)
	{
		setName(sName);
		printObj();
	}

	void printObj()
	{
		String sUrl = createInstance(m_sHome,m_sPath,m_sClassName);
		printStream("<form name=\""+m_sName+"\" action=\""+sUrl+"\">");
		printStream("<input type=\"hidden\" name=\"_argcount\" value=\"\">");
		for (int i=0;i<16;i++)
		{
			printStream("<input type=\"hidden\" name=\"_arg"+i+"\" value=\"\">");
		}
		printStream("<input type=\"hidden\" name=\"_cmd\" value=\"\">");
		printStream("<input type=\"hidden\" name=\"_name\" value=\""+m_sName+"\">");
		printStream("<input type=\"hidden\" name=\"_debug\" value=\""+m_nDebug+"\">");
		printStream("<input type=\"hidden\" name=\"treeidx\" value=\"\">");
		printStream("<input type=\"hidden\" name=\"idx\" value=\"\">");
		printStream("<input type=\"hidden\" name=\"parentid\" value=\"\">");
		printStream("<input type=\"hidden\" name=\"classpath\" value=\"\">");
		printStream("<script> document."+m_sName+".classpath.value = \""+this.insertDQuoteEsc(m_sClassPath)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"path\" value=\"\">");
		printStream("<script> document."+m_sName+".path.value = \""+this.insertDQuoteEsc(m_sPath)+"\"; </script>");
		printStream("<input type=\"hidden\" name=\"_imgpath\" value=\"\">");
		printStream("<script> document."+m_sName+"._imgpath.value = \""+this.insertDQuoteEsc(m_sImgPath)+"\"; </script>");
		printStream("</form>");
		printStream("<IFRAME NAME=\"ftreedata_"+m_sName+"\" frameborder=\"0\" src=\"\" scrolling=no STYLE=\"HEIGHT: 0px; WIDTH: 0px; \"></IFRAME>");
		printStream("<script>");
		printStream("	m_obj.m_oContainer = window;");
		printStream("</script>");
	}

	String getSql(String sArg)
	{
		// select id,parentid,text, (select count(*) cnt from tjobfolder where parentid = p.id) _childcount from tjobfolder p where parentid = 0 order by idx
		// select id,parentid,text,* from table where parentid = 3 order by idx
		// String sSql = "select id,parentid,text, (select count(*) cnt from tjobfolder where parentid = p.id) _childcount from tjobfolder p where parentid = "+sArg+" order by idx";
		return "";
	}

	String getDsn(String sArg)
	{
		String sDsn = "cm";
		return sDsn;
	}

	// db에서 tree node에 대한 정보를 가지고 온다.
	// node : vector
	// tree = vector of vector (tree를 나타내는 Vector의 element가 vector이고, 이 element Vector는 node에 대한 정보를 
	// 가지고 있다.
	// node에 대한 정보는 id, parentid, text, ....
	// 이 node에 대한 정보(Vector)는 getNodeData를 통해 넘겨지고(vNode)
	// 여기 getNodeData에서 String으로 "parentid","id","text","icon","iconexpand","bexpand","sAction","nChildcount"
	// String으로 만들어서, return
	Vector getSegment(String sParent)
	{
		String sSql = getSql(sParent);
		String sDsn = getDsn(sParent);
		if (JLString.IsEmpty(sSql) || JLString.IsEmpty(sDsn))
			return null;
		JLStmt sql = new JLStmt();
		if (sSql == null)
			return null;
		Vector vec = sql.SelectQueryVN(sSql,null,sDsn,1000,m_rMeta);
		if (vec == null)
			return null;
		if (vec.size() <= 0)
			return null;
		return vec;
	}

	// sh : shrink
	// ex : expand
	void printTree(String sParentNode,String parentid)
	{
		Vector folder = getSegment(parentid);
		printTree(folder,sParentNode,parentid);
	}
	
	void printDataStart()
	{
		print("	var vTree = new Array(");
	}

	void printDataEnd()
	{
		print("	);");
	}

	/*
	function _getData()
	{
		// parentid,id,name,icon,expandicon,expand,action,
		var vTree = new Array(
			-1,1,"루트","./images/log16.gif","./images/log16.gif",1,"",
			1,100,"hello1","./images/log16.gif","./images/log16.gif",1,"",
			1,2,"hello2","./images/log16.gif","./images/log16.gif",1,"",
			2,3,"hello3","./images/log16.gif","./images/log16.gif",1,""
		);
		return vTree;
	}
	JLTreeCtrl_addTree(null,_getData(),0);
	*/
	// format : parentid, id, text, icon, iconexpand, bexpand, sAction, nChildcount
	// bExpand : 1 - expand, 0 shrink
	void printData(Vector vTree)
	{
		printDataStart();
		if (vTree != null)
		{
			int nCount = vTree.size();
			String sHtml = "";
			Vector vNode = null;
			for (int i=0;i<nCount;i++)
			{
				vNode = (Vector)vTree.elementAt(i);
				if (vNode == null)
					continue;
				sHtml += getNodeData(vNode);
				if (i < nCount-1)
					sHtml += ",\n";
			}
			print(sHtml);
		}
		printDataEnd();
	}
	
	String getNodeData(Vector vNode)
	{
		if (vNode == null)
			return "";
		String sHtml = "";
		String parentid = (String)vNode.elementAt(m_rMeta.GetIntValue("parentid"));;
		String id = (String)vNode.elementAt(m_rMeta.GetIntValue("id"));
		String sName = (String)vNode.elementAt(m_rMeta.GetIntValue("text"));
		String sChildCount = (String)vNode.elementAt(m_rMeta.GetIntValue("_childcount"));
		String sAction = "";
		int nExpand = 0;
		sHtml += "\""+parentid+"\",\""+id+"\",\""+sName+"\",\"./images/log16.gif\",\"./images/log16.gif\","+nExpand+",\""+this.insertDQuoteEsc(sAction)+"\","+sChildCount+"";
		return sHtml;
	}

	void printData(JLRcd param)
	{
		printStream("<script>");
		String parentid = getStrValue(param,"parentid");
		Vector folder = getSegment(parentid);
		if (folder == null)
			folder = new Vector();
		printData(folder);
	
		printStream("if (parent.JLTreeCtrl_onDataReady != null) parent.JLTreeCtrl_onDataReady("+getStrValue(param,"treeidx")+","+getStrValue(param,"idx")+",vTree);");
		printStream("</script>");
	}

	void printTree(Vector folder,String sParentNode,String nParentID)
	{
		String sHtml = "";
		print("<script language=\"javascript\">");
		print("function JLTreeCtrl_printTree_"+m_sName+"()");
		print("{");
		printData(folder);
		print("var treeidx="+sParentNode+".treeidx;");
		print("JLTreeCtrl_initTree("+sParentNode+",vTree,treeidx);");
		print("JLTreeCtrl_setClass(0,\""+m_sClassName+"\");");
		print("JLTreeCtrl_printTree("+sParentNode+");");
		print("}");
		print("JLTreeCtrl_printTree_"+m_sName+"();");
		print("</script>");
	}

	/*
	sibling : s
	plus , minus : has child
	no child : leaf
	no sibling : ns
	
	icon_plus_s.gif : non leaf, sibling, shrink 
	icon_minus_s.gif : non leaf, sibling, expand
	icon_plus_s.gif : non leaf, sibling, shrink 
	icon_minus_s.gif : non leaf, sibling, expand
	
	icon_vert.gif
	*/
	int insertNode()
	{
		debugPrint(m_rRequest);

		int nCount = getIntValue(m_rRequest,"_argcount");
		String sArg[] = new String[nCount];
		for (int i=0;i<nCount;i++)
		{
			sArg[i] = getStrValue(m_rRequest,"_arg"+i);
		}
		String vRow[] = onInsertNode(sArg);
		if (vRow != null)
		{
			printStream("<script>");
			printStream("{ var arg = new Array();");
			int nSize = vRow.length;
			for (int i=0;i<nSize;i++)
			{
				String sVal = (String)vRow[i];
				if (JLString.IsEmpty(sVal))
					sVal = "";
				printStream("arg["+i+"] = \""+sVal+"\";");
			}
			// 이것은 container에서 submit됩니다. 따라서, parent가 하나가 붙습니다.
			printStream("if (parent.JLTreeCtrl_onFinishInsert != null) {  parent.JLTreeCtrl_onFinishInsert(arg); } ");
			printStream("} ");
			printStream("</script>");
		}
		return 0;
	}

	String[] onInsertNode(String sArg[])
	{
		return null;
	}

	int updateNode()
	{
		debugPrint(m_rRequest);

		int nCount = getIntValue(m_rRequest,"_argcount");
		String sArg[] = new String[nCount];
		for (int i=0;i<nCount;i++)
		{
			sArg[i] = getStrValue(m_rRequest,"_arg"+i);
		}
		String vRow[] = onUpdateNode(sArg);
		if (vRow != null)
		{
			printStream("<script>");
			printStream("{ var arg = new Array();");
			int nSize = vRow.length;
			for (int i=0;i<nSize;i++)
			{
				String sVal = (String)vRow[i];
				if (JLString.IsEmpty(sVal))
					sVal = "";
				printStream("arg["+i+"] = \""+sVal+"\";");
			}
			// 이것은 container에서 submit됩니다. 따라서, parent가 하나가 붙습니다.
			printStream("if (parent.JLTreeCtrl_onFinishUpdate != null) {  parent.JLTreeCtrl_onFinishUpdate(arg); } ");
			printStream("} ");
			printStream("</script>");
		}
		return 0;
	}

	String[] onUpdateNode(String sArg[])
	{
		return null;
	}

	int deleteNode()
	{
		debugPrint(m_rRequest);

		int nCount = getIntValue(m_rRequest,"_argcount");
		String sArg[] = new String[nCount];
		for (int i=0;i<nCount;i++)
		{
			sArg[i] = getStrValue(m_rRequest,"_arg"+i);
		}
		String vRow[] = onDeleteNode(sArg);
		if (vRow != null)
		{
			printStream("<script>");
			printStream("{ var arg = new Array();");
			int nSize = vRow.length;
			for (int i=0;i<nSize;i++)
			{
				String sVal = (String)vRow[i];
				if (JLString.IsEmpty(sVal))
					sVal = "";
				printStream("arg["+i+"] = \""+sVal+"\";");
			}
			// 이것은 container에서 submit됩니다. 따라서, parent가 하나가 붙습니다.
			printStream("if (parent.JLTreeCtrl_onFinishDelete != null) {  parent.JLTreeCtrl_onFinishDelete(arg); } ");
			printStream("} ");
			printStream("</script>");
		}
		return 0;
	}

	String[] onDeleteNode(String sArg[])
	{
		return null;
	}
}
%>

<!-- JLTreeCtrl.java : end -->
