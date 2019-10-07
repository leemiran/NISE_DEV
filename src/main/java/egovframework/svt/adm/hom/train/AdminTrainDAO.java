package egovframework.svt.adm.hom.train;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class AdminTrainDAO extends EgovAbstractDAO {

	public List<?> getTrainList() {
		return list("adminTrain.getTrainList", null);
	}

	public void insertTrain(Map<String, Object> commandMap) {
		insert("adminTrain.insertTrain", commandMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getTrainDetail(String trainSeq) {
		return (Map<String, String>) selectByPk("adminTrain.getTrainDetail", trainSeq);
	}

	public int updateTrain(Map<String, Object> commandMap) {
		return update("adminTrain.updateTrain", commandMap);
	}
	
	public int deleteTrain(Map<String, Object> commandMap) {
		return delete("adminTrain.deleteTrain", commandMap);
	}
	
	public int deleteTrainSubjFromTrain(Map<String, Object> commandMap) {
		return delete("adminTrain.deleteTrainSubjFromTrain", commandMap);
	}

	public List<?> getTrainSubjList(Map<String, Object> commandMap) {
		return list("adminTrain.getTrainSubjList", commandMap);
	}
	
	public void insertTrainSubj(Map<String, Object> commandMap) {
		insert("adminTrain.insertTrainSubj", commandMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getTrainSubjDetail(String trainSeq) {
		return (Map<String, String>) selectByPk("adminTrain.getTrainSubjDetail", trainSeq);
	}

	public int updateTrainSubj(Map<String, Object> commandMap) {
		return update("adminTrain.updateTrainSubj", commandMap);
	}
	
	public int deleteTrainSubj(Map<String, Object> commandMap) {
		return delete("adminTrain.deleteTrainSubj", commandMap);
	}

	public int deleteTrainSubjImg(String imgId) {
		return update("adminTrain.deleteTrainSubjImg", imgId);
	}

}
