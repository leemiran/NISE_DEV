<%@ page language = "java" contentType = "text/html; charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<META http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta content="Apache Forrest" name="Generator">
	<meta name="Forrest-version" content="0.8">
	<meta name="Forrest-skin-name" content="pelt">

	<link rel="shortcut icon" href="">
</head>
<body>

<script type="text/javascript">

	// --------------------------------
  // 서비스 함수들
	// --------------------------------

	function testAuth() {
	    wWidth = 360;
	    wHight = 120;

	    wX = (window.screen.width - wWidth) / 2;
	    wY = (window.screen.height - wHight) / 2;

	    var w = window.open("Sample-AuthRequest.jsp", "gPinLoginWin", "directories=no,toolbar=no,left="+wX+",top="+wY+",width="+wWidth+",height="+wHight);
	}
	
	function testMYPINAuth() {
		wWidth = 360;
	    wHight = 120;

	    wX = (window.screen.width - wWidth) / 2;
	    wY = (window.screen.height - wHight) / 2;
	    
	    attr = "SERVICE_DIV_CONNECT_MYPIN";

		if(mypinNumber.value.length < 13){
			alert("My-PIN번호를 확인하여 주십시오.");
			return;
		}else if ( !checkMyPinNumber(mypinNumber.value) ) {
	    	alert("My-PIN번호가 잘못되었습니다.");
	    	document.getElementById("mypinNumber").focus();
	    	return;
		}else if(!(requestAuthMethod.value == "00" || requestAuthMethod.value == "01")){
			alert("인증요청방법을 확인하여 주십시오.");
			return;
		}
	    
	    var url = "Sample-MyPinAuthRequest.jsp?Attr="+attr
	    		+ "&mypinNumber="+mypinNumber.value
	    		+ "&requestAuthMethod="+requestAuthMethod.value
	    		+ "&realName="+realName.value
	    		+ "&birthDate="+birthDate.value
	    		+ "&mypinPasswd="+mypinPasswd.value;

	    var w = window.open(url, "_blank", "directories=no,toolbar=no,left="+wX+",top="+wY+",width="+wWidth+",height="+wHight);
	}

    function testUserDupValue() {
			wWidth = 850;
			wHight = 520;
			wX = (window.screen.width - wWidth) / 2;
			wY = (window.screen.height - wHight) / 2;

			requrl = "Sample-UserDuplicationValue.jsp?regNo=" + regNo.value + "&siteId=" + siteId.value;

			var w = window.open(requrl, "_blank", "directories=no,toolbar=no,left="+wX+",top="+wY+",width="+wWidth+",height="+wHight);
	}

	function checkMyPinNumber(mypinNumber){
		if( mypinNumber.length == 13 ) {
			var A	  = mypinNumber.charAt(0);
			var B  	= mypinNumber.charAt(1);
			var C  	= mypinNumber.charAt(2);
			var D  	= mypinNumber.charAt(3);
			var E  	= mypinNumber.charAt(4);
			var F  	= mypinNumber.charAt(5);
			var G  	= mypinNumber.charAt(6);
			var H  	= mypinNumber.charAt(7);
			var I  	= mypinNumber.charAt(8);
			var J  	= mypinNumber.charAt(9);
			var K  	= mypinNumber.charAt(10);
			var L  	= mypinNumber.charAt(11);
			var Osub  	= mypinNumber.charAt(12);

			var SUMM = A*2 + B*1 + C*3 + D*9+ E*4+ F*1+G*5+H*9+I*6+J*1+K*7+L*9;
			var N = SUMM % 11;
			var Modvalue = 11 - N;
			var LapointVal =  Modvalue % 10 ;

			if ( Osub == LapointVal ) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

</script>

<div id="main">
	<div id="publishedStrip">
		<div id="level2tabs"></div>
	</div>
	<div id="content">
		<h1>Sample Page 테스트</h1>
		<div id="minitoc-area">
			<ul class="minitoc">
				<li>
					<a href="#sentence1">공공I-PIN 본인확인</a>
				</li>
				<li>
					<a href="#sentence2">My-PIN 본인확인</a>
				</li>
				<li>
					<a href="#sentence3">사용자 중복가입확인코드 생성하기(UserDupValue)</a>
				</li>
			</ul>
		</div>
	
<!-- ################################################################################ -->
<!-- ###   단락					                                                  ### -->
<!-- ################################################################################ -->
 	<br />
 	<br />
 	<br />
	<a name="N10011"></a><a name="sentence1"></a>
	<h2 class="boxed">공공I-PIN 본인확인</h2>
 	<hr color="#ffa500" />
	<div class="section">
		<p>
        	I-PIN 서비스의 가장 기본이 되며 또한 지금까지 앞 단계에서의 설정과정을 처음 테스트 해보는 서비스이기도 합니다.<br />
	       	이 서비스의 테스트로 지금까지의 설정정보가 올바른지 확인해 볼 수 있게 됩니다.	<br />
	       	본인확인 결과로는 개인식별번호와 이름 그리고 중복가입확인 정보 등이 포함됩니다.
		</p>
		<p>
			<input type="button" value="테스트 실행" onclick="testAuth()" />
		</p>
	</div>

<!-- ################################################################################ -->
<!-- ###   단락					                                                  ### -->
<!-- ################################################################################ -->

	<br />
	<br />
	<br />
	<a name="N1011D"></a><a name="sentence2"></a>
	<h2 class="boxed">My-PIN 본인확인</h2>
	<hr color="#ffa500" />
	<div class="section">
		<p>
			오프라인 상에서 사용하는 My-PIN 본인인증 연계를 확인할 수 있는 서비스입니다.
			<br />
			본인확인 결과로는<br />
			1. My-PIN번호 -> 중복가입확인정보, 상태값<br />
			2. My-PIN번호, 성명 -> 중복가입확인정보, 연계정보, 본인확인정보, 상태값<br />
			3. My-PIN번호, 생년월일 -> 중복가입확인정보, 연계정보, 본인확인정보, 상태값<br />
			4. My-PIN번호, 성명, 생년월일 -> 중복가입확인정보, 연계정보, 본인확인정보, 상태값<br />
			값이 전달됩니다.<br />			
			<b>주의!!</b><br />
			본 서비스 요청 시, 사용자의 My-PIN번호와 인증요청방법은 반드시 입력되어야 합니다.<br />
			(*)배치작업 시에는 인증요청방법을 "01" 로 바꿔야 합니다.
		</p>
	    <p>
	    	[필수] My-PIN번호 : <input id="mypinNumber" type="text" size="13" value="" maxlength="13" /><br />
			[필수] 인증요청방법 : <input id="requestAuthMethod" type="text" size="15" value="00" /><br />
			[선택] 성명 : <input id="realName" type="text" size="15" value="" /><br />
			[선택] 생년월일 : <input id="birthDate" type="text" size="15" value=""/><br />
			[선택] 비밀번호: <input id="mypinPasswd" type="text" size="15" value="" disabled="disabled"/><br /><br />
			<input type="button" value="테스트 실행" onclick="javascript:testMYPINAuth()" />
	    </p>
	</div>

<!-- ################################################################################ -->
<!-- ###   단락					                                                  ### -->
<!-- ################################################################################ -->
 	<br />
 	<br />
 	<br />
	<a name="N1011D"></a><a name="sentence3"></a>
	<h2 class="boxed">사용자 중복가입확인코드 생성하기(UserDupValue)</h2>
 	<hr color="#ffa500" />
	<div class="section">
		<p>
		        이용기관에서 본인확인 서비스로, I-PIN 서비스를 사용하지 않고  실명확인 등 다른 방법의 사용을 한 동안 유지해야 할 경우 I-PIN 서비스와의 호환성을 위해
		        중복가입확인코드의 생성이 필요합니다.
		        이 서비스는 사용자의 주민번호와 이용기관에 부여된 이용기관코드(Site ID)를 입력받아 중복가입확인코드를 생성해 줍니다.
 		</p>
		<div class="warning">
			<div class="label">주의</div>
			<div class="content">
				<p>
				          본 서비스 요청 시, 사용자의 주민등록번호와 사이트 ID를 입력 받아야 합니다. 이 때 입력받는 부분은 반드시 적합한 보안처리가 적용되어 처리되어야 하며
				          본 예제 페이지에서는 기본적으로 제공하지 않고 있습니다. 그리고 G-PIN 클라이언트 모듈 G-PIN Client Tomcat은 반드시 이용기관 WAS가 설치된 장비에 같이 설치 되는 것을 원칙으로 합니다.
				</p>
			</div>
		</div>
		<p>
			주민등록번호: <input id="regNo" type="text" size="13" value="" maxlength="13"><br />
			이용기관 사이트 ID: <input id="siteId" type="text" size="15" value=""/><br /><br />
			<input type="button" value="테스트 실행" onclick="testUserDupValue()" />
		</p>
	</div>

</div>
<!-- ================================================================================ -->
<!-- |end content|-->
<div class="clearboth">&nbsp;</div>

</div>
</body>
</html>
