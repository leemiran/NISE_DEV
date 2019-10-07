<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrIndexMain_head.jsp" %>
<!--logIn check-->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/userLoginCheck.jsp" %>

<script type="text/javascript">
//<![CDATA[
<c:if test="${!empty resultMsg}">
	alert("${resultMsg}");
</c:if>
//]]>
</script>

<%@page import="egovframework.com.utl.fcc.service.EgovDateUtil"%>
<form name="studyForm" id="studyForm" method="post" action="">
<input type="hidden" name="p_contenttype"/>
<input type="hidden" name="p_subj"/>
<input type="hidden" name="p_year"/>
<input type="hidden" name="p_subjseq"/>
<input type="hidden" name="p_studytype"/>
<input type="hidden" name="p_process"/>
<input type="hidden" name="p_next_process"/>
<input type="hidden" name="p_height"/>
<input type="hidden" name="p_width"/>
<input type="hidden" name="p_lcmstype"/>
</form>

<form name="<c:out value="${gsMainForm}"/>" id="<c:out value="${gsMainForm}"/>" method="post" onsubmit="return false;" action="">
    <input type="hidden" name="p_d_type" id="p_d_type"	value="O"/>
    
    <fieldset>
        <legend>비밀번호변경</legend>
        <%@ include file="/WEB-INF/jsp/egovframework/usr/com/usrCommonHidden.jsp"%>
        <!-- 비밀번호변경 내용부분 -->
        <p><img src="/images/user/mypage_change1.gif" alt="비밀번호변경안내" longdesc="#msg"/></p>
        <div id="msg" class="blind">
        	비밀번호 변경안내
            기존 비밀번호를 3개월간 변경하지 않으셨습니다.
            안전한 원격교육연수시스템의 이용을 위해 비밀번호 변경을 추천드립니다.
            타 사이트와 동일한 비밀번호를 사용하시는 분께서는 반드시 비밀번호를 변경해 주시기 바랍니다.
        </div>
        <div class="mypage_change">
        <ul>
        <li><label for="p_oldpw">기존 비밀번호</label> :<input type="password" name="p_oldpw" id="p_oldpw" value="" size="40" maxlength="20"/></li>
        <li><label for="p_pw">비밀번호 수정</label> :<input type="password" name="p_pw" id="p_pw" value="" size="40"  maxlength="20"/></li>
        <li><label for="p_pw2">비밀번호 확인</label> :<input type="password" name="p_pw2" id="p_pw2" value="" size="40"  maxlength="20"/></li>
        </ul>
        <p>
        * 비밀번호는 <strong>영문자+숫자+특수문자</strong>를 혼합하여(각 문자 1회 꼭 이용) 8자 이상 입력하셔야 합니다.<br />
        * 비밀번호내에 문자는 최소 2자 이상 입력하셔야 합니다.
        </p>
        </div>
        
        <div class="mypage_change_btn">
        <a href="#" onclick="formsubmit()"><img src="/images/user/mypage_change_btnok.gif" alt="확인" /></a> 
        <a href="/"><img src="/images/user/mypage_change_btncancle.gif" alt="다음에 변경하기" /></a>
        </div>
        <!-- // 비밀번호변경 내용부분 -->
    </fieldset>
</form>  
<script type="text/javascript">
//<![CDATA[
var thisForm = eval('document.<c:out value="${gsMainForm}"/>');
function  formsubmit(){
	if(thisForm.p_oldpw.value==""){
        alert("기존 비밀번호를 입력하여 주십시오") ;
        thisForm.p_oldpw.focus() ;
        return;
    }
	if(thisForm.p_pw.value!=""){
		
		//d_type
		thisForm.p_d_type.value = "T";	
		
        if(thisForm.p_pw.value.length < 7 || thisForm.p_pw.value.length >=20){
            alert("변경할 비밀번호는 최소 8자리이상 20자 미만으로 입력하여 주십시오") ;
            thisForm.p_pw.focus() ;
            thisForm.p_pw.value="" ;
            return;
        }else if(checkPwdEnd()){
            alert("변경할 비밀번호는 숫자,문자,특수문자 조합으로 하되 문자가 2자 이상으로 입력하여 주십시오");
            thisForm.p_pw.focus() ;
            thisForm.p_pw.value="" ;
            return;
        }else if(pwcheck()){
            alert("변경할 비밀번호는 숫자,문자,특수문자 조합으로 하되 문자가 2자 이상으로 입력하여 주십시오");
            thisForm.p_pw.focus() ;
            thisForm.p_pw.value="" ;
            return;
        }else if(thisForm.p_pw.value=='null'){
            alert("변경할 비밀번호는 아이디와 동일할 수 없습니다");
            thisForm.p_pw.focus() ;
            thisForm.p_pw.value="" ;
            return;
        }else if(thisForm.p_pw2.value==""){
            alert("변경할 비밀번호 확인을 위해 한번 더 입력해 주십시오") ;
            thisForm.p_pw2.focus() ;
            return;
        }else if(thisForm.p_pw.value!=thisForm.p_pw2.value){
            alert("변경할 비밀번호와 비밀번호확인이 일치하지 않습니다.") ;
            thisForm.p_pw2.focus() ;
            return;
        }

        
        var c_url   = this.location+"";		
        var url = "https://iedu.nise.go.kr/usr/mpg/pwdChangeAction.do";
		if(c_url.match("localhost")){		
			url = "/usr/mpg/pwdChangeAction.do";
		}	
		
        //thisForm.action = "https://iedu.nise.go.kr/usr/mpg/pwdChangeAction.do";
        thisForm.action = url;
        thisForm.target = "_self";
        thisForm.submit();
	}
	else
	{
		alert("변경할 비밀번호는 최소 8자리이상 20자 미만으로 입력하여 주십시오") ;
        thisForm.p_pw.focus() ;
        thisForm.p_pw.value="" ;
        return;
	}
}

function IntCount(aInputBox) {  // aInputBox에 있는 숫자의 갯수 return
    var comp = "1234567890";
    var aInputBoxValue = aInputBox;
    var len=aInputBoxValue.length;
    var count = 0;
    if(len > 0) {
        for(var i=0;i<len;i++) {
            if(comp.indexOf(aInputBoxValue.substring(i,i+1)) != -1) {
                count++;
            }
        }
    }
    return count;
}

function CharCount(aInputBox) {  // aInputBox에 있는 문자의 갯수 return
    var comp = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var aInputBoxValue = aInputBox;
    var len=aInputBoxValue.length;
    var count = 0;
    if(len > 0) {
        for(var i=0;i<len;i++) {
            if(comp.indexOf(aInputBoxValue.substring(i,i+1)) != -1) {
                count++;
            }
        }
    }
    return count;
}

function SpecialCount(aInputBox) {  // aInputBox에 있는 문자의 갯수 return
    var comp = "\`~!@#$%^&\*()_-+=|\}{[]:\?\.\,";
    var aInputBoxValue = aInputBox;
    var len=aInputBoxValue.length;
    var count = 0;
    if(len > 0) {
        for(var i=0;i<len;i++) {
            if(comp.indexOf(aInputBoxValue.substring(i,i+1)) != -1) {
                count++;
            }
        }
    }
    return count;
}

function checkPwdEnd(){
    var flag = false;
    var intCount = 0;
    var charCount = 0;
    //var IsRepeat = false;
    //숫자+문자 && 문자2개이상 && 동일문자,숫자로 구성되면 안됨
    intCount = IntCount(thisForm.p_pw.value);
    charCount = CharCount(thisForm.p_pw.value);
    specialCount = SpecialCount(thisForm.p_pw.value);
    //IsRepeat = IsRepeat(thisForm.p_pw.value);
    // 문제가 있는 경우  true return
    if( ( intCount==0 || charCount==0) || (charCount<2) ||specialCount==0 )
       flag = true;
    return flag;
}

function pwcheck()
{
    digits = "\`~!@#$%^&\*()_-+=|\}{[]:\?\.\,0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" ;
    j=0 ;
    valpw = thisForm.p_pw.value ;
    for (i=0;i<valpw.length;i++)
    {
        if(digits.indexOf(valpw.charAt(i))<0)
        {
            j=j+1 ;
        }
    }
    if(j>0  )
    {
        return true;
	}
    return false;
}

function onlyNumber(field)
{
	digits = "0123456789" ;
    var chkValue = field.value;
	for (i=0;i<chkValue.length;i++)
	{
		if(digits.indexOf(chkValue.charAt(i))<0)
		{
			field.value = "" ;
            alert("공백없이 숫자만 입력해 주십시오") ;
            field.focus();
            return;
		}
	}
}
//]]>
</script>
         
<!-- button -->
<%@ include file = "/WEB-INF/jsp/egovframework/usr/com/usrCommonBottom.jsp" %>
<!-- // button -->

