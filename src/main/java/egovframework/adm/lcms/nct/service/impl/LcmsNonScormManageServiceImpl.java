package egovframework.adm.lcms.nct.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.lcms.nct.dao.LcmsLessonDAO;
import egovframework.adm.lcms.nct.dao.LcmsModuleDAO;
import egovframework.adm.lcms.nct.dao.LcmsNonScormManageDAO;
import egovframework.adm.lcms.nct.service.LcmsNonScormManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("lcmsNonScormManageService")
public class LcmsNonScormManageServiceImpl extends EgovAbstractServiceImpl implements LcmsNonScormManageService {

    @Resource(name="lcmsNonScormManageDAO")
    private LcmsNonScormManageDAO lcmsNonScormManageDAO;
	
    @Resource(name="lcmsModuleDAO")
    private LcmsModuleDAO lcmsModuleDAO;
    
    @Resource(name="lcmsLessonDAO")
    private LcmsLessonDAO lcmsLessonDAO;
    
	public String selectContentPath( Map<String, Object> commandMap ) throws Exception{
		return lcmsNonScormManageDAO.selectContentPath(commandMap);
	}
	
	public int selectNonScormListTotCnt(Map<String, Object> commandMap) throws Exception{
		return lcmsNonScormManageDAO.selectNonScormListTotCnt(commandMap);
	}
	
	public List selectNonScormList(Map<String, Object> commandMap) throws Exception{
		return lcmsNonScormManageDAO.selectNonScormList(commandMap);
	}
	
	public int insertCotiExcel(List dataList, Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			Map xlsMap;
			for( int i=0; i<dataList.size(); i++ ){
				xlsMap = (Map)dataList.get(i);
				
				//엑셀에서 파일명만 받기때문에 정의한다.
				String starting = "";
				if( !xlsMap.get("parameter7").toString().equals("NA") ){
					starting = commandMap.get("subj")+"/docs/"+xlsMap.get("parameter0")+"/" + xlsMap.get("parameter7");
				}
				xlsMap.put("userid", 	commandMap.get("userid"));
				xlsMap.put("subj",		commandMap.get("subj"));
				xlsMap.put("module", 	xlsMap.get("parameter0"));
				xlsMap.put("owner", 	xlsMap.get("parameter1"));
				xlsMap.put("lessonCd", 	xlsMap.get("parameter2"));
				xlsMap.put("depth", 	xlsMap.get("parameter3"));
				xlsMap.put("orderNum", 	xlsMap.get("parameter4"));
				xlsMap.put("lessonName",xlsMap.get("parameter5"));
				xlsMap.put("progressYn",xlsMap.get("parameter6"));
				xlsMap.put("starting", 	starting);
				xlsMap.put("pageCount", xlsMap.get("parameter8"));
				xlsMap.put("eduTime", 0);
				xlsMap.put("eduTimeYn", "N");
				
				
				if( xlsMap.get("parameter1").toString().equals("00") ){
					xlsMap.put("orgTitle",xlsMap.get("parameter5"));
					xlsMap.put("userId", 	commandMap.get("userid"));
					lcmsModuleDAO.insertLcmsModule(xlsMap);
				}else{
					lcmsLessonDAO.insertLcmsLesson(xlsMap);
				}
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public int insertExcel(List dataList, Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			Map xlsMap;
			String oldModule = "";
			for( int i=0; i<dataList.size(); i++ ){
				xlsMap = (Map)dataList.get(i);
				
				xlsMap.put("userid", 	commandMap.get("userid"));
				xlsMap.put("subj",		commandMap.get("subj"));
				xlsMap.put("module", 	xlsMap.get("parameter0"));
				xlsMap.put("lesson", 	xlsMap.get("parameter2"));
				xlsMap.put("lessonCd", 	xlsMap.get("parameter2"));
				xlsMap.put("depth", 	1);
				xlsMap.put("orderNum", 	i+1);
				xlsMap.put("lessonName",xlsMap.get("parameter3"));
				xlsMap.put("progressYn","Y");
				xlsMap.put("starting", 	xlsMap.get("parameter4"));
				xlsMap.put("pageCount", 0);
				xlsMap.put("eduTime",   xlsMap.get("parameter5") != null ? xlsMap.get("parameter5") : 0);
				xlsMap.put("eduTimeYn", xlsMap.get("parameter5") != null ? "Y" : "N");
				
				if( !oldModule.equals(xlsMap.get("parameter0").toString()) ){
					xlsMap.put("orgTitle",xlsMap.get("parameter1"));
					xlsMap.put("userId", 	commandMap.get("userid"));
					lcmsModuleDAO.insertLcmsModule(xlsMap);
					oldModule = (String)xlsMap.get("parameter0");
				}
				lcmsLessonDAO.insertLcmsLesson2(xlsMap);
				
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public List selectNonScormOrgList(Map<String, Object> commandMap) throws Exception{
		return lcmsNonScormManageDAO.selectNonScormOrgList(commandMap);
	}
	
	public List selectCotiExcelList(Map<String, Object> commandMap ) throws Exception{
		return lcmsNonScormManageDAO.selectCotiExcelList(commandMap);
	}
	
	public List selectExcelList(Map<String, Object> commandMap ) throws Exception{
		return lcmsNonScormManageDAO.selectExcelList(commandMap);
	}
	
	public int deleteLcmsCA(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			lcmsModuleDAO.deleteModuleAll(commandMap);
			lcmsLessonDAO.deleteLessonAll(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public List selectContentCodeList(Map<String, Object> commandMap) throws Exception{
		return lcmsNonScormManageDAO.selectContentCodeList(commandMap);
	}
	
	public List selectProgressLogList(Map<String, Object> commandMap) throws Exception{
		return lcmsNonScormManageDAO.selectProgressLogList(commandMap);
	}
	
	public int deletePorgressLog(Map<String, Object> commandMap) throws Exception{
		return lcmsNonScormManageDAO.deletePorgressLog(commandMap);
	}
}
