package egovframework.adm.fin.service.impl;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import egovframework.adm.fin.dao.FinishInputDAO;
import egovframework.adm.fin.service.FinishInputService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("finishInputService")
public class FinishInputServiceImpl extends EgovAbstractServiceImpl implements FinishInputService{
	
	@Resource(name="finishInputDAO")
    private FinishInputDAO finishInputDAO;

	public int excelMemberInsert(List list, Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			if( list != null && list.size() > 0 ){
				String[] tmp = new String[list.size()];
				for(int i=1; i<list.size(); i++){
					Map map = (Map)list.get(i);
					String v_examnum = (String)map.get("parameter0");
		            String v_score   = (String)map.get("parameter1");
		            v_examnum = v_examnum.replace("'", "");
		            
		            if( v_examnum.equals("") ){
		            	return -1;
		            }
		            if( v_score.equals("") ){
		            	return -2;
		            }
		            tmp[i] = v_examnum+"!_"+v_score;
				}
				commandMap.put("tmp", tmp);
				finishInputDAO.insertExcelData(commandMap);
				try{
					String path = (String)commandMap.get("path");
					new File(path).delete();
				}catch(Exception ex){}
			}
		}catch(Exception ex){
			try{
				String path = (String)commandMap.get("path");
				new File(path).delete();
			}catch(Exception e){}
			isOk = 0;
			ex.printStackTrace();
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		
		return isOk;
	}

}
