<%@ page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/EPKI/EPKICommon.js"></script>
<script>
//EPKI Client Toolkit 설치 확인, 객체 생성 및 공통 속성 지정
SetupObjECT(true);
InitObjECT();

function RequestSessionData()
{
	var strRequestData;

    strRequestData = RequestSession("${strServerCert}", "SEED", "${sessionID}");

    if(strRequestData == "")
	{
		ECTErrorInfo();
	}else if(strRequestData == "100"){
		// 취소버튼 클릭시
		return;
	}
    else
	{
    	document.reqEpkiFrm.SessionRequestData.value = strRequestData;
    	document.reqEpkiFrm.action = "${pageContext.request.contextPath}/cert/epki/epkiResult.do";
    	document.reqEpkiFrm.target = "_self";
    	document.reqEpkiFrm.submit();
    }
}
</script>

<form name="reqEpkiFrm" method="post">
	<input type="hidden" name="SessionRequestData"/>
</form>
