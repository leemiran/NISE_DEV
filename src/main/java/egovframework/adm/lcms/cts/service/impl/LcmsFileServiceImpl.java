package egovframework.adm.lcms.cts.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.lcms.cts.dao.LcmsFileDAO;
import egovframework.adm.lcms.cts.service.LcmsFileService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("lcmsFileService")
public class LcmsFileServiceImpl extends EgovAbstractServiceImpl implements LcmsFileService {
	@Resource(name="lcmsFileDAO")
    private LcmsFileDAO lcmsFileDAO;
	
	public int insertLcmsFile(Map<String, Object> commandMap) throws Exception{
		return lcmsFileDAO.insertLcmsFile(commandMap);
	}

}
