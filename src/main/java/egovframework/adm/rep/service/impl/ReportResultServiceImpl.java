package egovframework.adm.rep.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.rep.dao.ReportResultDAO;
import egovframework.adm.rep.service.ReportResultService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("reportResultService")
public class ReportResultServiceImpl extends EgovAbstractServiceImpl implements ReportResultService{
	
	@Resource(name="reportResultDAO")
    private ReportResultDAO reportResultDAO;

	public List selectReportResultList(Map<String, Object> commandMap) throws Exception{
		return reportResultDAO.selectReportResultList(commandMap);
	}
	
	public Map selectViewOrder(Map<String, Object> commandMap) throws Exception{
		return reportResultDAO.selectViewOrder(commandMap);
	}
	
	public List selectReportStudentList(Map<String, Object> commandMap) throws Exception{
		return reportResultDAO.selectReportStudentList(commandMap);
	}
	
	public int updateReportResultScore(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String[] score = EgovStringUtil.getStringSequence(commandMap, "p_getscore");
			String[] finalscore = EgovStringUtil.getStringSequence(commandMap, "p_finalscore");
			String[] projid = EgovStringUtil.getStringSequence(commandMap, "p_projid");
			String[] minusscore = EgovStringUtil.getStringSequence(commandMap, "p_minusscore");
			String[] tmp = new String[projid.length];
			for( int i=0; i<projid.length; i++ ){
				tmp[i] = projid[i] + "!_" + score[i] + "!_" + minusscore[i] + "!_" + finalscore[i];
			}
			commandMap.put("tmp", tmp);
			reportResultDAO.updateReportResultData(commandMap);
			reportResultDAO.insertReportResultData(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return isOk;
	}
	
	public int insertExcelToDBNew(String path, List list, Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			if(list != null && list.size() > 0 ){
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					//String projid 	= (String)map.get("parameter0");
					String projid 	= (String)map.get("parameter6");
					//String score	= (String)map.get("parameter1");
					String score	= (String)map.get("parameter14");
					//String getscore	= (String)map.get("parameter2");
					String getscore	= (String)map.get("parameter12");
					String p_minusscore	= (String)map.get("parameter13");
					commandMap.put("p_projid", projid);
					commandMap.put("p_score", score);
					commandMap.put("p_getscore", getscore);
					commandMap.put("p_minusscore", p_minusscore);
					reportResultDAO.updateReportResult(commandMap);
				}
				
			}else{
				isOk = 0;
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			File del = new File(path);
			del.delete();
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return isOk;
	}
}
