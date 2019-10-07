package egovframework.svt.main;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;

@Repository
public class MainDAO extends EgovAbstractDAO {

	public List<?> getTrainSubjList() {
		return list("main.getTrainSubjList", null);
	}

	public List<?> getCalendarPeriodList() {
		return list("main.getCalendarPeriodList", null);
	}

	
}
