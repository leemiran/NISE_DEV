package egovframework.adm.cfg.mod.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("menuModuleDAO")
public class MenuModuleDAO extends EgovAbstractDAO{

	/**
	 * 메뉴모듈 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List menuModuleList(Map<String, Object> commandMap) throws Exception{
		return list("menuModuleDAO.menuModuleList", commandMap);
	}
	
	/**
	 * 모듈 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List menuModuleSubList(Map<String, Object> commandMap) throws Exception{
		return list("menuModuleDAO.menuModuleSubList", commandMap);
	}
	
	/**
	 * 메뉴코드명 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public String getMenuName(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("menuModuleDAO.getMenuName", commandMap);
	}
	
	/**
	 * 모듈 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map getModuleInfo(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("menuModuleDAO.getModuleInfo", commandMap);
	}
	
	/**
	 * 모듈 관한리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectMenuAuthList(Map<String, Object> commandMap ) throws Exception{
		return list("menuModuleDAO.selectMenuAuthList", commandMap);
	}
	
	/**
	 * 모듈 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuModuleSub(Map<String, Object> commandMap) throws Exception{
		return update("menuModuleDAO.updateMenuModuleSub", commandMap);
	}
	
	/**
	 * 모듈권한 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuModuleAuth(Map<String, Object> commandMap) throws Exception{
		return delete("menuModuleDAO.deleteMenuModuleAuth", commandMap);
	}
	
	/**
	 * 모듈권한 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertMenuModuleAuth(Map<String, Object> commandMap) throws Exception{
		insert("menuModuleDAO.insertMenuModuleAuth", commandMap);
	}
	
	/**
	 * 모듈 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuModuleSub(Map<String, Object> commandMap) throws Exception{
		return delete("menuModuleDAO.deleteMenuModuleSub", commandMap);
	}
	
	/**
	 * 모듈 프로세스삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuModuleProcess(Map<String, Object> commandMap) throws Exception{
		return delete("menuModuleDAO.deleteMenuModuleProcess", commandMap);
	}
	
	/**
	 * 모듈순번 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public String getSeqKey(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("menuModuleDAO.getSeqKey", commandMap);
	}
	
	/**
	 * 모듈 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertModuleSub(Map<String, Object> commandMap) throws Exception{
		insert("menuModuleDAO.insertModuleSub", commandMap);
	}
	
	/**
	 * 프로세스 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectProcessList(Map<String, Object> commandMap) throws Exception{
		return list("menuModuleDAO.selectProcessList", commandMap);
	}
	
	/**
	 * 프로세스 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map selectProcessInfo(Map<String, Object> commandMap ) throws Exception{
		return (Map)selectByPk("menuModuleDAO.selectProcessInfo", commandMap);
	}
	
	/**
	 * 프로세스 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateProcess(Map<String, Object>  commandMap) throws Exception{
		return update("menuModuleDAO.updateProcess", commandMap);
	}
	
	/**
	 * 프로세스 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public void insertProcess(Map<String, Object> commandMap) throws Exception{
		insert("menuModuleDAO.insertProcess", commandMap);
	}
}
