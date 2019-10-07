package egovframework.svt.lifetime;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.svt.util.KisaSeedEcb;
import egovframework.svt.util.KniseServiceCall;
import egovframework.svt.util.XmlstringToMap;

@Service
public class LifetimeService {
	
	@Autowired
	LifetimeDAO lifetimeDAO;
	
	@Autowired
	KniseServiceCall kniseServiceCall;

	public Map<String, Object> putSubj(Map<String, Object> commandMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		// 이수완료한 학생조회.
		List<Map<String, Object>> completeStudentList = lifetimeDAO.getCompleteStudentList(commandMap);
		
		int updateCnt = 0;
		int successCnt = 0;	//정상
		int failCnt = 0;	//시스템 오류
		int noneMember = 0;	//회원이 아닌 경우
		int noneSubj = 0;	//학습과정코드가 없는 경우
		int invalidClassId = 0;  //코드 오류
		
		// for
		for(Map<String, Object> completeStudent: completeStudentList) {
			String kniseId = String.valueOf(completeStudent.get("userid"));
			String eduTp = String.valueOf(completeStudent.get("upperclassNm"));
			String eduRelCd = String.valueOf(completeStudent.get("subj"));
			String p_year = String.valueOf(completeStudent.get("year"));
			String p_subjseq = String.valueOf(completeStudent.get("subjseq"));
			String eduNm = String.valueOf(completeStudent.get("subjnm"));
			String orgNm = String.valueOf(completeStudent.get("orgNm"));
			String eduStrtYmd = String.valueOf(completeStudent.get("edustart"));
			String eduEndYmd = String.valueOf(completeStudent.get("eduend"));
			String eduTm = String.valueOf(completeStudent.get("edutimesMin"));
			String passNo = String.valueOf(completeStudent.get("serno"));
			
			String retrunVal = kniseServiceCall.sendOrgClassComplete(kniseId
					, eduTp
					, eduRelCd
					, eduNm
					, orgNm
					, eduStrtYmd
					, eduEndYmd
					, eduTm
					, passNo
					,p_year
					,p_subjseq);			// 학습자 과정 이력 등록
			
			System.out.println("retrunVal ---> "+retrunVal);
			
			Map<String, String> returnMap = XmlstringToMap.convert(retrunVal);
			
			System.out.println("retrunVal ---> "+returnMap);
			
						
			
			commandMap.put("userid", completeStudent.get("userid"));
			commandMap.put("lifetimeResult", returnMap.get("resultCode"));
			
			if("0000".equals(returnMap.get("resultCode"))) {
				commandMap.put("lifetimeYn", "Y");
				successCnt ++;
			} else if("1001".equals(returnMap.get("resultCode"))) {
				commandMap.put("lifetimeYn", "N");
				noneSubj ++;
			} else if("1002".equals(returnMap.get("resultCode"))) {
				commandMap.put("lifetimeYn", "N");
				noneMember ++;
			} else if("1003".equals(returnMap.get("resultCode"))) {
				commandMap.put("lifetimeYn", "N");
				invalidClassId ++;
			} else {
				commandMap.put("lifetimeYn", "N");
				failCnt ++;
			}
			
			// update
			updateCnt += lifetimeDAO.updateLifetimeSubj(commandMap);
		}
		// for end
		resultMap.put("updateCnt", updateCnt);
		resultMap.put("successCnt", successCnt);
		resultMap.put("failCnt", failCnt);
		resultMap.put("noneMember", noneMember);
		return resultMap;
	}

	public Map<String, Object> join(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			// 평생계좌제 연동 여부
			String joinYn = kniseServiceCall.isMemberClass(String.valueOf(request.getSession().getAttribute("userid")));
			
			if("Y".equals(joinYn)) {
				resultMap.put("resultCode", "SUCCESS");
				resultMap.put("resultMsg", "이미 연동중입니다.");
				resultMap.put("joinYn", "Y");
			} else if("N".equals(joinYn)) {
				resultMap.put("resultCode", "SUCCESS");
				resultMap.put("joinYn", "N");
				resultMap.put("kniseId", KisaSeedEcb.SEED_ECB_Encrypt(String.valueOf(request.getSession().getAttribute("userid"))));
				resultMap.put("userNm", KisaSeedEcb.SEED_ECB_Encrypt(URLEncoder.encode(String.valueOf(request.getSession().getAttribute("name")), "UTF-8")));
				resultMap.put("birthday", KisaSeedEcb.SEED_ECB_Encrypt(String.valueOf(request.getSession().getAttribute("birthDate"))));
				resultMap.put("linkOrg", KisaSeedEcb.SEED_ECB_Encrypt("0013"));
			} else {
				resultMap.put("resultCode", "ERROR");
				resultMap.put("resultMsg", "평생계좌제 연동 오류");
				return resultMap;
			}
		} catch (Exception e) {
			resultMap.put("resultCode", "ERROR");
			resultMap.put("resultMsg", "시스템 오류, 관리자에게 문의해주세요.");
		}
		return resultMap;
	}
		
	public List selectSubjInfoDetailLifetime(Map<String, Object> commandMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();		
		//세부학습 과정
		List<Map<String, Object>> list = lifetimeDAO.selectSubjInfoDetailLifetime(commandMap);
		return list;
	}

}
