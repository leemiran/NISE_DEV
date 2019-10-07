package egovframework.adm.lcms.com.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.lcms.com.dao.CommonContentDAO;
import egovframework.adm.lcms.com.service.CommonContentService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("commonContentService")
public class CommonContentServiceImpl extends EgovAbstractServiceImpl implements CommonContentService{
	
	@Resource(name="commonContentDAO")
    private CommonContentDAO commonContentDAO;

	
	public Map selectMasterformData(Map<String, Object> commandMap) throws Exception{
		return commonContentDAO.selectMasterformData(commandMap);
	}
	
	public List selectMFMenuList(Map<String, Object> commandMap) throws Exception{
		return commonContentDAO.selectMFMenuList(commandMap);
	}
	
	public List selectMFSubjList(Map<String, Object> commandMap) throws Exception{
		return commonContentDAO.selectMFSubjList(commandMap);
	}
	
	public int updateMasterform(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		commonContentDAO.updateMasterform(commandMap);
		commonContentDAO.deleteMFSubjList(commandMap);
		
		String[] tmp = EgovStringUtil.getStringSequence(commandMap, "applyMenuList");
		String[] menu = new String[tmp.length];
		for( int i=0; i<tmp.length; i++ ){
			menu[i] = i+"@"+tmp[i];
		}
		commandMap.put("menu", menu);
		
		commonContentDAO.insertMFSubjList(commandMap);
		return isOk;
	}
	

}