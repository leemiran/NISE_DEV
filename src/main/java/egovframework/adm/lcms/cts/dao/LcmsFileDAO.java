package egovframework.adm.lcms.cts.dao;

import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("lcmsFileDAO")
public class LcmsFileDAO extends EgovAbstractDAO{

	//파일정보등록<LCMS_SCO 객체>
	public int insertLcmsFile(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			insert("lcmsFileDAO.insertLcmsFile", commandMap);
		}catch(Exception ex){
			isOk = 0;
			ex.printStackTrace();
		}
		return isOk;
	}
	
	public int deleteLcmsFile(Map<String, Object> commandMap) throws Exception{
		return delete("lcmsFileDAO.deleteLcmsFile", commandMap);
	}

}
