package egovframework.com.file.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.com.cmm.service.FileVO;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("fileDownloadDAO")
public class FileDownloadDAO extends EgovAbstractDAO{
    
	/**
	 * 파일 다운로드 dao
	 * 
	 * @return int
	 * @exception Exception
	 */
	public Object selectFileInfo(Map<String, Object> commandMap) throws Exception{
		
		return (Object)selectByPk("fileDownloadDAO.selectFileInfo", commandMap);
	}

	/**
	 * 학습년구년제 파일 다운로드
	 * @param commandMap
	 * @return
	 */
	public Object selectMadangFileInfo(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return (Object)selectByPk("fileDownloadDAO.selectMadangFileInfo", commandMap);
	}
	
	 	
}