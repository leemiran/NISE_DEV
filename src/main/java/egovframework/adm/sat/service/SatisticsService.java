package egovframework.adm.sat.service;

import java.util.List;
import java.util.Map;

public interface SatisticsService {
	/**
	 * 회원통계
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> memberSatisList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 과정별 교육실적 - 상세
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> subjectSatisDetailList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 연도별 교육실적
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> yearSubjectSatisList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 분야별교육실적
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	List<?> classSubjectSatisList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 연도별 접속카운터
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> countYearSatisList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 월별 접속카운터
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> countMonthSatisList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 연도별 회원 현황
	 * @param commandMap
	 * @return List<?>
	 * @throws Exception
	 */
	List<?> yearMemberList(Map<String, Object> map) throws Exception;

	/**
	 * 연도별 콘텐츠 보유 현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	List<?> yearContentsList(Map<String, Object> map) throws Exception;

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서)_이수현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	List<?> subjectResultReportList(Map<String, Object> map) throws Exception;

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서(시도별))
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	List<?> subjectResultReportCommunityList(Map<String, Object> map) throws Exception;

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서)_입금현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	List<?> subjectResultReporAmounttList(Map<String, Object> map) throws Exception;
	
}
