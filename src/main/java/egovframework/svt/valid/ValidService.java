package egovframework.svt.valid;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.adm.cou.service.GrSeqService;
import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.svt.util.AreaUtil;

@Service
public class ValidService {

	@Autowired
    private GrSeqService grSeqService;
	
	@Autowired
	private CommonAjaxManageService commonAjaxManageService;
	
	@Autowired
	ValidDAO validDAO;

	public Map<String, String> validNicePersonalNum(Map<String, String> paramMap) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		//String zipCode = paramMap.get("zipCode");
		String empGubun = paramMap.get("empGubun");
		String nicePersonalNum = paramMap.get("nicePersonalNum");
		String deptCd = paramMap.get("deptCd");
		
		// 나이스개인번호 유효성
		if(!Pattern.matches("^(A00|([B-K]|[MN]|[P-T])10)[1-9][0-9]{6}$", nicePersonalNum)) {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", "나이스개인번호 구성체계가 다릅니다.");
			return resultMap;
		}
		
		// 신분구분
		String jobGubun = nicePersonalNum.substring(3, 4);
		if("T".equals(empGubun) && (!"1".equals(jobGubun) && !"9".equals(jobGubun))		//20180808 교원T 신분구분 9추가
				|| "O".equals(empGubun) && !"6".equals(jobGubun)) {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", "선택하신 회원구분과 나이스개인번호의 신분구분이 다릅니다.");
			return resultMap;
		}
		
		// 시도교육청(교원일때)
		String niceAreaCode = nicePersonalNum.substring(0, 3);
		if("T".equals(empGubun)) {
			if(!"A00".equals(niceAreaCode)) {
				Map<String, Object> daoParamMap = new HashMap<String, Object>();
				daoParamMap.put("niceAreaCode", niceAreaCode);
				daoParamMap.put("deptCd", deptCd);
				
				if(1 > validDAO.getAreaCode(daoParamMap)) {
					resultMap.put("resultCode", "0");
					resultMap.put("resultMsg", "시도교육청과 나이스개인번호의 시도구분이 다릅니다.");
					return resultMap;
				}
			}
		}
		
		// 우편번호
//		String zipCodeToAreaCode = zipCodeToAreaCode(zipCode);
//		if(!niceAreaCode.equals(zipCodeToAreaCode)) {
//			resultMap.put("resultCode", "0");
//			resultMap.put("resultMsg", "우편번호와 나이스개인번호의 시도구분이 다릅니다.");
//			return resultMap;
//		}
		
		// 중복
		Map<String, Object> commandMap = new HashMap<String, Object>();
		commandMap.put("p_nicePersonalNum", nicePersonalNum);
		commandMap.put("p_userid", paramMap.get("p_userid"));
		if(commonAjaxManageService.nicePersonalNumOverlap(commandMap, "commonAjaxDAO.nicePersonalNumOverlap")) {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", "이미 사용중인 나이스개인번호 입니다.");
			return resultMap;
		}
		
		resultMap.put("resultCode", "1");
		resultMap.put("resultMsg", "사용 가능한 나이스개인번호 입니다.");
		return resultMap;
	}

	public String validSugangRequest(Map<String, Object> commandMap) throws Exception {
		// 명단이 있는 수강인지 체크
		// 명단이 있는 수강
		if(0 < validDAO.getAutoMemberCnt(commandMap)) {
			// 회원정보 가져오기
			Map<String, String> userInfo = validDAO.getUserInfo(String.valueOf(commandMap.get("userid")));
			userInfo.put("subj", String.valueOf(commandMap.get("p_subj")));
			userInfo.put("year", String.valueOf(commandMap.get("p_year")));
			userInfo.put("subjseq", String.valueOf(commandMap.get("p_subjseq")));
			
			// 신청자가 명단에 있을 경우
			if(0 < validDAO.getUserInAutoMember(userInfo)) {
				return validUserAreaSubjseq(userInfo);
			// 신청자가 명단에 없을 경우
			} else {
				return "본 과정은 특별연수로 지정대상자가 아닙니다.";
			}
		// 명단이 없는 수강
		} else {
			Map<String, String> userInfo = validDAO.getUserInfo(String.valueOf(commandMap.get("userid")));
			userInfo.put("subj", String.valueOf(commandMap.get("p_subj")));
			userInfo.put("year", String.valueOf(commandMap.get("p_year")));
			userInfo.put("subjseq", String.valueOf(commandMap.get("p_subjseq")));
			
			// 지역 체크
			return validUserAreaSubjseq(userInfo);
		}
		
	}
	
	public String validUserAreaSubjseq(Map<String, String> userInfo) throws Exception {
		// 지역 체크
		// 과정 지역 정보
		List<Map<String, String>> subjseqAreaList = validDAO.getSubjseqAreaList(userInfo);
		
		boolean isAllArea = false;
		for(Map<String, String> subjseqArea: subjseqAreaList) {
			if("A00".equals(subjseqArea.get("areaCode"))) {
				isAllArea = true;
				break;
			}
		}
		
		// 본인 지역 가져오기
		String userArea = validUserArea(userInfo);
		if(3 != userArea.length()) {
			return userArea;
		}
		
		if(!isAllArea) {
			boolean isRequest = false;
			for(Map<String, String> subjseqArea: subjseqAreaList) {
				if(userArea.equals(subjseqArea.get("areaCode"))) {
					isRequest = true;
					break;
				}
			}
			if(!isRequest) {
				return "지역이 일치하지 않아 수강신청이 불가능합니다.";
			}
		}
		
		return "";
	}
	
	public String zipCodeToAreaCode(String zipCode) throws Exception {
		String areaCode = null;
		if(zipCode.length() > 2) {
			zipCode = zipCode.substring(0, 2);
		}
		
		List<Map<String, String>> areaCodeList = (List<Map<String, String>>) grSeqService.selectAreaCodeList();
		
		int listIdx = 0;
		for(String[] arrZipCode: AreaUtil.ZIP_CODE_ARR) {
			if(Arrays.asList(arrZipCode).contains(zipCode)) {
				areaCode = areaCodeList.get(listIdx).get("code");
				break;
			}
			listIdx ++;
		}
		return areaCode;
	}
	
	public String validUserArea(String userid) throws Exception {
		Map<String, String> userInfo = validDAO.getUserInfo(userid);
		return validUserArea(userInfo);
	}
	
	public String validUserArea(Map<String, String> userInfo) throws Exception {
		// 교원
		if("T".equals(userInfo.get("empGubun"))) {
			// 교원 - 사립유치원
			if("A00".equals(userInfo.get("niceAreaCode"))) {
				if(null != userInfo.get("zipCode") && 5 == userInfo.get("zipCode").length()) {
					// 우편번호 코드화
					String zipCodeToAreaCode = zipCodeToAreaCode(userInfo.get("zipAreaCode"));
					return zipCodeToAreaCode;
				} else {
					return "도로명 우편번호를 사용해주세요.";
				}
			} else if("00039".equals(userInfo.get("jobCd"))) {
				if(null != userInfo.get("deptAreaCode") && 3 == userInfo.get("deptAreaCode").length()) {
					return userInfo.get("deptAreaCode");
				} else {
					return "시도교육청 정보가 없습니다.";
				}
			} else {
				if(null != userInfo.get("niceAreaCode") && 3 == userInfo.get("niceAreaCode").length()) {
					return userInfo.get("niceAreaCode");
				} else {
					return "나이스개인번호가 없습니다.";
				}
				/*
				if(null != userInfo.get("niceAreaCode") && userInfo.get("niceAreaCode").equals(userInfo.get("deptAreaCode"))) {
					return userInfo.get("niceAreaCode");
				} else {
					return "시도교육청과 나이스개인번호의 시도구분이 다릅니다.";
				}
				*/
			}
		// 교육전문직
		} else if("R".equals(userInfo.get("empGubun"))) {
			
			System.out.println("deptCd ----> "+ userInfo.get("deptCd"));
			
			if("1342000".equals(userInfo.get("deptCd"))) {	//교육부
				if(null != userInfo.get("zipCode") && 5 == userInfo.get("zipCode").length()) {
					// 우편번호 코드화
					String zipCodeToAreaCode = zipCodeToAreaCode(userInfo.get("zipAreaCode"));
					return zipCodeToAreaCode;
				} else {
					return "도로명 우편번호를 사용해주세요.";
				}
			}else{
				if(null != userInfo.get("deptAreaCode") && 3 == userInfo.get("deptAreaCode").length()) {
					return userInfo.get("deptAreaCode");
				} else {
					return "시도교육청 정보가 없습니다.";
				}
			}
		// 보조인력
		} else if("E".equals(userInfo.get("empGubun"))) {
			if(null != userInfo.get("deptAreaCode") && 3 == userInfo.get("deptAreaCode").length()) {
				return userInfo.get("deptAreaCode");
			} else {
				return "시도교육청 정보가 없습니다.";
			}
		// 일반회원, 공무원
		} else {
			if(null != userInfo.get("zipCode") && 5 == userInfo.get("zipCode").length()) {
				// 우편번호 코드화
				String zipCodeToAreaCode = zipCodeToAreaCode(userInfo.get("zipAreaCode"));
				return zipCodeToAreaCode;
			} else {
				return "도로명 우편번호를 사용해주세요.";
			}
		}
	}
	
	public int selectStudyLimitCnt (Map<String, Object> commandMap) throws Exception {
		int studyLimitCnt = validDAO.selectStudyLimitCnt(commandMap);
		return studyLimitCnt;
	}

}
