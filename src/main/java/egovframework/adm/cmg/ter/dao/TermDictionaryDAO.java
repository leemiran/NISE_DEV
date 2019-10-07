package egovframework.adm.cmg.ter.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("termDictionaryDAO")
public class TermDictionaryDAO extends EgovAbstractDAO{

	public List selectTermDictionaryList(Map<String, Object> commandMap) throws Exception{
		return list("termDictionaryDAO.selectTermDictionaryList", commandMap);
	}
	
	public List selectDicGroup(Map<String, Object> commandMap) throws Exception{
		return list("termDictionaryDAO.selectDicGroup", commandMap);
	}
	
	public void insertTermDictionary(Map<String, Object> commandMap) throws Exception{
		insert("termDictionaryDAO.insertTermDictionary", commandMap);
	}
	
	public String selectMaxSeq(Map<String, Object> commandMap) throws Exception{
		return (String)selectByPk("termDictionaryDAO.selectMaxSeq", commandMap);
	}
	
	public Map selectDictionaryData(Map<String, Object> commandMap) throws Exception{
		return (Map)selectByPk("termDictionaryDAO.selectDictionaryData", commandMap);
	}
	
	public int updateTermDictionary(Map<String, Object> commandMap) throws Exception{
		return update("termDictionaryDAO.updateTermDictionary", commandMap);
	}
	
	public int deleteTermDictionary(Map<String, Object> commandMap) throws Exception{
		return delete("termDictionaryDAO.deleteTermDictionary", commandMap);
	}
}
