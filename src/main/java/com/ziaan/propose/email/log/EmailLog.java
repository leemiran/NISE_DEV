package com.ziaan.propose.email.log;

import java.util.List;

import com.ziaan.library.DataBox;
import com.ziaan.library.RequestBox;
/**
 * 이메일이 발신되었는지 확인하기 위해 남기는 로그
 * @author Yang Seunghyeon
 *
 */
public interface EmailLog {
	/**
	 * 이메일 발신 내역을 기록한다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int insertEmailLog(RequestBox box)throws Exception;
	/**
	 * 이메일 발신 내역의 모든 목록을 가져온다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	List getEmailAllLogList(RequestBox box)throws Exception;
	/**
	 * 이메일 발신내역을 한건 가져온다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	DataBox retrieveLog(RequestBox box)throws Exception;
	
	/**
	 * tabSeq를 기준으로 묶어서 목록을 가져온다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	List getEmailLogListGroupByTabSeq(RequestBox box)throws Exception;
	
	/**
	 * tabSeq를 기준으로 묶어서 가저온 목록의 하위 목록을 보여준다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	List getSelectedEmailLogList(RequestBox box)throws Exception;
	
	/**
	 * 한번에 발신된 그룹의 flag 를 가져온다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int getMaxTabseq()throws Exception;
}
