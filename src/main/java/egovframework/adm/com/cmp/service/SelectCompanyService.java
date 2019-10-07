package egovframework.adm.com.cmp.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SelectCompanyService {
	
	public List getCompany(Map<String, Object> commandMap) throws Exception;
}
