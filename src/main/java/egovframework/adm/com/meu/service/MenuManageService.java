package  egovframework.adm.com.meu.service;

import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;

/** 
 * 메뉴관리에 관한 서비스 인터페이스 클래스를 정의한다.
 * @author 개발환경 개발팀 이용
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.20  이  용          최초 생성
 *
 * </pre>
 */

public interface MenuManageService {
    
	/**
	 * 관리자 메인메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectAdmTopMenuList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 관리자 서브메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List selectAdmLeftMenuList(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 관리자 네비게이터 타이틀조회
     * @param commandMap
     * @return
     * @throws Exception
     */
	Map getTitleInfo(Map<String, Object> commandMap) throws Exception;
	
	 
    /**
	 * 관리자 전체 메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    List selectAdmAllMenuList(Map<String, Object> commandMap ) throws Exception;
}