package egovframework.svt.adm.hom.calendar;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;

@Service
public class AdminCalendarService {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminCalendarService.class);
	
	@Autowired
	AdminCalendarDAO adminCalendarDAO;
	
	@Autowired
	EgovMessageSource egovMessageSource;
	
	public List<?> calendarList() {
		return adminCalendarDAO.getCalendarList();
	}

	public Map<String, String> insertCalendar(Map<String, Object> commandMap) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		adminCalendarDAO.insertCalendar(commandMap);
		resultMap.put("resultCode", "1");
		resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.insert"));
		return resultMap;
	}

	public Map<String, String> calendarDetail(String calendarSeq) {
		return adminCalendarDAO.getCalendarDetail(calendarSeq);
	}

	public Map<String, String> updateCalendar(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int updateCnt = adminCalendarDAO.updateCalendar(commandMap);
		if(0 < updateCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.update"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.update"));
		}
		return resultMap;
	}
	
	public Map<String, String> deleteCalendar(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int deleteCnt = adminCalendarDAO.deleteCalendar(commandMap);
		adminCalendarDAO.deleteCalendarPeriodFromCalendar(commandMap);
		if(0 < deleteCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.delete"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
		}
		return resultMap;
	}

	public List<?> calendarPeriodList(Map<String, Object> commandMap) {
		return adminCalendarDAO.getCalendarPeriodList(commandMap);
	}
	
	public Map<String, String> insertCalendarPeriod(Map<String, Object> commandMap) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		adminCalendarDAO.insertCalendarPeriod(commandMap);
		resultMap.put("resultCode", "1");
		resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.insert"));
		return resultMap;
	}

	public Map<String, String> calendarPeriodDetail(String calendarPeriodSeq) {
		return adminCalendarDAO.getCalendarPeriodDetail(calendarPeriodSeq);
	}

	public Map<String, String> updateCalendarPeriod(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int updateCnt = adminCalendarDAO.updateCalendarPeriod(commandMap);
		if(0 < updateCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.update"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.update"));
		}
		return resultMap;
	}
	
	public Map<String, String> deleteCalendarPeriod(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();

		int deleteCnt = adminCalendarDAO.deleteCalendarPeriod(commandMap);
		if(0 < deleteCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.delete"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
		}
		return resultMap;
	}

	public Map<String, String> calendarTitle() {
		return adminCalendarDAO.getCalendarTitle();
	}

	public Map<String, String> updateCalendarTitle(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int updateCnt = adminCalendarDAO.updateCalendarTitle(commandMap);
		if(0 < updateCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.update"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.update"));
		}
		return resultMap;
	}
}
