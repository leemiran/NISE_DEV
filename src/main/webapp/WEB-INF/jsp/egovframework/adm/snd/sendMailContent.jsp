<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<div style="margin-bottom: 10px; border: 1px solid black;">
	<div style="padding: 5px 10px; color: white; background-color: black; text-align: center;">제목</div>
	<div style="padding: 5px 10px;">${sendMailContent.subject}</div>
</div>

${sendMailContent.content}