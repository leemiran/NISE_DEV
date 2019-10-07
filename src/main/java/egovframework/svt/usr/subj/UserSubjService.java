package egovframework.svt.usr.subj;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSubjService {

	@Autowired
	UserSubjDAO userSubjDAO;

	public Object getSubjStudentLimit(Map<String, Object> commandMap) {
		return userSubjDAO.getSubjStudentLimit(commandMap);
	}
}
