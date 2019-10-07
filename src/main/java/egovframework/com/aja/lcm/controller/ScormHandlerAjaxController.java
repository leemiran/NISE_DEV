package egovframework.com.aja.lcm.controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.sql.Blob;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;


import org.adl.sequencer.ADLLaunch;
import org.adl.sequencer.ADLSeqUtilities;
import org.adl.sequencer.ADLSequencer;
import org.adl.sequencer.ADLTOC;
import org.adl.sequencer.ADLTracking;
import org.adl.sequencer.ADLValidRequests;
import org.adl.sequencer.SeqActivity;
import org.adl.sequencer.SeqActivityTree;
import org.adl.sequencer.SeqNavRequests;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContextFactory;

import org.mortbay.util.ajax.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.property.EgovPropertyService;


import egovframework.com.aja.lcm.domain.LmsStudyLogParamInf;
import egovframework.com.aja.lcm.domain.SCORMTreeBEAN;
import egovframework.com.aja.lcm.domain.ScormStudyRoomConfigBean;
import egovframework.com.aja.service.CommonAjaxManageService;


import egovframework.com.lcms.len.service.LcmsTocService;
import egovframework.com.lcms.len.domain.LcmsToc;

import egovframework.adm.lcms.cts.model.LcmsScormModel;
import egovframework.adm.lcms.cts.service.LcmsItemResourceService;
import egovframework.adm.lcms.cts.service.LcmsItemService;
import egovframework.adm.lcms.cts.service.LcmsScormSequenceService;
import egovframework.adm.lcms.cts.service.LcmsSerializerService;
import egovframework.adm.lcms.cts.domain.LcmsItemResource;
import egovframework.adm.lcms.cts.domain.LcmsScormSequence;
import egovframework.adm.lcms.cts.domain.LcmsSerializer;

import egovframework.adm.lcms.cts.domain.LcmsItem;

/**
 * 중복체크관련 처리 
 */
@Controller
public class ScormHandlerAjaxController {
   
	/** log */
    protected static final Log LOG = LogFactory.getLog(ScormHandlerAjaxController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	/** AJAX 공통 서비스*/
	@Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
	
	
	private SeqActivityTree mSeqActivityTree;
	private ADLLaunch mlaunch;

	
	/** LcmsToc */
	@Resource(name = "lcmsTocService")
	private LcmsTocService lcmsTocService;
	
	/** LcmsSerializer */
	@Resource(name = "lcmsSerializerService")
	private LcmsSerializerService lcmsSerializerService;
	
	/** LcmsItemService */
	@Resource(name = "lcmsItemService")
	private LcmsItemService lcmsItemService;
	
	
	/** LcmsItemResourceService */
	@Resource(name = "lcmsItemResourceService")
	 private LcmsItemResourceService lcmsItemResourceService;
	
	/** LcmsScormSequenceService */
	@Resource(name = "lcmsScormSequenceService")
	private LcmsScormSequenceService lcmsScormSequenceService;
	
	
	@RequestMapping(value="/com/aja/lcm/scormHandler.do")
	public void scormHandler() {
		mSeqActivityTree = null;
		mlaunch = null;
	}


		
	@RequestMapping(value="/com/aja/lcm/getContentsURL.do")
		public  ModelAndView getContentsURL(
				HttpServletRequest request, 
				HttpServletResponse response, 
				Map<String, Object> commandMap,
				@ModelAttribute("ScormStudyRoomConfigBean") ScormStudyRoomConfigBean scormStudyRoomConfigBean
				
		) throws Exception {
		
		response.setHeader("Cache-Control","no-store");   
    	response.setHeader("Pragma","no-cache");   
    	response.setDateHeader("Expires",0);
    	
    	
		 ModelAndView modelAndView = new ModelAndView();
		 
		 
		     Object result = null;
			 Map resultMap = new HashMap();
		     
				try {
					//System.out.println(">>> GetContentsURL Started....SCO ID : "+ //SCOID);
					scormStudyRoomConfigBean = getContentsURL2(commandMap,request);
					
					if (scormStudyRoomConfigBean.getItemURL() == null || "".equals(scormStudyRoomConfigBean.getItemURL())|| "null".equals(scormStudyRoomConfigBean.getItemURL())){
						throw new NullPointerException();
					} else {
						result = scormStudyRoomConfigBean;
					}
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(">>> Delete TOC ... ");
					int ret = lcmsTocService.deleteLcmsToc(commandMap);
					result = getContentsURL2(commandMap,request);
				}

				resultMap.put("result", result);


			modelAndView.addAllObjects(resultMap);
			modelAndView.setViewName("jsonView");
			
			return modelAndView;
		}
	
	
	/**
	 *  session 초기화
	 * 
	 * @return boolean result
	 * @throws Exception
	 */	
	 @RequestMapping(value="/com/aja/lcm/startClearSession.do")
	public  ModelAndView startClearSession(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {
			
		 	response.setHeader("Cache-Control","no-store");   
	    	response.setHeader("Pragma","no-cache");   
	    	response.setDateHeader("Expires",0);
	    	
	    	
			 ModelAndView modelAndView = new ModelAndView();
			 
		     String result = "true";
			 Map resultMap = new HashMap();

			try {
				if (mSeqActivityTree != null) {
					mSeqActivityTree.clearSessionState();
					mSeqActivityTree = null;
					mlaunch = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			resultMap.put("result", result);
			modelAndView.addAllObjects(resultMap);
			modelAndView.setViewName("jsonView");
			return modelAndView;
	 }
	
	/**
	 * session clear
	 * 
	 * @return boolean result
	 * @throws Exception
	 */
	
	@RequestMapping(value="/com/aja/lcm/clearSession.do")
	public  ModelAndView clearSession(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Map<String, Object> commandMap
	) throws Exception {
		
		response.setHeader("Cache-Control","no-store");   
    	response.setHeader("Pragma","no-cache");   
    	response.setDateHeader("Expires",0);
    	
    	
		 ModelAndView modelAndView = new ModelAndView();
		 
	     String result = "true";
		 Map resultMap = new HashMap();
			try {

				if (mSeqActivityTree != null) {
					mSeqActivityTree.clearSessionState();
					mSeqActivityTree = null;
					mlaunch = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		resultMap.put("result", result);
		modelAndView.addAllObjects(resultMap);
		modelAndView.setViewName("jsonView");
		
		return modelAndView;
	}
	/**
	 * 콘텐츠 경로 get
	 * 
	 * @return SCORMStudyRoomConfigBEAN configBean
	 * @throws Exception
	 */
	private ScormStudyRoomConfigBean getContentsURL2(Map<String, Object> commandMap,HttpServletRequest request)throws Exception {
		
		ScormStudyRoomConfigBean configBean = new ScormStudyRoomConfigBean();
		
		//SCORMRteSeq seq = SCORMRteSeq.getInstance();
		
		LcmsToc lcmsToc  = new LcmsToc();
		
		LcmsSerializer lcmsSerializer = new LcmsSerializer();

		SeqActivityTree activityTree = null;
		
		LcmsItemResource itemResource = new LcmsItemResource();
		
		//TocVO tocVo = null;
		
		String reqSCOID = commandMap.get("scoId").toString();
		String scoId    = commandMap.get("scoId").toString();
		String userId   = commandMap.get("userId").toString();
		String orgSeq   = commandMap.get("orgSeq").toString();
		
		
		boolean flag = false;

		//HttpSession session = WebContextFactory.get().getSession();

		if ("prev".equals(scoId) || "next".equals(scoId)|| "exit".equals(scoId) || "suspend".equals(scoId)) {
			if (mSeqActivityTree == null) {
				lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);
				
				mSeqActivityTree = getSeqActivityTree(commandMap, lcmsToc );

				mlaunch = this.getADLLaunch(mSeqActivityTree, scoId);
			} else {
				mlaunch = this.getADLLaunch(mSeqActivityTree, scoId);
			}
		} else if (mSeqActivityTree == null) {
			lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);  // 학습자의 학습 객체 직렬화를 가져온다. 
			
			mSeqActivityTree = getSeqActivityTree(commandMap, lcmsToc ); // 학습자 객체 직렬화가 없으면 .  컨텐츠 객체 직렬화에서  tree 를 가져오고  학습자 객체 직렬화에 넣는다 


			if (lcmsToc != null) {
				
				mlaunch = this.getADLLaunch(mSeqActivityTree);
				// ADLValidRequests mValidRequests = new ADLValidRequests();
				// mValidRequests = mlaunch.mNavState;
				try {
					reqSCOID = mSeqActivityTree.getMCurActivity().getID();
					
					if (mSeqActivityTree.getMCurActivity().getResourceID() != null && !mSeqActivityTree.getMCurActivity().getResourceID().equals("")) {
						mlaunch = this.getADLLaunch(mSeqActivityTree, reqSCOID);
					} else {
						mlaunch = this.getADLLaunch(mSeqActivityTree, "next");
					}
					flag = true;

				} catch (NullPointerException ne) {
					ADLSequencer sequencer = this.getADLSequencer(mSeqActivityTree);
					mlaunch = sequencer.navigate(SeqNavRequests.NAV_START);			
					
			    	commandMap.put("serializer",mSeqActivityTree);
			    	commandMap.put("idx",0L);
			    	
					lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);
					
					boolean result = insertToc(commandMap, lcmsToc);
					
					mSeqActivityTree = getSeqActivityTree(commandMap, lcmsToc );

					SeqActivity curActivity = mSeqActivityTree.getMCurActivity();
					
					if (curActivity == null) {
						mlaunch = this.getADLLaunch(mSeqActivityTree, reqSCOID);
					} else {
						String resourceId = curActivity.getResourceID();
						if (resourceId != null && !resourceId.equals("")) {
							mlaunch = this.getADLLaunch(mSeqActivityTree,reqSCOID);
						} else {
							mlaunch = this.getADLLaunch(mSeqActivityTree, "next");
						}
					}
					// ne.printStackTrace();
				}

			} else {
				mlaunch = this.getADLLaunch(mSeqActivityTree, reqSCOID);
			}
		} else {
			if (mSeqActivityTree.getMCurActivity() != null && mSeqActivityTree.getMCurActivity().getResourceID() != null && !mSeqActivityTree.getMCurActivity().getResourceID().equals("")) {
				mlaunch = this.getADLLaunch(mSeqActivityTree, reqSCOID);  
			} else {
				mlaunch = this.getADLLaunch(mSeqActivityTree, "next");
			}
			flag = false;
		}
		
		if (mSeqActivityTree == null) {
			LcmsItem itemVo  = null;
			
			if (scoId == null || "".equals(scoId)) {
				 itemVo = (LcmsItem)lcmsItemService.selectLcmsItem(commandMap);
		       
				if (itemVo != null) {
					scoId = itemVo.getItemId();
					commandMap.put("itemId", scoId);
				}
			}
			
			 itemVo = (LcmsItem)lcmsItemService.selectComLenRsrcSeqLcmsItem(commandMap);

			 itemResource = (LcmsItemResource)lcmsItemResourceService.selectComLenLcmsItemResource(commandMap);
			// seq.NonLaunchData(configBean, org_seq, scoId);
			 
			 configBean.setItemURL(encodeURL("/content/" + itemResource.getRsrcBaseDir()));
			return configBean;
		}
		
		mSeqActivityTree.setLearnerID(userId);
		
		
		ADLValidRequests ValidRequests = new ADLValidRequests();
		
		ADLSequencer sequencer = this.getADLSequencer(mSeqActivityTree);
		
		sequencer.getValidRequests(ValidRequests);
		
		SCORMTreeBEAN treeBean[] = this.getTreeBean(ValidRequests, mSeqActivityTree);
		configBean.setTreeBean(treeBean);
		
		// if (!mSeqActivityTree.isScrom())
		// nonScormTreeBEAN = treeBean;

		if (mlaunch.mNavState == null) {

	    	commandMap.put("serializer",mSeqActivityTree);
	    	commandMap.put("idx",0L);
	    	
	    	lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);
		
			boolean result = insertToc(commandMap, lcmsToc);
			lcmsToc = (LcmsToc)lcmsTocService.selectLcmsToc(commandMap);
			mSeqActivityTree = getSeqActivityTree(commandMap, lcmsToc );
			mlaunch = this.getADLLaunch(mSeqActivityTree, reqSCOID);
		}

		SeqActivity currentActivity = mSeqActivityTree.getMCurActivity();
		
		configBean = getButtonType(mlaunch, configBean);


		if (mlaunch.mActivityID == null && currentActivity != null) {
			configBean = this.getLaunchData(mlaunch, configBean, orgSeq,currentActivity.getID(), flag, commandMap);
		} else if (mlaunch.mActivityID != null) {
			configBean = this.getLaunchData(mlaunch, configBean, orgSeq,mlaunch.mActivityID, flag, commandMap);
		} else if (scoId != null && !"".equals(scoId)) {
			configBean = this.getLaunchData(mlaunch, configBean, orgSeq, scoId,flag, commandMap);

		} else {
			for (int i = 0; i < treeBean.length; i++) {
				if (treeBean[i].isSelect()) {
					scoId = treeBean[1].getScoId();
					mlaunch = this.getADLLaunch(mSeqActivityTree, scoId);
					configBean = this.getLaunchData(mlaunch, configBean,orgSeq, scoId, flag,commandMap);
					break;
				}
			}
		}

		if (mlaunch.mSeqNonContent != null) {
			// specialState = mlaunch.mSeqNonContent;
			// specialURL = getSpecialState(mlaunch.mSeqNonContent);
			configBean.setSuspendAllButton("");
		}

		//LmsStudyLogParamInf logInf = (or.keris.ngedu.web.lcms.api.lms_log.LmsStudyLogParamInf) session.getAttribute(or.keris.ngedu.web.lcms.api.common.Constants.LOG_OBJECT_SESSION);
		//logInf.setItem_id(SCOID);
		//configBean.setReqApplySeq(getReq_apply_seq(userId, mlaunch.mActivityID, orgSeq));
		//setSessionActivityTree();
		

		HttpSession session = request.getSession();
		
		session.setAttribute("mSeqActivityTree", mSeqActivityTree);
		session.setAttribute("mlaunch", mlaunch);
		return configBean;
	}
	
	
	/**
	 * SCOID 상태 확인 후 리턴
	 * @return ADLLaunch mlaunch
	 * @throws Exception
	 */
	public ADLLaunch getADLLaunch(SeqActivityTree activityTree, String SCOID) {
		ADLLaunch mlaunch = new ADLLaunch();
		ADLSequencer sequencer = this.getADLSequencer(activityTree);// sequencer 
		if (SCOID == null || SCOID.equals(""))
			mlaunch = sequencer.navigate(1);
		else if (SCOID.equals("prev"))
			mlaunch = sequencer.navigate(4);
		else if (SCOID.equals("next"))
			mlaunch = sequencer.navigate(3);
		else if (SCOID.equals("exit"))
			mlaunch = sequencer.navigate(8);
		else if (SCOID.equals("suspend"))
			mlaunch = sequencer.navigate(7);
		else
			mlaunch = sequencer.navigate(SCOID);
		if (mlaunch.mSeqNonContent != null && (mlaunch.mSeqNonContent.equals("_ENDSESSION_") || mlaunch.mSeqNonContent.equals("_COURSECOMPLETE_")))
			sequencer.clearSeqState();
		return mlaunch;
	}
	
	/**
	 * ADLSequencer get
	 * @return ADLSequencer sequencer
	 * @throws Exception
	 */
	public ADLSequencer getADLSequencer(SeqActivityTree activityTree) {
		ADLSequencer sequencer = new ADLSequencer();
		sequencer.setActivityTree(activityTree);
		return sequencer;
	}
	
	/**
	 * ADLLaunch get
	 * @return ADLLaunch mlaunch
	 * @throws Exception
	 */
	public ADLLaunch getADLLaunch(SeqActivityTree activityTree) {
		ADLLaunch mlaunch = new ADLLaunch();
//		SeqNavRequests mnavRequest = new SeqNavRequests();
		ADLSequencer sequencer = getADLSequencer(activityTree);
		mlaunch = sequencer.navigate(SeqNavRequests.NAV_NONE);
		return mlaunch;
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
			//serializeIdx=getSerializeIdx(course_map);
			LcmsSerializer lcmsSerializer = new LcmsSerializer();
			lcmsSerializer =  (LcmsSerializer)lcmsSerializerService.selectLcmsSerializer(commandMap);
			commandMap.put("idx",lcmsSerializer.getIdx());
		}
		
	    if(lcmsToc != null && lcmsToc.getTocIdx() > 0L){
	    	commandMap.put("tocIdx", lcmsToc.getTocIdx());
	    	
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
	
	public String encodeURL(String url) throws Exception{
		
		String[] urlArray = url.split("/");
		
		String ret="";
		for(int i=0;i<urlArray.length;i++){
			urlArray[i]=URLEncoder.encode(urlArray[i],"utf-8");
			if(i<urlArray.length && urlArray[i].length()>0){
				ret+="/";
			}
			ret+=urlArray[i];
		}

//		System.out.println("SOURCE URL ::::: "+url);		
//		System.out.println("RETURN URL ::::: "+ret);
		return ret;
	}
	
	/**
	 * 학습창 버튼 제어
	 * 
	 * @return SCORMStudyRoomConfigBEAN configBean
	 * @throws Exception
	 */
	public ScormStudyRoomConfigBean getButtonType(ADLLaunch mlaunch,ScormStudyRoomConfigBean configBean) throws Exception {
		synchronized (this) {
			try {
				String prevButton = "<a href='javascript:ContentExecutor.buttonProc(\"prev\");'><img src='"
					//	+ Constants.CONTEXTPATH
						+ "/images/learn/btn_back.gif'  border='0' alt='\uB4A4\uB85C'>";
				String nextButton = "<a href='javascript:ContentExecutor.buttonProc(\"next\");'><img src='"
					//	+ Constants.CONTEXTPATH
						+ "/images/learn/btn_next.gif'  border='0' alt='\uC55E\uC73C\uB85C'>";
				String nextExitButton = "";
				String quitButton = "<a href='javascript:ContentExecutor.quit()';><img src='"
					//	+ Constants.CONTEXTPATH
						+ "/images/learn/btn_end.gif'  border='0' alt='\uC885\uB8CC'>";
				ADLValidRequests mValidRequests = new ADLValidRequests();
				mValidRequests = mlaunch.mNavState;
				configBean.setQuitButton(quitButton);
				if (mValidRequests != null) {
					if (mValidRequests.mPrevious)
						configBean.setPrevButton(prevButton);
					if (mValidRequests.mContinue|| mValidRequests.mContinueExit)
						if (mValidRequests.mContinue)
							configBean.setNextButton(nextButton);
						else
							configBean.setNextButton(nextExitButton);
				}
				
				return configBean;
			} catch (Exception e) {
				String message = "";
				StackTraceElement[] el = e.getStackTrace();
				for (int i = 0; i < el.length; i++) {
					message += el[i].toString() + "\r\n";
				}
				System.out.println("Exception ::: " + message);
				throw new Exception(message);
			}
		}
	}
	
	/**
	 * LaunchData get
	 * @return SCORMStudyRoomConfigBEAN configBean
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ScormStudyRoomConfigBean getLaunchData(ADLLaunch mlaunch,ScormStudyRoomConfigBean configBean, String org_seq, String SCOID, boolean flag,Map<String, Object> commandMap) throws Exception {
		boolean matched = false;
		boolean seqLaunched = false;
		try {
			
			LcmsItem[] lcmsItem = null ;
			LcmsScormSequence[] lcmsScormSequence = null ;
			
			List itemList = lcmsItemService.selectLcmsItemList(commandMap);
			List sequenceList = lcmsScormSequenceService.selectLcmsScormSequenceList(commandMap);
			
			lcmsItem = new LcmsItem[itemList.size()];
			lcmsScormSequence = new LcmsScormSequence[sequenceList.size()];
			
			for(int i=0 ; i < itemList.size() ;i++){
				Map<String,Object> inputItem = (Map)itemList.get(i);
				
				lcmsItem[i] = new LcmsItem();
			    LcmsScormModel.bind(inputItem,lcmsItem[i]); // Map으로 받은  result 를  LcmsItem VO 에 매핑 
			    
				if( lcmsScormSequence != null){
					for (int j = 0; j < sequenceList.size(); j++) {
						Map<String,Object> inputSequence = (Map)sequenceList.get(j);
						lcmsScormSequence[j] = new LcmsScormSequence();
						LcmsScormModel.bind(inputSequence,lcmsScormSequence[j]); 
						
						if (lcmsItem[i].getItemSeq() == lcmsScormSequence[j].getItemSeq()) {
							lcmsItem[i].setNext(lcmsScormSequence[j].getHideUiCon());
							lcmsItem[i].setPrevious(lcmsScormSequence[j].getHideUiPre());
							lcmsItem[i].setAbandon(lcmsScormSequence[j].getHideUiAbd());
							lcmsItem[i].setExit(lcmsScormSequence[j].getHideUiEx());
							break;
						}
					}					
				}				
			}

			if ("prev".equals(SCOID) || "next".equals(SCOID)|| "exit".equals(SCOID) || "suspend".equals(SCOID)) {
				for (int i = 0; i < lcmsItem.length; i++) {
					
					String itemID = lcmsItem[i].getItemId();
					
					if (mlaunch.mActivityID != null&& mlaunch.mActivityID.equals(itemID))
						matched = true;
					else
						matched = false;
					
					if (matched) {
						seqLaunched = true;
						if (mlaunch.mActivityID != null && mlaunch.mActivityID.equals(itemID)) {
							configBean.setItemId(itemID);
							
							 commandMap.put("rsrcSeq", lcmsItem[i].getRsrcSeq());
							 LcmsItemResource itemResource = (LcmsItemResource)lcmsItemResourceService.selectComLenLcmsItemResource(commandMap);
							 configBean.setItemURL(encodeURL("/content/" + itemResource.getRsrcBaseDir()));
					
							if (lcmsItem[i].getItemParameters() != null)
								configBean.setItemURL(configBean.getItemURL()+ lcmsItem[i].getItemParameters());
							if (lcmsItem[i].getNext() != null&& !lcmsItem[i].getNext().equals(""))
								configBean.setNextButton("");
							if (lcmsItem[i].getPrevious() != null && !lcmsItem[i].getPrevious().equals("")) 
								configBean.setPrevButton("");		
							if (lcmsItem[i].getExit() != null && !lcmsItem[i].getExit().equals(""))
								configBean.setQuitButton("");
						}
					}
				}
			} else {
				// DB정보만으로 콘텐츠 위치 얻어옴
				for (int i = 0; lcmsItem != null && !seqLaunched && i < lcmsItem.length; i++) {
					String itemID = lcmsItem[i].getItemId();
					if (SCOID != null && SCOID.equals(itemID))
						matched = true;
					else
						matched = false;
					if (matched) {
						
						configBean.setItemId(itemID);
						commandMap.put("rsrcSeq", lcmsItem[i].getRsrcSeq());
						//LcmsItemResource itemResource = (LcmsItemResource)lcmsItemResourceService.selectComLenLcmsItemResource(commandMap);
						//configBean.setItemURL(encodeURL("/contents/service/" + itemResource.getRsrcBaseDir()));
				    	Map output = new HashMap();
			    		output.putAll((Map)lcmsItemResourceService.selectComLenLcmsItemResource(commandMap));
			    		
			    		configBean.setItemURL(encodeURL("/content/" + output.get("rsrcBaseDir")));
				

						if (lcmsItem[i].getItemParameters() != null)
							configBean.setItemURL(configBean.getItemURL()+ lcmsItem[i].getItemParameters());
						if (lcmsItem[i].getNext() != null && !lcmsItem[i].getNext().equals(""))
							configBean.setNextButton("");
						if (lcmsItem[i].getPrevious() != null && !lcmsItem[i].getPrevious().equals(""))
							configBean.setPrevButton("");
						if (lcmsItem[i].getExit() != null && !lcmsItem[i].getExit().equals(""))
							configBean.setQuitButton("");

						// SCOID가 없으면 시퀀싱 값으로 얻어온다.
						if (i == 0)
							configBean.setPrevButton("");
						if (i == lcmsItem.length - 1)
							configBean.setNextButton("");
					}
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
		}
		return configBean;
	}
	
	/**
	 * TreeBean get
	 * @return SCORMTreeBEAN[] treeBean
	 * @throws Exception
	 */
	public SCORMTreeBEAN[] getTreeBean(ADLValidRequests ValidRequests,SeqActivityTree activityTree) throws Exception {
		SCORMTreeBEAN treeBean[] = (SCORMTreeBEAN[]) null;
		Vector Tree = ValidRequests.mTOC;
		SeqActivity activity = activityTree.getMCurActivity();
		if(activity!=null)	activity.setSysAttempt(activityTree.getMCurActivity().getSysAttempt() + 1L);
		if (Tree != null && Tree.size() > 0) {
			treeBean = new SCORMTreeBEAN[Tree.size()];
			for (int i = Tree.size() - 1; i >= 0; i--) {
				ADLTOC AdlToc = (ADLTOC) Tree.get(i);
				ADLTracking Traking = activityTree.getActivity(AdlToc.mID).getMCurTracking();
				treeBean[Tree.size() - i - 1] = new SCORMTreeBEAN();
				treeBean[Tree.size() - i - 1].setCount(AdlToc.mCount - 1);
				treeBean[Tree.size() - i - 1].setDepth(AdlToc.mDepth);
				treeBean[Tree.size() - i - 1].setLeaf(AdlToc.mLeaf);
				

				//charsetInfo(AdlToc.mTitle);
				/*
				if(Constants.FROM_CHARSET!=null && Constants.TO_CHARSET!=null &&
						!"".equals(Constants.FROM_CHARSET) && !"".equals(Constants.TO_CHARSET)){
					AdlToc.mTitle=new String(AdlToc.mTitle.getBytes(Constants.FROM_CHARSET),Constants.TO_CHARSET);
					System.out.println(AdlToc.mTitle);
				}
				
				*/
				treeBean[Tree.size() - i - 1].setTitle(AdlToc.mTitle);
				treeBean[Tree.size() - i - 1].setScoId(AdlToc.mID);
				treeBean[Tree.size() - i - 1].setCurrent(AdlToc.mIsCurrent);
				treeBean[Tree.size() - i - 1].setParentId(AdlToc.mParent);
				treeBean[Tree.size() - i - 1].setSelect(AdlToc.mIsSelectable);
				treeBean[Tree.size() - i - 1].setSuccessStatus(activityTree.getActivity(AdlToc.mID).getSuccessStatus());
				treeBean[Tree.size() - i - 1].setTotalTime(convertTime(activityTree.getActivity(AdlToc.mID).getStudyTime()));
				if (treeBean[Tree.size() - i - 1].getTotalTime().equals("00:00:00"))
					treeBean[Tree.size() - i - 1].setTotalTime(null);
				Vector ChildElement = activityTree.getActivity(AdlToc.mID).getMActiveChildren();
				if (ChildElement != null)
					treeBean[Tree.size() - i - 1].setHasChildNodeCount(ChildElement.size());
				
				if (Traking != null) {
					if (!Traking.mProgress.equals("unknown")) {
						treeBean[Tree.size() - i - 1].setHasUserInfo(true);
						treeBean[Tree.size() - i - 1].setAttempt(activityTree.getActivity(AdlToc.mID).getSysAttempt());
						treeBean[Tree.size() - i - 1].setCompletionStatus(Traking.mProgress);
					}
					if (activityTree.getActivity(AdlToc.mID).getAttemptLimitControl()&& activityTree.getActivity(AdlToc.mID).getAttemptLimit() <= treeBean[Tree.size()- i - 1].getAttempt()) {
						treeBean[Tree.size() - i - 1].setSelect(false);
						if (activityTree.getActivity(AdlToc.mID).getAttemptLimit() < treeBean[Tree.size() - i- 1].getAttempt()) {
							activityTree.getActivity(AdlToc.mID).setSysAttempt(activityTree.getActivity(AdlToc.mID).getAttemptLimit());
							treeBean[Tree.size() - i - 1].setAttempt(activityTree.getActivity(AdlToc.mID).getSysAttempt());
						}
					}
				}
			}

			for (int i = 0; i < treeBean.length; i++)
				if (i + 1 < treeBean.length) {
					if (treeBean[i].getDepth() < treeBean[i + 1].getDepth())
						treeBean[i].setHasNodeCondition(1);
					else if (treeBean[i].getDepth() > treeBean[i + 1].getDepth())
						treeBean[i].setHasNodeCondition(2);
				} else if (i - 1 >= 0)
					if (treeBean[i - 1].getDepth() < treeBean[i].getDepth())
						treeBean[i].setHasNodeCondition(1);
					else if (treeBean[i - 1].getDepth() > treeBean[i].getDepth())
						treeBean[i].setHasNodeCondition(2);

			Vector parentId = new Vector();
			for (int i = 1; i < treeBean.length; i++) {
				treeBean[i].setCount(i);
				if (treeBean[i].getHasNodeCondition() == 1) {
					if (parentId.size() > 0)
						treeBean[i].setParentId(Integer.parseInt((String) parentId.get(parentId.size() - 1)));
					else
						treeBean[i].setParentId(0);
					parentId.add(String.valueOf(i));
				} else if (treeBean[i].getHasNodeCondition() == 2) {
					if (i != treeBean.length - 1) {
						treeBean[i].setParentId(Integer.parseInt((String) parentId.get(parentId.size() - 1)));
						for (int j = treeBean[i].getDepth() - treeBean[i + 1].getDepth(); j > 0; j--) {
							j--;
							parentId.remove(j);
						}
					}

				} else if (parentId.size() > 0)
					treeBean[i].setParentId(Integer.parseInt((String) parentId
							.get(parentId.size() - 1)));
				else
					treeBean[i].setParentId(0);
			}

		}
		return treeBean;
	}	
	
	/**
	 * 학습데이터 00:00:00 으로 변경
	 * @return String returnTotalStudyTime
	 * @throws Exception
	 */
	private String convertTime(String sCormTime) {
		String returnTotalStudyTime = null;
		long classHH = 0L;
		long classMM = 0L;
		long classSS = 0L;
		long hh = 0L;
		long mm = 0L;
		long ss = 0L;
		if (sCormTime != null) {
			if (sCormTime.indexOf("H") > 0) {
				hh = Long.parseLong(sCormTime.substring(
						sCormTime.indexOf("T") + 1, sCormTime.indexOf("H")));
				classHH += hh;
			}
			if (sCormTime.indexOf("M") > 0) {
				mm = Long.parseLong(sCormTime.substring(
						sCormTime.indexOf("H") + 1, sCormTime.indexOf("M")));
				classMM += mm;
			}
			if (sCormTime.indexOf("S") > 0) {
				ss = Long.parseLong(sCormTime.substring(
						sCormTime.indexOf("M") + 1, sCormTime.indexOf("S")));
				classSS += ss;
			}
		}
		classMM += (int) classSS / 60;
		classSS %= 60L;
		classHH += (int) classMM / 60;
		classMM %= 60L;
		classHH += (int) classMM / 60;
		classHH %= 24L;
		if (classHH < 10L)
			returnTotalStudyTime = "0" + classHH + ":";
		else
			returnTotalStudyTime = classHH + ":";
		if (classMM < 10L)
			returnTotalStudyTime = returnTotalStudyTime + "0" + classMM + ":";
		else
			returnTotalStudyTime = returnTotalStudyTime + classMM + ":";
		if (classSS < 10L)
			returnTotalStudyTime = returnTotalStudyTime + "0" + classSS;
		else
			returnTotalStudyTime = returnTotalStudyTime + String.valueOf(classSS);
		return returnTotalStudyTime;
	}
	
	 

	public static void charsetInfo(String pstr){
	  try{
	   String[] charsetArr = new String[44];
	   charsetArr[0] = "euc-kr";
	   charsetArr[1] = "utf-8";
	   charsetArr[2] = "ISO-8859-1";
	   charsetArr[3] = "KSC5601";
	   charsetArr[4] = "8859_1";
	   
	   charsetArr[5] = "csBig5";
	   charsetArr[6] = "eucjis";
	   charsetArr[7] = "gb18030-2000";
	   charsetArr[8] = "CP936";
	   charsetArr[9] = "csISO2022KR";
	   charsetArr[10] = "iso8859_13";
	   charsetArr[11] = "8859_15";
	   charsetArr[12] = "iso8859_2";
	   charsetArr[13] = "iso8859_4";
	   charsetArr[14] = "iso8859_5";
	   charsetArr[15] = "greek8";
	   charsetArr[16] = "iso8859_4";
	   charsetArr[17] = "iso8859_5";
	   charsetArr[18] = "iso8859_7";
	   charsetArr[19] = "iso8859_9";
	   charsetArr[20] = "JIS0201";
	   charsetArr[21] = "x0212";
	   charsetArr[22] = "koi8";
	   charsetArr[23] = "ms_kanji";
	   
	   charsetArr[24] = "ASCII";
	   charsetArr[25] = "utf16";
	   charsetArr[26] = "UTF_16BE";
	   charsetArr[27] = "cp1250";
	   charsetArr[28] = "cp1251";
	   charsetArr[29] = "cp1252";
	   charsetArr[30] = "cp1253";
	   charsetArr[31] = "cp1254";
	   charsetArr[32] = "cp1257";
	   charsetArr[33] = "MS932";
	   charsetArr[34] = "EUC_CN";
	   charsetArr[35] = "euc-jp-linux";
	   charsetArr[36] = "ISCII91";
	   charsetArr[37] = "JIS0208";
	   charsetArr[38] = "ms949";
	   charsetArr[39] = "johab";
	   charsetArr[40] = "ms936";
	   charsetArr[41] = "cp50220";
	   charsetArr[42] = "ms50221";
	   charsetArr[43] = "ms949";

	   
	   
	   for(int i=0;i<charsetArr.length;i++){
	    for(int j=0;j<charsetArr.length;j++){
	     if(i!=j){
	      String charset1 = charsetArr[i];
	      String charset2 = charsetArr[j];
	      String str=new String(pstr.getBytes(charset1),charset2);
	      
	     }
	    }
	   }
	   
	   
	  }catch(Exception e){
	   e.printStackTrace();
	  }
	 }


}