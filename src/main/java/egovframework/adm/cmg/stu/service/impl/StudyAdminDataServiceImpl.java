package egovframework.adm.cmg.stu.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.cmg.stu.dao.StudyAdminDataDAO;
import egovframework.adm.cmg.stu.service.StudyAdminDataService;
import egovframework.com.file.controller.FileController;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("studyAdminDataService")
public class StudyAdminDataServiceImpl extends EgovAbstractServiceImpl implements StudyAdminDataService{
	
	@Resource(name="studyAdminDataDAO")
    private StudyAdminDataDAO studyAdminDataDAO;

	public List selectStudyAdminDataList(Map<String, Object> commandMap) throws Exception{
		return studyAdminDataDAO.selectStudyAdminDataList(commandMap);
	}
	
	public List selectBoardListForAdmin(Map<String, Object> commandMap) throws Exception{
		return studyAdminDataDAO.selectBoardListForAdmin(commandMap);
	}
	
	public List selectBoardViewData(Map<String, Object> commandMap) throws Exception{
		return studyAdminDataDAO.selectBoardViewData(commandMap);
	}
	
	public int updateBoardData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			studyAdminDataDAO.updateBoardData(commandMap);
			
			Map inputMap = new HashMap();
			inputMap.put("p_seq", commandMap.get("p_seq"));
			inputMap.put("p_tabseq", commandMap.get("p_tabseq"));
			if( commandMap.get("p_filedel") != null ){
				String[] idx = EgovStringUtil.getStringSequence(commandMap, "p_filedel");
				for(int i=0; i<idx.length; i++ ){
					String fileseq = (String)commandMap.get("p_fileseq"+idx[i]);
					inputMap.put("p_fileseq", fileseq);
					studyAdminDataDAO.deleteBoardFile(inputMap);
				}
			}
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				int maxSeq = studyAdminDataDAO.selectMaxFileseq(commandMap);
				for( int i=0; i<multiFiles.size(); i++ ){
					inputMap.putAll((Map)multiFiles.get(i));
					inputMap.put("fileseq", maxSeq++);
					studyAdminDataDAO.insertBoardFile(inputMap);
				}
			}
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
	
		return isOk;
	}
	
	public int insertBoardData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			commandMap.put("p_seq", studyAdminDataDAO.selectMaxBoardseq(commandMap));
			studyAdminDataDAO.insertBoardData(commandMap);
			Map inputMap = new HashMap();
			inputMap.put("p_seq", commandMap.get("p_seq"));
			inputMap.put("p_tabseq", commandMap.get("p_tabseq"));
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				int maxSeq = studyAdminDataDAO.selectMaxFileseq(commandMap);
				for( int i=0; i<multiFiles.size(); i++ ){
					inputMap.putAll((Map)multiFiles.get(i));
					inputMap.put("fileseq", maxSeq++);
					studyAdminDataDAO.insertBoardFile(inputMap);
				}
			}
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	public int deleteBoardData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			studyAdminDataDAO.deleteBoardData(commandMap);
			studyAdminDataDAO.deleteBoardAllFile(commandMap);
			isOk = FileController.deleteFiles(commandMap);
			if( isOk == 0 ){
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		
		return isOk;
	}
}
