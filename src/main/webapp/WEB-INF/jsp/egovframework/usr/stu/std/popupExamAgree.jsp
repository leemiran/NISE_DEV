<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="${pageContext.request.contextPath}/script/jquery/jquery-1-9.1.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/user/popup.css">
</head>
<body>
<div id="popwrapper">
	<div class="con2">
		<div class="popCon">
			<!-- header -->
			<div class="tit_bg">
				<h2>시험 응시 동의</h2>
			</div>
			<!-- //header -->

			<div class="sub_txt_1depth">
				<h5>시험 응시 중 페이지 이동을 하지 마십시오.</h5>
				<ul>
					<li><strong>※ 시험 응시 전 [온라인평가 유의사항] 필독</strong>
						<p class="m10">페이지 이동 또는 페이지 최소화 시 비정상 종료 될 수 있으며, 비정상 종료시 성적에 불이익이 있을 수 있습니다.</p>
						<p class="m10">안내사항을 숙지 후 시험 응시 바랍니다.</p>
						<p class="m10 mrb10">시험을 응시하시겠습니까?</p>
						<p class="m10 mrb10" style="text-align: center;"><input type="checkbox" id="chkExamAgree"><label for="chkExamAgree">동의합니다.</label></p>
					</li>
				</ul>
			</div>

			<!-- button -->
			<ul class="btnCen m15">
				<li><a href="javascript:goExam();" class="pop_btn01"><span>확인</span></a><a href="javascript:window.close();" class="pop_btn01"><span>취소</span></a></li>
			</ul>
			<!-- // button -->
		</div>
	</div>
</div>
</body>

<script type="text/javascript">
function goExam() {
	if(!$('#chkExamAgree').is(':checked')) {
		alert('위 안내사항에 동의해주세요.');
		$('#chkExamAgree').focus();
	} else {
		opener.goExamAnswerPage('${p_lesson}', '${p_examtype}', '${p_papernum}', '${p_exam_subj}')
		window.close();
	}
}
</script>
</html>