package egovframework.com.tmp.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.tmp.dao.TempletManageDAO;
import egovframework.com.tmp.service.TempletManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("templetManageService")
public class TempletManageServiceImpl extends EgovAbstractServiceImpl implements TempletManageService{
	
	@Resource(name="templetManageDAO")
    private TempletManageDAO templetManageDAO;
	
	
	public String getGrcodeType(Map<String, Object> commandMap) throws Exception{
		return templetManageDAO.getGrcodeType(commandMap);
	}

}
