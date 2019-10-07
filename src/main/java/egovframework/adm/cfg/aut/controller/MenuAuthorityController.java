package egovframework.adm.cfg.aut.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.adm.cfg.aut.service.MenuAuthorityService;
import egovframework.com.cmm.EgovMessageSource;

@Controller
public class MenuAuthorityController {

	/** log */
	protected static final Log log = LogFactory.getLog(MenuAuthorityController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** menuAuthorityService */
	@Resource(name = "menuAuthorityService")
	MenuAuthorityService menuAuthorityService;
	
	/**
	 * 메뉴권한 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/aut/menuAuthUpdatePage.do")
	public String menuAuthUpdatePage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		
		List gadminList = menuAuthorityService.selectListGadmin(commandMap);
		model.addAttribute("gadminList", gadminList);
		
		commandMap.put("sqlString", this.getAuthQuery(gadminList));
		
		List list = menuAuthorityService.selectMenuAuthList(commandMap);
		model.addAttribute("list", list);
		
		model.addAllAttributes(commandMap);
		return "adm/cfg/aut/menuAuthUpdatePage";
	}
	
	/**
	 * 메뉴권한정보 수정
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/cfg/aut/menuAuthorityUpdate.do")
	public String menuAuthUpdate(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		String resultMsg = "";
		
		int isOk = menuAuthorityService.menuAuthUpdate(commandMap);
		if( isOk > 0 ){
			resultMsg = egovMessageSource.getMessage("success.common.update");
		}else{
			resultMsg = egovMessageSource.getMessage("fail.common.update");
		}
		model.addAttribute("resultMsg", resultMsg);
		model.addAllAttributes(commandMap);
		return "forward:/adm/cfg/aut/menuAuthUpdatePage.do";
	}
	
	
	
	
	
	/**
	 * 권한 종류 관련 쿼리추가
	 * @param ls
	 * @return
	 * @throws Exception
	 */
	public String getAuthQuery(List ls) throws Exception{
        String sql1 = "";
        String sql2 = "";
        int listCount = ls.size();
        for( int i=0; i<listCount; i++ ){
        	Map m = (Map)ls.get(i);
        	String gadmin = (String)m.get("gadmin");
        	if( i == 0 ){// 처음 쿼리 연결을 위하여
        		sql1 += ", '";
        	}else{// 문자간 결합시 사이에 '/' 추가
//        		sql1 += " || '/' || ";
        	}
        	sql2 += ", ";
        	// 각각의 권한별 운영자 권한 ID colnum
        	sql1 += ""+gadmin+"/";
        	// 각각의 권한별 권한값(읽기/쓰기) 컬럼 (row = >  colum 으로 변환)
            // 참고 : 해당값에 위치 지정을 위한 구분값 (맨앞숫자 한자리) 추가해줌 (  i  + 권한값(읽기/쓰기)  )
        	sql2 += " max(decode(gadmin, '"+gadmin+"', control, 'NA')) as control"+i;
        	if( (i+1) == listCount ){// 마지막 로우일 경우
        		sql1 = sql1.substring(0, sql1.length()-1);
        		sql1 += "' as gadmin ";
        	}
        }
		return sql1 + sql2;
	}
}
