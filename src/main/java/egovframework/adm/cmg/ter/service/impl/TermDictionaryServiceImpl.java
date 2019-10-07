package egovframework.adm.cmg.ter.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.cmg.ter.dao.TermDictionaryDAO;
import egovframework.adm.cmg.ter.service.TermDictionaryService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("termDictionaryService")
public class TermDictionaryServiceImpl extends EgovAbstractServiceImpl implements TermDictionaryService{
	
	@Resource(name="termDictionaryDAO")
    private TermDictionaryDAO termDictionaryDAO;

	public List selectTermDictionaryList(Map<String, Object> commandMap) throws Exception{
		return termDictionaryDAO.selectTermDictionaryList(commandMap);
	}
	
	public List selectDicGroup(Map<String, Object> commandMap) throws Exception{
		return termDictionaryDAO.selectDicGroup(commandMap);
	}
	
	public int insertTermDictionary(Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		commandMap.put("seq", termDictionaryDAO.selectMaxSeq(commandMap));
		termDictionaryDAO.insertTermDictionary(commandMap);
		return isOk;
	}

	public Map selectDictionaryData(Map<String, Object> commandMap) throws Exception{
		return termDictionaryDAO.selectDictionaryData(commandMap);
	}
	
	public int updateTermDictionary(Map<String, Object> commandMap) throws Exception{
		return termDictionaryDAO.updateTermDictionary(commandMap);
	}
	
	public int deleteTermDictionary(Map<String, Object> commandMap) throws Exception{
		return termDictionaryDAO.deleteTermDictionary(commandMap);
	}
	
	public int insertExcelUploadData(List list, Map<String, Object> commandMap) throws Exception{
		int isOk = 1;
		try{
			int seq = Integer.valueOf(termDictionaryDAO.selectMaxSeq(commandMap));
			for( int i=1; i<list.size(); i++ ){
				Map m = (Map)list.get(i);
				m.put("p_subj", commandMap.get("p_subj"));
				m.put("seq", seq++);
				commandMap.put("seq", seq++);
				commandMap.put("p_words", m.get("parameter0"));
				commandMap.put("p_groups", m.get("parameter1"));
				commandMap.put("p_descs", m.get("parameter2"));
				termDictionaryDAO.insertTermDictionary(commandMap);
			}
		}catch(Exception ex){
			isOk = 0;
		}
		return isOk;
	}
	
}
