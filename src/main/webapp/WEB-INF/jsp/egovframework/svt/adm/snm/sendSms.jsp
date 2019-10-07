<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admIndexMain_head.jsp"%>
<style>
.sms {
	background: url(/images/adm/common/bg_sms.gif) no-repeat;
	width: 234px;
	height: 438px;
}
.sms_input {
	margin: 83px 0px 0px 33px;
	font-size: 12px;
	border: 1px solid #97b0d8;
	padding: 2px;
	vertical-align: top;
	background-color: #bfcfe6;
	color: #152131;
	font-family: "돋움";
	width: 162px;
	height: 170px;
}
.sms .txt {
	padding: 10px 0px 0px 50px;
}
.sms .btn {
	padding: 24px 0px 0px 21px;
}
.file_input_hidden {
	height: 25px;
}
.giWrap {
    overflow: hidden;
    margin-top: 20px;
    width: 500px;
    padding: 7px;
    margin-bottom: 10px;
    margin-left: 10px;
    border: 1px solid #d6d6d6;
    background: #f2f2f2;
    height: 50px;
}
.giWrap .shTit {
    float: left;
    color: #888888;
    padding-left: 5px;
    padding-right: 15px;
    margin-right: 10px;
}
.mtab2 li .on {
    font-size: 14px;
    background: #fff;
    border: 2px solid #265c98;
    color: #333;
    padding-left: 15px;
    font-weight: bold;
    border-bottom: 0;
    /* background: #fff url(/images/adm/ico/ico_arrow1.gif) no-repeat 10px center; */
    letter-spacing: -1px;
}
.mtab2 li {
    width: 263px;
    text-align: center;
}
#tonumberzone {
    display: block;
    width: 218px;
    height: 89px;
    border: 1px solid #c5c5c5;
    min-width: 250px;
    max-width: 250px;
    resize: none;
}
.tbAuto li {
	padding: 5px 0;
}

/* xls */
.txt_sample a.imgExcel01 span {
    display: inline-block;
    zoom: 1;
    padding: 0 0 0 21px;
    background: url(/images/sms/imgExcel01.gif) no-repeat left top;
}
.sendT_scroll {
    margin: 0 0 20px;
    /* min-width: 100%;
    width: 100%; */
    height: 210px;
    overflow: auto;
}
.bbsLstBasic {
    table-layout: fixed; width: 100%;
}
.bbsLstBasic thead th {
    border-top: 1px solid #e5e5e5;
    border-bottom: 1px solid #e5e5e5;
    background-color: #f9f9f9;
    font-weight: bold;
    color: #545454;
}
.bbsLstBasic thead th, .bbsLstBasic tbody td, .bbsLstBasic tbody th {
    padding: 7px 6px;
    text-align: center;
    border: 1px solid #e5e5e5;
    border-width: 1px 1px;
}
.bbsLstBasic tbody td {
    border-bottom: 1px solid #e5e5e5;
    word-break: break-all;
}

.file_box2 {
    margin: 0 0 5px;
    padding: 0 0 14px;
    /* background: url(../images/sms/boxD3.gif) no-repeat left bottom; */
}
.file_box2 .file_area .file_input_div {
    float: left;
    position: relative;
    width: 76px;
    height: 25px;
    overflow: hidden;
}
.file_box2 .file_area .file_input_hidden {
    font-size: 12px;
    position: absolute;
    right: 0px;
    top: 0px;
    opacity: 0;
    filter: alpha(opacity=0);
    -ms-filter: "alpha(opacity=0)";
    -khtml-opacity: 0;
    -moz-opacity: 0;
}
.file_box2 .file_area .file_input_button {
    width: 70px;
    height: 25px;
    line-height: 24px;
    position: absolute;
    top: 0px;
    background-color: #f8f8f8;
    color: #555555;
    border: 1px solid #cccccc;
    margin-left: 5px;
}
.file_box2 .file_area .file_input_textbox {
    float: left;
    padding-left: 5px;
    width: 393px;
    border: 1px solid #adafb5;
    resize: none;
    line-height: 22px;
    height: 19px;
    background: #fff;
}
</style>
<script type="text/javascript">
	<c:if test="${!empty resultMsg}">
		alert("${resultMsg}");
	</c:if>
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.11.0.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/svt/jquery.form.js"></script>
<form name="${gsMainForm}" id="${gsMainForm}" method="post" action="">
	<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonHidden.jsp"%>
	<input type="hidden" name="sendType" id="sendType" value="direct">
	<input type="hidden" name="smsMms" id="smsMms" value="sms">
	<input type="hidden" name="p_subject">
	
	<div style="height: 500px;">
		<!-- 문자발송 -->
		<div class="sms floatL" style="width: 250px;">
			<ul>
				<li><textarea name="content" wrap="VIRTUAL" onKeyUp="checkLength()" rows="15" cols="30" class="sms_input"></textarea></li>
				<li class="txt">글자수 : <span id="noteLen">0</span> / 80Byte
				</li>
				<li class="btn"><a href="#none" onclick="send_sms()"><img src="/images/adm/common/btn_sms.gif" alt="문자발송" /></a></li>
			</ul>
		</div>
		<!--//문자발송 -->
		
		<div class="floatL" style="padding-left: 50px; width: 530px;">
			<div class="conwrap2">
				<ul class="mtab2">
					<li id="tabDirect" class="end"><a href="#" class="on">직접입력</a></li>
					<li id="tabXls" class="end"><a href="#">파일등록(xls)</a></li>
				</ul>
			</div>
			<div id="divDirect" class="popTb tbAuto">
				<ul>
					<li>
						<div class="smsWrap">
							<span class="shTit"><span class="num1">본인이 SMS 수신을 거부한 분에게는 전송되지 않습니다.</span></span>
						</div>
					</li>
					<li style="height: 150px;">
						<h4><label style="float: left; width: 120px;">수신번호 입력</label></h4>
						<div style="float: left;">
							<textarea name="tonumberzone" id="tonumberzone" rows="5" cols="30" onkeyup="chkPhoneNumber2(this)" onblur="chkPhoneNumber2(this)" onfocus="chkPhoneNumber2(this)" title="전화번호 직접입력 엑셀, 메모장 붙여넣기">전화번호 직접입력 엑셀, 메모장 붙여넣기</textarea>
							<p class="send_num"><span class="floatL" id="totPhoneCntDiv">총<strong class="point"> 0</strong>명</span><a href="javacript:;" onclick="delPhoneNumber1(); return false;" class="floatR">전체삭제</a></p>
						</div>
					</li>
				</ul>
			</div>
			<div id="divXls" style="display: none;">
				<div class="giWrap">
					<div class="file_box2">
						<div class="file_area">
							<input type="text" name="dispName" id="dispName" class="file_input_textbox" value="파일 업로드 (xls 파일 형식 지원)" readonly="readonly" title="업로드파일">
							<div class="file_input_div">
								<input type="button" value="찾아보기" class="file_input_button">
								<input type="file" name="tmpFile" id="tmpFile" class="file_input_hidden" title="파일찾기">
							</div>
						</div>
						<p class="txt_sample" style="margin-top: 3px;">
							<a href="javascript:;" onclick="downloadSample(); return false;" class="imgExcel01"><span style="margin-top: 5px;">샘플파일 다운로드</span></a>
						</p>
					</div>
				</div>
				<div class="smsWrap" id="divError" style="display: none;">
					<span class="shTit"><span class="num1">파일 중 유효하지 않거나 정확하지 않은 휴대폰번호가 <u id="errorCnt"></u>개 있습니다.</span></span>
					<a href="javascript:;" id="errorDownload">오류내역 다운로드</a>
				</div>
				<p class="send_num2" style="margin-top: 10px;"><span id="sPhoneCntDiv">총 <strong class="point" id="uploadPoint">0</strong>명</span></p>
				<p class="send_num2" style="margin: 10px 0;"><strong>등록내역 확인</strong>(상위 100건)</p>
				<div class="sendT_scroll" style="padding: 0 0 0 10px;">
					<table class="bbsLstBasic" id="bbsLstBasicMain" summary="휴대폰번호, 이름, 기타에 대한 테이블">
						<caption>SMS전송 주소록 목록</caption>
						<colgroup>
							<col width="20%">
							<col width="40%">
							<col width="40%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col" class="no">No</th>
								<th scope="col">휴대폰번호</th>
								<th scope="col">이름</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td class="no">1</td>
								<td></td>
								<td></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="giWrap">
				<span class="shTit"> <a href="javascript:;" onclick="AddChar('☆');return false;">☆</a> <a href="javascript:;" onclick="AddChar('○');return false;">○</a> <a href="javascript:;" onclick="AddChar('□');return false;">□</a> <a href="javascript:;" onclick="AddChar('◎');return false;">◎</a> <a href="javascript:;" onclick="AddChar('★');return false;">★</a> <a href="javascript:;" onclick="AddChar('●');return false;">●</a> <a href="javascript:;" onclick="AddChar('■');return false;">■</a> <a href="javascript:;" onclick="AddChar('⊙');return false;">⊙</a> <a href="javascript:;" onclick="AddChar('☏');return false;">☏</a> <a href="javascript:;" onclick="AddChar('☎');return false;">☎</a> <a href="javascript:;" onclick="AddChar('◈');return false;">◈</a> <a href="javascript:;" onclick="AddChar('▣');return false;">▣</a> <a href="javascript:;" onclick="AddChar('◐');return false;">◐</a> <a href="javascript:;" onclick="AddChar('◑');return false;">◑</a> <a href="javascript:;"
					onclick="AddChar('☜');return false;">☜</a> <a href="javascript:;" onclick="AddChar('☞');return false;">☞</a> <a href="javascript:;" onclick="AddChar('◀');return false;">◀</a> <a href="javascript:;" onclick="AddChar('▶');return false;">▶</a> <a href="javascript:;" onclick="AddChar('▲');return false;">▲</a> <a href="javascript:;" onclick="AddChar('▼');return false;">▼</a> <a href="javascript:;" onclick="AddChar('♠');return false;">♠</a> <a href="javascript:;" onclick="AddChar('♣');return false;">♣</a> <a href="javascript:;" onclick="AddChar('♥');return false;">♥</a> <a href="javascript:;" onclick="AddChar('◆');return false;">◆</a> <a href="javascript:;" onclick="AddChar('◁');return false;">◁</a> <a href="javascript:;" onclick="AddChar('▷');return false;">▷</a> <a href="javascript:;" onclick="AddChar('△');return false;">△</a> <a href="javascript:;" onclick="AddChar('▽');return false;">▽</a> <a href="javascript:;" onclick="AddChar('♤');return false;">♤</a> <a href="javascript:;"
					onclick="AddChar('♧');return false;">♧</a> <a href="javascript:;" onclick="AddChar('♡');return false;">♡</a> <a href="javascript:;" onclick="AddChar('◇');return false;">◇</a> <a href="javascript:;" onclick="AddChar('※');return false;">※</a> <a href="javascript:;" onclick="AddChar('♨');return false;">♨</a> <a href="javascript:;" onclick="AddChar('♪');return false;">♪</a> <a href="javascript:;" onclick="AddChar('♭');return false;">♭</a> <a href="javascript:;" onclick="AddChar('♩');return false;">♩</a> <a href="javascript:;" onclick="AddChar('♬');return false;">♬</a> <a href="javascript:;" onclick="AddChar('㉿');return false;">㉿</a> <a href="javascript:;" onclick="AddChar('㈜');return false;">㈜</a> <a href="javascript:;" onclick="AddChar('①');return false;">①</a> <a href="javascript:;" onclick="AddChar('②');return false;">②</a> <a href="javascript:;" onclick="AddChar('③');return false;">③</a> <a href="javascript:;" onclick="AddChar('④');return false;">④</a> <a href="javascript:;"
					onclick="AddChar('⑤');return false;">⑤</a> <a href="javascript:;" onclick="AddChar('⑥');return false;">⑥</a> <a href="javascript:;" onclick="AddChar('⑦');return false;">⑦</a> <a href="javascript:;" onclick="AddChar('⑧');return false;">⑧</a> <a href="javascript:;" onclick="AddChar('⑨');return false;">⑨</a> <a href="javascript:;" onclick="AddChar('⑩');return false;">⑩</a> <a href="javascript:;" onclick="AddChar('ⓐ');return false;">ⓐ</a> <a href="javascript:;" onclick="AddChar('ⓑ');return false;">ⓑ</a> <a href="javascript:;" onclick="AddChar('ⓒ');return false;">ⓒ</a> <a href="javascript:;" onclick="AddChar('ⓓ');return false;">ⓓ</a> <a href="javascript:;" onclick="AddChar('ⓔ');return false;">ⓔ</a> <a href="javascript:;" onclick="AddChar('ⓕ');return false;">ⓕ</a> <a href="javascript:;" onclick="AddChar('ⓖ');return false;">ⓖ</a> <a href="javascript:;" onclick="AddChar('ⓗ');return false;">ⓗ</a> <a href="javascript:;" onclick="AddChar('ⓘ');return false;">ⓘ</a> <a href="javascript:;"
					onclick="AddChar('ⓙ');return false;">ⓙ</a> <a href="javascript:;" onclick="AddChar('ⓚ');return false;">ⓚ</a> <a href="javascript:;" onclick="AddChar('ⓛ');return false;">ⓛ</a> <a href="javascript:;" onclick="AddChar('ⓜ');return false;">ⓜ</a> <a href="javascript:;" onclick="AddChar('ⓝ');return false;">ⓝ</a> <a href="javascript:;" onclick="AddChar('ⓞ');return false;">ⓞ</a> <a href="javascript:;" onclick="AddChar('ⓟ');return false;">ⓟ</a> <a href="javascript:;" onclick="AddChar('ⓠ');return false;">ⓠ</a> <a href="javascript:;" onclick="AddChar('ⓡ');return false;">ⓡ</a> <a href="javascript:;" onclick="AddChar('ⓢ');return false;">ⓢ</a> <a href="javascript:;" onclick="AddChar('ⓣ');return false;">ⓣ</a> <a href="javascript:;" onclick="AddChar('ⓤ');return false;">ⓤ</a> <a href="javascript:;" onclick="AddChar('ⓥ');return false;">ⓥ</a> <a href="javascript:;" onclick="AddChar('ⓦ');return false;">ⓦ</a> <a href="javascript:;" onclick="AddChar('ⓧ');return false;">ⓧ</a> <a href="javascript:;"
					onclick="AddChar('ⓨ');return false;">ⓨ</a> <a href="javascript:;" onclick="AddChar('ⓩ');return false;">ⓩ</a> <a href="javascript:;" onclick="AddChar('＠');return false;">＠</a>
				</span>
			</div>
		</div>
	</div>
</form>
<%@ include file="/WEB-INF/jsp/egovframework/adm/com/admCommonBottom.jsp"%>


<script type="text/javascript">
	$(document).ready(function () {
		$('#tabDirect, #tabXls').on('click', function() {
			if($(this).is('#tabDirect')) {
				$(this).find('a').addClass('on');
				$('#tabXls').find('a').removeClass('on');
				
				$('#divDirect').show();
				$('#divXls').hide();
				$('#sendType').val('direct');
			} else {
				$(this).find('a').addClass('on');
				$('#tabDirect').find('a').removeClass('on');
				
				$('#divXls').show();
				$('#divDirect').hide();
				$('#sendType').val('xls');
			}
		});
		
		$('body').on('change', '#tmpFile', function() {
		    var ext = this.value.substring(this.value.lastIndexOf(".")+1,this.value.length);   //파일의 확장자 분리
		    ext = ext.toLowerCase();
		    if (ext!="xls") {
		        alert('첨부파일은 xls 만 가능합니다.');
		        return;
		    }
			
			document.getElementById('dispName').value = this.value;
			ajaxFileUpload($('#tmpFile'), '${pageContext.request.contextPath}/adm/snm/uploadFile.do', function(result) {
	        	$('tbody').html($(result).find('tbody').html());
	        	$('#uploadPoint').text($(result).find('.point').text());
	        	
	        	var errorCnt = Number($(result).find('.errorCnt').text());
	        	if(errorCnt > 0) {
	        		$('#divError').show();
		        	$('#errorCnt').text(errorCnt);
		        	$('#errorDownload').attr('onclick', "errorDownload('" + $(result).find('.errorFile').text() + "');");
	        	} else {
	        		$('#divError').hide();
	        	}
	        });
		});
	});


	var frm = eval('document.<c:out value="${gsMainForm}"/>');
	
	function checkLength() 
	{

		var tmpStr, nStrLen, reserve, f, nStrLen2;

		sInputStr = frm.content.value;
		nStrLen = calculate_byte(sInputStr);
		if ( nStrLen > 80 ) {
			tmpStr = Cut_Str(sInputStr,20);
			reserve = nStrLen - 20;
			//alert("바이트가 초과되었습니다.(최대 80Bytes)\r\n초과된 부분은 전송되지 않습니다."); 
			// 80byte에 맞게 입력내용 수정
			frm.p_subject.value = tmpStr;
			nStrLen2 = calculate_byte(tmpStr);
			frm.smsMms.value = "mms";
			document.all.noteLen.innerHTML = nStrLen;
		} else {
			frm.smsMms.value = "sms";
			document.all.noteLen.innerHTML = nStrLen;
		}
		return;
	}
	
	/* 특수문자 이용 */
	function AddChar (ch){
		var t, msglen;

		msglen = 0;

		frm.content.value = frm.content.value + ch;
		l = frm.content.value.length;
		//document.all["specialcode"].style.display = "none";
		checkLength();
	}

	//바이트수계산
	function calculate_byte( sTargetStr ) 
	{
		var sTmpStr, sTmpChar;
		var nOriginLen = 0;
		var nStrLength = 0;
		 
		sTmpStr = new String(sTargetStr);
		nOriginLen = sTmpStr.length;
	
		for ( var i=0 ; i < nOriginLen ; i++ ) {
			sTmpChar = sTmpStr.charAt(i);
	
			if (escape(sTmpChar).length > 4) {
				nStrLength += 2;
			}else if (sTmpChar!='\r') {
				nStrLength ++;
			}
		}	
		return nStrLength; 	
	}
	
	function Cut_Str( sTargetStr , nMaxLen ) 
	{
		var sTmpStr, sTmpChar, sDestStr;
		var nOriginLen = 0;
		var nStrLength = 0;
		var sDestStr = "";
		sTmpStr = new String(sTargetStr);
		nOriginLen = sTmpStr.length;
	
		for ( var i=0 ; i < nOriginLen ; i++ ) {
			sTmpChar = sTmpStr.charAt(i);
	
			if (escape(sTmpChar).length > 4) {
				nStrLength = nStrLength + 2;
			} else if (sTmpChar!='\r') {
				nStrLength ++;
			}
	 
			if (nStrLength <= nMaxLen) {
				sDestStr = sDestStr + sTmpChar;
			} else {
				break;
			}
		}	
		return sDestStr; 	
	}
	
	function chkPhoneNumber2(obj){
    	if(obj.value == '전화번호 직접입력 엑셀, 메모장 붙여넣기'){
    		obj.value = '';
    	}
		var arrDat = obj.value.split("\n");
		var arrLen = arrDat.length;
		var totNum   = 0;
		if(arrDat[arrLen-1]  == ''){
			totNum = arrLen-1;
		}else{
			totNum = arrLen;
		}
    	document.getElementById("totPhoneCntDiv").innerHTML = "총<strong class='point'> "+totNum+"</strong>명"; 
    }
    
    function delPhoneNumber1(){
		if(confirm('입력한 수신자 정보를 모두 삭제하시겠습니까?')){
			document.getElementById('tonumberzone').value = '';
			return false;
		}
	}
	
	function ajaxFileUpload(files, action, callback)
	{
	    // iframe의 name이자, form의 target
	    var target_name = 'iframe_upload';

	    // form 생성
	    var form = $('<form action="'+action+'" method="post" enctype="multipart/form-data" style="display:none;" target="'+target_name+'"></form>');
	    $('body').append(form);
	    // 전송할 file element를 갖다 붙임
	    
	    var cloneFile = files.clone();
	    files.appendTo(form);

	    // iframe 생성
	    var iframe = $('<iframe src="javascript:false;" name="'+target_name+'" style="display:none;"></iframe>');
	    $('body').append(iframe);

	    // onload 이벤트 핸들러
	    // action에서 파일을 받아 처리한 결과값을 텍스트로 출력한다고 가정하고 iframe의 내부 데이터를 결과값으로 callback 호출
	    iframe.load(function(){
	        var doc = this.contentWindow ? this.contentWindow.document : (this.contentDocument ? this.contentDocument : this.document);
	        var root = doc.documentElement ? doc.documentElement : doc.body;
	        var result = root.textContent ? root.textContent : root.innerText;

	        callback(root);

	        // 전송처리 완료 후 생성했던 form, iframe 제거
	        form.remove();
	        iframe.remove();
	    });
	    
	    form.submit();
	    files.appendTo('.file_input_div');
	}

	function errorDownload(fileid) {
		window.location.href = '${pageContext.request.contextPath}/com/commonFileDownload.do?dir=sms&realfile=errorList.xls&savefile=' + fileid;
		return;
	}

	function send_sms()
	{
		if(frm.content.value == "")
		{
			alert("보내실 내용을 입력하세요!");
			frm.content.focus();
			return;
		}
		
		if (confirm("문자내용을 보내시겠습니까?"))
		{
			checkLength();
		    frm.action = "/adm/snm/sendSmsAction.do";
		    frm.target = "_self";
			frm.submit();
		}
	}
	
	function downloadSample(){
		window.location.href = '${pageContext.request.contextPath}/com/fileDownload.do?fileName=smsList.xls';
		return;
	}
</script>