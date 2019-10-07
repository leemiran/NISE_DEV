package egovframework.com.file.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.service.FileVO;
import egovframework.com.file.dao.FileDownloadDAO;
import egovframework.com.file.service.FileDownloadService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * 파일 다운로드 공통 서비스
 * @author 공통서비스 개발팀 
 * @since 2009.03.12
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.12            최초 생성 
 *  
 *  </pre>
 */
@Service("fileDownloadService")
public class FileDownloadServiceImpl extends EgovAbstractServiceImpl implements FileDownloadService {

    @Resource(name="fileDownloadDAO")
    private FileDownloadDAO fileDownloadDAO;

	/**
	 * 파일 다운로드 service
	 * @param vo SndngMailVO
	 * @return boolean
	 * @exception Exception
	 */
	public Object selectFileInfo(Map<String, Object> commandMap) throws Exception {
		
		return fileDownloadDAO.selectFileInfo(commandMap);
		
	}

	/**
	 * 학습년구년제 파일 다운로드
	 */
	public Object selectMadangFileInfo(Map<String, Object> commandMap)
			throws Exception {
		// TODO Auto-generated method stub
		return fileDownloadDAO.selectMadangFileInfo(commandMap);
	}
    
}
