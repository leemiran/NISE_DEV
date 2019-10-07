package egovframework.adm.hom.not.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.hom.not.dao.NoticeAdminDAO;
import egovframework.adm.hom.not.service.NoticeAdminService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.apache.log4j.Logger;

@Service("noticeAdminService")
public class NoticeAdminServiceImpl extends EgovAbstractServiceImpl implements NoticeAdminService{
	
	@Resource(name="noticeAdminDAO")
    private NoticeAdminDAO noticeAdminDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	public int selectTableseq(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectTableseq(commandMap);
	}

	public List selectNoticeListAll(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectNoticeListAll(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오전 1:54:22 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecListAll(java.util.Map)
	 */
	public List srSystemSpecListAll(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.srSystemSpecListAll(commandMap);
	}

	public int selectNoticeListTotCnt(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectNoticeListTotCnt(commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 8:19:36 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecListTotCnt(java.util.Map)
	 */
	public int srSystemSpecListTotCnt(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.srSystemSpecListTotCnt(commandMap);
	}
	
	public List selectNoticeList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectNoticeList(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 8:22:04 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecList(java.util.Map)
	 */
	public List srSystemSpecList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.srSystemSpecList(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 1. 오후 2:00:29 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecListExcelDown(java.util.Map)
	 */
	public List srSystemSpecListExcelDown(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.srSystemSpecListExcelDown(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 1. 오후 2:00:20 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectGrcodeList(java.util.Map)
	 */
	public List selectGrcodeList(Map<String, Object> commandMap) throws Exception{
		List list = new ArrayList();
		String gadmin = (String)commandMap.get("gadmin");
		if( gadmin.equals("F") || gadmin.equals("P") ){
			list = noticeAdminDAO.selectGrcodeList(commandMap);
		}else if( gadmin.equals("H") ){
			list = noticeAdminDAO.selectGrcodeList2(commandMap);
		}else if( gadmin.equals("K") ){
			list = noticeAdminDAO.selectGrcodeList3(commandMap);
		}else{
			list = noticeAdminDAO.selectGrcodeList4(commandMap);
		}
		return list;
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
	public boolean insertNotice(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		
		boolean isok = false;
		
		try {
			Object o = noticeAdminDAO.insertNotice(commandMap);
			
			if(o != null)
			{
				//보드인서트 일련번호
				commandMap.put("p_seq", o);
				
				//파일인서트
				if(fileList != null)
				{
					for(int i=0; i<fileList.size(); i++)
					{
						HashMap fileh = (HashMap)fileList.get(i);
						commandMap.putAll(fileh);
						
						o = noticeAdminDAO.insertBoardFile(commandMap);
						
						logger.info(this.getClass().getName() + " 보드 파일 인서트 seq : " + o);
					}
				}
				isok = true;
			}
			else
			{
				isok = false;
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오전 1:18:00 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#insertSrSystem(java.util.Map, java.util.List)
	 */
	public boolean insertSrSystem(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		
		boolean isok = false;
		
		try {
			Object o = noticeAdminDAO.insertSrSystem(commandMap);			
			isok = true;
			if(o != null)
			{
				//보드인서트 일련번호
				commandMap.put("p_seq", o);
				
				//파일인서트
				if(fileList != null)
				{
					for(int i=0; i<fileList.size(); i++)
					{
						HashMap fileh = (HashMap)fileList.get(i);
						commandMap.putAll(fileh);
						
						o = noticeAdminDAO.insertBoardFile(commandMap);
						
						logger.info(this.getClass().getName() + " 보드 파일 인서트 seq : " + o);
					}
				}
				isok = true;
			}
			else
			{
				isok = false;
			}
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 4:17:14 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#insertSrSystemReply(java.util.Map, java.util.List)
	 */
	public boolean insertSrSystemReply(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		
		boolean isok = false;
		
		try {
			Object o = noticeAdminDAO.insertSrSystemReply(commandMap);			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * 공지사항 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectNoticeView(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.selectNoticeView(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 20. 오후 11:29:20 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecView(java.util.Map)
	 */
	public Map<?, ?> srSystemSpecView(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.srSystemSpecView(commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 1:12:01 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecViewList(java.util.Map)
	 */
	public Map<?, ?> srSystemSpecViewList(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.srSystemSpecViewList(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오후 12:03:08 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#srSystemSpecViewListRe(java.util.Map)
	 */
	public Map<?, ?> srSystemSpecViewListRe(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.srSystemSpecViewListRe(commandMap);
	}

	/**
	 * 공지사항 첨부파일 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectBoardFileList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectBoardFileList(commandMap);
	}
	
	/**
	 * 공지사항 조회 수 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int updateNoticeReadCount(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.updateNoticeReadCount(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 2:20:03 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectReplyCount(java.util.Map)
	 */
	public int selectReplyCount(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectReplyCount(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:29:52 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectSrSystemReSeq(java.util.Map)
	 */
	public int selectSrSystemReSeq(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectSrSystemReSeq(commandMap);
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 29. 오후 6:28:14 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectConfirmDateCnt(java.util.Map)
	 */
	public int selectConfirmDateCnt(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectConfirmDateCnt(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:58:14 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectSrSystemSrListSeqMin(java.util.Map)
	 */
	public float selectSrSystemSrListSeqMin(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectSrSystemSrListSeqMin(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 8. 4. 오후 2:33:02 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectSrSystemSrListSeqMinRe(java.util.Map)
	 */
	public float selectSrSystemSrListSeqMinRe1(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectSrSystemSrListSeqMinRe1(commandMap);
	}
	public float selectSrSystemSrListSeqMinRe2(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectSrSystemSrListSeqMinRe2(commandMap);
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 3:34:59 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#selectSrSystemSrLevel(java.util.Map)
	 */
	public int selectSrSystemSrLevel(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectSrSystemSrLevel(commandMap);
	}

	/**
	 * 공지사항 게시글삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteNotice(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
//			게시글삭제
			noticeAdminDAO.deleteNotice(commandMap);
			
//			파일삭제
			noticeAdminDAO.deleteBoardFile(commandMap);
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 9:17:04 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#deleteSrSystem(java.util.Map)
	 */
	public boolean deleteSrSystem(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
//			게시글삭제
			noticeAdminDAO.deleteSrSystem(commandMap);
			
//			파일삭제
			noticeAdminDAO.deleteBoardFile(commandMap);
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 10:09:46 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#deleteSrSystemReply(java.util.Map)
	 */
	public boolean deleteSrSystemReply(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		try {
//			게시글삭제
			noticeAdminDAO.deleteSrSystemReply(commandMap);
			
//			파일삭제
			noticeAdminDAO.deleteBoardFile(commandMap);
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
		
	}

	
	/**
	 * 공지사항 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateNotice(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		boolean isok = false;
		
		String [] _Array_p_fileseq = (String []) commandMap.get("_Array_p_fileseq");
		String p_tabseq = (String) commandMap.get("p_tabseq");
		String p_seq = (String) commandMap.get("p_seq");
		
		
		try {
//			게시글수정
			noticeAdminDAO.updateNotice(commandMap);
			
			
			
//			파일삭제
			if(_Array_p_fileseq != null)
			{
				for(int i=0; i<_Array_p_fileseq.length; i++)
				{
					HashMap<String, Object> filemap = new HashMap<String, Object>();
					filemap.put("p_tabseq", p_tabseq);
					filemap.put("p_seq", p_seq);
					filemap.put("p_fileseq", _Array_p_fileseq[i]);
					
					noticeAdminDAO.deleteBoardFile(filemap);
				}
			}
			
			
			//파일인서트
			if(fileList != null)
			{
				for(int i=0; i<fileList.size(); i++)
				{
					HashMap fileh = (HashMap)fileList.get(i);
					commandMap.putAll(fileh);
					
					Object o = noticeAdminDAO.insertBoardFile(commandMap);
					
					logger.info(this.getClass().getName() + " 보드 파일 인서트 seq : " + o);
				}
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}

	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 5:24:29 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#updateSrSystem(java.util.Map, java.util.List)
	 */
	public boolean updateSrSystem(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		boolean isok = false;
		
		String [] _Array_p_fileseq = (String []) commandMap.get("_Array_p_fileseq");
		String p_tabseq = (String) commandMap.get("p_tabseq");
		String p_seq = (String) commandMap.get("p_seq");
		
		
		try {
//			게시글수정
			noticeAdminDAO.updateSrSystem(commandMap);			
			
//			파일삭제
			if(_Array_p_fileseq != null)
			{
				for(int i=0; i<_Array_p_fileseq.length; i++)
				{
					HashMap<String, Object> filemap = new HashMap<String, Object>();
					filemap.put("p_tabseq", p_tabseq);
					filemap.put("p_seq", p_seq);
					filemap.put("p_fileseq", _Array_p_fileseq[i]);
					
					noticeAdminDAO.deleteBoardFile(filemap);
				}
			}
			
			
			//파일인서트
			if(fileList != null)
			{
				for(int i=0; i<fileList.size(); i++)
				{
					HashMap fileh = (HashMap)fileList.get(i);
					commandMap.putAll(fileh);
					
					Object o = noticeAdminDAO.insertBoardFile(commandMap);
					
					logger.info(this.getClass().getName() + " 보드 파일 인서트 seq : " + o);
				}
			}
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 29. 오후 6:41:10 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 */
	public boolean confirmDateUpdate(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		
		String p_tabseq = (String) commandMap.get("p_tabseq");
		String p_seq = (String) commandMap.get("p_seq");
		
		
		try {
//			게시글수정
			noticeAdminDAO.confirmDateUpdate(commandMap);			

			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
		
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 3. 21. 오후 5:25:19 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#updateSrSystemReply(java.util.Map, java.util.List)
	 */
	public boolean updateSrSystemReply(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		boolean isok = false;
		
		String [] _Array_p_fileseq = (String []) commandMap.get("_Array_p_fileseq");
		String p_tabseq = (String) commandMap.get("p_tabseq");
		String p_seq = (String) commandMap.get("p_seq");
		
		
		try {
//			게시글수정
			noticeAdminDAO.updateSrSystemReply(commandMap);			
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	/**
	 * 
	 * 설  명 :
	 * @modify  2016. 6. 27. 오전 11:12:41 - 최성민
	 * This Area Change Content Write
	 * @param commandMap
	 * @param fileList
	 * @return
	 * @throws Exception
	 * @see egovframework.adm.hom.not.service.NoticeAdminService#updateSrSystemReply1(java.util.Map, java.util.List)
	 */
	public boolean updateSrSystemReply1(Map<String, Object> commandMap, List<?> fileList) throws Exception{
		boolean isok = false;
		
		String [] _Array_p_fileseq = (String []) commandMap.get("_Array_p_fileseq");
		String p_tabseq = (String) commandMap.get("p_tabseq");
		String p_seq = (String) commandMap.get("p_seq");
		
		
		try {
//			게시글수정
			noticeAdminDAO.updateSrSystemReply1(commandMap);			
			
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return isok;
	}
	
	
	/**
	 * faq 카테고리 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectCategoryList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectCategoryList(commandMap);
	}
	
	/**
	 * faq 카테고리 보기
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectCategoryView(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectCategoryView(commandMap);
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
	public boolean insertCategoryFaq(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			noticeAdminDAO.insertCategoryFaq(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}
	
	/**
	 * FAQ 카테고리 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateCategoryFaq(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			int cnt = noticeAdminDAO.updateCategoryFaq(commandMap);
			if(cnt > 0) isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}



	/**
	 * FAQ 카테고리 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCategoryFaq(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			//FAQ 삭제
			noticeAdminDAO.deleteCategoryFaq(commandMap);
			//FAQ 리스트 삭제
			noticeAdminDAO.deleteCategoryFaqList(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	
	}


	/**
	 * FAQ 전체개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectFaqListTotCnt(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectFaqListTotCnt(commandMap);
	}
	
	/**
	 * FAQ 페이지 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectFaqListPage(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectFaqListPage(commandMap);
	}
	
	
	/**
	 * FAQ 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectFaqList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectFaqList(commandMap);
	}
	
	
	/**
	 * FAQ 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectFaqView(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.selectFaqView(commandMap);
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
		return noticeAdminDAO.insertFaq(commandMap);
	}
	
	/**
	 * FAQ 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateFaq(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			int cnt = noticeAdminDAO.updateFaq(commandMap);
			if(cnt > 0) isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}

	/**
	 * FAQ 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFaq(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			noticeAdminDAO.deleteFaq(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}
	
	
	
	/**
	 * 출석고사장 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectSchoolRoomList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectSchoolRoomList(commandMap);
	}
	
	
	/**
	 * 출석고사장 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectSchoolRoomView(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.selectSchoolRoomView(commandMap);
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
		return noticeAdminDAO.insertSchoolRoom(commandMap);
	}
	
	/**
	 * 출석고사장 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateSchoolRoom(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			int cnt = noticeAdminDAO.updateSchoolRoom(commandMap);
			if(cnt > 0) isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}

	/**
	 * 출석고사장 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSchoolRoom(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			noticeAdminDAO.deleteSchoolRoom(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}
	
	
	
	
	/**
	 * QNA 리스트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectQnaList(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectQnaList(commandMap);
	}
	
	
	/**
	 * QNA 총개수
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaListTotCnt(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectQnaListTotCnt(commandMap);
	}
	
	/**
	 * QNA 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public Map<?, ?> selectQnaView(Map<String, Object> commandMap) throws Exception {
		
		//보기
		return noticeAdminDAO.selectQnaView(commandMap);
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
	public boolean insertQna(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			noticeAdminDAO.insertQna(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
		
		
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
	public boolean insertQnaReply(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			noticeAdminDAO.insertQnaReply(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
		
	}
	
	/**
	 * QNA 업데이트  -- 관리자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateQna(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			noticeAdminDAO.updateQna(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}
	
	/**
	 * QNA 조회수 업데이트
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateQnaCount(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			noticeAdminDAO.updateQnaCount(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}

	/**
	 * QNA 삭제
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean deleteQna(Map<String, Object> commandMap) throws Exception{
		
		boolean isok = false;
		try {
			
			noticeAdminDAO.deleteQna(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}

	/**
	 * QNA 업데이트 -- 사용자용
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public boolean updateQnaUser(Map<String, Object> commandMap) throws Exception{
		boolean isok = false;
		try {
			
			noticeAdminDAO.updateQnaUser(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}
	
	
	/**
	 * 공지사항 레이어 팝업 보기 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public List selectNoticeViewAll(Map<String, Object> commandMap) throws Exception {
		return noticeAdminDAO.selectNoticeViewAll(commandMap);
	}
	
	/**
	 * 스팸 조회
	 * @param commandMap
	 * @return
	 * @throws Exception
	 */
	public int selectQnaSpamCnt(Map<String, Object> commandMap) throws Exception{
		return noticeAdminDAO.selectQnaSpamCnt(commandMap);
	}
	
}
