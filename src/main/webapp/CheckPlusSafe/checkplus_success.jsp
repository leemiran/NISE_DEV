<%@ page language="java" contentType="text/html;charset=euc-kr" %>

<%
	NiceID.Check.CPClient niceCheck = new  NiceID.Check.CPClient();

    String sEncodeData = requestReplace(request.getParameter("EncodeData"), "encodeData");
    String sReserved1  = requestReplace(request.getParameter("param_r1"), "");
    String sReserved2  = requestReplace(request.getParameter("param_r2"), "");
    String sReserved3  = requestReplace(request.getParameter("param_r3"), "");

    String sSiteCode = "BO485";				   // NICE로부터 부여받은 사이트 코드
    String sSitePassword = "wimjtRT7xJSD";			 // NICE로부터 부여받은 사이트 패스워드

    String sCipherTime = "";				 // 복호화한 시간
    String sRequestNumber = "";			 // 요청 번호
    String sResponseNumber = "";		 // 인증 고유번호
    String sAuthType = "";				   // 인증 수단
    String sName = "";							 // 성명
    String sDupInfo = "";						 // 중복가입 확인값 (DI_64 byte)
    String sConnInfo = "";					 // 연계정보 확인값 (CI_88 byte)
    String sBirthDate = "";					 // 생일
    String sGender = "";						 // 성별
    String sNationalInfo = "";       // 내/외국인정보 (개발가이드 참조)
    String sMessage = "";
    String sPlainData = "";
    
    int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);

    if( iReturn == 0 )
    {
        sPlainData = niceCheck.getPlainData();
        sCipherTime = niceCheck.getCipherDateTime();
        
        // 데이타를 추출합니다.
        java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);
        
        sRequestNumber  = (String)mapresult.get("REQ_SEQ");
        sResponseNumber = (String)mapresult.get("RES_SEQ");
        sAuthType 			= (String)mapresult.get("AUTH_TYPE");
        sName 					= (String)mapresult.get("NAME");
        sBirthDate 			= (String)mapresult.get("BIRTHDATE");
        sGender 				= (String)mapresult.get("GENDER");
        sNationalInfo  	= (String)mapresult.get("NATIONALINFO");
        sDupInfo 				= (String)mapresult.get("DI");
        sConnInfo 			= (String)mapresult.get("CI");
        
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sRequestNumber "+sRequestNumber);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sResponseNumber "+sResponseNumber);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sAuthType "+sAuthType);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sName "+sName);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sBirthDate "+sBirthDate);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sGender "+sGender);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sNationalInfo "+sNationalInfo);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sDupInfo "+sDupInfo);
        //System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ sConnInfo "+sConnInfo);
        
        String session_sRequestNumber = (String)session.getAttribute("REQ_SEQ");
        //if(!sRequestNumber.equals(session_sRequestNumber))
        //{
         //   sMessage = "세션값이 다릅니다. 올바른 경로로 접근하시기 바랍니다.";
          //  sResponseNumber = "";
           // sAuthType = "";
       // }
    }
    else if( iReturn == -1)
    {
        sMessage = "복호화 시스템 에러입니다.";
    }    
    else if( iReturn == -4)
    {
        sMessage = "복호화 처리오류입니다.";
    }    
    else if( iReturn == -5)
    {
        sMessage = "복호화 해쉬 오류입니다.";
    }    
    else if( iReturn == -6)
    {
        sMessage = "복호화 데이터 오류입니다.";
    }    
    else if( iReturn == -9)
    {
        sMessage = "입력 데이터 오류입니다.";
    }    
    else if( iReturn == -12)
    {
        sMessage = "사이트 패스워드 오류입니다.";
    }    
    else
    {
        sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
    }

%>
<%!
public static String requestReplace (String paramValue, String gubun) {
        String result = "";
        
        if (paramValue != null) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
  }
%>

<html>
<head>
    <title>NICE신용평가정보 - CheckPlus 안심본인인증</title>
</head>
<body>
    
<% if(sMessage != null && !sMessage.equals("")) { %>    
	<script language="javascript">
	window.onload = function () {
		alert("<%= sMessage %>");
		window.close();
	}
	</script>
<% } else { %>
<form name="namecheckfrm" id="namecheckfrm" method="post" onsubmit="return false;" action="">
	<input type="hidden" name="sCipherTime" value="<%= sCipherTime %>"/>
	<input type="hidden" name="REQ_SEQ" value="<%= sRequestNumber %>"/>
	<input type="hidden" name="RES_SEQ" value="<%= sResponseNumber %>"/>
	<input type="hidden" name="AUTH_TYPE" value="<%= sAuthType %>"/>
	<input type="hidden" name="NAME" value="<%= sName %>"/>
	<input type="hidden" name="DI" value="<%= sDupInfo %>"/>
	<input type="hidden" name="CI" value="<%= sConnInfo %>"/>
	<input type="hidden" name="BIRTHDATE" value="<%= sBirthDate %>"/>
	<input type="hidden" name="GENDER" value="<%= sGender %>"/>
	<input type="hidden" name="NATIONAINFO" value="<%= sNationalInfo %>"/>
</form>
	<script language="javascript">
	window.onload = function () {

		opener.document.nameForm.sCipherTime.value 	= document.namecheckfrm.sCipherTime.value;
		opener.document.nameForm.REQ_SEQ.value 		= document.namecheckfrm.REQ_SEQ.value;
		opener.document.nameForm.RES_SEQ.value 		= document.namecheckfrm.RES_SEQ.value;
		opener.document.nameForm.AUTH_TYPE.value 	= document.namecheckfrm.AUTH_TYPE.value;
		opener.document.nameForm.DI.value 			= document.namecheckfrm.DI.value;
		opener.document.nameForm.CI.value 			= document.namecheckfrm.CI.value;
		opener.document.nameForm.birthDate.value 	= document.namecheckfrm.BIRTHDATE.value;
		opener.document.nameForm.sex.value 		    = document.namecheckfrm.GENDER.value;
		opener.document.nameForm.NATIONAINFO.value 	= document.namecheckfrm.NATIONAINFO.value;
		opener.document.nameForm.realName.value		= document.namecheckfrm.NAME.value;

		opener.goMemberJoinStep3();
		self.close();
	}
	</script>

<% } %>

<!--<center>-->
<!--    <p><p><p><p>-->
<!--    본인인증이 완료 되었습니다.<br>-->
<!--    <table border=1>-->
<!--        <tr>-->
<!--            <td>복호화한 시간</td>-->
<!--            <td><%= sCipherTime %> (YYMMDDHHMMSS)</td>-->
<!--        </tr>-->
<!--        <tr>-->
<!--            <td>요청 번호</td>-->
<!--            <td><%= sRequestNumber %></td>-->
<!--        </tr>            -->
<!--        <tr>-->
<!--            <td>NICE응답 번호</td>-->
<!--            <td><%= sResponseNumber %></td>-->
<!--        </tr>            -->
<!--        <tr>-->
<!--            <td>인증수단</td>-->
<!--            <td><%= sAuthType %></td>-->
<!--        </tr>-->
<!--				<tr>-->
<!--            <td>성명</td>-->
<!--            <td><%= sName %></td>-->
<!--        </tr>-->
<!--				<tr>-->
<!--            <td>중복가입 확인값(DI)</td>-->
<!--            <td><%= sDupInfo %></td>-->
<!--        </tr>-->
<!--				<tr>-->
<!--            <td>연계정보 확인값(CI)</td>-->
<!--            <td><%= sConnInfo %></td>-->
<!--        </tr>-->
<!--				<tr>-->
<!--            <td>생년월일</td>-->
<!--            <td><%= sBirthDate %></td>-->
<!--        </tr>-->
<!--				<tr>-->
<!--            <td>성별</td>-->
<!--            <td><%= sGender %></td>-->
<!--        </tr>-->
<!--				<tr>-->
<!--            <td>내/외국인정보</td>-->
<!--            <td><%= sNationalInfo %></td>-->
<!--        </tr>-->
<!--        <tr>-->
<!--            <td>RESERVED1</td>-->
<!--            <td><%= sReserved1 %></td>-->
<!--        </tr>-->
<!--        <tr>-->
<!--            <td>RESERVED2</td>-->
<!--            <td><%= sReserved2 %></td>-->
<!--        </tr>-->
<!--        <tr>-->
<!--            <td>RESERVED3</td>-->
<!--            <td><%= sReserved3 %></td>-->
<!--        </tr>-->
<!--    </table><br><br>        -->
<!--    <%= sMessage %><br>-->
<!--    </center>-->
</body>
</html>
