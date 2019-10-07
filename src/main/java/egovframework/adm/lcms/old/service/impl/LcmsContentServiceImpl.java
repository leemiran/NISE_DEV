package egovframework.adm.lcms.old.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.lcms.old.dao.LcmsContentDAO;
import egovframework.adm.lcms.old.service.LcmsContentService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.file.controller.FileController;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import org.apache.log4j.Logger;

@Service("lcmsContentService")
public class LcmsContentServiceImpl extends EgovAbstractServiceImpl implements LcmsContentService{
	
	@Resource(name="lcmsContentDAO")
    private LcmsContentDAO lcmsContentDAO;
	
	/** fileUtil */
    @Resource(name="EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    private Logger logger = Logger.getLogger(this.getClass());
    
	public int selectContentsListTotCnt(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectContentsListTotCnt(commandMap);
	}
	
	public List selectContentsList(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectContentsList(commandMap);
	}
	
	public Map selectSubjectData(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectSubjectData(commandMap);
	}
	
	public List selectItemList(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectItemList(commandMap);
	}
	
	public int deleteCA(Map<String, Object> commandMap) throws Exception{
		lcmsContentDAO.deleteModule(commandMap);
		lcmsContentDAO.deleteLesson(commandMap);
		return 1;
	}
	
	public Map selectModuleInfo(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectModuleInfo(commandMap);
	}
	
	public String selectModuleKey(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectModuleKey(commandMap);
	}
	
	public int insertModule(Map<String, Object> commandMap) throws Exception{
		lcmsContentDAO.insertModule(commandMap);
		return 1;
	}
	
	public int updateModule(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.updateModule(commandMap);
	}
	
	public Map selectLessonInfo(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectLessonInfo(commandMap);
	}
	
	public Map selectNewLessonInfo(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.selectNewLessonInfo(commandMap);
	}
	
	public int insertLesson(Map<String, Object> commandMap) throws Exception{
		lcmsContentDAO.insertLesson(commandMap);
		return 1;
	}
	
	public int updateLesson(Map<String, Object> commandMap) throws Exception{
		return lcmsContentDAO.updateLesson(commandMap);
	}
	
	
	//웹컨텐츠용업로드
	public String insertLcmsCAExcel(String deleteFile, List list, Map commandMap) throws Exception{
		String resultMsg = "";
		List<?> _result = null;
		String[] tempModule = null;
		String[] tempLesson = null;
		int lessonIdx = 0;
		String oldModule = "";
		ArrayList arr = new ArrayList();
		int isOk = 1;
		try{
			boolean flag = true;
			if(list != null && list.size() > 0 ){
				tempLesson = new String[list.size()-1];
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					String module 	= (String)map.get("parameter0");
					String modulenm	= (String)map.get("parameter1");
					String lesson	= (String)map.get("parameter2");
					String lessonnm = (String)map.get("parameter3");
					String starting = (String)map.get("parameter4");
					String webTime  = (String)map.get("parameter5");
					tempLesson[lessonIdx++] = module+"!"+lesson+"!"+lessonnm+"!"+starting+"!"+webTime;
					if( !module.equals(oldModule) ){
						oldModule = module;
						arr.add(module+"!"+modulenm);
					}
					if( "".equals(module) || "".equals(lesson) ){
						resultMsg = ("".equals(module) ? "module" : "lesson") + "정보를 확인하세요. 정확한양식은 샘플을 참조하세요.";
						flag = false;
						break;
					}
					
				}
				
				if( flag ){
					tempModule = new String[arr.size()];
					for( int idx=0; idx<arr.size(); idx++ ){
						tempModule[idx] = (String)arr.get(idx);
					}
					commandMap.put("tempModule", tempModule);
					commandMap.put("tempLesson", tempLesson);
					
					lcmsContentDAO.insertModuleExcel(commandMap);
					lcmsContentDAO.insertLessonExcel(commandMap);
					
					resultMsg = egovMessageSource.getMessage("success.common.insert");
				}
			}else{
				resultMsg = egovMessageSource.getMessage("fail.file.isempty");
			}
			
			File del = new File(deleteFile);
			del.delete();
		}catch(Exception e){
			File del = new File(deleteFile);
			del.delete();
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
			e.printStackTrace();
		}
		
		return resultMsg;
	}
	
	//모바일용 동영상 경로 챕터 업로드 - 업데이트만 수행한다. 
	//웹컨텐츠가 등록이 선행되어야 등록이 가능하다.
	public String updateLcmsMobileExcel(String deleteFile, List list, Map commandMap) throws Exception{
		String resultMsg = "";
		String oldModule = "";
		int cnt = 0;
		try{
			if(list != null && list.size() > 0 ){
				
				
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					String module 	= (String)map.get("parameter0");
					String modulenm	= (String)map.get("parameter1");
					String mobile_url	= (String)map.get("parameter2");
					String lesson	= (String)map.get("parameter3");
					String lessonnm	= (String)map.get("parameter4");
					String m_start	= (String)map.get("parameter5");
					String m_end = (String)map.get("parameter6");
					
					Map<String, Object> xlsMap = new HashMap<String, Object>();
					xlsMap.put("subj", commandMap.get("subj"));
					xlsMap.put("module", module);
					xlsMap.put("mobile_url", mobile_url);
					xlsMap.put("lesson", lesson);
					xlsMap.put("m_start", m_start);
					xlsMap.put("m_end", m_end);
					xlsMap.put("userid", commandMap.get("userid"));
					
					//모듈업데이트
					if( !module.equals(oldModule) ){
						oldModule = module;
						cnt += lcmsContentDAO.updateModuleExcel(xlsMap);
					}
					//레슨업데이트
					cnt += lcmsContentDAO.updateLessonExcel(xlsMap);
				}
				
				if(cnt> 0)
					resultMsg = egovMessageSource.getMessage("success.common.insert");
				else
					resultMsg = egovMessageSource.getMessage("fail.file.isempty");
				
			}else{
				resultMsg = egovMessageSource.getMessage("fail.file.isempty");
			}
			
			File del = new File(deleteFile);
			del.delete();
		}catch(Exception e){
			logger.info(e);
			File del = new File(deleteFile);
			del.delete();
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		
		return resultMsg;
	}
	
	public int deleteBetaProgress(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		lcmsContentDAO.delBetaProgress(commandMap);
		return isOk;
	}

	@Transactional
	public String insertModuleExcelUpload(String string, List list, Map<String, Object> commandMap) {
		String resultMsg = "";
		
		try {
			List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
			if(list != null && list.size() > 0 ){
				
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("module", map.get("parameter0"));
					paramMap.put("sdesc", map.get("parameter1"));
					paramMap.put("mobile_url", map.get("parameter2"));
					
					paramList.add(paramMap);
				}
				
				commandMap.put("list", paramList);

				lcmsContentDAO.insertModuleBackup(commandMap);
				lcmsContentDAO.deleteModule(commandMap);
				lcmsContentDAO.insertModuleExcelMultiple(commandMap);
				resultMsg = egovMessageSource.getMessage("success.common.insert");
			} else {
				resultMsg = egovMessageSource.getMessage("fail.file.isempty");
			}
			
		} catch(Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		return resultMsg;
	}

	public String insertLessonExcelUpload(String string, List list, Map<String, Object> commandMap) {
		String resultMsg = "";
		
		try {
			List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
			if(list != null && list.size() > 0 ){
				
				lcmsContentDAO.insertLessonBackup(commandMap);
				lcmsContentDAO.deleteLesson(commandMap);
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					
					Map<String, Object> paramMap = new HashMap<String, Object>();
					paramMap.put("module", map.get("parameter0"));
					paramMap.put("lesson", map.get("parameter1"));
					paramMap.put("sdesc", map.get("parameter2"));
					paramMap.put("starting", map.get("parameter3"));
					paramMap.put("m_start", map.get("parameter4"));
					paramMap.put("m_end", map.get("parameter5"));
					paramMap.put("lesson_time", map.get("parameter6"));
					
					commandMap.put("module", map.get("parameter0"));
					commandMap.put("lesson", map.get("parameter1"));
					commandMap.put("sdesc", map.get("parameter2"));
					commandMap.put("starting", map.get("parameter3"));
					commandMap.put("m_start", map.get("parameter4"));
					commandMap.put("m_end", map.get("parameter5"));
					commandMap.put("lesson_time", map.get("parameter6"));
					
					paramList.add(paramMap);
					
					lcmsContentDAO.insertLessonExcelOne(commandMap);
				}
				
				commandMap.put("list", paramList);				
				//lcmsContentDAO.insertLessonExcelMultiple(commandMap);
				resultMsg = egovMessageSource.getMessage("success.common.insert");
			} else {
				resultMsg = egovMessageSource.getMessage("fail.file.isempty");
			}
			
		} catch(Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			resultMsg = egovMessageSource.getMessage("fail.common.insert");
		}
		return resultMsg;
	}

	public List getModuleBackupGroupList(Map<String, Object> commandMap) {
		return lcmsContentDAO.getModuleBackupGroupList(commandMap);
	}

	public List getLessonBackupGroupList(Map<String, Object> commandMap) {
		return lcmsContentDAO.getLessonBackupGroupList(commandMap);
	}

	public int recoveryModule(Map<String, Object> commandMap) {
		try {
			lcmsContentDAO.deleteModule(commandMap);
			lcmsContentDAO.insertModuleRecovery(commandMap);
			return 1;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
	}

	public int recoveryLesson(Map<String, Object> commandMap) {
		try {
			lcmsContentDAO.deleteLesson(commandMap);
			lcmsContentDAO.insertLessonRecovery(commandMap);
			return 1;
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return 0;
		}
	}

	public int deleteModuleBackup(Map<String, Object> commandMap) {
		return lcmsContentDAO.deleteModuleBackup(commandMap);
	}

	public int deleteLessonBackup(Map<String, Object> commandMap) {
		return lcmsContentDAO.deleteLessonBackup(commandMap);
	}

	public List getSubjModuleList(Map<String, Object> commandMap) {
		return lcmsContentDAO.getSubjModuleList(commandMap);
	}

	public List getSubjLessonList(Map<String, Object> commandMap) {
		return lcmsContentDAO.getSubjLessonList(commandMap);
	}

	public List getSubjModuleBackupList(Map<String, Object> commandMap) {
		return lcmsContentDAO.getSubjModuleBackupList(commandMap);
	}

	public List getSubjLessonBackupList(Map<String, Object> commandMap) {
		return lcmsContentDAO.getSubjLessonBackupList(commandMap);
	}
	
	public List selectExamSubjList(Map<String, Object> commandMap) {
		return lcmsContentDAO.selectExamSubjList(commandMap);
	}
	
	public Map selectOriContents(Map<String, Object> commandMap){
		return lcmsContentDAO.selectOriContents(commandMap);
	}
	
	public List selectContentsFileList(Map<String, Object> commandMap){
		return lcmsContentDAO.selectContentsFileList(commandMap);
	}
	
	public boolean insertOriContentsFileInsert(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		try{
			
			String[] origfilename = (String[])  commandMap.get("origfilename");		// 원본 파일명		
			String[] savefilename = (String[])  commandMap.get("savefilename");		// 저장 파일명
			String[] savepath = (String[])  commandMap.get("savepath");		// 파일 저장경로
			String[] filesize = (String[])  commandMap.get("filesize");		// 파일 사이즈
			String[] foldername = (String[])  commandMap.get("foldername");		// 폴더정보(폴더 업로드시만)
			String[] customvalue = (String[])  commandMap.get("customvalue");		// 개발자 정의값
			String[] componentname = (String[])  commandMap.get("componentname");		// 컴포넌트 이름
			
			/*String[] origfilename = request.getParameterValues("_innorix_origfilename"); 	// 원본 파일명
			String[] savefilename = request.getParameterValues("_innorix_savefilename"); 	// 저장 파일명
			String[] savepath = request.getParameterValues("_innorix_savepath"); 			// 파일 저장경로
			String[] filesize = request.getParameterValues("_innorix_filesize"); 			// 파일 사이즈
			String[] foldername = request.getParameterValues("_innorix_folder"); 			// 폴더정보(폴더 업로드시만)
			String[] customvalue = request.getParameterValues("_innorix_customvalue"); 		// 개발자 정의값
			String[] componentname = request.getParameterValues("_innorix_componentname"); 	// 컴포넌트 이름
			
			
			commandMap.put("origfilename", request.getParameterValues("_innorix_origfilename"));
			boolean isOk = lcmsContentService.insertOriContentsFileInsert(commandMap);*/
			
			
			if (origfilename != null)
			{
				for (int i = 0; i < origfilename.length; i++)
				{
					/* 여기에 업로드 파일 정보를 DB에 입력하는 코드를 작성 합니다. */
					
					System.out.println("origfilename ----> "+origfilename[i]);
					System.out.println("savefilename ----> "+savefilename[i]);
					System.out.println("savepath ----> "+savepath[i]);					
					System.out.println("filesize ----> "+filesize[i]);
					System.out.println("customvalue ----> "+customvalue[i]);
					System.out.println("componentname ----> "+componentname[i]);
					
					commandMap.put("p_origfilename", origfilename[i]);
					commandMap.put("p_savefilename", savefilename[i]);
					commandMap.put("p_savepath", savepath[i]);
					commandMap.put("p_filesize", filesize[i]);
					commandMap.put("p_customvalue", customvalue[i]);
					commandMap.put("p_componentname", componentname[i]);
					
					lcmsContentDAO.insertOriContentsFileInsert(commandMap);
						
				}
			}
			
			isOk = true;
			
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public boolean insertOriContents(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		try{
			lcmsContentDAO.insertOriContents(commandMap);
			isOk = true;
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public boolean updateOriContents(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		try{
			
			String [] _Array_p_fileseq = (String []) commandMap.get("_Array_p_fileseq");
			//파일삭제
			if(_Array_p_fileseq != null)
			{
				FileController file = new FileController();
				for(int i=0; i<_Array_p_fileseq.length; i++)
				{
					commandMap.put("p_seq", _Array_p_fileseq[i]);
					
					List oriContentsFileList = lcmsContentDAO.selectContentsFileList(commandMap);
					Map oriContentsFile = (Map) oriContentsFileList.get(0);
					String savepath = oriContentsFile.get("savepath").toString();
					
					File tmp = new File(savepath);
					//파일삭제
					System.out.println("savepath ----> "+savepath);
					tmp.delete();								
					//DB파일삭제				
					lcmsContentDAO.deleteOriContentsFile(commandMap);
				}
			}				
			lcmsContentDAO.updateOriContents(commandMap);
			isOk = true;
		}catch(Exception ex ){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}
	
}
