package egovframework.svt.adm.sat;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class AdminSatDAO extends EgovAbstractDAO {

	public List<Map<String, Object>> getYearContentsList(Map<String, Object> commandMap) {
		return list("adminSat.getYearContentsList", commandMap);
	}

	public List<?> getYearMemberListByAddr(Map<String, Object> commandMap) {
		return list("adminSat.getYearMemberListByAddr", commandMap);
	}

	public String getTotalMemberCnt() {
		return (String) selectByPk("adminSat.getTotalMemberCnt", null);
	}

	public List<?> getYearMemberListByUpper(Map<String, Object> commandMap) {
		return list("adminSat.getYearMemberListByUpper", commandMap);
	}

	public List<?> getYearMemberListByHistory(Map<String, Object> commandMap) {
		return list("adminSat.getYearMemberListByHistory", commandMap);
	}

	public List<?> getYearMemberListByJob(Map<String, Object> commandMap) {
		return list("adminSat.getYearMemberListByJob", commandMap);
	}

	public List<?> getYearMemberList(Map<String, Object> commandMap) {
		return list("adminSat.getYearMemberList", commandMap);
	}

	public List<?> getYearMemberListByStat(Map<String, Object> commandMap) {
		return list("adminSat.getYearMemberListByStat", commandMap);
	}

	public List<?> getEduOffice(Map<String, Object> commandMap) {
		return list("adminSat.getEduOffice", commandMap);
	}

	public List<?> getEduOfficeDetail(Map<String, Object> commandMap) {
		return list("adminSat.getEduOfficeDetail", commandMap);
	}

	public List<Map<String, Object>> getEduOfficeIsu(Map<String, Object> commandMap) {
		return list("adminSat.getEduOfficeIsu", commandMap);
	}

	public String getAreaCodeName(Map<String, Object> commandMap) {
		return (String) selectByPk("adminSat.getAreaCodeName", commandMap);
	}

	public List<Map<String, Object>> getScore(Map<String, Object> commandMap) {
		return list("adminSat.getScore", commandMap);
	}

	public List<Map<String, Object>> getPoint(Map<String, Object> commandMap) {
		return list("adminSat.getPoint", commandMap);
	}
	
	public List<?> cursBunryuJob(Map<String, Object> commandMap) {
		return list("adminSat.cursBunryuJob", commandMap);
	}
	
}
