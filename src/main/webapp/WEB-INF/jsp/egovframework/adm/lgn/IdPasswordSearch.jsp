<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
 /**
  * @Class Name : EgovIdPasswordSearch.jsp
  * @Description : 아이디/비밀번호 찾기 화면
  * @Modification Information
  * @
  * @  수정일         수정자                   수정내용
  * @ -------    --------    ---------------------------
  * @ 2009.03.09    박지욱          최초 생성
  *
  *  @author 공통서비스 개발팀 박지욱
  *  @since 2009.03.09
  *  @version 1.0
  *  @see
  *  
  */
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="/css/egovframework/cmm/uat/uia/com.css" type="text/css">
<title>MOPAS 아이디/비밀번호 찾기</title>
<script>



function fnSearchId() {
	
	document.idForm.userSe.value = "USR";
	
	if (document.idForm.name.value =="") {
		alert("이름을 입력하세요");
	} else if (document.idForm.email.value =="") {
		alert("가입시 이메일주소를 입력하세요");
	} else {
		//window.open("/cmm/uat/uia/searchId.do?id=");
		document.idForm.target = "_blank";
		document.idForm.submit();
	}
}

function fnSearchPassword() {

	document.passwordForm.userSe.value = "USR";
	
	if (document.passwordForm.id.value =="") {
		alert("아이디를 입력하세요");
	} else if (document.passwordForm.name.value =="") {
		alert("이름을 입력하세요");	
	} else if (document.passwordForm.email.value =="") {
		alert("가입시 이메일주소를 입력하세요");
	} else if (document.passwordForm.passwordHint.value =="") {
		alert("비밀번호 힌트를 선택하세요");
	} else if (document.passwordForm.passwordCnsr.value =="") {
		alert("비밀번호 정답을 입력하세요");		
	} else {
		document.passwordForm.target = "_blank";
		document.passwordForm.submit();
	}
}

</script>
</head>
<body>
  <table width="700" border="1">
    <tr>
      <td width="350" height="250">
      
	      <!--아이디찾기 테이블 시작-->
	      <form name="idForm" action ="<c:url value='/adm/lgn/searchId.do'/>" method="post">
			<table width="303" border="0" cellspacing="8" cellpadding="0">
		      <tr>
				<td width="40%"class="title_left"><img src="/images/egovframework/cmm/uat/uia/tit_icon.gif" width="16" height="16" hspace="3" align="absmiddle">&nbsp;아이디 찾기</td>
		      </tr>
		      <tr>
				<td width="303" height="210" valign="top">
					<table width="303" border="0" align="center" cellpadding="0" cellspacing="0">
					  <tr>
					    <td height="5">&nbsp;</td>
					  </tr>
					  <tr>
					    <td>
					    	<table border="0" cellpadding="0" cellspacing="0" style="width:250px;margin-left:20px;">					
				             
				              <tr>
							    <td height="1">&nbsp;</td>
							  </tr>
				            </table>
						    <table border="0" cellpadding="0" cellspacing="0" style="width:250px;margin-left:20px;">
						      <tr>
								<td>
									<table width="142" border="0" cellpadding="0" cellspacing="0">
									  <tr>
									    <td class="required_text" nowrap>이름&nbsp;&nbsp;</td>
									    <td><input type="text" name="name" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px;" tabindex="4" maxlength="20"/></td>
									    <td/>
									  </tr>
									  <tr>
									    <td class="required_text" nowrap>이메일&nbsp;&nbsp;</td>
									    <td><input type="text" name="email" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px; ime-mode: disabled;" maxlength="30" tabindex="5" /></td>
									    <td/>
									  </tr>
									</table>
								</td>
					  		  </tr>
					  		  <tr>
					  		  	<td height="30">&nbsp;</td>
					  		  </tr>
					  		  <tr>
							    <td>
									<table border="0" cellspacing="0" cellpadding="0">
									  <tr>
									    <td><img src="/images/egovframework/cmm/uat/uia/bu2_left.gif" width="8" height="20"></td>
										<td background="/images/egovframework/cmm/uat/uia/bu2_bg.gif" class="text_left" nowrap><center><a href="javascript:fnSearchId();">아이디찾기</a></center></td>
										<td><img src="/images/egovframework/cmm/uat/uia/bu2_right.gif" width="8" height="20"></td>
									  </tr>
									</table>
								</td>
							  </tr>
							</table>
			    		</td>
			  		  </tr>
			  		  <tr>
					    <td height="2">&nbsp;</td>
					  </tr>
					</table>
				</td>
		      </tr>
		    </table>
		    <input name="userSe" type="hidden" value="GNR">
	    </form>
	    <!--아이디찾기 테이블 끝-->
      </td>
      <td width="350" height="250">
      	<!--비밀번호찾기 테이블 시작-->
	    <form name="passwordForm" action ="<c:url value='/adm/lgn/searchPassword.do'/>" method="post">
			<table width="303" border="0" cellspacing="8" cellpadding="0">
		      <tr>
				<td width="40%"class="title_left"><img src="/images/egovframework/cmm/uat/uia/tit_icon.gif" width="16" height="16" hspace="3" align="absmiddle">&nbsp;비밀번호 찾기</td>
		      </tr>
		      <tr>
				<td width="303" height="210" valign="top">
					<table width="303" border="0" align="center" cellpadding="0" cellspacing="0">
					  <tr>
					    <td height="5">&nbsp;</td>
					  </tr>
					  <tr>
					    <td>
					    	<table border="0" cellpadding="0" cellspacing="0" style="width:250px;margin-left:20px;">					
				              
				              <tr>
							    <td height="1">&nbsp;</td>
							  </tr>
				            </table>
						    <table border="0" cellpadding="0" cellspacing="0" style="width:250px;margin-left:20px;">
						      <tr>
								<td>
									<table width="142" border="0" cellpadding="0" cellspacing="0">
									  <tr>
									    <td class="required_text" nowrap>아이디&nbsp;&nbsp;</td>
									    <td><input type="text" name="id" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px; ime-mode: disabled;" tabindex="9" maxlength="15"/></td>
									    <td/>
									  </tr>
									  <tr>
									    <td class="required_text" nowrap>이름&nbsp;&nbsp;</td>
									    <td><input type="text" name="name" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px;" tabindex="10" maxlength="20"/></td>
									    <td/>
									  </tr>
									  <tr>
									    <td class="required_text" nowrap>이메일&nbsp;&nbsp;</td>
									    <td><input type="text" name="email" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px; ime-mode: disabled;" maxlength="30" tabindex="11" /></td>
									    <td/>
									  </tr>
									  <tr>
									    <td class="required_text" nowrap>비밀번호 힌트&nbsp;&nbsp;</td>
									    <td>
									      <select name="passwordHint" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px;" class="select" tabindex="12">
										    <option selected value=''>--선택하세요--</option>
										    <c:forEach var="result" items="${pwhtCdList}" varStatus="status">
											<option value='<c:out value="${result.code}"/>'><c:out value="${result.codeNm}"/></option>
											</c:forEach>
										  </select>
									    <td/>
									  </tr>
									  <tr>
									    <td class="required_text" nowrap>비밀번호 정답&nbsp;&nbsp;</td>
									    <td><input type="text" name="passwordCnsr" style="height: 16px; width: 150px; border: 1px solid #CCCCCC; margin: 0px; padding: 0px;" maxlength="50" tabindex="13" /></td>
									    <td/>
									  </tr>
									</table>
								</td>
					  		  </tr>
					  		  <tr>
					  		  	<td height="30">&nbsp;</td>
					  		  </tr>
					  		  <tr>
							    <td>
									<table border="0" cellspacing="0" cellpadding="0">
									  <tr>
									    <td><img src="/images/egovframework/cmm/uat/uia/bu2_left.gif" width="8" height="20"></td>
										<td background="/images/egovframework/cmm/uat/uia/bu2_bg.gif" class="text_left" nowrap><center><a href="javascript:fnSearchPassword();">비밀번호찾기</a></center></td>
										<td><img src="/images/egovframework/cmm/uat/uia/bu2_right.gif" width="8" height="20"></td>
									  </tr>
									</table>
								</td>
							  </tr>
							</table>
			    		</td>
			  		  </tr>
			  		  <tr>
					    <td height="2">&nbsp;</td>
					  </tr>
					</table>
				</td>
		      </tr>
		    </table>
		    <input name="userSe" type="hidden" value="">
	    </form>
	    <!--비밀번호찾기 테이블 끝-->
      </td>
    </tr>
  </table>
  <%@ include file = "/WEB-INF/jsp/egovframework/com/lib/pageName.jsp" %>
</body>
</html>

