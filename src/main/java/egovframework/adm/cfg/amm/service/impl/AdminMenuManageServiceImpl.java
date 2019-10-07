package egovframework.adm.cfg.amm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cfg.amm.dao.AdminMenuManageDAO;
import egovframework.adm.cfg.amm.service.AdminMenuManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("adminMenuManageService")
public class AdminMenuManageServiceImpl extends EgovAbstractServiceImpl implements AdminMenuManageService{

	@Resource(name="adminMenuManageDAO")
    private AdminMenuManageDAO adminMenuManageDAO;

	/**
	 * 운영메뉴관리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List adminMenuMngList(Map<String, Object> commandMap) throws Exception{
		if( commandMap.get("p_levels") == null ){
			commandMap.put("p_levels", 1);
		}
		return adminMenuManageDAO.adminMenuMngList(commandMap);
	}
	
	/**
	 * 운영메뉴관리 메뉴 상세정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map adminMenuMngView(Map<String, Object> commandMap) throws Exception{
		return adminMenuManageDAO.adminMenuMngView(commandMap);
	}
	
	/**
	 * 운영메뉴정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int adminMenuMngUpdate(Map<String, Object> commandMap) throws Exception{
		return adminMenuManageDAO.adminMenuMngUpdate(commandMap);
	}
	
	public Map getUpperInfo(Map<String, Object> commandMap) throws Exception{
		return adminMenuManageDAO.getUpperInfo(commandMap);
	}
	
	public int adminMenuMngInsert(Map<String, Object> commandMap) throws Exception{
		String menu = adminMenuManageDAO.getMenuKey(commandMap);
		commandMap.put("menu", menu);
		return adminMenuManageDAO.adminMenuMngInsert(commandMap);
	}
}
