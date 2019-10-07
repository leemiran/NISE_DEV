package egovframework.svt.adm.hom.train;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.svt.util.FileUtil;

@Service
public class AdminTrainService {

	/** log */
	protected static final Log log = LogFactory.getLog(AdminTrainService.class);
	
	@Autowired
	AdminTrainDAO adminTrainDAO;
	
	@Autowired
	EgovMessageSource egovMessageSource;
	
	@Autowired
	FileUtil fileUtil;
	
	public List<?> trainList() {
		return adminTrainDAO.getTrainList();
	}

	public Map<String, String> insertTrain(Map<String, Object> commandMap) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		adminTrainDAO.insertTrain(commandMap);
		resultMap.put("resultCode", "1");
		resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.insert"));
		return resultMap;
	}

	public Map<String, String> trainDetail(String trainSeq) {
		return adminTrainDAO.getTrainDetail(trainSeq);
	}

	public Map<String, String> updateTrain(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		int updateCnt = adminTrainDAO.updateTrain(commandMap);
		if(0 < updateCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.update"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.update"));
		}
		return resultMap;
	}
	
	public Map<String, String> deleteTrain(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		// 파일삭제
		List<Map<String, String>> trainSubjList = (List<Map<String, String>>) adminTrainDAO.getTrainSubjList(commandMap);
		
		for(Map<String, String> trainSubj: trainSubjList) {
			// 삭제하기 전 정보를 가져온다
			Map<String, String> trainSubjDetail = adminTrainDAO.getTrainSubjDetail(String.valueOf(trainSubj.get("trainSubjSeq")));
			
			// 파일삭제
			if(null != trainSubjDetail.get("imgId") && !"".equals(trainSubjDetail.get("imgId"))) {
				fileUtil.deleteFile("train", trainSubjDetail.get("imgId"));
			}
		}
		// 파일삭제 끝
		
		int deleteCnt = adminTrainDAO.deleteTrain(commandMap);
		adminTrainDAO.deleteTrainSubjFromTrain(commandMap);
		if(0 < deleteCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.delete"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
		}
		return resultMap;
	}

	public List<?> trainSubjList(Map<String, Object> commandMap) {
		return adminTrainDAO.getTrainSubjList(commandMap);
	}
	
	public Map<String, String> insertTrainSubj(Map<String, Object> commandMap) throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		adminTrainDAO.insertTrainSubj(commandMap);
		resultMap.put("resultCode", "1");
		resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.insert"));
		return resultMap;
	}

	public Map<String, String> trainSubjDetail(String trainSubjSeq) {
		return adminTrainDAO.getTrainSubjDetail(trainSubjSeq);
	}

	public Map<String, String> updateTrainSubj(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(null != commandMap.get("uploadFileName") && !"".equals(commandMap.get("uploadFileName"))) {
			Map<String, String> trainSubjDetail = adminTrainDAO.getTrainSubjDetail(commandMap.get("trainSubjSeq").toString());
			this.deleteTrainSubjImg(trainSubjDetail.get("imgId"));
		}
		
		int updateCnt = adminTrainDAO.updateTrainSubj(commandMap);
		if(0 < updateCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.update"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.update"));
		}
		return resultMap;
	}
	
	public Map<String, String> deleteTrainSubj(Map<String, Object> commandMap) {
		Map<String, String> resultMap = new HashMap<String, String>();

		// 파일삭제
		// 삭제하기 전 정보를 가져온다
		Map<String, String> trainSubjDetail = adminTrainDAO.getTrainSubjDetail(commandMap.get("trainSubjSeq").toString());
		
		// 파일삭제
		if(null != trainSubjDetail.get("imgId") && !"".equals(trainSubjDetail.get("imgId"))) {
			fileUtil.deleteFile("train", trainSubjDetail.get("imgId"));
		}
		// 파일삭제 끝
		
		int deleteCnt = adminTrainDAO.deleteTrainSubj(commandMap);
		if(0 < deleteCnt) {
			resultMap.put("resultCode", "1");
			resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.delete"));
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
		}
		return resultMap;
	}

	public Map<String, String> deleteTrainSubjImg(String imgId) {
		Map<String, String> resultMap = new HashMap<String, String>();
		
		if(fileUtil.deleteFile("train", imgId)) {
			int deleteCnt = adminTrainDAO.deleteTrainSubjImg(imgId);
			if(0 < deleteCnt) {
				resultMap.put("resultCode", "1");
				resultMap.put("resultMsg", egovMessageSource.getMessage("success.common.delete"));
			} else {
				resultMap.put("resultCode", "0");
				resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
			}
		} else {
			resultMap.put("resultCode", "0");
			resultMap.put("resultMsg", egovMessageSource.getMessage("fail.common.delete"));
		}
		return resultMap;
	}
}
