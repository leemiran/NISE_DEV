package egovframework.adm.hom.not.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository("noticeAdminDAO")
public class NoticeAdminDAO extends EgovAbstractDAO{
	
	public int selectTableseq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectTableseq", commandMap);
	}

	public List selectNoticeListAll(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectNoticeListAll", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오전 1:54:36 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List srSystemSpecListAll(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.srSystemSpecListAll", commandMap);
	}

	public int selectNoticeListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectNoticeListTotCnt", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 8:19:52 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int srSystemSpecListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.srSystemSpecListTotCnt", commandMap);
	}

	public List selectNoticeList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectNoticeList", commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 8:22:21 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List srSystemSpecList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.srSystemSpecList", commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 1. 오후 2:00:49 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List srSystemSpecListExcelDown(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.srSystemSpecListExcelDown", commandMap);
	}
	
	public List selectGrcodeList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectGrcodeList", commandMap);
	}
	
	public List selectGrcodeList2(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectGrcodeList2", commandMap);
	}
	
	public List selectGrcodeList3(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectGrcodeList3", commandMap);
	}
	
	public List selectGrcodeList4(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectGrcodeList4", commandMap);
	}
	
	/**
	 * 홈페이지 > 공지사항 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertNotice(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertNotice", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오전 1:19:04 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSrSystem(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertSrSystem", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 4:18:34 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Object insertSrSystemReply(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertSrSystemReply", commandMap);
	}


	/**
	 * 전체게시판의 파일을 등록하는 메소드
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertBoardFile(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertBoardFile", commandMap);
	}
	
	/**
	 * 공지사항 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectNoticeView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.selectNoticeView", commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 11:29:38 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> srSystemSpecView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.srSystemSpecView", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 1:12:18 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> srSystemSpecViewList(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.srSystemSpecViewList", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오후 12:03:25 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> srSystemSpecViewListRe(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.srSystemSpecViewListRe", commandMap);
	}

	/**
	 * 공지사항 첨부파일 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectBoardFileList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectBoardFileList", commandMap);
	}
	
	/**
	 * 공지사항 조회 수 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateNoticeReadCount(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateNoticeReadCount", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 2:20:15 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectReplyCount(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectReplyCount", commandMap);		
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:30:08 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSrSystemReSeq(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectSrSystemReSeq", commandMap);		
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 29. 오후 6:28:28 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectConfirmDateCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectConfirmDateCnt", commandMap);		
	}
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:58:28 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Float selectSrSystemSrListSeqMin(Map<String, Object> commandMap) throws Exception{
		return (Float)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectSrSystemSrListSeqMin", commandMap);		
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 4. 오후 2:33:17 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Float selectSrSystemSrListSeqMinRe1(Map<String, Object> commandMap) throws Exception{
		return (Float)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectSrSystemSrListSeqMinRe1", commandMap);		
	}
	public Float selectSrSystemSrListSeqMinRe2(Map<String, Object> commandMap) throws Exception{
		return (Float)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectSrSystemSrListSeqMinRe2", commandMap);		
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:35:15 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectSrSystemSrLevel(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectSrSystemSrLevel", commandMap);		
	}

	/**
	 * 공지사항 게시글삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteNotice(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteNotice", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 9:17:28 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSrSystem(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteSrSystem", commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 10:10:03 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSrSystemReply(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteSrSystemReply", commandMap);
	}
	
	/**
	 * 공지사항 첨부파일 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteBoardFile(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteBoardFile", commandMap);
	}
	
	/**
	 * 공지사항 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateNotice(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateNotice", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 2:02:56 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSrSystem(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateSrSystem", commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 29. 오후 6:41:59 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int confirmDateUpdate(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.confirmDateUpdate", commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 5:25:37 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSrSystemReply(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateSrSystemReply", commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오전 11:13:12 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSrSystemReply1(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateSrSystemReply1", commandMap);
	}

	/**
	 * faq 카테고리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCategoryList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectCategoryList", commandMap);
	}
	
	/**
	 * faq 카테고리 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectCategoryView(Map<String, Object> commandMap) throws Exception{
		return (Map<?, ?>)selectByPk("noticeAdminDAO.selectCategoryView", commandMap);
	}

	/**
	 * FAQ 카테고리 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertCategoryFaq(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertCategoryFaq", commandMap);
	}
	
	/**
	 * FAQ 카테고리 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateCategoryFaq(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateCategoryFaq", commandMap);
	}



	/**
	 * FAQ 카테고리 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteCategoryFaq(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteCategoryFaq", commandMap);
	}


	/**
	 * FAQ 카테고리로 등록된 리스트 모두 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteCategoryFaqList(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteCategoryFaqList", commandMap);
	}

	/**
	 * FAQ 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectFaqListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectFaqListTotCnt", commandMap);
	}
	
	/**
	 * FAQ 페이지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectFaqListPage(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectFaqListPage", commandMap);
	}
	
	/**
	 * FAQ 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectFaqList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectFaqList", commandMap);
	}
	
	
	/**
	 * FAQ 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectFaqView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.selectFaqView", commandMap);
	}
	
	/**
	 * FAQ 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertFaq(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertFaq", commandMap);
	}
	
	/**
	 * FAQ 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateFaq(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateFaq", commandMap);
	}



	/**
	 * FAQ 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteFaq(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteFaq", commandMap);
	}

	
	/**
	 * 출석고사장 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSchoolRoomList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectSchoolRoomList", commandMap);
	}
	
	
	/**
	 * 출석고사장 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSchoolRoomView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.selectSchoolRoomView", commandMap);
	}
	
	/**
	 * 출석고사장 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertSchoolRoom(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertSchoolRoom", commandMap);
	}
	
	/**
	 * 출석고사장 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateSchoolRoom(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateSchoolRoom", commandMap);
	}



	/**
	 * 출석고사장 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteSchoolRoom(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteSchoolRoom", commandMap);
	}

	
	
	
	
	/**
	 * QNA 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectQnaList(Map<String, Object> commandMap) throws Exception{
		return list("noticeAdminDAO.selectQnaList", commandMap);
	}
	
	/**
	 * QNA 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaListTotCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectQnaListTotCnt", commandMap);
	}
	
	/**
	 * QNA 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectQnaView(Map<String, Object> commandMap) throws Exception {
		return (Map<?, ?>)selectByPk("noticeAdminDAO.selectQnaView", commandMap);
	}
	
	/**
	 * QNA 등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertQna(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertQna", commandMap);
	}
	
	
	/**
	 * QNA 답변등록
	 * @param request
	 * @param response
	 * @param commandMap
	 * @param model
	 * @return
	 * @throws Exception
	 */
	public Object insertQnaReply(Map<String, Object> commandMap) throws Exception{
		return insert("noticeAdminDAO.insertQnaReply", commandMap);
	}
	
	
	/**
	 * QNA 업데이트 -- 관리자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateQna(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateQna", commandMap);
	}
	
	
	/**
	 * QNA 조회수 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateQnaCount(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateQnaCount", commandMap);
	}



	/**
	 * QNA 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int deleteQna(Map<String, Object> commandMap) throws Exception{
		return delete("noticeAdminDAO.deleteQna", commandMap);
	}

	
	/**
	 * QNA 업데이트 -- 사용자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateQnaUser(Map<String, Object> commandMap) throws Exception{
		return update("noticeAdminDAO.updateQnaUser", commandMap);
	}
	
	
	/**
	 * 공지사항 레이어 팝업 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectNoticeViewAll(Map<String, Object> commandMap) throws Exception {
		return list("noticeAdminDAO.selectNoticeViewAll", commandMap);
	}
	
	/**
	 * 스팸 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaSpamCnt(Map<String, Object> commandMap) throws Exception{
		return (Integer)getSqlMapClientTemplate().queryForObject("noticeAdminDAO.selectQnaSpamCnt", commandMap);
	}
	
	
	
}
