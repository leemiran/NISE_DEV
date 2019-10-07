package egovframework.adm.lcms.old.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface LcmsContentService {

	public int selectContentsListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectContentsList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectSubjectData(Map<String, Object> commandMap) throws Exception;
	
	public List selectItemList(Map<String, Object> commandMap) throws Exception;
	
	public int deleteCA(Map<String, Object> commandMap) throws Exception;
	
	public Map selectModuleInfo(Map<String, Object> commandMap) throws Exception;
	
	public String selectModuleKey(Map<String, Object> commandMap) throws Exception;
	
	public int insertModule(Map<String, Object> commandMap) throws Exception;
	
	public int updateModule(Map<String, Object> commandMap) throws Exception;
	
	public Map selectLessonInfo(Map<String, Object> commandMap) throws Exception;
	
	public Map selectNewLessonInfo(Map<String, Object> commandMap) throws Exception;
	
	public int insertLesson(Map<String, Object> commandMap) throws Exception;
	
	public int updateLesson(Map<String, Object> commandMap) throws Exception;
	
	public String insertLcmsCAExcel(String deleteFile, List list, Map commandMap) throws Exception;
	
	public String updateLcmsMobileExcel(String deleteFile, List list, Map commandMap) throws Exception;
	
	public int deleteBetaProgress(Map<String, Object> commandMap) throws Exception;

	public String insertModuleExcelUpload(String string, List list, Map<String, Object> commandMap);

	public String insertLessonExcelUpload(String string, List list, Map<String, Object> commandMap);

	public List getModuleBackupGroupList(Map<String, Object> commandMap);

	public List getLessonBackupGroupList(Map<String, Object> commandMap);

	public int recoveryModule(Map<String, Object> commandMap);

	public int recoveryLesson(Map<String, Object> commandMap);

	public int deleteModuleBackup(Map<String, Object> commandMap);

	public int deleteLessonBackup(Map<String, Object> commandMap);

	public List getSubjModuleList(Map<String, Object> commandMap);

	public List getSubjLessonList(Map<String, Object> commandMap);

	public List getSubjModuleBackupList(Map<String, Object> commandMap);

	public List getSubjLessonBackupList(Map<String, Object> commandMap);
	
	public List selectExamSubjList(Map<String, Object> commandMap);
	
	public Map selectOriContents(Map<String, Object> commandMap);
	
	public List selectContentsFileList(Map<String, Object> commandMap);
	
	public boolean  insertOriContentsFileInsert(Map<String, Object> commandMap) throws Exception;
	
	public boolean  insertOriContents(Map<String, Object> commandMap) throws Exception;
	
	public boolean  updateOriContents(Map<String, Object> commandMap) throws Exception;	
	
}