package egovframework.adm.rep.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.rep.dao.ReportProfDAO;
import egovframework.adm.rep.service.ReportProfService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("reportProfService")
public class ReportProfServiceImpl extends EgovAbstractServiceImpl implements ReportProfService{
	
	@Resource(name="reportProfDAO")
    private ReportProfDAO reportProfDAO;

	public List selectReportProfList(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportProfList(commandMap);
	}
	
	public int insertReportProfData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				for( int i=0; i<multiFiles.size(); i++ ){
					commandMap.putAll((Map)multiFiles.get(i));
				}
			}
			reportProfDAO.insertReportProfData(commandMap);
			
			reportProfDAO.insertFilesData(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public Map selectReportProfData(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportProfData(commandMap);
	}
	
	public List selectProfFiles(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectProfFiles(commandMap);
	}
	
	public int updateReportProfData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				for( int i=0; i<multiFiles.size(); i++ ){
					commandMap.putAll((Map)multiFiles.get(i));
				}
			}
			reportProfDAO.updateReportProfData(commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	public int reportProfWeightUpdateData(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			String[] reportseq = EgovStringUtil.getStringSequence(commandMap, "p_reportseq");
			String[] weight = EgovStringUtil.getStringSequence(commandMap, "p_weight");
			for( int i=0; i<reportseq.length; i++ ){
				commandMap.put("v_reportseq", reportseq[i]);
				commandMap.put("v_weight", weight[i]);
				
				reportProfDAO.reportProfWeightUpdateData(commandMap);
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	
	public List selectReportQuestionList(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportQuestionList(commandMap);
	}
	
	
	public Map selectReportQuestionView(Map<String, Object> commandMap) throws Exception{	
		return reportProfDAO.selectReportQuestionView(commandMap);
	}
	
	
	/**
	 * 과제 문항 등록
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean insertTzReportQues(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
			
			Object v_examnum = reportProfDAO.insertTzReportQues(commandMap); 	//과제 문항 등록
			
			isok = true;
				
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	//과제문항 과정의 문항 조회 
	public List selectReportQuestionSubjList(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportQuestionSubjList(commandMap);
	}
	
	public Map selectReportProfView(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportProfView(commandMap);
	}
	
	//과제 문항 리스트
	public List selectReportQuesList(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportQuesList(commandMap);
	}
	
	//과제 등록
	public int insertReportProf(Map<String, Object> commandMap) throws Exception{
		
		int isOk = 0;
		
		//과제 문항
		String []  v_param = (String []) commandMap.get("_Array_p_params");
		//체크박스 선택여부(1:checked/0:not checked)
		String []  v_checkvalue = (String []) commandMap.get("_Array_checkvalue");
		
		try{
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				for( int i=0; i<multiFiles.size(); i++ ){
					commandMap.putAll((Map)multiFiles.get(i));
				}
				//첨부파일
				//reportProfDAO.insertReportProf(commandMap);
				reportProfDAO.insertFilesData(commandMap);
			}
						
			//과제등록
			reportProfDAO.insertReportProf(commandMap);
			
			
			//평가과제문항삭제			
			reportProfDAO.deleteProjordSheet(commandMap);
			
			if(v_param != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					
					//v_checkvalue 1인 경우(선택)만 저장을 실행한다.
					if(v_checkvalue[i].equals("1"))
					{
						commandMap.put("v_quesnum", v_param[i]);
	                	//평가과제문항 저장
	                	reportProfDAO.insertProjordSheet(commandMap);	                	
					}
				}
			}
			isOk = 1;
			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}		
		return isOk;
	}
	
	//과제 수정
	public int updateReportProf(Map<String, Object> commandMap) throws Exception{
		
		int isOk = 0;
		
		//과제 문항
		String []  v_param = (String []) commandMap.get("_Array_p_params");
		//체크박스 선택여부(1:checked/0:not checked)
		String []  v_checkvalue = (String []) commandMap.get("_Array_checkvalue");
		
		try{
			List multiFiles = (ArrayList)commandMap.get("multiFiles");
			if( multiFiles != null && multiFiles.size() > 0 ){
				for( int i=0; i<multiFiles.size(); i++ ){
					commandMap.putAll((Map)multiFiles.get(i));
				}
				//첨부파일
				reportProfDAO.insertFilesData(commandMap);
			}
			
			if( commandMap.get("p_filedel") != null ){				
				reportProfDAO.deleteReportProfFiles(commandMap);				
			}
			//과제수정
			reportProfDAO.updateReportProf(commandMap);	
			//평가과제문항삭제			
			reportProfDAO.deleteProjordSheet(commandMap);
			
			if(v_param != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					
					//v_checkvalue 1인 경우(선택)만 저장을 실행한다.
					if(v_checkvalue[i].equals("1"))
					{
						commandMap.put("v_quesnum", v_param[i]);
	                	//평가과제문항 저장
	                	reportProfDAO.insertProjordSheet(commandMap);	                	
					}
				}
			}
			isOk = 1;
			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}		
		return isOk;
	}
	
	
	//과제 문항 수정
	public int updateTzReportQues(Map<String, Object> commandMap) throws Exception{
		
		int isOk = 0;		
		try{
			//과제수정
			reportProfDAO.updateTzReportQues(commandMap);	
			isOk = 1;
			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}		
		return isOk;
	}
	
	//과제 문항 삭제
	public int deleteTzReportQues(Map<String, Object> commandMap) throws Exception{
		
		int isOk = 0;		
		try{
			
			//과제문항 보기
			Map reportQuestionView =  reportProfDAO.selectReportQuestionView(commandMap);
			String useyn = reportQuestionView.get("useyn").toString();
			
			if(useyn.equals("Y")){
				isOk = -1;
				return isOk;
			}
			
			//과제수정
			reportProfDAO.deleteTzReportQues(commandMap);	
			isOk = 1;
			
		}catch(Exception ex){
			isOk = 0;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ex.printStackTrace();
		}		
		return isOk;
	}
	
	//과제 출제 문항 리스트
	public List selectReportQuesSubjseqList(Map<String, Object> commandMap) throws Exception{
		return reportProfDAO.selectReportQuesSubjseqList(commandMap);
	}
	

}
