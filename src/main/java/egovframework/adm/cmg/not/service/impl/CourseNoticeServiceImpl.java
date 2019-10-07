package egovframework.adm.cmg.not.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cmg.not.dao.CourseNoticeDAO;
import egovframework.adm.cmg.not.service.CourseNoticeService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.log4j.Logger;

@Service("courseNoticeService")
public class CourseNoticeServiceImpl extends EgovAbstractServiceImpl implements CourseNoticeService{
	
	@Resource(name="courseNoticeDAO")
    private CourseNoticeDAO courseNoticeDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public List selectcourseNoticeList(Map<String, Object> commandMap) throws Exception{
		return courseNoticeDAO.selectcourseNoticeList(commandMap);
	}
	
	public List selectNoticeSubList(Map<String, Object> commandMap) throws Exception{
		
		String[] year = EgovStringUtil.getStringSequence(commandMap, "p_year");
		String[] subjseq = EgovStringUtil.getStringSequence(commandMap, "p_subjseq");
		
		commandMap.put("arrYear", year);
		commandMap.put("arrSubjseq", subjseq);
		
		return courseNoticeDAO.selectNoticeSubList(commandMap);
	}

	public Map selectNoticeView(Map<String, Object> commandMap) throws Exception{
		return courseNoticeDAO.selectNoticeView(commandMap);
	}
	
	public int deleteNoticeData(Map<String, Object> commandMap) throws Exception{
		return courseNoticeDAO.deleteNoticeData(commandMap);
	}
	
	public int updateNoticeData(Map<String, Object> commandMap) throws Exception{
		return courseNoticeDAO.updateNoticeData(commandMap);
	}
	
	public int insertNoticeData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		commandMap.put("seq", courseNoticeDAO.selectNextSeq(commandMap));
		Object result = courseNoticeDAO.insertNoticeData(commandMap);
		logger.info("INSERT >>>>>>>>> "+result);
		return 1;
	}
	
}