package egovframework.svt.adm.snd.templete;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;

@Service
public class AdminSndTempleteService {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminSndTempleteService.class);
	
	@Autowired
	AdminSndTempleteDAO adminSndTempleteDAO;
	
	@Autowired
	EgovMessageSource egovMessageSource;
	
	public List<?> templeteList() {
		return adminSndTempleteDAO.getTempleteList();
	}

	public Map<String, String> insertTemplete(Map<String, Object> commandMap) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		adminSndTempleteDAO.insertTemplete(commandMap);
		resultMap.put("resultCode", "1");
		resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.insert"));
		return resultMap;
	}

	public Map<String, String> templeteDetail(String templeteSeq) {
		return adminSndTempleteDAO.getTempleteDetail(templeteSeq);
	}

	public Map<String, String> updateTemplete(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int updateCnt = adminSndTempleteDAO.updateTemplete(commandMap);
		if(0 < updateCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.update"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.update"));
		}
		return resultMap;
	}
	
	public Map<String, String> deleteTemplete(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int deleteCnt = adminSndTempleteDAO.deleteTemplete(commandMap);
		if(0 < deleteCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.delete"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
		}
		return resultMap;
	}

}
