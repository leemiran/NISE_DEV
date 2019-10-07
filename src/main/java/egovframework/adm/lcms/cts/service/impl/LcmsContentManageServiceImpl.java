package egovframework.adm.lcms.cts.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.lcms.cts.dao.LcmsContentManageDAO;
import egovframework.adm.lcms.cts.service.LcmsContentManageService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("lcmsContentManageService")
public class LcmsContentManageServiceImpl extends EgovAbstractServiceImpl implements LcmsContentManageService {

    @Resource(name="lcmsContentManageDAO")
    private LcmsContentManageDAO lcmsContentManageDAO;
	
	public String selectContentPath( Map<String, Object> commandMap ) throws Exception{
		return lcmsContentManageDAO.selectContentPath(commandMap);
	}
}
