package egovframework.svt.cert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class SciService {

	protected Log log = LogFactory.getLog(this.getClass());

	public String getEncriptData(String srvNo, HttpServletResponse response,
			String addVar) {
		String reqInfo = "";

		try {
			// 날짜 생성
			Calendar today = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String day = sdf.format(today.getTime());

			java.util.Random ran = new Random();
			// 랜덤 문자 길이
			int numLength = 6;
			String randomStr = "";

			for (int i = 0; i < numLength; i++) {
				// 0 ~ 9 랜덤 숫자 생성
				randomStr += ran.nextInt(10);
			}

			String id = "SPEQ001"; // 본인실명확인 회원사 아이디
			// String srvNo = "010002"; // 본인실명확인 서비스번호
			String reqNum = day + randomStr; // request.getParameter("reqNum");
												// // 본인실명확인 요청번호
			String exVar = "0000000000000000"; // 복호화용 임시필드
			String certDate = day; // request.getParameter("certDate"); //
									// 본인실명확인 요청시간
			String certGb = "H"; // request.getParameter("certGb"); // 본인실명확인
									// 본인확인 인증수단
			// String addVar = ""; //request.getParameter("addVar"); // 본인실명확인
			// 추가 파라메터

			/**
			 * 
			 * reqNum 값은 최종 결과값 복호화를 위한 SecuKey로 활용 되므로 중요합니다. reqNum 은 본인 확인
			 * 요청시 항상 새로운 값으로 중복 되지 않게 생성 해야 합니다. 쿠키 또는 Session및 기타 방법을 사용해서
			 * reqNum 값을 pcc_V3_result_seed.jsp에서 가져 올 수 있도록 해야 함. 샘플을 위해서 쿠키를
			 * 사용한 것이므로 참고 하시길 바랍니다.
			 * 
			 */
			Cookie c = new Cookie("reqNum", reqNum);
			// c.setMaxAge(1800); // <== 필요시 설정(초단위로 설정됩니다)
			c.setPath("/cert/sci");
			response.addCookie(c);

			// 01. 암호화 모듈 선언
			com.sci.v2.pcc.secu.SciSecuManager seed = new com.sci.v2.pcc.secu.SciSecuManager();

			// 02. 1차 암호화
			String encStr = "";
			reqInfo = id + "^" + srvNo + "^" + reqNum + "^" + certDate + "^"
					+ certGb + "^" + addVar + "^" + exVar; // 데이터 암호화
			encStr = seed.getEncPublic(reqInfo);

			// 03. 위변조 검증 값 생성
			com.sci.v2.pcc.secu.hmac.SciHmac hmac = new com.sci.v2.pcc.secu.hmac.SciHmac();
			String hmacMsg = hmac.HMacEncriptPublic(encStr);

			// 03. 2차 암호화
			reqInfo = seed.getEncPublic(encStr + "^" + hmacMsg + "^"
					+ "0000000000000000"); // 2차암호화
		} catch (Exception e) {
			log.error(e.toString());
		}

		return reqInfo;
	}

	public Map<String, String> parseResultToMap(HttpServletRequest request) {
		Map<String, String> resultMap = new HashMap<String, String>();

		String retInfo = ""; // 결과정보

		String name = ""; // 성명
		String sex = ""; // 성별
		String birYMD = ""; // 생년월일
		String fgnGbn = ""; // 내외국인 구분값

		String di = ""; // DI
		String ci1 = ""; // CI
		String ci2 = ""; // CI
		String civersion = ""; // CI Version

		String reqNum = ""; // 본인확인 요청번호
		String result = ""; // 본인확인결과 (Y/N)
		String certDate = ""; // 검증시간
		String certGb = ""; // 인증수단
		String cellNo = ""; // 핸드폰 번호
		String cellCorp = ""; // 이동통신사
		String addVar = "";

		// 복화화용 변수
		String encPara = "";
		String encMsg = "";
		String msgChk = "N";

		Cookie[] cookies = request.getCookies();
		String cookiename = "";
		String cookiereqNum = "";
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				cookiename = c.getName();
				cookiereqNum = c.getValue();
				if (cookiename.compareTo("reqNum") == 0)
					break;

				cookiereqNum = null;
			}
		}

		try {
			retInfo = request.getParameter("retInfo").trim();

			// 1. 암호화 모듈 (jar) Loading
			com.sci.v2.pcc.secu.SciSecuManager sciSecuMg = new com.sci.v2.pcc.secu.SciSecuManager();
			// 쿠키에서 생성한 값을 Key로 생성 한다.
			retInfo = sciSecuMg.getDec(retInfo, cookiereqNum);

			String[] aRetInfo1 = retInfo.split("\\^");

			encPara = aRetInfo1[0]; // 암호화된 통합 파라미터
			encMsg = aRetInfo1[1]; // 암호화된 통합 파라미터의 Hash값

			String encMsg2 = sciSecuMg.getMsg(encPara);
			// 3.위/변조 검증
			// ---------------------------------------------------------------
			if (encMsg2.equals(encMsg)) {
				msgChk = "Y";
			}

			if (!msgChk.equals("N")) {
				// 복호화 및 위/변조 검증
				// ---------------------------------------------------------------
				retInfo = sciSecuMg.getDec(encPara, cookiereqNum);

				String[] aRetInfo = retInfo.split("\\^");

				name = aRetInfo[0];
				birYMD = aRetInfo[1];
				sex = aRetInfo[2];
				fgnGbn = aRetInfo[3];
				di = aRetInfo[4];
				ci1 = aRetInfo[5];
				ci2 = aRetInfo[6];
				civersion = aRetInfo[7];
				reqNum = aRetInfo[8];
				result = aRetInfo[9];
				certGb = aRetInfo[10];
				cellNo = aRetInfo[11];
				cellCorp = aRetInfo[12];
				certDate = aRetInfo[13];
				addVar = aRetInfo[14];

				resultMap.put("name", name);
				resultMap.put("birYMD", birYMD);
				resultMap.put("sex", sex);
				resultMap.put("fgnGbn", fgnGbn);
				resultMap.put("di", di);
				resultMap.put("ci1", ci1);
				resultMap.put("ci2", ci2);
				resultMap.put("civersion", civersion);
				resultMap.put("reqNum", reqNum);
				resultMap.put("result", result);
				resultMap.put("certGb", certGb);
				resultMap.put("cellNo", cellNo);
				resultMap.put("cellCorp", cellCorp);
				resultMap.put("certDate", certDate);
				resultMap.put("addVar", addVar);
			}

			resultMap.put("msgChk", msgChk);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return resultMap;
	}
}
