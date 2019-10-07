package egovframework.adm.lcms.nct.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("lcmsNonScormManageDAO")
public class LcmsNonScormManageDAO extends EgovAbstractDAO{

	
	 public String selectContentPath( Map<String, Object> commandMap) throws Exception{
        return (String)getSqlMapClientTemplate().queryForObject("lcmsNonScormManageDAO.selectContentPath", commandMap);
    }
	 
	 public int selectNonScormListTotCnt(Map<String, Object> commandMap) throws Exception{
		 return (Integer)getSqlMapClientTemplate().queryForObject("lcmsNonScormManageDAO.selectNonScormListTotCnt", commandMap);
	 }
	 
	 public List selectNonScormList(Map<String, Object> commandMap) throws Exception{
		 return list("lcmsNonScormManageDAO.selectNonScormList", commandMap);
	 }
	 
	 public List selectNonScormOrgList(Map<String, Object> commandMap) throws Exception{
		 return list("lcmsNonScormManageDAO.selectNonScormOrgList", commandMap);
	 }
	 
	 public List selectCotiExcelList(Map<String, Object> commandMap) throws Exception{
		 return list("lcmsNonScormManageDAO.selectCotiExcelList", commandMap);
	 }
	 
	 public List selectExcelList(Map<String, Object> commandMap) throws Exception{
		 return list("lcmsNonScormManageDAO.selectExcelList", commandMap);
	 }
	 
	 public List selectContentCodeList(Map<String, Object> commandMap) throws Exception{
		 return list("lcmsNonScormManageDAO.selectContentCodeList", commandMap);
	 }
	 
	 public List selectProgressLogList(Map<String, Object> commandMap) throws Exception{
		 return list("lcmsNonScormManageDAO.selectProgressLogList", commandMap);
	 }
	 
	 public int deletePorgressLog(Map<String, Object> commandMap) throws Exception{
		 return delete("lcmsNonScormManageDAO.deletePorgressLog", commandMap);
	 }
	 
}
