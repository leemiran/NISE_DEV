package egovframework.adm.com.cmp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.adm.com.cmp.dao.SelectCompanyDAO;
import egovframework.adm.com.cmp.service.SelectCompanyService;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("selectCompanyService")
public class SelectCompanyServiceImpl extends EgovAbstractServiceImpl implements SelectCompanyService{
	
	@Resource(name="selectCompanyDAO")
    private SelectCompanyDAO selectCompanyDAO;

	public List getCompany(Map<String, Object> commandMap) throws Exception{
		List list = new ArrayList();
		String gadmin = (String)commandMap.get("pp_gadmin");
		if( gadmin.equals("H") ){
			list = selectCompanyDAO.getCompany1(commandMap); // 	교육그룹관리자
		}else if( gadmin.equals("K") ){
			list = selectCompanyDAO.getCompany2(commandMap); // 	회사관리자, 부서관리자
		}else{
			list = selectCompanyDAO.getCompany3(commandMap); // 	Ultravisor, Supervisor, 과목관리자, 강사
		}
		return list;
	}

}
