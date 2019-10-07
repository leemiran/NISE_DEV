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


<!-- JSTabCtrl.jsp : start -->
<%
	if (false) // JLString.IsEmpty(getEnvValue("debugscript")))
	{
		// String sPath = getEnvValue("classpath");
		// String sFile = JLString.BuildFilePath(sPath,"JSTabCtrl.js","/");

%>
<script language="javascript" src="<%=""%>"></script>
<%
	}
	else
	{
%>
<script>
<%@ include file="JSTabCtrl.js"%>
</script>
<%
	}
%>
<!-- JSTabCtrl.jsp : end -->
