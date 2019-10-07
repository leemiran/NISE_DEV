package egovframework.com.aja.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * AJAX 호출에 관한 공통 DAO 클래스를 정의한다.
 */

@Repository("commonAjaxManageDAO")
public class CommonAjaxManageDAO extends EgovAbstractDAO{

	/**
	 * 공통 조회 List
	 * @param Map commandMap
	 * @return List
	 * @exception Exception
	 */
	public List selectCommonAjaxManageList(Map<String, Object> commandMap, String sqlStr) throws Exception{
		return list(sqlStr, commandMap);
	}
	
	/**
	 * 공통 조회 int
	 * @param commandMap Map<String, Object> 
	 * @return int 
	 * @exception Exception
	 */
    public int selectCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) {
        return (Integer)getSqlMapClientTemplate().queryForObject(sqlStr, commandMap);
    }
    
    /**
     * 공통 Insert int
     * @param commandMap Map<String, Object> 
     * @return int 
     * @exception Exception
     */
    public void insertCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) {
    	insert(sqlStr, commandMap);
    }
    
    /**
     * 공통 Insert int
     * @param commandMap Map<String, Object> 
     * @return int 
     * @exception Exception
     */
    public void updateCommonAjaxManageInt(Map<String, Object> commandMap, String sqlStr) {
    	update(sqlStr, commandMap);
    }

	/**
	 * 공통 조회 Object
	 * @param commandMap Map<String, Object> 
	 * @return int 
	 * @exception Exception
	 */
    public Object selectCommonAjaxManageObject(Map<String, Object> commandMap, String sqlStr) {
        return getSqlMapClientTemplate().queryForObject(sqlStr, commandMap);
    }
    
    /**
     * 공통 조회 Map
     * @param commandMap Map<String, Object> 
     * @return int 
     * @exception Exception
     */
    public Map selectCommonAjaxManageMap(Map<String, Object> commandMap, String sqlStr) {
    	 return (Map)selectByPk(sqlStr, commandMap);
    }

	public int nicePersonalNumOverlap(Map<String, Object> mm, String sqlStr) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject(sqlStr, mm);
	}
	public Map nicePersonalChkValue(Map<String, Object> commandMap) throws Exception {
		return (Map)selectByPk("commonAjaxDAO.nicePersonalChkValue", commandMap);
	}
}