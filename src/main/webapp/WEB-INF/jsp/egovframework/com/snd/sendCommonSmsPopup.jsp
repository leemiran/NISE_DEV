<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopup_head.jsp" %>


<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">
alert("${resultMsg}");
</c:if>


<c:if test="${isClose}">
window.close();
</c:if>
-->
</script>



<!-- popup wrapper 팝업사이즈 650*370-->
<form name="<c:out value="${gsPopForm}"/>" id="<c:out value="${gsPopForm}"/>" method="post">	
	<input type="hidden" name="smsMms" id="smsMms" />
	<input type="hidden" name="p_subject" id="p_subject" />
		
<div id="popwrapper">
	<div class="popIn">
    	<div class="tit_bg">
			<h2>SMS전송</h2>
         </div>
		<!-- contents -->
		<div class="popCon" style=" overflow:hidden;">
        
        	<!-- 문자발송 -->
			<div class="sms floatL">
            	<ul>
                    <span><textarea name="content" wrap="VIRTUAL" onKeyUp="checkLength()" rows="15" cols="30" class="sms_input"></textarea></span>
                    <li class="txt">글자수 : <span id="noteLen">0</span> / 80Byte </li>
                    <li class="btn"><a href="#none" onclick="send_sms()"><img src="/images/adm/common/btn_sms.gif" alt="문자발송" /></a></li>
                </ul>
            </div>
            <!--//문자발송 -->
            
            <!-- detail wrap -->
            <div class="floatL">
                    <div class="smsWrap">
                        <span class="shTit"><span class="num1">본인이 SMS 수신을 거부한 분에게는 전송되지 않습니다.</span> </span>			
                    </div>
                    <div class="popTb tbAuto">
                        <table summary="아이디,성명,휴대전화로 구성" cellspacing="0" width="100%">
                <caption>발송목록</caption>
                <colgroup>
                                <col width="20%"/>
                                <col width="30%"/>
                                <col width="50%"/>                        
                            </colgroup>
                            <thead>
                                <tr>
                                    <th scope="row">아이디</th>
                                    <th scope="row">성명</th>
                                    <th scope="row">휴대전화</th>                                                
                                </tr>
                            </thead>
                            <tbody>
                                    
					<c:forEach items="${memberList}" var="result">
											<tr>
												<td class="txtC">
													<input type="hidden" name="p_userid" value="${result.userid}">
													<input type="hidden" name="p_handphone" value="${result.handphone}">
													<input type="hidden" name="p_name" value="${result.name}">
													${result.userid}
												</td>
												<td class="txtC">${result.name}</td>
												<td class="txtC">${result.handphone}</td>
											</tr>
					</c:forEach>
							</tbody>
                        </table>
                    </div>
                    
                    <div class="giWrap">
                        <span class="shTit">
                        
		<a  href="javascript:;" onclick="AddChar('☆');return false;">☆</a>
		<a  href="javascript:;" onclick="AddChar('○');return false;">○</a>
		<a  href="javascript:;" onclick="AddChar('□');return false;">□</a>
		<a  href="javascript:;" onclick="AddChar('◎');return false;">◎</a>
		<a  href="javascript:;" onclick="AddChar('★');return false;">★</a>
		<a  href="javascript:;" onclick="AddChar('●');return false;">●</a>
		<a  href="javascript:;" onclick="AddChar('■');return false;">■</a>
		<a  href="javascript:;" onclick="AddChar('⊙');return false;">⊙</a>
		<a  href="javascript:;" onclick="AddChar('☏');return false;">☏</a>
		<a  href="javascript:;" onclick="AddChar('☎');return false;">☎</a>
		<a  href="javascript:;" onclick="AddChar('◈');return false;">◈</a>
		<a  href="javascript:;" onclick="AddChar('▣');return false;">▣</a>
		<a  href="javascript:;" onclick="AddChar('◐');return false;">◐</a>
		<a  href="javascript:;" onclick="AddChar('◑');return false;">◑</a>
		<a  href="javascript:;" onclick="AddChar('☜');return false;">☜</a>
		<a  href="javascript:;" onclick="AddChar('☞');return false;">☞</a>
		<a  href="javascript:;" onclick="AddChar('◀');return false;">◀</a>
		<a  href="javascript:;" onclick="AddChar('▶');return false;">▶</a>
		<a  href="javascript:;" onclick="AddChar('▲');return false;">▲</a>
		<a  href="javascript:;" onclick="AddChar('▼');return false;">▼</a>
		<a  href="javascript:;" onclick="AddChar('♠');return false;">♠</a>
		<a  href="javascript:;" onclick="AddChar('♣');return false;">♣</a>
		<a  href="javascript:;" onclick="AddChar('♥');return false;">♥</a>
		<a  href="javascript:;" onclick="AddChar('◆');return false;">◆</a>
		<a  href="javascript:;" onclick="AddChar('◁');return false;">◁</a>
		<a  href="javascript:;" onclick="AddChar('▷');return false;">▷</a>
		<a  href="javascript:;" onclick="AddChar('△');return false;">△</a>
		<a  href="javascript:;" onclick="AddChar('▽');return false;">▽</a>
		<a  href="javascript:;" onclick="AddChar('♤');return false;">♤</a>
		<a  href="javascript:;" onclick="AddChar('♧');return false;">♧</a>
		<a  href="javascript:;" onclick="AddChar('♡');return false;">♡</a>
		<a  href="javascript:;" onclick="AddChar('◇');return false;">◇</a>
		<a  href="javascript:;" onclick="AddChar('※');return false;">※</a>
		<a  href="javascript:;" onclick="AddChar('♨');return false;">♨</a>
		<a  href="javascript:;" onclick="AddChar('♪');return false;">♪</a>
		<a  href="javascript:;" onclick="AddChar('♭');return false;">♭</a>
		<a  href="javascript:;" onclick="AddChar('♩');return false;">♩</a>
		<a  href="javascript:;" onclick="AddChar('♬');return false;">♬</a>
		<a  href="javascript:;" onclick="AddChar('㉿');return false;">㉿</a>
		<a  href="javascript:;" onclick="AddChar('㈜');return false;">㈜</a>
		<a  href="javascript:;" onclick="AddChar('①');return false;">①</a>
		<a  href="javascript:;" onclick="AddChar('②');return false;">②</a>
		<a  href="javascript:;" onclick="AddChar('③');return false;">③</a>
		<a  href="javascript:;" onclick="AddChar('④');return false;">④</a>
		<a  href="javascript:;" onclick="AddChar('⑤');return false;">⑤</a>
		<a  href="javascript:;" onclick="AddChar('⑥');return false;">⑥</a>
		<a  href="javascript:;" onclick="AddChar('⑦');return false;">⑦</a>
		<a  href="javascript:;" onclick="AddChar('⑧');return false;">⑧</a>
		<a  href="javascript:;" onclick="AddChar('⑨');return false;">⑨</a>
		<a  href="javascript:;" onclick="AddChar('⑩');return false;">⑩</a>
		<a  href="javascript:;" onclick="AddChar('ⓐ');return false;">ⓐ</a>
		<a  href="javascript:;" onclick="AddChar('ⓑ');return false;">ⓑ</a>
		<a  href="javascript:;" onclick="AddChar('ⓒ');return false;">ⓒ</a>
		<a  href="javascript:;" onclick="AddChar('ⓓ');return false;">ⓓ</a>
		<a  href="javascript:;" onclick="AddChar('ⓔ');return false;">ⓔ</a>
		<a  href="javascript:;" onclick="AddChar('ⓕ');return false;">ⓕ</a>
		<a  href="javascript:;" onclick="AddChar('ⓖ');return false;">ⓖ</a>
		<a  href="javascript:;" onclick="AddChar('ⓗ');return false;">ⓗ</a>
		<a  href="javascript:;" onclick="AddChar('ⓘ');return false;">ⓘ</a>
		<a  href="javascript:;" onclick="AddChar('ⓙ');return false;">ⓙ</a>
		<a  href="javascript:;" onclick="AddChar('ⓚ');return false;">ⓚ</a>
		<a  href="javascript:;" onclick="AddChar('ⓛ');return false;">ⓛ</a>
		<a  href="javascript:;" onclick="AddChar('ⓜ');return false;">ⓜ</a>
		<a  href="javascript:;" onclick="AddChar('ⓝ');return false;">ⓝ</a>
		<a  href="javascript:;" onclick="AddChar('ⓞ');return false;">ⓞ</a>
		<a  href="javascript:;" onclick="AddChar('ⓟ');return false;">ⓟ</a>
		<a  href="javascript:;" onclick="AddChar('ⓠ');return false;">ⓠ</a>
		<a  href="javascript:;" onclick="AddChar('ⓡ');return false;">ⓡ</a>
		<a  href="javascript:;" onclick="AddChar('ⓢ');return false;">ⓢ</a>
		<a  href="javascript:;" onclick="AddChar('ⓣ');return false;">ⓣ</a>
		<a  href="javascript:;" onclick="AddChar('ⓤ');return false;">ⓤ</a>
		<a  href="javascript:;" onclick="AddChar('ⓥ');return false;">ⓥ</a>
		<a  href="javascript:;" onclick="AddChar('ⓦ');return false;">ⓦ</a>
		<a  href="javascript:;" onclick="AddChar('ⓧ');return false;">ⓧ</a>
		<a  href="javascript:;" onclick="AddChar('ⓨ');return false;">ⓨ</a>
		<a  href="javascript:;" onclick="AddChar('ⓩ');return false;">ⓩ</a>
		<a  href="javascript:;" onclick="AddChar('＠');return false;">＠</a>
						
						</span>			
                    </div>
               </div>     
               
        <!-- // detail wrap -->
            
		</div>
		<!-- // contents -->

		<!-- button -->
		<ul class="btnCen">
			<li><a href="#none" onclick="window.close()" class="pop_btn01"><span>닫 기</span></a></li>			
		</ul>
		<!-- // button -->
	</div>
</div>
<!-- // popup wrapper -->
</form>

<%@ include file = "/WEB-INF/jsp/egovframework/adm/com/admPopupBottom.jsp" %>

<script type="text/javascript">
	var frm = eval('document.<c:out value="${gsPopForm}"/>');



	<c:if test="${empty isClose && (empty memberList || fn:length(memberList) == 0)}">
		alert("발송대상자가 존재하지 않습니다. 발송대상자가 존재하여야 문자발송이 가능합니다.");
		window.close();
	</c:if>


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
		    frm.action = "/com/snd/sendCommonSmsAction.do";
		    frm.target = "_self";
			frm.submit();
		}
	}
	

	
</script>