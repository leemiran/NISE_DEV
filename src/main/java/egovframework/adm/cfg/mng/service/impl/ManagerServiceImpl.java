package egovframework.adm.cfg.mng.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cfg.mng.dao.ManagerDAO;
import egovframework.adm.cfg.mng.service.ManagerService;
import egovframework.com.utl.fcc.service.EgovStringUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("managerService")
public class ManagerServiceImpl extends EgovAbstractServiceImpl implements ManagerService{

	@Resource(name="managerDAO")
    private ManagerDAO managerDAO;
	
	public List selectManagerList(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerList(commandMap);
	}
	
	public List selectGadminList(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectGadminList(commandMap);
	}
	
	public Map selectManagerView(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerView(commandMap);
	}
	
	public List selectManagerViewGrcode(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerViewGrcode(commandMap);
	}
	
	public List selectManagerViewSubj(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerViewSubj(commandMap);
	}
	
	public List selectManagerViewComp(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerViewComp(commandMap);
	}
	
	public int updateManager(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			managerDAO.updateManagerInfo(commandMap);
			
			commandMap.put("p_logmode", "U");						 
			managerDAO.managerInsertLog(commandMap);
			
			if(commandMap.get("p_gadminview") == null && commandMap.get("p_gadmin") != null){
				commandMap.put("p_gadminview", commandMap.get("p_gadmin"));
			}
			
			if( commandMap.get("p_isneedgrcode") != null && ((String)commandMap.get("p_isneedgrcode")).equals("Y") ){
				String[] v_grcode = EgovStringUtil.getStringSequence(commandMap, "p_grcode");
				managerDAO.deleteGrcode(commandMap);
				for( int i=0; i<v_grcode.length; i++ ){
					commandMap.put("pp_grcode", v_grcode[i]);
					managerDAO.insertGrcode(commandMap);
				}
			}
			if( commandMap.get("p_isneedsubj") != null && ((String)commandMap.get("p_isneedsubj")).equals("Y") ){
				String[] v_subj = EgovStringUtil.getStringSequence(commandMap, "p_subj");
				managerDAO.deleteSubj(commandMap);
				for( int i=0; i<v_subj.length; i++ ){
					commandMap.put("pp_subj", v_subj[i]);
					managerDAO.insertSubj(commandMap);
				}
			}
			if( commandMap.get("p_isneedcomp") != null && ((String)commandMap.get("p_isneedcomp")).equals("Y") ){
				String[] v_comp = EgovStringUtil.getStringSequence(commandMap, "p_comp");
				managerDAO.deleteComp(commandMap);
				for( int i=0; i<v_comp.length; i++ ){
					commandMap.put("pp_comp", v_comp[i]);
					managerDAO.insertComp(commandMap);
				}
			}
			if( commandMap.get("p_isneeddept") != null && ((String)commandMap.get("p_isneeddept")).equals("Y") ){
				String[] v_dept = EgovStringUtil.getStringSequence(commandMap, "p_dept");
				managerDAO.deleteComp(commandMap);
				for( int i=0; i<v_dept.length; i++ ){
					commandMap.put("pp_comp", v_dept[i]);
					managerDAO.insertComp(commandMap);
				}
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public List selectBranchList(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectBranchList(commandMap);
	}
	
	public List getGadminSelectNop(Map<String, Object> commandMap) throws Exception{
		return managerDAO.getGadminSelectNop(commandMap);
	}
	
	public int managerInsert(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int cnt = managerDAO.checkManagerCount(commandMap);
			if( cnt > 0 ){
				isOk = -99;
			}else{
				managerDAO.managerInsert(commandMap);
				
				commandMap.put("p_logmode", "I");
				commandMap.put("p_gadmin", commandMap.get("p_gadminview"));
				
				managerDAO.managerInsertLog(commandMap);
				
				if( commandMap.get("p_isneedgrcode") != null && ((String)commandMap.get("p_isneedgrcode")).equals("Y") ){
					String[] v_grcode = EgovStringUtil.getStringSequence(commandMap, "p_grcode");
					for( int i=0; i<v_grcode.length; i++ ){
						commandMap.put("pp_grcode", v_grcode[i]);
						managerDAO.insertGrcode(commandMap);
					}
				}
				if( commandMap.get("p_isneedsubj") != null && ((String)commandMap.get("p_isneedsubj")).equals("Y") ){
					String[] v_subj = EgovStringUtil.getStringSequence(commandMap, "p_subj");
					for( int i=0; i<v_subj.length; i++ ){
						commandMap.put("pp_subj", v_subj[i]);
						managerDAO.insertSubj(commandMap);
					}
				}
				if( commandMap.get("p_isneedcomp") != null && ((String)commandMap.get("p_isneedcomp")).equals("Y") ){
					String[] v_comp = EgovStringUtil.getStringSequence(commandMap, "p_comp");
					for( int i=0; i<v_comp.length; i++ ){
						commandMap.put("pp_comp", v_comp[i]);
						managerDAO.insertComp(commandMap);
					}
				}
				if( commandMap.get("p_isneeddept") != null && ((String)commandMap.get("p_isneeddept")).equals("Y") ){
					String[] v_dept = EgovStringUtil.getStringSequence(commandMap, "p_dept");
					for( int i=0; i<v_dept.length; i++ ){
						commandMap.put("pp_comp", v_dept[i]);
						managerDAO.insertComp(commandMap);
					}
				}
				managerDAO.deleteMenuAuth(commandMap);
				managerDAO.insertMenuAuth(commandMap);
			}
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public int managerDelete(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			managerDAO.deleteManager(commandMap);
			managerDAO.deleteGrcode(commandMap);
			managerDAO.deleteSubj(commandMap);
			managerDAO.deleteComp(commandMap);
			
			commandMap.put("p_logmode", "D");			
			managerDAO.managerInsertLog(commandMap);
			
		}catch(Exception ex ){
			isOk = 0;
			ex.printStackTrace();
		}
		
		return isOk;
	}
	
	public int selectManagerLogTotCnt(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerLogTotCnt(commandMap);
	}
	
	public List selectManagerLogList(Map<String, Object> commandMap) throws Exception{
		return managerDAO.selectManagerLogList(commandMap);
	}
	
}
