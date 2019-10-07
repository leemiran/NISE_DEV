package egovframework.usr.hom.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("portalActionMainDAO")
public class PortalActionMainDAO extends EgovAbstractDAO{

	/**
     * 권한 셀렉트 박스
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectAuthList(Map<String, Object> commandMap) throws Exception{
    	return list("portalActionMainDAO.selectAuthList", commandMap);
    }
    
    
    /**
     * 공지 팝업리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectNoticePopUpList(Map<String, Object> commandMap) throws Exception{
    	return list("portalActionMainDAO.selectNoticePopUpList", commandMap);
    }
    
    
    /**
     * 최근 공지 게시물리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectNoticeTopList(Map<String, Object> commandMap) throws Exception{
    	return list("portalActionMainDAO.selectNoticeTopList", commandMap);
    }
    
    /**
    * 전체과정리스트
    * @param commandMap
    * @return
    * @throws Exception
    */
   public List selectTrainingList(Map<String, Object> commandMap) throws Exception{
   		return list("portalActionMainDAO.selectTrainingList", commandMap);
   }












}
