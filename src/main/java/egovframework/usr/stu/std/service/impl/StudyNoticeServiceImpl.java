package egovframework.usr.stu.std.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.usr.stu.std.dao.StudyNoticeDAO;
import egovframework.usr.stu.std.service.StudyNoticeService;
import org.apache.log4j.Logger;

@Service("studyNoticeService")
public class StudyNoticeServiceImpl extends EgovAbstractServiceImpl implements StudyNoticeService{
	
	@Resource(name="studyNoticeDAO")
    private StudyNoticeDAO studyNoticeDAO;

	public List selectListGong(Map<String, Object> commandMap) throws Exception{
		return studyNoticeDAO.selectListGong(commandMap);
	}
	
	public List selectListGongAll_H(Map<String, Object> commandMap) throws Exception{
		return studyNoticeDAO.selectListGongAll_H(commandMap);
	}
	
	public Map selectViewGong(Map<String, Object> commandMap) throws Exception{
		studyNoticeDAO.updateViewCount(commandMap);
		return studyNoticeDAO.selectViewGong(commandMap);
	}
}
