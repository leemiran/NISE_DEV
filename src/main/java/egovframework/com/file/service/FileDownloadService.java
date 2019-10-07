package egovframework.com.file.service;

import java.util.Map;

import egovframework.com.cmm.service.FileVO;

/**
 * 파일 다운로드 공통 서비스
 * @author 공통서비스 개발팀 임성준
 * @since 2011.10.31
 * @version 1.0
 * @see
 *  
 * <pre>
 * << 개정이력(Modification Information) >>
 * 
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.10.31  임성준          최초 생성 
 *  
 *  </pre>
 */
public interface FileDownloadService {
	
	/**
	 * 파일 다운로드 service
	 * @param vo SndngMailVO
	 * @return boolean
	 * @exception Exception
	 */
	public Object selectFileInfo(Map<String, Object> commandMap) throws Exception;

	/**
	 * 학습년구년제 파일 다운로드
	 * @param commandMap
	 * @return
	 */
	public Object selectMadangFileInfo(Map<String, Object> commandMap) throws Exception;
	
}
