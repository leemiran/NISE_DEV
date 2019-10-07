package egovframework.usr.stu.std.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyNoticeService {

	public List selectListGong(Map<String, Object> commandMap) throws Exception;
	
	public List selectListGongAll_H(Map<String, Object> commandMap) throws Exception;
	
	public Map selectViewGong(Map<String, Object> commandMap) throws Exception;
}
