<%@page language="java" contentType="text/html; charset=UTF-8" import="com.epki.api.EpkiApi,
				com.epki.cms.SignedData, com.epki.cert.X509Certificate, com.epki.cert.CertValidator,
				com.epki.exception.*, com.epki.exception.CertificateExpiredException, com.epki.conf.ServerConf,
				com.epki.exception.CertificateRevokedException, com.epki.exception.CRLException" %>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();
	
		String resSignedData = request.getParameter("signedData");
    String resPlainText = request.getParameter("plainText");
    String certPathOption = request.getParameter("certPathOption");
    String certRevokeOption = request.getParameter("revokeOption");
    String signerDN = "";
    String verifyData = "";
    String testResult = "";

    try{
        //  epki-java 초기화
        EpkiApi.initApp();
    }catch(Exception e){
        e.printStackTrace();
    }

    try
    {
        String scvpUrl = "http://scvp.epki.go.kr:8080";			// SCVP 검증서버 IP:PORT

        ServerConf serverConf = new ServerConf();
        int pathOption, revokeOption;

        byte[] bverifyData = null;
        SignedData signedData = new SignedData();
        // 전자 서명 검증
        signedData.verify(resSignedData);
        // 서명자 인증서 획득
        X509Certificate signerCert = signedData.getSignerCert(0);

        // 서명자 인증서 검증
        CertValidator validator = new CertValidator();

        // 인증서 경로검증 옵션 체크
        if (certPathOption.equals("CERT_VERIFY_NONE")) {
            pathOption = CertValidator.REVOKE_CHECK_NONE;
        } else if (certPathOption.equals("CERT_VERIFY_FULLPATH")) {
            pathOption = CertValidator.CERT_VERIFY_FULLPATH;
        } else if (certPathOption.equals("CERT_VERIFY_CACERT")) {
            pathOption = CertValidator.CERT_VERIFY_CACERT;
        } else {
            pathOption = CertValidator.CERT_VERIFY_USERCERT;
        }

        // 인증서 폐지옵션 체크
        if (certRevokeOption.equals("REVOKE_CHECK_NONE")) {
            revokeOption = CertValidator.REVOKE_CHECK_NONE;
        } else if (certRevokeOption.equals("REVOKE_CHECK_CRL")) {
            revokeOption = CertValidator.REVOKE_CHECK_CRL;
        } else if (certRevokeOption.equals("REVOKE_CHECK_OCSP")) {
            revokeOption = CertValidator.REVOKE_CHECK_OCSP;
		} else if (certRevokeOption.equals("REVOKE_CHECK_SCVP")) {
				revokeOption = CertValidator.REVOKE_CHECK_SCVP;
				validator.setSCVPUrl(scvpUrl);
        } else if (certRevokeOption.equals("REVOKE_CHECK_ARL | REVOKE_CHECK_CRL")) {
            revokeOption = CertValidator.REVOKE_CHECK_ARL | CertValidator.REVOKE_CHECK_CRL;
        } else if (certRevokeOption.equals("REVOKE_CHECK_ARL | REVOKE_CHECK_OCSP")) {
            revokeOption = CertValidator.REVOKE_CHECK_ARL | CertValidator.REVOKE_CHECK_OCSP;
        } else if (certRevokeOption.equals("REVOKE_CHECK_CRL | REVOKE_CHECK_OCSP")) {
            revokeOption = CertValidator.REVOKE_CHECK_CRL | CertValidator.REVOKE_CHECK_OCSP;
        } else {
            revokeOption = CertValidator.REVOKE_CHECK_ALL;
        }

        // 인증서 검증 옵션 지정
        validator.setValidateCertPathOption(pathOption);

        // 서명 인증서 설정		SCVP 검증시는 필수
        validator.setServerSignCert(serverConf.getServerCert(ServerConf.CERT_TYPE_SIGN), serverConf.getServerPriKey(ServerConf.CERT_TYPE_SIGN));

        // 인증서 유효성 검증 옵션 (REVOKE_CHECK_SCVP)
        validator.setValidateRevokeOption(revokeOption);

        //인증서 정책 허용 (1.2.410.100001.5.3.1.3 EPKI 일반 인증서만 허용)
        //validator.setUserPolicy("1.2.410.100001.5.3.1.3");

        validator.validate(CertValidator.CERT_TYPE_SIGN, signerCert);

        // 서명한 인증서 획득
        signerDN = signerCert.getSubjectName();

        // 서명 원문 획득
        bverifyData = signedData.getMessage();
        
        String result = "";
        for (int i=0; i < bverifyData.length; i++) {
            result += Integer.toString( ( bverifyData[i] & 0xff ) + 0x100, 16).substring( 1 );
        }

        verifyData = new String(bverifyData);

        testResult = "검증 성공";
    } catch (CertificateExpiredException e) {
        testResult = "유효기간이 만료된 인증서입니다. " + e.toString();
    } catch (CertificateRevokedException e) {
        testResult = "폐지된 인증서입니다. " + e.toString();
    } catch (CRLException e) {
        testResult = "검증 실패 (서명용 인증서의 CRL 획득 여부 혹은 유효성을 확인 하세요!)" + e.toString();
    } catch (EpkiException e) {
        verifyData = e.toString();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>KSign CASE Test</title>

    <!-- 부트스트랩  CSS -->
    <link rel="stylesheet" href="../../kcase/lib/css/bootstrap.min.css"/>

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

            $("footer").css("top", $(window).scrollTop() + $(window).height() - 30);

            $(window).scroll(function () {
                $("footer").css("top", $(window).scrollTop() + $(window).height() - 30);
            });

            $(window).resize(function () {
                $("footer").css("top", $(window).scrollTop() + $(window).height() - 30);
            });
        });
    </script>
</head>
<body>

<div class="header">
    <h1 style="margin: 0px;">
        <div style="height:30px;"></div>
        <span onclick="javascript:location.href='../index.jsp'">HTML5 KSignCase-Agent Server API Test</span>
        <br/>
        <div>KSign Toolkit Development Dept.</div>
    </h1>
</div>

<div style="width:100%; height:1px; background-color: mediumpurple"></div>

<div class="content">
    <h2>전자서명 및 검증 결과 페이지</h2>
    <div>이 페이지는 전자서명을 수행하고 검증하는 기능에 대하여 테스트 합니다.</div>
    <section>
        <div id="sign_test_client" class="api-description-box">
            <h3 class="api-description-msg">전자서명 테스트(클라이언트)</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>클라이언트에서 전자서명을 생성하고 서버에서 검증합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        검증 결과
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%= testResult %></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        클라이언트에서 전송된 전자서명 값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%= resSignedData %></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 검증된 값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%= verifyData %></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서명한 인증서 정보
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%= signerDN %></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" onclick="javascript:history.back()" type="button" value="이전" />
                    </div>
                </div>
            </div>
        </div>
    </section>
</div>
<footer style="position: absolute;">
    <div>
        Copyright 2016. KSIGN all rights reserved.
    </div>
</footer>
</body>
</html>
