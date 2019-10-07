package egovframework.adm.sat.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import egovframework.adm.sat.service.SatisticsService;
import egovframework.adm.stu.service.StuMemberService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.com.tag.CustomFnTag;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.adm.cou.service.SubjectService;
import egovframework.adm.rsh.service.SulmunQuestService;

@Controller
public class SatisticsController {
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
	
	/** satisticsService */
	@Resource(name = "satisticsService")
    private SatisticsService satisticsService;
	
	/** stuMemberService */
	@Resource(name = "stuMemberService")
    private StuMemberService stuMemberService;
	
	/** sulmunQuestService */
	@Resource(name = "sulmunQuestService")
    private SulmunQuestService sulmunQuestService;
	
	/** subjectService */
	@Resource(name = "subjectService")
	private SubjectService subjectService;

	
	/**
	 * 회원통계
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/memberSatisList.do")
	public String memberSatisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = satisticsService.memberSatisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/sat/memberSatisList";
	}

	/**
	 * 과정별교육실적
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/subjectSatisList.do")
	public String subjectSatisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = stuMemberService.selectStuMemberCountList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/sat/subjectSatisList";
	}
	
	
	/**
	 * 과정별 결과 보고서
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/adm/sat/subjectFinishSatisList.do")
	public String subjectFinishSatisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String tab_gubun = "";
		if(commandMap.get("tab_gubun") != null && !"".equals(commandMap.get("tab_gubun"))){
			tab_gubun = commandMap.get("tab_gubun")+"";
		}
		
		log.error("tab_gubun : "+tab_gubun);
		
		
		if("A".equals(tab_gubun) || tab_gubun == "A"){
			//과정정보
			model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
			
			Map<String, Object> amtMap = new HashMap<String, Object>();//입금정보
			List<?> list_a = satisticsService.subjectResultReportList(commandMap);
			List<?> list_amount = satisticsService.subjectResultReporAmounttList(commandMap);
			
			amtMap.put("totalAmount", ((Map<String, Object>)list_a.get(0)).get("totalAmount"));								//전체 입금액
			amtMap.put("payTotalCnt", ((Map<String, Object>)list_a.get(0)).get("payTotalCnt"));								//징수 인원
			amtMap.put("totCnt", ((Map<String, Object>)list_a.get(0)).get("totCnt"));												//수강인원
			
			amtMap.put("subjChkfinalAmount", ((Map<String, Object>)list_a.get(0)).get("subjChkfinalAmount"));			//전체 입금액
			amtMap.put("chkfinalDeptCnt", ((Map<String, Object>)list_a.get(0)).get("chkfinalDeptCnt"));								//징수 인원
			amtMap.put("chkfinalSubCnt", ((Map<String, Object>)list_a.get(0)).get("chkfinalSubCnt"));								//징수 인원
			
			model.addAttribute("amtMap"	, amtMap);
			model.addAttribute("list_a"		, list_a);
			model.addAttribute("list_amount"		, list_amount);
			model.addAttribute("comList"	, satisticsService.subjectResultReportCommunityList(commandMap));
			model.addAllAttributes(commandMap);
			
		}else if("B".equals(tab_gubun) || tab_gubun == "B"){
			
			//과정정보
			model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
			
			//학습자 미이수정보
			List<?> list = stuMemberService.selectSubjectFinishSatisList(commandMap);
			model.addAttribute("list_b", list);
			
			//직무관련 -기타
			model.addAttribute("subjEtcCount", subjectService.selectSubjEtcCount(commandMap));
			
			//학습자 이수정보
			/*
			commandMap.put("p_isgraduated", "Y");
			List<?> passlist = stuMemberService.selectSubjectFinishSatisList(commandMap);
			model.addAttribute("passlist", passlist);
			*/
			model.addAllAttributes(commandMap);
		}else if("C".equals(tab_gubun) || tab_gubun == "C"){			
			
			log.error("전체설문결과보기");
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
					
//					문항 정렬 order by 문을 만든다.
					int i = 0;
					while ( st.hasMoreElements() ) {
						i++;
						String v_sulnum = (String)st.nextToken();
						
						if(i > 1) p_getSulnumsOderBy += SulmunSubjBean.SPLIT_COMMA;
						
						p_getSulnumsOderBy +=  v_sulnum + SulmunSubjBean.SPLIT_COMMA + i;
						
						p_sulnums.add(v_sulnum);
					}
					
//					문항 가져오기 완료------------------------------------------------
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
			model.addAllAttributes(commandMap);
			model.remove("view");
			//교육후기 리스트
			model.addAttribute("hukiList", hukiList);
			//과정정보
			model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
		}else {
			model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
			model.addAllAttributes(commandMap);
		
		}
				
		
		return "adm/sat/subjectFinishSatisList";
	}
	
	/**
	 * 과정별 결과 보고서 RD출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/subjectFinishSatisPrint.do")
	public String subjectFinishSatisPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		log.error("과정별 결과 보고서 RD출력");
		String tab_gubun = "";
		if(commandMap.get("tab_gubun") != null && !"".equals(commandMap.get("tab_gubun"))){
			tab_gubun = commandMap.get("tab_gubun")+"";
		}		
		String return_page = "adm/sat/subjectFinishSatisAPrint";
		Map<?,?> map = stuMemberService.selectSubjectFinishSatisView(commandMap);
		model.addAllAttributes(commandMap);
		//과정정보
		model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
		
		model.addAttribute("subjnm"				, commandMap.get("print_subjnm"));
		model.addAttribute("edustart"				, commandMap.get("print_edustart"));
		model.addAttribute("edutimes"			, commandMap.get("print_edutimes"));
		model.addAttribute("studentTotalCnt"	, commandMap.get("print_studentTotalCnt"));
		model.addAttribute("isgraduatedY"		, commandMap.get("print_isgraduatedY"));
		model.addAttribute("isgraduatedN"		, commandMap.get("print_isgraduatedN"));
		
		
		
		log.error("subjnm["+commandMap.get("print_subjnm")+"]");
		log.error("edustart["+commandMap.get("print_edustart")+"]");
		log.error("edutimes["+commandMap.get("print_edutimes")+"]");
		log.error("studentTotalCnt["+commandMap.get("print_studentTotalCnt")+"]");
		log.error("isgraduatedY["+commandMap.get("print_isgraduatedY")+"]");
		log.error("isgraduatedN["+commandMap.get("print_isgraduatedN")+"]");
		
		
		
		if("A".equals(tab_gubun) || tab_gubun == "A"){
			
			return_page = "adm/sat/subjectFinishSatisListAPrint";
			
		}else if("B".equals(tab_gubun) || tab_gubun == "B"){
			model.addAttribute("ischargeNm"			, commandMap.get("print_ischargeNm"));
			model.addAttribute("upperclassNm"		, commandMap.get("print_upperclassNm"));
			log.error("ischargeNm["+commandMap.get("print_ischargeNm")+"]");
			log.error("upperclassNm["+commandMap.get("print_upperclassNm")+"]");
			
			return_page = "adm/sat/subjectFinishSatisListBPrint";
			
		}else if("C".equals(tab_gubun) || tab_gubun == "C"){
			model.addAttribute("replycount"			, commandMap.get("print_replycount"));
			model.addAttribute("replyrate"			, commandMap.get("print_replyrate"));
			log.error("replycount["+commandMap.get("print_replycount")+"]");
			log.error("replyrate["+commandMap.get("print_replyrate")+"]");
			
			return_page = "adm/sat/subjectFinishSatisListCPrint";
			
		}
		
		return return_page;
	}
	
	
	/**
	 * 과정별 결정보고서 엑셀 다운로드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/subjectFinishSatisExcelDown.do")
	public String subjectFinishSatisExcelDown(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		log.error("과정별 이수현황 엑셀 다운로드 ");
		String tab_gubun = commandMap.get("tab_gubun")+"";
		String url = "adm/sat/subjectFinishSatisListAxls";
		log.error("tab_gubun : "+tab_gubun);
		//과정정보
		model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
		
		if("A".equals(tab_gubun) || tab_gubun == "A"){
			Map<String, Object> amtMap = new HashMap<String, Object>();//입금정보
			List<?> list_a = satisticsService.subjectResultReportList(commandMap);
			List<?> list_amount = satisticsService.subjectResultReporAmounttList(commandMap);
			
			amtMap.put("totalAmount", ((Map<String, Object>)list_a.get(0)).get("totalAmount"));								//전체 입금액
			amtMap.put("payTotalCnt", ((Map<String, Object>)list_a.get(0)).get("payTotalCnt"));								//징수 인원
			amtMap.put("totCnt", ((Map<String, Object>)list_a.get(0)).get("totCnt"));												//수강인원
			
			model.addAttribute("amtMap"	, amtMap);
			model.addAttribute("list_a"		, list_a);
			model.addAttribute("list_amount"		, list_amount);
			model.addAttribute("comList"	, satisticsService.subjectResultReportCommunityList(commandMap));
			url = "adm/sat/subjectFinishSatisListAxls";
		}else if("B".equals(tab_gubun) || tab_gubun == "B"){
			
			//학습자 미이수정보
			List<?> list = stuMemberService.selectSubjectFinishSatisList(commandMap);
			model.addAttribute("list_b", list);
			
			//직무관련 -기타
			model.addAttribute("subjEtcCount", subjectService.selectSubjEtcCount(commandMap));
			
			//학습자 이수정보
			/*
			commandMap.put("p_isgraduated", "Y");
			List<?> passlist = stuMemberService.selectSubjectFinishSatisList(commandMap);
			model.addAttribute("passlist", passlist);
			*/
			url = "adm/sat/subjectFinishSatisListBxls";
		}else if("C".equals(tab_gubun) || tab_gubun == "C"){
			
			url = "adm/sat/subjectFinishSatisListCxls";
			
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
					
//					문항 정렬 order by 문을 만든다.
					int i = 0;
					while ( st.hasMoreElements() ) {
						i++;
						String v_sulnum = (String)st.nextToken();
						
						if(i > 1) p_getSulnumsOderBy += SulmunSubjBean.SPLIT_COMMA;
						
						p_getSulnumsOderBy +=  v_sulnum + SulmunSubjBean.SPLIT_COMMA + i;
						
						p_sulnums.add(v_sulnum);
					}
					
//					문항 가져오기 완료------------------------------------------------
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
	       
	        //교육후기
			model.addAttribute("SulmunResultList", QuestionExampleDataList);
			
			 //설문 답안 통계정보
			model.addAttribute("hukiList", hukiList);
			
			
		}
		
		model.addAllAttributes(commandMap);
		
		//return new ModelAndView("subjectFinishSatisExcelView", "subjectFinishSatisMap", map);
		return url;
	}
	@RequestMapping(value="/adm/sat/subjectFinishSatisExcelDown1.do")
	public String subjectFinishSatisExcelDown1(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
		log.error("과정별 이수현황 엑셀 다운로드 ");
		String tab_gubun = commandMap.get("tab_gubun")+"";
		String url = "adm/sat/subjectFinishSatisListAxls";
		log.error("tab_gubun : "+tab_gubun);
		//과정정보
		model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
		
			//학습자 이수정보
		    commandMap.put("p_isgraduated", "Y");
			List<?> list = stuMemberService.selectSubjectFinishSatisList(commandMap);
			model.addAttribute("list_b", list);
			
			//직무관련 -기타
			model.addAttribute("subjEtcCount", subjectService.selectSubjEtcCount(commandMap));
			
			//학습자 이수정보
			/*
			commandMap.put("p_isgraduated", "Y");
			List<?> passlist = stuMemberService.selectSubjectFinishSatisList(commandMap);
			model.addAttribute("passlist", passlist);
			*/
			url = "adm/sat/subjectFinishSatisListBxlsUpload";

			
			//List<?> hukiList = sulmunQuestService.selectHukiList(commandMap);
			 //응답자수
//	        model.addAttribute("p_replycount", parseWithDefault(v_answers.size()+"", 0));
			
	        //수강자수
	        //model.addAttribute("p_studentcount", parseWithDefault(sulmunQuestService.selectSulStudentMemberCount(commandMap) + "", 0));
	       
	        //교육후기
//			model.addAttribute("SulmunResultList", QuestionExampleDataList);
			
			 //설문 답안 통계정보
			//model.addAttribute("hukiList", hukiList);
			
			

		
		model.addAllAttributes(commandMap);
		
		//return new ModelAndView("subjectFinishSatisExcelView", "subjectFinishSatisMap", map);
		return url;
	}
	
	
	
	
	/**
	 * 과정별교육실적 - 상세보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/subjectSatisDetailList.do")
	public String subjectSatisDetailList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String p_process = commandMap.get("p_process") + "";
		
		String url = "adm/sat/subjectSatisDetailList";
		
		if(p_process != null && p_process.equals("xlsdown")) url = "adm/sat/subjectSatisDetailXls";
		
		
		List<?> list = satisticsService.subjectSatisDetailList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return url;
	}
	
	
	
	/**
	 * 연도별교육실적
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/yearSubjectSatisList.do")
	public String yearSubjectSatisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = satisticsService.yearSubjectSatisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/sat/yearSubjectSatisList";
	}
	
	/**
	 * 분야별교육실적
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/classSubjectSatisList.do")
	public String classSubjectSatisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = satisticsService.classSubjectSatisList(commandMap);
		
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);	
		
		return "adm/sat/classSubjectSatisList";		
	}
	
	@RequestMapping(value="/adm/sat/classSubjectSatisListRd.do")
	public String classSubjectSatisListRd(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		model.addAllAttributes(commandMap);		
		return "adm/sat/classSubjectSatisListRd";		
	}
	
	
	
	/**
	 * 분야별교육실적
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sat/countMemberSatisList.do")
	public String countMemberSatisList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		//년도 없을시
		if(commandMap.get("ses_search_gyear") == null || "".equals(commandMap.get("ses_search_gyear")))
		{
			commandMap.put("ses_search_gyear", CustomFnTag.getFormatDateNow("yyyy"));
		}
		//월이 없을시
		if(commandMap.get("ses_search_gmonth") == null || "".equals(commandMap.get("ses_search_gmonth")))
		{
			commandMap.put("ses_search_gmonth", CustomFnTag.getFormatDateNow("MM"));
		}
		
//		연도별
		List<?> yearList = satisticsService.countYearSatisList(commandMap);
		model.addAttribute("yearList", yearList);	
		
//		월별
		List<?> monthList = satisticsService.countMonthSatisList(commandMap);
		model.addAttribute("monthList", monthList);	
		
		model.addAllAttributes(commandMap);
		
		return "adm/sat/countMemberSatisList";
	}
	
	/**
	 * RD출력
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adm/sta/yearSubjectPrint.do")
	public String suRoyJeungPrint(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception{
				
		String return_page = "adm/sat/yearSubjectSatisPrint";
		String ses_search_gyear = (String)commandMap.get("ses_search_gyear");
		
		if(ses_search_gyear.equals("")){
			ses_search_gyear = "-1";
		}
		if(commandMap.get("rd_gubun").equals("C")){
			return_page = "adm/sat/subjectSatisPrint";
		}
		if(commandMap.get("rd_gubun").equals("D")){
			return_page = "adm/sat/subjectSatisDetailPrint";
		}
		commandMap.remove("ses_search_gyear");
		commandMap.put("ses_search_gyear", ses_search_gyear);
		model.addAllAttributes(commandMap);
		
		return return_page;
	}
	
	/**
	 * 연도별 콘텐츠 보유 현황
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/adm/sat/yearContentsList.do")
	public String yearContentsList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		String searchYn = commandMap.get("searchYn") != null ? (String)commandMap.get("searchYn") : "N";
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<?> list = null;
		if("Y".equals(searchYn)){			
			list = satisticsService.yearContentsList(commandMap);
			if(list.size() > 0){
				resultMap.put("totalContLesson", ((Map<String, Object>)list.get(0)).get("totalContLesson"));
				resultMap.put("totalSeqCnt", ((Map<String, Object>)list.get(0)).get("totalSeqCnt"));
				resultMap.put("totalStudentCnt", ((Map<String, Object>)list.get(0)).get("totalStudentCnt"));
			}
		}	  
		
		
		model.addAttribute("resultMap", resultMap);	
		model.addAttribute("list", list);	
		model.addAllAttributes(commandMap);
		
		return "adm/sat/yearContentsList";
	}
	
	/**
	 * 연도별 회원 현황
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="/adm/sat/yearMemberList.do")
	public String yearMemberList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		
		List<?> list = satisticsService.yearMemberList(commandMap);
		
		model.addAllAttributes(commandMap);
		model.addAttribute("resultList", list);	
		
		for(Object map : list){
			model.addAttribute("memberCnt", ((Map)map).get("memberCnt"));	
			break;
		}
		
		return "adm/sat/yearMemberList";
	}*/
	
	
	/**
	 * 전체설문결과보기
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/adm/sat/subjectResultList.do")
	public String subjectResultList(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception {
		log.error("전체설문결과보기");
		String p_process = commandMap.get("p_process") + "";
		model.addAttribute("tab_gubun", "C");
		
		String url = "adm/sat/subjectFinishSatisList";
		
		if(p_process != null && p_process.equals("xlsdown")) url = "adm/sat/subjectResultListXls";
		//if(p_process != null && p_process.equals("xlsdetaildown")) url = "adm/rsh/sulResultList_Detail";
		//url = "adm/sat/subjectFinishSatisListCxls";
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
		
		
		 //응답자수
        model.addAttribute("p_replycount", parseWithDefault(v_answers.size()+"", 0));
		
        //수강자수
        model.addAttribute("p_studentcount", parseWithDefault(sulmunQuestService.selectSulStudentMemberCount(commandMap) + "", 0));
       
        //설문 답안 통계정보
		model.addAttribute("SulmunResultList", QuestionExampleDataList);
		
		model.addAllAttributes(commandMap);
		model.remove("view");
		//과정정보
		model.addAttribute("view", stuMemberService.selectSubjectFinishSatisView(commandMap));
		
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
}
