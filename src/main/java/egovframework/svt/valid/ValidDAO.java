package egovframework.svt.valid;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class ValidDAO extends EgovAbstractDAO {

	public int getAreaCode(Map<String, Object> daoParamMap) {
		return (Integer) selectByPk("valid.getArea", daoParamMap);
	}

	public int getAutoMemberCnt(Map<String, Object> commandMap) {
		return (Integer) selectByPk("valid.getAutoMemberCnt", commandMap);
	}

	public Map<String, String> getUserInfo(String userid) {
		return (Map<String, String>) selectByPk("valid.getUserInfo", userid);
	}

	public int getUserInAutoMember(Map<String, String> userInfo) {
		return (Integer) selectByPk("valid.getUserInAutoMember", userInfo);
	}

	public List<Map<String, String>> getSubjseqAreaList(Map<String, String> userInfo) {
		return list("valid.getSubjseqAreaList", userInfo);
	}
	
	public int selectStudyLimitCnt(Map<String, Object> commandMap){
		return (Integer) selectByPk("valid.selectStudyLimitCnt", commandMap);
	}

}
