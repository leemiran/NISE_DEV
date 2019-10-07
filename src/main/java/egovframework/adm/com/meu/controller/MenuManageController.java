package egovframework.adm.com.meu.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/* Pagination */
import egovframework.rte.fdl.property.EgovPropertyService;
/* Validator */
import org.springmodules.validation.commons.DefaultBeanValidator;
import egovframework.adm.com.meu.service.MenuManageService;
import egovframework.com.cmm.EgovMessageSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * 메뉴목록 관리및 메뉴생성, 사이트맵 생성을 처리하는 비즈니스 구현 클래스
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이  용          최초 생성
 *
 * </pre>
 */
@Controller
public class MenuManageController {

	protected Log log = LogFactory.getLog(this.getClass());
	/* Validator */
	@Autowired
	private DefaultBeanValidator beanValidator;
	/** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** MenuManageService */
	@Resource(name = "menuManageService")
    private MenuManageService menuManageService;

	/** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;	

   

    /**
     * 메뉴리스트를 조회한다. 
     * @param searchVO ComDefaultVO
     * @return 출력페이지정보 "sym/mpm/EgovMenuList" 
     * @exception Exception
     */
    @RequestMapping(value="/adm/com/meu/EgovMenuListSelect.do")
    public String selectMenuList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model ) throws Exception {
    	
    	String resultMsg    = "";
    	List list_menulist = menuManageService.selectAdmAllMenuList(commandMap);
//    	resultMsg = egovMessageSource.getMessage("success.common.select");
        
    	model.addAttribute("list_menulist", list_menulist);
//        model.addAttribute("resultMsg", resultMsg);
        model.addAllAttributes(commandMap);	
        
        return  "adm/meu/EgovMenuListSelect"; 
      	
    }
    
   
}
