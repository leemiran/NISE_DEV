/* 
 * LcmsCourseOrgService.java		1.00	2011-09-05 
 *
 * Copyright (c) 2011 ???? Co. All Rights Reserved.
 * 
 * This software is the confidential and proprietary information
 * of um2m.  You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement
 * you entered into with um2m.
 */
package egovframework.adm.lcms.cts.service;

import java.util.List;
import java.util.Map;


/**
 * <pre>
 * system      : 
 * menu        : 
 * source      : LcmsMetadataService.java
 * description : 
 * </pre> 
 * @version
 * <pre> 
 * 1.0	2011-09-05 created by ?
 * 1.1	
 * </pre>
 */


public interface LcmsMetadataService {
	
	
	public List selectLcmsMetadataElementList(Map<String, Object> commandMap) throws Exception;
	
	public int updateLcmsMetadataElement(Map<String, Object> commmandMap) throws Exception;
}
