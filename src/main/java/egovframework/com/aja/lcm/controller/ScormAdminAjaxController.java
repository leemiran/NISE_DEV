package egovframework.com.aja.lcm.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import egovframework.adm.lcms.ims.mainfest.RestructureHandler;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.pag.controller.PagingManageController;
import egovframework.rte.fdl.property.EgovPropertyService;


import egovframework.com.aja.lcm.AjaxXmlView;
import egovframework.com.aja.service.CommonAjaxManageService;





/**
 * 중복체크관련 처리 
 */
@Controller
public class ScormAdminAjaxController {
   
	/** log */
    protected static final Log log = LogFactory.getLog(ScormHandlerAjaxController.class);
    
    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
	
    /** pagingManageController */
    @Resource(name="pagingManageController")
    PagingManageController pagingManageController;
    
    
	/** AJAX 공통 서비스*/
	@Resource(name = "commonAjaxManageService")
    private CommonAjaxManageService commonAjaxManageService;
	

	
    /**
     * 컨텐츠 차시 순서 변경
     * 
     * @param HttpServletResponse response, HttpServletRequest request, Map commandMap
     * @return
     * @exception Exception
     */
    @RequestMapping(value = "/com/aja/lcm/updateOrgSort.do")
    public ModelAndView selectMenuCombo(HttpServletRequest request, HttpServletResponse response, Map commandMap) throws Exception {
    	
    	Object result = null;
    	String tmp = (String)commandMap.get("p_org");
    	String[] org = new String[tmp.split(",").length];
    	for( int i=0; i<org.length; i++ ){
    		org[i] = tmp.split(",")[i]+"!"+(i+1);
    	}
    	commandMap.remove("p_org");
    	commandMap.put("p_org", org);
    	
		result = commonAjaxManageService.updateCommonAjaxManageInt(commandMap, "lcmsOrganizationDAO.updateOrgSort");
		
		return getAjaxXmlData(result);
    }
    
    /**
     * 아이템추가
     * @param request
     * @param response
     * @param commandMap
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/com/aja/lcm/addItem.do")
    public ModelAndView addItem(HttpServletRequest request, HttpServletResponse response, Map commandMap) throws Exception{
    	
    	boolean result = true;
    	
    	RestructureHandler rh = new RestructureHandler();
    	
    	result = rh.addItem(commandMap);
    	
    	return getAjaxXmlData(result);
    }
    
    @RequestMapping(value="/com/aja/lcm/searchRestCrs.do")
    public ModelAndView searchRestCrs(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
    	
    	int totCnt = commonAjaxManageService.selectCommonAjaxManageInt(commandMap, "scormContentDAO.selectScormContentListTotCnt");
        pagingManageController.PagingManage(commandMap, model, totCnt);
    	
    	List list = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "scormContentDAO.selectScormContentList");
    	return getAjaxXmlData(list);
    }
    
    @RequestMapping(value="/com/aja/lcm/selectDetailContentList.do")
    public ModelAndView selectDetailContentList(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
    	
    	Object result = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "scormContentDAO.selectScormScoList");
    	
    	return getAjaxXmlData(result);
    }
    
    @RequestMapping(value="/com/aja/lcm/courseRsrcUpdate.do")
    public ModelAndView updateCourseRsrc(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
    	
    	boolean result = true;
    	RestructureHandler rh = new RestructureHandler();
    	
    	String rsrcSeq = (String)commandMap.get("oldItemId");
    	commandMap.put("rsrcSeq", rsrcSeq.split("@@")[1]);
    	String baseDir = (String)commonAjaxManageService.selectCommonAjaxManageObject(commandMap, "scormContentDAO.selectScormRsrcBaseDir");
    	
    	commandMap.put("baseDir", baseDir);
    	
    	result = rh.updateItem(commandMap);
    	
    	ModelAndView modelAndView = new ModelAndView();
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }
    
    @RequestMapping(value="/com/aja/lcm/removeItem.do")
    public ModelAndView removeItem(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
    	boolean result = true;
    	
    	RestructureHandler rh = new RestructureHandler();
    	
    	result = rh.removeItem(commandMap);
    	
    	ModelAndView modelAndView = new ModelAndView();
    	Map resultMap = new HashMap();
    	resultMap.put("result", result);
    	modelAndView.addAllObjects(resultMap);
    	modelAndView.setViewName("jsonView");
    	return modelAndView;
    }
    
    @RequestMapping(value="/com/aja/lcm/courseRsrcUpdateAll.do")
    public ModelAndView updateCourseRsrcAll(HttpServletRequest request, HttpServletResponse response, Map commandMap, ModelMap model) throws Exception{
    	
    	boolean result = true;
    	String baseDir = (String)commonAjaxManageService.selectCommonAjaxManageObject(commandMap, "scormContentDAO.selectScormRsrcBaseDir2");
    	String manifestPath = Globals.MANIFEST_PATH+commandMap.get("userid");
    	
		
		File exFile = new File(manifestPath, "imsmanifest.xml");
		if( exFile.exists() ){
			exFile.delete();
		}
		FileController file = new FileController();
		file.makeManifest(manifestPath);
		
		commandMap.put("baseDir", baseDir);
		commandMap.put("manifestPath", manifestPath);
		RestructureHandler rh = new RestructureHandler();
		List list = commonAjaxManageService.selectCommonAjaxManageList(commandMap, "scormContentDAO.selectScormRsrcInfo");
		String ident = "";
		for( int i=0; i<list.size(); i++ ){
			Map gMap = (Map)list.get(i);
			String rsrcSeq = (String)gMap.get("rsrcSeq");
			commandMap.put("oldItemId", gMap.get("oldItem"));
			String selectVal = "";
			String addItemId = "";
			if( gMap.get("highItemSeq") == null || ((String)gMap.get("highItemSeq")).equals("0") ){
				selectVal = "ORG#NEW";
				commandMap.put("selectVal", selectVal);
				ident = rh.newAddItem(commandMap);
				addItemId = ident;
				if( rsrcSeq != null && !rsrcSeq.equals("0") ){
					commandMap.put("selectVal", "ITM#"+ident);
				}
			}else{
				selectVal = "ITM#"+ident;
				commandMap.put("selectVal", selectVal);
				addItemId = rh.newAddItem(commandMap);
			}
			commandMap.put("addItemId", addItemId);
			result = rh.newUpdateItem(commandMap);
		}
    	
		return getAjaxXmlData(result);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public ModelAndView getAjaxXmlData(Object obj){
    	ModelAndView modelAndView = new ModelAndView(new AjaxXmlView()); 
    	StringBuffer sb = new StringBuffer();
    	sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
    	sb.append("<ajax_xml>");
    	
    	
    	if( obj.getClass() == (new ArrayList()).getClass() ){
    		List list = (List)obj;
    		if( list != null && list.size() > 0 ){
    			for( int i=0; i<list.size(); i++ ){
    				sb.append("<result>");
    				Map m = (Map)list.get(i);
    				Set set = m.keySet();
    				Object []key = set.toArray();
    				for( int j=0; j<key.length; j++ ){
    					String str = String.valueOf(key[j]);
    					sb.append("<"+str+">" + String.valueOf(m.get(str)) + "</"+str+">");
    				}
    				sb.append("</result>");
    			}
    		}
    	}else if( obj.getClass() == (new HashMap()).getClass() ){
    		sb.append("<result>");
			Map m = (Map)obj;
			Set set = m.keySet();
			Object []key = set.toArray();
			for( int j=0; j<key.length; j++ ){
				String str = String.valueOf(key[j]);
				sb.append("<"+str+">" + String.valueOf(m.get(str)) + "</"+str+">");
			}
			sb.append("</result>");
    	}else if( obj.getClass() == (new String()).getClass() ){
    		sb.append("<result>"+obj+"</result>");
    	}else if( obj.getClass() == ((Object)1).getClass() ){
    		sb.append("<result>"+obj+"</result>");
    	}else{
    		sb.append("<result>"+obj+"</result>");
    	}
    	
    	sb.append("</ajax_xml>");
    	modelAndView.addObject("ajaxXml", sb.toString());
    	return modelAndView;
    }

}