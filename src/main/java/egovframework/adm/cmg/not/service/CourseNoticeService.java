package egovframework.adm.cmg.not.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CourseNoticeService {

	public List selectcourseNoticeList(Map<String, Object> commandMap) throws Exception;
	
	public List selectNoticeSubList(Map<String, Object> commandMap) throws Exception;
	
	public Map selectNoticeView(Map<String, Object> commandMap) throws Exception;
	
	public int deleteNoticeData(Map<String, Object> commandMap) throws Exception;
	
	public int updateNoticeData(Map<String, Object> commandMap) throws Exception;
	
	public int insertNoticeData(Map<String, Object> commandMap) throws Exception;
	
}
