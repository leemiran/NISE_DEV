package egovframework.usr.stu.std.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("studyQnaDAO")
public class StudyQnaDAO extends EgovAbstractDAO{

	public int selectSQTableseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("studyQnaDAO.selectSQTableseq", commandMap);
	}
}
