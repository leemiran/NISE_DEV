package com.ziaan.propose.email;

import com.ziaan.library.RequestBox;
/**
 * 수강신청 관련 자동메일의 상황별 Method 를 정의한 인터페이스
 * @author Yang Seunghyeon
 * <br><br>
 *
 */
public interface SendRegisterCoursesEMail {
	/**
	 * 학습자가 수강신청을 하였을 때 자동메일을 발송한다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int sendRegisterCoursesMail(RequestBox box)throws Exception;
	
	/**
	 * 학습자가 수강신청을 취소하였을 때 자동메일을 발송한다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int canceledRegisterCoursesMail(RequestBox box)throws Exception;
	
	/**
	 * 관리자가 학습자의 수강신청을 반려하였을 때 자동메일을 발송한다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int sendRejectRegisterCoursesMail(RequestBox box)throws Exception;
	
	/**
	 * 관리자가 학습자의 수강신청을 승인하였을 때 자동메일을 발송한다.
	 * @param box
	 * @return
	 * @throws Exception
	 */
	int sendAcceptRegisterCoursesMail(RequestBox box)throws Exception;
	
	int sendSubjQnaStudyMail(RequestBox box)throws Exception;

}
