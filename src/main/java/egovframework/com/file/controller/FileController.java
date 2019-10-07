package egovframework.com.file.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;


import net.sf.jazzlib.ZipEntry;
import net.sf.jazzlib.ZipFile;
import net.sf.jazzlib.ZipInputStream;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.com.cmm.service.Globals;
import egovframework.com.lcms.inn.controller.innoDSFileUploadController;
import egovframework.com.utl.fcc.service.EgovStringUtil;

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
public class FileController {
	/** log */
    protected static final Log log = LogFactory.getLog(innoDSFileUploadController.class);
    
    static String chararray="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static Random random=new Random(System.currentTimeMillis());	
    
    ArrayList _fileList = new ArrayList();
    
    Namespace na1 = Namespace.getNamespace("adlcp", "http://www.adlnet.org/xsd/adlcp_v1p3");
	Namespace na2 = Namespace.getNamespace("adlseq", "http://www.adlnet.org/xsd/adlseq_v1p3");
	Namespace na3 = Namespace.getNamespace("adlnav", "http://www.adlnet.org/xsd/adlnav_v1p3");
	Namespace na4 = Namespace.getNamespace("imsss", "http://www.imsglobal.org/xsd/imsss");
    Namespace na5 = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
    
    public String fileUnZip2(String strSavePath, String strSavePath2, String saveFile) throws Exception{
    	String imsPath = "";
    	try{
    		ZipFile zipFile = new ZipFile(strSavePath+"/"+saveFile);
    		File zFile = new File(strSavePath+"/"+saveFile);
    		for(Enumeration zipEntries = zipFile.entries(); zipEntries.hasMoreElements();){
    			ZipEntry entry = (ZipEntry)zipEntries.nextElement();
    			String name = entry.getName();
    			name = new String(name.getBytes("latin1"),"euc-kr");
                name = name.replaceAll("\\\\","/");
                InputStream zis = null;
                FileOutputStream fos = null;
                File uncompressedFile_dir = null;
                File uncompressedFile = null;
                if(!entry.isDirectory())
                {
                    zis = zipFile.getInputStream(entry);
                    if(name.split("/").length > 1){
                        uncompressedFile_dir = new File(strSavePath2+"/"+name.substring(0,name.lastIndexOf("/")));
                        uncompressedFile_dir.mkdirs();
                    }
                    String temp_file = strSavePath2+File.separator+name;
                    uncompressedFile = new File(temp_file);
                    if(!uncompressedFile.isDirectory()){
    	                fos = new FileOutputStream(uncompressedFile);
    	                byte buffer[] = new byte[4096];
    	                int read = 0;
    	                for(read = zis.read(buffer); read >= 0; read = zis.read(buffer))
    	                    fos.write(buffer, 0, read);
    	
    	                fos.close();
    	                zis.close();
                    }
                }
    			
    		}
    		zipFile.close();
    		zFile.delete();
        }catch (IOException ioe)
        {
        	// 폴더 삭제
    		this.deleteDirector(strSavePath);
            ioe.printStackTrace();
        }
        return imsPath;
    }
    
    
    public ArrayList fileUnZip(String strSavePath, String saveFile) throws Exception{
    	ArrayList imsPath = new ArrayList();
    	try{
    		String inputPath = strSavePath;
    		new File(inputPath).mkdirs();
    		//imsPath = inputPath;
    		
    		ZipFile zipFile = new ZipFile(strSavePath+"/"+saveFile);
    		File zFile = new File(strSavePath+"/"+saveFile);
    		for(Enumeration zipEntries = zipFile.entries(); zipEntries.hasMoreElements();){
    			ZipEntry entry = (ZipEntry)zipEntries.nextElement();
    			String name = entry.getName();
    			name = new String(name.getBytes("latin1"),"euc-kr");
                name = name.replaceAll("\\\\","/");
                InputStream zis = null;
                FileOutputStream fos = null;
                File uncompressedFile_dir = null;
                File uncompressedFile = null;
                if(!entry.isDirectory())
                {
                    zis = zipFile.getInputStream(entry);
                    if(name.split("/").length > 1){
                        uncompressedFile_dir = new File(inputPath+"/"+name.substring(0,name.lastIndexOf("/")));
                        uncompressedFile_dir.mkdirs();
                        imsPath.add(inputPath+"/"+name.substring(0,name.lastIndexOf("/")));
                    }
                    String temp_file = inputPath+File.separator+name;
                    uncompressedFile = new File(temp_file);
                    if(!uncompressedFile.isDirectory()){
    	                fos = new FileOutputStream(uncompressedFile);
    	                byte buffer[] = new byte[4096];
    	                int read = 0;
    	                for(read = zis.read(buffer); read >= 0; read = zis.read(buffer))
    	                    fos.write(buffer, 0, read);
    	
    	                fos.close();
    	                zis.close();
                    }
                }
                if( name.endsWith("imsmanifest.xml")){
                	String path = inputPath+File.separator+name;
					Document doc = readManifestFile(path);
					File file = new File(path);
					String encoding = "UTF-8";
					convertEncoding(file, encoding);
					saveManifestFile(doc, path);
                }
    			
    		}
    		zipFile.close();
    		zFile.delete();
        }catch (IOException ioe)
        {
        	// 폴더 삭제
    		this.deleteDirector(strSavePath);
            ioe.printStackTrace();
        }
        return imsPath;
    }

    
    
    /**
     * 컨텐츠 업로드 후 imsmanifest.xml파일만 압축해제
     * @param strSavePath
     * @param saveFile
     * @return
     * @throws Exception
     */
    public ArrayList manifestUnZip(String strSavePath, String saveFile) throws Exception{
    	ArrayList imsPath = new ArrayList();
    	try{
    		
    		File zp = null;
    		ZipInputStream in = null;
    		FileOutputStream output = null;
    		
			in = new ZipInputStream(new FileInputStream(strSavePath+"/"+saveFile));
			zp = new File(strSavePath+"/"+saveFile);
			ZipEntry entry = null;
			// input stream내의 압축된 파일들을 하나씩 읽어들임.
			String mkDirNm = "";
			while((entry = in.getNextEntry()) != null){
				// zip 파일의 구조와 동일하게 가기위해 로컬의 디렉토리 구조를 만듬.
				String entryName = entry.getName();
				if( entryName.endsWith("imsmanifest.xml") ){
					if( !new File(strSavePath+"/tmp").isDirectory() ){
						new File(strSavePath+"/tmp").mkdirs();
					}
					mkDirNm = entryName.replace("imsmanifest.xml", "");
					new File(strSavePath +"/tmp/"+ mkDirNm).mkdirs();
					imsPath.add(strSavePath+"/tmp/"+ mkDirNm);
					
					// 해제할 각각 파일의 output stream을 생성
					output = new FileOutputStream(strSavePath +"/tmp/"+ entryName);
					int bytes_read;
					byte buf[] = new byte[1024];
					while((bytes_read = in.read(buf)) != -1){
						output.write(buf, 0, bytes_read);
					}
					// 하나의 파일이 압축해제됨
					output.close();
					
					String path = strSavePath +"/tmp/"+ entryName;
					Document doc = readManifestFile(path);
					File file = new File(path);
					String encoding = "UTF-8";
					convertEncoding(file, encoding);
					saveManifestFile(doc, path);
				}
			}
			in.close();
			
    	}catch(Exception ex){
    		imsPath = null;
    		log.info("manifestUnZip Exception");
    		log.info("exception : "+ex);
    	}
    	
    	return imsPath;
    }
    
    
    public static void convertEncoding(File file, String encoding) throws Exception{
    	try{
    		BufferedReader in = new BufferedReader(new FileReader(file));
    		String read = null;
    		List list = new ArrayList();
    		while( (read = in.readLine()) != null ){
    			list.add(read);
    		}
    		in.close();
    		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
    		for( int i=0; i<list.size(); i++ ){
    			out.write((String)list.get(i));
    			out.newLine();
    		}
    		out.close();
    	}catch(IOException e){
    		e.printStackTrace();
    		throw e;
    	}
    }
    
    /**
	 * description: manifestfile read.
	 * @return Document
	 * @throws Exception
	 */
	public Document readManifestFile(String filename) {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new File(filename));

			return doc;
		} catch (Exception e) {
			return null;
		}
	}
    
    /**
     * 특정파일 삭제
     * @param path
     * @param withName
     * @return
     * @throws Exception
     */
    public boolean deleteZipFile(String path, String withName) throws Exception{
    	boolean result = true;
    	try{
    		File dir = new File(path);
    		String[] list = dir.list();
    		for( int i=0; i<list.length; i++ ){
    			if( list[i].endsWith(withName) ){
    				new File(path+"/"+list[i]).delete();
    			}
    		}
    	}catch(Exception ex){
    		result = false;
    		log.info("deleteZipFile Exception");
    		log.info("exception : "+ex);
    	}
    	return result;
    }
    
    /**
     * 하위디렉토리 전체 삭제
     * @param deletePath
     * @return
     * @throws Exception
     */
    public boolean deleteDirector(String deletePath) throws Exception{
    	boolean result = true;
    	try{
    		File dir = new File(deletePath);
    		String[] list = dir.list();
    		File file;
    		for( int i=0; i<list.length; i++ ){
    			file = new File(deletePath+File.separator+list[i] );
    			if( file.isDirectory() ){
    				deleteDirector(file.getPath()+File.separator );
    			}else{
    				file.delete();
    			}
    		}
    		dir.delete();
    	}catch(Exception ex){
    		result = false;
    		log.info("deleteDirector Exception");
    		log.info("exception : "+ex);
    	}
    	return result;
    }
    
    /**
     * 하위디렉토리 전체 삭제
     * @param deletePath
     * @return
     * @throws Exception
     */
    public boolean deleteDirector2(String deletePath) throws Exception{
    	boolean result = true;
    	try{
    		File dir = new File(deletePath);
    		String[] list = dir.list();
    		File file;
    		for( int i=0; i<list.length; i++ ){
    			file = new File(deletePath+File.separator+list[i] );
    			if( list[i].toUpperCase().endsWith(".XLS") ){
    				file.delete();
    			}
    		}
    		dir.delete();
    	}catch(Exception ex){
    		result = false;
    		log.info("deleteDirector Exception");
    		log.info("exception : "+ex);
    	}
    	return result;
    }
    
    /**
     * 차시폴더생성의 랜점키생성
     * @param len
     * @return
     */
    public static String getRandomKey(int len){
		String ret="";
		for(int i=0;i<len;i++){
			ret+=chararray.charAt(random.nextInt(chararray.length()));
		}
		return ret;
	}
    
    /**
     * transferTo() 사용한 파일 복사후 원래파일삭제[이게 젤루 빠름]
     * @param oldfile 원래파일명[폴더포함]
     * @param targetfile 새파일명[폴더포함]
     * @throws Exception
     */
    public static void moveDirs(File oldfile, File targetfile) throws Exception{

        if(oldfile.isDirectory()){
        	if(!targetfile.exists()){
        		targetfile.mkdirs();
        	}
        	String[] children = oldfile.list();
        	for(int i = 0; i < children.length; i++){
        		moveTransfer(new File(oldfile, children[i]),new File(targetfile, children[i]));
        	}
        }else{
        	oldfile.renameTo(targetfile);
        }
        oldfile.delete();
    }
    
    /**
     * transferTo() 사용한 파일 복사후 원래파일삭제[이게 젤루 빠름]
     * @param oldfile 원래파일명[폴더포함]
     * @param targetfile 새파일명[폴더포함]
     * @throws Exception
     */
    public static void moveTransfer(File oldfile, File targetfile) throws Exception{

        if(oldfile.isDirectory()){
        	if(!targetfile.exists()){
        		targetfile.mkdirs();
        	}
        	String[] children = oldfile.list();
        	for(int i = 0; i < children.length; i++){
        		moveTransfer(new File(oldfile, children[i]),new File(targetfile, children[i]));
        	}
        }else{
        	FileInputStream f_in=new FileInputStream(oldfile);
            FileOutputStream f_out=new FileOutputStream(targetfile);
            try{
	            byte[] buffer=new byte[1024];
	            int len=f_in.read(buffer);
	            while(len>0){
	            	f_out.write(buffer, 0, len);
	            	len=f_in.read(buffer);
	            }
	            f_out.flush();
            }finally{
	            f_out.close();
	            f_in.close();
            }
        }
        oldfile.delete();
    }
    
    /**
	 * description: pif size 조회.
	 * @return int retval
	 * @throws Exception
	 */	
	public static int getScoSize(String path){
		int retval = 0;
		FileController futil = new FileController();
		ArrayList files = futil.scanDir(path,true);
		for(int i = 0; i < files.size(); i++){
			File f = new File(files.get(i).toString());
			//KB로 계산시 나머지가 있으면 +1KB해줌.
			if(f.length() % 1024 != 0){
				retval += f.length()/1024 +1;
			}else{
				retval += f.length()/1024;
			}
		}
		return retval;
	}
	
	/**
     * 파일의 목록을 리턴
     * @param dir
     * @param checksubdir
     * @return
     */
	public ArrayList scanDir(String dir, boolean checksubdir){
		File file = new File(dir);
		if(file.exists()){
			for(int i = 0; i < file.list().length; i++){
				File tmpfile = new File(dir+File.separator+file.list()[i]);
				if(tmpfile.isDirectory() && checksubdir){
					this.scanDir(dir+File.separator+file.list()[i], checksubdir);
				}else{
					this._fileList.add(tmpfile);
				}
			}
		}
		return _fileList;
	}
	
	
	public void makeManifest(String savePath){
		try{
			Document main = null;
			Element root = new Element("manifest");
			root.setAttribute("identifier", getUniqueID());
			root.setAttribute("version", "2.0");
			root.addNamespaceDeclaration(na1);
			root.addNamespaceDeclaration(na2);
			root.addNamespaceDeclaration(na3);
			root.addNamespaceDeclaration(na4);
			root.setAttribute("schemaLocation",
					          "http://www.imsglobal.org/xsd/imscp_v1p1 imscp_v1p1.xsd " +
					          "http://www.adlnet.org/xsd/adlcp_v1p3 adlcp_v1p3.xsd " +
					          "http://www.adlnet.org/xsd/adlseq_v1p3 adlseq_v1p3.xsd " +
					          "http://www.adlnet.org/xsd/adlnav_v1p3 adlnav_v1p3.xsd " +
					          "http://www.imsglobal.org/xsd/imsss imsss_v1p0.xsd",
					          na5);
			
			// metadata Element구성
			Element el1 = new Element("metadata");
			Element el1_1 = new Element("schema");
			Element el1_2 = new Element("schemaversion");
			el1_1.setText("LCMS KEM");
			el1_2.setText("KEM 2.0");
			el1.addContent(el1_1);
			el1.addContent(el1_2);
			// organization구성
			Element el2 = new Element("organizations");
			el2.setAttribute("default", getUniqueID());
			
			Element el2_1 = new Element("organization");
			el2_1.setAttribute("identifier", el2.getAttributeValue("default"));
			
			Element title = new Element("title");
			title.setText("신규차시");
			el2_1.addContent(title);
			el2.addContent(el2_1);
			
			// resource구성
			Element el3 = new Element("resources");
			
			root.addContent(el1);
			root.addContent(el2);
			root.addContent(el3);
			
			main = new Document();
			main.setRootElement(root);
			
			saveManifestFile(main, savePath + "/imsmanifest.xml");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public boolean saveManifestFile(Document doc, String fileName){
		OutputStream manifestOut = null;
		String dir = "";
		boolean flag = false;
		try{
			manifestOut = new FileOutputStream(fileName.trim(), false);
		}catch( FileNotFoundException e ){
			String filedir[] = fileName.split("/");
			for( int i=1; i<filedir.length-1; i++ ){
				dir += "/" + filedir[i].trim();
				new File(filedir[0].trim() + "/" + dir).mkdirs();
			}
			
			try{
				manifestOut = new FileOutputStream(fileName.trim(), false);
			}catch( FileNotFoundException e1 ){
				e1.printStackTrace();
				return false;
			}
		}
		XMLOutputter outputter = new XMLOutputter();
		Format f = outputter.getFormat();
		try{
			f.setEncoding("utf-8");
			f.setLineSeparator("\r\n");
			f.setIndent("    ");
			outputter.setFormat(f);
			outputter.output(doc, manifestOut);
			manifestOut.flush();
			manifestOut.close();
			flag = true;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
		return flag;
	}
	
	
	public String getUniqueID(){
		String key = "Edu-";
		key += String.valueOf(UUID.randomUUID());
		return key;
	}
	
	/**
	 * 엑셀파일 내용 반환
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public List getExcelDataList(String savePath) throws Exception{
		ArrayList list = new ArrayList();
		
		
		String fileName = "";
		
		
		if(savePath.endsWith(".xls"))
		{
			System.out.println("##########  xls 확장자 ##############");
			fileName = savePath;
		}
		else
		{
			String content[] = (new File(savePath)).list();
			
			for( int i=0; i<content.length; i++ ){
				if( content[i].endsWith(".xls") ){
					fileName = savePath+"/"+content[i];
				}
			}
		}
		
		
		log.info("# Xls read fileName : " + fileName);
		
		if( fileName.equals("") ){
			return null;
		}
		
		Workbook workbook = Workbook.getWorkbook(new File(fileName));
		Sheet sheet  = workbook.getSheet(0);
		
		int row = sheet.getRows();
		Map inputMap;
		String oldKey = "";
		int merge = 0;
		for( int i=0; i<row; i++ ){
			inputMap = new HashMap<String, Object>();
			Cell cell[] = sheet.getRow(i);
			for( int j=0; j<cell.length; j++ ){
				inputMap.put("parameter"+j, cell[j].getContents());
				
				log.info(i + "-" + j +")" + cell[j].getContents());
			}
			if( !oldKey.equals(cell[0].getContents()) ){
				merge = 0;
				oldKey = cell[0].getContents();
				for( int idx=0; idx<row; idx++ ){
					Cell cell2[] = sheet.getRow(idx);
					if( cell2[0].getContents().equals(oldKey) ){
						merge++;
					}
				}
			}
			inputMap.put("merge", merge);
			list.add(inputMap);
		}
		
		return list;
	}
	
	/**
	 * 중공교 컨텐츠 구조에 맞게 docs생성
	 * @param path
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String checkCotiFile(String path, String fileName) throws Exception{
		try{
    		ZipFile zipFile = new ZipFile(path+"/"+fileName);
    		boolean check = false;
    		for(Enumeration zipEntries = zipFile.entries(); zipEntries.hasMoreElements();){
    			ZipEntry entry = (ZipEntry)zipEntries.nextElement();
    			if( "docs".equals(entry.getName()) ){
    				check = true;
    			}
    		}
    		
    		if( !check ){
    			path = path+"/docs";
        		new File(path).mkdirs();
    		}
    		
    		
    		zipFile.close();
        }catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
		
		return path;
	}
	
	public String copyContentFile(String crsCode, String scoFolder, String baseDir, String strSavePath, String imspath, String inputPath) throws Exception{
		String path = Globals.CONTNET_REAL_PATH;
		try{
    		File targetfile = new File(inputPath+"/"+scoFolder);
    		targetfile.mkdirs();
			File copyfile = new File(path+baseDir);
			
			
    		if(copyfile.isDirectory()){
            	if(!targetfile.exists()){
            		targetfile.mkdirs();
            	}
            	String[] children = copyfile.list();
            	for(int i = 0; i < children.length; i++){
            		moveCopy(new File(copyfile, children[i]),new File(targetfile, children[i]));
            	}
            }else{
            	copyfile.renameTo(targetfile);
            }
    		
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return inputPath;
	}
	
	 /**
     * transferTo() 사용한 파일 복사후 원래파일삭제[이게 젤루 빠름]
     * @param oldfile 원래파일명[폴더포함]
     * @param targetfile 새파일명[폴더포함]
     * @throws Exception
     */
    public static void moveCopy(File oldfile, File targetfile) throws Exception{

        if(oldfile.isDirectory()){
        	if(!targetfile.exists()){
        		targetfile.mkdirs();
        	}
        	String[] children = oldfile.list();
        	for(int i = 0; i < children.length; i++){
        		moveCopy(new File(oldfile, children[i]),new File(targetfile, children[i]));
        	}
        }else{
        	FileInputStream f_in=new FileInputStream(oldfile);
            FileOutputStream f_out=new FileOutputStream(targetfile);
            try{
	            byte[] buffer=new byte[1024];
	            int len=f_in.read(buffer);
	            while(len>0){
	            	f_out.write(buffer, 0, len);
	            	len=f_in.read(buffer);
	            }
	            f_out.flush();
            }finally{
	            f_out.close();
	            f_in.close();
            }
        }
    }
    
    public static void uploadFile(HttpServletRequest request, Map<String, Object> commandMap) throws Exception{
    	MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		String dir = (String)commandMap.get("p_upload_dir");
		if( commandMap.get("p_upload_dir") == null || ((String)commandMap.get("p_upload_dir")).equals("") ){
			dir = "bulletin";
		}
		String strSavePath = EgovProperties.getProperty("Globals.defaultDP")+dir+"/";
		if( commandMap.get("p_filedel") != null ){
			String fileName = (String)commandMap.get("p_savefile");
			new File(strSavePath+fileName).delete();
		}
		
		Map result = new HashMap();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				commandMap.putAll(EgovFileMngUtil.uploadContentFile(mFile, strSavePath));
			}
		}
    }
    
    

    public static void uploadMultiFile(HttpServletRequest request, Map<String, Object> commandMap) throws Exception{
    	MultipartHttpServletRequest mptRequest = (MultipartHttpServletRequest)request;
		Iterator fileIter = mptRequest.getFileNames();
		String dir = "bulletin";
		if( commandMap.get("p_upload_dir") != null || !((String)commandMap.get("p_upload_dir")).equals("") ){
			dir = (String)commandMap.get("p_upload_dir");
		}
		String strSavePath = EgovProperties.getProperty("Globals.defaultDP")+dir+"/";
		if( commandMap.get("p_filedel") != null ){
			String[] idx = EgovStringUtil.getStringSequence(commandMap, "p_filedel");
			
			for(int i=0; i<idx.length; i++ ){
				
				String fileName = (String)commandMap.get("p_savefile"+idx[i]);
				new File(strSavePath+fileName).delete();
			}
		}
		
		ArrayList result = new ArrayList();
		while (fileIter.hasNext()) {
			MultipartFile mFile = mptRequest.getFile((String)fileIter.next());
		 
			if (mFile.getSize() > 0) {
				result.add(EgovFileMngUtil.uploadContentFile(mFile, strSavePath));
			}
		}
		commandMap.put("multiFiles", result);
    }
    
    public static int deleteFiles(Map<String, Object> commandMap) throws Exception{
    	String dir = "bulletin";
    	int isOk = 1;
    	try{
    		if( commandMap.get("p_upload_dir") != null || !((String)commandMap.get("p_upload_dir")).equals("") ){
    			dir = (String)commandMap.get("p_upload_dir");
    		}
    		String strSavePath = EgovProperties.getProperty("Globals.defaultDP")+dir+"/";
    		if( commandMap.get("p_savefile") != null ){
    			String[] fileName = EgovStringUtil.getStringSequence(commandMap, "p_savefile");
    			
    			for(int i=0; i<fileName.length; i++ ){
    				new File(strSavePath+fileName[i]).delete();
    			}
    		}
    	}catch(Exception ex){
    		isOk = 0;
    	}
    	return isOk;
    }
}
