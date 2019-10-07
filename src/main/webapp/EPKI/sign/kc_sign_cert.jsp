<%@page language="java" contentType="text/html; charset=UTF-8" import="java.math.BigInteger, java.util.Date,
				com.epki.api.EpkiApi, com.epki.cms.SignedData, com.epki.cert.X509Certificate,
				com.epki.cert.CertValidator, com.epki.crypto.*, com.epki.util.Base64,
				com.epki.exception.*, java.io.IOException, java.io.UnsupportedEncodingException"%>
<%
    HttpSession m_Session = request.getSession();
    String sessionID = m_Session.getId();

    String strSignedData = request.getParameter("signedData");
    String strVerifyData = "";
    int nVersion = 0;
    BigInteger bserial = new BigInteger("0", 16);
    String strSerial = "";
    String strCertSignAlgID = "";
    String strCertIssuer = "";
    String strCertSubject = "";
    String strCertFromDate = "";
    String strCertToDate = "";
    String strCertSignature = "";
    String strCertKeyUsage = "";
    String strCertExtKeyUsage = "";
    String strCertPolicy = "";
    String strCertPolicyID = "";
    String strCertBaseConst = "";
    String strCertSubjectAlt = "";
    String strCertAuthKeyID = "";
    String strCertSubKeyID = "";
    String strCertCRLDP = "";
    String strCertAIA = "";
    String strCertPublicKey = "";

    String testResult = "";
	
	    //  epki-java 초기화
    EpkiApi.initApp();

    try
    {
        byte[] bverifyData = null;
        byte[] bpublicKey = null;

        int pathOption, revokeOption;

        SignedData signedData = new SignedData();
        Base64 base64 = new Base64();

        // 전자 서명 검증
        signedData.verify(strSignedData);

        // 서명자 인증서 획득
        X509Certificate signerCert = signedData.getSignerCert(0);
        // 서명 원문 획득
        bverifyData = signedData.getMessage();
        strVerifyData = new String(bverifyData);

        nVersion = signerCert.getVersion();
        bserial = signerCert.getSerialNumber();
        strSerial = bserial.toString(16);
        strCertSignAlgID = signerCert.getSignatureAlgorithm();
        strCertIssuer = signerCert.getIssuerName();
        strCertSubject = signerCert.getSubjectName();
        Date dbefore = signerCert.getNotBefore();
        strCertFromDate = dbefore.toString();
        Date dafter = signerCert.getNotAfter();
        strCertToDate = dafter.toString();
        strCertSignature = signerCert.getSignature();
        strCertKeyUsage = signerCert.getKeyUsage();
        strCertExtKeyUsage = signerCert.getExtKeyUsage();
        strCertPolicy = signerCert.getCertPolicy();
        strCertPolicyID = signerCert.getCertPolicyID().replaceAll(" ",".");
        strCertBaseConst = signerCert.getBasicConstraints();
        strCertSubjectAlt = base64.encode(signerCert.getSubjectAltName());
        strCertAuthKeyID = signerCert.getAuthKeyID();
        strCertSubKeyID = signerCert.getSubKeyID();
        strCertCRLDP = signerCert.getCRLDP();
        strCertAIA = signerCert.getAIA();

        PublicKey publicKey = signerCert.getSubjectPublicKey();
        bpublicKey = publicKey.getKey();

		/* 정보 표시 방법에 따라 다양한 방법으로 변형하여 처리 */
        // 공개키 데이터 Base64 인코딩
        strCertPublicKey = base64.encode(bpublicKey);

        testResult = "성공";
    } catch (EpkiException e) {
        testResult = "전자서명 검증 실패" + e.toString();
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
        <div id="sign_test_server" class="api-description-box">
            <h3 class="api-description-msg">전자서명 데이터 내 인증서 정보 추출</h3>
            <!-- 테스트 설명 -->
            <div class="api-description-content"><h5>입력된 전자서명 데이터로 부터 인증서 정보를 추출하는 기능에 대하여 테스트 합니다.</h5></div>
            <div class=".api-description-box-gray">
                <div class="api-description-subject"><h4>기능 시험</h4></div>
                <div class="api-description-func">
                    <div style="width:100%;" class="">
                        <form action="" method="post">
                            <table class="table table-bordered">
                                <tr>
                                    <td class="table-input-name">
                                        테스트 결과
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=testResult%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        클라이언트에서 전송된 전자서명 값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strSignedData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서버에서 검증된 값
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strVerifyData%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        버전
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=nVersion%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        시리얼 넘버
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strSerial%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        서명 알고리즘
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertSignAlgID%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        발급자 이름
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertIssuer%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        주체 이름
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertSubject%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        유효기간 시작
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertFromDate%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        유효기간 끝
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertToDate%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        전자서명
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertSignature%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        키 사용
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertKeyUsage%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        인증서 정책
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertPolicy%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        주체 대체 이름
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertSubjectAlt%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        기관 키 식별자
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertAuthKeyID%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        주체 키 식별자
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertSubKeyID%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        CRL 배포 지점
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertCRLDP%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        기관 정보 엑세스
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertAIA%></textarea>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="table-input-name">
                                        공개 키
                                    </td>
                                    <td class="table-input-text">
                                        <textarea ondblclick="textareaResize(this)"><%=strCertPublicKey%></textarea>
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
