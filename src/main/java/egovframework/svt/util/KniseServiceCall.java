package egovframework.svt.util;

import java.net.URLEncoder;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.springframework.stereotype.Service;

@Service
public class KniseServiceCall {
	
	// 연계서버 정보
	private static final String _NILE_KNISE_URL = "http://203.235.44.99:8080/services/KniseService";	// 운영
	private static final String _NILE_NAMESPACE = "http://service.knise.nile.org";
	
	//sendOrgClassComplete
	public String sendOrgClassComplete(String tmp_userId
			, String eduTp
			, String eduRelCd
			, String eduNm
			, String orgNm
			, String eduStrtYmd
			, String eduEndYmd
			, String eduTm
			, String passNo
			,String p_year
			,String p_subjseq
			) throws Exception {
		String rtnVal = "";
		try {
			
			String courseCode =p_year+"_"+eduRelCd+"_"+p_subjseq; 
			String userId = KisaSeedEcb.SEED_ECB_Encrypt(tmp_userId);
			
			System.out.println("courseCode ---> "+courseCode);
			System.out.println("userId ---> "+userId);
			
			/*StringBuffer data = new StringBuffer()
				.append("<message>")
				.append("<eduContents>")
				.append("<eduTp><![CDATA["+URLEncoder.encode(eduTp, "UTF-8")+"]]></eduTp>")		//연수 종류
				.append("<eduRelCd><![CDATA[" +URLEncoder.encode(eduRelCd, "UTF-8")+ "]]></eduRelCd>")	//교육 코드
				.append("<eduNm><![CDATA["+URLEncoder.encode(eduNm, "UTF-8")+"]]></eduNm>")		//교육명
				.append("<orgNm><![CDATA["+URLEncoder.encode(orgNm, "UTF-8")+"]]></orgNm>") 	//기관명
				.append("<eduStrtYmd>"+eduStrtYmd+"</eduStrtYmd>")		// 교육 시작 일자
				.append("<eduEndYmd>"+eduEndYmd+"</eduEndYmd>")			// 교육 종료 일자
				.append("<eduTm>"+eduTm+"</eduTm>")						// 해당과정의 총 이수 시간
				.append("<passNo><![CDATA[" +URLEncoder.encode(passNo, "UTF-8")+ "]]></passNo>")						// 해당과정의 총 이수 시간
				.append("</eduContents>")
				.append("</message>");*/
			
			//20180515
			StringBuffer data = new StringBuffer()
				.append("<message>")
				.append("<list>")
				.append("<kniseId><![CDATA[" +URLEncoder.encode(userId, "UTF-8")+ "]]></kniseId>")		//국립특수교육원 아이디
				.append("<sublist>")
				.append("<eduRelCd>"+courseCode+"</eduRelCd>")
				.append("</sublist>")				
				.append("</list>")
				.append("</message>");			
			
			System.out.println("data ----> "+data);
			
			//create new service
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();

			//create new call object
			Call call = (Call) service.createCall();
			
			//setup end point url
			call.setTargetEndpointAddress(new java.net.URL(_NILE_KNISE_URL));
			
			//setup operation name
			call.setOperationName(new QName(_NILE_NAMESPACE, "sendOrgClassComplete"));		// sendOrgClassComplete 인터페이스 호출
				
			
			//setup parameter
			call.addParameter("kniseId", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.addParameter("data", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			
			//setup return type
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);		
			
			//invoke
			rtnVal = (String)call.invoke(new Object[] {userId, data.toString()});
			
			System.out.println("rtnVal ---> "+rtnVal);
						
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rtnVal.trim();
	}
	
	
	//sendOrgClassComplete
	public String isMemberClass(String tmp_userId) throws Exception {
		String rtnVal = "";
		try {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>req: " + tmp_userId);
			String userId = KisaSeedEcb.SEED_ECB_Encrypt(tmp_userId);
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>reqEnc: " + userId);
			
			//create new service
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();

			//create new call object
			Call call = (Call) service.createCall();
			
			//setup end point url
			call.setTargetEndpointAddress(new java.net.URL(_NILE_KNISE_URL));
			
			//setup operation name
			call.setOperationName(new QName(_NILE_NAMESPACE, "isMemberClass"));		// isMemberClass 인터페이스 호출
				
			
			//setup parameter
			call.addParameter("kniseId", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			
			//setup return type
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			
			//invoke
			rtnVal = (String)call.invoke(new Object[] {userId});
						
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rtnVal.trim();
	}
	
	// isCompleteOrgClass
	public String isCompleteOrgClass(String tmpUserId
			, String tmpEduRelCd) throws Exception {
		String rtnVal = "";
		try {
			
			String userId = KisaSeedEcb.SEED_ECB_Encrypt(tmpUserId);			
			String eduRelCd = KisaSeedEcb.SEED_ECB_Encrypt(tmpEduRelCd);
			
			
			//create new service
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();

			//create new call object
			Call call = (Call) service.createCall();
			
			//setup end point url
			call.setTargetEndpointAddress(new java.net.URL(_NILE_KNISE_URL));
			
			//setup operation name
			call.setOperationName(new QName(_NILE_NAMESPACE, "isCompleteOrgClass"));		// isCompleteOrgClass 인터페이스 호출
			
			
			//setup parameter
			call.addParameter("kniseId", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			call.addParameter("eduRelCd", org.apache.axis.Constants.XSD_STRING, javax.xml.rpc.ParameterMode.IN);
			
			//setup return type
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);		
			
			//invoke
			rtnVal = (String)call.invoke(new Object[] {userId, eduRelCd});
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return rtnVal.trim();
	}
	
	// seed encript user info , 회원가입시 필요 파라미터 참고
	public void seedUserInfoPrint() throws Exception {
		
		System.out.println("LinkOrg : " + KisaSeedEcb.SEED_ECB_Encrypt("0013"));
		System.out.println("kniseId : " + KisaSeedEcb.SEED_ECB_Encrypt("kniseuser"));
		System.out.println("userNm : " + KisaSeedEcb.SEED_ECB_Encrypt(URLEncoder.encode("이름","UTF-8")));		
		System.out.println("birthday : " + KisaSeedEcb.SEED_ECB_Encrypt("20170101"));
		
	}
	
}