package egovframework.adm.cash.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.springframework.stereotype.Service;

import egovframework.adm.book.dao.CpBookAdminDAO;
import egovframework.adm.cash.dao.CashPrintDAO;
import egovframework.adm.cash.service.CashPrintService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("cashPrintService")
public class CashPrintServiceImpl extends EgovAbstractServiceImpl implements CashPrintService{

	@Resource(name="cashPrintDAO")
    private CashPrintDAO cashPrintDAO;

	
	public List selectCashPrintList(Map<String, Object> commandMap)
			throws Exception {
		return cashPrintDAO.selectCashPrintList(commandMap);
	}


	public Map<?, ?> selectAdminCashPrint(Map<String, Object> commandMap)
			throws Exception {
		return cashPrintDAO.selectAdminCashPrint(commandMap);
	}


	public boolean deleteCashPrint(Map<String, Object> commandMap)
			throws Exception {
		boolean isok = false;
		try {
			
			cashPrintDAO.deleteCashPrint(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}


	public boolean insertCashPrint(Map<String, Object> commandMap)
			throws Exception {
		boolean isok = false;
		try {
			
			cashPrintDAO.insertCashPrint(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}


	public boolean updateCashPrint(Map<String, Object> commandMap)
			throws Exception {
		boolean isok = false;
		try {
			
			cashPrintDAO.updateCashPrint(commandMap);
			isok = true;
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		return isok;
	}


	public Map<?, ?> selectAdminCashPrintView(Map<String, Object> commandMap)
			throws Exception {
		return cashPrintDAO.selectAdminCashPrintView(commandMap);
	}

	
}
