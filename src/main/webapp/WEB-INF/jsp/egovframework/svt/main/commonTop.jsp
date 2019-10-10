<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div id="header">
	<div id="wrapper">
		<div class="wrap">
			<div class="top_menu">
				<!-- utilmenu -->
				<ul class="utilmenu">
					<!-- login, logout -->
					<c:choose>
						<c:when test="${empty sessionScope.userid}">
							<li class="top_login"><a href="/usr/mem/userLogin.do" onclick="changeMenu(7, 3);return false;"><img src="/images/user/btn_login.gif" alt="로그인" /></a></li>
						</c:when>
						<c:otherwise>
							<li class="top_login"><a href="/usr/lgn/portalUserLogout.do" onclick="fn_UserLogout();return false;"><img src="/images/user/btn_logout.gif" alt="로그아웃" /></a></li>
						</c:otherwise>
					</c:choose>
					<li><a href="/"><img src="/images/user/btn_home.gif" alt="HOME" /></a></li>
					<!-- 회원가입, 마이페이지 -->
					<c:choose>
						<c:when test="${empty sessionScope.userid}">
							<li><a href="/usr/mem/memJoinStep01.do" onclick="changeMenu(7, 1);return false;"><img src="/images/user/top_btn_join.gif" alt="회원가입" /></a></li>
						</c:when>
						<c:otherwise>
							<li><a href="/usr/mpg/memMyQnaList.do" onclick="changeMenu(6, 3);return false;"><img src="/images/user/top_btn_mypage.gif" alt="마이페이지" /></a></li>
						</c:otherwise>
					</c:choose>
					<li><a href="/usr/svc/faqList.do" onclick="changeMenu(4, 3);return false;"><img src="/images/user/btn_faq.gif" alt="FAQ" /></a></li>
					<li class="last"><a href="/usr/sit/siteMap.do" onclick="changeMenu(8, 1);return false;"><img src="/images/user/btn_sitemap.gif" alt="사이트맵" /></a></li>
				</ul>
				<!-- //utilmenu -->
			</div>
			<div class="clear"></div>
			<div class="logo">
				<a href="/"><img src="/images/common/main_17.jpg" alt="교육부 국립특수교육원 부설 원격교육연수원" /></a>
			</div>
			<!-- gnb -->
			<div id="gnb" tabindex="0">
				<ul class="list">
					<li><a href="javascript:;" id="m1" onclick="changeMenu(1, 1);return false;"><img src="/images/user/main_b1.jpg" alt="연수안내" /></a>
						<div class="depth02">
							<div class="depth02_bottom">
								<ul>
									<li><a href="javascript:;" onclick="changeMenu(1, 1);return false;">연수일정</a></li>
									<li><a href="javascript:;" onclick="changeMenu(1, 2);return false;">연수수강 절차</a></li>
									<li><a href="javascript:;" onclick="changeMenu(1, 3);return false;">학습방법</a></li>
									<li><a href="javascript:;" onclick="changeMenu(1, 4);return false;">평가방법</a></li>
									<li class="last"><a href="javascript:;" onclick="changeMenu(1, 5);return false;">모바일연수안내</a></li>
									<!-- <li><a href="javascript:;" onclick="changeMenu(1, 5);return false;">원격교육연수원 조직안내</a></li> -->
								</ul>
							</div>
						</div></li>
					<li class="li_left"><a href="javascript:;" id="m2" onclick="changeMenu(2, 1);return false;"><img src="/images/user/main_b2.jpg" alt="연수신청" /></a>
						<div class="depth02">
							<div class="depth02_bottom">
								<ul>
									<li><a href="javascript:;" onclick="changeMenu(2, 1);return false;">신청 가능한 연수</a></li>
									<!-- <li class="last"><a href="javascript:;" onclick="changeMenu(2, 2);return false;">개설교육과정</a></li> -->
								</ul>
							</div>
						</div></li>
					<li class="li_left"><a href="javascript:;" id="m3" onclick="changeMenu(3, 1);return false;"><img src="/images/user/main_b3.jpg" alt="나의강의실" /></a>
						<div class="depth02">
							<div class="depth02_bottom">
								<ul>
									<li><a href="javascript:;" onclick="changeMenu(3, 1);return false;">강의실</a></li>
									<li><a href="javascript:;" onclick="changeMenu(3, 2);return false;">나의문의함</a></li>
									<li><a href="javascript:;" onclick="changeMenu(3, 3);return false;">영수증/이수증</a></li>
									<!-- <li><a href="javascript:;" onclick="changeMenu(3, 4);return false;">연수지명 번호 입력</a></li> -->
									<li><a href="javascript:;" onclick="changeMenu(3, 5);return false;">개인정보 수정</a></li>
									<!-- <li class="last"><a href="javascript:;" onclick="changeMenu(3, 5);return false;">&nbsp;-&nbsp;개인정보 수정</a></li> -->
									<!-- <li class="last"><a href="javascript:;" onclick="changeMenu(3, 2);return false;">&nbsp;-&nbsp;회원탈퇴</a></li> -->
								</ul>
							</div>
						</div></li>
					<li class="li_left"><a href="javascript:;" id="m4" onclick="changeMenu(4, 1);return false;"><img src="/images/user/main_b4.jpg" alt="참여마당" /></a>
						<div class="depth02">
							<div class="depth02_bottom">
								<ul>
									<li><a href="javascript:;" onclick="changeMenu(4, 1);return false;">공지사항</a></li>
									<li><a href="javascript:;" onclick="changeMenu(4, 2);return false;">자주하는 질문</a></li>
									<li class="last"><a href="javascript:;" onclick="changeMenu(4, 3);return false;">연수문의</a></li>
								</ul>
							</div>
						</div></li>
					<li class="li_left"><a href="javascript:;" id="m5" onclick="window.open('/html/usr/fot/inquiryGuide.html','inquiryGuide_open','location=no,directories=no,resizable=no,status=no,toolbar=no,width=800,height=850,scrollbars=yes');return false" title="새창"><img src="/images/user/main_b5.jpg" alt="연수원소개" /></a>
						<div class="depth02">
							<div class="depth02_bottom">
								<ul>
									<li><a href="#" onclick="window.open('/html/usr/fot/inquiryGuide.html','inquiryGuide_open','location=no,directories=no,resizable=no,status=no,toolbar=no,width=800,height=850,scrollbars=yes');return false" title="새창">조직안내</a></li>
								</ul>
							</div>
						</div></li>	
				</ul>
			</div>
			<!-- // gnb -->
		</div>
	</div>
</div>
<span id="menu_end" tabindex="0"></span>
<form id="menuForm" name="menuForm" method="post" action="">
	<input type="hidden" name="menu_main" value="<c:out value="${menu_main}"  escapeXml="true" />" />
	<input type="hidden" name="menu_sub" value="<c:out value="${menu_sub}"  escapeXml="true" />" />
	<input type="hidden" name="menu_tab_title" value="<c:out value="${menu_tab_title}"  escapeXml="true" />" />
	<input type="hidden" name="menu_sub_title" value="<c:out value="${menu_sub_title}"  escapeXml="true" />" />
</form>
<script type="text/javascript">
	function changeMenu(main, sub) {
		//alert(main + " / " + sub);                                                                                                                                                            
		var menuFrm = document.getElementById("menuForm");
		menuFrm.menu_main.value = main;
		menuFrm.menu_sub.value = sub;
		menuFrm.action = getActionURL(main, sub);
		menuFrm.target = "_self";
		menuFrm.submit();
	}

	function getActionURL(p_main, p_sub) {
		var menuFrm = document.getElementById("menuForm");
		var url = "/usr/hom/portalActionMainPage.do"; //메인페이지                                                                                                                                   
		var tab = "";
		var sub = "";
		var curYear = new Date().getFullYear();
		if (p_main == 1) { // 연수안내                                                                                                                                                              
			tab = "연수안내";
			switch (p_sub) {
			case 1:
				//url = "/usr/info/eduSubjectList.do"; //연수과정안내
				url = "/usr/info/eduTrainingSchedule.do"; //연수과정안내
				sub = curYear+"년도 연수 일정";
				break;
			case 2:
				url = "/usr/info/registerProcess.do"; //연수수강절차                                                                                                                                  
				sub = "연수수강 절차";
				break;
			case 3:
				url = "/usr/info/studyMethod.do"; //학습방법                                                                                                                                        
				sub = "학습방법";
				break;
			case 4:
				url = "/usr/info/evaluationMethod.do"; //평가방법                                                                                                                                   
				sub = "평가방법";
				break;
			case 5:
				url = "/usr/info/apguideView.do"; //모바일연수안내                                                                                                                                     
				sub = "모바일연수안내";
				break;
			//case 5:
			//	url = "/usr/info/inquiryGuide.do"; //문의안내                                                                                                                                       
			//	sub = "조직안내";
			//	break;
			//case 6:                                                                                                                                                                           
			//url = "/usr/info/courseSchedule.do";	//연수일정                                                                                                                                      
			//sub = "교과목 및 시간배당";                                                                                                                                                               
			//break;                                                                                                                                                                            
			default:
				//url = "/usr/info/eduSubjectList.do"; //연수과정 안내    
				url = "/usr/info/eduTrainingSchedule.do"; //연수과정 안내				
				sub = curYear+"년도 연수 일정";
				break;
			}
		} else if (p_main == 2) { // 연수과정/안내및신청                                                                                                                                                 
			tab = "연수신청";
			switch (p_sub) {
			case 1:
				//url = "/usr/subj/subjInfoList.do"; //신청 가능한 과정                                                                                                                                  
				url = "/usr/subj/subjInfoSearchList.do"; //신청 가능한 과정
				sub = "신청 가능한 연수";
				break;
			case 2:
				url = "/usr/subj/subjInfoSearchList.do"; //개설교육과정                                                                                                                             
				sub = "개설교육과정";
				break;
			/*                                                                                                                                                                                  
				case 1:                                                                                                                                                                         
				url = "/usr/subj/subjGuideList.do";	//수강신청안내                                                                                                                                    
				sub = "수강신청안내";                                                                                                                                                                 
				break;                                                                                                                                                                          
			 */
			/*                                                                                                                                                                                  
			 case 2:                                                                                                                                                                            
			 url = "/usr/subj/propCancelList.do";	//수강신청 확인/취소                                                                                                                                
			 sub = "수강신청 확인/취소";                                                                                                                                                                
			 break;                                                                                                                                                                             
			 */
			default:
				//url = "/usr/subj/subjInfoList.do"; //신청 가능한 과정                                                                                                                                  
				url = "/usr/subj/subjInfoSearchList.do"; //신청 가능한 과정
				sub = "신청 가능한 연수";
				break;
			}
		} else if (p_main == 3) { //나의강의실                                                                                                                                                       
			tab = "나의강의실";
			switch (p_sub) {
			case 1:
				url = "/usr/stu/cou/courseStudyList.do"; //나의 학습과정                                                                                                                              
				sub = "강의실";
				break;
			case 2:
				url = "/usr/myh/myCursQnaList.do"; //나의 질문방                                                                                                                                     
				sub = "나의문의함";
				break;
			case 3:
				url = "/usr/svc/userService03.do"; //이수증 출력                                                                                                                                     
				sub = "영수증/이수증";
				break;
			case 4:
				url = "/usr/svc/userService01.do"; //이수증 출력                                                                                                                                     
				sub = "연수지명 번호 입력";
				break;
			case 5:
				url = "/usr/mpg/memMyPage.do"; //연수결재내역                                                                                                                                   
				sub = "개인정보수정";
				break;
			//case 6:
			//	url = "/usr/myh/myStudyHisList.do"; //나의 연수 이력                                                                                                                                  
			//	sub = "나의 연수 이력";
			//	break;
			//case 3:
			//	url = "/usr/svc/userService03.do"; //연수수강 내역                                                                                                                                  
			//	sub = "연수수강 내역";
			//	break;
			//case 4:
			//	url = "/usr/myh/celOfficeList02.do"; //출석고사 내역                                                                                                                                  
			//	sub = "출석고사 내역";
			//	break;
			default:
				url = "/usr/stu/cou/courseStudyList.do"; //나의 학습과정                                                                                                                              
				sub = "강의실";
				break;
			}
		} else if (p_main == 4) { //참여마당                                                                                                                                                        
			tab = "참여마당";
			switch (p_sub) {
			case 1:
				url = "/usr/bod/noticeList.do"; //공지사항                                                                                                                                          
				sub = "공지사항";
				break;
			case 2:
				url = "/usr/svc/faqList.do"; //자주하는 질문                                                                                                                                          
				sub = "자주하는 질문";
				break;
			case 3:
				url = "/usr/svc/qnaList01.do"; //연수관련상담                                                                                                                                         
				sub = "연수문의";
				break;
			//case 2:
			//	url = "/usr/bod/opinionUpgradeWrite.do"; //연수개선의견                                                                                                                               
			//	sub = "연수개선의견";
			//	break;
			//case 2:                                                                                                                                                                           
			//	url = "/usr/bod/dataList.do"; //특수교육자료실                                                                                                                                         
			//	sub = "특수교육자료실";                                                                                                                                                                
			//	break;                                                                                                                                                                          
			//case 4:                                                                                                                                                                           
			//	url = "/usr/bod/opinionUserList.do"; //연수후기                                                                                                                                     
			//	sub = "연수후기";                                                                                                                                                                   
			//	break;                                                                                                                                                                          
			default:
				url = "/usr/bod/noticeList.do"; //공지사항                                                                                                                                          
				sub = "공지사항";
				break;
			}
		} else if (p_main == 5) { //연수행정                                                                                                                                                        
			tab = "연수행정";
			switch (p_sub) {
			case 1:
				url = "/usr/svc/userService01.do"; //연수지명 번호 입력                                                                                                                                 
				sub = "연수지명 번호 입력";
				break;
			case 2:
				url = "/usr/svc/userService05.do"; //인가서 출력                                                                                                                                     
				sub = "인가서 출력";
				break;
			//case 2:
			//	url = "/usr/svc/userService02.do"; //출석고사장 입력                                                                                                                                   
			//	sub = "출석고사장 입력";
			//	break;
			//case 4:
			//	url = "/usr/svc/userService04.do"; //영수증 출력                                                                                                                                     
			//	sub = "영수증 출력";
			//	break;
			//case 6:
			//	url = "/usr/svc/qnaList02.do"; //입금확인 및 환불 요청                                                                                                                                   
			//	sub = "입금확인 및 환불 요청";
			//	break;
			default:
				url = "/usr/svc/qnaList03.do"; //연수지명 번호 입력                                                                                                                                     
				sub = "연수지명 번호 입력";
				break;
			}
		} else if (p_main == 6) { //마이페이지                                                                                                                                                       
			tab = "마이페이지";
			switch (p_sub) {
			case 1:
				url = "/usr/mpg/memMyQnaList.do"; //나의 상담내역                                                                                                                                     
				sub = "나의 상담내역";
				break;
			case 2:
				url = "/usr/mpg/concernInfoList.do"; //나의 관심과정                                                                                                                                  
				sub = "나의 관심과정";
				break;
			case 3:
				url = "/usr/mpg/memMyPage.do"; //개인정보수정                                                                                                                                         
				sub = "개인정보수정";
				break;
			case 4:
				url = "/usr/mpg/copyRight.do"; //개인정보처리방침                                                                                                                                       
				sub = "개인정보처리방침";
				break;
			case 5:
				url = "/usr/mpg/memberOut.do"; //회원탈퇴                                                                                                                                           
				sub = "회원탈퇴";
				break;
			case 6:
				url = "/usr/mpg/pwdChange.do"; //비밀번호변경                                                                                                                                         
				sub = "비밀번호변경";
				break;
			case 8:
				url = "/usr/mem/idIntergrationIdSearch.do"; //아이디통합                                                                                                                             
				sub = "아이디통합";
				break;
			case 9:
				url = "/usr/mpg/copyRight1.do";
				sub = "개인정보처리방침";
				break;
			default:
				url = "/usr/mpg/memMyQnaList.do"; //나의 상담내역                                                                                                                                     
				sub = "나의 상담내역";
				break;
			}
		} else if (p_main == 7) { //MEMBER                                                                                                                                                      
			tab = "회원가입";
			switch (p_sub) {
			case 1:
				url = "/usr/mem/memJoinStep01.do"; //회원가입                                                                                                                                       
				sub = "회원가입";
				break;
			case 2:
				url = "/usr/mem/userIdPwdSearch01.do"; //아이디/비밀번호찾기                                                                                                                             
				sub = "아이디/비밀번호찾기";
				break;
			case 3:
				url = "/usr/mem/userLogin.do"; //로그인                                                                                                                                            
				sub = "로그인";
				break;
			case 4:
				url = "/usr/mem/copyRight.do"; //개인정보처리방침                                                                                                                                       
				sub = "개인정보처리방침";
				break;
			case 5:
				url = "/usr/mem/memDormantPage01.do"; //휴면회원                                                                                                                                    
				sub = "휴면계정전환";
				break;
			default:
				url = "/usr/mem/memJoinStep01.do"; //회원가입                                                                                                                                       
				sub = "회원가입";
				break;
			}
		} else if (p_main == 8) { //사이트맵                                                                                                                                                        
			tab = "사이트맵";
			switch (p_sub) {
			case 1:
				url = "/usr/sit/siteMap.do"; //사이트맵                                                                                                                                             
				sub = "사이트맵";
				break;
			default:
				url = "/usr/sit/siteMap.do"; //사이트맵                                                                                                                                             
				sub = "사이트맵";
				break;
			}
		}

		menuFrm.menu_tab_title.value = tab;
		menuFrm.menu_sub_title.value = sub;
		return url;
	}

	function fn_keyEvent(fun) {
		if (event.keyCode == 13) {
			eval("this." + fun + "()");
		}
	}

	function fn_UserLogout(fun) {
		if (confirm("로그아웃 하시겠습니까?")) {
			var url = "/usr/lgn/portalUserLogout.do";
			window.location.replace(url);
		} else {
			return;
		}
	}
</script>