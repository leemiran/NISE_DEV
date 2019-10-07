<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>▒▒▒  eGovFrame Potal 온라인 지원 포탈  ▒▒▒</title>
</head>

<body>
<table width="100%" border="1" cellspacing="0" cellpadding="0" height="100%">
  <tr>
    <td align="left" valign="top" width="20%">
        <br/>기본메뉴리스트
        <br/>-사용자
        <br/>&nbsp;&nbsp;<a href="/uss/umt/user/EgovUserManage.do" target="unitPage">업무사용자 관리</a>
        <br/>&nbsp;&nbsp;<a href="/uss/umt/mber/EgovMberManage.do" target="unitPage">일반회원 관리</a>
        <br/>&nbsp;&nbsp;<a href="/uss/umt/entrprs/EgovEntrprsMberManage.do" target="unitPage">기업회원 관리</a>
        <br/>&nbsp;&nbsp;<a href="/uss/umt/cmm/EgovStplatCnfirmMber.do" target="unitPage">일반회원 가입처리</a>
        <br/>&nbsp;&nbsp;<a href="/uss/umt/cmm/EgovStplatCnfirmEntrprs.do" target="unitPage">기업회원 가입처리</a>
        <br/>-메일
        <br/>&nbsp;&nbsp;<a href="/ems/selectSndngMailList.do" target="unitPage">발송메일내역</a>
        <br/>&nbsp;&nbsp;<a href="/ems/selectSndngMailList.do" target="unitPage">발송메일등록</a>
        <br/>-코드
        <br/>&nbsp;&nbsp;<a href="/sym/ccm/ccc/EgovCcmCmmnClCodeList.do" target="unitPage">공통분류코드</a>
        <br/>&nbsp;&nbsp;<a href="/sym/ccm/cde/EgovCcmCmmnDetailCodeList.do" target="unitPage">공통상세코드</a>
        <br/>&nbsp;&nbsp;<a href="/sym/ccm/cca/EgovCcmCmmnCodeList.do" target="unitPage">공통코드</a>
        <br/>&nbsp;&nbsp;<a href="/sym/cal/EgovRestdeList.do" target="unitPage">휴일관리</a>
        <br/>&nbsp;&nbsp;<a href="/sym/ccm/zip/EgovCcmZipList.do" target="unitPage">우편번호관리</a>
        <br/>&nbsp;&nbsp;<a href="/sym/ccm/adc/EgovCcmAdministCodeList.do" target="unitPage">행정코드관리</a>      
    </td>
    <td width="80%" >
        <iframe id="unitPage" name="unitPage" width="100%" height="100%" src="#">
        </iframe>
    </td>
  </tr>
</table>
</body>
</html>
