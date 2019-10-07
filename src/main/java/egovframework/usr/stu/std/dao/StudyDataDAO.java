package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyDataDAO")
public class StudyDataDAO extends EgovAbstractDAO{

	public int selectSDTableseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyDataDAO.selectSDTableseq", commandMap);
	}
}
