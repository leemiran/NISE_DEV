package egovframework.adm.snm.service;

import java.util.List;
import java.util.Map;


public interface SmsSenderNumberManageService {

	/*
	 * 문자 발신자 전화 번호 수정
	 * */
	int updateSendFromHandPhon(Map<String, Object> map) throws Exception;

	
}
