package egovframework.svt.adm.prop.auto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;

@Service
public class AdminAutoMemberService {

	@Autowired
	AdminAutoMemberDAO adminAutoMemberDAO;
	
	@Autowired
	EgovMessageSource egovMessageSource;
	
	public Map<String, Object> insertAutoMember(List<Map<String, String>> autoMemberList, Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String errorMsg = "";
		int idx = 0;
		for(Map<String, String> autoMember: autoMemberList) {
			autoMember.put("excelEmpGubun", autoMember.get("empGubun"));
			if("1".equals(autoMember.get("empGubun"))) {
				autoMember.put("empGubun", "T");
				
				if(!Pattern.matches("^(A00|([B-K]|[MN]|[P-T])10)[1-9][0-9]{6}$", autoMember.get("nicePersonalNum"))) {
					errorMsg += "나이스개인번호를 다시 확인해주세요.\n";
				}
			} else if("2".equals(autoMember.get("empGubun"))) {
				autoMember.put("empGubun", "E");
				
				if(!isThisDateValid(autoMember.get("birthDate"))) {
					errorMsg += "생년월일을 다시 확인해주세요.\n";
				}
			} else {
				errorMsg += "교원, 보조인력 구분(1,2)을 다시 확인해주세요.\n";
			}
			if(!Pattern.matches("^[가-힣]{2,15}$", autoMember.get("name"))) {
				errorMsg += "이름(한글)을 다시 확인해주세요.\n";
			}
			if(!"".equals(errorMsg)) break;
			idx ++;
		}
		
		// 중복리스트 제거
		Set<Map<String, String>> duplicateList = new LinkedHashSet<Map<String, String>>(autoMemberList);
		autoMemberList.clear();
		autoMemberList.addAll(duplicateList);
		
		if(!"".equals(errorMsg)) {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.insert"));
			resultMap.put("errorMember", autoMemberList.get(idx));
			resultMap.put("errorIdx", idx);
			resultMap.put("errorMsg", errorMsg);
		} else {
			adminAutoMemberDAO.deleteAutoMember(commandMap);
			commandMap.put("autoMemberList", autoMemberList);
			
			//insert all이 안되서 
			for(Map<String, String> autoMember: autoMemberList) {
				autoMember.put("excelEmpGubun", autoMember.get("empGubun"));
				if("1".equals(autoMember.get("empGubun"))) {
					autoMember.put("empGubun", "T");
					
					if(!Pattern.matches("^(A00|([B-K]|[MN]|[P-T])10)[1-9][0-9]{6}$", autoMember.get("nicePersonalNum"))) {
						errorMsg += "나이스개인번호를 다시 확인해주세요.\n";
					}
				} else if("2".equals(autoMember.get("empGubun"))) {
					autoMember.put("empGubun", "E");
					
					if(!isThisDateValid(autoMember.get("birthDate"))) {
						errorMsg += "생년월일을 다시 확인해주세요.\n";
					}
				} else {
					errorMsg += "교원, 보조인력 구분(1,2)을 다시 확인해주세요.\n";
				}
				if(!Pattern.matches("^[가-힣]{2,15}$", autoMember.get("name"))) {
					errorMsg += "이름(한글)을 다시 확인해주세요.\n";
				}
				
				commandMap.put("nicePersonalNum", autoMember.get("nicePersonalNum"));
				commandMap.put("birthDate", autoMember.get("birthDate"));
				commandMap.put("empGubun", autoMember.get("empGubun"));
				commandMap.put("name", autoMember.get("name"));
				adminAutoMemberDAO.insertAutoMember(commandMap);
			}
			
			//adminAutoMemberDAO.insertAutoMember(commandMap);
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.insert"));
		}
		
		return resultMap;
	}

	public boolean isThisDateValid(String dateToValidate){
		if(dateToValidate == null){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		sdf.setLenient(false);

		try {
			Date date = sdf.parse(dateToValidate);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	public List<?> autoMemberList(Map<String, Object> commandMap) {
		return adminAutoMemberDAO.getAutoMemberList(commandMap);
	}
}
