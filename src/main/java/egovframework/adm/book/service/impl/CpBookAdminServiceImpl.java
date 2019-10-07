package egovframework.adm.book.service.impl;

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
import egovframework.adm.book.service.CpBookAdminService;
import egovframework.adm.cfg.amm.dao.AdminMenuManageDAO;
import egovframework.adm.cfg.amm.service.AdminMenuManageService;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("cpBookAdminService")
public class CpBookAdminServiceImpl extends EgovAbstractServiceImpl implements CpBookAdminService{

	@Resource(name="cpBookAdminDAO")
    private CpBookAdminDAO cpBookAdminDAO;

	
	public List selectCPList(Map<String, Object> commandMap) throws Exception {
		return cpBookAdminDAO.selectCPList(commandMap);
	}

	
	public List selectList(Map<String, Object> commandMap) throws Exception {
		return cpBookAdminDAO.selectList(commandMap);
	}

	
	public List selectSubj(Map<String, Object> commandMap) throws Exception {
		return cpBookAdminDAO.selectSubj(commandMap);
	}


	public boolean updateCpbookStatus(Map<String, Object> commandMap)
			throws Exception {
		
		boolean isok = false;
		
		try{
			
			String []  v_checkvalue = (String []) commandMap.get("_Array_p_checks");
			String  v_luserid = (String) commandMap.get("userid");
			
			if(v_checkvalue != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					
					StringTokenizer st  = new StringTokenizer( v_checkvalue[i], ",");
					String v_userid		= st.nextToken();
					String v_subj		= st.nextToken();
					String v_year		= st.nextToken();
					String v_subjseq	= st.nextToken();
					
					System.out.println("@@@@@@ v_checkvalue = "+v_checkvalue[i]);
	                
	                HashMap<String, Object> mm = new HashMap<String, Object>();
                
                	mm.put("v_luserid", v_luserid);
                	mm.put("v_userid", v_userid);
                	mm.put("v_subj", v_subj);
                	mm.put("v_year", v_year);
                	mm.put("v_subjseq", v_subjseq);
                	mm.put("v_status", commandMap.get("p_status"));
                	
                	System.out.println("@@@@@@ v_luserid  = "+v_luserid);
                	System.out.println("@@@@@@ v_userid   = "+v_userid);
                	System.out.println("@@@@@@ v_subj     = "+v_subj);
                	System.out.println("@@@@@@ v_year     = "+v_year);
                	System.out.println("@@@@@@ v_subjseq  = "+v_subjseq);
                	System.out.println("@@@@@@ v_status   = "+mm.get("v_status")+"");
                	
                	//배송상태 변경 
                	int cnt = cpBookAdminDAO.updateCpbookStatus(mm);
                	
                	if(cnt == 0) 
                	{
                		return false;
                	}
	                
				}
				isok = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return isok;
	}


	/**
	 * 과정정보
	 */
	public Map selectSubjInfo(Map<String, Object> commandMap) throws Exception {
		return cpBookAdminDAO.selectSubjInfo(commandMap);
	}


	public List selectDeliveryCompExcelList(Map<String, Object> commandMap)
			throws Exception {
		return cpBookAdminDAO.selectDeliveryCompExcelList(commandMap);
	}


	public String excelDownBookDelivery(Map<String, Object> commandMap, List<Object> fileList)
			throws Exception {
		
		String p_grcode = commandMap.get("p_grcode")+"";
		String p_subj = commandMap.get("p_subj")+"";
		String p_year = commandMap.get("p_year")+"";
		String p_subjseq = commandMap.get("p_subjseq")+"";
		String v_luserid = (String) commandMap.get("userid");
		
        HashMap fileh = (HashMap)fileList.get(0);
		commandMap.putAll(fileh);
		
		String  v_realFileName = commandMap.get("originalFileName")+"";
        String  v_newFileName  = commandMap.get("uploadFileName")+"";
      // String  v_newFileName  = "eb767ad1-b5c3-45c7-890c-23e11363b48d.xls";
        
        
        System.out.println("===================p_grcode       : "+p_grcode);
        System.out.println("===================p_subj         : "+p_subj);
        System.out.println("===================p_year         : "+p_year);
        System.out.println("===================p_subjseq      : "+p_subjseq);
        System.out.println("===================v_luserid      : "+v_luserid);
        System.out.println("===================v_realFileName : "+v_realFileName);
        System.out.println("===================v_newFileName  : "+v_newFileName);
		
		int i=0;
		int irows = 1;
		
		/**
   	    * 데이타 검증 관련 필드
   	    */
	    int is_inputok = 0;     // 에러 유무(정상 1, 공백 0)
	    int cnt_total = 0;      // 총 카운트
	    int cnt_succ = 0;       // 총 성공 카운트
	    int cnt_error = 0;      // 총 에러 카운트
	    String v_errnme="";     // 에러명
	    
	    //ID
	    int cnt_userid=0;
	    String str_userid="";
	    //이름
	    int cnt_name=0;
	    String str_name="";
	    //택배사코드
	    int cnt_delivery_comp=0;
	    String str_delivery_comp="";
	    //운송장번호
	    int cnt_delivery_number=0;
	    String str_delivery_number="";
	    
	    String str_result = "";
	    
	    Cell cell = null;
	    Sheet sheet = null;
	    Workbook workbook = null;
	    
	    String filePath = EgovProperties.getProperty("Globals.defaultDP")+"/bulletin/";
	    
	    System.out.println("@@@@@@@@@@@@@@@  filePath : "+filePath);
	    
	    try{
	    	
	    	workbook = Workbook.getWorkbook(new File(filePath+v_newFileName));
	    	sheet = workbook.getSheet(0);
	    	
	    	for(i=1; i<sheet.getRows(); i++){
	    		v_errnme = "";
	    		irows++;
	    		cnt_total++;
	    		int j=0;
	    		
	    		int v_idcnt=0;
	    		String v_userid= "";
	            String v_name="";
	            String v_delivery_comp="";
	            String v_delivery_number="";
	            
	            v_userid				= sheet.getCell(j,i).getContents();
	            v_name					= sheet.getCell(++j,i).getContents();
	            v_delivery_comp  		= sheet.getCell(++j,i).getContents();
	            v_delivery_number		= sheet.getCell(++j,i).getContents();
	            
	            System.out.println("v_userid          ::::["+i+ "]-["+j+"]"+v_userid);
	            System.out.println("v_name            ::::["+i+ "]-["+j+"]"+v_name);
	            System.out.println("v_delivery_comp   ::::["+i+ "]-["+j+"]"+v_delivery_comp);
	            System.out.println("v_delivery_number ::::["+i+ "]-["+j+"]"+v_delivery_number);
	            
	            //ID 확인 체크
            	if (v_userid.equals("")) { 
	        	    is_inputok = 2;
	        	    cnt_userid++;
	      	        str_userid += "["+irows+"로우의 ID를 입력해 주세요.]<br>";
	            }
            	
            	HashMap<String, Object> mm = new HashMap<String, Object>();
            	mm.put("v_subj", p_subj);
            	mm.put("v_subjseq", p_subjseq);
            	mm.put("v_year", p_year);
            	mm.put("v_userid", v_userid);
            	mm.put("v_luserid", v_luserid);
            	mm.put("v_name", v_name);
            	mm.put("v_delivery_comp", v_delivery_comp);
            	mm.put("v_delivery_number", v_delivery_number);
            	
            	Map memInfo = cpBookAdminDAO.selectDeliveryMemberInfo(mm);
            	
            	if(memInfo != null){
	            	v_userid = memInfo.get("userid")+"";
	            	mm.put("v_userid", v_userid);
	            	is_inputok = 1;
            	}
            	
            	 //이름 확인 체크 
	            if (v_name.equals("")) {
	        	    is_inputok = 2;
	        	    cnt_name++;
	      	        str_name += "["+irows+"로우의 이름을 입력해 주세요.]<br>";
	            }
	            //택배사 체크
            	if (v_delivery_comp.equals("")) { 
	        	    is_inputok = 2;
	        	    cnt_delivery_comp++;
	      	        str_delivery_comp += "["+irows+"로우의 택배사코드를 입력해 주세요.]<br>";
	            }
	            //운송장번호 확인 체크 
	            if (v_delivery_number.equals("")) {
	        	    is_inputok = 2;
	        	    cnt_delivery_number++;
	      	        str_delivery_number += "["+irows+"로우의 운송장번호를 입력해 주세요.]<br>";
	            }
	            
	            System.out.println("======================= is_inputok : "+is_inputok);
	            
	            if(is_inputok == 0) {
	            	cpBookAdminDAO.insertDeliveryMeber(mm);
	            	cnt_succ++;
	            }else if(is_inputok == 1) {
	            	cpBookAdminDAO.updateDeliveryMeber(mm);
	            	cnt_succ++;
	            }else{
	            	System.out.println("================================ fail ===================================");
	            	cnt_error++;
	            }
	            
	            str_result = "처리결과 <br><br>총입력 : "+cnt_total +"건  <br><br>성공: "+cnt_succ +"건 실패: "+cnt_error  +"건<br><br>";
	   	        str_result += "[오류 건수 상세 내용]<br><br>";
	   	        str_result += "1.ID 없음 :   "+cnt_userid+"건<br>"+str_userid+"<br><br>";       	    
	   	        str_result += "2.이름 없음 :  "+cnt_name+"건<br>"+str_name+"<br><br>";
	   	        str_result += "3.택배사코드 없음 :   "+cnt_delivery_comp+"건<br>"+str_delivery_comp+"<br><br>";
	   	     	str_result += "4.운송장번호 없음 :   "+cnt_delivery_number+"건<br>"+str_delivery_number+"<br><br>";
	   	     	
	   	     	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	   	     	System.out.println("str_result");
	   	     	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
	    	}
	    	
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
        
		return str_result;
	}


	public boolean deleteCpBook(Map<String, Object> commandMap)
			throws Exception {
		
		boolean isok = false;
		
		try{
			
			String []  v_checkvalue = (String []) commandMap.get("_Array_p_checks");
			
			if(v_checkvalue != null)
			{
				for ( int i = 0 ; i < v_checkvalue.length; i++ ) { 
					
					StringTokenizer st  = new StringTokenizer( v_checkvalue[i], ",");
					String v_userid		= st.nextToken();
					String v_subj		= st.nextToken();
					String v_year		= st.nextToken();
					String v_subjseq	= st.nextToken();
					
					System.out.println("@@@@@@ v_checkvalue = "+v_checkvalue[i]);
	                
	                HashMap<String, Object> mm = new HashMap<String, Object>();
                
                	mm.put("v_userid", v_userid);
                	mm.put("v_subj", v_subj);
                	mm.put("v_year", v_year);
                	mm.put("v_subjseq", v_subjseq);
                	
                	System.out.println("@@@@@@ v_userid   = "+v_userid);
                	System.out.println("@@@@@@ v_subj     = "+v_subj);
                	System.out.println("@@@@@@ v_year     = "+v_year);
                	System.out.println("@@@@@@ v_subjseq  = "+v_subjseq);
                	
                	//배송상태 변경 
                	int cnt = cpBookAdminDAO.deleteCpBook(mm);
                	
                	if(cnt == 0) 
                	{
                		return false;
                	}
	                
				}
				isok = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return isok;
	}

	
}
