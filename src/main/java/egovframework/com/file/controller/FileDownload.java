package egovframework.com.file.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * <pre>
 * 파일 다운로드
 * </pre>
 * 
 * @author 
 */
public class FileDownload extends AbstractView{
	
	public FileDownload() {		
        //받드시 octet-stream으로 설정
        super.setContentType("application/octet-stream");
    }

    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request,
            HttpServletResponse response) throws Exception  {    	
    	
		File file = (File) model.get("downloadFile");
		String fileName = model.get("fileName").toString();
		
		response.setContentType("application/octet-stream");
		response.setContentLength((int) file.length());		
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + java.net.URLEncoder.encode(fileName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");		
		
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);	
			FileCopyUtils.copy(fis, out);		
		} catch (IOException ex) {	
			
		}finally {
			if (fis != null)	fis.close();				
		}

    }
	

}
