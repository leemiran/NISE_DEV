package egovframework.svt.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;

/**
 * EgovFileMngUtil 참조
 * @author min
 *
 */
@Service
public class FileUtil {
	protected Log log = LogFactory.getLog(this.getClass());

	public Map<String, String> uploadImage(HttpServletRequest request, String dir, String paramNm) {
		Map<String, String> imgMap = new HashMap<String, String>();
		
		try {
			//기본 업로드 폴더
			String defaultDP = EgovProperties.getProperty("Globals.defaultDP");
			log.info("- 기본 업로드 폴더 : " + defaultDP);
			
			//파일업로드를 실행한다.
			MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest) request;
			MultipartFile file = mptRequest.getFile(paramNm);
			
			if (file.getSize() > 0) {
				imgMap.putAll(uploadContentFile(file, defaultDP + File.separator + dir));
			}
		} catch (Exception e) {
			log.error(e.toString());
		}
		return imgMap;
	}

	public HashMap<String, String> uploadContentFile(MultipartFile file, String filePath) throws Exception {

		File cFile = new File(filePath.substring(0, filePath.lastIndexOf("/")));
		createParentPath(cFile);
		if (!cFile.isDirectory())
			cFile.mkdir();

		HashMap<String, String> map = new HashMap<String, String>();
		String newName = "";
		String orginFileName = file.getOriginalFilename();

		int index = orginFileName.lastIndexOf(".");
		String fileExt = orginFileName.substring(index + 1);

		newName = String.valueOf(UUID.randomUUID()) + "." + fileExt;
		log.info("###############  newName " + newName);

		writeFile(file, newName, filePath);
		map.put(Globals.ORIGIN_FILE_NM, orginFileName);
		map.put(Globals.UPLOAD_FILE_NM, newName);

		return map;
	}

	public boolean createParentPath(File cFile) throws IOException {
		ArrayList p = new ArrayList();
		File parent = cFile.getParentFile();
		COMPLETE: while (parent != null) {
			if (!parent.exists())
				p.add(parent);
			else
				break COMPLETE;
			parent = parent.getParentFile();
		}
		for (int i = p.size() - 1; i >= 0; i--)
			((File) p.get(i)).mkdir();

		return true;
	}

	public void writeFile(MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(stordFilePath);

			if (!cFile.isDirectory())
				cFile.mkdir();

			bos = new FileOutputStream(stordFilePath + File.separator + newName);

			int bytesRead = 0;
			byte[] buffer = new byte[EgovFileMngUtil.BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, EgovFileMngUtil.BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception ignore) {
					Logger.getLogger(EgovFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception ignore) {
					Logger.getLogger(EgovFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
				}
			}
		}
	}
	
	public boolean deleteFile(String dir, String imgId) {
		return new File(EgovProperties.getProperty("Globals.defaultDP") + File.separator + dir + File.separator + imgId).delete();
	}
}
