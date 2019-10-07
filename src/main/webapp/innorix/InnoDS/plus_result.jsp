<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%request.setCharacterEncoding("utf-8");%>

<%
/*
Plus 전송창에서 업로드 완료후 전달되는 파라메터명, 파라메터값(POST)
클라이언트측에서 1개 파일 업로드가 완료된 시점에 현재 페이지 호출

파라메터 명 : 파라메터 값
_innorix_origfilename : 원본 파일명
_innorix_savefilename : 저장 파일명
_innorix_savepath : 저장위치(풀패스)
_innorix_filesize : 파일 사이즈
{개발자가 정의한 파라메터명} : {파라메터 값}
*/

String[] origfilename = request.getParameter("_innorix_origfilename").split("\\|"); // 원본 파일명
String[] savefilename = request.getParameter("_innorix_savefilename").split("\\|"); // 저장 파일명
String[] savepath = request.getParameter("_innorix_savepath").split("\\|", -1);      // 파일 저장경로
String[] filesize = request.getParameter("_innorix_filesize").split("\\|");			// 파일 사이즈
String[] foldername = request.getParameter("_innorix_folder").split("\\|", -1);			// 폴더정보(폴더 업로드시만)
String[] customvalue = request.getParameter("_innorix_customvalue").split("\\|", -1);	// 개발자 정의값
String[] componentname = request.getParameter("_innorix_componentname").split("\\|", -1);// 컴포넌트 이름

if (origfilename != null)
{
    for (int i = 0; i < origfilename.length; i++)
    {
		System.out.println("origfilename : " + origfilename[i]);
		System.out.println("savefilename : " + savefilename[i]);
		System.out.println("savepath : " + savepath[i]);
		System.out.println("filesize : " + filesize[i]);
		System.out.println("foldername : " + foldername[i]);
		System.out.println("customvalue : " + customvalue[i]);
		System.out.println("componentname : " + componentname[i]);
	}
}

/*
java.util.Enumeration er = request.getParameterNames();
while (er.hasMoreElements())
{
	String requestParamName = (String) er.nextElement();
	String requestParamValue = request.getParameter(requestParamName);
	System.out.println(requestParamName + " : " + requestParamValue);
}
*/
%>