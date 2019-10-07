<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%request.setCharacterEncoding("utf-8");%>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<head>
<title>Innorix File Transfer Solution - InnoDS Plus</title>
</head>

<body>
<%
request.setCharacterEncoding("utf-8");
String[] origfilename = request.getParameterValues("_innorix_origfilename");    // 원본 파일명
String[] savefilename = request.getParameterValues("_innorix_savefilename");    // 저장 파일명
String[] savepath = request.getParameterValues("_innorix_savepath");            // 저장위치(풀패스)
String[] filesize = request.getParameterValues("_innorix_filesize");            // 파일 사이즈
String[] customvalue = request.getParameterValues("_innorix_customvalue");      // 개발자 정의값
String[] foldername = request.getParameterValues("_innorix_folder");            // 폴더정보(폴더 업로드시)
String[] componentname = request.getParameterValues("_innorix_componentname");  // 전송한 컴포넌트 이름



if (origfilename != null)
{
    for (int i = 0; i < origfilename.length; i++)
    {

        /* 여기에 업로드 파일 정보를 DB에 입력하는 코드를 작성 합니다. */

        origfilename[i]
        savefilename[i]
        savepath[i]
        filesize[i]
        customvalue[i]
        foldername[i]
        componentname[i]

    }
}
%>


</body>
</html>