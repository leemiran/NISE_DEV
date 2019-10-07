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
    <title>Popup Menu</title>

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

            $("#search").click(function () {
                var userName = $("#userName").val();
                var userMail = $("#userMail").val();

                epki.genSymmetricKey(
                        "SEED",
                        function(symKey) {
                            epki.encrypt(
                                    "SEED",
                                    symKey,
                                    userName,
                                    function(encData) {
                                        // 암호화된 데이터
                                        alert(encData);
                                        $("#encData").val(encData);
                                    }
                            );
                        },
                        function(c, msg) {
                            alert(c + msg);
                        }
                );
            });

            $("#confirm").click(function() {
                window.opener.getReturnValue($("#userName").val());
                window.close();
            });

        });
    </script>
</head>
<body>
<table>
    <tr>
        <td>이름 : </td>
        <td><input type="text" id="userName" /></td>
    </tr>
    <tr>
        <td>e-mail : </td>
        <td><input type="text" id="userMail" /></td>
    </tr>
</table>

<input type="text" id="encData"/>
<button class="kc-btn-blue" id="search">ID/PW 찾기</button>
<button class="kc-btn-blue" id="confirm">확인</button>
</body>
</html>