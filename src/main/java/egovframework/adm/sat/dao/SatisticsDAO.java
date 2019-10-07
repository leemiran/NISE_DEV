package egovframework.adm.sat.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("satisticsDAO")
public class SatisticsDAO extends EgovAbstractDAO {

	
	/**
	 * 회원통계
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> memberSatisList(Map<String, Object> commandMap) throws Exception{
		return list("satisticsDAO.memberSatisList", commandMap);
	}
	
	/**
	 * 과정별 교육실적 - 상세
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> subjectSatisDetailList(Map<String, Object> commandMap) throws Exception{
		return list("satisticsDAO.subjectSatisDetailList", commandMap);
	}
	
	
	/**
	 * 연도별 교육실적
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> yearSubjectSatisList(Map<String, Object> commandMap) throws Exception{
		return list("satisticsDAO.yearSubjectSatisList", commandMap);
	}
	
	
	/**
	 * 분야별교육실적
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> classSubjectSatisList(Map<String, Object> commandMap) throws Exception{
		return list("satisticsDAO.classSubjectSatisList", commandMap);
	}
	
	/**
	 * 연도별 접속카운터
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> countYearSatisList(Map<String, Object> commandMap) throws Exception{
		return list("satisticsDAO.countYearSatisList", commandMap);
	}
	
	
	/**
	 * 월별 접속카운터
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> countMonthSatisList(Map<String, Object> commandMap) throws Exception{
		return list("satisticsDAO.countMonthSatisList", commandMap);
	}

	/**
	 * 연도별 회원 현황
	 * @param commandMap
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> yearMemberList(Map<String, Object> map) throws Exception{
		return list("satisticsDAO.yearMemberList_ver01", map);
	}

	/**
	 * 연도별 콘텐츠 보유 현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> yearContentsList(Map<String, Object> map) throws Exception{
		return list("satisticsDAO.yearContentsList", map);
	}
	
	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서)_이수현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> subjectResultReportList(Map<String, Object> map) throws Exception{
		return list("satisticsDAO.subjectResultReportList", map);
	}

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서(시도별))
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> subjectResultReportCommunityList(Map<String, Object> map) throws Exception{
		return list("satisticsDAO.subjectResultReportCommunityList", map);
	}

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서)_입금현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> subjectResultReporAmounttList(Map<String, Object> map) throws Exception{
		return list("satisticsDAO.subjectResultReporAmounttList", map);
	}
	
}
