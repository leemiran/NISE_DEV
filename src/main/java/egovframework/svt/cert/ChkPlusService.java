package egovframework.svt.cert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import NiceID.Check.CPClient;

@Service
public class ChkPlusService {

	protected Log log = LogFactory.getLog(this.getClass());
	
	public String getEncriptData(HttpServletRequest request, HttpServletResponse response,String retType) {
		String reqInfo = ""; // 암호화 된 데이터
		
		// 객체 생성
		CPClient niceCheck = new CPClient();
		
		try {

			/********************************************************************************************************************************************
				NICE평가정보 Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED
				
				서비스명 : 안심본인인증서비스 (CHECKPLUSSAFE) 서비스
				페이지명 : 안심본인인증서비스 (CHECKPLUSSAFE) 호출 페이지
			*********************************************************************************************************************************************/
	
			String sSiteCode				= "BO485";			// NICE평가정보에서 발급한 CHECKPLUSSAFE 서비스 사이트코드
			String sSitePw					= "wimjtRT7xJSD";		// NICE평가정보에서 발급한 CHECKPLUSSAFE 서비스 사이트패스워em
			
			String sCPRequest				= "";
			
			// CP요청번호 생성
			sCPRequest =niceCheck.getRequestNO(sSiteCode);
			
			String sAuthType = ""; // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서
			
			String popgubun 	= "N";		//Y : 취소버튼 있음 / N : 취소버튼 없음
			
			String customize 	= "";			//없으면 기본 웹페이지 / Mobile : 모바일페이지
			
			/*
			┌ sReturnURL 변수에 대한 설명  ─────────────────────────────────────────────────────
				암호화된 인증결과 데이터를 리턴받을 URL을 프로토콜부터 풀주소로 정의해주세요.
				
				* URL은 반드시 프로토콜부터 입력해야 하며 외부에서 접속이 가능한 주소여야 합니다.
				* 당사 샘플페이지 중 ipin_process.jsp 페이지가 인증결과 데이터를 리턴받는 페이지입니다.
		
				예 - http://www.test.co.kr/ipin_process.jsp
						 https://www.test.kr:4343/ipin_process.jsp
			└────────────────────────────────────────────────────────────────────
			*/
			String sReturnURL				= "http://iedu.nise.go.kr/cert/checkplus/chkPlusResult.do?retType=" + retType;
			//String sReturnURL				= "http://iedu.nise.go.kr/I-PIN2/ipin_process.jsp";
			//String sReturnURL = "http://localhost:8080/cert/checkplus/chkPlusResult.do?retType=" + retType;
			
			/*
			┌ sCPRequest 변수에 대한 설명  ─────────────────────────────────────────────────────
				CP요청번호는 추가적인 보안처리를 위한 변수입니다. 인증 후 인증결과 데이터와 함께 전달됩니다.
				세션에 저장된 값과 비교해 데이터 위변조를 검사하거나, 사용자 특정용으로 이용할 수 있습니다.	
				위변조 검사는 인증에 필수적인 처리는 아니며 보안을 위한 권고 사항입니다.		
				
				+ CP요청번호 설정 방법
					1. 당사에서 배포된 모듈로 생성
					2. 귀사에서 임의로 정의(최대 30byte) 
			└────────────────────────────────────────────────────────────────────
			*/
			
			String sPlainData = "7:REQ_SEQ" + sCPRequest.getBytes().length + ":" + sCPRequest +
                     			"8:SITECODE" + sSiteCode.getBytes().length + ":" + sSiteCode +
                                "9:AUTH_TYPE" + sAuthType.getBytes().length + ":" + sAuthType +
                                "7:RTN_URL" + sReturnURL.getBytes().length + ":" + sReturnURL +
                                "11:POPUP_GUBUN" + popgubun.getBytes().length + ":" + popgubun +
                                "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize;
			
			javax.servlet.http.HttpSession session = request.getSession();
			
			// CP요청번호 세션에 저장 
			// 저장된 값은 ipin_result.jsp 페이지에서 데이터 위변조 검사에 이용됩니다.
			session.setAttribute("REQ_SEQ" , sCPRequest);
	
	
			// 암호화 데이터 생성
			int iRtn = niceCheck.fnEncode(sSiteCode, sSitePw, sPlainData);
	
			// 암호화 처리결과코드에 따른 처리사항
			if (iRtn == 0)
			{
				// 암호화 데이터 추출
				// 추출된 암호화된 데이터는 당사 팝업 요청시 함께 보내주셔야 합니다.
				reqInfo = niceCheck.getCipherData();	//암호화 된 데이터
			}
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
		
		String sCipherTime = "";
		String sPlainData = "";

		try {
			/********************************************************************************************************************************************
				NICE평가정보 Copyright(c) KOREA INFOMATION SERVICE INC. ALL RIGHTS RESERVED
			
				서비스명 : 안심본인인증서비스 (CHECKPLUSSAFE) 서비스
				페이지명 : 안심본인인증서비스 (CHECKPLUSSAFE) r결과페이지
			 *********************************************************************************************************************************************/
		
			String sSiteCode				= "BO485";			// NICE평가정보에서 발급한 IPIN 서비스 사이트코드
			String sSitePw					= "wimjtRT7xJSD";		// NICE평가정보에서 발급한 IPIN 서비스 사이트패스워드
	
			// 인증결과 데이터를 암호화한 데이터입니다.
			// ipin_main.jsp 페이지에서 암호화된 데이터와는 다릅니다.
			String sResponseData = requestReplace(request.getParameter("EncodeData"), "encodeData");
	    
			javax.servlet.http.HttpSession session = request.getSession();
			// ipin_main.jsp 에서 세션에 저장한 CP요청번호를 추출합니다.
			// 데이터 위변조를 확인하기 위함이며 필수사항이 아닌 보안을 위한 권고사항입니다.
			String sCPRequest = (String)session.getAttribute("CPREQUEST");
	    
			// 객체 생성
			CPClient niceCheck = new CPClient();
		
			/*
			┌ 복호화 함수 설명  ──────────────────────────────────────────────────────────
				fnResponse 함수는 인증결과 데이터를 복호화 하는 함수입니다.
				Method 결과값인 복호화 처리결과코드(iRtn)에 따라 프로세스 진행여부를 파악합니다.
			
				'sCPRequest'값을 추가로 보내시면 CP요청번호 일치여부도 확인 가능합니니다. 
				(세션에 저장한 CPREQUEST 데이터로 검증)
			
				귀사에서 원하는 방식으로 호출하시기 바랍니다.
			└────────────────────────────────────────────────────────────────────
			 */
		    if (!sResponseData.equals("") && sResponseData != null)
		    {
		    	int iRtn = niceCheck.fnDecode(sSiteCode, sSitePw, sResponseData);
		    	//int iRtn = pClient.fnResponse(sSiteCode, sSitePw, sResponseData, sCPRequest);
		    	
		    	
				if (iRtn == 0)
				{
					sPlainData = niceCheck.getPlainData();
			        sCipherTime = niceCheck.getCipherDateTime();
			        
			        HashMap mapresult = niceCheck.fnParse(sPlainData);
					
					msgChk = "Y";
					// 복호화 및 위/변조 검증
					// ---------------------------------------------------------------

					//String sVNumber				= pClient.getVNumber();			// 가상주민번호 (13 byte, 숫자 또는 문자 포함)
					//String sAgeCode				= pClient.getAgeCode();			// 연령대 코드 (개발 가이드 참조)
					//String sCPRequestNum		= pClient.getCPRequestNO();		// CP 요청번호
				
					name = (String)mapresult.get("NAME");			// 이름
					birYMD = (String)mapresult.get("BIRTHDATE");		// 생년월일 (YYYYMMDD)
					if(mapresult.get("GENDER").equals("1"))
					{
						sex = "M";
					}
					else
					{
						sex = "F";
					}
					fgnGbn = (String)mapresult.get("NATIONALINFO");	// 내/외국인 정보 (개발 가이드 참조)
					di = (String)mapresult.get("DI");			// 중복가입 확인값 (DI - 64 byte 고유값)
					ci1 = "";
					ci2 = "";
					civersion = "";
					reqNum = "";
					result = "";
					certGb = "";
					cellNo = "";
					cellCorp = "";
					certDate = "";
					addVar = "";

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
		    }
		    
			resultMap.put("msgChk", msgChk);
		} catch (Exception e) {
			log.error(e.toString());
		}
		return resultMap;				
	}
	
	public String requestReplace (String paramValue, String gubun) {
        String result = "";
        
        if (paramValue != null) {
        	
        	paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

        	paramValue = paramValue.replaceAll("\\*", "");
        	paramValue = paramValue.replaceAll("\\?", "");
        	paramValue = paramValue.replaceAll("\\[", "");
        	paramValue = paramValue.replaceAll("\\{", "");
        	paramValue = paramValue.replaceAll("\\(", "");
        	paramValue = paramValue.replaceAll("\\)", "");
        	paramValue = paramValue.replaceAll("\\^", "");
        	paramValue = paramValue.replaceAll("\\$", "");
        	paramValue = paramValue.replaceAll("'", "");
        	paramValue = paramValue.replaceAll("@", "");
        	paramValue = paramValue.replaceAll("%", "");
        	paramValue = paramValue.replaceAll(";", "");
        	paramValue = paramValue.replaceAll(":", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll("#", "");
        	paramValue = paramValue.replaceAll("--", "");
        	paramValue = paramValue.replaceAll("-", "");
        	paramValue = paramValue.replaceAll(",", "");
        	
        	if(gubun != "encodeData"){
        		paramValue = paramValue.replaceAll("\\+", "");
        		paramValue = paramValue.replaceAll("/", "");
            paramValue = paramValue.replaceAll("=", "");
        	}
        	
        	result = paramValue;
            
        }
        return result;
	}	
}
