<%@page language="java" contentType="text/html; charset=UTF-8" import="com.epki.api.EpkiApi,
				com.epki.cms.SignedData, com.epki.cert.X509Certificate, com.epki.cert.CertValidator,
				com.epki.crypto.*, com.epki.util.Base64, com.epki.conf.ServerConf,
				com.epki.exception.*, java.io.IOException"%>

<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Popup Main</title>

    <!-- KSign Style Sheet -->
    <link rel="stylesheet" href="../../kcase/lib/css/kcase.css"/>

    <!-- Sample CSS -->
    <link rel="stylesheet" href="../../kcase/lib/css/sample.css"/>

    <script src="../../kcase/lib/js/jquery-1.11.3.js"></script>
    <script src="../../kcase/lib/js/jquery-ui.js"></script>

    <script src="../../kcase/lib/js/kcase_os_check.js"></script>
    <script src="../../kcase/EPKICommon.js"></script>

    <script type="text/javascript">
        $(document).ready(function() {
            var sessionId = '<%=sessionID%>';

            /* init - server's sessionId */
            epki.init(sessionId);

            $("#searchId").click(function () {

                var cw = screen.availWidth;     //화면 넓이
                var ch = screen.availHeight;    //화면 높이

                var sw = 300;    //띄울 창의 넓이
                var sh = 300;    //띄울 창의 높이

                var ml = (cw - sw) / 2;        //가운데 띄우기위한 창의 x위치
                var mt = (ch - sh) / 2;         //가운데 띄우기위한 창의 y위치

                var newWindow = window.open("popupmenu.jsp", "_blank", 'width=' + sw + ',height=' + sh + ',top=' + mt + ',left=' + ml);
                newWindow.focus();
            });

            // 팝업창에서 가져올 데이터
            window.getReturnValue = function(returnValue) {
                alert(returnValue);

                epki.genSymmetricKey(
                        "SEED",
                        function(symKey) {
                            alert(symKey.key);
                            epki.encrypt(
                                    "SEED",
                                    symKey,
                                    returnValue,
                                    function(encData) {
                                        // 암호화된 데이터
                                        alert(encData);
                                    }
                            );
                        },
                        function(c, msg) {
                            alert(c + msg);
                        }
                );
            }
        });


    </script>
</head>
<body>
<table>
    <tr>
        <td>ID : </td>
        <td><input type="text" id="userId" /></td>
    </tr>
    <tr>
        <td>PW : </td>
        <td><input type="password" id="password" /></td>
    </tr>
</table>

<button class="kc-btn-blue" id="searchId">ID/PW 찾기</button>
<button class="kc-btn-blue">확인</button>
</body>
</html>