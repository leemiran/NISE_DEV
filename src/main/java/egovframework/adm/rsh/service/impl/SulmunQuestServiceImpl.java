package egovframework.adm.rsh.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import egovframework.adm.rsh.dao.SulmunQuestDAO;
import egovframework.adm.rsh.service.SulmunQuestService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("sulmunQuestService")
public class SulmunQuestServiceImpl extends EgovAbstractServiceImpl implements SulmunQuestService{
	
	protected Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name="sulmunQuestDAO")
    private SulmunQuestDAO sulmunQuestDAO;
	
	/**
	 * 설문 문제 관리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> sulmunAllQuestList(Map<String, Object> commandMap) throws Exception {
		return sulmunQuestDAO.sulmunAllQuestList(commandMap);
	}
	
	/**
	 * 설문문제 정보
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectTzSulmunView(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectTzSulmunView(commandMap);
	}
	
	/**
	 * 설문문제답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectTzSulmunSelectList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectTzSulmunSelectList(commandMap);
	}
	
	/**
	 * 설문문제 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertTzSulmun(Map<String, Object> commandMap) throws Exception{
		
		
		boolean isok = false;
		try {
			
			
			
			String p_luserid = commandMap.get("userid") + "";
			String p_subj = commandMap.get("p_gubun") + "";
			String p_grcode = commandMap.get("p_grcode") + "";
			String p_distcode   = commandMap.get("p_distcode") + "";
			String p_sultype    = commandMap.get("p_sultype") + "";
			String p_sultext    = commandMap.get("p_sultext") + "";
			String p_sulreturn  = commandMap.get("p_sulreturn") + "";
			
			String p_selcount  = commandMap.get("p_selcount" + p_sultype) + "";
			String p_selmax  = commandMap.get("p_selmax" + p_sultype) + "";
			String p_scalecode  = commandMap.get("p_scalecode" + p_sultype) + "";
			
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("p_luserid", p_luserid);
			hm.put("p_subj", p_subj);
			hm.put("p_grcode", p_grcode);
			hm.put("p_distcode", p_distcode);
			hm.put("p_sultype", p_sultype);
			hm.put("p_sultext", p_sultext);
			hm.put("p_sulreturn", p_sulreturn);
			hm.put("p_selcount", p_selcount);
			hm.put("p_selmax", p_selmax);
			hm.put("p_scalecode", p_scalecode);
			
			
			//설문문제 등록
			Object p_sulnum = sulmunQuestDAO.insertTzSulmun(hm);
			
			if(p_sulnum != null)
			{
				//등록된 설문문제 코드 삽입
				hm.put("p_sulnum", p_sulnum);
				
				//설문 타입에 따라서 값을 가져온다.
				String[] _Array_p_seltext  = (String[])commandMap.get("_Array_p_seltext" + p_sultype);
				String[] _Array_p_selpoint  = (String[])commandMap.get("_Array_p_selpoint" + p_sultype);
				
				if(_Array_p_seltext != null)
				{
					for(int i=0; i<_Array_p_seltext.length; i++)
					{
						String p_seltext = _Array_p_seltext[i].trim();
						String p_selpoint = _Array_p_selpoint[i];
						
						//값이 있을때만
						if(p_seltext != null && !p_seltext.equals(""))
						{
							hm.put("p_selnum", i+1);
							hm.put("p_seltext", p_seltext);
							hm.put("p_selpoint", p_selpoint);
							
							sulmunQuestDAO.insertTzSulmunSelect(hm);
						}
					}
					
					isok = true;
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	
	/**
	 * 설문문제 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateTzSulmun(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			String p_luserid = commandMap.get("userid") + "";
			String p_sulnum = commandMap.get("p_sulnum") + "";
			String p_subj = commandMap.get("p_gubun") + "";
			String p_grcode = commandMap.get("p_grcode") + "";
			String p_distcode   = commandMap.get("p_distcode") + "";
			String p_sultype    = commandMap.get("p_sultype") + "";
			String p_sultext    = commandMap.get("p_sultext") + "";
			String p_sulreturn  = commandMap.get("p_sulreturn") + "";
			
			String p_selcount  = commandMap.get("p_selcount" + p_sultype) + "";
			String p_selmax  = commandMap.get("p_selmax" + p_sultype) + "";
			String p_scalecode  = commandMap.get("p_scalecode" + p_sultype) + "";
			
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("p_luserid", p_luserid);
			hm.put("p_sulnum", p_sulnum);
			hm.put("p_subj", p_subj);
			hm.put("p_grcode", p_grcode);
			hm.put("p_distcode", p_distcode);
			hm.put("p_sultype", p_sultype);
			hm.put("p_sultext", p_sultext);
			hm.put("p_sulreturn", p_sulreturn);
			hm.put("p_selcount", p_selcount);
			hm.put("p_selmax", p_selmax);
			hm.put("p_scalecode", p_scalecode);
			
			
//			설문문제수정
			int cnt = sulmunQuestDAO.updateTzSulmun(hm);
		
			if(cnt > 0)
			{
				//설문문제보기삭제
				sulmunQuestDAO.deleteTzSulmunSelect(hm);
				
				//설문문제보기등록
				//설문 타입에 따라서 값을 가져온다.
				String[] _Array_p_seltext  = (String[])commandMap.get("_Array_p_seltext" + p_sultype);
				String[] _Array_p_selpoint  = (String[])commandMap.get("_Array_p_selpoint" + p_sultype);
				
				if(_Array_p_seltext != null)
				{
					for(int i=0; i<_Array_p_seltext.length; i++)
					{
						String p_seltext = _Array_p_seltext[i].trim();
						String p_selpoint = _Array_p_selpoint[i];
						
						//값이 있을때만
						if(p_seltext != null && !p_seltext.equals(""))
						{
							hm.put("p_selnum", i+1);
							hm.put("p_seltext", p_seltext);
							hm.put("p_selpoint", p_selpoint);
							
							sulmunQuestDAO.insertTzSulmunSelect(hm);
						}
					}
					
					isok = true;
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 설문문제 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteTzSulmun(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			//설문지에서 사용되었는지를 확인한다.
			int cnt = sulmunQuestDAO.selectTzSulPaperCount(commandMap);
			
			if(cnt == 0)
			{
				String p_sulnum = commandMap.get("p_sulnum") + "";
				String p_subj = commandMap.get("p_gubun") + "";
				String p_grcode = commandMap.get("p_grcode") + "";
				
				HashMap<String, Object> hm = new HashMap<String, Object>();
				hm.put("p_sulnum", p_sulnum);
				hm.put("p_subj", p_subj);
				hm.put("p_grcode", p_grcode);
				
				
				//설문문제보기 삭제
				sulmunQuestDAO.deleteTzSulmunSelect(hm);
				//설문문제 삭제
				int delCnt = sulmunQuestDAO.deleteTzSulmun(hm);
				
				if(delCnt > 0) isok = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	
	
	
	
	
	
	/**
	 * 설문 척도 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectScaleList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectScaleList(commandMap);
	}
	
	/**
	 * 설문척도보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectScaleView(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectScaleView(commandMap);
	}
	
	/**
	 * 설문척도 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertScale(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			//척도 등록
			Object p_scalecode = sulmunQuestDAO.insertScale(commandMap);
			
			if(p_scalecode != null)
			{
				//등록된 설문척도 코드 삽입
				commandMap.put("p_scalecode", p_scalecode);
				//현척도의 보기를 등록한다.
				isok = this.insertScaleSelAction(commandMap);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	
	/**
	 * 설문척도 공통 등록 로직
	 * 설문척도 등록시, 수정시 사용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertScaleSelAction(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			int p_sgubun = Integer.parseInt(commandMap.get("p_sgubun")+"");
			String userid = commandMap.get("userid")+"";
			String p_scalecode = commandMap.get("p_scalecode")+"";
			
			
			//5점척도시
			String [] _Array_p_seltext1 = (String [])commandMap.get("_Array_p_seltext1");
			String [] _Array_p_selpoint1 = (String [])commandMap.get("_Array_p_selpoint1");
			
			//7점척도시
			String [] _Array_p_seltext2 = (String [])commandMap.get("_Array_p_seltext2");
			String [] _Array_p_selpoint2 = (String [])commandMap.get("_Array_p_selpoint2");
			
			
			for(int i=0; i<p_sgubun; i++)
			{
				HashMap<String, Object> mm = new HashMap<String, Object>();
				mm.put("p_scalecode", p_scalecode);
				mm.put("p_selnum", (i+1));
				mm.put("userid", userid);
				
				
				//7점척도
				if(p_sgubun > 5)
				{
					mm.put("p_seltext", _Array_p_seltext2[i]);
					mm.put("p_selpoint", _Array_p_selpoint2[i]);
				}
				//5점척도
				else
				{
					mm.put("p_seltext", _Array_p_seltext1[i]);
					mm.put("p_selpoint", _Array_p_selpoint1[i]);
				}
				
				//보기등록
				sulmunQuestDAO.insertScaleSel(mm);
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	
	
	/**
	 * 설문척도 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateScale(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			int cnt = sulmunQuestDAO.updateScale(commandMap);
		
			if(cnt > 0)
			{
				//보기삭제
				sulmunQuestDAO.deleteScaleSel(commandMap);
				//보기등록
				isok = this.insertScaleSelAction(commandMap);
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
		
	}
	
	/**
	 * 설문척도 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteScale(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			//설문에서 사용되었는지를 확인한다.
			int cnt = sulmunQuestDAO.selectSulScaleCount(commandMap);
			
			if(cnt == 0)
			{
				//보기 삭제
				sulmunQuestDAO.deleteScaleSel(commandMap);
				//척도 삭제
				int delCnt = sulmunQuestDAO.deleteScale(commandMap);
				
				if(delCnt > 0) isok = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 설문지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulPaperList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectSulPaperList(commandMap);
	}
	
	/**
	 * 설문지 전체 문제 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulPaperAllQuestList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectSulPaperAllQuestList(commandMap);
	}
	
	
	/**
	 * 설문지 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertSulPaper(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			Object obj = sulmunQuestDAO.insertSulPaper(commandMap);
			
			if(obj != null) isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 설문지 수정
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateSulPaper(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			int cnt = sulmunQuestDAO.updateSulPaper(commandMap);
		
			if(cnt > 0)
			{
				isok = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 설문지 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSulPaper(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			//과정에서 사용되어 졌는지를 검사한다.
			int cnt = sulmunQuestDAO.selectSulPaperCount(commandMap);
		
			if(cnt == 0)
			{
				//설문지삭제
				sulmunQuestDAO.deleteSulPaper(commandMap);
				isok = true;
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}

	
	/**
	 * 설문지 미리보기 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulPaperPreviewList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectSulPaperPreviewList(commandMap);
	}
	
	/**
	 * 과정의 대한 설문 문제 정보 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulResultSulNumsList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectSulResultSulNumsList(commandMap);
	}
	
	
	/**
	 * 설문 학습자 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectSulResultAnswersList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectSulResultAnswersList(commandMap);
	}
	
	/**
	 * 설문의 대한 수강생 전체인원수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSulStudentMemberCount(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectSulStudentMemberCount(commandMap);
	}

	/**
	 * 교육후기 리스트
	 */
	public List<?> selectHukiList(Map<String, Object> commandMap)
			throws Exception {
		return sulmunQuestDAO.selectHukiList(commandMap);
	}
	
	/**
	 * 표준편차
	 */
	public Map<?, ?> selectSulResultStddev(Map<String, Object> commandMap) throws Exception {
		return sulmunQuestDAO.selectSulResultStddev(commandMap);
	}
	
	/**
	 * 퍼센트 과정의 대한 설문 문제 정보 모두 가져오기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPerSulResultSulNumsList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectPerSulResultSulNumsList(commandMap);
	}
	
	/**
	 * 퍼센트 설문 학습자 답안 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List<?> selectPerSulResultAnswersList(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectPerSulResultAnswersList(commandMap);
	}
	
	/**
	 * 퍼센트 설문의 대한 수강생 전체인원수를 가져온다.
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectPerSulStudentMemberCount(Map<String, Object> commandMap) throws Exception{
		return sulmunQuestDAO.selectPerSulStudentMemberCount(commandMap);
	}
	
	
}
