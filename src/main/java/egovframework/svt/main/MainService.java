package egovframework.svt.main;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

	/** log */
	protected static final Log log = LogFactory.getLog(MainService.class);
	
	@Autowired
	MainDAO MainDAO;

	public List<?> getTrainSubjList() {
		return MainDAO.getTrainSubjList();
	}

	public List<?> getCalendarPeriodList() {
		return MainDAO.getCalendarPeriodList();
	}
	
}
