package egovframework.com.bod.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.com.bod.dao.BoardManageDAO;
import egovframework.com.bod.service.BoardManageService;
import egovframework.com.file.controller.FileController;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("boardManageService")
public class BoardManageServiceImpl extends EgovAbstractServiceImpl implements BoardManageService{
	
	@Resource(name="boardManageDAO")
    private BoardManageDAO boardManageDAO;

	public List selectBoardListForAdmin(Map<String, Object> commandMap) throws Exception{
		return boardManageDAO.selectBoardListForAdmin(commandMap);
	}
	
	public int selectBoardListForAdminBySubjseqTotCnt(Map<String, Object> commandMap) throws Exception{
		return boardManageDAO.selectBoardListForAdminBySubjseqTotCnt(commandMap);
	}
	
	public List selectBoardListForAdminBySubjseq(Map<String, Object> commandMap) throws Exception{
		return boardManageDAO.selectBoardListForAdminBySubjseq(commandMap);
	}
	
	public List selectBoard(Map<String, Object> commandMap) throws Exception{
		return boardManageDAO.selectBoard(commandMap);
	}
	
	public int updateBoardData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			boardManageDAO.updateBoardData(commandMap);
			
			Map inputMap = new HashMap();
			inputMap.put("p_seq", commandMap.get("p_seq"));
			inputMap.put("p_tabseq", commandMap.get("p_tabseq"));
			if( commandMap.get("p_filedel") != null ){
				String[] idx = EgovStringUtil.getStringSequence(commandMap, "p_filedel");
				for(int i=0; i<idx.length; i++ ){
					String fileseq = (String)commandMap.get("p_fileseq"+idx[i]);
					inputMap.put("p_fileseq", fileseq);
					boardManageDAO.deleteBoardFile(inputMap);
				}
			}
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				int maxSeq = boardManageDAO.selectMaxFileseq(commandMap);
				for( int i=0; i<multiFiles.size(); i++ ){
					inputMap.putAll((Map)multiFiles.get(i));
					inputMap.put("fileseq", maxSeq++);
					boardManageDAO.insertBoardFile(inputMap);
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
			int seq = boardManageDAO.selectMaxBoardseq(commandMap);
			commandMap.put("p_seq", seq);
			if( commandMap.get("p_refseq") == null || commandMap.get("p_refseq").toString().equals("") ){
				commandMap.put("p_refseq", seq);
				commandMap.put("p_levels", 1);
				commandMap.put("p_position", 1);
			}else{
				boardManageDAO.updateReplyPosition(commandMap);
			}
			boardManageDAO.insertBoardData(commandMap);
			Map inputMap = new HashMap();
			inputMap.put("p_seq", commandMap.get("p_seq"));
			inputMap.put("p_tabseq", commandMap.get("p_tabseq"));
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				int maxSeq = boardManageDAO.selectMaxFileseq(commandMap);
				for( int i=0; i<multiFiles.size(); i++ ){
					inputMap.putAll((Map)multiFiles.get(i));
					inputMap.put("fileseq", maxSeq++);
					boardManageDAO.insertBoardFile(inputMap);
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
			boardManageDAO.deleteBoardData(commandMap);
			boardManageDAO.deleteBoardAllFile(commandMap);
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
	
	public int updateBoardViewCount(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			boardManageDAO.updateBoardViewCount(commandMap);
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		
		return isOk;
	}
}
