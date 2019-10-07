package egovframework.adm.cfg.aut.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.cfg.aut.controller.MenuAuthorityController;
import egovframework.adm.cfg.aut.dao.MenuAuthorityDAO;
import egovframework.adm.cfg.aut.service.MenuAuthorityService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("menuAuthorityService")
public class MenuAuthorityServiceImpl extends EgovAbstractServiceImpl implements MenuAuthorityService{
	
	/** log */
	protected static final Log log = LogFactory.getLog(MenuAuthorityServiceImpl.class);

	@Resource(name="menuAuthorityDAO")
    private MenuAuthorityDAO menuAuthorityDAO;
	
	public List selectListGadmin(Map<String, Object> commandMap) throws Exception{
		return menuAuthorityDAO.selectListGadmin(commandMap);
	}
	
	public List selectMenuAuthList(Map<String, Object> commandMap) throws Exception{
		return menuAuthorityDAO.selectMenuAuthList(commandMap);
	}
	
	public int menuAuthUpdate(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		
		List list = menuAuthorityDAO.selectListGadmin(commandMap);
		
		menuAuthorityDAO.menuAuthDelete(commandMap);
		
		String[] vecMenu;
		String[] vecMenuSubseq;
		String[] vecGadmin;
		for( int i=0; i<list.size(); i++ ){
			Map m = (Map)list.get(i);
			String gadmin = (String)m.get("gadmin");
			vecMenu = (String[])commandMap.get("p_menu"+gadmin);
			vecMenuSubseq = (String[])commandMap.get("p_menusubseq"+gadmin);
			vecGadmin = (String[])commandMap.get("p_gadmin"+gadmin);
			String[] param = new String[vecMenu.length];
			for( int j=0; j<vecMenu.length; j++ ){
				String strR = "";
				String strW = "";
				if( commandMap.get("p_"+gadmin+"R"+j) != null ) strR = (String)commandMap.get("p_"+gadmin+"R"+j);
				if( commandMap.get("p_"+gadmin+"W"+j) != null ) strW = (String)commandMap.get("p_"+gadmin+"W"+j);
				param[j] = vecMenu[j] + "@" + vecMenuSubseq[j] + "@" + vecGadmin[j] + "@" + strR+strW;
			}
			commandMap.put("param", param);
			menuAuthorityDAO.menuAuthInsert(commandMap);
		}
		
		return isOk;
	}

	
}
