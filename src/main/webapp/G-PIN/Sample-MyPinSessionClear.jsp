<%@ page language = "java" contentType = "text/html; charset=UTF-8"%>

<%
    /**
     * session에 저장된 사용자정보를 삭제하는 페이지입니다.
     * 사용자 정보확인이 끝나서 더이상 필요가없다고 생각되면 반드시 session에서 사용자정보를 삭제해야합니다.
     */
    session.setAttribute("dupInfo", null);
    session.setAttribute("coInfo1", null);
    session.setAttribute("coInfo2", null);
    session.setAttribute("ciupdate", null);
    session.setAttribute("virtualNo", null);
    session.setAttribute("status", null);

    response.sendRedirect("Sample-MyPinAuthResponse.jsp");
%>
