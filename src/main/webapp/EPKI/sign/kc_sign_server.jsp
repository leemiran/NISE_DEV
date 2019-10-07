<%@page language="java" contentType="text/html; charset=UTF-8" import="com.epki.api.EpkiApi,
				com.epki.cms.SignedData, com.epki.cert.X509Certificate, com.epki.cert.CertValidator,
				com.epki.crypto.*, com.epki.util.Base64, com.epki.conf.ServerConf, ksign.jce.util.*, ksign.jce.provider.pkcs.*,
				com.epki.exception.*, java.io.IOException"%>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String tempPlainText = request.getParameter("plainText");
    String testSignedData = "";
    String testResult = "";

    String testPlainText = new String(tempPlainText.getBytes("ISO-8859-1"), "UTF-8");
	
		try
    {
        //  epki-java 초기화
        EpkiApi.initApp();
				
        byte[] bsignData = null;

        // server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
        ServerConf conf = new ServerConf();

        // 전자서명 위한 서명용 인증서 설정
        X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_SIGN);
        PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_SIGN);

        if(priKey == null) {
            throw new EpkiException("서버 개인키가 없음");
        }
        if(cert == null) {
            throw new EpkiException("서버 인증서가 없음");
        }

        // 서명용 인증서 검증
        CertValidator validator = new CertValidator();

        // 인증서 검증 옵션 지정
        validator.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);

		// [기본 설정]
		// SCVP 검증
		String scvpUrl = "http://scvp.epki.go.kr:8080";			// SCVP 검증서버 IP:PORT
		validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_SCVP);
		validator.setSCVPUrl(scvpUrl);
		validator.setServerSignCert(cert,priKey);
        
        // CRL 검증
        //validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_CRL);

        // ARL,CRL 검증
        //validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ARL | CertValidator.REVOKE_CHECK_CRL);

        // NONE
        //validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_NONE);

        // OCSP 검증
        // 사용자 인증서 내 AIA 필드에 OCSP 서버 정보가 없을 경우 server.conf 내 OCSP_Server 변수에 기본 OCSP 검증 서버 설정 가능
        // validator.setServerSignCert(cert, priKey);
        // validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_OCSP);

        validator.validate(CertValidator.CERT_TYPE_SIGN, cert);
				
		bsignData = PKCS7.signedData(testPlainText.getBytes(), cert.getX509CertObj(), priKey.getPrivKey(), "SHA1", true);

        // 전자서명 데이터 Base64 인코딩
        Base64 base64 = new Base64();
        testSignedData = base64.encode(bsignData);
    } catch (CertificateExpiredException e) {
        testResult = "유효기간이 만료된 인증서입니다. " + e.toString();
    } catch (CertificateRevokedException e) {
        testResult = "폐지된 인증서입니다. " + e.toString();
    } catch (EpkiException e) {
        testResult = "서명 생성 실패" + e.toString();
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

            var signSerInputs = $("#sign_test_server .api-btn-list").find("input");
            var signSerTexts = $("#sign_test_server .table-input-text").find("textarea");
            signSerInputs.eq(1).click(function() {
                /* Call Back Function Definition */
                var success = function(output) {
                    signSerTexts.eq(2).val(output);
                };
                var error = function(errCode, errMsg) {
                    alert(errCode + ": " + errMsg);
                };

                /* get signedData from textarea */
                var signedData = signSerTexts.eq(1).val();

                /* signedData verify */
                epki.verify(1, signedData, "", success, error);
            });

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
        <div id="sign_test_server" class="api-description-box">
            <h3 class="api-description-msg">전자서명 테스트(서버)</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>서버에서 생성된 전자서명을 클라이언트에서 검증합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        원문 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testPlainText%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서명 메시지
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testSignedData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        클라이언트 검증 결과
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testResult%></textarea>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                    <div class="api-btn-list">
                        <input class="kc-btn-blue" onclick="javascript:history.back()" type="button" value="이전" />
                        <input class="kc-btn-blue" type="button" value="검증" />
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
