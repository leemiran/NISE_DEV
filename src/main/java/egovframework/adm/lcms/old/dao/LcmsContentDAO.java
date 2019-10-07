package egovframework.adm.lcms.old.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("lcmsContentDAO")
public class LcmsContentDAO extends EgovAbstractDAO{

	public int selectContentsListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("lcmsContentDAO.selectContentsListTotCnt", commandMap);
	}
	
	public List selectContentsList(Map<String, Object> commandMap) throws Exception{
		return list("lcmsContentDAO.selectContentsList", commandMap);
	}
	
	public Map selectSubjectData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("lcmsContentDAO.selectSubjectData", commandMap);
	}
	
	public List selectItemList(Map<String, Object> commandMap) throws Exception{
		return list("lcmsContentDAO.selectItemList", commandMap);
	}
	
	public int deleteModule(Map<String, Object> commandMap) throws Exception{
		return delete("lcmsContentDAO.deleteModule", commandMap);
	}
	
	public int deleteLesson(Map<String, Object> commandMap) throws Exception{
		return delete("lcmsContentDAO.deleteLesson", commandMap);
	}
	
	public Map selectModuleInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("lcmsContentDAO.selectModuleInfo", commandMap);
	}
	
	public String selectModuleKey(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("lcmsContentDAO.selectModuleKey", commandMap);
	}
	
	public void insertModule(Map<String, Object> commandMap) throws Exception{
		insert("lcmsContentDAO.insertModule", commandMap);
	}
	
	public int updateModule(Map<String, Object> commandMap) throws Exception{
		return update("lcmsContentDAO.updateModule", commandMap);
	}
	
	public Map selectLessonInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("lcmsContentDAO.selectLessonInfo", commandMap);
	}
	
	public Map selectNewLessonInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("lcmsContentDAO.selectNewLessonInfo", commandMap);
	}
	
	public void insertLesson(Map<String, Object> commandMap) throws Exception{
		insert("lcmsContentDAO.insertLesson", commandMap);
	}
	
	public int updateLesson(Map<String, Object> commandMap) throws Exception{
		return update("lcmsContentDAO.updateLesson", commandMap);
	}
	
	public void insertModuleExcel(Map<String, Object> commandMap) throws Exception{
		insert("lcmsContentDAO.insertModuleExcel", commandMap);
	}
	
	public int updateModuleExcel(Map<String, Object> commandMap) throws Exception{
		return update("lcmsContentDAO.updateModuleExcel", commandMap);
	}
	
	public void insertLessonExcel(Map<String, Object> commandMap) throws Exception{
		insert("lcmsContentDAO.insertLessonExcel", commandMap);
	}
	
	public int updateLessonExcel(Map<String, Object> commandMap) throws Exception{
		return update("lcmsContentDAO.updateLessonExcel", commandMap);
	}
	
	public int delBetaProgress(Map<String, Object> commandMap) throws Exception{
		return delete("lcmsContentDAO.delBetaProgress", commandMap);
	}

	public void insertModuleBackup(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertModuleBackup", commandMap);
	}

	public void insertModuleExcelMultiple(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertModuleExcelMultiple", commandMap);
	}

	public void insertLessonBackup(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertLessonBackup", commandMap);
	}

	public void insertLessonExcelMultiple(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertLessonExcelMultiple", commandMap);
	}

	public List getModuleBackupGroupList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.getModuleBackupGroupList", commandMap);
	}

	public List getLessonBackupGroupList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.getLessonBackupGroupList", commandMap);
	}

	public void insertModuleRecovery(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertModuleRecovery", commandMap);
	}

	public void insertLessonRecovery(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertLessonRecovery", commandMap);
	}

	public int deleteModuleBackup(Map<String, Object> commandMap) {
		return delete("lcmsContentDAO.deleteModuleBackup", commandMap);
	}

	public int deleteLessonBackup(Map<String, Object> commandMap) {
		return delete("lcmsContentDAO.deleteLessonBackup", commandMap);
	}

	public List getSubjModuleList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.getSubjModuleList", commandMap);
	}

	public List getSubjLessonList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.getSubjLessonList", commandMap);
	}

	public List getSubjModuleBackupList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.getSubjModuleBackupList", commandMap);
	}

	public List getSubjLessonBackupList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.getSubjLessonBackupList", commandMap);
	}
	
	public void insertLessonExcelOne(Map<String, Object> commandMap) {
		insert("lcmsContentDAO.insertLessonExcelOne", commandMap);
	}
	
	public List selectExamSubjList(Map<String, Object> commandMap) {
		return list("lcmsContentDAO.selectExamSubjList", commandMap);
	}
	
	public Map selectOriContents(Map<String, Object> commandMap){
		return (Map)selectByPk("lcmsContentDAO.selectOriContents", commandMap);
	}
	
	public List selectContentsFileList(Map<String, Object> commandMap){
		return list("lcmsContentDAO.selectContentsFileList",commandMap);
	}
	
	public Object insertOriContentsFileInsert(Map<String, Object> commandMap) throws Exception{
		return insert("lcmsContentDAO.insertOriContentsFileInsert", commandMap);
	}
	
	public Object insertOriContents(Map<String, Object> commandMap) throws Exception{
		return insert("lcmsContentDAO.insertOriContents", commandMap);
	}
	
	
	public Object updateOriContents(Map<String, Object> commandMap) throws Exception{
		return insert("lcmsContentDAO.updateOriContents", commandMap);
	}
	
	public int deleteOriContentsFile(Map<String, Object> commandMap) throws Exception{
		return delete("lcmsContentDAO.deleteOriContentsFile", commandMap);
	}
	
}