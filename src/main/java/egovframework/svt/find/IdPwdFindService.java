package egovframework.svt.find;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import egovframework.adm.cfg.mem.service.MemberSearchService;
import egovframework.adm.sms.SMSSenderDAO;
import egovframework.com.cmm.EgovMessageSource;

@Service
public class IdPwdFindService {

	protected static final Log log = LogFactory.getLog(IdPwdFindService.class);
	
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	@Autowired
	IdPwdFindDAO idPwdFindDAO;
	
	@Resource(name = "SMSSenderDAO")
	SMSSenderDAO SMSSenderDAO;
	
	@Resource(name = "memberSearchService")
	MemberSearchService memberSearchService;
	
	public String findId(Map<String, String> paramMap, Model model) {
		Map<String, String> view = idPwdFindDAO.getIdPwdFind(paramMap);
		
		if(null != paramMap.get("epkiDn") && "" != String.valueOf(paramMap.get("epkiDn")) && null != view) {
			model.addAttribute("view", view);
		} else {
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		return "usr/mem/userIdPwdSearch02";
	}

	public String findPwd(Map<String, String> paramMap, Model model) throws Exception {
		Map<String, String> view = idPwdFindDAO.getIdPwdFind(paramMap);
		
		if(null != paramMap.get("epkiDn") && "" != String.valueOf(paramMap.get("epkiDn")) && null != view) {
			model.addAttribute("view", view);
			
			Map<String, Object> commandMap = new HashMap<String, Object>();
			commandMap.put("p_userid_1", paramMap.get("userid"));
			// p_userid_1 setting
			boolean ok = memberSearchService.updatePwdReset(commandMap);
			
			String p_handphone = String.valueOf(view.get("handphone"));
			
			String pwd  = view.get("pwd") + "";
			String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
			
			//문자발송
			if(ok == true && p_handphone != null && !p_handphone.equals("")) {
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("p_handphone", p_handphone);
				map.put("content", content);
				
				boolean isOk = SMSSenderDAO.dacomSmsSender(map);
				
				if(isOk) {
					model.addAttribute("view", view);
				} else {
					model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
					return "forward:/usr/mem/userIdPwdSearch01.do";
				}
			} else {
				model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
				return "forward:/usr/mem/userIdPwdSearch01.do";
			}
		} else {
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		return "usr/mem/userIdPwdSearch02";
	}

	public Map<String, Object> userPwdInit(Map<String, Object> commandMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		try {
			//p_userid_1
			boolean ok = memberSearchService.updatePwdReset(commandMap);
			
			String p_handphone = String.valueOf(commandMap.get("p_handphone"));
			String pwd = String.valueOf(commandMap.get("p_birthDate"));
			String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
			
			if(ok) {
				if(StringUtils.isNotBlank(p_handphone)) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("p_handphone", p_handphone);
					map.put("content", content);
					
					boolean isOk = SMSSenderDAO.dacomSmsSender(map);
					
					if(isOk) {
						resultMap.put("resultMsg", "비밀번호 초기화가 완료되었습니다.");
						resultMap.put("resultCode", "0000");
					} else {
						resultMap.put("resultMsg", "문자전송에 실패했습니다.");
						resultMap.put("resultCode", "1003");
					}
				} else {
					resultMap.put("resultMsg", "핸드폰번호가 없습니다.");
					resultMap.put("resultCode", "1002");
				}
			} else {
				resultMap.put("resultMsg", "비밀번호 초기화에 실패했습니다.");
				resultMap.put("resultCode", "1001");
			}
				
		} catch (Exception e) {
			resultMap.put("resultMsg", "비밀번호 초기화에 실패했습니다.");
			resultMap.put("resultCode", "1001");
		}
		
		return resultMap;
	}
	
	public String findIdHtml5(Map<String, String> paramMap, ModelMap model) {
		Map<String, String> view = idPwdFindDAO.getIdPwdFind(paramMap);
		
		if(null != paramMap.get("epkiDn") && "" != String.valueOf(paramMap.get("epkiDn")) && null != view) {
			model.addAttribute("view", view);
		} else {
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		return "usr/mem/userIdPwdSearch02";
	}
	
	public String findPwdHtml5(Map<String, String> paramMap, ModelMap model) throws Exception {
		
		Map<String, String> view = idPwdFindDAO.getIdPwdFind(paramMap);
		
		if(null != paramMap.get("epkiDn") && "" != String.valueOf(paramMap.get("epkiDn")) && null != view) {
			model.addAttribute("view", view);
			
			Map<String, Object> commandMap = new HashMap<String, Object>();
			commandMap.put("p_userid_1", paramMap.get("userid"));
			// p_userid_1 setting
			boolean ok = memberSearchService.updatePwdReset(commandMap);
			
			String p_handphone = String.valueOf(view.get("handphone"));
			
			String pwd  = view.get("pwd") + "";
			String content = "회원님의 비밀번호는 [" + pwd + "]입니다. 로그인하신 후 꼭 비밀번호 변경부탁드립니다.";
			
			//문자발송
			if(ok == true && p_handphone != null && !p_handphone.equals("")) {
				
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("p_handphone", p_handphone);
				map.put("content", content);
				
				boolean isOk = SMSSenderDAO.dacomSmsSender(map);
				
				if(isOk) {
					model.addAttribute("view", view);
				} else {
					model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
					return "forward:/usr/mem/userIdPwdSearch01.do";
				}
			} else {
				model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
				return "forward:/usr/mem/userIdPwdSearch01.do";
			}
		} else {
			model.addAttribute("resultMsg", egovMessageSource.getMessage("fail.common.idpwd.search"));
			return "forward:/usr/mem/userIdPwdSearch01.do";
		}
		return "usr/mem/userIdPwdSearch02";
	}
	
}
