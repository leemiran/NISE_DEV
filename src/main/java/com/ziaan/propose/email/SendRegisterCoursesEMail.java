package com.ziaan.propose.email;

import com.ziaan.library.RequestBox;
/**
 * ������û ���� �ڵ������� ��Ȳ�� Method �� ������ �������̽�
 * @author Yang Seunghyeon
 * <br><br>
 *
 */
public interface SendRegisterCoursesEMail {
	/**
	 * �н��ڰ� ������û�� �Ͽ��� �� �ڵ������� �߼��Ѵ�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int sendRegisterCoursesMail(RequestBox box)throws Exception;
	
	/**
	 * �н��ڰ� ������û�� ����Ͽ��� �� �ڵ������� �߼��Ѵ�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int canceledRegisterCoursesMail(RequestBox box)throws Exception;
	
	/**
	 * �����ڰ� �н����� ������û�� �ݷ��Ͽ��� �� �ڵ������� �߼��Ѵ�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int sendRejectRegisterCoursesMail(RequestBox box)throws Exception;
	
	/**
	 * �����ڰ� �н����� ������û�� �����Ͽ��� �� �ڵ������� �߼��Ѵ�.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int sendAcceptRegisterCoursesMail(RequestBox box)throws Exception;
	
	int sendSubjQnaStudyMail(RequestBox box)throws Exception;

}
