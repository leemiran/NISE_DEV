<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<%@ page import = "java.sql.*" %>
<%@ page import = "com.ziaan.research.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn2" uri="/WEB-INF/tld/fn.tld"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
	GregorianCalendar cl = new GregorianCalendar();
	
	String date = String.format("%TF", cl);
	String time = String.format("%TT", cl);

	String realFileName = "시스템문의관리_"+date+"_"+time; 
	response.setHeader("Pragma", "no-cache;");
	response.setHeader("Expires", "-1;");
	response.setHeader("Content-Transfer-Encoding", "binary;");
	response.setContentType("application/vnd.ms-excel;charset=euc-kr"); //content type setting
	response.setHeader("Content-Disposition", "attachment;filename="+new String(realFileName.getBytes("euc-kr"),"ISO-8859-1")+".xls");// 한글 파일명 설정시에..
%>
<html>
	<head>
		<title>시스템문의관리</title>
		<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">

		
	    <script language="javascript">
	    	var context = '<c:out value="${gsDomainContext}"/>';
		</script>		
		<style>
			@charset "utf-8";
			
			/* layout */
			html {height:100%}
			body {position:relative; height:100%; background:url(/images/adm/skin1/left_bg.gif) repeat-y left top;  }
			#wrapper {position:relative; width:100%; min-height:100%; height:100%; min-width:1000px; }
			#wrapper:after {content:""; display:block; clear:both; height:0; visibility:hidden;}
			#header {height:80px; background:url(/images/adm/common/header_bg.gif) repeat-x left top;}
			#header:after  {content:""; display:block; clear:both; height:0; visibility:hidden;}
			#LnbWrap {float:left; position:relative; width:200px;}
			#contents {position:relative; margin:25px 25px 0 225px; }
			#contents:ater {content:""; display:block; clear:both; height:0; visibility:hidden;}
			#footer {position:relative; border-top:1px solid #dfdfdf; background:#efefef; z-index:-1; clear:both;}
			
			#header .topLogoWrap {position:relative; clear:both; height:30px;}
			#header .topgnb {position:relative; clear:both; height:45px;  background:url(/images/adm/skin1/top_menu_bg.gif) repeat left top; border-bottom:1px solid #194d86;}
			.topLogoWrap h1 {float:left; margin:4px 0 0 20px;} /* header logo */
			#header .ie6w {height:1px; width:1270px; position:relative; line-height:0; font-size:0; padding:0; margin:0;}
			
			/* header login 정보 */
			.topLogoWrap .topRgith {float:right; text-align:right:right; margin-top:5px; position:relative;white-space:nowrap; }
			.topLogoWrap .topRgith .loginout {float:left;}
			.topLogoWrap .topRgith .loginout .name {float:left; background:url(/images/adm/common/icon_people.gif) no-repeat left top; padding-left:20px; padding-right:5px; font-weight:bold; color:#000; text-decoration:underline}
			.topLogoWrap .topRgith .loginout .loginList {float:left; position:relative; width:65px; margin:0 4px 0 8px; background:url(/images/adm2/common/top_selBg.gif) no-repeat left top; overflow:hidden; }
			.topLogoWrap .topRgith .loginout .loginList .firstT {padding:0 10px 0 10px; text-align:center;}
			.topLogoWrap .topRgith .loginout .loginList ul {position:absolute; left:0; top:0;}
			.topLogoWrap .topRgith .loginout .loginList li {display:block;}
			.topLogoWrap .topRgith .loginout .loginList li {padding:0 10px; }
			.topLogoWrap .topRgith .loginout .loginList .btn_down {position:absolute; right:0; top:0;} 
			.topLogoWrap .topRgith .loginout .btn {float:left}
			
			.topLogoWrap .topRgith .topInfo{float:left; margin:0 20px}
			.topLogoWrap .topRgith .topInfo li {float:left; padding:0 8px 8px 20px; background:url(/images/adm/common/icon_clock.gif) no-repeat left top;}
			.topLogoWrap .topRgith .topInfo li.last {background:none;}
			
			
			/* sub tit */
			.sub_tit  {overflow:hidden;}
			.sub_tit h4 {padding-left:20px; float:left; color:#333; background:url(/images/adm/skin1/sub_bullet.gif) no-repeat left 0px; margin-bottom:10px; letter-spacing:-1px;}
			.sub_tit img {float:right;}
			.sub_tit.left {float:left;}
			
			
			/* topGNB */
			.topgnb {padding:0; margin:0; width:100%; }
			.topgnb:after {content:""; display:block; clear:both; height:0; visibility:hidden;}
			.topgnb .topmenu {margin-top:3px; float:left; padding-left:10px; position:relative;}
			.topgnb .topmenu li {float:left; display:inline; padding:9px 7px 8px 5px; color:#fff; background:url(/images/adm/skin1/top_menu_line.gif) no-repeat right center; letter-spacing:-1px; font-size:14px; font-weight:bold; height:25px;}
			
			.topgnb .topmenu li a {display:inline-block; padding-left:7px; height:25px; color:#fff; }
			.topgnb .topmenu li a span {display:inline-block; line-height:24px; height:25px; padding-right:7px; cursor:pointer}
			.topgnb .topmenu li a:hover {float:left; padding-left:7px; line-height:24px; background:url(/images/adm/skin1/top_menu_over.gif) no-repeat left top; }
			.topgnb .topmenu li a:hover span{float:left; display:block; padding:0 7px 0 0; height:25px; ine-height:24px;color:#fff; background:url(/images/adm/skin1/top_menu_over.gif) no-repeat right top; font-weight:bold; letter-spacing:-1px}
			
			.topgnb .topmenu li.on a {float:left; padding-left:7px; line-height:24px; background:url(/images/adm/skin1/top_menu_over.gif) no-repeat left top; }
			.topgnb .topmenu li.on a span{float:left; display:block; padding:0 7px 0 0; height:25px; ine-height:24px;color:#fff; background:url(/images/adm/skin1/top_menu_over.gif) no-repeat right top; font-weight:bold; letter-spacing:-1px}
			.topgnb .topmenu li.last {background:none;}
			
			.topgnb .topmenu .submenu {position:absolute; left:0; top:43px; z-index:100; display:none; width:747px}
			.topgnb .topmenu .submenu li {float:left; padding:0 10px; line-height:1.3em; background:url(/images/adm2/common/top_menuLine02.gif) no-repeat right center; }
			.topgnb .topmenu .submenu li a {color:#656565}
			.topgnb .topmenu .submenu li a:hover {font-weight:bold; letter-spacing:-0.1em; text-decoration:underline;}
			.topgnb .topmenu .submenu li a.on {font-weight:bold; letter-spacing:-0.1em; text-decoration:underline;}
			
			/* top 검색 */
			.topgnb .searchForm {float:right; width:215px; margin-top:5px;}
			.topgnb .topSearch {padding:2px; background:#54b4f1; overflow:hidden; _zoom:1}
			.topgnb .topSearch input {float:left; width:155px; background:#f1f1f1; }
			.topgnb .topSearch a {width:50px; float:left; text-align:center; font-weight:bold; color:#fff !important; }
			.dark {height:10px; background:url(/images/adm/common/top_menu_dark.png) repeat-x left top; }
			
			
			/*** left ***/
			#LnbWrap .left_menu {float:left; padding-top:10px; margin-left:7px; _margin-left:4px;}
			#LnbWrap .left_menu li {position:relative; width:29px; display:block; }
			#LnbWrap .left_menu li a {display:block; font-weight:bold; color:#c2cee6; padding-top:10px; background:url(/images/adm2/common/lnb_bg_out.gif) no-repeat left top; }
			#LnbWrap .left_menu li a span {display:block;font-weight:bold; color:#c2cee6; line-height:1.2em; padding:0 10px 20px; text-align:center;background:url(/images/adm2/common/lnb_bg_out.gif) no-repeat left bottom;}
			#LnbWrap .left_menu li a:hover  {background:url(/images/adm2/common/lnb_bg_over.gif) no-repeat left top; }
			#LnbWrap .left_menu li a:hover span {color:#ffffff; background:url(/images/adm2/common/lnb_bg_over.gif) no-repeat left bottom;}
			#LnbWrap .left_menu li .on  {background:url(/images/adm2/common/lnb_bg_over.gif) no-repeat left top; }
			#LnbWrap .left_menu li .on span {color:#ffffff; background:url(/images/adm2/common/lnb_bg_over.gif) no-repeat left bottom;}
			#LnbWrap .left_menu li em {font-weight:bold; display:block; margin-top:-3px; margin-left:2px}
			
			#LnbWrap .leftCon {padding:10px 0 0 10px}
			#LnbWrap .leftCon .left_loginInfo {width:180px; margin:0 auto; padding-top:15px; text-align:center; background:url(/images/adm2/common/left_title_bgT.gif) no-repeat left top;}
			#LnbWrap .leftCon .left_loginInfo .in {padding-bottom:10px; background:url(/images/adm2/common/left_title_bgT.gif) no-repeat left bottom;}
			#LnbWrap .leftCon .left_loginInfo .tit {color:#8ed1fe; font-size:11px;}
			#LnbWrap .leftCon .left_loginInfo .name {width:140px; margin:0 auto; padding-bottom:5px; color:#fff; background:url(/images/adm2/common/left_ico3.gif) no-repeat left center;}
			#LnbWrap .leftCon .left_loginInfo .name strong{text-decoration:underline}
			#LnbWrap .leftCon .left_loginInfo .inbox {width:164px; margin:0 auto;  padding-top:2px; background:url(/images/adm2/common/left_title_in.gif) no-repeat left top;}
			#LnbWrap .leftCon .left_loginInfo .inbox .in2  {padding-bottom:1px; text-align:center; background:url(/images/adm2/common/left_title_in.gif) no-repeat left bottom;}
			#LnbWrap .leftCon .left_loginInfo .inbox .in2 a {font-size:11px; }
			#LnbWrap .leftCon .left_loginInfo .inbox .in2 a:hover{text-decoration:underline}
			#LnbWrap .leftCon .left_loginInfo.tarea  {padding-top:13px;}
			#LnbWrap .leftCon .left_loginInfo.tarea .in {height:28px;}
			#LnbWrap .leftCon .left_loginInfo .title2 {font-size:14px; font-weight:bold; color:#fff}
			#LnbWrap .leftCon .left_loginInfo .gomain {text-align:center; margin-top:3px;}
			#LnbWrap .leftCon .left_con1 {width:180px; margin:10px auto 0; padding-bottom:10px; background:url(/images/adm2/common/left_conbg.gif) repeat-x left top;}
			#LnbWrap .leftCon .left_con1 dt {padding-top:10px; padding-left:25px; line-height:20px; font-weight:bold; display:block;}
			#LnbWrap .leftCon .left_con1 dd {padding-left:25px; display:block;}
			#LnbWrap .leftCon .left_con1 dt.tit1 {background:url(/images/adm2/common/left_ico01.gif) no-repeat left 10px;}
			#LnbWrap .leftCon .left_con1 dt.tit2 {background:url(/images/adm2/common/left_ico02.gif) no-repeat 3px 10px;}
			
			#LnbWrap .leftCon .left_con2 {width:180px; margin:0 auto; padding:15px 0 10px;background:url(/images/adm2/common/left_conbg.gif) repeat-x left top;}
			#LnbWrap .leftCon .left_con2 .pg {text-align:right;}
			#LnbWrap .leftCon .left_con2 .pg .txt {display:inline; color:#4c7aac;}
			#LnbWrap .leftCon .left_con2 .pg .txt strong {color:#333}
			#LnbWrap .leftCon .left_con2 .pg .btn {display:inline; vertical-align:middle;}
			#LnbWrap .leftCon .left_con2 .img img {border:1px solid #9cbee4}
			
			/* 일정 */
			#LnbWrap .leftCon .left_calendarWrap {padding:10px 15px 10px; margin-right:1px; border-top:1px solid #adcbed; border-bottom:1px solid #adcbed; background:#d4e6f4}
			#LnbWrap .leftCon .left_calendarWrap .tit {margin-left:14px; padding-left:20px; padding-bottom:5px; font-weight:bold; color:#2b3747; background:url(/images/adm2/ico/ico_calendar2.gif) no-repeat left 3px;} 
			#LnbWrap .leftCon .left_calendarWrap .calendarBox {width:180px; margin:0 auto; padding:15px 0 10px; background:#fff; overflow:hidden; border:1px solid #c0d3e2;}
			#LnbWrap .leftCon .left_calendarWrap .calendarBox .top {text-align:center; margin:0 auto; width:130px}
			#LnbWrap .leftCon .left_calendarWrap .calendarBox .top .date {float:left; padding:0 10px; font-size:14px; font-weight:bold;}
			#LnbWrap .leftCon .left_calendarWrap .calendarBox .top a {float:left; overflow:hidden;}
			#LnbWrap .leftCon .left_calendarWrap .calendarBox .top .btnPrv {width:16px; height:15px; background:url(/images/adm2/common/left_btnNext.gif) no-repeat left top;}
			#LnbWrap .leftCon .left_calendarWrap .calendarBox .top .btnNext  {width:16px; height:15px; background:url(/images/adm2/common/left_btnPrv.gif) no-repeat left top;}
			#LnbWrap .leftCon .left_calendarWrap .calendarBox .top img {vertical-align:middle;}
			#LnbWrap .leftCon .left_calendarWrap table {background:#bbd6f3;width:90%; margin:0 auto; }
			#LnbWrap .leftCon .left_calendarWrap th {text-align:center; padding:3px 0; background:#fff;font-size:11px;}
			#LnbWrap .leftCon .left_calendarWrap td {text-align:center; padding:2px;font-size:11px;}
			#LnbWrap .leftCon .left_calendarWrap th.sun {color:#ed1806}
			#LnbWrap .leftCon .left_calendarWrap td a {display:block;font-size:11px; }
			#LnbWrap .leftCon .left_calendarWrap td a:hover {text-decoration:underline}
			#LnbWrap .leftCon .left_calendarWrap td .on {overflow:hidden; border:1px solid #2a2a2a; background:#fff url(/images/adm2/common/left_cal_ico.gif) no-repeat right bottom; }
			#LnbWrap .leftCon .left_calendarWrap .sun a {color:#ed1809}
			
			/* left 배너 */
			#LnbWrap .leftCon .left_banner {margin:15px auto 0; width:180px; }
			#LnbWrap .leftCon .left_banner li {margin-bottom:3px}
			#LnbWrap .leftCon .left_banner .bbox {height:75px; padding-left:15px; background:url(/images/adm2/common/left_banner02_bg.gif) no-repeat left top; overflow:hidden;}
			#LnbWrap .leftCon .left_banner .bbox dt {display:block; text-align:center; margin:6px 0 5px; padding:0;}
			#LnbWrap .leftCon .left_banner .bbox dd {float:left; margin-right:15px;}
			#LnbWrap .leftCon .left_banner .bbox dd a {font-size:11px; color:#2e3547}
			#LnbWrap .leftCon .left_banner .bbox dd a:hover {text-decoration:underline}
			#LnbWrap .leftCon .left_banner .bbox dd a strong {display:block;}
			#LnbWrap .leftCon .left_banner .bbox dd a strong:hover {text-decoration:underline}
			#LnbWrap .leftCon .left_banner .bbox dd.last {margin-right:0}
			
			/* left 매뉴 */
			.lnbSub {margin-top:10px; margin-bottom:50px; overflow:auto; x-overflow:hidden;}
			.lnbSub .dep1 li {margin-bottom:3px; _margin-bottom:0: padding:0; line-height:0}
			
			.lnbSub .dep1 li .separator {
			 margin: -7px 15px 0px 0;
			 height:1px;
			 background: url(/images/adm/ico/ball_gray.gif); 
			 padding: 0px 0 0px 0;
			}
			
			.lnbSub .dep1 li .on {color:#111; padding-left:30px; font-size:14px; background:url(/images/adm/skin1/left_bullet.gif) no-repeat 5px -5px; font-weight:bold; display:block;text-decoration:underline;}
			.lnbSub .dep1 li .on:hover {color:#111; text-decoration:underline;}
			.lnbSub .dep1 li .down {padding-left:30px; font-size:14px; background:url(/images/adm/skin1/left_bullet.gif) no-repeat 5px -30px; font-weight:bold; display:block;text-decoration:none;}
			.lnbSub .dep1 li .down:hover {color:#111; padding-left:30px; font-size:14px; background:url(/images/adm/skin1/left_bullet.gif) no-repeat 5px -5px; font-weight:bold; display:block;text-decoration:none;}
			
			/* tit */
			#contents .titWrap {padding:0px 0 15px; overflow:hidden; _zoom:1}
			#contents .titWrap h3 {float:left; padding-left:24px; line-height:1.2em; font-size:14px; font-weight:bold; background:url(/images/adm/skin1/tit_bullet.gif) no-repeat left top; letter-spacing:-1px;}
			
			
			/* button */
			.btn_search {display:inline-block; line-height:21px; height:20px; padding-left:8px; padding-left:8px; background:url(/images/adm/common/btn_search.gif) no-repeat left top;}
			.btn_search span {display:inline-block; line-height:21px; height:20px; padding-right:8px; min-width:30px; _width:30px; color:#fff; text-align:center; font-weight:bold; background:url(/images/adm/common/btn_search.gif) no-repeat right top;}
			
			.btn_search01 {display:inline-block; line-height:21px; height:22px; padding-left:8px; padding-left:15px; background:url(/images/adm/common/btn_search01.gif) no-repeat left top;}
			.btn_search01 span {display:inline-block; line-height:21px; height:22px; padding-right:8px; min-width:30px; _width:30px; padding-top:2px; color:#555; text-align:center; font-weight:bold; background:url(/images/adm/common/btn_search01.gif) no-repeat right top;}
			
			.btn_del {display:inline-block; line-height:21px; height:22px; padding-left:8px; padding-left:15px; background:url(/images/adm/common/btn_del.gif) no-repeat left top;}
			.btn_del span {display:inline-block; line-height:21px; height:22px; padding-right:8px; min-width:30px; _width:30px; padding-top:2px; color:#555; text-align:center; font-weight:bold; background:url(/images/adm/common/btn_del.gif) no-repeat right top;}
			
			.btn_plus {display:inline-block; line-height:21px; height:22px; padding-left:8px; padding-left:15px; background:url(/images/adm/common/btn_plus.gif) no-repeat left top;}
			.btn_plus span {display:inline-block; line-height:21px; height:22px; padding-right:8px; min-width:30px; _width:30px; padding-top:2px; color:#555; text-align:center; font-weight:bold; background:url(/images/adm/common/btn_plus.gif) no-repeat right top;}
			
			.btn_save {display:inline-block; line-height:21px; height:22px; padding-left:8px; padding-left:15px; background:url(/images/adm/common/btn_save.gif) no-repeat left top;}
			.btn_save span {display:inline-block; line-height:21px; height:22px; padding-right:8px; min-width:30px; _width:30px; padding-top:2px; color:#555; text-align:center; font-weight:bold; background:url(/images/adm/common/btn_save.gif) no-repeat right top;}
			
			.btn_edit {display:inline-block; line-height:21px; height:22px; padding-left:8px; padding-left:15px; background:url(/images/adm/common/btn_edit.gif) no-repeat left top;}
			.btn_edit span {display:inline-block; line-height:21px; height:22px; padding-right:8px; min-width:30px; _width:30px; padding-top:2px; color:#555; text-align:center; font-weight:bold; background:url(/images/adm/common/btn_edit.gif) no-repeat right top;}
			
			.btn_red {display:inline-block; line-height:22px; height:25px; padding-left:15px; background:url(/images/adm/skin1/btn_red.gif) no-repeat left top;} 
			.btn_red span{display:inline-block; line-height:22px; height:25px; padding-right:9px; color:#fff; font-weight:bold; background:url(/images/adm/skin1/btn_red.gif) no-repeat right top; letter-spacing:-1px;}
			
			.btn_go {display:inline-block; line-height:20px; height:20px; padding-left:7px; background:url(/images/adm2/btn/sbtn04.gif) no-repeat left top;}
			.btn_go span {display:inline-block; line-height:20px; height:20px; padding-right:9px; color:#426390; font-weight:bold; background:url(/images/adm2/btn/sbtn04.gif) no-repeat right top;}
			
			.btn01 {display:inline-block; line-height:22px; height:25px; padding-left:15px; background:url(/images/adm/skin1/btn01.gif) no-repeat left top;} 
			.btn01 span{display:inline-block; line-height:22px; height:25px; padding-right:9px; color:#1d69cb; font-weight:bold; background:url(/images/adm/skin1/btn01.gif) no-repeat right top; letter-spacing:-1px;}
			
			.btn01_off {display:inline-block; line-height:22px; height:25px; padding-left:15px; background:url(/images/adm/skin1/btn01_off.gif) no-repeat left top;} 
			.btn01_off span{display:inline-block; line-height:22px; height:25px; padding-right:9px; color:#666; font-weight:bold; background:url(/images/adm/skin1/btn01_off.gif) no-repeat right top; letter-spacing:-1px;}
			
			.btn02 {display:inline-block; line-height:32px; height:35px; padding-left:26px; background:url(/images/adm/skin1/btn02.gif) no-repeat left top;} 
			.btn02 span{display:inline-block; line-height:32px; height:35px; font-size:14px; padding-right:15px; color:#fff; font-weight:bold; background:url(/images/adm/skin1/btn02.gif) no-repeat right top; letter-spacing:-1px;}
			
			.btn03 {display:inline-block; line-height:32px; height:35px; padding-left:26px; background:url(/images/adm/skin1/btn03.gif) no-repeat left top;} 
			.btn03 span{display:inline-block; line-height:32px; height:35px; font-size:14px; padding-right:15px; color:#666; font-weight:bold; background:url(/images/adm/skin1/btn03.gif) no-repeat right top; letter-spacing:-1px;}
			
			.pop_btn01 {display:inline-block; line-height:24px; height:29px; padding-left:9px; background:url(/images/adm/skin1/pop_btn01.gif) no-repeat left top;} 
			.pop_btn01 span{display:inline-block; line-height:24px; height:29px; padding-right:13px; color:#333; font-weight:bold; background:url(/images/adm/skin1/pop_btn01.gif) no-repeat right top; letter-spacing:-1px;}
			
			.btn_excel {display:inline-block; line-height:22px; height:25px; padding-left:25px; background:url(/images/adm/common/btn_excel.gif) no-repeat left top;} 
			.btn_excel span{display:inline-block; line-height:22px; height:25px; padding-right:13px; color:#1d69cb; font-weight:bold; background:url(/images/adm/common/btn_excel.gif) no-repeat right top; letter-spacing:-1px;}
			
			.lnbSub a {line-height:22px;}
			
			
			/* 메인탭 */
			.conwrap2 {position:relative; width:100%; padding:0; margin:0px 0px 20px 0px; border-right:1px solid #e6e6e6;  border-left:1px solid #e6e6e6; border-top:0; background:url(/images/adm/skin1/tab_bg01.gif) repeat-x left top; overflow:hidden; *zoom:1}
			.mtab2 {padding:0; margin:0; height:37px;}
			.mtab2:affter  {content:""; display:block; clear:both;}
			.mtab2 li {float:left; display:block; background:url(/images/adm/skin1/tab_line.gif) no-repeat right center;}
			.mtab2 li a {display:block; padding:0 15px; line-height:36px;}
			.mtab2 li a:hover {text-decoration:underline}
			.mtab2 li .on {font-size:14px; background:#fff; border:2px solid #265c98; color:#333; padding-left:32px; font-weight:bold; border-bottom:0; background:#fff url(/images/adm/ico/ico_arrow1.gif) no-repeat 10px center; letter-spacing:-1px;}
			.mtab2 li.end {background:none;}
			.mtab2 li.last {float:right; background:none; padding-right:10px;}
			.mtab2 li.last a {display:inline; padding:0;}
			
			.mtab2con {padding:5px 10px;}
			.mtab2con:after {content:""; display:block; clear:both;}
			.mtab2con .month2 {display:block; clear:both; background:#a5a5a5; height:20px; _zoom:1; _padding-top:5px;}
			.mtab2con .month2 li {display:inline; padding:0 13px; line-height:20px; background:url(/images/adm/skin1/tab_bgline.gif) no-repeat right center;}
			.mtab2con .month2 li a,  .month li a:visited{color:#fff; font-weight:bold; margin-left:-2px;}
			.mtab2con .month2 li a:hover {text-decoration:underline}
			.mtab2con .month2 li.last {background:none}
			.mtab2con .month2 li.btn {float:right; background:none; padding-top:2px; *margin-top:-21px;}
			.conwrap2 .tbList2 {border-top:0;}
			.conwrap2 .tbList th {border:0;}
			.conwrap2 .tbList td {border-bottom:0;}
			.conwrap2 .tbList td.right {color:#4b7000; padding-right:15px;}
			.conwrap2 .tbList .hover td {background:#ededed}
			
			/* table line */
			.tbList {border-top:2px solid #3d88e2; _zoom:1}
			
			
			
			
			/*** footer ***/
			#footer {margin-top:50px; margin-left:266px; padding:20px 40px; color:#808080;}
			#footer .add {padding-bottom:5px;}
			#footer .phone li {padding:0 20px; display:inline; background:url(/images/adm2/common/foot_line.gif) no-repeat right center; } 
			#footer .phone li.first {padding-left:0;}
			#footer .phone li.last {background:none;}
			
			/* sitemap */
			.sitemap {float:left;}
			.sitemap h3 {width:175px; height:35px; background:url(/images/adm/skin1/sitemap_bg.gif) no-repeat left center;font-size:14px; color:#265c98; font-weight:bold; letter-spacing:-1px; padding:10px 0px 0px 30px; margin:0px 15px 0px 0px;}
			.sitemap ul {width:150px; margin:0px; padding:0px 0px 0px 10px;}
			.sitemap li { background:url(/images/adm/skin1/search_bullet.gif) no-repeat 3px 6px; border-bottom:1px dashed #999; color:#555; padding:6px 0px 5px 10px;}
			.sitemap li.line {padding:0px; margin-left:5px;  border-bottom:1px solid #039; background:none;}
			@charset "utf-8";
			
			
			.tab {margin-bottom:10px; background:url(/images/adm/common/tab_back_bg.gif) repeat-x left bottom; overflow:hidden; _zoom:1}
			.tab li {float:left; margin:0 2px; }
			.tab li a {float:left; color:#868684;  background:url(/images/adm/common/tab_rl.gif) no-repeat left top; }
			.tab li a span {float:left; line-height:22px; padding-left:15px; height:22px; background:url(/images/adm/common/tab_rl.gif) no-repeat right center;  cursor:pointer}
			.tab .w130 {width:130px; text-align:center;}
			.tab .w80 {width:80px; text-align:center;}
			.tab li a:hover {background:url(/images/adm/common/tab_rl_on.gif) no-repeat left top;}
			.tab li a:hover span {padding-right:15px; color:#426491; font-weight:bold; background:url(/images/adm/common/tab_rl_on.gif) no-repeat right top; letter-spacing:-1px;}
			
			.tab li .on {background:url(/images/adm/common/tab_rl_on.gif) no-repeat left top; }
			.tab li .on span {color:#426491; font-weight:bold;  background:url(/images/adm/common/tab_rl_on.gif) no-repeat right top; letter-spacing:-1px;}
			
			
			/* sub main */
			.submainWrap {float:left; width:735px; margin:10px 25px 0 20px; padding-bottom:50px;}
			.submainWrap h2 {font-size:0; line-height:0; padding:0; margin:0;}
			.submainWrap h2 img {font-size:0; line-height:0;}
			.submainWrap h3 {clear:both; margin-bottom:10px; padding-left:20px; font-weight:bold; background:url(/images/adm/common/tit_sbullet01.gif) no-repeat left center;}
			.submainWrap .conTop {padding-bottom:25px; margin:0; height:104px; }
			.submainWrap .conTop:after {content:""; display:block; clear:both; height:0; visibility:hidden;}
			.submainWrap .conTop .lCon {float:left; width:313px;padding:15px 0 15px 15px; height:74px; background:url(/images/adm/common/smain_img01.gif) no-repeat left top;}
			.submainWrap .conTop .lCon dt {float:left; padding-left:10px; line-height:1.7em; font-weight:bold; background:url(/images/adm/common/smain_bullet.gif) no-repeat left center;}
			.submainWrap .conTop .lCon dd {float:left; width:80px; line-height:1.7em;  font-weight:bold; color:#2577e7}
			.submainWrap .conTop .rCon {float:right; width:369px; height:85px; padding:10px 15px; background:url(/images/adm/common/smain_img02.gif) no-repeat right top; }
			.submainWrap .conTop .lCon .last {margin-right:0; width:75px;}
			.submainWrap .conTop .lCon .total {margin:10px 0 0 60px; font-weight:bold; font-size:14px; background:none; padding:0;}
			.submainWrap .conTop .lCon .dtotal {margin-top:10px; font-size:14px; font-weight:bold; color:#333; }
			.submainWrap .conTop .lCon .dtotal span {font-size:14px; font-weight:bold; color:#ec1709}
			.submainWrap .conTop .lCon dd.red {color:#ec1709}
			.submainWrap .conTop .lCon dd.btn {float:none; clear:both;display:block; text-align:center; width:auto; padding:5px 0 0; margin:0;}
			
			.submainWrap .conTop .rCon .tit {clear:both; background:none; padding:0 0 5px; overflow:hidden;}
			.submainWrap .conTop .rCon .tit strong {float:left; font-weight:bold; color:#436392;}
			.submainWrap .conTop .rCon .btn_more {float:right;}
			.submainWrap .conTop .rCon li {clear:both; display:block; padding-left:7px; background:url(/images/adm/common/smain_bullet2.gif) no-repeat left center; }
			.submainWrap .conTop .rCon li a:hover {text-decoration:underline}
			.submainWrap .selwrap .btn {float:right; margin-top:10px;}
			.submainWrap .selwrap {overflow:hidden; padding-bottom:6px;}
			
			.smainTab {clear:both; background:url(/images/adm/common/smain_img03.gif) no-repeat left top;}
			.smainTab ul {background:url(/images/adm/common/smain_img03.gif) no-repeat right top}
			.smainTab ul li {line-height:23px; padding:0 19px; display:inline; background:url(/images/adm/common/img_line.gif) no-repeat right center;}
			.smainTab ul li.last {background:none; *padding-right:13px;}
			.smainTab ul li a:hover {font-weight:bold; color:#426291; letter-spacing:-1px;}
			.smainTab ul li a.on{font-weight:bold; color:#426291; letter-spacing:-1px;}
			.submainWrap .paging {margin-bottom:35px;} 
			.submainWrap .confoot li {float:left;margin-right:8px; padding-right:5px; background:url(/images/adm/common/smain_img04.gif) no-repeat right top;}
			.submainWrap .confoot li a {padding:5px 15px 7px 18px; font-weight:bold; color:#666; display:block; background:url(/images/adm/common/smain_img04.gif) no-repeat left top;}
			.submainWrap .confoot li.last {margin-right:0;}
			
			/* contents */
			#contents .titWrap .location {float:right; color:#aaaaaa; background:url(/images/adm/common/top_sbullet.gif) no-repeat left center;}
			#contents .titWrap .location span {color:#666666; padding:0 10px 0 0; background:url(/images/adm/common/top_locabg.gif) no-repeat right center;}
			#contents .titWrap .location .home {color:#6795cd; padding-left:10px}
			#contents .titWrap .location .on {font-weight:bold;color:#333333; background-image:none}
			#contents .titWrap .location img {vertical-align:middle;}
			#contents h4.stit {padding-left:15px; padding-bottom:10px; font-weight:bold; font-size:12px; background:url(/images/adm/common/tit_sbullet01.gif) no-repeat left top;}
			#contents .stit .stxt {color:#405F8D}
			#contents h5.stit2 {margin:20px 0 10px; padding-left:10px; font-weight:bold; font-size:12px; background:url(/images/adm/common/tit_sbullet02.gif) no-repeat left center;} 
			
			/* search */
			.searchWrap {padding:13px 13px 8px 13px; margin-bottom:25px; border:1px solid #d6d6d6;background:#f2f2f2; text-align:center; overflow:hidden; _zoom:1}
			.searchWrap .in {width:343px; padding-left:30px; height:30px; background:url(/images/adm/common/ico_gl01.png) no-repeat left top; overflow:hidden; }
			.searchWrap .in select, .searchWrap .in input, .searchWrap .in a {float:left; margin:0 2px;}
			.searchWrap input {width:180px; }
			.searchWrap input.date {width:100px}
			.searchWrap img {vertical-align:middle}
			.searchWrap .datewrap li {float:left; overflow:hidden; line-height:20px}
			.searchWrap .datewrap li input {width:auto;}
			.searchWrap .chr {width:auto}
			.searchWrap .datewrap li input, .datewrap li a, select {float:none; vertical-align:middle;}
			.searchWrap .datewrap li .date {width:85px}
			.searchWrap .shLine {display:block; height:2px; clear:both; padding:7px 0; background:url(/images/adm/common/search_line.gif) repeat-x left center; }
			.searchWrap .shTit {float:left; font-weight:bold; color:#555; padding-left:7px; margin-right:10px; background:url(/images/adm/skin1/search_bullet.gif) no-repeat 0px 0px;}
			.searchWrap .shTit2 {font-weight:bold; color:#3f5e89}
			
			.searchWrap .schST1 li {float:left; width:30%; text-align:left;}
			.searchWrap .schST1 .shTit2 {margin-right:10px}
			.searchWrap .schST2 li{width:30%; text-align:left; display:inline; margin:0 50px}
			
			.searchWrap .formCh {text-align:left; padding:10px; border:1px solid #dbdbdb; background:#fff; overflow:hidden;}
			.searchWrap li {display:inline; margin-right:10px;}
			.searchWrap .checkList li .groupL {margin-left:10px;} 
			.searchWrap .checkList li .groupL .on {color:#415d8c; font-weight:bold;}
			.searchWrap .checkList li .groupL a {padding-right:5px;}
			/*.searchWrap .nameSort li {margin-right:7px; padding-right:10px; background:url(/images/adm/sub/search_line2.gif) no-repeat right center}
			.searchWrap .nameSort li a {color:#666;}
			.searchWrap .nameSort li a:hover {color:#415d8c; font-weight:bold; letter-spacing:-1px}
			.searchWrap .nameSort li a.on {color:#415d8c; font-weight:bold;}*/
			.searchWrap .frRe {margin-left:10px; margin-top:10px; padding-left:20px; text-align:left; background:url(/images/adm/sub/ico_fr.gif) no-repeat left center;}
			.searchWrap .frRe dt, .searchWrap .frRe dd{display:inline;}
			
			.searchWrap .con dt {float:left; font-weight:bold; color:#425d8a; margin-right:5px; }
			.searchWrap .con dd {float:left; margin-right:20px}
			.searchWrap th {color:#3f5e89; padding:5px 0; font-weight:bold}
			
			
			/* m_box */
			.box_line { overflow:hidden; padding:0px;}
			.box_line li {float:left; padding-right:50px;  width:29%; background:url(/images/adm/ico/ico_arrow.gif) no-repeat right 30px; }
			.box_line li.bg_none {background:none;}
			.box {padding:13px 13px 8px 13px; margin-bottom:25px; border:1px solid #d6d6d6; background:#f2f2f2; text-align:center; overflow:hidden; _zoom:1}
			.box .in {padding-left:30px; height:30px; background:url(/images/adm/common/ico_gl01.png) no-repeat left top; overflow:hidden; }
			.box img {vertical-align:middle}
			.box .box_text li { float:left; overflow:hidden; width:100%; line-height:20px; height:30px;}
			.box .chr {width:auto}
			.box .shLine {display:block; height:2px; clear:both; padding:7px 0; background:url(/images/adm/common/search_line.gif) repeat-x left center; }
			.box .shTit {float:left; font-weight:bold; color:#555; padding-left:5px; padding-bottom:5px; margin-right:10px; background:url(/images/adm/skin1/search_bullet.gif) no-repeat 0px 0px;}
			.box .right {float:right; padding-left:7px;}
			
			.box .schST1 li {float:left; width:30%; text-align:left;}
			.box .schST1 .shTit2 {margin-right:10px}
			.box .schST2 li{width:30%; text-align:left; display:inline; margin:0 50px}
			.box .txt_box {clear:both; overflow:hidden; text-align:left; padding:5px; line-height:20px; border:1px solid #CCC; background:#fff;}
			.box .txt_box p {padding-left:12px; background:url(/images/adm/ico/ico_tit.gif) no-repeat 5px 5px;}
			
			/* tab */
			.tab {padding-bottom:1px; background:url(/images/adm/common/tab_back_bg.gif) repeat-x left bottom; overflow:hidden; _zoom:1}
			.tab li {float:left; margin:0 2px}
			.tab li a {float:left; padding-left:4px; color:#868684;  background:url(/images/adm/common/tab_rl.gif) no-repeat left top;overflow:hidden; }
			.tab li a span {float:left; padding-right:15px; line-height:22px; height:22px; background:url(/images/adm/common/tab_rl.gif) no-repeat right center;  cursor:pointer}
			.tab .w130 {width:130px; text-align:center;}
			.tab .w80 {width:80px; text-align:center;}
			.tab li a:hover {background:url(/images/adm/common/tab_rl_on.gif) no-repeat left top;}
			.tab li a:hover span {color:#555; font-weight:bold;  background:url(/images/adm/common/tab_rl_on.gif) no-repeat right top; letter-spacing:-1px;}
			
			.tab li .on {background:url(/images/adm/common/tab_rl_on.gif) no-repeat left top; }
			.tab li .on span {color:#555; font-weight:bold;  background:url(/images/adm/common/tab_rl_on.gif) no-repeat right top; letter-spacing:-1px;}
			
			
			/* table contents */
			.listTop { padding-bottom:8px; overflow:hidden; _zoom:1}
			.listTop .left {float:left;}
			.listTop .right {float:right; font-weight:bold; color:#333333}
			.listTop .right span {font-weight:bold;}
			.listTop .listSel {border:1px solid #7f9db9;}
			
			/* table - list */
			.tbList thead th {padding:5px 4px; font-weight:bold; color:#666666; text-align:center; border:1px solid #d9d9d9; border-top:none; background:#f5f5f5;}
			.tbList tbody td {text-align:center; padding:5px 4px; color:#333; border:1px solid #d9d9d9; word-wrap: break-word;}
			.tbList tbody td.num {color:#888888;}
			.tbList tbody td.left {text-align:left; padding-left:15px; padding-right:10px;}
			.tbList thead th.left {text-align:left; padding-left:15px; padding-right:10px;}
			.tbList tbody td.left2 {text-align:left; padding-right:10px;}
			.tbList tbody td a {color:#333;}
			.tbList tbody td a:hover {color:#000; text-decoration:underline }
			.tbList tbody .hover td {background:#f5f5f5}
			.tbList td img {vertical-align:middle;}
			.tbList td .ico_re {margin-top:2px; *margin-top:0; vertical-align:top;}
			.tbList td .ico {padding-bottom:3px;}
			.tbList td .sbtn03 {vertical-align:middle}
			.tbList td .sbtn03 .ico2 {margin-top:5px;}
			.tbList td.re3 {padding-left:25px}
			.tbList td.re {text-align:left; padding-left:30px; }
			.tbList td.re a {display:block; padding-left:40px; background:url(/images/adm/ico/ico_reply.gif) no-repeat 0 center;}
			.tbList .bg {background:#f5f5f5; }
			.tbList .check {background:url(/images/adm/common/check.gif) no-repeat center center;}
			.tbList .name {color:#0173ff; font-weight:bold;}
			
			
			.tbList1 {background:none; border-top:2px solid #6b9bd5;_zoom:1}
			.tbList1 thead th {padding:5px 0; font-weight:bold; color:#666666; text-align:center;  border:1px solid #d9d9d9; background:#efefef; }
			.tbList1 tbody td {text-align:center; padding:5px 0; color:#333; border-bottom:1px solid #d9d9d9; border:1px solid #d9d9d9}
			.tbList1 tbody td.left {text-align:left; padding-left:15px; padding-right:10px;}
			.tbList1 tbody td a:hover {color:#3f5e89; font-weight:bold; text-decoration:underline }
			.tbList1 tbody .hover td {background:#f2f2f2}
			
			/* table - detail */
			.tbDetail {margin-bottom:25px; border-top:2px solid #3d88e2; _zoom:1}
			.tbDetail th { color:#666666; background:#f5f5f5;}
			.tbDetail th, .tbDetail td {padding:5px 5px 5px 15px; border:1px solid #d9d9d9; border-top:0px; text-align:left; line-height:180%;}
			
			.tbDetail td {border-left:1px solid #d9d9d9;}
			.tbDetail td.noborderL {border-left:1px solid #d9d9d9;}
			.tbDetail td.borderR {border-right:1px solid #d9d9d9}
			.tbDetail .title th {color:#666666}
			.tbDetail .title td {color:#000000; font-weight:bold;}
			.tbDetail .point {color:#3f5e89; font-weight:bold;}
			
			.tbDetail .info dt{color:#3f5e89; font-weight:bold; background:url(/images/adm/common/img_line.gif) no-repeat right top;}
			.tbDetail .info dt, .tbDetail .info dd {display:inline; padding:0 8px 0 10px;}
			.tbDetail .info dd {color:#666666}
			.tbDetail .tbcon {color:#333; border:0; padding:30px; height:250px; vertical-align:top;}
			.tbDetail .tbcon2 {color:#333; border-bottom:1px solid #d9d9d9; border-left:0; padding:30px; vertical-align:top; line-height:22px;}
			.tbDetail .tbcon3 {color:#333; border-bottom:1px solid #d9d9d9; border-left:0; padding:30px; vertical-align:top;}
			.tbDetail .img100{width:100px;}
			.tbDetail .bc {background:#d6d6d6; border:1px solid #bcbcbc;}
			.tbDetail .photo1 {text-align:middle; padding-top:10px;padding-bottom:10px;}
			.tbDetail .photo1 img{width:110px; height:140px;  vertical-align:middle}
			
			.tbVeiw {padding:4px; border:1px solid #d6d6d6; background:#ededed;_zoom:1}
			.tbVeiw th, .tbVeiw td {padding:10px 10px 10px 17px; border-bottom:1px solid #d6d6d6}
			.tbVeiw th {background:#e0e8f0; text-align:left; font-weight:bold; color:#3f5e89}
			.tbVeiw td {background:#fff}
			.tbVeiw .checkL2 {float:left;}
			.tbVeiw .checkL2 li {display:inline;}
			.tbVeiw .btn_print {float:right;}
			
			/* table - write */
			.tbWrite {border-top:2px solid #6b9bd5; overflow:hidden; _width:99%;}
			.tbWrite th {font-weight:bold; color:#3f5e89; border-right:1px solid #d9d9d9; background:#efefef}
			.tbWrite th, .tbWrite td {padding:5px 5px 5px 20px; border-bottom:1px solid #d9d9d9; text-align:left;}
			.tbWrite .editor {padding:0; border-bottom:1px solid #d9d9d9; }
			.tbWrite .strar {color:#688dc2; padding-right:3px;}
			.tbWrite .strarRed {color:#f84236}
			.tbWrite .subjectW input {width:99%}
			.tbWrite .checkL {text-align:right; padding-right:10px; }
			.tbWrite .checkL li {display:inline;}
			.tbWrite .searchForm {position:relative; width:555px; overflow:hidden;}
			.tbWrite .searchForm .inputT {width:420px; float:left;}
			.tbWrite .searchForm .btn_go {margin-left:5px; float:left; }
			.tbWrite .searchForm .fileInput {position:absolute; right:50px; filter:Alpha(opacity=0); opacity:0; z-index:10; height:auto; background:#fff;}
			.tbWrite img {vertical-align:middle}
			.tbWrite .list1 span {display:inline; margin-right:5px;}
			.tbWrite textarea {width:99%; height:50px; }
			.tbWrite .ico2 {margin-top: 5px;}
			.tbWrite .temlist{padding:5px 0; height:140px;}
			.tbWrite .temlist p{float:left; width:170px;}
			.tbWrite .temlist span{display:block; padding-top:5px;}
			.tbWrite .multi {height:50px; width:80%}
			
			/* 테이블 타이틀 */
			.tdtitList {border-top:2px solid #265c98; margin-bottom:25px; _zoom:1}
			.tdtitList table {table-layout:fixed;}
			.tdtitList thead th {padding:5px 0; font-weight:bold; color:#555555; border:1px solid #cccccc;  text-align:left; padding-left:10px; border-top:none; background:#eaeaea; white-space:nowrap}
			.tdtitList tbody th {padding:5px 0; font-weight:bold; color:#555555; border:1px solid #cccccc;   text-align:left; padding-left:10px; border-top:none; background:#eaeaea; white-space:nowrap}
			.tdtitList thead th.last {border-right:0px;}
			.tdtitList thead td.last {border-right:0px;}
			.tdtitList tbody td.last {border-right:0px;}
			.tdtitList tbody td {text-align:left; padding:5px; color:#555; border-bottom:1px solid #d9d9d9; border-right:1px solid #d9d9d9; word-wrap: break-word;}
			.tdtitList thead td {text-align:left; padding:5px; color:#555; border-bottom:1px solid #d9d9d9; border-right:1px solid #d9d9d9; word-wrap: break-word;}
			.tdtitList tbody td.num {color:#888888;}
			.tdtitList tbody td.left {text-align:left; padding-left:10px; padding-right:10px;}
			.tdtitList tbody td.left2 {text-align:left; padding-right:10px;}
			.tdtitList tbody td.replay {text-align:left; padding-left:20px;}
			.tdtitList tbody td.replay img {padding-right:5px;}
			.tdtitList tbody td a {color:#333;}
			
			
			/*  */
			.tbDetail .writeWrap {background:#f2f2f2; border:1px solid #d6d6d6; padding:13px; overflow:hidden;}
			.tbDetail .writeWrap .tit_reply {font-weight:bold; color:#666; padding-left:22px; background:url(/images/adm/sub/bullet_reply.gif) no-repeat 2px center;}
			.tbDetail .writeWrap .tawrap  {position:relative; padding-right:90px;}
			.tbDetail .writeWrap .tawrap .ta {width:100%; height:45px;}
			.tbDetail .writeWrap .tawrap .btn {position:absolute; top:0; right:0;}
			.tbDetail .replyList {margin-top:5px;width:100%;}
			.tbDetail .replyList li {position:relative; padding:5px 15px; border-bottom:1px solid #ededed; overflow:hidden; color:#666; }
			.tbDetail .replyList li .name {float:left; font-weight:bold; width:66px;}
			.tbDetail .replyList li .txt {float:left; width:68%;}
			.tbDetail .replyList li .date {float:right; width:20%; text-align:right;}
			.titdetail{background:#6b9bd5; padding:7px; height:18px; color:#ffffff;}
			.titdetail:after {content:""; display:block; clear:both; height:0;}
			.titdetail h2{float:left; font-size:14px; font-weight:bold;}
			.titdetail p{float:right;}
			.titdetail p span{font-weight:bold;}
			
			/* calendar */
			.calendarWrap {padding:0; margin:0;}
			.calendarWrap .calTop {text-align:center; margin-bottom:25px; height:20px;}
			.calendarWrap .calTop .btn {margin:0 13px 0; display:inline-block; }
			.calendarWrap .calTop .btn img {vertical-align:middle; margin-top:5px; *margin-top:0;}
			.calendarWrap .calTop .txt {text-align:center; font-size:16px; font-weight:bold; vertical-align:top;}
			.calendarWrap .toptxt {overflow:hidden; padding-bottom:10px; _padding-bottom:30px;}
			.calendarWrap .toptxt .t {float:left;}
			.calendarWrap .toptxt .t strong {text-decoration:underline}
			.calendarWrap .toptxt .t_ico {float:right; }
			.calendarWrap .toptxt .t_ico li {display:inline; margin-left:20px; font-weight:bold;}
			.calendarWrap .toptxt .t_ico img {vertical-align:middle;}
			.calendarWrap .calTb {border-top:2px solid #6b9bd5; _width:99%;}
			.calendarWrap .calTb th {line-height:28px; background:#f2f2f2; font-weight:bold}
			.calendarWrap .calTb th, .calTb td {border:1px solid #dfdfdf}
			.calendarWrap .calTb td {text-align:right; vertical-align:top; padding:7px; height:80px;}
			.calendarWrap .calTb .sun a{color:#f84236}
			.calendarWrap .calTb .calon {background:#efefef;}
			.calendarWrap .calTb .calCon {text-align:left;}
			.calendarWrap .calTb .calCon span {font-weight:bold; display:block; padding:2px 0 0;}
			.calendarWrap .calTb .calCon span a {font-weight:bold;}
			.calendarWrap .calTb .calCon span a:hover {text-decoration:underline }
			.calendarWrap .calTb .calCon span .ico_cal {float:left; margin-right:2px;}
			
			/* common */
			.btnArea {text-align:right; padding:25px 0 40px;}
			.btnArea li {display:inline; margin:0 2px;}
			.btnR {float:right;}
			.btnR li {display:inline;}
			.btnL {float:left;}
			.btnL li {display:inline;}
			
			.btnCen {text-align:center; margin:0 auto;  padding:25px 0 40px;}
			.btnCen li{display:inline; margin:0 2px;}
			.tbTop_txt {padding-bottom:25px; color:#405f8d;}
			.tbTop_txt .underL {text-decoration:underline;}
			.btnT {padding-bottom:0 !important;}
			.tempImg01 img {width:126px; height:114px; border:1px solid #b6b7b9; }
			.conList li {display:block; padding-bottom:10px; color:#777}
			.conList li strong {padding-left:10px; display:block; background:url(/images/adm/common/tit_sbullet02.gif) no-repeat left center;}
			.conList li span {display:block; padding-left:14px;}
			
			/* paging */
			.paging {text-align:center; margin-top:25px; _height:40px; _padding-top:3px;}
			.paging a {display:inline; margin:0 3px; vertical-align:middle; }
			.paging a img {vertical-align:middle; }
			.paging a:hover {text-decoration:underline}
			.paging a.on {margin:0; color:#333;  padding:2px 5px; line-height:25px; font-weight:bold;}
			.paging .btn {border:0; background:0; padding:0; margin:0 2px;}
			.paging .btn:hover {border:0; background:0; padding:0; margin:0 2px; font-size:0; line-height:0;}
			.paging .btn img {font-size:0; line-height:0;}
			
			
			/* login */
			#loginWrap {text-align:center; height:100%; width:100%; background:url(/images/adm/common/login_bg.gif) repeat-x left top; overflow:hidden;}
			#loginWrap .loginBox {margin:102px auto 0; text-align:left; height:404px; width:515px; background:url(/images/adm/common/login_bg2.gif) no-repeat left top; overflow:hidden}
			#loginWrap .loginBox h1 {margin:55px 0 0 45px}
			#loginWrap .loginBox h2 {margin:88px 0 18px 145px;}
			#loginWrap .loginBox .loginForm {margin:0 99px; clear:both; overflow:hidden;}
			#loginWrap .loginBox .loginForm .inputbox {float:left; text-align:left; width:180px; border:3px solid #879ca2; overflow:hidden; }
			#loginWrap .loginBox .loginForm .inputbox input {width:90%; background:#fff;  padding-left:10px; font-size:14px; line-height:26px; height:26px;font-weight:bold; border:0; color:#333;}
			#loginWrap .loginBox .loginForm .inputbox .idt {background:#fff url(/images/adm/common/login_idt.gif) no-repeat 10px center; }
			#loginWrap .loginBox .loginForm a {float:left; margin-left:5px} 
			#loginWrap .loginBox .text {width:270px; margin:20px 0 0 100px;}
			#loginWrap .loginBox .text a {vertical-align:bottom}
			#loginWrap .loginBox .text strong {text-decoration:underline}
			
			/* text style */
			.num1 {color:#ec180a !important; font-weight:bold}
			.num2 {color:#3e5d89; font-weight:bold}
			.tred {color:#ec180a !important;}
			.file {color:#666 !important}
			.point {color:#fc4404 !important;}
			.smallT {font-size:11px; color:#666666; padding-top:5px;}
			
			
			div.edu {font-size:11px; width:100%; text-align: left; color:#666;}
			div.edu dt {color:#666; padding:10px 0px 0px 20px; background:url('/images/adm/icon/m_icon.gif') no-repeat 9px 12px;}
			div.edu dd {padding:5px 0px 0px 35px; background:url('/images/adm/icon/m_icon.gif') no-repeat 23px -9px;}
			
			.graph_1 {height:15px; clear:both; float:none; background:url(/images/adm/common/graph01_bg.gif);}
			
			.error_404 {position:relative; width:680px; height:229px;  background:url('/images/adm/common/code404.jpg') no-repeat center center;}
			.error_404 .btn_pos {padding:170px 0 0 190px;}
			
			.error_500 {position:relative; width:680px; height:236px;  background:url('/images/adm/common/code500.jpg') no-repeat center center;}
			.error_500 .btn_pos {padding:180px 0 0 90px;}
			@charset "utf-8";
			/*default definition*/
			*{margin: 0;padding: 0;}
			body{font-family:dotum,돋움,sans-serif;margin: 0;padding: 0;color:#333333; font-size: 12px; line-height: 1.5em;}
			div{margin: 0;padding: 0;}
			ul,li{list-style:none;margin:0;padding:0;}
			ol {margin:0;padding:0;}
			p{margin: 0;padding: 0;}
			dl,dt,dd{margin: 0;padding: 0;}
			
			a:link,a:visited{text-decoration:underline; color:#626262;}
			a:hover,a:active{color:#fc6817;}
			hr{display:none;}
			.png24 {tmp:expression(setPng24(this));}
			
			
			input[type="checkbox"]{border:0;}
			input[type="radio"]{border:0;}
			.borderNone{border:none;}
			
			/* form all start */
			form {margin: 0;padding: 0;}
			select {font-size:12px; color:#555; font-family:"돋움"; line-height:25px; padding:2px 2px; border:1px solid #bababa}
			input .file {font-size:12px; color:#555; font-family:"돋움"; padding:2px 2px; border:1px solid #bababa}
			input {font-size:12px; border:1px solid #c6c6c6; padding:2px; color:#777777; font-family:"돋움"; height:15px;}
			input.txtput {height:17px; margin-bottom:1px; padding:0 2px;}
			.chr {padding:0; margin:0; width:auto; height:auto; border:0;} /* input radio버튼 checkbox css */
			textarea {padding:2px; border:1px solid #dadada; font-size:12px; line-height:1.5em; color:#777777; font-family:"돋움"; resize: none;}
			hr {display:none;}
			
			caption, legend {font-size:0;height:0;line-height:0;overflow:hidden;position:absolute;visibility:hidden;width:0;}
			img, input.type-image {border:0px none;vertical-align:middle;}
			
			/* table all start */
			table, th, td {border-spacing:0; border-collapse:collapse;}
			/* table all end */
			.clear {clear:both;}
			.floatL	{float:left !important;}
			.floatR	{float:right !important;}
			.txtL {text-align:left !important;}
			.txtR {text-align:right !important}
			.txtC {text-align:center !important;}
			.vrT{vertical-align:top !important;}
			.vrM {vertical-align:middle !important;}
			.vrB {vertical-align:bottom !important;}
			.mrt3 {margin-top:3px !important}
			.mrt0 {margin-top:0 !important} .mrt10 {margin-top:10px !important} .mrt20 {margin-top:20px !important} .mrt30 {margin-top:30px !important}
			.mrb0 {margin-bottom:0 !important} .mrb10 {margin-bottom:10px !important} .mrb20 {margin-bottom:20px !important} .mrb30 {margin-bottom:30px !important}
			.mrr10 {margin-right:10px !important} .mrr20 {margin-right:20px !important} .mrr30 {margin-right:10px !important}
			.mrl10 {margin-left:10px !important} .mrl20 {margin-left:20px !important} .mrl30 {margin-left:30px !important}
			.nobtr {border-top:0 !important;}
			.nobbr {border-bottom:0 !important;}
			.w50{width:50px !important;}
			
			/* mamgin */
			.MT10 {margin-top:10px !important;}
			.MT20 {margin-top:20px !important;}
			
			.ML10 {margin-left:10px !important;}
			.ML20 {margin-left:20px !important;}
			.ML30 {margin-left:30px !important;}
			.ML40 {margin-left:40px !important;}
			
			.MR05 {margin-right:5px !important;}
			.MR10 {margin-right:10px !important;}
			.MR20 {margin-right:20px !important;}
			.MR30 {margin-right:30px !important;}
			.MR40 {margin-right:40px !important;}
			
			/* padding */
			.PL10 {padding-left:10px !important;}
			@charset "utf-8";
			
			
			.m_box {border:1px solid #eaeaea; padding:10px;}
			.m_searchWrap {padding:7px; margin-bottom:10px; border:1px solid #d6d6d6;background:#f2f2f2; text-align:center; overflow:hidden; _zoom:1}
			.m_searchWrap .shTit {float:left; font-weight:bold; color:#555; padding-left:5px; padding-right:15px; margin-right:10px; background:url(/images/adm/ico/ico_tit.gif) no-repeat 0px 5px;}
			#m_contents {position:relative; margin:25px; }      
		</style>
	</head>	
	<body bgcolor="#FFFFFF" text="#000000" topmargin="0" leftmargin="0">
		<div class="tbList">
		<!-- detail wrap -->
			<table summary="" cellspacing="0" width="100%" border=1 class="" >
				<caption>시스템문의관리</caption>
				<colgroup>
					<col />
					<col />				
					<col />
					<col />
					<col />
					<col />
					<col />				
					<col />
				</colgroup>
				<tbody>
					<tr class="tbcenter">
						<td>NO</td>
						<td>시스템구분</td>
						<td>제목</td>
						<td>등록자</td>
						<td>등록일</td>
						<td>완료일</td>
						<td>내용확인일시</td>
						<td>진행율</td>
					</tr>
					<c:forEach items="${list}" var="result" varStatus="i">
					<tr>
						<td >${result.rn}</td>
						<td >${result.sysGubun}</td>					
						<td class="left">
							<c:if test="${result.busyGubun eq 'A' and result.reSeq eq 0}">(보통)</c:if>
							<c:if test="${result.busyGubun eq 'B' and result.reSeq eq 0}">(긴급)</c:if>
							<c:if test="${result.srLevel > 0}">
								 <c:forEach var="i" begin="1" end="${result.srLevel}"> <!-- scope 생략으로 페이지 영역에 저장 step생략으로 1씩 증가 -->
								 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</c:forEach>							
								(답변)&nbsp;
							</c:if>
							${result.reqTitle}
						</td>   
						<td >${result.regName}</td>
						<td >${result.regDate}</td>
						<td >${result.finishDate}</td>					
						<td >
							${fn2:getFormatDate(result.confirmDate, 'yyyy.MM.dd HH:mm:ss')}
						</td>					
						<td >${result.procRate}
							<c:if test="${result.reSeq ne 0}">%</c:if> 
						</td>
					</tr>					 
					</c:forEach>                
				</tbody>
			</table>	
		</div>
	</body>
</html> 