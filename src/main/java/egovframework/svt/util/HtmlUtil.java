package egovframework.svt.util;

import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

@Service
public class HtmlUtil {

	/**
	 * 
	 * @param commandMap
	 * @param escapeKey ("key1,key2,key3,...")
	 */
	public void escapeMap (Map<String, Object> commandMap, String escapeKey) {
		String[] splitEscapeKey = escapeKey.split(",");
		for(String key: splitEscapeKey) {
			if(null != commandMap.get(key))
				commandMap.put(key, HtmlUtils.htmlEscape(String.valueOf(commandMap.get(key))));
		}
	}
}
