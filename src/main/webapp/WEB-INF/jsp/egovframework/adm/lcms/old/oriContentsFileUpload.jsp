<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.lang.*,java.math.*" %>
<%@ page import="java.security.*" %>
<%@ page import="com.innorix.servlet.multipartrequest.MultipartRequest" %>
<%@ page import="com.innorix.servlet.compress.Compress" %>
<%@ page import="com.innorix.servlet.integrity.Integrity" %>
<%@ page import="com.innorix.servlet.transfer.InnorixTransfer" %>

<%
// 파일이 저장될 서버측 전체경로
//String saveDir = delimiterReplace(request.getRealPath(request.getServletPath()));
//saveDir = saveDir.substring(0, saveDir.lastIndexOf("/") + 1) + "data";
//localhost
String saveDir = "D:\\KNISE_CUBRID\\workspace\\knise_cubrid\\src\\main\\webapp\\innorix\\InnoDS\\data";
//test_server
//String saveDir = "/NCIS/WebApp/deploy/nise.war/condata2/innorix";
//real_server
//String saveDir = "/contents/dp/InnoDS/data";

int maxPostSize = 2147483000;	// bytes

InnorixTransfer innoTransfer = new InnorixTransfer(request, response, maxPostSize, "UTF-8", saveDir);

String actionFlag		= innoTransfer.GetParameter("_action");					// 동작 구분 getFileInfo(업로드 정보 생성) | attachFile(파일전송) | attachFileCompleted(전송완료)
String uniqueFname		= innoTransfer.GetParameter("_unique_filename");		// 전송 파일의 유니크한 이름
String origFname		= innoTransfer.GetParameter("_orig_filename");			// 원본 파일명
String saveFname		= innoTransfer.GetParameter("_new_filename");			// 저장 파일명
String fileSize			= innoTransfer.GetParameter("_filesize");				// 원본 사이즈
String startOffset		= innoTransfer.GetParameter("_start_offset");			// 전달 되는 분할 파일의 시작 지점
String endOffset		= innoTransfer.GetParameter("_end_offset");				// 전달 되는 분할 파일의 종료 지점

System.out.println("---------------------------------------------------");
System.out.println("uniqueFname \t: " + uniqueFname);
System.out.println("actionFlag \t: " + actionFlag);
System.out.println("origFname \t: " + origFname);
System.out.println("saveFname \t: " + saveFname);
System.out.println("fileSize \t: " + fileSize);
System.out.println("startOffset \t: " + startOffset);
System.out.println("endOffset \t: " + endOffset);

// 중복 파일 존재시 강제 덮어쓰기 설정
innoTransfer.SetOverwrite(false);

// 저장 파일명을 타임스탬프+확장자 형식으로 변경
if (origFname != null)
{
	int ext_pos = origFname.lastIndexOf(".");
	String ext = origFname.substring(ext_pos);
	Date time = new Date();
	saveFname = time.getTime()+ext;
}

// 업로드 파일 저장
String result = innoTransfer.Save(saveFname);
System.out.println("Save(); \t: " + result);

/*
	Save() 함수 리턴값
	- 0000 정상
	- 0001 경로 없음
	- 0002 쓰기 권한 없음
	- 0003 무결성 검사 실패
	- 1001 디렉토리 생성실패
	- 1002 압축해제 실패
*/

if (result == "0002")
{
    // 예외 발생시 클라이언트에 전달할 개발자 정의 코드와 메세지
    // innoTransfer.ShowCustomError("1000(*고정값)", 메세지, 상세내용, 사용자측 재 전송시도 유무 );
    innoTransfer.ShowCustomError("1000", "파일저정 예외사항 발생", "서버측 경로에 쓰기 권한이 존재하지 않습니다!!", false);
}

// 개별 파일 업로드 시작시점
if (actionFlag.equals("getFileInfo"))
{
	System.out.println("---------------------------------------------------");
	System.out.println(uniqueFname + " # start upload");
}

// 개별 파일 업로드 완료시점
if (actionFlag.equals("attachFileCompleted"))
{
	System.out.println("---------------------------------------------------");
	System.out.println(uniqueFname + " # upload done");
}
%>

<%!
private String delimiterReplace(String fullDir)
{
	String ret1 = fullDir.replaceAll("\\\\+", "/");
	String ret2 = ret1.replaceAll("\\/+", "/");

	return ret2;
}
%>