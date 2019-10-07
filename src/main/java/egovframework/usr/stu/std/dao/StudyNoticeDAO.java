package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyNoticeDAO")
public class StudyNoticeDAO extends EgovAbstractDAO{

	public List selectListGong(Map<String, Object> commandMap) throws Exception{
		return list("studyNoticeDAO.selectListGong", commandMap);
	}
	
	public List selectListGongAll_H(Map<String, Object> commandMap) throws Exception{
		return list("studyNoticeDAO.selectListGongAll_H", commandMap);
	}
	
	public int updateViewCount(Map<String, Object> commandMap) throws Exception{
		return update("studyNoticeDAO.updateViewCount", commandMap);
	}
	
	public Map selectViewGong(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("studyNoticeDAO.selectViewGong", commandMap);
	}
}
