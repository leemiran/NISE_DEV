<%@ page language="java" 	 contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<html lang="ko">
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<%@page import="java.io.File"%>
<%@page import="com.oreilly.servlet.MultipartRequest"%>
<%@ taglib prefix="c" 		 uri="http://java.sun.com/jsp/jstl/core" %>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
</head>
<%

String path =  request.getSession().getServletContext().getRealPath("/")+ File.separator  +"smartEditor\\photo_uploader\\uploadFolder" ; // 이미지가 저장될 주소
System.out.println("start~~~~~~~~~~~~~~~~");
System.out.println(path);
System.out.println("end~~~~~~~~~~~~~~");
String filename = "";

if(request.getContentLength() > 10*1024*1024 ){
%>
	<script>alert("업로드 용량(총 10Mytes)을 초과하였습니다.");history.back();</script>
<%
	return;

} else {

	try {

		
		MultipartRequest multi=new MultipartRequest(request, path, 15*1024*1024, "UTF-8", new DefaultFileRenamePolicy());
	
		java.text.SimpleDateFormat formatter2 = new java.text.SimpleDateFormat ("yyyy_MM_dd_HHmmss", java.util.Locale.KOREA);
		int cnt = 1;
		String upfile = (multi.getFilesystemName("Filedata"));
		if (!upfile.equals("")) {
			String dateString = formatter2.format(new java.util.Date());
			String moveFileName = dateString + upfile.substring(upfile.lastIndexOf(".") );
			String fileExt = upfile.substring(upfile.lastIndexOf(".") + 1);
			File sourceFile = new File(path + File.separator + upfile);
			File targetFile = new File(path + File.separator + moveFileName);
			sourceFile.renameTo(targetFile);
			filename = moveFileName;
			System.out.println("upfile : " + upfile);
			System.out.println("targetFile : " + targetFile);
			System.out.println("moveFileName : " + moveFileName);
			System.out.println("filename : " + filename);
			System.out.println("moveFileName : " + moveFileName);
			System.out.println("moveFileName : " + moveFileName);
			//sourceFile.delete();
			%>
			<%
			
		}
	} catch (Exception e) {
		System.out.println("e : " + e.getMessage());
	}
}
%>
</html>

<script type="text/javascript" defer="defer">
	
	function fileAttach(){ 
	   	var fname= '<c:out value="<%=filename%>" />';
	    alert("asdfasdf->"+fname);
	    try{
	    	 opener.parent.pasteHTML(fname);
             window.close();
	    }catch(e){ 
             //alert(e); 
	    }
	}
	fileAttach();
	this.window.close();
	
	
</script>
