package egovframework.adm.cfg.ccm.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cfg.ccm.dao.CourseClassificationDAO;
import egovframework.adm.cfg.ccm.service.CourseClassificationService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("courseClassificationService")
public class CourseClassificationServiceImpl extends EgovAbstractServiceImpl implements CourseClassificationService{
	
	@Resource(name="courseClassificationDAO")
    private CourseClassificationDAO courseClassificationDAO;

	/**
	 * 과정분류 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCourseClassificationList(Map<String, Object> commandMap) throws Exception{
		return courseClassificationDAO.selectCourseClassificationList(commandMap);
	}
	
	/**
	 * 과정분류 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateCourseClassification(Map<String, Object> commandMap) throws Exception{
		return courseClassificationDAO.updateCourseClassification(commandMap);
	}
	
	/**
	 * 과정분류 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteCourseClassification(Map<String, Object> commandMap) throws Exception{
		return courseClassificationDAO.deleteCourseClassification(commandMap);
	}
	
	public String getNewClassCode(Map<String, Object> commandMap) throws Exception{
		
		String code = ((String)commandMap.get("p_classtype")).equals("upper") ? "A00" : ((String)commandMap.get("p_classtype")).equals("middle") ? "B00" : "C00";
		commandMap.put("p_code", code);
		
		return courseClassificationDAO.getNewClassCode(commandMap);
	}
	
	public List selectClassList(Map<String, Object> commandMap) throws Exception{
		return courseClassificationDAO.selectClassList(commandMap);
	}
	
	public int insertCourseClassification(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		String type = (String)commandMap.get("p_classtype");
		String classCode = (String)commandMap.get("p_classcode");
		if( type.equals("upper") ){
			commandMap.put("p_upperclass", classCode);
			commandMap.put("p_middleclass", "000");
			commandMap.put("p_lowerclass", "000");
		}else if( type.equals("middle") ){
			commandMap.put("p_middleclass", classCode);
			commandMap.put("p_lowerclass", "000");
		}else{
			commandMap.put("p_lowerclass", classCode);
		}
		String subjclass = (String)commandMap.get("p_upperclass")+(String)commandMap.get("p_middleclass")+(String)commandMap.get("p_lowerclass");
		commandMap.put("p_subjclass", subjclass);
		courseClassificationDAO.insertCourseClassification(commandMap);
		return isOk;
	}

}
