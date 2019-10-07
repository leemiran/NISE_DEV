package egovframework.usr.hom.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface PortalActionMainService {

	public List selectAuthList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
     * 공지 팝업리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectNoticePopUpList(Map<String, Object> commandMap) throws Exception;
    
    
    /**
     * 최근 공지 게시물리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectNoticeTopList(Map<String, Object> commandMap) throws Exception;
    
    /**
     * 전체과정리스트
     * @param commandMap
     * @return
     * @throws Exception
     */
    public List selectTrainingList(Map<String, Object> commandMap) throws Exception;
    
    
}
