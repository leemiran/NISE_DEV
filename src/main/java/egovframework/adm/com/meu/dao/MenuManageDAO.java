package egovframework.adm.com.meu.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
/**
 * 메뉴관리, 메뉴생성, 사이트맵 생성에 대한 DAO 클래스를 정의한다.
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

@Repository("menuManageDAO")
public class MenuManageDAO extends EgovAbstractDAO{
	/**
	 * 관리자 메인메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectAdmTopMenuList(Map<String, Object> commandMap) throws Exception{
    	return list("menuManageDAO.selectAdmTopMenuList", commandMap);
    }
    
    /**
	 * 관리자 서브메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectAdmLeftMenuList(Map<String, Object> commandMap ) throws Exception{
    	return list("menuManageDAO.selectAdmLeftMenuList", commandMap);
    }
    
    /**
     * 관리자 네비게이터 타이틀조회
     * @param commandMap
     * @return
     * @throws Exception
     */
    public Map getTitleInfo(Map<String, Object> commandMap) throws Exception{
    	return (Map)selectByPk("menuManageDAO.getTitleInfo", commandMap);
    }
    
    /**
	 * 관리자 전체 메뉴 리스트조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
    public List selectAdmAllMenuList(Map<String, Object> commandMap ) throws Exception{
    	return list("menuManageDAO.selectAdmAllMenuList", commandMap);
    }
    
}