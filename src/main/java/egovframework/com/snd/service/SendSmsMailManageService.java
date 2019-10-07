package egovframework.com.snd.service;

import java.util.List;
import java.util.Map;


public interface SendSmsMailManageService {

	public List getSendMemberInfo(Map<String, Object> commandMap) throws Exception;
	
	public int freeMailSend(Map<String, Object> commandMap) throws Exception;

	/*
	 * 발송자 핸드폰 번호 조회
	 * */
	public Map<String, Object> getSendFromHandPhon(Map<String, Object> commandMap) throws Exception;
	
	//회원구분별 인원수
	public Map<String, Object> selectMemberGubunCount(Map<String, Object> commandMap) throws Exception;
	
	//회원구분별 메일발송
	public int insertMemberGubunSendMail(Map<String, Object> commandMap) throws Exception;
	
	//수신거부
	public int updateNoMemberGubunSendMail(Map<String, Object> commandMap) throws Exception;
	
	//발송리스트 CNT
	int selectSendMailListCnt(Map<String, Object> commandMap) throws Exception;
	
	//발송리스트
	public List selectSendMailList(Map<String, Object> commandMap) throws Exception;
	
	//선택 회원 메일발송
	public int insertMemberSearchMail(Map<String, Object> commandMap) throws Exception;
	
	
	
	//휴면계정 메일발송
	public void insertCloseUserSendMail(Map<String, Object> commandMap) throws Exception;
	
	public List selectCloseUser(Map<String, Object> commandMap) throws Exception;

	public Map<String, Object> selectSendMailContent(Map<String, Object> commandMap);
	
}
