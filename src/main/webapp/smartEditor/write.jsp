<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<p>테스트 페이지</p>
<script type="text/javascript" src="<%=request.getContextPath()%>/smartEditor/js/HuskyEZCreator.js" charset="utf-8"></script>

<textarea name="content" id="textAreaContent" rows="10" cols="100" style="width:766px; height:412px; display:none;">
에디터에 기본으로 삽입할 글(수정 모드)이 없다면 이 value 값을 지정하지 않으시면 됩니다.
</textarea>

<script type="text/javascript">
var oEditors = [];
nhn.husky.EZCreator.createInIFrame({
oAppRef: oEditors,
elPlaceHolder: "textAreaContent",
sSkinURI: "<%=request.getContextPath()%>/smartEditor/SmartEditor2Skin.html",
fCreator: "createSEditor2"
});

//‘저장’ 버튼을 누르는 등 저장을 위한 액션을 했을 때 submitContents가 호출된다고 가정한다.
function submitContents(elClickedObj) {
// 에디터의 내용이 textarea에 적용된다.
oEditors.getById["textAreaContent"].exec("UPDATE_CONTENTS_FIELD", []);
// 에디터의 내용에 대한 값 검증은 이곳에서
// document.getElementById("ir1").value를 이용해서 처리한다.
try {
elClickedObj.form.submit();
} catch(e) {}

}

//textArea에 이미지 첨부
var fileName="";
var upfileName="";
function pasteHTML(filepath){
    var sHTML = '<img src="<%=request.getContextPath()%>/smartEditor/photo_uploader/uploadFolder/'+fileName+'"'+" alt="+upfileName+">";

    oEditors.getById["textAreaContent"].exec("PASTE_HTML", [sHTML]); 

}

</script>
</html>