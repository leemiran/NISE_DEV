package egovframework.com.inc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

public class SimpleUrlAnnotationHandlerMapping extends DefaultAnnotationHandlerMapping {
 
	//url리스트, 중복값을 허용하지 않음으로 Set 객체에 담는다.
	private Set<String> urls;
 
	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}
 
	/**
	 * @RequestMapping로 선언된 url중에 프로퍼티 urls에 정의된 url만 remapping해 return
	 * url mapping시에는 PathMatcher를 사용하는데, 별도로 등록한 PathMatcher가 없다면 AntPathMatcher를 사용한다.
	 * @param urlsArray - @RequestMapping로 선언된 url list
	 * @return urlsArray중에 설정된 url을 필터링해서 return.
	 */
	private String[] remappingUrls(String[] urlsArray){
 
		if(urlsArray==null){
			return null;
		}
 
		ArrayList<String> remappedUrls = new ArrayList<String>();
 
		for(Iterator<String> it = this.urls.iterator(); it.hasNext();){
			String urlPattern = (String) it.next();
			for(int i=0;i<urlsArray.length;i++){
				if(getPathMatcher().matchStart(urlPattern, urlsArray[i])){					
					remappedUrls.add(urlsArray[i]);
				}
			}
		}
 
		return (String[]) remappedUrls.toArray(new String[remappedUrls.size()]);		
	}
 
	/**
	 * @RequestMapping로 선언된 url을 필터링하기 위해
	 * org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping의 
	 * 메소드 protected String[] determineUrlsForHandler(String beanName)를 override.
	 * 
	 * @param beanName - the name of the candidate bean
	 * @return 빈에 해당하는 URL list
	 */
	protected String[] determineUrlsForHandler(String beanName) {
		return remappingUrls(super.determineUrlsForHandler(beanName));
	}	
}