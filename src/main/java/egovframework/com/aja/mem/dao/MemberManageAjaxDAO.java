package egovframework.com.aja.mem.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

/**
 * 회원 관련 DAO 클래스를 정의한다.
 */

@Repository("memberManageAjaxDAO")
public class MemberManageAjaxDAO extends EgovAbstractDAO{

	/**
	 * 회원정보
	 * @param Map commandMap
	 * @return Object
	 * @exception Exception
	 */
	public Object selectMemberInfo(Map<String, Object> commandMap) throws Exception{
		return (Object)getSqlMapClientTemplate().queryForObject("memberManageAjaxDAO.selectMemberInfo", commandMap);
	}
	
	/**
	 * 회원정보에 임시비밀번호 여부 체크
	 * @param commandMap Map<String, Object> 
	 * @return int 
	 * @exception Exception
	 */
    public int updateMemberInfoPw(Map<String, Object> commandMap) {
        return update("memberManageAjaxDAO.updateMemberInfoPw", commandMap);
    }
    
    /**
	 * 임시비밀번호 변경 내역 저장
	 * @param commandMap Map<String, Object> 
	 * @return Object 
	 * @exception Exception
	 */
    public String insertPWInitialization(Map<String, Object> commandMap) {
        return (String)insert("memberManageAjaxDAO.insertPWInitialization", commandMap);
    }
    
    /**
	 * 회원정보
	 * @param Map commandMap
	 * @return Object
	 * @exception Exception
	 */
	public Object selectMemberSenderInfo(Map<String, Object> commandMap) throws Exception{
		return (Object)getSqlMapClientTemplate().queryForObject("memberManageAjaxDAO.selectMemberSenderInfo", commandMap);
	}
      	
}