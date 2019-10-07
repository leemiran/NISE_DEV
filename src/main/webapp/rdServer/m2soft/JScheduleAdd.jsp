<%@ page import="java.net.*,java.util.*,java.text.*,java.io.*,m2soft.rdsystem.server.core.rdscheduler.*,m2soft.rdsystem.server.core.rddbagent.jdbc.*,m2soft.rdsystem.server.core.rddbagent.beans.*,m2soft.rdsystem.server.core.rddbagent.util.*,m2soft.rdsystem.server.core.install.Message,m2soft.rdsystem.server.core.rddbagent.*" %>
<%String contentType1 = Message.getcontentType(); response.setContentType(contentType1); %>
<%@ page session="false" %>
<%@ include file="session.h"%>

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
    String scheduleadd_01 = Message.get("scheduleadd_01");
    String scheduleadd_02 = Message.get("scheduleadd_02");
    String scname ="";
    String osname ="";
    String url ="";

    //sjs 05.27
    StringTokenizer period, jsppathname;
    String eachPeriod ="";
%>
<%
   if ((request.getParameter("jsppath")) != null)
   {
      if (!(request.getParameter("jsppath")).equals(""))
      {
         try{
               //sjs 06.02
               ////////////////At the time of registration, using user defination to preliminary check /////////////////
               m2soft.rdsystem.server.core.rddbagent.util.URLData ud = new m2soft.rdsystem.server.core.rddbagent.util.URLData();
               StringTokenizer st = new StringTokenizer(ud.get(request.getParameter("jsppath")),":");
               String eachToken = "";
               while (st.hasMoreTokens())
               {
                  eachToken = st.nextToken();
                  if (eachToken.length() != 14)
                  {
                     throw new StringIndexOutOfBoundsException();
                  }
               }

         }catch(FileNotFoundException fe) {
            %><script>alert("<%= Message.get("scuserdef_02") %>");history.back();</script><%
         }catch(StringIndexOutOfBoundsException se){
            %><script>alert("<%= Message.get("scuserdef_01") %>");history.back();</script><%
         }catch (Exception e){
            %><script>alert("Error: <%=e%>");history.back();</script><%
         }
      }
   }

    /////////////////Scheduling registration//////////////////
   try{
   	osname = System.getProperty("os.name").toLowerCase();
      scname = request.getParameter("schedulejob_name");
////////At the time of clicking the ADD-BUTTON /////////////////////////////

      if(scname != null) {
         Addschedule addsc = new Addschedule(out,response,request,servicename,logpath,dbencoding);
         addsc.AddList();


      if(AgentProcess.getScheduleServer() != null) AgentProcess.getScheduleServer().startSchedule();

/*       Socket connSock = null;
       DataInputStream sin = null;
       DataOutputStream sout = null;
      try {
           //Generate Socket
         connSock = new Socket ("localhost", 4989);

         //Get DataInputStream and DataOutputStream of connectioned socket
         sin = new DataInputStream(connSock.getInputStream());
         sout = new DataOutputStream(connSock.getOutputStream());
         sout.writeUTF("Anonymous");
         sout.flush();
         sout.writeUTF("scstart");
         sout.flush();

      }catch (Exception ex){
         if(ex instanceof ConnectException)
            out.println(scheduleadd_01);
         else
           out.println(ex);
      }
*/
      String url = "JMainHome.jsp?rowcount=1&rows="+ROWCOUNT;

      debugPrint("<script>window.location='"+url+"';</script>");
      return;

      }

   }catch (Exception e){
      out.println(scheduleadd_02 +e.getMessage());
   }

%>
<%--///////////////Print the screen of Scheduling Add///////////////////////////////////////////////////////////////////////////////--%>

<html><head><title><%= Message.get("scheduleadd_03") %></title>
<meta http-equiv="Content-Type" content="<%= contentType1 %>">
<style>
<%@ include file="icis.css" %>
</style>
<script language="JavaScript">

function numchk(num) {

   var str = "1234567890";

   for (i=0; i< num.length; i++) {
      numcheck = num.charAt(i);
      for ( j = 0 ;  j < str.length ; j++){
         if (numcheck == str.charAt(j)) break;
         if (j+1 == str.length) return false;
      }
   }
   return true;
}

function monthchk(num){
   if(num < 1 || num > 12)
      return false;
   else
      return true;
}

function daychk(num){
   if(num < 1 || num > 31)
      return false;
   else
      return true;
}

function hourchk(num){
   if(num < 1 || num > 24)
      return false;
   else
      return true;
}



function Formclear(){
   var frm = document.scform;
   frm.reset();
}

function CheckInput() {
   var frm = document.scform;

   if(!frm.schedulejob_name.value){
      alert(document.setmessage.scheduleadd_04.value);
      frm.schedulejob_name.focus();
      return;

   }

   if(!frm.schedulefile.value){
      alert(document.setmessage.scheduleadd_05.value);
      frm.schedulefile.focus();
      return;
   }

   if(!frm.ocxtempletename.value){
      alert(document.setmessage.scheduleadd_06.value);
      frm.ocxtempletename.focus();
      return;
   }

	//bgso 2002/10/09 
   if(!(!frm.ocxtempletename.value)){
	   var ocxtempletenameCheck1=frm.ocxtempletename.value
		var ocxtempletenameCheck2=ocxtempletenameCheck1.indexOf(".");
		if(!(ocxtempletenameCheck2==-1)){
			alert(document.setmessage.scheduleadd_06_1.value);
			frm.ocxtempletename.focus();
      	return;
		}
	}

   if(!frm.start_yyyy.value || !numchk(frm.start_yyyy.value) || (frm.start_yyyy.value.length != 4)){
      alert(document.setmessage.scheduleadd_07.value);
      frm.start_yyyy.focus();
      return;
   }

   if(!frm.start_mm.value || !numchk(frm.start_mm.value)){
      alert(document.setmessage.scheduleadd_08.value);
      frm.start_mm.focus();
      return;
   }

   if(!frm.start_dd.value || !numchk(frm.start_dd.value)){
      alert(document.setmessage.scheduleadd_09.value);
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
            alert(document.setmessage.scheduleadd_10.value);
            frm.once_yyyy.focus();
            return;
         }

         break;

      case "everyweek" :
         if(!frm.dayofweek.value) {
            alert(document.setmessage.scheduleadd_11.value);
            frm.dayofweek.focus();
         return;
         }

         if(!frm.dayofweek_hours.value){
            alert(document.setmessage.scheduleadd_12.value);
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
               alert(document.setmessage.scheduleadd_12.value);
               frm.everymonth_dd.focus();
               return;
            }

         } else if(everymonth_type == "month") {
            if(!frm.everymonth_weekweek.value){
               alert(document.setmessage.scheduleadd_14.value);
               frm.everymonth_weekweek.focus();
               return;
            }

            if(!frm.everymonth_dayofweekweek.value){
               alert(document.setmessage.scheduleadd_15.value);
               frm.everymonth_dayofweekweek.focus();
               return;
            }

         }
       break;

       case "everyday" :

          if(!frm.syear.value || !numchk(frm.syear.value) || !numchk(frm.smonth.value) || !numchk(frm.sday.value) || !numchk(frm.eyear.value) || !numchk(frm.emonth.value) || !numchk(frm.eday.value) || !numchk(frm.ehour.value)){
               alert(document.setmessage.scheduleadd_16.value);
               frm.syear.focus();
               return;
            }

       break;

      case "everytime" :

      if(!frm.tsyear.value || !numchk(frm.tsyear.value) || !numchk(frm.tsmonth.value) || !numchk(frm.tsday.value) || !numchk(frm.teyear.value) || !numchk(frm.temonth.value) || !numchk(frm.teday.value) || !numchk(frm.tehour.value)){
         alert(document.setmessage.scheduleadd_17.value);
         frm.tsyear.focus();
         return;
      }
      if(!monthchk(frm.tsmonth.value) || !daychk(frm.tsday.value) || !hourchk(frm.tsday.value) || !monthchk(frm.tsmonth.value) || !daychk(frm.tsday.value) || !hourchk(frm.tsday.value)){
         alert(document.setmessage.scheduleadd_18.value);
         frm.tsyear.focus();
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
      alert(document.setmessage.scheduleadd_19.value);
      frm.maillist.focus();
      return;
   }

   frm.ocxtempletename.value = frm.owner.value+ '/' + frm.ocxtempletename.value;

   frm.action="JScheduleAdd.jsp";
   frm.submit();
}

function selectList(frmname, frm) {
   var newWin = window.open("JManageAddress.jsp?opcode=5&targetlist=" + frmname + "&list=" + frm.value, "NewWindow",'Width=500,Height=350,toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars=1,resizable=1');
   self.name = "MainWindow";
   newWin.opener = self;
   newWin.focus();
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
			// b=start position of print option e=The last position of print option
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
			// b=start position of print option e=The last position of print option
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

function OnClickPrintCheck()
{
	if(document.scform.chkserverprint.checked){
		if(document.scform.driverlist.value != "false"){
			document.scform.parameters.value = document.scform.driverlist.value;
		}
	}else{
		document.scform.parameters.value = "";
	}
}
</script>
</head>
<body bgcolor="#ffffff" text="#000000" leftmargin="15" topmargin="10" marginwidth="0" marginheight="0">

<form name="setmessage">
	<input type="hidden" name="scheduleadd_04" value=<%= Message.get("scheduleadd_04") %> >
	<input type="hidden" name="scheduleadd_05" value=<%= Message.get("scheduleadd_05") %> >
	<input type="hidden" name="scheduleadd_06" value=<%= Message.get("scheduleadd_06") %> >
	<input type="hidden" name="scheduleadd_06_1" value=<%= Message.get("scheduleadd_06_1") %> >
	<input type="hidden" name="scheduleadd_07" value=<%= Message.get("scheduleadd_07") %> >
	<input type="hidden" name="scheduleadd_08" value=<%= Message.get("scheduleadd_08") %> >
	<input type="hidden" name="scheduleadd_09" value=<%= Message.get("scheduleadd_09") %> >
	<input type="hidden" name="scheduleadd_10" value=<%= Message.get("scheduleadd_10") %> >
	<input type="hidden" name="scheduleadd_11" value=<%= Message.get("scheduleadd_11") %> >
	<input type="hidden" name="scheduleadd_12" value=<%= Message.get("scheduleadd_12") %> >
	<input type="hidden" name="scheduleadd_13" value=<%= Message.get("scheduleadd_13") %> >
	<input type="hidden" name="scheduleadd_14" value=<%= Message.get("scheduleadd_14") %> >
	<input type="hidden" name="scheduleadd_15" value=<%= Message.get("scheduleadd_15") %> >
	<input type="hidden" name="scheduleadd_16" value=<%= Message.get("scheduleadd_16") %> >
	<input type="hidden" name="scheduleadd_17" value=<%= Message.get("scheduleadd_17") %> >
	<input type="hidden" name="scheduleadd_18" value=<%= Message.get("scheduleadd_18") %> >
	<input type="hidden" name="scheduleadd_19" value=<%= Message.get("scheduleadd_19") %> >
   <input type="hidden" name="scheduleadd_111" value="<%= Message.get("scheduleadd_111") %>" >
</form>

<%--///Form tag --%>
<form name="scform" method="post">
<input type="hidden" name="owner" value="<%=sessionUser%>" >
<table border="0" width=1024 cellpadding=1 cellspacing=0>
   <td height="26" align="left" bgcolor="<%=m_sBtnFace%>">
      <font color="white"><b><%= Message.get("scheduleadd_20") %></b></font>
   </td>
</table>

<table border="0" width="100%" cellpadding=2 cellspacing=0>
<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_21") %>&nbsp;<input type="text" name="schedulejob_name" size="22" maxlength="16"  value="" class="style1">
   </td>
</tr>
<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_22") %><br>
      &nbsp;<input type="readonly" name="schedulefile" size="100" values="180" class="style1" enable=false ><br>
      &nbsp;<iframe frameborder="0" height="200" scrolling="auto" src="JDocAdd.jsp?add=yes" width="640" name="body" STYLE="border-style:solid;border-width:1;"></iframe><br>
   </td>
</tr>
<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_23") %>&nbsp;<input type="text" name="parameters" size="75" maxlength=100 values="" class="style1"><br>

      <%if(osname.indexOf("window") != -1 || osname.indexOf("xp") != -1) { %>
	      <input type="checkbox" name="chkserverprint" size="30" onClick="OnClickDriverList()" unchecked><%= Message.get("scheduleadd_24") %>
	      <select name="driverlist" onChange="OnClickDriverList()">
	         <option value="false" selected><%= Message.get("scheduleadd_25") %></option>
	         <%
               String prnlist = new String(new WinSystemPrinter().getSystemPrinters());
	         	StringTokenizer st = new StringTokenizer(prnlist,",");
	         	while (st.hasMoreTokens()) {
	         		String tmp = st.nextToken();
	         %>
	         	   <option value="<%=tmp%>"><%=tmp%></option>
	         <%
	        		}
			   %>
	      </select>
			<br>
		<%}%>
      &nbsp;&nbsp;
   </td>
</tr>

<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_27") %><br>
      &nbsp;&nbsp;&nbsp;&nbsp;<%=datapath%>/
      <input type="text" name="ocxtempletename" size="30" maxlength=60 values="" class="style1">
      (<%=Message.get("JScheduleAdd.refparaminxmsg") %>:<input type="text" name="refparaminx" size="2" maxlength=5 value="" class="style1">)
      <br>
   </td>
</tr>
<tr>
   <td colspan=2>
      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_29") %>
      <%!
      Calendar sdate = new GregorianCalendar();
      %>
      <input type="text" name="start_yyyy"  size = 4 maxlength=4  value="<%=sdate.get(Calendar.YEAR)%>" class="style1"><%= Message.get("scheduleadd_30") %>&nbsp;
      <input type="text" name="start_mm"  size=2 maxlength=2   value="<%=sdate.get(Calendar.MONTH)+1%>" class="style1"><%= Message.get("scheduleadd_31") %>&nbsp;
      <input type="text" name="start_dd"  size=2 maxlength=2   value="<%=sdate.get(Calendar.DAY_OF_MONTH)%>" class="style1"><%= Message.get("scheduleadd_32") %>&nbsp;
      <input type="text" name="start_hh"  size=2 maxlength=2   value="<%=sdate.get(Calendar.HOUR_OF_DAY)%>" class="style1"><%= Message.get("scheduleadd_40") %>~
      <br>
      <br>
   </td>
</tr>
<tr>

   <td colspan=2>
   <img src="images/color_25_006699.gif" hspace=2 nobr><%= Message.get("scheduleadd_34") %>
   </td>
</tr>
<tr>
  <!--       //input the day of scheduling, table start// -->
   <td>

   <table border=0 cellpadding=1 cellspacing=1>

   <td bgcolor=#B0C4DE>
      <input type="radio" value="once" name="launch_type" checked><%= Message.get("scheduleadd_35") %>
   </td>
   <td valign=center>
      <%= Message.get("scheduleadd_36") %>
      &nbsp;<input type="text" name="once_yyyy" size=4 maxlength=4   value="<%=sdate1.get(Calendar.YEAR)%>" class="style1"><%= Message.get("scheduleadd_37") %>
      &nbsp;<input type="text" name="once_mm" size=2 maxlength=4  value="<%=sdate1.get(Calendar.MONTH)+1%>" class="style1"><%= Message.get("scheduleadd_38") %>
      &nbsp;<input type="text" name="once_dd" size=2 maxlength=4  value="<%=sdate1.get(Calendar.DAY_OF_MONTH)%>" class="style1"><%= Message.get("scheduleadd_39") %>
      &nbsp;<input type="text" name="once_hh"  size=2 maxlength=2 value="<%=sdate1.get(Calendar.HOUR_OF_DAY)%>" class="style1"><%= Message.get("scheduleadd_40") %>
      &nbsp;<input type="text" name="once_minute"  size=2 maxlength=2   value="<%=sdate1.get(Calendar.MINUTE)%>" class="style1"><%= Message.get("scheduleadd_41") %>
   </td>
</tr>
<tr>


   <td bgcolor=#B0C4DE>
      <input type="radio" value="everyweek" name="launch_type"><%= Message.get("scheduleadd_42") %>
   </td>
   <td>
      <select name="dayofweek">
         <option selected><%= Message.get("scheduleadd_43") %></option>
         <option value="2"><%= Message.get("scheduleadd_44") %></option>
         <option value="3"><%= Message.get("scheduleadd_45") %></option>
         <option value="4"><%= Message.get("scheduleadd_46") %></option>
         <option value="5"><%= Message.get("scheduleadd_47") %></option>
         <option value="6"><%= Message.get("scheduleadd_48") %></option>
         <option value="7"><%= Message.get("scheduleadd_49") %></option>
         <option value="1"><%= Message.get("scheduleadd_50") %></option>
      </select><%= Message.get("scheduleadd_51") %> &nbsp;&nbsp;
      <select name="dayofweek_hours">
         <option selected><%= Message.get("scheduleadd_52") %></option>
         <option value="0">0</option>
         <option value="1">1</option>
         <option value="2">2</option>
         <option value="3">3</option>
         <option value="4">4</option>
         <option value="5">5</option>
         <option value="6">6</option>
         <option value="7">7</option>
         <option value="8">8</option>
         <option value="9">9</option>
         <option value="10">10</option>
         <option value="11">11</option>
         <option value="12">12</option>
         <option value="13">13</option>
         <option value="14">14</option>
         <option value="15">15</option>
         <option value="16">16</option>
         <option value="17">17</option>
         <option value="18">18</option>
         <option value="19">19</option>
         <option value="20">20</option>
         <option value="21">21</option>
         <option value="22">22</option>
         <option value="23">23</option>
      </select><%= Message.get("scheduleadd_53") %>
      <input type="text" name="dayofweek_minute"  size=2 maxlength=2 value="" class="style1"><%= Message.get("scheduleadd_54") %>
   </td>
</tr>
<tr>
   <td colspan=2 bgcolor=#B0C4DE>
      <input type="radio" value="everymonth" name="launch_type"><%= Message.get("scheduleadd_55") %>
   </td>
</tr>
<tr>
   <td>
      &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="day" name="everymonth_type" checked><%= Message.get("scheduleadd_56") %>
   </td>
   <td>
      <%= Message.get("scheduleadd_57") %> &nbsp;
      <input type="text" name="everymonth_dd"  size=2 maxlength=2 value="" class="style1"><%= Message.get("scheduleadd_58") %>&nbsp;
      <input type="text" name="everymonth_hh"  size=2 maxlength=2 value="" class="style1"><%= Message.get("scheduleadd_40") %>&nbsp;
      <input type="text" name="everymonth_minute"  size=2 maxlength=2   value="" class="style1"><%= Message.get("scheduleadd_60") %>
   </td>
</tr>
<tr>
   <td>
      &nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="month" name="everymonth_type"><%= Message.get("scheduleadd_61") %>
   </td>
   <td>
      <select name="everymonth_weekweek">
         <option selected><%= Message.get("scheduleadd_62") %></option>
         <option value="1">&nbsp;1</option>
         <option value="2">&nbsp;2</option>
         <option value="3">&nbsp;3</option>
         <option value="4">&nbsp;4</option>
         <option value="5">&nbsp;5</option>
      </select><%= Message.get("scheduleadd_63") %>&nbsp;&nbsp;
      <select name="everymonth_dayofweekweek">
         <option selected><%= Message.get("scheduleadd_64") %></option>
         <option value="2"><%= Message.get("scheduleadd_65") %></option>
         <option value="3"><%= Message.get("scheduleadd_66") %></option>
         <option value="4"><%= Message.get("scheduleadd_67") %></option>
         <option value="5"><%= Message.get("scheduleadd_68") %></option>
         <option value="6"><%= Message.get("scheduleadd_69") %></option>
         <option value="7"><%= Message.get("scheduleadd_70") %></option>
         <option value="1"><%= Message.get("scheduleadd_71") %></option>
      </select><%= Message.get("scheduleadd_72") %> &nbsp;&nbsp;
      <select name="everymonth_hoursofweekweek">
         <option selected><%= Message.get("scheduleadd_73") %></option>
         <option value="0">0</option>
         <option value="1">1</option>
         <option value="2">2</option>
         <option value="3">3</option>
         <option value="4">4</option>
         <option value="5">5</option>
         <option value="6">6</option>
         <option value="7">7</option>
         <option value="8">8</option>
         <option value="9">9</option>
         <option value="10">10</option>
         <option value="11">11</option>
         <option value="12">12</option>
         <option value="13">13</option>
         <option value="14">14</option>
         <option value="15">15</option>
         <option value="16">16</option>
         <option value="17">17</option>
         <option value="18">18</option>
         <option value="19">19</option>
         <option value="20">20</option>
         <option value="21">21</option>
         <option value="22">22</option>
         <option value="23">23</option>
      </select><%= Message.get("scheduleadd_74") %>
      <input type="text" name="everymonth_minuteofweekweek"  size=2 maxlength=2  value="" class="style1"><%= Message.get("scheduleadd_75") %>
      <br>
   </td>
   </tr>
   <tr>
      <td bgcolor=#B0C4DE>
         <input type="radio" value="everyday" name="launch_type"><%= Message.get("scheduleadd_76") %>
      </td>
      <td>

       <input type="text" name="syear"  size=4 maxlength=4 value="<%=sdate1.get(Calendar.YEAR)%>" class="style1"><%= Message.get("scheduleadd_77") %>&nbsp;
       <input type="text" name="smonth"  size=2 maxlength=3 value="<%=sdate1.get(Calendar.MONTH)+1%>" class="style1"><%= Message.get("scheduleadd_78") %>&nbsp;
       <input type="text" name="sday"  size=2 maxlength=3   value="<%=sdate1.get(Calendar.DAY_OF_MONTH)%>" class="style1"><%= Message.get("scheduleadd_79") %>
        &nbsp;<b> ~</b>
       <input type="text" name="eyear"  size=4 maxlength=4 value="<%=sdate1.get(Calendar.YEAR)%>" class="style1"><%= Message.get("scheduleadd_80") %>&nbsp;
       <input type="text" name="emonth"  size=2 maxlength=3 value="" class="style1"><%= Message.get("scheduleadd_81") %>&nbsp;
       <input type="text" name="eday"  size=2 maxlength=3   value="" class="style1"><%= Message.get("scheduleadd_82") %>&nbsp;
       <input type="text" name="ehour"  size=2 maxlength=3  value="" class="style1"><%= Message.get("scheduleadd_40") %>
       <input type="text" name="emin"  size=2 maxlength=3 value="" class="style1"><%= Message.get("scheduleadd_84") %>

     </td>

   </tr>
<tr>
      <td bgcolor=#B0C4DE>
         <input type="radio" value="everytime" name="launch_type"><%= Message.get("scheduleadd_85") %>
      </td>
      <td>
      <%!
      Calendar sdate1 = new GregorianCalendar();
      %>
       <input type="text" name="tsyear"  size=4 maxlength=4 value="<%=sdate1.get(Calendar.YEAR)%>" class="style1"><%= Message.get("scheduleadd_86") %>&nbsp;
       <input type="text" name="tsmonth"  size=2 maxlength=3 value="<%=sdate1.get(Calendar.MONTH)+1%>" class="style1"><%= Message.get("scheduleadd_87") %>&nbsp;
       <input type="text" name="tsday"  size=2 maxlength=3   value="<%=sdate1.get(Calendar.DAY_OF_MONTH)%>" class="style1"><%= Message.get("scheduleadd_88") %>
       <input type="text" name="tshour"  size=2 maxlength=3  value="<%=sdate1.get(Calendar.HOUR_OF_DAY)%>" class="style1"><%= Message.get("scheduleadd_89") %>
        &nbsp;<b> ~</b>
       <input type="text" name="teyear"  size=4 maxlength=4 value="<%=sdate1.get(Calendar.YEAR)%>" class="style1"><%= Message.get("scheduleadd_90") %>&nbsp;
       <input type="text" name="temonth"  size=2 maxlength=3 value="" class="style1"><%= Message.get("scheduleadd_91") %>&nbsp;
       <input type="text" name="teday"  size=2 maxlength=3   value="" class="style1"><%= Message.get("scheduleadd_92") %>&nbsp;
       <input type="text" name="tehour"  size=2 maxlength=3  value="" class="style1"><%= Message.get("scheduleadd_93") %><br>
       <%= Message.get("scheduleadd_94") %>
       <input type="text" name="everymin"  size=3 maxlength=5  value="" class="style1"><%= Message.get("scheduleadd_95") %>
     </td>

     </td>
   <!--sjs 05.24 add the condition of e-mail sending: user defination -->
   <%if (!rp.getProperty("server.definedPeriod","notDefined").equals("notDefined")) {%>
   <tr>
      <td bgcolor=#B0C4DE>
         <input type="radio" value="userDefined" name="launch_type"><%= Message.get("scheduleadd_107") %>
      </td>
      <td>
         <select name="definedPeriod" onChange="javascript:setJspPath();">
            <option selected><%= Message.get("scheduleadd_107") %></option>
            <%
            period = new StringTokenizer(rp.getProperty("server.definedPeriod"),"^");
            jsppathname = new StringTokenizer(rp.getProperty("server.jsppath",""),"^");
            if (period != null && jsppathname != null && period.countTokens() != jsppathname.countTokens())
            {
               %><script>alert("<%= Message.get("scheduleadd_110") %>");</script><%
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
         <input type="text" name="jsppath" size=30 maxlength=100 value="" class="style1">&nbsp;&nbsp;
         <input type="button" value="<%= Message.get("scheduleadd_108") %>" onClick="javascript:showSchedulingList();" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center">
         <input type="hidden" name="definedPeriodIndex" value="">
      </td>
   </tr>
     </table>

   </tr>
   <%}%>

<tr>
   <td colspan=2>

      <img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_96") %><br>
      <input type="radio" value="no" name="sendmail" checked><%= Message.get("scheduleadd_97") %><br>
      <input type="radio" value="yes" name="sendmail"><%= Message.get("scheduleadd_98") %>

         <table border=0 cellpadding=0 cellspacing=0>
            <tr>
               <td>
                  <img src="images/space.gif" width=20 height=1 border=0>
               </td>
               <td><%= Message.get("scheduleadd_98_1") %>
                  <input type="text" name="maillist"  size="70" maxlength="255" value="" class="style1">

                  <!-- <input type="button" value=<%= Message.get("scheduleadd_99") %> onClick="javascript:selectList('document.scform.maillist',document.scform.maillist);" style="font-size:9pt; width:100;height:18;border-width:1px; border-color:gray; border-style:solid; background-color:#e4e4e4; text-align:center"> -->
               </td>
            </tr>
            <tr>
               <td>
                  <img src="images/space.gif" width=20 height=1 border=0></td><td><br><img src="images/color_25_006699.gif" hspace=2><%= Message.get("scheduleadd_102") %><input type="radio" value="HTML" name="send_type"><%= Message.get("scheduleadd_103") %>&nbsp;<input type="radio" value="TEXT" name="send_type" checked><%= Message.get("scheduleadd_104") %>
               </td>
            </tr>
         </table>

         <%
             String type = Message.getcontentType().toLowerCase();

             if((type.indexOf("kr") != -1 || type.indexOf("ksc") != -1) && m2soft.rdsystem.server.core.rddbagent.AgentProcess.isSupportSms())
             {
               out.print("<table><td><img src=images/cellphone.gif hsapce=10 align=bottom>&nbsp;</td>");
               out.print("<td>"+Message.get("JScheduleAddSMS") +"<input type=text name=\"refmaillist\"  size=70 maxlength=255 class='style1'></td>/</tr></table>");
             }
         %>

   </td>
</tr>
<tr>
</table>

<%

   int btnX = 450;
   int btnY = 830;

   JLButton btnadd = new JLButton();
   btnadd.printButton(btnX,btnY,"confirm",105,3,Message.get("scheduleadd_105"),"CheckInput()","images/button_icon_add.gif",20);
   btnadd.printButton(btnX+110,btnY,"del",105,3,Message.get("scheduleadd_106"),"Formclear()","images/button_icon_del.gif",20);
%>
</form>
</body>
</html>