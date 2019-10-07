/**
 * 개요
 * - 로그인정책에 대한 controller 클래스를 정의한다.
 * 
 * 상세내용
 * - 로그인정책에 대한 등록, 수정, 삭제, 조회, 반영확인 기능을 제공한다.
 * - 로그인정책의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author lee.m.j
 * @version 1.0
 * @created 03-8-2009 오후 2:08:53
 */

package egovframework.adm.lgn.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springmodules.validation.commons.DefaultBeanValidator;

import egovframework.com.cmm.EgovMessageSource;
//import egovframework.com.sec.ram.security.userdetails.util.EgovUserDetailsHelper;


import egovframework.adm.lgn.service.LoginPolicyService;
import egovframework.adm.lgn.domain.LoginPolicy;
import egovframework.adm.lgn.domain.LoginPolicyVO;
import egovframework.adm.lgn.domain.LoginVO;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class LoginPolicyController {
	
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	@Resource(name="LoginPolicyService")
	LoginPolicyService LoginPolicyService;
	
    @Autowired
	private DefaultBeanValidator beanValidator;
    
	/**
	 * 로그인정책 목록 조회화면으로 이동한다.
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/selectLoginPolicyListView.do")
	public String selectLoginPolicyListView() throws Exception {
		return "/cmm/uat/uia/EgovLoginPolicyList";
	}	

	/**
	 * 로그인정책 목록을 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/selectLoginPolicyList.do")
	public String selectLoginPolicyList(@ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO, 
			                             ModelMap model) throws Exception {
		
    	/** paging */
    	PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(loginPolicyVO.getPageIndex());
	    paginationInfo.setRecordCountPerPage(loginPolicyVO.getPageUnit());
	    paginationInfo.setPageSize(loginPolicyVO.getPageSize());
		
	    loginPolicyVO.setFirstIndex(paginationInfo.getFirstRecordIndex());
	    loginPolicyVO.setLastIndex(paginationInfo.getLastRecordIndex());
	    loginPolicyVO.setRecordCountPerPage(paginationInfo.getRecordCountPerPage());
		
	    loginPolicyVO.setLoginPolicyList(LoginPolicyService.selectLoginPolicyList(loginPolicyVO));
        model.addAttribute("loginPolicyList", loginPolicyVO.getLoginPolicyList());
        
        int totCnt = LoginPolicyService.selectLoginPolicyListTotCnt(loginPolicyVO);
	    paginationInfo.setTotalRecordCount(totCnt);
        model.addAttribute("paginationInfo", paginationInfo);
        model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));

		return "/cmm/uat/uia/EgovLoginPolicyList";
	}

	/**
	 * 로그인정책 목록의 상세정보를 조회한다.
	 * @param loginPolicyVO - 로그인정책 VO
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/getLoginPolicy.do")
	public String selectLoginPolicy(@RequestParam("emplyrId") String emplyrId, 
			                        @ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO, 
                                     ModelMap model) throws Exception {
		
		loginPolicyVO.setEmplyrId(emplyrId);

		model.addAttribute("loginPolicy", LoginPolicyService.selectLoginPolicy(loginPolicyVO));
		model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
		
		LoginPolicyVO vo = (LoginPolicyVO)model.get("loginPolicy");
		
		if(vo.getRegYn().equals("N")) 
			return "/cmm/uat/uia/EgovLoginPolicyRegist";
		else 
			return "/cmm/uat/uia/EgovLoginPolicyUpdt";
	}

	/**
	 * 로그인정책 정보 등록화면으로 이동한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/addLoginPolicyView.do")
	public String insertLoginPolicyView(@RequestParam("emplyrId") String emplyrId, 
                                        @ModelAttribute("loginPolicyVO") LoginPolicyVO loginPolicyVO, 
                                         ModelMap model) throws Exception {
		
		loginPolicyVO.setEmplyrId(emplyrId);
		
		model.addAttribute("loginPolicy", LoginPolicyService.selectLoginPolicy(loginPolicyVO));
		model.addAttribute("message", egovMessageSource.getMessage("success.common.select"));
    	
		return "/cmm/uat/uia/EgovLoginPolicyRegist";
	}

	/**
	 * 로그인정책 정보를 신규로 등록한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/addLoginPolicy.do")
	public String insertLoginPolicy(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy, 
			                         BindingResult bindingResult,
                                     ModelMap model) throws Exception {
		
		beanValidator.validate(loginPolicy, bindingResult); //validation 수행		
		
    	if (bindingResult.hasErrors()) { 
    		model.addAttribute("loginPolicyVO", loginPolicy);
			return "/cmm/uat/uia/EgovLoginPolicyRegist";
		} else {
			
//			LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
//			loginPolicy.setUserId(user.getId());
			
			LoginPolicyService.insertLoginPolicy(loginPolicy);
	    	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
	    	
			return "forward:/com/uat/uia/getLoginPolicy.do";		
		}
	}                             		

	/**
	 * 기 등록된 로그인정책 정보를 수정한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/updtLoginPolicy.do")
	public String updateLoginPolicy(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy, 
			                         BindingResult bindingResult,
                                     ModelMap model) throws Exception {
		
		beanValidator.validate(loginPolicy, bindingResult); //validation 수행	
		
    	if (bindingResult.hasErrors()) { 
    		model.addAttribute("loginPolicyVO", loginPolicy);
			return "/cmm/uat/uia/EgovLoginPolicyUpdt";
		} else {
//			LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
//			loginPolicy.setUserId(user.getId());
			
			LoginPolicyService.updateLoginPolicy(loginPolicy);
	    	model.addAttribute("message", egovMessageSource.getMessage("success.common.update"));
	    	
	    	return "forward:/com/uat/uia/getLoginPolicy.do";
		}
	}

	/**
	 * 기 등록된 로그인정책 정보를 삭제한다.
	 * @param loginPolicy - 로그인정책 model
	 * @return String - 리턴 Url
	 */
	@RequestMapping("/adm/lgn/removeLoginPolicy.do")
	public String deleteLoginPolicy(@ModelAttribute("loginPolicy") LoginPolicy loginPolicy, 
                                     ModelMap model) throws Exception {

		LoginPolicyService.deleteLoginPolicy(loginPolicy);

		model.addAttribute("message", egovMessageSource.getMessage("success.common.delete"));
		return "forward:/com/uat/uia/selectLoginPolicyList.do";
	}


}
