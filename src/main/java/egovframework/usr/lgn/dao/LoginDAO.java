package egovframework.usr.lgn.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("loginDAO")
public class LoginDAO extends EgovAbstractDAO{

	
	/**
     * 회사별로 포탈 로그인 가능여부에 따라 제한을 둔다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int checkPortalYn(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("loginDAO.checkPortalYn", commandMap);
    }
    
    /**
     * 로그인 회원정보
     * @param commandMap
     * @return
     * @throws Exception
     */
    public Map login(Map<String, Object> commandMap ) throws Exception{
    	return (Map)selectByPk("loginDAO.login", commandMap);
    }
    
    /**
     * 최초 로그인 체크
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int selectLoginCount(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("loginDAO.selectLoginCount", commandMap);
    }
    
    public int updateLoginInfo(Map<String, Object> commandMap) throws Exception{
    	return update("loginDAO.updateLoginInfo", commandMap);
    }
    
    /**
     * 로그인정보 삭제
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int deleteLoginId(Map<String, Object> commandMap) throws Exception{
    	return delete("loginDAO.deleteLoginId", commandMap);
    }
    
    /**
     * 로그인정보 등록
     * @param commandMap
     * @throws Exception
     */
    public void insertLoginId(Map<String, Object> commandMap) throws Exception{
    	insert("loginDAO.insertLoginId", commandMap);
    }
    
    /**
     * 로그인 대상자 기록
     * @param commandMap
     * @throws Exception
     */
    public void insertLoginLog(Map<String, Object> commandMap) throws Exception{
    	insert("loginDAO.insertLoginLog", commandMap);
    }
    
    /**
     * 비밀번호 오류 회수 증가
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int updateLoginFailCount(Map<String, Object> commandMap) throws Exception{
    	return update("loginDAO.updateLoginFailCount", commandMap);
    }
    
    /**
     * 권한이 강사일 경우 강사 로그아웃 정보를 입력
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int tutorLogout(Map<String, Object> commandMap) throws Exception{
    	return update("loginDAO.tutorLogout", commandMap);
    }
    
    
    /**
     * 기존 비밀번호와 일치하는지를 개수로 가져온다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int userOldPwdCount(Map<String, Object> commandMap) throws Exception{
    	return (Integer)getSqlMapClientTemplate().queryForObject("loginDAO.userOldPwdCount", commandMap);
    }
    
    /**
     * 비밀번호와 비밀번호 변경일자를 업데이트 한다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int updateUserPasswdChange(Map<String, Object> commandMap) throws Exception{
    	return update("loginDAO.updateUserPasswdChange", commandMap);
    }

	public Map loginCert(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return (Map)selectByPk("loginDAO.loginCert", commandMap);
	}

	public String selectUserIdNo(Map<String, Object> commandMap) {
		return (String)getSqlMapClientTemplate().queryForObject("loginDAO.selectUserIdNo", commandMap);
	}

	public int updateDn(Map<String, Object> commandMap) {
		return update("loginDAO.updateDn_S", commandMap);
	}

	public Map epkiDnYn(Map<String, Object> commandMap) {
		return (Map)selectByPk("loginDAO.epkiDnYn", commandMap);
	}
    
	public int checkDormantYn(Map<String, Object> commandMap) {
		return (Integer)getSqlMapClientTemplate().queryForObject("loginDAO.checkDormantYn", commandMap);
	}
	
    
}
