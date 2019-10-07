package egovframework.adm.lcms.nuc.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface NotUseContentService {

	public int selectNotUseContenListTotCnt(Map<String, Object> commandMap) throws Exception;
	
	public List selectNotUseContenListList(Map<String, Object> commandMap) throws Exception;
	
}