package egovframework.com.aja.lcm.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.adl.api.ecmascript.APIErrorManager;
import org.adl.datamodels.DMErrorCodes;
import org.adl.datamodels.DMFactory;
import org.adl.datamodels.DMInterface;
import org.adl.datamodels.DMProcessingInfo;
import org.adl.datamodels.SCODataManager;
import org.adl.datamodels.nav.SCORM_2004_NAV_DM;
import org.adl.sequencer.ADLLaunch;
import org.adl.sequencer.ADLObjStatus;
import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.ADLSequencer;
import org.adl.sequencer.ADLTracking;
import org.adl.sequencer.ADLValidRequests;
import org.adl.sequencer.SeqActivity;
import org.adl.sequencer.SeqActivityTree;
import org.adl.util.MessageCollection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.lcms.cts.domain.LcmsCmiCommentfromlms;
import egovframework.adm.lcms.cts.domain.LcmsCmiCommentlearner;
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectinfo;
import egovframework.adm.lcms.cts.domain.LcmsCmiObjectives;
import egovframework.adm.lcms.cts.domain.LcmsItem;
import egovframework.adm.lcms.cts.domain.LcmsSeqObjectives;
import egovframework.adm.lcms.cts.domain.LcmsSerializer;
import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsCmiCommentfromlmsService;
import egovframework.adm.lcms.cts.service.LcmsCmiCommentlearnerService;
import egovframework.adm.lcms.cts.service.LcmsCmiObjectcommoninfoService;
import egovframework.adm.lcms.cts.service.LcmsCmiObjectinfoService;
import egovframework.adm.lcms.cts.service.LcmsCmiObjectivesService;
import egovframework.adm.lcms.cts.service.LcmsItemService;
import egovframework.adm.lcms.cts.service.LcmsScormSequenceService;
import egovframework.adm.lcms.cts.service.LcmsSeqObjectivesService;
import egovframework.adm.lcms.cts.service.LcmsSerializerService;
import egovframework.com.aja.lcm.domain.LmsStudyLogParamInf;
import egovframework.com.aja.lcm.domain.SessionDMInstance;
import egovframework.com.aja.service.CommonAjaxManageService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.lcms.len.domain.LcmsToc;
import egovframework.com.lcms.len.service.LcmsTocService;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 중복체크관련 처리 
 */
@Controller
public class ScormDmManagerAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(ScormHandlerAjaxController.class);
    
    
    protected  static boolean isCommit = true;
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	
	/** AJAX 공통 서비스*/
	@Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
	
	/** LcmsItemService */
	@Resource(name = "lcmsItemService")
	private LcmsItemService lcmsItemService;
	
	
	/** LcmsToc */
	@Resource(name = "lcmsTocService")
	private LcmsTocService lcmsTocService;
	
	/** LcmsSerializer */
	@Resource(name = "lcmsSerializerService")
	private LcmsSerializerService lcmsSerializerService;
	

	
	/** LcmsScormSequenceService */
	@Resource(name = "lcmsScormSequenceService")
	private LcmsScormSequenceService lcmsScormSequenceService;
	
	/** LcmsSeqObjectivesService */
	@Resource(name = "lcmsSeqObjectivesService")
	private LcmsSeqObjectivesService lcmsSeqObjectivesService;
	
	/** LcmsCmiObjectcommoninfoService */
	@Resource(name = "lcmsCmiObjectcommoninfoService")
	private LcmsCmiObjectcommoninfoService lcmsCmiObjectcommoninfoService;
	
	/** LcmsCmiObjectinfoService */
	@Resource(name = "lcmsCmiObjectinfoService")
	private LcmsCmiObjectinfoService lcmsCmiObjectinfoService;
	
	/** LcmsCmiObjectives**/
	@Resource(name = "lcmsCmiObjectivesService")
	private LcmsCmiObjectivesService lcmsCmiObjectivesService;
	
	/** LcmsCmiCommentfromlmsService**/
	@Resource(name = "lcmsCmiCommentfromlmsService")
	private LcmsCmiCommentfromlmsService lcmsCmiCommentfromlmsService;
	
	/** LcmsCmiCommentlearnerService**/
	@Resource(name = "lcmsCmiCommentlearnerService")
	private LcmsCmiCommentlearnerService lcmsCmiCommentlearnerService;
	

	
	private SeqActivityTree mSeqActivityTree;
	private ADLLaunch mlaunch;

	
	public final String mPRIMARY_OBJ_ID = null;

	private boolean isObject;

	private SessionDMInstance DMBean;

	//private LcmsCommentFromLMSVO commentLmsVoList[];
	private LcmsCmiCommentfromlms commentLmsVoList[]; 
	
	
	@RequestMapping(value="/com/aja/lcm/checkScormEduLimit.do")
	public ModelAndView checkScormEduLimit(HttpServletRequest rquest, HttpServletResponse response, Map<String, Object> commandMap) throws Exception{
		ModelAndView modelAndView = new ModelAndView();
    	Map resultMap = new HashMap();
    	Map result = new HashMap();
    	result.put("eduLimit", "PLAY");
    	if( commandMap.get("review") == null || commandMap.get("review").toString().equals("N") ){
    		result.remove("eduLimit");
    		result.put("eduLimit", lcmsCmiObjectinfoService.checkScormEduLimit(commandMap));
    	}
    	resultMap.put("result", result);
    	
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
	}
	

	@RequestMapping(value="/com/aja/lcm/ScormDmManagerAjaxController.do")
	public void ScormDmManagerAjaxController(
			HttpServletResponse response, 
			HttpServletRequest request, 
			Map<String, Object> commandMap
			) {
		
		isObject = false;
		DMBean = new SessionDMInstance();
		commentLmsVoList = null;
	}
	
	/**
	 * 학습시작 API
	 * 
	 * @return
	 * @throws Exception /com/aja/lcm/InitializeAPIEM.do
	 */
	@RequestMapping(value="/com/aja/lcm/InitializeAPIEM.do")
	public  ModelAndView InitializeAPIEM(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
		
		ModelAndView modelAndView = new ModelAndView();
			try {
				isObject = false;
				commentLmsVoList = null;
				isCommit = false;
				
				APIErrorManager mLMSErrorManager = new APIErrorManager(2);
				DMBean = new SessionDMInstance();
				DMBean.setAPIEM(mLMSErrorManager);
			} catch (Exception e) {
				String message = "";
				StackTraceElement[] el = e.getStackTrace();
				for (int i = 0; i < el.length; i++) {
					message += el[i].toString() + "\r\n";
				}
				System.out.println("Exception ::: " + message);
				throw new Exception(message);
			}

		modelAndView.addObject("true");
		modelAndView.setViewName("jsonView");
		return modelAndView;
	}	
	
	/**
	 * 학습시작
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/com/aja/lcm/Initialize.do")
	public  ModelAndView Initialize(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
		ModelAndView modelAndView = new ModelAndView();
		  
		SCODataManager SCODM = null;
		
		HttpSession session = request.getSession();
		SeqActivityTree activityTree = (SeqActivityTree) session.getAttribute("mSeqActivityTree");
		ADLLaunch mlaunch = (ADLLaunch) session.getAttribute("mlaunch");

		String iUserName   = commandMap.get("learnerName").toString();
		String itemId      = commandMap.get("itemId").toString();
		int orgSeq         = Integer.parseInt(commandMap.get("orgSeq").toString());
		String courseMap   = commandMap.get("courseMapSeq").toString();
		String user_ip     = request.getRemoteAddr();
		String nowYear   = commandMap.get("nowYear").toString();

		if (null != iUserName && !"".equals(iUserName)) {
			//iUserName = learner_name;
		} else {
			iUserName = (String) commandMap.get("suserName");
		}
		
		DMBean.setMActivityID(itemId);
		DMBean.setMSCOID(itemId);
		DMBean.setOrgSeq(orgSeq);
		DMBean.setCourse_map(courseMap);
		
		if (activityTree != null && mlaunch != null) {
			SeqActivity act = activityTree.getActivity(itemId);
			if (act != null) {
				ADLTracking track = act.getMCurTracking();
				if (track != null) {
					DMBean.setMNumAttempt(String.valueOf(track.mAttempt));
				}
			}
			
			DMBean.setMUserID(activityTree.getLearnerID());
		}
		DMBean.setMUserName(iUserName);
		DMBean.setSCODM(getSCODM(DMBean.getMUserID(), DMBean.getMSCOID(),courseMap, DMBean.getMActivityID(), orgSeq, nowYear));
		if (activityTree != null)
			DMBean.setMSeqActivityTree(activityTree);
		DMBean.setStartTime(System.currentTimeMillis());

		if (!isObject) {
			//RTEDH.initializeStateData(DMBean, DMBean.getMNumAttempt(),DMBean.getMSCOID(), orgSeq, itemId);
			SCODM = DMBean.getSCODM();
			SCODM.addDM(1);
			SCODM.addDM(2);
			initSCOData(SCODM,commandMap);
		}
		SCODM = DMBean.getSCODM();
		ADLSequencer mSequencer = new ADLSequencer();
		ADLValidRequests mState = new ADLValidRequests();
		mSequencer.setActivityTree(DMBean.getMSeqActivityTree());
		mSequencer.getValidRequests(mState);
		Vector mStatusVector = new Vector();
		mStatusVector = mSequencer.getObjStatusSet(DMBean.getMSCOID());
		ADLObjStatus mObjStatus = new ADLObjStatus();
		String obj = new String();
		if (mStatusVector != null) {
			if (!isObject) {
				for (int i = 0; i < mStatusVector.size(); i++) {
					mObjStatus = (ADLObjStatus) mStatusVector.get(i);
					obj = "cmi.objectives." + i + ".id";
					DMInterface.processSetValue(obj, mObjStatus.mObjID,true, SCODM);
					obj = "cmi.objectives." + i + ".success_status";
					
					if (mObjStatus.mStatus.equalsIgnoreCase("satisfied"))
						DMInterface.processSetValue(obj, "passed", true,SCODM);
					else if (mObjStatus.mStatus.equalsIgnoreCase("notSatisfied"))
						DMInterface.processSetValue(obj, "failed", true,SCODM);
					obj = "cmi.objectives." + i + ".score.scaled";
					
					if (mObjStatus.mHasMeasure) {
						Double norm = new Double(mObjStatus.mMeasure);
						DMInterface.processSetValue(obj, norm.toString(),true, SCODM);
					}
				}

			}
		}
		DMBean.setSCODM(SCODM);
		DMBean.setStartTime(System.currentTimeMillis());
		DMBean.setMValidRequests(mState);
		DMBean.setUser_ip(user_ip);
		isObject = false;
		
		//String save = commandMap.get("save").toString();
		//String reqApplySeq = commandMap.get("reqApplySeq").toString();
		String save = "";
		String reqApplySeq = "";
		
		LmsStudyLogParamInf logInf = null;
		
		
		
		this.InitCMIDB(DMBean, save, 1, 60, reqApplySeq, orgSeq, logInf,nowYear);
		/**
		// 학습시작시 학습데이터를 DB에 넣는다.
		CMIdbHandler cmIdbHandler = new CMIdbHandler();
		// if (save.equals("Y")) {
		cmIdbHandler.InitCMIDB(DMBean, save, 1, 60, reqApplySeq, org_seq,logInf);
		// }


        **/
		
		
		// if (save.equals("Y")) {
		//String session_key = (String) session.getId();
		//insertRteLog(session_key, "Initialize");
		// }
		
		session.setAttribute("save", save);
		session.setAttribute("ECM_SCODM", SCODM);
		//session.setAttribute("DMBean", DMBean);
		
		//modelAndView.addAllObjects("true");
		Map resultMap = new HashMap();
		resultMap.put("result", "true");
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");

		return modelAndView;
	
	}
			



	
	
	@RequestMapping(value="/com/aja/lcm/setLastError.do")
	public  ModelAndView setLastError(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
		
		String result = "";
	    ModelAndView modelAndView = new ModelAndView();
		int errorCode = Integer.parseInt(commandMap.get("errorCode").toString());
		APIErrorManager mLMSErrorManager = null;
		if (DMBean == null || DMBean.getAPIEM() == null) {
			result = "";
		} else {
			mLMSErrorManager = DMBean.getAPIEM();
			mLMSErrorManager.setCurrentErrorCode(errorCode);
			DMBean.setAPIEM(mLMSErrorManager);
			result= "";
		}
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	
	}
	
	


	/**
	 * DWR 오류 get
	 * 
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/com/aja/lcm/getLastError.do")
	public  ModelAndView getLastError(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
	    String result = "";
	    ModelAndView modelAndView = new ModelAndView();
	    
		APIErrorManager mLMSErrorManager = null;
		HttpSession session = request.getSession();
		//SessionDMInstance DMBean = (SessionDMInstance) session.getAttribute("DMBean");
		if (DMBean == null || DMBean.getAPIEM() == null) {
			result = "false";
			
		} else {
			mLMSErrorManager = DMBean.getAPIEM();
			result = String.valueOf(mLMSErrorManager.getCurrentErrorCode());
		}
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	
	}
	
	/**
	 * DWR 오류 get
	 * 
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/com/aja/lcm/getErrorString.do")
	
	public  ModelAndView getErrorString(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
	    ModelAndView modelAndView = new ModelAndView();
	    String result = "";
	    String errorCode = commandMap.get("errorCode").toString();
		APIErrorManager mLMSErrorManager = null;
		HttpSession session = request.getSession();
		//SessionDMInstance DMBean = (SessionDMInstance) session.getAttribute("DMBean");
		if (DMBean == null || DMBean.getAPIEM() == null) {
			result = "";
		} else {
			mLMSErrorManager = DMBean.getAPIEM();
			result = String.valueOf(mLMSErrorManager.getErrorDescription(errorCode));
		}
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	
	}
	
	
	/**
	 * errorcode get
	 * 
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping(value="/com/aja/lcm/getDiagnostic.do")
	
	public  ModelAndView getDiagnostic(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
	    ModelAndView modelAndView = new ModelAndView();
	    String result = "";
	    String errorCode = commandMap.get("errorCode").toString();
		APIErrorManager mLMSErrorManager = null;
		HttpSession session = request.getSession();
		SessionDMInstance DMBean = (SessionDMInstance) session.getAttribute("DMBean");
		if (DMBean == null || DMBean.getAPIEM() == null) {
			result = "";
		} else {
			mLMSErrorManager = DMBean.getAPIEM();
			result = String.valueOf(mLMSErrorManager.getErrorDiagnostic(errorCode));
		}
		Map resultMap = new HashMap();
		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	}

	/**
	 * SCODM get
	 * 
	 * @return SCODataManager SCODM
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
    public SCODataManager getSCODM(String userId, String SCOID,String course_map, String item_id, int org_seq, String nowYear) throws Exception {
		
		Map<String,Object> commandMap = new HashMap();
		commandMap.put("learnerId", userId);
		commandMap.put("courseMapSeq", course_map);
		commandMap.put("orgSeq", org_seq);
		commandMap.put("itemId", SCOID);
		commandMap.put("nowYear", nowYear);
		
		
         Map output = lcmsCmiObjectinfoService.selectLcmsCmiObjectinfo(commandMap);
		 LcmsCmiObjectinfo objVo = new LcmsCmiObjectinfo();
		 LcmsScormModel.bind(output,objVo); 

		 
		SCODataManager SCODM = new SCODataManager();
		if (objVo != null && output != null) {
			// SCODM = (SCODataManager) scoDmVO.getScodm();

			// Add a SCORM 2004 Data Model
			SCODM.addDM(DMFactory.DM_SCORM_2004);
			// Add a SCORM 2004 Nav Data Model
			SCODM.addDM(DMFactory.DM_SCORM_NAV);

			if (objVo.getLearnerId() != null) {
				DMInterface.processSetValue("cmi.learner_id", objVo.getLearnerId(), true, SCODM);
			}
			if (objVo.getLearnerName() != null) {
				DMInterface.processSetValue("cmi.learner_name", objVo.getLearnerName(), true, SCODM);
			}
			if (objVo.getCompletionStatus() != null) {
				DMInterface.processSetValue("cmi.completion_status", objVo.getCompletionStatus(), true, SCODM);
			}
			if (objVo.getCompletionThreshold() != null) {
				DMInterface.processSetValue("cmi.completion_threshold", objVo.getCompletionThreshold(), true, SCODM);
			}
			if (objVo.getCredit() != null) {
				DMInterface.processSetValue("cmi.credit", objVo.getCredit(),true, SCODM);
			}
			if (objVo.getEntryInfo() != null) {
				DMInterface.processSetValue("cmi.entry", objVo.getEntryInfo(),true, SCODM);
			} else {
				DMInterface.processSetValue("cmi.entry", objVo.getEntryInfo(),true, SCODM);
			}
			if (objVo.getLaunchData() != null) {
				DMInterface.processSetValue("cmi.launch_data", objVo.getLaunchData(), true, SCODM);
			}
			if (objVo.getExitStatus() != null) {
				DMInterface.processSetValue("cmi.exit", objVo.getExitStatus(),true, SCODM);
			} else {
				DMInterface.processSetValue("cmi.exit", "", true, SCODM);
			}
			if (objVo.getLocation() != null) {
				DMInterface.processSetValue("cmi.location",objVo.getLocation(), true, SCODM);
			}
			if (objVo.getMaxTimeAllowed() != null) {
				DMInterface.processSetValue("cmi.max_time_allowed", objVo.getMaxTimeAllowed(), true, SCODM);
			}
			if (objVo.getModeInfo() != null) {
				DMInterface.processSetValue("cmi.mode", objVo.getModeInfo(),true, SCODM);
			}
			if (objVo.getProgressMeasure() != null) {
				DMInterface.processSetValue("cmi.progress_measure", objVo.getProgressMeasure(), true, SCODM);
			}
			if (objVo.getScaledPassingScore() != null) {
				DMInterface.processSetValue("cmi.scaled_passing_score", objVo.getScaledPassingScore(), true, SCODM);
			}
			if (objVo.getScoreScaled() != null) {
				DMInterface.processSetValue("cmi.score.scaled", objVo.getScoreScaled(), true, SCODM);
			}
			if (objVo.getScoreRaw() != null) {
				DMInterface.processSetValue("cmi.score.raw", objVo.getScoreRaw(), true, SCODM);
			}
			if (objVo.getScoreMax() != null) {
				DMInterface.processSetValue("cmi.score.max", objVo.getScoreMax(), true, SCODM);
			}
			if (objVo.getScoreMin() != null) {
				DMInterface.processSetValue("cmi.score.min", objVo.getScoreMin(), true, SCODM);
			}
			if (objVo.getSuccessStatus() != null) {
				DMInterface.processSetValue("cmi.success_status", objVo.getSuccessStatus(), true, SCODM);
			}
			if (objVo.getTimeLimitAction() != null) {
				DMInterface.processSetValue("cmi.time_limit_action", objVo.getTimeLimitAction(), true, SCODM);
			}
			if (objVo.getSuspendData() != null) {
				DMInterface.processSetValue("cmi.suspend_data", objVo.getSuspendData(), true, SCODM);
			}

			if (objVo.getTotalTime() != null) {
				DMInterface.processSetValue("cmi.total_time", objVo.getTotalTime(), true, SCODM);
			}
			if (objVo.getLearnerPreferenceAudioLevel() != null) {
				DMInterface.processSetValue("cmi.learner_preference.audio_level", objVo.getLearnerPreferenceAudioLevel(), true,SCODM);
			}
			if (objVo.getLearnerPreferenceAudioCapti() != null) {
				DMInterface.processSetValue("cmi.learner_preference.audio_captioning", objVo.getLearnerPreferenceAudioCapti(), true,SCODM);
			}
			if (objVo.getLearnerPreferenceDeliverySp() != null) {
				DMInterface.processSetValue("cmi.learner_preference.delivery_speed", objVo.getLearnerPreferenceDeliverySp(), true,SCODM);
			}
			if (objVo.getLearnerPreferenceLanguage() != null) {
				DMInterface.processSetValue("cmi.learner_preference.language",objVo.getLearnerPreferenceLanguage(), true, SCODM);
			}
			if (objVo.getAttempt() > 0) {
				DMInterface.processSetValue("cmi.attempt", String.valueOf(objVo.getAttempt()), true, SCODM);
			}
			DMBean.setProgress_measure(objVo.getProgressMeasure());
			DMBean.setTotal_time(objVo.getTotalTime());
			DMBean.setAttempt(objVo.getAttempt());
			DMInterface.processSetValue("cmi.completion_status", objVo.getCompletionStatus(), true, SCODM);

			DMInterface.processSetValue("cmi._version", "1.0", true, SCODM);

			List list = lcmsCmiObjectivesService.selectLcmsCmiObjectivesList(commandMap);
			
			LcmsCmiObjectives[] cmiObjVo  =  new LcmsCmiObjectives[list.size()];
			if(list.size() != 0){
				for(int i = 0 ; i > list.size(); i++){
					cmiObjVo[i] = new  LcmsCmiObjectives();
					Map output1 = (Map)list.get(i);
					LcmsScormModel.bind(output1,cmiObjVo[i]); 
					
					if (cmiObjVo[i].getObjectivesId() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".id", cmiObjVo[i].getObjectivesId(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesScoreScaled() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".score.scaled", cmiObjVo[i].getObjectivesScoreScaled(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesScoreRaw() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".score.raw", cmiObjVo[i].getObjectivesScoreRaw(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesScoreMax() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".score.max", cmiObjVo[i].getObjectivesScoreMax(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesScoreMin() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".score.min", cmiObjVo[i].getObjectivesScoreMin(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesSuccessStatus() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".success_status", cmiObjVo[i].getObjectivesSuccessStatus(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesCompletionStatus() != null) {
						DMInterface.processSetValue("cmi.objectives." + i + ".completion_status", cmiObjVo[i].getObjectivesCompletionStatus(),true, SCODM);
					}
	
					if (cmiObjVo[i].getObjectivesProgressMeasure() != null) {
						DMInterface.processSetValue("cmi.objectives." + i+ ".progress_measure", cmiObjVo[i].getObjectivesProgressMeasure(), true, SCODM);
					}
					if (cmiObjVo[i].getObjectivesDescription() != null) {
						DMInterface.processSetValue("cmi.objectives." + i+ ".description", cmiObjVo[i].getObjectivesDescription(), true, SCODM);
					}
				}
			}
			
			
			
			List list1 = lcmsCmiCommentfromlmsService.selectLcmsCmiCommentfromlmsList(commandMap);

			commentLmsVoList =  new LcmsCmiCommentfromlms[list1.size()];
			if(list1.size() != 0){
				for(int i = 0 ; i > list1.size(); i++){
					commentLmsVoList[i] = new  LcmsCmiCommentfromlms();
					Map output1 = (Map)list1.get(i);
					LcmsScormModel.bind(output1,commentLmsVoList[i]); 
					
					if (commentLmsVoList[i].getCommentsLmsComment() != null) {
						DMInterface.processSetValue("cmi.comments_from_lms."+ i + ".comment", commentLmsVoList[i].getCommentsLmsComment(), true, SCODM);
					}
					if (commentLmsVoList[i].getCommentsLmsLocation() != null) {
						DMInterface.processSetValue("cmi.comments_from_lms."+ i + ".location", commentLmsVoList[i].getCommentsLmsLocation(), true, SCODM);
					}
					if (commentLmsVoList[i].getCommentsLmsTimestamp() != null) {
						DMInterface.processSetValue("cmi.comments_from_lms."+ i + ".timestamp", commentLmsVoList[i].getCommentsLmsTimestamp(), true, SCODM);
					}
				}
			}

			LcmsCmiCommentlearner[] commentlearnerVo = null;
			
			List list2 = lcmsCmiCommentlearnerService.selectLcmsCmiCommentlearnerList(commandMap);
			
			
			commentlearnerVo =  new LcmsCmiCommentlearner[list2.size()];
			if(list2.size() != 0){
				for(int i = 0 ; i > list1.size(); i++){
					commentlearnerVo[i] = new  LcmsCmiCommentlearner();
					Map output1 = (Map)list1.get(i);
					LcmsScormModel.bind(output1,commentlearnerVo[i]); 
					
					if (commentlearnerVo[i].getCmtsLearnerCmt() != null) {
						DMInterface.processSetValue("cmi.comments_from_learner." + i + ".comment",commentlearnerVo[i].getCmtsLearnerCmt(),true, SCODM);
					}
					if (commentlearnerVo[i].getCmtsLearnerLocation() != null) {
						DMInterface.processSetValue("cmi.comments_from_learner." + i + ".location",commentlearnerVo[i].getCmtsLearnerLocation(),true, SCODM);
					}
					if (commentlearnerVo[i].getCmtsLearnerTimestamp() != null) {
						DMInterface.processSetValue("cmi.comments_from_learner."+ i + ".timestamp", commentlearnerVo[i].getCmtsLearnerTimestamp(), true,SCODM);
					}
				}
			}

			isObject = true;
		}
		return SCODM;
	}
	
	/**
	 * description: 학습객체 시작 데이터.
	 * @return 
	 * @throws Exception
	 */	
	@SuppressWarnings("unchecked")
	private void initSCOData(SCODataManager ioSCOData,Map<String, Object> commandMap)throws Exception {

		try {
			
	        Map output = new HashMap();         
	        output.putAll((Map)lcmsItemService.selectLcmsItem01(commandMap));  
	        LcmsItem itemVo = new LcmsItem();
	        LcmsScormModel.bind(output,itemVo); 
	        commandMap.put("itemSeq", itemVo.getItemSeq());

	        Map output1 = lcmsScormSequenceService.selectLcmsScormSequenceSeqIdxNum(commandMap);

	        commandMap.put("seqIdxNum", output1.get("seqIdxNum"));

	        Map output2 = lcmsSeqObjectivesService.selectLcmsSeqObjectives01(commandMap);  
	        LcmsSeqObjectives seqObjectiveVO = new LcmsSeqObjectives();
	        LcmsScormModel.bind(output2,seqObjectiveVO); 


			String masteryScore = null;
			String dataFromLMS = null;
			String maxTime = null;
			String timeLimitAction = null;
			String completionThreshold = null;
			
			if (seqObjectiveVO != null)
				masteryScore = String.valueOf(seqObjectiveVO.getMinnormalMeasure());
			
			dataFromLMS = itemVo.getDataFromLms();
			maxTime = String.valueOf(itemVo.getItemMaxTime());
			timeLimitAction = itemVo.getItemTlAction();
			completionThreshold = itemVo.getItemThreshold();
			
			String audLev = new String();
			String audCap = new String();
			String delSpd = new String();
			String lang   = new String();
			
			String element = new String();
			element = "cmi.learner_id";
			DMInterface.processSetValue(element, commandMap.get("userId").toString(), true, ioSCOData);
			element = "cmi.learner_name";
			DMInterface.processSetValue(element, commandMap.get("learnerName").toString(), true, ioSCOData);
			element = "cmi.credit";
			DMInterface.processSetValue(element, "credit", true,ioSCOData);
			element = "cmi.mode";
			DMInterface.processSetValue(element, "normal", true,ioSCOData);
			if (dataFromLMS != null && !dataFromLMS.equals("")) {
				element = "cmi.launch_data";
				DMInterface.processSetValue(element, dataFromLMS, true,
						ioSCOData);
			}
			if (masteryScore != null && !masteryScore.equals("")) {
				element = "cmi.scaled_passing_score";
				DMInterface.processSetValue(element, masteryScore, true,ioSCOData);
			}
			if (timeLimitAction != null && !timeLimitAction.equals("")) {
				element = "cmi.time_limit_action";
				DMInterface.processSetValue(element, timeLimitAction,true, ioSCOData);
			}
			if (completionThreshold != null && !completionThreshold.equals("")) {
				element = "cmi.completion_threshold";
				DMInterface.processSetValue(element, completionThreshold,true, ioSCOData);
			}
			if (maxTime != null && !maxTime.equals("")) {
				element = "cmi.max_time_allowed";
				DMInterface.processSetValue(element, maxTime, true,ioSCOData);
			}
			element = "cmi.learner_preference.audio_level";
			DMInterface.processSetValue(element, audLev, true, ioSCOData);
			element = "cmi.learner_preference.audio_captioning";
			DMInterface.processSetValue(element, audCap, true, ioSCOData);
			element = "cmi.learner_preference.delivery_speed";
			DMInterface.processSetValue(element, delSpd, true, ioSCOData);
			element = "cmi.learner_preference.language";
			DMInterface.processSetValue(element, lang, true, ioSCOData);
			DMBean.setSCODM(ioSCOData);

		} catch (Exception ex) {
			throw new Exception(ex);
		}
	}

	
	
	
	
	
	
	/**
	 * CMIObjectInfo insert
	 * @return boolean flag
	 * @throws Exception
	 */
    public boolean InitCMIDB(
    		SessionDMInstance DMBean,
    		String save,
    		int attempt,
    		int sec,
    		String reqApplySeq,
    		int org_seq,
    		LmsStudyLogParamInf logInf,
    		String nowYear
    		)
        throws SQLException, Exception
    {
        boolean flag = false;
        int progresscode = InsertCMIObjectInfo(DMBean, save, attempt, sec, reqApplySeq,org_seq, logInf, nowYear);
        if(progresscode > 0)
        {
            flag = true;
            flag &= InsertObjectives(DMBean, progresscode,nowYear);
        }
        return flag;
    }
	/**
	 * Objectives insert
	 * @return boolean result
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean InsertObjectives(SessionDMInstance DMBean,int progresscode,String nowYear) throws Exception {
		boolean result = true;
		DMProcessingInfo dmInfo = new DMProcessingInfo();
		
		int dmErrorCode = 0;
		
		dmErrorCode = DMInterface.processGetValue("cmi.objectives._count", true, DMBean.getSCODM(), dmInfo);
		
		Integer size = new Integer(dmInfo.mValue);
		int numObjs = size.intValue();
		if (dmErrorCode != DMErrorCodes.NO_ERROR || numObjs == 0) {
			if (numObjs == 0) {
				result = true;
			}
		} else {
			
			try {
				
				Map<String,Object> inputMap = new HashMap();
				
				
				Map<String,Object> delMap = new HashMap();
				
				delMap.put("courseMapSeq", Integer.parseInt(DMBean.getCourse_map()));
				delMap.put("itemId", DMBean.getMSCOID());
				delMap.put("userID", DMBean.getMUserID());
				delMap.put("learnerId", DMBean.getMUserID());
				delMap.put("nowYear", nowYear);
				
				
				inputMap.put("delMap", delMap);
				
				LcmsCmiObjectives[] vo = new LcmsCmiObjectives[numObjs];
				List inputList = new ArrayList();
			
				for (int i = 0; i < numObjs; i++) {
					String objId = null;
					String objScoreScaled = null;
					String objScoreRaw = null;
					String objScoreMax = null;
					String objScoreMin = null;
					String objSuccessStatus = null;
					String objCompletionStatus = null;
					String objProgressMeasure = null;
					String objDescription = null;

					dmErrorCode = DMInterface.processGetValue("cmi.objectives."+ i + ".id", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objId = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives."+ i + ".score.scaled", true, DMBean.getSCODM(),dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objScoreScaled = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".score.raw", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objScoreRaw = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".score.max", true, DMBean.getSCODM(),dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objScoreMax = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".score.min", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objScoreMin = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".success_status", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objSuccessStatus = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".completion_status", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objCompletionStatus = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".progress_measure", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objProgressMeasure = dmInfo.mValue;
					dmErrorCode = DMInterface.processGetValue("cmi.objectives." + i + ".description", true, DMBean.getSCODM(), dmInfo);
					if (dmErrorCode == DMErrorCodes.NO_ERROR)
						objDescription = dmInfo.mValue;

					vo[i] = new LcmsCmiObjectives();
					vo[i].setCourseMapSeq(Integer.parseInt(DMBean.getCourse_map()));
					vo[i].setItemId(DMBean.getMSCOID());
					vo[i].setUserId(DMBean.getMUserID());
					vo[i].setLearnerId(DMBean.getMUserID());
					vo[i].setObjectivesCount(numObjs);
					vo[i].setOrgSeq(DMBean.getOrgSeq());
					vo[i].setObjectivesId(objId);
					vo[i].setObjectivesScoreScaled(objScoreScaled);
					vo[i].setObjectivesScoreRaw(objScoreRaw);
					vo[i].setObjectivesScoreMax(objScoreMax);
					vo[i].setObjectivesScoreMin(objScoreMin);
					vo[i].setObjectivesSuccessStatus(objSuccessStatus);
					vo[i].setObjectivesCompletionStatus(objCompletionStatus);
					vo[i].setObjectivesProgressMeasure(objProgressMeasure);
					vo[i].setObjectivesDescription(objDescription);
					vo[i].setNowYear(nowYear);
					
					inputList.add(vo[i]);
				}

				Object result_ = lcmsCmiObjectivesService.insertLcmsCmiObjectives(inputMap, inputList);

			} catch (Exception e) {
				String message = e.getClass().getName() + " : " + e.getMessage();
				StackTraceElement[] ste = e.getStackTrace();
				for (int i = 0; i < ste.length; i++) {
					message += "\r\n" + ste[i].getFileName() + " : "
							+ ste[i].getMethodName() + " at "
							+ ste[i].getLineNumber();
				}
			}
		}
		return result;
	}
	/**
	 * CMIObjectInfo insert
	 * @return int ret
	 * @throws Exception
	 */
    public int InsertCMIObjectInfo(
    		SessionDMInstance DMBean, String save, int attempt, int sec,String reqApplySeq,int org_seq,LmsStudyLogParamInf logInf,String nowYear)
        throws Exception
    {
        int ret;
        ret = 0;
        String totalTime = "";
        try
        
        {

        	LcmsCmiObjectinfo objectInfoVO = null;
    		Map<String,Object> commandMap = new HashMap();
    		commandMap.put("learnerId", DMBean.getMUserID());
    		commandMap.put("courseMapSeq", DMBean.getCourse_map());
    		commandMap.put("orgSeq", DMBean.getOrgSeq());
    		commandMap.put("itemId", DMBean.getMSCOID());
    		commandMap.put("nowYear", nowYear);

            Map output = lcmsCmiObjectinfoService.selectLcmsCmiObjectinfo(commandMap);
    		
    		if(output != null){
    			objectInfoVO = new LcmsCmiObjectinfo();
    			LcmsScormModel.bind(output,objectInfoVO); 
    		}
    		
            boolean isObjectInfo = false;

            DMProcessingInfo dmInfo = new DMProcessingInfo();
            int dmErrorCode = 0;
            
            if(objectInfoVO != null)
            {
                isObjectInfo = true;
                dmErrorCode = DMInterface.processSetValue("cmi.suspend_data", objectInfoVO.getSuspendData(), true, DMBean.getSCODM());
                totalTime = objectInfoVO.getTotalTime();
            } else {
                objectInfoVO = new LcmsCmiObjectinfo();
                
                objectInfoVO.setCourseMapSeq(Integer.parseInt(DMBean.getCourse_map()));
                objectInfoVO.setOrgSeq(DMBean.getOrgSeq());
                objectInfoVO.setLearnerId(DMBean.getMUserID());
                objectInfoVO.setItemId(DMBean.getMSCOID()); 
                
                objectInfoVO.setLearnerName(DMBean.getMUserName());
                objectInfoVO.setUserId(DMBean.getMUserID());
            }
            dmErrorCode = DMInterface.processGetValue("cmi.completion_status", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setCompletionStatus(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.completion_threshold", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setCompletionThreshold(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.credit", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setCredit(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.entry", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setEntryInfo(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.exit", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setExitStatus(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.location", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setLocation(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.max_time_allowed", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setMaxTimeAllowed(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.mode", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setModeInfo(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.progress_measure", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0){
                objectInfoVO.setProgressMeasure(dmInfo.mValue);
                int prValue=0;
                try{
                	prValue=(int)(Float.parseFloat(dmInfo.mValue) * 100);
                }catch(NumberFormatException e){
                	prValue=0;
                }
                if(logInf!=null) logInf.setProgress_measure(String.valueOf(prValue));
            }
            dmErrorCode = DMInterface.processGetValue("cmi.scaled_passing_score", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setScaledPassingScore(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.score.scaled", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setScoreScaled(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.score.raw", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setScoreRaw(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.score.max", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setScoreMax(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.score.min", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setScoreMin(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.success_status", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setSuccessStatus(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.time_limit_action", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setTimeLimitAction(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.suspend_data", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setSuspendData(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.total_time", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0){
                objectInfoVO.setTotalTime(dmInfo.mValue);
                if(logInf!=null)  logInf.setTotal_time(this.getStudyTime(dmInfo.mValue));
            }
            dmErrorCode = DMInterface.processGetValue("cmi.learner_preference.audio_level", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setLearnerPreferenceAudioLevel(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.learner_preference.audio_captioning", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setLearnerPreferenceAudioCapti(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.learner_preference.delivery_speed", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setLearnerPreferenceDeliverySp(dmInfo.mValue);
            dmErrorCode = DMInterface.processGetValue("cmi.learner_preference.language", true, DMBean.getSCODM(), dmInfo);
            if(dmErrorCode == 0)
                objectInfoVO.setLearnerPreferenceLanguage(dmInfo.mValue);
            
            //학습시작시만 학습횟수를 늘여준다.
            objectInfoVO.setAttempt(objectInfoVO.getAttempt() + attempt);
            
            
            //과정기수의 학습년도를 넣어준다   요넘이 년도별 학습 테이블을  결정해준다.   
            objectInfoVO.setNowYear(nowYear);
            
            if(logInf!=null)  logInf.setAttempt(objectInfoVO.getAttempt());
            if(isObjectInfo){
            	if(attempt == 1){
            		objectInfoVO.setTotalTime(totalTime);
            		 if(logInf!=null)  logInf.setTotal_time(this.getStudyTime(totalTime));dmErrorCode = DMInterface.processSetValue("cmi.total_time",totalTime, true, DMBean.getSCODM());
            	}
                ret = lcmsCmiObjectinfoService.updateLcmsCmiObjectinfo(objectInfoVO);
            }else{
            	if(sec == 60){
	                dmErrorCode = DMInterface.processGetValue("cmi.total_time", true, DMBean.getSCODM(), dmInfo);
	                if(dmErrorCode == 0)
            		objectInfoVO.setTotalTime(calcTime(60L));
	                if(logInf!=null)  logInf.setTotal_time(this.getStudyTime(calcTime(60L)));
            		Object ret1 = lcmsCmiObjectinfoService.insertLcmsCmiObjectinfo(objectInfoVO);
            		ret = 1; 
            	}
            } 
            
            /** 
             * CMIDBHandler.java  363 줄   학습 진도 저장 
            Constants.LOGGER_CLASS_INF.onCommit(con,logInf);
            if(save.equals("Y")){
            	 if(logInf!=null) { 
            		logInf.setItem_id(DMBean.getMSCOID());
            	 	logInf.setOrg_seq(DMBean.getOrgSeq());
            	 	Constants.LOGGER_CLASS_INF.onCommit(con,logInf);
            	 }
            }
            **/

    	}catch(Exception e){
			e.printStackTrace();
			throw e;	
		}
        return ret;
    }
    
	/**
	 * lms 진도상태 저장
	 * @return 
	 * @throws Exception
	 */ 
    
    /**
	public void LmsProgressState(HashMap map)
		throws Exception {
		VOMapper dao = new VOMapper();
		try {			
			queryid = "lcms.study.LMS_LG_PROGRESS_STATE_SELECT";//+((String)map.get("lms_code"));
			sql = sqlLoader.getQuery(queryid);
			int cnt = dao.getIntegerValue(sql, map);
			if(cnt>0){
				queryid = "lcms.study.LMS_LG_PROGRESS_STATE_UPDATE";//+((String)map.get("lms_code"));
				sql = sqlLoader.getQuery(queryid);
				dao.executeUpdate(sql, map);
				
			}else{
				queryid = "lcms.study.LMS_LG_PROGRESS_STATE_INSERT";//+((String)map.get("lms_code"));
				sql = sqlLoader.getQuery(queryid);
				dao.executeUpdate(sql, map);				
			}	
		}catch(Exception e){
			e.printStackTrace();
			throw e;	
		}
	}    
    **/
    
	/**
	 * 시간데이터 PT0H0M0S 로변환
	 * @return 
	 * @throws Exception
	 */ 
	private String calcTime(long sessionTime) {
		String ret = null;
		long hh = 0L;
		long mm = 0L;
		long ss = 0L;
		long tTime = hh * 3600L + mm * 60L + ss + sessionTime;
		int h = (int) (tTime / 3600L);
		int m = (int) ((tTime % 3600L) / 60L);
		int s = (int) (tTime % 3600L % 60L);
		ret = "PT" + h + "H" + m + "M" + s + "S";
		return ret;
	}
	
	
	/**
	 * description: 시간데이터를 00H00M00S로 변경후 리턴.
	 * @return String rets
	 * @throws Exception
	 */	
	public static int getStudyTime(String time){
		try{
			int rets = 0;		
			int h = Integer.parseInt(time.substring(2, time.indexOf("H")));
			int m = Integer.parseInt(time.substring(time.indexOf("H")+1, time.indexOf("M")));
			int s = Integer.parseInt(time.substring(time.indexOf("M")+1, time.indexOf("S")));
	
			rets = h*3600+m*60+s;
			
			return rets;	
		}catch(Exception e){
			return 0;
		}
	} 
	
	
	/**
	 * cmicode data get
	 * 
	 * @return String
	 * @throws Exception
	 */
	
	@RequestMapping(value="/com/aja/lcm/getValue.do")
	public  ModelAndView getValue(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
		ModelAndView modelAndView = new ModelAndView();
		
		Map returnData = new HashMap();
		
		String cmiCode ="";
		
		cmiCode = commandMap.get("cmiCode").toString();

		SCODataManager SCODM = null;
		String result = "";
		APIErrorManager mLMSErrorManager = null;
		String str = "cmi.comments_from_lms.";
		if (cmiCode.indexOf(str) != -1) {
			if (commentLmsVoList != null) {
				String realCode = cmiCode.substring(str.length());
				if (realCode.equals("_count")) {
					result = String.valueOf(commentLmsVoList.length);
				} else {
					String num = realCode.substring(0, 1);
					String value = realCode.substring(2, realCode.length());
					if (value.equals("comment"))
						result = commentLmsVoList[Integer.parseInt(num)].getCommentsLmsComment();
					else if (value.equals("location"))
						result = commentLmsVoList[Integer.parseInt(num)].getCommentsLmsComment();
					else if (value.equals("timestamp"))
						result = commentLmsVoList[Integer.parseInt(num)].getCommentsLmsComment();
				}
			}
		} else {
			DMProcessingInfo dmInfo = new DMProcessingInfo();
			SCODM = DMBean.getSCODM();
			mLMSErrorManager = DMBean.getAPIEM();
			int dmErrorCode = 0;
			mLMSErrorManager.clearCurrentErrorCode();
			dmErrorCode = DMInterface.processGetValue(cmiCode, false, SCODM,dmInfo);
			mLMSErrorManager.setCurrentErrorCode(dmErrorCode);
			if (dmErrorCode == 0) {
				result = dmInfo.mValue;
			}

			DMBean.setSCODM(SCODM);
			DMBean.setAPIEM(mLMSErrorManager);
		}
		
		returnData.put("result", result);
		
		modelAndView.addAllObjects(returnData);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	}
	
	/**
	 * cmicode data set
	 * 
	 * @return String
	 * @throws Exception
	 */
	
	@RequestMapping(value="/com/aja/lcm/setValue.do")
	public  ModelAndView setValue(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {	
		
		ModelAndView modelAndView = new ModelAndView();
	
		
		//public String setValue(String cmiCode, String value) throws Exception {
		String cmiCode = commandMap.get("cmiCode").toString(); 
		String value   = commandMap.get("value").toString();
		Map returnData = new HashMap();
		
		SCODataManager SCODM = null;
		String result = "false";
		APIErrorManager mLMSErrorManager = null;
		DMProcessingInfo dmInfo = new DMProcessingInfo();
		SCODM = DMBean.getSCODM();
		mLMSErrorManager = DMBean.getAPIEM();
		int dmErrorCode = 0;
		dmErrorCode = DMInterface.processSetValue(cmiCode, value, false, SCODM);
		if (dmErrorCode == 0 && "cmi.score.scaled".equals(cmiCode)) {
			dmErrorCode = DMInterface.processGetValue("cmi.scaled_passing_score", true, true, SCODM, dmInfo);
			if (dmInfo.mValue == null)
				dmInfo.mValue = "0";
			double scoreScaled = Double.parseDouble(value);
			double ScaledPassingScore = "".equals(dmInfo.mValue) ? 0 : Double
					.parseDouble(dmInfo.mValue);
			String successStatus = "failed";
			if (scoreScaled >= ScaledPassingScore)
				successStatus = "passed";
			dmErrorCode = DMInterface.processSetValue("cmi.success_status",successStatus, true, SCODM);
			dmErrorCode = DMInterface.processGetValue("cmi.success_status",true, SCODM, dmInfo);
			if (dmErrorCode == 0)
				successStatus = dmInfo.mValue;
			dmErrorCode = DMInterface.processGetValue("cmi.objectives._count", true, SCODM, dmInfo);
			if (dmInfo.mValue == null)
				dmInfo.mValue = "0";
			Integer.parseInt(dmInfo.mValue);
		} else if (dmErrorCode == 0 && "cmi.progress_measure".equals(cmiCode))
			if ("1".equals(value) || "1.0".equals(value)) {
				dmErrorCode = DMInterface.processSetValue("cmi.completion_status", "completed", true, SCODM);
			} else {
				dmErrorCode = DMInterface.processGetValue("cmi.completion_threshold", true, true, SCODM, dmInfo);
				String threshold = dmInfo.mValue;
				if (threshold != null && !"".equals(threshold)) {
					double th = Double.parseDouble(threshold);
					double val = Double.parseDouble(value);
					if (th > val)
						dmErrorCode = DMInterface.processSetValue("cmi.completion_status", "incomplete", true,SCODM);
					else
						dmErrorCode = DMInterface.processSetValue("cmi.completion_status", "completed", true,SCODM);
				}
			}
		mLMSErrorManager.setCurrentErrorCode(dmErrorCode);
		if (mLMSErrorManager.getCurrentErrorCode().equals("0"))
			result = "true";
		MessageCollection MC = MessageCollection.getInstance();
		MC.clear();
		DMBean.setSCODM(SCODM);
		DMBean.setAPIEM(mLMSErrorManager);
		//return result;
		
		returnData.put("result", result);
		modelAndView.addAllObjects(returnData);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	}
	
	/**
	 * 학습종료
	 * 
	 * @return String[] returnData
	 * @throws Exception
	 */
	
	
	@RequestMapping(value="/com/aja/lcm/Terminate.do")
	public  ModelAndView Terminate(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {
		ModelAndView modelAndView = new ModelAndView();
		Map returnData = new HashMap();
		
		Map result = new HashMap();
		
		Object resultObject = null;
		try {
			String param = "";
			if(commandMap.get("iparam") != null)
				param = commandMap.get("iparam").toString();
			
			SCODataManager SCODM = null;
			DMProcessingInfo dmInfo = new DMProcessingInfo();
		
			SCODM = DMBean.getSCODM();
			if (SCODM == null){
				result.put("tempEvent", "");
				result.put("isChoice",  "");
				
				resultObject = result;
				returnData.put("result", resultObject);
				
				modelAndView.addAllObjects(returnData);
				modelAndView.setViewName("jsonView");
				return modelAndView;
			} 
			// mLMSErrorManager = DMBean.getAPIEM();
			int dmErrorCode = 0;
			dmErrorCode = DMInterface.processGetValue("cmi.exit", true, true,SCODM, dmInfo);
			String exitValue = dmInfo.mValue;
			String tempEvent = "_none_";
			boolean isChoice = false;
			if (dmErrorCode == 0)
				exitValue = dmInfo.mValue;
			else
				exitValue = new String("");
			
			if (exitValue.equals("logout")) {
				tempEvent = "suspendAll";
			} else {
				SCORM_2004_NAV_DM navDM = (SCORM_2004_NAV_DM) SCODM.getDataModel("adl");
				String event = navDM.getNavRequest();
				if (event != null)
					if (event.equals("3"))
						tempEvent = "next";
					else if (event.equals("4"))
						tempEvent = "previous";
					else if (event.equals("8"))
						tempEvent = "exit";
					else if (event.equals("9"))
						tempEvent = "exitAll";
					else if (event.equals("5"))
						tempEvent = "abandon";
					else if (event.equals("6"))
						tempEvent = "abandonAll";
					else if (event.equals("0")) {
						tempEvent = "_none_";
					} else {
						tempEvent = event;
						isChoice = true;
					}
			}
			result.put("tempEvent", tempEvent);
			result.put("isChoice",  String.valueOf(isChoice));

			dmErrorCode = DMInterface.processGetValue("cmi.exit", true, true,SCODM, dmInfo);
			dmErrorCode = DMInterface.processSetValue("adl.nav.request","_none_", true, SCODM);
			// String.valueOf(insertDM(DMBean));
			//HttpSession session = WebContextFactory.get().getSession();
			
			HttpSession session = request.getSession();
			// String save = (String) session.getAttribute("save");
			// if (save.equals("Y")) {
				//String session_key = session.getId();
				//insertRteLog(session_key, "Terminate");
			// }
			session.removeAttribute("save");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		resultObject = result;
		returnData.put("result", resultObject);
		modelAndView.addAllObjects(returnData);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	}
	
	
	/**
	 * 학습데이터 저장
	 * 
	 * @return String result
	 * @throws Exception
	 */
	/**
	public String Commit(
			String save, 
			String reqApplySeq, 
			String org_seq,
			or.keris.ngedu.web.lcms.api.lms_log.LmsStudyLogParamInf logInf)
			throws Exception {
    **/
	
	@RequestMapping(value="/com/aja/lcm/Commit.do")
	public  ModelAndView Commit(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {
		
		ModelAndView modelAndView = new ModelAndView();
		
		Map returnData = new HashMap();
		
		//String save        = commandMap.get("save").toString();
		//String itemId      = commandMap.get("itemId").toString();
		//String courseMap   = commandMap.get("courseMapSeq").toString();
		//String reqApplySeq = commandMap.get("reqApplySeq").toString();
		
		String save  = "";
		String reqApplySeq = "";
		
		String iUserName   = commandMap.get("learnerName").toString();
		int orgSeq         = Integer.parseInt(commandMap.get("orgSeq").toString());
		String nowYear     = commandMap.get("nowYear").toString();

		
		String result = "false";
		if (DMBean.getCourse_map() == null){
			//return result;
			modelAndView.addObject(result);
			modelAndView.setViewName("jsonView");
			return modelAndView;
		}
			
		try {
			LmsStudyLogParamInf logInf = null; // 현제 활용방안 검토
			HttpSession session = request.getSession();
			SCODataManager SCODM = null;
			APIErrorManager mLMSErrorManager = null;
			String sessionOTime = null;
			SCODM = DMBean.getSCODM();
			int dmErrorCode = 0;
			DMProcessingInfo dmInfo = new DMProcessingInfo();
			mLMSErrorManager = DMBean.getAPIEM();
			long iTime = System.currentTimeMillis() - DMBean.getStartTime();
			DMBean.setStartTime(System.currentTimeMillis());
			iTime /= 1000L;
			int hh = (int) (iTime / 3600L);
			int mm = (int) ((iTime % 3600L) / 60L);
			int ss = (int) (iTime % 3600L % 60L);
			int sec = (hh * 3600) + (mm * 60) + ss;
			String sessionTime = "PT" + hh + "H" + mm + "M" + ss + "S";
			dmErrorCode = DMInterface.processGetValue("cmi.session_time", true,true, SCODM, dmInfo);

			// if (dmErrorCode == 0)
			// sessionOTime = dmInfo.mValue;
			if (sessionOTime != null && !"".equals(sessionOTime)
					&& !"null".equals(sessionOTime)) {
				iTime = 0L;
				if (sessionOTime.indexOf("H") > 0) {
					iTime += Long.parseLong(sessionOTime.substring(sessionOTime.indexOf("T") + 1, sessionOTime.indexOf("H"))) * 3600L;
				} else {
					sessionOTime = sessionOTime.substring(0, sessionOTime.indexOf("T") + 1)+ "0H"+ sessionOTime.substring(sessionOTime.indexOf("T") + 1, sessionOTime.length());
					iTime += 0L;
				}
				if (sessionOTime.indexOf("M") > 0)
					iTime += Long.parseLong(sessionOTime.substring(sessionOTime
							.indexOf("H") + 1, sessionOTime.indexOf("M"))) * 60L;
				else
					iTime += 0L;
				if (sessionOTime.indexOf("S") > 0) {
					iTime += Double.parseDouble(sessionOTime.substring(
							sessionOTime.indexOf("M") + 1, sessionOTime
									.indexOf("S")));
					// Long.parseLong(sessionOTime.substring(sessionOTime
					// .indexOf("M") + 1, sessionOTime.indexOf("S")));
				} else {
					sessionOTime = sessionOTime + "0S";
					iTime += 0L;
				}
			}
			dmErrorCode = DMInterface.processGetValue("cmi.total_time", true,SCODM, dmInfo);
			String totalTime = dmInfo.mValue;
			totalTime = this.calcTime(totalTime, iTime);
			
			//logInf.setSession_time(iTime);
			
			dmErrorCode = DMInterface.processSetValue("cmi.total_time",totalTime, true, SCODM);
			SeqActivityTree mSeqActivityTree = DMBean.getMSeqActivityTree();
			if (mSeqActivityTree == null) {
				//SCORMRteSeq seq = SCORMRteSeq.getInstance();
				LcmsToc lcmsToc  = new LcmsToc();

				commandMap.put("userId",DMBean.getMUserID());
				commandMap.put("Course_map",DMBean.getCourse_map());
				
				lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);  // 학습자의 학습 객체 직렬화를 가져온다. 
				
				//TocVO tocvo = seq.getTocVO(DMBean.getMUserID(), DMBean.getCourse_map());
				DMBean.setMSeqActivityTree(mSeqActivityTree = getSeqActivityTree(commandMap,lcmsToc));
			}
			
			//주석 풀었음
			 mSeqActivityTree.getActivity(DMBean.getMActivityID()).setStudyTime(totalTime);

			String completionStatus = null;
			String SCOEntry = null;
			double normalScore = -1D;
			String masteryStatus = null;
			String score = null;
			int err = 0;
			err = DMInterface.processGetValue("cmi.completion_status", true,SCODM, dmInfo);
			completionStatus = dmInfo.mValue;
			err = DMInterface.processGetValue("cmi.success_status", true,SCODM, dmInfo);
			masteryStatus = dmInfo.mValue;
	
			//주석 풀었음
			 // mSeqActivityTree.getActivity(DMBean.getMActivityID()).setSuccessStatus(masteryStatus);

			err = DMInterface.processGetValue("cmi.entry", true, true, SCODM,dmInfo);
			SCOEntry = dmInfo.mValue;
			err = DMInterface.processGetValue("cmi.score.scaled", true, SCODM,dmInfo);
			if (err == 0)
				score = dmInfo.mValue;
			else
				score = "";
			err = DMInterface.processSetValue("cmi.entry", "resume", true,SCODM);
			if (mSeqActivityTree != null) {
				ADLSequencer theSequencer = new ADLSequencer();
				theSequencer.setActivityTree(mSeqActivityTree);
				// SeqActivity act = mSeqActivityTree.getActivity(DMBean.getMActivityID());
				mSeqActivityTree.getActivity(DMBean.getMActivityID());
				err = DMInterface.processGetValue("cmi.objectives._count",true, SCODM, dmInfo);
				Integer size = new Integer(dmInfo.mValue);
				int numObjs = size.intValue();
				for (int i = 0; i < numObjs; i++) {
					String objID = new String("");
					String objMS = new String("");
					String objScore = new String("");
					String obj = new String("");
					obj = "cmi.objectives." + i + ".id";
					err = DMInterface.processGetValue(obj, true, SCODM, dmInfo);
					objID = dmInfo.mValue;
					obj = "cmi.objectives." + i + ".success_status";
					err = DMInterface.processGetValue(obj, true, SCODM, dmInfo);
					objMS = dmInfo.mValue;
					if (objMS.equals("passed"))
						theSequencer.setAttemptObjSatisfied(DMBean
								.getMActivityID(), objID, "satisfied");
					else if (objMS.equals("failed"))
						theSequencer.setAttemptObjSatisfied(DMBean.getMActivityID(), objID, "notSatisfied");
					else
						theSequencer.setAttemptObjSatisfied(DMBean.getMActivityID(), objID, "unknown");
					obj = "cmi.objectives." + i + ".score.scaled";
					err = DMInterface.processGetValue(obj, true, SCODM, dmInfo);
					if (err == 0)
						objScore = dmInfo.mValue;
					if (!objScore.equals("") && !objScore.equals("unknown"))
						try {
							normalScore = (new Double(objScore)).doubleValue();
							theSequencer.setAttemptObjMeasure(DMBean.getMActivityID(), objID, normalScore);
						} catch (Exception e) {
							e.printStackTrace();
						}
					else
						theSequencer.clearAttemptObjMeasure(DMBean.getMActivityID(), objID);
				}

				theSequencer.setAttemptProgressStatus(DMBean.getMActivityID(),completionStatus);
				if (SCOEntry.equals("resume"))
					theSequencer.reportSuspension(DMBean.getMActivityID(), true);
				else
					theSequencer.reportSuspension(DMBean.getMActivityID(),false);
				if (masteryStatus.equals("passed"))
					theSequencer.setAttemptObjSatisfied(
							DMBean.getMActivityID(), mPRIMARY_OBJ_ID,"satisfied");
				else if (masteryStatus.equals("failed"))
					theSequencer.setAttemptObjSatisfied(DMBean.getMActivityID(), mPRIMARY_OBJ_ID,"notSatisfied");
				else
					theSequencer.setAttemptObjSatisfied(DMBean.getMActivityID(), mPRIMARY_OBJ_ID, "unknown");
				if (!score.equals("") && !score.equals("unknown"))
					try {
						normalScore = (new Double(score)).doubleValue();
						theSequencer.setAttemptObjMeasure(DMBean.getMActivityID(),mPRIMARY_OBJ_ID, normalScore);
					} catch (Exception e) {
						e.printStackTrace();
					}
				else
					theSequencer.clearAttemptObjMeasure(DMBean.getMActivityID(), mPRIMARY_OBJ_ID);

				if (DMBean.getMValidRequests() != null) {
					theSequencer.getValidRequests(DMBean.getMValidRequests());
					mSeqActivityTree = theSequencer.getActivityTree();
				}
				session.setAttribute("mSeqActivityTree", mSeqActivityTree);
				

				

				LcmsToc lcmsToc  = new LcmsToc();

				commandMap.put("userId",DMBean.getMUserID());
				commandMap.put("courseMapSeq",DMBean.getCourse_map());
				
				lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);  // 학습자의 학습 객체 직렬화를 가져온다. 
		    	if(lcmsToc != null){
					commandMap.put("serializer",mSeqActivityTree);
			    	commandMap.put("idx",lcmsToc.getIdx());
			    	commandMap.put("tocIdx",lcmsToc.getTocIdx());
		    	}
			     
		    	
		    	int  result_update = lcmsTocService.updateLcmsToc(commandMap);

			}
			DMBean.setSCODM(SCODM);
			DMBean.setAPIEM(mLMSErrorManager);
			

			result = String.valueOf(this.InitCMIDB(DMBean, save, 0, sec, reqApplySeq, orgSeq, logInf, nowYear));
		} catch (Exception ee) {
			ee.printStackTrace();
			throw ee;
		}
		//return result;
		returnData.put("result", result);
		modelAndView.addAllObjects(returnData);
		modelAndView.setViewName("jsonView");
		return modelAndView;
	}
	
	/**
	 * 학습시간 데이터 PT0H0M0S형식으로 변환
	 * 
	 * @return String ret
	 * @throws Exception
	 */
	private String calcTime(String totalTime, long sessionTime)
			throws Exception {

		String ret = null;
		try {
			long hh = 0L;
			long mm = 0L;
			long ss = 0L;
			String _totalTime = totalTime;
			if (totalTime == null)
				_totalTime = "PT0H0M0S";
			if (_totalTime.indexOf("H") > 0)
				hh = Long.parseLong(_totalTime.substring(_totalTime
						.indexOf("T") + 1, _totalTime.indexOf("H")));
			if (_totalTime.indexOf("M") > 0)
				mm = Long.parseLong(_totalTime.substring(_totalTime
						.indexOf("H") + 1, _totalTime.indexOf("M")));
			if (_totalTime.indexOf("S") > 0)
				ss = Long.parseLong(_totalTime.substring(_totalTime
						.indexOf("M") + 1, _totalTime.indexOf("S")));
			long tTime = hh * 3600L + mm * 60L + ss + sessionTime;
			int h = (int) (tTime / 3600L);
			int m = (int) ((tTime % 3600L) / 60L);
			int s = (int) (tTime % 3600L % 60L);
			ret = "PT" + h + "H" + m + "M" + s + "S";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	

	public SeqActivityTree getSeqActivityTree(Map<String, Object> commandMap,LcmsToc lcmsToc ) throws Exception {
		
		SeqActivityTree activityTree = null;
		
		LcmsSerializer lcmsSerializer = new LcmsSerializer();
		
		if (lcmsToc != null && lcmsToc.getSerializer() != null) {
			activityTree = (SeqActivityTree) lcmsToc.getSerializer();
		} else {
			
			//getSeqActivityTree(TocVO tocVO, String userId, String course_map) 
			lcmsSerializer =  (LcmsSerializer)lcmsSerializerService.selectLcmsSerializer(commandMap);
			
			if (lcmsSerializer != null) {
				activityTree =  (SeqActivityTree)lcmsSerializer.getSerializer();

	            Vector theGobalObjectiveList = activityTree.getGlobalObjectives();   
	            
	            if(theGobalObjectiveList != null){
	                ADLSeqUtilities.createGlobalObjs(commandMap.get("userId").toString(), null, theGobalObjectiveList);
	            }
		    	commandMap.put("serializer",activityTree);
		    	commandMap.put("idx",lcmsSerializer.getIdx());

		    	boolean result = insertToc(commandMap, lcmsToc);

	        }
			
		}	
		return activityTree;	
	}
	
	public boolean insertToc(Map<String, Object> commandMap,LcmsToc lcmsToc) throws Exception {
		
		boolean result = false;
		
		long serializeIdx = 0;
		
		serializeIdx = Integer.parseInt(commandMap.get("idx").toString());

    	//insertToc
		if(serializeIdx == 0L){
			LcmsSerializer lcmsSerializer = new LcmsSerializer();
			lcmsSerializer =  (LcmsSerializer)lcmsSerializerService.selectLcmsSerializer(commandMap);
			commandMap.put("idx",lcmsSerializer.getIdx());
		}
		
	    if(lcmsToc != null && lcmsToc.getTocIdx() > 0L){

		    	commandMap.put("tocIdx",lcmsToc.getTocIdx());
	    	
	       int   result_update = lcmsTocService.updateLcmsToc(commandMap);
	       
	       if(result_update > 0)
	    	   result = true;
	    } else {
	       Object resert_insert = lcmsTocService.insertLcmsToc(commandMap);
	       
	       if(resert_insert != null)
	    	   result = true;
	    }
		return result;	
	}
}