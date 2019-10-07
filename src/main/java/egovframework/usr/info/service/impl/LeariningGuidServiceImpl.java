package egovframework.usr.info.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.adm.cfg.mng.dao.ManagerDAO;
import egovframework.adm.sta.log.dao.LoginLogDAO;
import egovframework.com.cmm.service.Globals;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.svt.valid.ValidService;
import egovframework.usr.lgn.dao.LoginDAO;
import egovframework.usr.lgn.service.LoginService;
import egovframework.usr.info.service.LeariningGuidService;
import egovframework.usr.info.dao.LeariningGuidDAO;

@Service("leariningGuidService")
public class LeariningGuidServiceImpl extends EgovAbstractServiceImpl implements LeariningGuidService{
	
	@Resource(name="loginDAO")
    private LoginDAO loginDAO;
	
	@Resource(name="loginLogDAO")
	private LoginLogDAO loginLogDAO;
	
	@Resource(name="leariningGuidDAO")
    private LeariningGuidDAO leariningGuidDAO;
	
	@Autowired
	ValidService validService;
	

	public List<?> selectEduTrainingScheduleList(Map<String, Object> commandMap) throws Exception {
		return leariningGuidDAO.selectEduTrainingScheduleList(commandMap);
	}
	
	
	public List selectEduTrainingFileList(Map<String, Object> commandMap) throws Exception{
		return leariningGuidDAO.selectEduTrainingFileList(commandMap);
	}

	public List<?> selectEduTrainingCourseList(Map<String, Object> commandMap) throws Exception {
		return leariningGuidDAO.selectEduTrainingCourseList(commandMap);
	}
	
	public Map<?, ?> selectEduTrainingView(Map<String, Object> commandMap) throws Exception {
		return leariningGuidDAO.selectEduTrainingView(commandMap);
	}
	
}
