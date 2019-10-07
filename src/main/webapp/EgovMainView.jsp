<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
<link rel="stylesheet" href="css/css.css" type="text/css">
<title>행정안전부 공통서비스 테스트 사이트(업무사용자)</title>
<script language="javascript">
function chk_all(val) {
	
	var arr_chk = document.getElementsByName("chk");
	
		if (val == "Y") {
		
			for(i=0;i< arr_chk.length; i++) {
				arr_chk[i].checked =true;
			}
		}
		else if(val == "N") {
			for(i=0;i< arr_chk.length; i++) {
				arr_chk[i].checked =false;
			}
		}

}
</script>
</head>

<body>
<c:import url="./head.jsp" />
<table width="900" border="0" cellspacing="0" cellpadding="0">
    <tr>
    <!-- left menu start -->
    <td width="181" valign="top">
        <table width="181" height="94" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td valign="bottom" style="background-image:url('images/left_menu_top.gif'); background-repeat:no-repeat;">
                <div class="LeftMenuTitle">보안관리</div>
                <div class="LeftMenuWelcome">탁현민님 어서오세요</div>
                <div class="LeftMenuBtn"><font color="#000"><b><a href="#"><img src="images/btn_logout.gif"></a></b></font></div></td>
            </tr>
            <tr>
                <td><div class="LeftMenuBg"><a href="#">사용자관리</a></div></td>
            </tr>
            <tr>
                <td><div class="LeftMenuBgList"><a href="#">나의정보수정</a></div><div class="LeftMenuBgList"><a href="#">사용자관리</a></div></td>
           </tr>
            <tr>
                <td><div class="LeftMenuBg"><a href="#">사용자관리</a></div></td>
            </tr>
            <tr>
                <td><div class="LeftMenuBgList"><a href="#">나의정보수정</a></div><div class="LeftMenuBgList"><a href="#">사용자관리</a></div></td>
            </tr>
            <tr>
                <td><div class="LeftMenuBg"><a href="#">사용자관리</a></div></td>
            </tr>
            <tr>
                <td><div class="LeftMenuBgList"><a href="#">나의정보수정</a></div><div class="LeftMenuBgList"><a href="#">사용자관리</a></div></td>
            </tr>
            <tr>
                <td><img src="images/left_menu_btm.gif"></td>
            </tr>
        </table>
    </td>
<!-- 본문시작 -->
    <td valign="top">
        <table height="100%" border="0" cellpadding="0" cellspacing="0">
            <tr>
                <td width="717">
                    <!-- QuickLink -->
                    <div class="QuickLink">
                        <table width="240" border="0" cellspacing="0" cellpadding="0" align="right">
                            <tr>
                                <td width="80" align="right"><a href="#">HOME></a></td>
                                <td width="80" align="center"><a href="#">사용자인증></a></td>
                                <td width="80" align="left"><a href="#">보안관리</a></td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
            <tr>
                <!--Page title -->
                <td width="717">
                    <div class="PageTitle">
                        <table width="100%" height="26" border="0" align="center" cellpadding="0" cellspacing="0">
                            <tr>
                                <td><font color="#000" class="t_strong">보안관리</font></td>
                            </tr>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
        <!-- LIST -->
        <!-- article_list -->
        <div>
        <table width="712px" cellspacing="0" cellpadding="0" border="0" align="center" class="table_listA">
        <colgroup>
        <col width="30px">
        <col width="30px">
        <col width="70px">
        <col width="70px">
        <col width="260px">
        <col width="70px">
        <col width="50px">
        <col width="50px">
        <col width="50px">
        </colgroup>
            <thead>
                <tr>
                    <th>NO.</th>
                    <th>선택</th>
                    <th>스페이스명</th>
                    <th>스페이스키</th>
                    <th>Description</th>
                    <th>사용자할당관리</th>
                    <th>필드관리</th>
                    <th>상태관리</th>
                    <th>수정</th>
                </tr>
            </thead>
            <tbody>
            <!-- loop -->
                <tr align="center">
                    <td>1</td>
                    <td><a href="#"><input type="checkbox" name="chk" class="checkbox"></a></td>
                    <td>스페이스명</td>
                    <td>스페이스키</td>
                    <td><a href="#">DescriptionDescription</a></td>
                    <td><span><input type="button"value="사용자할당관리"></span></td>
                    <td><span><input type="button"value="필드관리"></span></td>
                    <td><span><input type="button"value="상태관리"></span></td>
                    <td><span><input type="button"value="수정"></span></td>
                </tr>
            <!--// loop -->
            </tbody>
        </table>
        <!-- end line -->
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="1px" bgcolor="#eaecf0"></td>
            </tr>
        </table>

        <!-- button -->
        <table>
            <tr>
                <td><span><input type="button" id="ChkTrue" onClick="chk_all('Y')" value="전체선택"><input type="button" id="ChkFalse" onClick="chk_all('N')" value="선택해제"></span></td>
                <td><span><input type="button"value="스페이스등록"><input type="button"value="스페이스삭제"></span></td>
            </tr>
        </table>
        <!-- //button -->
        </div>
        <!-- page -->
        <div class="PageNumber">
        <span><a href="#">이전페이지로</a></span>
        <a href="#">1</a> 
        <a href="#">2</a> 
        <a href="#">3</a> 
        <a href="#">4</a>
        <a href="#">5</a>
        <a href="#">6</a>
        <a href="#">7</a> 
        <a href="#">8</a>
        <a href="#">9</a>
        <a href="#">10</a>
        <span>
        <a href="#">다음페이지로</a>
        </span>
        </div>
        <!--// page -->
        <!--// LIST -->
    
        </td>
        </tr>
        </table>
<!-- bottom -->
<table width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#f4f4f4" class="BottomStyle">
    <tr>
        <td width="900px" height="80px"align="center">Copyright 2009 All rights reserved.</td>
        <td></td>
    </tr>
</table>
</body>
</html>