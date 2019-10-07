package egovframework.svt.adm.hom.calendar;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class AdminCalendarDAO extends EgovAbstractDAO {

	public List<?> getCalendarList() {
		return list("adminCalendar.getCalendarList", null);
	}

	public void insertCalendar(Map<String, Object> commandMap) {
		insert("adminCalendar.insertCalendar", commandMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCalendarDetail(String trainSeq) {
		return (Map<String, String>) selectByPk("adminCalendar.getCalendarDetail", trainSeq);
	}

	public int updateCalendar(Map<String, Object> commandMap) {
		return update("adminCalendar.updateCalendar", commandMap);
	}
	
	public int deleteCalendar(Map<String, Object> commandMap) {
		return delete("adminCalendar.deleteCalendar", commandMap);
	}
	
	public int deleteCalendarPeriodFromCalendar(Map<String, Object> commandMap) {
		return delete("adminCalendar.deleteCalendarPeriodFromCalendar", commandMap);
	}

	public List<?> getCalendarPeriodList(Map<String, Object> commandMap) {
		return list("adminCalendar.getCalendarPeriodList", commandMap);
	}
	
	public void insertCalendarPeriod(Map<String, Object> commandMap) {
		insert("adminCalendar.insertCalendarPeriod", commandMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCalendarPeriodDetail(String trainSeq) {
		return (Map<String, String>) selectByPk("adminCalendar.getCalendarPeriodDetail", trainSeq);
	}

	public int updateCalendarPeriod(Map<String, Object> commandMap) {
		return update("adminCalendar.updateCalendarPeriod", commandMap);
	}
	
	public int deleteCalendarPeriod(Map<String, Object> commandMap) {
		return delete("adminCalendar.deleteCalendarPeriod", commandMap);
	}

	public Map<String, String> getCalendarTitle() {
		return (Map<String, String>) selectByPk("adminCalendar.getCalendarTitle", null);
	}

	public int updateCalendarTitle(Map<String, Object> commandMap) {
		return update("adminCalendar.updateCalendarTitle", commandMap);
	}

}
