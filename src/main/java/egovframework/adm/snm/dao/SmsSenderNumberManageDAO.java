package egovframework.adm.snm.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("smsSenderNumberManageDAO")
public class SmsSenderNumberManageDAO extends EgovAbstractDAO{

	/*
	 * 문자 발신자 전호 번호 수정
	 * */
	public int updateSendFromHandPhon(Map<String, Object> map) {
		return update("SmsSenderNumberManageDAO.updateSendFromHandPhon", map);
	}

	
}
