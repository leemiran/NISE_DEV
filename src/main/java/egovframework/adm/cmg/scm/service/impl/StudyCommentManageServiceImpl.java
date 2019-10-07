package egovframework.adm.cmg.scm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.cmg.scm.dao.StudyCommentManageDAO;
import egovframework.adm.cmg.scm.service.StudyCommentManageService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("studyCommentManageService")
public class StudyCommentManageServiceImpl extends EgovAbstractServiceImpl implements StudyCommentManageService{
	
	@Resource(name="studyCommentManageDAO")
    private StudyCommentManageDAO studyCommentManageDAO;
	
	public List selectCommentDateList(Map<String, Object> commandMap) throws Exception{
		return studyCommentManageDAO.selectCommentDateList(commandMap);
	}
	
	public List selectCommentList(Map<String, Object> commandMap) throws Exception{
		return studyCommentManageDAO.selectCommentList(commandMap);
	}
	
	public List selectCommentSubList(Map<String, Object> commandMap) throws Exception{
		return studyCommentManageDAO.selectCommentSubList(commandMap);
	}
	
	public int updateCommentDateList(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String[] chk = EgovStringUtil.getStringSequence(commandMap, "p_chk");
			for( int i=0; i<chk.length; i++ ){
				String[] tmp = chk[i].split(",");
				Map inputMap = new HashMap();
				inputMap.put("p_seq", 		tmp[0]);
				inputMap.put("p_subj", 		tmp[1]);
				inputMap.put("p_year", 		tmp[2]);
				inputMap.put("p_subjseq", 	tmp[3]);
				inputMap.put("p_userid", 	tmp[4]);
				inputMap.put("p_hidden", 	commandMap.get("p_hidden"));
				studyCommentManageDAO.updateCommentDateList(inputMap);
			}
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}
		return isOk;
	}
}
