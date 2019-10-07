<%@ page import="java.util.*,java.sql.*,java.lang.*,java.io.*,java.io.File,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.core.rddbagent.jdbc.*" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="dbproperties.h"%>

<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLMultiFrame.java"%>

<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request, out);
	
   //sjs 04.14 get the current status of db encryption
   cdp = CurrentEncStatus.getDbEncStatus();
   CInfo[] pools = RdMs.getJDBCStatus();
   String gname;
   if(pools != null){
      gname=pools[0].get1+"group";
      CurrentEncStatus.setCurrentDbTab(gname);
   } 

   String poolname = m_param.GetStrValue("poolname");
   String url = m_param.GetStrValue("url");
   String opcode = m_param.GetStrValue("opcode");

	if (!RdmrdDBAgent.getUseWasPool() && opcode != null && (opcode.equals("up") || opcode.equals("del"))) {
		for (int i = 0; i < dp.getPairs().length; i++) {
			String name = (String) dp.getPairs()[i].getKey();

			if (name.endsWith(".url")) {
				String poolName = name.substring(0, name
						.lastIndexOf("."));

				if (poolName.equals(poolname)) {
					if (opcode.equals("up")) {
						dp.put(poolName + ".url", url);
						dp.put(poolName + ".user", m_param
								.GetStrValue("user"));
						dp.put(poolName + ".password", m_param
								.GetStrValue("password"));
						dp.put(poolName + ".maxconn", m_param
								.GetStrValue("maxconn"));
					} else {
						dp.remove(poolName + ".url");
						dp.remove(poolName + ".user");
						dp.remove(poolName + ".password");
						dp.remove(poolName + ".maxconn");
					}
					//sjs 04.10
					dp.saveToFile(RDUtil.getDbPropPath(), cdp);
					debugPrint("<body bgcolor="
							+ m_sBtnFace
							+ " text=#000000 leftmargin=15 topmargin=10 marginwidth=0 marginheight=0>");
					debugPrint("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
					break;
				}
			}
		}
	}
	// jwlee 08.12.17 DELETE WAS POOL
	if (RdmrdDBAgent.getUseWasPool() && opcode != null && opcode.equals("del")) {
		int j = 0;
		String poolsName = "";
		
		for (int i = 0; i < dp.getPairs().length; i++) {
			String name = (String) dp.getPairs()[i].getKey();

			if (pools != null) {
				for (; j < pools.length; j++) {	// delete this poolname in poolname list
					if (!poolname.equals(pools[j].get1)) {
						poolsName += " " + pools[j].get1;
					}
				}
			} else {
				out.println("Not found service name Not found service name");
			}
			//  waspools= setting in db.properties
			if (name.startsWith("waspools")) {
				dp.put("waspools", poolsName);
				dp.saveToFile(RDUtil.getDbPropPath(), cdp);
				if(!RdMs.reloadConnectionManager()) {
					debugPrint("<body bgcolor="
							+ m_sBtnFace
							+ " text=#000000 leftmargin=15 topmargin=10 marginwidth=0 marginheight=0>");
					debugPrint("<script>alert('reload Connection Manager Failed')</script>");	
				}
				debugPrint("<body bgcolor="
						+ m_sBtnFace
						+ " text=#000000 leftmargin=15 topmargin=10 marginwidth=0 marginheight=0>");
				debugPrint("<script>alert('Must be servletengine restarted in order for the new value to take effect')</script>");
				break;
			}
		}
	}

	Vector driverclassnames = RdMs.getRegistedDriverClassNames();

	if (url == null)
		url = "";
	String dbkind = "";
	int vsize = driverclassnames.size();
	int inx, subinx;

	String isSelected[] = new String[vsize];

	inx = url.indexOf("jdbc:");
	if (inx != -1) {
		subinx = url.indexOf(":", inx + 5);
		if (subinx != -1)
			dbkind = url.substring(inx + 5, subinx);
	}

	for (int i = 0; i < vsize; i++) {
		if (((String) driverclassnames.elementAt(i)).indexOf(dbkind) != -1)
			isSelected[i] = "selected";
		else
			isSelected[i] = "";
	}
%>
<style>
<%@ include file="icis.css" %>
</style>
<script>
function onup(v) {
 var frm = document.jdbcpool;
 frm.opcode.value='up';
 frm.submit();
}
function ondel() {
 var frm = document.jdbcpool;
 frm.opcode.value='del';
 frm.submit();
}

function hide(){
  window.status=" ";
}

</script>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0" onload='hide();'>
<%
	int btnX = 190;
	JLButton btnadd = new JLButton();
	if (!RdmrdDBAgent.getUseWasPool()) // WAS pool is true
		btnadd.printButton(btnX, 240, "ref2", 105, 3, Message
				.get("jrptjdbcpools_01"), "onup()", "images/edit.gif",
				20);
	btnadd.printButton(btnX + 110, 240, "ref3", 105, 3, Message
			.get("jrptjdbcpools_02"), "ondel()", "images/delete.gif",
			20);
%>
<form method='POST' name='jdbcpool' action='JRptJdbcPools.jsp'>

<table border=0 cellspacing=2>
<tr>
<%
	if (!RdmrdDBAgent.getUseWasPool()) // WAS pool is true
	{
%>
<td width=35%>Driver Classname</td>
<td>
<select name='DriverClassName'>
<%
	out.flush();

		for (int i = 0; i < vsize; i++) {
			out.println("<option " + isSelected[i] + ">"
					+ (String) driverclassnames.elementAt(i)
					+ "</option>");
			out.flush();
		}
%>
</select>
</td></tr>
<tr>
<td>URL</td><td><input type=text name='url' value='<%=url%>' size=40 class='style2'></td></tr>
<tr><td>User</td><td><input type=text name='user' value='<%=m_param.GetStrValue("user")%>' size=20 class='style2'></td></tr>
<tr><td>Password</td><td><input type=text name='password' value='<%=m_param.GetStrValue("password")%>' size=20 class='style2'></td></tr>
<tr><td>Max Connection</td><td><input type=text name='maxconn' value='<%=m_param.GetStrValue("maxconn")%>' size=20 class='style2'></td></tr>
<tr><td height=25>Cached Connection</td><td><font color=blue><%=m_param.GetStrValue("freeconn")%></td></tr>
<%
	} // end of if(!RdmrdDBAgent.getUseWasPool())
%>
<tr><td width=160 height=25><%=Message.get("jrptjdbcpools_03")%></td><td><font color=red><%=RdMs.getTestConnection(poolname, null)%><%
	if (!RdMs.getTestConnection(poolname, null)) {
		out.println("(" + RdMs.getLastErrmsg(poolname) + ")");
	}
%></font></td></tr>
</table>
<input type=hidden name='poolname' value='<%=poolname%>'>
<input type=hidden name='freeconn' value='<%=m_param.GetStrValue("freeconn")%>'>
<input type=hidden name='opcode'>
</form>
