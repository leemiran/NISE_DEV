package egovframework.com.log;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.ui.ModelMap;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestParam;

public class LogManageAspect {
	
	Logger log = Logger.getLogger(this.getClass());
 
	/**
   	* 시스템 로그정보를 생성한다. Around("execution(public * egovframework.adm.**..*Controller.*(..))")
   	* @param ProceedingJoinPoint
   	* @return Object
   	* @throws Exception  
   	*/
	
	public Object aroundController(ProceedingJoinPoint joinPoint) throws Throwable {
		
	    StopWatch stopWatch = new StopWatch();
	    
	    Object outObj  = null;
	    Object[] inObj = joinPoint.getArgs();
	    
		ModelMap model = new ModelMap();
		
		String[] joinPointMethodArr = null;
		
	    try {
	    	stopWatch.start();
	    	
	    	Object retValue = joinPoint.proceed();
	    	inObj = joinPoint.getArgs();
	    	
		    joinPointMethodArr = this.getJoinPointMethod(joinPoint);
		    
		    //log.info("-----------------------------------------  REQUEST VALUE LOG START ------------------------------------");
		    
		    for(int i=0; i<inObj.length; i++){
	    		
		    	Iterator iterator = null;
		    	
		    	if(inObj[i] instanceof Map && !(inObj[i] instanceof ModelMap)){
		    		Set set = ((HashMap) inObj[i]).keySet();
		    		iterator = set.iterator();
		    		
		    		while(iterator.hasNext()) {
						String key = String.valueOf(iterator.next());
						Object value = ((HashMap) inObj[i]).get(key);
						//log.info("|  " + key + " : " + value);
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
				    				//log.info("| @" + annotations[x][y].annotationType().getSimpleName() + "(value=" + param.value() +  ", required=" +param.required()+ ") : " + inObj[i]);
			    				}
			    			}
			    		}
		    		}
		    	}
		    }
		    
		    //log.info("------------------------------------------ REQUEST VALUE LOG END --------------------------------------\n");
	    
	    	outObj = retValue;
	    	
	    	return outObj;
	    	
	    } catch (Throwable e) {
	    	throw e;
	    } finally {
	    	
	    	if( log.isInfoEnabled()) {
		    	//log.info("--------------------------------------- EXECUTE INFORMATION -------------------------------------------");
		    	////log.info("| URL INFORMATION 		: 	" + request.getRequestURL());
		    	//log.info("| CONTROLLER INFORMATION 	: 	" + joinPoint.getTarget().getClass().getName());
		    	//log.info("| METHOD INFORMATION 		: 	" + joinPoint.getSignature().getName());
		    	
		    	if(joinPointMethodArr != null){
			    	for(String str: joinPointMethodArr) {
			    		//log.info("| PARAMETER INFORMATION 	: 	" + str);
			    	}
		    	}
		    	
		    	//log.info("-------------------------------------------------------------------------------------------------------\n");
		    	
				stopWatch.stop();
		    	//log.info("-----------------------------------------  CONTROLLER PROCESS TIME ------------------------------------");
		    	//log.info("| TOTAL TIME   : " + stopWatch.getTotalTimeSeconds() + " SECOND");
		    	this.checkMemory();
		    	//log.info("-------------------------------------------------------------------------------------------------------\n");
	    	}
	    }
	}
	
	public void checkMemory() {
		if( log.isInfoEnabled()) {
			Runtime rt = Runtime.getRuntime();
			//log.info("| TOTAL MEMORY : " + rt.totalMemory());
			//log.info("| FREE SIZE    : " + rt.freeMemory());
			long total = rt.totalMemory();
			long free = rt.freeMemory();
			long heap = total - free;
			long sum = (heap * 100) / total;
			//log.info("| HEAP SIZE    : " + heap + " (" + sum + "%)" );
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