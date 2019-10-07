<%@ page pageEncoding="UTF-8"%>
<script>
    var PCC_window;

    function popupCertSci() {
        PCC_window = window.open('', 'PCCV3Window', 'width=430, height=560, resizable=1, scrollbars=no, status=0, titlebar=0, toolbar=0, left=300, top=200' );

        if(PCC_window == null) { 
			 alert(" ※ 윈도우 XP SP2 또는 인터넷 익스플로러 7 사용자일 경우에는 \n    화면 상단에 있는 팝업 차단 알림줄을 클릭하여 팝업을 허용해 주시기 바랍니다. \n\n※ MSN,야후,구글 팝업 차단 툴바가 설치된 경우 팝업허용을 해주시기 바랍니다.");
        }

        document.reqSciFrm.action = 'https://pcc.siren24.com/pcc_V3/jsp/pcc_V3_j10.jsp';
        document.reqSciFrm.target = 'PCCV3Window';
        document.reqSciFrm.submit();
    }
    
    function reqSci(retType, addVar) {
    	$.ajax({
    		url: '${pageContext.request.contextPath}/cert/sci/reqSci.do'
    		, type: 'post'
    		, data: {
    			retType: retType
    			, addVar: addVar
    		}
    		, dataType: 'json'
    		, success: function(result) {
    			if(result.resultCode == 'SUCCESS') {
    				$('#reqSciFrm input[name=reqInfo]').val(result.reqInfo);
    				$('#reqSciFrm input[name=retUrl]').val(result.retUrl);
    				
    				popupCertSci();
    			} else {
    				alert('안심 본인인증 에러, 관리자에게 문의해 주세요.');
    			}
    		}
    		, error: function(xhr, status, error) {
    			console.log(status);
    			console.log(error);   
    		}
    	});
    }
</script>

<!-- 본인실명확인서비스 요청 form --------------------------->
<form name="reqSciFrm" id="reqSciFrm" method="post">
    <input type="hidden" name="reqInfo">
    <input type="hidden" name="retUrl">
</form>
<!--End 본인실명확인서비스 요청 form ----------------------->