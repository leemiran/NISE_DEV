package egovframework.com.log;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.log.service.LogManageAspectLogService;

@SuppressWarnings("unchecked")
public class LogManageAspectLog {
	
	Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * egovOnlinePollService
	 * @uml.property  name="logManageAspectLogService"
	 * @uml.associationEnd  readOnly="true"
	 */
    @Resource(name = "logManageAspectLogService")
    private LogManageAspectLogService logManageAspectLogService;
    
	/**
   	* 시스템 로그정보를 생성한다. Around("execution(public * egovframework.adm.**..*Controller.*(..))")
   	* @param ProceedingJoinPoint
   	* @return Object
   	* @throws Exception  
   	*/
	@SuppressWarnings("finally")
	
	public Object aroundInsertController(ProceedingJoinPoint joinPoint) throws Throwable {
		Object retValue = joinPoint.proceed();		
		String actionType = "I";
		actionLog(joinPoint, actionType);
	    return retValue;
	}
	
	public Object aroundUpdateController(ProceedingJoinPoint joinPoint) throws Throwable {
		Object retValue = joinPoint.proceed();
		String actionType = "U";
		actionLog(joinPoint, actionType);
	    return retValue;
	}
	
	public Object aroundDeleteController(ProceedingJoinPoint joinPoint) throws Throwable {
		Object retValue = joinPoint.proceed();
		String actionType = "D";
		actionLog(joinPoint, actionType);
	    return retValue;
	}
	
	public void actionLog(ProceedingJoinPoint joinPoint, String actionType) throws Throwable {
		
		
		StopWatch stopWatch = new StopWatch();
	    /*//log.info("actionType ===========> "+ actionType); */	   
		Object outObj  = null;
	    Object[] inObj = joinPoint.getArgs();
	    
		ModelMap model = new ModelMap();
		
		String[] joinPointMethodArr = null;
		
	    try {
	    	stopWatch.start();
	    	
	    	
	    	inObj = joinPoint.getArgs();
	    	
		    joinPointMethodArr = this.getJoinPointMethod(joinPoint);
			
		    /*//log.info("----------------------------------------- 22222222222 REQUEST VALUE LOG START ------------------------------------");*/		    
		    StringBuffer contents = new StringBuffer();
		    
		    for(int i=0; i<inObj.length; i++){
	    		
		    	Iterator iterator = null;
		    	
		    	if(inObj[i] instanceof Map && !(inObj[i] instanceof ModelMap)){
		    		Set set = ((HashMap) inObj[i]).keySet();
		    		iterator = set.iterator();
		    		
		    		while(iterator.hasNext()) {
						String key = String.valueOf(iterator.next());
						Object value = ((HashMap) inObj[i]).get(key);
						if(!"sidNo".equals(key)){
							contents.append("|  " + key + " : " + value+"\n");
						}
						/*//log.info("|  " + key + " : " + value);*/		    		
					}
		    	}
		    	
		    	else if( !(inObj[i] instanceof Map) && !(inObj[i] instanceof ModelMap) ){
		    		
		    		boolean _hook = true;
		    		
		    		if(inObj[i] != null){
		    			_hook = inObj[i].getClass().getSimpleName().indexOf("VO") == -1 ? true: false;
		    		}
		    		
		    		if(_hook){
			    		Annotation[][] annotations = ((MethodSignature) joinPoint.getSignature()). getMethod() .getParameterAnnotations();
			    		
			    		for(int x=0; x<annotations.length; x++){
			    			for(int y=0; y<annotations[x].length; y++){
			    				if(annotations[x][y].annotationType() == RequestParam.class){
			    					RequestParam param = (RequestParam) annotations[x][y];
			    					contents.append("| @" + annotations[x][y].annotationType().getSimpleName() + "(value=" + param.value() +  ", required=" +param.required()+ ") : " + inObj[i]+"\n");
			    					/*//log.info("| @" + annotations[x][y].annotationType().getSimpleName() + "(value=" + param.value() +  ", required=" +param.required()+ ") : " + inObj[i]);*/			    				}
			    			}
			    		}
		    		}
		    	}
		    }
		    
		    /*//log.info("------------------------------------------ 22222222222 REQUEST VALUE LOG END --------------------------------------\n");*/	    
	    		    	
	    	/****************** 로그저장 start **********************/
		   
	    	Map request = null;
			
			for (int i = 0; i < inObj.length; i++) {
				
				Iterator iterator = null;
				
				if (inObj[i] instanceof Map && !(inObj[i] instanceof ModelMap)) {
					request = (Map) inObj[i];
				}
			}
			String gadmin ="ZZ";
			Map inputMap = new HashMap();
			if (request != null && request.get("userid") != null) {
				/*////log.info("REQUEST suserId  ==========> "+ request.get("suserId"));
				////log.info("REQUEST saspCode  ==========> "+ request.get("saspCode"));
				//log.info("REQUEST gadmin  ==========> "+ request.get("gadmin"));*/				
				gadmin = request.get("gadmin") !=null ? request.get("gadmin").toString() : "ZZ";
				//inputMap.put("aspCode", "1001");
				inputMap.put("userid", request.get("userid"));
			}
			/*////log.info("REQUEST contents  ==========> "+ contents);*/			
			
			inputMap.put("actionType", actionType);
			inputMap.put("contents", contents.toString());
			inputMap.put("controllerInfo", joinPoint.getTarget().getClass().getName());
			inputMap.put("methodInfo", joinPoint.getSignature().getName());			
			if(!"ZZ".equals(gadmin)){
				logManageAspectLogService.manageAspectLogActionLog(inputMap);
			}
			/****************** 로그저장 end **********************/
	    	
	    	
	    	
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	    	
	    	if( log.isInfoEnabled()) {
		    	/*//log.info("--------------------------------------- 22222222222 EXECUTE INFORMATION -------------------------------------------");
		    	////log.info("| URL INFORMATION 		: 	" + request.getRequestURL());
		    	//log.info("| CONTROLLER INFORMATION 	: 	" + joinPoint.getTarget().getClass().getName());
		    	//log.info("| METHOD INFORMATION 		: 	" + joinPoint.getSignature().getName());*/		    	
		    	if(joinPointMethodArr != null){
			    	for(String str: joinPointMethodArr) {
			    		//log.info("| PARAMETER INFORMATION 	: 	" + str);
			    	}
		    	}
		    	
		    	/*//log.info("-------------------------------------------------------------------------------------------------------\n");*/		    	
				stopWatch.stop();
		    	/*//log.info("-----------------------------------------  22222222222 CONTROLLER PROCESS TIME ------------------------------------");
		    	//log.info("| TOTAL TIME   : " + stopWatch.getTotalTimeSeconds() + " SECOND");*/		    	
				this.checkMemory();
		    	/*//log.info("-------------------------------------------------------------------------------------------------------\n");*/	    	
			}
	    }
	}
	
	public void checkMemory() {
		if( log.isInfoEnabled()) {
			Runtime rt = Runtime.getRuntime();
			/*//log.info("| TOTAL MEMORY : " + rt.totalMemory());
			//log.info("| FREE SIZE    : " + rt.freeMemory());*/			
			long total = rt.totalMemory();
			long free = rt.freeMemory();
			long heap = total - free;
			long sum = (heap * 100) / total;
			/*//log.info("| HEAP SIZE    : " + heap + " (" + sum + "%)" );*/		
		}
	}
	
	public String[] getJoinPointMethod(ProceedingJoinPoint joinPoint){
		
		if (!ProceedingJoinPoint.METHOD_EXECUTION.equals(joinPoint.getKind())) {
			return null;
		}
		
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		
		String strMethod = methodSignature.getMethod().toString();
		String strParam  = "";
		
		if(strMethod.indexOf("(") != -1 && strMethod.indexOf(")") != -1){
			strParam = strMethod.substring(strMethod.indexOf("(")+1, strMethod.indexOf(")"));
		}
		
		String[] strParamArr = strParam.split(",");
		
		return strParamArr;
		
	}
}