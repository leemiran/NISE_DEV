package egovframework.usr.hom.service.Impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.hom.dao.PortalActionMainDAO;
import egovframework.usr.hom.service.PortalActionMainService;

@Service("portalActionMainService")
public class PortalActionMainServiceImpl extends EgovAbstractServiceImpl implements PortalActionMainService{
	
	@Resource(name="portalActionMainDAO")
    private PortalActionMainDAO portalActionMainDAO;

	/**
	 * 권한 셀렉트 박스
	 * @param commandMap
	 * @param issuccess
	 * @return
	 * @throws Exception
	 */
	public List selectAuthList(Map<String, Object> commandMap) throws Exception{
		return portalActionMainDAO.selectAuthList(commandMap);
	}

	
	
	/**
     * 공지 팝업리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectNoticePopUpList(Map<String, Object> commandMap) throws Exception{
    	return portalActionMainDAO.selectNoticePopUpList(commandMap);
    }
    
    
    /**
     * 최근 공지 게시물리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectNoticeTopList(Map<String, Object> commandMap) throws Exception{
    	return portalActionMainDAO.selectNoticeTopList(commandMap);
    }
    
    /**
     * 전체과정리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectTrainingList(Map<String, Object> commandMap) throws Exception{
    	return portalActionMainDAO.selectTrainingList(commandMap);
    }
    
}
