package egovframework.adm.com.meu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
/* 엑셀 처리 POI lib */
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.adm.com.meu.service.MenuManageService;
import egovframework.adm.com.meu.dao.MenuManageDAO;

/** 
 * 메뉴목록관리, 생성, 사이트맵을 처리하는 비즈니스 구현 클래스를 정의한다.
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

@Service("menuManageService")
public class MenuManageServiceImpl extends EgovAbstractServiceImpl implements MenuManageService{

	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="menuManageDAO")
    private MenuManageDAO menuManageDAO;
	
	/**
	 * 관리자 메인메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectAdmTopMenuList(Map<String, Object> commandMap ) throws Exception{
		return menuManageDAO.selectAdmTopMenuList(commandMap);
	}
	
	/**
	 * 관리자 서브메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectAdmLeftMenuList(Map<String, Object> commandMap) throws Exception{
		return menuManageDAO.selectAdmLeftMenuList(commandMap);
	}
	
	/**
     * 관리자 네비게이터 타이틀조회
     * @param commandMap
     * @return
     * @throws Exception
     */
	public Map getTitleInfo(Map<String, Object> commandMap) throws Exception{
		return menuManageDAO.getTitleInfo(commandMap);
	}
	
	 
    /**
	 * 관리자 전체 메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectAdmAllMenuList(Map<String, Object> commandMap ) throws Exception{
    	return menuManageDAO.selectAdmAllMenuList(commandMap);
    }
}