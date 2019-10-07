<%@ page pageEncoding="UTF-8"%>
<script>
    var PCC_window;

    function popupCertIPin() {
        PCC_window = window.open('', 'popupIPIN2', 'width=450, height=550, top=200, left=300, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');

        if(PCC_window == null) { 
			 alert(" ※ 윈도우 XP SP2 또는 인터넷 익스플로러 7 사용자일 경우에는 \n    화면 상단에 있는 팝업 차단 알림줄을 클릭하여 팝업을 허용해 주시기 바랍니다. \n\n※ MSN,야후,구글 팝업 차단 툴바가 설치된 경우 팝업허용을 해주시기 바랍니다.");
        }

        document.reqIPinFrm.action = 'https://cert.vno.co.kr/ipin.cb';
        document.reqIPinFrm.target = 'popupIPIN2';
        document.reqIPinFrm.submit();
    }
    
    function reqIPin(retType, addVar) {
    	$.ajax({
    		url: '${pageContext.request.contextPath}/cert/ipin/reqIPin.do'
    		, type: 'post'
    		, data: {
    			retType: retType
    			, addVar: addVar
    		}
    		, dataType: 'json'
    		, success: function(result) {
    			if(result.resultCode == 'SUCCESS') {
    				$('#reqIPinFrm input[name=m]').val(result.m);
    				$('#reqIPinFrm input[name=enc_data]').val(result.enc_data);
    				$('#reqIPinFrm input[name=retUrl]').val(result.retUrl);
    				
    				popupCertIPin();
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
<form name="reqIPinFrm" id="reqIPinFrm" method="post">
    <input type="hidden" name="m">
    <input type="hidden" name="enc_data">
    <input type="hidden" name="retUrl">
    <input type="hidden" name="param_r1">
    <input type="hidden" name="param_r2">
    <input type="hidden" name="param_r3">
</form>
<!--End 본인실명확인서비스 요청 form ----------------------->