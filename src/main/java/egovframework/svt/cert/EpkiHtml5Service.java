package egovframework.svt.cert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import com.epki.api.EpkiApi;
import com.epki.cert.CertValidator;
import com.epki.cert.X509Certificate;
import com.epki.cms.EnvelopedData;
import com.epki.cms.SignedData;
import com.epki.conf.ServerConf;
import com.epki.crypto.PrivateKey;
import com.epki.crypto.SecretKey;
import com.epki.exception.CertificateExpiredException;
import com.epki.exception.CertificateRevokedException;
import com.epki.exception.EpkiException;
import com.epki.session.SecureSession;
import com.epki.util.Base64;
import com.epki.vid.IdentityValidator;

@Service
public class EpkiHtml5Service {

	protected Log log = LogFactory.getLog(this.getClass());

	public void getHtml5ServerCert(HttpServletRequest request, ModelMap model) {
		String strServerCert = "";
		String sessionID = request.getSession().getId();

		System.out.println("@@@@@@@@@  " + sessionID);

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
			System.out.println("Session Request Error!<BR>");
			System.out.println(e.toString());
		} catch (Exception e) {
			System.out.println("Session Request Error!<BR>");
			System.out.println(e.toString());
		}

		System.out.println("strServerCert ----> " + strServerCert);
		System.out.println("sessionID ----> " + sessionID);

		model.addAttribute("serverCert", strServerCert);
		model.addAttribute("sessionId", sessionID);

	}

	public void getHtml5Dn(HttpServletRequest request, ModelMap model,
			Map<String, Object> commandMap) throws IOException {
		HttpSession m_Session = request.getSession();
		String sessionID = m_Session.getId();

		String loginRequestData = request.getParameter("reqSecLoginData");

		String testResult = "";

		String subjectDn = "";
		String issuerDn = "";
		BigInteger bserial = null;

		String strAlgID = "";
		String strSessionKey = "";
		String strSessionIV = "";
		String birthDate = "";
		String sex = "";

		try {
			EpkiApi.initApp();

			Base64 base64 = new Base64();

			byte[] bsessionKey = null;
			byte[] bsessionIV = null;
			byte[] bencryptedData = null;
			byte[] bdecryptData = null;

			// 서버 설정 객체를 생성합니다.
			ServerConf serverConf = new ServerConf();

			// 서버 인증서 정보를 가져옵니다.
			X509Certificate cert = serverConf
					.getServerCert(ServerConf.CERT_TYPE_KM);
			PrivateKey priKey = serverConf
					.getServerPriKey(ServerConf.CERT_TYPE_KM);

			// 채널보안 로그인 동시수행 메시지에서 채널보안 키 정보를 읽어옵니다.
			EnvelopedData envloped = new EnvelopedData();
			byte[] developedData = envloped.develop(
					base64.decode(loginRequestData), cert, priKey);

			// 채널보안에서 사용할 키를 가져옵니다.
			SecretKey secretKey = envloped.getSecretKey();

			SignedData signedData = new SignedData();

			// 채널보안 로그인 동시수행 메시지에서 로그인 요청 메시지를 읽어옵니다.
			signedData.verify(base64.decode(new String(developedData)));
			byte[] reqLoginBytes = signedData.getMessage();

			System.out.println("getHtml5Dn reqLoginBytes ----> "
					+ reqLoginBytes);

			// JSON Parse Start
			JSONObject reqJsonData = (JSONObject) JSONValue.parse(new String(
					reqLoginBytes));

			// 로그인 요청 메시지에서 R값을 추출합니다.
			String rNumber = (String) reqJsonData.get("RN");

			// 로그인 요청 메시지에서 VID 값을 읽어옵니다.
			String vid = (String) reqJsonData.get("VID");

			// 인증 요청 메시지로부터 클라이언트 인증서를 획득합니다.
			X509Certificate loginCert = signedData.getSignerCert(0);

			if (vid != null) {

				// 요청자 VID 정보가 포함될 경우 신원확인을 합니다.
				loginCert.verifyVID(base64.decode(rNumber), vid);

				birthDate = "19" + vid.substring(0, 6);

				sex = vid.toString().substring(6, 7);

				if ("1".equals(sex)) {
					sex = "M";
				} else if ("2".equals(sex)) {
					sex = "F";
				}

			}

			// 인증서를 검증하기 위해 CertValidator 객체를 생성합니다.
			CertValidator validator = new CertValidator();

			// 클라이언트 인증서를 검증할 옵션을 설정합니다.
			validator
					.setValidateCertPathOption(CertValidator.CERT_VERIFY_FULLPATH);
			validator.setValidateRevokeOption(CertValidator.REVOKE_CHECK_CRL);

			// 클라이언트 인증서를 검증합니다.
			validator.validate(CertValidator.CERT_TYPE_SIGN, loginCert);

			strAlgID = secretKey.getKeyAlg();
			bsessionKey = secretKey.getKey();
			bsessionIV = secretKey.getIV();

			// 세션키 Base64 인코딩
			Base64 encoder = new Base64();
			strSessionKey = encoder.encode(bsessionKey);
			strSessionIV = encoder.encode(bsessionIV);

			m_Session.setAttribute("sessionKey", strSessionKey);
			m_Session.setAttribute("sessionIV", strSessionIV);
			m_Session.setAttribute("algorithm", strAlgID);

			// 요청자 인증서 정보 획득
			subjectDn = loginCert.getSubjectName();
			issuerDn = loginCert.getIssuerName();

			// 로그인한 사용자 DN
			System.out.println("getHtml5Dn dn ----> " + subjectDn);
			System.out.println("getHtml5Dn birthDate ----> " + birthDate);
			System.out.println("getHtml5Dn sex ----> " + sex);
			commandMap.put("dn", subjectDn);
			commandMap.put("birthDate", birthDate);
			commandMap.put("sex", sex);

			bserial = new BigInteger("1");
			bserial = loginCert.getSerialNumber();

			testResult = "성공";

		} catch (CertificateExpiredException e) {
			testResult = "유효기간이 만료된 인증서입니다. " + e.toString();
		} catch (CertificateRevokedException e) {
			testResult = "폐지된 인증서입니다. " + e.toString();
		} catch (EpkiException e) {
			testResult = e.toString();
		}

	}

}
