package egovframework.adm.hom.not.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface NoticeAdminService {
	
	public int selectTableseq(Map<String, Object> commandMap) throws Exception;

	public List selectNoticeListAll(Map<String, Object> commandMap) throws Exception;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오전 1:54:03 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List srSystemSpecListAll(Map<String, Object> commandMap) throws Exception;
	
	public int selectNoticeListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 8:19:03 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int srSystemSpecListTotCnt(Map<String, Object> commandMap) throws Exception;
		
	
	public List selectNoticeList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 8:21:46 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List srSystemSpecList(Map<String, Object> commandMap) throws Exception;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 1. 오후 1:59:59 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List srSystemSpecListExcelDown(Map<String, Object> commandMap) throws Exception;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 1. 오후 1:59:47 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectGrcodeList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 홈페이지 > 공지사항 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertNotice(Map<String, Object> commandMap, List<?> fileList) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오전 1:17:31 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public boolean insertSrSystem(Map<String, Object> commandMap, List<?> fileList) throws Exception;	

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 4:16:53 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public boolean insertSrSystemReply(Map<String, Object> commandMap, List<?> fileList) throws Exception;	

	/**
	 * 공지사항 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectNoticeView(Map<String, Object> commandMap) throws Exception ;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 11:29:05 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> srSystemSpecView(Map<String, Object> commandMap) throws Exception ;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 5:19:13 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> srSystemSpecViewList(Map<String, Object> commandMap) throws Exception ;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오후 12:02:46 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> srSystemSpecViewListRe(Map<String, Object> commandMap) throws Exception ;
		
	/**
	 * 공지사항 첨부파일 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectBoardFileList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 공지사항 조회 수 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateNoticeReadCount(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 2:19:26 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectReplyCount(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:29:35 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSrSystemReSeq(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 29. 오후 6:27:55 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectConfirmDateCnt(Map<String, Object> commandMap) throws Exception;	

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:57:52 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public float selectSrSystemSrListSeqMin(Map<String, Object> commandMap) throws Exception;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 4. 오후 2:32:47 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public float selectSrSystemSrListSeqMinRe1(Map<String, Object> commandMap) throws Exception;
	public float selectSrSystemSrListSeqMinRe2(Map<String, Object> commandMap) throws Exception;
	

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:34:45 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSrSystemSrLevel(Map<String, Object> commandMap) throws Exception;	
	
	/**
	 * 공지사항 게시글삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteNotice(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 9:16:47 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSrSystem(Map<String, Object> commandMap) throws Exception;	

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 10:09:09 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSrSystemReply(Map<String, Object> commandMap) throws Exception;	
	
	/**
	 * 공지사항 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateNotice(Map<String, Object> commandMap, List<?> fileList) throws Exception;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 2:02:04 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public boolean updateSrSystem(Map<String, Object> commandMap, List<?> fileList) throws Exception;
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 29. 오후 6:40:41 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean confirmDateUpdate(Map<String, Object> commandMap) throws Exception;	

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 5:24:15 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public boolean updateSrSystemReply(Map<String, Object> commandMap, List<?> fileList) throws Exception;

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오전 11:12:26 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public boolean updateSrSystemReply1(Map<String, Object> commandMap, List<?> fileList) throws Exception;

	/**
	 * faq 카테고리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCategoryList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * faq 카테고리 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectCategoryView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * FAQ 카테고리 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertCategoryFaq(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * FAQ 카테고리 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateCategoryFaq(Map<String, Object> commandMap) throws Exception;



	/**
	 * FAQ 카테고리 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCategoryFaq(Map<String, Object> commandMap) throws Exception;

	/**
	 * FAQ 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectFaqListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * FAQ 페이지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectFaqListPage(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * FAQ 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectFaqList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * FAQ 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectFaqView(Map<String, Object> commandMap) throws Exception ;
	
	/**
	 * FAQ 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertFaq(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * FAQ 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateFaq(Map<String, Object> commandMap) throws Exception;



	/**
	 * FAQ 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFaq(Map<String, Object> commandMap) throws Exception;
	
	
	
	/**
	 * 출석고사장 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSchoolRoomList(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 출석고사장 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSchoolRoomView(Map<String, Object> commandMap) throws Exception ;
	
	/**
	 * 출석고사장 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertSchoolRoom(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * 출석고사장 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateSchoolRoom(Map<String, Object> commandMap) throws Exception;



	/**
	 * 출석고사장 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSchoolRoom(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * QNA 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectQnaList(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * QNA 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * QNA 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectQnaView(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * QNA 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertQna(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * QNA 답변등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public boolean insertQnaReply(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * QNA 업데이트 -- 관리자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateQna(Map<String, Object> commandMap) throws Exception;


	/**
	 * QNA 조회수 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateQnaCount(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * QNA 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteQna(Map<String, Object> commandMap) throws Exception;
	
	/**
	 * QNA 업데이트 -- 사용자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateQnaUser(Map<String, Object> commandMap) throws Exception;
	
	
	/**
	 * 공지사항 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectNoticeViewAll(Map<String, Object> commandMap) throws Exception ;
	
	/**
	 * 스팸 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaSpamCnt(Map<String, Object> commandMap) throws Exception;
}
