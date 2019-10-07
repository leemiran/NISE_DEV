package egovframework.com.snd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.sms.SMSSenderDAO;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.snd.service.SendSmsMailManageService;
import egovframework.com.utl.fcc.service.EgovStringUtil;

@Controller
public class SendSmsMailManageController {

	/** log */
	protected static final Log log = LogFactory.getLog(SendSmsMailManageController.class);
	
	/** EgovMessageSource */
	@Resource(name = "egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** sendMailManageService */
	@Resource(name = "sendSmsMailManageService")
	SendSmsMailManageService sendSmsMailManageService;
	

	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/** SMSSender */
	@Resource(name = "SMSSenderDAO")
	SMSSenderDAO SMSSenderDAO;
	
	/**
	 * 공통메일발송화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/sendCommonMailPopup.do")
	public String sendCommonMailPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String[] arrCheckId = EgovStringUtil.getStringSequence(commandMap, commandMap.get("checkId") + "");
		
		List<Object> arrUserIdList = new ArrayList<Object>();
		
		
		if(arrCheckId != null)
		{
			
			//발송대상자 가져오기
			arrUserIdList = getSendMemberList(commandMap, arrCheckId);
			
			
			//값이 있다면 가져온다.
			if(arrUserIdList != null)
			{
				commandMap.put("arrUserId", arrUserIdList);
				
				List memberList = sendSmsMailManageService.getSendMemberInfo(commandMap);
				model.addAttribute("memberList", memberList);
			}
		}
		
		
		
		
		model.addAllAttributes(commandMap);
		return "com/snd/sendCommonMailPopup";
	}
	
	/**
	 * 공통메일발송하기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/sendCommonMailAction.do")
	public String sendCommonMailAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = sendSmsMailManageService.freeMailSend(commandMap);
		if( isOk > 0 ){
			resultMsg = "정상적으로 발송되었습니다.";
		}else{
			resultMsg = "메일발송에 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAttribute("isClose", true);
		model.addAllAttributes(commandMap);
		return "forward:/com/snd/sendCommonMailPopup.do";
	}
	
	
	/**
	 * 공통문자발송화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/sendCommonSmsPopup.do")
	public String sendCommonSmsPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String[] arrCheckId = EgovStringUtil.getStringSequence(commandMap, commandMap.get("checkId") + "");
		
		List<Object> arrUserIdList = new ArrayList<Object>();
		
		
		if(arrCheckId != null)
		{
			//발송대상자 가져오기
			arrUserIdList = getSendMemberList(commandMap, arrCheckId);
			
			//값이 있다면 가져온다.
			if(arrUserIdList != null)
			{
				commandMap.put("arrUserId", arrUserIdList);
				
				List memberList = sendSmsMailManageService.getSendMemberInfo(commandMap);
				model.addAttribute("memberList", memberList);
			}
		}
		
		
		
		
		model.addAllAttributes(commandMap);
		return "com/snd/sendCommonSmsPopup";
	}
	
	/**
	 * 공통문자발송하기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/sendCommonSmsAction.do")
	public String sendCommonSmsAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		Map<String, Object> fromHp = sendSmsMailManageService.getSendFromHandPhon(commandMap);
		
		if(fromHp.containsKey("hometel") && fromHp.get("hometel") != null && !"".equals(fromHp.get("hometel"))){
			commandMap.put("fromHandPone", fromHp.get("hometel"));
		}
		
		boolean isOk = SMSSenderDAO.dacomSmsSender(commandMap);
		
		
		if( isOk ){
			resultMsg = "정상적으로 발송되었습니다.";
		}else{
			resultMsg = "문자발송에 실패하였습니다.";
		}
		
		model.addAttribute("resultMsg", resultMsg);
		model.addAttribute("isClose", true);
		model.addAllAttributes(commandMap);
		return "com/snd/sendCommonSmsPopup";
	}
	
	
	/**
	 * 발송대상자를 찾아서 리턴한다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	private List<Object> getSendMemberList(Map<String, Object> commandMap, String[] arrCheckId)
	{
		List<Object> arrUserIdList = new ArrayList<Object>();
		
		if(commandMap.get("checkId").equals("_Array_p_checks"))		//신청입과 및 학습운영에서 보낸 checkbox명
		{
			for(int i=0; i<arrCheckId.length; i++)
			{
				String v_userid = "";
				//"PRF001402,2012,0004,admin,20120419200325489000"
				StringTokenizer st      = new StringTokenizer(arrCheckId[i], ",");
				if(st.hasMoreElements())
				{
					 v_userid    	= (String)st.nextToken();
					 v_userid      	= (String)st.nextToken();
					 v_userid      	= (String)st.nextToken();
					 v_userid   	= (String)st.nextToken();	//4번째에 userid가 존재한다.
					 
					 arrUserIdList.add(v_userid);
				}
			}
		}
		else //회원검색/그외 p_userid, p_key1 에서 보내는 키
		{
			for(int i=0; i<arrCheckId.length; i++)
			{
				String v_userid = arrCheckId[i];
				 arrUserIdList.add(v_userid);
			}
		}
		
		return arrUserIdList;
	}
	
	
	/**
	 * 공통메일발송화면
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/memberGubunMailForm.do")
	public String memberGubunMailForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		//회원구분별 인원수
		Map<String, Object> memberGubunCount = sendSmsMailManageService.selectMemberGubunCount(commandMap);
		
		model.addAttribute("memberGubunCount", memberGubunCount);	
		
		model.addAllAttributes(commandMap);
		return "com/snd/memberGubunMailForm";
	}
	
	/**
	 * 공통메일발송하기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/memberGubunSendMailAction.do")
	//public ModelAndView memberGubunSendMailAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
	public String memberGubunSendMailAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{

		String resultMsg = "";
		
		
		
		String t_emp_gubun = commandMap.get("t_emp_gubun") != null ? (String)commandMap.get("t_emp_gubun") : "";
		
		System.out.println("t_emp_gubun ---> "+ t_emp_gubun);
		int isOk = 0;
		if(!"".equals(t_emp_gubun)){		
			commandMap.put("p_send_kind", "1");
			isOk = sendSmsMailManageService.insertMemberGubunSendMail(commandMap);
		}
		
		String [] arry_mailTo    = (String []) commandMap.get("_Array_p_key1") ;
				
		if(arry_mailTo!=null){
			System.out.println("arry_mailTo ----> "+arry_mailTo.length);
			if(arry_mailTo.length>0){		
				commandMap.put("p_send_kind", "2");
				int isOk1 = sendSmsMailManageService.insertMemberGubunSendMail(commandMap);
				isOk = isOk+isOk1;
			}
		}
				
		if( isOk > 0 ){
			//resultMsg = isOk+"건이 발송되었습니다.";
			resultMsg = "메일이 발송되었습니다.";
		}else{
			resultMsg = "메일발송에 실패하였습니다.";
		}
		
		
		ModelAndView modelAndView = new ModelAndView();
		 
		model.addAttribute("resultMsg", resultMsg);
		model.addAttribute("isClose", true);
		model.addAllAttributes(commandMap);
		

		return "forward:/adm/snd/memberGubunMailForm.do";
		
		/*Map resultMap = new HashMap();
        resultMap.put("resultMsg", resultMsg);        
        resultMap.put("isOk", isOk);
		modelAndView.addAllObjects(resultMap);		
		modelAndView.setViewName("jsonView");
        return modelAndView;*/
	}
	
	
	/**
	 * 공통메일발송하기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/snd/NoSendMailAction.do")
	public String NoSendMailAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		
		String resultMsg = "";
		
		int isOk = sendSmsMailManageService.updateNoMemberGubunSendMail(commandMap);
				
		
		
		ModelAndView modelAndView = new ModelAndView();
		 
		model.addAttribute("resultMsg", resultMsg);
		model.addAttribute("isClose", true);
		model.addAllAttributes(commandMap);
		return "com/snd/NoSendMailAction";
		
		/*Map resultMap = new HashMap();
        resultMap.put("resultMsg", resultMsg);        
        resultMap.put("isOk", isOk);
		modelAndView.addAllObjects(resultMap);		
		modelAndView.setViewName("jsonView");
        return modelAndView;*/
	}
	
	
	
	
	
	@RequestMapping(value="/com/snd/sendMemberSerarchPopup.do")
	public String sendMemberSerarchPopup(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		int totCnt = sendSmsMailManageService.selectSendMailListCnt(commandMap);
		
		System.out.println("totCnt ----> "+totCnt);
		
        pagingManageController.PagingManage(commandMap, model, totCnt);
		
               
        
		List<?> list = sendSmsMailManageService.selectSendMailList(commandMap);		
		model.addAttribute("list", list);	
		
		model.addAllAttributes(commandMap);
		
		return "com/snd/sendMemberSerarchPopup";
	}
	
	
	
}
