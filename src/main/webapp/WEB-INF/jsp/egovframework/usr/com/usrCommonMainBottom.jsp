<%@ page pageEncoding="UTF-8"%>

<!-- footer -->
<div id="footer">
	<div class="foot1">
		<!-- footer menu -->
		<ul class="footMenu">
			<!-- <li><a href="#" onclick="changeMenu(1, 5)"><img src="/images/user/footer_btn01.gif" alt="원격교육연수원 안내" /></a></li> -->				
			<!-- <li><a href="#" onclick="window.open('/html/usr/fot/inquiryGuide.html','inquiryGuide_open','location=no,directories=no,resizable=no,status=no,toolbar=no,width=800,height=850,scrollbars=yes');return false" title="새창"><img src="/images/user/footer_btn01.gif" alt="원격교육연수원 안내" /></a></li> -->
			<!--<li><a href="#" onclick="changeMenu(7, 1)"><img src="/images/user/footer_btn02.gif" alt="개인정보보호정책 " /></a></li>-->
			<c:if test="${empty sessionScope.userid}">
				<li><a href="#" onclick="changeMenu(7, 4);return false;"><img src="/images/user/footer_btn05.gif" alt="개인정보처리방침 " /></a></li>
			</c:if>
			<c:if test="${not empty sessionScope.userid}">
				<li><a href="#" onclick="changeMenu(6, 4);return false;"><img src="/images/user/footer_btn05.gif" alt="개인정보처리방침 " /></a></li>
			</c:if>

			<li><a href="#" onclick="window.open('/html/usr/fot/email_block.html','email_open','location=no,directories=no,resizable=no,status=no,toolbar=no,width=800,height=365,scrollbars=no');return false" title="새창"><img src="/images/user/footer_btn03.gif" alt="이메일 무단 수집거부" /></a></li>
			<li><a href="#" onclick="window.open('/html/usr/fot/copyrightPolicy.html','copyrightPolicy','location=no,directories=no,resizable=no,status=no,toolbar=no,width=800,height=750,scrollbars=no');return false" title="저작권 정책 새창"><img src="/images/user/footer_btn07.gif" alt="저작권 정책" /></a></li>
			<li class="last"><a href="#" onclick="window.open('/html/usr/fot/copyrightReport.html','copyrightReport','location=no,directories=no,resizable=no,status=no,toolbar=no,width=800,height=520,scrollbars=no');return false" title="저작권 신고 서비스 새창"><img src="/images/user/footer_btn08.gif" alt="저작권 신고 서비스" /></a></li>
		</ul>
		<!-- <p style="text-align: right; padding-right: 10px">
			<a href="http://www.gov30.go.kr/gov30/index.do" target="_blank"> <img src="/images/user/dreambanner.gif" alt="꿈과 끼를 키우는 행복교육 정부 3.0 개방 공유 소통 협력" height="48px" width="180px" /></a>
		</p> -->
	</div>
	<div class="foot2">
		<p class="footlogo">
			<img src="/images/user/footer_logo.gif" alt="교육부 국립특수교육원" />
		</p>
		<ul class="add">
			<li><img src="/images/user/footer_add01.gif" alt="주소 : 충남 아산시 배방읍 공원로 40 TEL : 041.537.1475 FAX : 041.537.1473" /></li>
			<li><img src="/images/user/footer_add02.gif" alt="고유번호:134-83-01004, (국립특수교육원 부설 원격교육연수원) 대표자:김은숙" /></li>
			<li><img src="/images/user/footer_add03.gif" alt="copyright(c)2010 국립특수교육원 all rights reserved" /></li>
		</ul>
		<p style="padding-right:5px;text-align:right">
			<a href="http://www.webwatch.or.kr/Situation/WA_Situation.html?MenuCD=110" target="_blank" ><img src="/images/user/wa20180319.jpg" border="0" alt="과학기술정보통신부 WEB ACCESSIBILITY 마크(웹 접근성 품질인증 마크)" title="국가 공인 인증기관 : 웹와치 (인증현황페이지로 새창열림)"  height="53px" width="88px" /></a> 
		</p>
		<!-- <p style="padding-top: 5px">
			<a href="http://www.gov30.go.kr/gov30/index.do" target="_blank"> <img src="/images/user/dreambanner.gif" alt="꿈과 끼를 키우는 행복교육 정부 3.0 개방 공유 소통 협력" height="48px" width="180px" /></a>
		</p> -->
	</div>
</div>
<!-- // footer -->
<!--  퀵메뉴-->
<div id="quick">
	<div class="quickm">
		<script type="text/javascript">initMoving(document.getElementById("quick"),100,66,250);</script>
		<div class="quick_img">
			<ul>
				<li><img src="/images/user/quick1.png" alt="퀵메뉴타이틀" /></li>
				<li><a href="#" onclick="changeMenu(3, 3);return false;"><img src="/images/user/quick2.jpg" alt="영수증/이수증" /></a></li>
				<li><a href="#" onclick="changeMenu(3, 4);return false;"><img src="/images/user/quick3.jpg" alt="지명번호 등록/변경" /></a></li>
				<li><a href="#" onclick="changeMenu(1, 1);return false;"><img src="/images/user/quick4.jpg" alt="연수일정" /></a></li>
				<li><a href="#" onclick="changeMenu(4, 3);return false;"><img src="/images/user/quick5.jpg" alt="연수문의" /></a></li>
				<li><a href="#" onclick="changeMenu(5, 2);return false;"><img src="/images/user/quick6.jpg" alt="교육부인가서" /></a></li>

			</ul>
		</div>
	</div>
</div>
<!--  퀵메뉴-->
<!-- 페이지 정보 -->
<%@ include file="/WEB-INF/jsp/egovframework/com/lib/pageName.jsp"%>
<!-- 페이지 정보 -->
<!-- // wrapper -->

<style>
.all_knise_modal_popup {
	min-width: 817px;
	max-width: 817px;
	min-height: 600px;
	display: none;
	/**/
}
.b-close {
	border-radius: 7px;
	font: bold 131% sans-serif;
	padding: 0 6px 2px;
	background-color: #2b91af;
	color: #fff;
	cursor: pointer;
	text-align: center;
	text-decoration: none;
	position: absolute;
	right: -7px;
	top: -7px;
}
.b-close:hover {
	background-color: #1e1e1e;
}
</style>

<div class="all_knise_modal_popup">
	<div class="b-close">Close</div>
	<div class="all_knise_modal_popup_content"></div>
</div>

</body>
</html>