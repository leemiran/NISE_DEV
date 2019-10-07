package egovframework.adm.rsh.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ziaan.research.SulmunExampleData;
import com.ziaan.research.SulmunQuestionExampleData;
import com.ziaan.research.SulmunSubjBean;
import com.ziaan.research.SulmunSubjectAnswerData;



import egovframework.adm.rsh.service.SulmunQuestService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;

@Controller
public class SulmunQuestController {
	/** log */
	protected Log log = LogFactory.getLog(this.getClass());

	/** EgovPropertyService */
	@Resource(name = "propertiesService")
	protected EgovPropertyService propertiesService;
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;
	
	/** PagingManageController */
	@Resource(name = "pagingManageController")
	private PagingManageController pagingManageController;
	
	/** sulmunQuestService */
	@Resource(name = "sulmunQuestService")
    private SulmunQuestService sulmunQuestService;
	
	
	/**
	 * 설문 문제 관리 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulmunAllQuestList.do")
	public String sulmunAllQuestList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = sulmunQuestService.sulmunAllQuestList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/sulmunAllQuestList";
	}

	/**
	 * 설문 문제 관리 등록/수정 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulmunAllQuestView.do")
	public String sulmunAllQuestView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
//		설문문제
		model.addAttribute("view", sulmunQuestService.selectTzSulmunView(commandMap));
		
//		설문문제 보기 리스트
		List<?> list = sulmunQuestService.selectTzSulmunSelectList(commandMap);
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/sulmunAllQuestView";
	}
	
	
	/**
	 * 설문 척도 등록/수정/삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulmunAllQuestAction.do")
	public String sulmunAllQuestAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/rsh/sulmunAllQuestList.do";
		String p_process = commandMap.get("p_process") + "";
		
		boolean isok = false;
		
		if(p_process.equals("insert"))
		{
			isok = sulmunQuestService.insertTzSulmun(commandMap);
		}
		else if(p_process.equals("update"))
		{
			isok = sulmunQuestService.updateTzSulmun(commandMap);
		}
		else if(p_process.equals("delete"))
		{
			isok = sulmunQuestService.deleteTzSulmun(commandMap);
		}
		
		
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
		}else{
//			사용되어진 문제가 있다면
			if(p_process.equals("delete"))
				resultMsg = egovMessageSource.getMessage("fail.common.status.count." + p_process);
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	
	
	/**
	 * 설문 척도 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/scaleList.do")
	public String selectScaleList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = sulmunQuestService.selectScaleList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/scaleList";
	}
	
	
	/**
	 * 설문 척도 보기 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/scaleView.do")
	public String selectScaleView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = sulmunQuestService.selectScaleView(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/scaleView";
	}

	 /**
     * 설문 척도 보기 리스트 - ajax
     * 
     * @param HttpServletResponse
     *            response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */

    @RequestMapping(value = "/adm/rsh/scaleViewAjax.do") 
    public ModelAndView scaleViewAjax( HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap) throws Exception {
        Object result = null;
        ModelAndView modelAndView = new ModelAndView();
        
        List<?> list = sulmunQuestService.selectScaleView(commandMap);

        Map resultMap = new HashMap();
        resultMap.put("result", list);
        modelAndView.addAllObjects(resultMap);
        modelAndView.setViewName("jsonView");
        return modelAndView;
    }
    
    
	/**
	 * 설문 척도 등록/수정/삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/scaleAction.do")
	public String selectScaleAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/rsh/scaleList.do";
		String p_process = commandMap.get("p_process") + "";
		
		boolean isok = false;
		
		if(p_process.equals("insert"))
		{
			isok = sulmunQuestService.insertScale(commandMap);
		}
		else if(p_process.equals("update"))
		{
			isok = sulmunQuestService.updateScale(commandMap);
		}
		else if(p_process.equals("delete"))
		{
			isok = sulmunQuestService.deleteScale(commandMap);
		}
		
		
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
		}else{
//			사용되어진 문제가 있다면
			if(p_process.equals("delete"))
				resultMsg = egovMessageSource.getMessage("fail.common.status.count." + p_process);
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 설문지 리스트
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulPaperAllQuestList.do")
	public String sulPaperAllQuestList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = sulmunQuestService.selectSulPaperList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/sulPaperAllQuestList";
	}
	
	
	
	/**
	 * 설문지 등록/수정 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulPaperAllQuestView.do")
	public String sulPaperAllQuestView(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		String p_sulpapernum = commandMap.get("p_sulpapernum") + "";
		
		
//		전체문제리스트
		List<?> list = sulmunQuestService.selectSulPaperAllQuestList(commandMap);
		model.addAttribute("list", list);
		
//		등록된 문제정보
		if(p_sulpapernum != null && !p_sulpapernum.equals(""))
		{
			Map view = (Map) sulmunQuestService.selectSulPaperList(commandMap).get(0);
			model.addAttribute("view", view);
			
			if(view != null)
			{
				//등록된 설문번호
				String getSulnums = view.get("sulnums") + "";
				
//				문항이 등록된 순서대로 정렬하기
				String p_getSulnumsOderBy = "";
				StringTokenizer st = new StringTokenizer(getSulnums, SulmunSubjBean.SPLIT_COMMA);
				int i = 0;
				while ( st.hasMoreElements() ) {
					i++;
					String v_sulnum = (String)st.nextToken();
					
					if(i > 1) p_getSulnumsOderBy += SulmunSubjBean.SPLIT_COMMA;
					
					p_getSulnumsOderBy +=  v_sulnum + SulmunSubjBean.SPLIT_COMMA + i;
				}
				commandMap.put("p_getSulnums", getSulnums);
				commandMap.put("p_getSulnumsOrderBy", p_getSulnumsOderBy);

				
				
//				등록된문제리스트
				List<?> sulList = sulmunQuestService.selectSulPaperAllQuestList(commandMap);
				model.addAttribute("sulList", sulList);
				
				commandMap.remove("p_getSulnums");
				commandMap.remove("p_getSulnumsOrderBy");
			}
		}
		
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/sulPaperAllQuestView";
	}
	
	
	/**
	 * 설문지 등록/수정 페이지
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulPaperPreviewList.do")
	public String sulPaperPreviewList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		String p_sulpapernum = commandMap.get("p_sulpapernum") + "";
		
		
//		등록된 문제정보
		if(p_sulpapernum != null && !p_sulpapernum.equals(""))
		{
			//설문지 정보
			Map view = (Map) sulmunQuestService.selectSulPaperList(commandMap).get(0);
			model.addAttribute("view", view);
			
			if(view != null)
			{
				//등록된 설문번호
				String getSulnums = view.get("sulnums") + "";
				
//				문항이 등록된 순서대로 정렬하기
				String p_getSulnumsOderBy = "";
				StringTokenizer st = new StringTokenizer(getSulnums, SulmunSubjBean.SPLIT_COMMA);
				int i = 0;
				while ( st.hasMoreElements() ) {
					i++;
					String v_sulnum = (String)st.nextToken();
					
					if(i > 1) p_getSulnumsOderBy += SulmunSubjBean.SPLIT_COMMA;
					
					p_getSulnumsOderBy +=  v_sulnum + SulmunSubjBean.SPLIT_COMMA + i;
				}
				commandMap.put("p_getSulnums", getSulnums);
				commandMap.put("p_getSulnumsOrderBy", p_getSulnumsOderBy);

				
				
//				등록된문제리스트
				List<?> sulList = sulmunQuestService.selectSulPaperPreviewList(commandMap);
				model.addAttribute("sulList", sulList);
				
				commandMap.remove("p_getSulnums");
				commandMap.remove("p_getSulnumsOrderBy");
			}
		}
		
		model.addAllAttributes(commandMap);
		
		return "adm/rsh/sulPaperPreviewList";
	}
	
	
	
	
    
    
	/**
	 * 설문 척도 등록/수정/삭제
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulPaperAllQuestAction.do")
	public String sulPaperAllQuestAction(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String resultMsg = "";
		String url = "/adm/rsh/sulPaperAllQuestList.do";
		String p_process = commandMap.get("p_process") + "";
		
		boolean isok = false;
		
		if(p_process.equals("insert"))
		{
			isok = sulmunQuestService.insertSulPaper(commandMap);
		}
		else if(p_process.equals("update"))
		{
			isok = sulmunQuestService.updateSulPaper(commandMap);
		}
		else if(p_process.equals("delete"))
		{
			isok = sulmunQuestService.deleteSulPaper(commandMap);
		}
		
		
		
		if(isok)
		{
			resultMsg = egovMessageSource.getMessage("success.common." + p_process);
			
		}else{
//			사용되어진 문제가 있다면
			if(p_process.equals("delete"))
				resultMsg = egovMessageSource.getMessage("fail.common.status.count." + p_process);
			else
				resultMsg = egovMessageSource.getMessage("fail.common." + p_process);
		}
		
		model.addAttribute("isOpenerReload", "OK");
		model.addAttribute("isClose", "OK");
		model.addAttribute("resultMsg", resultMsg);
		
		model.addAllAttributes(commandMap);
		return "forward:" + url;
	}
	
	/**
	 * 설문통계
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulResultList.do")
	public String sulResultList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = commandMap.get("p_process") + "";
		
		String url = "adm/rsh/sulResultList";
		
		if(p_process != null && p_process.equals("xlsdown")) url = "adm/rsh/sulResultXls";
		if(p_process != null && p_process.equals("xlsdetaildown")) url = "adm/rsh/sulResultList_Detail";
		
		Hashtable hash = new Hashtable();
        ArrayList QuestionExampleDataList = new ArrayList();
		SulmunQuestionExampleData data = null;
        SulmunExampleData exampledata  = null;
        
        Vector v_answers = new Vector();
        
		List<?> subjList = sulmunQuestService.selectSulResultSulNumsList(commandMap);
		
		
		//설문문제번호 
		String subjSulnums = "";
		
		if(subjList != null)
		{
			for(int k=0; k<subjList.size(); k++)
			{
				int v_bef_sulnum = 0;
				Vector p_sulnums = new Vector();
				String p_getSulnumsOderBy = "";
				
				Map mm = (Map)subjList.get(k);
				String p_getSulnums = mm.get("sulnums") + "";
				
				//설문번호를 넣어준다.
				commandMap.put("p_sulpapernum", mm.get("sulpapernum"));
				
				
				StringTokenizer st = new StringTokenizer(p_getSulnums, SulmunSubjBean.SPLIT_COMMA);
				
//				문항 정렬 order by 문을 만든다.
				int i = 0;
				while ( st.hasMoreElements() ) {
					i++;
					String v_sulnum = (String)st.nextToken();
					
					if(i > 1) p_getSulnumsOderBy += SulmunSubjBean.SPLIT_COMMA;
					
					p_getSulnumsOderBy +=  v_sulnum + SulmunSubjBean.SPLIT_COMMA + i;
					
					p_sulnums.add(v_sulnum);
				}
				
//				문항 가져오기 완료------------------------------------------------
				log.info("설문 문항 전체 번호 : " + p_getSulnums);
				log.info("설문 문항 Order by 절 : " + p_getSulnumsOderBy);
				
				commandMap.put("p_getSulnums", p_getSulnums);
				commandMap.put("p_getSulnumsOrderBy", p_getSulnumsOderBy);
				List<?> sulList = sulmunQuestService.selectSulPaperPreviewList(commandMap);
				commandMap.remove("p_getSulnums");
				commandMap.remove("p_getSulnumsOrderBy");
				
				
				
				
				//문항가져오기------------------------------------------------
				for(i=0; i<sulList.size(); i++)
				{
					Map ls = (Map)sulList.get(i);
					
					if ( v_bef_sulnum != parseWithDefault(ls.get("sulnum")+"", 0) ) { 
	                    data = new SulmunQuestionExampleData();
	                    data.setSubj( ls.get("subj")+"" );
	                    data.setSulnum( parseWithDefault(ls.get("sulnum")+"", 0) );
	                    data.setSultext( ls.get("sultext")+"" );
	                    data.setSultype( ls.get("sultype")+"" );
	                    data.setSultypenm( ls.get("sultypenm")+"" );
	                    data.setDistcode( ls.get("distcode")+"" );
	                    data.setDistcodenm( ls.get("distcodenm")+"" );
	                }
	                exampledata = new SulmunExampleData();
	                exampledata.setSubj(data.getSubj() );
	                exampledata.setSulnum(data.getSulnum() );
	                exampledata.setSelnum( parseWithDefault(ls.get("selnum")+"", 0) );
	                exampledata.setSelpoint( parseWithDefault(ls.get("selpoint")+"", 0) );
	                exampledata.setSeltext( ls.get("seltext")+"" );
	                data.add(exampledata);
	                if ( v_bef_sulnum != data.getSulnum() )  { 
	                    hash.put(String.valueOf(data.getSulnum() ), data);
	                    v_bef_sulnum = data.getSulnum();
	                }
				}
				
				
				 data = null;
				 log.info(" >>  >>  >>  >>  > p_sulnums size : " + p_sulnums.size() );               
	             for (i = 0; i < p_sulnums.size(); i++ ) { 
	                 data = (SulmunQuestionExampleData)hash.get((String)p_sulnums.get(i));
	                 if ( data != null ) { 
	                	 QuestionExampleDataList.add(data);
	                 }
	             }
	             
	             
	           //문항가져오기------------------------------------------------
	             List<?> answersList = sulmunQuestService.selectSulResultAnswersList(commandMap);
	            
				for(i=0; i<answersList.size(); i++)
				{
					Map ls = (Map)answersList.get(i);
					v_answers.add(ls.get("answers"));
				}
					
					
	             this.ComputeCount(QuestionExampleDataList, v_answers, answersList, model);
				
	            
	             
				
			}
		}
		

		List<?> hukiList = sulmunQuestService.selectHukiList(commandMap);
		
		 //응답자수
        model.addAttribute("p_replycount", parseWithDefault(v_answers.size()+"", 0));
		
        //수강자수
        model.addAttribute("p_studentcount", parseWithDefault(sulmunQuestService.selectSulStudentMemberCount(commandMap) + "", 0));
        
        //설문 답안 통계정보
		model.addAttribute("SulmunResultList", QuestionExampleDataList);
		
		//교육후기 리스트
		model.addAttribute("hukiList", hukiList);
		
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	/**
	 * 숫자변환때문에 만듬
	 * @param number
	 * @param def
	 * @return int
	 * @throws Exception
	 */
	public int parseWithDefault(String number, int def)
	{
		try {
			return Integer.parseInt(number);
		} catch(NumberFormatException e) {
			return def;
		}
	}
	
	/**
    과목설문 결과보기 응답율 구하기 
    @param box          receive from the form object and session
    @return ArrayList
    */
    public void ComputeCount(ArrayList p_list, Vector p_answers, List<?> answersList, ModelMap model) throws Exception { 
        StringTokenizer st1 = null;
        StringTokenizer st2 = null;

        SulmunQuestionExampleData data = null;
        SulmunSubjectAnswerData answerdata  = null;
		Vector subject = new Vector();
		Vector complex = new Vector();
        String v_answers = "";
        String v_answer  = "";
        int index=0;
        ArrayList list = new ArrayList();

        try { 
            for ( int i = 0; i < p_answers.size(); i++ ) { 
                v_answers = (String)p_answers.get(i);
                Map ls = (Map)answersList.get(i);
                ArrayList list1 = new ArrayList();
                
                st1 = new StringTokenizer(v_answers, SulmunSubjBean.SPLIT_COMMA);
                index = 0;
                while ( st1.hasMoreElements() && index < p_list.size() ) { 
                	
                    v_answer = (String)st1.nextToken();
                    data = (SulmunQuestionExampleData)p_list.get(index);
                    
                    
                    answerdata = new SulmunSubjectAnswerData();
                    answerdata.setUserid(ls.get("userid")+"");
                    answerdata.setName(ls.get("name")+"");
                    answerdata.setSex(ls.get("sex")+"");
                    answerdata.setEmpnm(ls.get("empNm")+"");
                    answerdata.setBirthdate(ls.get("birthdate")+"");
                    answerdata.setAge(ls.get("age")+"");
                    answerdata.setPositonnm(ls.get("postionNm")+"");
                    answerdata.setSulnum(index);
                    answerdata.setSubj(data.getSubj());
                    answerdata.setAnstext(v_answer);
                    
                    if ( data.getSultype().equals(SulmunSubjBean.OBJECT_QUESTION)) { 
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                        answerdata.setAnstext(v_answer);
//                        log.info(i + ") v_answers : " + data.getSulnum() + "/" + data.getSelnum(index) + "/" + v_answer + "/" + answerdata.getUserid() + "/" + index);
                        
                    } else if ( data.getSultype().equals(SulmunSubjBean.MULTI_QUESTION)) { 
                        st2 = new StringTokenizer(v_answer,SulmunSubjBean.SPLIT_COLON);
                        while ( st2.hasMoreElements() ) { 
                            data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
                            answerdata.setAnstext(v_answer);
//                            log.info(i + ") v_answers : " + data.getSulnum() + "/" + data.getSelnum(index) + "/" + v_answer + "/" + answerdata.getUserid() + "/" + index);
                        }
                    } else if ( data.getSultype().equals(SulmunSubjBean.SUBJECT_QUESTION)) { 
                        subject.add(v_answer);// System.out.println("v_answer " + v_answer);
                        answerdata.setAnstext(v_answer);
                    } else if ( data.getSultype().equals(SulmunSubjBean.COMPLEX_QUESTION)) { 
                        st2 = new StringTokenizer(v_answer,SulmunSubjBean.SPLIT_COLON);
                        v_answer = (String)st2.nextToken();
                        answerdata.setAnstext(v_answer);
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
//                        log.info(i + ") v_answers : " + data.getSulnum() + "/" + data.getSelnum(index) + "/" + v_answer + "/" + answerdata.getUserid() + "/" + index);
                        String v_sanswer = "";
                        if ( st2.hasMoreElements() ) { 
                            v_sanswer = (String)st2.nextToken();
                            answerdata.setAnstext(v_sanswer);
                        }
                        complex.add(v_sanswer);
                    } else if ( data.getSultype().equals(SulmunSubjBean.FSCALE_QUESTION)) { 
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
//                        log.info(i + ") v_answers : " + data.getSulnum() + "/" + data.getSelnum(index) + "/" + v_answer + "/" + answerdata.getUserid() + "/" + index);
                        answerdata.setAnstext(v_answer);
                    } else if ( data.getSultype().equals(SulmunSubjBean.SSCALE_QUESTION)) { 
                        data.IncreasReplyCount(Integer.valueOf(v_answer).intValue() );
//                        log.info(i + ") v_answers : " + data.getSulnum() + "/" + data.getSelnum(index) + "/" + v_answer + "/" + answerdata.getUserid() + "/" + index);
                        answerdata.setAnstext(v_answer);
                    } 
                    list1.add(answerdata);
                    
                    index++;
                }
                
                list.add(list1);
                
            }
            
            model.addAttribute("answerList", list);
            // 응답비율을 계산한다. 리스트((설문번호1, 보기1,2,3..))
            for ( int i = 0; i < p_list.size(); i++ ) { 
                data = (SulmunQuestionExampleData)p_list.get(i);
	   				data.ComputeRate();
					data.setComplexAnswer(complex);
					data.setSubjectAnswer(subject);
					//System.out.println(subject);
            }
        } catch ( Exception ex ) { 
        	ex.printStackTrace();
            throw new Exception(ex.getMessage());
        }
    }
    
    /**
	 * 설문조사 시스템
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/rsh/sulResultStats.do")
	public String sulResultStats(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		
		Calendar gCal = Calendar.getInstance();		
		String v_dyear	= Integer.toString(gCal.get(Calendar.YEAR));
		if(commandMap.get("ses_search_gyear") == null){
			commandMap.put("ses_search_gyear", v_dyear);
		}
		
		String p_process = commandMap.get("p_process") + "";
		
		int p_per = commandMap.get("p_per") != null ? Integer.parseInt(commandMap.get("p_per").toString()) : 0;
		
		String[] p_temp_gubun = new String[4]; 
		
		p_temp_gubun[0] = commandMap.get("p_temp_gubun_1") != null ? commandMap.get("p_temp_gubun_1").toString() : "";
		p_temp_gubun[1] = commandMap.get("p_temp_gubun_2") != null ? commandMap.get("p_temp_gubun_2").toString() : "";
		p_temp_gubun[2] = commandMap.get("p_temp_gubun_3") != null ? commandMap.get("p_temp_gubun_3").toString() : "";
		p_temp_gubun[3] = commandMap.get("p_temp_gubun_4") != null ? commandMap.get("p_temp_gubun_4").toString() : "";
		
		String temp_gubun = "";
		int gg=0;
		for(int g=0; g<4; g++){			
			if(p_temp_gubun[g].length() > 0){
				if(gg==0){
					gg++;
					temp_gubun =  "'"+p_temp_gubun[g]+"'";
				}else{
					temp_gubun += ",'"+p_temp_gubun[g]+"'";
				}
			}
		}
		System.out.println("temp_gubun ----> "+temp_gubun);
		commandMap.put("temp_gubun", temp_gubun);
		
		System.out.println("p_per -----> "+p_per);
		
		if(0 == p_per){			
			System.out.println("p_per222 -----> "+p_per);			
			commandMap.put("p_per", p_per);			
		}
		
		System.out.println("commandMap p_per -----> "+commandMap.get("p_per"));
		
		
		
		String url = "adm/rsh/sulResultStats";
		
		if(p_process != null && p_process.equals("xlsdown")) url = "adm/rsh/sulResultStatsXls";
		if(p_process != null && p_process.equals("xlsdetaildown")) url = "adm/rsh/sulResultStats_Detail";
		
		Hashtable hash = new Hashtable();
        ArrayList QuestionExampleDataList = new ArrayList();
		SulmunQuestionExampleData data = null;
        SulmunExampleData exampledata  = null;
        
        Vector v_answers = new Vector();
        
		List<?> subjList = null;	
		
		if(p_per>0 && gg>=1){
			subjList = sulmunQuestService.selectPerSulResultSulNumsList(commandMap);
		}
		
		//설문문제번호 
		String subjSulnums = "";
		int p_studentcount  = 0;
		int p_replycount = 0;
		
		//subjList이 null일 경우만 체크 하도록 되어 있어 subjList(조회된데이터)가 없을 경우에 List의 공백을 체크하도록 수정 2019.09.18
		if(subjList != null && !subjList.isEmpty())
		{
			for(int k=0; k<subjList.size(); k++)
			{
				int v_bef_sulnum = 0;
				Vector p_sulnums = new Vector();
				String p_getSulnumsOderBy = "";
				
				Map mm = (Map)subjList.get(k);
				String p_getSulnums = mm.get("sulnums") + "";
				
				
				System.out.println("sulpapernum -----> "+mm.get("sulpapernum"));
				
				//설문번호를 넣어준다.
				commandMap.put("p_sulpapernum", mm.get("sulpapernum"));
				
				
				StringTokenizer st = new StringTokenizer(p_getSulnums, SulmunSubjBean.SPLIT_COMMA);
				
//				문항 정렬 order by 문을 만든다.
				int i = 0;
				while ( st.hasMoreElements() ) {
					i++;
					String v_sulnum = (String)st.nextToken();
					
					if(i > 1) p_getSulnumsOderBy += SulmunSubjBean.SPLIT_COMMA;
					
					p_getSulnumsOderBy +=  v_sulnum + SulmunSubjBean.SPLIT_COMMA + i;
					
					p_sulnums.add(v_sulnum);
				}
				
//				문항 가져오기 완료------------------------------------------------
				log.info("설문 문항 전체 번호 : " + p_getSulnums);
				log.info("설문 문항 Order by 절 : " + p_getSulnumsOderBy);
				
				commandMap.put("p_getSulnums", p_getSulnums);
				commandMap.put("p_getSulnumsOrderBy", p_getSulnumsOderBy);
				
				commandMap.put("p_sulTypeNumber", "5");
				System.out.println("p_sulTypeNumber ------> "+commandMap.get("p_sulTypeNumber"));
				List<?> sulList = sulmunQuestService.selectSulPaperPreviewList(commandMap);
				
				commandMap.remove("p_getSulnums");
				commandMap.remove("p_getSulnumsOrderBy");
				
				
				
				
				//문항가져오기------------------------------------------------
				for(i=0; i<sulList.size(); i++)
				{
					Map ls = (Map)sulList.get(i);
					
					if ( v_bef_sulnum != parseWithDefault(ls.get("sulnum")+"", 0) ) { 
	                    data = new SulmunQuestionExampleData();
	                    data.setSubj( ls.get("subj")+"" );
	                    data.setSulnum( parseWithDefault(ls.get("sulnum")+"", 0) );
	                    data.setSultext( ls.get("sultext")+"" );
	                    data.setSultype( ls.get("sultype")+"" );
	                    data.setSultypenm( ls.get("sultypenm")+"" );
	                    data.setDistcode( ls.get("distcode")+"" );
	                    data.setDistcodenm( ls.get("distcodenm")+"" );
	                }
	                exampledata = new SulmunExampleData();
	                exampledata.setSubj(data.getSubj() );
	                exampledata.setSulnum(data.getSulnum() );
	                exampledata.setSelnum( parseWithDefault(ls.get("selnum")+"", 0) );
	                exampledata.setSelpoint( parseWithDefault(ls.get("selpoint")+"", 0) );
	                exampledata.setSeltext( ls.get("seltext")+"" );
	                data.add(exampledata);
	                if ( v_bef_sulnum != data.getSulnum() )  { 
	                    hash.put(String.valueOf(data.getSulnum() ), data);
	                    v_bef_sulnum = data.getSulnum();
	                }
				}
				
				
				
				data = null;
				 log.info(" >>  >>  >>  >>  > per p_sulnums size : " + p_sulnums.size() );               
	             for (i = 0; i < p_sulnums.size(); i++ ) { 
	                 data = (SulmunQuestionExampleData)hash.get((String)p_sulnums.get(i));
	                 if ( data != null ) { 
	                	 QuestionExampleDataList.add(data);
	                 }
	             }	
				
	            //문항가져오기------------------------------------------------
	            List<?> answersList = sulmunQuestService.selectPerSulResultAnswersList(commandMap);
	            
				for(i=0; i<answersList.size(); i++)
				{
					Map ls = (Map)answersList.get(i);
					v_answers.add(ls.get("answers"));
				}
				
	            this.ComputeCount(QuestionExampleDataList, v_answers, answersList, model);
				
			}
		
		

			//List<?> hukiList = sulmunQuestService.selectHukiList(commandMap);
			
			//응답자수
			p_replycount = parseWithDefault(v_answers.size()+"", 0);
		    //수강자
	        p_studentcount  = parseWithDefault(sulmunQuestService.selectPerSulStudentMemberCount(commandMap) + "", 0);
		   
	     
			
			/**************************************/
			try {
				
			
			
				ArrayList list = (ArrayList)QuestionExampleDataList;
				
			    data    = null;
			    SulmunExampleData         subdata = null;
		
				int k = 0;
				int l = 0;
				
				int sulCount = 0;
				double sulAvg = 0; 
				
				double[] param_v_point = new double[list.size()];
				
				//out.println("###############" +  list.size());
			    for (int i=0; i < list.size(); i++) {
			        data = (SulmunQuestionExampleData)list.get(i);
			        
			        if (data.getSultype().equals(SulmunSubjBean.FSCALE_QUESTION)) {			//객관식
			     	   
				     	   System.out.println("SulmunSubjBean.FSCALE_QUESTION 111111 ----> "+ SulmunSubjBean.FSCALE_QUESTION);
				     	   System.out.println("data.getSultype() ----> "+ data.getSultype());
				     	   
				 	          double d = 0; 
				 			  int person = 0;
				 			  double v_point = 0;
				       
				 		   for (int j=1; j <= data.size(); j++) {
				 	            subdata  = (SulmunExampleData)data.get(j); 
				                 if (subdata != null) { 
				                 	
				                 	System.out.println("subdata.getReplycnt() ----> "+ subdata.getReplycnt());
				 					System.out.println("subdata.getSelpoint() ----> "+ subdata.getSelpoint());
				 					
				 					
				 					d +=  (subdata.getReplycnt()) * subdata.getSelpoint();
				 					person += subdata.getReplycnt();
				 				}
				             }	
				                
				 		   
				 			
				 		v_point = d / person;	
				 		
				 		v_point = Math.round(v_point *100d) / 100d;
				 		System.out.println("v_point 111 ----> "+ v_point);
				 		
				 		sulCount++;
				 		sulAvg += v_point;
				 		
				 		param_v_point[i] = v_point;
				 		
				 		System.out.println(i+ " d ----> "+ d);
				 		System.out.println("person ----> "+ person);
				 		System.out.println("v_point ----> "+ v_point);
				 		System.out.println("sulCount ----> "+ sulCount);
				  		System.out.println("sulAvg ----> "+ sulAvg);
				  		
				  		commandMap.put("param_v_point", param_v_point);
				  		
			        }
			        
			        
			    }
			    
			    
			    for(int i = 0; i < param_v_point.length; i++)
		        {
		            for(int j = i; j < param_v_point.length; j++)         // 첫번째 배열의 값이 두번째 배열보다 크면 자리를 바꿈
		            {                                            
		                if(param_v_point[i] > param_v_point[j])           // 순서대로 다음의 배열과 비교하여 큰 경우에 자리를 바꿈
		                {                                        
		                    double temp = param_v_point[i];
		                    param_v_point[i] = param_v_point[j];
		                    param_v_point[j] = temp;
		                }
		            }
		        }
			    
		        for(int i = 0; i < param_v_point.length; i++)
		        {
		            System.out.println(param_v_point[i]);    // 첫번째 배열부터 내림차순으로 출력
		        }
		        //System.out.println(param_v_point[0]);        // 최소값이 첫번째 배열에 저장
		        
		        commandMap.put("param_v_point",param_v_point);
		        commandMap.put("rn_num",10);
		        
		        Map<?, ?> sulmunStddev1 = sulmunQuestService.selectSulResultStddev(commandMap);
		        
		        commandMap.put("rn_num",5);	        
		        Map<?, ?> sulmunStddev2 = sulmunQuestService.selectSulResultStddev(commandMap);
		        
		        
			    double y1[] = quartile(request, response, commandMap, model, param_v_point, 10);
			    System.out.println("y1[0] -----> "+y1[0]);
		        System.out.println("y1[1] -----> "+y1[1]);
		        System.out.println("y1[2] -----> "+y1[2]);
		        System.out.println("y1[3] -----> "+y1[3]);
		        
		        double y2[] = quartile(request, response, commandMap, model, param_v_point, 5);
			    System.out.println("y2[0] -----> "+y2[0]);
		        System.out.println("y2[1] -----> "+y2[1]);
		        System.out.println("y2[2] -----> "+y2[2]);
		        System.out.println("y2[3] -----> "+y2[3]);
		        
		        model.addAttribute("y1", y1);
		        model.addAttribute("y2", y2);
		        model.addAttribute("sulmunStddev1", sulmunStddev1);
		        model.addAttribute("sulmunStddev2", sulmunStddev2);
		        
			} catch (Exception e) {
				e.printStackTrace();			
				System.out.println("e -----> "+ e);
			}
		    /**************************************/ 
		}
		
		//교육후기 리스트
		//model.addAttribute("hukiList", hukiList);
		model.addAttribute("p_replycount", p_replycount);
		//수강자수
	    model.addAttribute("p_studentcount", p_studentcount);
	    //설문 답안 통계정보
		model.addAttribute("SulmunResultList", QuestionExampleDataList);
		
		model.addAllAttributes(commandMap);
		return url;
	}
	
	
	public double[] quartile(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model, double param_v_point[], int n) throws Exception {
		
        //double p = 0.2;
		double p = 0.25;
        double ajag = 0; 
        int intajag = 0;
        double aj = 0;
        double ag = 0;
        int po_1 = 0;
        int po_2 = 0;
        double po_1_val = 0;
        double po_2_val = 0;	        
        double[] y = new double[4];
        
        System.out.println("n -----> "+n);
        for(int m=1; m <= 4; m++){
        //for(int m=1; m <= 3; m++){
        	if(m==2){
        		p = 0.4;
        		//p = 0.5;
        	}
        	if(m==3){
        		p = 0.6;
        		//p = 0.75;
        	}
        	if(m==4){
        		p = 0.8;
        	}	
        	ajag = (n-1)*p;
        	intajag = (int) ajag;
        	aj = intajag;
        	ag = ajag-intajag;
        	
        	po_1 = intajag+1;
        	po_2 = intajag+2;
        	
        	po_1_val = param_v_point[po_1-1];
        	po_2_val = param_v_point[po_2-1];
        	
        	y[m-1] = (1-ag)*(po_1_val) +ag*(po_2_val);
        	
        	System.out.println("ajag -----> "+ajag);
        	System.out.println("intajag -----> "+intajag);
        	System.out.println("aj -----> "+aj);
        	System.out.println("ag -----> "+ag);
        	System.out.println("po_1 -----> "+po_1);
        	System.out.println("po_2 -----> "+po_2);
        	System.out.println("po_1_val -----> "+po_1_val);
        	System.out.println("po_2_val -----> "+po_2_val);
        	System.out.println("y -----> "+y);
        	
        }
        
        
        return y;
        
	}
    
    
}
