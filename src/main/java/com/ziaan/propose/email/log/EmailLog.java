package com.ziaan.propose.email.log;

import java.util.List;

import com.ziaan.library.DataBox;
import com.ziaan.library.RequestBox;
/**
 * �̸����� �߽ŵǾ����� Ȯ���ϱ� ���� ����� �α�
 * @author Yang Seunghyeon
 *
 */
public interface EmailLog {
	/**
	 * �̸��� �߽� ������ ����Ѵ�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int insertEmailLog(RequestBox box)throws Exception;
	/**
	 * �̸��� �߽� ������ ��� ����� �����´�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	List getEmailAllLogList(RequestBox box)throws Exception;
	/**
	 * �̸��� �߽ų����� �Ѱ� �����´�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	DataBox retrieveLog(RequestBox box)throws Exception;
	
	/**
	 * tabSeq�� �������� ��� ����� �����´�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	List getEmailLogListGroupByTabSeq(RequestBox box)throws Exception;
	
	/**
	 * tabSeq�� �������� ��� ������ ����� ���� ����� �����ش�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	List getSelectedEmailLogList(RequestBox box)throws Exception;
	
	/**
	 * �ѹ��� �߽ŵ� �׷��� flag �� �����´�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int getMaxTabseq()throws Exception;
}
