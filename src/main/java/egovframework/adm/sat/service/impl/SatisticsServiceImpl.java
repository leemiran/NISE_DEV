package egovframework.adm.sat.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.sat.dao.SatisticsDAO;
import egovframework.adm.sat.service.SatisticsService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("satisticsService")
public class SatisticsServiceImpl extends EgovAbstractServiceImpl implements SatisticsService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="satisticsDAO")
    private SatisticsDAO satisticsDAO;
	
	/**
	 * 회원통계
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> memberSatisList(Map<String, Object> commandMap) throws Exception {
		return satisticsDAO.memberSatisList(commandMap);
	}
	
	/**
	 * 과정별 교육실적 - 상세
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> subjectSatisDetailList(Map<String, Object> commandMap) throws Exception{
		return satisticsDAO.subjectSatisDetailList(commandMap);
	}
	
	/**
	 * 연도별 교육실적
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> yearSubjectSatisList(Map<String, Object> commandMap) throws Exception{
		return satisticsDAO.yearSubjectSatisList(commandMap);
	}
	
	
	/**
	 * 분야별교육실적
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> classSubjectSatisList(Map<String, Object> commandMap) throws Exception{
		return satisticsDAO.classSubjectSatisList(commandMap);
	}
	
	/**
	 * 연도별 접속카운터
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> countYearSatisList(Map<String, Object> commandMap) throws Exception{
		return satisticsDAO.countYearSatisList(commandMap);
	}
	
	
	/**
	 * 월별 접속카운터
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> countMonthSatisList(Map<String, Object> commandMap) throws Exception{
		return satisticsDAO.countMonthSatisList(commandMap);
	}

	/**
	 * 연도별 회원 현황
	 * @param commandMap
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> yearMemberList(Map<String, Object> map) throws Exception {
		return satisticsDAO.yearMemberList(map);
	}

	/**
	 * 연도별 콘텐츠 보유 현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> yearContentsList(Map<String, Object> map) throws Exception {
		return satisticsDAO.yearContentsList(map);
	}

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서)_이수현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> subjectResultReportList(Map<String, Object> map) 	throws Exception {
		return satisticsDAO.subjectResultReportList(map);
	}

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서(시도별))
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> subjectResultReportCommunityList(Map<String, Object> map) throws Exception {
		return satisticsDAO.subjectResultReportCommunityList(map);
	}

	/**
	 * 이수자관리 > 과정별결과보고서(결과보고서)_입금현황
	 * @param map
	 * @return List<?>
	 * @throws Exception
	 */
	public List<?> subjectResultReporAmounttList(Map<String, Object> map) throws Exception {
		return satisticsDAO.subjectResultReporAmounttList(map);
	}
}
