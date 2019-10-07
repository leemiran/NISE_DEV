
<%!
public class JLSchTree extends JLTreeCtrl
{
	JLSchTree(javax.servlet.jsp.JspWriter out)
	{
		super(out);
		setClassName("JLSchTree");
	}
	
	// get the information of tree node from db
	// node : vector
	// tree = vector of vector (vector is an element of Vector which shows tree, ÀÌ element Vector holds the information of node  
	// about the  information of node is  id, parentid, text, ....
	// about the node information(Vector) is transferred through getNodeData
	// "parentid","id","text","icon","iconexpand","bexpand","sAction","nChildcount" are String type.
	// return  String type
	Vector getSegment(String sParent)
	{
		//System.out.println("getCountEx");
		Vector vSeg = new Vector();
		RdmrdDBAgent agent = null;    //JDBC connection management
		RDJDBCHelper rdhelper = null; //JDBC record management
		
		String sSql = "SELECT   id,name, parents,(SELECT   COUNT(*) cnt FROM      schedule_folder s WHERE   s.parents = p.id) AS childcount FROM      schedule_folder p WHERE   (p.parents = '"+sParent+"')";
		
		int cnt = 0;
		try
		{
			agent = new RdmrdDBAgent(servicename);
			rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
			
			if(rdhelper == null){
			System.out.println("db conneciton error");
			return vSeg ;
			}
			
			rdhelper.select(sSql);
			rdhelper.execute();
			int i = 0;
			while(rdhelper.next()){
			//System.out.println(i++);
			Vector vRcd = new Vector();
			
		
			// Database coonection through using MSSQL
			// in the case of  korean alphabet is breaked
			// the case of setting Hangeul  Incoding, Decoding on the screen of environment configuration
			// Decoding data
			// hangleflag is defined in property.h
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

	// format : parentid, id, text, icon, iconexpand, bexpand, sAction, nChildcount
	// bExpand : 1 - expand, 0 shrink
	String getNodeData(Vector vNode)
	{
		if (vNode == null)
			return "";
		String sHtml = "";
		String parentid = (String)vNode.elementAt(2);
		parentid.trim();
		String id = (String)vNode.elementAt(0);
		id.trim();
		String sName = (String)vNode.elementAt(1);
		sName.trim();
		String sAction = "onclickSch('"+id+"')";
		String sChildCount = (String)vNode.elementAt(3);
		sChildCount.trim();
		int nExpand = 0;
		sHtml += "\""+parentid+"\",\""+id+"\",\""+sName+"\",\"./images/log16.gif\",\"./images/log16.gif\","+nExpand+",\""+this.insertDQuoteEsc(sAction)+"\","+sChildCount+"";
		return sHtml;
	}

	String[] onInsertNode(String sArg[])
	{
		/*
		arg[0] = id;
		arg[1] = text;
		arg[2] = icon;
		arg[3] = iconex;
		arg[4] = expand;
		arg[5] = action;
		arg[6] = childcount;
		arg[7] = parentid;
		arg[7] = parenttreeidx;
		arg[7] = parentnodeidx;
		*/
		if (sArg == null)
			return null;

		String sGuid = JLSystem.GetGUID();
		{		
		
			RdmrdDBAgent agent = null;
			RDJDBCHelper rdhelper = null; 
			
			String schedule = sArg[1];// "ScheduleTest";
			String parents = sArg[7];//"1";
			String sSql = "INSERT INTO schedule_folder (name,parents,guid) VALUES ('"+schedule+"',"+parents+",'"+sGuid+"')";
			debugPrint(sSql);
			//if (true) return;
			
			try
			{
				agent = new RdmrdDBAgent(servicename);
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
				
				if(rdhelper == null){
				   System.out.println("db conneciton error");
				return null;
				}
			
				rdhelper.select(sSql);
				rdhelper.execute();
			}catch (Exception e){
			
			}finally{
			try{
			   if(rdhelper != null)
			      rdhelper.close();
			}catch (Exception e){}
			   agent.disconnect();
			}		
	
	
		}
		String sID = "";
		{		
		
			RdmrdDBAgent agent = null;
			RDJDBCHelper rdhelper = null; 
			
			String sSql = "select id from schedule_folder where guid = '"+sGuid+"'";
			debugPrint(sSql);
			//if (true) return;
			
			try
			{
				agent = new RdmrdDBAgent(servicename);
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
				
				if(rdhelper == null){
				System.out.println("db conneciton error");
				return null;
				}
			
				rdhelper.select(sSql);
				rdhelper.execute();	
				
				while(rdhelper.next())
				{
					sID = rdhelper.getString(1).trim()			;
				}			
				
			}catch (Exception e){
			
			}
			finally{
			try{
			rdhelper.close();
			}catch (Exception e){}
			agent.disconnect();
			}	
		}
		sArg[0] = sID;
		return sArg;
	}


	String[] onUpdateNode(String sArg[])
	{
		/*
		arg[0] = id;
		arg[1] = text;
		arg[2] = parentid;
		*/
		if (sArg == null)
			return null;
		{		
		
			RdmrdDBAgent agent = null;
			RDJDBCHelper rdhelper = null; 
			
			String schedule = sArg[1];// "ScheduleTest";
			// String parents = sArg[7];//"1";
			String sSql = "update schedule_folder set name = '"+schedule+"' where id = " + sArg[0];
			debugPrint(sSql);
			//if (true) return;
			
			try
			{
				agent = new RdmrdDBAgent(servicename);
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
				
				if(rdhelper == null){
				System.out.println("db conneciton error");
				return null;
				}
			
				rdhelper.select(sSql);
				rdhelper.execute();
			}catch (Exception e){
			
			}finally{
			try{
			rdhelper.close();
			}catch (Exception e){}
			agent.disconnect();
			}		
	
	
		}
		return sArg;
	}
	
	String[] onDeleteNode(String sArg[])
	{
		/*
		arg[0] = id;
		arg[1] = text;
		arg[2] = parentid;
		*/
		if (sArg == null)
			return null;

		{		
		
			RdmrdDBAgent agent = null;
			RDJDBCHelper rdhelper = null; 
			
			// String schedule = sArg[1];// "ScheduleTest";
			// String parents = sArg[7];//"1";
			String sSql = "delete schedule_folder  where id = " + sArg[0];
			debugPrint(sSql);
			//if (true) return;
			
			try
			{
				agent = new RdmrdDBAgent(servicename);
				rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);
				
				if(rdhelper == null){
				System.out.println("db conneciton error");
				return null;
				}
			
				rdhelper.select(sSql);
				rdhelper.execute();
			}catch (Exception e){
			
			}finally{
			try{
			rdhelper.close();
			}catch (Exception e){}
			agent.disconnect();
			}		
	
	
		}
		return sArg;
	}
}

%>
