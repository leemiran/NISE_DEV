package egovframework.svt.cert;

import java.math.BigInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.epki.api.EpkiApi;
import com.epki.cert.CertValidator;
import com.epki.cert.X509Certificate;
import com.epki.conf.ServerConf;
import com.epki.crypto.PrivateKey;
import com.epki.crypto.SecretKey;
import com.epki.exception.CertificateExpiredException;
import com.epki.exception.EpkiException;
import com.epki.session.SecureSession;
import com.epki.util.Base64;
import com.epki.vid.IdentityValidator;

@Service
public class EpkiService {

	protected Log log = LogFactory.getLog(this.getClass());

	public String getServerCert() {
		String strServerCert = "";

		try {
			// epki-java 초기화
			EpkiApi.initApp();

			byte[] bsserverCert = null;

			// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
			ServerConf conf = new ServerConf();

			// 복호화를 위한 키관리용 인증서 설정
			X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);

			bsserverCert = cert.getCert();

			// 서버인증서 Base64 인코딩
			Base64 encoder = new Base64();
			strServerCert = encoder.encode(bsserverCert);
		} catch (EpkiException e) {
			log.error(e.toString());
			return strServerCert;
		} catch (Exception e) {
			log.error(e.toString());
			return strServerCert;
		}

		return strServerCert;
	}

	public boolean isCert(HttpServletRequest request) {
		HttpSession m_Session = request.getSession();
		String sessionID = m_Session.getId();
		String strRequestData = request.getParameter("SessionRequestData");

		String strAlgID = "";
		String strSessionKey = "";
		String strSessionIV = "";
		String strUserDN = new String();
		String strIssuerDN = new String();

		String chkValue = "";

		try {
			// epki-java 초기화
			EpkiApi.initApp();

			if (strRequestData == null)
				throw new Exception("전달 파라미터 값이 없습니다. 로그인 후 다시 사용하세요");

			byte[] bsessionKey = null;
			byte[] bsessionIV = null;
			SecureSession secureSession = new SecureSession();
			secureSession.setSessionID(sessionID);

			// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
			ServerConf conf = new ServerConf();

			// 복호화를 위한 키관리용 인증서 설정
			X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);

			PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM,
					"");

			secureSession.response(cert, priKey, strRequestData);

			// 보안 세션 요청자 인증서 획득
			X509Certificate clientCert;
			clientCert = secureSession.getClientCert();

			// 요청자 인증서 검증
			CertValidator validator = new CertValidator();

			// 인증서 검증 옵션 지정
			// 인증서 체인 구성 옵션
			validator
					.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);

			// [기본 설정]
			// ARL,CRL 검증
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ARL
					| CertValidator.REVOKE_CHECK_CRL);
			// OCSP 검증
			// 사용장 인증서 내 AIA 필드에 OCSP 서버 정보가 없을 경우 server.conf 내 OCSP_Server 변수에
			// 기본 OCSP 검증 서버 설정 가능
			// PrivateKey priKey_Sign =
			// conf.getServerPriKey(ServerConf.CERT_TYPE_SIGN, "");
			// validator.setOCSPCert(conf.getServerCert(1),priKey_Sign);
			// validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ARL
			// | CertValidator.REVOKE_CHECK_CRL);

			validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);

			// 요청자 인증서 정보 획득
			strUserDN = clientCert.getSubjectName();
			strIssuerDN = clientCert.getIssuerName();
			BigInteger bserial = new BigInteger("1");
			bserial = clientCert.getSerialNumber();

			// 세션키 획득
			SecretKey secretKey = secureSession.getSK();

			strAlgID = secretKey.getKeyAlg();
			bsessionKey = secretKey.getKey();
			bsessionIV = secretKey.getIV();

			// 세션키 Base64 인코딩
			Base64 encoder = new Base64();
			strSessionKey = encoder.encode(bsessionKey);
			strSessionIV = encoder.encode(bsessionIV);

			m_Session.setAttribute("sessionKey", strSessionKey);
			m_Session.setAttribute("sessionIV", strSessionIV);
			m_Session.setAttribute("AlgID", strAlgID);
			m_Session.setAttribute("UserDN", strUserDN);
			m_Session.setAttribute("IssuerDN", strIssuerDN);
			m_Session.setAttribute("Serial", bserial);

		} catch (EpkiException e) {
			log.error(e.toString());
			return false;
		} catch (CertificateExpiredException e) {
			log.error(e.toString());
			return false;
		} catch (Exception e) {
			log.error(e.toString());
			return false;
		}
		return true;
	}

	public String getUserDN(HttpServletRequest request) {
		String strRequestData = request.getParameter("RequestData");
		// 요청 데이터에 신원확인 정보가 포함되어 있는 경우 사용하지 않음
		// String strUserID = request.getParameter("UserID");
		String strUserDN = "";

		// epki-java 초기화
		try {
			EpkiApi.initApp();

			// server.conf 파일로 부터 서버에 설정된 인증서 및 개인키의 경로 정보를 얻음
			ServerConf conf = new ServerConf();

			// 복호화를 위한 키관리용 인증서 설정
			X509Certificate cert = conf.getServerCert(ServerConf.CERT_TYPE_KM);

			PrivateKey priKey = conf.getServerPriKey(ServerConf.CERT_TYPE_KM,
					"");

			// 요청자 신원확인 정보 검증
			IdentityValidator vidData = new IdentityValidator();
			// out.println(strRequestData);
			// vidData.verify(cert, priKey, strRequestData, strUserID);
			vidData.verify(cert, priKey, strRequestData, "");

			// 요청 데이터에 신원확인 정보가 포함되어 있는 경우
			// vidData.verify(cert, priKey, strRequestData, "");

			// 요청자 인증서 획득
			X509Certificate clientCert;
			clientCert = vidData.getUserCert();

			// 요청자 인증서 검증
			CertValidator validator = new CertValidator();

			// 인증서 검증 옵션 지정
			validator
					.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_ALL);

			validator.validate(CertValidator.CERT_TYPE_SIGN, clientCert);

			// 요청자 인증서 정보 획득
			strUserDN = clientCert.getSubjectName();
			log.info(">>>>>>>>>>>>>" + strUserDN);
		} catch (EpkiException e) {
			log.error(e.toString());
		} catch (CertificateExpiredException e) {
			log.error(e.toString());
		} catch (Exception e) {
			log.error(e.toString());
		}
		return strUserDN;
	}
}
