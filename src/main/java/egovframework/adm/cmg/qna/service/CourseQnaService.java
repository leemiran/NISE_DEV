package egovframework.adm.cmg.qna.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface CourseQnaService {

	public List selectAdminList(Map<String, Object> commandMap) throws Exception;
}
