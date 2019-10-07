package egovframework.adm.cou.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.cou.dao.SubjectDAO;
import egovframework.adm.cou.service.SubjectService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("subjectService")
public class SubjectServiceImpl extends EgovAbstractServiceImpl implements SubjectService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="subjectDAO")
    private SubjectDAO subjectDAO;
	
	/**
	 * 과정목록 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSubjectList(Map<String, Object> commandMap)
			throws Exception {
		return subjectDAO.selectSubjectList(commandMap);
	}
	public List selectAreaList(Map<String, Object> commandMap)
			throws Exception {
		return subjectDAO.selectAreaList(commandMap);
	}
	
	/**
	 * 과정목록총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSubjectListTotCnt(Map<String, Object> commandMap)
			throws Exception {
		return subjectDAO.selectSubjectListTotCnt(commandMap);
	}
	
	/**
	 * 과정상세화면
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSubjectView(Map<String, Object> commandMap) throws Exception {
		return subjectDAO.selectSubjectView(commandMap);
	}
	
	/**
	 * 과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSubject(Map<String, Object> commandMap) throws Exception{
		Object obj = null;
		
		try{
			obj = subjectDAO.insertSubject(commandMap);
			
			commandMap.put("subj", obj);
			subjectDAO.insertGrpSubj(commandMap);
			subjectDAO.insertSubjContInfo(commandMap);
			
		}catch(Exception ex){
			obj = null;
			ex.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * 과정 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSubject(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{
			//과정수정
			isOk = subjectDAO.updateSubject(commandMap);
			
			//기수명수정하기
			isOk += subjectDAO.updateSubjSeq(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSubject(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{
			isOk = subjectDAO.deleteGrSubj(commandMap);
			isOk += subjectDAO.deleteBds(commandMap);
			isOk += subjectDAO.deletePreview(commandMap);
			isOk += subjectDAO.deleteSubject(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 이어보기 관련정보테이블
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertSubjContInfo(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{
			subjectDAO.insertSubjContInfo(commandMap);
			isOk = 1;
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 교육그룹 연결
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int insertGrpSubj(Map<String, Object> commandMap) throws Exception{
		int isOk = 0;
		try{
			subjectDAO.insertGrpSubj(commandMap);
			isOk = 1;
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	/**
	 * 나의관심과정 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectConcernInfoList(Map<String, Object> commandMap) throws Exception{
		return subjectDAO.selectConcernInfoList(commandMap);
	}
	
	
	/**
	 * 관심과정 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteConcernInfo(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		
		String [] _Array_p_checks = (String []) commandMap.get("_Array_p_checks");
		String p_userid = (String) commandMap.get("userid");
		
		try{
			if(_Array_p_checks != null)
			{
				for(int i=0; i<_Array_p_checks.length; i++)
				{
					HashMap mm = new HashMap();
					mm.put("p_subj", _Array_p_checks[i]);
					mm.put("p_userid", p_userid);
					
					subjectDAO.deleteConcernInfo(mm);
					
				}
				isOk = true;
			}
		}catch(Exception ex){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 관심과정 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertConcernInfo(Map<String, Object> commandMap) throws Exception{
		boolean isOk = false;
		
		String [] _Array_p_checks = (String []) commandMap.get("_Array_p_checks");
		String p_subj = (String) commandMap.get("p_subj");
		String p_userid = (String) commandMap.get("userid");
		
		
		try{
			
			//과정목록에서 저장
			if(_Array_p_checks != null)
			{
				for(int i=0; i<_Array_p_checks.length; i++)
				{
					HashMap mm = new HashMap();
					mm.put("p_subj", _Array_p_checks[i]);
					mm.put("p_userid", p_userid);
					
					int cnt = subjectDAO.selectConcernInfoCnt(mm);
					
//					등록이 안된 과정이면 등록한다.
					if(cnt == 0) subjectDAO.insertConcernInfo(mm);
					
				}
				isOk = true;
			}
//			과정 보기에서 저장
			else if(p_subj != null)
			{
				HashMap mm = new HashMap();
				mm.put("p_subj", p_subj);
				mm.put("p_userid", p_userid);
				
				int cnt = subjectDAO.selectConcernInfoCnt(mm);
				
//				등록이 안된 과정이면 등록한다.
				if(cnt == 0) subjectDAO.insertConcernInfo(mm);
				
				isOk = true;
			}
			
		}catch(Exception ex){
			isOk = false;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	/**
	 * 직무관련 -기타
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSubjEtcCount(Map<String, Object> commandMap)
			throws Exception {
		return subjectDAO.selectSubjEtcCount(commandMap);
	}
	
}
