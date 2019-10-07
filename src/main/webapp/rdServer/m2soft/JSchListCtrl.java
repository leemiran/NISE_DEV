<%@ page import="java.util.*,java.sql.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.*,m2soft.rdsystem.server.logger.*" %>
<%@ include file="properties.h"%>

<script>

	function modify_sc(scid,scname)
	{
		var wMainFrame = getAncestor(window,"_mainFrame");
		if (wMainFrame != null)
		{
			var sFrame = "JSchedulelist_info";
			var sSrc = "JSchedulelist_info.jsp?scid="+scid;
			wMainFrame._openFrame("rightframe",sFrame,sSrc);
		}

	}

	function delete_sc(scid,scname)
	{
	   var ret = confirm("<%=Message.get("schedulelist_26")%>");
		if(ret)
		{
			var wMainFrame = getAncestor(window,"_mainFrame");
			if (wMainFrame != null)
			{
				var sFrame = "scheduledel";
				var sSrc = "scheduledel.jsp?scid=" + scid + "&del=yes&scname=" + scname;
				wMainFrame._openFrame("rightframe",sFrame,sSrc);
			}
		}
	}

	function f_openWin(scid,scname)
	{
	   // bgso 2002/10/06 setting the position of pop-up 
      x = 10 +event.clientX;
      y = 210 + event.clientY;

		var addr = "viewscheduledate.jsp?scid=" + scid;
		popUpWin = window.open(addr,'s','toolbar=0,location=no,directories=0,status=0,menubar=no,scrollbars=1,resizable=no,width=200,height=250,left=' + x + ',top=' + y);
		if(navigator.appName == 'Netscape'){
			popUpWin.focus();
		}
	}

	function myclick(id,arg)
	{
	}
</script>

<%!
	public class JSchListCtrl extends JLListCtrl
	{

		String owner="admin";

		javax.servlet.http.HttpSession session = null;
		JSchListCtrl(javax.servlet.jsp.JspWriter out,javax.servlet.http.HttpSession session)
		{
			super(out);
			this.session = session;
			setClassName("JSchListCtrl");
		}

		// get the total
		int getCountEx()
		{
			RdmrdDBAgent agent = null;    //JDBC connection manager
			RDJDBCHelper rdhelper = null; //JDBC record manager

			owner = sessionUser;
			String countQuery ="";

			if(owner != null && !owner.equals("admin"))
				countQuery="select count(*) from schedule where owner = '" + owner +"'";
			else
				countQuery="select count(*) from schedule";

			int cnt = 0;
			try{
				agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);

				if(rdhelper == null){
					System.out.println("db conneciton error");
					return 0 ;
				}

				rdhelper.select(countQuery); //get the total number of socket firstly
				rdhelper.execute();
				rdhelper.next();
				cnt = rdhelper.getInt(1);

			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try{
				rdhelper.close();
				}catch (Exception e){}
				agent.disconnect();
			}
			return cnt;
		}

		// database
		Vector getSegment(int nPageCount,int nPageStart,int nPageSize)
		{
			//System.out.println("getCountEx");
			RdmrdDBAgent agent = null;    //JDBC connection manager
			RDJDBCHelper rdhelper = null; //JDBC record manager

			// scid, scname, reportfilename, ocxhtmname, start_yyyy, start_mm, start_dd,
			// start_hh, launch_type, once_yyyy, once_mm, once_dd, once_hh, once_minute,
			// sdayofweek, sdayofweek_hours, sdayofweek_minute, everymonth_type, everymonth_dd,
			// everymonth_hh, everymonth_minute, everymonth_weekweek, everymonth_dayofweekweek,
			// everymonth_hoursofweekweek, everymonth_minuteofweekweek, sendmail, refto, send_type

         		String sTmp;

			if(!owner.equals("admin"))
		            sTmp = "select * from schedule where owner='"+owner+"' order by scid";
		         else
		            sTmp = "select * from schedule order by scid";

			//getpagesql can convert  sql into another sql  by which the correspoding page can be retrieved.
			// 2003.07.30 kokim it is not suitable for oracle.
			//String mainQuery= getPageSql(sTmp,nPageCount,nPageStart,nPageSize,"scid");
			String mainQuery= sTmp;
			//debugPrint(mainQuery);
			//print("<script>alert('"+mainQuery+"');</script>");

			Vector vSeg = new Vector();

			int cnt = 0;
			try{
				agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);

				if(rdhelper == null){
					System.out.println("db conneciton error");
					return vSeg ;
				}

				rdhelper.select(mainQuery);
				rdhelper.execute();
				int i = 0;

				while(rdhelper.next())
				{

					Vector vRcd = new Vector();

					//  since it can not retrieve the data through getString more than two times in odbc,  insert them into parameters (bgso).
					String data1  = rdhelper.getString(1);

					String data2  = rdhelper.getString(2);
					String data3  = rdhelper.getString(3);
					String data5  = rdhelper.getString(5);
					String data10 = rdhelper.getString(10);
					String data27 = rdhelper.getString(27);

					if(data2 != null)
						vRcd.addElement(data2);
					else
						vRcd.addElement("");

					if(data3 != null)
						vRcd.addElement(data3);
					else
						vRcd.addElement("");

					if(data5 != null)
						vRcd.addElement(data5);
					else
						vRcd.addElement("");

					//vRcd.addElement(rdhelper.getString(10));
					if (data10.equals("once"))
						vRcd.addElement(Message.get("schedulelist_12"));
					else if (data10.equals("everyweek"))
						vRcd.addElement(Message.get("schedulelist_13"));
					else if (data10.equals("everyday"))
						vRcd.addElement(Message.get("schedulelist_14"));
					else if (data10.equals("everytime"))
						vRcd.addElement(Message.get("schedulelist_15"));
               				else if (data10.equals("userDefined"))
                  				vRcd.addElement(Message.get("schedulelist_12"));
					else
						vRcd.addElement("");

					if(data27 != null)
						vRcd.addElement(data27);
					else
						vRcd.addElement("");
					vRcd.addElement(Message.get("schedulelist_25"));
					vRcd.addElement(Message.get("schedulelist_23"));
					vRcd.addElement(Message.get("schedulelist_24"));
					vRcd.addElement(data1);

					vSeg.addElement(vRcd);
				}

			}catch (Exception e){
				e.printStackTrace();
			}finally{
				try{
				rdhelper.close();
				}catch (Exception e){}
				agent.disconnect();
			}

			return vSeg;
		}

		String onGetClickAction(int nRow,int nCol,Vector vRcd)
		{
				if(nCol == 6)
					return "f_openWin('" + (String)vRcd.elementAt(8) + "','"  + (String)vRcd.elementAt(0) +"')";
				else if(nCol == 7)
					return "modify_sc('" + (String)vRcd.elementAt(8) + "','"  + (String)vRcd.elementAt(0) +"')";
				else if(nCol == 8)
					return "delete_sc('" + (String)vRcd.elementAt(8) + "','"  + (String)vRcd.elementAt(0) +"')";
				else
					return "myclick('"+nRow+"_"+nCol+"','"+(String)vRcd.elementAt(1)+"')";
			//	return "myclick('"+nRow+"_"+nCol+"','"+(String)vRcd.elementAt(1)+"')";
		}
	}
%>
