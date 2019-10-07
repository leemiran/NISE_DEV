package egovframework.adm.cmg.not.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("courseNoticeDAO")
public class CourseNoticeDAO extends EgovAbstractDAO{

	public List selectcourseNoticeList(Map<String, Object> commandMap) throws Exception{
		return list("courseNoticeDAO.selectcourseNoticeList", commandMap);
	}
	
	public List selectNoticeSubList(Map<String, Object> commandMap) throws Exception{
		return list("courseNoticeDAO.selectNoticeSubList", commandMap);
	}
	
	public Map selectNoticeView(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("courseNoticeDAO.selectNoticeView", commandMap);
	}
	
	public int deleteNoticeData(Map<String, Object> commandMap) throws Exception{
		return delete("courseNoticeDAO.deleteNoticeData", commandMap);
	}
	
	public int updateNoticeData(Map<String, Object> commandMap) throws Exception{
		return update("courseNoticeDAO.updateNoticeData", commandMap);
	}
	
	public int selectNextSeq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("courseNoticeDAO.selectNextSeq", commandMap);
	}
	
	public Object insertNoticeData(Map<String, Object> commandMap) throws Exception{
		return insert("courseNoticeDAO.insertNoticeData", commandMap);
	}
}
