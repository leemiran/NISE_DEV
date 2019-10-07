package egovframework.svt.adm.sat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminSatService {

	@Autowired
	AdminSatDAO adminSatDAO;
	
	public List<Map<String, Object>> popupYearContents(Map<String, Object> commandMap) {
		List<Map<String, Object>> yearContentsList = adminSatDAO.getYearContentsList(commandMap);
		return yearContentsList;
	}

	public List<?> yearMemberListByAddr(Map<String, Object> commandMap) {
		return adminSatDAO.getYearMemberListByAddr(commandMap);
	}

	public String getTotalMemberCnt() {
		return adminSatDAO.getTotalMemberCnt();
	}

	public List<?> yearMemberListByUpper(Map<String, Object> commandMap) {
		return adminSatDAO.getYearMemberListByUpper(commandMap);
	}

	public List<?> yearMemberListByHistory(Map<String, Object> commandMap) {
		return adminSatDAO.getYearMemberListByHistory(commandMap);
	}

	public List<?> yearMemberListByJob(Map<String, Object> commandMap) {
		return adminSatDAO.getYearMemberListByJob(commandMap);
	}

	public List<?> yearMemberList(Map<String, Object> commandMap) {
		return adminSatDAO.getYearMemberList(commandMap);
	}

	public List<?> yearMemberListByStat(Map<String, Object> commandMap) {
		return adminSatDAO.getYearMemberListByStat(commandMap);
	}

	public List<?> eduOffice(Map<String, Object> commandMap) {
		return adminSatDAO.getEduOffice(commandMap);
	}

	public List<?> eduOfficeDetail(Map<String, Object> commandMap) {
		return adminSatDAO.getEduOfficeDetail(commandMap);
	}

	public List<Map<String, Object>> popupEduOfficeIsu(Map<String, Object> commandMap) {
		List<Map<String, Object>> eduOfficeIsu = adminSatDAO.getEduOfficeIsu(commandMap);
		return eduOfficeIsu;
	}

	public String getAreaCodeName(Map<String, Object> commandMap) {
		return adminSatDAO.getAreaCodeName(commandMap);
	}

	public List<Map<String, Object>> score(Map<String, Object> commandMap) {
		return adminSatDAO.getScore(commandMap);
	}

	public List<Map<String, Object>> point(Map<String, Object> commandMap) {
		return adminSatDAO.getPoint(commandMap);
	}
	
	public List<?> cursBunryuJob(Map<String, Object> commandMap) {
		return adminSatDAO.cursBunryuJob(commandMap);
	}
	
}
