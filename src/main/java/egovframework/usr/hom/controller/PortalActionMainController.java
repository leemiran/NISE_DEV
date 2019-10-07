package egovframework.usr.hom.controller;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;



import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.usr.hom.service.PortalActionMainService;
import oracle.sql.CLOB;
import com.tmax.tibero.jdbc.TbClob;

@Controller
public class PortalActionMainController {

	/** log */
	protected static final Log log = LogFactory.getLog(PortalActionMainController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** portalActionMainService */
	@Resource(name = "portalActionMainService")
	PortalActionMainService portalActionMainService;
	
	/** noticeAdminService */
	@Resource(name = "noticeAdminService")
	NoticeAdminService noticeAdminService;
	
	
	
	/*@RequestMapping(value="/usr/hom/portalActionMainPage.do")
	public String portalMainPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String p_d_type = commandMap.get("p_d_type") !=null ? commandMap.get("p_d_type").toString() : "T";
		
		if(commandMap.get("p_auth") != null ){
			HttpSession session = request.getSession();
			session.setAttribute("gadmin", commandMap.get("p_auth"));
		}
		
		//권한 셀렉트 박스
		List authList = portalActionMainService.selectAuthList(commandMap);
		model.addAttribute("authList", authList);
		
//		팝업리스트
		List popupList = portalActionMainService.selectNoticePopUpList(commandMap);
		//popupList.addAll(portalActionMainService.selectNoticePopUpList(commandMap));
		model.addAttribute("popupList", popupList);

//		최근공지리스트
		List topNoticeList = portalActionMainService.selectNoticeTopList(commandMap);
		model.addAttribute("topNoticeList", topNoticeList);
		
		

//		과정전체 리스트
		List trainingList = portalActionMainService.selectTrainingList(commandMap);
		model.addAttribute("trainingList", trainingList);
		
//      레이어팝업 리스트		
		commandMap.put("p_tabseq", "12");
		commandMap.put("popupListAttr", popupList);
		List view	=	noticeAdminService.selectNoticeViewAll(commandMap);
		//view.addAll(noticeAdminService.selectNoticeViewAll(commandMap));
		Iterator it = view.iterator();
		while(it.hasNext()){
			Map<String, Object> ListDataMap = (Map<String, Object>) it.next(); 
			//System.out.println(ListDataMap.get("adcontent"));
			Reader reader = null;
			if("O".equals(p_d_type)){
				CLOB clob = (CLOB)ListDataMap.get("adcontent");
				reader = clob.getCharacterStream();
			}else{
				TbClob clob = (TbClob)ListDataMap.get("adcontent");
				reader = clob.getCharacterStream();
			}
			
			StringBuffer out = new StringBuffer();
			
			char[] buff = new char[1024];
			
			int nchars=0;
			
			while((nchars=reader.read(buff)) > 0){
				out.append(buff,0,nchars);
			}
			ListDataMap.put("adcontent", out.toString());
			
		}
		model.addAttribute("view",view);
		
		model.addAllAttributes(commandMap);
		return "usr/hom/portalActionMainPage";
	}*/
	
	
	
	/**
	 * 팝업
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/usr/hom/noticePopup.do")
	public String insertPage(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		commandMap.put("p_tabseq", "12");
		
		//보기 정보
		model.addAttribute("view", noticeAdminService.selectNoticeView(commandMap));
		
		//첨부파일 리스트 정보
		List fileList = noticeAdminService.selectBoardFileList(commandMap);
		model.addAttribute("fileList", fileList);
		
		
		model.addAllAttributes(commandMap);
		return "usr/hom/noticePopup";
	}
	
}
