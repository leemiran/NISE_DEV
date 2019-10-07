<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmHead.jsp" %>
<body class="bg">
        <!-- skipmenu -->
        <div class="skipmenu">
            <dl>
                <dt class="hidden">바로가기 메뉴</dt>
                <dd><a href="#header" class="skip_link">Top영역</a></dd>
                <dd><a href="#container" class="skip_link">본문바로가기</a></dd>
            </dl>
        </div>
        <!-- //skipmenu -->    
        <!-- wrap-->
        <div id="wrap">
            <!-- 공통헤더 -->
            <%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmMainTop.jsp" %>
            <!-- //공통헤더 -->
	<!-- 본문 -->
	<div id="container">
		<div class="wzone">
			<form id="frmSearchId" name="frmSearchId" method="post" action="" onSubmit="return CtlExecutor.requestAjaxUserId();">
            <fieldset>
            <legend>비밀번호찾기</legend>
			<!-- 아이디찾기 -->
			<h2 class="bul"><img src="/images/mbl/common/tit_fid.png" alt="아이디 찾기" /></h2>
			<div class="memzone">
				<ul class="login idsearch">
					<li>
						<label for="txt_p_name" class="tit"><img src="/images/mbl/common/tit_name.gif" alt="이름" /></label>
						<span><input type="text" name="txt_p_name" value="" id="txt_p_name" class="memput" /></span>
					</li>
					<li>
						<label for="p_email" class="tit"><img src="/images/mbl/common/tit_cino.gif"  alt="이메일" /></label>
						<span><input type="text" name="p_email" value="" id="p_email" class="memput1"  title="이메일"/></span> 
						
					</li>
				</ul>
				<p class="btn"><input type="image" src="/images/mbl/btn/btn_find.gif" alt="찾기" style="width:58px;height:62px;border:none;"/></p>
			</div>		
			</fieldset>
			</form>
			<p class="bline mrt20" id="useridDisplayHtml"></p>
			
			
            <form id="frmLogin" name="frmLogin" method="post" action="/mbl/main/idpwMobileAction.do" onSubmit="return CtlExecutor.requestAjaxUserPwd();">           	
            	
            <fieldset>
            <legend>비밀번호찾기</legend>
			<!-- 비번 찾기 -->
			<h2 class="bul"><img src="/images/mbl/common/tit_fpw.png" alt="비밀번호 찾기" /></h2>
			<div class="memzone">
				<ul class="login idsearch">
					<li>
						<label for="p_userid" class="tit"><img src="/images/mbl/common/tit_id2.gif" alt="아이디" /></label>
						<span><input type="text" name="p_userid_1" value="" id="p_userid_1" class="memput" /></span>
					</li>
					<li>
						<label for="p_name" class="tit"><img src="/images/mbl/common/tit_name.gif" alt="이름" /></label>
						<span><input type="text" name="p_name_1" value="" id="p_name_1" class="memput" /></span>
					</li>
					<li>
						<label for="p_email" class="tit"><img src="/images/mbl/common/tit_cino.gif"  alt="이메일" /></label>
						<span><input type="text" name="p_email_1" value="" id="p_email_1" class="memput" title="이메일"/></span> 
						
					</li>
					<li>
						<label for="p_email" class="tit"><img src="/images/mbl/common/tit_handphone.gif"  alt="핸드폰" /></label>
						<span><input type="text" name="p_hp1" id="p_hp1" maxlength="3" size="4" title="핸드폰 국번" value="010"/> - 
						<input type="text" name="p_hp2" id="p_hp2" maxlength="4" size="5" title="핸드폰 앞자리"/> - 
						<input type="text" name="p_hp3" id="p_hp3" maxlength="4" size="5" title="핸드폰 뒷자리"/></span> 
						
					</li>
				</ul>
				<p class="btn"><input type="image" src="/images/mbl/btn/btn_find.gif" alt="찾기" style="width:58px;height:62px;border:none;"/></p>
			</div>
			<p class="bline mrt20" id="useridDisplayHtmlPwd"></p>
            </fieldset>
            </form>
		</div>
	</div>
	<!-- //본문 -->
</div>
<!-- //wrap-->
<!-- 페이지 스크립트 영역 -->
<script type="text/javascript">
<!--		
var CtlExecutor = {	       
	requestAjaxUserId : function () {
		if($('#txt_p_name').val() == "")
		{
			alert("이름을 입력하세요");
			$('#txt_p_name').focus();
			return false;
		}
		if($('#p_email').val() == "")
		{
			alert("이메일를 입력하세요");
			$('#p_email').focus();
			return false;
		}
		
		 var param = "p_name="+$('#txt_p_name').val()+"&p_email="+$('#p_email').val();
		 
		 $.ajax({
		  type:"post",
		  url : "/com/usr/userIdSearchAction.do",
		  data: param,
		  success:function(data){
				if(data)
				{
					if(data.result != null)
					{
						$('#useridDisplayHtml').html("회원님의 아이디는 <strong class='cyan'>" + data.result.userid + "</strong>입니다.")
					}
					else
					{
						$('#useridDisplayHtml').html("입력하신 정보가 존재하지 않습니다.");
					}
				}
				else
				{
					$('#useridDisplayHtml').html("입력하신 정보가 존재하지 않습니다.");
				}
		  },
		  error:function(e){
		   alert(e);
		  }
		 });
		 return false;
		},

		requestAjaxUserPwd : function () {
			if($('#p_userid_1').val() == "")
			{
				alert("아이디를 입력하세요");
				$('#p_userid_1').focus();
				return false;
			}
			if($('#p_name_1').val() == "")
			{
				alert("이름을 입력하세요");
				$('#p_name_1').focus();
				return false;
			}
			if($('#p_email_1').val() == "")
			{
				alert("이메일를 입력하세요");
				$('#p_email_1').focus();
				return false;
			}
			
			if($('#p_hp1').val() == "")
			{
				alert("핸드폰번호를 입력하세요");
				$('#p_hp1').focus();
				return false;
			}
			if($('#p_hp2').val() == "")
			{
				alert("핸드폰번호를 입력하세요");
				$('#p_hp2').focus();
				return false;
			}
			if($('#p_hp3').val() == "")
			{
				alert("핸드폰번호를 입력하세요");
				$('#p_hp3').focus();
				return false;
			}
			
			
			 var param = "p_userid_1="+$('#p_userid_1').val()+"&p_name_1="+$('#p_name_1').val()+"&p_email_1="+$('#p_email_1').val()+"&p_mode=pwdcheck&p_hp1="+$('#p_hp1').val()+"&p_hp2="+$('#p_hp2').val()+"&p_hp3="+$('#p_hp3').val();
			 
			 $.ajax({
			  type:"post",
			  url : "/com/usr/userIdSearchAction.do",
			  data: param,
			  success:function(data){
					if(data)
					{
						if(data.result != null)
						{
							$('#useridDisplayHtmlPwd').html("임시 비밀번호가 핸드폰으로 발송되었습니다.")
						}
						else
						{
							$('#useridDisplayHtmlPwd').html("입력하신 정보가 존재하지 않습니다.");
						}
					}
					else
					{
						$('#useridDisplayHtmlPwd').html("입력하신 정보가 존재하지 않습니다.");
					}
			  },
			  error:function(e){
			   alert(e);
			  }
			 });
			 return false;
		}
} 
--> 
</script>
</body>
<%@ include file = "/WEB-INF/jsp/egovframework/mbl/com/mblCmmBottom.jsp" %>
