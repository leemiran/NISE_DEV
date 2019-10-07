package egovframework.adm.cmg.sab.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface StudyAdminBoardService {

	public List selectAdminList(Map<String, Object> commandMap) throws Exception;
}
