package egovframework.com.tag;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import egovframework.rte.fdl.property.EgovPropertyService;
import enc.C;

/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : ReportTag.java
 * description : 
 * </pre>
 * 
 * @version
 * 
 * <pre>
 *  
 * 1.0	2010-10-21 
 * 1.1	
 * </pre>
 */

public class ReportTag extends TagSupport {
	
	/** EgovPropertyService */
/*    @Resource(name = "propertiesService")
    private static EgovPropertyService propertiesService;
*/    
    //실서버 
	private static String REPORT_SERVER_IP = "203.236.236.24";
	private static String REPORT_SERVER_URL = "http://" + REPORT_SERVER_IP + "/rdServer/";
	private static String REPORT_SERVER_CAB = "http://" + REPORT_SERVER_IP + "/rdServer/";
	private static String REPORT_SERVER_SERVICE_NAME = "newknise";
	private static String REPORT_SERVER_SITE_NAME= "국립특수교육원 원격연수 시스템";	
	private static String REPORT_VIEWER_OBJECT_ID= "Rdviewer";
	private static String REPORT_VIEWER_CLSID= "clsid:ADB6D20D-80A1-4aa4-88AE-B2DC820DA076";
	private static String REPORT_VIEWER_VERSION= "rdviewer50.cab#version=5,0,0,258";
	private static String REPORT_VIEWER_WIDTH= "100%";
	private static String REPORT_VIEWER_HEIGHT= "100%";
	
	
	//private static String REPORT_MADOWNLOAD_SERVER_URL= "http://125.60.59.34/ngsrd/rdServer/markany/";
	private static String REPORT_MADOWNLOAD_SERVER_CAB= "http://" + REPORT_SERVER_IP + "/rdServer/";
	private static String REPORT_MADOWNLOAD_OBJECT_ID = "MaDownloadRD";
	private static String REPORT_MADOWNLOAD_CLSID     = "clsid:3B780B78-73B9-49B8-9630-3E60EDE61C73";
	private static String REPORT_MADOWNLOAD_VERSION   = "MaDownloadRD.cab#version=2,5,1,10";
	private static String REPORT_MADOWNLOAD_WIDTH     = "";
	private static String REPORT_MADOWNLOAD_HEIGHT    = "";
	
   
	
	/**
	 * 
	 * 
	private static String REPORT_SERVER_SERVICE_NAME = propertiesService.getString("report.server.serviceName");
	private static String REPORT_SERVER_SITE_NAME    = propertiesService.getString("report.server.siteName");
	
	private static String REPORT_VIEWER_OBJECT_ID = propertiesService.getString("report.viewer.object.id");
	private static String REPORT_VIEWER_CLSID     = propertiesService.getString("report.viewer.clsid");
	private static String REPORT_VIEWER_VERSION   = propertiesService.getString("report.viewer.version");
	private static String REPORT_VIEWER_WIDTH     = propertiesService.getString("report.viewer.width");
	private static String REPORT_VIEWER_HEIGHT    = propertiesService.getString("report.viewer.height");
    **/
	private static String HIDE_CONTROL_TAG = "<TEXTAREA ID=\"{0}_\" NAME=\"{0}_\" STYLE=\"display:none;\" ROWS=\"0\" COLS=\"0\">\n{1}</TEXTAREA>\n"
											+ "<SCRIPT language=\"javascript\">\n"
											+ "document.write( $(\"#{0}_\").val());\n"
											+ "$(\"#{0}_\").val(\"\");"
											+ "</SCRIPT>\n";
	private static String REPORT_OBJECT_TAG = "<OBJECT ID=\"{0}\" NAME=\"{0}\" CLASSID=\"{1}\" CODEBASE=\"{2}\" WIDTH=\"{3}\" HEIGHT=\"{4}\" >\n</OBJECT>\n";
	
	private static String REPORT_MADOWNLODERD_OBJECT_TAG = "<OBJECT ID=\"{0}\" NAME=\"{0}\" CLASSID=\"{1}\" CODEBASE=\"{2}\" WIDTH=\"{3}\" HEIGHT=\"{4}\" >\n</OBJECT>\n";

	/**
	 * 
	 */
	private static final long serialVersionUID = -1146134413067129167L;

	private String title;
	private String file;
	private String rv;
	private String rp;
	private String rprnn;
	private String toolbar;
	private String printoff;
	private String mark;
	private String rpageorder;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getRv() {
		return rv;
	}
	public String getRpageorder(){
		return rpageorder;
	}
	public void setRpageorder(String rpageorder){
		this.rpageorder = rpageorder;
	}
	public void setRv(String rv) {
		this.rv = rv;
	}

	public String getRp() {
		return rp;
	}

	public void setRp(String rp) {
		this.rp = rp;
	}
	

	public String getRprnn() {
		return rprnn;
	}

	public void setRprnn(String rprnn) {
		this.rprnn = rprnn;
	}

	public String getPrintoff() {
		return printoff;
	}

	public void setPrintoff(String printoff) {
		this.printoff = printoff;
	}
	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public int doStartTag() throws JspException {
		if (file == null)
			file = "";
		if (title == null)
			title = "";
		if (rv == null)
			rv = "";
		if (rp == null)
			rp = "";
		if (rprnn == null)
			rprnn = "";
		if (toolbar == null)
			toolbar = "Y";
		if (printoff == null)
			printoff = "N";
		if (mark == null)
			mark = "N";
		if (rpageorder == null)
			rpageorder = "";
        return SKIP_BODY;
    }
	
    public int doEndTag() throws JspException {
        try {
        	JspWriter out = pageContext.getOut();

        	HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        	
    		String html = "";

    		html += reportControl();
    		html += reportParameter( request);

            out.print( html);
            //System.out.println( html);
        }
        catch (Exception e) {
            throw new JspTagException("I/O Exception : " + e.getMessage());
        }
        
        return EVAL_PAGE;
    }
    
	private String reportControl()
	{
		String reportControl = "";
		
		if(mark.equals("Y")){
			reportControl = reportControl + MessageFormat.format( REPORT_MADOWNLODERD_OBJECT_TAG, new Object[] { REPORT_MADOWNLOAD_OBJECT_ID, REPORT_MADOWNLOAD_CLSID, REPORT_MADOWNLOAD_SERVER_CAB+REPORT_MADOWNLOAD_VERSION, REPORT_MADOWNLOAD_WIDTH, REPORT_MADOWNLOAD_HEIGHT});
		}
		
		reportControl = reportControl + MessageFormat.format( REPORT_OBJECT_TAG, new Object[] { REPORT_VIEWER_OBJECT_ID, REPORT_VIEWER_CLSID, REPORT_SERVER_CAB+REPORT_VIEWER_VERSION, REPORT_VIEWER_WIDTH, REPORT_VIEWER_HEIGHT});
		
		return MessageFormat.format( HIDE_CONTROL_TAG, new Object[]{ "reportControl", reportControl});
	}
    
    private String reportParameter( HttpServletRequest request) throws Exception
    {
    	
    	String path = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath() ; 
        
    	
    	//mrd 파일 패스
        String fileURL = "http://"+REPORT_SERVER_IP+"/mrd/" + getFile();
    	
        if("127.0.0.1".equals(request.getRemoteAddr())){
        	fileURL = path + "/mrd/" + getFile();
        }
        
        //System.out.println("MRD PATH IS : " + path);
        //System.out.println("MRD fileURL IS : " + fileURL);
        
        String parameter = "/rfn ["+REPORT_SERVER_URL+"rdagent.jsp] /rsn ["+REPORT_SERVER_SERVICE_NAME+"] /rpxpos [25] " + " /rmessageboxshow [2] ";
        parameter += "/rv site[" + REPORT_SERVER_SITE_NAME + "] title["+title+"] " + getRv();       
        parameter += "/rp " + getRp();
        
        if(mark.equals("Y")){
        	parameter += " /rmultipage /rwatchprn ";
        }
        if(!getRprnn().equals("")){
        	parameter += "/rprnn " + getRprnn();
        }
        if(!getRpageorder().equals("")){
        	parameter += "/rpageorder " + getRpageorder();
        }
        //System.out.println("MRD parameter IS : " + parameter);
        
		//System.out.println("TEST1234 :    "+REPORT_VIEWER_OBJECT_ID+".MAFileOpen('"+fileURL+"','"+parameter+"','"+REPORT_SERVER_URL+"rdagent.jsp');");
        
		String encryptFileURL = "";
		String encryptParameter = "";
		if(!mark.equals("Y")){
			 encryptFileURL = new String( C.process( fileURL, 1));
			 encryptParameter = new String( C.process( parameter, 1));
		}else{
			 encryptFileURL = fileURL;
		     encryptParameter = parameter;
		}
        //String encryptFileURL = fileURL;
    	//String encryptParameter = parameter;
    
    	StringBuffer sb = new StringBuffer();
    	
    	sb.append("<script language=\"javascript\">").append("\n");
    	/*
    	sb.append("    $('#"+REPORT_VIEWER_OBJECT_ID+"').SetBackgroundColor(255,255,255);").append("\n");
    	sb.append("    $('#"+REPORT_VIEWER_OBJECT_ID+"').AutoAdjust=0;").append("\n");
    	sb.append("    $('#"+REPORT_VIEWER_OBJECT_ID+"').SetParameterEncrypt(1);").append("\n");
    	sb.append("    $('#"+REPORT_VIEWER_OBJECT_ID+"').SetKindOfParam(1);").append("\n");
    	sb.append("    $('#"+REPORT_VIEWER_OBJECT_ID+"').SetReportDialogInfo(1,'','',1,'','');").append("\n");
    	*/
    	sb.append("    "+REPORT_VIEWER_OBJECT_ID+".SetBackgroundColor(255,255,255);").append("\n");
    	sb.append("    "+REPORT_VIEWER_OBJECT_ID+".AutoAdjust=0;").append("\n");
    	
    	if(!mark.equals("Y")){
    	  sb.append("    "+REPORT_VIEWER_OBJECT_ID+".SetParameterEncrypt(1);").append("\n");
    	  sb.append("    "+REPORT_VIEWER_OBJECT_ID+".SetKindOfParam(1);").append("\n");
    	}
    	sb.append("    "+REPORT_VIEWER_OBJECT_ID+".SetReportDialogInfo(1,'','',1,'','');").append("\n");
    	if(toolbar.equals("N")){
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (0);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (2);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (3);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (4);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (5);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (6);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (7);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (8);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (9);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (10);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (11);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (12);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (13);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (14);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (15);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (16);").append("\n");
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".DisableToolbar (17);").append("\n");
    		
    	 	if(printoff.equals("Y")){
    	 		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".PrintFinished();").append("\n");
        	}
    	}
    	if(mark.equals("Y")){
    		/* 마 크*/
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".MAFileOpen2('"+encryptFileURL+"','"+encryptParameter+"','"+REPORT_SERVER_URL+"rdagent.jsp');").append("\n");
    	}else{
    		sb.append("    "+REPORT_VIEWER_OBJECT_ID+".FileOpen('"+encryptFileURL+"','"+encryptParameter+"');").append("\n");
    	}
    	sb.append("</script>").append("\n");
    	
    	/*
    	if(printoff.equals("Y")){
    		sb.append("<SCRIPT LANGUAGE=JavaScript FOR=Rdviewer event=\"PrintFinished()\"> ").append("\n");
    		sb.append("</script>").append("\n");
    	}
    	*/
    	return sb.toString();
    }

    public void release() {
    	title = null;
		file = null;
		rv = null;
		rp = null;    	
		rprnn = null;
		mark  = null;
		rpageorder = null;
        super.release();
    }

	public String getToolbar() {
		return toolbar;
	}

	public void setToolbar(String toolbar) {
		this.toolbar = toolbar;
	}
	
/*	public EgovPropertyService getPropertyService() {
		if( propertiesService == null){
			propertiesService = (EgovPropertyService)this.getWebApplicationBean("propertiesService");
		}		
		return propertiesService;
	}
*/	
	public  WebApplicationContext getContext()
	{
	return ContextLoader.getCurrentWebApplicationContext();
	}
    public  Object getWebApplicationBean(String beanName)
    {
      return getContext().getBean(beanName);
    }
}
