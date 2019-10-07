<%@ page pageEncoding="UTF-8"%>
<div id="LnbWrap">
	<ul class="left_menu">
		<c:if test="${menu_main eq 1}">
		<li><a href="#" onclick="changeMenu(1, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="연수일정" /></a></li>
		<li><a href="#" onclick="changeMenu(1, 2)"><img src="/images/user/left_menu0${menu_main}_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="연수수강 절차"/></a></li>
		<li><a href="#" onclick="changeMenu(1, 3)"><img src="/images/user/left_menu0${menu_main}_3_<c:out value="${menu_sub eq 3 ? 'on' : 'off'}"/>.gif" alt="학습방법" /></a></li>
		<li><a href="#" onclick="changeMenu(1, 4)"><img src="/images/user/left_menu0${menu_main}_4_<c:out value="${menu_sub eq 4 ? 'on' : 'off'}"/>.gif" alt="평가방법" /></a></li>	
	    <li><a href="#" onclick="changeMenu(1, 5)"><img src="/images/user/left_menu0${menu_main}_6_<c:out value="${menu_sub eq 5 ? 'on' : 'off'}"/>.gif" alt="모바일연수안내" /></a></li>
		<%-- <li><a href="#" onclick="changeMenu(1, 5)"><img src="/images/user/left_menu0${menu_main}_5_<c:out value="${menu_sub eq 5 ? 'on' : 'off'}"/>.gif" alt="문의안내" /></a></li> --%>
	    <!--<li><a href="#" onclick="changeMenu(1, 6)"><img src="/images/user/left_menu02_6_<c:out value="${menu_sub eq 6 ? 'on' : 'off'}"/>.gif" alt="교과목 및 시간배당" /></a></li>	-->		
		</c:if>
		<c:if test="${menu_main eq 2}">
		<li><a href="#" onclick="changeMenu(2, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="신청가능한연수" /></a></li>
		<%-- <li><a href="#" onclick="changeMenu(2, 2)"><img src="/images/user/left_menu0${menu_main}_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="개설교육과정"/></a></li> --%>
		<!--<li><a href="#" onclick="changeMenu(3, 1)"><img src="/images/user/left_menu03_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="수강신청안내" /></a></li>-->
		<!--<li><a href="#" onclick="changeMenu(2, 3)"><img src="/images/user/left_menu0${menu_main}_3_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="수강신청 및 취소/반려" /></a></li>-->
		</c:if>
		<c:if test="${menu_main eq 3}">
		<li><a href="#" onclick="changeMenu(3, 1)"><img src="/images/user/left_menu03_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="강의실" /></a></li>
		<li><a href="#" onclick="changeMenu(3, 2)"><img src="/images/user/left_menu03_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="나의 문의함"/></a></li>
		<li><a href="#" onclick="changeMenu(3, 3)"><img src="/images/user/left_menu05_3_<c:out value="${menu_sub eq 3 ? 'on' : 'off'}"/>.gif" alt="영수증 /이수증" /></a></li>
		<li><a href="#" onclick="changeMenu(3, 4)"><img src="/images/user/left_menu05_1_<c:out value="${menu_sub eq 4 ? 'on' : 'off'}"/>.gif" alt="연수지명 번호 입력" /></a></li>
		<li><a href="#" onclick="changeMenu(3, 5)"><img src="/images/user/left_menu03_4_<c:out value="${menu_sub eq 5 ? 'on' : 'off'}"/>.gif" alt="개인정보 관리" /></a></li>
		<%-- <li><a href="#" onclick="changeMenu(3, 5)"><img src="/images/user/left_menu03_4_1_<c:out value="${menu_sub eq 5 ? 'on' : 'off'}"/>.gif" alt="개인정보 수정" /></a></li> --%>
		</c:if>	
		<c:if test="${menu_main eq 4}">
		<li><a href="#" onclick="changeMenu(4, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="공지사항" /></a></li>
		<li><a href="#" onclick="changeMenu(4, 2)"><img src="/images/user/left_menu0${menu_main}_5_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="자주묻는질문" /></a></li>
		<li><a href="#" onclick="changeMenu(4, 3)"><img src="/images/user/left_menu0${menu_main}_6_<c:out value="${menu_sub eq 3 ? 'on' : 'off'}"/>.gif" alt="연수문의" /></a></li>
		<%-- <li><a href="#" onclick="changeMenu(4, 2)"><img src="/images/user/left_menu0${menu_main}_3_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="연수후기" /></a></li> --%>
		<%--<li><a href="#" onclick="changeMenu(4, 2)"><img src="/images/user/left_menu0${menu_main}_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="특수교육자료실"/></a></li>--%>
		<%-- <li><a href="#" onclick="changeMenu(4, 4)"><img src="/images/user/left_menu0${menu_main}_4_<c:out value="${menu_sub eq 4 ? 'on' : 'off'}"/>.gif" alt="연수개선의견"/></a></li>--%>
		</c:if>
		<c:if test="${menu_main eq 5}">
		<%-- <li><a href="#" onclick="changeMenu(5, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="연수지명 번호 입력" /></a></li> --%>
		<li><a href="#" onclick="changeMenu(5, 2)"><img src="/images/user/left_menu0${menu_main}_5_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="인가서 출력" /></a></li>
		<%-- <li><a href="#" onclick="changeMenu(5, 2)"><img src="/images/user/left_menu0${menu_main}_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="출석고사장 입력"/></a></li>
		<li><a href="#" onclick="changeMenu(5, 3)"><img src="/images/user/left_menu0${menu_main}_3_<c:out value="${menu_sub eq 3 ? 'on' : 'off'}"/>.gif" alt="이수증 출력" /></a></li>
		<li><a href="#" onclick="changeMenu(5, 4)"><img src="/images/user/left_menu0${menu_main}_4_<c:out value="${menu_sub eq 4 ? 'on' : 'off'}"/>.gif" alt="영수증 출력" /></a></li>	 --%>
		<%-- <li><a href="#" onclick="changeMenu(5, 6)"><img src="/images/user/left_menu0${menu_main}_6_<c:out value="${menu_sub eq 6 ? 'on' : 'off'}"/>.gif" alt="입금확인 및 환불요청" /></a></li> --%>	
		</c:if>
		<c:if test="${menu_main eq 6}">
		<%-- <li><a href="#" onclick="changeMenu(6, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="나의상담내역" /></a></li>
		<li><a href="#" onclick="changeMenu(6, 2)"><img src="/images/user/left_menu0${menu_main}_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="나의관심과정"/></a></li> --%>
		<li><a href="#" onclick="changeMenu(6, 3)"><img src="/images/user/left_menu0${menu_main}_3_<c:out value="${menu_sub eq 3 ? 'on' : 'off'}"/>.gif" alt="개인정보수정" /></a></li>
		<li><a href="#" onclick="changeMenu(6, 6)"><img src="/images/user/left_menu0${menu_main}_6_<c:out value="${menu_sub eq 6 ? 'on' : 'off'}"/>.gif" alt="비밀번호변경" /></a></li>
		<li><a href="#" onclick="changeMenu(6, 4)"><img src="/images/user/left_menu07_4_<c:out value="${menu_sub eq 4 ? 'on' : 'off'}"/>.gif" alt="개인정보처리방침" /></a></li>
		<li><a href="#" onclick="changeMenu(6, 5)"><img src="/images/user/left_menu0${menu_main}_7_<c:out value="${menu_sub eq 5 ? 'on' : 'off'}"/>.gif" alt="회원탈퇴" /></a></li>
		<li><a href="#" onclick="changeMenu(6, 8)"><img src="/images/user/left_menu0${menu_main}_8_<c:out value="${menu_sub eq 8 ? 'on' : 'off'}"/>.gif" alt="아이디통합" /></a></li>
		<li><a href="#" onclick="leftLifetimeJoin();return false;"><img src="/images/user/left_menu0${menu_main}_10_<c:out value="${menu_sub eq 10 ? 'on' : 'off'}"/>.gif" alt="평생학습계좌" /></a></li>
		</c:if>
		<c:if test="${menu_main eq 7}">
		<li><a href="#" onclick="changeMenu(7, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="회원가입" /></a></li>
		<li><a href="#" onclick="changeMenu(7, 2)"><img src="/images/user/left_menu0${menu_main}_2_<c:out value="${menu_sub eq 2 ? 'on' : 'off'}"/>.gif" alt="아이디/비밀번호찾기"/></a></li>
		<li><a href="#" onclick="changeMenu(7, 3)"><img src="/images/user/left_menu0${menu_main}_3_<c:out value="${menu_sub eq 3 ? 'on' : 'off'}"/>.gif" alt="로그인" /></a></li>
		<li><a href="#" onclick="changeMenu(7, 4)"><img src="/images/user/left_menu0${menu_main}_4_<c:out value="${menu_sub eq 4 ? 'on' : 'off'}"/>.gif" alt="개인정보처리방침" /></a></li>
		<li><a href="#" onclick="changeMenu(7, 5)"><img src="/images/user/left_menu0${menu_main}_5_<c:out value="${menu_sub eq 5 ? 'on' : 'off'}"/>.gif" alt="휴면계정전환" /></a></li>
		</c:if>
		<c:if test="${menu_main eq 8}">
		<li><a href="#" onclick="changeMenu(8, 1)"><img src="/images/user/left_menu0${menu_main}_1_<c:out value="${menu_sub eq 1 ? 'on' : 'off'}"/>.gif" alt="사이트맵" /></a></li>
		</c:if>
	</ul>	
	<div>
		<!--<img src="/images/user/leftbanner_callcenter01.gif" alt="연수지원 : 041)537-1475~8, 평일 09시30분 ~ 17시50분, 원격문의 : 070)7434-2581, 평일 09시30분 ~ 17시50분"/><br/>-->
		<!--<img src="/images/user/m_tit04_1.gif" alt="연수문의/안내, 원격연수운영팀, 연수문의: 041)537-1475~8 평일:09시30분~17시50분, 팩스 : 041)537-1479 , 이메일:jmx386@moe.go.kr, 원격서비스 지원센터, 원격지원 : 070)7434-2581 평일:09시30분~17시50분, 이메일: nurisys@nurisys.co.kr" />-->
		<img src="/images/user/m_tit04_1_1.gif" alt="고객센터 평일:09:30~17:50 점심:11:50~13:00 원격연수운영팀 041)537-1475~9 기술문의/원격지원 02-6345-6787" />
		<a href="https://988.co.kr/ek" title="새창으로 사이트열기" target="blank" ><img src="/images/user/leftbanner_callcenter02_1.gif" alt="PC원격지원서비스 바로가기"/></a>
	</div>
</div>

<script type="text/javascript">
function leftLifetimeJoin() {
	$.ajax({
		url: '${pageContext.request.contextPath}/lifetime/join.do'
		, type: 'post'
		, dataType: 'json'
		, success: function(result) {
			if(result.resultCode == 'SUCCESS' && result.joinYn == 'N') {
				popLifetimeJoin(result);
			} else {
				alert(result.resultMsg);
			}
		}
		, error: function(xhr, status, error) {
			console.log(status);
			console.log(error);   
		}
	});
}
function popLifetimeJoin(obj) {
	if(!confirm('평생학습계좌제 연동을 위해 평생학습계좌제 회원가입을 해주세요. http://www.all.go.kr/center/link/knise 해당페이지에서 계좌제 가입을하게 되며 본인인증하여 계좌제 회원 아이디가 있을 시 해당 계좌제 아이디 정보에 국립특수교육원 사용자아이디 정보가 저장됩니다.\n회원가입하시겠습니까?')) return;
	var windowUrl = "http://www.all.go.kr/center/link/knise/requestOrgLearnAccForm.do";
	
	
	$("#eduForm1 input[name=linkOrg]").val(obj.linkOrg);
	$("#eduForm1 input[name=kniseId]").val(obj.kniseId);
	$("#eduForm1 input[name=userNm]").val(obj.userNm);
	$("#eduForm1 input[name=birthday]").val(obj.birthday);
	$("#eduForm1").attr({
		action : windowUrl
	}).submit();
}
</script>
<form method="post" name="eduForm1" id="eduForm1" target="_blank">
	<input type="hidden" id="linkOrg" name="linkOrg" />
	<input type="hidden" id="kniseId" name="kniseId" />
	<input type="hidden" id="userNm" name="userNm" />
	<input type="hidden" id="birthday" name="birthday" />
</form>