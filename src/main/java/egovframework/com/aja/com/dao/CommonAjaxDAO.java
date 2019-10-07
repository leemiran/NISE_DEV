package egovframework.com.aja.com.dao;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.com.cmm.ComDefaultVO;
/**
 * 공통 Ajax 정의
 * @author 개발환경 개발팀 김기종
 * @since 2011.01.25
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2011.01.25  김기종          최초 생성
 *
 * </pre>
 */

@Repository("commonAjaxDAO")
public class CommonAjaxDAO extends EgovAbstractDAO{
    
	/**
	 * 메뉴구조 콤보 조회  (Main)
	 * @param vo MenuManageVO
	 * @return List
	 * @exception Exception
	 */
	public List selectMainMenuCombo(Map<String, Object> inputMap) throws Exception{
		return list("commonAjaxDAO.selectMainMenuCombo", inputMap);
	}
	
	/**
	 * 메뉴구조 콤보 조회  (Detail)
	 * @param vo MenuManageVO
	 * @return List
	 * @exception Exception
	 */
	public List selectDetailMenuCombo(Map<String, Object> inputMap) throws Exception{
		return list("commonAjaxDAO.selectDetailMenuCombo", inputMap);
	}
	
	
	/**
	 * RD print log insert
	 * @param vo MenuManageVO
	 * @return List
	 * @exception Exception
	 */
	public void insertPrintLog(Map<String, Object> inputMap) throws Exception{
		insert("commonAjaxDAO.insertPrintLog", inputMap);
	}
	
    
    
    
	 	
}