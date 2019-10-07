package egovframework.svt.lifetime;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class LifetimeDAO extends EgovAbstractDAO {

	public List<Map<String, Object>> getCompleteStudentList(Map<String, Object> commandMap) {
		return list("lifetime.getCompleteStudentList", commandMap);
	}

	public int updateLifetimeSubj(Map<String, Object> commandMap) {
		// TODO Auto-generated method stub
		return update("lifetime.updateLifetimeSubj", commandMap);
	}
	
	public List<Map<String, Object>> selectSubjInfoDetailLifetime(Map<String, Object> commandMap) {
		return list("lifetime.selectSubjInfoDetailLifetime", commandMap);
	}

}
