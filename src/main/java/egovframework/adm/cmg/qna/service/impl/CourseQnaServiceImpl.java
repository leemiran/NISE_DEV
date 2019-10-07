package egovframework.adm.cmg.qna.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cmg.qna.dao.CourseQnaDAO;
import egovframework.adm.cmg.qna.service.CourseQnaService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("courseQnaService")
public class CourseQnaServiceImpl extends EgovAbstractServiceImpl implements CourseQnaService{
	
	@Resource(name="courseQnaDAO")
    private CourseQnaDAO courseQnaDAO;

	public List selectAdminList(Map<String, Object> commandMap) throws Exception{
		return courseQnaDAO.selectAdminList(commandMap);
	}
}
