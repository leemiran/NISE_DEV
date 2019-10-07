package egovframework.usr.stu.std.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.std.dao.StudyDataDAO;
import egovframework.usr.stu.std.service.StudyDataService;

@Service("studyDataService")
public class StudyDataServiceImpl extends EgovAbstractServiceImpl implements StudyDataService{
	
	@Resource(name="studyDataDAO")
    private StudyDataDAO studyDataDAO;

	public int selectSDTableseq(Map<String, Object> commandMap) throws Exception{
		return studyDataDAO.selectSDTableseq(commandMap);
	}
}
