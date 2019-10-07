package egovframework.com.snd.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("sendSmsMailManageDAO")
public class SendSmsMailManageDAO extends EgovAbstractDAO{

	public List getSendMemberInfo(Map<String, Object> commandMap) throws Exception{
		return list("sendSmsMailManageDAO.getSendMemberInfo", commandMap);
	}
	
	public void insertEmailLog(Map<String, Object> commandMap) throws Exception{
		insert("sendSmsMailManageDAO.insertEmailLog", commandMap);
	}

	/*
	 * 발송자 핸드폰 번호 조회
	 * */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSendFromHandPhon(Map<String, Object> map) {
		return (Map<String, Object>)selectByPk("sendSmsMailManageDAO.getSendFromHandPhon", map);
	}
	
	//회원구분별 인원수
	public Map<String, Object> selectMemberGubunCount(Map<String, Object> commandMap) throws Exception{
		return (Map<String, Object>)selectByPk("sendSmsMailManageDAO.selectMemberGubunCount",commandMap);
	}
	
	//회원구분별 리스트
	public List selectMemberGubunSendMailList(Map<String, Object> commandMap) throws Exception{
		return list("sendSmsMailManageDAO.selectMemberGubunSendMailList",commandMap);
	}
	
	public void insertMemberGubunSendMailForm(Map<String, Object> commandMap) throws Exception{
		insert("sendSmsMailManageDAO.insertMemberGubunSendMailForm",commandMap);		
	}
	
	//
	public void insertMemberGubunSendMail(Map<String, Object> commandMap) throws Exception{
		insert("sendSmsMailManageDAO.insertMemberGubunSendMail",commandMap);		
	}
	
	public void insertMemberGubunDormantSendMail(Map<String, Object> commandMap) throws Exception{
		insert("sendSmsMailManageDAO.insertMemberGubunDormantSendMail",commandMap);		
	}
	
	public int updateNoMemberGubunSendMail(Map<String, Object> commandMap) throws Exception{
		return update("sendSmsMailManageDAO.updateNoMemberGubunSendMail", commandMap);
	}
	
	//
	public void insertMemberSearchMail(Map<String, Object> commandMap) throws Exception{
		insert("sendSmsMailManageDAO.insertMemberSearchMail",commandMap);		
	}
	
	
	public List selectCloseUser(Map<String, Object> commandMap) throws Exception{
		return list("sendSmsMailManageDAO.selectCloseUser",commandMap);
	}
	
	
}
