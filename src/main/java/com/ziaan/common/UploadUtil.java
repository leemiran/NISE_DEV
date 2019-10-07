 /**
 * UploadUtil.java
 * <p>제목:업로드 폴더 생성을 위한 세션셋팅</p>
 * <p>설명:업로드 폴더 생성을 위한 세션셋팅 제어 프로그램</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: credu </p>
 * @author 박성철 2007. 12. 04
 * @version 1.0
 */
 
package com.ziaan.common;

import java.util.Random;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import com.ziaan.library.*;

public class UploadUtil {

   /************************************************************************************
   * v_uptype : P - 과제,팀프로젝트  (/uploadkey/yearsubjseq/grcode/subj/class/seq)
   *            L - 강의실 기타      (/uploadkey/yearsubjseq/grcode/subj/class)
   *            N - 강의실 외        (/uploadkey/grcode/)
   *            C - 커뮤니티         (/uploadkey/)
   *            S - 기타             (/uploadkey/) + yearsubjseq/grcode/subj/class/ + box.getString("p_strfilepath");
   *            T - 기타             (/uploadkey/) + box.getString("p_strfilepath");
   ************************************************************************************/

    /*
    * 용량제한 없는 세션세팅
    @param v_uptype 업로드형식
    */
    public static void performSetSession(String v_uptype, RequestBox box, PrintWriter out) throws Exception {
        performSetSession(v_uptype, "0", box, out);
    }

    /*
    * 용량제한 세션세팅
    @param v_uptype 업로드형식
    @param v_maxpostsize 제한용량
    */
    public static void performSetSession(String v_uptype, String v_maxpostsize, RequestBox box, PrintWriter out) throws Exception {
        try{
            box.setSession("maxpostsize", v_maxpostsize);

            String v_uploadPath = performMakeFilepath(v_uptype, box);
            box.setSession("uploadPath",v_uploadPath);

        }catch (Exception ex){
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, out);
            throw new Exception("performSetSession()\r\n" + ex.getMessage());
        }
    }

    /*
    * filepathe를 만들어 준다
    @param v_uptype 업로드형식
    @param v_dirUpload 프로퍼티에 등록된 업로드 경로
    */
    public static String performMakeFilepath(String v_uptype, RequestBox box) throws Exception {
        String v_filepath = "";

        try{
            if( v_uptype.equals("L") || v_uptype.equals("P") || v_uptype.equals("S") ) {
//                String v_grcode  = box.getString("p_grcode");
//                String v_subj    = box.getString("p_subj");
//                String v_year    = box.getString("p_year");
//                String v_subjseq = box.getString("p_subjseq");
//                String v_class   = box.getString("p_class");
                
                String v_searchyearsubj = box.getString("searchSubj");
    			if(!"".equals(v_searchyearsubj)){
    				box.setSession("psubj",    v_searchyearsubj.split("[$]")[0]);
    				box.setSession("pgrcode",  v_searchyearsubj.split("[$]")[1]);
    				box.setSession("pyear",    v_searchyearsubj.split("[$]")[4]);
    				box.setSession("psubjseq", v_searchyearsubj.split("[$]")[2]);
    				box.setSession("pclass",   v_searchyearsubj.split("[$]")[3]);
    			}	

                //v_filepath = "/" + v_grcode + "/" + v_subj + "/" + v_year + "/" + v_subjseq + "/" + v_class;
    			if(box.getSession("pgrcode").equals(""))  box.setSession("pgrcode",  box.getSession("grcode"));
                v_filepath = "/" + box.getSession("pyear") + "/" + box.getSession("psubjseq") + "/" + box.getSession("pgrcode") + "/" + box.getSession("psubj") + "/" + box.getSession("pclass");

                if( v_uptype.equals("P") ) {
                    String v_ordseq  = box.getString("p_ordseq");
                    v_filepath = v_filepath + "/" + v_ordseq;
                }

                if( v_uptype.equals("S") ) {
                    String v_strfilepath  = box.getString("p_strfilepath");
                    v_filepath = v_filepath + "/" + v_strfilepath;
                }

            }else if( v_uptype.equals("N") ) {
                String v_grcode  = box.getString("p_grcode");
                v_filepath = "/" + v_grcode;
            }else if( v_uptype.equals("E") ) {  //온라인 시험
                String v_grcode  = box.getString("p_grcode");
                String v_subj    = box.getString("p_subj");
                String v_year    = box.getString("p_year");
                String v_subjseq = box.getString("p_subjseq");
                v_filepath = "/" + v_year+v_subjseq + "/" + v_grcode + "/" + v_subj;
            }else if ( v_uptype.equals("T") ) {
                String v_strfilepath  = box.getString("p_strfilepath");
                v_filepath = "/" + v_strfilepath;
            }else if( v_uptype.equals("C") ) {
                String v_cmuno  = box.getString("p_cmuno");
                v_filepath = "/" + v_cmuno;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, ex.getMessage());
            throw new Exception("performMakeFilepath()\r\n" + ex.getMessage());
        }
        return v_filepath;
    }


    /*
    * Flash Uploader를 생성한다.
    @param uptype       업로드 형식
    @param width        가로 사이즈
    @param height       세로 사이즈
    @param filecnt      첨부파일 수
    @param maxzsize     용량제한 사이즈 (단위 MB, ""->프로퍼티 등록값)
    @param fileext      첨부파일 확장자 ""->모든확장자, (;;;;;)
    @param btnupload    업로드 버튼 표시 여부 "true", "false"
    @param filelist     등록된파일
    */
    public static String performMakeFlashUploader(String uptype,
    											  String width,
												  String height,
												  int filecnt,
												  String maxsize,
												  String fileext,
												  String btnupload,
												  RequestBox box,
												  String filelist) throws Exception {
        String result = "";

        try{
            ConfigSet config = new ConfigSet();

            /*if (filecnt<=0) {
                return result;
            }*/

            String v_servletName = box.getString("p_grservlet");
            String s_userid      = box.getSession("userid");

            String v_filepath    = performMakeFilepath(uptype,box);
            String v_dirKey      = config.getDir(config.getProperty("dir.upload"), v_servletName);

			//System.out.println("**************v_filepath : "+v_filepath);
			//System.out.println("************** v_dirKey: "+v_dirKey);

            String v_max         = "";

            if ( maxsize.equals("") ) {
                v_max = config.getProperty("maxpostsize."+ v_dirKey);
            }else{
                v_max = maxsize;
            }

            String v_files       = "";
            char a = (char)2;
            char b = (char)3;

            if (fileext.equals("")) {fileext="*.*"; }

            result +="\r\n <div id=\"upfileDiv\"></div>";
            result +="\r\n <script type=\"text/javascript\" src=\"/script/KOREAN/flashFileUpload.js\"></script>";
            result +="\r\n <script type=\"text/javascript\" src=\"/script/KOREAN/FlashObject.js\"></script>";
            result +="\r\n <script type=\"text/javascript\">";
            result +="\r\n   var flashObj = new FlashObject ('/images/flash/fupload.swf', 'p_files_flash', '"+width+"', '"+height+"', 8, '#FFFFFF', true) ";
            result +="\r\n   flashObj.addVariable (\"p_uploadURL\"   , \"/learn/user/report/upload.jsp\");";
            result +="\r\n   flashObj.addVariable (\"p_name\"     	 , \"p_files\");";
            result +="\r\n   flashObj.addVariable (\"p_fileCnt\"     , \""+filecnt+"\");";
            result +="\r\n   flashObj.addVariable (\"p_maxSize\"     , \""+v_max+"\");";
            result +="\r\n   flashObj.addVariable (\"p_filehint\"    , \""+v_servletName+"\");";
            result +="\r\n   flashObj.addVariable (\"p_filelasthint\", \""+s_userid+"\");";
            result +="\r\n   flashObj.addVariable (\"p_uploadPath\"  , \""+v_filepath+"\");";
            result +="\r\n   flashObj.addVariable (\"p_fileext\"     , \""+fileext+"\");";
            result +="\r\n   flashObj.addVariable (\"p_btnUpload\"   , \""+btnupload+"\");";
            result +="\r\n   flashObj.addVariable (\"p_files\"       , \""+filelist+"\");";
            result +="\r\n   flashObj.write ('upfileDiv'); ";
            result +="\r\n </script>";
            result +="\r\n <input type=\"hidden\" name=\"p_files\" id=\"p_files\" value=\"\" /> ";
            result +="\r\n <input type=\"hidden\" name=\"p_files_exist\" id=\"p_files_exist\" value=\"\" /> ";
            result +="\r\n <div id=\"delfileDiv\" style='display:none;'></div>";

        }catch (Exception ex){
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, ex.getMessage());
            throw new Exception("performMakeFlashUploader()\r\n" + ex.getMessage());
        }
        return result;
    }



    /*
    * Flash Uploader를 생성한다.
    @param uptype       업로드 형식
    @param width        가로 사이즈
    @param height       세로 사이즈
    @param maxzsize     용량제한 사이즈 (단위 MB, ""->프로퍼티 등록값)
    @param fileext      첨부파일 확장자 ""->모든확장자, (;;;;;)
    @param btnupload    업로드 버튼 표시 여부 "true", "false"
    @param filelist     등록된파일
    */
    public static String performMakeFlashUploaderSingle(String uptype,
    											  String width,
												  String height,
												  String maxsize,
												  String fileext,
												  String btnupload,
												  RequestBox box,
												  String filelist) throws Exception {
        String result = "";

        try{
            ConfigSet config = new ConfigSet();

			//가변적 폼 네임
            String v_inputname	 = box.getString("p_inputname");
            if (v_inputname.equals("")){
            	v_inputname = "p_files";
            }

            String v_servletName = box.getString("p_grservlet");
            String s_userid      = box.getSession("userid");
            
            System.out.println("v_servletName================>"+v_servletName);
            
            String v_filepath    = performMakeFilepath(uptype,box);
            String v_dirKey      = config.getDir(config.getProperty("dir.upload"), v_servletName);
            String v_max         = "";

            //System.out.println("uploadPath=" + config.getProperty("dir.upload." + v_dirKey));
            //System.out.println("v_servletName=" + v_servletName);
			//System.out.println("v_filepath=" + v_filepath);
			//System.out.println("v_dirKey="   + v_dirKey);

            if ( maxsize.equals("") ) {
                v_max = config.getProperty("maxpostsize."+ v_dirKey);
            }else{
                v_max = maxsize;
            }

            String v_files       = "";
            char a = (char)2;
            char b = (char)3;

            if (fileext.equals("")) {fileext="*.*"; }

            result +="\r\n <div id=\"upfileDiv"+ v_inputname +"\"></div>";
            result +="\r\n <script type=\"text/javascript\" src=\"/script/KOREAN/flashFileUpload.js\"></script>";
            result +="\r\n <script type=\"text/javascript\" src=\"/script/KOREAN/FlashObject.js\"></script>";
            result +="\r\n <script type=\"text/javascript\">";
            result +="\r\n   var flashObj = new FlashObject ('/images/flash/fuploadSingle.swf', '"+ v_inputname +"_flash', '"+width+"', '"+height+"', 8, '#FFFFFF', true) ";
            result +="\r\n   flashObj.addVariable (\"p_uploadURL\"   , \"/learn/user/report/upload.jsp\");";
            result +="\r\n   flashObj.addVariable (\"p_fileCnt\"     , \"1\");";
            result +="\r\n   flashObj.addVariable (\"p_name\"     	 , \""+v_inputname+"\");";
            result +="\r\n   flashObj.addVariable (\"p_maxSize\"     , \""+v_max+"\");";
            result +="\r\n   flashObj.addVariable (\"p_filehint\"    , \""+v_servletName+"\");";
            result +="\r\n   flashObj.addVariable (\"p_filelasthint\", \""+s_userid+"\");";
            result +="\r\n   flashObj.addVariable (\"p_uploadPath\"  , \""+v_filepath+"\");";
            result +="\r\n   flashObj.addVariable (\"p_fileext\"     , \""+fileext+"\");";
            result +="\r\n   flashObj.addVariable (\"p_btnUpload\"   , \""+btnupload+"\");";
            result +="\r\n   flashObj.addVariable (\"p_files\"       , \""+filelist+"\");";
            result +="\r\n   flashObj.write ('upfileDiv"+ v_inputname +"'); ";
            result +="\r\n </script>";
            result +="\r\n <input type=\"hidden\" name='"+ v_inputname +"' id='"+ v_inputname +"' value=\"\" /> ";
            result +="\r\n <input type=\"hidden\" name='"+ v_inputname +"_exist' id='"+ v_inputname +"_exist' value=\"\" /> ";
            result +="\r\n <div id=\"delfileDiv"+ v_inputname +"\" style='display:none;'></div>";

        }catch (Exception ex){
            ex.printStackTrace();
            ErrorManager.getErrorStackTrace(ex, box, ex.getMessage());
            throw new Exception("performMakeFlashUploader()\r\n" + ex.getMessage());
        }
        return result;
    }

    /* 첨부파일 5개 고정, 사이즈 고정, 모든 확장자로 고정, 용량제한 프로퍼티값, 업로드 버튼 없음 */
    public static String performMakeFlashUploader5(String uptype ,RequestBox box) throws Exception {
        return performMakeFlashUploader( uptype, 5, "", "", "false", box);

    }
    /* 첨부파일 1개 고정, 사이즈 고정, 모든 확장자로 고정, 용량제한 프로퍼티값, 업로드 버튼 없음 */
    public static String performMakeFlashUploader1(String uptype, RequestBox box) throws Exception {
        return performMakeFlashUploader1( uptype, box, "", "");
    }

    /* 첨부파일 1개 고정, 사이즈 고정, 모든 확장자로 고정, 용량제한 프로퍼티값, 업로드 버튼 없음 */
    public static String performMakeFlashUploader1(String uptype, RequestBox box, String filelist) throws Exception {
        return performMakeFlashUploader1( uptype, box, filelist, "");
    }


    /* 첨부파일 1개 고정, 사이즈 고정, 모든 확장자로 고정, 용량제한 프로퍼티값, 업로드 버튼 없음 */
    public static String performMakeFlashUploader1(String uptype, RequestBox box, String filelist, String fileext) throws Exception {
        ConfigSet config = new ConfigSet();
        DecimalFormat df = new java.text.DecimalFormat("#########.####");

        String v_servletName = box.getString("p_grservlet");
        String v_dirKey      = config.getDir(config.getProperty("dir.upload"), v_servletName);
        String v_max         = config.getProperty("maxpostsize."+ v_dirKey);
        String maxsize       = df.format((Double.parseDouble(v_max)*1024*1024)/1024/1024);

        return performMakeFlashUploaderSingle( uptype, "100%",  "50!", maxsize, fileext, "false", box, filelist);
    }


    /* 사이즈 고정, 모든 확장자로 고정, 용량제한 프로퍼티값 - 첨부한 용량, 업로드 버튼 없음 */
    public static String performMakeFlashUploader(String uptype, int filecnt, int uploadsize, RequestBox box) throws Exception {
        ConfigSet config = new ConfigSet();
        DecimalFormat df = new java.text.DecimalFormat("#########.####");

        String v_servletName = box.getString("p_grservlet");
        String v_dirKey      = config.getDir(config.getProperty("dir.upload"), v_servletName);
        String v_max         = config.getProperty("maxpostsize."+ v_dirKey);
        String maxsize       = df.format((Double.parseDouble(v_max)*1024*1024 - uploadsize)/1024/1024);

        return performMakeFlashUploader( uptype, filecnt, maxsize, "", "false", box);
    }

    /* 사이즈 고정, 모든 확장자로 고정, 용량제한 프로퍼티값 - 첨부한 용량, 업로드 버튼 없음 */
    public static String performMakeFlashUploader(String uptype, int filecnt, int uploadsize, RequestBox box, String filelist) throws Exception {
        ConfigSet config = new ConfigSet();
        DecimalFormat df = new java.text.DecimalFormat("#########.####");

        String v_servletName = box.getString("p_grservlet");
        String v_dirKey      = config.getDir(config.getProperty("dir.upload"), v_servletName);
        String v_max         = config.getProperty("maxpostsize."+ v_dirKey);
        String maxsize       = df.format((Double.parseDouble(v_max)*1024*1024 - uploadsize)/1024/1024);

        return performMakeFlashUploader( uptype, filecnt, maxsize, "", "false", box, filelist);
    }

    /* 사이즈 고정, 모든 확장자로 고정, 업로드 버튼 없음 */
    public static String performMakeFlashUploader(String uptype, int filecnt, String maxsize, RequestBox box) throws Exception {
        return performMakeFlashUploader( uptype, filecnt, maxsize, "", "false", box);
    }
    /* 첨부파일 5개 고정, 사이즈 고정, 업로드 버튼 없음 */
    public static String performMakeFlashUploader(String uptype, String maxsize, String fileext, RequestBox box) throws Exception {
//        System.out.println("test : "+ fileext);
        return performMakeFlashUploader( uptype, 5, maxsize, fileext, "false", box);
    }
    /* 사이즈 고정, 업로드 버튼 없음 */
    public static String performMakeFlashUploader(String uptype, int filecnt, String maxsize, String fileext, RequestBox box) throws Exception {
        return performMakeFlashUploader( uptype, filecnt, maxsize, fileext, "false", box);
    }
    /* 사이즈 고정*/
    public static String performMakeFlashUploader(String uptype, int filecnt, String maxsize, String fileext, String btnupload, RequestBox box) throws Exception {
        return performMakeFlashUploader( uptype, "100%",  "120", filecnt, maxsize, fileext, btnupload, box, "");
    }
    /* 사이즈 고정*/
    public static String performMakeFlashUploader(String uptype, int filecnt, String maxsize, String fileext, String btnupload, RequestBox box, String filelist) throws Exception {
        return performMakeFlashUploader( uptype, "100%",  "120", filecnt, maxsize, fileext, btnupload, box, filelist);
    }


}

