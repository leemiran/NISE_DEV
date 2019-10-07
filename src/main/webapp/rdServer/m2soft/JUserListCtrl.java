<%@ page import="java.util.*,java.sql.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.logger.*" %> 
<%@ include file="properties.h"%>

<script>
	function myclick(id)
	{
		alert(id);
	}
</script>

<%!
	public class JUserListCtrl extends JLListCtrl
	{
		
		JUserListCtrl(javax.servlet.jsp.JspWriter out)
		{
			super(out);
			setClassName("JUserListCtrl");
		}
		
		// get the total 
		int getCountEx()
		{
			System.out.println("getCountEx");
			RdmrdDBAgent agent = null;    //JDBC Connection Manager
			RDJDBCHelper rdhelper = null; //JDBC Record Manager
			String countQuery="select count(*) from scuser";
			
			int cnt = 0;
			try{
				agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
				
				if(rdhelper == null){
				System.out.println("db conneciton error");
				return 0 ;
			}
			
			rdhelper.select(countQuery); //Get the total number of schedule firstly
			rdhelper.execute();
			rdhelper.next();
			cnt = rdhelper.getInt(1);
			
			}catch (Exception e){
			
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
			RdmrdDBAgent agent = null;    //JDBC Connection Manager
			RDJDBCHelper rdhelper = null; //JDBC Record Manager
			
			String sTmp = "select * from scuser order by userid";
			//  getpagesql can convert  sql into another sql  by which the correspoding page can be retrieved.
			String mainQuery= getPageSql(sTmp,nPageCount,nPageStart,nPageSize,"userid");
			// debugPrint(mainQuery);
			// print("<script>alert('"+mainQuery+"');</script>");
			
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
				while(rdhelper.next()){
				//System.out.println(i++);
				Vector vRcd = new Vector();
				if(hangleflag)
				{
					vRcd.addElement(RDUtil.toHangleDecode(rdhelper.getString(1).trim()));
					vRcd.addElement(RDUtil.toHangleDecode(rdhelper.getString(2).trim()));
					vRcd.addElement(RDUtil.toHangleDecode(rdhelper.getString(3).trim()));
					vRcd.addElement(RDUtil.toHangleDecode(rdhelper.getString(4).trim()));
				}
				else
				{
					vRcd.addElement(rdhelper.getString(1).trim());
					vRcd.addElement(rdhelper.getString(2).trim());
					vRcd.addElement(rdhelper.getString(3).trim());
					vRcd.addElement(rdhelper.getString(4).trim());
				}

				vSeg.addElement(vRcd);            	
			}      
			}catch (Exception e){
			
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
			return "myclick('"+nRow+"_"+nCol+"','"+(String)vRcd.elementAt(1)+"')";
		}
	}
%>
