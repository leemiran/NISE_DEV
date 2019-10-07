<%@ page import="java.util.*,java.text.*,java.io.*,m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.logger.*" %>
<%@ page session="false" %>
<%@ include file="session.h"%>
<% String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ include file="../control/lib/JLJsp.jsp"%>
<%@ include file="../control/lib/JLObj.java"%>
<%@ include file="../control/lib/JLRuntimeClass.java"%>
<%@ include file="properties.h"%>
<%@ include file="../control/lib/JSButton.jsp"%>
<%@ include file="../control/lib/JLButton.java"%>

<%
	initArg(request,out);
%>

<%!
    String schedulelist_info_01 = Message.get("schedulelist_info_01");
    String schedulelist_info_02 = Message.get("schedulelist_info_02");
    String schedulelist_info_03 = Message.get("schedulelist_info_03");
%>
<%!
   static final String SAPCE ="";
   RdmrdDBAgent agent = null;
   String scid     = "";
   String selectQuery= "";
   String osname ="";
   String scname ="",
   schedulefile ="",
   ocxtempletename="",
   refparaminx="",
   parameters="",
   start_yyyy="",
   start_mm="",
   start_dd="",
   start_hh="",
   launch_type="",
   once_yyyy="",
   once_mm="",
   once_dd="",
   once_hh="",
   once_minute="",
   dayofweek="",
   dayofweek_hours="",
   dayofweek_minute="",
   everymonth_type="",
   everymonth_dd="",
   everymonth_hh="",
   everymonth_minute="",
   everymonth_weekweek="",
   everymonth_dayofweekweek="",
   everymonth_hoursofweekweek="",
   everymonth_minuteofweekweek="",
   sendmail="",
   maillist="",
   refmaillist="",
   send_type ="",
   rowcount = "0";
   //yyyymmddhhsstt StringVector
   Vector dateString;
   Calendar rcalendar = new GregorianCalendar();
   Calendar startdate = new GregorianCalendar();
   String scheduleDate = null;
   RdMakeSchedule rdDateClass = new RdMakeSchedule();
   RDJDBCHelper rdhelper;

   //sjs 05.27
   StringTokenizer period, jsppathname;
   String eachPeriod ="";

%>
<%
   try{

	osname = System.getProperty("os.name").toLowerCase();
	rowcount = request.getParameter("rowcount"); //Page NO
	scid = request.getParameter("scid");

      agent = new RdmrdDBAgent(servicename,RdLogManager.getInstance().getScheduleLog());
      rdhelper = agent.createHelper(RdmrdDBAgent.HELPER_PREPARED_STATEMENT);

      if(rdhelper == null){
         out.println(schedulelist_info_01);
         return;
      }

      String drive = rdhelper.getDriverName().toLowerCase();
      if(drive.indexOf("db2") != -1 || drive.indexOf("as400") != -1 || drive.indexOf("as/400") != -1)
         selectQuery = schedulelist_info_02+scid+"'";
	   else if(drive.indexOf("odbc") != -1)
		   selectQuery = "select * from schedule where scid="+scid;
      else
         selectQuery = schedulelist_info_03+scid+"'";

      rdhelper.select(selectQuery);

      rdhelper.execute();
      rdhelper.next();

      parameters = rdhelper.getString("parameters");
      if(parameters == null)
         parameters ="";
      scname = rdhelper.getString("scname");

%>
<%
//sjs 05.27
String launch_userDefined="",
       jsppath="",
       definedPeriodIndex ="";

String launch_check_once="",
       launch_check_everyweek="",
       launch_check_everymonth="",
       launch_check_everyday="",
       launch_check_everytime="";


String everymonth_type_day="";
String everymonth_type_month="";

String once_year="";
String once_month="";
String once_day="";
String once_hour="";
String once_minute="";
String week_day="";
String week_hour="";
String week_minute="";
String month_day_day="";
String month_day_hour="";
String month_day_minute="";
String month_month_week="";
String month_month_day="";
String month_month_hour="";
String month_month_minute="";

String sendmail_check="";
String sendmailno_check="";
String sendmail="";
String mailto="";
String refto="";
String send_type="";
String send_type_html_check="";
String send_type_url_check="",
	syear ="",
	smonth ="",
	sday="",
	shour="",
	eyear="",
	emonth="",
	eday="",
	ehour="",
	emin="";


if((rdhelper.getString("launch_type")).equals("once") ) {
   launch_check_once = "checked";
   once_year = rdhelper.getString("once_yyyy").trim();
   once_month = rdhelper.getString("once_mm").trim();
   once_day = rdhelper.getString("once_dd").trim();
   once_hour = rdhelper.getString("once_hh").trim();
   once_minute = rdhelper.getString("once_minute").trim();

}else if((rdhelper.getString("launch_type")).equals("everyweek") ) {
   launch_check_everyweek="checked";
   week_day = rdhelper.getString("sdayofweek").trim();
   week_hour = rdhelper.getString("sdayofweek_hours").trim();
   week_minute = rdhelper.getString("sdayofweek_minute").trim();
}else if((rdhelper.getString("launch_type")).equals("everymonth") ) {
   launch_check_everymonth ="checked";
   if( (rdhelper.getString("everymonth_type").trim()).equals("day")) {
      everymonth_type_day = "checked";
      month_day_day = rdhelper.getString("everymonth_dd").trim();
      month_day_hour = rdhelper.getString("everymonth_hh").trim();
      month_day_minute = rdhelper.getString("everymonth_minute").trim();
   }else if( (rdhelper.getString("everymonth_type")).equals("month")) {
      everymonth_type_month = "checked";
      month_month_week = rdhelper.getString("everymonth_weekweek").trim();
      month_month_day = rdhelper.getString("everymonth_dayofweekweek").trim();
      month_month_hour = rdhelper.getString("everymonth_hoursofweekweek").trim();
      month_month_minute = rdhelper.getString("everymonth_minuteofweekweek").trim();
   }
}else if((rdhelper.getString("launch_type")).equals("everyday") ) {
   launch_check_everyday ="checked";

   syear = rdhelper.getString("start_yyyy").trim();
   smonth = rdhelper.getString("start_mm").trim();
   sday = rdhelper.getString("start_dd").trim();
   eyear = rdhelper.getString("once_yyyy").trim();
   emonth = rdhelper.getString("once_mm").trim();
   eday = rdhelper.getString("once_dd").trim();
   ehour = rdhelper.getString("start_hh").trim();
   emin = rdhelper.getString("once_minute").trim();

}else if((rdhelper.getString("launch_type")).equals("everytime") ) {
   launch_check_everytime ="checked";

   syear = rdhelper.getString("start_yyyy").trim();
   smonth = rdhelper.getString("start_mm").trim();
   sday = rdhelper.getString("start_dd").trim();
   shour = rdhelper.getString("start_hh").trim();
   eyear = rdhelper.getString("once_yyyy").trim();
   emonth = rdhelper.getString("once_mm").trim();
   eday = rdhelper.getString("once_dd").trim();
   ehour = rdhelper.getString("once_hh").trim();
   emin = rdhelper.getString("once_minute").trim();

}else if((rdhelper.getString("launch_type")).equals("userDefined") ) {
   try
   {
     if (rp.getProperty("server.definedPeriod","notDefined").equals("notDefined"))
      {
         %><script>alert("<%=Message.get("scheduleadd_110")%>");history.back();</script><%
      }

      launch_userDefined ="checked";
      jsppath = rdhelper.getString("refto").trim();
      definedPeriodIndex = rdhelper.getString("everymonth_dd").trim();
   }catch (Exception e)
   {
      System.out.println("Error has been occured!");
      e.printStackTrace();
   }
}

sendmail = rdhelper.getString("sendmail").trim();
send_type = rdhelper.getString("send_type").trim();

if(sendmail.equals("yes")){
   sendmail_check ="checked";
   mailto = rdhelper.getString("mailto");
   refto = rdhelper.getString("refto");
   if(refto == null)
      refto ="";
}
else
   sendmailno_check="checked";

   if(send_type.equals("TEXT"))
      send_type_url_check = "checked";
   else
      send_type_html_check = "checked";
%>

<html>
<head>
<title><%= Message.get("schedulelist_info_04") %></title>

<style type="text/css">
<%@ include file="addschedule.css" %>
</style>

<script language="JavaScript">
function OnClickDriverList()
{
	var strParam;
	strParam = document.scform.parameters.value;
	//alert(strParam.length());
	if(document.scform.chkserverprint.checked){
		if(document.scform.driverlist.value != "false"){
			// /rserverprn /rpdrv check whether [driver name] does exist
			if(strParam.indexOf("/rserverprn") == -1){
				document.scform.parameters.value = document.scform.parameters.value + " /rserverprn /rpdrv [" + document.scform.driverlist.value +"]";
			}else{
				// 
				var b,e;
				b = strParam.indexOf("/rserverprn");
				b = strParam.indexOf("[", b);
				e = strParam.indexOf("]", b);

				var str1, str2;
				str1 = strParam.substring(0,b+1);
				str2 = strParam.substring(e);
				document.scform.parameters.value = str1 + document.scform.driverlist.value + str2;
			}
		}else{
			// b=start opsition of print option e=final opsition of print option
			var b,e;
			b = strParam.indexOf("/rserverprn");
			e = strParam.indexOf("]", b);

			var str1, str2;
			str1 = strParam.substring(0,b);
			str2 = strParam.substring(e+1);
			document.scform.parameters.value = str1 + str2;
		}
	}else{
		if(strParam.indexOf("/rserverprn") != -1){
			// b=start opsition of print option e=final opsition of print option
			var b,e;
			b = strParam.indexOf("/rserverprn");
			e = strParam.indexOf("]", b);

			var str1, str2;
			str1 = strParam.substring(0,b);
			str2 = strParam.substring(e+1);
			document.scform.parameters.value = str1 + str2;
		}
	}
}

<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}

function selectList(frmname, frm) {
   var newWin = window.open("JManageAddress.jsp?opcode=5&targetlist=" + frmname + "&list=" + frm.value, "NewWindow",'Width=400,Height=350,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=1');
   self.name = "MainWindow";
   newWin.opener = self;
   newWin.focus();
}

function numchk(num) {
      var str = "1234567890";
      for (i=0; i< num.length; i++){
         numcheck = num.charAt(i);
         for ( j = 0 ;  j < str.length ; j++){
            if (numcheck == str.charAt(j)) break;
            if (j+1 == str.length) return false;
         }
        }
        return true;
}



function numchk(num) {
      var str = "1234567890";
      for (i=0; i< num.length; i++){
         numcheck = num.charAt(i);
         for ( j = 0 ;  j < str.length ; j++){
            if (numcheck == str.charAt(j)) break;
            if (j+1 == str.length) return false;
         }
        }
        return true;
}

function Formclear(){
   var frm = document.scform;
   frm.reset();
}

function CheckInput() {
   var frm = document.scform;

   if(!frm.schedulejob_name.value){
      alert(document.setmessage.scheduleList_info_javascript_01.value);
      frm.schedulejob_name.focus();
      return;
   }

   if(!frm.schedulefile.value){
      alert(document.setmessage.scheduleList_info_javascript_02.value);
      frm.schedulefile.focus();
      return;
   }

   if(!frm.ocxtempletename.value){
      alert(document.setmessage.scheduleList_info_javascript_03.value);
      frm.ocxtempletename.focus();
      return;
   }


   if(!frm.start_yyyy.value || !numchk(frm.start_yyyy.value) || (frm.start_yyyy.value.length != 4)){
      alert(document.setmessage.scheduleList_info_javascript_04.value);
      frm.start_yyyy.focus();
      return;
   }


   if(!frm.start_mm.value || !numchk(frm.start_mm.value)){
      alert(document.setmessage.scheduleList_info_javascript_05.value);
      alert(frm.start_mm.value);
      frm.start_mm.focus();
      return;
   }

   if(!frm.start_dd.value || !numchk(frm.start_dd.value)){
      alert(document.setmessage.scheduleList_info_javascript_06.value);
      frm.start_dd.focus();
      return;
   }

   var launch_type = "";

   for (var i = 0 ; i < frm.launch_type.length ; i++){
         if (frm.launch_type[i].checked == true) {
            launch_type = frm.launch_type[i].value; }
   }

   switch (launch_type) {
      case "once":
         if( (!frm.once_yyyy.value || (frm.once_yyyy.value.length != 4)) || (!frm.once_mm.value) || (!frm.once_dd.value) || (!frm.once_hh.value)
         || !numchk(frm.once_yyyy.value) || !numchk(frm.once_mm.value) || !numchk(frm.once_dd.value) || !numchk(frm.once_hh.value) ){
            alert(document.setmessage.scheduleList_info_javascript_07.value);
            frm.once_yyyy.focus();
            return;
         }

         break;

      case "everyweek" :
         if(!frm.dayofweek.value) {
            alert(document.setmessage.scheduleList_info_javascript_08.value);
            frm.dayofweek.focus();
         return;
         }

         if(!frm.dayofweek_hours.value){
            alert(document.setmessage.scheduleList_info_javascript_09.value);
            frm.dayofweek_hours.focus();
            return;
         }

         break;

         case "everymonth" :
            var everymonth_type ="";
            for (var i = 0 ; i < frm.everymonth_type.length ; i++){
               if (frm.everymonth_type[i].checked == true) {
                  everymonth_type = frm.everymonth_type[i].value; }
            }

            if(everymonth_type == "day"){
               if(!frm.everymonth_dd.value || !numchk(frm.everymonth_dd.value) || !numchk(frm.everymonth_hh.value)){
                  alert(document.setmessage.scheduleList_info_javascript_10.value);
                  frm.everymonth_dd.focus();
                  return;
               }


            } else if(everymonth_type == "month") {
               if(!frm.everymonth_weekweek.value){
                  alert(document.setmessage.scheduleList_info_javascript_11.value);
                  frm.everymonth_weekweek.focus();
                  return;
               }

               if(!frm.everymonth_dayofweekweek.value){
                  alert(document.setmessage.scheduleList_info_javascript_12.value);
                  frm.everymonth_dayofweekweek.focus();
                  return;
               }

            }

         break;

         case "everyday" :
          if(!frm.syear.value || !numchk(frm.syear.value) || !numchk(frm.smonth.value) || !numchk(frm.sday.value) || !numchk(frm.eyear.value) || !numchk(frm.emonth.value) || !numchk(frm.eday.value) || !numchk(frm.ehour.value)){
               alert(document.setmessage.scheduleList_info_javascript_13.value);
               frm.syear.focus();
               return;
            }
         break;

         case "userDefined" :
         /**
         if(!frm.jsppath.value || frm.jsppath.value == "undefined"){
            alert(document.setmessage.scheduleadd_111.value);
            frm.jsppath.focus();
            return;
         }
         **/
         break;
  }


   var mailto = "";
   for(i=0;i<frm.sendmail.length; i++){
      if(frm.sendmail[i].checked == true){
         mailto = frm.sendmail[i].value;
      }
   }


   if((mailto == "yes") && (!frm.maillist.value)){
      alert(document.setmessage.scheduleList_info_javascript_14.value);
      frm.maillist.focus();
      return;
   }

	//bgso 2002/10/09
   if(!(!frm.ocxtempletename.value)){
	   var ocxtempletenameCheck1=frm.ocxtempletename.value
		var ocxtempletenameCheck2=ocxtempletenameCheck1.indexOf(".");
		if(!(ocxtempletenameCheck2==-1)){
			alert("The style of file name is wrong (it can not include .)");
			frm.ocxtempletename.focus();
      	return;
		}
	}

   frm.action="schedulemod.jsp?scid=<%=scid%>";
   frm.submit();

}

//sjs 05.24
function showSchedulingList() {
   var jsp = document.scform.jsppath.value;
   window.open("JManageScheduling.jsp?currentjsp="+ jsp, "NewWindow",'Width=200,Height=300,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=1');
}

//sjs 05.24
function setJspPath()
{
   document.scform.jsppath.value = jspnames[document.scform.definedPeriod.options.selectedIndex];
   document.scform.definedPeriodIndex.value =  document.scform.definedPeriod.options.selectedIndex;
   document.scform.launch_type[5].checked = true;
}
//sjs 05.24
function setSelectBox()
{
   document.scform.definedPeriod.options.selectedIndex = "<%=definedPeriodIndex%>";
   document.scform.definedPeriodIndex.value = "<%=definedPeriodIndex%>";
}

function delSchedule(){

   var ret = confirm(document.setmessage.scheduleList_info_javascript_15.value);
   if(ret)  {
      var action = "scheduledel.jsp?scid=<%=scid%>&del=yes&scname=<%=scname%>";
      window.open(action,window.name);
   }
}

-->
</script>

</head>
<%if (launch_userDefined.equals("checked"))
{%>
<body bgcolor="#ffffff" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0" onLoad="javascript:setSelectBox();">
<%}
else
{%>
<body bgcolor="#ffffff" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">
<%}%>

<form name="setmessage">
	<input type="hidden" name="scheduleList_info_javascript_01" value=<%= Message.get("scheduleList_info_javascript_01") %> >
	<input type="hidden" name="scheduleList_info_javascript_02" value=<%= Message.get("scheduleList_info_javascript_02") %> >
	<input type="hidden" name="scheduleList_info_javascript_03" value=<%= Message.get("scheduleList_info_javascript_03") %> >
	<input type="hidden" name="scheduleList_info_javascript_04" value=<%= Message.get("scheduleList_info_javascript_04") %> >
	<input type="hidden" name="scheduleList_info_javascript_05" value=<%= Message.get("scheduleList_info_javascript_05") %> >
	<input type="hidden" name="scheduleList_info_javascript_06" value=<%= Message.get("scheduleList_info_javascript_06") %> >
	<input type="hidden" name="scheduleList_info_javascript_07" value=<%= Message.get("scheduleList_info_javascript_07") %> >
	<input type="hidden" name="scheduleList_info_javascript_08" value=<%= Message.get("scheduleList_info_javascript_08") %> >
	<input type="hidden" name="scheduleList_info_javascript_09" value=<%= Message.get("scheduleList_info_javascript_09") %> >
	<input type="hidden" name="scheduleList_info_javascript_10" value=<%= Message.get("scheduleList_info_javascript_10") %> >
	<input type="hidden" name="scheduleList_info_javascript_11" value=<%= Message.get("scheduleList_info_javascript_11") %> >
	<input type="hidden" name="scheduleList_info_javascript_12" value=<%= Message.get("scheduleList_info_javascript_12") %> >
	<input type="hidden" name="scheduleList_info_javascript_13" value=<%= Message.get("scheduleList_info_javascript_13") %> >
	<input type="hidden" name="scheduleList_info_javascript_14" value=<%= Message.get("scheduleList_info_javascript_14") %> >
	<input type="hidden" name="scheduleList_info_javascript_15" value=<%= Message.get("scheduleList_info_javascript_15") %> >
   <input type="hidden" name="scheduleadd_111" value="<%= Message.get("scheduleadd_111") %>" >
</form>

<form name="scform" method="post">
<input type="hidden" name="owner" value="<%=sessionUser%>" >
<table border="0" width=100% cellpadding=1 cellspacing=0>
   <td height="25" align="left" bgcolor="<%=m_sBtnFace%>">
		<font color="white"><b><%= Message.get("schedulelist_info_05") %></b></font>
	</td>
</table>

<table border="0" width="70%" cellpadding=2 cellspacing=0>
<tr>
	<td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("schedulelist_info_06")%>&nbsp;<input type="text" name="schedulejob_name" size="22" maxlength="16"   value="<%=scname%>" class="style1">
	</td>
</tr>

<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_22") %><br>
      &nbsp;<input type="readonly" name="schedulefile" size="100" values="123" class="style1" enable=false value="<%=rdhelper.getString("reportfilename")%>" ><br>
      &nbsp;<iframe frameborder="0" height="200" scrolling="auto" src="JDocAdd.jsp?add=yes" width="640" name="body" STYLE="border-style:solid;border-width:1;"></iframe><br>
   </td>
</tr>
<tr>

<td colspan=2><img src="images/color_25_006699.gif" hspace=2><%= Message.get("schedulelist_info_08") %>&nbsp;
        <input type="text" name="parameters" size="75" maxlength=100 values="" class="style1" value="<%=parameters%>"><br>
	      <%-- Windows OS--%>
	      <%if(osname.indexOf("window") != -1) { %>
	      	<% if(parameters.indexOf("/rserver") != -1) { %>
			      &nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="chkserverprint" size="20" onClick="OnClickDriverList()" checked><%= Message.get("schedulelist_info_09") %>
			   <% }else{ %>
			   	&nbsp;&nbsp;&nbsp;&nbsp;<input type="checkbox" name="chkserverprint" size="20" onClick="OnClickDriverList()" unchecked><%= Message.get("schedulelist_info_10") %>
			   <% } %>
		      <select name="driverlist" onChange="OnClickDriverList()">
	         	<option value="false" selected><%= Message.get("schedulelist_info_11") %></option>
		         <%
                  String prnlist = new String(new m2soft.rdsystem.server.core.rddbagent.util.WinSystemPrinter().getSystemPrinters());
		         	StringTokenizer st = new StringTokenizer(prnlist,",");
		         	while (st.hasMoreTokens()) {
		         		String tmp = st.nextToken();
		         %>
		         	   <option value="<%=tmp%>"><%=tmp%></option>
		         <%
		        		}
				   %>
		      </select>
				<br><br>
			<%}%>
			<%-- Windows OS --%>
      </td>
</tr>

<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("schedulelist_info_12") %>&nbsp;
      <%=datapath%>/

      <input type="text" name="ocxtempletename" size="30" maxlength=60 value="<%=rdhelper.getString("ocxhtmname")%>" class="style1">
      (<%=Message.get("JScheduleAdd.refparaminxmsg") %>:<input type="text" name="refparaminx" size="2" maxlength=5 value="<%=rdhelper.getString("refparaminx")%>" class="style1">)

      <br>
   </td>
</tr>

<td colspan=2><img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_29") %>


<input type="text" name="start_yyyy" size = 4 maxlength=4   value="<%=rdhelper.getString("start_yyyy").trim()%>" class="style1"><%= Message.get("schedulelist_info_15") %>&nbsp;
<input type="text" name="start_mm"  size=2 maxlength=3   value="<%=rdhelper.getString("start_mm").trim()%>" class="style1"><%= Message.get("schedulelist_info_16") %>&nbsp;
<input type="text" name="start_dd"  size=2 maxlength=3   value="<%=rdhelper.getString("start_dd").trim()%>" class="style1"><%= Message.get("schedulelist_info_17") %>&nbsp;
<input type="text" name="start_hh"  size=2 maxlength=3   value="<%=rdhelper.getString("start_hh").trim()%>" class="style1"><%= Message.get("schedulelist_info_18") %>
<br><br>
</td>
<tr>
<td colspan=2><img src="images/color_25_006699.gif" hspace=2 nobr><%= Message.get("schedulelist_info_19") %></td>
<tr>
<td><input type="radio" value="once" name="launch_type" <%=launch_check_once%>><%= Message.get("schedulelist_info_20") %></td>
<td>
<%= Message.get("schedulelist_info_21") %>
&nbsp;
        <input type="text" name="once_yyyy" size=4 maxlength=4 class="style1" value="<%=once_year%>">
        <%= Message.get("schedulelist_info_22") %>
&nbsp;
        <input type="text" name="once_mm"  size=2 maxlength=2 class="style1"  value="<%=once_month%>">
        <%= Message.get("schedulelist_info_23") %>
&nbsp;
        <input type="text" name="once_dd"  size=2 maxlength=2  value="<%=once_day%>" class="style1">
        <%= Message.get("schedulelist_info_24") %>
&nbsp;
        <input type="text" name="once_hh"  size=2 maxlength=2  value="<%=once_hour%>" class="style1">
        <%= Message.get("schedulelist_info_25") %>
&nbsp;
        <input type="text" name="once_minute"  size=2 maxlength=2 value="<%=once_minute%>" class="style1">
        <%= Message.get("schedulelist_info_26") %>
</td>
<tr>
<td><input type="radio" value="everyweek" name="launch_type" <%=launch_check_everyweek%>><%= Message.get("schedulelist_info_27") %></td>
<td>
<select name="dayofweek">
       <option><%= Message.get("schedulelist_info_28") %></option>
          <option value="2" <%if(week_day.equals("2")) out.print("selected");%>><%= Message.get("schedulelist_info_29") %></option>
          <option value="3" <%if(week_day.equals("3")) out.print("selected");%>><%= Message.get("schedulelist_info_30") %></option>
          <option value="4" <%if(week_day.equals("4")) out.print("selected");%>><%= Message.get("schedulelist_info_31") %></option>
          <option value="5" <%if(week_day.equals("5")) out.print("selected");%>><%= Message.get("schedulelist_info_32") %></option>
          <option value="6" <%if(week_day.equals("6")) out.print("selected");%>><%= Message.get("schedulelist_info_33") %></option>
          <option value="7" <%if(week_day.equals("7")) out.print("selected");%>><%= Message.get("schedulelist_info_34") %></option>
          <option value="1" <%if(week_day.equals("1")) out.print("selected");%>><%= Message.get("schedulelist_info_35") %></option>
        </select><%= Message.get("schedulelist_info_36") %>
&nbsp;&nbsp;
<select name="dayofweek_hours">
        <option><%= Message.get("schedulelist_info_37") %> </option>
          <option value="0" <%if(week_hour.equals("0")) out.print("selected");%>>0</option>
          <option value="1" <%if(week_hour.equals("1")) out.print("selected");%>>1</option>
          <option value="2" <%if(week_hour.equals("2")) out.print("selected");%>>2</option>
          <option value="3" <%if(week_hour.equals("3")) out.print("selected");%>>3</option>
          <option value="4" <%if(week_hour.equals("4")) out.print("selected");%>>4</option>
          <option value="5" <%if(week_hour.equals("5")) out.print("selected");%>>5</option>
          <option value="6" <%if(week_hour.equals("6")) out.print("selected");%>>6</option>
          <option value="7" <%if(week_hour.equals("7")) out.print("selected");%>>7</option>
          <option value="8" <%if(week_hour.equals("8")) out.print("selected");%>>8</option>
          <option value="9" <%if(week_hour.equals("9")) out.print("selected");%>>9</option>
          <option value="10" <%if(week_hour.equals("10")) out.print("selected");%>>10</option>
          <option value="11" <%if(week_hour.equals("11")) out.print("selected");%>>11</option>
          <option value="12" <%if(week_hour.equals("12")) out.print("selected");%>>12</option>
          <option value="13" <%if(week_hour.equals("13")) out.print("selected");%>>13</option>
          <option value="14" <%if(week_hour.equals("14")) out.print("selected");%>>14</option>
          <option value="15" <%if(week_hour.equals("15")) out.print("selected");%>>15</option>
          <option value="16" <%if(week_hour.equals("16")) out.print("selected");%>>16</option>
          <option value="17" <%if(week_hour.equals("17")) out.print("selected");%>>17</option>
          <option value="18" <%if(week_hour.equals("18")) out.print("selected");%>>18</option>
          <option value="19" <%if(week_hour.equals("19")) out.print("selected");%>>19</option>
          <option value="20" <%if(week_hour.equals("20")) out.print("selected");%>>20</option>
          <option value="21" <%if(week_hour.equals("21")) out.print("selected");%>>21</option>
          <option value="22" <%if(week_hour.equals("22")) out.print("selected");%>>22</option>
          <option value="23" <%if(week_hour.equals("23")) out.print("selected");%>>23</option>
        </select><%= Message.get("schedulelist_info_38") %>

        <input type="text" name="dayofweek_minute"  size=2 maxlength=2  value="<%=week_minute%>" class="style1">
        <%= Message.get("schedulelist_info_39") %>

</td>
<tr>
<td colspan=2><input type="radio" value="everymonth" name="launch_type" <%=launch_check_everymonth%>><%= Message.get("schedulelist_info_40") %></td>
<tr>
<td>&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="day" name="everymonth_type" <%=everymonth_type_day%>><%= Message.get("schedulelist_info_41") %></td>
<td>
<%= Message.get("schedulelist_info_42") %> &nbsp;
        <input type="text" name="everymonth_dd"  size=2 maxlength=2  value="<%=month_day_day%>" class="style1">
        <%= Message.get("schedulelist_info_43") %>&nbsp;
        <input type="text" name="everymonth_hh"  size=2 maxlength=2  value="<%=month_day_hour%>" class="style1">
        <%= Message.get("schedulelist_info_44") %>
        <input type="text" name="everymonth_minute"  size=2 maxlength=2 value="<%=month_day_minute%>" class="style1">
        <%= Message.get("schedulelist_info_45") %>
</td>
<tr>
<td>
&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="month" name="everymonth_type" <%=everymonth_type_month%>><%= Message.get("schedulelist_info_46") %>
</td>
<td>
<select name="everymonth_weekweek">
   <option selected><%= Message.get("schedulelist_info_47") %></option>
   <option value="1" <%if(month_month_week.equals("1")) out.print("selected");%>>&nbsp;1</option>
   <option value="2" <%if(month_month_week.equals("2")) out.print("selected");%>>&nbsp;2</option>
   <option value="3" <%if(month_month_week.equals("3")) out.print("selected");%>>&nbsp;3</option>
   <option value="4" <%if(month_month_week.equals("4")) out.print("selected");%>>&nbsp;4</option>
</select><%= Message.get("schedulelist_info_48") %>
&nbsp;&nbsp;
<select name="everymonth_dayofweekweek">
   <option selected><%= Message.get("schedulelist_info_49") %></option>
   <option value="2" <%if(month_month_day.equals("2")) out.print("selected");%>><%= Message.get("schedulelist_info_50") %></option>
   <option value="3" <%if(month_month_day.equals("3")) out.print("selected");%>><%= Message.get("schedulelist_info_51") %></option>
   <option value="4" <%if(month_month_day.equals("4")) out.print("selected");%>><%= Message.get("schedulelist_info_52") %></option>
   <option value="5" <%if(month_month_day.equals("5")) out.print("selected");%>><%= Message.get("schedulelist_info_53") %></option>
   <option value="6" <%if(month_month_day.equals("6")) out.print("selected");%>><%= Message.get("schedulelist_info_54") %></option>
   <option value="7" <%if(month_month_day.equals("7")) out.print("selected");%>><%= Message.get("schedulelist_info_55") %></option>
   <option value="1" <%if(month_month_day.equals("1")) out.print("selected");%>><%= Message.get("schedulelist_info_56") %></option>
</select><%= Message.get("schedulelist_info_57") %>
&nbsp;&nbsp;
<select name="everymonth_hoursofweekweek">
   <option selected><%= Message.get("schedulelist_info_58") %></option>
   <option value="0" <%if(month_month_hour.equals("0")) out.print("selected");%>>0</option>
   <option value="1" <%if(month_month_hour.equals("1")) out.print("selected");%>>1</option>
   <option value="2" <%if(month_month_hour.equals("2")) out.print("selected");%>>2</option>
   <option value="3" <%if(month_month_hour.equals("3")) out.print("selected");%>>3</option>
   <option value="4" <%if(month_month_hour.equals("4")) out.print("selected");%>>4</option>
   <option value="5" <%if(month_month_hour.equals("5")) out.print("selected");%>>5</option>
   <option value="6" <%if(month_month_hour.equals("6")) out.print("selected");%>>6</option>
   <option value="7" <%if(month_month_hour.equals("7")) out.print("selected");%>>7</option>
   <option value="8" <%if(month_month_hour.equals("8")) out.print("selected");%>>8</option>
   <option value="9" <%if(month_month_hour.equals("9")) out.print("selected");%>>9</option>
   <option value="10" <%if(month_month_hour.equals("10")) out.print("selected");%>>10</option>
   <option value="11" <%if(month_month_hour.equals("11")) out.print("selected");%>>11</option>
   <option value="12" <%if(month_month_hour.equals("12")) out.print("selected");%>>12</option>
   <option value="13" <%if(month_month_hour.equals("13")) out.print("selected");%>>13</option>
   <option value="14" <%if(month_month_hour.equals("14")) out.print("selected");%>>14</option>
   <option value="15" <%if(month_month_hour.equals("15")) out.print("selected");%>>15</option>
   <option value="16" <%if(month_month_hour.equals("16")) out.print("selected");%>>16</option>
   <option value="17" <%if(month_month_hour.equals("17")) out.print("selected");%>>17</option>
   <option value="18" <%if(month_month_hour.equals("18")) out.print("selected");%>>18</option>
   <option value="19" <%if(month_month_hour.equals("19")) out.print("selected");%>>19</option>
   <option value="20" <%if(month_month_hour.equals("20")) out.print("selected");%>>20</option>
   <option value="21" <%if(month_month_hour.equals("21")) out.print("selected");%>>21</option>
   <option value="22" <%if(month_month_hour.equals("22")) out.print("selected");%>>22</option>
   <option value="23" <%if(month_month_hour.equals("23")) out.print("selected");%>>23</option>
</select><%= Message.get("schedulelist_info_59") %>
<input type="text" name="everymonth_minuteofweekweek"  size=2 maxlength=2  value="<%=month_month_minute%>" class="style1">
<%= Message.get("schedulelist_info_60") %>

</td>
</tr>
   <tr>
      <td>
         <input type="radio" value="everyday" name="launch_type" <%=launch_check_everyday%>><%= Message.get("schedulelist_info_61") %>
      </td>
      <td>

       <input type="text" name="syear"  size=4 maxlength=4 value="<%=syear%>" class="style1"><%= Message.get("schedulelist_info_62") %>&nbsp;
       <input type="text" name="smonth"  size=2 maxlength=3 value="<%=smonth%>" class="style1"><%= Message.get("schedulelist_info_63") %>&nbsp;
       <input type="text" name="sday"  size=2 maxlength=3   value="<%=sday%>" class="style1"><%= Message.get("schedulelist_info_64") %>
        &nbsp;<b> ~</b>
       <input type="text" name="eyear"  size=4 maxlength=4 value="<%=eyear%>" class="style1"><%= Message.get("schedulelist_info_65") %>&nbsp;
       <input type="text" name="emonth"  size=2 maxlength=3 value="<%=emonth%>" class="style1"><%= Message.get("schedulelist_info_66") %>&nbsp;
       <input type="text" name="eday"  size=2 maxlength=3   value="<%=eday%>" class="style1"><%= Message.get("schedulelist_info_67") %>&nbsp;
       <input type="text" name="ehour"  size=2 maxlength=3  value="<%=ehour%>" class="style1"><%= Message.get("schedulelist_info_68") %>(24)
       <input type="text" name="emin"  size=2 maxlength=3 value="<%=emin%>" class="style1"><%= Message.get("schedulelist_info_69") %>

     </td>

   </tr>
   <tr>
      <td>
         <input type="radio" value="everytime" name="launch_type" <%=launch_check_everytime%>><%= Message.get("schedulelist_info_70") %>
      </td>
      <td>
      <%!
      Calendar sdate1 = new GregorianCalendar();
      %>
       <input type="text" name="tsyear"  size=4 maxlength=4 value="<%=syear%>" class="style1"><%= Message.get("schedulelist_info_71") %>&nbsp;
       <input type="text" name="tsmonth"  size=2 maxlength=3 value="<%=smonth%>" class="style1"><%= Message.get("schedulelist_info_72") %>&nbsp;
       <input type="text" name="tsday"  size=2 maxlength=3   value="<%=sday%>" class="style1"><%= Message.get("schedulelist_info_73") %>
       <input type="text" name="tshour"  size=2 maxlength=3  value="<%=shour%>" class="style1"><%= Message.get("schedulelist_info_74") %>(24)
        &nbsp;<b> ~</b>
       <input type="text" name="teyear"  size=4 maxlength=4 value="<%=eyear%>" class="style1"><%= Message.get("schedulelist_info_75") %>&nbsp;
       <input type="text" name="temonth"  size=2 maxlength=3 value="<%=emonth%>" class="style1"><%= Message.get("schedulelist_info_76") %>&nbsp;
       <input type="text" name="teday"  size=2 maxlength=3   value="<%=eday%>" class="style1"><%= Message.get("schedulelist_info_77") %>&nbsp;
       <input type="text" name="tehour"  size=2 maxlength=3  value="<%=ehour%>" class="style1"><%= Message.get("schedulelist_info_78") %>(24)<br>
       <%= Message.get("schedulelist_info_79") %>
       <input type="text" name="everymin"  size=3 maxlength=5  value="<%=emin%>" class="style1"><%= Message.get("schedulelist_info_80") %>
       <input type="hidden" name="rowcount" value="<%=rowcount%>">

     </td>

   </tr>
   <!--sjs 05.24 Add the condition of mail sending: user defination -->
   <%if (!rp.getProperty("server.definedPeriod","notDefined").equals("notDefined")) {%>
   <tr>
      <td>
         <input type="radio" value="userDefined" name="launch_type" <%=launch_userDefined%>><%= Message.get("scheduleadd_107") %>
      </td>
      <td>
         <select name="definedPeriod" onChange="javascript:setJspPath();">
            <option selected><%= Message.get("scheduleadd_107") %></option>
            <%
            period = new StringTokenizer(rp.getProperty("server.definedPeriod"),"^");
            jsppathname = new StringTokenizer(rp.getProperty("server.jsppath"," "),"^");
            if (period != null && jsppathname != null && period.countTokens() != jsppathname.countTokens())
            {
               %><!--<script>alert("<%= Message.get("scheduleadd_110") %>");</script> --><%
            }
            while (period.hasMoreTokens())
            {
               eachPeriod = period.nextToken();
               out.print("<option value=\""+eachPeriod+"\" ");
               out.print(">"+eachPeriod+"</option>");
               out.println();
            }
            %>
         </select>
         <%
            int counter=1;
            out.println("<script>");
            out.println("var counter = 1;");
            out.print("var jspnames=new Array(");
            out.print(jsppathname.countTokens());
            out.println(");");
            if (jsppathname != null)
            {
               out.println("jspnames[0] = \'\';");
            }
            while (jsppathname.hasMoreTokens() && jsppathname != null)
            {
               out.println("jspnames["+counter+"] = \'"+jsppathname.nextToken()+"\';");
               counter++;
            }
            out.println("</script>");
         %>
         <input type="text" name="jsppath" size=30 maxlength=100 value="<%=jsppath%>" class="style1">&nbsp;&nbsp;
         <input type="button" value="<%= Message.get("scheduleadd_108") %>" onClick="javascript:showSchedulingList();" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center">
         <input type="hidden" name="definedPeriodIndex" value="">
      </td>
   </tr>
   <%}%>
<tr>
<td colspan=2>
<img src="images/color_25_006699.gif" hspace=2><%= Message.get("schedulelist_info_81") %><br>
<input type="radio" value="no" name="sendmail" <%=sendmailno_check%>><%= Message.get("schedulelist_info_82") %> <br>
<input type="radio" value="yes" name="sendmail" <%=sendmail_check%>><%= Message.get("schedulelist_info_83") %>
   <table border=0 cellpadding=0 cellspacing=0>
   <td><img src="images/space.gif" width=20 height=1 border=0></td><td><%= Message.get("schedulelist_info_84") %>
       <input type="text" name="maillist"  size="80" maxlength="255" value="<%=mailto%>" class="style1">
       <!-- <input type="button" value="<%= Message.get("schedulelist_info_85") %>" onClick="javascript:selectList('document.scform.maillist',document.scform.maillist);" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center"> -->
   </td>
   <tr>
   <td>
      <img src="images/space.gif" width=20 height=1 border=0>
   </td>
   <td>
      <br>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("schedulelist_info_88") %><input type="radio" value="HTML" name="send_type" <%=send_type_html_check%>>HTML&nbsp;<input type="radio" value="TEXT" name="send_type" <%=send_type_url_check%>><%= Message.get("schedulelist_info_89") %>
   </td>
   </tr>
   </table>
</td>
</table>

      <%String type =Message.getcontentType().toLowerCase();if((type.indexOf("kr") != -1 || type.indexOf("ksc") != -1) && m2soft.rdsystem.server.core.rddbagent.AgentProcess.isSupportSms())
         {
            out.print("<table><tr><td><img src=images/cellphone.gif hsapce=10 align=bottom>&nbsp;</td>");
            out.print("<td>"+Message.get("JScheduleAddSMS") +"<input type=text name=\"refmaillist\"  value=\""+refto+"\" size=70 maxlength=255 class='style1'></td>/</tr></table>");
         }
      %>

</form>

<%
   }catch (Exception e){
      e.printStackTrace();
      out.println("rdhelpser.close()");
   }finally{
     try{
        rdhelper.close();
        agent.disconnect();
     }catch (Exception c){out.println(c.getMessage());}
   }
   %>

 <%
   int btnX = 500;
   int btnY = 830;

   JLButton btnadd = new JLButton();
   btnadd.printButton(btnX,btnY,"up",105,3,Message.get("menu_05"),"CheckInput()","images/button_icon_add.gif",20);
   btnadd.printButton(btnX+110,btnY,"del",105,3,Message.get("menu_06"),"delSchedule()","images/button_icon_del.gif",20);
%>
<br>
 </body>
 </html>