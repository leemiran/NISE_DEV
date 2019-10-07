package egovframework.usr.lgn.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

	public int checkPortalYn(Map<String, Object> commandMap) throws Exception;
	
	public int login(HttpServletRequest request, Map<String, Object> commandMap) throws Exception;
	
	public int tutorLogout(Map<String, Object> commandMap) throws Exception;
	
	/**
     * 기존 비밀번호와 일치하는지를 개수로 가져온다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int userOldPwdCount(Map<String, Object> commandMap) throws Exception;
    
    /**
     * 비밀번호와 비밀번호 변경일자를 업데이트 한다.
     * @param commandMap
     * @return
     * @throws Exception
     */
    public int updateUserPasswdChange(Map<String, Object> commandMap) throws Exception;

	public int actionCrtfctLogin(HttpServletRequest request, Map<String, Object> commandMap) throws Exception;

	public String selectUserIdNo(Map<String, Object> commandMap) throws Exception;

	public int updateDn(Map<String, Object> commandMap) throws Exception;
	
	public int checkDormantYn(Map<String, Object> commandMap) throws Exception;
	
	public int actionCrtfctLoginHtml5(HttpServletRequest request, Map<String, Object> commandMap) throws Exception; 
	
}
