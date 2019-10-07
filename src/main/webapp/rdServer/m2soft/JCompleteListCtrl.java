<%@ page import="java.util.*,java.sql.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.logger.*" %> 
<%@ include file="properties.h"%>

<script>

	//call _setContextMenu from JLContextMenu_onContextMenuEx(wnd,id) 
	function _setContextMenu(id,wnd,nLeft,nTop)
	{
		if (wnd == null)
			return;
		JLContextMenu_init(wnd,window,id,200);
		JLContextMenu_addItem(200,"images/button_icon_search.gif","view in detail","onviewdetail()");
		JLContextMenu_addItem(200,"images/button_icon_statistics.gif","registration information","onproperty()");
		JLContextMenu_addItem(200,"images/button_icon_write.gif","modify","onmodify()");
		JLContextMenu_printObj();
	}
	
	function onviewdetail()
	{
		alert('view carefully');
	}

	function onproperty()
	{
		alert('registration information');
	}

	function onmodify()
	{
		alert('modify');
	}

</script>


<script>
	function myclick(id)
	{
		alert(id);
	}
</script>

<%!
	public class JCompleteListCtrl extends JLListCtrl
	{
		
		JCompleteListCtrl(javax.servlet.jsp.JspWriter out)
		{
			super(out);
			setClassName("JCompleteListCtrl");
		}
		
		// get the total 
		int getCountEx()
		{
//			System.out.println("getCountEx");
			RdmrdDBAgent agent = null;    //JDBC Connection Mananger
			RDJDBCHelper rdhelper = null; //JDBC record manager
			String countQuery="select count(*) from schedule";
			
			int cnt = 0;
			try{
				agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
				
				if(rdhelper == null){
				System.out.println("db conneciton error");
				return 0 ;
			}
			
			rdhelper.select(countQuery); //first, get the total number of schedule
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
			RdmrdDBAgent agent = null;    //JDBC connection manager
			RDJDBCHelper rdhelper = null; //JDBC record manager
			
			String sTmp = "select * from schedule order by scid";
			// getpagesql can convert  sql into another sql  by which the correspoding page can be retrieved.
			// 2003.07.30 kokim it will not applied to oracle.
			//String mainQuery= getPageSql(sTmp,nPageCount,nPageStart,nPageSize,"scid");
			String mainQuery= sTmp;
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
				vRcd.addElement(rdhelper.getString(2));
				vRcd.addElement(rdhelper.getString(3));
				vRcd.addElement(rdhelper.getString(5));
				vRcd.addElement(rdhelper.getString(10));
				vRcd.addElement(rdhelper.getString(27));
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
	}
%>
