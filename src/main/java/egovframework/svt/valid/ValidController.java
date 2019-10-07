package egovframework.svt.valid;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ValidController {
	
	@Autowired
	ValidService validService;

	@RequestMapping(value="/valid/nicePersonalNum.do")
	public String validNicePersonalNum(ModelMap model
			//, @RequestParam String zipCode
			, @RequestParam String empGubun
			, @RequestParam String nicePersonalNum
			, @RequestParam(required = false) String deptCd
			, @RequestParam(required = false) String p_userid) throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		//paramMap.put("zipCode", zipCode);
		paramMap.put("empGubun", empGubun);
		paramMap.put("nicePersonalNum", nicePersonalNum);
		paramMap.put("deptCd", deptCd);
		paramMap.put("p_userid", p_userid);
		
		model.addAllAttributes(validService.validNicePersonalNum(paramMap));
		return "jsonView";
	}
	
	@RequestMapping(value="/valid/isLogin.do")
	public String isLogin(ModelMap model,HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
		
		//System.out.println("commandMap ---> "+commandMap);		
		//System.out.println("year ---> "+commandMap.get("year"));
		//System.out.println("subj ---> "+commandMap.get("subj"));
		//System.out.println("subjseq ---> "+commandMap.get("subjseq"));
		
		//System.out.println("p_review_study_yn ---> "+commandMap.get("p_review_study_yn"));
		
		String pReviewStudyYn =  commandMap.get("p_review_study_yn") !=null ? commandMap.get("p_review_study_yn").toString() : "N";
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(null == request.getSession().getAttribute("userid")) {
        	resultMap.put("resultCode", false);
        	resultMap.put("resultMsg", "개인정보보호를 위하여 아래의 경우\n자동 로그아웃되니 재로그인 하여 주십시오.\n(60분이상 미사용등)");
    	} else {
    		int studyLimitCnt = 1;
    		if("N".equals(pReviewStudyYn)){
    			studyLimitCnt = validService.selectStudyLimitCnt(commandMap);
    		}
    		if(studyLimitCnt == 0 ){
    			//resultMap.put("resultCode", false);
            	resultMap.put("resultMsg", " 학습이 종료되어 진도체크가 되지 않습니다.\n학습창을 닫고 나의강의실 > 복습하기에서 학습하시기 바랍니다.");
    			resultMap.put("resultCode", true);
    		}else{
    			resultMap.put("resultMsg", "1");
    			resultMap.put("resultCode", true);
    		}
    		
    	}
		model.addAllAttributes(resultMap);
		return "jsonView";
	}
}
