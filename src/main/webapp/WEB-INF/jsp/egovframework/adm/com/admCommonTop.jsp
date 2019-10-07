<%@ page pageEncoding="UTF-8"%>
<!--  
/* 	WScript.Shell로 익스플로러를 새로운 세션의 화면을 실행시킨다.
*	인터넷 옵션 > 보안 > 사용자지정수준에서 
*	[스크립트가 안전하지 않은 것으로 표시된 ActiveX 컨트롤 초기화] 이부분을 사용으로 설정하여야 실행될 수 있다
*/
-->
<script language="VBScript">
Function exec(url, chk)
	Set objShell = CreateObject("WScript.Shell")
	objShell.Run "IExplore.exe " + chk + url
End Function
</script>



<script language="javascript">
	function logOut(){
		location.href="/adm/hom/actionLogout.do";
	}


	/**
	* admMenuInitOption
	* 메뉴 클릭시 메뉴의 대한 검색 세션 값을 삭제하기 위한 parameter
	* CommandMapArgumentResolver 에서 값을 제거 하게 되어 있음
	*/
	function fn_admin_pageMove(menu, submenu, pgm){
		var frm = eval('document.<c:out value="${gsMenuForm}"/>');

		//테스트용 수정필요!!
		if( pgm.endsWith(".do") ){
			frm.action = pgm + "?admMenuInitOption=Y";
		}else{
			frm.action = "/adm/com/main/admActionMainPage.do?admMenuInitOption=Y";
		}
		frm.s_menu.value = menu;
		frm.s_submenu.value = submenu;
		frm.target = "_self";
		frm.submit();
	}

	function fn_keyEvent(fun){
		if( event.keyCode == 13 ){
			eval("this."+fun+"()");
		}
	}

	function fn_sendMail(formName, process){
		var frm = eval('document.'+formName);
		if( !this.fn_checkCount(frm) ) return;
		window.open('', 'sendMail', 'width=750,height=500');
		var url = "";
		switch(process){
			case 'SendFreeMail':
				url = "/com/mai/sendFreeMailPopup.do";
				break;

			default:
				url = "/com/mai/sendFreeMailPopup.do";
			break;
		}
		frm.action = url;
		frm.target = "sendMail";
		frm.submit()
	}

	function fn_download(realfile, savefile, dir){
		var url = "/com/commonFileDownload.do?";
		url += "realfile="+realfile;
		url += "&savefile="+savefile;
		url += "&dir="+dir;
		window.location.href = url;
		return;
	}

	function fn_sendSMS(formName){
		var frm = eval('document.'+formName);
		if( !this.fn_checkCount(frm) ) return;
		window.open('', 'sendSMS', 'width=750,height=500');
		frm.action = "/com/sms/sendSMSPopup.do";
		frm.target = "sendSMS";
		frm.submit()
	}

	function fn_checkCount(frm){
		var target = frm.p_key1;
		var cnt = 0;
		if( target ){
			if(target.length){
				for( i=0; i<target.length; i++ ){
					if( target[i].checked ) cnt++;
				}
			}else{
				if( target.checked ) cnt++;
			}
		}
		if( cnt == 0 ){
			alert("대상을 선택하세요.");
			return false;
		}
		return true;
	}
	String.prototype.endsWith = function(str){
		return (str == this.substring(this.length-3, this.length));
	};


	function fn_admLogout(){
		
		if(confirm("로그아웃 하시겠습니까?")) 
		{
			var url = "/usr/lgn/portalUserLogout.do";
			opener.document.location.replace(url);
			window.close();
	    } 
	    else 
		{
	        return;
	    }
	}
</script>
	<form id="<c:out value="${gsMenuForm}"/>" name="<c:out value="${gsMenuForm}"/>" method="post">
		<input type="hidden" id="s_menu" 		name="s_menu" 		value="${s_menu}"/>
		<input type="hidden" id="s_submenu" 	name="s_submenu" 	value="${s_submenu}"/>
	</form>
	<!-- header -->
	<div id="header">
		<div class="topLogoWrap">
			<h1><a href="/adm/com/main/admActionMainPage.do"><img src="/images/adm/common/logo.gif" alt="국립특수교육원부설 원격교육연수원" /></a></h1><!-- logo -->
			<div class="topRgith"><!-- 로그인정보1 -->
				<div class="loginout">
					<span class="name"><c:out value="${sessionScope.name}"/> 님</span> 	                                     	
					<a href="#" class="btn" onclick="fn_admLogout()"><img src="/images/adm/common/btn_logout.gif" alt="로그아웃" /></a>
				</div>
				<ul class="topInfo"><!-- 로그인정보2 -->
					<li><a href="#">최종접속일 : <c:out value="${sessionScope.lastlg}"/></a></li>					
				</ul>
			</div>
		</div>
		<!-- gnb -->
		<div class="topgnb">
			<ul class="topmenu">
				<!--<li class="${requestScope["javax.servlet.forward.request_uri"] == '/adm/com/main/admActionMainPage.do' ? 'on' : ''}">
					<a href="/adm/com/main/admActionMainPage.do">
						<span>운영현황</span>
					</a>
				</li>
			--><c:forEach items="${list_admTopMenu}" var="result" varStatus="i">
				<c:set var="class" value=""/>
				<c:if test="${result.menu == s_menu}"><c:set var="class" value="on"/></c:if>
				<li class="<c:out value="${class}"/>">
					<a href="#" onclick="javascript:fn_admin_pageMove('<c:out value="${result.menu}"/>', '<c:out value="${result.submenu}"/>', '<c:out value="${result.pgm}"/>')">
						<span><c:out value="${result.menunm}"/></span>
					</a>
				</li>
			</c:forEach>
				<!--<li class="">
					<a href="#" onclick="javascript:window.open('http://sms.lgtelecom.com/');">
						<span>SMS</span>
					</a>
				</li>
				<li class="">
					<a href="#" onclick="javascript:window.open('http://pgweb.uplus.co.kr');">
						<span>결제관리</span>
					</a>
				</li>
			--></ul>			
		</div>        
        <div class="dark"></div>        
		<!-- // gnb -->
	</div>
	<!-- // header -->