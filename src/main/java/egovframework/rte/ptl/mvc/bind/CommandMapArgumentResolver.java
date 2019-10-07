package egovframework.rte.ptl.mvc.bind;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mortbay.log.Log;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

public class CommandMapArgumentResolver implements WebArgumentResolver {
    public Object resolveArgument(MethodParameter methodParameter, NativeWebRequest webRequest) throws Exception {
        Class clazz = methodParameter.getParameterType();
        String paramName = methodParameter.getParameterName();

        // 기본조회 파라미터

//        String S_Seq_Crum = "";
//        String S_ShowDetail = "";
//        
//        String selectSeqCurm = "";

        if ((clazz.equals(Map.class)) && (paramName.equals("commandMap"))) {

            Map commandMap = new HashMap();

            HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
            Enumeration enumeration = request.getParameterNames();

            // 맵으로날라오는 세션 값을 사칭한 아이디 삭제
//          commandMap.put("suserId", null);
            HttpSession session = request.getSession(true);
            Enumeration senumeration = session.getAttributeNames();

            //관리자 페이지 메뉴 클릭시 옵션설정 Y : session검색 정보 삭제, N : continue
           String admMenuInitOption = "N";
           
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String[] values = request.getParameterValues(key);
//
//                if (key.equals("S_Seq_Crum")) {
//                    S_Seq_Crum = (String) ((values.length > 1) ? values : values[0]);
//                    continue;
//                }
//                
//                if (key.equals("selectSeqCurm")) {
//                	selectSeqCurm = (String) ((values.length > 1) ? values : values[0]);
//                    continue;
//                }
//
//                if (key.equals("S_ShowDetail")) {
//                    S_ShowDetail = (String) ((values.length > 1) ? values : values[0]);
//                    continue;
//                }
                
                
                if (values != null) {
                    // 배열로 초기화하는 폼값들은 모두 배열로 받는다. _Array시작하면 instance는 배열로 저장된다.
                    if (key.startsWith("_Array")) {
                        commandMap.put(key, (String[]) values);
                        //Log.info("request commandMap \"_Array\" values length : " + values.length);
                    } else {
                    	if(key.startsWith("ses_search_"))
                    	{
                    		session.removeAttribute(key);
                    		session.setAttribute(key, (values.length > 1) ? values : values[0]);
                    	}
                    	
                    	commandMap.remove(key);
                		commandMap.put(key, (values.length > 1) ? values : values[0]);
                		
                        //Log.info("## request : " + key + " / " + ((values.length > 1) ? values : values[0]));
                    }
                }
            }
            
          //관리자 페이지 메뉴 클릭시 옵션설정 Y : session검색 정보 삭제, N : continue
            if(commandMap.get("admMenuInitOption") != null && !"".equals(commandMap.get("admMenuInitOption")))		admMenuInitOption = commandMap.get("admMenuInitOption") + "";

            while (senumeration.hasMoreElements()) {
                String skey = (String) senumeration.nextElement();

                //검색박스는 세션에 등록하지 않는다. 검색 controller에서 세션에 저장한다.
//                if(skey.indexOf("ses_search_", 0) > -1)
//                	continue;
//                else
                if(admMenuInitOption.equals("Y") && skey.startsWith("ses_search_"))
            	{
            		session.removeAttribute(skey);
            	}
                else
                {
	                commandMap.remove(skey);
	            	commandMap.put(skey, session.getAttribute(skey));
                }
            }
            
/*
            String S_DateType = (String) commandMap.get("S_DateType");
            String S_SDate = (String) commandMap.get("S_SDate");
            String S_EDate = (String) commandMap.get("S_EDate");
            
            
            if (S_DateType == null || S_DateType.equals("")) {
                S_DateType = "3";
                
                Calendar c_day = Calendar.getInstance();
                int Y, M, D = 0;

                Y = c_day.get(Calendar.YEAR);
                M = c_day.get(Calendar.MONTH) + 3;
                D = c_day.get(Calendar.DATE);

                if (S_EDate == null || S_EDate.equals("")) {
                 S_EDate = Y + "-" + ((M < 10) ? "0" + M : M) + "-" + ((D < 10) ? "0" + D : D);
                }

                c_day.add(c_day.MONTH, -3);

                Y = c_day.get(Calendar.YEAR);
                M = c_day.get(Calendar.MONTH) + 1;
                D = c_day.get(Calendar.DATE);

                if (S_SDate == null || S_SDate.equals("")) {
                 S_SDate = Y + "-" + ((M < 10) ? "0" + M : M) + "-" + ((D < 10) ? "0" + D : D);
                }
                
            }
            
            
            String selectSDate = (String) commandMap.get("selectSDate");
            String selectEDate = (String) commandMap.get("selectEDate");
            
            Calendar c = Calendar.getInstance();
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    		c.setTime(new Date(System.currentTimeMillis()));
//    		String toDay = sdf.format(c.getTime());
    		
    		
            if(selectSDate == null || selectSDate.equals("") ){
        		c.add(Calendar.MONTH, -1);
        		Date minusOneMonth = c.getTime();
        		selectSDate = sdf.format(minusOneMonth);
        		
        	}
        	
        	if(selectEDate == null || selectEDate.equals("")){
        		c.add(Calendar.MONTH, 2);
        		Date plusOneMonth = c.getTime();
        		selectEDate = sdf.format(plusOneMonth);
        	}
        	
            System.out.println("S_DateType="+S_DateType);
            System.out.println("S_SDate="+S_SDate);
            System.out.println("S_EDate="+S_EDate);            
            
            commandMap.remove("S_DateType");
            commandMap.remove("S_SDate");
            commandMap.remove("S_EDate");
            
            
            commandMap.put("S_DateType", S_DateType);
            commandMap.put("S_SDate", S_SDate);
            commandMap.put("S_EDate", S_EDate);
            

            
            commandMap.remove("selectSDate");
            commandMap.remove("selectEDate");
            
            commandMap.put("selectSDate", selectSDate);
            commandMap.put("selectEDate", selectEDate);
            
            
            session.removeAttribute("S_Seq_Crum");
            session.setAttribute("S_Seq_Crum", S_Seq_Crum);
            
            session.removeAttribute("selectSeqCurm");
            session.setAttribute("selectSeqCurm", selectSeqCurm);

            session.removeAttribute("S_ShowDetail");
            session.setAttribute("S_ShowDetail", S_ShowDetail);

            commandMap.put("selectSeqCurm", selectSeqCurm);
            commandMap.put("S_Seq_Crum", S_Seq_Crum);
            commandMap.put("S_ShowDetail", S_ShowDetail);
*/
            
            
            
            return commandMap;
        }
        return UNRESOLVED;
    }
}