package egovframework.adm.tut.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.cfg.mem.dao.MemberSearchDAO;
import egovframework.adm.cfg.mng.dao.ManagerDAO;
import egovframework.adm.tut.dao.TutorDAO;
import egovframework.adm.tut.service.TutorService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("tutorService")
public class TutorServiceImpl extends EgovAbstractServiceImpl implements TutorService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="tutorDAO")
    private TutorDAO tutorDAO;
	
	@Resource(name="managerDAO")
    private ManagerDAO managerDAO;
	
	
	@Resource(name="memberSearchDAO")
    private MemberSearchDAO memberSearchDAO;
	
	
	/**
	 * 강사 리스트 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectTutorTotCnt(Map<String, Object> commandMap) throws Exception{
		return tutorDAO.selectTutorTotCnt(commandMap);
	}
	
	
	/**
	 * 강사 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTutorList(Map<String, Object> commandMap) throws Exception {
		return tutorDAO.selectTutorList(commandMap);
	}

	
	/**
	 * 강사 강의 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTutorSubjList(Map<String, Object> commandMap) throws Exception{
		return tutorDAO.selectTutorSubjList(commandMap);
	}
	
	/**
	 * 강사 강의 이력 리스트 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTutorSubjHistoryList(Map<String, Object> commandMap) throws Exception{
		return tutorDAO.selectTutorSubjHistoryList(commandMap);
	}
	
	/**
	 * 강사 정보 보기 화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectTutorView(Map<String, Object> commandMap) throws Exception{
		return tutorDAO.selectTutorView(commandMap);
	}
	
	/**
	 * 강사 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertTutor(Map<String, Object> commandMap) throws Exception{
		
		int isok = 0;
		
		// 강사 과정 리스트 
		String [] _Array_p_subj = (String [])commandMap.get("_Array_p_subj");
		
		// 강사권한 부여시 권한 등록
		String p_manager = (String)commandMap.get("p_manager");
		
		// 비회원 강사일 경우
		String p_saoi = (String)commandMap.get("p_saoi");
		
		try {
			//아이디 중복체크
			int cnt = tutorDAO.selectTutorUserIdCount(commandMap);
			if(cnt == 0)
			{
				Object o = tutorDAO.insertTutor(commandMap);
				
				//강사 과정 등록
				if(_Array_p_subj != null)
				{
					//강사과정 삭제
					tutorDAO.deleteTutorSubj(commandMap);
					
					
					for(int i=0; i<_Array_p_subj.length; i++)
					{
						String v_subj = _Array_p_subj[i];
						commandMap.put("v_subj", v_subj);
						
						o = tutorDAO.insertTutorSubj(commandMap);
					}
				}
				
	//			강사권한 일단 삭제
				managerDAO.deleteManager(commandMap);
				
				// 강사권한 부여시 권한 등록
	            if (p_manager.equals("Y")) {
	                // TZ_MANAGER 등록
	            	
	            	commandMap.put("p_gadminview", commandMap.get("p_gadmin"));
	            	commandMap.put("p_commented", "");
	            	
	            	int mcnt = managerDAO.checkManagerCount(commandMap);
	            	//중복체크
	            	if(mcnt == 0) managerDAO.managerInsert(commandMap);
	            	
	            	
	            }
	
	            // 비회원 강사일 경우
	            if (p_saoi.equals("2")) {
	                // Member TABLE에 Insert
	                // TZ_MEMBER 등록
	            	int mcnt = memberSearchDAO.selectExistId(commandMap);
	            	//중복체크
	            	if(mcnt == 0) memberSearchDAO.insertTutorInMember(commandMap);
	            }
	            	
	            isok = 99;
			}
			else
			{
				//중복됨..
				isok = 1;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 강사 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateTutor(Map<String, Object> commandMap) throws Exception{
		int isok = 0;
		
		// 강사 과정 리스트 
		String [] _Array_p_subj = (String [])commandMap.get("_Array_p_subj");
		
		// 강사권한 부여시 권한 등록
		String p_manager = (String)commandMap.get("p_manager");
		
		try {
			isok += tutorDAO.updateTutor(commandMap);
			
			//강사 과정 등록
			if(_Array_p_subj != null)
			{
				//강사과정 삭제
				isok += tutorDAO.deleteTutorSubj(commandMap);
				
				for(int i=0; i<_Array_p_subj.length; i++)
				{
					String v_subj = _Array_p_subj[i];
					commandMap.put("v_subj", v_subj);
					
					tutorDAO.insertTutorSubj(commandMap);
				}
			}
			
	//		강사권한 일단 삭제
			isok += managerDAO.deleteManager(commandMap);
			
			// 강사권한 부여시 권한 등록
	        if (p_manager.equals("Y")) {
	        	// TZ_MANAGER 등록
	        	commandMap.put("p_gadminview", commandMap.get("p_gadmin"));
	        	commandMap.put("p_commented", "");
	        	
	        	int mcnt = managerDAO.checkManagerCount(commandMap);
	        	//중복체크
	        	if(mcnt == 0) managerDAO.managerInsert(commandMap);
	        	
	        }
	
		}catch(Exception ex){
			ex.printStackTrace();
			 isok = 0;
		}
        
        return isok;
	}
	
	
	/**
	 * 강사 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteTutor(Map<String, Object> commandMap) throws Exception{
		int isok = 0;
		
		try {
//	강사삭제
			isok += tutorDAO.deleteTutor(commandMap);
			
//	강사과정 삭제
			isok += tutorDAO.deleteTutorSubj(commandMap);
				
//	강사권한 일단 삭제
			isok += managerDAO.deleteManager(commandMap);
	        
		}catch(Exception ex){
			ex.printStackTrace();
			 isok = 0;
		}
        
        return isok;
	}
	
	
}
