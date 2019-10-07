package egovframework.adm.lcms.nuc.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.lcms.nuc.dao.NotUseContentDAO;
import egovframework.adm.lcms.nuc.service.NotUseContentService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.FileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.excel.impl.EgovExcelServiceImpl;
import egovframework.rte.fdl.filehandling.EgovFileUtil;

@Service("notUseContentService")
public class NotUseContentServiceImpl extends EgovAbstractServiceImpl implements NotUseContentService{
	
	@Resource(name="notUseContentDAO")
    private NotUseContentDAO notUseContentDAO;
	
	/** fileUtil */
    @Resource(name="EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;
    
    /** EgovMessageSource */
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;

	public int selectNotUseContenListTotCnt(Map<String, Object> commandMap) throws Exception{
		return notUseContentDAO.selectNotUseContenListTotCnt(commandMap);
	}
	
	public List selectNotUseContenListList(Map<String, Object> commandMap) throws Exception{
		return notUseContentDAO.selectNotUseContenListList(commandMap);
	}	
}
