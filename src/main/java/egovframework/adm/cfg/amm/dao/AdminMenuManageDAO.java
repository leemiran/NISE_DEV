package egovframework.adm.cfg.amm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("adminMenuManageDAO")
public class AdminMenuManageDAO extends EgovAbstractDAO{

	/**
	 * 운영메뉴관리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List adminMenuMngList(Map<String, Object> commandMap) throws Exception{
		return list("adminMenuManageDAO.adminMenuMngList", commandMap);
	}
	
	/**
	 * 운영메뉴관리 메뉴 상세정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map adminMenuMngView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("adminMenuManageDAO.adminMenuMngView", commandMap);
	}
	
	/**
	 * 운영메뉴정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int adminMenuMngUpdate(Map<String, Object> commandMap) throws Exception{
		return update("adminMenuManageDAO.adminMenuMngUpdate", commandMap);
	}
	
	public Map getUpperInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("adminMenuManageDAO.getUpperInfo", commandMap);
	}
	
	public String getMenuKey(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("adminMenuManageDAO.getMenuKey", commandMap);
	}
	
	public int adminMenuMngInsert(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			insert("adminMenuManageDAO.adminMenuMngInsert", commandMap);
		}catch(Exception ex ){
			isOk = 0;
		}
		return isOk;
	}
}
