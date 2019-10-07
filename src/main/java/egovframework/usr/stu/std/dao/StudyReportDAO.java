package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyReportDAO")
public class StudyReportDAO extends EgovAbstractDAO{

	public Map selectViewOrderStu(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyReportDAO.selectViewOrderStu", commandMap);
	}
	
	public Map selectProfData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyReportDAO.selectProfData", commandMap);
	}
	
	public List selectProfFiles(Map<String, Object> commandMap) throws Exception{
		return list("studyReportDAO.selectProfFiles", commandMap);
	}
	
	public int selectProfScore(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyReportDAO.selectProfScore", commandMap);
	}
	
	public int updateProf(Map<String, Object> commandMap) throws Exception{
		return update("studyReportDAO.updateProf", commandMap);
	}
	
	public void insertProf(Map<String, Object> commandMap) throws Exception{
		insert("studyReportDAO.insertProf", commandMap);
	}
}
