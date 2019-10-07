package egovframework.usr.stu.std.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.std.dao.StudyQnaDAO;
import egovframework.usr.stu.std.service.StudyQnaService;

@Service("studyQnaService")
public class StudyQnaServiceImpl extends EgovAbstractServiceImpl implements StudyQnaService{
	
	@Resource(name="studyQnaDAO")
    private StudyQnaDAO studyQnaDAO;

	public int selectSQTableseq(Map<String, Object> commandMap) throws Exception{
		return studyQnaDAO.selectSQTableseq(commandMap);
	}
}
