package egovframework.adm.lcms.nct.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface LcmsNonScormManageService {

	public String selectContentPath( Map<String, Object> commandMap) throws Exception;
	
	public int selectNonScormListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectNonScormList(Map<String, Object> commandMap) throws Exception;
	
	public int insertCotiExcel(List dataList, Map<String, Object> commandMap) throws Exception;
	
	public int insertExcel(List dataList, Map<String, Object> commandMap) throws Exception;
	
	public List selectNonScormOrgList(Map<String, Object> commandMap) throws Exception;
	
	public List selectCotiExcelList(Map<String, Object> commandMap) throws Exception;
	
	public List selectExcelList(Map<String, Object> commandMap) throws Exception;
	
	public int deleteLcmsCA(Map<String, Object> commandMap) throws Exception;
	
	public List selectContentCodeList(Map<String, Object> commandMap) throws Exception;
	
	public List selectProgressLogList(Map<String, Object> commandMap) throws Exception;
	
	public int deletePorgressLog(Map<String, Object> commandMap) throws Exception;
}
