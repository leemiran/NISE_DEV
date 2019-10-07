<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="customFn" uri="NumberFunctions"%>
<%@ taglib prefix="fn2" 	 uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="ui" 		 uri="/WEB-INF/tld/ui.tld"%>
<%@ taglib prefix="spring" 	 uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" 	 uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" 		 uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" 	 uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ajax" 	 uri="http://ajaxtags.sourceforge.net/tags/ajaxtags" %>


<c:set var="gsMainForm" value="kniseForm"/>
<c:set var="gsDomain" value="${fn:replace(pageContext.request.requestURL, pageContext.request.requestURI , '')}" />
<c:set var="gsDomainContext" value="${fn:replace(pageContext.request.requestURL, fn:replace(pageContext.request.requestURI, pageContext.request.contextPath, ''), '')}" />
<c:set var="gsSystemDiv" value="${pageContext.request.contextPath}" />
<c:set var="gsServerPort" value="${pageContext.request.serverPort}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="ko" lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="apple-mobile-web-app-status-bar-style" content="white"/>
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=medium-dpi" name="viewport" />
<title> 스마트러닝 </title>
<link rel="stylesheet" type="text/css" href="<c:out value="${gsDomainContext}"/>/css/mbl/import.css" />
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/script/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js/cresys_lib.js"></script>

<!-- RSA 자바스크립트 라이브러리 -->
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js//rsa/jsbn.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js//rsa/rsa.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js//rsa/prng4.js"></script>
<script type="text/javascript" src="<c:out value="${gsDomainContext}"/>/js//rsa/rng.js"></script>	
<!-- RSA 암호화 처리 스크립트 -->

<script type="text/javascript">
	//<![CDATA[
   var agent = navigator.userAgent;
   if (agent.match(/iPhone/) != null || agent.match(/iPod/) != null) {
    document.write('<meta name="viewport" content="minimum-scale=1.0,maximum-scale=1.0,initial-scale=1.0,user-scalable=no" />');
   }else {
    document.write('<meta name="viewport" content="minimum-scale=0.75,maximum-scale=0.75,initial-scale=0.75,user-scalable=no" />');
   }   
	//]]>
 </script>
 <script type="text/javascript">
	//로그인
	function login(){
		var frm = document.getElementById("frmLogin");
		
		if(frm.userId.value == "") {
			alert("아이디를  입력해주세요");
			frm.userId.focus();
			return false;
		}

		if(frm.pwd.value == "") {
			alert("비밀번호를  입력해주세요");
			frm.pwd.focus();
			return false;
		}
		
		/* var c_url   = this.location+"";		
		if(c_url.match("localhost")){		
			frm.action = "/usr/lgn/portalUserLogin.do";
		}else{
			frm.action = "https://iedu.nise.go.kr/usr/lgn/portalUserLogin.do";
		} */
		
		//frm.action = "https://iedu.nise.go.kr/usr/lgn/portalUserLogin.do";
		frm.action = "/usr/lgn/portalUserLogin.do";
		
		
    	frm.submit();
	} 

	//엔터키
	function fn_keyEvent(fun){
		if( event.keyCode == 13 ){
			eval("this."+fun+"()");
		}
	}

	function setCookie (name, value, expires) {
	  document.cookie = name + "=" + escape (value) +
	    "; path=/; expires=" + expires.toGMTString();
	}


	function getCookie(Name) {
	  var search = Name + "="
	  if (document.cookie.length > 0) { // 쿠키가 설정되어 있다면
	    offset = document.cookie.indexOf(search)
	    if (offset != -1) { // 쿠키가 존재하면
	      offset += search.length
	      // set index of beginning of value
	      end = document.cookie.indexOf(";", offset)
	      // 쿠키 값의 마지막 위치 인덱스 번호 설정
	      if (end == -1)
	        end = document.cookie.length
	      return unescape(document.cookie.substring(offset, end))
	    }
	  }
	  return "";
	}
	function saveid(form) {
	  var expdate = new Date();
	  // 기본적으로 30일동안 기억하게 함. 일수를 조절하려면 * 30에서 숫자를 조절하면 됨
	  if (form.checksaveid.checked)
	    expdate.setTime(expdate.getTime() + 1000 * 3600 * 24 * 30); // 30일
	  else
	    expdate.setTime(expdate.getTime() - 1); // 쿠키 삭제조건
	  	setCookie("saveid", form.userId.value, expdate);
	  	setCookie("savepw", form.pwd.value, expdate);
	}


	
	function getid(form) {
	  form.checksaveid.checked = ((form.userId.value = getCookie("saveid")) != "");
	  form.checksaveid.checked = ((form.pwd.value = getCookie("savepw")) != "");
	}
	
	
	//로그인
	function loginMobile(){
		
		
		var frm = document.getElementById("frmLogin");
		
		//d_type
		frm.p_d_type.value = "T";
		
		if(frm.userId.value == "") {
			alert("아이디를  입력해주세요..");
			frm.userId.focus();
			return false;
		}

		if(frm.pwd.value == "") {
			alert("비밀번호를  입력해주세요");
			frm.pwd.focus();
			return false;
		}
		
		saveid(frm);
		
		//사용자 계정정보 암호화전 평문
		var uid = $("#userId").val();
		var pwd = $("#pwd").val();
	 
		//RSA 암호화 생성
		var rsa = new RSAKey();
	 	rsa.setPublic($("#RSAModulus").val(), $("#RSAExponent").val());
		
		//사용자 계정정보를 암호화 처리
		uid = rsa.encrypt(uid);
		pwd = rsa.encrypt(pwd);
		
		$("#userId").val(uid);
		$("#pwd").val(pwd);		
		
		/* var c_url   = this.location+"";		
		if(c_url.match("localhost")){		
			frm.action = "/usr/lgn/portalUserLogin.do";
		}else{
			frm.action = "https://iedu.nise.go.kr/usr/lgn/portalUserLogin.do";
		} */
		
		//frm.action = "https://iedu.nise.go.kr/usr/lgn/portalUserLogin.do";
		frm.action = "/usr/lgn/portalUserLoginMobile.do";
		
		
    	frm.submit();
	} 
	
	var c_url   = this.location+"";	
	/* if(c_url.match("http://iedu.nise.go.kr")){
	//if(c_url.match("localhost")){	
		location.href="/temp_index/temp_index.jsp";
	} */
		
</script>
<script type="text/javascript">
<!--
<c:if test="${!empty resultMsg}">alert("${resultMsg}");</c:if>
-->
</script>




</head>




