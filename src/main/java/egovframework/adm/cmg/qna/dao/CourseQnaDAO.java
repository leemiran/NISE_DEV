package egovframework.adm.cmg.qna.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("courseQnaDAO")
public class CourseQnaDAO extends EgovAbstractDAO{

	public List selectAdminList(Map<String, Object> commandMap) throws Exception{
		return list("courseQnaDAO.selectAdminList", commandMap);
	}
}
