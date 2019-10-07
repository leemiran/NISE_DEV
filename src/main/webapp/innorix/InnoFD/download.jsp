<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>

<%
String saveDir = delimiterReplace(request.getRealPath(request.getServletPath()));
String filePath = saveDir.substring(0, saveDir.lastIndexOf("/") + 1);

/*
	filePath
		파일이 저장된 스토리지의 전체 경로이며 아래 형식 입니다.
		디렉토리 구분은 윈도우, 유닉스 모두 "/" 문자를 사용 합니다.
		윈도우 예시 - C:/storage/path1/path2/data
		유닉스 예시 - /storage/path1/path2/data
*/

// 클라이언트에서 downloadType을 stream으로 설정하면 자동 전달되는 GET Param 값 입니다.
String szStartOffset = request.getParameter("_StartOffset");
String szEndOffSet = request.getParameter("_EndOffset");

// 파일의 스트림 데이터 참조를 위해 전달되는 GET Param 값 입니다. 형식에 제한되지 않습니다. 
String fileID = request.getParameter("fileID");
String fileName = new String();

if (fileID.equals("111"))
	fileName = "2015 INNORIX Solution Brochure(r2).pdf";
	
if (fileID.equals("222"))
	fileName = "2015 INNORIX Solution Brochure(r2).pdf";
	
if (fileID.equals("333"))
	fileName = "2015 INNORIX Solution Brochure(r2).pdf";

File file = new File(filePath + fileName);

response.setContentType("application/octet-stream");
response.setHeader("Accept-Ranges", "bytes");
response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName +"\"");

long StartOffset = 0;
long EndOffset = 0;
	
if (szStartOffset != null) {
	StartOffset = Long.parseLong(szStartOffset);
}

if (szEndOffSet != null) {
	EndOffset = Long.parseLong(szEndOffSet);
}

long OffsetLength = 0;

if(szStartOffset != null || szEndOffSet != null) {
	OffsetLength = EndOffset - StartOffset + 1;
	response.setHeader("Content-Length", "" + OffsetLength);
} else {
	response.setHeader("Content-Length", "" + file.length());
}

byte b[] = new byte[8192];
BufferedInputStream input = null;
BufferedOutputStream output = null;

try {
	input = new BufferedInputStream(new FileInputStream(file));
	output = new BufferedOutputStream(response.getOutputStream());

	int read = 0;

	if (StartOffset != 0) {
		long n = input.skip(StartOffset);
		if (n == -1) read = -1;
	}

	if(OffsetLength > 0) {
		while (OffsetLength > 0) {
			if(OffsetLength < 8192) {
				b = new byte[(int)OffsetLength];
			}
			read = input.read(b);
			if(read == -1)
				break;

			output.write(b, 0, read);
			OffsetLength = OffsetLength - read;
		}
	}
	
	System.out.println("========== Downloading : " + System.currentTimeMillis() + "==========");
	System.out.println("filePath \t: " + filePath + "/" + fileName	);
	System.out.println("TotalLength \t: " + file.length() );
	System.out.println("StartOffset \t: " + StartOffset);	
	System.out.println("EndOffset \t: " + EndOffset);
	
} catch(Exception e) {
	e.printStackTrace();
} finally {
	if(output != null) {
		output.flush();
		output.close();
	}

	if(input != null) {
		input.close();
	}
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