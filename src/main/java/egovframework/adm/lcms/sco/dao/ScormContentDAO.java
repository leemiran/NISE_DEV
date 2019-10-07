package egovframework.adm.lcms.sco.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * 컨텐츠 대한 DAO 클래스를 정의한다.
 */

@Repository("scormContentDAO")
public class ScormContentDAO extends EgovAbstractDAO{

	/**
	 * 목록을 조회
	 * @param Map commandMap
	 * @return List
	 * @exception Exception
	 */
	public List selectScormContentList(Map<String, Object> commandMap) throws Exception{
		return list("scormContentDAO.selectScormContentList", commandMap);
	}	
	
	/**
	 * 목록 총건수를 조회한다.
	 * @param commandMap Map<String, Object> 
	 * @return int 
	 * @exception Exception
	 */
    public int selectScormContentListTotCnt(Map<String, Object> commandMap) {
        return (Integer)getSqlMapClientTemplate().queryForObject("scormContentDAO.selectScormContentListTotCnt", commandMap);
    }

    /**
     * 목록을 조회
     * @param Map commandMap
     * @return List
     * @exception Exception
     */
    public List selectScormNewScoPageList(Map<String, Object> commandMap) throws Exception{
    	return list("scormContentDAO.selectScormNewScoPageList", commandMap);
    }	
    
    /**
     * 차시 등록정보 조회
     * @param Map commandMap
     * @return List
     * @exception Exception
     */
    public Map selectScormCreateInfo(Map<String, Object> commandMap) throws Exception{
    	return (Map)selectByPk("scormContentDAO.selectScormCreateInfo", commandMap);
    }	
    
    
    public List selectFileBaseDirList(Map<String, Object> commandMap ) throws Exception{
    	return list("scormContentDAO.selectFileBaseDirList", commandMap);
    }
    
    public List selectProgressLogList(Map<String, Object> commandMap) throws Exception{
    	return list("scormContentDAO.selectProgressLogList", commandMap);
    }
    
    public int deleteTocLog(Map<String, Object> commandMap) throws Exception{
    	return delete("scormContentDAO.deleteTocLog", commandMap);
    }
 	
    public int deleteLog(Map<String, Object> commandMap) throws Exception{
    	return delete("scormContentDAO.deleteLog", commandMap);
    }
    
}