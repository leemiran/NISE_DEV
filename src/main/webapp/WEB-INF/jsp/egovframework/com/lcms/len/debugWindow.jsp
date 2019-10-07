<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
	function debug(str){
		var debugArea=document.getElementById("debug_div");
		debugArea.innerHTML+="<BR>"+"<font color='blue'>"+str+"</font>";
	}
	function error(str){
		var debugArea=document.getElementById("debug_div");
		debugArea.innerHTML+="<BR>"+"<font color='RED'>"+str+"</font>";
	}	
</script>
</head>
<body>
<div id="debug_div">
</div>
</body>
</html>