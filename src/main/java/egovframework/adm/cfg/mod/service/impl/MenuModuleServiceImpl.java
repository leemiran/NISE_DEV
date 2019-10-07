package egovframework.adm.cfg.mod.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.cfg.mod.dao.MenuModuleDAO;
import egovframework.adm.cfg.mod.service.MenuModuleService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("menuModuleService")
public class MenuModuleServiceImpl extends EgovAbstractServiceImpl implements MenuModuleService{

	/** log */
	protected static final Log log = LogFactory.getLog(MenuModuleServiceImpl.class);

	@Resource(name="menuModuleDAO")
    private MenuModuleDAO menuModuleDAO;
	
	/**
	 * 메뉴모듈 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List menuModuleList(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.menuModuleList(commandMap);
	}
	
	/**
	 * 모듈 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List menuModuleSubList(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.menuModuleSubList(commandMap);
	}
	
	/**
	 * 메뉴코드명 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public String getMenuName(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.getMenuName(commandMap);
	}
	
	/**
	 * 모듈 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map getModuleInfo(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.getModuleInfo(commandMap);
	}
	
	/**
	 * 모듈 관한리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectMenuAuthList(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.selectMenuAuthList(commandMap);
	}
	
	/**
	 * 모듈정보 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateMenuModule(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			List list = menuModuleDAO.selectMenuAuthList(commandMap);
			
			menuModuleDAO.updateMenuModuleSub(commandMap);
			menuModuleDAO.deleteMenuModuleAuth(commandMap);
			
			for( int i=0; i<list.size(); i++ ){
				Map m = (Map)list.get(i);
				String gadmin = (String)m.get("gadmin");
				log.info("gadmin : "+gadmin);
				String rControl = commandMap.get("p_"+gadmin+"R") == null ? "" : "r";
				String wControl = commandMap.get("p_"+gadmin+"W") == null ? "" : "w";
				log.info(rControl+wControl);
				commandMap.put("p_gadmin", gadmin);
				commandMap.put("p_control", rControl+wControl);
				menuModuleDAO.insertMenuModuleAuth(commandMap);
			}
		}catch(Exception ex){
			isOk = 0;
		}
		return isOk; 
	}
	
	/**
	 * 모듈정보 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteMenuModule(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			menuModuleDAO.deleteMenuModuleSub(commandMap);
			menuModuleDAO.deleteMenuModuleAuth(commandMap);
			menuModuleDAO.deleteMenuModuleProcess(commandMap);
		}catch(Exception ex){
			isOk = 0;
		}
		return isOk; 
	}
	
	/**
	 * 모듈정보 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertModule(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String seq = menuModuleDAO.getSeqKey(commandMap);
			commandMap.put("p_seq", seq);
			menuModuleDAO.insertModuleSub(commandMap);
			String[] gadmin = (String[])commandMap.get("p_gadmin");
			for( int i=0; i<gadmin.length; i++ ){
				commandMap.remove("p_gadmin");
				commandMap.remove("p_control");
				commandMap.put("p_gadmin", gadmin[i]);
				String r = commandMap.get("p_"+gadmin[i]+"R") == null ? "" : "r";
				String w = commandMap.get("p_"+gadmin[i]+"W") == null ? "" : "w";
				commandMap.put("p_control", r+w);
				menuModuleDAO.insertMenuModuleAuth(commandMap);
				
			}
		}catch(Exception ex ){
			isOk = 0;
		}
		return isOk;
	}
	
	/**
	 * 프로세스 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectProcessList(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.selectProcessList(commandMap);
	}
	
	/**
	 * 프로세스 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map selectProcessInfo(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.selectProcessInfo(commandMap);
	}
	
	/**
	 * 프로세스 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateProcess(Map<String, Object> commandMap) throws Exception{
		return menuModuleDAO.updateProcess(commandMap);
	}
	
	/**
	 * 프로세스 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertProcess(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			menuModuleDAO.insertProcess(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk; 
	}
}
