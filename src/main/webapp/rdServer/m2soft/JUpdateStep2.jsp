<%@ page import="java.util.*,java.sql.*,java.lang.*, java.io.*,java.io.File, m2soft.rdsystem.server.log.l.*,m2soft.rdsystem.server.core.rddbagent.util.RDUtil,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.update.*" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSHttp.jsp"%>
<%@ include file="../control/lib/JLHttp.java"%>
<%@ include file="../control/lib/JLFrame.java"%>
<%@ include file="../control/lib/JLListCtrl.java" %>
<%@ include file="../control/lib/JLMultiFrame.java"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>
<%@ include file="../control/lib/JSContextMenu.jsp" %>
<%@ include file="../control/lib/JLContextMenu.java" %>

<%
   initArg(request,out);
   out.println("<body bgcolor="+m_sBtnFace+">");
%>

<style>
<%@ include file="icis.css" %>
</style>

<script language="JavaScript">
   var isIE=document.all?true:false;
   var isNS4=document.layers?true:false;
   var isNS6=!isIE&&document.getElementById?true:false;

   function replaceIt(size,f,per)
   {
      var _n = size;
      var str = '<table> <td width=600><p><img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>'+f+'</p></td></tr></table>   <table><td> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>  <img src=images/hline.gif width='+size+' height=25 hspace=0 vspace=0></td></tr></table>    <table width=609><tr><td width=145><p> <img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>0% </p></td><td width=93> <p>&nbsp;</p></td><td width=188> <p align=center>( '+per+' )</p></td><td width=36><p>&nbsp;</p></td><td width=83> <p>&nbsp;</p> </td><td width=38> <p>100%</p> </td></tr></table>';

      if (isNS4)
      {
         document.disp.document.open();
         document.disp.document.write(str);
         document.disp.document.close();
      }
      if (isIE)
      {
         document.all.disp.innerHTML=str;
      }

      if(isNS6)
      {
         document.getElementById("disp").innerHTML=str;
      }
   }

   function onstart(url,l, t, w, h)
   {
      var windowprops = "location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no" +",left=" + l + ",top=" + t + ",width=" + w + ",height=" + h;
      popup = window.open(url,"MenuPopup",windowprops);
   }
</script>
<body bgcolor="<%=m_sBtnFace%>" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<%
     String checkstep = request.getParameter("checkstep");
     String postmessage = request.getParameter("postmessage");

     if( checkstep.equals("step1") )
     { 
%>
			<table border="0" width="663">
				<tr>
				<td width="27">
	            <p>&nbsp;</p>
				</td>
				<td width="137">
					<p align="center">
					<font size="2">&nbsp;</font>
					</p>
				</td>
				<td width="233">
					<p align="center">
					<font size="2">&nbsp;</font>
					</p>
				</td>
				<td width="248">
					<p align="center">
					<font size="2">&nbsp;</font>
					</p>
				</td>
				</tr>
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>
				<td width="137">
					<p align="center"><font size="2">&nbsp;</font></p>
				</td>
				<td width="233">
					<p align="center">
					<font size="2"><b><%= Message.get("jupdatestep2_01") %></b></font><!-- local version information -->
					</p>
				</td>
				<td width="248">
					<p align="center">
					<font size="2"><b><%= Message.get("jupdatestep2_02") %></b></font><!-- remote version information -->
					</p>
				</td>
				</tr>
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>
				<td width="626" colspan="3">
					<hr>
				</td>
				</tr>
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>

				<td width="137">
					<p align="center">
					<font size="2"><b><%= Message.get("jupdatestep2_04") %></b></font><!-- local date -->
					</p>
					<p align="center">
					<font size="2"><b><%= Message.get("jupdatestep2_05") %></b></font><!-- local version -->
					</p>
					<p align="center">
					<font size="2"><b><%= Message.get("jupdatestep2_06") %></b></font><!-- local id -->
					</p>
				</td>

				<td width="233">
					<p align="center">
					<font size="2">
					<%
						RDServerUpdateManager ru = null;
						try
						{
							ru = new RDServerUpdateManager();
						} catch (Exception e) {}

						//local information
						LocalInfo li = ru.getLocalInfo();
						if(li.isalive() && li.isjarfileExists())
						{
							String localv = Float.toString( li.getVersion() );
							
							if(localv.length() == 5)
								localv = localv + "00";
							else if(localv.length() == 6)
								localv = localv + "0";
							else if(localv.length() >= 8)
								localv = localv.substring(0,7);  				
							
							String printLocalVerison = localv.substring(localv.length()-3, localv.length()); 
							String majorVersion = li.getMasterVersion();
							printLocalVerison = majorVersion.substring(0,3)+"."+majorVersion.substring(3,majorVersion.length())+"." + printLocalVerison;
							
							out.print( "<p align=center>"+li.getCreateDate()+"</p>" );
							out.print( "<p align=center>"+printLocalVerison+"</p>" );
							out.print( "<p align=center>"+li.getServerID()+"</p>" );
						} else {
							out.print( "<p align=center>"+Message.get("jupdatestep2_11")+"</p>" ); //error
						}
					%>
					</font>
				</td>

				<td width="248">
					<p align="center">
					<font size="2">
					<%
						//remote information
						RemoteInfo ri = ru.getRemoteInfo( li.getLanguage() );

						if(ri.IsAlive() && ri.isSmartUpdateiniExists())
						{
							String remotev = Float.toString( ri.getVersion() );
							
							if(remotev.length() == 5)
								remotev = remotev + "00";
							else if(remotev.length() == 6)
								remotev = remotev + "0";
							else if(remotev.length() >= 8)
								remotev = remotev.substring(0,7);  				
							
							String printRemoteVerison = remotev.substring(remotev.length()-3, remotev.length()); 
							String majorVersion = ri.getMasterVersion();
							printRemoteVerison = majorVersion.substring(0,3)+"."+majorVersion.substring(3,majorVersion.length())+"." + printRemoteVerison;
									
									ru.setUpdateInfo();
							out.print( "<p align=center>"+ri.getCreateDate()+"</p>" );
							out.print( "<p align=center>"+printRemoteVerison+"</p>" );
							out.print( "<p align=center>"+ri.getServerID()+"</p>" );
						} else {
							out.print( "<p align=center>"+Message.get("jupdatestep2_11")+"</p>" );
							//¿¡·¯
						}
					%>
					</font>
				</td>
				</tr>
				<tr>

				<td width="27">
					<p>&nbsp;</p>
				</td>
				<td width="626" colspan="3">
					<hr>
				</td>
				</tr>
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>

				<td width="137">
					<p align="center"><font size="2">&nbsp;</font></p>
					<p align="center">
					<font size="2">
					<%
						if( ru.hasUpdate() )
						{
							if( li.getVersion() < ri.getVersion())
							{
								out.println( "<b>"+Message.get("jupdatestep2_08")+"</b>" );
								//The new version function
							}
						}
					%>
					</font>
					</p>
				</td>
					
				<td width="485" colspan="2">
	            <p align="left">
					<%
						if( ru.hasUpdate() )
						{
							if( li.getVersion() < ri.getVersion())
								out.println( ru.getAppendInfo(ru.getSubjectInfo(li.getThisVersionInfo()),ru.getSubjectInfo(ri.getThisVersionInfo())) );
							else
								out.println( Message.get("jupdatestep2_10") );
								//It is not necessary to update this version
						} else {
							out.println( Message.get(ru.getErrorMessage()) );
						}
					%>
					</p>
				</td>
				</tr>

				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>
				<td width="626" colspan="3">
					<hr>
				</td>				    
				</tr>
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>

				<td width="137">
					<p align="center">
					<font size="2">
					<%
						if( ru.hasUpdate() )
							if( li.getVersion() < ri.getVersion() )
								out.println( "<b>"+Message.get("jupdatestep2_09")+"</b>" );
								// matters that require attention
					%>
					</font>
					</p>
				</td>
				
				<td width="485" colspan="2">
					<p align="center">
					<%
						if( ru.hasUpdate() )
						{
							if( li.getVersion() < ri.getVersion() )
							{
								if (System.getProperty("file.encoding").indexOf("8859") != -1)
									out.println(RDUtil.toHangleDecode(ri.getPreMessage()));
								else
									out.println( ri.getPreMessage() );
							}
						}
					%>
					</p>
				</td>
				</tr>
				
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>
				<td width="626" colspan="3">
					<hr>
				</td>
				</tr>
				<tr>
				<td width="27">
					<p>&nbsp;</p>
				</td>

				<td width="626" colspan="3">
	            <p align="center">
					<%
						if( ru.hasUpdate() )
						{
							if( li.getVersion() < ri.getVersion() )
							{
								out.println( "<form name=login action=JUpdateRecoveryFrame.jsp?mode=update method=post>" );
								JLButton btnadd = new JLButton();
								//The list of received files
								btnadd.printButton(280,410,"envup",170,2, "<font size=2><b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+Message.get("jupdatestep1_05")+"</b></font>" ,"javascript:login.submit();","images/admin_update.gif",20);
								out.println("<input type=hidden name=checkstep value=step2>");
								out.println("<input type=hidden name=postmessage value='"+ri.getPostMessage()+"'>");
								out.println( "</form>" );
							}
						}
					%>
					</p>
				</td>
				</tr>
			</table>
			<p>
<%
		}
		else if( checkstep.equals("step2") )
		{
			out.println("<script>javascript:onstart('http://m2soft.co.kr/report/RDServerUpdate/CreateFileList.asp',0,0,1,1);</script>");
			try
			{
				out.flush();
				Thread.sleep(1000);
			} catch (Exception e) {}
%>

			<hr>
			<div id=disp>

			<table>
				<tr>
				<td width=600>
					<p><img src=images/blank.gif width=70 height=5 hspace=0 vspace=0></p>
				</td>
				</tr>
			</table>
			
			<table>
				<tr>
				<td>
					<img src=images/blank.gif width=70 height=5 hspace=0 vspace=0><img src=images/hline.gif width='2' height=25 hspace=0 vspace=0>
				</td>
				</tr>
			</table>

			<table width=609>
				<tr>
				<td width=145>
					<p><img src=images/blank.gif width=70 height=5 hspace=0 vspace=0>0% </p>
				</td>
				<td width=93> 
					<p>&nbsp;</p></td><td width=188> <p align=center>( 0 / 0 )</p>
				</td>
				<td width=36>
					<p>&nbsp;</p></td><td width=83> <p>&nbsp;</p> 
				</td>
				<td width=38> 
					<p>100%</p> 
				</td>
				</tr>
			</table>

			</div>
			<hr>
		<%
      	RDServerUpdateManager ru = new RDServerUpdateManager();
      	LocalInfo  li = ru.getLocalInfo();
      	RemoteInfo ri = ru.getRemoteInfo(li.getLanguage());
      	ru.setUpdateInfo();

      	if( ru.hasUpdate() )
			{
				int width = 0;
				String file = "", downcompletefile="";
				long p = 500/ru.getTotalFileSize();
				String per = "";
				ru.startUpdateRun(ru);

				while(true)
				{
					p=(ru.getDownFileSize()*500)/ru.getTotalFileSize();
					per = Long.toString(ru.getDownFileSize()) +" byte / "+ Long.toString(ru.getTotalFileSize())+" byte";
					
					// The downloading file
					file = ru.getUpdateDestFileName()+Message.get("RDServerUpdateImpl_hasUpdate_08")+"<img src=images/blank.gif width=50 height=5 hspace=0 vspace=0>";
					out.println("<script>javascript:replaceIt("+ p +",'"+file+"','"+per+"')</script>");
					
					// The downloaded file
					if( downcompletefile.equals(ru.getReturnMessage()) )
						downcompletefile = ru.getReturnMessage();
					else{
						downcompletefile = ru.getReturnMessage();
						width = 410 - downcompletefile.length();
						out.println( downcompletefile +"<img src=images/blank.gif width="+width+" height=5 hspace=0 vspace=0>" );
					}
					
					out.flush();
					
					if(ru.getTotalFileSize() <= ru.getDownFileSize())
						break;
					if(ru.getUpdatComplete())
						break;
					
					try{ Thread.sleep(100);	} catch (Exception e) {}
				}
				
				per = Long.toString(ru.getTotalFileSize()) +" / "+ Long.toString(ru.getTotalFileSize());
				// matters that require attention
				out.println("<script>javascript:replaceIt(500,'"+Message.get("RDServerUpdateImpl_hasUpdate_09")+"','"+per+"')</script>");
				out.println("<hr>");

				if( ru.getUpdateSuccess() )
				{
					if( !li.getSetUpMessage().equals("") )
					{
						out.println( "<p></p>" );
						out.println( "<p></p>" );
						out.println( li.getSetUpMessage() );
					}
					out.println( "<b><p></p>" );
					out.println( "<p></p>" );
					out.println( Message.get("jupdatestep2_12") );	//update successfully!
					out.println( "<p></p>" );
					
									if (System.getProperty("file.encoding").indexOf("8859") != -1)
										out.println(RDUtil.toHangleDecode( ri.getPostMessage() ));
									else
										out.println( ri.getPostMessage() );
									out.println( "<p></p>" );
				} else {
					int temp = ru.getFileNum() - ru.getUpdateFileNum();
					out.println( ru.getFileNum() + Message.get("jupdatestep2_13") +"&nbsp;"+ temp + Message.get("jupdatestep2_14") + "<p>" +Message.get("jupdatestep2_17") );
					//jupdatestep2_13
					//jupdatestep2_14
					//jupdatestep2_17=If you want to know the reason of download failure, please click Error View in menu of Log Management.
				}

			} else {
				out.println( Message.get(ru.getErrorMessage() ) + "</b>");
			}
		%>
			<hr>
<% 
		} else {
			response.sendRedirect("JUpdateMain.jsp");
		}
%>

