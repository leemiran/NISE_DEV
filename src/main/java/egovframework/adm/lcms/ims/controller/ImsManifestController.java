package egovframework.adm.lcms.ims.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.adm.lcms.ims.mainfest.manifestTableBean;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.file.controller.FileController;
import egovframework.com.lcms.inn.controller.innoDSFileUploadController;


/** 
 * 
 * @author 
 * @since 
 * @version 1.0
 * @see
 *
 * <pre>
 *
 * </pre>
 */
@Controller
public class ImsManifestController {

	/** log */
    protected static final Log log = LogFactory.getLog(innoDSFileUploadController.class);
    
    /**
	 * imsmanafest.xml 내용확인
	 * 
	 * @param curmQustManageService 
	 * @param Map commandMap, ModelMap model
	 * @return 출력페이지정보 "adm/ims/imsManifestList.jsp"
	 * @exception Exception 
	 */	
	@RequestMapping(value="/adm/lcms/ims/imsManifestList.do")
	public String uploadForm(HttpServletRequest request, HttpServletResponse response, Map<String, Object> commandMap, ModelMap model) throws Exception { 

		String strSavePath = (String)commandMap.get("path");
		ArrayList maniList = null;
		ArrayList imsPath = new ArrayList();
		ArrayList manis = new ArrayList();
		ArrayList result = new ArrayList();
		
		FileController file = new FileController();
		String contents[] = (new File(strSavePath)).list();
		for(int j=0; j<contents.length; j++){
			String saveFile = contents[j];
			imsPath = file.manifestUnZip(strSavePath, saveFile);
			
			if( imsPath != null ){
				maniList = getManiList(imsPath);
				int[] mani_cnt = new int[imsPath.size()];
				for( int k=0; k<maniList.size(); k++ ){
					String[][] mani_list = (String[][]) maniList.get(k);
					mani_cnt[k] = mani_list.length;
					manis.add(mani_list);
					
					for ( int i=0; i<mani_cnt[k]; i++ ){
						for( int r=0; r<mani_list[i].length; r++ ){
							mani_list[i][r] = mani_list[i][r] == null ? "" : mani_list[i][r];													
						}
						HashMap<String, Object> m = new HashMap<String, Object>();
						String type = mani_list[i][0].toUpperCase().indexOf("ORGANIZATION") != -1 ? "Y" : "N";
						m.put("type", 		type);
						m.put("gubun", 		mani_list[i][0]);
						m.put("title", 		mani_list[i][1]);
						m.put("fileName", 	mani_list[i][2]);
						m.put("filePath", 	mani_list[i][3]);
						m.put("scormType", 	mani_list[i][4]);
						m.put("status", 	mani_list[i][5]);
						m.put("lmsValue", 	mani_list[i][6]);
						m.put("score", 		mani_list[i][7]);
						result.add(m);
						
					}
				}
			}
		}
		
		
		model.addAttribute("maniList", result);
		model.addAllAttributes(commandMap);
		
	  	return "adm/lcms/ims/imsManifestList";
	}
	
	
	public ArrayList getManiList(ArrayList savePath) throws Exception{
        String[][] table=null;
        ArrayList retval = new ArrayList();
		try
		{
			for( int i=0; i<savePath.size(); i++ ){
				String path = (String)savePath.get(i);
				manifestTableBean tablebean = new manifestTableBean();
				tablebean.setXmlFile(path, "imsmanifest.xml", "");
				table = tablebean.getTableValue();
				retval.add(table);
			}
	        
		}catch(Exception e) {
			log.info("getManiList Exception : "+e);
		}
		return retval;
	}
}
